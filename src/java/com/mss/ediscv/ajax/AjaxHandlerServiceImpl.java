/*
 * AjaxHandlerServiceImpl.java
 * Created on June 11, 2008, 12:57 AM
 *greensheetListSearch
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package com.mss.ediscv.ajax;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.ServiceLocatorException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.util.StringTokenizer;
import com.mss.ediscv.util.FileUtility;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.mss.ediscv.lfc.PoLifecycleBean;
import com.mss.ediscv.lfc.AsnLifecycleBean;
import com.mss.ediscv.lfc.InvoiceLifecycleBean;
import com.mss.ediscv.lfc.LtInvoicesBean;
import com.mss.ediscv.lfc.LtResponsesBean;
import com.mss.ediscv.lfc.LtShipmentsBean;
import com.mss.ediscv.lfc.LtTenderBean;
import com.mss.ediscv.lfc.PaymentLifecycleBean;
import com.mss.ediscv.util.LifecycleUtility;
import java.sql.Statement;
import com.mss.ediscv.util.DataSourceDataProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.MailManager;
import com.mss.ediscv.util.PasswordUtil;
import com.mss.ediscv.util.WildCardSql;
import com.mss.ediscv.utilities.CertMonitorServiceImpl;
import java.sql.CallableStatement;
import java.sql.Types;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author miracle
 */
public class AjaxHandlerServiceImpl implements AjaxHandlerService {

    /**
     * Creating a reference variable for Connection
     */
    private Connection connection;
    /**
     * Creating a reference variable for preparedStatement
     */
    private PreparedStatement preparedStatement;
    /**
     * Creating a reference variable for Resultset
     */
    private ResultSet resultSet;
    /**
     * Creating a reference variable for String Buffer
     */
    private StringBuffer stringBuffer;
    /**
     * Creating a String queryString used to store SQL Query
     */
    private String queryString;
    /**
     * Creating a boolean flag to return true or false
     */
    private boolean flag;
    /**
     * Creating a String noRecords to replace spaces in Ajax response
     */
    private String noRecords = "no records";
    private HttpServletRequest httpServletRequest;
    private StringBuffer queryStringBuffer;
    /**
     * Tp statement
     */
    private Statement statement;
    /**
     * Life cycle Beans
     */
    private PoLifecycleBean poLifecycleBean;
    private AsnLifecycleBean asnLifecycleBean;
    private InvoiceLifecycleBean invoiceLifecycleBean;
    private PaymentLifecycleBean paymentLifecycleBean;
    private LtTenderBean ltTenderBean;
    private LtResponsesBean ltResponsesBean;
    private LtShipmentsBean ltShipmentsBean;
    private LtInvoicesBean ltInvoicesBean;
    private ArrayList<PoLifecycleBean> poLifecycleBeanList;
    private ArrayList<AsnLifecycleBean> asnLifecycleBeanList;
    private ArrayList<InvoiceLifecycleBean> invoiceLifecycleBeanList;
    private ArrayList<PaymentLifecycleBean> PaymentLifecycleBeanList;

    public AjaxHandlerServiceImpl() {
    }

