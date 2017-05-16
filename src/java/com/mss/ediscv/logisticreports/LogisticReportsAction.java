/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.logisticreports;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocator;
import com.opensymphony.xwork2.ActionSupport;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.log4j.Logger;

/**
 * @author miracle
 */
public class LogisticReportsAction extends ActionSupport implements ServletRequestAware {

    private static Logger logger = Logger.getLogger(LogisticReportsAction.class.getName());
    private HttpServletRequest httpServletRequest;
    private String resultType;
    private String formAction;
    private List correlationList;
    private List docTypeList;
    private String docdatepicker;
    private String docdatepickerfrom;
    private String docSenderId;
    private String docSenderName;
    private String docType;
    private String status;
    private String ackStatus;
    private String docBusId;
    private String docRecName;
    private String check;
    private List<LogisticReportsBean> documentList;
    private Map partnerMap;
    private String reportrange;
    private String database;

    public String getLogisticReports() throws Exception {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            String defaultFlowId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString();
            String defaultFlowName = DataSourceDataProvider.getInstance().getFlowNameByFlowID(defaultFlowId);
            httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_LOG_DOC_LIST);
            httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_LOGSHIPMENT_LIST);
            httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_LOAD_LIST);
            httpServletRequest.getSession(false).removeAttribute(AppConstants.SES_LTRESPONSE_LIST);
            List corrList;
            List docList;
            corrList = DataSourceDataProvider.getInstance().getCorrelationNames(0, 2);
            docList = DataSourceDataProvider.getInstance().getDocumentTypeList(0, 2);
            setDocdatepicker(DateUtility.getInstance().getCurrentMySqlDateTime1());
            setCorrelationList(corrList);
            setDocTypeList(docList);
            if (!defaultFlowName.equals("Logistics")) {
                defaultFlowId = DataSourceDataProvider.getInstance().getFlowIdByFlowName("Logistics");
                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_USER_DEFAULT_FLOWID, defaultFlowId);
            }
            if ("ARCHIVE".equals(getDatabase())) {
                    setDatabase("ARCHIVE");
                } else {
                    setDatabase("MSCVP");
                }
            setResultType(SUCCESS);
        }
        return getResultType();
    }

    public String logisticreportsSearch() {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                execute();
                if (getCheck() == null) {
                    setCheck("1");
                } else if (getCheck().equals("")) {
                    setCheck("1");
                }
                List corrList;
                List docList;
                corrList = DataSourceDataProvider.getInstance().getCorrelationNames(0, 2);
                docList = DataSourceDataProvider.getInstance().getDocumentTypeList(0, 2);
                //setDocdatepicker(DateUtility.getInstance().getCurrentMySqlDateTime1());
                setCorrelationList(corrList);
                setDocTypeList(docList);
                if ("ARCHIVE".equals(getDatabase())) {
                      documentList = ServiceLocator.getLogisticReportsService().getDocumentListArchive(this);
                    } else {
                       documentList = ServiceLocator.getLogisticReportsService().getDocumentList(this);
                    }
                
                httpServletRequest.getSession(false).setAttribute(AppConstants.SES_LOG_DOC_LIST, documentList);
                setResultType(SUCCESS);
            } catch (Exception ex) {
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, ex.getMessage());
                setResultType("error");
            }
        }
        return getResultType();
    }

    public String getDashboard() {
        setResultType(LOGIN);
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            try {
                String defaultFlowId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString();
                String defaultFlowName = DataSourceDataProvider.getInstance().getFlowNameByFlowID(defaultFlowId);
                if (!defaultFlowName.equals("Logistics")) {
                    defaultFlowId = DataSourceDataProvider.getInstance().getFlowIdByFlowName("Logistics");
                    httpServletRequest.getSession(false).setAttribute(AppConstants.SES_USER_DEFAULT_FLOWID, defaultFlowId);
                }
                List docList;
                //docList = DataSourceDataProvider.getInstance().getDocumentTypeList();
                docList = DataSourceDataProvider.getInstance().getDocumentTypeList(0, 2);
                setDocTypeList(docList);
                setPartnerMap(DataSourceDataProvider.getInstance().getDashboardPartnerMap("2"));
                setDocdatepicker(DateUtility.getInstance().getCurrentMySqlDateTime1());
                resultType = SUCCESS;
                setResultType(SUCCESS);
            } catch (Exception exception) {
                setResultType(ERROR);
            }
        }
        return getResultType();
    }

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
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
     * @return the formAction
     */
    public String getFormAction() {
        return formAction;
    }

    /**
     * @param formAction the formAction to set
     */
    public void setFormAction(String formAction) {
        this.formAction = formAction;
    }

    /**
     * @return the correlationList
     */
    public List getCorrelationList() {
        return correlationList;
    }

    /**
     * @param correlationList the correlationList to set
     */
    public void setCorrelationList(List correlationList) {
        this.correlationList = correlationList;
    }

    /**
     * @return the docTypeList
     */
    public List getDocTypeList() {
        return docTypeList;
    }

    /**
     * @param docTypeList the docTypeList to set
     */
    public void setDocTypeList(List docTypeList) {
        this.docTypeList = docTypeList;
    }

    /**
     * @return the docSenderId
     */
    public String getDocSenderId() {
        return docSenderId;
    }

    /**
     * @param docSenderId the docSenderId to set
     */
    public void setDocSenderId(String docSenderId) {
        this.docSenderId = docSenderId;
    }

    /**
     * @return the docSenderName
     */
    public String getDocSenderName() {
        return docSenderName;
    }

    /**
     * @param docSenderName the docSenderName to set
     */
    public void setDocSenderName(String docSenderName) {
        this.docSenderName = docSenderName;
    }

    /**
     * @return the docReceiverId
     */
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
     * @return the partnerMap
     */
    public Map getPartnerMap() {
        return partnerMap;
    }

    /**
     * @param partnerMap the partnerMap to set
     */
    public void setPartnerMap(Map partnerMap) {
        this.partnerMap = partnerMap;
    }

    public String getDocdatepicker() {
        return docdatepicker;
    }

    public void setDocdatepicker(String docdatepicker) {
        this.docdatepicker = docdatepicker;
    }

    public String getDocdatepickerfrom() {
        return docdatepickerfrom;
    }

    public void setDocdatepickerfrom(String docdatepickerfrom) {
        this.docdatepickerfrom = docdatepickerfrom;
    }

    public String getDocBusId() {
        return docBusId;
    }

    public void setDocBusId(String docBusId) {
        this.docBusId = docBusId;
    }

    public String getDocRecName() {
        return docRecName;
    }

    public void setDocRecName(String docRecName) {
        this.docRecName = docRecName;
    }

    public String getCheck() {
        return check;
    }

    public void setCheck(String check) {
        this.check = check;
    }

    public String getReportrange() {
        return reportrange;
    }

    public void setReportrange(String reportrange) {
        this.reportrange = reportrange;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }
    
}
