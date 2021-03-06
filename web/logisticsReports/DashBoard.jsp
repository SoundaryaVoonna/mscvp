
<%@page import="com.mss.ediscv.reports.ReportsBean"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%-- <!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"> --%>
<!DOCTYPE html>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>

<%@page import="com.mss.ediscv.doc.DocRepositoryBean"%>

<%@page import="java.sql.Connection"%>
<%@  page import="com.mss.ediscv.util.AppConstants"%>


<html class=" js canvas canvastext geolocation crosswindowmessaging no-websqldatabase indexeddb hashchange historymanagement draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms no-csstransforms3d csstransitions  video audio localstorage sessionstorage webworkers applicationcache svg smil svgclippaths   fontface">
    <head>
        <title>Miracle Supply Chain Visibility portal</title>
        <!-- Tell the browser to be responsive to screen width -->
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css"> 
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script type="text/javascript" src="https://www.google.com/jsapi"></script>

        <script>
            google.load("visualization", "1", {packages: ["corechart"]});
            function doOnLoad() {
                $("#dashboardLM").addClass("active");
                // $("#dashboard1").addClass("active");
                $("#ltdashboard").addClass("active");
                $("#ltdashboard i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
            $(function () {
                $('#attach_box').click(function () {
                    $('#sec_box').show();
                    return false;
                });
            });
            $(function () {
                $('#detail_link').click(function () {
                    $('#detail_box').show();
                    return false;
                });
            });
            function demo() {
                $(function () {
                    $('#detail_box').show();
                    return false;
                });
            }
            function resetValues() { 
                document.getElementById('docdatepickerfrom').value = "";
                document.getElementById('docdatepicker').value = "";
                document.getElementById('docType').value = "-1";
                document.getElementById('reportrange').value = "";
                document.getElementById('status').value = "-1";
                $('#gridDiv').hide();
                $('#detail_box').hide();
            }
        </script>
        <style>
            #excels{

                margin-left: 58%;
            }
            #excel{
                float:right;
                margin-right: 19%;
            }





            .space_1{
                padding-top: 2%;
            }
            .loadingImg {
                width: 100%;
                height: 100%;
                top: 0px;
                left: 0px;
                position: fixed;
                display: block;
                opacity: 0.7;
                background-color: #9999C2;
                z-index: 99;
                text-align: center;
            }

            #LoadingContent {
                position: absolute;
                top: 50%;
                left: 50%;
                z-index: 100;
            }
            #LoadingContent > img{
                width:150px;
            }
        </style>

    </head>

    <%--<body onload="doOnLoad();initDateTime('docdatepickerfrom','docdatepicker','<%=check %>');setStyle('docRep','');">  --%>

    <body  class="hold-transition skin-blue sidebar-mini" onload="doOnLoad();
            setStyle('logisticreportsId', 'logisticdashboardCurrId');">
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>

        <div>
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>

        <!-- end Special Centered Box -->




        <div class="content-wrapper">
            <!-- Content Header (Page header) -->


            <!-- Main content --> 

            <section class="content-header">
                <%String contextPath = request.getContextPath();
                %>
                <h1>
                    Dashboard
                    <!--<small>Logistics</small>-->
                </h1>