    /**
     * This method is used to get the Consultant Details
     *
     * @param consultantMail
     * @return String
     * @throws com.mss.mirage.util.ServiceLocatorException
     */
    public String getPoDetails(String poNumber, String poInst, String database) throws ServiceLocatorException {
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        if ("ARCHIVE".equals(database)) {
            queryString = "SELECT ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.ISA_NUMBER,"
                    + "ARCHIVE_FILES.GS_CONTROL_NUMBER,ARCHIVE_FILES.ST_CONTROL_NUMBER,ARCHIVE_PO.FILE_ID as FILE_ID,ARCHIVE_PO.PO_Number,ARCHIVE_PO.Order_Date,ARCHIVE_PO.PO_VALUE,ARCHIVE_PO.SAP_IDOC_Number,"
                    + "ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_PO.DELIVERY_STATUS,ARCHIVE_PO.SHIP_DATE,ARCHIVE_PO.ROUTINGS, ARCHIVE_PO.INVOICED_AMOUNT,ARCHIVE_PO.PAYMENT_RECEIVED,ARCHIVE_PO.SHIP_ADDRESS_ID,ARCHIVE_PO.BILL_ADDRESS_ID,"
                    + "ARCHIVE_PO.ITEM_QTY,ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH, "
                    + "ARCHIVE_FILES.ORG_FILEPATH,ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_PO.SO_NUMBER,ARCHIVE_FILES.STATUS,ARCHIVE_FILES.ISA_DATE,ARCHIVE_FILES.ISA_TIME,"
                    + "ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,ARCHIVE_PO.ORDER_STATUS AS ORDER_STATUS,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME "
                    + "FROM ARCHIVE_PO LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_PO.PO_NUMBER = ARCHIVE_FILES.PRI_KEY_VAL and ARCHIVE_PO.FILE_ID = ARCHIVE_FILES.FILE_ID) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=ARCHIVE_FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = ARCHIVE_FILES.RECEIVER_ID) "
                    + "WHERE FLOWFLAG like 'M' AND ARCHIVE_PO.PO_NUMBER LIKE '%" + poNumber + "%' and ARCHIVE_FILES.FILE_ID "
                    + "LIKE '%" + poInst + "%'";
        } else {
            queryString = "SELECT FILES.FILE_TYPE,Files.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.ISA_NUMBER,"
                    + "FILES.GS_CONTROL_NUMBER,FILES.ST_CONTROL_NUMBER,PO.FILE_ID as FILE_ID,PO.PO_Number,PO.Order_Date,PO.PO_VALUE,PO.SAP_IDOC_Number,"
                    + "FILES.SENDER_ID,FILES.RECEIVER_ID,PO.DELIVERY_STATUS,PO.SHIP_DATE,PO.ROUTINGS, PO.INVOICED_AMOUNT,PO.PAYMENT_RECEIVED,PO.SHIP_ADDRESS_ID,PO.BILL_ADDRESS_ID,"
                    + "PO.ITEM_QTY,FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH, "
                    + "FILES.ORG_FILEPATH,FILES.ERR_MESSAGE,PO.SO_NUMBER,FILES.STATUS,FILES.ISA_DATE,FILES.ISA_TIME,"
                    + "FILES.ACK_FILE_ID as ACK_FILE_ID,PO.ORDER_STATUS AS ORDER_STATUS,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME "
                    + "FROM PO LEFT OUTER JOIN FILES ON (PO.PO_NUMBER = FILES.PRI_KEY_VAL and PO.FILE_ID = FILES.FILE_ID) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = FILES.RECEIVER_ID) "
                    + "WHERE FLOWFLAG like 'M' AND PO.PO_NUMBER LIKE '%" + poNumber + "%' and FILES.FILE_ID "
                    + "LIKE '%" + poInst + "%'";
        }
        System.out.println("queryString==========" + queryString);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILEID>" + resultSet.getString("FILE_ID") + "</FILEID>");
                } else {
                    sb.append("<FILEID>NO</FILEID>");
                }
                if (resultSet.getString("PO_Number") != null && !"".equals(resultSet.getString("PO_Number"))) {
                    sb.append("<PONUM>" + resultSet.getString("PO_Number") + "</PONUM>");
                } else {
                    sb.append("<PONUM>NO</PONUM>");
                }
                if (resultSet.getString("Order_Date") != null && !"".equals(resultSet.getString("Order_Date"))) {
                    sb.append("<PODATE>" + resultSet.getString("Order_Date") + "</PODATE>");
                } else {
                    sb.append("<PODATE>NO</PODATE>");
                }
                if (resultSet.getString("PO_VALUE") != null && !"".equals(resultSet.getString("PO_VALUE"))) {
                    sb.append("<POVALUE>" + resultSet.getString("PO_VALUE") + "</POVALUE>");
                } else {
                    sb.append("<POVALUE>NO</POVALUE>");
                }
                if (resultSet.getString("SHIP_DATE") != null && !"".equals(resultSet.getString("SHIP_DATE"))) {
                    sb.append("<SHIP_DATE>" + resultSet.getString("SHIP_DATE") + "</SHIP_DATE>");
                } else {
                    sb.append("<SHIP_DATE>NO</SHIP_DATE>");
                }
                if (resultSet.getString("ROUTINGS") != null && !"".equals(resultSet.getString("ROUTINGS"))) {
                    sb.append("<ROUTINGS>" + resultSet.getString("ROUTINGS") + "</ROUTINGS>");
                } else {
                    sb.append("<ROUTINGS>NO</ROUTINGS>");
                }
                if (resultSet.getString("INVOICED_AMOUNT") != null && !"".equals(resultSet.getString("INVOICED_AMOUNT"))) {
                    sb.append("<INVOICED_AMOUNT>" + resultSet.getString("INVOICED_AMOUNT") + "</INVOICED_AMOUNT>");
                } else {
                    sb.append("<INVOICED_AMOUNT>NO</INVOICED_AMOUNT>");
                }
                if (resultSet.getString("PAYMENT_RECEIVED") != null && !"".equals(resultSet.getString("PAYMENT_RECEIVED"))) {
                    sb.append("<PAYMENT_RECEIVED>" + resultSet.getString("PAYMENT_RECEIVED") + "</PAYMENT_RECEIVED>");
                } else {
                    sb.append("<PAYMENT_RECEIVED>NO</PAYMENT_RECEIVED>");
                }
                if (resultSet.getString("SHIP_ADDRESS_ID") != null && !"".equals(resultSet.getString("SHIP_ADDRESS_ID"))) {
                    sb.append("<SHIP_ADDRESS_ID>" + resultSet.getString("SHIP_ADDRESS_ID") + "</SHIP_ADDRESS_ID>");
                } else {
                    sb.append("<SHIP_ADDRESS_ID>NO</SHIP_ADDRESS_ID>");
                }
                if (resultSet.getString("BILL_ADDRESS_ID") != null && !"".equals(resultSet.getString("BILL_ADDRESS_ID"))) {
                    sb.append("<BILL_ADDRESS_ID>" + resultSet.getString("BILL_ADDRESS_ID") + "</BILL_ADDRESS_ID>");
                } else {
                    sb.append("<BILL_ADDRESS_ID>NO</BILL_ADDRESS_ID>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISA_NUMBER>" + resultSet.getString("ISA_NUMBER") + "</ISA_NUMBER>");
                } else {
                    sb.append("<ISA_NUMBER>NO</ISA_NUMBER>");
                }
                //FILE_TYPE
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILE_TYPE>" + resultSet.getString("FILE_TYPE") + "</FILE_TYPE>");
                } else {
                    sb.append("<FILE_TYPE>NO</FILE_TYPE>");
                }
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>NO</GS_CONTROL_NUMBER>");
                }
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>NO</ST_CONTROL_NUMBER>");
                }
                if (resultSet.getString("SAP_IDOC_Number") != null && !"".equals(resultSet.getString("SAP_IDOC_Number"))) {
                    sb.append("<SAPIDOCNUM>" + resultSet.getString("SAP_IDOC_Number") + "</SAPIDOCNUM>");
                } else {
                    sb.append("<SAPIDOCNUM>NO</SAPIDOCNUM>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDER_ID>" + resultSet.getString("SENDER_ID") + "</SENDER_ID>");
                } else {
                    sb.append("<SENDER_ID>NO</SENDER_ID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVER_ID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVER_ID>");
                } else {
                    sb.append("<RECEIVER_ID>NO</RECEIVER_ID>");
                }
                if (resultSet.getString("SENDER_NAME") != null && !"".equals(resultSet.getString("SENDER_NAME"))) {
                    sb.append("<SENDER_NAME>" + resultSet.getString("SENDER_NAME") + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>NO</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_NAME") != null && !"".equals(resultSet.getString("RECEIVER_NAME"))) {
                    sb.append("<RECEIVER_NAME>" + resultSet.getString("RECEIVER_NAME") + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>NO</RECEIVER_NAME>");
                }
                if (resultSet.getString("DELIVERY_STATUS") != null && !"".equals(resultSet.getString("DELIVERY_STATUS"))) {
                    sb.append("<DELSTATUS>" + resultSet.getString("DELIVERY_STATUS").toUpperCase() + "</DELSTATUS>");
                } else {
                    sb.append("<DELSTATUS>NO</DELSTATUS>");
                }
                if (resultSet.getString("ITEM_QTY") != null && !"".equals(resultSet.getString("ITEM_QTY"))) {
                    sb.append("<ITEMQTY>" + resultSet.getString("ITEM_QTY") + "</ITEMQTY>");
                } else {
                    sb.append("<ITEMQTY>NO</ITEMQTY>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>NO</STATUS>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>NO</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                if (resultSet.getString("ORG_FILEPATH") != null) {
                    if (new File(resultSet.getString("ORG_FILEPATH")).exists() && new File(resultSet.getString("ORG_FILEPATH")).isFile()) {
                        sb.append("<ORGFILEPATH>" + resultSet.getString("ORG_FILEPATH") + "</ORGFILEPATH>");
                    } else {
                        sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                    }
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }
                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }
                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }
                if (resultSet.getString("SO_NUMBER") != null && !"".equals(resultSet.getString("SO_NUMBER"))) {
                    sb.append("<SO_NUMBER>" + resultSet.getString("SO_NUMBER") + "</SO_NUMBER>");
                } else {
                    sb.append("<SO_NUMBER>NO</SO_NUMBER>");
                }
                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }
                if (resultSet.getString("ORDER_STATUS") != null && !"".equals(resultSet.getString("ORDER_STATUS"))) {
                    sb.append("<ORDER_STATUS>" + resultSet.getString("ORDER_STATUS") + "</ORDER_STATUS>");
                } else {
                    sb.append("<ORDER_STATUS>NO</ORDER_STATUS>");
                }
                sb.append("<ISA_DATE>" + resultSet.getString("ISA_DATE") + "</ISA_DATE>");
                sb.append("<ISA_TIME>" + resultSet.getString("ISA_TIME") + "</ISA_TIME>");
                String sapDetails = DataSourceDataProvider.getInstance().getSapDetails(poInst, poNumber);
                if (!sapDetails.equals("None")) {
                    sb.append("<SAP_DETAILS>YES</SAP_DETAILS>");
                    String sapDetailsInfo[] = sapDetails.split("\\|");
                    sb.append("<SAP_USER>" + sapDetailsInfo[0] + "</SAP_USER>");
                    sb.append("<IDOC_NUMBER>" + sapDetailsInfo[1] + "</IDOC_NUMBER>");
                    sb.append("<PO_NUMBER>" + sapDetailsInfo[2] + "</PO_NUMBER>");
                    sb.append("<PO_DATE>" + sapDetailsInfo[3] + "</PO_DATE>");
                    sb.append("<IDOC_STATUS_CODE>" + sapDetailsInfo[4] + "</IDOC_STATUS_CODE>");
                    sb.append("<IDOC_STATUS_DESCRIPTION>" + sapDetailsInfo[5] + "</IDOC_STATUS_DESCRIPTION>");
                } else {
                    sb.append("<SAP_DETAILS>NO</SAP_DETAILS>");
                }
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String getASNDetails(String asnNumber, String poNumber, String fileID, String database) throws ServiceLocatorException {
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        if ("ARCHIVE".equals(database)) {
            queryString = "SELECT ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,"
                    + "ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,ARCHIVE_ASN.FILE_ID as FILE_ID,ARCHIVE_ASN.ASN_NUMBER as ASN_NUMBER,ARCHIVE_ASN.PO_NUMBER as PO_NUMBER,"
                    + "ARCHIVE_ASN.BOL_NUMBER as BOL_NUMBER,ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.ISA_DATE as  ISA_DATE,ARCHIVE_FILES.ISA_TIME, "
                    + " ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                    + "ARCHIVE_FILES.ORG_FILEPATH,ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.STATUS,ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,ARCHIVE_ASN.SHIP_DATE as SHIP_DATE,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME "
                    + "FROM ARCHIVE_ASN LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_ASN.ASN_NUMBER = ARCHIVE_FILES.PRI_KEY_VAL AND ARCHIVE_ASN.FILE_ID = ARCHIVE_FILES.FILE_ID) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=ARCHIVE_FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = ARCHIVE_FILES.RECEIVER_ID) "
                    + "WHERE FLOWFLAG like 'M' AND ASN_NUMBER LIKE '%" + asnNumber + "%' AND ARCHIVE_ASN.PO_NUMBER LIKE '%" + poNumber + "%'"
                    + " AND ARCHIVE_ASN.FILE_ID LIKE '" + fileID + "'";
        } else {
            queryString = "SELECT FILES.FILE_TYPE,Files.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,ASN.FILE_ID as FILE_ID,ASN.ASN_NUMBER as ASN_NUMBER,ASN.PO_NUMBER as PO_NUMBER,"
                    + "ASN.BOL_NUMBER as BOL_NUMBER,FILES.ISA_NUMBER as ISA_NUMBER,FILES.ISA_DATE as  ISA_DATE,FILES.ISA_TIME, "
                    + " FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                    + "FILES.ORG_FILEPATH,FILES.ERR_MESSAGE,FILES.STATUS,FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "FILES.ACK_FILE_ID as ACK_FILE_ID,ASN.SHIP_DATE as SHIP_DATE,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME "
                    + "FROM ASN LEFT OUTER JOIN FILES ON (ASN.ASN_NUMBER = FILES.PRI_KEY_VAL AND ASN.FILE_ID = FILES.FILE_ID) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = FILES.RECEIVER_ID) "
                    + "WHERE FLOWFLAG like 'M' AND ASN_NUMBER LIKE '%" + asnNumber + "%' AND ASN.PO_NUMBER LIKE '%" + poNumber + "%'"
                    + " AND ASN.FILE_ID LIKE '" + fileID + "'";
        }
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILEID>" + resultSet.getString("FILE_ID") + "</FILEID>");
                } else {
                    sb.append("<FILEID>NO</FILEID>");
                }
                if (resultSet.getString("ASN_NUMBER") != null && !"".equals(resultSet.getString("ASN_NUMBER"))) {
                    sb.append("<ASNNUMBER>" + resultSet.getString("ASN_NUMBER") + "</ASNNUMBER>");
                } else {
                    sb.append("<ASNNUMBER>NO</ASNNUMBER>");
                }
                if (resultSet.getString("PO_NUMBER") != null && !"".equals(resultSet.getString("PO_NUMBER"))) {
                    sb.append("<PONUMBER>" + resultSet.getString("PO_NUMBER") + "</PONUMBER>");
                } else {
                    sb.append("<PONUMBER>NO</PONUMBER>");
                }
                if (resultSet.getString("BOL_NUMBER") != null && !"".equals(resultSet.getString("BOL_NUMBER"))) {
                    sb.append("<BOLNUMBER>" + resultSet.getString("BOL_NUMBER") + "</BOLNUMBER>");
                } else {
                    sb.append("<BOLNUMBER>NO</BOLNUMBER>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISANUMBER>" + resultSet.getString("ISA_NUMBER") + "</ISANUMBER>");
                } else {
                    sb.append("<ISANUMBER>NO</ISANUMBER>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISADATE>" + resultSet.getString("ISA_DATE") + "</ISADATE>");
                } else {
                    sb.append("<ISADATE>NO</ISADATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISATIME>" + resultSet.getString("ISA_TIME") + "</ISATIME>");
                } else {
                    sb.append("<ISATIME>NO</ISATIME>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>NO</STATUS>");
                }
                if (resultSet.getString("SHIP_DATE") != null && !"".equals(resultSet.getString("SHIP_DATE"))) {
                    sb.append("<SHIPDATE>" + resultSet.getString("SHIP_DATE") + "</SHIPDATE>");
                } else {
                    sb.append("<SHIPDATE>NO</SHIPDATE>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>NO</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDER_ID>" + resultSet.getString("SENDER_ID") + "</SENDER_ID>");
                } else {
                    sb.append("<SENDER_ID>NO</SENDER_ID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVER_ID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVER_ID>");
                } else {
                    sb.append("<RECEIVER_ID>NO</RECEIVER_ID>");
                }
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILE_TYPE>" + resultSet.getString("FILE_TYPE") + "</FILE_TYPE>");
                } else {
                    sb.append("<FILE_TYPE>NO</FILE_TYPE>");
                }
                if (resultSet.getString("SENDER_NAME") != null && !"".equals(resultSet.getString("SENDER_NAME"))) {
                    sb.append("<SENDER_NAME>" + resultSet.getString("SENDER_NAME") + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>NO</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_NAME") != null && !"".equals(resultSet.getString("RECEIVER_NAME"))) {
                    sb.append("<RECEIVER_NAME>" + resultSet.getString("RECEIVER_NAME") + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>NO</RECEIVER_NAME>");
                }
                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                if (resultSet.getString("ORG_FILEPATH") != null) {
                    if (new File(resultSet.getString("ORG_FILEPATH")).exists() && new File(resultSet.getString("ORG_FILEPATH")).isFile()) {
                        sb.append("<ORGFILEPATH>" + resultSet.getString("ORG_FILEPATH") + "</ORGFILEPATH>");
                    } else {
                        sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                    }
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }
                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }
                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }
                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }
                //SST_CONTROL_NUMBER,
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>NO</ST_CONTROL_NUMBER>");
                }
                //GS_CONTROL_NUMBER,
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>NO</GS_CONTROL_NUMBER>");
                }
                String sapDetails = DataSourceDataProvider.getInstance().getSapDetails(resultSet.getString("FILE_ID"), resultSet.getString("PO_NUMBER"));
                if (!sapDetails.equals("None")) {
                    sb.append("<SAP_DETAILS>YES</SAP_DETAILS>");
                    String sapDetailsInfo[] = sapDetails.split("\\|");
                    sb.append("<SAP_USER>" + sapDetailsInfo[0] + "</SAP_USER>");
                    sb.append("<IDOC_NUMBER>" + sapDetailsInfo[1] + "</IDOC_NUMBER>");
                    sb.append("<PO_NUMBER>" + sapDetailsInfo[2] + "</PO_NUMBER>");
                    sb.append("<PO_DATE>" + sapDetailsInfo[3] + "</PO_DATE>");
                    sb.append("<IDOC_STATUS_CODE>" + sapDetailsInfo[4] + "</IDOC_STATUS_CODE>");
                    sb.append("<IDOC_STATUS_DESCRIPTION>" + sapDetailsInfo[5] + "</IDOC_STATUS_DESCRIPTION>");
                } else {
                    sb.append("<SAP_DETAILS>NO</SAP_DETAILS>");
                }
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String getInvDetails(String invNumber, String poNumber, String fileID, String database) throws ServiceLocatorException {
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        if ("ARCHIVE".equals(database)) {
            queryString = "SELECT ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "ARCHIVE_INVOICE.FILE_ID as FILE_ID,ARCHIVE_INVOICE.INVOICE_NUMBER as INVOICE_NUMBER,ARCHIVE_INVOICE.PO_NUMBER as PO_NUMBER,"
                    + "ARCHIVE_INVOICE.ITEM_QTY as ITEM_QTY,ARCHIVE_INVOICE.INVOICE_AMOUNT as INVOICE_AMOUNT,ARCHIVE_INVOICE.INVOICE_DATE as INVOICE_DATE,"
                    + "ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME,"
                    + " ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                    + "ARCHIVE_FILES.ORG_FILEPATH,ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.STATUS,ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME "
                    + " FROM ARCHIVE_INVOICE LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_INVOICE.INVOICE_NUMBER = ARCHIVE_FILES.PRI_KEY_VAL AND ARCHIVE_INVOICE.FILE_ID = ARCHIVE_FILES.FILE_ID) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=ARCHIVE_FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = ARCHIVE_FILES.RECEIVER_ID) "
                    + " WHERE FLOWFLAG like 'M' AND ARCHIVE_INVOICE.INVOICE_NUMBER LIKE '%" + invNumber + "%' AND ARCHIVE_INVOICE.PO_NUMBER LIKE '%" + poNumber + "%'"
                    + " AND ARCHIVE_INVOICE.FILE_ID='" + fileID + "'";
        } else {
            queryString = "SELECT FILES.FILE_TYPE,Files.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "INVOICE.FILE_ID as FILE_ID,INVOICE.INVOICE_NUMBER as INVOICE_NUMBER,INVOICE.PO_NUMBER as PO_NUMBER,"
                    + "INVOICE.ITEM_QTY as ITEM_QTY,INVOICE.INVOICE_AMOUNT as INVOICE_AMOUNT,INVOICE.INVOICE_DATE as INVOICE_DATE,"
                    + "Files.ISA_NUMBER as ISA_NUMBER,Files.ISA_DATE as ISA_DATE,Files.ISA_TIME as ISA_TIME,"
                    + " FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                    + "FILES.ORG_FILEPATH,FILES.ERR_MESSAGE,FILES.STATUS,FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "FILES.ACK_FILE_ID as ACK_FILE_ID,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME "
                    + " FROM INVOICE LEFT OUTER JOIN FILES ON (INVOICE.INVOICE_NUMBER = FILES.PRI_KEY_VAL AND INVOICE.FILE_ID = FILES.FILE_ID) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = FILES.RECEIVER_ID) "
                    + " WHERE FLOWFLAG like 'M' AND INVOICE.INVOICE_NUMBER LIKE '%" + invNumber + "%' AND INVOICE.PO_NUMBER LIKE '%" + poNumber + "%'"
                    + " AND INVOICE.FILE_ID='" + fileID + "'";
        }

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILEID>" + resultSet.getString("FILE_ID") + "</FILEID>");
                } else {
                    sb.append("<FILEID>--</FILEID>");
                }
                if (resultSet.getString("INVOICE_NUMBER") != null && !"".equals(resultSet.getString("INVOICE_NUMBER"))) {
                    sb.append("<INVNUMBER>" + resultSet.getString("INVOICE_NUMBER") + "</INVNUMBER>");
                } else {
                    sb.append("<INVNUMBER>--</INVNUMBER>");
                }
                if (resultSet.getString("PO_NUMBER") != null && !"".equals(resultSet.getString("PO_NUMBER"))) {
                    sb.append("<PONUMBER>" + resultSet.getString("PO_NUMBER") + "</PONUMBER>");
                } else {
                    sb.append("<PONUMBER>--</PONUMBER>");
                }
                if (resultSet.getString("ITEM_QTY") != null && !"".equals(resultSet.getString("ITEM_QTY"))) {
                    sb.append("<ITEMQTY>" + resultSet.getString("ITEM_QTY") + "</ITEMQTY>");
                } else {
                    sb.append("<ITEMQTY>--</ITEMQTY>");
                }
                if (resultSet.getString("INVOICE_AMOUNT") != null && !"".equals(resultSet.getString("INVOICE_AMOUNT"))) {
                    sb.append("<INVAMT>" + resultSet.getString("INVOICE_AMOUNT") + "</INVAMT>");
                } else {
                    sb.append("<INVAMT>--</INVAMT>");
                }
                if (resultSet.getString("INVOICE_DATE") != null && !"".equals(resultSet.getString("INVOICE_DATE"))) {
                    sb.append("<INVDATE>" + resultSet.getString("INVOICE_DATE") + "</INVDATE>");
                } else {
                    sb.append("<INVDATE>NO</INVDATE>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISANUM>" + resultSet.getString("ISA_NUMBER") + "</ISANUM>");
                } else {
                    sb.append("<ISANUM>--</ISANUM>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISADATE>" + resultSet.getString("ISA_DATE") + "</ISADATE>");
                } else {
                    sb.append("<ISADATE>--</ISADATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISATIME>" + resultSet.getString("ISA_TIME") + "</ISATIME>");
                } else {
                    sb.append("<ISATIME>--</ISATIME>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILETYPE>" + resultSet.getString("FILE_TYPE") + "</FILETYPE>");
                } else {
                    sb.append("<FILETYPE>--</FILETYPE>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDER_ID>" + resultSet.getString("SENDER_ID") + "</SENDER_ID>");
                } else {
                    sb.append("<SENDER_ID>--</SENDER_ID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVER_ID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVER_ID>");
                } else {
                    sb.append("<RECEIVER_ID>--</RECEIVER_ID>");
                }
                if (resultSet.getString("SENDER_NAME") != null && !"".equals(resultSet.getString("SENDER_NAME"))) {
                    sb.append("<SENDER_NAME>" + resultSet.getString("SENDER_NAME") + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_NAME") != null && !"".equals(resultSet.getString("RECEIVER_NAME"))) {
                    sb.append("<RECEIVER_NAME>" + resultSet.getString("RECEIVER_NAME") + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                }
                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                if (resultSet.getString("ORG_FILEPATH") != null) {
                    if (new File(resultSet.getString("ORG_FILEPATH")).exists() && new File(resultSet.getString("ORG_FILEPATH")).isFile()) {
                        sb.append("<ORGFILEPATH>" + resultSet.getString("ORG_FILEPATH") + "</ORGFILEPATH>");
                    } else {
                        sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                    }
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }
                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }
                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }
                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }
                //TRANSACTION_TYPE,
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                //SST_CONTROL_NUMBER,
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>--</ST_CONTROL_NUMBER>");
                }
                //GS_CONTROL_NUMBER,
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>--</GS_CONTROL_NUMBER>");
                }
                String sapDetails = DataSourceDataProvider.getInstance().getSapDetails(resultSet.getString("FILE_ID"), resultSet.getString("PO_NUMBER"));

                if (!sapDetails.equals("None")) {
                    sb.append("<SAP_DETAILS>YES</SAP_DETAILS>");
                    String sapDetailsInfo[] = sapDetails.split("\\|");
                    sb.append("<SAP_USER>" + sapDetailsInfo[0] + "</SAP_USER>");
                    sb.append("<IDOC_NUMBER>" + sapDetailsInfo[1] + "</IDOC_NUMBER>");
                    sb.append("<PO_NUMBER>" + sapDetailsInfo[2] + "</PO_NUMBER>");
                    sb.append("<PO_DATE>" + sapDetailsInfo[3] + "</PO_DATE>");
                    sb.append("<IDOC_STATUS_CODE>" + sapDetailsInfo[4] + "</IDOC_STATUS_CODE>");
                    sb.append("<IDOC_STATUS_DESCRIPTION>" + sapDetailsInfo[5] + "</IDOC_STATUS_DESCRIPTION>");
                } else {
                    sb.append("<SAP_DETAILS>NO</SAP_DETAILS>");
                }
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return sb.toString();
    }

    /**
     * @param isaNumber
     * @return
     * @throws ServiceLocatorException
     */
    public String getDocDetails(String instanceid, String ponum, int id, String database) throws ServiceLocatorException {
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        if ("ARCHIVE".equals(database)) {
            queryString = "select ARCHIVE_FILES.STATUS,ARCHIVE_FILES.DIRECTION as DIRECTION,ARCHIVE_FILES.FILE_ID,ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                    + "ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.SEC_KEY_VAL as SEC_KEY_VAL,ARCHIVE_FILES.PRI_KEY_TYPE as PRI_KEY_TYPE,"
                    + "ARCHIVE_FILES.PRI_KEY_VAL as PRI_KEY_VAL,ARCHIVE_FILES.ORG_FILEPATH as ORG_FILEPATH,ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.ISA_DATE as ISA_DATE,"
                    + "ARCHIVE_FILES.ISA_TIME as ISA_TIME,ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,ARCHIVE_FILES.ERR_MESSAGE,"
                    + "ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID, ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH from ARCHIVE_FILES "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID = ARCHIVE_FILES.SENDER_ID) "
                    + "LEFT OUTER JOIN TP TP2 ON (TP2.ID = ARCHIVE_FILES.RECEIVER_ID) "
                    + "where FLOWFLAG like 'M' AND ARCHIVE_FILES.FILE_ID LIKE '%" + instanceid + "%' AND ARCHIVE_FILES.ID =" + id;
        } else {
            queryString = "select FILES.STATUS,FILES.DIRECTION as DIRECTION,FILES.FILE_ID,FILES.FILE_TYPE,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                    + "FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.SEC_KEY_VAL as SEC_KEY_VAL,FILES.PRI_KEY_TYPE as PRI_KEY_TYPE,"
                    + "FILES.PRI_KEY_VAL as PRI_KEY_VAL,FILES.ORG_FILEPATH as ORG_FILEPATH,FILES.ISA_NUMBER as ISA_NUMBER,FILES.ISA_DATE as ISA_DATE,"
                    + "FILES.ISA_TIME as ISA_TIME,FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,FILES.ERR_MESSAGE,"
                    + "FILES.ACK_FILE_ID as ACK_FILE_ID, FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH from FILES "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=FILES.SENDER_ID) "
                    + "LEFT OUTER JOIN TP TP2 ON (TP2.ID = FILES.RECEIVER_ID) "
                    + "where FLOWFLAG like 'M' AND FILES.FILE_ID LIKE '%" + instanceid + "%' AND FILES.ID =" + id;
        }
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILEID>" + resultSet.getString("FILE_ID") + "</FILEID>");
                } else {
                    sb.append("<FILEID>--</FILEID>");
                }
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILETYPE>" + resultSet.getString("FILE_TYPE") + "</FILETYPE>");
                } else {
                    sb.append("<FILETYPE>--</FILETYPE>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDERID>" + resultSet.getString("SENDER_ID") + "</SENDERID>");
                } else {
                    sb.append("<SENDERID>--</SENDERID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVERID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVERID>");
                } else {
                    sb.append("<RECEIVERID>--</RECEIVERID>");
                }
                if (resultSet.getString("SENDER_NAME") == null) {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>" + resultSet.getString("SENDER_NAME") + "</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_NAME") == null) {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>" + resultSet.getString("RECEIVER_NAME") + "</RECEIVER_NAME>");
                }
                //DIRECTION
                if (resultSet.getString("DIRECTION") != null && !"".equals(resultSet.getString("DIRECTION"))) {
                    sb.append("<DIRECTION>" + resultSet.getString("DIRECTION").toLowerCase() + "</DIRECTION>");
                } else {
                    sb.append("<DIRECTION>--</DIRECTION>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISA_NUMBER>" + resultSet.getString("ISA_NUMBER") + "</ISA_NUMBER>");
                } else {
                    sb.append("<ISA_NUMBER>--</ISA_NUMBER>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISA_DATE>" + resultSet.getString("ISA_DATE") + "</ISA_DATE>");
                } else {
                    sb.append("<ISA_DATE>--</ISA_DATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISA_TIME>" + resultSet.getString("ISA_TIME") + "</ISA_TIME>");
                } else {
                    sb.append("<ISA_TIME>--</ISA_TIME>");
                }
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equalsIgnoreCase(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>--</GS_CONTROL_NUMBER>");
                }
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>--</ST_CONTROL_NUMBER>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("SEC_KEY_VAL") != null && !"".equalsIgnoreCase(resultSet.getString("SEC_KEY_VAL"))) {
                    sb.append("<SEC_KEY_VAL>" + resultSet.getString("SEC_KEY_VAL") + "</SEC_KEY_VAL>");
                } else {
                    sb.append("<SEC_KEY_VAL>--</SEC_KEY_VAL>");
                }
                if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("PO")) {
                    sb.append("<PRI_KEY_TYPE>PO</PRI_KEY_TYPE>");
                } else if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("ASN")) {
                    sb.append("<PRI_KEY_TYPE> ASN </PRI_KEY_TYPE>");
                } else if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("IN")) {
                    sb.append("<PRI_KEY_TYPE> Invoice </PRI_KEY_TYPE>");
                } else if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("PAYMENT")) {
                    sb.append("<PRI_KEY_TYPE> Cheque </PRI_KEY_TYPE>");
                } else {
                    sb.append("<PRI_KEY_TYPE>--</PRI_KEY_TYPE>");
                }
                if (resultSet.getString("PRI_KEY_VAL") != null && !"".equals(resultSet.getString("PRI_KEY_VAL"))) {
                    sb.append("<PRI_KEY_VAL>" + resultSet.getString("PRI_KEY_VAL") + "</PRI_KEY_VAL>");
                } else {
                    sb.append("<PRI_KEY_VAL>--</PRI_KEY_VAL>");
                }
                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }

                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                if (resultSet.getString("ORG_FILEPATH") != null) {
                    if (new File(resultSet.getString("ORG_FILEPATH")).exists() && new File(resultSet.getString("ORG_FILEPATH")).isFile()) {
                        sb.append("<ORG_FILEPATH>" + resultSet.getString("ORG_FILEPATH") + "</ORG_FILEPATH>");
                    } else {
                        sb.append("<ORG_FILEPATH>No File</ORG_FILEPATH>");
                    }
                } else {
                    sb.append("<ORG_FILEPATH>No File</ORG_FILEPATH>");
                }
                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }
                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }
                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }
                String sapDetails = DataSourceDataProvider.getInstance().getSapDetails(instanceid, ponum);
                if (!sapDetails.equals("None")) {
                    sb.append("<SAP_DETAILS>YES</SAP_DETAILS>");
                    String sapDetailsInfo[] = sapDetails.split("\\|");
                    sb.append("<SAP_USER>" + sapDetailsInfo[0] + "</SAP_USER>");
                    sb.append("<IDOC_NUMBER>" + sapDetailsInfo[1] + "</IDOC_NUMBER>");
                    sb.append("<PO_NUMBER>" + sapDetailsInfo[2] + "</PO_NUMBER>");
                    sb.append("<PO_DATE>" + sapDetailsInfo[3] + "</PO_DATE>");
                    sb.append("<IDOC_STATUS_CODE>" + sapDetailsInfo[4] + "</IDOC_STATUS_CODE>");
                    sb.append("<IDOC_STATUS_DESCRIPTION>" + sapDetailsInfo[5] + "</IDOC_STATUS_DESCRIPTION>");
                } else {
                    sb.append("<SAP_DETAILS>NO</SAP_DETAILS>");
                }
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("doc deatails-------" + sb.toString());
        return sb.toString();
    }

    public String getPaymentDetails(String fileId, String database) throws ServiceLocatorException {
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        if ("ARCHIVE".equals(database)) {
            queryString = "SELECT ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.ISA_NUMBER,ARCHIVE_FILES.GS_CONTROL_NUMBER,ARCHIVE_FILES.ST_CONTROL_NUMBER,"
                    + "ARCHIVE_FILES.TRANSACTION_TYPE,ARCHIVE_FILES.SEC_KEY_VAL,ARCHIVE_PAYMENT.INVOICE_NUMBER,ARCHIVE_FILES.FILE_ID as FILE_ID, ARCHIVE_PAYMENT.Check_Number as Check_Number, "
                    + "ARCHIVE_FILES.SENDER_ID as SENDER_ID, ARCHIVE_FILES.RECEIVER_ID as RECEIVER_ID,"
                    + "ARCHIVE_FILES.PRE_TRANS_FILEPATH as PRE_TRANS_FILEPATH, ARCHIVE_FILES.POST_TRANS_FILEPATH as POST_TRANS_FILEPATH, "
                    + "ARCHIVE_FILES.ORG_FILEPATH as ORG_FILEPATH,ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME,ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "ARCHIVE_FILES.ERR_MESSAGE as ERR_MESSAGE,ARCHIVE_FILES.STATUS as STATUS, ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME "
                    + "FROM ARCHIVE_PAYMENT LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_PAYMENT.FILE_ID=ARCHIVE_FILES.FILE_ID) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=ARCHIVE_FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = ARCHIVE_FILES.RECEIVER_ID) "
                    + "WHERE FLOWFLAG like 'M' AND ARCHIVE_FILES.FILE_ID = '" + fileId + "'";
        } else {
            queryString = "SELECT FILES.FILE_TYPE,FILES.ISA_NUMBER,FILES.GS_CONTROL_NUMBER,FILES.ST_CONTROL_NUMBER,FILES.TRANSACTION_TYPE,FILES.SEC_KEY_VAL,PAYMENT.INVOICE_NUMBER,FILES.FILE_ID as FILE_ID, PAYMENT.Check_Number as Check_Number, "
                    + "FILES.SENDER_ID as SENDER_ID, FILES.RECEIVER_ID as RECEIVER_ID,"
                    + "FILES.PRE_TRANS_FILEPATH as PRE_TRANS_FILEPATH, FILES.POST_TRANS_FILEPATH as POST_TRANS_FILEPATH, "
                    + "FILES.ORG_FILEPATH as ORG_FILEPATH,FILES.ISA_DATE as ISA_DATE,FILES.ISA_TIME as ISA_TIME,FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "FILES.ERR_MESSAGE as ERR_MESSAGE,FILES.STATUS as STATUS, FILES.ACK_FILE_ID as ACK_FILE_ID,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME "
                    + "FROM Payment LEFT OUTER JOIN FILES ON (PAYMENT.FILE_ID=FILES.FILE_ID) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = FILES.RECEIVER_ID) "
                    + "WHERE FLOWFLAG like 'M' AND FILES.FILE_ID = '" + fileId + "'";
        }

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILE_ID>" + resultSet.getString("FILE_ID") + "</FILE_ID>");
                } else {
                    sb.append("<FILE_ID>--</FILE_ID>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISA_DATE>" + resultSet.getString("ISA_DATE") + "</ISA_DATE>");
                } else {
                    sb.append("<ISA_DATE>--</ISA_DATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISA_TIME>" + resultSet.getString("ISA_TIME") + "</ISA_TIME>");
                } else {
                    sb.append("<ISA_TIME>--</ISA_TIME>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDER_ID>" + resultSet.getString("SENDER_ID") + "</SENDER_ID>");
                } else {
                    sb.append("<SENDER_ID>--</SENDER_ID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVER_ID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVER_ID>");
                } else {
                    sb.append("<RECEIVER_ID>--</RECEIVER_ID>");
                }
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILE_TYPE>" + resultSet.getString("FILE_TYPE") + "</FILE_TYPE>");
                } else {
                    sb.append("<FILE_TYPE>--</FILE_TYPE>");
                }
                if (resultSet.getString("SENDER_NAME") != null && !"".equals(resultSet.getString("SENDER_NAME"))) {
                    sb.append("<SENDER_NAME>" + resultSet.getString("SENDER_NAME") + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_NAME") != null && !"".equals(resultSet.getString("RECEIVER_NAME"))) {
                    sb.append("<RECEIVER_NAME>" + resultSet.getString("RECEIVER_NAME") + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("Check_Number") != null && !"".equals(resultSet.getString("Check_Number"))) {
                    sb.append("<Check_Number>" + resultSet.getString("Check_Number") + "</Check_Number>");
                } else {
                    sb.append("<Check_Number>NO</Check_Number>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISA_NUMBER>" + resultSet.getString("ISA_NUMBER") + "</ISA_NUMBER>");
                } else {
                    sb.append("<ISA_NUMBER>--</ISA_NUMBER>");
                }
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>--</GS_CONTROL_NUMBER>");
                }
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>--</ST_CONTROL_NUMBER>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("SEC_KEY_VAL") != null && !"".equals(resultSet.getString("SEC_KEY_VAL"))) {
                    sb.append("<SEC_KEY_VAL>" + resultSet.getString("SEC_KEY_VAL") + "</SEC_KEY_VAL>");
                } else {
                    sb.append("<SEC_KEY_VAL>NO</SEC_KEY_VAL>");
                }
                if (resultSet.getString("INVOICE_NUMBER") != null && !"".equals(resultSet.getString("INVOICE_NUMBER"))) {
                    sb.append("<INVOICE_NUMBER>" + resultSet.getString("INVOICE_NUMBER") + "</INVOICE_NUMBER>");
                } else {
                    sb.append("<INVOICE_NUMBER>NO</INVOICE_NUMBER>");
                }
                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                if (resultSet.getString("ORG_FILEPATH") != null) {
                    if (new File(resultSet.getString("ORG_FILEPATH")).exists() && new File(resultSet.getString("ORG_FILEPATH")).isFile()) {
                        sb.append("<ORGFILEPATH>" + resultSet.getString("ORG_FILEPATH") + "</ORGFILEPATH>");
                    } else {
                        sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                    }
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }
                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILE>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILE>");
                    } else {
                        sb.append("<ACKFILE>No File</ACKFILE>");
                    }
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }
                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }
                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }
                String sapDetails = DataSourceDataProvider.getInstance().getSapDetails(resultSet.getString("FILE_ID"), resultSet.getString("SEC_KEY_VAL"));

                if (!sapDetails.equals("None")) {
                    sb.append("<SAP_DETAILS>YES</SAP_DETAILS>");
                    String sapDetailsInfo[] = sapDetails.split("\\|");
                    sb.append("<SAP_USER>" + sapDetailsInfo[0] + "</SAP_USER>");
                    sb.append("<IDOC_NUMBER>" + sapDetailsInfo[1] + "</IDOC_NUMBER>");
                    sb.append("<PO_NUMBER>" + sapDetailsInfo[2] + "</PO_NUMBER>");
                    sb.append("<PO_DATE>" + sapDetailsInfo[3] + "</PO_DATE>");
                    sb.append("<IDOC_STATUS_CODE>" + sapDetailsInfo[4] + "</IDOC_STATUS_CODE>");
                    sb.append("<IDOC_STATUS_DESCRIPTION>" + sapDetailsInfo[5] + "</IDOC_STATUS_DESCRIPTION>");
                } else {
                    sb.append("<SAP_DETAILS>NO</SAP_DETAILS>");
                }
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String getDocCopy(String poList, String type, String database) throws ServiceLocatorException {
        String resultString = "";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        String pos = "";
        String file_id = "";
        String queryString = "";
        StringTokenizer st = new StringTokenizer(poList, "^");
        while (st.hasMoreTokens()) {
            String firstToken = st.nextToken();
            StringTokenizer st1 = new StringTokenizer(firstToken, "|");
            while (st1.hasMoreTokens()) {
                String poNum = st1.nextToken();
                String fileId = st1.nextToken();
                if (poNum != null && fileId != null) {
                    if (type.equals("POST")) {
                        if ("ARCHIVE".equals(database)) {
                            queryString = "select distinct(ARCHIVE_PO.PO_NUMBER) as PO_NUMBER,ARCHIVE_FILES.FILE_ID,"
                                    + "POST_TRANS_FILEPATH,RE_TRANSLATE_FILEPATH from ARCHIVE_PO "
                                    + "LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_PO.PO_NUMBER=ARCHIVE_FILES.PRI_KEY_VAL) where "
                                    + "ARCHIVE_PO.PO_NUMBER LIKE ('" + poNum + "') and ARCHIVE_FILES.FILE_ID LIKE '" + fileId + "'";
                        } else {
                            queryString = "select distinct(PO.PO_NUMBER) as PO_NUMBER,FILES.FILE_ID,"
                                    + "POST_TRANS_FILEPATH,RE_TRANSLATE_FILEPATH from PO "
                                    + "LEFT OUTER JOIN FILES ON (PO.PO_NUMBER=FILES.PRI_KEY_VAL) where "
                                    + "PO.PO_NUMBER LIKE ('" + poNum + "') and FILES.FILE_ID LIKE '" + fileId + "'";
                        }
                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            statement = connection.prepareStatement(queryString);
                            resultSet = statement.executeQuery();
                            int i = 0;
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {
                                    srcDest.put(new File(resultSet.getString(3)), new File(resultSet.getString(4)));
                                    isGetting = true;
                                }
                            }
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().copyPostMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                String result = DataSourceDataProvider.getInstance().UpdateReProcessStatus("RETRANSMITTED", fileId, poNum, "M");
                                resultString = resultString + "PO : " + poNum + " was retransmitted successfully." + "\n";
                            } else {
                                resultString = resultString + "PO : " + poNum + " = " + "Source file not found!" + "\n";
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } catch (ServiceLocatorException sle) {
                            sle.printStackTrace();
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
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else {
                        if ("ARCHIVE".equals(database)) {
                            queryString = "select distinct(ARCHIVE_PO.PO_NUMBER) as PO_NUMBER,ARCHIVE_FILES.FILE_ID,"
                                    + "PRE_TRANS_FILEPATH,RE_SUBMIT_FILEPATH,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                                    + "ARCHIVE_FILES.TRANSACTION_TYPE from ARCHIVE_PO LEFT OUTER JOIN ARCHIVE_FILES "
                                    + "ON (ARCHIVE_PO.PO_NUMBER=ARCHIVE_FILES.PRI_KEY_VAL) where ARCHIVE_PO.PO_NUMBER LIKE ('" + poNum + "') "
                                    + "and ARCHIVE_FILES.FILE_ID LIKE '" + fileId + "'";
                        } else {
                            queryString = "select distinct(PO.PO_NUMBER) as PO_NUMBER,FILES.FILE_ID,"
                                    + "PRE_TRANS_FILEPATH,RE_SUBMIT_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.TRANSACTION_TYPE from PO LEFT OUTER JOIN FILES "
                                    + "ON (PO.PO_NUMBER=FILES.PRI_KEY_VAL) where PO.PO_NUMBER LIKE ('" + poNum + "') "
                                    + "and FILES.FILE_ID LIKE '" + fileId + "'";
                        }
                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            statement = connection.prepareStatement(queryString);
                            resultSet = statement.executeQuery();
                            int i = 0;
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                String newFilePath = resultSet.getString(4) + "|" + poNum + "|" + fileId + "|" + resultSet.getString(5) + "|" + resultSet.getString(6) + "|" + resultSet.getString(7);
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {
                                    // System.out.println("pre----"+resultSet.getString(3));
                                    //System.out.println("resubmit----"+resultSet.getString(4));
                                    srcDest.put(new File(resultSet.getString(3)), new File(resultSet.getString(4)));
                                    //srcDest.put(new File(resultSet.getString(3)), newFilePath);
                                    isGetting = true;
                                }
                            }
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().copyPreMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                String result = DataSourceDataProvider.getInstance().UpdateReProcessStatus("RESUBMITTED", fileId, poNum, "M");
                                resultString = resultString + "PO : " + poNum + " was resubmission successfully." + "\n";
                            } else {
                                resultString = resultString + "PO : " + poNum + " = " + "Source file not found!" + "\n";
                            }

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } catch (ServiceLocatorException sle) {
                            sle.printStackTrace();
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
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return resultString;
    }

    public String getDocASNCopy(String asnList, String type) throws ServiceLocatorException {
        String resultString = "";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        String pos = "";
        String file_id = "";
        String queryString = "";
        StringTokenizer st = new StringTokenizer(asnList, "^");
        while (st.hasMoreTokens()) {
            String firstToken = st.nextToken();
            StringTokenizer st1 = new StringTokenizer(firstToken, "|");
            while (st1.hasMoreTokens()) {
                String asnNum = st1.nextToken();
                String fileId = st1.nextToken();
                if (asnNum != null && fileId != null) {
                    if (type.equals("POST")) {
                        queryString = "select distinct(ASN.ASN_NUMBER) as ASN_NUMBER,FILES.FILE_ID,"
                                + "POST_TRANS_FILEPATH,RE_TRANSLATE_FILEPATH from ASN LEFT OUTER JOIN FILES "
                                + "ON (ASN.ASN_NUMBER=FILES.PRI_KEY_VAL)"
                                + "where ASN.ASN_NUMBER LIKE ('" + asnNum + "') and FILES.FILE_ID LIKE '" + fileId + "'";
                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            statement = connection.prepareStatement(queryString);
                            resultSet = statement.executeQuery();
                            int i = 0;
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {
                                    srcDest.put(new File(resultSet.getString(3)), new File(resultSet.getString(4)));
                                    isGetting = true;
                                }
                            }
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().copyPostMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                String result = DataSourceDataProvider.getInstance().UpdateReProcessStatus("RETRANSMITTED", fileId, asnNum, "M");
                                resultString = resultString + "PO : " + asnNum + " was retransmitted successfully." + "\n";
                            } else {
                                resultString = resultString + "PO : " + asnNum + " = " + "Source file not found!" + "\n";
                            }

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } catch (ServiceLocatorException sle) {
                            sle.printStackTrace();
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
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else {
                        queryString = "select distinct(ASN.ASN_NUMBER) as ASN_NUMBER,FILES.FILE_ID,"
                                + "PRE_TRANS_FILEPATH,RE_SUBMIT_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.TRANSACTION_TYPE from ASN "
                                + "LEFT OUTER JOIN FILES ON (ASN.ASN_NUMBER=FILES.PRI_KEY_VAL)"
                                + "where ASN.ASN_NUMBER LIKE ('" + asnNum + "') and FILES.FILE_ID LIKE '" + fileId + "'";
                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            statement = connection.prepareStatement(queryString);
                            resultSet = statement.executeQuery();
                            int i = 0;
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                String newFilePath = resultSet.getString(4) + "|" + asnNum + "|" + fileId + "|" + resultSet.getString(5) + "|" + resultSet.getString(6) + "|" + resultSet.getString(7);
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {
                                    srcDest.put(new File(resultSet.getString(3)), newFilePath);
                                    isGetting = true;
                                }
                            }
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().copyPreMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                String result = DataSourceDataProvider.getInstance().UpdateReProcessStatus("RESUBMITTED", fileId, asnNum, "M");
                                resultString = resultString + "ASN : " + asnNum + " was resubmission successfully." + "\n";
                            } else {
                                resultString = resultString + "ASN : " + asnNum + " = " + "Source file not found!" + "\n";
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } catch (ServiceLocatorException sle) {
                            sle.printStackTrace();
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
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return resultString;
    }

    public String getInvCopy(String invList, String type) throws ServiceLocatorException {
        String resultString = "";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        String pos = "";
        String file_id = "";
        String queryString = "";
        StringTokenizer st = new StringTokenizer(invList, "^");
        while (st.hasMoreTokens()) {
            String firstToken = st.nextToken();
            StringTokenizer st1 = new StringTokenizer(firstToken, "|");
            while (st1.hasMoreTokens()) {
                String invNum = st1.nextToken();
                String fileId = st1.nextToken();
                if (invNum != null && fileId != null) {
                    if (type.equals("POST")) {
                        queryString = "select distinct(INVOICE.INVOICE_NUMBER) as INVOICE_NUMBER,FILES.FILE_ID,"
                                + "POST_TRANS_FILEPATH,RE_TRANSLATE_FILEPATH from INVOICE LEFT OUTER JOIN FILES "
                                + "ON (INVOICE.INVOICE_NUMBER=FILES.PRI_KEY_VAL)"
                                + "where INVOICE.INVOICE_NUMBER LIKE ('" + invNum + "') and FILES.FILE_ID LIKE '" + fileId + "'";
                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            statement = connection.prepareStatement(queryString);
                            resultSet = statement.executeQuery();
                            int i = 0;
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {
                                    srcDest.put(new File(resultSet.getString(3)), new File(resultSet.getString(4)));
                                    isGetting = true;
                                }
                            }
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().copyPostMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                String result = DataSourceDataProvider.getInstance().UpdateReProcessStatus("RETRANSMITTED", fileId, invNum, "M");
                                resultString = resultString + "INV : " + invNum + " was retransmitted successfully." + "\n";
                            } else {
                                resultString = resultString + "INV : " + invNum + " = " + "Source file not found!" + "\n";
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } catch (ServiceLocatorException sle) {
                            sle.printStackTrace();
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
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else {
                        queryString = "select distinct(INVOICE.INVOICE_NUMBER) as INVOICE_NUMBER,FILES.FILE_ID,"
                                + "PRE_TRANS_FILEPATH,RE_SUBMIT_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.TRANSACTION_TYPE from INVOICE "
                                + "LEFT OUTER JOIN FILES ON (INVOICE.INVOICE_NUMBER=FILES.PRI_KEY_VAL)"
                                + "where INVOICE.INVOICE_NUMBER LIKE ('" + invNum + "') and FILES.FILE_ID LIKE '" + fileId + "'";
                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            statement = connection.prepareStatement(queryString);
                            resultSet = statement.executeQuery();
                            int i = 0;
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                String newFilePath = resultSet.getString(4) + "|" + invNum + "|" + fileId + "|" + resultSet.getString(5) + "|" + resultSet.getString(6) + "|" + resultSet.getString(7);
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {
                                    srcDest.put(new File(resultSet.getString(3)), newFilePath);
                                    isGetting = true;
                                }
                            }
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().copyPreMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                String result = DataSourceDataProvider.getInstance().UpdateReProcessStatus("RESUBMITTED", fileId, invNum, "M");
                                resultString = resultString + "INV : " + invNum + " was resubmission successfully." + "\n";
                            } else {
                                resultString = resultString + "INV : " + invNum + " = " + "Source file not found!" + "\n";
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } catch (ServiceLocatorException sle) {
                            sle.printStackTrace();
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
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return resultString;
    }

    public String getPaymentCopy(String paymentList, String type) throws ServiceLocatorException {
        String resultString = "";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        String pos = "";
        String file_id = "";
        String queryString = "";
        StringTokenizer st = new StringTokenizer(paymentList, "^");
        while (st.hasMoreTokens()) {
            String firstToken = st.nextToken();
            StringTokenizer st1 = new StringTokenizer(firstToken, "|");
            while (st1.hasMoreTokens()) {
                String invNum = st1.nextToken();
                String fileId = st1.nextToken();
                if (invNum != null && fileId != null) {
                    if (type.equals("POST")) {
                        queryString = "select distinct(PAYMENT.CHECK_NUMBER) as CHECK_NUMBER,FILES.FILE_ID,"
                                + "POST_TRANS_FILEPATH,RE_TRANSLATE_FILEPATH from PAYMENT LEFT OUTER JOIN FILES "
                                + "ON (PAYMENT.CHECK_NUMBER=FILES.PRI_KEY_VAL)"
                                + "where PAYMENT.CHECK_NUMBER LIKE ('" + invNum + "') and FILES.FILE_ID LIKE '" + fileId + "'";
                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            statement = connection.prepareStatement(queryString);
                            resultSet = statement.executeQuery();
                            int i = 0;
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {
                                    srcDest.put(new File(resultSet.getString(3)), new File(resultSet.getString(4)));
                                    isGetting = true;
                                }
                            }
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().copyPostMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                String result = DataSourceDataProvider.getInstance().UpdateReProcessStatus("RETRANSMITTED", fileId, invNum, "M");
                                resultString = resultString + "INV : " + invNum + " was retransmitted successfully." + "\n";
                            } else {
                                resultString = resultString + "INV : " + invNum + " = " + "Source file not found!" + "\n";
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } catch (ServiceLocatorException sle) {
                            sle.printStackTrace();
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
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    } else {
                        queryString = "select distinct(PAYMENT.CHECK_NUMBER) as CHECK_NUMBER,FILES.FILE_ID,"
                                + "PRE_TRANS_FILEPATH,RE_SUBMIT_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.TRANSACTION_TYPE from PAYMENT "
                                + "LEFT OUTER JOIN FILES ON (PAYMENT.CHECK_NUMBER=FILES.PRI_KEY_VAL)"
                                + "where PAYMENT.CHECK_NUMBER LIKE ('" + invNum + "') and FILES.FILE_ID LIKE '" + fileId + "'";
                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            statement = connection.prepareStatement(queryString);
                            resultSet = statement.executeQuery();
                            int i = 0;
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                String newFilePath = resultSet.getString(4) + "|" + invNum + "|" + fileId + "|" + resultSet.getString(5) + "|" + resultSet.getString(6) + "|" + resultSet.getString(7);
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {
                                    srcDest.put(new File(resultSet.getString(3)), newFilePath);
                                    isGetting = true;
                                }
                            }
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().copyPreMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            if (res.equals("Success")) {
                                String result = DataSourceDataProvider.getInstance().UpdateReProcessStatus("RESUBMITTED", fileId, invNum, "M");
                                resultString = resultString + "INV : " + invNum + " was resubmission successfully." + "\n";
                            } else {
                                resultString = resultString + "INV : " + invNum + " = " + "Source file not found!" + "\n";
                            }
                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } catch (ServiceLocatorException sle) {
                            sle.printStackTrace();
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
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
        return resultString;
    }

    public String getLoadCopy(String loadList, String type, String database) throws ServiceLocatorException {
        // System.out.println("In getDocCopy!!!");
        String resultString = "";
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        //  System.out.println("poList111"+poList);

        String pos = "";
        String file_id = "";
        String queryString = "";
        StringTokenizer st = new StringTokenizer(loadList, "^");
        // System.out.println("start token");
        while (st.hasMoreTokens()) {

            String firstToken = st.nextToken();
            //  System.out.println("firetToken "+firstToken);
            StringTokenizer st1 = new StringTokenizer(firstToken, "|");
            while (st1.hasMoreTokens()) {
                //  System.out.println("po-->"+st1.nextToken());
                String shipmentId = st1.nextToken();
                String fileId = st1.nextToken();

                if (shipmentId != null && fileId != null) {
                    if (type.equals("POST")) {

                        /**
                         * REtransmitt.. Posttranslatefile...
                         *
                         */
                        //fileDestPath = Properties.getProperty("mscvp.postpath");
                        // queryString = "select POST_TRANS_FILEPATH,RE_TRANSLATE_FILEPATH from PO LEFT OUTER JOIN FILES ON (PO.PO_NUMBER=FILES.SEC_KEY_VAL) where PO_NUMBER IN ("+pos.substring(0, pos.length()-1)+")";
                        if ("ARCHIVE".equals(database)) {
                            queryString = "select distinct(ARCHIVE_TRANSPORT_LOADTENDER.SHIPMENT_ID) as SHIPMENT_ID,ARCHIVE_FILES.FILE_ID,"
                                    + "POST_TRANS_FILEPATH,RE_TRANSLATE_FILEPATH from ARCHIVE_TRANSPORT_LOADTENDER "
                                    + "LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_TRANSPORT_LOADTENDER.SHIPMENT_ID=ARCHIVE_FILES.PRI_KEY_VAL) where "
                                    + "ARCHIVE_TRANSPORT_LOADTENDER.SHIPMENT_ID LIKE ('" + shipmentId + "') and ARCHIVE_FILES.FILE_ID LIKE '" + fileId + "'";
                        } else {
                            queryString = "select distinct(TRANSPORT_LOADTENDER.SHIPMENT_ID) as SHIPMENT_ID,FILES.FILE_ID,"
                                    + "POST_TRANS_FILEPATH,RE_TRANSLATE_FILEPATH from TRANSPORT_LOADTENDER "
                                    + "LEFT OUTER JOIN FILES ON (TRANSPORT_LOADTENDER.SHIPMENT_ID=FILES.PRI_KEY_VAL) where "
                                    + "TRANSPORT_LOADTENDER.SHIPMENT_ID LIKE ('" + shipmentId + "') and FILES.FILE_ID LIKE '" + fileId + "'";
                        }

                        boolean isGetting = false;
                        try {
                            connection = ConnectionProvider.getInstance().getConnection();
                            statement = connection.prepareStatement(queryString);
                            System.out.println("Post Query-->" + queryString);
                            resultSet = statement.executeQuery();
                            // sb.append("<xml version=\"1.0\">");
                            //sb.append("<DETAILS>");
                            int i = 0;
                            // ArrayList al=new ArrayList();
                            Map srcDest = new HashMap();
                            // System.out.println("Before Loop!!");
                            //String newFilePath = resultSet.getString(4)+"|"+poNum+"|"+fileId;
                            while (resultSet.next()) {
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {

                                    srcDest.put(new File(resultSet.getString(3)), new File(resultSet.getString(4)));
                                    //srcDest.put(new File(resultSet.getString(3)),newFilePath);
                                    isGetting = true;
                                }
                            }
                            // System.out.println("before!!!");
                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().loadTenderCopyPostMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            //String res = FileUtility.getInstance().copyMapFiles(srcDest);
                            //String res = FileUtility.getInstance().copyFiles(al,new File(fileDestPath));

                            if (res.equals("Success")) {
                                String result = DataSourceDataProvider.getInstance().UpdateReProcessStatus("RETRANSMITTED", fileId, shipmentId, "L");
                                resultString = resultString + "ShipmentId : " + shipmentId + " was retransmitted successfully." + "\n";
                            } else {
                                resultString = resultString + "ShipmentId : " + shipmentId + " = " + "Source file not found!" + "\n";
                            }

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } catch (ServiceLocatorException sle) {
                            sle.printStackTrace();
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
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                        //end

                    } else {
                        /**
                         * PREtranslation :: Resubmit... working fine
                         *
                         */
                        if ("ARCHIVE".equals(database)) {//ARCHIVE_FILES
                            queryString = "select distinct(ARCHIVE_TRANSPORT_LOADTENDER.SHIPMENT_ID) as SHIPMENT_ID,ARCHIVE_FILES.FILE_ID,"
                                    + "PRE_TRANS_FILEPATH,RE_SUBMIT_FILEPATH,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_FILES.TRANSACTION_TYPE from ARCHIVE_TRANSPORT_LOADTENDER LEFT OUTER JOIN ARCHIVE_FILES "
                                    + "ON (ARCHIVE_TRANSPORT_LOADTENDER.SHIPMENT_ID=ARCHIVE_FILES.PRI_KEY_VAL) where ARCHIVE_TRANSPORT_LOADTENDER.SHIPMENT_ID LIKE ('" + shipmentId + "') "
                                    + "and ARCHIVE_FILES.FILE_ID LIKE '" + fileId + "'";
                        } else {
                            queryString = "select distinct(TRANSPORT_LOADTENDER.SHIPMENT_ID) as SHIPMENT_ID,FILES.FILE_ID,"
                                    + "PRE_TRANS_FILEPATH,RE_SUBMIT_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.TRANSACTION_TYPE from TRANSPORT_LOADTENDER LEFT OUTER JOIN FILES "
                                    + "ON (TRANSPORT_LOADTENDER.SHIPMENT_ID=FILES.PRI_KEY_VAL) where TRANSPORT_LOADTENDER.SHIPMENT_ID LIKE ('" + shipmentId + "') "
                                    + "and FILES.FILE_ID LIKE '" + fileId + "'";
                        }

                        //start
                        boolean isGetting = false;
                        try {
                            System.out.println("Pre Query-->" + queryString);
                            connection = ConnectionProvider.getInstance().getConnection();
                            statement = connection.prepareStatement(queryString);
                            resultSet = statement.executeQuery();
                            // sb.append("<xml version=\"1.0\">");
                            //sb.append("<DETAILS>");
                            int i = 0;
                            // ArrayList al=new ArrayList();
                            Map srcDest = new HashMap();
                            while (resultSet.next()) {
                                if (resultSet.getString(3) != null && resultSet.getString(4) != null) {

                                    srcDest.put(new File(resultSet.getString(3)), new File(resultSet.getString(4)));
                                    //srcDest.put(new File(resultSet.getString(3)),newFilePath);
                                    isGetting = true;
                                }
                            }

                            String res = null;
                            if (!srcDest.isEmpty()) {
                                res = FileUtility.getInstance().loadTenderCopyPreMapFiles(srcDest);
                            } else {
                                res = "Error";
                            }
                            //String res = FileUtility.getInstance().copyMapFiles(srcDest);
                            //String res = FileUtility.getInstance().copyFiles(al,new File(fileDestPath));

                            if (res.equals("Success")) {
                                String result = DataSourceDataProvider.getInstance().UpdateReProcessStatus("RESUBMITTED", fileId, shipmentId, "L");
                                resultString = resultString + "ShipmentId : " + shipmentId + " was resubmission successfully." + "\n";
                            } else {
                                resultString = resultString + "ShipmentId : " + shipmentId + " = " + "Source file not found!" + "\n";
                            }

                        } catch (SQLException ex) {
                            ex.printStackTrace();
                        } catch (ServiceLocatorException sle) {
                            sle.printStackTrace();
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
                            } catch (SQLException ex) {
                                ex.printStackTrace();
                            }
                        }
                        //end

                    }

                    //  System.out.println("queryString-->"+queryString);
                }

            }

        }

        return resultString;
    }

    /**
     * Life cycle Details
     *
     */
    public String getLifeCycleDetails(String poNumber, String fileId, String type, String database) throws ServiceLocatorException {

        String resultXml = "";

        LifecycleUtility lifecycleUtility = new LifecycleUtility();

        if (type.equalsIgnoreCase("PO")) {
            // System.out.println("in po");
            poLifecycleBean = lifecycleUtility.getLFCPoDetails(poNumber, fileId, database);
            resultXml = getPoDetails(poLifecycleBean);
        }

        if (type.equalsIgnoreCase("ASN")) {
            // System.out.println("in ASN");
            asnLifecycleBean = lifecycleUtility.getLFCAsnDetails(poNumber, fileId, database);
            resultXml = getASNDetails(asnLifecycleBean);
        }

        if (type.equalsIgnoreCase("INV")) {
            // System.out.println("in INV");
            invoiceLifecycleBean = lifecycleUtility.getLFCInvoiceDetails(poNumber, fileId, database);
            resultXml = getINVDetails(invoiceLifecycleBean);
        }
        if (type.equalsIgnoreCase("PAYMENT")) {
            //  System.out.println("in payment");
            paymentLifecycleBean = lifecycleUtility.getLFCPaymentDetails(poNumber, fileId, database);
            resultXml = getPayDetails(paymentLifecycleBean);
        }
        return resultXml;

    }

    /**
     * PO
     *
     */
    private String getPoDetails(PoLifecycleBean poLifecycleBean) throws ServiceLocatorException {
        StringBuffer sb = new StringBuffer();
        boolean isGetting = false;

        sb.append("<xml version=\"1.0\">");
        sb.append("<DETAILS>");
        if (poLifecycleBean.getRes().equals("1")) {
            sb.append("<DETAIL><VALID>true</VALID>");
            sb.append("<FILEID>" + poLifecycleBean.getFileId() + "</FILEID>");
            sb.append("<SENDER_ID>" + poLifecycleBean.getSenderId() + "</SENDER_ID>");
            sb.append("<RECEIVER_ID>" + poLifecycleBean.getRecId() + "</RECEIVER_ID>");

            sb.append("<SENDER_NAME>" + poLifecycleBean.getSenName() + "</SENDER_NAME>");
            sb.append("<RECEIVER_NAME>" + poLifecycleBean.getRecName() + "</RECEIVER_NAME>");

            sb.append("<TRAN_NUMBER>" + poLifecycleBean.getTranType() + "</TRAN_NUMBER>");

            /*
             * Newly added start
             */
            sb.append("<PO_NUMBER>" + poLifecycleBean.getPoNumber() + "</PO_NUMBER>");
            sb.append("<PO_DATE>" + poLifecycleBean.getPodate() + "</PO_DATE>");
            sb.append("<PO_STATUS>" + poLifecycleBean.getPoStatus() + "</PO_STATUS>");
            sb.append("<SO_NUMBER>" + poLifecycleBean.getSoNumber() + "</SO_NUMBER>");
            sb.append("<SAPIDOC_NUMBER>" + poLifecycleBean.getSapIdocNum() + "</SAPIDOC_NUMBER>");
            sb.append("<ITEM_QTY>" + poLifecycleBean.getIteamQty() + "</ITEM_QTY>");

            sb.append("<ASN_NUMBER>" + poLifecycleBean.getAsnNumber() + "</ASN_NUMBER>");
            sb.append("<BOL_NUMBER>" + poLifecycleBean.getBolNumber() + "</BOL_NUMBER>");
            sb.append("<ISA_NUMBER>" + poLifecycleBean.getIsaCtrlNum() + "</ISA_NUMBER>");
            sb.append("<ISA_DATE>" + poLifecycleBean.getIsaDate() + "</ISA_DATE>");
            sb.append("<ISA_TIME>" + poLifecycleBean.getIsaTime() + "</ISA_TIME>");
            sb.append("<INV_NUMBER>" + poLifecycleBean.getInvNumber() + "</INV_NUMBER>");
            sb.append("<INV_AMOUNT>" + poLifecycleBean.getInvAmt() + "</INV_AMOUNT>");
            sb.append("<CHEQUE_NUMBER>" + poLifecycleBean.getChequeNum() + "</CHEQUE_NUMBER>");
            sb.append("<TRANS_TYPE>" + poLifecycleBean.getTranType() + "</TRANS_TYPE>");
            sb.append("<DATETIME>" + poLifecycleBean.getDatetimeRec() + "</DATETIME>");

            sb.append("<STATUS>" + poLifecycleBean.getStatus() + "</STATUS>");
            sb.append("<DIRECTION>" + poLifecycleBean.getDirection().toLowerCase() + "</DIRECTION>");
            sb.append("<GS_CONTROL_NUMBER>" + poLifecycleBean.getGsCtrlNum() + "</GS_CONTROL_NUMBER>");
            sb.append("<ST_CONTROL_NUMBER>" + poLifecycleBean.getStCtrlNum() + "</ST_CONTROL_NUMBER>");
            sb.append("<PRI_KEY_TYPE>" + poLifecycleBean.getPrimary_key_type()+ "</PRI_KEY_TYPE>");
            sb.append("<PRI_KEY_VAL>" + poLifecycleBean.getPrimary_key_val()+ "</PRI_KEY_VAL>");
            /*
             * End
             */
            if (poLifecycleBean.getPreFile() != null) {
                if (new File(poLifecycleBean.getPreFile()).exists() && new File(poLifecycleBean.getPreFile()).isFile()) {
                    sb.append("<PRETRANSFILEPATH>" + poLifecycleBean.getPreFile() + "</PRETRANSFILEPATH>");
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
            } else {
                sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
            }

            if (poLifecycleBean.getPostTranFile() != null) {
                if (new File(poLifecycleBean.getPostTranFile()).exists() && new File(poLifecycleBean.getPostTranFile()).isFile()) {
                    sb.append("<POSTTRANSFILEPATH>" + poLifecycleBean.getPostTranFile() + "</POSTTRANSFILEPATH>");
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
            } else {
                sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
            }

            if (poLifecycleBean.getOrgFile() != null) {
                if (new File(poLifecycleBean.getOrgFile()).exists() && new File(poLifecycleBean.getOrgFile()).isFile()) {
                    sb.append("<ORGFILEPATH>" + poLifecycleBean.getOrgFile() + "</ORGFILEPATH>");
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }
            } else {
                sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
            }

            if (poLifecycleBean.getAckFile() != null) {
                if (new File(poLifecycleBean.getAckFile()).exists() && new File(poLifecycleBean.getAckFile()).isFile()) {
                    sb.append("<ACKFILE>" + poLifecycleBean.getAckFile() + "</ACKFILE>");
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }
            } else {
                sb.append("<ACKFILE>No File</ACKFILE>");
            }

            sb.append("</DETAIL>");
            isGetting = true;
        }
        if (!isGetting) {
            isGetting = false;
            sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
        }

        sb.append("</DETAILS>");
        sb.append("</xml>");

        // System.out.println("poxml --->"+sb.toString());
        return sb.toString();

    }

    /**
     * ASN
     *
     */
    private String getASNDetails(AsnLifecycleBean asnLifecycleBean) throws ServiceLocatorException {
        StringBuffer sb = new StringBuffer();
        boolean isGetting = false;

        sb.append("<xml version=\"1.0\">");
        sb.append("<DETAILS>");
        if (asnLifecycleBean.getRes().equals("1")) {
            sb.append("<DETAIL><VALID>true</VALID>");
            /*sb.append("<FILEID>" +asnLifecycleBean.getFileId() + "</FILEID>");
             sb.append("<SENDER_ID>" +asnLifecycleBean.getSenderId() + "</SENDER_ID>");
             sb.append("<RECEIVER_ID>" +asnLifecycleBean.getRecId() + "</RECEIVER_ID>");
             sb.append("<TRAN_NUMBER>" +asnLifecycleBean.getTranType() + "</TRAN_NUMBER>");*/

            sb.append("<FILEID>" + asnLifecycleBean.getFileId() + "</FILEID>");
            sb.append("<SENDER_ID>" + asnLifecycleBean.getSenderId() + "</SENDER_ID>");
            sb.append("<RECEIVER_ID>" + asnLifecycleBean.getRecId() + "</RECEIVER_ID>");

            sb.append("<SENDER_NAME>" + asnLifecycleBean.getSenName() + "</SENDER_NAME>");
            sb.append("<RECEIVER_NAME>" + asnLifecycleBean.getRecName() + "</RECEIVER_NAME>");

            sb.append("<TRAN_NUMBER>" + asnLifecycleBean.getTranType() + "</TRAN_NUMBER>");
            sb.append("<STATUS>" + asnLifecycleBean.getStatus() + "</STATUS>");
            /*
             * Newly added start
             */
            sb.append("<PO_NUMBER>" + asnLifecycleBean.getPoNumber() + "</PO_NUMBER>");
            sb.append("<PO_DATE>" + asnLifecycleBean.getPodate() + "</PO_DATE>");
            sb.append("<PO_STATUS>" + asnLifecycleBean.getPoStatus() + "</PO_STATUS>");
            sb.append("<SO_NUMBER>" + asnLifecycleBean.getSoNumber() + "</SO_NUMBER>");
            sb.append("<SAPIDOC_NUMBER>" + asnLifecycleBean.getSapIdocNum() + "</SAPIDOC_NUMBER>");
            sb.append("<ITEM_QTY>" + asnLifecycleBean.getIteamQty() + "</ITEM_QTY>");
            sb.append("<DATETIME>" + asnLifecycleBean.getDatetimeRec() + "</DATETIME>");

            sb.append("<ASN_NUMBER>" + asnLifecycleBean.getAsnNumber() + "</ASN_NUMBER>");
            sb.append("<BOL_NUMBER>" + asnLifecycleBean.getBolNumber() + "</BOL_NUMBER>");
            sb.append("<ISA_NUMBER>" + asnLifecycleBean.getIsaCtrlNum() + "</ISA_NUMBER>");
            sb.append("<ISA_DATE>" + asnLifecycleBean.getIsaDate() + "</ISA_DATE>");
            sb.append("<ISA_TIME>" + asnLifecycleBean.getIsaTime() + "</ISA_TIME>");
            sb.append("<INV_NUMBER>" + asnLifecycleBean.getInvNumber() + "</INV_NUMBER>");
            sb.append("<INV_AMOUNT>" + asnLifecycleBean.getInvAmt() + "</INV_AMOUNT>");
            sb.append("<CHEQUE_NUMBER>" + asnLifecycleBean.getChequeNum() + "</CHEQUE_NUMBER>");
            sb.append("<TRANS_TYPE>" + asnLifecycleBean.getTranType() + "</TRANS_TYPE>");

            sb.append("<DIRECTION>" + asnLifecycleBean.getDirection().toLowerCase() + "</DIRECTION>");
            sb.append("<GS_CONTROL_NUMBER>" + asnLifecycleBean.getGsCtrlNum() + "</GS_CONTROL_NUMBER>");
            sb.append("<ST_CONTROL_NUMBER>" + asnLifecycleBean.getStCtrlNum() + "</ST_CONTROL_NUMBER>");
            sb.append("<PRI_KEY_TYPE>" + asnLifecycleBean.getPrimary_key_type()+ "</PRI_KEY_TYPE>");
            sb.append("<PRI_KEY_VAL>" + asnLifecycleBean.getPrimary_key_val()+ "</PRI_KEY_VAL>");
            if (asnLifecycleBean.getPreFile() != null) {
                if (new File(asnLifecycleBean.getPreFile()).exists() && new File(asnLifecycleBean.getPreFile()).isFile()) {
                    sb.append("<PRETRANSFILEPATH>" + asnLifecycleBean.getPreFile() + "</PRETRANSFILEPATH>");
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
            } else {
                sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
            }

            if (asnLifecycleBean.getPostTranFile() != null) {
                if (new File(asnLifecycleBean.getPostTranFile()).exists() && new File(asnLifecycleBean.getPostTranFile()).isFile()) {
                    sb.append("<POSTTRANSFILEPATH>" + asnLifecycleBean.getPostTranFile() + "</POSTTRANSFILEPATH>");
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
            } else {
                sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
            }

            if (asnLifecycleBean.getOrgFile() != null) {
                if (new File(asnLifecycleBean.getOrgFile()).exists() && new File(asnLifecycleBean.getOrgFile()).isFile()) {
                    sb.append("<ORGFILEPATH>" + asnLifecycleBean.getOrgFile() + "</ORGFILEPATH>");
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }
            } else {
                sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
            }

            if (asnLifecycleBean.getAckFile() != null) {
                if (new File(asnLifecycleBean.getAckFile()).exists() && new File(asnLifecycleBean.getAckFile()).isFile()) {
                    sb.append("<ACKFILE>" + asnLifecycleBean.getAckFile() + "</ACKFILE>");
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }
            } else {
                sb.append("<ACKFILE>No File</ACKFILE>");
            }

            sb.append("</DETAIL>");
            isGetting = true;
        }
        if (!isGetting) {
            isGetting = false;
            sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
        }

        sb.append("</DETAILS>");
        sb.append("</xml>");

        // System.out.println("poxml --->"+sb.toString());
        return sb.toString();

    }

    /*
     * INV
     */
    private String getINVDetails(InvoiceLifecycleBean invoiceLifecycleBean) throws ServiceLocatorException {
        StringBuffer sb = new StringBuffer();
        boolean isGetting = false;

        sb.append("<xml version=\"1.0\">");
        sb.append("<DETAILS>");
        if (invoiceLifecycleBean.getRes().equals("1")) {
            sb.append("<DETAIL><VALID>true</VALID>");
            /*   sb.append("<FILEID>" +invoiceLifecycleBean.getFileId() + "</FILEID>");
             sb.append("<SENDER_ID>" +invoiceLifecycleBean.getSenderId() + "</SENDER_ID>");
             sb.append("<RECEIVER_ID>" +invoiceLifecycleBean.getRecId() + "</RECEIVER_ID>");
             sb.append("<TRAN_NUMBER>" +invoiceLifecycleBean.getTranType() + "</TRAN_NUMBER>");*/

            sb.append("<FILEID>" + invoiceLifecycleBean.getFileId() + "</FILEID>");
            sb.append("<SENDER_ID>" + invoiceLifecycleBean.getSenderId() + "</SENDER_ID>");
            sb.append("<RECEIVER_ID>" + invoiceLifecycleBean.getRecId() + "</RECEIVER_ID>");

            sb.append("<SENDER_NAME>" + invoiceLifecycleBean.getSenName() + "</SENDER_NAME>");
            sb.append("<RECEIVER_NAME>" + invoiceLifecycleBean.getRecName() + "</RECEIVER_NAME>");

            sb.append("<TRAN_NUMBER>" + invoiceLifecycleBean.getTranType() + "</TRAN_NUMBER>");
            sb.append("<STATUS>" + invoiceLifecycleBean.getStatus() + "</STATUS>");

            sb.append("<DIRECTION>" + invoiceLifecycleBean.getDirection().toLowerCase() + "</DIRECTION>");


            /*
             * Newly added start
             */
            sb.append("<PO_NUMBER>" + invoiceLifecycleBean.getPoNumber() + "</PO_NUMBER>");
            sb.append("<PO_DATE>" + invoiceLifecycleBean.getPodate() + "</PO_DATE>");
            sb.append("<PO_STATUS>" + invoiceLifecycleBean.getPoStatus() + "</PO_STATUS>");
            sb.append("<SO_NUMBER>" + invoiceLifecycleBean.getSoNumber() + "</SO_NUMBER>");
            sb.append("<SAPIDOC_NUMBER>" + invoiceLifecycleBean.getSapIdocNum() + "</SAPIDOC_NUMBER>");
            sb.append("<ITEM_QTY>" + invoiceLifecycleBean.getIteamQty() + "</ITEM_QTY>");
            sb.append("<DATETIME>" + invoiceLifecycleBean.getDatetimeRec() + "</DATETIME>");

            sb.append("<ASN_NUMBER>" + invoiceLifecycleBean.getAsnNumber() + "</ASN_NUMBER>");
            sb.append("<BOL_NUMBER>" + invoiceLifecycleBean.getBolNumber() + "</BOL_NUMBER>");
            sb.append("<ISA_NUMBER>" + invoiceLifecycleBean.getIsaCtrlNum() + "</ISA_NUMBER>");
            sb.append("<ISA_DATE>" + invoiceLifecycleBean.getIsaDate() + "</ISA_DATE>");
            sb.append("<ISA_TIME>" + invoiceLifecycleBean.getIsaTime() + "</ISA_TIME>");
            sb.append("<INV_NUMBER>" + invoiceLifecycleBean.getInvNumber() + "</INV_NUMBER>");
            sb.append("<INV_AMOUNT>" + invoiceLifecycleBean.getInvAmt() + "</INV_AMOUNT>");
            sb.append("<CHEQUE_NUMBER>" + invoiceLifecycleBean.getChequeNum() + "</CHEQUE_NUMBER>");
            sb.append("<TRANS_TYPE>" + invoiceLifecycleBean.getTranType() + "</TRANS_TYPE>");
            sb.append("<GS_CONTROL_NUMBER>" + invoiceLifecycleBean.getGsCtrlNum() + "</GS_CONTROL_NUMBER>");
            sb.append("<ST_CONTROL_NUMBER>" + invoiceLifecycleBean.getStCtrlNum() + "</ST_CONTROL_NUMBER>");
            sb.append("<PRI_KEY_TYPE>" + invoiceLifecycleBean.getPrimary_key_type()+ "</PRI_KEY_TYPE>");
            sb.append("<PRI_KEY_VAL>" + invoiceLifecycleBean.getPrimary_key_val()+ "</PRI_KEY_VAL>");
            if (invoiceLifecycleBean.getPreFile() != null) {
                if (new File(invoiceLifecycleBean.getPreFile()).exists() && new File(invoiceLifecycleBean.getPreFile()).isFile()) {
                    sb.append("<PRETRANSFILEPATH>" + invoiceLifecycleBean.getPreFile() + "</PRETRANSFILEPATH>");
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
            } else {
                sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
            }

            if (invoiceLifecycleBean.getPostTranFile() != null) {
                if (new File(invoiceLifecycleBean.getPostTranFile()).exists() && new File(invoiceLifecycleBean.getPostTranFile()).isFile()) {
                    sb.append("<POSTTRANSFILEPATH>" + invoiceLifecycleBean.getPostTranFile() + "</POSTTRANSFILEPATH>");
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
            } else {
                sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
            }

            if (invoiceLifecycleBean.getOrgFile() != null) {
                if (new File(invoiceLifecycleBean.getOrgFile()).exists() && new File(invoiceLifecycleBean.getOrgFile()).isFile()) {
                    sb.append("<ORGFILEPATH>" + invoiceLifecycleBean.getOrgFile() + "</ORGFILEPATH>");
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }
            } else {
                sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
            }

            if (invoiceLifecycleBean.getAckFile() != null) {
                if (new File(invoiceLifecycleBean.getAckFile()).exists() && new File(invoiceLifecycleBean.getAckFile()).isFile()) {
                    sb.append("<ACKFILE>" + invoiceLifecycleBean.getAckFile() + "</ACKFILE>");
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }
            } else {
                sb.append("<ACKFILE>No File</ACKFILE>");
            }

            sb.append("</DETAIL>");
            isGetting = true;
        }
        if (!isGetting) {
            isGetting = false;
            sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
        }

        sb.append("</DETAILS>");
        sb.append("</xml>");

        //  System.out.println("poxml --->"+sb.toString());
        return sb.toString();

    }

    /**
     * Payment
     *
     */
    private String getPayDetails(PaymentLifecycleBean paymentLifecycleBean) throws ServiceLocatorException {
        StringBuffer sb = new StringBuffer();
        boolean isGetting = false;

        sb.append("<xml version=\"1.0\">");
        sb.append("<DETAILS>");
        if (paymentLifecycleBean.getRes().equals("1")) {
            sb.append("<DETAIL><VALID>true</VALID>");
            /* sb.append("<FILEID>" +paymentLifecycleBean.getFileId() + "</FILEID>");
             sb.append("<SENDER_ID>" +paymentLifecycleBean.getSenderId() + "</SENDER_ID>");
             sb.append("<RECEIVER_ID>" +paymentLifecycleBean.getRecId() + "</RECEIVER_ID>");
             sb.append("<TRAN_NUMBER>" +paymentLifecycleBean.getTranType() + "</TRAN_NUMBER>");*/

            sb.append("<FILEID>" + paymentLifecycleBean.getFileId() + "</FILEID>");
            sb.append("<SENDER_ID>" + paymentLifecycleBean.getSenderId() + "</SENDER_ID>");
            sb.append("<RECEIVER_ID>" + paymentLifecycleBean.getRecId() + "</RECEIVER_ID>");

            sb.append("<SENDER_NAME>" + paymentLifecycleBean.getSenName() + "</SENDER_NAME>");
            sb.append("<RECEIVER_NAME>" + paymentLifecycleBean.getRecName() + "</RECEIVER_NAME>");

            sb.append("<DIRECTION>" + paymentLifecycleBean.getDirection().toLowerCase() + "</DIRECTION>");
            sb.append("<STATUS>" + paymentLifecycleBean.getStatus() + "</STATUS>");
            sb.append("<TRAN_NUMBER>" + paymentLifecycleBean.getTranType() + "</TRAN_NUMBER>");
            /*
             * Newly added start
             */
            sb.append("<PO_NUMBER>" + paymentLifecycleBean.getPoNumber() + "</PO_NUMBER>");
            sb.append("<PO_DATE>" + paymentLifecycleBean.getPodate() + "</PO_DATE>");
            sb.append("<PO_STATUS>" + paymentLifecycleBean.getPoStatus() + "</PO_STATUS>");
            sb.append("<SO_NUMBER>" + paymentLifecycleBean.getSoNumber() + "</SO_NUMBER>");
            sb.append("<SAPIDOC_NUMBER>" + paymentLifecycleBean.getSapIdocNum() + "</SAPIDOC_NUMBER>");
            sb.append("<ITEM_QTY>" + paymentLifecycleBean.getIteamQty() + "</ITEM_QTY>");
            sb.append("<DATETIME>" + paymentLifecycleBean.getDatetimeRec() + "</DATETIME>");

            sb.append("<ASN_NUMBER>" + paymentLifecycleBean.getAsnNumber() + "</ASN_NUMBER>");
            sb.append("<BOL_NUMBER>" + paymentLifecycleBean.getBolNumber() + "</BOL_NUMBER>");
            sb.append("<ISA_NUMBER>" + paymentLifecycleBean.getIsaCtrlNum() + "</ISA_NUMBER>");
            sb.append("<ISA_DATE>" + paymentLifecycleBean.getIsaDate() + "</ISA_DATE>");
            sb.append("<ISA_TIME>" + paymentLifecycleBean.getIsaTime() + "</ISA_TIME>");
            sb.append("<INV_NUMBER>" + paymentLifecycleBean.getInvNumber() + "</INV_NUMBER>");
            sb.append("<INV_AMOUNT>" + paymentLifecycleBean.getInvAmt() + "</INV_AMOUNT>");
            sb.append("<GS_CONTROL_NUMBER>" + paymentLifecycleBean.getGsCtrlNum() + "</GS_CONTROL_NUMBER>");
            sb.append("<ST_CONTROL_NUMBER>" + paymentLifecycleBean.getStCtrlNum() + "</ST_CONTROL_NUMBER>");
            sb.append("<PRI_KEY_TYPE>" + paymentLifecycleBean.getPrimary_key_type()+ "</PRI_KEY_TYPE>");
            sb.append("<PRI_KEY_VAL>" + paymentLifecycleBean.getPrimary_key_val()+ "</PRI_KEY_VAL>");
            if (paymentLifecycleBean.getChequeNum() != null && !"".equals(paymentLifecycleBean.getChequeNum())) {
                sb.append("<CHEQUE_NUMBER>" + paymentLifecycleBean.getChequeNum() + "</CHEQUE_NUMBER>");
            } else {
                sb.append("<CHEQUE_NUMBER>0</CHEQUE_NUMBER>");
            }
            sb.append("<TRANS_TYPE>" + paymentLifecycleBean.getTranType() + "</TRANS_TYPE>");

            if (paymentLifecycleBean.getPreFile() != null) {
                if (new File(paymentLifecycleBean.getPreFile()).exists() && new File(paymentLifecycleBean.getPreFile()).isFile()) {
                    sb.append("<PRETRANSFILEPATH>" + paymentLifecycleBean.getPreFile() + "</PRETRANSFILEPATH>");
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
            } else {
                sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
            }

            if (paymentLifecycleBean.getPostTranFile() != null) {
                if (new File(paymentLifecycleBean.getPostTranFile()).exists() && new File(paymentLifecycleBean.getPostTranFile()).isFile()) {
                    sb.append("<POSTTRANSFILEPATH>" + paymentLifecycleBean.getPostTranFile() + "</POSTTRANSFILEPATH>");
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
            } else {
                sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
            }

            if (paymentLifecycleBean.getOrgFile() != null) {
                if (new File(paymentLifecycleBean.getOrgFile()).exists() && new File(paymentLifecycleBean.getOrgFile()).isFile()) {
                    sb.append("<ORGFILEPATH>" + paymentLifecycleBean.getOrgFile() + "</ORGFILEPATH>");
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }
            } else {
                sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
            }

            if (paymentLifecycleBean.getAckFile() != null) {
                if (new File(paymentLifecycleBean.getAckFile()).exists() && new File(paymentLifecycleBean.getAckFile()).isFile()) {
                    sb.append("<ACKFILE>" + paymentLifecycleBean.getAckFile() + "</ACKFILE>");
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }
            } else {
                sb.append("<ACKFILE>No File</ACKFILE>");
            }

            sb.append("</DETAIL>");
            isGetting = true;
        }
        if (!isGetting) {
            isGetting = false;
            sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
        }

        sb.append("</DETAILS>");
        sb.append("</xml>");

        // System.out.println("poxml --->"+sb.toString());
        return sb.toString();

    }

    public String getLtLifecycleDetails(String poNumber, String fileId, String type, String database) throws ServiceLocatorException {
        String resultXml = "";
        LifecycleUtility lifecycleUtility = new LifecycleUtility();
        if (type.equalsIgnoreCase("LOADTENDER")) {
            ltTenderBean = lifecycleUtility.getLtLoadtenderDetails(poNumber, fileId, database);
            resultXml = getLtLoadtenderDetailsXml(ltTenderBean);
        }
        if (type.equalsIgnoreCase("RESPONSE")) {
            ltResponsesBean = lifecycleUtility.getLtResponseDetails(poNumber, fileId, database);
            resultXml = getLtResponseDetailsXml(ltResponsesBean);
        }
        if (type.equalsIgnoreCase("SHIPMENT")) {
            ltShipmentsBean = lifecycleUtility.getLtShipmentDetails(poNumber, fileId, database);
            resultXml = getLtShipmentDetailsXml(ltShipmentsBean);
        }
        if (type.equalsIgnoreCase("INVOICE")) {
            ltInvoicesBean = lifecycleUtility.getLtInvoiceDetails(poNumber, fileId, database);
            resultXml = getLtInvoiceDetailsXml(ltInvoicesBean);
        }
        return resultXml;
    }

    /**
     * PO
     */
    private String getLtLoadtenderDetailsXml(LtTenderBean ltTenderBean) throws ServiceLocatorException {
        StringBuffer sb = new StringBuffer();
        boolean isGetting = false;
        sb.append("<xml version=\"1.0\">");
        sb.append("<DETAILS>");
        if (ltTenderBean.getRes().equals("1")) {
            sb.append("<DETAIL><VALID>true</VALID>");
            sb.append("<FILEID>" + ltTenderBean.getFileId() + "</FILEID>");
            sb.append("<SENDER_ID>" + ltTenderBean.getSenderId() + "</SENDER_ID>");
            sb.append("<RECEIVER_ID>" + ltTenderBean.getRecId() + "</RECEIVER_ID>");
            sb.append("<SENDER_NAME>" + ltTenderBean.getSenName() + "</SENDER_NAME>");
            sb.append("<RECEIVER_NAME>" + ltTenderBean.getRecName() + "</RECEIVER_NAME>");
            sb.append("<PO_NUMBER>" + ltTenderBean.getPoNumber() + "</PO_NUMBER>");
            sb.append("<TRAN_NUMBER>" + ltTenderBean.getTran_type() + "</TRAN_NUMBER>");
            sb.append("<ISA_NUMBER>" + ltTenderBean.getIsaNum() + "</ISA_NUMBER>");
            sb.append("<ISA_DATE>" + ltTenderBean.getIsaDate() + "</ISA_DATE>");
            sb.append("<ISA_TIME>" + ltTenderBean.getIsaTime() + "</ISA_TIME>");
            sb.append("<TRANS_TYPE>" + ltTenderBean.getTran_type() + "</TRANS_TYPE>");
            sb.append("<DATETIME>" + ltTenderBean.getDatetime() + "</DATETIME>");
            sb.append("<STATUS>" + ltTenderBean.getStatus() + "</STATUS>");
            sb.append("<DIRECTION>" + ltTenderBean.getDirection().toLowerCase() + "</DIRECTION>");
            if (ltTenderBean.getPreFile() != null) {
                if (new File(ltTenderBean.getPreFile()).exists()) {
                    sb.append("<PRETRANSFILEPATH>" + ltTenderBean.getPreFile() + "</PRETRANSFILEPATH>");
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
            } else {
                sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
            }
            if (ltTenderBean.getPostTranFile() != null) {
                if (new File(ltTenderBean.getPostTranFile()).exists()) {
                    sb.append("<POSTTRANSFILEPATH>" + ltTenderBean.getPostTranFile() + "</POSTTRANSFILEPATH>");
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
            } else {
                sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
            }
            if (ltTenderBean.getAckFile() != null) {
                if (new File(ltTenderBean.getAckFile()).exists()) {
                    sb.append("<ACKFILE>" + ltTenderBean.getAckFile() + "</ACKFILE>");
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }
            } else {
                sb.append("<ACKFILE>No File</ACKFILE>");
            }
            if (ltTenderBean.getComments() != null && !"".equals(ltTenderBean.getComments())) {
                sb.append("<COMMENTS>" + ltTenderBean.getComments().trim() + "</COMMENTS>");
            } else {
                sb.append("<COMMENTS>NO</COMMENTS>");
            }
            if (ltTenderBean.getModFilePath() != null && !"".equals(ltTenderBean.getModFilePath())) {
                if (new File(ltTenderBean.getModFilePath()).exists()) {
                    sb.append("<MODPOSTTRANSFILEPATH>" + ltTenderBean.getModFilePath().trim() + "</MODPOSTTRANSFILEPATH>");
                } else {
                    sb.append("<MODPOSTTRANSFILEPATH>No File</MODPOSTTRANSFILEPATH>");
                }
            } else {
                sb.append("<MODPOSTTRANSFILEPATH>No File</MODPOSTTRANSFILEPATH>");
            }
            //  sb.append("<MODFLAG>" + ltTenderBean.getModFlag() + "</MODFLAG>");
            sb.append("<ERRORMSG>" + ltTenderBean.getErrorMessage() + "</ERRORMSG>");
            sb.append("</DETAIL>");
            isGetting = true;
        }
        if (!isGetting) {
            isGetting = false;
            sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
        }
        sb.append("</DETAILS>");
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * ASN
     */
    private String getLtResponseDetailsXml(LtResponsesBean ltResponsesBean) throws ServiceLocatorException {
        StringBuffer sb = new StringBuffer();
        boolean isGetting = false;
        sb.append("<xml version=\"1.0\">");
        sb.append("<DETAILS>");
        if (ltResponsesBean.getRes() != null && ltResponsesBean.getRes().equals("1")) {
            sb.append("<DETAIL><VALID>true</VALID>");
            sb.append("<FILEID>" + ltResponsesBean.getFileId() + "</FILEID>");
            sb.append("<SENDER_ID>" + ltResponsesBean.getSenderId() + "</SENDER_ID>");
            sb.append("<RECEIVER_ID>" + ltResponsesBean.getRecId() + "</RECEIVER_ID>");
            sb.append("<SENDER_NAME>" + ltResponsesBean.getSenName() + "</SENDER_NAME>");
            sb.append("<RECEIVER_NAME>" + ltResponsesBean.getRecName() + "</RECEIVER_NAME>");
            sb.append("<PO_NUMBER>" + ltResponsesBean.getPoNumber() + "</PO_NUMBER>");
            sb.append("<TRAN_NUMBER>" + ltResponsesBean.getTran_type() + "</TRAN_NUMBER>");
            sb.append("<STATUS>" + ltResponsesBean.getStatus() + "</STATUS>");
            sb.append("<DATETIME>" + ltResponsesBean.getDatetime() + "</DATETIME>");
            sb.append("<ISA_NUMBER>" + ltResponsesBean.getIsaNum() + "</ISA_NUMBER>");
            sb.append("<ISA_DATE>" + ltResponsesBean.getIsaDate() + "</ISA_DATE>");
            sb.append("<ISA_TIME>" + ltResponsesBean.getIsaTime() + "</ISA_TIME>");
            sb.append("<TRANS_TYPE>" + ltResponsesBean.getTran_type() + "</TRANS_TYPE>");
            sb.append("<DIRECTION>" + ltResponsesBean.getDirection().toLowerCase() + "</DIRECTION>");
            if (ltResponsesBean.getPreFile() != null) {
                if (new File(ltResponsesBean.getPreFile()).exists()) {
                    sb.append("<PRETRANSFILEPATH>" + ltResponsesBean.getPreFile() + "</PRETRANSFILEPATH>");
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
            } else {
                sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
            }
            if (ltResponsesBean.getPostTranFile() != null) {
                if (new File(ltResponsesBean.getPostTranFile()).exists()) {
                    sb.append("<POSTTRANSFILEPATH>" + ltResponsesBean.getPostTranFile() + "</POSTTRANSFILEPATH>");
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
            } else {
                sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
            }
            if (ltResponsesBean.getAckFile() != null) {
                if (new File(ltResponsesBean.getAckFile()).exists()) {
                    sb.append("<ACKFILE>" + ltResponsesBean.getAckFile() + "</ACKFILE>");
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }
            } else {
                sb.append("<ACKFILE>No File</ACKFILE>");
            }
            sb.append("<ERRORMSG>" + ltResponsesBean.getErrorMessage() + "</ERRORMSG>");
            sb.append("</DETAIL>");
            isGetting = true;
        }
        if (!isGetting) {
            isGetting = false;
            sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
        }
        sb.append("</DETAILS>");
        sb.append("</xml>");
        return sb.toString();
    }

    /*
     * INV
     */
    private String getLtShipmentDetailsXml(LtShipmentsBean ltShipmentsBean) throws ServiceLocatorException {
        StringBuffer sb = new StringBuffer();
        boolean isGetting = false;
        sb.append("<xml version=\"1.0\">");
        sb.append("<DETAILS>");
        if (ltShipmentsBean.getRes().equals("1")) {
            sb.append("<DETAIL><VALID>true</VALID>");
            sb.append("<FILEID>" + ltShipmentsBean.getFileId() + "</FILEID>");
            sb.append("<SENDER_ID>" + ltShipmentsBean.getSenderId() + "</SENDER_ID>");
            sb.append("<RECEIVER_ID>" + ltShipmentsBean.getRecId() + "</RECEIVER_ID>");
            sb.append("<SENDER_NAME>" + ltShipmentsBean.getSenName() + "</SENDER_NAME>");
            sb.append("<RECEIVER_NAME>" + ltShipmentsBean.getRecName() + "</RECEIVER_NAME>");
            sb.append("<PO_NUMBER>" + ltShipmentsBean.getPoNumber() + "</PO_NUMBER>");
            sb.append("<TRAN_NUMBER>" + ltShipmentsBean.getTran_type() + "</TRAN_NUMBER>");
            sb.append("<STATUS>" + ltShipmentsBean.getStatus() + "</STATUS>");
            sb.append("<DIRECTION>" + ltShipmentsBean.getDirection().toLowerCase() + "</DIRECTION>");
            sb.append("<DATETIME>" + ltShipmentsBean.getDatetime() + "</DATETIME>");
            sb.append("<ISA_NUMBER>" + ltShipmentsBean.getIsaNum() + "</ISA_NUMBER>");
            sb.append("<ISA_DATE>" + ltShipmentsBean.getIsaDate() + "</ISA_DATE>");
            sb.append("<ISA_TIME>" + ltShipmentsBean.getIsaTime() + "</ISA_TIME>");
            sb.append("<TRANS_TYPE>" + ltShipmentsBean.getTran_type() + "</TRANS_TYPE>");
            if (ltShipmentsBean.getPreFile() != null) {
                if (new File(ltShipmentsBean.getPreFile()).exists()) {
                    sb.append("<PRETRANSFILEPATH>" + ltShipmentsBean.getPreFile() + "</PRETRANSFILEPATH>");
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
            } else {
                sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
            }
            if (ltShipmentsBean.getPostTranFile() != null) {
                if (new File(ltShipmentsBean.getPostTranFile()).exists()) {
                    sb.append("<POSTTRANSFILEPATH>" + ltShipmentsBean.getPostTranFile() + "</POSTTRANSFILEPATH>");
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
            } else {
                sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
            }
            if (ltShipmentsBean.getAckFile() != null) {
                if (new File(ltShipmentsBean.getAckFile()).exists()) {
                    sb.append("<ACKFILE>" + ltShipmentsBean.getAckFile() + "</ACKFILE>");
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }
            } else {
                sb.append("<ACKFILE>No File</ACKFILE>");
            }
            sb.append("<ERRORMSG>" + ltShipmentsBean.getErrorMessage() + "</ERRORMSG>");
            sb.append("</DETAIL>");
            isGetting = true;
        }
        if (!isGetting) {
            isGetting = false;
            sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
        }
        sb.append("</DETAILS>");
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * Payment
     */
    private String getLtInvoiceDetailsXml(LtInvoicesBean ltInvoicesBean) throws ServiceLocatorException {
        StringBuffer sb = new StringBuffer();
        boolean isGetting = false;
        sb.append("<xml version=\"1.0\">");
        sb.append("<DETAILS>");
        if (ltInvoicesBean.getRes().equals("1")) {
            sb.append("<DETAIL><VALID>true</VALID>");
            sb.append("<FILEID>" + ltInvoicesBean.getFileId() + "</FILEID>");
            sb.append("<SENDER_ID>" + ltInvoicesBean.getSenderId() + "</SENDER_ID>");
            sb.append("<RECEIVER_ID>" + ltInvoicesBean.getRecId() + "</RECEIVER_ID>");
            sb.append("<SENDER_NAME>" + ltInvoicesBean.getSenName() + "</SENDER_NAME>");
            sb.append("<RECEIVER_NAME>" + ltInvoicesBean.getRecName() + "</RECEIVER_NAME>");
            sb.append("<DIRECTION>" + ltInvoicesBean.getDirection().toLowerCase() + "</DIRECTION>");
            sb.append("<STATUS>" + ltInvoicesBean.getStatus() + "</STATUS>");
            sb.append("<TRAN_NUMBER>" + ltInvoicesBean.getTran_type() + "</TRAN_NUMBER>");
            sb.append("<PO_NUMBER>" + ltInvoicesBean.getPoNumber() + "</PO_NUMBER>");
            sb.append("<DATETIME>" + ltInvoicesBean.getDatetime() + "</DATETIME>");
            sb.append("<ISA_NUMBER>" + ltInvoicesBean.getIsaNum() + "</ISA_NUMBER>");
            sb.append("<ISA_DATE>" + ltInvoicesBean.getIsaDate() + "</ISA_DATE>");
            sb.append("<ISA_TIME>" + ltInvoicesBean.getIsaTime() + "</ISA_TIME>");
            sb.append("<TRANS_TYPE>" + ltInvoicesBean.getTran_type() + "</TRANS_TYPE>");
            if (ltInvoicesBean.getPreFile() != null) {
                if (new File(ltInvoicesBean.getPreFile()).exists()) {
                    sb.append("<PRETRANSFILEPATH>" + ltInvoicesBean.getPreFile() + "</PRETRANSFILEPATH>");
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }
            } else {
                sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
            }
            if (ltInvoicesBean.getPostTranFile() != null) {
                if (new File(ltInvoicesBean.getPostTranFile()).exists()) {
                    sb.append("<POSTTRANSFILEPATH>" + ltInvoicesBean.getPostTranFile() + "</POSTTRANSFILEPATH>");
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
            } else {
                sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
            }
            if (ltInvoicesBean.getAckFile() != null) {
                if (new File(ltInvoicesBean.getAckFile()).exists()) {
                    sb.append("<ACKFILE>" + ltInvoicesBean.getAckFile() + "</ACKFILE>");
                } else {
                    sb.append("<ACKFILE>No File</ACKFILE>");
                }
            } else {
                sb.append("<ACKFILE>No File</ACKFILE>");
            }
            sb.append("<ERRORMSG>" + ltInvoicesBean.getErrorMessage() + "</ERRORMSG>");
            sb.append("</DETAIL>");
            isGetting = true;
        }
        if (!isGetting) {
            isGetting = false;
            sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
        }
        sb.append("</DETAILS>");
        sb.append("</xml>");
        return sb.toString();
    }

    /**
     * TP Methods
     *
     *
     */
    public String getTpDetails(String tpId) throws ServiceLocatorException {

        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();

        queryString = "SELECT * FROM TP WHERE ID='" + tpId + "'";

        //System.out.println("QUERY IS "+queryString);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("ID") != null && !"".equals(resultSet.getString("ID"))) {
                    sb.append("<ID>" + resultSet.getString("ID") + "</ID>");
                } else {
                    sb.append("<ID>--</ID>");
                }
                if (resultSet.getString("NAME") != null && !"".equals(resultSet.getString("NAME"))) {
                    sb.append("<NAME>" + resultSet.getString("NAME") + "</NAME>");
                } else {
                    sb.append("<NAME>--</NAME>");
                }

                // sb.append("<NAME>" +resultSet.getString("NAME") + "</NAME>");
                if (resultSet.getString("CONTACT_INFO") != null && !"".equals(resultSet.getString("CONTACT_INFO"))) {
                    sb.append("<CONTACT_INFO>" + resultSet.getString("CONTACT_INFO") + "</CONTACT_INFO>");
                } else {
                    sb.append("<CONTACT_INFO>--</CONTACT_INFO>");
                }

                //  sb.append("<CONTACT_INFO>" +resultSet.getString("CONTACT_INFO") + "</CONTACT_INFO>");
                if (resultSet.getString("VENDOR_NUMBER") != null && !"".equals(resultSet.getString("VENDOR_NUMBER"))) {
                    sb.append("<VENDOR_NUMBER>" + resultSet.getString("VENDOR_NUMBER") + "</VENDOR_NUMBER>");
                } else {
                    sb.append("<VENDOR_NUMBER>--</VENDOR_NUMBER>");
                }

                //sb.append("<VENDOR_NUMBER>" +resultSet.getString("VENDOR_NUMBER") + "</VENDOR_NUMBER>");
                if (resultSet.getString("DEPARTMENTS") != null && !"".equals(resultSet.getString("DEPARTMENTS"))) {
                    sb.append("<DEPARTMENTS>" + resultSet.getString("DEPARTMENTS") + "</DEPARTMENTS>");
                } else {
                    sb.append("<DEPARTMENTS>--</DEPARTMENTS>");
                }

                // sb.append("<DEPARTMENTS>" +resultSet.getString("DEPARTMENTS") + "</DEPARTMENTS>");
                if (resultSet.getString("EDI_COMM_ID") != null && !"".equals(resultSet.getString("EDI_COMM_ID"))) {
                    sb.append("<EDI_COMM_ID>" + resultSet.getString("EDI_COMM_ID") + "</EDI_COMM_ID>");
                } else {
                    sb.append("<EDI_COMM_ID>--</EDI_COMM_ID>");
                }

                // sb.append("<EDI_COMM_ID>" +resultSet.getString("EDI_COMM_ID") + "</EDI_COMM_ID>");
                if (resultSet.getString("QUALIFIER") != null && !"".equals(resultSet.getString("QUALIFIER"))) {
                    sb.append("<QUALIFIER>" + resultSet.getString("QUALIFIER") + "</QUALIFIER>");
                } else {
                    sb.append("<QUALIFIER>--</QUALIFIER>");
                }

                //  sb.append("<QUALIFIER>" +resultSet.getString("QUALIFIER") + "</QUALIFIER>");
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
            // System.out.println("xml string -->"+sb.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // System.out.println("this is string >> "+sb.toString());
        return sb.toString();
    }

    public String updateTpDetails(AjaxHandlerAction ajaxHandlerAction) throws ServiceLocatorException {

        boolean isGetting = false;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        String id = ajaxHandlerAction.getTpId();

        //System.out.println(ajaxHandlerAction.getName()+" "+ajaxHandlerAction.getContact()+ " "+ajaxHandlerAction.getPhno()+" "+ajaxHandlerAction.getDept()+" "+ajaxHandlerAction.getCommid()+" "+ajaxHandlerAction.getQualifier());
        queryString = "UPDATE TP SET NAME='" + ajaxHandlerAction.getName() + "'"
                + ",CONTACT_INFO='" + ajaxHandlerAction.getContact() + "',VENDOR_NUMBER='" + ajaxHandlerAction.getPhno() + ""
                + "',DEPARTMENTS='" + ajaxHandlerAction.getDept() + "',EDI_COMM_ID='" + ajaxHandlerAction.getCommid() + "'"
                + ",QUALIFIER='" + ajaxHandlerAction.getQualifier() + "' WHERE ID='" + id + "'";

        //System.out.println("QUERY IS "+queryString);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();

            int count = statement.executeUpdate(queryString);

            if (count > 0) {
                sb.append("Trading Partner " + id + " Successfully Updated!");
            } else {
                sb.append("Sorry ! Please Try again.");
            }
            // System.out.println("xml string -->"+sb.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // System.out.println("this is string >> "+sb.toString());
        return sb.toString();
    }

    public String getTpDetailsByName(String name) throws ServiceLocatorException {
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        //queryString = "SELECT * FROM DOCUMENT WHERE ISA_NUMBER LIKE '%"+isaNumber+"%'";
        queryString = "select tp_details.tp_id as id,tp_details.city as city,tp_details.zip as zip,"
                + "tp_details.qualifier as qualifier,tp_details.network as network,tp_details.vendor_number as vendor,"
                + "tp_details.department_number as department,tp_details.duns as duns,tp_details.ship_duns as ship,"
                + "tp_details.billing_duns as billing,tp_details.order_duns as order,"
                + "tp_details.as2_url as url,tp_details.as2_cert as cert "
                + "from tp LEFT OUTER JOIN tp_details on (tp_details.TP_ID=tp.ID) where tp.name='" + name + "'";

        // System.out.println("QUERY IS "+queryString);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("id") != null && !"".equals(resultSet.getString("id"))) {
                    sb.append("<ID>").append(resultSet.getString("id")).append("</ID>");
                } else {
                    sb.append("<ID>").append("NO").append("</ID>");
                }

                if (resultSet.getString("city") != null && !"".equals(resultSet.getString("city"))) {
                    sb.append("<CITY>").append(resultSet.getString("city")).append("</CITY>");

                } else {
                    sb.append("<CITY>").append("NO").append("</CITY>");
                }

                if (resultSet.getString("zip") != null && !"".equals(resultSet.getString("zip"))) {
                    sb.append("<ZIP>").append(resultSet.getString("zip")).append("</ZIP>");

                } else {
                    sb.append("<ZIP>").append("NO").append("</ZIP>");
                }

                if (resultSet.getString("qualifier") != null && !"".equals(resultSet.getString("qualifier"))) {
                    sb.append("<QUALIFIER>").append(resultSet.getString("qualifier")).append("</QUALIFIER>");

                } else {
                    sb.append("<QUALIFIER>").append("NO").append("</QUALIFIER>");
                }

                if (resultSet.getString("network") != null && !"".equals(resultSet.getString("network"))) {
                    sb.append("<NETWORK>").append(resultSet.getString("network")).append("</NETWORK>");

                } else {
                    sb.append("<NETWORK>").append("NO").append("</NETWORK>");
                }
                //add ed...
                if (resultSet.getString("vendor") != null && !"".equals(resultSet.getString("vendor"))) {
                    sb.append("<VENDOR>").append(resultSet.getString("vendor")).append("</VENDOR>");

                } else {
                    sb.append("<VENDOR>").append("NO").append("</VENDOR>");
                }

                if (resultSet.getString("department") != null && !"".equals(resultSet.getString("department"))) {
                    sb.append("<DEPARTMENT>").append(resultSet.getString("department")).append("</DEPARTMENT>");

                } else {
                    sb.append("<DEPARTMENT>").append("NO").append("</DEPARTMENT>");
                }

                if (resultSet.getString("duns") != null && !"".equals(resultSet.getString("duns"))) {
                    sb.append("<DUNS>").append(resultSet.getString("duns")).append("</DUNS>");

                } else {
                    sb.append("<DUNS>").append("NO").append("</DUNS>");
                }

                if (resultSet.getString("ship") != null && !"".equals(resultSet.getString("ship"))) {
                    sb.append("<SHIP>").append(resultSet.getString("ship")).append("</SHIP>");

                } else {
                    sb.append("<SHIP>").append("NO").append("</SHIP>");
                }

                if (resultSet.getString("billing") != null && !"".equals(resultSet.getString("billing"))) {
                    sb.append("<BILLING>").append(resultSet.getString("billing")).append("</BILLING>");

                } else {
                    sb.append("<BILLING>").append("NO").append("</BILLING>");
                }

                if (resultSet.getString("order") != null && !"".equals(resultSet.getString("order"))) {
                    sb.append("<ORDER>").append(resultSet.getString("order")).append("</ORDER>");

                } else {
                    sb.append("<ORDER>").append("NO").append("</ORDER>");
                }

                if (resultSet.getString("url") != null && !"".equals(resultSet.getString("url"))) {
                    sb.append("<URL>").append(resultSet.getString("url")).append("</URL>");

                } else {
                    sb.append("<URL>").append("NO").append("</URL>");
                }

                if (resultSet.getString("cert") != null && !"".equals(resultSet.getString("cert"))) {
                    sb.append("<CERT>").append(resultSet.getString("cert")).append("</CERT>");

                } else {
                    sb.append("<CERT>").append("NO").append("</CERT>");
                }
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // System.out.println("this is string >> "+sb.toString());
        return sb.toString();
    }

    public String getTpDetailInformation(String tpId, String defFlowId) throws ServiceLocatorException {
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();

        // System.out.println("QUERY IS "+queryString);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("select TP.ID as tpId,tp.NAME as tpName,TP_NAME,CONTACT_NAME,BVR_UDI_ID,tp_details.NAME as bvrName,"
                    + "PHONE_NUMBER,EMAIL,ADDRESS,CITY,STATE,ZIP,NETWORK,URL,PO_TYPE_BASIC,PO_TYPE_SOQ,INVOICE_TYPE_STORE,"
                    + "INVOICE_TYPE_MASTER,DEVELOPING,VENDOR,ORDER_DUNS,SHIP_DUNS,PAY_DUNS,DEPARTMENT_NUMBER,BUYER_NAME,"
                    + "BUYER_PHONE,BUYER_EMAIL,CUSTOMER_NAME,CUSTOMER_PHONE,CUSTOMER_EMAIL from tp LEFT OUTER JOIN tp_details on (tp.ID=tp_details.TP_ID) where tp.ID=? and tp.FLOW_FLAG=?");
            preparedStatement.setString(1, tpId);
            preparedStatement.setString(2, defFlowId);
            resultSet = preparedStatement.executeQuery();

            // statement = connection.prepareStatement(queryString);
            //resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("tpId") != null && !"".equals(resultSet.getString("tpId"))) {
                    sb.append("<ID>").append(resultSet.getString("tpId")).append("</ID>");
                } else {
                    sb.append("<ID>").append("NO").append("</ID>");
                }

                if (resultSet.getString("CITY") != null && !"".equals(resultSet.getString("CITY"))) {
                    sb.append("<CITY>").append(resultSet.getString("CITY")).append("</CITY>");

                } else {
                    sb.append("<CITY>").append("NO").append("</CITY>");
                }

                if (resultSet.getString("ZIP") != null && !"".equals(resultSet.getString("ZIP"))) {
                    sb.append("<ZIP>").append(resultSet.getString("ZIP")).append("</ZIP>");

                } else {
                    sb.append("<ZIP>").append("NO").append("</ZIP>");
                }

                if (resultSet.getString("NETWORK") != null && !"".equals(resultSet.getString("NETWORK"))) {
                    sb.append("<NETWORK>").append(resultSet.getString("NETWORK")).append("</NETWORK>");

                } else {
                    sb.append("<NETWORK>").append("NO").append("</NETWORK>");
                }
                //add ed...
                if (resultSet.getString("VENDOR") != null && !"".equals(resultSet.getString("VENDOR"))) {
                    sb.append("<VENDOR>").append(resultSet.getString("VENDOR")).append("</VENDOR>");

                } else {
                    sb.append("<VENDOR>").append("NO").append("</VENDOR>");
                }

                if (resultSet.getString("DEPARTMENT_NUMBER") != null && !"".equals(resultSet.getString("DEPARTMENT_NUMBER"))) {
                    sb.append("<DEPARTMENT>").append(resultSet.getString("DEPARTMENT_NUMBER")).append("</DEPARTMENT>");

                } else {
                    sb.append("<DEPARTMENT>").append("NO").append("</DEPARTMENT>");
                }

                if (resultSet.getString("SHIP_DUNS") != null && !"".equals(resultSet.getString("SHIP_DUNS"))) {
                    sb.append("<SHIP>").append(resultSet.getString("SHIP_DUNS")).append("</SHIP>");

                } else {
                    sb.append("<SHIP>").append("NO").append("</SHIP>");
                }

                if (resultSet.getString("PAY_DUNS") != null && !"".equals(resultSet.getString("PAY_DUNS"))) {
                    sb.append("<PAY_DUNS>").append(resultSet.getString("PAY_DUNS")).append("</PAY_DUNS>");

                } else {
                    sb.append("<PAY_DUNS>").append("NO").append("</PAY_DUNS>");
                }

                if (resultSet.getString("ORDER_DUNS") != null && !"".equals(resultSet.getString("ORDER_DUNS"))) {
                    sb.append("<ORDER>").append(resultSet.getString("ORDER_DUNS")).append("</ORDER>");

                } else {
                    sb.append("<ORDER>").append("NO").append("</ORDER>");
                }

                if (resultSet.getString("URL") != null && !"".equals(resultSet.getString("URL"))) {
                    sb.append("<URL>").append(resultSet.getString("URL")).append("</URL>");

                } else {
                    sb.append("<URL>").append("NO").append("</URL>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
        } finally {
            try {

                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // System.out.println("this is string >> "+sb.toString());
        return sb.toString();
    }

    public String getLogisticsDocDetails(String instanceid, int id, String database) throws ServiceLocatorException {

        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        //queryString = "select DOCUMENT.ISA_NUMBER,DOCUMENT.DOCUMENT_TYPE,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH from DOCUMENT LEFT OUTER JOIN FILES on (DOCUMENT.FILE_ID= FILES.FILE_ID) where FILES.ISA_NUMBER LIKE '%"+isaNumber+"%'";
        if ("ARCHIVE".equals(database)) {
            queryString = "select ARCHIVE_FILES.FILE_ID,ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.SENDER_ID,"
                    + "ARCHIVE_FILES.RECEIVER_ID,ARCHIVE_FILES.PRE_TRANS_FILEPATH,"
                    + "ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.SEC_KEY_VAL as SEC_KEY_VAL,"
                    + "ARCHIVE_FILES.PRI_KEY_TYPE as PRI_KEY_TYPE,ARCHIVE_FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                    + "ARCHIVE_FILES.ORG_FILEPATH as ORG_FILEPATH,ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,"
                    + "ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,"
                    + "ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,TP1.NAME as SENDER_NAME,"
                    + "TP2.NAME as RECEIVER_NAME,ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,ARCHIVE_FILES.ISA_DATE as ISA_DATE,"
                    + "ARCHIVE_FILES.ISA_TIME as ISA_TIME,ARCHIVE_FILES.STATUS as STATUS,ARCHIVE_FILES.DIRECTION as DIRECTION,tl.BOL_NUMBER as BOL_NUMBER ,tl.CO_NUMBER as CO_NUMBER,tl.PO_NUMBER as PO_NUMBER  "
                    + "FROM ARCHIVE_FILES LEFT OUTER JOIN ARCHIVE_Transport_loadtender tl on (tl.FILE_ID=ARCHIVE_FILES.FILE_ID and tl.SHIPMENT_ID=ARCHIVE_FILES.SEC_KEY_VAL) "
                    + "LEFT OUTER JOIN TP TP1 "
                    + "ON (TP1.ID=ARCHIVE_FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 "
                    + "ON (TP2.ID = ARCHIVE_FILES.RECEIVER_ID) "
                    + "where ARCHIVE_FILES.FILE_ID LIKE '%" + instanceid + "%' and ARCHIVE_FILES.ID =" + id;
        } else {
            queryString = "select FILES.FILE_ID,FILES.FILE_TYPE,FILES.SENDER_ID,"
                    + "FILES.RECEIVER_ID,FILES.PRE_TRANS_FILEPATH,"
                    + "FILES.POST_TRANS_FILEPATH,FILES.SEC_KEY_VAL as SEC_KEY_VAL,"
                    + "FILES.PRI_KEY_TYPE as PRI_KEY_TYPE,FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                    + "FILES.ORG_FILEPATH as ORG_FILEPATH,FILES.ISA_NUMBER as ISA_NUMBER,"
                    + "FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,"
                    + "FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,TP1.NAME as SENDER_NAME,"
                    + "TP2.NAME as RECEIVER_NAME,FILES.ERR_MESSAGE,FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "FILES.ACK_FILE_ID as ACK_FILE_ID,FILES.ISA_DATE as ISA_DATE,"
                    + "FILES.ISA_TIME as ISA_TIME,FILES.STATUS as STATUS,FILES.DIRECTION as DIRECTION,tl.BOL_NUMBER as BOL_NUMBER ,tl.CO_NUMBER as CO_NUMBER,tl.PO_NUMBER as PO_NUMBER  "
                    + "FROM FILES LEFT OUTER JOIN Transport_loadtender tl on (tl.FILE_ID=FILES.FILE_ID and tl.SHIPMENT_ID=FILES.SEC_KEY_VAL) "
                    + "LEFT OUTER JOIN TP TP1 "
                    + "ON (TP1.ID=FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 "
                    + "ON (TP2.ID = FILES.RECEIVER_ID) "
                    + "where FILES.FILE_ID LIKE '%" + instanceid + "%' and FILES.ID =" + id;
        }

        System.out.println("QUERY IS " + queryString);

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILEID>" + resultSet.getString("FILE_ID") + "</FILEID>");
                } else {
                    sb.append("<FILEID>--</FILEID>");
                }
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILETYPE>" + resultSet.getString("FILE_TYPE") + "</FILETYPE>");
                } else {
                    sb.append("<FILETYPE>--</FILETYPE>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDERID>" + resultSet.getString("SENDER_ID") + "</SENDERID>");
                } else {
                    sb.append("<SENDERID>--</SENDERID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVERID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVERID>");
                } else {
                    sb.append("<RECEIVERID>--</RECEIVERID>");
                }
                if (resultSet.getString("DIRECTION") != null && !"".equals(resultSet.getString("DIRECTION"))) {
                    sb.append("<DIRECTION>" + resultSet.getString("DIRECTION") + "</DIRECTION>");
                } else {
                    sb.append("<DIRECTION>--</DIRECTION>");
                }
                if (resultSet.getString("BOL_NUMBER") != null && !"".equals(resultSet.getString("BOL_NUMBER"))) {
                    sb.append("<BOL_NUMBER>" + resultSet.getString("BOL_NUMBER").trim() + "</BOL_NUMBER>");
                } else {
                    sb.append("<BOL_NUMBER>--</BOL_NUMBER>");
                }

                if (resultSet.getString("CO_NUMBER") != null && !"".equals(resultSet.getString("CO_NUMBER"))) {
                    sb.append("<CO_NUMBER>" + resultSet.getString("CO_NUMBER").trim() + "</CO_NUMBER>");
                } else {
                    sb.append("<CO_NUMBER>--</CO_NUMBER>");
                }

                if (resultSet.getString("PO_NUMBER") != null && !"".equals(resultSet.getString("PO_NUMBER"))) {
                    sb.append("<PO_NUMBER>" + resultSet.getString("PO_NUMBER").trim() + "</PO_NUMBER>");
                } else {
                    sb.append("<PO_NUMBER>--</PO_NUMBER>");
                }
                if (resultSet.getString("SENDER_NAME") != null && !"".equals(resultSet.getString("SENDER_NAME"))) {
                    sb.append("<SENDER_NAME>" + resultSet.getString("SENDER_NAME") + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_NAME") != null && !"".equals(resultSet.getString("RECEIVER_NAME"))) {
                    sb.append("<RECEIVER_NAME>" + resultSet.getString("RECEIVER_NAME") + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                }
                if (resultSet.getString("SEC_KEY_VAL") != null && !"".equals(resultSet.getString("SEC_KEY_VAL"))) {
                    sb.append("<SEC_KEY_VAL>" + resultSet.getString("SEC_KEY_VAL") + "</SEC_KEY_VAL>");
                } else {
                    sb.append("<SEC_KEY_VAL>--</SEC_KEY_VAL>");
                }
                // sb.append("<ISA_DATE>" +resultSet.getString("isa_date") + "</ISA_DATE>");
                //  sb.append("<ISA_TIME>" +resultSet.getString("isa_time") + "</ISA_TIME>");
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISA_NUMBER>" + resultSet.getString("ISA_NUMBER") + "</ISA_NUMBER>");
                } else {
                    sb.append("<ISA_NUMBER>--</ISA_NUMBER>");
                }
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>--</GS_CONTROL_NUMBER>");
                }
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>--</ST_CONTROL_NUMBER>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISA_DATE>" + resultSet.getString("ISA_DATE") + "</ISA_DATE>");
                } else {
                    sb.append("<ISA_DATE>--</ISA_DATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISA_TIME>" + resultSet.getString("ISA_TIME") + "</ISA_TIME>");
                } else {
                    sb.append("<ISA_TIME>--</ISA_TIME>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }

                //  System.out.println("pri key type--->"+resultSet.getString("PRI_KEY_TYPE")+"----pri key val--->"+resultSet.getString("PRI_KEY_VAL"));
                if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("RID")) {
                    sb.append("<PRI_KEY_TYPE>RID</PRI_KEY_TYPE>");
                }
                if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("SID")) {
                    sb.append("<PRI_KEY_TYPE> SID </PRI_KEY_TYPE>");
                } else if (resultSet.getString("PRI_KEY_TYPE") != null) {
                    sb.append("<PRI_KEY_TYPE> " + resultSet.getString("PRI_KEY_TYPE") + " </PRI_KEY_TYPE>");
                }

                // sb.append("<PRI_KEY_VAL>" +resultSet.getString("PRI_KEY_VAL") + "</PRI_KEY_VAL>");
                if (resultSet.getString("PRI_KEY_VAL") != null && !"".equals(resultSet.getString("PRI_KEY_VAL"))) {
                    sb.append("<PRI_KEY_VAL>" + resultSet.getString("PRI_KEY_VAL") + "</PRI_KEY_VAL>");
                } else {
                    sb.append("<PRI_KEY_VAL>--</PRI_KEY_VAL>");
                }

                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }

                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }

                if (resultSet.getString("ORG_FILEPATH") != null) {
                    if (new File(resultSet.getString("ORG_FILEPATH")).exists() && new File(resultSet.getString("ORG_FILEPATH")).isFile()) {
                        sb.append("<ORG_FILEPATH>" + resultSet.getString("ORG_FILEPATH") + "</ORG_FILEPATH>");
                    } else {
                        sb.append("<ORG_FILEPATH>No File</ORG_FILEPATH>");
                    }
                } else {
                    sb.append("<ORG_FILEPATH>No File</ORG_FILEPATH>");
                }

                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }

                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    // System.out.println("hiiii");
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    // System.out.println("hiiii else");
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }
                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // System.out.println("this is string >> "+sb.toString());
        return sb.toString();
    }

    public String getLoadTenderingDetails(String instanceid, String ponum, String database) throws ServiceLocatorException {

        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        //queryString = "select DOCUMENT.ISA_NUMBER,DOCUMENT.DOCUMENT_TYPE,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH from DOCUMENT LEFT OUTER JOIN FILES on (DOCUMENT.FILE_ID= FILES.FILE_ID) where FILES.ISA_NUMBER LIKE '%"+isaNumber+"%'";
        if ("ARCHIVE".equals(database)) {
            queryString = "SELECT ARCHIVE_FILES.FILE_ID,ARCHIVE_FILES.FILE_TYPE,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                    + "ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.SEC_KEY_VAL as SEC_KEY_VAL,"
                    + "ARCHIVE_FILES.PRI_KEY_TYPE as PRI_KEY_TYPE,ARCHIVE_FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                    + "ARCHIVE_FILES.ORG_FILEPATH as ORG_FILEPATH,ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,"
                    + "ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,"
                    + "ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,ARCHIVE_FILES.DIRECTION as DIRECTION , ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME,ARCHIVE_FILES.STATUS, "
                    + "ARCHIVE_Transport_loadtender.BOL_NUMBER as BOL_NUMBER,ARCHIVE_Transport_loadtender.CO_NUMBER as CO_NUMBER, ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH, "
                    + "ARCHIVE_Transport_loadtender.TOTAL_WEIGHT as TOTAL_WEIGHT,ARCHIVE_Transport_loadtender.TOTAL_VOLUME as TOTAL_VOLUME,"
                    + "ARCHIVE_Transport_loadtender.TOTAL_PIECES as TOTAL_PIECES,ARCHIVE_Transport_loadtender.PO_NUMBER as PO_NUMBER FROM ARCHIVE_FILES  "
                    + "LEFT OUTER JOIN ARCHIVE_Transport_loadtender  ON (ARCHIVE_FILES.FILE_ID=ARCHIVE_Transport_loadtender.FILE_ID and ARCHIVE_FILES.SEC_KEY_VAL=ARCHIVE_Transport_loadtender.SHIPMENT_ID)   "
                    + "LEFT OUTER JOIN TP TP1 ON(TP1.ID=ARCHIVE_FILES.SENDER_ID ) LEFT OUTER JOIN TP TP2 ON(TP2.ID=ARCHIVE_FILES.RECEIVER_ID) "
                    + "where ARCHIVE_FILES.FILE_ID LIKE '%" + instanceid + "%' and ARCHIVE_FILES.SEC_KEY_VAL LIKE '%" + ponum + "%'";
        } else {
            queryString = "SELECT FILES.FILE_ID,FILES.FILE_TYPE,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                    + "FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.SEC_KEY_VAL as SEC_KEY_VAL,"
                    + "FILES.PRI_KEY_TYPE as PRI_KEY_TYPE,FILES.PRI_KEY_VAL as PRI_KEY_VAL,"
                    + "FILES.ORG_FILEPATH as ORG_FILEPATH,FILES.ISA_NUMBER as ISA_NUMBER,"
                    + "FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME,"
                    + "FILES.ERR_MESSAGE,FILES.ACK_FILE_ID as ACK_FILE_ID,FILES.DIRECTION as DIRECTION , FILES.ISA_DATE as ISA_DATE,FILES.ISA_TIME as ISA_TIME,FILES.STATUS, "
                    + "Transport_loadtender.BOL_NUMBER as BOL_NUMBER,Transport_loadtender.CO_NUMBER as CO_NUMBER, FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH, "
                    + "Transport_loadtender.TOTAL_WEIGHT as TOTAL_WEIGHT,Transport_loadtender.TOTAL_VOLUME as TOTAL_VOLUME, "
                    + "Transport_loadtender.TOTAL_PIECES as TOTAL_PIECES,Transport_loadtender.PO_NUMBER as PO_NUMBER FROM FILES  LEFT OUTER JOIN Transport_loadtender  ON (FILES.FILE_ID=Transport_loadtender.FILE_ID and FILES.SEC_KEY_VAL=Transport_loadtender.SHIPMENT_ID)   LEFT OUTER JOIN TP TP1 ON(TP1.ID=FILES.SENDER_ID ) LEFT OUTER JOIN TP TP2 ON(TP2.ID=FILES.RECEIVER_ID) "
                    + "where FILES.FILE_ID LIKE '%" + instanceid + "%' and FILES.SEC_KEY_VAL LIKE '%" + ponum + "%'";
        }
        System.out.println("QUERY  of the data IS " + queryString);

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILEID>" + resultSet.getString("FILE_ID").trim() + "</FILEID>");
                } else {
                    sb.append("<FILEID>--</FILEID>");
                }
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILETYPE>" + resultSet.getString("FILE_TYPE").trim() + "</FILETYPE>");
                } else {
                    sb.append("<FILETYPE>--</FILETYPE>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDERID>" + resultSet.getString("SENDER_ID").trim() + "</SENDERID>");
                } else {
                    sb.append("<SENDERID>--</SENDERID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVERID>" + resultSet.getString("RECEIVER_ID").trim() + "</RECEIVERID>");
                } else {
                    sb.append("<RECEIVERID>--</RECEIVERID>");
                }
                if (resultSet.getString("DIRECTION") != null && !"".equals(resultSet.getString("DIRECTION"))) {
                    sb.append("<DIRECTION>" + resultSet.getString("DIRECTION").trim() + "</DIRECTION>");
                } else {
                    sb.append("<DIRECTION>--</DIRECTION>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISA_DATE>" + resultSet.getString("ISA_DATE").trim() + "</ISA_DATE>");
                } else {
                    sb.append("<ISA_DATE>--</ISA_DATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISA_TIME>" + resultSet.getString("ISA_TIME").trim() + "</ISA_TIME>");
                } else {
                    sb.append("<ISA_TIME>--</ISA_TIME>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS").trim() + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("BOL_NUMBER") != null && !"".equals(resultSet.getString("BOL_NUMBER"))) {
                    sb.append("<BOL_NUMBER>" + resultSet.getString("BOL_NUMBER").trim() + "</BOL_NUMBER>");
                } else {
                    sb.append("<BOL_NUMBER>--</BOL_NUMBER>");
                }

                // sb.append("<>" +resultSet.getString("PO_NUMBER").trim() + "</PO_NUMBER>");
                if (resultSet.getString("PO_NUMBER") != null && !"".equals(resultSet.getString("PO_NUMBER"))) {
                    sb.append("<PO_NUMBER>" + resultSet.getString("PO_NUMBER").trim() + "</PO_NUMBER>");
                } else {
                    sb.append("<PO_NUMBER>--</PO_NUMBER>");
                }
                if (resultSet.getString("CO_NUMBER") != null && !"".equals(resultSet.getString("CO_NUMBER"))) {
                    sb.append("<CO_NUMBER>" + resultSet.getString("CO_NUMBER").trim() + "</CO_NUMBER>");
                } else {
                    sb.append("<CO_NUMBER>--</CO_NUMBER>");
                }
                if (resultSet.getString("TOTAL_WEIGHT") != null && !"".equals(resultSet.getString("TOTAL_WEIGHT"))) {
                    sb.append("<TOTAL_WEIGHT>" + resultSet.getString("TOTAL_WEIGHT").trim() + "</TOTAL_WEIGHT>");
                } else {
                    sb.append("<TOTAL_WEIGHT>--</TOTAL_WEIGHT>");
                }
                if (resultSet.getString("TOTAL_PIECES") != null && !"".equals(resultSet.getString("TOTAL_PIECES"))) {
                    sb.append("<TOTAL_PIECES>" + resultSet.getString("TOTAL_PIECES").trim() + "</TOTAL_PIECES>");
                } else {
                    sb.append("<TOTAL_PIECES>--</TOTAL_PIECES>");
                }
                if (resultSet.getString("TOTAL_VOLUME") != null && !"".equals(resultSet.getString("TOTAL_VOLUME"))) {
                    sb.append("<TOTAL_VOLUME>" + resultSet.getString("TOTAL_VOLUME").trim() + "</TOTAL_VOLUME>");
                } else {
                    sb.append("<TOTAL_VOLUME>--</TOTAL_VOLUME>");
                }
                if (resultSet.getString("SENDER_NAME") != null && !"".equals(resultSet.getString("SENDER_NAME"))) {
                    sb.append("<SENDER_NAME>" + resultSet.getString("SENDER_NAME").trim() + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                }
                // sb.append("<>" +resultSet.getString("SENDER_NAME").trim() + "</SENDER_NAME>");
                if (resultSet.getString("RECEIVER_NAME") != null && !"".equals(resultSet.getString("RECEIVER_NAME"))) {
                    sb.append("<RECEIVER_NAME>" + resultSet.getString("RECEIVER_NAME").trim() + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                }
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER").trim() + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>--</ST_CONTROL_NUMBER>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISA_NUMBER>" + resultSet.getString("ISA_NUMBER").trim() + "</ISA_NUMBER>");
                } else {
                    sb.append("<ISA_NUMBER>--</ISA_NUMBER>");
                }
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER").trim() + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<FILEID>--</FILEID>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE").trim() + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("SEC_KEY_VAL") != null && !"".equals(resultSet.getString("SEC_KEY_VAL"))) {
                    sb.append("<SEC_KEY_VAL>" + resultSet.getString("SEC_KEY_VAL").trim() + "</SEC_KEY_VAL>");
                } else {
                    sb.append("<SEC_KEY_VAL>--</SEC_KEY_VAL>");
                }

                if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("RID")) {
                    sb.append("<PRI_KEY_TYPE>RID</PRI_KEY_TYPE>");
                } else if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("SID")) {
                    sb.append("<PRI_KEY_TYPE> SID </PRI_KEY_TYPE>");
                } else if (resultSet.getString("PRI_KEY_TYPE") != null && resultSet.getString("PRI_KEY_TYPE").equalsIgnoreCase("BOL")) {
                    sb.append("<PRI_KEY_TYPE> BOL </PRI_KEY_TYPE>");
                } else {
                    sb.append("<PRI_KEY_TYPE>--</PRI_KEY_TYPE>");
                }

                if (resultSet.getString("PRI_KEY_VAL") != null && !"".equals(resultSet.getString("PRI_KEY_VAL"))) {
                    sb.append("<PRI_KEY_VAL>" + resultSet.getString("PRI_KEY_VAL").trim() + "</PRI_KEY_VAL>");
                } else {
                    sb.append("<PRI_KEY_VAL>--</PRI_KEY_VAL>");
                }

                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH").trim() + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }

                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH").trim() + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }

                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID").trim() + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }

                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }

                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE").trim() + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("this is load tendering string >> " + sb.toString());
        return sb.toString();
    }

    public String getLtResponseDetails(String fileId, String refId, String database) throws ServiceLocatorException {

        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        if ("ARCHIVE".equals(database)) {
            queryString = "SELECT DISTINCT(ARCHIVE_FILES.FILE_ID) as FILE_ID,ARCHIVE_TRANSPORT_LT_RESPONSE.SHIPMENT_ID as SHIPMENT_ID,"
                    + "ARCHIVE_FILES.FILE_TYPE as FILE_TYPE,"
                    + "ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,TP1.ID as SENDER_ID,TP1.NAME as SENDER_NAME,TP2.ID as RECEIVER_ID,"
                    + "TP2.NAME as RECEIVER_NAME,ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME, ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "ARCHIVE_FILES.STATUS as STATUS,ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.ACK_FILE_ID,ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.SEC_KEY_VAL as REFERENCE"
                    + " FROM ARCHIVE_TRANSPORT_LT_RESPONSE LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_TRANSPORT_LT_RESPONSE.FILE_ID =ARCHIVE_FILES.FILE_ID)"
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=ARCHIVE_FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID=ARCHIVE_FILES.RECEIVER_ID) "
                    + "WHERE 1=1 AND ARCHIVE_TRANSPORT_LT_RESPONSE.FILE_ID = '" + fileId + "' AND ARCHIVE_TRANSPORT_LT_RESPONSE.REF_ID='" + refId + "'  AND ARCHIVE_FILES.FLOWFLAG = 'L'  ";
        } else {
            queryString = "SELECT DISTINCT(FILES.FILE_ID) as FILE_ID,TRANSPORT_LT_RESPONSE.SHIPMENT_ID as SHIPMENT_ID,"
                    + "FILES.FILE_TYPE as FILE_TYPE,"
                    + "FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,TP1.ID as SENDER_ID,TP1.NAME as SENDER_NAME,TP2.ID as RECEIVER_ID,"
                    + "TP2.NAME as RECEIVER_NAME,FILES.ISA_NUMBER as ISA_NUMBER,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,FILES.ISA_DATE as ISA_DATE,FILES.ISA_TIME as ISA_TIME, FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "FILES.STATUS as STATUS,FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.ACK_FILE_ID,FILES.ERR_MESSAGE,FILES.SEC_KEY_VAL as REFERENCE"
                    + " FROM TRANSPORT_LT_RESPONSE LEFT OUTER JOIN FILES ON (TRANSPORT_LT_RESPONSE.FILE_ID =FILES.FILE_ID)"
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID=FILES.RECEIVER_ID) "
                    + "WHERE 1=1 AND TRANSPORT_LT_RESPONSE.FILE_ID = '" + fileId + "' AND TRANSPORT_LT_RESPONSE.REF_ID='" + refId + "'  AND FILES.FLOWFLAG = 'L'  ";
        }
        // System.out.println("invNumber--->"+invNumber); 

        System.out.println("LT Response QUERY IS " + queryString);

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILE_ID>" + resultSet.getString("FILE_ID") + "</FILE_ID>");
                } else {
                    sb.append("<FILE_ID>--</FILE_ID>");
                }
                if (resultSet.getString("SHIPMENT_ID") != null && !"".equals(resultSet.getString("SHIPMENT_ID"))) {
                    sb.append("<SHIPMENT_ID>" + resultSet.getString("SHIPMENT_ID") + "</SHIPMENT_ID>");
                } else {
                    sb.append("<SHIPMENT_ID>--</SHIPMENT_ID>");
                }
                if (resultSet.getString("FILE_TYPE") != null && !"".equals(resultSet.getString("FILE_TYPE"))) {
                    sb.append("<FILE_TYPE>" + resultSet.getString("FILE_TYPE") + "</FILE_TYPE>");
                } else {
                    sb.append("<FILETYPE>--</FILETYPE>");
                }
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDER_ID>" + resultSet.getString("SENDER_ID") + "</SENDER_ID>");
                } else {
                    sb.append("<SENDER_ID>--</SENDER_ID>");
                }
                if (resultSet.getString("SENDER_NAME") != null && !"".equals(resultSet.getString("SENDER_NAME"))) {
                    sb.append("<SENDER_NAME>" + resultSet.getString("SENDER_NAME") + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVER_ID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVER_ID>");
                } else {
                    sb.append("<RECEIVER_ID>--</RECEIVER_ID>");
                }
                if (resultSet.getString("RECEIVER_NAME") != null && !"".equals(resultSet.getString("RECEIVER_NAME"))) {
                    sb.append("<RECEIVER_NAME>" + resultSet.getString("RECEIVER_NAME") + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISA_NUMBER>" + resultSet.getString("ISA_NUMBER") + "</ISA_NUMBER>");
                } else {
                    sb.append("<ISA_NUMBER>--</ISA_NUMBER>");
                }
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>--</GS_CONTROL_NUMBER>");
                }
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>--</ST_CONTROL_NUMBER>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISA_DATE>" + resultSet.getString("ISA_DATE") + "</ISA_DATE>");
                } else {
                    sb.append("<ISA_DATE>--</ISA_DATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISA_TIME>" + resultSet.getString("ISA_TIME") + "</ISA_TIME>");
                } else {
                    sb.append("<ISA_TIME>--</ISA_TIME>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("REFERENCE") != null && !"".equals(resultSet.getString("REFERENCE"))) {
                    sb.append("<REFERENCE>" + resultSet.getString("REFERENCE") + "</REFERENCE>");
                } else {
                    sb.append("<REFERENCE>--</REFERENCE>");
                }
                if (resultSet.getString("PRE_TRANS_FILEPATH") != null && !"".equals(resultSet.getString("PRE_TRANS_FILEPATH"))) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }

                if (resultSet.getString("POST_TRANS_FILEPATH") != null && !"".equals(resultSet.getString("POST_TRANS_FILEPATH"))) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }
                /*   
                 if(resultSet.getString("ORG_FILEPATH")!=null){ 
                 if(new File(resultSet.getString("ORG_FILEPATH")).exists()){
                 sb.append("<ORGFILEPATH>" +resultSet.getString("ORG_FILEPATH") + "</ORGFILEPATH>");
                 }else{
                 sb.append("<ORGFILEPATH>No File</ORGFILEPATH>"); 
                 }
                 }else{
                 sb.append("<ORGFILEPATH>No File</ORGFILEPATH>"); 
                 } 
                 */
                if (resultSet.getString("ACK_FILE_ID") != null && !"".equals(resultSet.getString("ACK_FILE_ID"))) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }

                // System.out.println("ERR_MESSAGE---->"+resultSet.getString("ERR_MESSAGE"));
                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    //  System.out.println("hiiii");
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    //  System.out.println("hiiii else");
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }
                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // System.out.println("this is string >> "+sb.toString());
        return sb.toString();
    }
    /*
     * Date : 26-06-2013
     * Logistics Invoice DetailInformation
     * 
     * 
     */

    public String getLogisticsInvDetails(String invNumber, int id, String database) throws ServiceLocatorException {

        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();// ARCHIVE_FILES
        if ("ARCHIVE".equals(database)) {
            queryString = "SELECT ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "TRANSPORT_INVOICE.FILE_ID as FILE_ID,TRANSPORT_INVOICE.INVOICE_NUMBER as INVOICE_NUMBER,TRANSPORT_INVOICE.PO_NUMBER as PO_NUMBER,"
                    + "TRANSPORT_INVOICE.TOTAL_WEIGHT as TOTAL_WEIGHT,TRANSPORT_INVOICE.TOTAL_AMOUNT as TOTAL_AMOUNT,"
                    + "ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME,"
                    + " ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                    + "ARCHIVE_FILES.ORG_FILEPATH,ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.STATUS,ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME "
                    + " FROM TRANSPORT_INVOICE "
                    + "LEFT OUTER JOIN ARCHIVE_FILES ON (TRANSPORT_INVOICE.FILE_ID =ARCHIVE_FILES.FILE_ID) "
                    //+ "LEFT OUTER JOIN ARCHIVE_FILES ON (TRANSPORT_INVOICE.INVOICE_NUMBER = ARCHIVE_FILES.PRI_KEY_VAL AND TRANSPORT_INVOICE.FILE_ID = ARCHIVE_FILES.FILE_ID) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=ARCHIVE_FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = ARCHIVE_FILES.RECEIVER_ID) "
                    + " WHERE TRANSPORT_INVOICE.INVOICE_NUMBER LIKE '%" + invNumber + "%' AND TRANSPORT_INVOICE.ID =" + id;
        } else {
            queryString = "SELECT FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "TRANSPORT_INVOICE.FILE_ID as FILE_ID,TRANSPORT_INVOICE.INVOICE_NUMBER as INVOICE_NUMBER,TRANSPORT_INVOICE.PO_NUMBER as PO_NUMBER,"
                    + "TRANSPORT_INVOICE.TOTAL_WEIGHT as TOTAL_WEIGHT,TRANSPORT_INVOICE.TOTAL_AMOUNT as TOTAL_AMOUNT,"
                    + "FILES.ISA_NUMBER as ISA_NUMBER,FILES.ISA_DATE as ISA_DATE,FILES.ISA_TIME as ISA_TIME,"
                    + " FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                    + "FILES.ORG_FILEPATH,FILES.ERR_MESSAGE,FILES.STATUS,FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "FILES.ACK_FILE_ID as ACK_FILE_ID,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME "
                    + " FROM TRANSPORT_INVOICE "
                    + "LEFT OUTER JOIN FILES ON (TRANSPORT_INVOICE.FILE_ID =FILES.FILE_ID) "
                    //+ "LEFT OUTER JOIN FILES ON (TRANSPORT_INVOICE.INVOICE_NUMBER = FILES.PRI_KEY_VAL AND TRANSPORT_INVOICE.FILE_ID = FILES.FILE_ID) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = FILES.RECEIVER_ID) "
                    + " WHERE TRANSPORT_INVOICE.INVOICE_NUMBER LIKE '%" + invNumber + "%' AND TRANSPORT_INVOICE.ID =" + id;
        }
        // System.out.println("invNumber--->"+invNumber); 

        System.out.println("Logistics Invoice Details-->" + queryString);

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILEID>" + resultSet.getString("FILE_ID") + "</FILEID>");
                } else {
                    sb.append("<FILEID>--</FILEID>");
                }
                if (resultSet.getString("INVOICE_NUMBER") != null && !"".equals(resultSet.getString("INVOICE_NUMBER"))) {
                    sb.append("<INVNUMBER>" + resultSet.getString("INVOICE_NUMBER") + "</INVNUMBER>");
                } else {
                    sb.append("<INVNUMBER>--</INVNUMBER>");
                }
                if (resultSet.getString("PO_NUMBER") != null && !"".equals(resultSet.getString("PO_NUMBER"))) {
                    sb.append("<PONUMBER>" + resultSet.getString("PO_NUMBER") + "</PONUMBER>");
                } else {
                    sb.append("<PONUMBER>--</PONUMBER>");
                }
                if (resultSet.getString("TOTAL_WEIGHT") != null && !"".equals(resultSet.getString("TOTAL_WEIGHT"))) {
                    sb.append("<ITEMQTY>" + resultSet.getString("TOTAL_WEIGHT") + "</ITEMQTY>");
                } else {
                    sb.append("<ITEMQTY>--</ITEMQTY>");
                }
                if (resultSet.getString("TOTAL_AMOUNT") != null && !"".equals(resultSet.getString("TOTAL_AMOUNT"))) {
                    sb.append("<INVAMT>" + resultSet.getString("TOTAL_AMOUNT") + "</INVAMT>");
                } else {
                    sb.append("<INVAMT>--</INVAMT>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISANUM>" + resultSet.getString("ISA_NUMBER") + "</ISANUM>");
                } else {
                    sb.append("<ISANUM>--</ISANUM>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISADATE>" + resultSet.getString("ISA_DATE") + "</ISADATE>");
                } else {
                    sb.append("<ISADATE>--</ISADATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISATIME>" + resultSet.getString("ISA_TIME") + "</ISATIME>");
                } else {
                    sb.append("<ISATIME>--</ISATIME>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDER_ID>" + resultSet.getString("SENDER_ID") + "</SENDER_ID>");
                } else {
                    sb.append("<SENDER_ID>--</SENDER_ID>");
                }
                if (resultSet.getString("RECEIVER_ID") != null && !"".equals(resultSet.getString("RECEIVER_ID"))) {
                    sb.append("<RECEIVER_ID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVER_ID>");
                } else {
                    sb.append("<RECEIVER_ID>--</RECEIVER_ID>");
                }
                if (resultSet.getString("SENDER_NAME") != null && !"".equals(resultSet.getString("SENDER_NAME"))) {
                    sb.append("<SENDER_NAME>" + resultSet.getString("SENDER_NAME") + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_NAME") != null && !"".equals(resultSet.getString("RECEIVER_NAME"))) {
                    sb.append("<RECEIVER_NAME>" + resultSet.getString("RECEIVER_NAME") + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                }
                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }

                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }

                if (resultSet.getString("ORG_FILEPATH") != null) {
                    if (new File(resultSet.getString("ORG_FILEPATH")).exists() && new File(resultSet.getString("ORG_FILEPATH")).isFile()) {
                        sb.append("<ORGFILEPATH>" + resultSet.getString("ORG_FILEPATH") + "</ORGFILEPATH>");
                    } else {
                        sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                    }
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }

                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }

                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }

                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    sb.append("<ST_CONTROL_NUMBER>--</ST_CONTROL_NUMBER>");
                }
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    sb.append("<GS_CONTROL_NUMBER>--</GS_CONTROL_NUMBER>");
                }
                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // System.out.println("this is string >> "+sb.toString());
        return sb.toString();
    }

    /*
     * Date : 20-06-2013
     * Logistics Invoice DetailInformation
     * 
     * 
     */
    public String getLogisticsShipmentDetails(String asnNumber, String poNumber, int id, String database) throws ServiceLocatorException {

        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        if ("ARCHIVE".equals(database)) {
            queryString = "SELECT ARCHIVE_FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "ARCHIVE_TRANSPORT_SHIPMENT.FILE_ID as FILE_ID,ARCHIVE_TRANSPORT_SHIPMENT.SHIPMENT_ID as SHIPMENT_ID,ARCHIVE_TRANSPORT_SHIPMENT.PO_NUMBER as PO_NUMBER,"
                    + "ARCHIVE_TRANSPORT_SHIPMENT.TOTAL_WEIGHT as TOTAL_WEIGHT,ARCHIVE_TRANSPORT_SHIPMENT.TOTAL_VOLUME as TOTAL_VOLUME,"
                    + "ARCHIVE_FILES.ISA_NUMBER as ISA_NUMBER,ARCHIVE_FILES.ISA_DATE as ISA_DATE,ARCHIVE_FILES.ISA_TIME as ISA_TIME,"
                    + " ARCHIVE_FILES.PRE_TRANS_FILEPATH,ARCHIVE_FILES.POST_TRANS_FILEPATH,ARCHIVE_FILES.SENDER_ID,ARCHIVE_FILES.RECEIVER_ID,"
                    + "ARCHIVE_FILES.ORG_FILEPATH,ARCHIVE_FILES.ERR_MESSAGE,ARCHIVE_FILES.STATUS,ARCHIVE_FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "ARCHIVE_FILES.ACK_FILE_ID as ACK_FILE_ID,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME "
                    + " FROM ARCHIVE_TRANSPORT_SHIPMENT "
                    + "LEFT OUTER JOIN ARCHIVE_FILES ON (ARCHIVE_TRANSPORT_SHIPMENT.FILE_ID =ARCHIVE_FILES.FILE_ID) "
                    // + "LEFT OUTER JOIN ARCHIVE_FILES ON ((ARCHIVE_TRANSPORT_SHIPMENT.SHIPMENT_ID =ARCHIVE_FILES.SEC_KEY_VAL) AND ((ARCHIVE_TRANSPORT_SHIPMENT.BOL_NUMBER =ARCHIVE_FILES.PRI_KEY_VAL) OR (ARCHIVE_TRANSPORT_SHIPMENT.STOP_SEQ_NUM =ARCHIVE_FILES.PRI_KEY_VAL))) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=ARCHIVE_FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = ARCHIVE_FILES.RECEIVER_ID) "
                    + " WHERE ARCHIVE_TRANSPORT_SHIPMENT.SHIPMENT_ID LIKE '%" + asnNumber + "%'  AND ARCHIVE_TRANSPORT_SHIPMENT.ID =" + id;
        } else {
            queryString = "SELECT FILES.TRANSACTION_TYPE as TRANSACTION_TYPE,FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                    + "TRANSPORT_SHIPMENT.FILE_ID as FILE_ID,TRANSPORT_SHIPMENT.SHIPMENT_ID as SHIPMENT_ID,TRANSPORT_SHIPMENT.PO_NUMBER as PO_NUMBER,"
                    + "TRANSPORT_SHIPMENT.TOTAL_WEIGHT as TOTAL_WEIGHT,TRANSPORT_SHIPMENT.TOTAL_VOLUME as TOTAL_VOLUME,"
                    + "FILES.ISA_NUMBER as ISA_NUMBER,FILES.ISA_DATE as ISA_DATE,FILES.ISA_TIME as ISA_TIME,"
                    + " FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH,FILES.SENDER_ID,FILES.RECEIVER_ID,"
                    + "FILES.ORG_FILEPATH,FILES.ERR_MESSAGE,FILES.STATUS,FILES.ERROR_REPORT_FILEPATH as ERROR_REPORT_FILEPATH,"
                    + "FILES.ACK_FILE_ID as ACK_FILE_ID,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME "
                    + " FROM TRANSPORT_SHIPMENT "
                    + "LEFT OUTER JOIN FILES ON (TRANSPORT_SHIPMENT.FILE_ID =FILES.FILE_ID) "
                    // + "LEFT OUTER JOIN FILES ON ((TRANSPORT_SHIPMENT.SHIPMENT_ID =FILES.SEC_KEY_VAL) AND ((TRANSPORT_SHIPMENT.BOL_NUMBER =FILES.PRI_KEY_VAL) OR (TRANSPORT_SHIPMENT.STOP_SEQ_NUM =FILES.PRI_KEY_VAL))) "
                    + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID = FILES.RECEIVER_ID) "
                    + " WHERE TRANSPORT_SHIPMENT.SHIPMENT_ID LIKE '%" + asnNumber + "%'  AND TRANSPORT_SHIPMENT.ID =" + id;
        }
        // System.out.println("invNumber--->"+invNumber); 
        // System.out.println("Logistics Shipment Details--> "+queryString);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<FILEID>" + resultSet.getString("FILE_ID") + "</FILEID>");
                } else {
                    sb.append("<FILEID>--</FILEID>");
                }
                if (resultSet.getString("SHIPMENT_ID") != null && !"".equals(resultSet.getString("SHIPMENT_ID"))) {
                    sb.append("<ASNNUMBER>" + resultSet.getString("SHIPMENT_ID") + "</ASNNUMBER>");
                } else {
                    sb.append("<ASNNUMBER>--</ASNNUMBER>");
                }
                if (resultSet.getString("PO_NUMBER") != null && !"".equals(resultSet.getString("PO_NUMBER"))) {
                    sb.append("<PONUMBER>" + resultSet.getString("PO_NUMBER") + "</PONUMBER>");
                } else {
                    sb.append("<PONUMBER>--</PONUMBER>");
                }
                if (resultSet.getString("TOTAL_WEIGHT") != null && !"".equals(resultSet.getString("TOTAL_WEIGHT"))) {
                    sb.append("<ITEMQTY>" + resultSet.getString("TOTAL_WEIGHT") + "</ITEMQTY>");
                } else {
                    sb.append("<ITEMQTY>--</ITEMQTY>");
                }
                if (resultSet.getString("TOTAL_VOLUME") != null && !"".equals(resultSet.getString("TOTAL_VOLUME"))) {
                    sb.append("<ASNVOLUME>" + resultSet.getString("TOTAL_VOLUME") + "</ASNVOLUME>");
                } else {
                    sb.append("<ASNVOLUME>--</ASNVOLUME>");
                }
                if (resultSet.getString("ISA_NUMBER") != null && !"".equals(resultSet.getString("ISA_NUMBER"))) {
                    sb.append("<ISANUM>" + resultSet.getString("ISA_NUMBER") + "</ISANUM>");
                } else {
                    sb.append("<ISANUM>--</ISANUM>");
                }
                if (resultSet.getString("ISA_DATE") != null && !"".equals(resultSet.getString("ISA_DATE"))) {
                    sb.append("<ISADATE>" + resultSet.getString("ISA_DATE") + "</ISADATE>");
                } else {
                    sb.append("<ISADATE>--</ISADATE>");
                }
                if (resultSet.getString("ISA_TIME") != null && !"".equals(resultSet.getString("ISA_TIME"))) {
                    sb.append("<ISATIME>" + resultSet.getString("ISA_TIME") + "</ISATIME>");
                } else {
                    sb.append("<ISATIME>--</ISATIME>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("SENDER_ID") != null && !"".equals(resultSet.getString("SENDER_ID"))) {
                    sb.append("<SENDER_ID>" + resultSet.getString("SENDER_ID") + "</SENDER_ID>");
                } else {
                    sb.append("<SENDER_ID>--</SENDER_ID>");
                }
                if (resultSet.getString("FILE_ID") != null && !"".equals(resultSet.getString("FILE_ID"))) {
                    sb.append("<RECEIVER_ID>" + resultSet.getString("RECEIVER_ID") + "</RECEIVER_ID>");
                } else {
                    sb.append("<FILEID>--</FILEID>");
                }
                if (resultSet.getString("SENDER_NAME") != null && !"".equals(resultSet.getString("SENDER_NAME"))) {
                    sb.append("<SENDER_NAME>" + resultSet.getString("SENDER_NAME") + "</SENDER_NAME>");
                } else {
                    sb.append("<SENDER_NAME>--</SENDER_NAME>");
                }
                if (resultSet.getString("RECEIVER_NAME") != null && !"".equals(resultSet.getString("RECEIVER_NAME"))) {
                    sb.append("<RECEIVER_NAME>" + resultSet.getString("RECEIVER_NAME") + "</RECEIVER_NAME>");
                } else {
                    sb.append("<RECEIVER_NAME>--</RECEIVER_NAME>");
                }

                if (resultSet.getString("PRE_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("PRE_TRANS_FILEPATH")).exists() && new File(resultSet.getString("PRE_TRANS_FILEPATH")).isFile()) {
                        sb.append("<PRETRANSFILEPATH>" + resultSet.getString("PRE_TRANS_FILEPATH") + "</PRETRANSFILEPATH>");
                    } else {
                        sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                    }
                } else {
                    sb.append("<PRETRANSFILEPATH>No File</PRETRANSFILEPATH>");
                }

                if (resultSet.getString("POST_TRANS_FILEPATH") != null) {
                    if (new File(resultSet.getString("POST_TRANS_FILEPATH")).exists() && new File(resultSet.getString("POST_TRANS_FILEPATH")).isFile()) {
                        sb.append("<POSTTRANSFILEPATH>" + resultSet.getString("POST_TRANS_FILEPATH") + "</POSTTRANSFILEPATH>");
                    } else {
                        sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                    }
                } else {
                    sb.append("<POSTTRANSFILEPATH>No File</POSTTRANSFILEPATH>");
                }

                if (resultSet.getString("ORG_FILEPATH") != null) {
                    if (new File(resultSet.getString("ORG_FILEPATH")).exists() && new File(resultSet.getString("ORG_FILEPATH")).isFile()) {
                        sb.append("<ORGFILEPATH>" + resultSet.getString("ORG_FILEPATH") + "</ORGFILEPATH>");
                    } else {
                        sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                    }
                } else {
                    sb.append("<ORGFILEPATH>No File</ORGFILEPATH>");
                }

                if (resultSet.getString("ACK_FILE_ID") != null) {
                    if (new File(resultSet.getString("ACK_FILE_ID")).exists() && new File(resultSet.getString("ACK_FILE_ID")).isFile()) {
                        sb.append("<ACKFILEID>" + resultSet.getString("ACK_FILE_ID") + "</ACKFILEID>");
                    } else {
                        sb.append("<ACKFILEID>No File</ACKFILEID>");
                    }
                } else {
                    sb.append("<ACKFILEID>No File</ACKFILEID>");
                }

                if (resultSet.getString("ERR_MESSAGE") != null && !"".equals(resultSet.getString("ERR_MESSAGE"))) {
                    sb.append("<ERR_MESSAGE>" + resultSet.getString("ERR_MESSAGE") + "</ERR_MESSAGE>");
                } else {
                    sb.append("<ERR_MESSAGE>NO MSG</ERR_MESSAGE>");
                }

                //TRANSACTION_TYPE,
                if (resultSet.getString("TRANSACTION_TYPE") != null && !"".equals(resultSet.getString("TRANSACTION_TYPE"))) {
                    sb.append("<TRANSACTION_TYPE>" + resultSet.getString("TRANSACTION_TYPE") + "</TRANSACTION_TYPE>");
                } else {
                    sb.append("<TRANSACTION_TYPE>--</TRANSACTION_TYPE>");
                }
                //SST_CONTROL_NUMBER,
                if (resultSet.getString("ST_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("ST_CONTROL_NUMBER"))) {
                    //  System.out.println("hiiii");
                    sb.append("<ST_CONTROL_NUMBER>" + resultSet.getString("ST_CONTROL_NUMBER") + "</ST_CONTROL_NUMBER>");
                } else {
                    //  System.out.println("hiiii else");
                    sb.append("<ST_CONTROL_NUMBER>--</ST_CONTROL_NUMBER>");
                }
                //GS_CONTROL_NUMBER,
                if (resultSet.getString("GS_CONTROL_NUMBER") != null && !"".equals(resultSet.getString("GS_CONTROL_NUMBER"))) {
                    //  System.out.println("hiiii");
                    sb.append("<GS_CONTROL_NUMBER>" + resultSet.getString("GS_CONTROL_NUMBER") + "</GS_CONTROL_NUMBER>");
                } else {
                    //  System.out.println("hiiii else");
                    sb.append("<GS_CONTROL_NUMBER>--</GS_CONTROL_NUMBER>");
                }

                if (resultSet.getString("ERROR_REPORT_FILEPATH") != null) {
                    if (new File(resultSet.getString("ERROR_REPORT_FILEPATH")).exists() && new File(resultSet.getString("ERROR_REPORT_FILEPATH")).isFile()) {
                        sb.append("<ERROR_REPORT_FILEPATH>" + resultSet.getString("ERROR_REPORT_FILEPATH") + "</ERROR_REPORT_FILEPATH>");
                    } else {
                        sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                    }
                } else {
                    sb.append("<ERROR_REPORT_FILEPATH>No File</ERROR_REPORT_FILEPATH>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // System.out.println("this is string >> "+sb.toString());
        return sb.toString();
    }

    /*
     * Method For DocVisibility Detail Info
     * Author : Santosh Kola
     * Date : 01/05/2014
     */
    public String getDocVisibilityDetails(int docId) throws ServiceLocatorException {
        Connection connection = null;
        CallableStatement callableStatement = null;
        StringBuffer sb = new StringBuffer();
        String responseString = null;
        boolean isGetting = false;
        System.out.println("docId-->" + docId);
        try {

            connection = ConnectionProvider.getInstance().getConnection();
            callableStatement = connection.prepareCall("CALL SPDOCVISIBILITY(?,?)");
            callableStatement.setInt(1, docId);
            callableStatement.registerOutParameter(2, Types.VARCHAR);
            boolean isExecute = callableStatement.execute();
            System.out.println("isExecute-->" + isExecute);
            responseString = callableStatement.getString(2);
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            if (responseString != null && !"".equals(responseString)) {
                if (!"".equals(responseString.trim())) {
                    //------------------------------------ START --------------------------------
                    sb.append("<DETAIL><VALID>true</VALID>");
                    String detailInfo[] = responseString.split("\\^");

                    if (detailInfo[0] != null && !"".equals(detailInfo[0])) {
                        if (!"".equals(detailInfo[0].trim())) {
                            System.out.println("detailInfo-->" + detailInfo[0]);
                            //---------- detailInfo[0] Start ------------------
                            String commonInfo[] = detailInfo[0].split("\\|");

                            sb.append("<FILE_ID>" + commonInfo[0] + "</FILE_ID>");
                            sb.append("<PARENT_FILE_ID>" + commonInfo[1] + "</PARENT_FILE_ID>");
                            sb.append("<FILE_TYPE>" + commonInfo[2] + "</FILE_TYPE>");
                            sb.append("<FILE_ORIGIN>" + commonInfo[3] + "</FILE_ORIGIN>");
                            sb.append("<TRAN_MESS_TYPE>" + commonInfo[4] + "</TRAN_MESS_TYPE>");
                            sb.append("<SENDER_ID>" + commonInfo[5] + "</SENDER_ID>");
                            sb.append("<RECEIVER_ID>" + commonInfo[6] + "</RECEIVER_ID>");
                            sb.append("<INTERCHANGE_CONTROLNO>" + commonInfo[7] + "</INTERCHANGE_CONTROLNO>");
                            sb.append("<FUNCTIONAL_CONTROLNO>" + commonInfo[8] + "</FUNCTIONAL_CONTROLNO>");
                            sb.append("<MESSAGE_CONTROLNO>" + commonInfo[9] + "</MESSAGE_CONTROLNO>");
                            sb.append("<DATE_TIME_RECEIVED>" + commonInfo[10] + "</DATE_TIME_RECEIVED>");
                            sb.append("<DIRECTION>" + commonInfo[11] + "</DIRECTION>");
                            sb.append("<STATUS>" + commonInfo[12] + "</STATUS>");
                            sb.append("<ERR_MESSAGE>" + commonInfo[13] + "</ERR_MESSAGE>");
                            sb.append("<ACK_STATUS>" + commonInfo[14] + "</ACK_STATUS>");
                            sb.append("<ID>" + commonInfo[15] + "</ID>");
                            sb.append("<ISA_TIME>" + commonInfo[16] + "</ISA_TIME>");
                            sb.append("<ISA_DATE>" + commonInfo[17] + "</ISA_DATE>");
                            //  String empId = "123";
                            // sb.append("<test userId=\""+empId+"\">" + commonInfo[17] + "</test>");

                            /*     String sapDetails = DataSourceDataProvider.getInstance().getSapDetails(commonInfo[0]);
                             String sapDetailsInfo[] = sapDetails.split("\\|");
                             sb.append("<SAP_USER>" + sapDetailsInfo[0] + "</SAP_USER>");
                             sb.append("<IDOC_NUMBER>" + sapDetailsInfo[1] + "</IDOC_NUMBER>");
                             sb.append("<PO_NUMBER>" + sapDetailsInfo[2] + "</PO_NUMBER>");
                             sb.append("<PO_DATE>" + sapDetailsInfo[3] + "</PO_DATE>");
                             sb.append("<IDOC_STATUS_CODE>" + sapDetailsInfo[4] + "</IDOC_STATUS_CODE>");
                             sb.append("<IDOC_STATUS_DESCRIPTION>" + sapDetailsInfo[5] + "</IDOC_STATUS_DESCRIPTION>");
                             */
                            //---------- detailInfo[0] End ------------------
                        }

                    }

                    if (detailInfo[1] != null && !"".equals(detailInfo[1])) {
                        if (!"".equals(detailInfo[1].trim())) {
                            System.out.println("App fields-->" + detailInfo[1]);
                            String appFieldInfo[] = detailInfo[1].split("\\*");
                            sb.append("<APPFIELDS>");
                            for (int i = 0; i < appFieldInfo.length; i++) {
                                sb.append("<APPFIELD label=\"" + appFieldInfo[i].split("\\@")[0] + "\">");
                                if (appFieldInfo[i].split("\\@").length == 2) {
                                    sb.append(appFieldInfo[i].split("\\@")[1]);
                                } else {
                                    sb.append("_");
                                }
                                sb.append("</APPFIELD>");
                            }
                            sb.append("</APPFIELDS>");

                        }
                    }

                    sb.append("</DETAIL>");
                    isGetting = true;

                    //------------------------------------ END --------------------------------  
                }
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }
            sb.append("</DETAILS>");
            sb.append("</xml>");
            // System.out.println("Response String-->" + callableStatement.getString(2));
            //sb.append(callableStatement.getString(2));

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
        } finally {
            try {
                if (callableStatement != null) {
                    callableStatement.close();
                    callableStatement = null;
                }

                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return sb.toString();
    }

    public String getPartnerDetails(String partnerId) throws ServiceLocatorException {

        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();

        // System.out.println("invNumber--->"+invNumber); 
        queryString = "select tp.ID as TP_ID,tp.NAME as TP_NAME,tp_details.INTERNALIDENTIFIER,tp_details.APPLICATIONID,tp_details.STATE,tp.STATUS,tp.MODIFIED_TS,tp.MODIFIED_BY,tp.CREATED_TS from tp LEFT OUTER JOIN tp_details on(tp_details.TP_ID=tp.ID) WHERE 1=1 AND tp.ID='" + partnerId + "'";

        System.out.println("queryString--> " + queryString);

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                if (resultSet.getString("TP_ID") != null && !"".equals(resultSet.getString("TP_ID"))) {
                    sb.append("<TP_ID>" + resultSet.getString("TP_ID") + "</TP_ID>");
                } else {
                    sb.append("<TP_ID>--</TP_ID>");
                }
                if (resultSet.getString("TP_NAME") != null && !"".equals(resultSet.getString("TP_NAME"))) {
                    sb.append("<TP_NAME>" + resultSet.getString("TP_NAME") + "</TP_NAME>");
                } else {
                    sb.append("<TP_NAME>--</TP_NAME>");
                }
                if (resultSet.getString("INTERNALIDENTIFIER") != null && !"".equals(resultSet.getString("INTERNALIDENTIFIER"))) {
                    sb.append("<INTERNALIDENTIFIER>" + resultSet.getString("INTERNALIDENTIFIER") + "</INTERNALIDENTIFIER>");
                } else {
                    sb.append("<INTERNALIDENTIFIER>--</INTERNALIDENTIFIER>");
                }
                if (resultSet.getString("APPLICATIONID") != null && !"".equals(resultSet.getString("APPLICATIONID"))) {
                    sb.append("<APPLICATIONID>" + resultSet.getString("APPLICATIONID") + "</APPLICATIONID>");
                } else {
                    sb.append("<APPLICATIONID>--</APPLICATIONID>");
                }
                if (resultSet.getString("STATE") != null && !"".equals(resultSet.getString("STATE"))) {
                    sb.append("<STATE>" + resultSet.getString("STATE") + "</STATE>");
                } else {
                    sb.append("<STATE>--</STATE>");
                }
                if (resultSet.getString("MODIFIED_TS") != null && !"".equals(resultSet.getString("MODIFIED_TS"))) {
                    sb.append("<MODIFIED_TS>" + resultSet.getString("MODIFIED_TS") + "</MODIFIED_TS>");
                } else {
                    sb.append("<MODIFIED_TS>--</MODIFIED_TS>");
                }
                if (resultSet.getString("MODIFIED_BY") != null && !"".equals(resultSet.getString("MODIFIED_BY"))) {
                    sb.append("<MODIFIED_BY>" + resultSet.getString("MODIFIED_BY") + "</MODIFIED_BY>");
                } else {
                    sb.append("<MODIFIED_BY>--</MODIFIED_BY>");
                }
                if (resultSet.getString("CREATED_TS") != null && !"".equals(resultSet.getString("CREATED_TS"))) {
                    sb.append("<CREATED_TS>" + resultSet.getString("CREATED_TS") + "</CREATED_TS>");
                } else {
                    sb.append("<CREATED_TS>--</CREATED_TS>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // System.out.println("this is string >> "+sb.toString());
        return sb.toString();
    }

    public String getRoutingDetails(String routingId) throws ServiceLocatorException {
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();

        // System.out.println("invNumber--->"+invNumber); 
        queryString = "SELECT * FROM ROUTERINFO WHERE ROUTER_ID=" + routingId;

        System.out.println("queryString--> " + queryString);

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                sb.append("<ROUTER_ID>" + resultSet.getString("ROUTER_ID") + "</ROUTER_ID>");
                sb.append("<ROUTER_NAME>" + resultSet.getString("ROUTER_NAME") + "</ROUTER_NAME>");

                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("ACCEPTORLOOKUPALIAS") != null && !"".equals(resultSet.getString("ACCEPTORLOOKUPALIAS"))) {
                    sb.append("<ACCEPTORLOOKUPALIAS>" + resultSet.getString("ACCEPTORLOOKUPALIAS") + "</ACCEPTORLOOKUPALIAS>");
                } else {
                    sb.append("<ACCEPTORLOOKUPALIAS>--</ACCEPTORLOOKUPALIAS>");
                }
                if (resultSet.getString("INTERNALROUTEREMAIL") != null && !"".equals(resultSet.getString("INTERNALROUTEREMAIL"))) {
                    sb.append("<INTERNALROUTEREMAIL>" + resultSet.getString("INTERNALROUTEREMAIL") + "</INTERNALROUTEREMAIL>");
                } else {
                    sb.append("<INTERNALROUTEREMAIL>--</INTERNALROUTEREMAIL>");
                }
                if (resultSet.getString("DESTMAILBOX") != null && !"".equals(resultSet.getString("DESTMAILBOX"))) {
                    sb.append("<DESTMAILBOX>" + resultSet.getString("DESTMAILBOX") + "</DESTMAILBOX>");
                } else {
                    sb.append("<DESTMAILBOX>--</DESTMAILBOX>");
                }
                if (resultSet.getString("SYSTEMTYPE") != null && !"".equals(resultSet.getString("SYSTEMTYPE"))) {
                    sb.append("<SYSTEMTYPE>" + resultSet.getString("SYSTEMTYPE") + "</SYSTEMTYPE>");
                } else {
                    sb.append("<SYSTEMTYPE>--</SYSTEMTYPE>");
                }
                if (resultSet.getString("DIRECTION") != null && !"".equals(resultSet.getString("DIRECTION"))) {
                    sb.append("<DIRECTION>" + resultSet.getString("DIRECTION") + "</DIRECTION>");
                } else {
                    sb.append("<DIRECTION>--</DIRECTION>");
                }
                if (resultSet.getString("ENVELOPE") != null && !"".equals(resultSet.getString("ENVELOPE"))) {
                    sb.append("<ENVELOPE>" + resultSet.getString("ENVELOPE") + "</ENVELOPE>");
                } else {
                    sb.append("<ENVELOPE>--</ENVELOPE>");
                }

                sb.append("<CREATEDDATE>" + resultSet.getString("CREATEDDATE") + "</CREATEDDATE>");

                if (resultSet.getString("MODIFIEDDATE") != null && !"".equals(resultSet.getString("MODIFIEDDATE"))) {
                    sb.append("<MODIFIEDDATE>" + resultSet.getString("MODIFIEDDATE") + "</MODIFIEDDATE>");
                } else {
                    sb.append("<MODIFIEDDATE>--</MODIFIEDDATE>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // System.out.println("this is string >> "+sb.toString());
        return sb.toString();
    }

    public String getB2bChannelDetails(String b2bChannelId) throws ServiceLocatorException {
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();

        // System.out.println("invNumber--->"+invNumber); 
        queryString = "SELECT * FROM B2BCHANNELSLIST WHERE B2BCHANNELS_ID=" + b2bChannelId;

        System.out.println("queryString--> " + queryString);

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                sb.append("<B2BCHANNELS_ID>" + resultSet.getString("B2BCHANNELS_ID") + "</B2BCHANNELS_ID>");
                sb.append("<TP_ID>" + resultSet.getString("TP_ID") + "</TP_ID>");

                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                if (resultSet.getString("DIRECTION") != null && !"".equals(resultSet.getString("DIRECTION"))) {
                    sb.append("<DIRECTION>" + resultSet.getString("DIRECTION") + "</DIRECTION>");
                } else {
                    sb.append("<DIRECTION>--</DIRECTION>");
                }
                if (resultSet.getString("PROTOCOL") != null && !"".equals(resultSet.getString("PROTOCOL"))) {
                    sb.append("<PROTOCOL>" + resultSet.getString("PROTOCOL") + "</PROTOCOL>");
                } else {
                    sb.append("<PROTOCOL>--</PROTOCOL>");
                }
                if (resultSet.getString("HOST") != null && !"".equals(resultSet.getString("HOST"))) {
                    sb.append("<HOST>" + resultSet.getString("HOST") + "</HOST>");
                } else {
                    sb.append("<HOST>--</HOST>");
                }
                if (resultSet.getString("USERNAME") != null && !"".equals(resultSet.getString("USERNAME"))) {
                    sb.append("<USERNAME>" + resultSet.getString("USERNAME") + "</USERNAME>");
                } else {
                    sb.append("<USERNAME>--</USERNAME>");
                }
                if (resultSet.getString("PRODUCERMAILBOX") != null && !"".equals(resultSet.getString("PRODUCERMAILBOX"))) {
                    sb.append("<PRODUCERMAILBOX>" + resultSet.getString("PRODUCERMAILBOX") + "</PRODUCERMAILBOX>");
                } else {
                    sb.append("<PRODUCERMAILBOX>--</PRODUCERMAILBOX>");
                }
                if (resultSet.getString("CONSUMERMAILBOX") != null && !"".equals(resultSet.getString("CONSUMERMAILBOX"))) {
                    sb.append("<CONSUMERMAILBOX>" + resultSet.getString("CONSUMERMAILBOX") + "</CONSUMERMAILBOX>");
                } else {
                    sb.append("<CONSUMERMAILBOX>--</CONSUMERMAILBOX>");
                }
                if (resultSet.getString("POOLINGCODE") != null && !"".equals(resultSet.getString("POOLINGCODE"))) {
                    sb.append("<POOLINGCODE>" + resultSet.getString("POOLINGCODE") + "</POOLINGCODE>");
                } else {
                    sb.append("<POOLINGCODE>--</POOLINGCODE>");
                }

                if (resultSet.getString("APPID") != null && !"".equals(resultSet.getString("APPID"))) {
                    sb.append("<APPID>" + resultSet.getString("APPID") + "</APPID>");
                } else {
                    sb.append("<APPID>--</APPID>");
                }

                if (resultSet.getString("SENDERID") != null && !"".equals(resultSet.getString("SENDERID"))) {
                    sb.append("<SENDERID>" + resultSet.getString("SENDERID") + "</SENDERID>");
                } else {
                    sb.append("<SENDERID>--</SENDERID>");
                }

                if (resultSet.getString("RECEIVERID") != null && !"".equals(resultSet.getString("RECEIVERID"))) {
                    sb.append("<RECEIVERID>" + resultSet.getString("RECEIVERID") + "</RECEIVERID>");
                } else {
                    sb.append("<RECEIVERID>--</RECEIVERID>");
                }

                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // System.out.println("this is string >> "+sb.toString());
        return sb.toString();
    }

    public String getPartnerInfo(String partnerId) throws ServiceLocatorException {
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();

        // System.out.println("invNumber--->"+invNumber); 
        queryString = "select tp.ID as TP_ID,tp.NAME as TP_NAME,tp_details.INTERNALIDENTIFIER,tp_details.APPLICATIONID,tp_details.STATE,tp.STATUS,tp.MODIFIED_TS,tp.MODIFIED_BY,tp.CREATED_TS from tp LEFT OUTER JOIN tp_details on(tp_details.TP_ID=tp.ID) WHERE 1=1 AND tp.ID='" + partnerId + "'";
        String response = "None";
        System.out.println("queryString--> " + queryString);

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                response = resultSet.getString("APPLICATIONID") + "|" + resultSet.getString("INTERNALIDENTIFIER") + "|" + resultSet.getString("TP_ID");
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // System.out.println("this is string >> "+sb.toString());
        return response;
    }

    public String getRouterInfo(String routerName) throws ServiceLocatorException {
        ///boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String resultString = "";

        // System.out.println("invNumber--->"+invNumber); 
        queryString = "select ACCEPTORLOOKUPALIAS,DESTMAILBOX from ROUTERINFO where ROUTER_ID =" + routerName;

        // System.out.println("Logistics Invoice Details-->"+queryString);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();

            while (resultSet.next()) {

                resultString = resultSet.getString("ACCEPTORLOOKUPALIAS") + "|" + resultSet.getString("DESTMAILBOX");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // System.out.println("this is string >> "+sb.toString());
        return resultString;
    }

    public String getBusinessProcessInfo(String businessProcessId) throws ServiceLocatorException {
        ///boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String resultString = "";

        // System.out.println("invNumber--->"+invNumber); 
        //    queryString = "SELECT INVOKEMETHOD,MULTIPLEMSG,ADAPTER,TRANSLATIONMAPNAME,DOCEXTMAPNAME,PRODUCERMAILBOX FROM BUSINESSPROCESSINFO WHERE BP_ID ="+businessProcessId;
        queryString = "SELECT INVKOVEMETHOD,MULTIPLEMSG,ADAPTER FROM PROCESSRELATEDINFO WHERE REL_ID =" + businessProcessId;

        // System.out.println("Logistics Invoice Details-->"+queryString);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(queryString);
            resultSet = statement.executeQuery();

            if (resultSet.next()) {

                resultString = resultSet.getString("INVKOVEMETHOD") + "|" + resultSet.getString("MULTIPLEMSG") + "|" + resultSet.getString("ADAPTER");

            }

        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return resultString;
    }

    public String getDeliveryChannelDetails(int deliveryChannelId) throws ServiceLocatorException {
        boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        StringBuffer deliverChannelSearchQuery = new StringBuffer();
        // System.out.println("invNumber--->"+invNumber); 
        //  queryString ="SELECT * FROM B2BCHANNELSLIST WHERE B2BCHANNELS_ID="+b2bChannelId;

        System.out.println("queryString--> " + queryString);

        try {
            deliverChannelSearchQuery.append("select DELIVERYCHNNELINFO.DELIVERYCHN_ID,DELIVERYCHNNELINFO.PARTNER_ID as PartnerId,TP.NAME as PartnerName,DELIVERYCHNNELINFO.ROUTING_ID as routingId,ROUTERINFO.ROUTER_NAME as routingName,bp.REL_ID as bpId,bp.RELNAME as bpName,trans.REL_ID as transId,trans.RELNAME as transName,dem.REL_ID as demId,dem.RELNAME as demName,pmb.REL_ID as pmbId,pmb.RELNAME as pmbName,DELEVERYCHANNELDESCRPTION.VALUE as encodingId,DELEVERYCHANNELDESCRPTION.DESCRIPTION as encodingName,SEQUENCE,ARCHIVEFLAG,ARCHIVEDIRCTORY,OUTPUTFILENAME,OUTPUTFORMAT,DELIVERYCHNNELINFO.STATUS   from (((((((DELIVERYCHNNELINFO  JOIN TP on (TP.ID=DELIVERYCHNNELINFO.PARTNER_ID) ) JOIN ROUTERINFO on (ROUTERINFO.ROUTER_ID=DELIVERYCHNNELINFO.ROUTING_ID)) ");
            deliverChannelSearchQuery.append(" JOIN PROCESSRELATEDINFO bp on (bp.REL_ID=DELIVERYCHNNELINFO.BUSINESSPROCESSNAME))");
            deliverChannelSearchQuery.append(" JOIN PROCESSRELATEDINFO trans on (trans.REL_ID=DELIVERYCHNNELINFO.TRANSLATIONMAP))");
            deliverChannelSearchQuery.append(" JOIN PROCESSRELATEDINFO dem on (dem.REL_ID=DELIVERYCHNNELINFO.DOCEXTRACTMAP))");
            deliverChannelSearchQuery.append(" JOIN PROCESSRELATEDINFO pmb on (pmb.REL_ID=DELIVERYCHNNELINFO.PRODUCERMAILBOX))");
            deliverChannelSearchQuery.append(" JOIN DELEVERYCHANNELDESCRPTION on (DELEVERYCHANNELDESCRPTION.VALUE=DELIVERYCHNNELINFO.ENCODING)) WHERE DELIVERYCHN_ID=" + deliveryChannelId);
            System.out.println("queryString--> " + deliverChannelSearchQuery.toString());

            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.prepareStatement(deliverChannelSearchQuery.toString());
            resultSet = statement.executeQuery();
            sb.append("<xml version=\"1.0\">");
            sb.append("<DETAILS>");
            while (resultSet.next()) {
                sb.append("<DETAIL><VALID>true</VALID>");
                sb.append("<PARTNER_ID>" + resultSet.getString("PartnerId") + "</PARTNER_ID>");
                sb.append("<PartnerName>" + resultSet.getString("PartnerName") + "</PartnerName>");

                if (resultSet.getString("routingName") != null && !"".equals(resultSet.getString("routingName"))) {
                    sb.append("<ROUTER_NAME>" + resultSet.getString("routingName") + "</ROUTER_NAME>");
                } else {
                    sb.append("<ROUTER_NAME>--</ROUTER_NAME>");
                }
                if (resultSet.getString("bpName") != null && !"".equals(resultSet.getString("bpName"))) {
                    sb.append("<bpName>" + resultSet.getString("bpName") + "</bpName>");
                } else {
                    sb.append("<bpName>--</bpName>");
                }
                if (resultSet.getString("transName") != null && !"".equals(resultSet.getString("transName"))) {
                    sb.append("<transName>" + resultSet.getString("transName") + "</transName>");
                } else {
                    sb.append("<transName>--</transName>");
                }
                if (resultSet.getString("demName") != null && !"".equals(resultSet.getString("demName"))) {
                    sb.append("<demName>" + resultSet.getString("demName") + "</demName>");
                } else {
                    sb.append("<demName>--</demName>");
                }
                if (resultSet.getString("pmbName") != null && !"".equals(resultSet.getString("pmbName"))) {
                    sb.append("<pmbName>" + resultSet.getString("pmbName") + "</pmbName>");
                } else {
                    sb.append("<pmbName>--</pmbName>");
                }
                if (resultSet.getString("encodingName") != null && !"".equals(resultSet.getString("encodingName"))) {
                    sb.append("<encodingName>" + resultSet.getString("encodingName") + "</encodingName>");
                } else {
                    sb.append("<encodingName>--</encodingName>");
                }
                if (resultSet.getString("SEQUENCE") != null && !"".equals(resultSet.getString("SEQUENCE"))) {
                    sb.append("<SEQUENCE>" + resultSet.getString("SEQUENCE") + "</SEQUENCE>");
                } else {
                    sb.append("<SEQUENCE>--</SEQUENCE>");
                }

                if (resultSet.getString("ARCHIVEFLAG") != null && !"".equals(resultSet.getString("ARCHIVEFLAG"))) {
                    sb.append("<ARCHIVEFLAG>" + resultSet.getString("ARCHIVEFLAG") + "</ARCHIVEFLAG>");
                } else {
                    sb.append("<ARCHIVEFLAG>--</ARCHIVEFLAG>");
                }

                if (resultSet.getString("ARCHIVEDIRCTORY") != null && !"".equals(resultSet.getString("ARCHIVEDIRCTORY"))) {
                    sb.append("<ARCHIVEDIRCTORY>" + resultSet.getString("ARCHIVEDIRCTORY") + "</ARCHIVEDIRCTORY>");
                } else {
                    sb.append("<ARCHIVEDIRCTORY>--</ARCHIVEDIRCTORY>");
                }

                if (resultSet.getString("OUTPUTFILENAME") != null && !"".equals(resultSet.getString("OUTPUTFILENAME"))) {
                    sb.append("<OUTPUTFILENAME>" + resultSet.getString("OUTPUTFILENAME") + "</OUTPUTFILENAME>");
                } else {
                    sb.append("<OUTPUTFILENAME>--</OUTPUTFILENAME>");
                }

                if (resultSet.getString("OUTPUTFORMAT") != null && !"".equals(resultSet.getString("OUTPUTFORMAT"))) {
                    sb.append("<OUTPUTFORMAT>" + resultSet.getString("OUTPUTFORMAT") + "</OUTPUTFORMAT>");
                } else {
                    sb.append("<OUTPUTFORMAT>--</OUTPUTFORMAT>");
                }
                if (resultSet.getString("STATUS") != null && !"".equals(resultSet.getString("STATUS"))) {
                    sb.append("<STATUS>" + resultSet.getString("STATUS") + "</STATUS>");
                } else {
                    sb.append("<STATUS>--</STATUS>");
                }
                sb.append("</DETAIL>");
                isGetting = true;
            }
            if (!isGetting) {
                isGetting = false;
                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
            }

            sb.append("</DETAILS>");
            sb.append("</xml>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("this is string >> " + sb.toString());
        return sb.toString();
    }

    public String getDashboardDetails(AjaxHandlerAction ajaxHandlerAction) throws ServiceLocatorException {
        ///boolean isGetting = false;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        String resultString = "";
        String inboundString = "";
        String outboundString = "";
        PreparedStatement preparedStatement = null;
        String tmp_Recieved_From = "";
        String tmp_Recieved_ToTime = "";
        // System.out.println("invNumber--->"+invNumber); 
        //    queryString = "SELECT INVOKEMETHOD,MULTIPLEMSG,ADAPTER,TRANSLATIONMAPNAME,DOCEXTMAPNAME,PRODUCERMAILBOX FROM BUSINESSPROCESSINFO WHERE BP_ID ="+businessProcessId;
        if (ajaxHandlerAction.getFlag().equals("M")) {
            queryString = "select count(DIRECTION) as total from FILES where (SENDER_ID = ? or RECEIVER_ID=?) and DIRECTION=? AND FLOWFLAG='M'";
        }
        if (ajaxHandlerAction.getFlag().equals("L")) {
            queryString = "select count(DIRECTION) as total from FILES where (SENDER_ID = ? or RECEIVER_ID=?) and DIRECTION=? AND FLOWFLAG='L'";
        }
        // System.out.println("Logistics Invoice Details-->"+queryString);
        try {

            if (!"".equals(ajaxHandlerAction.getStartDate()) && ajaxHandlerAction.getStartDate() != null) {
                tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(ajaxHandlerAction.getStartDate());
                queryString = queryString + "AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "' ";
            }
            if (!"".equals(ajaxHandlerAction.getEndDate()) && ajaxHandlerAction.getEndDate() != null) {
                tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(ajaxHandlerAction.getEndDate());
                queryString = queryString + "AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "' ";
            }

            if (!"-1".equals(ajaxHandlerAction.getStatus()) && ajaxHandlerAction.getStatus() != null) {
                queryString = queryString + WildCardSql.getWildCardSql1("FILES.STATUS", ajaxHandlerAction.getStatus().trim());

            }
            if (!"-1".equals(ajaxHandlerAction.getDocType()) && ajaxHandlerAction.getDocType() != null) {
                queryString = queryString + WildCardSql.getWildCardSql1("FILES.TRANSACTION_TYPE", ajaxHandlerAction.getDocType().trim());

            }

            connection = ConnectionProvider.getInstance().getConnection();
            // statement = connection.prepareStatement(queryString);
            preparedStatement = connection.prepareStatement(queryString);
            int inboundTotal = 0;
            int outboundTotal = 0;
            Map partnerMap = null;
            if ("ALL".equalsIgnoreCase(ajaxHandlerAction.getPartnerId())) {
                if (ajaxHandlerAction.getFlag().equals("M")) {
                    partnerMap = DataSourceDataProvider.getInstance().getDashboardPartnerMap("2");
                } else {
                    partnerMap = DataSourceDataProvider.getInstance().getDashboardPartnerMap("3");
                }

                Iterator entries = partnerMap.entrySet().iterator();

                while (entries.hasNext()) {
                    Entry thisEntry = (Entry) entries.next();
                    Object key = thisEntry.getKey();
                    //System.out.println("Key-->"+)
                    Object value = thisEntry.getValue();
                    // ...
                    // inboundString = inboundString+(String)key+"|";
                    preparedStatement.setString(1, (String) key);
                    preparedStatement.setString(2, (String) key);
                    preparedStatement.setString(3, "INBOUND");
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        inboundTotal = resultSet.getInt("total");
                    }

                    resultSet.close();
                    //outboundString = outboundString+(String)key+"|";
                    preparedStatement.setString(1, (String) key);
                    preparedStatement.setString(2, (String) key);
                    preparedStatement.setString(3, "OUTBOUND");
                    resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        outboundTotal = resultSet.getInt("total");
                    }
                    if (inboundTotal != 0) {
                        inboundString = inboundString + (String) key + "_" + (String) value + "|" + inboundTotal + "^";

                    }
                    if (outboundTotal != 0) {
                        outboundString = outboundString + (String) key + "_" + (String) value + "|" + outboundTotal + "^";
                    }
                    inboundTotal = 0;
                    outboundTotal = 0;
                    resultSet.close();

                }

            } else {
                String partnerName = DataSourceDataProvider.getInstance().getPartnerNameById(ajaxHandlerAction.getPartnerId());
                //   inboundString = inboundString+ajaxHandlerAction.getPartnerId()+"|";
                preparedStatement.setString(1, ajaxHandlerAction.getPartnerId());
                preparedStatement.setString(2, ajaxHandlerAction.getPartnerId());
                preparedStatement.setString(3, "INBOUND");
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    inboundTotal = resultSet.getInt("total");
                }
                //  inboundString = inboundString+resultSet.getInt("total")+"^";

                resultSet.close();
                //  outboundString = outboundString+ajaxHandlerAction.getPartnerId()+"|";
                preparedStatement.setString(1, ajaxHandlerAction.getPartnerId());
                preparedStatement.setString(2, ajaxHandlerAction.getPartnerId());
                preparedStatement.setString(3, "OUTBOUND");
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    outboundTotal = resultSet.getInt("total");//(String) key + "_" + partnerName
                }
                if (inboundTotal != 0) {
                    inboundString = inboundString + ajaxHandlerAction.getPartnerId() + "_" + partnerName + "|" + inboundTotal + "^";

                }
                if (outboundTotal != 0) {
                    outboundString = outboundString + ajaxHandlerAction.getPartnerId() + "_" + partnerName + "|" + outboundTotal + "^";
                }
                // outboundString = outboundString+resultSet.getInt("total")+"^";

                resultSet.close();
            }

            //  resultSet = statement.executeQuery();
            resultString = inboundString + "*" + outboundString;
            System.out.println("resultString--->" + resultString);
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
        } finally {
            try {

                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        return resultString;
    }

    public String getReportDeleteDetails(int id) throws ServiceLocatorException {

        boolean isGetting = false;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        StringBuffer sb = new StringBuffer();
        //statement = connection.createStatement();

        //queryString = "select DOCUMENT.ISA_NUMBER,DOCUMENT.DOCUMENT_TYPE,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH from DOCUMENT LEFT OUTER JOIN FILES on (DOCUMENT.FILE_ID= FILES.FILE_ID) where FILES.ISA_NUMBER LIKE '%"+isaNumber+"%'";
        queryString = "UPDATE SCHEDULER SET SCH_STATUS = 'InActive' WHERE SCH_ID =" + id;

        System.out.println("QUERY IS " + queryString);

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();

            int count = statement.executeUpdate(queryString);

//            sb.append("<xml version=\"1.0\">");
//            sb.append("<DETAILS>");
            if (count > 0) {
                sb.append("Report " + id + " Successfully Deleted!");
            } else {
                sb.append("Sorry ! Please Try again.");
            }

//            if (!isGetting) {
//                isGetting = false;
//                sb.append("<DETAIL><VALID>false</VALID></DETAIL>");
//            }
//            sb.append("</DETAILS>");
//            sb.append("</xml>");
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        // System.out.println("this is string >> "+sb.toString());
        return sb.toString();
    }

    public String getReportOverlayDetails(int id, String startDate) throws ServiceLocatorException {

        Connection connection = null;
        Statement statement = null;
        String reportpath = "Nodata";
        //statement = connection.createStatement() UPDATE SCHEDULER SET SCH_STATUS = 'InActive' WHERE SCH_ID =" + id;

        //queryString = "select DOCUMENT.ISA_NUMBER,DOCUMENT.DOCUMENT_TYPE,FILES.SENDER_ID,FILES.RECEIVER_ID,FILES.PRE_TRANS_FILEPATH,FILES.POST_TRANS_FILEPATH from DOCUMENT LEFT OUTER JOIN FILES on (DOCUMENT.FILE_ID= FILES.FILE_ID) where FILES.ISA_NUMBER LIKE '%"+isaNumber+"%'";
        startDate = DateUtility.getInstance().DateViewToDBCompare(startDate);
        startDate = startDate.substring(0, 10);
        queryString = "SELECT SCH_REPORTPATH from SCH_LOOKUPS where SCH_ID=" + id + " and date(SCH_RUNDATE) = DATE('" + startDate + "')";

        System.out.println("QUERY IS " + queryString);

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(queryString);
            if (resultSet.next()) {
                reportpath = resultSet.getString("SCH_REPORTPATH");

            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        } catch (ServiceLocatorException sle) {
            sle.printStackTrace();
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
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
        System.out.println("this is string >> " + reportpath);
        return reportpath;
    }

    @Override
    public String forgotPassword(String userid) throws ServiceLocatorException {
        String str = null;
        String email = null;
        String fname = null;
        String lname = null;
        String name = null;
        String password = null;
        PasswordUtil passwordUtil = new PasswordUtil();
        String sqlQuery = "SELECT PASSWD,EMAIL,FNME,LNME FROM M_USER WHERE LOGINID=?";
        try {
            //System.out.println("Before !!!");
            connection = ConnectionProvider.getInstance().getConnection();
            //System.out.println("Connected !!!-->"+connection);
            preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(1, userid);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                password = passwordUtil.decryptPwd(resultSet.getString(1));
                email = resultSet.getString(2);
                fname = resultSet.getString(3);
                lname = resultSet.getString(4);

            }
            name = fname + " " + lname;
            System.out.println("password is " + password);
            System.out.println("email  is " + email);
            System.out.println("fname is" + fname);
            System.out.println("lname is" + lname);

            MailManager m = new MailManager();
            str = m.sendPwd(email, password, userid, name);

            System.out.println("str is------------------> " + str);
        } catch (Exception ex) {

            System.err.println("ex-->" + ex.getMessage());

            throw new ServiceLocatorException(ex);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException sqle) {

                throw new ServiceLocatorException(sqle);
            }
        }// closing finally block

        return str;
    }

    @Override
    public int updateMyPwd(AjaxHandlerAction ajaxhandleraction, String loginId) throws ServiceLocatorException {
        System.out.println("into update password");
        int updatedRows = 0;
        String password = null;
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        System.out.println("login id is " + loginId);
        PasswordUtil passwordUtility = new PasswordUtil();
        String queryString = "SELECT LOGINID,PASSWD FROM M_USER WHERE LOGINID='" + loginId + "'";

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(queryString);
            while (resultSet.next()) {
                password = resultSet.getString("PASSWD");
            }
            System.out.println("password is " + password);
            System.out.println("getOldPwd() is " + ajaxhandleraction.getOldPwd());

            password = passwordUtility.decryptPwd(password);
            if (ajaxhandleraction.getOldPwd().equals(password)) {
                if (ajaxhandleraction.getNewPwd().equals(ajaxhandleraction.getCnfrmPwd())) {
                    String encryptPass = passwordUtility.encryptPwd(ajaxhandleraction.getNewPwd());
                    System.out.println("encryptPass is " + encryptPass);
                    queryString = "UPDATE M_USER SET PASSWD='" + encryptPass + "' WHERE LOGINID='" + loginId + "'";
                    statement = connection.createStatement();
                    updatedRows = statement.executeUpdate(queryString);
                } else {
                    updatedRows = 100;
                }
            } else {
                updatedRows = 200;
            }
            System.out.println("after updation!! " + updatedRows);

        } catch (SQLException se) {
            throw new ServiceLocatorException(se);
        } finally {
            try {
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
        return updatedRows;
    }
    //method to search whether sender item and receiver item exists in the database for code list or not 

    @Override
    public int searchItems(String senderItem, String recItem, String selectedName) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String queryString = null;
        String query = null;
        String ver = null;
        int count = 0;
        connection = ConnectionProvider.getInstance().getOracleConnection();
        System.out.println("selected name is " + selectedName);
        try {
            if (selectedName != null && !"-1".equals(selectedName)) {

                query = "SELECT DEFAULT_VERSION from CODELIST_XREF_VERS where LIST_NAME='" + selectedName + "'";
                preparedStatement = connection.prepareStatement(query);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    ver = resultSet.getString("DEFAULT_VERSION");
                }

                queryString = "SELECT COUNT(*) AS COUNT FROM CODELIST_XREF_ITEM WHERE SENDER_ITEM='" + senderItem + "' AND RECEIVER_ITEM='" + recItem + "' AND LIST_NAME='" + selectedName + "'AND LIST_VERSION='" + ver + "'";

//            else
//            {
//                queryString = "SELECT COUNT(*) AS COUNT FROM CODELIST_XREF_ITEM WHERE SENDER_ITEM='" + senderItem + "' AND RECEIVER_ITEM='" + recItem + "'";
//            }
                preparedStatement = connection.prepareStatement(queryString);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    count = resultSet.getInt("COUNT");
                }
            }
        } catch (SQLException sql) {
            throw new ServiceLocatorException(sql);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException ex) {
                throw new ServiceLocatorException(ex);
            }
        }
        return count;
    }

    @Override
    public int checkCodeListName(String newCodeListName) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String queryString = null;
        int count = 0;
        connection = ConnectionProvider.getInstance().getOracleConnection();

        try {
            System.out.println("selected CodeList Name is " + newCodeListName);

            queryString = "SELECT COUNT(*) AS COUNT FROM CODELIST_XREF_VERS  WHERE LIST_NAME='" + newCodeListName + "'";

            preparedStatement = connection.prepareStatement(queryString);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt("COUNT");
            }
        } catch (SQLException sql) {
            throw new ServiceLocatorException(sql);
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException ex) {
                throw new ServiceLocatorException(ex);
            }
        }
        return count;
    }

    @Override
    public String addCodeList(String jsonData, String userName, String newCodeListName) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        String queryString = null;
        connection = ConnectionProvider.getInstance().getOracleConnection();
        JSONArray array = null;
        JSONObject jsonObj = null;
        int updatedRows = 0;
        int updatedRows1 = 0;
        int updatedRows2 = 0;
        int count = 0;

        try {
            array = new JSONArray(jsonData);

        } catch (JSONException ex) {
            Logger.getLogger(CertMonitorServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            System.out.println("selected CodeList Name is " + newCodeListName);

            queryString = "SELECT COUNT(*) AS COUNT FROM CODELIST_XREF_VERS  WHERE LIST_NAME='" + newCodeListName + "'";

            preparedStatement = connection.prepareStatement(queryString);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                count = resultSet.getInt("COUNT");
            }
            if (count == 0) {
                queryString = "INSERT INTO SI_USER.CODELIST_XREF_ITEM "
                        + "(LIST_NAME, SENDER_ID, RECEIVER_ID, LIST_VERSION, SENDER_ITEM, RECEIVER_ITEM, TEXT1, TEXT2, TEXT3, TEXT4, DESCRIPTION, TEXT5, TEXT6, TEXT7, TEXT8, TEXT9)"
                        + " VALUES (?, ?, ?,? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
                String queryString1 = "INSERT INTO SI_USER.CODELIST_XREF_VERS"
                        + "	(LIST_NAME, SENDER_ID, RECEIVER_ID, DEFAULT_VERSION, LIST_VERSION)"
                        + "VALUES (?, ?, ?, ?, ?)";
                String queryString2 = "INSERT INTO SI_USER.CODE_LIST_XREF"
                        + "	(LIST_NAME, SENDER_ID, RECEIVER_ID, LIST_VERSION, STATUS, COMMENTS,  USERNAME, CREATE_DATE)"
                        + "VALUES (?, ?, ?, ?,? ,?,?, ?)";
                for (int i = 0; i < array.length(); i++) {
                    jsonObj = array.getJSONObject(i);
                    preparedStatement = connection.prepareStatement(queryString);
                    preparedStatement.setString(1, jsonObj.getString("listName1"));
//                System.out.println("jsonObj.getString(\"senderIdInst\") --> "+ jsonObj.getString("senderIdInst") +" jsonObj.getString(\"recId\") --> "+jsonObj.getString("recId"));
//                if(!"".equalsIgnoreCase(jsonObj.getString("senderIdInst")) || jsonObj.getString("senderIdInst") != null){
//                preparedStatement.setString(2, jsonObj.getString("senderIdInst"));
//                }else{
                    preparedStatement.setString(2, "");
                    //}
//                if(!"".equalsIgnoreCase(jsonObj.getString("recId")) || jsonObj.getString("recId") != null){
//                preparedStatement.setString(3, jsonObj.getString("recId"));
//                }else{
                    preparedStatement.setString(3, "");
                    //}
                    preparedStatement.setInt(4, 1);
                    preparedStatement.setString(5, jsonObj.getString("senderItem"));
                    preparedStatement.setString(6, jsonObj.getString("recItem"));
                    if (!"".equalsIgnoreCase(jsonObj.getString("text1").trim()) && jsonObj.getString("text1") != null) {
                        preparedStatement.setString(7, jsonObj.getString("text1"));
                    } else {
                        preparedStatement.setString(7, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text2").trim()) && jsonObj.getString("text2") != null) {
                        preparedStatement.setString(8, jsonObj.getString("text2"));
                    } else {
                        preparedStatement.setString(8, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text3").trim()) && jsonObj.getString("text3") != null) {
                        preparedStatement.setString(9, jsonObj.getString("text3"));
                    } else {
                        preparedStatement.setString(9, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text4").trim()) && jsonObj.getString("text4") != null) {
                        preparedStatement.setString(10, jsonObj.getString("text4"));
                    } else {
                        preparedStatement.setString(10, "");
                    }
                    preparedStatement.setString(11, jsonObj.getString("desc"));
                    if (!"".equalsIgnoreCase(jsonObj.getString("text5").trim()) && jsonObj.getString("text5") != null) {
                        preparedStatement.setString(12, jsonObj.getString("text5"));
                    } else {
                        preparedStatement.setString(12, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text6").trim()) && jsonObj.getString("text6") != null) {
                        preparedStatement.setString(13, jsonObj.getString("text6"));
                    } else {
                        preparedStatement.setString(13, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text7").trim()) && jsonObj.getString("text7") != null) {
                        preparedStatement.setString(14, jsonObj.getString("text7"));
                    } else {
                        preparedStatement.setString(14, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text8").trim()) && jsonObj.getString("text8") != null) {
                        preparedStatement.setString(15, jsonObj.getString("text8"));
                    } else {
                        preparedStatement.setString(15, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text9").trim()) && jsonObj.getString("text9") != null) {
                        preparedStatement.setString(16, jsonObj.getString("text9"));
                    } else {
                        preparedStatement.setString(16, "");
                    }
                    preparedStatement1 = connection.prepareStatement(queryString1);
                    preparedStatement1.setString(1, jsonObj.getString("listName1"));
                    preparedStatement1.setString(2, "");
                    preparedStatement1.setString(3, "");
                    preparedStatement1.setInt(4, 1);
                    preparedStatement1.setInt(5, 1);
                    preparedStatement2 = connection.prepareStatement(queryString2);
                    preparedStatement2.setString(1, jsonObj.getString("listName1"));
                    preparedStatement2.setString(2, "");
                    preparedStatement2.setString(3, "");
                    preparedStatement2.setInt(4, 1);
                    preparedStatement2.setInt(5, 1);
                    preparedStatement2.setString(6, "null");
                    preparedStatement2.setString(7, userName);
                    //java.sql.Date d=new java.sql.Date(i);
                    //SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    //SimpleDateFormat sd=new SimpleDateFormat("YYYY-MM-dd HH24:mm:SS.0");
                    preparedStatement2.setTimestamp(8, DateUtility.getInstance().getCurrentDB2Timestamp());
                    updatedRows = preparedStatement.executeUpdate();

                    if (i == 0) {
                        updatedRows1 = preparedStatement1.executeUpdate();
                        updatedRows2 = preparedStatement2.executeUpdate();
                    }
                }
                if (updatedRows > 0 && updatedRows1 > 0 && updatedRows2 > 0) {
                    return "Inserted successfully";
                } else {
                    return "Please Try Again";
                }
            } else {
                return "Code list name already exists ..Please try with new one ";
            }
        } catch (SQLException sql) {
            throw new ServiceLocatorException(sql);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {

                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException ex) {
                throw new ServiceLocatorException(ex);
            }
        }
        return null;
    }

    @Override
    public String deleteCodeList(String jsonData) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        String queryString = null;
        String queryString1 = null;
        String queryString2 = null;
        connection = ConnectionProvider.getInstance().getOracleConnection();
        JSONArray array = null;
        JSONObject jsonObj = null;
        int updatedRows = 0;
        int updatedRows1 = 0;
        int updatedRows2 = 0;
        try {
            array = new JSONArray(jsonData);

        } catch (JSONException ex) {
            Logger.getLogger(CertMonitorServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            queryString = "DELETE FROM SI_USER.CODELIST_XREF_ITEM WHERE LIST_NAME=? AND LIST_VERSION=? AND SENDER_ITEM=? AND RECEIVER_ITEM=?";
            queryString1 = "DELETE FROM SI_USER.CODELIST_XREF_VERS WHERE LIST_NAME=? AND LIST_VERSION=?";
            queryString2 = "DELETE FROM SI_USER.CODE_LIST_XREF WHERE LIST_NAME=? AND LIST_VERSION=?";
            System.out.println("json array size is " + array.length());
            for (int i = 0; i < array.length(); i++) {
                System.out.println("i value is " + i);
                jsonObj = array.getJSONObject(i);
                preparedStatement = connection.prepareStatement(queryString);
                preparedStatement.setString(1, jsonObj.getString("listName1"));
                preparedStatement.setInt(2, Integer.parseInt(jsonObj.getString("listVerson")));
                preparedStatement.setString(3, jsonObj.getString("senderItem"));
                preparedStatement.setString(4, jsonObj.getString("recItem"));
//                preparedStatement1 = connection.prepareStatement(queryString1);
//                preparedStatement1.setString(1, jsonObj.getString("listName1"));
//                preparedStatement1.setInt(2, Integer.parseInt(jsonObj.getString("listVerson")));
//                preparedStatement2 = connection.prepareStatement(queryString2);
//                preparedStatement2.setString(1, jsonObj.getString("listName1"));
//                preparedStatement2.setInt(2, Integer.parseInt(jsonObj.getString("listVerson")));
                updatedRows = preparedStatement.executeUpdate();
                System.out.println("updatedRows : " + updatedRows);
                //updatedRows1 = preparedStatement1.executeUpdate();
                //updatedRows2 = preparedStatement2.executeUpdate();
            }
        } catch (SQLException sql) {
            throw new ServiceLocatorException(sql);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {

                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException ex) {
                throw new ServiceLocatorException(ex);
            }
        }
        //System.out.println("updatedrows"+updatedRows);
        if (updatedRows > 0) {
            // System.out.println("updatedrows");
            return "Deleted successfully";
        } else {
            return "Please Try Again";
        }
    }

    public String updateCodeList(String listName, String jsonData, String userName, int listitems) throws ServiceLocatorException {
        System.out.println("listName------for update" + listName);
        System.out.println("listName------for update " + listitems);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PreparedStatement preparedStatement1 = null;
        PreparedStatement preparedStatement2 = null;
        String queryString = null;
        String queryString1 = null;
        String queryString2 = null;
        String updateQueryString = null;
        int updatedRows = 0;
        int updatedRows1 = 0;
        int updatedRows2 = 0;
        JSONArray array = null;
        JSONObject jsonObj = null;
        connection = ConnectionProvider.getInstance().getOracleConnection();

        try {
            System.out.println("json data is " + jsonData);
            array = new JSONArray(jsonData);

        } catch (JSONException ex) {
            Logger.getLogger(CertMonitorServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            System.out.println("array size is " + array.length());
            int count = 0;
            int addVersion = 0;
            for (int i = 0; i < array.length(); i++) {
                System.out.println("i value is " + i);
                int listNameMap = 0;
//                if (i < listitems) {
                jsonObj = array.getJSONObject(i);
                queryString = "SELECT  LIST_VERSION FROM CODELIST_XREF_VERS WHERE LIST_NAME ='" + jsonObj.getString("listName1") + "'";
                preparedStatement = connection.prepareStatement(queryString);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    listNameMap = resultSet.getInt("LIST_VERSION");
                }
                if (listNameMap > 0) {
                    if (i == 0) {
                        addVersion = listNameMap + 1;
                        updateQueryString = "UPDATE CODELIST_XREF_VERS SET LIST_VERSION=?, DEFAULT_VERSION=?, SENDER_ID=?, RECEIVER_ID=?  WHERE LIST_NAME=?";
                        preparedStatement = connection.prepareStatement(updateQueryString);
                        preparedStatement.setInt(1, addVersion);
                        preparedStatement.setInt(2, addVersion);
                        preparedStatement.setString(3, "");
                        preparedStatement.setString(4, "");
                        preparedStatement.setString(5, jsonObj.getString("listName1"));
                        updatedRows = preparedStatement.executeUpdate();
                    }
                    queryString1 = "INSERT INTO SI_USER.CODELIST_XREF_ITEM "
                            + "(LIST_NAME, SENDER_ID, RECEIVER_ID, LIST_VERSION, SENDER_ITEM, RECEIVER_ITEM, TEXT1, TEXT2, TEXT3, TEXT4, DESCRIPTION, TEXT5, TEXT6, TEXT7, TEXT8, TEXT9)"
                            + " VALUES (?, ?, ?,? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
//             queryString1 = "INSERT INTO SI_USER.CODELIST_XREF_VERS"
//                    + "	(LIST_NAME, SENDER_ID, RECEIVER_ID, DEFAULT_VERSION, LIST_VERSION)"
//                    + "VALUES (?, ?, ?, ?, ?)";
                    queryString2 = "INSERT INTO SI_USER.CODE_LIST_XREF"
                            + "(LIST_NAME, SENDER_ID, RECEIVER_ID, LIST_VERSION, STATUS, COMMENTS,  USERNAME, CREATE_DATE)"
                            + "VALUES (?, ?, ?, ?,? ,?,?, ?)";
                    // for (int i = 0; i < array.length(); i++) {
                    jsonObj = array.getJSONObject(i);
                    preparedStatement1 = connection.prepareStatement(queryString1);
                    preparedStatement1.setString(1, jsonObj.getString("listName1"));
                    preparedStatement1.setString(2, "");
                    preparedStatement1.setString(3, "");
                    preparedStatement1.setInt(4, addVersion);
                    preparedStatement1.setString(5, jsonObj.getString("senderItem"));
                    preparedStatement1.setString(6, jsonObj.getString("recItem"));
                    if (!"".equalsIgnoreCase(jsonObj.getString("text1").trim()) && jsonObj.getString("text1") != null) {
                        preparedStatement1.setString(7, jsonObj.getString("text1"));
                    } else {
                        preparedStatement1.setString(7, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text2").trim()) && jsonObj.getString("text2") != null) {
                        preparedStatement1.setString(8, jsonObj.getString("text2"));
                    } else {
                        preparedStatement1.setString(8, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text3").trim()) && jsonObj.getString("text3") != null) {
                        preparedStatement1.setString(9, jsonObj.getString("text3"));
                    } else {
                        preparedStatement1.setString(9, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text4").trim()) && jsonObj.getString("text4") != null) {
                        preparedStatement1.setString(10, jsonObj.getString("text4"));
                    } else {
                        preparedStatement1.setString(10, "");
                    }
                    preparedStatement1.setString(11, jsonObj.getString("desc"));
                    if (!"".equalsIgnoreCase(jsonObj.getString("text5").trim()) && jsonObj.getString("text5") != null) {
                        preparedStatement1.setString(12, jsonObj.getString("text5"));
                    } else {
                        preparedStatement1.setString(12, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text6").trim()) && jsonObj.getString("text6") != null) {
                        preparedStatement1.setString(13, jsonObj.getString("text6"));
                    } else {
                        preparedStatement1.setString(13, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text7").trim()) && jsonObj.getString("text7") != null) {
                        preparedStatement1.setString(14, jsonObj.getString("text7"));
                    } else {
                        preparedStatement1.setString(14, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text8").trim()) && jsonObj.getString("text8") != null) {
                        preparedStatement1.setString(15, jsonObj.getString("text8"));
                    } else {
                        preparedStatement1.setString(15, "");
                    }
                    if (!"".equalsIgnoreCase(jsonObj.getString("text9").trim()) && jsonObj.getString("text9") != null) {
                        preparedStatement1.setString(16, jsonObj.getString("text9"));
                    } else {
                        preparedStatement1.setString(16, "");
                    }
                    updatedRows1 = preparedStatement1.executeUpdate();
                    if (i == 0) {
                        System.out.println("i-----" + i);
                        preparedStatement2 = connection.prepareStatement(queryString2);
                        preparedStatement2.setString(1, jsonObj.getString("listName1"));
                        preparedStatement2.setString(2, "");
                        preparedStatement2.setString(3, "");
                        preparedStatement2.setInt(4, addVersion);
                        preparedStatement2.setInt(5, 1);
                        preparedStatement2.setString(6, "null");
                        preparedStatement2.setString(7, userName);
                        //java.sql.Date d=new java.sql.Date(i);
                        //SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                        //SimpleDateFormat sd=new SimpleDateFormat("YYYY-MM-dd HH24:mm:SS.0");
                        preparedStatement2.setTimestamp(8, DateUtility.getInstance().getCurrentDB2Timestamp());
                        // updatedRows = preparedStatement.executeUpdate();
                        updatedRows2 = preparedStatement2.executeUpdate();
                    }
                    //}
                }
//                } else {
//                    queryString = "INSERT INTO SI_USER.CODELIST_XREF_ITEM "
//                            + "(LIST_NAME, SENDER_ID, RECEIVER_ID, LIST_VERSION, SENDER_ITEM, RECEIVER_ITEM, TEXT1, TEXT2, TEXT3, TEXT4, DESCRIPTION, TEXT5, TEXT6, TEXT7, TEXT8, TEXT9)"
//                            + " VALUES (?, ?, ?,? , ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
////                    queryString1 = "INSERT INTO SI_USER.CODELIST_XREF_VERS"
////                            + "	(LIST_NAME, SENDER_ID, RECEIVER_ID, DEFAULT_VERSION, LIST_VERSION)"
////                            + "VALUES (?, ?, ?, ?, ?)";
////                    queryString2 = "INSERT INTO SI_USER.CODE_LIST_XREF"
////                            + "	(LIST_NAME, SENDER_ID, RECEIVER_ID, LIST_VERSION, STATUS, COMMENTS,  USERNAME, CREATE_DATE)"
////                            + "VALUES (?, ?, ?, ?,? ,?,?, ?)";
//                    //   for (int i = 0; i < array.length(); i++) {
//                    jsonObj = array.getJSONObject(i);
//                    preparedStatement = connection.prepareStatement(queryString);
//                    preparedStatement.setString(1, jsonObj.getString("listName1"));
//                    preparedStatement.setString(2, " ");
//                    preparedStatement.setString(3, " ");
//                    preparedStatement.setInt(4, addVersion);
//                    preparedStatement.setString(5, jsonObj.getString("senderItem"));
//                    preparedStatement.setString(6, jsonObj.getString("recItem"));
//                    if (!"".equalsIgnoreCase(jsonObj.getString("text1")) && jsonObj.getString("text1") != null) {
//                        preparedStatement1.setString(7, jsonObj.getString("text1"));
//                    } else {
//                        preparedStatement1.setString(7, " ");
//                    }
//                    if (!"".equalsIgnoreCase(jsonObj.getString("text2")) && jsonObj.getString("text2") != null) {
//                        preparedStatement1.setString(8, jsonObj.getString("text2"));
//                    } else {
//                        preparedStatement1.setString(8, " ");
//                    }
//                    if (!"".equalsIgnoreCase(jsonObj.getString("text3")) && jsonObj.getString("text3") != null) {
//                        preparedStatement1.setString(9, jsonObj.getString("text3"));
//                    } else {
//                        preparedStatement1.setString(9, " ");
//                    }
//                    if (!"".equalsIgnoreCase(jsonObj.getString("text4")) && jsonObj.getString("text4") != null) {
//                        preparedStatement1.setString(10, jsonObj.getString("text4"));
//                    } else {
//                        preparedStatement1.setString(10, " ");
//                    }
//                    preparedStatement1.setString(11, jsonObj.getString("desc"));
//                    if (!"".equalsIgnoreCase(jsonObj.getString("text5")) && jsonObj.getString("text5") != null) {
//                        preparedStatement1.setString(12, jsonObj.getString("text5"));
//                    } else {
//                        preparedStatement1.setString(12, " ");
//                    }
//                    if (!"".equalsIgnoreCase(jsonObj.getString("text6")) && jsonObj.getString("text6") != null) {
//                        preparedStatement1.setString(13, jsonObj.getString("text6"));
//                    } else {
//                        preparedStatement1.setString(13, " ");
//                    }
//                    if (!"".equalsIgnoreCase(jsonObj.getString("text7")) && jsonObj.getString("text7") != null) {
//                        preparedStatement1.setString(14, jsonObj.getString("text7"));
//                    } else {
//                        preparedStatement1.setString(14, " ");
//                    }
//                    if (!"".equalsIgnoreCase(jsonObj.getString("text8")) && jsonObj.getString("text8") != null) {
//                        preparedStatement1.setString(15, jsonObj.getString("text8"));
//                    } else {
//                        preparedStatement1.setString(15, " ");
//                    }
//                    if (!"".equalsIgnoreCase(jsonObj.getString("text9")) && jsonObj.getString("text9") != null) {
//                        preparedStatement1.setString(16, jsonObj.getString("text9"));
//                    } else {
//                        preparedStatement1.setString(16, " ");
//                    }
////                    preparedStatement1 = connection.prepareStatement(queryString1);
////                    preparedStatement1.setString(1, jsonObj.getString("listName1"));
////                    preparedStatement1.setString(2, jsonObj.getString("senderIdInst"));
////                    preparedStatement1.setString(3, jsonObj.getString("recId"));
////                    preparedStatement1.setInt(4, addVersion);
////                    preparedStatement1.setInt(5, addVersion);
////
////                    preparedStatement2 = connection.prepareStatement(queryString2);
////                    preparedStatement2.setString(1, jsonObj.getString("listName1"));
////                    preparedStatement2.setString(2, jsonObj.getString("senderIdInst"));
////                    preparedStatement2.setString(3, jsonObj.getString("recId"));
////                    preparedStatement2.setInt(4, addVersion);
////                    preparedStatement2.setInt(5, 1);
////                    preparedStatement2.setString(6, "");
////                    preparedStatement2.setString(7, userName);
////                    preparedStatement2.setTimestamp(8, DateUtility.getInstance().getCurrentDB2Timestamp());
////                    if ((count == 0)) {
////                        updatedRows1 = preparedStatement1.executeUpdate();
////                        updatedRows2 = preparedStatement2.executeUpdate();
////                        count++;
////                    }
//                    updatedRows = preparedStatement.executeUpdate();
//                    // }
//                }
            }
        } catch (SQLException sql) {
            throw new ServiceLocatorException(sql);
        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException ex) {
                throw new ServiceLocatorException(ex);
            }
        }
        if (updatedRows > 0 && updatedRows1 > 0 && updatedRows2 > 0) {
            return "Updated successfully";
        } else {
            return "Please Try Again";
        }

    }

    /**
     *
     * This method is used to get the Consultant Resumes
     *
     * @param consultantId
     * @return String
     * @throws com.mss.mirage.util.ServiceLocatorException
     */
    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public void setHttpServletRequest(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }
}
