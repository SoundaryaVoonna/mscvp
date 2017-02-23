<!DOCTYPE HTML>
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


<html class=" js canvas canvastext geolocation crosswindowmessaging no-websqldatabase indexeddb hashchange historymanagement draganddrop websockets rgba hsla multiplebgs backgroundsize borderimage borderradius boxshadow opacity cssanimations csscolumns cssgradients no-cssreflections csstransforms no-csstransforms3d csstransitions  video audio localstorage sessionstorage webworkers applicationcache svg smil svgclippaths   fontface">
    <head>
        <title>Miracle Supply Chain Visibility portal</title>

        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
        <link rel="stylesheet" href='<s:url value="/includes/bootstrap/css/userdefined.css"/>'>
        <link rel="stylesheet" href='<s:url value="/includes/plugins/datatables/dataTables.bootstrap.css"/>' type="text/css">
        <link rel="stylesheet" href='<s:url value="/includes/plugins/daterangepicker/daterangepicker.css"/>' type="text/css">
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        

        <style>
            .content-wrapper{
                min-height: 552px !important;
            }
            
        </style>
        <script>
            var myCalendar;
            function doOnLoad() {
                $("#excelreports").addClass("active");
                $("#reports").addClass("active");
                  
                $("#manufacturing").addClass("active");
                
                 
                $("#excelreports i").addClass("text-red");
	document.getElementById('loadingAcoountSearch').style.display="none";
            }
        </script>
        <script type="text/javascript"> 
  
  
 
            function resetvalues()
            {
   
                document.getElementById('docSenderId').value="-1";
                document.getElementById('docSenderName').value="-1";
                document.getElementById('docReceiverId').value="-1";
                document.getElementById('docReceiverName').value="-1";
                document.getElementById('reportrange').value="";
                document.getElementById('docdatepickerfrom').value="";
                document.getElementById('docdatepicker').value="";

                document.getElementById('docType').value="-1"; 
    
    
                document.getElementById('status').value="-1"; 
                document.getElementById('ackStatus').value="-1"; 

                $('#gridDiv').hide();
    
            }
            /* $(document).ready(function() {
              $('ul.sf-menu').sooperfish();
            });*/
    
    
   
    
    
    
        </script>




    </head>

    <%--<body onload="doOnLoad();initDateTime('docdatepickerfrom','docdatepicker','<%=check %>');setStyle('docRep','');">  --%>

    <body onload="doOnLoad();check();" class="hold-transition skin-blue sidebar-mini">
        <script type="text/javascript" src='<s:url value="/includes/js/wz_tooltip.js"/>'></script>

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
                    Excel Reports
                    <small>Manufacturing</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-wrench"></i>Manufacturing</a></li>
                    <li class="active"> Excel Reports</li>
                </ol>
            </section>
            <br>

            <section class="content">

                <div class="box box-primary">
