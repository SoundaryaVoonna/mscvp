/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.inv;

import com.mss.ediscv.util.ConnectionProvider;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import com.mss.ediscv.util.WildCardSql;
import java.sql.*;
import java.util.ArrayList;

/**
 * @author miracle
 */
public class InvoiceServiceImpl implements InvoiceService {

    Connection connection = null;
    Statement statement = null;
    PreparedStatement preparedStatement = null;
    ResultSet resultSet = null;
    CallableStatement callableStatement = null;
    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    String strFormat = "yyyy-MM-dd";
    DateFormat myDateFormat = new SimpleDateFormat(strFormat);
    Calendar cal = new GregorianCalendar();
    java.util.Date now = cal.getTime();
    long time = now.getTime();
    java.sql.Date date = new java.sql.Date(time);
    int callableStatementUpdateCount;
    private ArrayList<InvoiceBean> invoiceList;
    private InvoiceBean invoiceBean;
    private static Logger logger = Logger.getLogger(com.mss.ediscv.shipment.ShipmentServiceImpl.class.getName());

    //@Override
    public ArrayList<InvoiceBean> buildinvoiceQuery(InvoiceAction invoiceAction) throws ServiceLocatorException {
        StringBuffer invoiceSearchQuery = new StringBuffer();
        String invdatepickerTO = invoiceAction.getInvdatepicker();
        String invdatepickerfrom = invoiceAction.getInvdatepickerfrom();
        String invSenderId = "";
        if (invoiceAction.getInvSenderId()!=null &&!invoiceAction.getInvSenderId().equals("-1")) {
            invSenderId = invoiceAction.getInvSenderId();
        }
        String invSenderName = "";
        if (invoiceAction.getInvSenderName()!=null && !invoiceAction.getInvSenderName().equals("-1")) {
            invSenderName = invoiceAction.getInvSenderName();
        }
        String invBusId = "";
        if (invoiceAction.getInvBusId()!=null && !invoiceAction.getInvBusId().equals("-1")) {
            invBusId = invoiceAction.getInvBusId();
        }
        String invRecName = "";
        if (invoiceAction.getInvRecName()!=null && !invoiceAction.getInvRecName().equals("-1")) {
            invRecName = invoiceAction.getInvRecName();
        }
        String invPoNum = invoiceAction.getInvPoNum();
        String invNum = invoiceAction.getInvNum();
        String status = invoiceAction.getStatus();
        String ackStatus = invoiceAction.getAckStatus();
        String corrattribute = invoiceAction.getCorrattribute();
        String corrvalue = invoiceAction.getCorrvalue();
        String doctype = "";
        if (invoiceAction.getDocType()!=null && !invoiceAction.getDocType().equals("-1")) {
            doctype = invoiceAction.getDocType();
        }
        invoiceSearchQuery.append("SELECT DISTINCT(FILES.FILE_ID) as FILEID,INVOICE.INVOICE_NUMBER as INVOICE_NUMBER,FILES.DIRECTION as DIRECTION,"
                + "INVOICE.PO_NUMBER,INVOICE.ITEM_QTY,INVOICE.INVOICE_AMOUNT,FILES.ACK_STATUS as ACK_STATUS,FILES.STATUS as STATUS,FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,FILES.REPROCESSSTATUS,"
                + "INVOICE_DATE,TP2.NAME as RECEIVER_NAME,TP1.NAME as SENDER_NAME FROM INVOICE LEFT OUTER JOIN FILES ON "
                + "(INVOICE.INVOICE_NUMBER=FILES.PRI_KEY_VAL  AND INVOICE.FILE_ID = FILES.FILE_ID) LEFT OUTER JOIN TP TP1 ON "
                + "(TP1.ID=FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID=FILES.RECEIVER_ID)");
        invoiceSearchQuery.append(" WHERE 1=1 AND FLOWFLAG like 'M' ");
        /* 
         *  Db2 Date formate
         **/
        if (invdatepickerfrom != null && !"".equals(invdatepickerfrom)) {
            tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(invdatepickerfrom);
            invoiceSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
        }
        if (invdatepickerTO != null && !"".equals(invdatepickerTO)) {
            tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(invdatepickerTO);
            invoiceSearchQuery.append(" AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "'");
        }

        if (corrattribute!=null && corrattribute.equalsIgnoreCase("Invoice Number")) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                invoiceSearchQuery.append(WildCardSql.getWildCardSql1("INVOICE.INVOICE_NUMBER", corrvalue.trim().toUpperCase()));
            }
        }
         //Direction
         if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                invoiceSearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue.trim().toUpperCase()));
            }
        }
        //Instance Id 
        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                invoiceSearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue.trim().toUpperCase()));
            }
        }
        
        //DocType
        if (doctype != null && !"".equals(doctype.trim())) {
            invoiceSearchQuery.append(WildCardSql.getWildCardSql1("FILES.TRANSACTION_TYPE", doctype.trim()));
        }
        //Status
        if (status != null && !"-1".equals(status.trim())) {
            invoiceSearchQuery.append(WildCardSql.getWildCardSql1("FILES.STATUS", status.trim()));
        }
        //ACK_STATUS
        if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
            invoiceSearchQuery.append(WildCardSql.getWildCardSql1("FILES.ACK_STATUS", ackStatus.trim()));
        }

        if (invSenderId != null && !"".equals(invSenderId.trim())) {
            invoiceSearchQuery.append(WildCardSql.getWildCardSql1("TP1.ID", invSenderId.trim().toUpperCase()));
        }
        if (invBusId != null && !"".equals(invBusId.trim())) {
            invoiceSearchQuery.append(WildCardSql.getWildCardSql1("TP2.ID", invBusId.trim().toUpperCase()));
        }
        if (invSenderName != null && !"".equals(invSenderName.trim())) {
            invoiceSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", invSenderName.trim().toUpperCase()));
        }
        if (invRecName != null && !"".equals(invRecName.trim())) {
            invoiceSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", invRecName.trim().toUpperCase()));
        }
        invoiceSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
        String searchQuery = invoiceSearchQuery.toString();
        System.out.println("mscvp invoice query-->"+searchQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            invoiceList = new ArrayList<InvoiceBean>();
            while (resultSet.next()) {
                InvoiceBean invoiceBean = new InvoiceBean();
                invoiceBean.setPoNumber(resultSet.getString("PO_NUMBER"));
                invoiceBean.setFileId(resultSet.getString("FILEID"));
                String direction = resultSet.getString("DIRECTION");
                invoiceBean.setDirection(direction);
                invoiceBean.setItemQty(resultSet.getString("ITEM_QTY"));
                invoiceBean.setInvNumber(resultSet.getString("INVOICE_NUMBER"));
                invoiceBean.setInvAmount(resultSet.getString("INVOICE_AMOUNT"));
                invoiceBean.setInvDate(resultSet.getDate("INVOICE_DATE"));
                if ("INBOUND".equalsIgnoreCase(direction)) {
                    invoiceBean.setPname(resultSet.getString("SENDER_NAME"));
                } else {
                    invoiceBean.setPname(resultSet.getString("RECEIVER_NAME"));
                }
                invoiceBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                invoiceBean.setStatus(resultSet.getString("STATUS"));
                invoiceBean.setDate_time_rec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                invoiceBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                invoiceList.add(invoiceBean);
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
        return invoiceList;
    }
    
    public ArrayList<InvoiceBean> buildinvoiceQueryArchive(InvoiceAction invoiceAction) throws ServiceLocatorException {
        StringBuffer invoiceSearchQuery = new StringBuffer();
        String invdatepickerTO = invoiceAction.getInvdatepicker();
        String invdatepickerfrom = invoiceAction.getInvdatepickerfrom();
        String invSenderId = "";
        if (invoiceAction.getInvSenderId()!=null &&!invoiceAction.getInvSenderId().equals("-1")) {
            invSenderId = invoiceAction.getInvSenderId();
        }
        String invSenderName = "";
        if (invoiceAction.getInvSenderName()!=null && !invoiceAction.getInvSenderName().equals("-1")) {
            invSenderName = invoiceAction.getInvSenderName();
        }
        String invBusId = "";
        if (invoiceAction.getInvBusId()!=null && !invoiceAction.getInvBusId().equals("-1")) {
            invBusId = invoiceAction.getInvBusId();
        }
        String invRecName = "";
        if (invoiceAction.getInvRecName()!=null && !invoiceAction.getInvRecName().equals("-1")) {
            invRecName = invoiceAction.getInvRecName();
        }
        String invPoNum = invoiceAction.getInvPoNum();
        String invNum = invoiceAction.getInvNum();
        String status = invoiceAction.getStatus();
        String ackStatus = invoiceAction.getAckStatus();
        String corrattribute = invoiceAction.getCorrattribute();
        String corrvalue = invoiceAction.getCorrvalue();
        String doctype = "";
        if (invoiceAction.getDocType()!=null && !invoiceAction.getDocType().equals("-1")) {
            doctype = invoiceAction.getDocType();
        }
        invoiceSearchQuery.append("SELECT DISTINCT(ARCHIVE_INVOICE.INVOICE_NUMBER) as INVOICE_NUMBER,ARCHIVE_FILES.FILE_ID as FILEID,ARCHIVE_FILES.DIRECTION as DIRECTION,"
                + "ARCHIVE_INVOICE.PO_NUMBER,ARCHIVE_INVOICE.ITEM_QTY,ARCHIVE_INVOICE.INVOICE_AMOUNT,ARCHIVE_FILES.ACK_STATUS as ACK_STATUS,ARCHIVE_FILES.STATUS as STATUS,ARCHIVE_FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED,ARCHIVE_FILES.REPROCESSSTATUS,"
                + "INVOICE_DATE,TP2.NAME as RECEIVER_NAME,TP1.NAME as SENDER_NAME FROM ARCHIVE_INVOICE LEFT OUTER JOIN ARCHIVE_FILES ON "
                + "(ARCHIVE_INVOICE.INVOICE_NUMBER=ARCHIVE_FILES.PRI_KEY_VAL  AND ARCHIVE_INVOICE.FILE_ID = ARCHIVE_FILES.FILE_ID) LEFT OUTER JOIN TP TP1 ON "
                + "(TP1.ID=ARCHIVE_FILES.SENDER_ID) LEFT OUTER JOIN TP TP2 ON (TP2.ID=ARCHIVE_FILES.RECEIVER_ID)");
        invoiceSearchQuery.append(" WHERE 1=1 AND FLOWFLAG like 'M' ");
        /* 
         *  Db2 Date formate
         **/
        if (invdatepickerfrom != null && !"".equals(invdatepickerfrom)) {
            tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(invdatepickerfrom);
            invoiceSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
        }
        if (invdatepickerTO != null && !"".equals(invdatepickerTO)) {
            tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(invdatepickerTO);
            invoiceSearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "'");
        }

        if (corrattribute!=null && corrattribute.equalsIgnoreCase("Invoice Number")) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                invoiceSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue.trim().toUpperCase()));
            }
        }
         //Direction
         if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                invoiceSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue.trim().toUpperCase()));
            }
        }
        //Instance Id 
        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                invoiceSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue.trim().toUpperCase()));
            }
        }
        
        //DocType
        if (doctype != null && !"".equals(doctype.trim())) {
            invoiceSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.TRANSACTION_TYPE", doctype.trim()));
        }
        //Status
        if (status != null && !"-1".equals(status.trim())) {
            invoiceSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.STATUS", status.trim()));
        }
        //ACK_STATUS
        if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
            invoiceSearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ACK_STATUS", ackStatus.trim()));
        }

        if (invSenderId != null && !"".equals(invSenderId.trim())) {
            invoiceSearchQuery.append(WildCardSql.getWildCardSql1("TP1.ID", invSenderId.trim().toUpperCase()));
        }
        if (invBusId != null && !"".equals(invBusId.trim())) {
            invoiceSearchQuery.append(WildCardSql.getWildCardSql1("TP2.ID", invBusId.trim().toUpperCase()));
        }
        if (invSenderName != null && !"".equals(invSenderName.trim())) {
            invoiceSearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", invSenderName.trim().toUpperCase()));
        }
        if (invRecName != null && !"".equals(invRecName.trim())) {
            invoiceSearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", invRecName.trim().toUpperCase()));
        }
        invoiceSearchQuery.append(" order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
        String searchQuery = invoiceSearchQuery.toString();
         System.out.println("archive invoice query-->"+searchQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            invoiceList = new ArrayList<InvoiceBean>();
            while (resultSet.next()) {
                InvoiceBean invoiceBean = new InvoiceBean();
                invoiceBean.setPoNumber(resultSet.getString("PO_NUMBER"));
                invoiceBean.setFileId(resultSet.getString("FILEID"));
                String direction = resultSet.getString("DIRECTION");
                invoiceBean.setDirection(direction);
                invoiceBean.setItemQty(resultSet.getString("ITEM_QTY"));
                invoiceBean.setInvNumber(resultSet.getString("INVOICE_NUMBER"));
                invoiceBean.setInvAmount(resultSet.getString("INVOICE_AMOUNT"));
                invoiceBean.setInvDate(resultSet.getDate("INVOICE_DATE"));
                if ("INBOUND".equalsIgnoreCase(direction)) {
                    invoiceBean.setPname(resultSet.getString("SENDER_NAME"));
                } else {
                    invoiceBean.setPname(resultSet.getString("RECEIVER_NAME"));
                }
                invoiceBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                invoiceBean.setStatus(resultSet.getString("STATUS"));
                invoiceBean.setDate_time_rec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                invoiceBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                invoiceList.add(invoiceBean);
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
        return invoiceList;
    }

    /**
     * @return the invoiceBean
     */
    public InvoiceBean getInvoiceBean() {
        return invoiceBean;
    }

    /**
     * @param invoiceBean the invoiceBean to set
     */
    public void setInvoiceBean(InvoiceBean invoiceBean) {
        this.invoiceBean = invoiceBean;
    }
}
