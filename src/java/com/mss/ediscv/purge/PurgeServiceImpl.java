/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.purge;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.DateUtility;
import com.mss.ediscv.util.ServiceLocatorException;
import java.sql.BatchUpdateException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import org.apache.log4j.Logger;

/**
 *
 * @author miracle
 */
public class PurgeServiceImpl implements PurgeService {

    Connection connection = null;
    PreparedStatement preparedStatement = null;
    Statement statement = null;
    ResultSet resultSet = null;
    CallableStatement callableStatement = null;
    private static Logger logger = Logger.getLogger(PurgeServiceImpl.class.getName());
    String responseString = null;
    Calendar cal = new GregorianCalendar();
    java.util.Date now = cal.getTime();
    long time = now.getTime();
    java.sql.Date date = new java.sql.Date(time);
    boolean updateArchHistQueryFlag = false;

    public String purgeProcess(PurgeAction purgeAction, String username, String defaultFlowName) throws ServiceLocatorException {

        String dayCount = purgeAction.getDayCount();
        String transType = purgeAction.getTransType();
        String comments = purgeAction.getComments();
        String user = username;
        String flag = "Purge";

        //System.out.println("purge process method in purgeserviceimpl" + transType + "  " + user + "  " + comments);

        Map deleteMap = new TreeMap();
        List priKeyList = new ArrayList();
        StringBuffer queryString = new StringBuffer("");
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            // queryString.append("insert into ARCHIVE_HISTORY(TRANSACTION_TYPE, DAYS_COUNT, USER, COMMENTS, DATE, FLAG) values ('" + transType + "','" + dayCount + "','" + user + "','" + comments + "','" + date + "','" + flag + "');");

            //System.out.println(" insert query updated ");
            queryString.append("select FILE_ID, Transaction_Type from ARCHIVE_FILES where DATE(DATE_TIME_RECEIVED) <  DATE(CURRENT TIMESTAMP - " + dayCount + " DAYS)");
            //queryString.append("delete * from ARCHIVE_FILES where DATE(DATE_TIME_RECEIVED) <  DATE(CURRENT TIMESTAMP - " +dayCount+ " DAYS)");
            //queryString.append("select Id, Transaction_Type,FILE_ID,DATE_TIME_RECEIVED   from FILES where DATE(DATE_TIME_RECEIVED) <  DATE(CURRENT TIMESTAMP - " +dayCount+ " DAYS)");
            //System.out.println(" select query updated ");
            if (!transType.equals("-1")) {
                queryString.append(" AND Transaction_Type = '" + transType + "'");
            }
            //System.out.println("queryString purge process --> " + queryString);
            preparedStatement = connection.prepareStatement(queryString.toString());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                deleteMap.put(resultSet.getString("FILE_ID"), resultSet.getString("Transaction_Type"));
            }
            Set set = deleteMap.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();
                deleteReocords((String) me.getKey(), (String) me.getValue(), dayCount, user, comments, date, flag, defaultFlowName);
                //System.out.println(" in while loop before delete records method");
            }
            //connection.commit();
            responseString = "<font color='green'>Purge Process Completed Successfully</font>";
        } catch (SQLException e) {
            e.printStackTrace();
            responseString = "<font color='red'>Please try Again</font>";
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            responseString = "<font color='red'>Please try again!</font>";
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
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return responseString;
    }

    public void deleteReocords(String fileId, String transType, String dayCount, String user, String comments, java.sql.Date date, String flag, String defaultFlowName) throws ServiceLocatorException {
        Connection connection = null;
        Statement statement = null;
        String updateArchHistQuery = null;
        //System.out.println("deleterecords method in purgeserviceimpl");

        try {
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(false);
            statement = connection.createStatement();
           
                String deleteArchFilesQuery = "DELETE FROM ARCHIVE_FILES WHERE File_ID='" + fileId + "'";
                if (!(updateArchHistQueryFlag)) {
                    updateArchHistQuery = "insert into ARCHIVE_HISTORY(TRANSACTION_TYPE, DAYS_COUNT, USER, COMMENTS, DATE, FLAG) values ('" + transType + "','" + dayCount + "','" + user + "','" + comments + "','" + date + "','" + flag + "')";
                    statement.addBatch(updateArchHistQuery);
                    updateArchHistQueryFlag = true;
                    //System.out.println("running ==== in boolean loop");
                }

                String deleteArchtransQuery = "";
                 if (defaultFlowName.equalsIgnoreCase("Manufacturing")) {
                if (transType.equals("850")) {
                    deleteArchtransQuery = "DELETE FROM ARCHIVE_PO WHERE File_ID='" + fileId + "'";
                } else if (transType.equals("856")) {
                    deleteArchtransQuery = "DELETE FROM ARCHIVE_ASN WHERE File_ID='" + fileId + "'";
                } else if (transType.equals("810")) {
                    deleteArchtransQuery = "DELETE FROM ARCHIVE_INVOICE WHERE File_ID='" + fileId + "'";
                } else if (transType.equals("820")) {
                    deleteArchtransQuery = "DELETE FROM ARCHIVE_PAYMENT WHERE File_ID='" + fileId + "'";
                }

                statement.addBatch(deleteArchFilesQuery);
                statement.addBatch(deleteArchtransQuery);

//                System.out.println("deleteArchFilesQuery -->" + deleteArchFilesQuery);
//                System.out.println("updateArchHistQuery -->" + updateArchHistQuery);
//                System.out.println("deleteArchtransQuery -->" + deleteArchtransQuery);

              
            } else if (defaultFlowName.equalsIgnoreCase("Logistics")) {
                //System.out.println("");
                  if (transType.equals("204")) {
                    deleteArchtransQuery = "DELETE FROM ARCHIVE_TRANSPORT_LOADTENDER WHERE File_ID='" + fileId + "'";
                } else if (transType.equals("990")) {
                    deleteArchtransQuery = "DELETE FROM ARCHIVE_TRANSPORT_LT_RESPONSE WHERE File_ID='" + fileId + "'";
                } else if (transType.equals("214")) {
                    deleteArchtransQuery = "DELETE FROM ARCHIVE_TRANSPORT_SHIPMENT WHERE File_ID='" + fileId + "'";
                } else if (transType.equals("210")) {
                    deleteArchtransQuery = "DELETE FROM ARCHIVE_TRANSPORT_INVOICE WHERE File_ID='" + fileId + "'";
                }

                statement.addBatch(deleteArchFilesQuery);
                statement.addBatch(deleteArchtransQuery);

            }
                   int[] count = statement.executeBatch();
                //System.out.println(" count=== " + count);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();

            // } catch (Exception ex) {
            //      ex.printStackTrace();
            // } finally {
            //     try {
            //        if (statement != null) {
            //            statement.close();
            //            statement = null;
            //       }
            //       if (connection != null) {
            //           connection.close();
            //           connection = null;
            //      }
            //  } catch (SQLException se) {
            //       throw new ServiceLocatorException(se);
            //   }
        }
    }

    ///added for Archiving  
    public String archiveProcess(PurgeAction purgeAction, String username, String flowName) throws ServiceLocatorException {

        String dayCount = purgeAction.getDayCount();
        String transType = purgeAction.getTransType();
        String comments = purgeAction.getComments();
        String user = username;
        String flag = "Archive";

        //System.out.println("purge process method in purgeserviceimpl" + transType + "  " + user + "  " + comments + "" + dayCount);

        Map deleteMap = new TreeMap();

        List fileisList = new ArrayList();

        StringBuffer queryString = new StringBuffer("");
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            // queryString.append("insert into ARCHIVE_HISTORY(TRANSACTION_TYPE, DAYS_COUNT, USER, COMMENTS, DATE, FLAG) values ('" + transType + "','" + dayCount + "','" + user + "','" + comments + "','" + date + "','" + flag + "');");

            //System.out.println(" insert query updated ");
            if ("Logistics".equals(flowName)) {
                //System.out.println("archiveProcess flow name is  logistics");
                queryString.append("select FILE_ID from FILES where DATE(DATE_TIME_RECEIVED) <  DATE(CURRENT TIMESTAMP - " + dayCount + " DAYS) and TRANSACTION_TYPE=" + transType + " AND FLOWFLAG='L'");
            } else if ("Manufacturing".equals(flowName)) {
                queryString.append("select FILE_ID from FILES where DATE(DATE_TIME_RECEIVED) <  DATE(CURRENT TIMESTAMP - " + dayCount + " DAYS) and TRANSACTION_TYPE=" + transType + " AND FLOWFLAG='M'");
            }
            //System.out.println("queryString is "+queryString);
            //queryString.append("select FILE_ID, Transaction_Type from ARCHIVE_FILES where DATE(DATE_TIME_RECEIVED) <  DATE(CURRENT TIMESTAMP - " + dayCount + " DAYS)");
            //queryString.append("delete * from ARCHIVE_FILES where DATE(DATE_TIME_RECEIVED) <  DATE(CURRENT TIMESTAMP - " +dayCount+ " DAYS)");
            //queryString.append("select Id, Transaction_Type,FILE_ID,DATE_TIME_RECEIVED   from FILES where DATE(DATE_TIME_RECEIVED) <  DATE(CURRENT TIMESTAMP - " +dayCount+ " DAYS)");
            //System.out.println(" select query updated ");
            preparedStatement = connection.prepareStatement(queryString.toString());
            //System.out.println("query == " + queryString.toString());
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                fileisList.add(resultSet.getString("FILE_ID"));
                //System.out.println("File id is "+resultSet.getString("FILE_ID"));

            }

            // System.out.println("list values"+fileisList.iterator().next().toString());
            for (int i = 0; i < fileisList.size(); i++) {
               
             
                    //System.out.println("list values = " + (String) fileisList.get(i).toString());
                   
                archiveReocords((String) fileisList.get(i).toString(), transType, dayCount, user, comments, date, flag, flowName);
            
            }
            Set set = deleteMap.entrySet();
            Iterator i = set.iterator();
            while (i.hasNext()) {
                Map.Entry me = (Map.Entry) i.next();

                //System.out.println(" in while loop before delete records method");
            }
            //connection.commit();
            responseString = "<font color='green'>Archive Process Completed Successfully</font>";
        } catch (SQLException e) {
            e.printStackTrace();
            responseString = "<font color='red'>Please try Again</font>";
            e.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
            responseString = "<font color='red'>Please try again!</font>";
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
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return responseString;
    }

    public void archiveReocords(String fileId, String transType, String dayCount, String user, String comments, java.sql.Date date, String flag, String flowName) throws ServiceLocatorException {

        Connection connection = null;
        Statement statement = null;

        String insertArchFilesQuery = null;
        String deleteFilesQuery = null;
        String updateArchHistQuery = null;
        String insertArchTransQuery = null;
        String deleteArchTransQuery = null;

        //System.out.println("deleterecords method in purgeserviceimpl");
        //System.out.println("flow name is " + flowName);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            connection.setAutoCommit(true);
            statement = connection.createStatement();
            //String deleteArchFilesQuery = "DELETE FROM FILES WHERE File_ID='" + fileId + "'";

            if (!(updateArchHistQueryFlag)) {

                insertArchFilesQuery = "insert into ARCHIVE_FILES select f.* from files f where DATE(DATE_TIME_RECEIVED) <  DATE(CURRENT TIMESTAMP - " + dayCount + " DAYS) and Transaction_Type= '" + transType + "' ";
                //deleteFilesQuery = "DELETE FROM FILES where DATE(DATE_TIME_RECEIVED) <  DATE(CURRENT TIMESTAMP - " + dayCount + " DAYS) and Transaction_Type= '"+transType+"' ";
                updateArchHistQuery = "insert into ARCHIVE_HISTORY(TRANSACTION_TYPE, DAYS_COUNT, USER, COMMENTS, DATE, FLAG) values ('" + transType + "','" + dayCount + "','" + user + "','" + comments + "','" + date + "','" + flag + "')";

                statement.addBatch(insertArchFilesQuery);
                //statement.addBatch(deleteFilesQuery);
                statement.addBatch(updateArchHistQuery);

                updateArchHistQueryFlag = true;
                //System.out.println("insertArchFilesQuery" + insertArchFilesQuery);
                //System.out.println("updateArchHistQuery" + updateArchHistQuery);
            }
            if (flowName.equalsIgnoreCase("Manufacturing")) {
                if (transType.equals("850")) {
                    insertArchTransQuery = "INSERT INTO ARCHIVE_PO SELECT t.* FROM MSCVP.PO t where file_ID= '" + fileId + "' ";
                    //deleteArchTransQuery = "DELETE FROM ARCHIVE_PO WHERE File_ID='" + fileId + "'";
                } else if (transType.equals("856")) {
                    insertArchTransQuery = "INSERT INTO ARCHIVE_ASN SELECT t.* FROM MSCVP.ASN t where file_ID= '" + fileId + "' ";
                    //deleteArchTransQuery = "DELETE FROM ARCHIVE_ASN WHERE File_ID='" + fileId + "'";
                } else if (transType.equals("810")) {
                    insertArchTransQuery = "INSERT INTO ARCHIVE_INVOICE SELECT t.* FROM MSCVP.INVOICE t where file_ID= '" + fileId + "' ";
                    // deleteArchTransQuery = "DELETE FROM ARCHIVE_INVOICE WHERE File_ID='" + fileId + "'";
                } else if (transType.equals("820")) {
                    insertArchTransQuery = "INSERT INTO ARCHIVE_PAYMENT SELECT t.* FROM MSCVP.PAYMENT t where file_ID= '" + fileId + "' ";
                    // deleteArchTransQuery = "DELETE FROM ARCHIVE_PAYMENT WHERE File_ID='" + fileId + "'";
                }

                //System.out.println("insertArchTransQuery -->" + insertArchTransQuery);

                statement.addBatch(insertArchTransQuery);
                //statement.addBatch(deleteArchTransQuery);

            } else if (flowName.equalsIgnoreCase("Logistics")) {
                //System.out.println("In archive records of logistics flow");
                if (transType.equals("204")) {
                    insertArchTransQuery = "INSERT INTO ARCHIVE_TRANSPORT_LOADTENDER SELECT t.* FROM MSCVP.TRANSPORT_LOADTENDER t where file_ID= '" + fileId + "' ";
                    //deleteArchTransQuery = "DELETE FROM ARCHIVE_PO WHERE File_ID='" + fileId + "'";
                } else if (transType.equals("990")) {
                    insertArchTransQuery = "INSERT INTO ARCHIVE_TRANSPORT_LT_RESPONSE SELECT t.* FROM MSCVP.TRANSPORT_LT_RESPONSE t where file_ID= '" + fileId + "' ";
                    //deleteArchTransQuery = "DELETE FROM ARCHIVE_ASN WHERE File_ID='" + fileId + "'";
                } else if (transType.equals("214")) {
                    insertArchTransQuery = "INSERT INTO ARCHIVE_TRANSPORT_SHIPMENT SELECT t.* FROM MSCVP.TRANSPORT_SHIPMENT t where file_ID= '" + fileId + "' ";
                    // deleteArchTransQuery = "DELETE FROM ARCHIVE_INVOICE WHERE File_ID='" + fileId + "'";
                } else if (transType.equals("210")) {
                    insertArchTransQuery = "INSERT INTO ARCHIVE_TRANSPORT_INVOICE SELECT t.* FROM MSCVP.TRANSPORT_INVOICE t where file_ID= '" + fileId + "' ";
                    // deleteArchTransQuery = "DELETE FROM ARCHIVE_PAYMENT WHERE File_ID='" + fileId + "'";
                }

                //System.out.println("insertArchTransQuery -->" + insertArchTransQuery);

                statement.addBatch(insertArchTransQuery);
            }
            int[] count = statement.executeBatch();
            //System.out.println(" count=== " + count);
            connection.commit();
        } catch (BatchUpdateException e) {
            e.getNextException();

        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    @Override
    public List getPurHistoryData(String username, String from, String to, String transType) throws ServiceLocatorException {

        List<PurgeHistoryBean> list = new ArrayList<PurgeHistoryBean>();
        StringBuffer archiveHistoryQuery = new StringBuffer();
        String fromDate = DateUtility.getInstance().DateViewToDBCompare(from);
        String toDate = DateUtility.getInstance().DateViewToDBCompare(to);
        archiveHistoryQuery.append("SELECT *  FROM ARCHIVE_HISTORY WHERE DATE >='" + fromDate + "' AND DATE <='" + toDate + "'");
        //and flag = "purge"
        if ((transType != null) && (!"-1".equals(transType.trim()))) {

            archiveHistoryQuery.append("AND TRANSACTION_TYPE = '" + transType + "'");
        }
        archiveHistoryQuery.append("AND FLAG = 'Purge'");
        //System.out.println("archiveHistoryQuery is " + archiveHistoryQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(archiveHistoryQuery.toString());
            while (resultSet.next()) {
                PurgeHistoryBean purgeHistoryBean = new PurgeHistoryBean();
                purgeHistoryBean.setUser(resultSet.getString("USER"));
                purgeHistoryBean.setTransactionType(resultSet.getString("TRANSACTION_TYPE"));
                purgeHistoryBean.setDaysCount(resultSet.getInt("DAYS_COUNT"));
                purgeHistoryBean.setComments(resultSet.getString("COMMENTS"));
                purgeHistoryBean.setArchiveDate(resultSet.getTimestamp("DATE"));
                list.add(purgeHistoryBean);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return list;
    }

    public List getArcHistoryData(String username, String from, String to, String transType) throws ServiceLocatorException {

        List<PurgeHistoryBean> list = new ArrayList<PurgeHistoryBean>();

        StringBuffer archiveHistoryQuery = new StringBuffer();
        String fromDate = DateUtility.getInstance().DateViewToDBCompare(from);
        String toDate = DateUtility.getInstance().DateViewToDBCompare(to);
        archiveHistoryQuery.append("SELECT *  FROM ARCHIVE_HISTORY WHERE DATE >='" + fromDate + "' AND DATE <='" + toDate + "'");
        //and flag = "purge"
        if ((transType != null) && (!"-1".equals(transType.trim()))) {

            archiveHistoryQuery.append("AND TRANSACTION_TYPE = '" + transType + "'");
        }
        archiveHistoryQuery.append("AND FLAG = 'Archive'");
        //System.out.println("archiveHistoryQuery is " + archiveHistoryQuery);
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(archiveHistoryQuery.toString());
            while (resultSet.next()) {
                PurgeHistoryBean purgeHistoryBean = new PurgeHistoryBean();

                purgeHistoryBean.setUser(resultSet.getString("USER"));
                purgeHistoryBean.setTransactionType(resultSet.getString("TRANSACTION_TYPE"));
                purgeHistoryBean.setDaysCount(resultSet.getInt("DAYS_COUNT"));
                purgeHistoryBean.setComments(resultSet.getString("COMMENTS"));
                purgeHistoryBean.setArchiveDate(resultSet.getTimestamp("DATE"));
                list.add(purgeHistoryBean);
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
        return list;

    }

}
