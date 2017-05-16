<%@page import="com.mss.ediscv.logisticreports.LogisticReportsBean"%>
<%@page import="com.mss.ediscv.reports.ReportsBean"%>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page buffer="50kb" autoFlush="true" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%@page import="com.mss.ediscv.doc.DocRepositoryBean"%>
<%@ taglib uri="/WEB-INF/tlds/dbgrid.tld" prefix="grd"%>
<%@ page import="com.freeware.gridtag.*"%>
<%@page import="java.sql.Connection"%>
<%@  page import="com.mss.ediscv.util.AppConstants"%>
<%@ page import="com.mss.ediscv.util.ConnectionProvider"%>
<%@ page import="java.sql.SQLException"%>
<%@ page import = "java.util.ResourceBundle" %>
<%--<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>--%>
<%@page buffer="50kb" autoFlush="true" %>

<!DOCTYPE html>
<html class=" js canvas canvastext geolocation crosswindowmessaging no-websqldatabase indexeddb hashchange historymanagement draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms no-csstransforms3d csstransitions  video audio localstorage sessionstorage webworkers applicationcache svg smil svgclippaths   fontface">
    <head>
        <title>Miracle Supply Chain Visibility portal</title>
        <meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
        <meta http-equiv="pragma" content="no-cache" />
        <meta http-equiv="cache-control" content="no-cache" />
        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css"> 
        <script language="JavaScript"  src='<s:url value="/includes/js/jquery-1.9.1.js"></s:url>'></script>
        <%--   <script language="JavaScript"
        src='<s:url value="/includes/js/generalValidations.js"/>'></script>  --%>
        <script>
            var myCalendar;
            function doOnLoad() {

                $("#ltreports").addClass("active");
                $("#ltexcelreports").addClass("active");
                $("#logistics").addClass("active");
                $("#ltexcelreports i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";

            }
        </script>
        <script type="text/javascript">
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

            // New function to show the left grid

            function demo() {
                $(function () {

                    $('#detail_box').show();
                    return false;
                });

            }

            function getDetails(val, ponum) {
                //  alert("hiiii");    
                getLogisticsDocDetails(val, ponum);
            }
            //            function checkDates()
            //            {
            //    
            //                var docdatepickerfrom = document.getElementById('docdatepickerfrom').value;
            //                var docdatepicker = document.getElementById('docdatepicker').value;
            //                if(docdatepickerfrom == "" && docdatepicker == ""){
            //                    alert("Please enter Date From and Date To");
            //                    return false;
            //                }
            //                var res = compareDates(docdatepickerfrom,docdatepicker);
            //     
            //                return res;
            //            }
            function resetvalues()
            {
                  $('.myRadio').attr('checked', false);
                document.getElementById('docdatepickerfrom').value = "";
                document.getElementById('docdatepicker').value = "";
                document.getElementById('docSenderId').value = "";
                document.getElementById('docSenderName').value = "";
                document.getElementById('docBusId').value = "";
                document.getElementById('docRecName').value = "";

                document.getElementById('docType').value = "-1";

                document.getElementById('status').value = "-1";
                document.getElementById('reportrange').value = "";

                //$('#detail_box').hide();
                $('#gridDiv').hide();

            }

        </script>
        <script>
                $(function () {
                    //   $("#example1").DataTable();
                    $('#results').DataTable({
                        "paging": true,
                        "lengthChange": true,
                        "searching": true,
                        "ordering": true,
                        "info": true,
                        "autoWidth": true,
                        order: [[0, 'desc']]
                    });
                });
            </script>




    </head>
    <%
        String check = null;
        if (request.getAttribute("check") != null) {
            check = request.getAttribute("check").toString();
        }

        //System.out.println("check-->"+check);
    %>
    <body onload="doOnLoad();
            check();" class="hold-transition skin-blue sidebar-mini" >
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>
        <div>
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>
        
        <div class="content-wrapper">
            
            <section class="content-header">
                <h1>
                    Excel Reports
                    <!--<small>Logistics</small>-->
                </h1>
<!--                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                    <li class="active">Excel Reports</li>
                </ol>-->
            </section>

            <section class="content">

                <div class="box box-primary">
                    <div class="box-header with-border">

                        <div class="box-tools pull-right">

                        </div>
                    </div>  
                    <div class="box-body">
                        <div id="text">
                            <div  style="alignment-adjust:central;" >
                                <%String contextPath = request.getContextPath();
                                %>


                                <s:form action="../logisticsReports/logisticreportsSearch.action" method="post" name="documentForm" id="documentForm" theme="simple">
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="row">
                                                    <div class="col-sm-3"><label>Database&nbsp;&nbsp;&nbsp;&nbsp;:&nbsp;&nbsp;</label>
                                                        <s:radio cssClass="myRadio" id="database" name="database" value="%{database}" list="#@java.util.LinkedHashMap@{'MSCVP':'LIVE','ARCHIVE':'ARCHIVE'}"/>
                                                    </div>
                                                </div>
                                                <div class="row">
                                                    <div class="col-sm-3"> <label>Date Range</label>
                                                        <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left"   value="%{reportrange}" tabindex="1" /> 
                                                    </div>

                                                    <script type="text/javascript">
        function Date1()
        {
            var date = document.documentForm.reportrange.value;
            var arr = date.split("-");
            var x = arr[1].trim();
            document.getElementById("docdatepickerfrom").value = arr[0];
            document.getElementById("docdatepicker").value = x;

        }
                                                    </script>


                                                    <s:hidden id="docdatepickerfrom" name="docdatepickerfrom" />
                                                    <s:hidden id="docdatepicker" name="docdatepicker"/>
                                                    <div  class="col-sm-3">
                                                        <label>Document Type</label>  
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="docTypeList" name="docType" id="docType" value="%{docType}" tabindex="2" />
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label>Sender Id</label>
                                                        <s:textfield name="docSenderId"  id="docSenderId" cssClass="form-control"   value="%{docSenderId}" tabindex="3" /> 
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label>Sender Name</label>
                                                        <s:textfield name="docSenderName"  id="docSenderName" cssClass="form-control"   value="%{docSenderName}" tabindex="4" /> 
                                                    </div>
                                                </div>
                                                <br>
                                                <div class="row">
                                                    <div class="col-sm-3">
                                                        <label>Receiver Id</label>
                                                        <s:textfield name="docBusId"  id="docBusId" cssClass="form-control"   value="%{docBusId}" tabindex="5" /> 
                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label>Receiver Name</label>
                                                        <s:textfield name="docRecName"  id="docRecName" cssClass="form-control"   value="%{docRecName}" tabindex="6" /> 
                                                    </div> 

                                                    <div class="col-sm-3">
                                                        <label>Status</label>
                                                        <s:select headerKey="-1" headerValue="Select Type" list="{'Success','Error','Warning'}" name="status" id="status" value="%{status}" tabindex="7"  cssClass="form-control"/> 
                                                    </div> 
                                                </div>
                                                <br>
                                                <div class="row">
                                                    <div class="col-sm-2"> <s:submit value="Search" cssClass="btn btn-primary col-sm-12" onclick="Date1()" tabindex="8"/></div>
                                                    <div class="col-sm-2"><strong><input type="button" value="Reset" class="btn btn-primary col-sm-12" tabindex="9" onclick="return resetvalues();"/></strong></div>


                                                    <div id="loadingAcoountSearch" class="loadingImg">
                                                        <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                    </div>


                                                    <s:hidden name="sampleValue" id="sampleValue" value="2"/>

                                                </s:form>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div></div></div>

            </section>
            <div id="gridDiv">

                <s:if test="#session.logdocumentList != null"> 
                    <%--- GRid start --%>

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
                                        <div style="overflow-x:auto;">                 

                                            <table align="left" width="100%"
                                                   border="0" cellpadding="0" cellspacing="0">
                                                <tr>
                                                    <td style="background-color: white;">

                                                        <table  id="results" class="table table-bordered table-hover">
                                                            <%
                                                                java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_LOG_DOC_LIST);

                                                                if (list.size() != 0) {
                                                                    LogisticReportsBean logisticsReportBean;
                                                            %>
                                                            <thead> <tr>
                                                                    <th>DateTime</th>
                                                                    <th>FileFormat</th> 
                                                                    <th>InstanceId</th>
                                                                    <th>Partner</th>
                                                                    
                                                                    <th>TransType</th>
                                                                    <th>Direction</th>
                                                                    <th >Status</th>
                                                                    <th>Reprocess</th>
                                                                    <th>FileName </th>
                                                                </tr></thead>
                                                            <tbody>


                                                                <%
                                                                    for (int i = 0; i < list.size(); i++) {
                                                                        logisticsReportBean = (LogisticReportsBean) list.get(i);

                                                                        if (i % 2 == 0) {
                                                                            cssValue = "whiteStripe";
                                                                        } else {
                                                                            cssValue = "grayEditSelection";
                                                                        }
                                                                %>
                                                                <tr>   
                                                                    <td>
                                                                        <%                                                                            
                                                                            if (logisticsReportBean.getDate_time_rec() != null && !"".equals(logisticsReportBean.getDate_time_rec())) {
                                                                                out.println(logisticsReportBean.getDate_time_rec().toString().substring(0, logisticsReportBean.getDate_time_rec().toString().lastIndexOf(":")));
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (logisticsReportBean.getFile_type() != null && !"".equals(logisticsReportBean.getFile_type())) {
                                                                                out.println(logisticsReportBean.getFile_type());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>
                                                                    <td><%--<a href="javascript:getDetails('<%=logisticsReportBean.getFile_id()%>','<%=logisticsReportBean.getPoNumber()%>');"> --%>
                                                                        <%
                                                                            if (logisticsReportBean.getFile_id() != null && !"".equals(logisticsReportBean.getFile_id())) {
                                                                                out.println(logisticsReportBean.getFile_id());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>
                                                                        <%-- </a> --%>
                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (logisticsReportBean.getPname() != null && !"".equals(logisticsReportBean.getPname())) {
                                                                                out.println(logisticsReportBean.getPname());
                                                                            } else {
                                                                                out.println("-");
                                                                            }


                                                                        %>

                                                                    </td>


                                                                       
                                                                    <td>
                                                                        <%
                                                                            if (logisticsReportBean.getTransaction_type() != null && !"".equals(logisticsReportBean.getTransaction_type())) {
                                                                                out.println(logisticsReportBean.getTransaction_type());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>
                                                                    <td>
                                                                        <%
                                                                            if (logisticsReportBean.getDirection() != null && !"".equals(logisticsReportBean.getDirection())) {
                                                                                out.println(logisticsReportBean.getDirection());
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>  


                                                                    <td>
                                                                        <%
                                                                            if (logisticsReportBean.getStatus() != null && !"".equals(logisticsReportBean.getStatus())) {
                                                                                if (logisticsReportBean.getStatus().equalsIgnoreCase("ERROR")) {
                                                                                    out.println("<font color='red'>" + logisticsReportBean.getStatus() + "</font>");
                                                                                } else if (logisticsReportBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                                    out.println("<font color='green'>" + logisticsReportBean.getStatus() + "</font>");
                                                                                } else {
                                                                                    out.println("<font color='orange'>" + logisticsReportBean.getStatus() + "</font>");
                                                                                }
                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>

                                                                    <td>
                                                                        <%
                                                                            //out.println(logisticsDocBean.getReProcessStatus());
                                                                            if (logisticsReportBean.getReProcessStatus() != null && !"".equals(logisticsReportBean.getReProcessStatus())) {
                                                                                out.println(logisticsReportBean.getReProcessStatus().toUpperCase());

                                                                            } else {
                                                                                out.println("-");
                                                                            }
                                                                        %>

                                                                    </td>
                                                                    <td style="word-break:break-all;">

                                                                        <%
                                                                            //out.println(logisticsDocBean.getReProcessStatus());
                                                                            if (logisticsReportBean.getFile_name() != null && !"".equals(logisticsReportBean.getFile_name())) {
                                                                                out.println(logisticsReportBean.getFile_name().toUpperCase());

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
                                                                                // String contextPath = request.getContextPath();
                                                                                // out.println("<img  border='0' align='top'  src='"+contextPath+"/includes/images/alert.gif'/><b> No Records Found to Display!</b>");
                                                                                out.println("<img  border='0' align='top'  src='" + contextPath + "/includes/images/alert.gif'/><b>No records found for the given search criteria. Please try a different search criteria!</b>");
                                                                            }

                                                                        %>
                                                                    </td>
                                                                </tr></tbody>
                                                        </table>

                                                    </td>
                                                </tr>
                                                <%                                                    if (list.size() != 0) {
                                                %>
                                                <tr >
                                                    <!--                                                                        <td align="right" colspan="28" style="background-color: white;">
                                                                                                                                <div align="right" id="pageNavPosition">hello</div>
                                                                                                                            </td>-->
                                                </tr> 
                                                <% }%>
                                            </table>
                                        </div>
                                        <%-- Process butttons  start --%>
                                        <%
                                            if (list.size() != 0) {
                                        %><br>
                                        <table align="right">
                                            <tr>
                                                <td style="background-color: white;">
                                                    <strong><input type="button" value="Generate Excel" class="btn btn-effect-ripple btn-primary" onclick="return gridDownload('logisticsReport', 'xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/></strong>
                                                </td>
                                            </tr>
                                        </table> 

                                        <%}%>
                                        <%-- process buttons end--%>
                                        <%-- Grid End --%>

                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                </s:if> 


            </div>

            


        </div>
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>


        <script>
            $('input[name="daterange"]').daterangepicker();
        </script>
        <script language="JavaScript"  src='<s:url value="/includes/js/DateValidation.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/GridNavigation.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
        <script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>




    </body>


</html>