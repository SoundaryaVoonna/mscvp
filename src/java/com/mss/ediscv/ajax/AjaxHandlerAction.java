/*
 * AjaxHandlerAction.java
 *
 * Created on June 11, 2008, 12:22 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.mss.ediscv.ajax;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.ServiceLocator;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.SUCCESS;
import com.opensymphony.xwork2.ActionSupport;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

/**
 *
 * @author miracle
 */
public class AjaxHandlerAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    /**
     * Creating a reference variable for HttpServletRequest.
     */
    private HttpServletRequest httpServletRequest;
    /**
     * Creating a reference variable for HttpServletResponse.
     */
    private HttpServletResponse httpServletResponse;
    private int id;
    private String responseString;
    private String poNumber;
    private String poInst;
    private String asnNumber;
    private String invNumber;
    private String isaNumber;
    private String poList;
    // new field for asn copy
    private String asnList;
    private String invList;
    private String paymentList;
    private String type;
    private String fileId;
    private String tpId;
    private String name;
    private String dept;
    private String commid;
    private String contact;
    private String phno;
    private String qualifier;
    private String refId;
    private String loadList;
    private int docId;
    // new screen fields
    private String partnerId;
    private String routingId;
    private String b2bChannelId;
    private String routerId;
    private String businessProcessId;
    /**
     * Creates a new instance of AjaxHandlerAction
     */
    private int deliveryChannelId;
    // Dash board fields
    private String startDate;
    private String endDate;
    private String senderId;
    private String receiverId;
    private String docType;
    private String ackStatus;
    private String status;
    private String direction;
    private String oldPwd;
    private String newPwd;
    private String cnfrmPwd;
    private String senderItem;
    private String recItem;

    private String database;

    private String selectedName;
    private String newListName;
    private List listNameMap;
    private String json;
    private String listName;
    private int items;
    private String modifieddate;
    private String flag;

    public AjaxHandlerAction() {
    }

    public String getPoDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getPoDetails(getPoNumber(), getPoInst(), getDatabase()).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getAsnDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getASNDetails(getAsnNumber(), getPoNumber(), getFileId(), getDatabase()).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getInvDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getInvDetails(getInvNumber(), getPoNumber(), getFileId(), getDatabase()).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getDocDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getDocDetails(getIsaNumber(), getPoNumber(), getId(), getDatabase()).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getReportDeleteDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getReportDeleteDetails(getId()).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getPaymentDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getPaymentDetails(getFileId(), getDatabase()).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getDocCopy() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getDocCopy(getPoList().toString(), getType().toString(), getDatabase());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    //get asn datails for copy
    public String getDocASNCopy() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getDocASNCopy(getAsnList().toString(), getType().toString());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getInvCopy() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getInvCopy(getInvList().toString(), getType().toString());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getPaymentCopy() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getInvCopy(getPaymentList().toString(), getType().toString());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getLoadCopy() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getLoadCopy(getLoadList().toString(), getType().toString(),getDatabase().toString());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     *
     * Life cycle Calls
     */
    public String getLifecycleDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getLifeCycleDetails(getPoNumber(), getFileId(), getType(), getDatabase()).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getLtLifecycleDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                System.out.println("into life cycle action");
                responseString = ServiceLocator.getAjaxHandlerService().getLtLifecycleDetails(getPoNumber(), getFileId(), getType(),getDatabase()).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                //ex.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Tp Add and Update*
     */
    /**
     * Tp Details
     *
     */
    public String getTpDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getTpDetails(getTpId()).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }
    /*
     * 
     * To Update tp Details
     */

    public String updateTpDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().updateTpDetails(this).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    //for getting TradingPartner Detail Information
    public String getTpDetailInformation() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getTpDetailInformation(getTpId(), httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString()).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getLogisticsDocDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getLogisticsDocDetails(getIsaNumber(), getId(), getDatabase()).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getLoadTenderingDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getLoadTenderingDetails(getIsaNumber(), getPoNumber(), getDatabase()).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getLtResponseDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getLtResponseDetails(getFileId(), getRefId(), getDatabase()).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getLogisticsInvDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getLogisticsInvDetails(getInvNumber(), getId(), getDatabase()).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getLogisticsShipmentDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getLogisticsShipmentDetails(getAsnNumber(), getPoNumber(), getId(), getDatabase()).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /*Method for document visibility detalinfo
     * Author : Santosh Kola
     * Date : 01-06-2014
     * 
     */
    public String getDocVisibilityDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getDocVisibilityDetails(getDocId());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    // new screens
    public String getPartnerDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getPartnerDetails(getPartnerId());
                System.err.println("responseStringAction--->" + responseString);
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getRoutingDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getRoutingDetails(getRoutingId());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getB2bChannelDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getB2bChannelDetails(getB2bChannelId());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getPartnerInfo() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getPartnerInfo(getPartnerId());
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getRouterInfo() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getRouterInfo(getRouterId());
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getBusinessProcessInfo() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getBusinessProcessInfo(getBusinessProcessId());
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String getDeliveryChannelDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getDeliveryChannelDetails(getDeliveryChannelId());
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    /*Method for displaying Dashboarddetails
     * Date : 02/19/2015
     * 
     */
    public String getDashboardDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getDashboardDetails(this);
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    // new action for schdular task
    public String getReportOverlayDetails() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                responseString = ServiceLocator.getAjaxHandlerService().getReportOverlayDetails(getId(), getStartDate()).toString();
                httpServletResponse.setContentType("text/xml");
                httpServletResponse.getWriter().write(responseString);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String forgotPassword() throws Exception {
        try {
            String userid = getUserid();
            String response = ServiceLocator.getAjaxHandlerService().forgotPassword(userid);
            httpServletResponse.setContentType("text/html");
            if (response.equals("success")) {
                httpServletResponse.getWriter().write(SUCCESS);
            } else {
                httpServletResponse.getWriter().write(ERROR);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String changePassword() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                String loginId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString();
                int n;
                n = ServiceLocator.getAjaxHandlerService().updateMyPwd(this, loginId);
                if (n == 1) {
                    responseString = "<font color=\"green\" size=\"3\">Password updated successfully</font>";
                } else if (n == 100) {
                    responseString = "<font color=\"red\" size=\"3\">New Password and Confirm Password must be same!</font>";
                } else if (n == 200) {
                    responseString = "<font color=\"red\" size=\"3\">Please enter correct Old Password!</font>";
                } else {
                    responseString = "<font color=\"red\" size=\"3\">Password updation failed!</font>";
                }
                httpServletResponse.setContentType("text/html");
                httpServletResponse.getWriter().write(responseString);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    //method to search whether sender item and receiver item exists in the database for code list or not 
    public String searchItems() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                String loginId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString();
                int n;
                n = ServiceLocator.getAjaxHandlerService().searchItems(getSenderItem(), getRecItem(), getSelectedName());
                if (n > 0) {
                    responseString = "Failure";
                } else {
                    responseString = "Success";
                }
                httpServletResponse.setContentType("text/html");
                httpServletResponse.getWriter().write(responseString);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    //method to check whether new code list nameexists in the database or not for adding new codeList 
    public String checkCodeListName() {
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                String loginId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString();
                int n;
                n = ServiceLocator.getAjaxHandlerService().checkCodeListName(getNewListName());
                if (n > 0) {
                    responseString = "Failure";
                } else {
                    responseString = "Success";
                }
                httpServletResponse.setContentType("text/html");
                httpServletResponse.getWriter().write(responseString);

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }

    public String doCodeVersionUpdate() throws Exception {
        System.out.println("doCodeVersionUpdate-----");
        String resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME).toString() != null) {
            try {
                String resultMessage = "";
                String codeList = "";
                String userName = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString();
                System.out.println("username is " + userName);
                List codeList1 = new ArrayList();
                codeList1 = (List) httpServletRequest.getSession(false).getAttribute(AppConstants.CODE_LIST);
                int codeListSize = codeList1.size();
                resultMessage = ServiceLocator.getAjaxHandlerService().updateCodeList(getListName(), getJson(), userName, codeListSize);
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
                setListNameMap(DataSourceDataProvider.getInstance().getListName());
                setListName("-1");
                resultType = SUCCESS;
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(resultMessage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String doCodeListAdd() throws Exception {
        String resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME).toString() != null) {
            try {
                String userName = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_LOGIN_ID).toString();
                System.out.println("username is " + userName);
                String resultMessage = "";
                List codeList = new ArrayList();
                resultMessage = ServiceLocator.getAjaxHandlerService().addCodeList(getJson(), userName, getNewListName());
                httpServletRequest.getSession(false).removeAttribute(AppConstants.CODE_LIST);
                resultType = SUCCESS;
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(resultMessage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public String doCodeListDelete() throws Exception {
        String resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME).toString() != null) {
            try {
                String resultMessage = "";
                List codeList = new ArrayList();
                resultMessage = ServiceLocator.getAjaxHandlerService().deleteCodeList(getJson());
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
                //getCodeListName();
//                CertMonitorAction c = new CertMonitorAction();
//                c.getCodeListItems();
                setListNameMap(DataSourceDataProvider.getInstance().getListName());
                resultType = SUCCESS;
                httpServletResponse.setContentType("text");
                httpServletResponse.getWriter().write(resultMessage);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    /**
     *
     * This method is used to set the Servlet Request
     *
     * @param httpServletRequest
     */
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    /**
     *
     * This method is used to set the Servlet Response
     *
     * @param httpServletResponse
     */
    public void setServletResponse(HttpServletResponse httpServletResponse) {
        this.httpServletResponse = httpServletResponse;
    }

    /**
     * @return the poNumber
     */
    public String getPoNumber() {
        return poNumber;
    }

    /**
     * @param poNumber the poNumber to set
     */
    public void setPoNumber(String poNumber) {
        this.poNumber = poNumber;
    }

    /**
     * @return the asnNumber
     */
    public String getAsnNumber() {
        return asnNumber;
    }

    /**
     * @param asnNumber the asnNumber to set
     */
    public void setAsnNumber(String asnNumber) {
        this.asnNumber = asnNumber;
    }

    /**
     * @return the invNumber
     */
    public String getInvNumber() {
        return invNumber;
    }

    /**
     * @param invNumber the invNumber to set
     */
    public void setInvNumber(String invNumber) {
        this.invNumber = invNumber;
    }

    /**
     * @return the isaNumber
     */
    public String getIsaNumber() {
        return isaNumber;
    }

    /**
     * @param isaNumber the isaNumber to set
     */
    public void setIsaNumber(String isaNumber) {
        this.isaNumber = isaNumber;
    }

    /**
     * @return the poList
     */
    public String getPoList() {
        return poList;
    }

    /**
     * @param poList the poList to set
     */
    public void setPoList(String poList) {
        this.poList = poList;
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * @return the poInst
     */
    public String getPoInst() {
        return poInst;
    }

    /**
     * @param poInst the poInst to set
     */
    public void setPoInst(String poInst) {
        this.poInst = poInst;
    }

    /**
     * @return the fileId
     */
    public String getFileId() {
        return fileId;
    }

    /**
     * @param fileId the fileId to set
     */
    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    /**
     * @return the tpId
     */
    public String getTpId() {
        return tpId;
    }

    /**
     * @param tpId the tpId to set
     */
    public void setTpId(String tpId) {
        this.tpId = tpId;
    }

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the dept
     */
    public String getDept() {
        return dept;
    }

    /**
     * @param dept the dept to set
     */
    public void setDept(String dept) {
        this.dept = dept;
    }

    /**
     * @return the commid
     */
    public String getCommid() {
        return commid;
    }

    /**
     * @param commid the commid to set
     */
    public void setCommid(String commid) {
        this.commid = commid;
    }

    /**
     * @return the contact
     */
    public String getContact() {
        return contact;
    }

    /**
     * @param contact the contact to set
     */
    public void setContact(String contact) {
        this.contact = contact;
    }

    /**
     * @return the phno
     */
    public String getPhno() {
        return phno;
    }

    /**
     * @param phno the phno to set
     */
    public void setPhno(String phno) {
        this.phno = phno;
    }

    /**
     * @return the qualifier
     */
    public String getQualifier() {
        return qualifier;
    }

    /**
     * @param qualifier the qualifier to set
     */
    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    /**
     * @return the refId
     */
    public String getRefId() {
        return refId;
    }

    /**
     * @param refId the refId to set
     */
    public void setRefId(String refId) {
        this.refId = refId;
    }

    /**
     * @return the loadList
     */
    public String getLoadList() {
        return loadList;
    }

    /**
     * @param loadList the loadList to set
     */
    public void setLoadList(String loadList) {
        this.loadList = loadList;
    }

    /**
     * @return the docId
     */
    public int getDocId() {
        return docId;
    }

    /**
     * @param docId the docId to set
     */
    public void setDocId(int docId) {
        this.docId = docId;
    }

    /**
     * @return the partnerId
     */
    public String getPartnerId() {
        return partnerId;
    }

    /**
     * @param partnerId the partnerId to set
     */
    public void setPartnerId(String partnerId) {
        this.partnerId = partnerId;
    }

    /**
     * @return the routingId
     */
    public String getRoutingId() {
        return routingId;
    }

    /**
     * @param routingId the routingId to set
     */
    public void setRoutingId(String routingId) {
        this.routingId = routingId;
    }

    /**
     * @return the b2bChannelId
     */
    public String getB2bChannelId() {
        return b2bChannelId;
    }

    /**
     * @param b2bChannelId the b2bChannelId to set
     */
    public void setB2bChannelId(String b2bChannelId) {
        this.b2bChannelId = b2bChannelId;
    }

    /**
     * @return the routerId
     */
    public String getRouterId() {
        return routerId;
    }

    /**
     * @param routerId the routerId to set
     */
    public void setRouterId(String routerId) {
        this.routerId = routerId;
    }

    /**
     * @return the businessProcessId
     */
    public String getBusinessProcessId() {
        return businessProcessId;
    }

    /**
     * @param businessProcessId the businessProcessId to set
     */
    public void setBusinessProcessId(String businessProcessId) {
        this.businessProcessId = businessProcessId;
    }

    /**
     * @return the deliveryChannelId
     */
    public int getDeliveryChannelId() {
        return deliveryChannelId;
    }

    /**
     * @param deliveryChannelId the deliveryChannelId to set
     */
    public void setDeliveryChannelId(int deliveryChannelId) {
        this.deliveryChannelId = deliveryChannelId;
    }

    /**
     * @return the startDate
     */
    public String getStartDate() {
        return startDate;
    }

    /**
     * @param startDate the startDate to set
     */
    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    /**
     * @return the endDate
     */
    public String getEndDate() {
        return endDate;
    }

    /**
     * @param endDate the endDate to set
     */
    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    /**
     * @return the senderId
     */
    public String getSenderId() {
        return senderId;
    }

    /**
     * @param senderId the senderId to set
     */
    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    /**
     * @return the receiverId
     */
    public String getReceiverId() {
        return receiverId;
    }

    /**
     * @param receiverId the receiverId to set
     */
    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    /**
     * @return the docType
     */
    public String getDocType() {
        return docType;
    }

    /**
     * @param docType the docType to set
     */
    public void setDocType(String docType) {
        this.docType = docType;
    }

    /**
     * @return the ackStatus
     */
    public String getAckStatus() {
        return ackStatus;
    }

    /**
     * @param ackStatus the ackStatus to set
     */
    public void setAckStatus(String ackStatus) {
        this.ackStatus = ackStatus;
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
     * @return the direction
     */
    public String getDirection() {
        return direction;
    }

    /**
     * @param direction the direction to set
     */
    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAsnList() {
        return asnList;
    }

    public void setAsnList(String asnList) {
        this.asnList = asnList;
    }

    public String getInvList() {
        return invList;
    }

    public void setInvList(String invList) {
        this.invList = invList;
    }

    public String getPaymentList() {
        return paymentList;
    }

    public void setPaymentList(String paymentList) {
        this.paymentList = paymentList;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
    private String userid;

    public String getCnfrmPwd() {
        return cnfrmPwd;
    }

    public void setCnfrmPwd(String cnfrmPwd) {
        this.cnfrmPwd = cnfrmPwd;
    }

    public String getNewPwd() {
        return newPwd;
    }

    public void setNewPwd(String newPwd) {
        this.newPwd = newPwd;
    }

    public String getOldPwd() {
        return oldPwd;
    }

    public void setOldPwd(String oldPwd) {
        this.oldPwd = oldPwd;
    }

    public String getSenderItem() {
        return senderItem;
    }

    public void setSenderItem(String senderItem) {
        this.senderItem = senderItem;
    }

    public String getRecItem() {
        return recItem;
    }

    public void setRecItem(String recItem) {
        this.recItem = recItem;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

    public String getSelectedName() {
        return selectedName;
    }

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
    }

    public String getNewListName() {
        return newListName;
    }

    public void setNewListName(String newListName) {
        this.newListName = newListName;
    }

    public List getListNameMap() {
        return listNameMap;
    }

    public void setListNameMap(List listNameMap) {
        this.listNameMap = listNameMap;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getResponseString() {
        return responseString;
    }

    public void setResponseString(String responseString) {
        this.responseString = responseString;
    }

    public int getItems() {
        return items;
    }

    public void setItems(int items) {
        this.items = items;
    }

    public String getModifieddate() {
        return modifieddate;
    }

    public void setModifieddate(String modifieddate) {
        this.modifieddate = modifieddate;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

}
