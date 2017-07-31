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
    private InventoryBean inventoryBean;
    private static Logger logger = Logger.getLogger(InventoryServiceImpl.class.getName());

    //@Override
    public ArrayList<InventoryBean> buildInventorySearchQuery(InventoryAction inventoryAction) throws ServiceLocatorException {
        StringBuffer inventorySearchQuery = new StringBuffer();
        String datepickerTo = inventoryAction.getDocdatepicker();
        String datePickerFrom = inventoryAction.getDocdatepickerfrom();
        String senderId = "";
        if (inventoryAction.getSenderId() != null && !inventoryAction.getSenderId().equals("-1")) {
            senderId = inventoryAction.getSenderId();
        }
        String senderName = "";
        if (inventoryAction.getSenderName() != null && !inventoryAction.getSenderName().equals("-1")) {
            senderName = inventoryAction.getSenderName();
        }
        String recName = "";
        if (inventoryAction.getRecName() != null && !inventoryAction.getRecName().equals("-1")) {
            recName = inventoryAction.getRecName();
        }
        String recId = "";
        if (inventoryAction.getBuId() != null && !inventoryAction.getBuId().equals("-1")) {
            recId = inventoryAction.getBuId();
        }
        String bolNum = inventoryAction.getBolNum();
        String poNum = inventoryAction.getPoNum();
        String status = inventoryAction.getStatus();
        String ackStatus = inventoryAction.getAckStatus();
        String corrattribute = inventoryAction.getCorrattribute();
        String corrvalue = inventoryAction.getCorrvalue();
        String corrattribute1 = inventoryAction.getCorrattribute1();
        String corrattribute2 = inventoryAction.getCorrattribute2();
        String corrvalue1 = inventoryAction.getCorrvalue1();
        String corrvalue2 = inventoryAction.getCorrvalue2();
        String doctype = "";
        if (inventoryAction.getDocType() != null && !inventoryAction.getDocType().equals("-1")) {
            doctype = inventoryAction.getDocType();
        }
        inventorySearchQuery.append("select DISTINCT(FILES.FILE_ID) as FILE_ID,INVENTORY.ID,FILES.ISA_NUMBER,FILES.GS_CONTROL_NUMBER,FILES.SENDER_ID,FILES.RECEIVER_ID,INVENTORY.REFERENCE_NUMBER,INVENTORY.REPORTING_DATE,"
                + "FILES.DIRECTION,FILES.STATUS,INVENTORY.VENDOR_NAME,INVENTORY.VENDOR_LOCATION,TP1.NAME as SENDER_NAME,TP2.NAME as RECEIVER_NAME from INVENTORY JOIN "
                + "FILES ON (FILES.FILE_ID=INVENTORY.FILE_ID) LEFT OUTER JOIN TP  TP1 ON (TP1.ID=FILES.SENDER_ID) "
                + "LEFT OUTER JOIN TP  TP2 ON (TP2.ID=FILES.RECEIVER_ID) where 1=1 ");
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
        
        //REPORTING_PERIOD
        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("REPORTING_PERIOD"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.REFERENCE_NUMBER", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("REPORTING_PERIOD"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.REFERENCE_NUMBER", corrvalue1.trim().toUpperCase()));
            }
        }
        if ((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("REPORTING_PERIOD"))) {
            if (corrvalue2 != null && !"".equals(corrvalue2.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.REFERENCE_NUMBER", corrvalue2.trim().toUpperCase()));
            }
        }
        //REPORTING_DATE
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("REPORTING_DATE"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.REPORTING_DATE", corrvalue1.trim().toUpperCase()));
            }
        }
        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("REPORTING_DATE"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.REPORTING_DATE", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("REPORTING_DATE"))) {
            if (corrvalue2 != null && !"".equals(corrvalue2.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.REPORTING_DATE", corrvalue2.trim().toUpperCase()));
            }
        }
        //VENDOR_NAME
        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("VENDOR_NAME"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.VENDOR_NAME", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("VENDOR_NAME"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.VENDOR_NAME", corrvalue1.trim().toUpperCase()));
            }
        }
        if ((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("VENDOR_NAME"))) {
            if (corrvalue2 != null && !"".equals(corrvalue2.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.VENDOR_NAME", corrvalue2.trim().toUpperCase()));
            }
        }
        //VENDOR_LOCATION
        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("VENDOR_LOCATION"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.VENDOR_LOCATION", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("VENDOR_LOCATION"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.VENDOR_LOCATION", corrvalue1.trim().toUpperCase()));
            }
        }
        if ((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("VENDOR_LOCATION"))) {
            if (corrvalue2 != null && !"".equals(corrvalue2.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.VENDOR_LOCATION", corrvalue2.trim().toUpperCase()));
            }
        }
   //INSTANCE_ID
        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("INSTANCE_ID"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.FILE_ID", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("INSTANCE_ID"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.FILE_ID", corrvalue1.trim().toUpperCase()));
            }
        }
        if ((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("INSTANCE_ID"))) {
            if (corrvalue2 != null && !"".equals(corrvalue2.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("INVENTORY.FILE_ID", corrvalue2.trim().toUpperCase()));
            }
        }
        //Direction
