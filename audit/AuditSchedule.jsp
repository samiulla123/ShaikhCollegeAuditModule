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
	if(document.getElementById("type").value=="")
	{
		alert("Select Audit Type");
		document.AuditModule.type.focus();
		return false;
	}
	if(document.getElementById("facId").value=="")
	{
		alert("Select Faculty Id");
		document.AuditModule.facId.focus();
		return false;
	}
	if(document.getElementById("dep").value=="")
	{
		alert("Select Department/Unit");
		document.AuditModule.dep.focus();
		return false;
	}
	if(document.getElementById("audite").value=="")
	{	
		alert("Select auditee Name");
		document.AuditModule.auditee.focus();
		return false;
	}
	if(document.getElementById("date").value=="")
	{
		alert("date");
		document.AuditModule.audit_date.focus();
		return false;
	}
	if(document.getElementById("from").value=="")
	{
		alert("Select time");
		document.AuditModule.from.focus();
		return false;
	}
	if(document.getElementById("to").value=="")
	{
		alert("select time");
		document.AuditModule.to.focus();
		return false;
	}
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
<div style="float: left; width: 100%">
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
<h5>Audit Schedule Form</h5>
<table>
<tr>
	<td><label style="font-weight: bold; font-family: sans-serif;">Audit Type : </label></td>
	<td><select name="type" class="form-control" id="type"  style="background-color: #FFECEC;" data-live-search="true" data-live-search-style="startsWith" class="selectpicker bd"  data-size="10" onchange="reLoad()">
		<option value="">Select Audit Type</option>
		<%ar=db.selectAuditType();
		for(i=0;i<ar.size();i++){
			ArrayList res=(ArrayList)ar.get(i);
		%>
		<option value="<%=res.get(0)%>" <%if(res.get(0).equals(audit)){ %>selected<%} %>><%=res.get(1)%></option>
		<%} %>
	</select></td>
</tr>
<tr>
	<td><label style="font-weight: bold; font-family: sans-serif;">Auditor Name : </label></td> 
	<td><select name="facId" class="form-control" id="facId" style="background-color: #FFECEC;" data-live-search="true" data-live-search-style="startsWith" class="selectpicker bd"  data-size="10" onchange="reLoad()">
	<option value="">Select Auditor Name</option>
		<%
		String fac_id="";
		String fName="";
		String mName="";
		String lName="";
		String FullName="";
		String sh_name="";
		ArrayList assoc=db.SlectDepAssociate(email);
		for(int k=0;k<assoc.size();k++)
		{
			ArrayList asocN=(ArrayList)assoc.get(k);
			fac_id=""+asocN.get(0);
			fName=""+asocN.get(1);
			mName=""+asocN.get(2);
			lName=""+asocN.get(3);
			sh_name=""+asocN.get(4);
			FullName=""+fName+" "+mName+" "+lName;
			System.out.println("Auditor Name "+FullName);%>
		<option value="<%=fac_id%>" <%if(fac_id.equals(auditorId)){ %>selected<%} %>><%=FullName %></option>
	  <%} %>
	</select></td>
</tr>
<tr>
	<td><label style="font-weight: bold; font-family: sans-serif;">Select Unit/Department : </label></td>
	<td><select name="dep" class="form-control" id="dep" style="background-color: #FFECEC;" data-live-search="true" data-live-search-style="startsWith" class="selectpicker bd"  data-size="10" onchange="reLoad()">
		<option value="">Select Department</option>
		<%ArrayList dep=db.selectDepartment();
	System.out.println("Department details "+dep); 
	for(int j=0;j<dep.size();j++){
	ArrayList depUn=(ArrayList)dep.get(j);
	%>
		<option value="<%=depUn.get(0)%>" <%if(depUn.get(0).equals(depUnit)){ %>selected<%} %>><%=depUn.get(1) %></option>
		<%} %>
	</select></td>
</tr>
<tr> 
	<td><label style="font-weight: bold; font-family: sans-serif;"> Auditee Name: </label></td>
	<td><select name="auditee" class="form-control" id="audite" style="background-color: #FFECEC;" data-live-search="true" data-live-search-style="startsWith" class="selectpicker bd"  data-size="10" onchange="reLoad()">
		<option value="">Select Auditee Name</option>
		<%
		System.out.println("Department Name "+depUnit);
		ArrayList dep1234=db.selectAudeteeName(depUnit);
	System.out.println("Department details== "+dep1234); 
	String auditeeId="";
	String auditeeFullName="";
	for(int j=0;j<dep1234.size();j++){
	ArrayList depUn=(ArrayList)dep1234.get(j);
	auditeeId=""+depUn.get(0);
	auditeeFullName=""+depUn.get(1)+" "+depUn.get(2)+" "+depUn.get(3);  
	%>
		<option value="<%=auditeeId%>" <%if(auditeeId.equals(auditeeName)) {%>selected<%} %>><%=auditeeFullName %></option>
		<%} %>
	</select></td>
