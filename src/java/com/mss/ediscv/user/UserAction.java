/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.user;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.AuthorizationManager;
import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.ServiceLocator;
import com.mss.ediscv.util.ServiceLocatorException;
import com.opensymphony.xwork2.ActionSupport;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.ServletResponseAware;

/**
 * @author miracle1
 */
public class UserAction extends ActionSupport implements ServletRequestAware, ServletResponseAware, ParameterAware {

    private HttpServletRequest httpServletRequest;
    private static Logger logger = Logger.getLogger(UserAction.class.getName());
    private String resultType;
    private String fname;
    private String lname;
    private String email;
    private String ophno;
    private String createdBy;
    private String active;
    private String deptId;
    private String status;
    private String role;
    private String loginId;
    private String confirmPwd;
    private String newPwd;
    private String oldPwd;
    private String currentAction;
    private String userPageId;
    private String id;
    String resultMessage;
    ServiceLocator serviceLocator;
    private List userList;
    private int userRoleId;
    private UserBean userBean;
    private int userId;
    private String userName;
    private Map assignedFlowsMap;
    private Map notAssignedFlowsMap;
    private Map primaryFlowsList;
    private String primaryFlow;
    private Map parameters;
    private List addedFlowsList;
    private Map UserRolesMap;
    private String organization;
    private String designation;
    private String location;
    private Date dob;
    private String phonenumber;
    private String education;
    private String gender;
    private File imagePath = null;
    private File imageUpdate;
    private String imageUpdateFileName;
    private HttpServletResponse httpServletResponse;
    private boolean logistics;
    private boolean manufacturing;
    private boolean docvisibility;