//        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) {
//            if (corrvalue != null && !"".equals(corrvalue.trim())) {
//                inventorySearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue.trim().toUpperCase()));
//            }
//        }
//        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) {
//            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
//                inventorySearchQuery.append(WildCardSql.getWildCardSql1("FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
//            }
//        }

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
       // inventorySearchQuery.append("order by DATE_TIME_RECEIVED DESC fetch first 50 rows only");
        inventorySearchQuery.append(" fetch first 50 rows only");
        String searchQuery = inventorySearchQuery.toString();
        System.out.println("mscvp inventorySearchQuery query-->" + searchQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            inventoryList = new ArrayList<InventoryBean>();
            while (resultSet.next()) {
                InventoryBean inventoryBean = new InventoryBean();
                inventoryBean.setInventory_id(resultSet.getInt("ID"));
                inventoryBean.setReportingPeriod(resultSet.getString("REFERENCE_NUMBER"));
                inventoryBean.setReportingDate(resultSet.getString("REPORTING_DATE"));
                inventoryBean.setVendorName(resultSet.getString("VENDOR_NAME"));
                inventoryBean.setVendorLocation(resultSet.getString("VENDOR_LOCATION"));
                inventoryBean.setIsaNum(resultSet.getString("ISA_NUMBER"));
                inventoryBean.setGsCtrl(resultSet.getString("GS_CONTROL_NUMBER"));
                String direction = resultSet.getString("DIRECTION");
                inventoryBean.setDirection(direction);
                if ("INBOUND".equalsIgnoreCase(direction)) {
                    inventoryBean.setPname(resultSet.getString("SENDER_NAME"));
                } else {
                    inventoryBean.setPname(resultSet.getString("RECEIVER_NAME"));
                }
                inventoryBean.setStatus(resultSet.getString("STATUS"));
               // inventoryBean.setDate_time_rec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                inventoryBean.setFile_id(resultSet.getString("FILE_ID"));
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
        String datepickerTo = inventoryAction.getDocdatepicker();
        String datePickerFrom = inventoryAction.getDocdatepickerfrom();
        String senderId = "";
        if (inventoryAction.getSenderId() != null && !inventoryAction.getSenderId().equals("-1")) {
            senderId = inventoryAction.getSenderId();
        }
        String senderName = "";
        if (inventoryAction.getSenderName() != null && !inventoryAction.getSenderName().equals("-1")) {
            senderName = inventoryAction.getSenderName();
        }
        String recName = "";
        if (inventoryAction.getRecName() != null && !inventoryAction.getRecName().equals("-1")) {
            recName = inventoryAction.getRecName();
        }
        String recId = "";
        if (inventoryAction.getBuId() != null && !inventoryAction.getBuId().equals("-1")) {
            recId = inventoryAction.getBuId();
        }
        String bolNum = inventoryAction.getBolNum();
        String poNum = inventoryAction.getPoNum();
        String status = inventoryAction.getStatus();
        String ackStatus = inventoryAction.getAckStatus();
        String corrattribute = inventoryAction.getCorrattribute();
        String corrvalue = inventoryAction.getCorrvalue();
        String corrattribute1 = inventoryAction.getCorrattribute1();
        String corrattribute2 = inventoryAction.getCorrattribute2();
        String corrvalue1 = inventoryAction.getCorrvalue1();
        String corrvalue2 = inventoryAction.getCorrvalue2();
        String doctype = "";
        if (inventoryAction.getDocType() != null && !inventoryAction.getDocType().equals("-1")) {
            doctype = inventoryAction.getDocType();
        }
       inventorySearchQuery.append("select DISTINCT(ARCHIVE_INVENTORY.FILE_ID) as FILE_ID,ARCHIVE_INVENTORY.ID,FILES.ISA_NUMBER,FILES.GS_CONTROL_NUMBER,FILES.SENDER_ID,FILES.RECEIVER_ID,ARCHIVE_INVENTORY.REFERENCE_NUMBER,ARCHIVE_INVENTORY.REPORTING_DATE,"
                + "ARCHIVE_FILES.DIRECTION,ARCHIVE_FILES.STATUS,ARCHIVE_INVENTORY.VENDOR_NAME,ARCHIVE_INVENTORY.VENDOR_LOCATION,TP1.NAME,TP2.NAME from ARCHIVE_INVENTORY JOIN "
                + "ARCHIVE_FILES ON (ARCHIVE_FILES.FILE_ID=ARCHIVE_INVENTORY.FILE_ID) LEFT OUTER JOIN TP  TP1 ON (TP1.ID=ARCHIVE_FILES.SENDER_ID) "
                + "LEFT OUTER JOIN TP  TP2 ON (TP2.ID=ARCHIVE_FILES.RECEIVER_ID) where 1=1 ");
				
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
        
        //REPORTING_PERIOD
        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("REPORTING_PERIOD"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.REFERENCE_NUMBER", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("REPORTING_PERIOD"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.REFERENCE_NUMBER", corrvalue1.trim().toUpperCase()));
            }
        }
        if ((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("REPORTING_PERIOD"))) {
            if (corrvalue2 != null && !"".equals(corrvalue2.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.REFERENCE_NUMBER", corrvalue2.trim().toUpperCase()));
            }
        }
        //REPORTING_DATE
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("REPORTING_DATE"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.REPORTING_DATE", corrvalue1.trim().toUpperCase()));
            }
        }
        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("REPORTING_DATE"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.REPORTING_DATE", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("REPORTING_DATE"))) {
            if (corrvalue2 != null && !"".equals(corrvalue2.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.REPORTING_DATE", corrvalue2.trim().toUpperCase()));
            }
        }
        //VENDOR_NAME
        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("VENDOR_NAME"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.VENDOR_NAME", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("VENDOR_NAME"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.VENDOR_NAME", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("VENDOR_NAME"))) {
            if (corrvalue2 != null && !"".equals(corrvalue2.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.VENDOR_NAME", corrvalue2.trim().toUpperCase()));
            }
        }
        //VENDOR_LOCATION
        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("VENDOR_LOCATION"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.VENDOR_LOCATION", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("VENDOR_LOCATION"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.VENDOR_LOCATION", corrvalue1.trim().toUpperCase()));
            }
        }
        if ((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("VENDOR_LOCATION"))) {
            if (corrvalue2 != null && !"".equals(corrvalue2.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.VENDOR_LOCATION", corrvalue2.trim().toUpperCase()));
            }
        }
   //INSTANCE_ID
        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("INSTANCE_ID"))) {
            if (corrvalue != null && !"".equals(corrvalue.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.FILE_ID", corrvalue.trim().toUpperCase()));
            }
        }
        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("INSTANCE_ID"))) {
            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.FILE_ID", corrvalue1.trim().toUpperCase()));
            }
        }
        if ((corrattribute2 != null) && (corrattribute2.equalsIgnoreCase("INSTANCE_ID"))) {
            if (corrvalue2 != null && !"".equals(corrvalue2.trim())) {
                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_INVENTORY.FILE_ID", corrvalue2.trim().toUpperCase()));
            }
        }
        //Direction