<!--                    <div class="box-header with-border">
                        <h3 class="box-title"> Excel Reports</h3>
                        <div class="box-tools pull-right">

                        </div>
                    </div>  -->
                    <div class="box-body">
                        <div id="text">
                            <div style="alignment-adjust:central;" >
                                <%String contextPath = request.getContextPath();
                                %>


                                <s:form action="../reports/reportsSearch.action" method="post" name="reportsForm" id="reportsForm" theme="simple">
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="row">
                                                    <div class="col-sm-3"> <label>Date Range</label>
                                                       <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left"   value="%{reportrange}"  onchange="Date1()"/> 
                                                    </div>
                                                    <script type="text/javascript">
                                                        function Date1()
                                                        {
                                                            var date=document.reportsForm.reportrange.value;
                                                            var arr=date.split("-");
                                                            var x=arr[1].trim();
                                                            document.getElementById("docdatepickerfrom").value = arr[0];
                                                            document.getElementById("docdatepicker").value =x ;
                                                        }
                                                    </script>

                                                    <s:hidden id="docdatepickerfrom" name="docdatepickerfrom" />
                                                    <s:hidden id="docdatepicker" name="docdatepicker"/>
                                                    <!--                                                    <script type="text/javascript">
                                                                                                            $(function() {
                                                        
                                                                                                                function cb(start, end) {
                                                                                                                    $('#reportrange span').html(start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
                                                                                                                }
                                                                                                                cb(moment().subtract(29, 'days'), moment());
                                                    
                                                                                                                $('#reportrange').daterangepicker({
                                                                                                                    ranges: {
                                                                                                                        'Today': [moment(), moment()],
                                                               
                                                                                                                        'Last 7 Days': [moment().subtract(6, 'days'), moment()],
                                                                                                                        'Last 30 Days': [moment().subtract(29, 'days'), moment()],
                                                                                                                        'This Month': [moment().startOf('month'), moment().endOf('month')],
                                                                                                                        'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')],
                                                                                                                        'Transactions Until': [moment()]
                                                                                                                    },
                                                                                                                    timePicker: true,
                                                                                                                    timepicker12Hours:true,
                                                                                                                    timepickerseconds:true,
                                                                                                                    timePickerIncrement: 1,
                                                                                                                    locale: {
                                                                                                                        format: 'MM/DD/YYYY h:mm A'
                                                                                                                    }
                                                                                                                }, cb);
                                                    
                                                                                                            });
                                                                                                        </script>-->
                                                    <div  class="col-sm-3">

                                                        <label>Sender Id</label> 
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="senderIdList" name="docSenderId" id="docSenderId" value="%{docSenderId}" />


                                                    </div>
                                                    <div  class="col-sm-3">

                                                        <label>Sender Name</label> 
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="senderNameList" name="docSenderName" id="docSenderName" value="%{docSenderName}" />


                                                    </div>
                                                    <div  class="col-sm-3">

                                                        <label>Receiver Id</label> 
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="receiverIdList" name="docReceiverId" id="docReceiverId" value="%{docReceiverId}" />


                                                    </div> 
                                                </div>

                                                <br>
                                                <div class="row">
                                                    <div  class="col-sm-3">

                                                        <label>Receiver Name</label> 
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="receiverNameList" name="docReceiverName" id="docReceiverName" value="%{docReceiverName}" />


                                                    </div>  
                                                    <div  class="col-sm-3">

                                                        <label>Document Type</label>

                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="docTypeList" name="docType" id="docType" value="%{docType}" />
                                                    </div>  
                                                    <div  class="col-sm-3">

                                                        <label>Status</label> 
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="{'Success','Error','Warning'}" name="status" id="status" value="%{status}" />
                                                    </div>  
                                                    <div  class="col-sm-3">

                                                        <label>Ack Status</label> 
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="{'Overdue','Accepted','Rejected'}" name="ackStatus" id="ackStatus" value="%{ackStatus}" />
                                                    </div>
                                                      <div id="loadingAcoountSearch" class="loadingImg">
                                                    <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                                </div>
                                                </div>
                                            </div>
                                        </div>
                                        <br>

                                        <div class="row">
                                            <div class="col-sm-2"><s:submit value="Search" cssClass="btn btn-primary col-sm-12" tabindex="16"/></div>

                                            <div class="col-sm-2"><strong><input type="button" value="Reset"  tabindex="17" class="btn btn-primary col-sm-12" onclick="return resetvalues();"/></strong></div>

                                            <s:hidden name="sampleValue" id="sampleValue" value="2"/>

                                        </s:form>


                                    </div>
                                </div>
                            </div>
                        </div></div>

                </div>

            </section>

            <div id="gridDiv">
                <s:if test="#session.documentReportList != null"> 
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
                                                        <div class="grid_overflow">

                                                            <table id="results"  class="table table-bordered table-hover">
                                                                <%
                                                                    java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_DOCREPORT_LIST);

                                                                    if (list.size() != 0) {
                                                                        ReportsBean docRepositoryBean;
                                                                %>
                                                                <thead> <tr>
                                                                        <%-- <td >ISA #</td>
                                                                         <td >File Format</td>
                                                                         <td>Direction</td>
                                                                         <td >Date</td>
                                                                         <td>Status</td>  --%>
                                                                        <th>File&nbsp;Format</th> 
                                                                        <th>InstanceId</th>
                                                                        <th>Partner</th>

                                                                        <th>DateTime</th>
                                                                        <%-- <td >ISA #</td>  --%>

                                                                        <%-- <td >DOC_ORIGIN</td> --%>
                                                                        <th>Trans&nbsp;Type</th>
                                                                        <th >Direction</th>

                                                                        <th>Status</th>
                                                                        <%-- <td >ACK_STATUS</td>  --%>
                                                                        <th>Reprocess</th>


                                                                    </tr></thead><tbody>


                                                                    <%
                                                                        for (int i = 0; i < list.size(); i++) {
                                                                            docRepositoryBean = (ReportsBean) list.get(i);

                                                                            if (i % 2 == 0) {
                                                                                cssValue = "whiteStripe";
                                                                            } else {
                                                                                cssValue = "grayEditSelection";
                                                                            }
                                                                    %>
                                                                <td>
                                                                    <%
                                                                        out.println(docRepositoryBean.getFile_type());
                                                                    %>

                                                                </td>
                                                                <td><%-- <a href="javascript:getDetails('<%=docRepositoryBean.getFile_id()%>','<%=docRepositoryBean.getPoNumber()%>');"> --%>
                                                                    <%
                                                                        out.println(docRepositoryBean.getFile_id());
                                                                    %>
                                                                    <%--  </a> --%>
                                                                </td>
                                                                <td>
                                                                    <%

                                                                        out.println(docRepositoryBean.getPname());



                                                                    %>

                                                                </td>


                                                                <td>
                                                                    <%
                                                                        out.println(docRepositoryBean.getDate_time_rec().toString().substring(0, docRepositoryBean.getDate_time_rec().toString().lastIndexOf(":")));
                                                                    %>

                                                                </td>  

                                                                <%-- <td> 
                                                                         <%
                                                                         out.println(docRepositoryBean.getIsa_number());
                                                                         %>
                                                                        
                                                                     </td>  --%>



                                                                <td>
                                                                    <%
                                                                        out.println(docRepositoryBean.getTransaction_type());
                                                                    %>

                                                                </td>
                                                                <td>
                                                                    <%
                                                                        out.println(docRepositoryBean.getDirection().toUpperCase());
                                                                    %>

                                                                </td>  


                                                                <td>
                                                                    <%
                                                                        if (docRepositoryBean.getStatus().equalsIgnoreCase("ERROR")) {%> <a href="#" onclick="getErrorMessage('<%=docRepositoryBean.getStatus().toUpperCase()%>','<%=docRepositoryBean.getErrorMessage()%>')" onmouseover="Tip('Click here to view Error Info.')" onmouseout="UnTip()">        
                                                                        <%
                                                                            out.println("<font color='red'>" + docRepositoryBean.getStatus().toUpperCase() + "</font>");
                                                                        %></a><%
                                                                            } else if (docRepositoryBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                                out.println("<font color='green'>" + docRepositoryBean.getStatus().toUpperCase() + "</font>");
                                                                            } else {
                                                                                out.println("<font color='orange'>" + docRepositoryBean.getStatus().toUpperCase() + "</font>");
                                                                            }

                                                                        %>

                                                                </td>
                                                                <%--   <td>
                                                                           <%
                                                                           
                                                                             //out.println(poLifeCycleBean.getAckStatus());
                                                                             if(docRepositoryBean.getAckStatus().equalsIgnoreCase("REJECTED")){       
                                                                                  out.println("<font color='red'>"+docRepositoryBean.getAckStatus()+"</font>");
                                                                              }else if(docRepositoryBean.getAckStatus().equalsIgnoreCase("ACCEPTED")){
                                                                                  out.println("<font color='green'>"+docRepositoryBean.getAckStatus()+"</font>");
                                                                              }else {
                                                                                   out.println("<font color='orange'>"+docRepositoryBean.getAckStatus()+"</font>");
                                                                                }
                                                                                               
                                                                             %>
                                                                         </td> --%>
                                                                <td>
                                                                    <%
                                                                        //out.println(docRepositoryBean.getReProcessStatus());
                                                                        if (docRepositoryBean.getReProcessStatus() != null) {
                                                                            out.println(docRepositoryBean.getReProcessStatus().toUpperCase());

                                                                        } else {
                                                                            out.println("");
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
                                                                </tr>
                                                            </table></td></tr>
                                                            <%
                                                                if (list.size() != 0) {
                                                            %>
                                                <tr >
                                                    <!--                                                        <td align="right" colspan="28" style="background-color: white;">
                                                                                                                <div align="right" id="pageNavPosition">hello</div>-->

                                                </tr> 


                                             
                                                <% }%>
                                                </tbody></table>
                                                </div>




                                            <%-- Process butttons  start --%>
                                            <%
                                                if (list.size() != 0) {
                                            %>
                                            <table align="right">
                                                <tr>
                                                    <td style="background-color: white;">
                                                        <strong><input type="button" value="Generate Excel" class="btn btn-effect-ripple btn-primary" onclick="return gridDownload('documentReport','xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/></strong>
                                                    </td>
                                                </tr>
                                            </table> 
                                            <%}%>
                                        </div>

                                    </div>
                                </div>
                            </div> </section>
                            <%-- process buttons end--%>
                            <%-- Grid End --%>
                        </s:if>
            </div> 
            <script>
                $(function () {
                    // $("#example1").DataTable();
                    $('#results').DataTable({
                        "paging": true,
                        "lengthChange": true,
                        "searching": true,
                        "ordering": true,
                        "info": true,
                        "autoWidth": true
                    });
                });
            </script>

        </div> 
        <div>
            <s:include value="../includes/template/footer.jsp"/>
        </div>
        <script type="text/javascript" src='<s:url value="../includes/js/DateValidation.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
        <script language="JavaScript"  src='<s:url value="/includes/js/downloadAjax.js"/>'></script>


        <script>
            $('input[name="daterange"]').daterangepicker();
        </script>


        <!-- Bootstrap 3.3.5 -->
        <script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
        <script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
        <script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>
        



    </body>


</html>