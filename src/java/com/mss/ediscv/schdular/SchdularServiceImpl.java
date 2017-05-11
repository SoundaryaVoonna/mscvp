/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.schdular;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.PasswordUtil;
import com.mss.ediscv.util.ServiceLocatorException;
import java.lang.String;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.apache.log4j.Logger;
import java.util.Iterator;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * @author miracle
 */
public class SchdularServiceImpl implements SchdularService {

    private HttpServletRequest httpServletRequest;
    private String resultMessage;
    Connection connection = null;
    PreparedStatement preparedStatement = null;
    CallableStatement callableStatement = null;
    Statement statement = null;
    ResultSet resultSet = null;
    String tmp_Recieved_From = "";
    String tmp_Recieved_ToTime = "";
    String strFormat = "yyyy-MM-dd";
    DateFormat myDateFormat = new SimpleDateFormat(strFormat);
    Calendar cal = new GregorianCalendar();
    java.util.Date now = cal.getTime();
    long time = now.getTime();
    java.sql.Date date = new java.sql.Date(time);
    private static Logger logger = Logger.getLogger(SchdularServiceImpl.class.getName());
    String responseString = null;
    private ArrayList<SchdularBean> schdularList;

    public ArrayList<SchdularBean> getSchdularList(SchdularAction schdularAction) throws ServiceLocatorException {
        StringBuffer documentSearchQuery = new StringBuffer();
        String status = schdularAction.getStatus();
        documentSearchQuery.append("SELECT SCH_ID,SCH_TITLE,SCH_TYPE,SCH_TS,SCH_STATUS from SCHEDULER ");
        documentSearchQuery.append("WHERE 1=1 ");
        //Status
        if (status != null && !"-1".equals(status.trim())) {
            documentSearchQuery.append(" AND SCHEDULER.SCH_STATUS='" + status + "' ");
        }
        String searchQuery = documentSearchQuery.toString();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            statement = connection.createStatement();
            resultSet = statement.executeQuery(searchQuery);
            schdularList = new ArrayList<SchdularBean>();
            while (resultSet.next()) {
                SchdularBean schdularBean = new SchdularBean();
                schdularBean.setId(resultSet.getInt("SCH_ID"));
                schdularBean.setSchtitle(resultSet.getString("SCH_TITLE"));
                schdularBean.setSchtype(resultSet.getString("SCH_TYPE"));
                schdularBean.setSchhrFormat(resultSet.getString("SCH_TS"));
                schdularBean.setStatus(resultSet.getString("SCH_STATUS"));
                schdularList.add(schdularBean);
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
        return schdularList;
    }

    public String SchdularAdd(SchdularAction schdularAction) throws ServiceLocatorException {
        StringBuilder str = new StringBuilder(schdularAction.getSchhours());
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String time = schdularAction.getSchhours() + " " + schdularAction.getSchhrFormat();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("INSERT INTO SCHEDULER(SCH_TITLE,SCH_TYPE,SCH_STATUS,SCH_TS,RECIVER_IDS,EXTRANAL_EMAILIDS,REPORTS_TYPE) VALUES (?,?,?,?,?,?,?)");
            preparedStatement.setString(1, schdularAction.getSchtitle());
            preparedStatement.setString(2, schdularAction.getSchType());
            preparedStatement.setString(3, "Active");
            preparedStatement.setString(4, time);
            preparedStatement.setString(5, schdularAction.getUserEmail());
            preparedStatement.setString(6, schdularAction.getExtranalmailids());
            preparedStatement.setString(7, schdularAction.getReportsType());
            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                responseString = "<font color='green'>Schduler added succesfully.</font>";
            } else {
                responseString = "<font color='red'>Please try again!</font>";
            }
            if (time != null && !time.equals("")) {
                int count = 0;
                preparedStatement = connection.prepareStatement("select count(*) as total from SCHEDULER where SCH_TYPE=? AND SCH_TS=?");
                preparedStatement.setString(1, schdularAction.getSchType());
                String time1 = schdularAction.getSchhours() + " " + schdularAction.getSchhrFormat();
                preparedStatement.setString(2, time1);
                resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    count = resultSet.getInt("total");
                }
                if (count > 1) {
                    responseString = "<font color='green'>SchdulerTime alredy In Running Please try to anthor time.</font>";
                }
            }
        } catch (SQLException e) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            e.printStackTrace();
        } catch (Exception ex) {
            responseString = "<font color='red'>Please try again!</font>";
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
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return responseString;
    }

    public SchdularAction schdularEdit(SchdularAction schdularAction) throws ServiceLocatorException {
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT SCH_ID,SCH_TITLE,SCH_TYPE,SCH_TS,RECIVER_IDS,EXTRANAL_EMAILIDS,REPORTS_TYPE from SCHEDULER WHERE SCH_ID=?");
            preparedStatement.setInt(1, schdularAction.getId());
            resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                schdularAction.setId(resultSet.getInt("SCH_ID"));
                schdularAction.setSchtitle(resultSet.getString("SCH_TITLE"));
                schdularAction.setSchType(resultSet.getString("SCH_TYPE"));
                String time1 = resultSet.getString("SCH_TS");
                String[] parts = time1.split(" ");
                String hours = parts[0];
                String hoursformate = parts[1];
                schdularAction.setSchhours(hours);
                schdularAction.setSchhrFormat(hoursformate);
                String Email = resultSet.getString("RECIVER_IDS");
                String[] reciverids = Email.split(",");
                List<String> wordList = Arrays.asList(reciverids);
                Iterator<String> iter = wordList.iterator();
                List<String> copy = new ArrayList<String>();
                while (iter.hasNext()) {
                    copy.add(iter.next().trim());
                }
                schdularAction.setReceiverids(copy);
                schdularAction.setExtranalmailids(resultSet.getString("EXTRANAL_EMAILIDS"));
                schdularAction.setReportsType(resultSet.getString("REPORTS_TYPE"));
            }
        } catch (SQLException e) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            e.printStackTrace();
        } catch (Exception ex) {
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
        return schdularAction;
    }

    public String updateSchdular(SchdularAction schdularAction) throws ServiceLocatorException {
        String time = schdularAction.getSchhours() + " " + schdularAction.getSchhrFormat();
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("UPDATE SCHEDULER SET SCH_TITLE=?,SCH_TYPE=?,SCH_TS=?,RECIVER_IDS=?,EXTRANAL_EMAILIDS=?,REPORTS_TYPE=? WHERE SCH_ID='" + schdularAction.getId() + "'");
            preparedStatement.setString(1, schdularAction.getSchtitle());
            preparedStatement.setString(2, schdularAction.getSchType());
            preparedStatement.setString(3, time);
            preparedStatement.setString(4, schdularAction.getUserEmail());
            preparedStatement.setString(5, schdularAction.getExtranalmailids());
            preparedStatement.setString(6, schdularAction.getReportsType());
            int i = preparedStatement.executeUpdate();
            if (i > 0) {
                responseString = "<font color='green'>Schdular updated succesfully.</font>";
            } else {
                responseString = "<font color='red'>Please try again!</font>";
            }
        } catch (SQLException e) {
            responseString = "<font color='red'>Please try with different Id!</font>";
            e.printStackTrace();
        } catch (Exception ex) {
            responseString = "<font color='red'>Please try later!</font>";
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
            } catch (SQLException se) {
                throw new ServiceLocatorException(se);
            }
        }
        return responseString;
    }

    public boolean getAuthdownloadUsercheck(SchdularAction schdularAction) throws ServiceLocatorException {
        boolean isUserExist = false;
        String password = null;
        String username = null;
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        PasswordUtil passwordUtility = new PasswordUtil();
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT LOGINID,PASSWD FROM M_USER WHERE LOGINID=?");
            preparedStatement.setString(1, schdularAction.getLoginId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                username = resultSet.getString("LOGINID");
                password = resultSet.getString("PASSWD");
                String decryptedPwd = passwordUtility.decryptPwd(password);
                if (decryptedPwd.equals(schdularAction.getPassword())) {
                    isUserExist = true;
                }
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
        return isUserExist;
    }

    public String SchdularRecordPath(SchdularAction schdularAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String reportpath = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT SCH_REPORTPATH from SCH_LOOKUPS where SCH_REF_ID=?");
            preparedStatement.setInt(1, schdularAction.getScheduleRefId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                reportpath = resultSet.getString("SCH_REPORTPATH");
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
        return reportpath;
    }

    public String SchdularEmailids(SchdularAction schdularAction) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String Emailids = null;
        try {
            connection = ConnectionProvider.getInstance().getConnection();
            preparedStatement = connection.prepareStatement("SELECT RECIVER_IDS FROM SCHEDULER LEFT OUTER JOIN SCH_LOOKUPS ON (SCHEDULER.SCH_ID = SCH_LOOKUPS.SCH_ID) where SCH_REF_ID=?");
            preparedStatement.setInt(1, schdularAction.getScheduleRefId());
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Emailids = resultSet.getString("RECIVER_IDS");
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
        return Emailids;
    }
}