    public String prepare() throws Exception {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            HttpSession httpSession = httpServletRequest.getSession(false);
            try {
                httpSession.removeAttribute(AppConstants.SES_PAYMENT_LIST);
                httpSession.removeAttribute(AppConstants.SES_SHIPMENT_LIST);
                httpSession.removeAttribute(AppConstants.SES_DOC_LIST);
                httpSession.removeAttribute(AppConstants.SES_INV_LIST);
                httpSession.removeAttribute(AppConstants.SES_PO_LIST);
                httpSession.removeAttribute(AppConstants.SES_USER_LIST);
                setUserRolesMap(DataSourceDataProvider.getInstance().getMsscvpRoles());
                setCurrentAction("../user/createUser.action");
                setUserPageId("0");
                setResultType(SUCCESS);
            } catch (Exception ex) {
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, ex.getMessage());
                setResultType("error");
            }
        }
        return getResultType();
    }

    public String resetMyPassword() throws Exception {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            prepare();
        }
        return getResultType();
    }

    public String resetUserPassword() throws Exception {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            setResultType("accessFailed");
            int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
            if (AuthorizationManager.getInstance().isAuthorizedUser("USER_EDIT", userRoleId)) {
                prepare();
            }
        }
        return getResultType();
    }

    public String userSearch() throws Exception {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            setResultType("accessFailed");
            int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
            if (AuthorizationManager.getInstance().isAuthorizedUser("USER_EDIT", userRoleId)) {
                prepare();
            }
        }
        return getResultType();
    }

    public String doCreateUser() throws Exception {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            setResultType("accessFailed");
            int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
            if (AuthorizationManager.getInstance().isAuthorizedUser("USER_EDIT", userRoleId)) {
                prepare();
            }
        }
        return getResultType();
    }

    public String createUser() throws Exception {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            resultType = "accessFailed";
            int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
            if (AuthorizationManager.getInstance().isAuthorizedUser("USER_EDIT", userRoleId)) {
                boolean isUserExist = false;
                isUserExist = ServiceLocator.getUserService().userCheckExist(this);
                if (isUserExist) {
                    resultMessage = "<font color=\"red\" size=\"3\">Oops! This User registered already In our System!</font>";
                    setResultType(INPUT);
                } else {
                    setCreatedBy(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString());
                    int i = 0;
                    resultMessage = ServiceLocator.getUserService().addUser(this);
                    resetValues();
                    setResultType(SUCCESS);
                }
                setUserRolesMap(DataSourceDataProvider.getInstance().getMsscvpRoles());
            }
        }
        setCurrentAction("../user/createUser.action");
        setUserPageId("0");
        httpServletRequest.setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
        return getResultType();
    }

    public void resetValues() {
        setActive("-1");
        setDeptId("-1");
        setEmail("");
        setFname("");
        setLname("");
        setOphno("");
        setRole("-1");
        setStatus("-1");
        setConfirmPwd("");
        setLoginId("");
        setNewPwd("");
    }

    public String updateUserPwd() throws Exception {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            resultType = "accessFailed";
            int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
            if (AuthorizationManager.getInstance().isAuthorizedUser("USER_EDIT", userRoleId)) {
                try {
                    if ((getNewPwd().length()) < 3) {
                        if (!(getNewPwd().equals("")) && !(getConfirmPwd().equals(""))) {
                            int updatedRows = ServiceLocator.getUserService().updateUserPwd(this);
                            if (updatedRows == 1) {
                                resetValues();
                                resultMessage = "<font color=\"green\" size=\"3\">You have changed User password succesfully </font>";
                                resultType = SUCCESS;
                            } else {
                                resultMessage = "<font color=\"red\" size=\"3\">Sorry!Please enter valid password! Or Your are not authorized person to change the above person password!</font>";
                                resultType = INPUT;
                            }
                        } else {
                            resultMessage = "<font color=\"red\" size=\"3\">Sorry!Please enter password!</font> ";
                        }
                    } else {
                        resultMessage = "<font color=\"red\" size=\"3\">New Password must be grater than or equal to 3 characters!</font> ";
                    }
                    httpServletRequest.setAttribute("resultMessage", resultMessage);
                    resultType = SUCCESS;
                } catch (Exception ex) {
                    httpServletRequest.getSession(false).setAttribute("errorMessage", ex.toString());
                    resultType = ERROR;
                }
            }
        }
        return resultType;
    }

    public String updateMyPwd() throws Exception {
        try {
            resultType = LOGIN;
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
                try {
                    setLoginId(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString());
                     if (!"".equals(getOldPwd())) {
                          if ((!"".equals(getNewPwd())) && (!"".equals(getConfirmPwd()))) {
                         if ((getNewPwd().length()) > 2) {
                        if (getNewPwd().equals(getConfirmPwd())) {
                            int updatedRows = ServiceLocator.getUserService().updateMyPwd(this);
                            if (updatedRows == 1) {
                                resultMessage = "<font color=\"green\" size=\"3\">Congrats! You have changed your password succesfully </font>";
                            } else if (updatedRows == 100) {
                                resultMessage = "<font color=\"red\" size=\"3\">Please enter correct Old Password!</font>";
                            }else {
                                resultMessage = "<font color=\"red\" size=\"3\">Sorry! We are not able to change your password. Please try again later! </font>";
                            } 
                        } else {
                            resultMessage = "<font color=\"red\" size=\"3\">Passwords do not match!</font>";
                        }
                    } else {
                        resultMessage = "<font color=\"red\" size=\"3\">New Password must be grater than or equal to 3 characters!</font> ";
                    }
                     }else {
                        resultMessage = "<font color=\"red\" size=\"3\">Please enter both New Password and Confirm Password!</font> ";
                    }
                }else {
                        resultMessage = "<font color=\"red\" size=\"3\">Please enter Old Password!</font> ";
                    }

                    getUserProfile();
                    httpServletRequest.setAttribute("resultMessage", resultMessage);
                    resultType = SUCCESS;
                } catch (Exception ex) {
                    httpServletRequest.getSession(false).setAttribute("errorMessage", ex.toString());
                    resultType = ERROR;
                }
            }
        } catch (Exception ex) {
            httpServletRequest.getSession(false).setAttribute("errorMessage", ex.toString());
            resultType = ERROR;
        }
        return resultType;
    }

    public String getUserSearchList() throws Exception {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            resultType = "accessFailed";
            int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
            if (AuthorizationManager.getInstance().isAuthorizedUser("USER_EDIT", userRoleId)) {
                try {
                    prepare();
                    setUserList(ServiceLocator.getUserService().getSearchUserList(this));
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SES_USER_LIST, getUserList());
                    resultType = SUCCESS;
                } catch (Exception ex) {
                    httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, ex.getMessage());
                    resultType = "error";
                }
            }
        }
        return resultType;
    }

    public String userEdit() throws Exception {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            resultType = "accessFailed";
            int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
            if (AuthorizationManager.getInstance().isAuthorizedUser("USER_EDIT", userRoleId)) {
                try {
                    String responseString = "";
                    ServiceLocator.getUserService().editUser(this);
                    httpServletRequest.setAttribute(AppConstants.REQ_RESULT_MSG, responseString);
                    setUserRolesMap(DataSourceDataProvider.getInstance().getMsscvpRoles());
                    setCurrentAction("../user/doUpdateUser.action");
                    setUserPageId("1");
                    resultType = SUCCESS;
                } catch (Exception ex) {
                    httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, ex.getMessage());
                    resultType = "error";
                }
            }
        }
        return resultType;
    }

    public String backToSearchList() throws Exception {
        getUserSearchList();
        return SUCCESS;
    }

    public String doUpdateUser() throws Exception {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            resultType = "accessFailed";
            int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
            if (AuthorizationManager.getInstance().isAuthorizedUser("USER_EDIT", userRoleId)) {
                try {
                    prepare();
                    String responseString = "";
                    setCreatedBy(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString());
                    responseString = ServiceLocator.getUserService().updateUser(this);
                    httpServletRequest.setAttribute(AppConstants.REQ_RESULT_MSG, responseString);
                    setCurrentAction("../user/doUpdateUser.action");
                    setUserPageId("1");
                    resultType = SUCCESS;
                } catch (Exception ex) {
                    httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, ex.getMessage());
                    resultType = "error";
                }
            }
        }
        return resultType;
    }

    public String getAssingnedFlows() {
        resultType = LOGIN;
        DataSourceDataProvider dataSourceDataProvider = null;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            resultType = "accessFailed";
            int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
            if (AuthorizationManager.getInstance().isAuthorizedUser("USER_EDIT", userRoleId)) {
                setUserRoleId(Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString()));
                resultType = "accessFailed";
                try {
                    dataSourceDataProvider = DataSourceDataProvider.getInstance();
                    UserBean ub = ServiceLocator.getUserService().userDetails(getUserId());
                    setUserBean(ub);
                    setAssignedFlowsMap(dataSourceDataProvider.getAssignedFlows(getUserId()));

                    Iterator<Map.Entry<String, String>> iterator = assignedFlowsMap.entrySet().iterator();
                    while (iterator.hasNext()) {
                        Map.Entry<String, String> entry = iterator.next();
                        if (entry.getValue().equals("Logistics")) {
                            setLogistics(true);
                        }

                        if (entry.getValue().equals("Manufacturing")) {
                            setManufacturing(true);
                        }
                        if (entry.getValue().equals("DocumentVisibility")) {
                            setDocvisibility(true);
                        }
                    }
                    setNotAssignedFlowsMap(dataSourceDataProvider.getNotAssignedFlows(getUserId()));
                    setPrimaryFlowsList(dataSourceDataProvider.getFlowbyflowKey(AppConstants.FLOWS_OPTIONS));
                    resultType = SUCCESS;
                } catch (Exception ex) {
                    httpServletRequest.getSession(false).setAttribute("errorMessage", ex.toString());
                    ex.printStackTrace();
                    resultType = ERROR;
                }
            }
        }
        return resultType;
    }

    public String getTransferFlow() throws Exception {
        List addedFlowsList;
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            resultType = "accessFailed";
            int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
            if (AuthorizationManager.getInstance().isAuthorizedUser("USER_EDIT", userRoleId)) {
                userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
                resultType = "accessFailed";
                try {
                    int count = 0;
                    addedFlowsList = new ArrayList();
                    int[] rightParams = new int[3];
                    int i = 0;

                    if (isLogistics()) {
                        rightParams[count] = 3;
                        count++;
                    }
                    if (isManufacturing()) {
                        rightParams[count] = 2;
                        count++;
                    }
                    if (isDocvisibility()) {
                        rightParams[count] = 5;
                    }
                    String[] leftParams = (String[]) parameters.get("leftSideUserFlows");
                    int insertedRows = ServiceLocator.getUserService().insertFlows(rightParams, this.getUserId(), Integer.parseInt(getPrimaryFlow()));
                    System.out.println("insertedRows " + insertedRows);
                    resultType = SUCCESS;
                    resultMessage = "<font color=\"green\" size=\"3\">Flows has been successfully Added!</font>";
                    httpServletRequest.setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);

                } catch (Exception ex) {
                    httpServletRequest.getSession(false).setAttribute("errorMessage", ex.toString());
                    resultType = ERROR;
                }
            }
        }
        return resultType;
    }

    public String getUserProfile() throws Exception {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            resultType = "accessFailed";
            int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
            String userId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_ID).toString();
            String loginId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString();
            try {
                setUserBean(ServiceLocator.getUserService().userProfile(userId, loginId));
                resultType = SUCCESS;
            } catch (Exception ex) {
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, ex.getMessage());
                resultType = "error";
            }
        }
        return resultType;
    }

    public String updateProfile() {
        resultType = LOGIN;
        int updatedRows;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            userId = Integer.parseInt(httpServletRequest.getSession().getAttribute(AppConstants.SES_USER_ID).toString());
            userRoleId = Integer.parseInt(httpServletRequest.getSession().getAttribute(AppConstants.SES_ROLE_ID).toString());
            resultType = "accessFailed";
            try {
                updatedRows = ServiceLocator.getUserService().updateUserProfile(this, userId);
                if (updatedRows == 1) {
                    resultMessage = "<font color=\"green\" size=\"3\">Profile Updated Successfully!</font>";
                } else {
                    resultMessage = "<font color=\"red\" size=\"3\">Please Try again!</font>";
                }
                getUserProfile();
                httpServletRequest.setAttribute(AppConstants.RESULT_MSG, resultMessage);
                resultType = SUCCESS;
            } catch (Exception ex) {
                httpServletRequest.getSession(false).setAttribute("errorMessage", ex.toString());
                resultType = ERROR;
            }
        }
        return resultType;
    }

    public String uploadImage() throws IOException, IllegalStateException, ServletException, ServiceLocatorException {
        resultType = LOGIN;
        int result = 0;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            userId = Integer.parseInt(httpServletRequest.getSession().getAttribute(AppConstants.SES_USER_ID).toString());
            userRoleId = Integer.parseInt(httpServletRequest.getSession().getAttribute(AppConstants.SES_ROLE_ID).toString());
            resultType = "accessFailed";
            try {
                result = ServiceLocator.getUserService().uploadImage(imageUpdate, userId);
                if (result == 1) {
                    resultMessage = "<font color=\"green\" size=\"3\">Image Updated Successfully!</font>";
                } else {
                    resultMessage = "<font color=\"red\" size=\"3\">Please Try again!</font>";
                }
                getUserProfile();
                httpServletRequest.setAttribute(AppConstants.RESULT_MSG, resultMessage);
                resultType = SUCCESS;
            } catch (Exception ex) {
                httpServletRequest.getSession(false).setAttribute("errorMessage", ex.toString());
                resultType = ERROR;
            }
        }
        return resultType;
    }

    /*    public void renderImage() {
     byte[] image = null;
     try {
     byte[] imgData = (byte[]) httpServletRequest.getSession(false).getAttribute(AppConstants.SESSION_USER_IMAGE);
     // display the image
     httpServletResponse.setContentType("image/gif");
     OutputStream o = httpServletResponse.getOutputStream();
     o.write(imgData);
     o.flush();
     o.close();
     } catch (Exception ex) {
     httpServletRequest.getSession(false).setAttribute("errorMessage", ex);
     } finally {
     if (image != null) {
     image = null;
     }
     }
     } */
    public void renderImage() {
        byte[] image = null;
        byte[] imgData = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        userId = Integer.parseInt(httpServletRequest.getSession().getAttribute(AppConstants.SES_USER_ID).toString());
        String queryString = "SELECT IMAGE FROM MSCVP.M_USER WHERE ID=" + userId;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(queryString);
            while (resultSet.next()) {
                if ((resultSet.getBinaryStream("IMAGE") != null)) {
                    InputStream is = resultSet.getBinaryStream("IMAGE");
                    imgData = IOUtils.toByteArray(is);
                } else {
                    File imagefile = new File("C:/MSCVP_DEMO/User_Image/user-icon.png");
                    FileInputStream fis = new FileInputStream(imagefile);
                    imgData = IOUtils.toByteArray(fis);
                }
            }
            httpServletResponse.setContentType("image/gif");
            OutputStream o = httpServletResponse.getOutputStream();
            o.write(imgData);
            o.flush();
            o.close();
        } catch (Exception ex) {
            httpServletRequest.getSession(false).setAttribute("errorMessage", ex);
        } finally {
            if (image != null) {
                image = null;
            }
        }
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    /**
     * @return the resultType
     */
    public String getResultType() {
        return resultType;
    }

    /**
     * @param resultType the resultType to set
     */
    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    /**
     * @return the fname
     */
    public String getFname() {
        return fname;
    }

    /**
     * @param fname the fname to set
     */
    public void setFname(String fname) {
        this.fname = fname;
    }

    /**
     * @return the lname
     */
    public String getLname() {
        return lname;
    }

    /**
     * @param lname the lname to set
     */
    public void setLname(String lname) {
        this.lname = lname;
    }

    /**
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * @return the ophno
     */
    public String getOphno() {
        return ophno;
    }

    /**
     * @param ophno the ophno to set
     */
    public void setOphno(String ophno) {
        this.ophno = ophno;
    }

    /**
     * @return the createdBy
     */
    public String getCreatedBy() {
        return createdBy;
    }

    /**
     * @param createdBy the createdBy to set
     */
    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    /**
     * @return the active
     */
    public String getActive() {
        return active;
    }

    /**
     * @param active the active to set
     */
    public void setActive(String active) {
        this.active = active;
    }

    /**
     * @return the deptId
     */
    public String getDeptId() {
        return deptId;
    }

    /**
     * @param deptId the deptId to set
     */
    public void setDeptId(String deptId) {
        this.deptId = deptId;
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * @return the role
     */
    public String getRole() {
        return role;
    }

    /**
     * @param role the role to set
     */
    public void setRole(String role) {
        this.role = role;
    }

    /**
     * @return the loginId
     */
    public String getLoginId() {
        return loginId;
    }

    /**
     * @param loginId the loginId to set
     */
    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    /**
     * @return the confirmPwd
     */
    public String getConfirmPwd() {
        return confirmPwd;
    }

    /**
     * @param confirmPwd the confirmPwd to set
     */
    public void setConfirmPwd(String confirmPwd) {
        this.confirmPwd = confirmPwd;
    }

    /**
     * @return the newPwd
     */
    public String getNewPwd() {
        return newPwd;
    }

    /**
     * @param newPwd the newPwd to set
     */
    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    /**
     * @return the oldPwd
     */
    public String getOldPwd() {
        return oldPwd;
    }

    /**
     * @param oldPwd the oldPwd to set
     */
    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    /**
     * @return the currentAction
     */
    public String getCurrentAction() {
        return currentAction;
    }

    /**
     * @param currentAction the currentAction to set
     */
    public void setCurrentAction(String currentAction) {
        this.currentAction = currentAction;
    }

    /**
     * @return the userPageId
     */
    public String getUserPageId() {
        return userPageId;
    }

    /**
     * @param userPageId the userPageId to set
     */
    public void setUserPageId(String userPageId) {
        this.userPageId = userPageId;
    }

    /**
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id the id to set
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return the userList
     */
    public List getUserList() {
        return userList;
    }

    /**
     * @param userList the userList to set
     */
    public void setUserList(List userList) {
        this.userList = userList;
    }

    /**
     * @return the userRoleId
     */
    public int getUserRoleId() {
        return userRoleId;
    }

    /**
     * @param userRoleId the userRoleId to set
     */
    public void setUserRoleId(int userRoleId) {
        this.userRoleId = userRoleId;
    }

    /**
     * @return the userBean
     */
    public UserBean getUserBean() {
        return userBean;
    }

    /**
     * @param userBean the userBean to set
     */
    public void setUserBean(UserBean userBean) {
        this.userBean = userBean;
    }

    /**
     * @return the userId
     */
    public int getUserId() {
        return userId;
    }

    /**
     * @param userId the userId to set
     */
    public void setUserId(int userId) {
        this.userId = userId;
    }

    /**
     * @return the userName
     */
    public String getUserName() {
        return userName;
    }

    /**
     * @param userName the userName to set
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }

    /**
     * @return the assignedFlowsMap
     */
    public Map getAssignedFlowsMap() {
        return assignedFlowsMap;
    }

    /**
     * @param assignedFlowsMap the assignedFlowsMap to set
     */
    public void setAssignedFlowsMap(Map assignedFlowsMap) {
        this.assignedFlowsMap = assignedFlowsMap;
    }

    /**
     * @return the notAssignedFlowsMap
     */
    public Map getNotAssignedFlowsMap() {
        return notAssignedFlowsMap;
    }

    /**
     * @param notAssignedFlowsMap the notAssignedFlowsMap to set
     */
    public void setNotAssignedFlowsMap(Map notAssignedFlowsMap) {
        this.notAssignedFlowsMap = notAssignedFlowsMap;
    }

    /**
     * @return the primaryFlowsList
     */
    public Map getPrimaryFlowsList() {
        return primaryFlowsList;
    }

    /**
     * @param primaryFlowsList the primaryFlowsList to set
     */
    public void setPrimaryFlowsList(Map primaryFlowsList) {
        this.primaryFlowsList = primaryFlowsList;
    }

    /**
     * @return the primaryFlow
     */
    public String getPrimaryFlow() {
        return primaryFlow;
    }

    /**
     * @param primaryFlow the primaryFlow to set
     */
    public void setPrimaryFlow(String primaryFlow) {
        this.primaryFlow = primaryFlow;
    }

    /**
     * @return the parameters
     */
    public Map getParameters() {
        return parameters;
    }

    /**
     * @param parameters the parameters to set
     */
    public void setParameters(Map parameters) {
        this.parameters = parameters;
    }

    /**
     * @return the addedFlowsList
     */
    public List getAddedFlowsList() {
        return addedFlowsList;
    }

    /**
     * @param addedFlowsList the addedFlowsList to set
     */
    public void setAddedFlowsList(List addedFlowsList) {
        this.addedFlowsList = addedFlowsList;
    }

    /**
     * @return the UserRolesMap
     */
    public Map getUserRolesMap() {
        return UserRolesMap;
    }

    /**
     * @param UserRolesMap the UserRolesMap to set
     */
    public void setUserRolesMap(Map UserRolesMap) {
        this.UserRolesMap = UserRolesMap;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public File getImagePath() {
        return imagePath;
    }

    public void setImagePath(File imagePath) {
        this.imagePath = imagePath;
    }

    public File getImageUpdate() {
        return imageUpdate;
    }

    public void setImageUpdate(File imageUpdate) {
        this.imageUpdate = imageUpdate;
    }

    public String getImageUpdateFileName() {
        return imageUpdateFileName;
    }

    public void setImageUpdateFileName(String imageUpdateFileName) {
        this.imageUpdateFileName = imageUpdateFileName;
    }

    public boolean isLogistics() {
        return logistics;
    }

    public void setLogistics(boolean logistics) {
        this.logistics = logistics;
    }

    public boolean isManufacturing() {
        return manufacturing;
    }

    public void setManufacturing(boolean manufacturing) {
        this.manufacturing = manufacturing;
    }

    public boolean isDocvisibility() {
        return docvisibility;
    }

    public void setDocvisibility(boolean docvisibility) {
        this.docvisibility = docvisibility;
    }

}
