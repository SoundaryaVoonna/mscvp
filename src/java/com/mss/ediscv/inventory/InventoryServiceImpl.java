package com.mss.ediscv.inventory;

import com.mss.ediscv.util.ConnectionProvider;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import com.mss.ediscv.util.WildCardSql;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * @author miracle
 */
public class InventoryServiceImpl implements InventoryService {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement statement = null;
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
    private ArrayList<InventoryBean> inventoryList;
    private InventoryBean inventoryAction;
    private static Logger logger = Logger.getLogger(InventoryServiceImpl.class.getName());

    //@Override
    public ArrayList<InventoryBean> buildInventorySearchQuery(InventoryAction inventoryAction) throws ServiceLocatorException {
        StringBuffer inventorySearchQuery = new StringBuffer();
        String datepickerTo = inventoryAction.getDatepicker();
        String datePickerFrom = inventoryAction.getDatepickerfrom();
        String senderId = "";
        if (inventoryAction.getSenderId()!=null && !inventoryAction.getSenderId().equals("-1")) {
            senderId = inventoryAction.getSenderId();
        }
        String senderName = "";
        if (inventoryAction.getSenderName()!=null && !inventoryAction.getSenderName().equals("-1")) {
            senderName = inventoryAction.getSenderName();
        }
        String recName = "";
        if (inventoryAction.getRecName()!=null && !inventoryAction.getRecName().equals("-1")) {
            recName = inventoryAction.getRecName();
        }
        String recId = "";
        if (inventoryAction.getBuId()!=null && !inventoryAction.getBuId().equals("-1")) {
            recId = inventoryAction.getBuId();
        }
        String bolNum = inventoryAction.getBolNum();
        String poNum = inventoryAction.getPoNum();
        String status = inventoryAction.getStatus();
        String ackStatus = inventoryAction.getAckStatus();
        String corrattribute = inventoryAction.getCorrattribute();
        String corrvalue = inventoryAction.getCorrvalue();
        String corrattribute1 = inventoryAction.getCorrattribute1();
        String corrvalue1 = inventoryAction.getCorrvalue1();
        String doctype = "";
        if (inventoryAction.getDocType()!=null && !inventoryAction.getDocType().equals("-1")) {
            doctype = inventoryAction.getDocType();
        }
        inventorySearchQuery.append("SELECT DISTINCT(ASN.FILE_ID) as FILE_ID,"
                + "ASN.ASN_NUMBER as ASN_NUMBER,ASN.PO_NUMBER as PO_NUMBER,"
                + "ASN.BOL_NUMBER as BOL_NUMBER,ASN.ISA_NUMBER as ISA_NUMBER,ASN.SHIP_DATE as SHIP_DATE, "
                + "TP2.NAME as RECEIVER_NAME,TP1.NAME as SENDER_NAME,FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                + " FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER, FILES.DIRECTION as DIRECTION,"
                + " FILES.STATUS as STATUS, FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED ,"
                + "FILES.ACK_STATUS as ACK_STATUS,FILES.REPROCESSSTATUS"
                + " FROM ASN LEFT OUTER JOIN FILES "
                + "ON (ASN.ASN_NUMBER = FILES.PRI_KEY_VAL AND ASN.FILE_ID = FILES.FILE_ID) "
                + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=FILES.SENDER_ID) "
                + "LEFT OUTER JOIN TP TP2 ON (TP2.ID=FILES.RECEIVER_ID)");
        inventorySearchQuery.append(" WHERE 1=1 AND FLOWFLAG like 'M' ");
        // Db2 Date formate
        if (datePickerFrom != null && !"".equals(datePickerFrom)) {
          //  StringTokenizer st = new StringTokenizer(datePickerFrom, " ");
         //   String datePickerFrom1 = st.nextToken();
            tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(datePickerFrom);
            inventorySearchQuery.append(" AND FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
        }
        if (datepickerTo != null && !"".equals(datepickerTo)) {
            //StringTokenizer st1 = new StringTokenizer(datepickerTo, " ");
            //String datepickerTo1 = st1.nextToken();
            tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(datepickerTo);
            inventorySearchQuery.append(" AND FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "'");
        }
        //newly added for corrletionstart
        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Shipment Number"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("FILES.PRI_KEY_VAL", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Shipment Number"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("FILES.PRI_KEY_VAL", corrvalue1.trim().toUpperCase()));
            }
        }
        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("BOL Number"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ASN.BOL_NUMBER", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("BOL Number"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ASN.BOL_NUMBER", corrvalue1.trim().toUpperCase()));
            }
        }
        //PO NUMBER
        if ((corrattribute != null)  && (corrattribute.equalsIgnoreCase("PO Number"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ASN.PO_NUMBER", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("PO Number"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ASN.PO_NUMBER", corrvalue1.trim().toUpperCase()));
            }
        }
          if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("FILES.FILE_ID", corrvalue1.trim().toUpperCase()));
            }
        }
         //Direction
         if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
            }
        }
        
        //Doc Type
        if (doctype != null && !"".equals(doctype.trim())) {
            inventorySearchQuery.append(WildCardSql.getWildCardSql1("FILES.TRANSACTION_TYPE", doctype.trim()));
        }
        //Status
        if (status != null && !"-1".equals(status.trim())) {
            inventorySearchQuery.append(WildCardSql.getWildCardSql1("FILES.STATUS", status.trim()));
        }
        //ACK_STATUS
        if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
            inventorySearchQuery.append(WildCardSql.getWildCardSql1("FILES.ACK_STATUS", ackStatus.trim()));
        }
        if (senderId != null && !"".equals(senderId.trim())) {
            inventorySearchQuery.append(WildCardSql.getWildCardSql1("TP1.ID", senderId.trim().toUpperCase()));
        }
        if (recId != null && !"".equals(recId.trim())) {
            inventorySearchQuery.append(WildCardSql.getWildCardSql1("TP2.ID", recId.trim().toUpperCase()));
        }
        if (senderName != null && !"".equals(senderName.trim())) {
            inventorySearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", senderName.trim().toUpperCase()));
        }
        if (recName != null && !"".equals(recName.trim())) {
            inventorySearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", recName.trim().toUpperCase()));
        }
        inventorySearchQuery.append("order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
        String searchQuery = inventorySearchQuery.toString();
         System.out.println("mscvp shipment query-->"+searchQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            inventoryList = new ArrayList<InventoryBean>();
            while (resultSet.next()) {
                InventoryBean inventoryBean = new InventoryBean();
                inventoryBean.setAsnNo(resultSet.getString("ASN_NUMBER"));
                inventoryBean.setPoNo(resultSet.getString("PO_NUMBER"));
                inventoryBean.setBolNo(resultSet.getString("BOL_NUMBER"));
                inventoryBean.setIsa(resultSet.getString("ISA_NUMBER"));
                inventoryBean.setShipmentDate(resultSet.getString("SHIP_DATE"));
                inventoryBean.setGsCtrl(resultSet.getString("GS_CONTROL_NUMBER"));
                inventoryBean.setStCtrl(resultSet.getString("ST_CONTROL_NUMBER"));
                String direction = resultSet.getString("DIRECTION");
                inventoryBean.setDirection(direction);
                if ("INBOUND".equalsIgnoreCase(direction)) {
                    inventoryBean.setPname(resultSet.getString("SENDER_NAME"));
                } else {
                    inventoryBean.setPname(resultSet.getString("RECEIVER_NAME"));
                }
                inventoryBean.setStatus(resultSet.getString("STATUS"));
                inventoryBean.setDate_time_rec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                inventoryBean.setFile_id(resultSet.getString("FILE_ID"));
                inventoryBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                inventoryBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                inventoryList.add(inventoryBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.out.println("hi" + ex.getMessage());
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
        return inventoryList;
    }
    
     public ArrayList<InventoryBean> buildInventorySearchQueryArchive(InventoryAction inventoryAction) throws ServiceLocatorException {
        StringBuffer inventorySearchQuery = new StringBuffer();
        String datepickerTo = inventoryAction.getDatepicker();
        String datePickerFrom = inventoryAction.getDatepickerfrom();
        String senderId = "";
        if (inventoryAction.getSenderId()!=null && !inventoryAction.getSenderId().equals("-1")) {
            senderId = inventoryAction.getSenderId();
        }
        String senderName = "";
        if (inventoryAction.getSenderName()!=null && !inventoryAction.getSenderName().equals("-1")) {
            senderName = inventoryAction.getSenderName();
        }
        String recName = "";
        if (inventoryAction.getRecName()!=null && !inventoryAction.getRecName().equals("-1")) {
            recName = inventoryAction.getRecName();
        }
        String recId = "";
        if (inventoryAction.getBuId()!=null && !inventoryAction.getBuId().equals("-1")) {
            recId = inventoryAction.getBuId();
        }
        String bolNum = inventoryAction.getBolNum();
        String poNum = inventoryAction.getPoNum();
        String status = inventoryAction.getStatus();
        String ackStatus = inventoryAction.getAckStatus();
        String corrattribute = inventoryAction.getCorrattribute();
        String corrvalue = inventoryAction.getCorrvalue();
        String corrattribute1 = inventoryAction.getCorrattribute1();
        String corrvalue1 = inventoryAction.getCorrvalue1();
        String doctype = "";
        if (inventoryAction.getDocType()!=null && !inventoryAction.getDocType().equals("-1")) {
            doctype = inventoryAction.getDocType();
        }
        inventorySearchQuery.append("SELECT DISTINCT(ARCHIVE_ASN.FILE_ID) as FILE_ID,"
                + "ARCHIVE_ASN.ASN_NUMBER as ASN_NUMBER,ARCHIVE_ASN.PO_NUMBER as PO_NUMBER,"
                + "ARCHIVE_ASN.BOL_NUMBER as BOL_NUMBER,ARCHIVE_ASN.ISA_NUMBER as ISA_NUMBER,ARCHIVE_ASN.SHIP_DATE as SHIP_DATE, "
                + "TP2.NAME as RECEIVER_NAME,TP1.NAME as SENDER_NAME,ARCHIVE_FILES.GS_CONTROL_NUMBER as GS_CONTROL_NUMBER,"
                + " ARCHIVE_FILES.ST_CONTROL_NUMBER as ST_CONTROL_NUMBER, ARCHIVE_FILES.DIRECTION as DIRECTION,"
                + " ARCHIVE_FILES.STATUS as STATUS, ARCHIVE_FILES.DATE_TIME_RECEIVED as DATE_TIME_RECEIVED ,"
                + "ARCHIVE_FILES.ACK_STATUS as ACK_STATUS,ARCHIVE_FILES.REPROCESSSTATUS"
                + " FROM ARCHIVE_ASN LEFT OUTER JOIN ARCHIVE_FILES "
                + "ON (ARCHIVE_ASN.ASN_NUMBER = ARCHIVE_FILES.PRI_KEY_VAL AND ARCHIVE_ASN.FILE_ID = ARCHIVE_FILES.FILE_ID) "
                + "LEFT OUTER JOIN TP TP1 ON (TP1.ID=ARCHIVE_FILES.SENDER_ID) "
                + "LEFT OUTER JOIN TP TP2 ON (TP2.ID=ARCHIVE_FILES.RECEIVER_ID)");
        inventorySearchQuery.append(" WHERE 1=1 AND FLOWFLAG like 'M' ");
        // Db2 Date formate
        if (datePickerFrom != null && !"".equals(datePickerFrom)) {
          //  StringTokenizer st = new StringTokenizer(datePickerFrom, " ");
         //   String datePickerFrom1 = st.nextToken();
            tmp_Recieved_From = DateUtility.getInstance().DateViewToDBCompare(datePickerFrom);
            inventorySearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED >= '" + tmp_Recieved_From + "'");
        }
        if (datepickerTo != null && !"".equals(datepickerTo)) {
            //StringTokenizer st1 = new StringTokenizer(datepickerTo, " ");
            //String datepickerTo1 = st1.nextToken();
            tmp_Recieved_ToTime = DateUtility.getInstance().DateViewToDBCompare(datepickerTo);
            inventorySearchQuery.append(" AND ARCHIVE_FILES.DATE_TIME_RECEIVED <= '" + tmp_Recieved_ToTime + "'");
        }
        //newly added for corrletionstart
        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Shipment Number"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Shipment Number"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.PRI_KEY_VAL", corrvalue1.trim().toUpperCase()));
            }
        }
        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("BOL Number"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_ASN.BOL_NUMBER", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("BOL Number"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_ASN.BOL_NUMBER", corrvalue1.trim().toUpperCase()));
            }
        }
        //PO NUMBER
        if ((corrattribute != null)  && (corrattribute.equalsIgnoreCase("PO Number"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_ASN.PO_NUMBER", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("PO Number"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_ASN.PO_NUMBER", corrvalue1.trim().toUpperCase()));
            }
        }
          if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Instance Id"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Instance Id"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.FILE_ID", corrvalue1.trim().toUpperCase()));
            }
        }
         //Direction
         if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
            }
        }
        
        //Doc Type
        if (doctype != null && !"".equals(doctype.trim())) {
            inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.TRANSACTION_TYPE", doctype.trim()));
        }
        //Status
        if (status != null && !"-1".equals(status.trim())) {
            inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.STATUS", status.trim()));
        }
        //ACK_STATUS
        if (ackStatus != null && !"-1".equals(ackStatus.trim())) {
            inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.ACK_STATUS", ackStatus.trim()));
        }
        if (senderId != null && !"".equals(senderId.trim())) {
            inventorySearchQuery.append(WildCardSql.getWildCardSql1("TP1.ID", senderId.trim().toUpperCase()));
        }
        if (recId != null && !"".equals(recId.trim())) {
            inventorySearchQuery.append(WildCardSql.getWildCardSql1("TP2.ID", recId.trim().toUpperCase()));
        }
        if (senderName != null && !"".equals(senderName.trim())) {
            inventorySearchQuery.append(WildCardSql.getWildCardSql1("TP1.NAME", senderName.trim().toUpperCase()));
        }
        if (recName != null && !"".equals(recName.trim())) {
            inventorySearchQuery.append(WildCardSql.getWildCardSql1("TP2.NAME", recName.trim().toUpperCase()));
        }
        inventorySearchQuery.append("order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
        String searchQuery = inventorySearchQuery.toString();
        System.out.println("archive shipment query-->"+searchQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            inventoryList = new ArrayList<InventoryBean>();
            while (resultSet.next()) {
                InventoryBean inventoryBean = new InventoryBean();
                inventoryBean.setAsnNo(resultSet.getString("ASN_NUMBER"));
                inventoryBean.setPoNo(resultSet.getString("PO_NUMBER"));
                inventoryBean.setBolNo(resultSet.getString("BOL_NUMBER"));
                inventoryBean.setIsa(resultSet.getString("ISA_NUMBER"));
                inventoryBean.setShipmentDate(resultSet.getString("SHIP_DATE"));
                inventoryBean.setGsCtrl(resultSet.getString("GS_CONTROL_NUMBER"));
                inventoryBean.setStCtrl(resultSet.getString("ST_CONTROL_NUMBER"));
                String direction = resultSet.getString("DIRECTION");
                inventoryBean.setDirection(direction);
                if ("INBOUND".equalsIgnoreCase(direction)) {
                    inventoryBean.setPname(resultSet.getString("SENDER_NAME"));
                } else {
                    inventoryBean.setPname(resultSet.getString("RECEIVER_NAME"));
                }
                inventoryBean.setStatus(resultSet.getString("STATUS"));
                inventoryBean.setDate_time_rec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                inventoryBean.setFile_id(resultSet.getString("FILE_ID"));
                inventoryBean.setAckStatus(resultSet.getString("ACK_STATUS"));
                inventoryBean.setReProcessStatus(resultSet.getString("REPROCESSSTATUS"));
                inventoryList.add(inventoryBean);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception ex) {
            System.out.println("hi" + ex.getMessage());
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
        return inventoryList;
    }

    /**
     * @return the inventoryList
     */
    public ArrayList<InventoryBean> getShipmentList() {
        return inventoryList;
    }

    /**
     * @param inventoryList the inventoryList to set
     */
    public void setShipmentList(ArrayList<InventoryBean> inventoryList) {
        this.inventoryList = inventoryList;
    }
}