</tr>
<tr>
	<td><label style="font-weight: bold; font-family: sans-serif;">
		Date </label>
	</td>
	<td>
    	<input type="text" name="audit_date" id="date" class="form-control" style="background-color:#FFECEC ;" onfocus="javascript:popUpCalendar(this,document.AuditModule.audit_date,'dd-mmmm-yyyy');">
    </td>
</tr>
<tr>
	<td><label style="font-weight: bold; font-family: sans-serif;">Time</label></td>
	<td><label>From</label> <select name="from" id="from" class="" style="background-color: #FFECEC; width: 100px;">
	<option value="">Hours</option>
	<%for(int j=1;j<=24;j++){
		if(j<=9){%>
		<option value="<%="0"+j%>"><%="0"+j%></option>
		<%}else{ %>
		<option value="<%=j%>"><%=j%></option>
		<%} %>
	<%} %></select>
	<label> To </label><select name="to" id="to" class="" style="background-color: #FFECEC; #FFECEC; width: 100px;">
	<option value="">Minute</option>
	<%for(int j=1;j<=60;j++){
		if(j<=9){%>
		<option value="<%="0"+j%>"><%="0"+j%></option>
		<%}else { %>
		<option value="<%=j%>"><%=j%></option>
		<%} %>
	<%} %></select></td>
</tr>
<tr>
	<td><label style="font-weight: bold; font-family: sans-serif;">Previous Audit Point</label></td>
	<td><select name="PreviousPoint" id="to" class="" style="background-color: #FFECEC; #FFECEC; width: 100px;">
	<%int ar2=db.auditObsrStatus(depUnit, audit);
	System.out.println("Audit Points "+ar2); 
	if(ar2==0){%>
		<option value="">No Previous Point</option>
	<%}else{ 
		String auditT=db.selectAuditSingleType(audit);%>
		<option value="<%=audit%>"><%=auditT %></option></select>
	<%} %></td>
</tr>
<tr>
	<td colspan="2">
	<center><input type="button" value="Add To List" id="btn_dis" class="btn" onclick="addData()">
	<input type="reset" vlaue="Reset" class="btn" id="btn_reset"></center></td>
</tr>
</table>
</div>
</div>
</form>
<!-- Add Form Data -->
<%
	ArrayList ListData=db.FetchAuditList(email);
	System.out.println("List Data "+ListData);
 %>
<form  name="ListOfAudit" method="post" action="../../VisitorServlet">
<input type="hidden" name="beanIndicator" value="auditorSchedule">
<input type="hidden" name="task" value="ADD">
<input type="hidden" name="op" value="3">
<div style="float: right; width: 50%;" >
<div style="float: left; overflow-x:auto;">
<center><h5>List Of Audit Department</h5></center>
	<table>
		<tr>
			<th>Sr. No.</th>
			<th width="2px">Unit</th>
			<th>Auditee</th>
			<th>Auditor</th>
			<th>Date</th>
			<th>Time</th>
		</tr>
		<%int count=1;
		for(int k=0; k<ListData.size();k++){  %>
		<tr>
		<%
			System.out.println("List Data ");
		  ArrayList List=(ArrayList)ListData.get(k);
		  System.out.println("Value "+List.get(k));%>
			<td><%=count %>
			<input type="hidden" name="AuditId" value="<%=List.get(0)%>"></td>
			<%
				ArrayList ad=db.FetchUnitName(List.get(3).toString());
				String unitName="";
				if(ad.size()>0)
				{
					unitName=ad.get(0).toString().replace("[","").replace("]","");
				}
			%>
			<td><%=unitName %></td>
			<%
				ArrayList AuditeeName=db.FetchFacultyShName(List.get(4).toString());
				System.out.println("Auditeeer Name "+AuditeeName);
				String auditeeSHName="";
				if(ad.size()>0)
				{
					auditeeSHName=AuditeeName.get(0).toString().replace("[","").replace("]","");
				}
			 %>
			<td><%=auditeeSHName.toUpperCase() %></td>
			<%
				ArrayList AuditorName=db.FetchFacultyShName(List.get(5).toString());
				System.out.println("Auditor Name "+AuditorName);
				String auditorSHName="";
				if(ad.size()>0)
				{
					auditorSHName=AuditorName.get(0).toString().replace("[","").replace("]","");
				}
			 %>
			<td><%=auditorSHName.toUpperCase() %></td>
			<td><%=List.get(6) %></td>
			<td><%=List.get(7) %> to <%=List.get(8) %></td>
		</tr>
		<%count=count+1;} %>
		<tr>
			<td colspan="6"><center><input type="submit" value="create" class="btn"></center></td>
		</tr>
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