//        if ((corrattribute != null) && (corrattribute.equalsIgnoreCase("Direction"))) {
//            if (corrvalue != null && !"".equals(corrvalue.trim())) {
//                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_ARCHIVE_FILES.DIRECTION", corrvalue.trim().toUpperCase()));
//            }
//        }
//        if ((corrattribute1 != null) && (corrattribute1.equalsIgnoreCase("Direction"))) {
//            if (corrvalue1 != null && !"".equals(corrvalue1.trim())) {
//                inventorySearchQuery.append(WildCardSql.getWildCardSql1("ARCHIVE_FILES.DIRECTION", corrvalue1.trim().toUpperCase()));
//            }
//        }

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
        inventorySearchQuery.append(" fetch first 50 rows only");
        String searchQuery = inventorySearchQuery.toString();
        System.out.println("mscvp inventorySearchQuery query-->" + searchQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            inventoryList = new ArrayList<InventoryBean>();
            while (resultSet.next()) {
                InventoryBean inventoryBean = new InventoryBean();
                inventoryBean.setReportingPeriod(resultSet.getString("REFERENCE_NUMBER"));
                inventoryBean.setReportingDate(resultSet.getString("REPORTING_DATE"));
                inventoryBean.setVendorName(resultSet.getString("VENDOR_NAME"));
                inventoryBean.setVendorLocation(resultSet.getString("VENDOR_LOCATION"));
                inventoryBean.setIsaNum(resultSet.getString("ISA_NUMBER"));
                inventoryBean.setGsCtrl(resultSet.getString("GS_CONTROL_NUMBER"));
                String direction = resultSet.getString("DIRECTION");
                inventoryBean.setDirection(direction);
                if ("INBOUND".equalsIgnoreCase(direction)) {
                    inventoryBean.setPname(resultSet.getString("SENDER_NAME"));
                } else {
                    inventoryBean.setPname(resultSet.getString("RECEIVER_NAME"));
                }
                inventoryBean.setStatus(resultSet.getString("STATUS"));
                //inventoryBean.setDate_time_rec(resultSet.getTimestamp("DATE_TIME_RECEIVED"));
                inventoryBean.setFile_id(resultSet.getString("FILE_ID"));
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
