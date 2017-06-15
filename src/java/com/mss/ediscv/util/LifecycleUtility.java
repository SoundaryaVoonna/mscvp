/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.util;

import com.mss.ediscv.lfc.PoLifecycleBean;
import com.mss.ediscv.lfc.AsnLifecycleBean;
import com.mss.ediscv.lfc.InvoiceLifecycleBean;
import com.mss.ediscv.lfc.LtInvoicesBean;
import com.mss.ediscv.lfc.LtResponsesBean;
import com.mss.ediscv.lfc.LtShipmentsBean;
import com.mss.ediscv.lfc.LtTenderBean;
import com.mss.ediscv.lfc.PaymentLifecycleBean;
import java.sql.*;
import java.util.ArrayList;

/**
 * @author miracle
 */
public class LifecycleUtility {

    private static LifecycleUtility _instance;

    public static LifecycleUtility getInstance() {
        if (_instance == null) {
            _instance = new LifecycleUtility();
        }
        return _instance;
    }

    private PoLifecycleBean poLifecycleBean;
    private AsnLifecycleBean asnLifecycleBean;
    private InvoiceLifecycleBean invoiceLifecycleBean;
    private PaymentLifecycleBean paymentLifecycleBean;
    private ArrayList<PoLifecycleBean> poLifecycleBeanList;
    private ArrayList<AsnLifecycleBean> asnLifecycleBeanList;
    private ArrayList<InvoiceLifecycleBean> invoiceLifecycleBeanList;
    private ArrayList<PaymentLifecycleBean> PaymentLifecycleBeanList;
    private ArrayList<LtTenderBean> ltTenderBeanList;
    private ArrayList<LtTenderBean> ltResponsesBeanList;
    private ArrayList<LtTenderBean> ltShipmentBeanList;
    private ArrayList<LtTenderBean> ltInvoicesBeanList;
    Connection connection = null;
    Statement statement = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    CallableStatement callableStatement = null;
    LtTenderBean ltTenderBean = null;
    LtResponsesBean ltResponsesBean = null;
    LtShipmentsBean ltShipmentBean = null;
    LtInvoicesBean ltInvoicesBean = null;
    //LtTenderBean ltTenderBean=null;