<!--                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                    <li class="active">Dashboard</li>
                </ol>-->
            </section>

            <br>
            <section class="content">
                <div class="row">
                    <div class="col-sm-12"> 

                        <!--box-->
                        <div class="box box-primary">
                            <div class="box-header with-border">
                                <!--<h3 class="box-title">Dashboard</h3>-->
                                <div class="box-tools pull-right">

                                </div>
                            </div><!-- /.box-header -->
                            <div class="box-body">
                                <div id="text">
                                    <!-- /.col (right) -->
                                    <!-- First Row -->

                                    <s:form action="#" method="post" name="dashboardForm" id="dashboardForm" theme="simple">
                                        <div class="row">

                                            <div class="col-sm-12">
                                                <div class="row">
                                                    <div class="col-sm-3"> <label>Date Range</label>
                                                        <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left"   value="%{reportrange}" onchange="Date1()" tabindex="1"/> 
                                                    </div>
                                                    <script type="text/javascript">
        function Date1()
        {
            //   alert("in to date1");
            var date = document.dashboardForm.reportrange.value;
            // alert(date);
            var arr = date.split("-");

            var x = arr[1].trim();
            //alert("assigning");
            document.getElementById("docdatepickerfrom").value = arr[0];
            document.getElementById("docdatepicker").value = x;

            var datefrom = document.getElementById("docdatepickerfrom").value;
            //alert(datefrom);
            var dateto = document.getElementById("docdatepicker").value;
            // alert(dateto);
        }
                                                    </script>
                                                    <s:hidden id="docdatepickerfrom" name="docdatepickerfrom" />
                                                    <s:hidden id="docdatepicker" name="docdatepicker"/>

                                                    <div class="col-sm-3">  <label for="partnerMapId">Trading Partner Name </label>
                                                        <s:select  
                                                            name="partnerMapId" 
                                                            id="partnerMapId"
                                                            headerKey="ALL"
                                                            headerValue="All"
                                                            list="partnerMap" 
                                                            tabindex="2" cssClass="form-control"
                                                            value="" />

                                                    </div><!-- /.box -->
                                                </div>


                                                <br>

                                                <%--  <td class="lableLeft">Direction:</td>
                                            <td >
                                                <s:select list="{'ALL','INBOUND','OUTBOUND'}" name="direction" id="direction" value="%{direction}" tabindex="13" cssStyle="width : 150px"/>
                                                
                                            </td> --%>
                                                <div class="row">
                                                    <div class="col-sm-3"> 
                                                        <label for="docType">Document Type</label>

                                                        <%-- <s:select headerKey="-1" headerValue="Select Type" list="docTypeList" name="docType" id="docType" value="%{docType}" tabindex="13" cssStyle="width : 150px"/> --%>
                                                        <s:select headerKey="-1" headerValue="Select Type" list="docTypeList" name="docType" id="docType" value="%{docType}" tabindex="3" cssClass="form-control"/>
                                                    </div>  

                                                    <div class="col-sm-3">
                                                        <label for="status">Status</label>
                                                        <s:select headerKey="-1" headerValue="Select Type" list="{'Success','Error','Warning'}" name="status" id="status" value="%{status}" tabindex="4" cssClass="form-control"/>  
                                                    </div>
                                                </div>
                                            </div>

                                        </div>
                                    </div>
                                </div>

                                <div class="box-footer text-center">
                                    <div class="row">
                                        <%--     <s:submit value="Search" cssClass="button" tabindex="16"/> --%>
                                        <div class="col-sm-2"> <strong><input type="button" value="Generate" class="btn btn-primary col-sm-12" tabindex="5" onclick="getDashboardDeatls('L');"/></strong></div>

                                        <div class="col-sm-2"> <strong><input type="reset" value="Reset" class="btn btn-primary col-sm-12" tabindex="6" onclick="return resetValues();"/></strong></div>
                                    </div>

                                    <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                    <s:hidden name="sampleValue" id="sampleValue" value="2"/>
                                    <div id="loadingAcoountSearch" class="loadingImg">
                                        <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                    </div>
                                </div><!-- /.box-footer -->
                            </div><!-- /.box -->

                            <!--box-->
                        </s:form>

                    </div>


                </div>
            </section>
            <!--main content-->
            <!-- /.content-wrapper -->





            <section class="content">
                <div class="content grid_space" id="gridDiv"  style="display: none">
                    <div class="content_item">

<!--                        <div id="loadingImage" align="center"> <img  src="../includes/images/ajax-loader.gif" /></div>-->
                            <s:hidden id="inboundTrans" name="inboundTrans"/>
                            <s:hidden id="outboundTrans" name="outboundTrans"/>
                        <div id="tblCharts" style="display: none;overflow: hidden" >
                            <div class="row">

                                <div class="col-md-5">    
                                    <div id="inboundPiechart" style="width: 400px; height: 350px;" ></div></div>
                                <div class="col-md-5">
                                    <div id="outboundPiechart" style="width: 400px; height: 350px;" ></div></div>
                            </div>
                            <div class="col-sm-12" >
                                <div class="col-sm-10" class="space_1" style="margin-top:2%"> 
                                    <strong class="col-sm-2 pull-right"><input type="button" value="Generate Excel" class="btn btn-primary" onclick="return gridDashboardDownload('dash', 'xls', document.getElementById('inboundTrans').value, document.getElementById('outboundTrans').value);" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()"/> </strong>  

                                    <strong class="col-sm-2 pull-right"><input type="button" value="Generate PDF" class="btn btn-primary" onclick="return gridDashboardDownload('dash', 'pdf', document.getElementById('inboundTrans').value, document.getElementById('outboundTrans').value);" onmouseover="Tip('Click here to generate an pdf Report.')" onmouseout="UnTip()"  id="excel"/> </strong>  
                                </div>
                            </div>
                        </div>

                        <%-- process buttons end--%>
                        <%-- Grid End --%>

                    </div></div>
            </section>


            <%-- process buttons end--%>
            <%-- Grid End --%>

        </div>



    </div> 
    <script type="text/javascript">
        var pager = new Pager('results', 10);
        pager.init();
        pager.showPageNav('pager', 'pageNavPosition');
        pager.showPage(1);
    </script>
</div>
</div>
</div> 
<%--    <div id="footer">  --%>
<div>
    <s:include value="../includes/template/footer.jsp"/>
</div> 
<%--   	</div> --%>
<script>
    $('input[name="daterange"]').daterangepicker();
</script>
<script type="text/javascript" src='<s:url value="../includes/plugins/datepicker/daterangepicker.js"/>'></script>
<script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
<script language="JavaScript"  src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
<script type="text/javascript" src='<s:url value="../includes/js/DateValidation.js"/>'></script>
<script language="JavaScript"  src='<s:url value="/includes/js/overlay.js"/>'></script>
<script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
<script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
<!-- AdminLTE dashboard demo (This is only for demo purposes) -->
<script src='<s:url value="../includes/bootstrap/js/pages/dashboard.js"/>'></script>
</body>
</html>
