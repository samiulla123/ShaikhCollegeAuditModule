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
}
function addData()
{
	
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
<%AuditDBQuery db=new AuditDBQuery();%>
<body>
<form  name="ListOfAudit" method="post">
<input type="hidden" name="beanIndicator" value="auditorSchedule">
<input type="hidden" name="task" value="ADD">
<input type="hidden" name="op" value="">
<input type="hidden" name="cmpt" value="">
<center><h5>Pending Points</h5></center>
	<table>
		<tr>
			<th>Sl.No</th>
			<th>Department</th>
			<th>AuditType</th>
			<th>Date</th>
		</tr>
		<%ArrayList ar=db.selectAuditDetails();
		System.out.println("Query Data "+ar);
		int count=1; 
		for(int i=0;i<ar.size();i++){
		ArrayList dt=(ArrayList)ar.get(i);
		%>
			<tr>
				<td><%=count %></td>
				<%String DepName=db.DepartmentName(dt.get(3).toString()); 
				System.out.println("Audit type name "+DepName);%>
				<td><a href="AuditSummaryReport.jsp?auditType=<%=dt.get(1)%>&auditId=<%=dt.get(0)%>&ProgId=<%=dt.get(3)%>&RefNo=<%=dt.get(14)%>&departName=<%=DepName%>&auditorName=<%=dt.get(5)%>&auditeeName=<%=dt.get(4)%>&auditDate=<%=dt.get(6)%>"><%=DepName %></a></td>
				<%String audType=db.selecttype(dt.get(1).toString()); 
				System.out.println("Audit type name "+audType);%>
				<td><%=audType %></td>
				<td><%=dt.get(6) %></td>
			</tr>
		<%count++;} %>
	</table>
</form>
</BODY>
<%}else{ %>
<%@include file="../cookieTracker/CookieTrackerBottom.jsp" %>
<%} %>
</HTML>