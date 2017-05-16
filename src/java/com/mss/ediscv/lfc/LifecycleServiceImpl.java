/**
 *
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
package com.mss.ediscv.lfc;

import com.mss.ediscv.util.AppConstants;
import com.mss.ediscv.util.LifecycleUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import java.sql.*;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import javax.servlet.http.HttpServletRequest;

public class LifecycleServiceImpl implements LifecycleService {

    Connection connection = null;
    Statement statement = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    CallableStatement callableStatement = null;
    private LifecycleBeans lifecycleBeans;
    private ArrayList<PoLifecycleBean> poLifecycleBeanList;
    private ArrayList<AsnLifecycleBean> asnLifecycleBeanList;
    private ArrayList<InvoiceLifecycleBean> invoiceLifecycleBeanList;
    private ArrayList<PaymentLifecycleBean> PaymentLifecycleBeanList;
    private ArrayList<LtTenderBean> loadTenderBeanList;
    private ArrayList<LtTenderBean> ltshipLifecycleBeanList;
    private ArrayList<LtTenderBean> ltInvoiceLifecycleBeanList;
    private ArrayList<LtTenderBean> ltResponseLifecycleBeanList;
    private static Logger logger = Logger.getLogger(com.mss.ediscv.shipment.ShipmentServiceImpl.class.getName());

    //@Override
    public void buildLifeCycleBeans(LifecycleAction lifecycleAction, HttpServletRequest httpServletRequest) throws ServiceLocatorException {
        lifecycleBeans = new LifecycleBeans();
        String Ponum = lifecycleAction.getPoNumber();
        String database = lifecycleAction.getDatabase();
        LifecycleUtility lifecycleUtility = new LifecycleUtility();
        poLifecycleBeanList = lifecycleUtility.addPoLifeCycleBean(Ponum, database);
        httpServletRequest.getSession(false).setAttribute(AppConstants.LFC_SES_PO_LIST, poLifecycleBeanList);
        /**
         * ASN process*
         */
        asnLifecycleBeanList = lifecycleUtility.addAsnLifecycleBean(Ponum, database);
        httpServletRequest.getSession(false).setAttribute(AppConstants.LFC_SES_ASN_LIST, asnLifecycleBeanList);
        /**
         * INVOICE *
         */
        invoiceLifecycleBeanList = lifecycleUtility.addInvoiceLifecycleBean(Ponum, database);
        httpServletRequest.getSession(false).setAttribute(AppConstants.LFC_SES_INVOICE_LIST, invoiceLifecycleBeanList);
        /**
         * PAYMENT *
         */
        PaymentLifecycleBeanList = lifecycleUtility.addPaymentLifecycleBean(Ponum, database);
        httpServletRequest.getSession(false).setAttribute(AppConstants.LFC_SES_PAYMENT_LIST, PaymentLifecycleBeanList);
    }

    //Life Cycle for Logistics
    public void buildLtLifeCycleBeans(LifecycleAction lifecycleAction, HttpServletRequest httpServletRequest) throws ServiceLocatorException {
        lifecycleBeans = new LifecycleBeans();
        String shipmentNum = lifecycleAction.getShipmentNumber();
         String database = lifecycleAction.getDatabase();
        LifecycleUtility lifecycleUtility = new LifecycleUtility();
        loadTenderBeanList = lifecycleUtility.getLtLoadtender(shipmentNum, database);
        // httpServletRequest.getSession(false).setAttribute(AppConstants.LFC_SES_PO_LIST, loadTenderBeanList);
        /**
         * ASN process*
         */
        ltshipLifecycleBeanList = lifecycleUtility.getLtShipment(shipmentNum, database);
        //httpServletRequest.getSession(false).setAttribute(AppConstants.LFC_SES_ASN_LIST, ltshipLifecycleBeanList);
        /**
         * INVOICE *
         */
        ltInvoiceLifecycleBeanList = lifecycleUtility.getLtInvoice(shipmentNum, database);
        // httpServletRequest.getSession(false).setAttribute(AppConstants.LFC_SES_INVOICE_LIST, ltInvoiceLifecycleBeanList);
        /**
         * PAYMENT *
         */
        ltResponseLifecycleBeanList = lifecycleUtility.getLtResponse(shipmentNum, database);
        // httpServletRequest.getSession(false).setAttribute(AppConstants.LFC_SES_PAYMENT_LIST, ltResponseLifecycleBeanList);

        ArrayList LfcList = new ArrayList();
        LfcList.addAll(loadTenderBeanList);
        LfcList.addAll(ltshipLifecycleBeanList);
        LfcList.addAll(ltInvoiceLifecycleBeanList);
        LfcList.addAll(ltResponseLifecycleBeanList);
        System.out.println("impl LfcList" + LfcList.size());

        //Collections.sort(LfcList,new DateTimeComparator()); 
        //  LfcList.get
        httpServletRequest.getSession(false).setAttribute(AppConstants.LFC_SES_LTTENDER_LIST, LfcList);

    }
}
