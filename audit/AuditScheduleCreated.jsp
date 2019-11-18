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
	String depar=request.getParameter("dep");
	String msg=(String)session.getAttribute("error");
	session.removeAttribute("error");
	System.out.println("Department id "+depar);
	String audit=request.getParameter("type");
	String auditorId=request.getParameter("facId");
	String depUnit=request.getParameter("dep");
	String auditeeName=request.getParameter("auditee");
	System.out.println("Audit Type "+audit+" Auditore ID "+auditorId+" dePUNIT "+depUnit+" auditeeName "+auditeeName);
	AuditDBQuery db=new AuditDBQuery();
	ArrayList ar=new ArrayList();
	ar=db.selectAuditType();
	int i=0;
 %>
<body>
<div style="float: left; width: 100%" class="container-fluid">
<form name="AuditModule" method="post">
<input type="hidden" name="beanIndicator" value="auditorSchedule">
<input type="hidden" name="task" value="ADD">
<input type="hidden" name="op" value="2">
<input type="hidden" name="email" value="<%=email%>"> 
<div style="float: left; width: 50%;">
<div style=" float: left; overflow-x:auto;">
<%if(msg!=null){ %>
	<center><h5 style="color: red;"><%=msg %></h5></center>
<%} %>
<h5>Audit Schedule</h5>
<table class="table-stripped">
	<%ArrayList aud=db.auditScheduleLink(email);
	System.out.println("Audit Details "+aud);
	if(!aud.equals(null))
	{
		for(int j=0;j<aud.size();j++)
		{
			ArrayList dep=(ArrayList)aud.get(j);
			ArrayList depdetails=db.FetchUnitName(dep.get(3).toString());
			System.out.println("Department Details "+depdetails);%>
			<tr>
				<td><a href="AuditObservationSheet.jsp?progId=<%=dep.get(3)%>&auditId=<%=dep.get(0)%>&auditTypeId=<%=dep.get(1)%>&auditCreatedDate=<%=dep.get(10)%>"><%=depdetails.get(0).toString().replace("[","").replace("]","") %> (<%=dep.get(10) %>)</a></td>
			</tr>
	  <%}
	}%>
</table>
</div>
</div>
</form>
</div>
</BODY>
<%}else{ %>
<%@include file="../cookieTracker/CookieTrackerBottom.jsp" %>
<%} %>
</HTML>