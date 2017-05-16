

/*
 * Author : Santosh Kola
 * Created Date : 07/01/2013
 * 
 */
package com.mss.ediscv.purge;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.AuthorizationManager;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.ServiceLocator;
import com.mss.ediscv.util.ServiceLocatorException;
import com.opensymphony.xwork2.ActionSupport;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.apache.struts2.interceptor.ServletRequestAware;

/**
 * @author miracle
 */
public class PurgeAction extends ActionSupport implements ServletRequestAware {

    private HttpServletRequest httpServletRequest;
    private String datepickerfrom;
    private String datepicker;
    private String transType;
    private String resultType;
    private String resultMessage;
    private String dayCount;
    private String comments;
    private String docdatepicker;
    private String reportrange;
    private Map flowName;

    public String getDocdatepickerfrom() {
        return docdatepickerfrom;
    }

    public void setDocdatepickerfrom(String docdatepickerfrom) {
        this.docdatepickerfrom = docdatepickerfrom;
    }
    private String docdatepickerfrom;
    
    

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

      
    //added by raja for Archiving the data
    
    
    public String getArcProPage(){
        
         Map userFlow=  (Map)httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP);
            setFlowName(DataSourceDataProvider.getInstance().getTransactionType(userFlow));
        System.out.println(" =============== rajarajaraja");
          
        return SUCCESS;
    }
    
    public String getArcHisPage(){
         Map userFlow=  (Map)httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP);
            setFlowName(DataSourceDataProvider.getInstance().getTransactionType(userFlow));
        return SUCCESS;
    }
    public String getPurProPage(){
         Map userFlow=  (Map)httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP);
            setFlowName(DataSourceDataProvider.getInstance().getTransactionType(userFlow));
        return SUCCESS;
    }
    public String getPurHisPage(){
         Map userFlow=  (Map)httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP);
            setFlowName(DataSourceDataProvider.getInstance().getTransactionType(userFlow));
        return SUCCESS;
    }
    
    
    
    public String doArchiveProcess() throws ServiceLocatorException {
        resultType = LOGIN;
        String username = (String)httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME);
          String defaultFlowId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString();
         String defaultFlowName = DataSourceDataProvider.getInstance().getFlowNameByFlowID(defaultFlowId);
       
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            resultType = "accessFailed";
            int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
             
            if (AuthorizationManager.getInstance().isAuthorizedUser("PURGE_PROCESS", userRoleId)) {
                try {
                    resultMessage = ServiceLocator.getPurgeService().archiveProcess(this, username,defaultFlowName);
                    setDayCount("");
                    setTransType("-1");
                     Map userFlow=  (Map)httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP);
        setFlowName(DataSourceDataProvider.getInstance().getTransactionType(userFlow));
                    httpServletRequest.setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
                    resultType = SUCCESS;
                } catch (Exception e) {
                    resultType = ERROR;
                    e.printStackTrace();
                }
            }
        }
        return resultType;
    }
    
    public String doPurgeProcess() throws ServiceLocatorException {
        resultType = LOGIN;
      
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            resultType = "accessFailed";
             String username = (String)httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME);
              String defaultFlowId = httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_DEFAULT_FLOWID).toString();
             
         String defaultFlowName = DataSourceDataProvider.getInstance().getFlowNameByFlowID(defaultFlowId);
            int userRoleId = Integer.parseInt(httpServletRequest.getSession(false).getAttribute(AppConstants.SES_ROLE_ID).toString());
            if (AuthorizationManager.getInstance().isAuthorizedUser("PURGE_PROCESS", userRoleId)) {
                try {
                    resultMessage = ServiceLocator.getPurgeService().purgeProcess(this, username, defaultFlowName);
                    setDayCount("");
                    setTransType("-1");
                      Map userFlow=  (Map)httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP);
            setFlowName(DataSourceDataProvider.getInstance().getTransactionType(userFlow)); 
                    httpServletRequest.setAttribute(AppConstants.REQ_RESULT_MSG, resultMessage);
                    resultType = SUCCESS;
                } catch (Exception e) {
                    resultType = ERROR;
                    e.printStackTrace();
                }
            }
        }
        return resultType;
    }
    
    public String getPurHistory(){
        
        httpServletRequest.getSession(false).removeAttribute(AppConstants.PURGEHISTORY_LIST);
        Map userFlow=  (Map)httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP);
            setFlowName(DataSourceDataProvider.getInstance().getTransactionType(userFlow));
        
        try{
           
             String username = (String)httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME);
        List purgeHistorylist = null;
        purgeHistorylist = ServiceLocator.getPurgeService().getPurHistoryData(username, getDatepickerfrom(),getDatepicker(), getTransType() );
            System.out.println("purgeHistorylist"+purgeHistorylist.size());
         httpServletRequest.getSession(false).setAttribute(AppConstants.PURGEHISTORY_LIST, purgeHistorylist);
        
        }catch(Exception e){
            e.printStackTrace();
        }
        
             return "success";  
    }
    
    public String getArcHistory(){
        
         httpServletRequest.getSession(false).removeAttribute(AppConstants.ARCHIVEHISTORY_LIST);
         Map userFlow=  (Map)httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_FLOW_MAP);
            setFlowName(DataSourceDataProvider.getInstance().getTransactionType(userFlow));
        try{
            
          
             String username = (String)httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME);
         List archiveHistorylist = null;
            System.out.println("to is  is "+getDatepicker());
        archiveHistorylist = ServiceLocator.getPurgeService().getArcHistoryData(username, getDatepickerfrom(),getDatepicker(), getTransType() );
         httpServletRequest.getSession(false).setAttribute(AppConstants.ARCHIVEHISTORY_LIST, archiveHistorylist);
        
        }catch(Exception e){
            e.printStackTrace();
        }
        
             return "success";  
    }
    
    

    @Override
    public void setServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public String getDatepickerfrom() {
        return datepickerfrom;
    }

    public void setDatepickerfrom(String datepickerfrom) {
        this.datepickerfrom = datepickerfrom;
    }

    public String getDatepicker() {
        return datepicker;
    }

    public void setDatepicker(String datepicker) {
        this.datepicker = datepicker;
    }

  

    
    /**
     * @return the transType
     */
    public String getTransType() {
        return transType;
    }

    /**
     * @param transType the transType to set
     */
    public void setTransType(String transType) {
        this.transType = transType;
    }

    /**
     * @return the dayCount
     */
    public String getDayCount() {
        return dayCount;
    }

    /**
     * @param dayCount the dayCount to set
     */
    public void setDayCount(String dayCount) {
        this.dayCount = dayCount;
    }

    public String getReportrange() {
        return reportrange;
    }

    public void setReportrange(String reportrange) {
        this.reportrange = reportrange;
    }

    public Map getFlowName() {
        return flowName;
    }

    public void setFlowName(Map flowName) {
        this.flowName = flowName;
    }
    
}