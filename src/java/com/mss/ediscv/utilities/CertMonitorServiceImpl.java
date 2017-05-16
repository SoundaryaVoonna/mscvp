/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mss.ediscv.utilities;

import com.mss.ediscv.util.ConnectionProvider;
import com.mss.ediscv.util.ServiceLocatorException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author miracle
 */
public class CertMonitorServiceImpl implements CertMonitorService {

    public List getCertMonitorData(String certType, String dateFrom, String dateTo) throws ServiceLocatorException {

        String cType = certType;
        Connection con = null;
        List<LinkedHashMap> al = new LinkedList<LinkedHashMap>();
        System.out.println("dateFrom is " + dateFrom);
        String date = null;
        String dateto = null;
        if ((dateFrom != null) && !"".equalsIgnoreCase(dateFrom)) {
            date = dateFrom.replace("/", "-").substring(0, 10);
            dateto = dateTo.replace("/", "-").substring(0, 10);

        }
        System.out.println("datefrom is " + date);
        System.out.println("dateto is " + dateto);
        try {

            Class.forName("oracle.jdbc.OracleDriver");
            con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.1.179:1521:orcl", "si_user", "SI_admin1");
            Statement st = con.createStatement();
            ResultSet rs = null;
            if ("TRUSTED".equalsIgnoreCase(cType)) {
                if ((dateFrom != null) && !"".equalsIgnoreCase(dateFrom)) {
                    rs = st.executeQuery("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL , (to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy')) AS DAYS FROM TRUSTED_CERT_INFO WHERE NOT_BEFORE > to_date('" + date + "','mm-dd-yyyy') AND NOT_AFTER < to_date('" + dateto + "','mm-dd-yyyy') ORDER BY DAYS ");
                    //  System.out.println("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL , (to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy')) AS DAYS FROM TRUSTED_CERT_INFO WHERE NOT_BEFORE > to_date('" + date + "','mm-dd-yyyy') AND NOT_AFTER < to_date('" + dateto + "','mm-dd-yyyy') ORDER BY DAYS ");
                } else {
                    rs = st.executeQuery("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL , (to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy')) AS DAYS FROM TRUSTED_CERT_INFO ORDER BY DAYS ");
                    //  System.out.println("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL , (to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy')) AS DAYS FROM TRUSTED_CERT_INFO ORDER BY DAYS ");

                }
            } else if ("CA".equalsIgnoreCase(cType)) {
                if ((dateFrom != null) && !"".equalsIgnoreCase(dateFrom)) {
                    rs = st.executeQuery("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL , (to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy')) AS DAYS FROM CA_CERT_INFO WHERE NOT_BEFORE > to_date('" + date + "','mm-dd-yyyy') AND NOT_AFTER < to_date('" + dateto + "','mm-dd-yyyy')  ORDER BY DAYS");
                    // System.out.println("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL , (to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy')) AS DAYS FROM CA_CERT_INFO WHERE NOT_BEFORE > to_date('" + date + "','mm-dd-yyyy') AND NOT_AFTER < to_date('" + dateto + "','mm-dd-yyyy')  ORDER BY DAYS");;
                } else {
                    rs = st.executeQuery("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL , (to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy')) AS DAYS FROM CA_CERT_INFO   ORDER BY DAYS");
                    // System.out.println("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL , (to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy')) AS DAYS FROM CA_CERT_INFO   ORDER BY DAYS");
                }
            } else if ("SYSTEM".equalsIgnoreCase(cType)) {
                if ((dateFrom != null) && !"".equalsIgnoreCase(dateFrom)) {
                    rs = st.executeQuery("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL ,(to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy'))  AS DAYS FROM CERTS_AND_PRI_KEY WHERE NOT_BEFORE > to_date('" + date + "','mm-dd-yyyy') AND NOT_AFTER < to_date('" + dateto + "','mm-dd-yyyy')   ORDER BY DAYS");
                    // System.out.println("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL ,(to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy'))  AS DAYS FROM CERTS_AND_PRI_KEY WHERE NOT_BEFORE > to_date('" + date + "','mm-dd-yyyy') AND NOT_AFTER < to_date('" + dateto + "','mm-dd-yyyy')   ORDER BY DAYS");;
                } else {
                    rs = st.executeQuery("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL ,(to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy'))  AS DAYS FROM CERTS_AND_PRI_KEY   ORDER BY DAYS");
                    // System.out.println("SELECT NAME AS CERTIFICATE_NAME,NOT_BEFORE as VALID_FROM , NOT_AFTER as VALID_TILL ,(to_date (NOT_AFTER,'dd-MM-yyyy') - to_date(SYSDATE,'dd-MM-yyyy'))  AS DAYS FROM CERTS_AND_PRI_KEY   ORDER BY DAYS");
                }
            }

            ResultSetMetaData md = rs.getMetaData();
            int columncount = md.getColumnCount();
            LinkedHashMap map;

            while (rs.next()) {
                map = new LinkedHashMap();
                System.out.println("map = " + map);
                for (int i = 1; i <= columncount; i++) {

                    map.put(md.getColumnName(i), rs.getObject(i));
                }
                al.add(map);
                System.out.println("al**" + al);
            }

        } catch (SQLException s) {

            s.printStackTrace();

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return al;

    }

    @Override
    public List doCodeListItems(String selectedName) throws ServiceLocatorException {
        System.out.println("in getListName");
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String queryString = null;
        connection = ConnectionProvider.getInstance().getOracleConnection();
        List codeList = new ArrayList();
        CodeListBean codeListBean = null;
        String listName = null;
        System.out.println("before try");
        try {
            //queryString = "SELECT  * FROM CODELIST_XREF_ITEM WHERE  max(LIST_VERSION) AND LIST_NAME='" + selectedName + "'";
            queryString = "SELECT * FROM CODELIST_XREF_ITEM WHERE LIST_NAME='" + selectedName + "' AND LIST_VERSION=(SELECT DEFAULT_VERSION from CODELIST_XREF_VERS where LIST_NAME='" + selectedName + "' )";
            preparedStatement = connection.prepareStatement(queryString);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                codeListBean = new CodeListBean();
                
                codeListBean.setListName(resultSet.getString("LIST_NAME"));
                codeListBean.setSender_id(resultSet.getString("SENDER_ID"));
                codeListBean.setReceiver_id(resultSet.getString("RECEIVER_ID"));
                codeListBean.setList_version(resultSet.getString("LIST_VERSION"));
                codeListBean.setSender_item(resultSet.getString("SENDER_ITEM"));
                codeListBean.setReceiver_item(resultSet.getString("RECEIVER_ITEM"));
                System.out.println("resultSet.getString(\"TEXT1\") is "+resultSet.getString("TEXT1"));
                if (resultSet.getString("TEXT1") != null && !"".equalsIgnoreCase(resultSet.getString("TEXT1"))) {
                //if (!"".equalsIgnoreCase(resultSet.getString("TEXT1")) && resultSet.getString("TEXT1") != null) {
                    codeListBean.setText1(resultSet.getString("TEXT1"));
                } else {
                    System.out.println("else");
                    codeListBean.setText1("");
                }
                if (resultSet.getString("TEXT2")!=null &&  !"".equalsIgnoreCase(resultSet.getString("TEXT2"))) {
                    System.out.println("in if");
                //if (!"".equalsIgnoreCase(resultSet.getString("TEXT2")) && resultSet.getString("TEXT2") != null) {
                codeListBean.setText2(resultSet.getString("TEXT2"));
                } else {
                     System.out.println("else");
                    codeListBean.setText2("");
                }
                if (resultSet.getString("TEXT3") != null && !"".equalsIgnoreCase(resultSet.getString("TEXT3"))) {
                //if (!"".equalsIgnoreCase(resultSet.getString("TEXT3")) && resultSet.getString("TEXT3") != null) {
                codeListBean.setText3(resultSet.getString("TEXT3"));
                } else {
                    codeListBean.setText3("");
                }
                if (!"".equalsIgnoreCase(resultSet.getString("TEXT4")) && resultSet.getString("TEXT4") != null) {
                codeListBean.setText4(resultSet.getString("TEXT4"));
                } else {
                    codeListBean.setText4("");
                }
                codeListBean.setDescription(resultSet.getString("DESCRIPTION"));
                if (!"".equalsIgnoreCase(resultSet.getString("TEXT5")) && resultSet.getString("TEXT5") != null) {
                codeListBean.setText5(resultSet.getString("TEXT5"));
                } else {
                    codeListBean.setText5("");
                }
                if (!"".equalsIgnoreCase(resultSet.getString("TEXT6")) && resultSet.getString("TEXT6") != null) {
                codeListBean.setText6(resultSet.getString("TEXT6"));
                } else {
                    codeListBean.setText6("");
                }
                if (!"".equalsIgnoreCase(resultSet.getString("TEXT7")) && resultSet.getString("TEXT7") != null) {
                codeListBean.setText7(resultSet.getString("TEXT7"));
                } else {
                    codeListBean.setText7("");
                }
                if (!"".equalsIgnoreCase(resultSet.getString("TEXT8")) && resultSet.getString("TEXT8") != null) {
                codeListBean.setText8(resultSet.getString("TEXT8"));
                } else {
                    codeListBean.setText8("");
                }
                if (!"".equalsIgnoreCase(resultSet.getString("TEXT9")) && resultSet.getString("TEXT9") != null) {
                codeListBean.setText9(resultSet.getString("TEXT9"));
                } else {
                    codeListBean.setText9("");
                }
                codeList.add(codeListBean);

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
        return codeList;
    }

    @Override
    public List getCodeListNames(String selectedName) throws ServiceLocatorException {
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String queryString = null;
        connection = ConnectionProvider.getInstance().getOracleConnection();
        List listNameMap = new ArrayList();
        String listName = null;
        try {
            //queryString = "SELECT  DISTINCT(LIST_NAME) FROM CODELIST_XREF_ITEM WHERE UPPER(LIST_NAME) LIKE '" + selectedName.toUpperCase() + "%'";
            queryString = "SELECT  DISTINCT(LIST_NAME) FROM CODELIST_XREF_ITEM WHERE UPPER(LIST_NAME) LIKE '%" + selectedName.trim().toUpperCase() + "%'";
            System.out.println("queryString----" + queryString);
            preparedStatement = connection.prepareStatement(queryString);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                listNameMap.add(resultSet.getString("LIST_NAME"));
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
        return listNameMap;
    }

   

}
