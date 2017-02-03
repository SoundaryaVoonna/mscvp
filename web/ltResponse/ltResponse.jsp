
<%@page import="com.mss.ediscv.ltResponse.LtResponseBean"%>

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
        <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
        <script type="text/javascript">
            function doOnLoad()
            {
                $("#ltresponse").addClass("active");
                $("#logistics").addClass("active");
                $("#ltresponse i").addClass("text-red");
                document.getElementById('loadingAcoountSearch').style.display = "none";
            }
        </script>
        <script type="text/javascript">
            function hide()
            {
                $('#hide-menu1').removeClass('show-menu');
            }
//            $('body,html').click(function(e){
//                $('#hide-menu1').removeClass('show-menu');
//               
//            });
        </script>


    </head>

    <%
        String check = null;
        if (request.getAttribute("check") != null) {
            check = request.getAttribute("check").toString();
        }

        //System.out.println("check-->"+check);
    %>
    <body onload="check();doOnLoad()" class="hold-transition skin-blue sidebar-mini">
        <div>
            <s:include value="../includes/template/header.jsp"/>
        </div>
        <div>
            <s:include value="../includes/template/sidemenu.jsp"/>
        </div>
        <div class="content-wrapper">
            <section class="content-header">
                <h1>
                    Response
                    <small>Logistics</small>
                </h1>
                <ol class="breadcrumb">
                    <li><a href="#"><i class="fa fa-truck"></i>Logistics</a></li>
                    <li class="active">Response</li>
                </ol>
            </section>
            <br>
            <section class="content">

                <div class="box box-primary">

                    <div class="box-body">
                        <div id="text">


                            <div  style="alignment-adjust:central;" >
                                <%String contextPath = request.getContextPath();
                                %>

                                <s:form action="../ltResponse/doSearchltResponse.action" method="post" name="ltResponseForm" id="ltResponseForm" theme="simple">
                                    <div class="form-group">
                                        <div class="row">
                                            <div class="col-sm-12">
                                                <div class="row">
                                                    <div class="col-sm-3"> <label>Date range</label>
                                                        <s:textfield name="reportrange"  id="reportrange" cssClass="form-control pull-left"   value="%{reportrange}" onchange="Date1();" /> 
                                                    </div>

                                                    <script type="text/javascript">
                                                        function Date1() {
                                                            var date = document.ltResponseForm.reportrange.value;
                                                            var arr = date.split("-");
                                                            var x = arr[1].trim();
                                                            document.getElementById("datepickerfrom").value = arr[0];
                                                            document.getElementById("datepickerTo").value = x;
                                                        }
                                                    </script>

                                                    <s:hidden id="datepickerfrom" name="datepickerfrom" />
                                                    <s:hidden id="datepickerTo" name="datepickerTo"/>
                                                    <div  class="col-sm-3">

                                                        <label>Document Type</label> 
                                                        <s:select headerKey="-1" cssClass="form-control" headerValue="Select Type" list="docTypeList" name="docType" id="docType" value="%{docType}" />
                                                    </div>

                                                    <div  class="col-sm-3">

                                                        <label>Sender Id</label>  
                                                        <s:textfield cssClass="form-control" name="senderId" id="senderId" value="%{senderId}"  />
                                                    </div>

                                                    <div  class="col-sm-3">
                                                        <label>Sender Name</label>  
                                                        <s:textfield cssClass="form-control" name="senderName" id="senderName" value="%{senderName}"  />
                                                    </div>


                                                </div>
                                                <br>
                                                <div class="row">
                                                    <div class="col-sm-3">
                                                        <label>Receiver Id</label>
                                                        <s:textfield cssClass="form-control" name="receiverId" id="receiverId" value="%{receiverId}"  />

                                                    </div>
                                                    <div class="col-sm-3">
                                                        <label>Receiver Name</label>
                                                        <s:textfield cssClass="form-control" name="receiverName" id="receiverName" value="%{receiverName}"  />

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
                                        <div id="loadingAcoountSearch" class="loadingImg">
                                            <span id ="LoadingContent" > <img src="<s:url value="/includes/images/Loader2.gif"/>"   ></span>
                                        </div>

                                        <br>
                                        <span id="span1">
                                        </span>
                                        <div class="row">

                                            <div class="col-sm-2"><s:submit value="Search"  onclick="return checkCorrelation();" cssClass="btn btn-primary col-sm-12" tabindex="16"/></div>

                                            <div class="col-sm-2"><strong><input type="button" value="Reset"  tabindex="17" class="btn btn-primary col-sm-12" onclick="return resetvalues();"/></strong></div>

                                            <s:hidden name="sampleValue" id="sampleValue" value="2"/>

                                        </s:form>
                                    </div>
                                </div>
                            </div>
                        </div></div>
            </section>
            <div id="gridDiv">     

                <s:if test="#session.ltResponseList != null"> 
                    <%--- GRid start --%>
                    <section class="content">
                        <div class="row">
                            <div class="col-xs-12">
                                <div class="box">
                                    <div class="box-header">
                                        <h3 class="box-title">Table</h3>
                                    </div><!-- /.box-header -->
                                    <div class="box-body">
                                        <%!String cssValue = "whiteStripe";
                                            int resultsetTotal;%>
                                        <div style="overflow-x:auto;">      
                                            <table align="left" width="100%"
                                                   border="0" cellpadding="0" cellspacing="0" >
                                                <tr>
                                                    <td style="background-color: white;">
                                                        <div style="overflow-x:auto;">      
                                                            <table id="results"  class="table table-bordered table-hover">
                                                                <%
                                                                    java.util.List list = (java.util.List) session.getAttribute(AppConstants.SES_LTRESPONSE_LIST);

                                                                    if (list.size() != 0) {
                                                                        LtResponseBean ltResponseBean;
                                                                %>
                                                                <thead><tr>
                                                                        <%-- <td >ISA #</td>
                                                                         <td >File Format</td>
                                                                         <td>Direction</td>
                                                                         <td >Date</td>
                                                                         <td>Status</td>  --%>
                                                                        <th >FileFormat</th> 
                                                                        <th >InstanceId</th>
                                                                        <th >OrderId</th>
                                                                        <th >Shipment</th>


                                                                        <%--   <th >DATETIME</th>
                                                                            <th >ISA #</th>  --%>

                                                                        <%-- <th >DOC_ORIGIN</th> --%>
                                                                        <th >TransType</th>
                                                                        <th >Direction</th>

                                                                        <th >Status</th>


                                                                    </tr>
                                                                </thead>
                                                                <tbody>
                                                                    <tr>

                                                                        <%
                                                                            for (int i = 0; i < list.size(); i++) {
                                                                                ltResponseBean = (LtResponseBean) list.get(i);

                                                                                if (i % 2 == 0) {
                                                                                    cssValue = "whiteStripe";
                                                                                } else {
                                                                                    cssValue = "grayEditSelection";
                                                                                }
                                                                        %>
                                                                        <td>
                                                                            <%
                                                                                if (ltResponseBean.getFileType() != null) {
                                                                                    out.println(ltResponseBean.getFileType());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }
                                                                            %>

                                                                        </td>
                                                                        <td>
                                                                            <%
                                                                                if (ltResponseBean.getFileId() != null) {
                                                                                    out.println(ltResponseBean.getFileId());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }
                                                                            %>

                                                                        </td>

                                                                        <td><a href="javascript:getDetails('<%=ltResponseBean.getFileId()%>','<%=ltResponseBean.getRefId()%>');">
                                                                                <%
                                                                                    if (ltResponseBean.getRefId() != null) {

                                                                                        out.println(ltResponseBean.getRefId());

                                                                                    } else {
                                                                                        out.println("-");
                                                                                    }

                                                                                %>
                                                                            </a>     
                                                                        </td>



                                                                        <td>
                                                                            <%                                                                                if (ltResponseBean.getShipmentId() != null) {
                                                                                    out.println(ltResponseBean.getShipmentId());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }


                                                                            %>

                                                                        </td>
                                                                        <td>
                                                                            <%                                                                                if (ltResponseBean.getTransType() != null) {
                                                                                    out.println(ltResponseBean.getTransType());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }
                                                                            %>

                                                                        </td>
                                                                        <td>
                                                                            <%
                                                                                if (ltResponseBean.getDirection() != null) {
                                                                                    out.println(ltResponseBean.getDirection());
                                                                                } else {
                                                                                    out.println("-");
                                                                                }
                                                                            %>

                                                                        </td>  


                                                                        <td>
                                                                            <%
                                                                                if (ltResponseBean.getStatus() != null) {
                                                                                    if (ltResponseBean.getStatus().equalsIgnoreCase("ERROR")) {
                                                                                        out.println("<font color='red'>" + ltResponseBean.getStatus().toUpperCase() + "</font>");
                                                                                    } else if (ltResponseBean.getStatus().equalsIgnoreCase("SUCCESS")) {
                                                                                        out.println("<font color='green'>" + ltResponseBean.getStatus().toUpperCase() + "</font>");
                                                                                    } else {
                                                                                        out.println("<font color='orange'>" + ltResponseBean.getStatus().toUpperCase() + "</font>");
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
                                                                                    // String contextPath = request.getContextPath();
                                                                                    // out.println("<img  border='0' align='top'  src='"+contextPath+"/includes/images/alert.gif'/><b> No Records Found to Display!</b>");
                                                                                    out.println("<img  border='0' align='top'  src='" + contextPath + "/includes/images/alert.gif'/><b>No records found for the given search criteria. Please try a different search criteria!</b>");
                                                                                }

                                                                            %>
                                                                        </td>
                                                                    </tr>

                                                            </table>
                                                    </td>
                                                </tr>
                                                <%                                                    if (list.size() != 0) {
                                                %>
                                                <tr>
                                                    <!--                                                                        <td align="right" colspan="28" style="background-color: white;">
                                                                                                                                <div align="right" id="pageNavPosition"></div>-->
                                                    </td>
                                                </tr> 

                                                <% }%>   </tbody>
                                            </table>
                                        </div>

                                        <%-- Process butttons  start --%>
                                        <%
                                            if (list.size() != 0) {
                                        %><br>
                                        <table align="right">
                                            <tr>
                                                <td style="background-color: white;">
                                                    <strong><input type="button" value="Generate Excel" class="btn btn-effect-ripple btn-primary" onclick="return gridDownload('ltResponse', 'xls');" onmouseover="Tip('Click here to generate an excel Report.')" onmouseout="UnTip()" id="excel"/></strong>
                                                </td>
                                            </tr>
                                        </table> 
                                    </div>



                                </div>
                                <%}%>
                                <%-- process buttons end--%>
                                <%-- Grid End --%>
                            </div>
                        </div></div>
            </div></section>

        <div id="hide-menu1" class="hide-menu message">

            <div class="row col-sm-12">

                <br>
                <div class="col-sm-6"> <label class="labelw"> Instance Id </label>
                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="resInstanceid" name="resInstanceid" readonly="true"/>
                </div>
                <div class="col-sm-6"> <label class="labelw"> Shipment # </label>
                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="resShipment" name="resShipment" readonly="true"/>
                </div>
            </div>
            <div class="row col-sm-12"> <br>
                <div class="col-sm-6"> <label class="labelw"> DocType: </label>
                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="resFiletype" name="resFiletype" readonly="true" />
                </div>
                <div class="col-sm-6"> <label class="labelw">Transaction Type </label>
                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="resTransactiontype" name="resTransactiontype"  readonly="true"/>
                </div>
            </div>

            <div id="senderinfo"><br>
                <div class="row col-sm-12">
                    <div class="col-sm-6"> <h4>Sender Info :</h4></div>
                    <div class="col-sm-6"></div>
                    <div class="col-sm-6"></div>

                </div>
                <br>
                <div class="row col-sm-12">
                    <div class="col-sm-6"> <label class="labelw">  Id </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="resSenderid" name="resSenderid" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> Name </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="resSendername" name="resSendername" readonly="true"/>
                    </div>
                </div>
            </div>
            <br>
            <div id="receiverinfo"><br>
                <div class="row col-sm-12">
                    <div class="col-sm-6"> <h4>Partner Info:</h4></div>
                    <div class="col-sm-6"></div>
                    <div class="col-sm-6"></div>
                </div>

                <br>
                <div class="row col-sm-12 clear">
                    <div class="col-sm-6"> <label class="labelw">  Id </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="resReceiverid" name="resReceiverid" readonly="true"/>
                    </div>
                    <div class="col-sm-6"> <label class="labelw"> Name </label>
                        <s:textfield cssClass="form-control"  required="required" placeholder="" id="resReceivername" name="resReceivername" readonly="true"/>
                    </div>
                </div>
            </div>
            <div class="row col-sm-12 clear">
                <div class="col-sm-6"> <label class="labelw">  ISA #  : </label>
                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="resIsa" name="resIsa" readonly="true"/>
                </div>
                <div class="col-sm-6"> <label class="labelw"> GS # </label>
                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="resGs" name="resGs" readonly="true"/>
                </div>


            </div>

            <div class="row col-sm-12" style="margin-top:10px;" >
                <div class="col-sm-6"> <label class="labelw"> ST #  : </label>
                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="resSt" name="resSt" readonly="true"/>
                </div>
                <div class="col-sm-6"> <label class="labelw">ISA Date:</label>
                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="resIsadate" name="resIsadate" readonly="true"/>
                </div>

                <div class="col-sm-6"> <label class="labelw">ISA Time :</label>
                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="resIsatime" name="resIsatime" readonly="true"/>
                </div>
                <div class="col-sm-6"> <label class="labelw"> OrderId </label>
                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="resReference" name="resReference" readonly="true"/>
                </div>
                <div class="col-sm-6"> <label class="labelw"> STATUS </label>
                    <s:textfield cssClass="form-control"  required="required" placeholder="" id="resStatus" name="resStatus" readonly="true"/>
                </div>

            </div>

            <div class="row col-sm-12" ><br>
                <div class="col-sm-6"> <label class="labelw">Pre-Translation:</label></div>
                <div class="col-sm-6">   <div id="resPreTranslation" ></div>

                </div>
                <div class="row col-sm-12" >
                    <div class="col-sm-6"> <label class="labelw">Post-Translation:</label></div>
                    <div class="col-sm-6"> <div id="resPostTranslation"></div>

                    </div></div>
                <div class="row col-sm-12" >
                    <div class="col-sm-6"> <label class="labelw">997AckFile</label></div>
                    <div class="col-sm-6"> <div id="resAckfileid"></div>

                    </div>
                    <br><br><br><br><br><br>
                </div>
            </div>
            <div class="row col-sm-12" id="errorDiv" style="display: none">
                <div class="col-sm-6"> <label class="labelw"> Error&nbsp;Message </label></div>
                <div class="col-sm-6" id="resErrormessage" style="color: red"></div>
            </div>

            <div id="noresult"></div>
            <div class="col-sm-12">  <button type="button" class="btn btn-primary col-sm-11" style="margin-left:12px; " id="hide-menu" onclick="hide()" value="X">Close</button></div>
        </div>

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
            "autoWidth": false
        });
    });