    public ArrayList<PoLifecycleBean> addPoLifeCycleBean(String poNumber, String database) throws ServiceLocatorException {
        poLifecycleBeanList = new ArrayList<PoLifecycleBean>();
        StringBuffer lifeCycleQuery = new StringBuffer();
        String poNum = poNumber;
        if ("ARCHIVE".equals(database)) {
            lifeCycleQuery.append("select DISTINCT(ARCHIVE_FILES.FILE_ID), ARCHIVE_FILES.FILE_TYPE, "
                    + "ARCHIVE_FILES.TRANSACTION_TYPE, ARCHIVE_FILES.DIRECTION,ARCHIVE_FILES.DATE_TIME_RECEIVED,ARCHIVE_FILES.STATUS, "
                    + "ARCHIVE_FILES.ACK_STATUS,ARCHIVE_FILES.REPROCESSSTATUS,ARCHIVE_PO.PO_NUMBER  "
                    + "FROM ARCHIVE_FILES LEFT OUTER JOIN ARCHIVE_PO ON "
                    + "(ARCHIVE_PO.PO_NUMBER = ARCHIVE_FILES.PRI_KEY_VAL AND ARCHIVE_PO.FILE_ID = ARCHIVE_FILES.FILE_ID) "
                    + "WHERE ARCHIVE_PO.PO_NUMBER LIKE '%" + poNum + "%'"
                    + " ORDER BY ARCHIVE_FILES.DATE_TIME_RECEIVED");
        } else {
            lifeCycleQuery.append("select DISTINCT(FILES.FILE_ID), FILES.FILE_TYPE, "
                    + "FILES.TRANSACTION_TYPE, FILES.DIRECTION,FILES.DATE_TIME_RECEIVED,FILES.STATUS, "
                    + "FILES.ACK_STATUS,FILES.REPROCESSSTATUS,PO.PO_NUMBER  "
                    + "FROM FILES LEFT OUTER JOIN PO ON "
                    + "(PO.PO_NUMBER = FILES.PRI_KEY_VAL AND PO.FILE_ID = FILES.FILE_ID) "
                    + "WHERE PO.PO_NUMBER LIKE '%" + poNum + "%'"
                    + " ORDER BY FILES.DATE_TIME_RECEIVED");
        }
        String searchQuery = lifeCycleQuery.toString();
        System.out.println("searchQuery lfc Po------>" + searchQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            while (resultSet.next()) {
                poLifecycleBean = new PoLifecycleBean();
                poLifecycleBean.setFileType(resultSet.getString("FILE_TYPE"));
                poLifecycleBean.setTranType(resultSet.getString("TRANSACTION_TYPE"));
                // poLifecycleBean.setSenderId(resultSet.getString("SENDER_ID"));
                // poLifecycleBean.setRecId(resultSet.getString("RECEIVER_ID"));
                poLifecycleBean.setDirection(resultSet.getString("DIRECTION"));
                poLifecycleBean.setDatetimeRec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                poLifecycleBean.setStatus(resultSet.getString("STATUS"));
                poLifecycleBean.setPoNumber(resultSet.getString("PO_NUMBER"));

                poLifecycleBean.setFileId(resultSet.getString("FILE_ID"));

//                poLifecycleBean.setStCtrlNum(resultSet.getString("ST_CONTROL_NUMBER"));
//                poLifecycleBean.setGsCtrlNum(resultSet.getString("GS_CONTROL_NUMBER"));
//                if (resultSet.getString("SAP_IDOC_Number") != null && !"".equals(resultSet.getString("SAP_IDOC_Number"))) {
//                    poLifecycleBean.setSapIdocNum(resultSet.getString("SAP_IDOC_NUMBER"));
//                } else {
//                    poLifecycleBean.setSapIdocNum("0");
//                }
//                poLifecycleBean.setPreFile(resultSet.getString("PRE_TRANS_FILEPATH"));
//                poLifecycleBean.setPostTranFile(resultSet.getString("POST_TRANS_FILEPATH"));
//                poLifecycleBean.setOrgFile(resultSet.getString("ORG_FILEPATH"));
//                poLifecycleBean.setAckFile(resultSet.getString("ACK_FILE_ID"));
//                if (resultSet.getString("ORDER_DATE") != null && !"".equals(resultSet.getString("ORDER_DATE"))) {
//                poLifecycleBean.setPodate(resultSet.getDate("ORDER_DATE").toString());
//                }else{
//                    poLifecycleBean.setPodate("--");
//                }
//                poLifecycleBean.setPoValue(resultSet.getString("PO_VALUE"));
//                poLifecycleBean.setPoStatus(resultSet.getString("ORDER_STATUS"));
//                if (resultSet.getString("SO_NUMBER") != null && !"".equals(resultSet.getString("SO_NUMBER"))) {
//                    poLifecycleBean.setSoNumber(resultSet.getString("SO_NUMBER"));
//                } else {
//                    poLifecycleBean.setSoNumber("0");
//                }
//                poLifecycleBean.setIteamQty(resultSet.getString("ITEM_QTY"));
                poLifecycleBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                poLifecycleBean.setRes("1");
                poLifecycleBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                poLifecycleBeanList.add(poLifecycleBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        //System.out.println("poLifecycleBeanList-----"+poLifecycleBeanList.toString());
        //System.out.println("poLifecycleBeanList-----"+poLifecycleBeanList.size());
        return poLifecycleBeanList;

    }

    public ArrayList<AsnLifecycleBean> addAsnLifecycleBean(String poNumber, String database) throws ServiceLocatorException {
        asnLifecycleBeanList = new ArrayList<AsnLifecycleBean>();
        StringBuffer lifeCycleQuery = new StringBuffer();
        String poNum = poNumber;
        if ("ARCHIVE".equals(database)) {
            lifeCycleQuery.append("select DISTINCT(ARCHIVE_FILES.FILE_ID),ARCHIVE_FILES.FILE_TYPE, ARCHIVE_FILES.TRANSACTION_TYPE, ARCHIVE_FILES.DIRECTION,"
                    + "ARCHIVE_FILES.DATE_TIME_RECEIVED,ARCHIVE_FILES.STATUS, ARCHIVE_ASN.PO_NUMBER ,ARCHIVE_FILES.ACK_STATUS,ARCHIVE_FILES.REPROCESSSTATUS "
                    + "from ARCHIVE_FILES LEFT OUTER JOIN ARCHIVE_ASN ON "
                    + "(ARCHIVE_ASN.FILE_ID=ARCHIVE_FILES.FILE_ID) WHERE ARCHIVE_ASN.PO_NUMBER LIKE '%" + poNum + "%'"
                    + " ORDER BY ARCHIVE_FILES.DATE_TIME_RECEIVED");
        } else {
            lifeCycleQuery.append("select DISTINCT(FILES.FILE_ID),FILES.FILE_TYPE, FILES.TRANSACTION_TYPE, FILES.DIRECTION,"
                    + "FILES.DATE_TIME_RECEIVED,FILES.STATUS, ASN.PO_NUMBER ,FILES.ACK_STATUS,FILES.REPROCESSSTATUS "
                    + "from FILES LEFT OUTER JOIN ASN ON "
                    + "(ASN.FILE_ID=FILES.FILE_ID) WHERE ASN.PO_NUMBER LIKE '%" + poNum + "%'"
                    + " ORDER BY FILES.DATE_TIME_RECEIVED");
        }
        String searchQuery = lifeCycleQuery.toString();
        System.out.println("searchQuery lfc ASN------   " + searchQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            while (resultSet.next()) {
                asnLifecycleBean = new AsnLifecycleBean();
                asnLifecycleBean.setFileType(resultSet.getString("FILE_TYPE"));
                asnLifecycleBean.setTranType(resultSet.getString("TRANSACTION_TYPE"));
                //  asnLifecycleBean.setSenderId(resultSet.getString("SENDER_ID"));
                // asnLifecycleBean.setRecId(resultSet.getString("RECEIVER_ID"));
                asnLifecycleBean.setDirection(resultSet.getString("DIRECTION"));
                asnLifecycleBean.setDatetimeRec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                asnLifecycleBean.setStatus(resultSet.getString("STATUS"));
                asnLifecycleBean.setPoNumber(resultSet.getString("PO_NUMBER"));
                //  asnLifecycleBean.setAsnNumber(resultSet.getString("ASN_NUMBER"));
                //  asnLifecycleBean.setPoNumber(resultSet.getString("PO_NUMBER"));
                asnLifecycleBean.setAckStatus(resultSet.getString("ACK_STATUS"));
//                asnLifecycleBean.setBolNumber(resultSet.getString("BOL_NUMBER"));
//                asnLifecycleBean.setIsaDate(resultSet.getString("ISA_DATE"));
//                asnLifecycleBean.setIsaTime(resultSet.getString("ISA_TIME"));
//                asnLifecycleBean.setIsaCtrlNum(resultSet.getString("ISA_NUMBER"));
                asnLifecycleBean.setFileId(resultSet.getString("FILE_ID"));
//                asnLifecycleBean.setStCtrlNum(resultSet.getString("ST_CONTROL_NUMBER"));
//                asnLifecycleBean.setGsCtrlNum(resultSet.getString("GS_CONTROL_NUMBER"));
//                asnLifecycleBean.setSapIdocNum("0");
//                asnLifecycleBean.setPreFile(resultSet.getString("PRE_TRANS_FILEPATH"));
//                asnLifecycleBean.setPostTranFile(resultSet.getString("POST_TRANS_FILEPATH"));
//                asnLifecycleBean.setOrgFile(resultSet.getString("ORG_FILEPATH"));
//                asnLifecycleBean.setAckFile(resultSet.getString("ACK_FILE_ID"));
//                asnLifecycleBean.setIsaCtrlNum("0");
//                asnLifecycleBean.setInvNumber("0");
//                asnLifecycleBean.setPodate("0");
//                asnLifecycleBean.setPoValue("0");
//                asnLifecycleBean.setPoStatus("0");
//                asnLifecycleBean.setSoNumber("0");
//                asnLifecycleBean.setIteamQty("0");
//                asnLifecycleBean.setInvAmt("0");
//                asnLifecycleBean.setChequeNum("0");
//                asnLifecycleBean.setInvNumber("0");
                asnLifecycleBean.setRes("1");
                asnLifecycleBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                asnLifecycleBeanList.add(asnLifecycleBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return asnLifecycleBeanList;
    }

    public ArrayList<InvoiceLifecycleBean> addInvoiceLifecycleBean(String poNumber, String database) throws ServiceLocatorException {
        invoiceLifecycleBeanList = new ArrayList<InvoiceLifecycleBean>();
        StringBuffer lifeCycleQuery = new StringBuffer();
        String poNum = poNumber;
        if ("ARCHIVE".equals(database)) {
            lifeCycleQuery.append("select DISTINCT(ARCHIVE_FILES.FILE_ID),ARCHIVE_FILES.FILE_TYPE, "
                    + "ARCHIVE_FILES.TRANSACTION_TYPE, ARCHIVE_FILES.DIRECTION,"
                    + "ARCHIVE_FILES.DATE_TIME_RECEIVED,ARCHIVE_FILES.STATUS, ARCHIVE_INVOICE.PO_NUMBER,ARCHIVE_FILES.REPROCESSSTATUS,ARCHIVE_FILES.ACK_STATUS "
                    + "from ARCHIVE_FILES LEFT OUTER JOIN "
                    + "ARCHIVE_INVOICE ON (ARCHIVE_INVOICE.FILE_ID=ARCHIVE_FILES.FILE_ID) WHERE ARCHIVE_INVOICE.PO_NUMBER LIKE '%" + poNum + "%'"
                    + " ORDER BY ARCHIVE_FILES.DATE_TIME_RECEIVED");
        } else {
            lifeCycleQuery.append("select DISTINCT(FILES.FILE_ID),FILES.FILE_TYPE, "
                    + "FILES.TRANSACTION_TYPE, FILES.DIRECTION,"
                    + "FILES.DATE_TIME_RECEIVED,FILES.STATUS, INVOICE.PO_NUMBER,FILES.REPROCESSSTATUS,FILES.ACK_STATUS "
                    + "from FILES LEFT OUTER JOIN "
                    + "INVOICE ON (INVOICE.FILE_ID=FILES.FILE_ID) WHERE INVOICE.PO_NUMBER LIKE '%" + poNum + "%'"
                    + " ORDER BY FILES.DATE_TIME_RECEIVED");
        }
        String searchQuery = lifeCycleQuery.toString();
        System.out.println("searchQuery lfc Invoice------   " + searchQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            while (resultSet.next()) {
                invoiceLifecycleBean = new InvoiceLifecycleBean();
                invoiceLifecycleBean.setFileType(resultSet.getString("FILE_TYPE"));
                invoiceLifecycleBean.setTranType(resultSet.getString("TRANSACTION_TYPE"));
//                invoiceLifecycleBean.setSenderId(resultSet.getString("SENDER_ID"));
//                invoiceLifecycleBean.setRecId(resultSet.getString("RECEIVER_ID"));
                invoiceLifecycleBean.setDirection(resultSet.getString("DIRECTION"));
                invoiceLifecycleBean.setDatetimeRec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                invoiceLifecycleBean.setStatus(resultSet.getString("STATUS"));
                invoiceLifecycleBean.setPoNumber(resultSet.getString("PO_NUMBER"));
                invoiceLifecycleBean.setFileId(resultSet.getString("FILE_ID"));
//                invoiceLifecycleBean.setStCtrlNum(resultSet.getString("ST_CONTROL_NUMBER"));
//                invoiceLifecycleBean.setGsCtrlNum(resultSet.getString("GS_CONTROL_NUMBER"));
//                invoiceLifecycleBean.setSapIdocNum("0");
//                invoiceLifecycleBean.setIsaCtrlNum(" ");
//                invoiceLifecycleBean.setAsnNumber("0");
//                invoiceLifecycleBean.setInvNumber(resultSet.getString("INVOICE_NUMBER"));
//                invoiceLifecycleBean.setPreFile(resultSet.getString("PRE_TRANS_FILEPATH"));
//                invoiceLifecycleBean.setPostTranFile(resultSet.getString("POST_TRANS_FILEPATH"));
//                invoiceLifecycleBean.setOrgFile(resultSet.getString("ORG_FILEPATH"));
//                invoiceLifecycleBean.setAckFile(resultSet.getString("ACK_FILE_ID"));
                invoiceLifecycleBean.setAckStatus(resultSet.getString("ACK_STATUS"));
//                invoiceLifecycleBean.setIsaCtrlNum("0");
//                invoiceLifecycleBean.setInvNumber("0");
//                invoiceLifecycleBean.setBolNumber("0");
//                invoiceLifecycleBean.setPodate("0");
//                invoiceLifecycleBean.setPoValue("0");
//                invoiceLifecycleBean.setPoStatus("0");
//                invoiceLifecycleBean.setSoNumber("0");
//                invoiceLifecycleBean.setIteamQty("0");
//                invoiceLifecycleBean.setIsaDate(resultSet.getString("ISA_DATE"));
//                invoiceLifecycleBean.setIsaTime(resultSet.getString("ISA_TIME"));
//                invoiceLifecycleBean.setIsaCtrlNum(resultSet.getString("ISA_NUMBER"));
//                invoiceLifecycleBean.setInvAmt(resultSet.getString("INVOICE_AMOUNT"));
                //  invoiceLifecycleBean.setChequeNum("0");
                invoiceLifecycleBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                invoiceLifecycleBean.setRes("1");
                invoiceLifecycleBeanList.add(invoiceLifecycleBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return invoiceLifecycleBeanList;
    }

    public ArrayList<PaymentLifecycleBean> addPaymentLifecycleBean(String poNumber, String database) throws ServiceLocatorException {
        PaymentLifecycleBeanList = new ArrayList<PaymentLifecycleBean>();
        StringBuffer lifeCycleQuery = new StringBuffer();
        String poNum = poNumber;
        if ("ARCHIVE".equals(database)) {
            lifeCycleQuery.append("select DISTINCT(ARCHIVE_FILES.FILE_ID),ARCHIVE_FILES.FILE_TYPE, ARCHIVE_FILES.TRANSACTION_TYPE,"
                    + "ARCHIVE_FILES.DIRECTION,ARCHIVE_FILES.DATE_TIME_RECEIVED,ARCHIVE_FILES.STATUS, "
                    + "ARCHIVE_PAYMENT.PO_NUMBER ,ARCHIVE_FILES.ACK_STATUS,ARCHIVE_FILES.REPROCESSSTATUS "
                    + "FROM ARCHIVE_FILES LEFT OUTER JOIN "
                    + " ARCHIVE_PAYMENT ON (ARCHIVE_PAYMENT.FILE_ID=ARCHIVE_FILES.FILE_ID) WHERE ARCHIVE_PAYMENT.PO_NUMBER LIKE '%" + poNum + "%'"
                    + " ORDER BY ARCHIVE_FILES.DATE_TIME_RECEIVED");
        } else {
            lifeCycleQuery.append("select DISTINCT(FILES.FILE_ID),FILES.FILE_TYPE, FILES.TRANSACTION_TYPE,"
                    + "FILES.DIRECTION,FILES.DATE_TIME_RECEIVED,FILES.STATUS, "
                    + "PAYMENT.PO_NUMBER ,FILES.ACK_STATUS,FILES.REPROCESSSTATUS "
                    + "from FILES LEFT OUTER JOIN "
                    + " PAYMENT ON (PAYMENT.FILE_ID=FILES.FILE_ID) WHERE PAYMENT.PO_NUMBER LIKE '%" + poNum + "%'"
                    + " ORDER BY FILES.DATE_TIME_RECEIVED");
        }

        String searchQuery = lifeCycleQuery.toString();
        System.out.println("searchQuery PAYMENT------   " + searchQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            while (resultSet.next()) {
                paymentLifecycleBean = new PaymentLifecycleBean();
                paymentLifecycleBean.setFileType(resultSet.getString("FILE_TYPE"));
                paymentLifecycleBean.setTranType(resultSet.getString("TRANSACTION_TYPE"));
//                paymentLifecycleBean.setSenderId(resultSet.getString("SENDER_ID"));
//                paymentLifecycleBean.setRecId(resultSet.getString("RECEIVER_ID"));
                paymentLifecycleBean.setDirection(resultSet.getString("DIRECTION"));
                paymentLifecycleBean.setDatetimeRec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                paymentLifecycleBean.setStatus(resultSet.getString("STATUS"));
                paymentLifecycleBean.setPoNumber(resultSet.getString("PO_NUMBER"));
                paymentLifecycleBean.setFileId(resultSet.getString("FILE_ID"));
//                paymentLifecycleBean.setStCtrlNum(resultSet.getString("ST_CONTROL_NUMBER"));
//                paymentLifecycleBean.setGsCtrlNum(resultSet.getString("GS_CONTROL_NUMBER"));
//                paymentLifecycleBean.setSapIdocNum("0");
                paymentLifecycleBean.setAckStatus(resultSet.getString("ACK_STATUS"));
//                paymentLifecycleBean.setAsnNumber("0");
//                paymentLifecycleBean.setPreFile(resultSet.getString("PRE_TRANS_FILEPATH"));
//                paymentLifecycleBean.setPostTranFile(resultSet.getString("POST_TRANS_FILEPATH"));
//                paymentLifecycleBean.setOrgFile(resultSet.getString("ORG_FILEPATH"));
//                paymentLifecycleBean.setAckFile(resultSet.getString("ACK_FILE_ID"));
//                paymentLifecycleBean.setIsaCtrlNum("0");
//                paymentLifecycleBean.setInvNumber("0");
//                paymentLifecycleBean.setBolNumber("0");
//                paymentLifecycleBean.setPodate("0");
//                paymentLifecycleBean.setPoValue("0");
//                paymentLifecycleBean.setPoStatus("0");
//                paymentLifecycleBean.setSoNumber("0");
//                paymentLifecycleBean.setIteamQty("0");
//                paymentLifecycleBean.setIsaDate("0");
//                paymentLifecycleBean.setIsaTime("0");
//                paymentLifecycleBean.setIsaCtrlNum("0");
//                paymentLifecycleBean.setInvAmt("0");
//                paymentLifecycleBean.setChequeNum(resultSet.getString("CHECK_NUMBER"));
                paymentLifecycleBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                paymentLifecycleBean.setRes("1");
                PaymentLifecycleBeanList.add(paymentLifecycleBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return PaymentLifecycleBeanList;
    }

    /**
     * Detail info
     *
     */
    public PoLifecycleBean getLFCPoDetails(String poNumber, String fileID, String database) throws ServiceLocatorException {
        poLifecycleBean = new PoLifecycleBean();
        StringBuffer lifeCycleQuery = new StringBuffer();
        String poNum = poNumber;
        if ("ARCHIVE".equals(database)) {
            lifeCycleQuery.append("select ARCHIVE_FILES.FILE_ID, ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.TRANSACTION_TYPE, ARCHIVE_FILES.DIRECTION,ARCHIVE_FILES.DATE_TIME_RECEIVED, "
                    + "ARCHIVE_FILES.ST_CONTROL_NUMBER, ARCHIVE_FILES.GS_CONTROL_NUMBER,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID, ARCHIVE_FILES.STATUS, ARCHIVE_PO.SAP_IDOC_NUMBER, "
                    + "ARCHIVE_FILES.ISA_NUMBER, ARCHIVE_FILES.ISA_DATE, ARCHIVE_FILES.ISA_TIME,ARCHIVE_PO.PO_NUMBER ,ARCHIVE_PO.ORDER_DATE,ARCHIVE_PO.PO_VALUE,ARCHIVE_PO.ORDER_STATUS,ARCHIVE_PO.SO_NUMBER, "
                    + "ARCHIVE_PO.ITEM_QTY,ARCHIVE_FILES.ACK_STATUS,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH, "
                    + "ARCHIVE_FILES.ORG_FILEPATH,ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,ARCHIVE_FILES.PRI_KEY_VAL as PRI_KEY_VAL,ARCHIVE_FILES.PRI_KEY_TYPE as PRI_KEY_TYPE FROM ARCHIVE_PO LEFT OUTER JOIN ARCHIVE_FILES ON "
                    + "(ARCHIVE_PO.PO_NUMBER = ARCHIVE_FILES.PRI_KEY_VAL AND ARCHIVE_PO.FILE_ID = ARCHIVE_FILES.FILE_ID) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=ARCHIVE_FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = ARCHIVE_FILES.RECEIVER_ID) "
                    + "WHERE FLOWFLAG like 'M' AND ARCHIVE_PO.PO_NUMBER LIKE '%" + poNum + "%' AND ARCHIVE_FILES.FILE_ID LIKE '%" + fileID + "%'"
                    + " ORDER BY ARCHIVE_FILES.DATE_TIME_RECEIVED");
        } else {
            lifeCycleQuery.append("select FILES.FILE_ID, FILES.FILE_TYPE,FILES.TRANSACTION_TYPE, FILES.DIRECTION,FILES.DATE_TIME_RECEIVED, "
                    + "FILES.GS_CONTROL_NUMBER,FILES.SENDER_ID,FILES.RECEIVER_ID, FILES.STATUS, PO.SAP_IDOC_NUMBER, "
                    + "FILES.ISA_NUMBER, FILES.ISA_DATE, FILES.ISA_TIME,PO.PO_NUMBER ,PO.ORDER_DATE,PO.PO_VALUE,PO.ORDER_STATUS,PO.SO_NUMBER, "
                    + "PO.ITEM_QTY,FILES.ACK_STATUS,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH, "
                    + "FILES.ORG_FILEPATH,FILES.ACK_FILE_ID as ACK_FILE_ID,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,FILES.PRI_KEY_VAL as PRI_KEY_VAL,FILES.PRI_KEY_TYPE as PRI_KEY_TYPE FROM PO LEFT OUTER JOIN FILES ON "
                    + "(PO.PO_NUMBER = FILES.PRI_KEY_VAL AND PO.FILE_ID = FILES.FILE_ID) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = FILES.RECEIVER_ID) "
                    + "WHERE FLOWFLAG like 'M' AND PO.PO_NUMBER LIKE '%" + poNum + "%' AND FILES.FILE_ID LIKE '%" + fileID + "%'"
                    + " ORDER BY FILES.DATE_TIME_RECEIVED");
        }
        String searchQuery = lifeCycleQuery.toString();
        System.out.println("lfc po query-->" + searchQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            while (resultSet.next()) {
                System.out.println("FILE_TYPE-->" + resultSet.getString("FILE_TYPE"));
                if ((resultSet.getString("FILE_TYPE") != null) && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    poLifecycleBean.setFileType(resultSet.getString("FILE_TYPE"));
                } else {
                    poLifecycleBean.setFileType("--");
                }
                System.out.println("TRANSACTION_TYPE-->" + resultSet.getString("TRANSACTION_TYPE"));
                if ((resultSet.getString("TRANSACTION_TYPE") != null) && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    poLifecycleBean.setTranType(resultSet.getString("TRANSACTION_TYPE"));
                } else {
                    poLifecycleBean.setTranType("--");
                }
                System.out.println("SENDER_ID-->" + resultSet.getString("SENDER_ID"));
                if ((resultSet.getString("SENDER_ID") != null) && !"".equals(resultSet.getString("SENDER_ID"))) {
                    poLifecycleBean.setSenderId(resultSet.getString("SENDER_ID"));
                } else {
                    poLifecycleBean.setSenderId("--");
                }
                System.out.println("RECEIVER_ID-->" + resultSet.getString("RECEIVER_ID"));
                if ((resultSet.getString("RECEIVER_ID") != null) && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    poLifecycleBean.setRecId(resultSet.getString("RECEIVER_ID"));
                } else {
                    poLifecycleBean.setRecId("--");
                }
                System.out.println("DIRECTION-->" + resultSet.getString("DIRECTION"));
                if ((resultSet.getString("DIRECTION") != null) && !"".equals(resultSet.getString("DIRECTION"))) {
                    poLifecycleBean.setDirection(resultSet.getString("DIRECTION"));
                } else {
                    poLifecycleBean.setDirection("--");
                }
                System.out.println("DATE_TIME_RECEIVED-->" + resultSet.getString("DATE_TIME_RECEIVED"));
                poLifecycleBean.setDatetimeRec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                System.out.println("STATUS-->" + resultSet.getString("STATUS"));
                if ((resultSet.getString("STATUS") != null) && !"".equals(resultSet.getString("STATUS"))) {
                    poLifecycleBean.setStatus(resultSet.getString("STATUS"));
                } else {
                    poLifecycleBean.setStatus("--");
                }
                System.out.println("PO_NUMBER-->" + resultSet.getString("PO_NUMBER"));
                if ((resultSet.getString("PO_NUMBER") != null) && !"".equals(resultSet.getString("PO_NUMBER"))) {
                    poLifecycleBean.setPoNumber(resultSet.getString("PO_NUMBER"));
                } else {
                    poLifecycleBean.setPoNumber("--");
                }
                System.out.println("SENDER_NAME-->" + resultSet.getString("SENDER_NAME"));
                if ((resultSet.getString("SENDER_NAME") != null) && !"".equals(resultSet.getString("SENDER_NAME"))) {
                    poLifecycleBean.setSenName(resultSet.getString("SENDER_NAME"));
                } else {
                    poLifecycleBean.setSenName("--");
                }
                System.out.println("RECEIVER_NAME-->" + resultSet.getString("RECEIVER_NAME"));
                if ((resultSet.getString("RECEIVER_NAME") != null) && !"".equals(resultSet.getString("RECEIVER_NAME"))) {
                    poLifecycleBean.setRecName(resultSet.getString("RECEIVER_NAME"));
                } else {
                    poLifecycleBean.setRecName("--");
                }
                System.out.println("FILE_ID-->" + resultSet.getString("FILE_ID"));
                poLifecycleBean.setFileId(resultSet.getString("FILE_ID"));
                System.out.println("SAP_IDOC_NUMBER-->" + resultSet.getString("SAP_IDOC_NUMBER"));
                if (resultSet.getString("SAP_IDOC_NUMBER") != null && !"".equals(resultSet.getString("SAP_IDOC_NUMBER"))) {
                    poLifecycleBean.setSapIdocNum(resultSet.getString("SAP_IDOC_NUMBER"));
                } else {
                    poLifecycleBean.setSapIdocNum("0");
                }
                System.out.println("PRE_TRANS_FILEPATH-->" + resultSet.getString("PRE_TRANS_FILEPATH"));
                if ((resultSet.getString("PRE_TRANS_FILEPATH") != null) && !"".equals(resultSet.getString("PRE_TRANS_FILEPATH"))) {
                    poLifecycleBean.setPreFile(resultSet.getString("PRE_TRANS_FILEPATH"));
                } else {
                    poLifecycleBean.setPreFile("--");
                }
                System.out.println("POST_TRANS_FILEPATH-->" + resultSet.getString("POST_TRANS_FILEPATH"));
                if ((resultSet.getString("POST_TRANS_FILEPATH") != null) && !"".equals(resultSet.getString("POST_TRANS_FILEPATH"))) {
                    poLifecycleBean.setPostTranFile(resultSet.getString("POST_TRANS_FILEPATH"));
                } else {
                    poLifecycleBean.setPostTranFile("--");
                }
                System.out.println("ORG_FILEPATH-->" + resultSet.getString("ORG_FILEPATH"));
                if ((resultSet.getString("ORG_FILEPATH") != null) && !"".equals(resultSet.getString("ORG_FILEPATH"))) {
                    poLifecycleBean.setOrgFile(resultSet.getString("ORG_FILEPATH"));
                } else {
                    poLifecycleBean.setOrgFile("--");
                }
                System.out.println("ACK_FILE_ID-->" + resultSet.getString("ACK_FILE_ID"));
                if ((resultSet.getString("ACK_FILE_ID") != null) && !"".equals(resultSet.getString("ACK_FILE_ID"))) {
                    poLifecycleBean.setAckFile(resultSet.getString("ACK_FILE_ID"));
                } else {
                    poLifecycleBean.setAckFile("--");
                }
                System.out.println("ORDER_DATE-->" + resultSet.getString("ORDER_DATE"));
                if ((resultSet.getString("ORDER_DATE") != null) && !"".equals(resultSet.getString("ORDER_DATE"))) {
                    poLifecycleBean.setPodate(resultSet.getDate("ORDER_DATE").toString());
                } else {
                    poLifecycleBean.setPodate("--");
                }
                System.out.println("PO_VALUE-->" + resultSet.getString("PO_VALUE"));
                if ((resultSet.getString("PO_VALUE") != null) && !"".equals(resultSet.getString("PO_VALUE"))) {
                    poLifecycleBean.setPoValue(resultSet.getString("PO_VALUE"));
                } else {
                    poLifecycleBean.setPoValue("--");
                }
                System.out.println("ORDER_STATUS-->" + resultSet.getString("ORDER_STATUS"));
                if ((resultSet.getString("ORDER_STATUS") != null) && !"".equals(resultSet.getString("ORDER_STATUS"))) {
                    poLifecycleBean.setPoStatus(resultSet.getString("ORDER_STATUS"));
                } else {
                    poLifecycleBean.setPoStatus("--");
                }
                System.out.println("SO_NUMBER-->" + resultSet.getString("SO_NUMBER"));
                if (resultSet.getString("SO_NUMBER") != null && !"".equals(resultSet.getString("SO_NUMBER"))) {
                    poLifecycleBean.setSoNumber(resultSet.getString("SO_NUMBER"));
                } else {
                    poLifecycleBean.setSoNumber("0");
                }
                System.out.println("ITEM_QTY-->" + resultSet.getString("ITEM_QTY"));
                if ((resultSet.getString("ITEM_QTY") != null) && !"".equals(resultSet.getString("ITEM_QTY"))) {
                    poLifecycleBean.setIteamQty(resultSet.getString("ITEM_QTY"));
                } else {
                    poLifecycleBean.setIteamQty("--");
                }
                System.out.println("ACK_STATUS-->" + resultSet.getString("ACK_STATUS"));
                if ((resultSet.getString("ACK_STATUS") != null) && !"".equals(resultSet.getString("ACK_STATUS"))) {
                    poLifecycleBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                } else {
                    poLifecycleBean.setAckStatus("--");
                }
                System.out.println("ISA_DATE-->" + resultSet.getString("ISA_DATE"));
                if ((resultSet.getString("ISA_DATE") != null) && !"".equals(resultSet.getString("ISA_DATE"))) {
                    poLifecycleBean.setIsaDate(resultSet.getString("ISA_DATE"));
                } else {
                    poLifecycleBean.setIsaDate("--");
                }
                System.out.println("ISA_TIME-->" + resultSet.getString("ISA_TIME"));
                if ((resultSet.getString("ISA_TIME") != null) && !"".equals(resultSet.getString("ISA_TIME"))) {
                    poLifecycleBean.setIsaTime(resultSet.getString("ISA_TIME"));
                } else {
                    poLifecycleBean.setIsaTime("--");
                }
                System.out.println("ISA_NUMBER-->" + resultSet.getString("ISA_NUMBER"));
                if ((resultSet.getString("ISA_NUMBER") != null) && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    poLifecycleBean.setIsaCtrlNum(resultSet.getString("ISA_NUMBER"));
                } else {
                    poLifecycleBean.setIsaCtrlNum("--");
                }
                if ((resultSet.getString("GS_CONTROL_NUMBER") != null) && !"".equals(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    poLifecycleBean.setGsCtrlNum(resultSet.getString("GS_CONTROL_NUMBER"));
                } else {
                    poLifecycleBean.setGsCtrlNum("--");
                }
                if ((resultSet.getString("ST_CONTROL_NUMBER") != null) && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    poLifecycleBean.setStCtrlNum(resultSet.getString("ST_CONTROL_NUMBER"));
                } else {
                    poLifecycleBean.setStCtrlNum("--");
                }
                if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("PO")) {
                    poLifecycleBean.setPrimary_key_type("PO");
                }
                if (resultSet.getString("PRI_KEY_VAL") != null && !"".equals(resultSet.getString("PRI_KEY_VAL"))) {
                    poLifecycleBean.setPrimary_key_val(resultSet.getString("PRI_KEY_VAL"));
                } else {
                    poLifecycleBean.setPrimary_key_val(resultSet.getString("PRI_KEY_VAL"));
                }
                poLifecycleBean.setBolNumber("0");
                poLifecycleBean.setInvAmt("0");
                poLifecycleBean.setChequeNum("0");
                poLifecycleBean.setAsnNumber("0");
                poLifecycleBean.setInvNumber("0");
                poLifecycleBean.setRes("1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return poLifecycleBean;
    }

    public AsnLifecycleBean getLFCAsnDetails(String poNumber, String fileId, String database) throws ServiceLocatorException {
        asnLifecycleBean = new AsnLifecycleBean();
        StringBuffer lifeCycleQuery = new StringBuffer();
        String poNum = poNumber;
        if ("ARCHIVE".equals(database)) {
            lifeCycleQuery.append("SELECT ARCHIVE_ASN.ASN_NUMBER,ARCHIVE_FILES.FILE_ID,ARCHIVE_FILES.FILE_TYPE, ARCHIVE_FILES.TRANSACTION_TYPE, ARCHIVE_FILES.DIRECTION,"
                    + " ARCHIVE_FILES.DATE_TIME_RECEIVED, ARCHIVE_FILES.ST_CONTROL_NUMBER,ARCHIVE_FILES.GS_CONTROL_NUMBER,ARCHIVE_FILES.SENDER_ID, ARCHIVE_FILES.RECEIVER_ID, "
                    + " ARCHIVE_FILES.STATUS, ARCHIVE_ASN.PO_NUMBER ,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,"
                    + " ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.ISA_NUMBER,ARCHIVE_FILES.ISA_DATE,ARCHIVE_FILES.ISA_TIME,ARCHIVE_ASN.BOL_NUMBER,"
                    + " ARCHIVE_FILES.ORG_FILEPATH,ARCHIVE_FILES.ACK_STATUS,ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,ARCHIVE_FILES.PRI_KEY_VAL as PRI_KEY_VAL,ARCHIVE_FILES.PRI_KEY_TYPE as PRI_KEY_TYPE "
                    + " FROM ARCHIVE_ASN LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_ASN.FILE_ID=ARCHIVE_FILES.FILE_ID) "
                    + " LEFT OUTER JOIN TP TP1 ON (TP1.ID=ARCHIVE_FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = ARCHIVE_FILES.RECEIVER_ID) "
                    + " WHERE FLOWFLAG like 'M' AND ARCHIVE_ASN.PO_NUMBER LIKE '%" + poNum + "%' AND ARCHIVE_FILES.FILE_ID LIKE '%" + fileId + "%'"
                    + " ORDER BY ARCHIVE_FILES.DATE_TIME_RECEIVED");
        } else {
            lifeCycleQuery.append("select ASN.ASN_NUMBER,FILES.FILE_ID,FILES.FILE_TYPE, FILES.TRANSACTION_TYPE, FILES.DIRECTION,"
                    + "FILES.DATE_TIME_RECEIVED,FILES.GS_CONTROL_NUMBER,FILES.SENDER_ID, FILES.RECEIVER_ID, "
                    + "FILES.STATUS, ASN.PO_NUMBER ,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,"
                    + " FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.ISA_NUMBER,FILES.ISA_DATE,FILES.ISA_TIME,ASN.BOL_NUMBER,"
                    + "FILES.ORG_FILEPATH,FILES.ACK_STATUS,FILES.ACK_FILE_ID as ACK_FILE_ID,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,FILES.PRI_KEY_VAL as PRI_KEY_VAL,FILES.PRI_KEY_TYPE as PRI_KEY_TYPE "
                    + "from ASN LEFT OUTER JOIN FILES ON (ASN.FILE_ID=FILES.FILE_ID) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = FILES.RECEIVER_ID) "
                    + "WHERE FLOWFLAG like 'M' AND ASN.PO_NUMBER LIKE '%" + poNum + "%' AND FILES.FILE_ID LIKE '%" + fileId + "%'"
                    + " ORDER BY FILES.DATE_TIME_RECEIVED");
        }
        String searchQuery = lifeCycleQuery.toString();
        System.out.println("lfc asn query-->" + searchQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            while (resultSet.next()) {
                asnLifecycleBean.setFileType(resultSet.getString("FILE_TYPE"));
                asnLifecycleBean.setTranType(resultSet.getString("TRANSACTION_TYPE"));
                asnLifecycleBean.setSenderId(resultSet.getString("SENDER_ID"));
                asnLifecycleBean.setRecId(resultSet.getString("RECEIVER_ID"));
                asnLifecycleBean.setDirection(resultSet.getString("DIRECTION"));
                asnLifecycleBean.setDatetimeRec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                asnLifecycleBean.setStatus(resultSet.getString("STATUS"));
                asnLifecycleBean.setPoNumber(resultSet.getString("PO_NUMBER"));
                asnLifecycleBean.setAsnNumber(resultSet.getString("ASN_NUMBER"));
                asnLifecycleBean.setPoNumber(resultSet.getString("PO_NUMBER"));
                asnLifecycleBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                asnLifecycleBean.setBolNumber(resultSet.getString("BOL_NUMBER"));
                asnLifecycleBean.setIsaDate(resultSet.getString("ISA_DATE"));
                asnLifecycleBean.setIsaTime(resultSet.getString("ISA_TIME"));
                asnLifecycleBean.setIsaCtrlNum(resultSet.getString("ISA_NUMBER"));
                asnLifecycleBean.setFileId(resultSet.getString("FILE_ID"));
                asnLifecycleBean.setSapIdocNum("0");
                asnLifecycleBean.setPreFile(resultSet.getString("PRE_TRANS_FILEPATH"));
                asnLifecycleBean.setPostTranFile(resultSet.getString("POST_TRANS_FILEPATH"));
                asnLifecycleBean.setOrgFile(resultSet.getString("ORG_FILEPATH"));
                asnLifecycleBean.setAckFile(resultSet.getString("ACK_FILE_ID"));
                asnLifecycleBean.setIsaCtrlNum(resultSet.getString("ISA_NUMBER"));
                asnLifecycleBean.setInvNumber("0");
                asnLifecycleBean.setSenName(resultSet.getString("SENDER_NAME"));
                asnLifecycleBean.setRecName(resultSet.getString("RECEIVER_NAME"));
                asnLifecycleBean.setGsCtrlNum(resultSet.getString("GS_CONTROL_NUMBER"));
                asnLifecycleBean.setStCtrlNum(resultSet.getString("ST_CONTROL_NUMBER"));
                if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("ASN")) {
                    asnLifecycleBean.setPrimary_key_type("ASN");
                }
                asnLifecycleBean.setPrimary_key_val(resultSet.getString("PRI_KEY_VAL"));
                asnLifecycleBean.setPodate("0");
                asnLifecycleBean.setPoValue("0");
                asnLifecycleBean.setPoStatus("0");
                asnLifecycleBean.setSoNumber("0");
                asnLifecycleBean.setIteamQty("0");
                asnLifecycleBean.setInvAmt("0");
                asnLifecycleBean.setChequeNum("0");
                asnLifecycleBean.setInvNumber("0");
                asnLifecycleBean.setRes("1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return asnLifecycleBean;
    }

    public InvoiceLifecycleBean getLFCInvoiceDetails(String poNumber, String fileId, String database) throws ServiceLocatorException {
        invoiceLifecycleBean = new InvoiceLifecycleBean();
        StringBuffer lifeCycleQuery = new StringBuffer();
        String poNum = poNumber;
        if ("ARCHIVE".equals(database)) {
            lifeCycleQuery.append("select ARCHIVE_INVOICE.INVOICE_NUMBER,ARCHIVE_FILES.FILE_ID, ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.TRANSACTION_TYPE, ARCHIVE_FILES.DIRECTION,"
                    + " ARCHIVE_FILES.DATE_TIME_RECEIVED, ARCHIVE_FILES.ST_CONTROL_NUMBER, ARCHIVE_FILES.GS_CONTROL_NUMBER,ARCHIVE_FILES.SENDER_ID, ARCHIVE_FILES.RECEIVER_ID, "
                    + " ARCHIVE_FILES.STATUS, ARCHIVE_INVOICE.PO_NUMBER,ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.ORG_FILEPATH,ARCHIVE_INVOICE.INVOICE_AMOUNT,"
                    + " ARCHIVE_INVOICE.INVOICE_DATE,ARCHIVE_FILES.ISA_NUMBER, ARCHIVE_FILES.ISA_DATE,ARCHIVE_FILES.ISA_TIME,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,"
                    + " ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,ARCHIVE_FILES.ACK_STATUS,ARCHIVE_FILES.PRI_KEY_VAL as PRI_KEY_VAL,ARCHIVE_FILES.PRI_KEY_TYPE as PRI_KEY_TYPE from ARCHIVE_INVOICE LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_INVOICE.FILE_ID=ARCHIVE_FILES.FILE_ID) "
                    + " LEFT OUTER JOIN TP TP1 ON (TP1.ID=ARCHIVE_FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = ARCHIVE_FILES.RECEIVER_ID) "
                    + " WHERE FLOWFLAG like 'M' AND ARCHIVE_INVOICE.PO_NUMBER LIKE '%" + poNum + "%' AND ARCHIVE_FILES.FILE_ID LIKE '%" + fileId + "%'"
                    + " ORDER BY ARCHIVE_FILES.DATE_TIME_RECEIVED");
        } else {
            lifeCycleQuery.append("select INVOICE.INVOICE_NUMBER,FILES.FILE_ID, FILES.FILE_TYPE,FILES.TRANSACTION_TYPE, FILES.DIRECTION,"
                    + "FILES.DATE_TIME_RECEIVED,FILES.GS_CONTROL_NUMBER,FILES.SENDER_ID, FILES.RECEIVER_ID, "
                    + "FILES.STATUS, INVOICE.PO_NUMBER,FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.ORG_FILEPATH,INVOICE.INVOICE_AMOUNT,"
                    + "INVOICE.INVOICE_DATE,FILES.ISA_NUMBER, FILES.ISA_DATE,FILES.ISA_TIME,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,"
                    + "FILES.ACK_FILE_ID as ACK_FILE_ID,FILES.ACK_STATUS,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,FILES.PRI_KEY_VAL as PRI_KEY_VAL,FILES.PRI_KEY_TYPE as PRI_KEY_TYPE from INVOICE LEFT OUTER JOIN FILES ON (INVOICE.FILE_ID=FILES.FILE_ID) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = FILES.RECEIVER_ID) "
                    + "WHERE FLOWFLAG like 'M' AND INVOICE.PO_NUMBER LIKE '%" + poNum + "%' AND FILES.FILE_ID LIKE '%" + fileId + "%'"
                    + " ORDER BY FILES.DATE_TIME_RECEIVED");
        }
        String searchQuery = lifeCycleQuery.toString();
        System.out.println("lfc invoice query-->" + searchQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            while (resultSet.next()) {
                invoiceLifecycleBean.setFileType(resultSet.getString("FILE_TYPE"));
                invoiceLifecycleBean.setTranType(resultSet.getString("TRANSACTION_TYPE"));
                invoiceLifecycleBean.setSenderId(resultSet.getString("SENDER_ID"));
                invoiceLifecycleBean.setRecId(resultSet.getString("RECEIVER_ID"));
                invoiceLifecycleBean.setDirection(resultSet.getString("DIRECTION"));
                invoiceLifecycleBean.setDatetimeRec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                invoiceLifecycleBean.setStatus(resultSet.getString("STATUS"));
                invoiceLifecycleBean.setPoNumber(resultSet.getString("PO_NUMBER"));
                invoiceLifecycleBean.setFileId(resultSet.getString("FILE_ID"));
                invoiceLifecycleBean.setSapIdocNum("0");
                invoiceLifecycleBean.setIsaCtrlNum("0");
                invoiceLifecycleBean.setAsnNumber("0");
                invoiceLifecycleBean.setInvNumber(resultSet.getString("INVOICE_NUMBER"));
                invoiceLifecycleBean.setPreFile(resultSet.getString("PRE_TRANS_FILEPATH"));
                invoiceLifecycleBean.setPostTranFile(resultSet.getString("POST_TRANS_FILEPATH"));
                invoiceLifecycleBean.setOrgFile(resultSet.getString("ORG_FILEPATH"));
                invoiceLifecycleBean.setAckFile(resultSet.getString("ACK_FILE_ID"));
                invoiceLifecycleBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                invoiceLifecycleBean.setIsaCtrlNum("0");
                invoiceLifecycleBean.setInvNumber("0");
                invoiceLifecycleBean.setBolNumber("0");
                invoiceLifecycleBean.setSenName(resultSet.getString("SENDER_NAME"));
                invoiceLifecycleBean.setRecName(resultSet.getString("RECEIVER_NAME"));
                invoiceLifecycleBean.setPodate("0");
                invoiceLifecycleBean.setPoValue("0");
                invoiceLifecycleBean.setPoStatus("0");
                invoiceLifecycleBean.setSoNumber("0");
                invoiceLifecycleBean.setIteamQty("0");
                invoiceLifecycleBean.setIsaDate(resultSet.getString("ISA_DATE"));
                invoiceLifecycleBean.setIsaTime(resultSet.getString("ISA_TIME"));
                invoiceLifecycleBean.setIsaCtrlNum(resultSet.getString("ISA_NUMBER"));
                invoiceLifecycleBean.setInvAmt(resultSet.getString("INVOICE_AMOUNT"));
                invoiceLifecycleBean.setGsCtrlNum(resultSet.getString("GS_CONTROL_NUMBER"));
                invoiceLifecycleBean.setStCtrlNum(resultSet.getString("ST_CONTROL_NUMBER"));
                if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("IN")) {
                    invoiceLifecycleBean.setPrimary_key_type("IN");
                }
                invoiceLifecycleBean.setPrimary_key_val(resultSet.getString("PRI_KEY_VAL"));
                invoiceLifecycleBean.setChequeNum("0");
                invoiceLifecycleBean.setRes("1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return invoiceLifecycleBean;
    }

    public PaymentLifecycleBean getLFCPaymentDetails(String poNumber, String fileId, String database) throws ServiceLocatorException {
        paymentLifecycleBean = new PaymentLifecycleBean();
        StringBuffer lifeCycleQuery = new StringBuffer();
        String poNum = poNumber;
        if ("ARCHIVE".equals(database)) {
            lifeCycleQuery.append("select ARCHIVE_FILES.FILE_ID, ARCHIVE_FILES.FILE_TYPE, ARCHIVE_FILES.TRANSACTION_TYPE,ARCHIVE_FILES.DIRECTION,ARCHIVE_FILES.DATE_TIME_RECEIVED, "
                    + "ARCHIVE_FILES.ST_CONTROL_NUMBER,ARCHIVE_FILES.GS_CONTROL_NUMBER,ARCHIVE_FILES.SENDER_ID, ARCHIVE_FILES.RECEIVER_ID, ARCHIVE_FILES.STATUS,ARCHIVE_PAYMENT.PO_NUMBER ,"
                    + "ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME,ARCHIVE_FILES.PRE_TRANS_FILEPATH,"
                    + "ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.ORG_FILEPATH,ARCHIVE_FILES.ACK_STATUS,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,"
                    + "ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID, ARCHIVE_PAYMENT.CHECK_NUMBER,ARCHIVE_FILES.PRI_KEY_VAL as PRI_KEY_VAL,ARCHIVE_FILES.PRI_KEY_TYPE as PRI_KEY_TYPE from ARCHIVE_PAYMENT LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_PAYMENT.FILE_ID=ARCHIVE_FILES.FILE_ID) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=ARCHIVE_FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = ARCHIVE_FILES.RECEIVER_ID) "
                    + " WHERE FLOWFLAG like 'M' AND ARCHIVE_PAYMENT.PO_NUMBER LIKE '%" + poNum + "%' AND ARCHIVE_FILES.FILE_ID LIKE '%" + fileId + "%'"
                    + " ORDER BY ARCHIVE_FILES.DATE_TIME_RECEIVED");
        } else {
            lifeCycleQuery.append("select FILES.FILE_ID, FILES.FILE_TYPE, FILES.TRANSACTION_TYPE,FILES.DIRECTION,FILES.DATE_TIME_RECEIVED, "
                    + "FILES.GS_CONTROL_NUMBER,FILES.SENDER_ID, FILES.RECEIVER_ID, FILES.STATUS,PAYMENT.PO_NUMBER ,"
                    + "FILES.ISA_NUMBER as ISA_NUMBER,FILES.ISA_DATE as ISA_DATE,FILES.ISA_TIME as ISA_TIME,FILES.PRE_TRANS_FILEPATH,"
                    + "FILES.POST_TRANS_FILEPATH,FILES.ORG_FILEPATH,FILES.ACK_STATUS,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,"
                    + "FILES.ACK_FILE_ID as ACK_FILE_ID, PAYMENT.CHECK_NUMBER,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,FILES.PRI_KEY_VAL as PRI_KEY_VAL,FILES.PRI_KEY_TYPE as PRI_KEY_TYPE from PAYMENT LEFT OUTER JOIN FILES ON (PAYMENT.FILE_ID=FILES.FILE_ID) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = FILES.RECEIVER_ID) "
                    + " WHERE FLOWFLAG like 'M' AND PAYMENT.PO_NUMBER LIKE '%" + poNum + "%' AND FILES.FILE_ID LIKE '%" + fileId + "%'"
                    + " ORDER BY FILES.DATE_TIME_RECEIVED");
        }
        String searchQuery = lifeCycleQuery.toString();
        System.out.println("lfc payment query-->" + searchQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            while (resultSet.next()) {
                paymentLifecycleBean.setFileType(resultSet.getString("FILE_TYPE"));
                paymentLifecycleBean.setTranType(resultSet.getString("TRANSACTION_TYPE"));
                paymentLifecycleBean.setSenderId(resultSet.getString("SENDER_ID"));
                paymentLifecycleBean.setRecId(resultSet.getString("RECEIVER_ID"));
                paymentLifecycleBean.setDirection(resultSet.getString("DIRECTION"));
                paymentLifecycleBean.setDatetimeRec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                paymentLifecycleBean.setStatus(resultSet.getString("STATUS"));
                paymentLifecycleBean.setPoNumber(resultSet.getString("PO_NUMBER"));
                paymentLifecycleBean.setFileId(resultSet.getString("FILE_ID"));
                paymentLifecycleBean.setSapIdocNum("0");
                paymentLifecycleBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                paymentLifecycleBean.setAsnNumber("0");
                paymentLifecycleBean.setSenName(resultSet.getString("SENDER_NAME"));
                paymentLifecycleBean.setRecName(resultSet.getString("RECEIVER_NAME"));
                paymentLifecycleBean.setPreFile(resultSet.getString("PRE_TRANS_FILEPATH"));
                paymentLifecycleBean.setPostTranFile(resultSet.getString("POST_TRANS_FILEPATH"));
                paymentLifecycleBean.setOrgFile(resultSet.getString("ORG_FILEPATH"));
                paymentLifecycleBean.setAckFile(resultSet.getString("ACK_FILE_ID"));
                paymentLifecycleBean.setIsaCtrlNum(resultSet.getString("ISA_NUMBER"));
                paymentLifecycleBean.setInvNumber("0");
                paymentLifecycleBean.setBolNumber("0");
                paymentLifecycleBean.setPodate("0");
                paymentLifecycleBean.setPoValue("0");
                paymentLifecycleBean.setPoStatus("0");
                paymentLifecycleBean.setSoNumber("0");
                paymentLifecycleBean.setIteamQty("0");
                paymentLifecycleBean.setIsaDate(resultSet.getString("ISA_DATE"));
                paymentLifecycleBean.setIsaTime(resultSet.getString("ISA_TIME"));
                paymentLifecycleBean.setInvAmt("0");
                paymentLifecycleBean.setChequeNum(resultSet.getString("CHECK_NUMBER"));
                paymentLifecycleBean.setGsCtrlNum(resultSet.getString("GS_CONTROL_NUMBER"));
                paymentLifecycleBean.setStCtrlNum(resultSet.getString("ST_CONTROL_NUMBER"));
                if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("PAYMENT")) {
                    paymentLifecycleBean.setPrimary_key_type("PAYMENT");
                }
                paymentLifecycleBean.setPrimary_key_val(resultSet.getString("PRI_KEY_VAL"));
                paymentLifecycleBean.setRes("1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return paymentLifecycleBean;
    }

    public ArrayList<LtTenderBean> getLtLoadtender(String shipmentNumber, String database) throws ServiceLocatorException {

        ltTenderBeanList = new ArrayList<LtTenderBean>();

        StringBuffer lifeCycleQuery = new StringBuffer();
        if ("ARCHIVE".equals(database)) {
            lifeCycleQuery.append("SELECT tf.FILE_ID as file_id,tf.ISA_NUMBER as isa_number,tl.SHIPMENT_ID as SHIPMENT_ID,"
                    + " tf.FILE_TYPE as file_type,tf.FILE_ORIGIN as file_origin,tf.TRANSACTION_TYPE as tran_type,tf.POST_TRANS_FILEPATH as file_path,"
                    + " tf.ACK_STATUS as ack_status,tf.DIRECTION as direction,tf.DATE_TIME_RECEIVED as datetime,"
                    + " tf.STATUS as status,tf.PRI_KEY_VAL as prival,tf.REPROCESSSTATUS as REPROCESSSTATUS "
                    + " FROM ARCHIVE_Transport_loadtender tl LEFT OUTER JOIN ARCHIVE_FILES TF ON "
                    + " (tl.FILE_ID=tf.FILE_ID and tl.SHIPMENT_ID=tf.PRI_KEY_VAL) "
                    + " where 1=1 and SHIPMENT_ID= '" + shipmentNumber + "' order by DATE_TIME_RECEIVED desc ");
        } else {
            lifeCycleQuery.append("SELECT tf.FILE_ID as file_id,tf.ISA_NUMBER as isa_number,tl.SHIPMENT_ID as SHIPMENT_ID,"
                    + " tf.FILE_TYPE as file_type,tf.FILE_ORIGIN as file_origin,tf.TRANSACTION_TYPE as tran_type,tf.POST_TRANS_FILEPATH as file_path,"
                    + " tf.ACK_STATUS as ack_status,tf.DIRECTION as direction,tf.DATE_TIME_RECEIVED as datetime,"
                    + " tf.STATUS as status,tf.PRI_KEY_VAL as prival,tf.REPROCESSSTATUS as REPROCESSSTATUS "
                    + " FROM Transport_loadtender tl LEFT OUTER JOIN FILES TF ON "
                    + " (tl.FILE_ID=tf.FILE_ID and tl.SHIPMENT_ID=tf.PRI_KEY_VAL) "
                    + " where 1=1 and SHIPMENT_ID= '" + shipmentNumber + "' order by DATE_TIME_RECEIVED desc ");
        }
        System.out.println("getLtLoadtender query--->" + lifeCycleQuery.toString());

        String searchQuery = lifeCycleQuery.toString();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);

            while (resultSet.next()) {
                ltTenderBean = new LtTenderBean();

                // TP2.ID , tp2.NAME as name,
                ltTenderBean.setFileId(resultSet.getString("file_id"));
                ltTenderBean.setIsaNum(resultSet.getString("isa_number"));
                ltTenderBean.setShipmentid(resultSet.getString("SHIPMENT_ID"));
                ltTenderBean.setFileType(resultSet.getString("file_type"));
                ltTenderBean.setFile_origin(resultSet.getString("file_origin"));
                ltTenderBean.setTran_type(resultSet.getString("tran_type"));
                ltTenderBean.setFile_path(resultSet.getString("file_path"));
                ltTenderBean.setAckStatus(resultSet.getString("ack_status"));

                ltTenderBean.setDirection(resultSet.getString("direction"));
                ltTenderBean.setDatetime(resultSet.getTimestamp("datetime"));
                ltTenderBean.setStatus(resultSet.getString("status"));

                ltTenderBean.setSecval(resultSet.getString("prival"));
                ltTenderBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));

                ltTenderBeanList.add(ltTenderBean);

            }
            // lifecycleBeans.setPoLifecycleBean(poLifecycleBean);

        } catch (SQLException e) {
            //System.out.println("I am in catch block coming in IMpl");
            // TODO Auto-generated catch block
            //  e.printStackTrace();
        } catch (Exception ex) {
            //System.out.println("hi"+ex.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }

        // System.out.println("Size in LFC PO Action --->"+ltTenderBeanList.size());
        return ltTenderBeanList;
    }

    public ArrayList<LtTenderBean> getLtResponse(String shipmentNumber, String database) throws ServiceLocatorException {
        ltResponsesBeanList = new ArrayList<LtTenderBean>();
        StringBuffer lifeCycleQuery = new StringBuffer();
        //String poNum = poNumber;
        // System.out.println("LifeCycleUtill ASN---->"+poNum);
        // asnLifecycleBean.setRes("0");
        if ("ARCHIVE".equals(database)) {
            lifeCycleQuery.append("SELECT tf.FILE_ID as file_id,tf.ISA_NUMBER as isa_number,tl.SHIPMENT_ID as SHIPMENT_ID,"
                    + " tf.FILE_TYPE as file_type,tf.FILE_ORIGIN as file_origin,tf.TRANSACTION_TYPE as tran_type,tf.POST_TRANS_FILEPATH"
                    + " as file_path,"
                    + " tf.ACK_STATUS as ack_status,tf.DIRECTION as direction,tf.DATE_TIME_RECEIVED as datetime,"
                    + " tf.STATUS as status,tf.PRI_KEY_VAL as prival,tf.REPROCESSSTATUS as REPROCESSSTATUS "
                    + " FROM ARCHIVE_TRANSPORT_LT_RESPONSE tl LEFT OUTER JOIN ARCHIVE_FILES TF ON"
                    + " (tl.FILE_ID=tf.FILE_ID and tl.SHIPMENT_ID=tf.PRI_KEY_VAL)"
                    + " where 1=1 and SHIPMENT_ID='" + shipmentNumber + "' order by DATE_TIME_RECEIVED desc ");
        } else {
            lifeCycleQuery.append("SELECT tf.FILE_ID as file_id,tf.ISA_NUMBER as isa_number,tl.SHIPMENT_ID as SHIPMENT_ID,"
                    + " tf.FILE_TYPE as file_type,tf.FILE_ORIGIN as file_origin,tf.TRANSACTION_TYPE as tran_type,tf.POST_TRANS_FILEPATH"
                    + " as file_path,"
                    + " tf.ACK_STATUS as ack_status,tf.DIRECTION as direction,tf.DATE_TIME_RECEIVED as datetime,"
                    + " tf.STATUS as status,tf.PRI_KEY_VAL as prival,tf.REPROCESSSTATUS as REPROCESSSTATUS "
                    + " FROM TRANSPORT_LT_RESPONSE tl LEFT OUTER JOIN FILES TF ON"
                    + " (tl.FILE_ID=tf.FILE_ID and tl.SHIPMENT_ID=tf.PRI_KEY_VAL)"
                    + " where 1=1 and SHIPMENT_ID='" + shipmentNumber + "' order by DATE_TIME_RECEIVED desc ");
        }

        System.out.println("getResponse-->" + lifeCycleQuery.toString());
        String searchQuery = lifeCycleQuery.toString();

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);

            while (resultSet.next()) {
                ltTenderBean = new LtTenderBean();

                // TP2.ID , tp2.NAME as name,
                ltTenderBean.setFileId(resultSet.getString("file_id"));
                ltTenderBean.setIsaNum(resultSet.getString("isa_number"));
                ltTenderBean.setShipmentid(resultSet.getString("SHIPMENT_ID"));
                ltTenderBean.setFileType(resultSet.getString("file_type"));
                ltTenderBean.setFile_origin(resultSet.getString("file_origin"));
                ltTenderBean.setTran_type(resultSet.getString("tran_type"));
                ltTenderBean.setFile_path(resultSet.getString("file_path"));
                ltTenderBean.setAckStatus(resultSet.getString("ack_status"));

                ltTenderBean.setDirection(resultSet.getString("direction"));
                ltTenderBean.setDatetime(resultSet.getTimestamp("datetime"));
                ltTenderBean.setStatus(resultSet.getString("status"));

                ltTenderBean.setSecval(resultSet.getString("prival"));
                ltTenderBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                ltResponsesBeanList.add(ltTenderBean);

            }
            // lifecycleBeans.setAsnLifecycleBean(asnLifecycleBean);

        } catch (SQLException e) {
            //System.out.println("I am in catch block coming in IMpl");
            // TODO Auto-generated catch block
            //  e.printStackTrace();
        } catch (Exception ex) {
            //System.out.println("hi"+ex.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        // System.out.println("Size in LFC Response Action --->"+ltResponsesBeanList.size());
        return ltResponsesBeanList;
    }

    public ArrayList<LtTenderBean> getLtShipment(String shipmentNumber, String database) throws ServiceLocatorException {
        ltShipmentBeanList = new ArrayList<LtTenderBean>();
        StringBuffer lifeCycleQuery = new StringBuffer();
        //String poNum = poNumber;
        // System.out.println("LifeCycleService impl INVOICE---->"+poNum);
        //invoiceLifecycleBean.setRes("0");
        if ("ARCHIVE".equals(database)) {
            lifeCycleQuery.append("SELECT tf.FILE_ID as file_id,tf.ISA_NUMBER as isa_number,tl.SHIPMENT_ID as SHIPMENT_ID,"
                    + " tf.FILE_TYPE as file_type,tf.FILE_ORIGIN as file_origin,tf.TRANSACTION_TYPE as tran_type,tf.POST_TRANS_FILEPATH"
                    + " as file_path,"
                    + " tf.ACK_STATUS as ack_status,tf.DIRECTION as direction,tf.DATE_TIME_RECEIVED as datetime,"
                    + " tf.STATUS as status,tf.PRI_KEY_VAL as prival,tf.REPROCESSSTATUS as REPROCESSSTATUS"
                    + " FROM ARCHIVE_TRANSPORT_SHIPMENT tl LEFT OUTER JOIN ARCHIVE_FILES TF ON "
                    + " (tl.FILE_ID=tf.FILE_ID and tl.SHIPMENT_ID=tf.PRI_KEY_VAL)"
                    + " where 1=1 and SHIPMENT_ID='" + shipmentNumber + "' order by DATE_TIME_RECEIVED desc ");
        } else {
            lifeCycleQuery.append("SELECT tf.FILE_ID as file_id,tf.ISA_NUMBER as isa_number,tl.SHIPMENT_ID as SHIPMENT_ID,"
                    + " tf.FILE_TYPE as file_type,tf.FILE_ORIGIN as file_origin,tf.TRANSACTION_TYPE as tran_type,tf.POST_TRANS_FILEPATH"
                    + " as file_path,"
                    + " tf.ACK_STATUS as ack_status,tf.DIRECTION as direction,tf.DATE_TIME_RECEIVED as datetime,"
                    + " tf.STATUS as status,tf.PRI_KEY_VAL as prival,tf.REPROCESSSTATUS as REPROCESSSTATUS"
                    + " FROM TRANSPORT_SHIPMENT tl LEFT OUTER JOIN FILES TF ON "
                    + " (tl.FILE_ID=tf.FILE_ID and tl.SHIPMENT_ID=tf.PRI_KEY_VAL)"
                    + " where 1=1 and SHIPMENT_ID='" + shipmentNumber + "' order by DATE_TIME_RECEIVED desc ");
        }

        System.out.println("getShipment Query-->" + lifeCycleQuery.toString());
        String searchQuery = lifeCycleQuery.toString();

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);

            while (resultSet.next()) {
                ltTenderBean = new LtTenderBean();

                ltTenderBean.setFileId(resultSet.getString("file_id"));
                ltTenderBean.setIsaNum(resultSet.getString("isa_number"));
                ltTenderBean.setShipmentid(resultSet.getString("SHIPMENT_ID"));
                ltTenderBean.setFileType(resultSet.getString("file_type"));
                ltTenderBean.setFile_origin(resultSet.getString("file_origin"));
                ltTenderBean.setTran_type(resultSet.getString("tran_type"));
                ltTenderBean.setFile_path(resultSet.getString("file_path"));
                ltTenderBean.setAckStatus(resultSet.getString("ack_status"));

                ltTenderBean.setDirection(resultSet.getString("direction"));
                ltTenderBean.setDatetime(resultSet.getTimestamp("datetime"));
                ltTenderBean.setStatus(resultSet.getString("status"));

                ltTenderBean.setSecval(resultSet.getString("prival"));
                ltTenderBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                ltShipmentBeanList.add(ltTenderBean);

            }
            // lifecycleBeans.setInvoiceLifecycleBean(invoiceLifecycleBean);

        } catch (SQLException e) {
            //System.out.println("I am in catch block coming in IMpl");
            // TODO Auto-generated catch block
            //  e.printStackTrace();
        } catch (Exception ex) {
            //System.out.println("hi"+ex.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        // System.out.println("Size in LFC Shipment Action --->"+ltShipmentBeanList.size());
        return ltShipmentBeanList;
    }

    public ArrayList<LtTenderBean> getLtInvoice(String shipmentNumber, String database) throws ServiceLocatorException {
        ltInvoicesBeanList = new ArrayList<LtTenderBean>();

        StringBuffer lifeCycleQuery = new StringBuffer();
        // String poNum = poNumber;
        // System.out.println("LifeCycleService impl PAYMENTS---->"+poNum);
        // paymentLifecycleBean.setRes("1");
        if ("ARCHIVE".equals(database)) {
            lifeCycleQuery.append("SELECT tf.FILE_ID as file_id,tf.ISA_NUMBER as isa_number,tl.SHIPMENT_ID as SHIPMENT_ID,"
                    + " tf.FILE_TYPE as file_type,tf.FILE_ORIGIN as file_origin,tf.TRANSACTION_TYPE as tran_type,tf.POST_TRANS_FILEPATH "
                    + " as file_path,"
                    + "  tf.ACK_STATUS as ack_status,tf.DIRECTION as direction,tf.DATE_TIME_RECEIVED as datetime,"
                    + " tf.STATUS as status,tf.PRI_KEY_VAL as prival,tf.REPROCESSSTATUS as REPROCESSSTATUS "
                    + " FROM ARCHIVE_TRANSPORT_INVOICE tl LEFT OUTER JOIN ARCHIVE_FILES TF ON"
                    + " (tl.FILE_ID=tf.FILE_ID and tl.SHIPMENT_ID=tf.PRI_KEY_VAL)  "
                    + " where 1=1 and SHIPMENT_ID='" + shipmentNumber + "' order by DATE_TIME_RECEIVED desc ");
        } else {
            lifeCycleQuery.append("SELECT tf.FILE_ID as file_id,tf.ISA_NUMBER as isa_number,tl.SHIPMENT_ID as SHIPMENT_ID,"
                    + " tf.FILE_TYPE as file_type,tf.FILE_ORIGIN as file_origin,tf.TRANSACTION_TYPE as tran_type,tf.POST_TRANS_FILEPATH "
                    + " as file_path,"
                    + "  tf.ACK_STATUS as ack_status,tf.DIRECTION as direction,tf.DATE_TIME_RECEIVED as datetime,"
                    + " tf.STATUS as status,tf.PRI_KEY_VAL as prival,tf.REPROCESSSTATUS as REPROCESSSTATUS "
                    + " FROM TRANSPORT_INVOICE tl LEFT OUTER JOIN FILES TF ON"
                    + " (tl.FILE_ID=tf.FILE_ID and tl.SHIPMENT_ID=tf.PRI_KEY_VAL)  "
                    + " where 1=1 and SHIPMENT_ID='" + shipmentNumber + "' order by DATE_TIME_RECEIVED desc ");
        }

        System.out.println("getInvoice Query-->" + lifeCycleQuery.toString());
        String searchQuery = lifeCycleQuery.toString();

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);

            while (resultSet.next()) {
                ltTenderBean = new LtTenderBean();

                ltTenderBean.setFileId(resultSet.getString("file_id"));
                ltTenderBean.setIsaNum(resultSet.getString("isa_number"));
                ltTenderBean.setShipmentid(resultSet.getString("SHIPMENT_ID"));
                ltTenderBean.setFileType(resultSet.getString("file_type"));
                ltTenderBean.setFile_origin(resultSet.getString("file_origin"));
                ltTenderBean.setTran_type(resultSet.getString("tran_type"));
                ltTenderBean.setFile_path(resultSet.getString("file_path"));
                ltTenderBean.setAckStatus(resultSet.getString("ack_status"));

                ltTenderBean.setDirection(resultSet.getString("direction"));
                ltTenderBean.setDatetime(resultSet.getTimestamp("datetime"));
                ltTenderBean.setStatus(resultSet.getString("status"));

                ltTenderBean.setSecval(resultSet.getString("prival"));
                ltTenderBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));

                ltInvoicesBeanList.add(ltTenderBean);

            }
            //lifecycleBeans.setPaymentLifecycleBean(paymentLifecycleBean);

        } catch (SQLException e) {
            //System.out.println("I am in catch block coming in IMpl");
            // TODO Auto-generated catch block
            // e.printStackTrace();
        } catch (Exception ex) {
            //System.out.println("hi"+ex.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        //System.out.println("Size in LFC Invoice Action --->"+ltInvoicesBeanList.size());
        return ltInvoicesBeanList;
    }

    public LtTenderBean getLtLoadtenderDetails(String poNumber, String fileId, String database) throws ServiceLocatorException {

        StringBuffer lifeCycleQuery = new StringBuffer();
        String poNum = poNumber;
        // System.out.println("IN getLtLoadtender-->" + poNum);
        // poLifecycleBean.setRes("0");
        if ("ARCHIVE".equals(database)) {
            lifeCycleQuery.append("SELECT ARCHIVE_ARCHIVE_FILES.REPROCESSSTATUS,ARCHIVE_TRANSPORT_LOADTENDER.SHIPMENT_ID,ARCHIVE_FILES.FILE_ID,ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                + " ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                + " ARCHIVE_FILES.PRI_KEY_TYPE as PRI_KEY_TYPE,ARCHIVE_FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                + " ARCHIVE_FILES.ORG_FILEPATH as ORG_FILEPATH,ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,"
                + " ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                + " ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,"
                + " ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,ARCHIVE_FILES.DIRECTION as DIRECTION , ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME,ARCHIVE_FILES.STATUS, "
                + " ARCHIVE_TRANSPORT_LOADTENDER.CO_NUMBER as CO_NUMBER, "
                + " ARCHIVE_TRANSPORT_LOADTENDER.TOTAL_WEIGHT as TOTAL_WEIGHT,ARCHIVE_TRANSPORT_LOADTENDER.TOTAL_VOLUME as TOTAL_VOLUME,"
                + " ARCHIVE_TRANSPORT_LOADTENDER.TOTAL_PIECES as TOTAL_PIECES,ARCHIVE_TRANSPORT_LOADTENDER.PO_NUMBER as PO_NUMBER "
                + " FROM ARCHIVE_FILES  LEFT OUTER JOIN ARCHIVE_TRANSPORT_LOADTENDER  ON (ARCHIVE_FILES.FILE_ID=ARCHIVE_TRANSPORT_LOADTENDER.FILE_ID and ARCHIVE_FILES.PRI_KEY_VAL=ARCHIVE_TRANSPORT_LOADTENDER.SHIPMENT_ID)"
                + " LEFT OUTER JOIN TP TP1 ON(TP1.ID=ARCHIVE_FILES.SENDER_ID AND TP1.STATUS='ACTIVE') LEFT OUTER JOIN TP TP2 ON(TP2.ID=ARCHIVE_FILES.RECEIVER_ID AND TP2.STATUS='ACTIVE')   "
                + " where ARCHIVE_FILES.FILE_ID LIKE '%" + fileId + "%' and ARCHIVE_FILES.PRI_KEY_VAL LIKE '%" + poNum + "%'");
        } else {
        lifeCycleQuery.append("SELECT FILES.REPROCESSSTATUS,Transport_loadtender.SHIPMENT_ID,FILES.FILE_ID,FILES.FILE_TYPE,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                + " FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                + " FILES.PRI_KEY_TYPE as PRI_KEY_TYPE,FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                + " FILES.ORG_FILEPATH as ORG_FILEPATH,FILES.ISA_NUMBER as ISA_NUMBER,"
                + " FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                + " FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,"
                + " FILES.ERR_MESSAGE,FILES.ACK_FILE_ID as ACK_FILE_ID,FILES.DIRECTION as DIRECTION , FILES.ISA_DATE as ISA_DATE,FILES.ISA_TIME as ISA_TIME,FILES.STATUS, "
                + " Transport_loadtender.CO_NUMBER as CO_NUMBER, "
                + " Transport_loadtender.TOTAL_WEIGHT as TOTAL_WEIGHT,Transport_loadtender.TOTAL_VOLUME as TOTAL_VOLUME,"
                + " Transport_loadtender.TOTAL_PIECES as TOTAL_PIECES,Transport_loadtender.PO_NUMBER as PO_NUMBER "
                + " FROM FILES  LEFT OUTER JOIN Transport_loadtender  ON (FILES.FILE_ID=Transport_loadtender.FILE_ID and FILES.PRI_KEY_VAL=Transport_loadtender.SHIPMENT_ID)"
                + " LEFT OUTER JOIN TP TP1 ON(TP1.ID=FILES.SENDER_ID AND TP1.STATUS='ACTIVE') LEFT OUTER JOIN TP TP2 ON(TP2.ID=FILES.RECEIVER_ID AND TP2.STATUS='ACTIVE')   "
                + " where FILES.FILE_ID LIKE '%" + fileId + "%' and FILES.PRI_KEY_VAL LIKE '%" + poNum + "%'");
        }
          System.out.println("getLtLoadtender query--->" + lifeCycleQuery.toString());
        String searchQuery = lifeCycleQuery.toString();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);

            while (resultSet.next()) {
                ltTenderBean = new LtTenderBean();

                // TP2.ID , tp2.NAME as name,
                ltTenderBean.setFileId(resultSet.getString("FILE_ID"));
                ltTenderBean.setIsaNum(resultSet.getString("ISA_NUMBER"));
                ltTenderBean.setShipmentid(resultSet.getString("SHIPMENT_ID"));
                ltTenderBean.setFileType(resultSet.getString("FILE_TYPE"));
                //  ltTenderBean.setFile_origin(resultSet.getString("file_origin"));
                ltTenderBean.setTran_type(resultSet.getString("TRANSACTION_TYPE"));
                ltTenderBean.setPreFile(resultSet.getString("PRE_TRANS_FILEPATH"));
                ltTenderBean.setPostTranFile(resultSet.getString("POST_TRANS_FILEPATH"));
                ltTenderBean.setAckFile(resultSet.getString("ACK_FILE_ID"));

                ltTenderBean.setDirection(resultSet.getString("DIRECTION"));
                // ltTenderBean.setDatetime(resultSet.getTimestamp("datetime"));
                ltTenderBean.setStatus(resultSet.getString("STATUS"));

                ltTenderBean.setSecval(resultSet.getString("PRI_KEY_VAL"));

                ltTenderBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));

                ltTenderBean.setSenderId(resultSet.getString("SENDER_ID"));
                //ltTenderBean.setBolNumber(resultSet.getString("BOL_NUMBER")) ; 
                ltTenderBean.setPoNumber(resultSet.getString("PO_NUMBER"));

                ltTenderBean.setSenName(resultSet.getString("SENDER_NAME"));
                ltTenderBean.setRecId(resultSet.getString("RECEIVER_ID"));

                ltTenderBean.setIsaTime(resultSet.getString("ISA_TIME"));
                ltTenderBean.setIsaDate(resultSet.getString("ISA_DATE"));

                ltTenderBean.setIsaNum(resultSet.getString("ISA_NUMBER"));

                ltTenderBean.setRecName(resultSet.getString("RECEIVER_NAME"));

//                if (resultSet.getString("comments") != null && !"".equals(resultSet.getString("comments"))) {
//                    // System.out.println("hiiii");
//                    ltTenderBean.setComments(resultSet.getString("comments"));
//                } else {
//                    // System.out.println("hiiii else");
//                    ltTenderBean.setComments("");
//                }
//
//                // System.out.println("mod file--->"+resultSet.getString("modFilePath"));
//                if (resultSet.getString("modFilePath") != null) {
//                    if (new File(resultSet.getString("modFilePath")).exists()) {
//                        ltTenderBean.setModFilePath(resultSet.getString("modFilePath"));
//                    } else {
//                        ltTenderBean.setModFilePath("");
//                    }
//                } else {
//                    ltTenderBean.setModFilePath("");
//                }
//
//                ltTenderBean.setModFlag(resultSet.getString("MOD_FLAG"));
                ltTenderBean.setErrorMessage(resultSet.getString("ERR_MESSAGE"));

                //  sb.append("<MODFLAG>" + resultSet.getInt("MOD_FLAG") + "</MODFLAG>");
                ltTenderBean.setRes("1");
            }
            // lifecycleBeans.setPoLifecycleBean(poLifecycleBean);

        } catch (SQLException e) {
            //System.out.println("I am in catch block coming in IMpl");
            // TODO Auto-generated catch block
            // e.printStackTrace();
        } catch (Exception ex) {
            //System.out.println("hi"+ex.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }

        //  System.out.println("Size in LFC PO Action --->"+poLifecycleBeanList.size());
        return ltTenderBean;
    }

    public LtResponsesBean getLtResponseDetails(String poNumber, String fileId, String database) throws ServiceLocatorException {

        StringBuffer lifeCycleQuery = new StringBuffer();
        String poNum = poNumber;
        // System.out.println("LifeCycleUtill ASN---->"+poNum);
        // asnLifecycleBean.setRes("0");
        if ("ARCHIVE".equals(database)) {
            lifeCycleQuery.append("SELECT DISTINCT(ARCHIVE_FILES.FILE_ID) as FILE_ID,ARCHIVE_TRANSPORT_LT_RESPONSE.SHIPMENT_ID as SHIPMENT_ID,"
                + " ARCHIVE_FILES.FILE_TYPE as FILE_TYPE,ARCHIVE_FILES.REPROCESSSTATUS,ARCHIVE_FILES.DIRECTION as DIRECTION,"
                + " ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                + " ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,"
                + " ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME,"
                + " ARCHIVE_FILES.STATUS as STATUS,ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.ACK_FILE_ID,ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.PRI_KEY_VAL as PRI_KEY_VAL"
                + " FROM ARCHIVE_TRANSPORT_LT_RESPONSE LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_TRANSPORT_LT_RESPONSE.FILE_ID =ARCHIVE_FILES.FILE_ID)"
                + " LEFT OUTER JOIN TP TP1 ON(TP1.ID=ARCHIVE_FILES.SENDER_ID AND TP1.STATUS='ACTIVE') LEFT OUTER JOIN TP TP2 ON(TP2.ID=ARCHIVE_FILES.RECEIVER_ID AND TP2.STATUS='ACTIVE') "
                + " WHERE 1=1 AND ARCHIVE_TRANSPORT_LT_RESPONSE.FILE_ID = '" + fileId + "' AND ARCHIVE_TRANSPORT_LT_RESPONSE.SHIPMENT_ID='" + poNumber + "'");
        }else{
        lifeCycleQuery.append("SELECT DISTINCT(FILES.FILE_ID) as FILE_ID,TRANSPORT_LT_RESPONSE.SHIPMENT_ID as SHIPMENT_ID,"
                + " FILES.FILE_TYPE as FILE_TYPE,FILES.REPROCESSSTATUS,FILES.DIRECTION as DIRECTION,"
                + " FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                + " FILES.ISA_NUMBER as ISA_NUMBER,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,"
                + " FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,FILES.ISA_DATE as ISA_DATE,FILES.ISA_TIME as ISA_TIME,"
                + " FILES.STATUS as STATUS,FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.ACK_FILE_ID,FILES.ERR_MESSAGE,FILES.PRI_KEY_VAL as PRI_KEY_VAL"
                + " FROM TRANSPORT_LT_RESPONSE LEFT OUTER JOIN FILES ON (TRANSPORT_LT_RESPONSE.FILE_ID =FILES.FILE_ID)"
                + " LEFT OUTER JOIN TP TP1 ON(TP1.ID=FILES.SENDER_ID AND TP1.STATUS='ACTIVE') LEFT OUTER JOIN TP TP2 ON(TP2.ID=FILES.RECEIVER_ID AND TP2.STATUS='ACTIVE') "
                + " WHERE 1=1 AND TRANSPORT_LT_RESPONSE.FILE_ID = '" + fileId + "' AND TRANSPORT_LT_RESPONSE.SHIPMENT_ID='" + poNumber + "'");
        }
      System.out.println("getResponse-->" + lifeCycleQuery.toString());
        String searchQuery = lifeCycleQuery.toString();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            while (resultSet.next()) {
                ltResponsesBean = new LtResponsesBean();

                // TP2.ID , tp2.NAME as name,
                // TP2.ID , tp2.NAME as name,
                ltResponsesBean.setFileId(resultSet.getString("FILE_ID"));
                ltResponsesBean.setIsaNum(resultSet.getString("ISA_NUMBER"));
                ltResponsesBean.setShipmentid(resultSet.getString("SHIPMENT_ID"));
                ltResponsesBean.setFileType(resultSet.getString("FILE_TYPE"));
                //  ltTenderBean.setFile_origin(resultSet.getString("file_origin"));
                ltResponsesBean.setTran_type(resultSet.getString("TRANSACTION_TYPE"));
                ltResponsesBean.setPreFile(resultSet.getString("PRE_TRANS_FILEPATH"));
                ltResponsesBean.setPostTranFile(resultSet.getString("POST_TRANS_FILEPATH"));
                ltResponsesBean.setAckFile(resultSet.getString("ACK_FILE_ID"));

                ltResponsesBean.setDirection(resultSet.getString("DIRECTION"));
                // ltTenderBean.setBolNumber(resultSet.getString("BOL_NUMBER"));
                ltResponsesBean.setStatus(resultSet.getString("STATUS"));

                ltResponsesBean.setSecval(resultSet.getString("PRI_KEY_VAL"));

                ltResponsesBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));

                ltResponsesBean.setSenderId(resultSet.getString("SENDER_ID"));

                //  ltResponsesBean.setPoNumber(resultSet.getString("PO_NUMBER"));
                ltResponsesBean.setSenName(resultSet.getString("SENDER_NAME"));
                ltResponsesBean.setRecId(resultSet.getString("RECEIVER_ID"));

                ltResponsesBean.setIsaTime(resultSet.getString("ISA_TIME"));
                ltResponsesBean.setIsaDate(resultSet.getString("ISA_DATE"));

                ltResponsesBean.setIsaNum(resultSet.getString("ISA_NUMBER"));

                ltResponsesBean.setRecName(resultSet.getString("RECEIVER_NAME"));
                ltResponsesBean.setErrorMessage(resultSet.getString("ERR_MESSAGE"));

                ltResponsesBean.setRes("1");

            }
            // lifecycleBeans.setAsnLifecycleBean(asnLifecycleBean);

        } catch (SQLException e) {
            //System.out.println("I am in catch block coming in IMpl");
            // TODO Auto-generated catch block
            //e.printStackTrace();
        } catch (Exception ex) {
            //System.out.println("hi"+ex.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        // System.out.println("LifeCycleService impl ltResponsesBean---->"+ltResponsesBean.toString());
        return ltResponsesBean;
    }

    public LtShipmentsBean getLtShipmentDetails(String poNumber, String fileId, String database) throws ServiceLocatorException {

        StringBuffer lifeCycleQuery = new StringBuffer();
        String poNum = poNumber;
        // System.out.println("LifeCycleService impl INVOICE---->"+poNum);
        //invoiceLifecycleBean.setRes("0");
        if ("ARCHIVE".equals(database)) {
            lifeCycleQuery.append("SELECT ARCHIVE_FILES.REPROCESSSTATUS,ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                + " ARCHIVE_TRANSPORT_SHIPMENT.FILE_ID as FILE_ID,ARCHIVE_TRANSPORT_SHIPMENT.SHIPMENT_ID as SHIPMENT_ID,ARCHIVE_TRANSPORT_SHIPMENT.PO_NUMBER as PO_NUMBER,"
                + " ARCHIVE_TRANSPORT_SHIPMENT.TOTAL_WEIGHT as TOTAL_WEIGHT,ARCHIVE_TRANSPORT_SHIPMENT.TOTAL_VOLUME as TOTAL_VOLUME,"
                + " ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,"
                + " ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                + " ARCHIVE_FILES.ORG_FILEPATH,ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.STATUS,ARCHIVE_FILES.FILE_TYPE as FILE_TYPE,ARCHIVE_FILES.DIRECTION as DIRECTION,"
                + " ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID FROM ARCHIVE_TRANSPORT_SHIPMENT "
                + " LEFT OUTER JOIN ARCHIVE_FILES ON ((ARCHIVE_TRANSPORT_SHIPMENT.FILE_ID =ARCHIVE_FILES.FILE_ID) AND (ARCHIVE_TRANSPORT_SHIPMENT.SHIPMENT_ID =ARCHIVE_FILES.PRI_KEY_VAL)) "
                + " LEFT OUTER JOIN TP TP1 ON (TP1.ID=ARCHIVE_FILES.SENDER_ID AND TP1.STATUS='ACTIVE') LEFT OUTER JOIN TP TP2 ON (TP2.ID = ARCHIVE_FILES.RECEIVER_ID AND TP2.STATUS='ACTIVE') "
                + " WHERE ARCHIVE_TRANSPORT_SHIPMENT.SHIPMENT_ID LIKE '%" + poNumber + "%' AND ARCHIVE_FILES.FILE_ID LIKE '%" + fileId + "%'");
        }else{
        lifeCycleQuery.append("SELECT FILES.REPROCESSSTATUS,FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                + " TRANSPORT_SHIPMENT.FILE_ID as FILE_ID,TRANSPORT_SHIPMENT.SHIPMENT_ID as SHIPMENT_ID,TRANSPORT_SHIPMENT.PO_NUMBER as PO_NUMBER,"
                + " TRANSPORT_SHIPMENT.TOTAL_WEIGHT as TOTAL_WEIGHT,TRANSPORT_SHIPMENT.TOTAL_VOLUME as TOTAL_VOLUME,"
                + " FILES.ISA_NUMBER as ISA_NUMBER,FILES.ISA_DATE as ISA_DATE,FILES.ISA_TIME as ISA_TIME,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,"
                + " FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                + " FILES.ORG_FILEPATH,FILES.ERR_MESSAGE,FILES.STATUS,FILES.FILE_TYPE as FILE_TYPE,FILES.DIRECTION as DIRECTION,"
                + " FILES.ACK_FILE_ID as ACK_FILE_ID FROM TRANSPORT_SHIPMENT "
                + " LEFT OUTER JOIN FILES ON ((TRANSPORT_SHIPMENT.FILE_ID =FILES.FILE_ID) AND (TRANSPORT_SHIPMENT.SHIPMENT_ID =FILES.PRI_KEY_VAL)) "
                + " LEFT OUTER JOIN TP TP1 ON (TP1.ID=FILES.SENDER_ID AND TP1.STATUS='ACTIVE') LEFT OUTER JOIN TP TP2 ON (TP2.ID = FILES.RECEIVER_ID AND TP2.STATUS='ACTIVE') "
                + " WHERE TRANSPORT_SHIPMENT.SHIPMENT_ID LIKE '%" + poNumber + "%' AND FILES.FILE_ID LIKE '%" + fileId + "%'");
        }
        System.out.println("getShipment Query-->" + lifeCycleQuery.toString());
        String searchQuery = lifeCycleQuery.toString();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);

            while (resultSet.next()) {
                ltShipmentBean = new LtShipmentsBean();

                ltShipmentBean.setFileId(resultSet.getString("FILE_ID"));
                ltShipmentBean.setIsaNum(resultSet.getString("ISA_NUMBER"));
                ltShipmentBean.setShipmentid(resultSet.getString("SHIPMENT_ID"));
                ltShipmentBean.setFileType(resultSet.getString("FILE_TYPE"));
                //  ltTenderBean.setFile_origin(resultSet.getString("file_origin"));
                ltShipmentBean.setTran_type(resultSet.getString("TRANSACTION_TYPE"));
                ltShipmentBean.setPreFile(resultSet.getString("PRE_TRANS_FILEPATH"));
                ltShipmentBean.setPostTranFile(resultSet.getString("POST_TRANS_FILEPATH"));
                ltShipmentBean.setAckFile(resultSet.getString("ACK_FILE_ID"));

                ltShipmentBean.setDirection(resultSet.getString("DIRECTION"));
                // ltTenderBean.setDatetime(resultSet.getTimestamp("datetime"));
                ltShipmentBean.setStatus(resultSet.getString("STATUS"));

                ltShipmentBean.setSecval(resultSet.getString("PRI_KEY_VAL"));

                ltShipmentBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));

                ltShipmentBean.setSenderId(resultSet.getString("SENDER_ID"));

                ltShipmentBean.setPoNumber(resultSet.getString("PO_NUMBER"));

                ltShipmentBean.setSenName(resultSet.getString("SENDER_NAME"));
                ltShipmentBean.setRecId(resultSet.getString("RECEIVER_ID"));

                ltShipmentBean.setIsaTime(resultSet.getString("ISA_TIME"));
                ltShipmentBean.setIsaDate(resultSet.getString("ISA_DATE"));

                ltShipmentBean.setIsaNum(resultSet.getString("ISA_NUMBER"));

                ltShipmentBean.setRecName(resultSet.getString("RECEIVER_NAME"));
                ltShipmentBean.setErrorMessage(resultSet.getString("ERR_MESSAGE"));

                ltShipmentBean.setRes("1");

            }
            // lifecycleBeans.setInvoiceLifecycleBean(invoiceLifecycleBean);

        } catch (SQLException e) {
            //System.out.println("I am in catch block coming in IMpl");
            // TODO Auto-generated catch block
            // e.printStackTrace();
        } catch (Exception ex) {
            //System.out.println("hi"+ex.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        //System.out.println("LifeCycleService impl ltShipmentBean---->"+ltShipmentBean.toString());
        return ltShipmentBean;
    }

    public LtInvoicesBean getLtInvoiceDetails(String poNumber, String fileId, String database) throws ServiceLocatorException {
        StringBuffer lifeCycleQuery = new StringBuffer();
        String poNum = poNumber;
        // System.out.println("LifeCycleService impl PAYMENTS---->"+poNum);
        // paymentLifecycleBean.setRes("1");
        if ("ARCHIVE".equals(database)) {
            lifeCycleQuery.append("SELECT ARCHIVE_TRANSPORT_INVOICE.SHIPMENT_ID,ARCHIVE_FILES.REPROCESSSTATUS,ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                + "ARCHIVE_TRANSPORT_INVOICE.FILE_ID as FILE_ID,ARCHIVE_TRANSPORT_INVOICE.INVOICE_NUMBER as INVOICE_NUMBER,ARCHIVE_TRANSPORT_INVOICE.PO_NUMBER as PO_NUMBER,"
                + "ARCHIVE_TRANSPORT_INVOICE.TOTAL_WEIGHT as TOTAL_WEIGHT,ARCHIVE_TRANSPORT_INVOICE.TOTAL_AMOUNT as TOTAL_AMOUNT,"
                + "ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,"
                + " ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                + "ARCHIVE_FILES.ORG_FILEPATH,ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.STATUS, ARCHIVE_FILES.FILE_TYPE as FILE_TYPE,ARCHIVE_FILES.DIRECTION as DIRECTION,"
                + "ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID FROM ARCHIVE_TRANSPORT_INVOICE "
                + "LEFT OUTER JOIN ARCHIVE_FILES ON ((ARCHIVE_TRANSPORT_INVOICE.FILE_ID =ARCHIVE_FILES.FILE_ID) AND (ARCHIVE_TRANSPORT_INVOICE.SHIPMENT_ID = ARCHIVE_FILES.PRI_KEY_VAL)) "
                + " LEFT OUTER JOIN TP TP1 ON(TP1.ID=ARCHIVE_FILES.SENDER_ID AND TP1.STATUS='ACTIVE') LEFT OUTER JOIN TP TP2 ON(TP2.ID=ARCHIVE_FILES.RECEIVER_ID AND TP2.STATUS='ACTIVE') "
                + " WHERE ARCHIVE_TRANSPORT_INVOICE.SHIPMENT_ID LIKE '%" + poNumber + "%' AND ARCHIVE_FILES.FILE_ID LIKE '%" + fileId + "%'");
        }else{
        lifeCycleQuery.append("SELECT TRANSPORT_INVOICE.SHIPMENT_ID,FILES.REPROCESSSTATUS,FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                + "TRANSPORT_INVOICE.FILE_ID as FILE_ID,TRANSPORT_INVOICE.INVOICE_NUMBER as INVOICE_NUMBER,TRANSPORT_INVOICE.PO_NUMBER as PO_NUMBER,"
                + "TRANSPORT_INVOICE.TOTAL_WEIGHT as TOTAL_WEIGHT,TRANSPORT_INVOICE.TOTAL_AMOUNT as TOTAL_AMOUNT,"
                + "FILES.ISA_NUMBER as ISA_NUMBER,FILES.ISA_DATE as ISA_DATE,FILES.ISA_TIME as ISA_TIME,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,"
                + " FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                + "FILES.ORG_FILEPATH,FILES.ERR_MESSAGE,FILES.STATUS, FILES.FILE_TYPE as FILE_TYPE,FILES.DIRECTION as DIRECTION,"
                + "FILES.ACK_FILE_ID as ACK_FILE_ID FROM TRANSPORT_INVOICE "
                + "LEFT OUTER JOIN FILES ON ((TRANSPORT_INVOICE.FILE_ID =FILES.FILE_ID) AND (TRANSPORT_INVOICE.SHIPMENT_ID = FILES.PRI_KEY_VAL)) "
                + " LEFT OUTER JOIN TP TP1 ON(TP1.ID=FILES.SENDER_ID AND TP1.STATUS='ACTIVE') LEFT OUTER JOIN TP TP2 ON(TP2.ID=FILES.RECEIVER_ID AND TP2.STATUS='ACTIVE') "
                + " WHERE TRANSPORT_INVOICE.SHIPMENT_ID LIKE '%" + poNumber + "%' AND FILES.FILE_ID LIKE '%" + fileId + "%'");
        }
        System.out.println("getInvoice Query-->" + lifeCycleQuery.toString());
        String searchQuery = lifeCycleQuery.toString();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            while (resultSet.next()) {
                ltInvoicesBean = new LtInvoicesBean();
                ltInvoicesBean.setFileId(resultSet.getString("FILE_ID"));
                ltInvoicesBean.setIsaNum(resultSet.getString("ISA_NUMBER"));
                ltInvoicesBean.setShipmentid(resultSet.getString("SHIPMENT_ID"));
                ltInvoicesBean.setFileType(resultSet.getString("FILE_TYPE"));
                //  ltTenderBean.setFile_origin(resultSet.getString("file_origin"));
                ltInvoicesBean.setTran_type(resultSet.getString("TRANSACTION_TYPE"));
                ltInvoicesBean.setPreFile(resultSet.getString("PRE_TRANS_FILEPATH"));
                ltInvoicesBean.setPostTranFile(resultSet.getString("POST_TRANS_FILEPATH"));
                ltInvoicesBean.setAckFile(resultSet.getString("ACK_FILE_ID"));

                ltInvoicesBean.setDirection(resultSet.getString("DIRECTION"));
                // ltTenderBean.setDatetime(resultSet.getTimestamp("datetime"));
                ltInvoicesBean.setStatus(resultSet.getString("STATUS"));

                ltInvoicesBean.setSecval(resultSet.getString("PRI_KEY_VAL"));

                ltInvoicesBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));

                ltInvoicesBean.setSenderId(resultSet.getString("SENDER_ID"));

                ltInvoicesBean.setPoNumber(resultSet.getString("PO_NUMBER"));

                ltInvoicesBean.setSenName(resultSet.getString("SENDER_NAME"));
                ltInvoicesBean.setRecId(resultSet.getString("RECEIVER_ID"));

                ltInvoicesBean.setIsaTime(resultSet.getString("ISA_TIME"));
                ltInvoicesBean.setIsaDate(resultSet.getString("ISA_DATE"));

                ltInvoicesBean.setIsaNum(resultSet.getString("ISA_NUMBER"));

                ltInvoicesBean.setRecName(resultSet.getString("RECEIVER_NAME"));
                ltInvoicesBean.setErrorMessage(resultSet.getString("ERR_MESSAGE"));

                ltInvoicesBean.setRes("1");

            }
            //lifecycleBeans.setPaymentLifecycleBean(paymentLifecycleBean);

        } catch (SQLException e) {
            //System.out.println("I am in catch block coming in IMpl");
            // TODO Auto-generated catch block
            //   e.printStackTrace();
        } catch (Exception ex) {
            //System.out.println("hi"+ex.getMessage());
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (statement != null) {
                    statement.close();
                    statement = null;
                }

                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return ltInvoicesBean;
    }

}
