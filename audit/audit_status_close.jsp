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
	document.getElementById("btn_dis").disabled=true;
	document.getElementById("btn_reset").disabled=true;
	document.AuditModule.action="../../VisitorServlet";
	document.AuditModule.submit();
}
</script>
<style>
table {
  font-family: arial, sans-serif;
  /* border-collapse: collapse; */
  width: 100%;
  border: 1px solid black;
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
	int count=1;
	String msg=(String)session.getAttribute("error");
	session.removeAttribute("error");
 %>
<body>
<form  name="ListOfAudit" method="post" action="../../VisitorServlet">
<input type="hidden" name="beanIndicator" value="auditorSchedule">
<input type="hidden" name="task" value="ADD">
<input type="hidden" name="op" value="3">
<%if(msg!=null) {
%><h5><label style="color:red"><%=msg %></label></h5>
<%} %>
<center><h5>Pending Points</h5></center>
	<table>
		<tr>
			<th>Sl.No</th>
			<th>Pending Point</th>
			<th>Closure Date</th>
		</tr>
			<%ArrayList arr=db.selectPendingPoints(email); %>
			<% 
			 System.out.println("Pending Point "+arr);
			for(int i=0;i<arr.size();i++){ 
			ArrayList pending=(ArrayList)arr.get(i);
			ArrayList points=db.SelectAuditPoints(pending.get(2).toString());
			System.out.println("Points whcih are pending "+points);
			for(int j=0;j<points.size();j++){
			ArrayList Li=(ArrayList)points.get(j);
			System.out.println("ArrayList "+Li.get(0));
			%>
		<tr>
			<td><%=count %></td>
			<td><a href="audit_closing.jsp?auditStsId=<%=pending.get(0)%>&auditObId=<%=pending.get(2)%>&auditPoint=<%=Li.get(0)%>&auditClosureDate=<%=Li.get(1) %>"><%=Li.get(0) %></a></td>
			<td><%=Li.get(1) %></td>
		</tr>
		<%
		}
		count=count+1;} %>
	</table>
</form>
</BODY>
<%}else{ %>
<%@include file="../cookieTracker/CookieTrackerBottom.jsp" %>
<%} %>
</HTML>