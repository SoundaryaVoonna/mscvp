
/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.utilities;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.ServiceLocator;
import com.mss.ediscv.util.ServiceLocatorException;
import com.opensymphony.xwork2.ActionSupport;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.catalina.core.ApplicationContext;
import org.apache.struts2.interceptor.ParameterAware;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.apache.struts2.interceptor.ServletResponseAware;

/**
 *
 * @author miracle
 */
public class CertMonitorAction extends ActionSupport implements ServletRequestAware, ServletResponseAware {

    private String certType;
    HttpServletRequest hsrequest;
    HttpServletResponse hsresponse;
    private String docdatepickerfrom;
    private String docdatepicker;
    private String reportrange;

    private String listName;
    private String name;
    private String selectedName;
    private List listNameMap;
    private String json;
    private int items;
    private String modifieddate;

    public void setServletRequest(HttpServletRequest hsrequest) {
        this.hsrequest = hsrequest;
    }

    public HttpServletRequest getServletRequest() {
        return hsrequest;
    }

    public void setServletResponse(HttpServletResponse hsresponse) {

        this.hsresponse = hsresponse;
    }

    public HttpServletResponse getServletResponse() {

        return hsresponse;
    }

    public String getCertType() {
        return certType;
    }

    public void setCertType(String certType) {
        this.certType = certType;
    }

    public String getDocdatepickerfrom() {
        return docdatepickerfrom;
    }

    public void setDocdatepickerfrom(String docdatepickerfrom) {
        this.docdatepickerfrom = docdatepickerfrom;
    }

    public String getDocdatepicker() {
        return docdatepicker;
    }

    public void setDocdatepicker(String docdatepicker) {
        this.docdatepicker = docdatepicker;
    }

    public String getReportrange() {
        return reportrange;
    }

    public void setReportrange(String reportrange) {
        this.reportrange = reportrange;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelectedName() {
        return selectedName;
    }

    public void setSelectedName(String selectedName) {
        this.selectedName = selectedName;
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

    public String getCertMonitor() throws Exception {
        String resultType = LOGIN;

        String cert = "SYSTEM";
        List list = ServiceLocator.getCertMonitorService().getCertMonitorData(getCertType(), getDocdatepickerfrom(), getDocdatepicker());
        hsrequest.getSession(false).setAttribute(AppConstants.CERTMONITOR_LIST, list);
        resultType = SUCCESS;
        return resultType;

    }

    public String codeList() throws ServiceLocatorException {
        String resultType = LOGIN;
        if (hsrequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME).toString() != null) {
            hsrequest.getSession(false).removeAttribute(AppConstants.CODE_LIST);
            setListNameMap(DataSourceDataProvider.getInstance().getListName());
            resultType = SUCCESS;
        }
        return resultType;
    }

    public String getCodeListItems() throws Exception {
        System.out.println("11111");
        String resultType = LOGIN;
        if (hsrequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME).toString() != null) {

            try {
                hsrequest.getSession(false).removeAttribute(AppConstants.CODE_LIST);
                Date d = new Date();
                SimpleDateFormat sd = new SimpleDateFormat("MM/dd/yyyy");
                setListNameMap(DataSourceDataProvider.getInstance().getListName());
                List codeList = new ArrayList();
                codeList = ServiceLocator.getCertMonitorService().doCodeListItems(getListName());
                hsrequest.getSession(false).setAttribute(AppConstants.CODE_LIST, codeList);
                setItems(codeList.size());
                if (codeList.size() == 0) {
                    setSelectedName("");
                    setModifieddate("");
                } else {
                    setModifieddate(sd.format(d));
                }
                resultType = SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultType;
    }
    // method to get the code list according to the given name

    public String getCodeListName() throws Exception {
        String resultType = LOGIN;
        if (hsrequest.getSession(false).getAttribute(AppConstants.SES_USER_NAME).toString() != null) {
            try {
                hsrequest.getSession(false).removeAttribute(AppConstants.CODE_LIST);
                String resultMessage = "";
                List codeList = new ArrayList();

                setListNameMap(ServiceLocator.getCertMonitorService().getCodeListNames(getName()));
                resultType = SUCCESS;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return resultType;
    }

}
