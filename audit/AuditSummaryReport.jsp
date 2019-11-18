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

</script>
<style>
table {
  font-family: arial, sans-serif;
   /* border-collapse: collapse;  */
   border: 2px solid black;
  width: 100%;
}
td, th {
  border: 1px solid #dddddd;
  text-align: left;
  border: 2px solid black;
  padding: 8px;
}

tr:nth-child(even) {
  background-color: #dddddd;
  border: 2px solid black;
}
</style>
<%AuditDBQuery db=new AuditDBQuery();
String progId=request.getParameter("ProgId");
String auditId=request.getParameter("auditId");
String auditType=request.getParameter("auditType");
String refNo=request.getParameter("RefNo");
String DepartmentName=request.getParameter("departName");
String auditorName=request.getParameter("auditorName");
String auditeeName=request.getParameter("auditeeName");
String auditDate=request.getParameter("auditDate");
System.out.println("ProgMaster Id "+progId+" AuditID "+auditId+" Audit Type "+auditType );
System.out.println("Department Name  "+DepartmentName+" auditorName "+auditorName+" auditeeName  "+auditeeName+" Audit date" );%>
</HEAD>
<body>
<form  name="pendingPoints" method="post" >
<input type="hidden" name="beanIndicator" value="auditorSchedule">
<input type="hidden" name="task" value="ADD">
<div class="container">
	<div style="border: 2pdx solid black; ">
	&nbsp; &nbsp; &nbsp;<label style="font-size: 15px; font-family: sans-serif; font-weight: bold;">Reference Number: <label style="color: red;"><%=refNo %></label></label> &nbsp; &nbsp; &nbsp; &nbsp;&nbsp; &nbsp; &nbsp;&nbsp; &nbsp;&nbsp; &nbsp; &nbsp;<label style="font-size: 15px; font-family: sans-serif; font-weight: bold;">Audit Date: <label style="color: red;"><%=auditDate%></label></label><br/>
	 &nbsp; &nbsp; &nbsp;  &nbsp; &nbsp; &nbsp;<br/><label style="font-size: 20px; font-family: sans-serif; font-weight: bold;">CMS AUDIT OBSERVATION SHEET</label>	<br/><br/>
	 &nbsp; &nbsp; &nbsp; <label style="font-size: 15px; font-family: sans-serif; font-weight: bold;">Unit/Dept.:<label style="color: red;"> <%=DepartmentName %></label></label> &nbsp; &nbsp; &nbsp;
	 <% ArrayList dep2=db.SelectEmployeName(auditorName);
	 String nameAuditor="";
			for(int k=0;k<dep2.size();k++)
			{
				ArrayList depUn=(ArrayList)dep2.get(k);
				nameAuditor=""+depUn.get(1)+" "+depUn.get(2)+" "+depUn.get(3); %>
		  <%} %>
		 <label style="font-size: 15px; font-family: sans-serif; font-weight: bold;"> Auditor: <label style="color: red;"><%=nameAuditor %></label></label>&nbsp; &nbsp; &nbsp;
		   <% ArrayList dep3=db.SelectEmployeName(auditeeName);
	 String nameAuditee="";
			for(int k=0;k<dep3.size();k++)
			{
				ArrayList depUn=(ArrayList)dep3.get(k);
				nameAuditee=""+depUn.get(1)+" "+depUn.get(2)+" "+depUn.get(3); %>
		  <%} %>
		<label style="font-size: 15px; font-family: sans-serif; font-weight: bold;">  Auditee:<label style="color: red;"> <%=nameAuditee %></label></label>
	</div>
<table>
	<tr>
		<th>Sl.No.(1)</th>
		<th>Audit Observations w.r.t QP/WI/GL/Others (2)</th>
		<th>O+/AOC/OFI/Marks (3)</th>
		<th>Action By (4)</th>
		<th>Closure of col 3 (5)</th>
		<th>Review audit Findings of F/U </th>
	</tr>
	<%ArrayList li=db.selectAuditObservation(progId, auditId);
	System.out.println("List Data "+li); 
	int count=1;
	for(int j=0;j<li.size();j++)
	{
	ArrayList ar=(ArrayList)li.get(j);
	%>
	<tr>
		<td><%=count %></td>
		<td><%=ar.get(4) %></td>
		<td><%=ar.get(5) %></td>
		<%String auditeeFullName="";
			ArrayList dep=db.SelectEmployeName(ar.get(6).toString());
			for(int k=0;k<dep.size();k++)
			{
				ArrayList depUn=(ArrayList)dep.get(k);
				auditeeFullName=""+depUn.get(1)+" "+depUn.get(2)+" "+depUn.get(3); %>
		  <%} %><td><%=auditeeFullName %></td>
		  <%ArrayList cmnts=db.selectPreviousStatus(ar.get(0).toString());
		  for(int d=0;d<cmnts.size();d++){ 
		  	ArrayList cmndt=(ArrayList)cmnts.get(d);
		  	System.out.println("True/False "+cmndt.get(0));%>
			<td><%=cmndt.get(0) %></td>
			<td><%=cmndt.get(1) %></td>
		<%} %>
	</tr>
	<%count=count+1;
	} %>
</table>
</div>
</form>
</BODY>
<%}else{ %>
<%@include file="../cookieTracker/CookieTrackerBottom.jsp" %>
<%} %>
</HTML>