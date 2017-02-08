<!DOCTYPE HTML>
<%@page import="com.mss.ediscv.logisticsshipment.LtShipmentBean"%>

<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>


<%@ taglib uri="/WEB-INF/tlds/dbgrid.tld" prefix="grd"%>
<%@ page import="com.freeware.gridtag.*"%>
<%@page import="java.sql.Connection"%>
<%@  page import="com.mss.ediscv.util.AppConstants"%>
<%@ page import="com.mss.ediscv.util.ConnectionProvider"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import = "java.util.ResourceBundle" %>

<html>
    <head>
        <title>Miracle Supply Chain Visibility Portal</title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />

        <%-- <meta name="description" content="website description" />
         <meta name="keywords" content="website keywords, website keywords" />
         <meta http-equiv="content-type" content="text/html; charset=UTF-8" />  --%>

        <style>
            /*            .content-wrapper{
                            min-height: 552px !important;
                        }*/
            .ltship_ftr{
                position: absolute;
                width: 83%;
                z-index:-1 !important;
            }
        </style>

        <script>
            function doOnLoad() {
                $("#ltsupplychain").addClass("active");
                $("#ltshipment").addClass("active");
                $("#logistics").addClass("active");
                $("#ltshipment i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
        </script>

        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>'/> 
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>

        <script type="text/javascript">

            /* close button script*/
            $(document).ready(function (c) {
                $('.alert-close').on('click', function (c) {
                    $('.message').fadeOut('slow', function (c) {
                        $('.message').remove();
                    });
                });
            });
        </script>
        <script>
            function hide()
            {

                $('#hide-menu1').removeClass('show-menu');
            }
//            $('body,html').click(function(e){
//                $('#hide-menu1').removeClass('show-menu');
//            });

        </script>
    </head>
    <%
        String check = null;
        if (request.getAttribute("check") != null) {
            check = request.getAttribute("check").toString();
        }

    %>
    <%--<body onload="doOnLoad();initDate('shpdatepickerfrom','shpdatepicker','<%=check %>');setStyle('mainShipment','shipmentCurrId');">  --%>
    <body onload="doOnLoad();
            check();" class="hold-transition skin-blue sidebar-mini"> 
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>  
        <script type="text/javascript">
        function check()
        {

            var value1 = document.getElementById("corrattribute1").value;

            if (value1 != "-1")
                document.getElementById("corr").style.display = "block";
            else
                document.getElementById("corr").style.display = "none";
                 }
        </script>


        <div>
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>
        <div class="content-wrapper">
            <!-- Content Header (Page header) -->


            <!-- Main content --> 

            <section class="content-header">
                <h1>
                    Shipment
                    <small>Logistics</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                    <li class="active">Shipment</li>
                </ol>
            </section>
            <br>
            <div id="loadingAcoountSearch" class="loadingImg">
                <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
            </div>
            <section class="content">

                <div class="box box-primary">
                    <!--                    <div class="box-header with-border">
                                            <h3 class="box-title">Shipment</h3>
                                            <div class="box-tools pull-right">
                    
                                            </div>
                                        </div>  -->
                    <div class="box-body">
                        <div id="text">

                            <div  style="alignment-adjust:central;" >
                                <%String contextPath = request.getContextPath();
                                %>

                                <s:form action="../logisticsshipment/ltShipmentSearch.action" method="post" name="ltshipmentForm" id="ltshipmentForm" theme="simple">
                                    <s:hidden id="datepickerfrom" name="datepickerfrom" />
                                    <s:hidden id="datepickerTo" name="datepickerTo"/>
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="row">
                                                    <div class="col-sm-3"> <label>Date range</label>
                                                        <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left"   value="%{reportrange}" onchange="Date1();" /> 
                                                    </div>

                                                    <script type="text/javascript">
                                                        function Date1() {
                                                            var date = document.ltshipmentForm.reportrange.value;
                                                            var arr = date.split("-");
                                                            var x = arr[1].trim();
                                                            document.getElementById("datepickerfrom").value = arr[0];
                                                            document.getElementById("datepickerTo").value = x;

                                                        }
                                                    </script>

                                                    <div  class="col-sm-3">
                                                        <label>Document Type</label> 
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="docTypeList" name="docType" id="docType" value="%{docType}" />
                                                    </div>


                                                    <div  class="col-sm-3">
                                                        <label>Sender Id</label>  
                                                        <s:textfield  cssClass="form-control" name="senderId" id="senderId" value="%{senderId}"/>
                                                    </div>

                                                    <div  class="col-sm-3">
                                                        <label>Sender Name</label>  
                                                        <s:textfield cssClass="form-control"  name="senderName" id="senderName" value="%{senderName}"  />
                                                    </div>


                                                </div>
                                                <br>
                                                <div class="row">
                                                    <div class="col-sm-3">
                                                        <label>Receiver Id</label>
                                                        <s:textfield  cssClass="form-control"  name="receiverId" id="receiverId" value="%{receiverId}"  />
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label>Receiver Name</label>
                                                        <s:textfield  cssClass="form-control"  name="receiverName" id="receiverName" value="%{receiverName}"  />
                                                    </div>
                                                    <!--                                                        <div class="col-sm-3">
                                                                                                                <label for="ackStatus">Ack Status</label>
                                                    <%--<s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="{'Overdue','Accepted','Rejected'}" name="ackStatus" id="ackStatus" value="%{ackStatus}"  /> --%>
                                                </div>-->
                                                    <div class="col-sm-3">
                                                        <label for="status">Status</label>
                                                        <s:select headerKey="-1" headerValue="Select Type" cssClass="form-control" list="{'Success','Error','Warning'}" name="status" id="status" value="%{status}"  /> 
                                                    </div>
                                                </div>
                                                <br>
                                                <div class="row">
                                                    <div class="col-sm-3">
                                                        <label for="corrattribute">Correlation</label>
                                                        <s:select headerKey="-1" headerValue="Select Attribute" cssClass="form-control" list="correlationList" name="corrattribute" id="corrattribute" value="%{corrattribute}"  />
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label for="corrvalue">Value</label>
                                                        <s:textfield cssClass="form-control" name="corrvalue" id="corrvalue" value="%{corrvalue}" />
                                                    </div>
                                                    <div class="col-sm-3"><br>
                                                        <button  type="button" id="addButton" name="addButton" value="Add Div" class="btn btn-success"   style="margin-top:6px ;" ><i class="fa fa-plus"></i></button>
                                                        &nbsp; <label>Add Filter</label>
                                                    </div>
                                                </div>
                                                <script>

                                                </script>                                      

                                                <script>
                                                    var count = 0;
                                                </script>                                          

                                                <script>
                                                    $("#addButton").click(function () {
                                                        count++;
                                                        if (count == 1)
                                                            document.getElementById("corr").style.display = "block";
                                                        
                                                        else
                                                            alert('Limit exceded.... cant add more fields');
                                                    })

                                                </script>
                                                <div id="corr" style="display: none">
                                                    <br>   <div class="row">
                                                        <div class="col-sm-3">
                                                            <label for="corrattribute1">Correlation</label>
                                                            <s:select headerKey="-1" headerValue="Select Attribute" cssClass="form-control" list="correlationList" name="corrattribute1" id="corrattribute1" value="%{corrattribute1}"/>
                                                        </div>
                                                        <div class="col-sm-3">
                                                            <label for="corrvalue1">Value</label>
                                                            <s:textfield cssClass="form-control" name="corrvalue1" id="corrvalue1" value="%{corrvalue1}" />
                                                        </div>

                                                    </div>
                                                </div>
                                              
                                            </div>
                                        </div>
                                        <br>
                                        <span id="span1">
                                        </span>



                                        <div class="row">






                                            <div class="col-sm-2"><s:submit value="Search"  onclick="return checkCorrelation();" cssClass="btn btn-primary col-sm-12" tabindex="16"/></div>

                                            <div class="col-sm-2"><strong><input type="button" value="Reset"  tabindex="17" class="btn btn-primary col-sm-12" onclick="return resetvaluesLtShipment();"/></strong></div>

                                            <s:hidden name="sampleValue" id="sampleValue" value="2"/>

                                        </s:form>





                                    </div>
                                </div>
                            </div></div>
                    </div></div>
            </section>
            <div id="gridDiv">     
                <s:if test="#session.ltShipmentList != null">
                    <%!String cssValue = "whiteStripe";
                        int resultsetTotal;%>
                    <section class="content">



                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box">
                                    <div class="box-header">
                                        <h3 class="box-title">Table</h3>
                                    </div><!-- /.box-header -->
                                    <div class="box-body">
                                        <%--<table  border="0" cellspacing="0" cellpadding="0" bgcolor="#FFFFFF">
                                            <tr align="center">
                                                <td>     --%>

                                        <div style="overflow-x:auto;">      

                                            <table align="left" width="100%"
                                                   border="0" cellpadding="0" cellspacing="0" >
                                                <tr>
                                                    <td style="background-color: white;">
                                                        <!--<div style="overflow-x:auto;">--> 
                                                        <table id="results"  class="table table-bordered table-hover">
                                                            <%
                                                                java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_LTSHIPMENT_LIST);

                                                                if (list.size() != 0) {

                                                                    LtShipmentBean ltShipmentBean;
                                                            %>
                                                            <thead><tr>
                                                                    <th>InstanceId</th>
                                                                    <th >Shipment #</th>
                                                                    <th >Partner</th>  
                                                                    <th >Carrier Status</th>  
                                                                        <%-- <th>Ship Date</th> --%>
                                                                    <th>DateTime</th>
                                                                        <%--<th>ISA #</th> --%>
                                                                        <%-- <th>GS #</th>  --%>
                                                                        <%-- <th>ST #</th> --%>
                                                                    <th>Direction</th>
                                                                    <th>Status</th>

                                                                    <%--<td >PO #</td> --%>




                                                                </tr>
                                                            </thead>
                                                            <tbody>
                                                                <tr>


                                                                    <%
                                                                        for (int i = 0; i < list.size(); i++) {
                                                                            ltShipmentBean = (LtShipmentBean) list.get(i);

                                                                            if (i % 2 == 0) {
                                                                                cssValue = "whiteStripe";
                                                                            } else {
                                                                                cssValue = "grayEditSelection";
                                                                            }
                                                                    %>

                                                                    <%-- <td><a href="#" onclick="return demo();" > --%>
                                                                    <td>
                                                                        <%
                                                                            if (ltShipmentBean.getInstanceId() != null) {
                                                                                out.println(ltShipmentBean.getInstanceId());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td>  <a href="javascript:getDetails('<%=ltShipmentBean.getAsnNum()%>','<%=ltShipmentBean.getPoNum()%>','<%=ltShipmentBean.getId()%>');">
                                                                            <%
                                                                                if (ltShipmentBean.getAsnNum() != null) {
                                                                                    out.println(ltShipmentBean.getAsnNum());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }
                                                                            %>
                                                                        </a> 
                                                                    </td>

                                                                    <td>
                                                                        <%
                                                                            if (ltShipmentBean.getPartner() != null) {
                                                                                out.println(ltShipmentBean.getPartner());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (ltShipmentBean.getCarrierStatus() != null) {
                                                                                if (ltShipmentBean.getCarrierStatus().equalsIgnoreCase("AA")) {
                                                                                    out.println(ltShipmentBean.getCarrierStatus() + "_pick_up appointment");
                                                                                } else if (ltShipmentBean.getCarrierStatus().equalsIgnoreCase("AB")) {
                                                                                    out.println(ltShipmentBean.getCarrierStatus() + "_Delivery appointment");
                                                                                } else if (ltShipmentBean.getCarrierStatus().equalsIgnoreCase("X3")) {
                                                                                    out.println(ltShipmentBean.getCarrierStatus() + "_Arrived at Pick_up Location");
                                                                                } else if (ltShipmentBean.getCarrierStatus().equalsIgnoreCase("AF")) {
                                                                                    out.println(ltShipmentBean.getCarrierStatus() + "_Departed from pick_up Location");
                                                                                } else if (ltShipmentBean.getCarrierStatus().equalsIgnoreCase("X1")) {
                                                                                    out.println(ltShipmentBean.getCarrierStatus() + "_Arrived at Delivery Location");
                                                                                } else if (ltShipmentBean.getCarrierStatus().equalsIgnoreCase("D1")) {
                                                                                    out.println(ltShipmentBean.getCarrierStatus() + "_Completed Unloading Delivery Location");
                                                                                } else if (ltShipmentBean.getCarrierStatus().equalsIgnoreCase("CD")) {
                                                                                    out.println(ltShipmentBean.getCarrierStatus() + "_Carrier Departed Delivery Location");
                                                                                }
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (ltShipmentBean.getDateTime() != null) {
                                                                                out.println(ltShipmentBean.getDateTime().toString().substring(0, ltShipmentBean.getDateTime().toString().lastIndexOf(":")));
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (ltShipmentBean.getDirection() != null) {
                                                                                out.println(ltShipmentBean.getDirection().toUpperCase());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (ltShipmentBean.getStatus() != null) {
                                                                                if (ltShipmentBean.getStatus().equalsIgnoreCase("ERROR")) {
                                                                                    out.println("<font color='red'>" + ltShipmentBean.getStatus().toUpperCase() + "</font>");
                                                                                } else if (ltShipmentBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                                    out.println("<font color='green'>" + ltShipmentBean.getStatus().toUpperCase() + "</font>");
                                                                                } else {
                                                                                    out.println("<font color='orange'>" + ltShipmentBean.getStatus().toUpperCase() + "</font>");
                                                                                }
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                    </td>

                                                                </tr>
                                                                <%
                                                                    }
                                                                } else {
                                                                %>
                                                                <tr><td>
                                                                        <%
                                                                                out.println("<img  border='0' align='top'  src='" + contextPath + "/includes/images/alert.gif'/><b>No records found for the given search criteria. Please try a different search criteria!</b>");
                                                                            }

                                                                        %>
                                                                    </td>
                                                                </tr>
                                                        </table></div>
                                                    </td>
                                                </tr>
                                                <%                                                    if (list.size() != 0) {

                                                %>
                                                <tr>
                                                    <!--                                                                        <td align="right" colspan="28" style="background-color: white;">
                                                                                                                                <div align="right" id="pageNavPosition"></div>-->
                                                    </td>
                                                </tr>
                                                </tbody>
                                                <%}%>

                                            </table>
                                        </div>




                                        <%-- Process butttons  start --%>
                                        <%
                                            if (list.size() != 0) {
                                        %><br>
                                        <table align="right">
                                            <tr>
                                                <td style="background-color: white;">
                                                    <strong><input type="button" value="Generate Excel" class="btn btn-effect-ripple btn-primary" onclick="return gridDownload('ltShipment', 'xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/></strong>
                                                </td>
                                            </tr>
                                        </table> 
                                        <%}%> </div> 
                                </div>
                            </div></div>
                    </section>


                </s:if> 
            </div>
            <%-- process buttons end--%>

            <%-- detail info starts --%>

            <div id="hide-menu1" class="hide-menu message ">

                <div class="row col-sm-12">

                    <br>
                    <div class="col-sm-6"> <label class="labelw"> Instance Id </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="LSInstanceId" name="LSInstanceId" readonly="true" />
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> Shipment # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="LSAsnnumber" name="LSAsnnumber" readonly="true" />
                    </div>
                </div>
                <div class="row col-sm-12"> <br>
                    <div class="col-sm-6"> <label class="labelw"> PO # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="LSPo" name="LSPo" readonly="true" />
                    </div>
                    <div class="col-sm-6"> <label class="labelw">Item Qty</label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="LSItemqty" name="LSItemqty" readonly="true"/>
                    </div>
                </div>
                <div class="row col-sm-12">
                    <div class="col-sm-6"> <label class="labelw"> Shipment Volume </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="LSAsnvolume" name="LSAsnvolume" readonly="true" />
                    </div>
                    <div class="col-sm-6"> <label class="labelw">IS A Number</label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="LSIsANumber" name="LSIsANumber" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">IS A Date</label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="LSIsADate" name="LSIsADate" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw">IS A Time</label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="LSIsATime" name="LSIsATime" readonly="true"/>
                    </div>
                </div>
                <br>
                <div id="senderinfo">
                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <h4>Sender Info :</h4></div>
                        <div class="col-sm-6"></div>
                        <div class="col-sm-6"></div>

                    </div>
                    <br>
                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <label class="labelw">  Id </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="LSSenderId" name="LSSenderId" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw"> Name </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="LSSenderName" name="LSSenderName" readonly="true"/>
                        </div>
                    </div>
                </div>
                <br>
                <div id="receiverinfo">
                    <div class="row col-sm-12">
                        <div class="col-sm-6"> <h4>Receiver Info:</h4></div>
                        <div class="col-sm-6"></div>
                        <div class="col-sm-6"></div>
                    </div>

                    <br>
                    <div class="row col-sm-12 clear">
                        <div class="col-sm-6"> <label class="labelw">  Id </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="LSReceiverId" name="LSReceiverId" readonly="true"/>
                        </div>
                        <div class="col-sm-6"> <label class="labelw"> Name </label>
                            <s:textfield cssClass="form-control"  required="required" placeholder="" id="LSReceiverName" name="LSReceiverName" readonly="true" />
                        </div>
                    </div>
                </div>
                <div class="row col-sm-12 clear">

                    <div class="col-sm-6"> <label class="labelw"> GS </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="LSGs" name="LSGs" readonly="true"/>
                    </div>

                    <div class="col-sm-6"> <label class="labelw">  ST </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="LSSt" name="LSSt" readonly="true"/>
                    </div>

                    <div class="col-sm-6"> <label class="labelw"> Transaction # </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="LSTransactionType" name="LSTransactionType" readonly="true"/>
                    </div>

                    <div class="col-sm-6"> <label class="labelw"> STATUS </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="LSDetailInfoStatus" name="LSDetailInfoStatus" readonly="true"/>
                    </div>
                </div>

                <div class="row col-sm-12" >
                    <div class="col-sm-6"> <label class="labelw">  Pre Transition  </label></div>
                    <div class="col-sm-6">  <div id="LSPreTranslation"></div>
                    </div>
                    <div class="row col-sm-12" >
                        <div class="col-sm-6"> <label class="labelw"> Post Transition </label></div>
                        <div class="col-sm-6"><div id="LSPostTranslation"></div>
                        </div></div>
                    <div class="row col-sm-12" >
                        <div class="col-sm-6"> <label class="labelw"> Org File Path </label></div>
                        <div class="col-sm-6">  <div id="LSOrgFilePath"></div>
                        </div></div>
                    <div class="row col-sm-12" >
                        <div class="col-sm-6"> <label class="labelw"> 997 ACK File </label></div>
                        <div class="col-sm-6">   <div id="LSAckFileid"></div>
                        </div></div>
                    <br><br><br><br><br><br>
                </div>
                <div class="row col-sm-12" >
                    <div class="col-sm-6"> <label class="labelw"> Error Message </label></div>
                    <div id="LSErrorMessage"></div>
                </div>
                <div id="noresult"></div>

                <div class="col-sm-12"> <button type="button" class="btn btn-primary col-sm-11" id="hide-menu" onclick="hide()" style="margin-left:12px;" value="X">Close</button></div>
            </div>    </div>


        <%-- detail info ends --%>




        <script>
            $(function () {
                // $("#example1").DataTable();
                $('#results').DataTable({
                    "paging": true,
                    "lengthChange": true,
                    "searching": true,
                    "ordering": true,
                    "info": true,
                    "autoWidth": false
                });
            });
        </script>
        <%--  <div id="footer">
                <h2><font color="white">&#169 2013 Miracle Software Systems, Inc. All rights reserved</font></h2>
                       </div> --%>
    </div>       
    <div>
        <s:include value="../includes/template/footer.jsp"/>
    </div>


    <script>
        $('input[name="daterange"]').daterangepicker();
    </script>

    <script language="JavaScript"  src='<s:url value="/includes/js/DateValidation.js"/>'></script>
    <script language="JavaScript"  src='<s:url value="/includes/js/generalValidations.js"/>'></script>
    <script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
    <script language="JavaScript"  src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
    <script type="text/javascript" src='<s:url value="../includes/js/DateValidation.js"/>'></script>
    <script type="text/javascript" src='<s:url value="../includes/js/Correlation.js"/>'></script>
    <script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
    <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
    <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
    <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>

    <script type="text/javascript">
        function getDetails(val, ponum, id)
        {
            getLogisticsShipmentDetails(val, ponum, id);
        }
        function resetvaluesLtShipment()
        {
            document.getElementById('datepickerfrom').value = "";
            document.getElementById('datepickerTo').value = "";
            document.getElementById('docType').value = "-1";
            document.getElementById('senderId').value = "";
            document.getElementById('senderName').value = "";
            document.getElementById('receiverId').value = "";
            document.getElementById('receiverName').value = "";
            document.getElementById('status').value = "-1";
            document.getElementById('corrattribute').value = "-1";
            document.getElementById('corrvalue').value = "";
            document.getElementById('corrattribute1').value = "-1";
            document.getElementById('corrvalue1').value = "";
            document.getElementById('corrattribute2').value = "-1";
            document.getElementById('corrvalue2').value = "";
            document.getElementById('reportrange').value = "";
            //  document.getElementById('gridDiv').style.display = 'none'; 
            $('#gridDiv').hide();
        }

        function checkCorrelation() {

            //   alert("hiii");
            var corrattr = document.getElementById('corrattribute').value;
            var corrval = document.getElementById('corrvalue').value;

            var corrattr1 = document.getElementById('corrattribute1').value;
            var corrval1 = document.getElementById('corrvalue1').value;



            if ((corrattr != "-1") && (corrval == "")) {
                alert("please enter Correlation Value!!!");
                return false;
            }
            if ((corrattr == "-1") && (corrval != "")) {
                alert("please select Correlation!");
                return false;
            }

            if ((corrattr1 != "-1") && (corrval1 == "")) {
                alert("please enter Correlation Value!!!");
                return false;
            }
            if ((corrattr1 == "-1") && (corrval1 != "")) {
                alert("please select Correlation!");
                return false;
            }

        }

    </script>

</body>


</html>
