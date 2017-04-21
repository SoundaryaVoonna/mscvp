/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.utilities;

import com.mss.ediscv.util.ServiceLocatorException;
import java.util.List;

/**
 *
 * @author miracle
 */
public interface CertMonitorService {

    public List getCertMonitorData(String certType,String dateFrom,String dateTo) throws ServiceLocatorException;
    
    public List doCodeListItems(String selectedName) throws ServiceLocatorException;
    
    public List getCodeListNames(String name) throws ServiceLocatorException;

    public String addCodeList(String jsonData, String userName) throws ServiceLocatorException;
    
    public String updateCodeList(String listName,String jsonData,String userName,int items) throws ServiceLocatorException;

    public String deleteCodeList(String jsonData) throws ServiceLocatorException;
}