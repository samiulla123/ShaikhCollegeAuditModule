<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="com.gen.cms.util.DateManager"%>
<%@page import="com.gen.cms.assets.beans.*"%> 
<%@page import="java.util.ArrayList"%> 
<%@page import="java.sql.ResultSet"%>
<HTML>
<HEAD>
<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
%>


<%@ page import="com.gen.cms.assets.beans.*"%>
<%@ page import="java.util.*"%>

<%@ page errorPage="../../error.jsp" %>

<%@ include file="../cookieTracker/CookieTrackerTop.jsp" %> 
<%
	int num=0;
	String browserType=(String)request.getHeader("User-Agent");
	System.out.println("Browser Type "+browserType); 
	Assets_DBQueries dbVer=new Assets_DBQueries();
	String version=dbVer.BrowserVersion(browserType);
	float ver=11;
	if(!version.isEmpty())
	{
	ver=Float.parseFloat(version);
	}
	System.out.println("Version of IE "+ver);
	if(ver<11.0){
		num=1;%>
	<!-- Code Comes here -->
	<%}else if(version.isEmpty()){%>
		 <meta http-equiv="X-UA-Compatible" content="IE=edge">
		 <meta name="viewport" content="width=device-width, initial-scale=1">
		 <meta name="robots" content="noindex, nofollow">
		<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
	    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/css/bootstrap-select.css" />
	    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
	    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
	    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/js/bootstrap-select.js"></script>
	<%} %>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1" />
<META name="GENERATOR" content="IBM WebSphere Studio" />
<META http-equiv="Content-Style-Type" content="text/css" />
<LINK href="../../stylesheet/main.css" rel="stylesheet" type="text/css" />
<SCRIPT language="javascript" src="../../javascript/Validate.js"></SCRIPT>
<script language="javascript" src="../../javascript/popcalendar.js"></script>
<jsp:useBean id="assetManager" class="com.gen.cms.assets.beans.Assets_DBQueries"></jsp:useBean>


<TITLE>Schedule Audit</TITLE>
<script type="text/javascript">
function reLoad()
{
	document.AuditModule.action="";
	document.AuditModule.submit();
}
function addData()
{
	if(document.getElementById("auditComments").value=="")
	{
		alert("Please Enter Comments");
		return false;
	}
	if(document.getElementById("rd").checked==true)
	{
		var a=document.getElementById("rd").value;
		document.ListOfAudit.cmpt.value=document.getElementById("rd").value;
	}
	else if(document.getElementById("rd1").checked==true)
	{
		var a=document.getElementById("rd1").value;
		document.ListOfAudit.cmpt.value=document.getElementById("rd1").value;
	}
	/* if(document.getElementById("cmplt").value=="checked") */
	document.getElementById("btn_dis").disabled=true;
	document.ListOfAudit.action="../../VisitorServlet";
	document.ListOfAudit.submit(); 
	window.close();
}
</script>
<style>
table {
  font-family: arial, sans-serif;
  /* border-collapse: collapse; */
  width: 100%;
}
td, th {
  border: 1px solid #dddddd;
  text-align: left;
  padding: 8px;
}

tr:nth-child(even) {
  background-color: #dddddd;
}
</style>
</HEAD>
<%
	AuditDBQuery db=new AuditDBQuery();
	ArrayList ar=new ArrayList();
	ar=db.selectAuditType();
	int count=0;
	String auditStsId=request.getParameter("auditStsId");
	String auditObId=request.getParameter("auditObId");
	String auditPoints=request.getParameter("auditPoint");
	String ClosureDate=request.getParameter("auditClosureDate");
	System.out.println("Audit Observ ID "+auditObId+" Audit status "+auditStsId+" AuditPoint "+auditPoints+" cluser Date "+ClosureDate);
 %>
<body>
<form  name="ListOfAudit" method="post">
<input type="hidden" name="beanIndicator" value="auditorSchedule">
<input type="hidden" name="task" value="ADD">
<input type="hidden" name="op" value="6">
<input type="hidden" name="cmpt" value="">
<input type="hidden" name="auditSts" value="<%=auditStsId%>">
<center><h5>Pending Points</h5></center>
	<table>
		<tr>
			<td>Points</td>
			<td><label style="color:red"><%=auditPoints %></label></td>
			<td>Closure Date</td>
			<td><label style="color:red"><%=ClosureDate %></label></td>
		</tr>
		<tr>
			<td colspan="4">
			Completed <input type="radio" name="cmplt" id="rd" value="1" checked> &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp; &nbsp;
			Not-Completed <input type="radio" name="cmplt" id="rd1" value="0"></td>
			
		</tr>
		<tr>
			<td colspan="2">Complete Date</td>
			<td colspan="2"> <input type="text" name="audit_date" id="date" class="form-control" style="background-color:#FFECEC ;" onfocus="javascript:popUpCalendar(this,document.ListOfAudit.audit_date,'dd-mmmm-yyyy');"></td>
		</tr>
		<tr>
			<td colspan="2">Comments</td>
			<td colspan="2"><textarea rows="4" cols="20" name="cmnt" id="auditComments" class="form-control" onpaste="return false" style="background-color:#FFECEC ; border-style: solid; border-width: 1px" tabindex="1" onkeypress="return (event.keyCode!=39)"></textarea></td>
		</tr>
		<tr>
			<td colspan="4"><input type="button" id="btn_dis" value="Submit" onclick="return addData()"></td>
			<td></td>
		</tr>
	</table>
</form>
</BODY>
<%}else{ %>
<%@include file="../cookieTracker/CookieTrackerBottom.jsp" %>
<%} %>
</HTML>