/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.schdular;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocator;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.servlet.http.HttpServletResponse;

/**
 * @author miracle
 */
public class SchdularAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private HttpServletRequest httpServletRequest;
    private String sqlQuery;
    private String docSearchQuery;
    private String submitFrm;
    private String resultType;
    private String userEmail;
    private String weekelyReport;
    private String currentDsnName;
    private Map userMap;
    private String schtitle;
    private String comments;
    private String status;
    private String schType;
    private String schhours;
    private String schhrFormat;
    private int id;
    private int schid;
    private String currentAction;
    private String userPageId;
    private String ReciverIds;
    private String Userid;
    private String schStartdate;
    private String docdatepickerfrom;
    private String loginId;
    String resultMessage;
    private String schminutes;
    private String password;
    private int scheduleRefId;
    private String inputPath;
    private String contentDisposition = "FileName=inline";
    public InputStream inputStream;
    public OutputStream outputStream;
    private HttpServletResponse httpServletResponse;
    private String extranalmailids;
    private String fileName;
    private List receiverids;
    private String reportsType;
    private List<SchdularBean> schdularList;
    private static Logger logger = Logger.getLogger(SchdularAction.class.getName());
    private String configFlowFlag;

    public String getSchedular() throws Exception {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            String defaultFlowId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString();
            if (getConfigFlowFlag().equals("logistics")) {
                defaultFlowId = DataSourceDataProvider.getInstance().getFlowIdByFlowName("Logistics");
                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_USER_DEFAULT_FLOWID, defaultFlowId);
            } else if (getConfigFlowFlag().equals("manufacturing")) {
                defaultFlowId = DataSourceDataProvider.getInstance().getFlowIdByFlowName("Manufacturing");
                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_USER_DEFAULT_FLOWID, defaultFlowId);
            }
            setUserMap(DataSourceDataProvider.getInstance().getUserMap());
            setSchStartdate(DateUtility.getInstance().getCurrentMySqlDateTime1());
            if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_SCHDULAR_LIST) != null) {
                httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_SCHDULAR_LIST);
            }
            resultType = SUCCESS;
            setResultType(SUCCESS);
        }
        return getResultType();
    }

    public String getSchedularsearch() {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                schdularList = ServiceLocator.getSchdularService().getSchdularList(this);
                if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_SCHDULAR_LIST) != null) {
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_SCHDULAR_LIST);
                }
                setSchStartdate(DateUtility.getInstance().getCurrentMySqlDateTime1());
                if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_SCHDULAR_LIST) != null) {
                    httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_SCHDULAR_LIST);
                }
                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_SCHDULAR_LIST, schdularList);
                resultType = SUCCESS;
                setResultType(SUCCESS);
            } catch (Exception exception) {
                setResultType(ERROR);
            }
        }
        return getResultType();
    }

    public String getSchedularAdd() throws Exception {
        setResultType(LOGIN);
        if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            setUserMap(DataSourceDataProvider.getInstance().getUserMap());
            setCurrentAction("../partner/doAddSchdular.action");
            setUserPageId("0");
            resultType = SUCCESS;
            setResultType(SUCCESS);
        }
        return getResultType();
    }

    public String doAddSchdular() throws Exception {
        setResultType(LOGIN);
        if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            setUserMap(DataSourceDataProvider.getInstance().getUserMap());
            setCurrentAction("doAddSchdular");
            setUserPageId("0");
            String resultMessage = ServiceLocator.getSchdularService().SchdularAdd(this);
            getHttpServletRequest().setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
            resultType = SUCCESS;
        }
        return resultType;
    }

    public String getSchedularEdit() throws Exception {
        setResultType(LOGIN);
        if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            setUserMap(DataSourceDataProvider.getInstance().getUserMap());
            ServiceLocator.getSchdularService().schdularEdit(this);
            setCurrentAction("doUpdateSchduler");
            setUserPageId("1");
            resultType = SUCCESS;
        }
        return resultType;
    }

    public String doUpdateSchduler() throws Exception {
        setResultType(LOGIN);
        if (getHttpServletRequest().getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            setCurrentAction("doUpdateSchduler");
            setUserPageId("1");
            setUserMap(DataSourceDataProvider.getInstance().getUserMap());
            String resultMessage = ServiceLocator.getSchdularService().updateSchdular(this);
             setId(getId());
            getSchedularEdit();
            getHttpServletRequest().setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
            resultType = SUCCESS;
        }
        return resultType;
    }

    public String authdownload() throws Exception {
        String filePath = ServiceLocator.getSchdularService().SchdularRecordPath(this);
        DownloadReport(filePath);
        return getResultType();
    }

    public String authdownloadUser() throws Exception {
        setResultType(LOGIN);
        String filePath = ServiceLocator.getSchdularService().SchdularRecordPath(this);
        DownloadReport(filePath);
        resultMessage = "<font color='green'> Username not authorized.</font>";
        setResultType(SUCCESS);
        return getResultType();
    }

    public void DownloadReport(String filepath) {
        String responseString = "";
        try {
            httpServletResponse.setContentType("application/force-download");
            File file = new File(filepath);
            if (file.exists()) {
                fileName = file.getName();
                inputStream = new FileInputStream(file);
                outputStream = httpServletResponse.getOutputStream();
                httpServletResponse.setHeader("Content-Disposition", "attachment;filename=\"" + fileName + "\"");
                int noOfBytesRead = 0;
                byte[] byteArray = null;
                while (true) {
                    byteArray = new byte[1024];
                    noOfBytesRead = inputStream.read(byteArray);
                    if (noOfBytesRead == 0) {
                        break;
                    }
                    outputStream.write(byteArray, 0, noOfBytesRead);
                }
                responseString = "downLoaded!!";
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } else {
                throw new FileNotFoundException("File not found");
            }
        } catch (FileNotFoundException ex) {
            try {
                httpServletResponse.sendRedirect("../general/exception.action?exceptionMessage='No File found'");
            } catch (IOException ex1) {
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                inputStream.close();
                outputStream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }

    public String getDocdatepickerfrom() {
        return docdatepickerfrom;
    }

    public void setDocdatepickerfrom(String docdatepickerfrom) {
        this.docdatepickerfrom = docdatepickerfrom;
    }

    public void setDocSearchQuery(String docSearchQuery) {
        this.docSearchQuery = docSearchQuery;
    }

    public String getDocSearchQuery() {
        return docSearchQuery;
    }

    public void setSubmitFrm(String submitFrm) {
        this.submitFrm = submitFrm;
    }

    public String getSubmitFrm() {
        return submitFrm;
    }

    public void setResultType(String resultType) {
        this.resultType = resultType;
    }

    public String getResultType() {
        return resultType;
    }

    public void setCurrentDsnName(String currentDsnName) {
        this.currentDsnName = currentDsnName;
    }

    public String getCurrentDsnName() {
        return currentDsnName;
    }

    /**
     * @return the docdatepicker
     */
    public String getWeekelyReport() {
        return weekelyReport;
    }

    public void setWeekelyReport(String weekelyReport) {
        this.weekelyReport = weekelyReport;
    }

    public Map getUserMap() {
        return userMap;
    }

    public void setUserMap(Map userMap) {
        this.userMap = userMap;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public String getSchtitle() {
        return schtitle;
    }

    public void setSchtitle(String schtitle) {
        this.schtitle = schtitle;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getSchType() {
        return schType;
    }

    public void setSchType(String schType) {
        this.schType = schType;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCurrentAction() {
        return currentAction;
    }

    public void setCurrentAction(String currentAction) {
        this.currentAction = currentAction;
    }

    public String getUserPageId() {
        return userPageId;
    }

    public void setUserPageId(String userPageId) {
        this.userPageId = userPageId;
    }

    public String getReciverIds() {
        return ReciverIds;
    }

    public void setReciverIds(String ReciverIds) {
        this.ReciverIds = ReciverIds;
    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String Userid) {
        this.Userid = Userid;
    }

    public String getSchhours() {
        return schhours;
    }

    public void setSchhours(String schhours) {
        this.schhours = schhours;
    }

    public String getSchhrFormat() {
        return schhrFormat;
    }

    public void setSchhrFormat(String schhrFormat) {
        this.schhrFormat = schhrFormat;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getSchStartdate() {
        return schStartdate;
    }

    public void setSchStartdate(String schStartdate) {
        this.schStartdate = schStartdate;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getScheduleRefId() {
        return scheduleRefId;
    }

    public void setScheduleRefId(int scheduleRefId) {
        this.scheduleRefId = scheduleRefId;
    }

    public int getSchid() {
        return schid;
    }

    public void setSchid(int schid) {
        this.schid = schid;
    }

    public List getReceiverids() {
        return receiverids;
    }

    public void setReceiverids(List receiverids) {
        this.receiverids = receiverids;
    }

    public String getExtranalmailids() {
        return extranalmailids;
    }

    public void setExtranalmailids(String extranalmailids) {
        this.extranalmailids = extranalmailids;
    }

    public String getReportsType() {
        return reportsType;
    }

    public void setReportsType(String reportsType) {
        this.reportsType = reportsType;
    }

    public String getSchminutes() {
        return schminutes;
    }

    public void setSchminutes(String schminutes) {
        this.schminutes = schminutes;
    }

    public void setServletResponse(HttpServletResponse httpServletResponse) {

        this.httpServletResponse = httpServletResponse;
    }

    public String getConfigFlowFlag() {
        return configFlowFlag;
    }

    public void setConfigFlowFlag(String configFlowFlag) {
        this.configFlowFlag = configFlowFlag;
    }
    /**
     * @return the docSenderId
     */
}
