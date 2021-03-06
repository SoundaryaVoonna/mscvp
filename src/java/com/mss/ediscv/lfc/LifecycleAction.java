/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.lfc;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.ServiceLocator;
import com.opensymphony.xwork2.ActionSupport;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.log4j.Logger;
import org.apache.struts2.interceptor.ServletRequestAware;

/**
 * @author miracle
 */
public class LifecycleAction extends ActionSupport implements ServletRequestAware {

    private HttpServletRequest httpServletRequest;
    private String sqlQuery;
    private String docSearchQuery;
    private String submitFrm;
    private String resultType;
    private LifecycleBeans lifeCycleBeans;
    private String currentDsnName;
    private String poNumber;
    private String shipmentNumber;
    private PoLifecycleBean poLifecycleBean;
    private static Logger logger = Logger.getLogger(LifecycleAction.class.getName());
    private String database;

    public String prepare() throws Exception {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            HttpSession httpSession = httpServletRequest.getSession(false);
            try {
                if ("ARCHIVE".equals(getDatabase())) {
                    setDatabase("ARCHIVE");
                } else {
                    setDatabase("MSCVP");
                }
                ServiceLocator.getLifeCycleService().buildLifeCycleBeans(this, httpServletRequest);
                resultType = SUCCESS;
            } catch (Exception ex) {
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, ex.getMessage());
                resultType = "error";
            }
        }
        return resultType;
    }

    //Life Cycle for logistics
    public String ltPrepare() throws Exception {
        resultType = LOGIN;
        if (httpServletRequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME) != null) {
            HttpSession httpSession = httpServletRequest.getSession(false);
            try {
                if ("ARCHIVE".equals(getDatabase())) {
                    setDatabase("ARCHIVE");
                } else {
                    setDatabase("MSCVP");
                }
                ServiceLocator.getLifeCycleService().buildLtLifeCycleBeans(this, httpServletRequest);
                resultType = SUCCESS;
            } catch (Exception ex) {
                httpServletRequest.getSession(false).setAttribute(AppConstants.REQ_EXCEPTION_MSG, ex.getMessage());
                resultType = "error";
            }
        }
        return resultType;
    }

    public void setServletRequest(HttpServletRequest reqObj) {
        this.setHttpServletRequest(reqObj);
    }

    /**
     * @param reqObj
     */
    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }

    public void setSqlQuery(String sqlQuery) {
        this.sqlQuery = sqlQuery;
    }

    public String getSqlQuery() {
        return sqlQuery;
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
     * @return the lifeCycleBeans
     */
    public LifecycleBeans getLifeCycleBeans() {
        return lifeCycleBeans;
    }

    /**
     * @param lifeCycleBeans the lifeCycleBeans to set
     */
    public void setLifeCycleBeans(LifecycleBeans lifeCycleBeans) {
        this.lifeCycleBeans = lifeCycleBeans;
    }

    /**
     * @return the poLifecycleBean
     */
    public PoLifecycleBean getPoLifecycleBean() {
        return poLifecycleBean;
    }

    /**
     * @param poLifecycleBean the poLifecycleBean to set
     */
    public void setPoLifecycleBean(PoLifecycleBean poLifecycleBean) {
        this.poLifecycleBean = poLifecycleBean;
    }

    public String getShipmentNumber() {
        return shipmentNumber;
    }

    public void setShipmentNumber(String shipmentNumber) {
        this.shipmentNumber = shipmentNumber;
    }

    public String getDatabase() {
        return database;
    }

    public void setDatabase(String database) {
        this.database = database;
    }

}