</script>

</div>
<div>
    <s:include value="../includes/template/footer.jsp"/>
</div>
<script>
    $('input[name="daterange"]').daterangepicker();
</script>
<script language="JavaScript" src='<s:url value="/includes/js/DateValidation.js"/>'></script>
<script language="JavaScript" src='<s:url value="/includes/js/GeneralAjax.js"/>'></script>
<script language="JavaScript" src='<s:url value="/includes/js/downloadAjax.js"/>'></script>
<script type="text/javascript" src='<s:url value="../includes/js/Correlation.js"/>'></script>
<!-- Bootstrap 3.3.5 -->
<script src='<s:url value="../includes/plugins/daterangepicker/daterangepicker.js"/>'></script>
<script src='<s:url value="../includes/bootstrap/js/app.min.js"/>'></script>
<script src='<s:url value="../includes/plugins/datatables/jquery.dataTables.min.js"/>'></script>
<script src='<s:url value="../includes/plugins/datatables/dataTables.bootstrap.min.js"/>'></script>

<script type="text/javascript">

    function getDetails(fileId, refId)
    {
        //  alert("hiiii");    

        getLtResponseDetails(fileId, refId);
    }
    function checkCorrelation() {
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
    function resetvalues()
    {
        document.getElementById('datepickerfrom').value = "";
        document.getElementById('datepickerTo').value = "";
        document.getElementById('senderId').value = "";
        document.getElementById('senderName').value = "";
        document.getElementById('receiverId').value = "";
        document.getElementById('receiverName').value = "";
        document.getElementById('corrattribute').value = "-1";
        document.getElementById('corrvalue').value = "";
        document.getElementById('docType').value = "-1";
        document.getElementById('corrattribute1').value = "-1";
        document.getElementById('corrvalue1').value = "";
        document.getElementById('reportrange').value = "";
        document.getElementById('status').value = "-1";

        $('#gridDiv').hide();
    }
</script>

</body>


</html>
