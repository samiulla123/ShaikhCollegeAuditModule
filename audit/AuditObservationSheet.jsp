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
var arr=[];
function radioButton(id)
{
	if(document.getElementById("rd"+id).checked==true)
	{
		 document.pendingPoints.cmp.value=document.getElementById("rd"+id).value;
		 document.pendingPoints.auditObId.value=id;
	}
	else if(document.getElementById("rd1"+id).checked==true)
	{
		document.pendingPoints.cmp.value=document.getElementById("rd1"+id).value;
		document.pendingPoints.auditObId.value=id;
	}
	document.pendingPoints.action="../../VisitorServlet";
	document.pendingPoints.submit(); 
	/* for(var j=0;j<arr.length;j++)
	{
		alert(j+" ValJ "+arr[j]);
		document.pendingPoints.cmp.value=arr[j];
		alert("Array Value Addded "+document.pendingPoints.cmp.value);
	} */
}
/* function reLoadData()
{	
	alert(arr);
	for(var j=0;j<arr.length;j++)
	{
		alert(j+" ValJ "+arr[j]);
		document.pendingPoints.cmp.value=arr[j];
		alert("Array Value Addded "+document.pendingPoints.cmp.value);
	}
	document.pendingPoints.action="../../VisitorServlet";
	document.pendingPoints.submit(); 
} */

function addData()
{
	/* if(document.getElementById("AuditPoint").value=="")
	{
		alert("Enter Audit Points");
		return false;
	} */
	if(document.getElementById("AuditMark").value=="")
	{
		alert("Enter audit marks");
		return false;
	}
	if(document.getElementById("audite").value=="")
	{
		alert("Enter Action By Name Name");
		return false;
	}
	if(document.getElementById("mark").value=="")
	{
		alert("Select Marks Type");
		return false;
	}
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
	
	String prgId="";
	prgId=request.getParameter("progId");
	String audiSched=request.getParameter("auditId");
	String auditType1=request.getParameter("auditTypeId");
	String auditPrevsPoint=request.getParameter("auditPreId");
	String auditdate=request.getParameter("auditCreatedDate");
	System.out.println("Audit Created Date "+auditdate);
	String previousval="0";
	if(auditPrevsPoint!=null)
	{
		previousval="1";
	}
	String prog_id="";
	String auditId="";
	String auditType="";
	String auditUniquDate="";
	try
	{
		if(prgId==null){
		//if(prgId.equals("null") || prgId=="null" || prgId==null || prgId.equals(null))
			prog_id=(String)session.getAttribute("progID");
			session.removeAttribute("progID");
			auditId=(String)session.getAttribute("audtId");
			session.removeAttribute("audtId");
			auditType=(String)session.getAttribute("auditType");
			session.removeAttribute("audtId");
			auditUniquDate=(String)session.getAttribute("auditCrDate");
			session.removeAttribute("audtId");
		}
		
		else
		{
			prog_id=prgId;
			auditId=audiSched;
			auditType=auditType1;
			auditUniquDate=auditdate;
			System.out.println("Rpog Mst Id in if "+prgId+" Audit Id in if "+audiSched);
		}
	}catch(NullPointerException e)
	{
		System.out.println("Exception "+e);
	}
	
	String msg=(String)session.getAttribute("error");
	session.removeAttribute("error");
	String auditeeName=request.getParameter("auditee");
	System.out.println("Rpog Mst Id12 "+prog_id+" Audit Id12 "+auditId+" Is Empty "+auditType+" Audit pre Point "+auditPrevsPoint);
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
<input type="hidden" name="op" value="4">
<input type="hidden" name="prePoint" value="<%=previousval%>">
<input type="hidden" name="progId" value="<%=prog_id%>">
<input type="hidden" name="auditId" value="<%=auditId%>">
<input type="hidden" name="auditType" value="<%=auditType%>"> 
<input type="hidden" name="email" value="<%=email%>"> 
<input type="hidden" name="uniqueDate" value="<%=auditUniquDate%>">
<div class="container-fluid" style="width: 50%;">
<div style="overflow-x:auto;">
<a href="#pending" ><h4>Click Here To View Pending Point</h4></a>
<%if(msg!=null){ %>
	<center><h5><%=msg %></h5></center>
<%} %>
<h5>Audit Observation Sheet</h5>

<table>
<%String prePoint=db.selectPrePoint(prog_id);%>
<tr>
	<td><label style="font-weight: bold; font-family: sans-serif;">Audit Point : </label></td>
	<td><textarea rows="4" cols="20" name="auditTextArea" id="AuditPoint" class="form-control" onpaste="return false" style="background-color:#FFECEC ; border-style: solid; border-width: 1px" tabindex="1" onkeypress="return (event.keyCode!=39)"></textarea></td>
</tr>
<tr>
	<td><label style="font-weight: bold; font-family: sans-serif;">MarksType</label></td>
	<td><select name="markType" class="form-control" id="mark" style="background-color: #FFECEC;" data-live-search="true" data-live-search-style="startsWith" class="selectpicker bd"  data-size="10">
		<option value="">select mark</option>
		<option value="1">O+</option>  
		<option value="2">AOC</option>
		<option value="3">OFI</option>
	</select></td>
</tr>
<tr>
	<td><label style="font-weight: bold; font-family: sans-serif;">Audit Marks : </label></td> 
	<td><input type="text" name="auditMark" id="AuditMark" class="form-control" onpaste="return false" style="background-color:#FFECEC ;" size="30" value=""  maxlength="25" style="border-style: solid; border-width: 1px" tabindex="1" onkeypress="return (event.keyCode > 47 && event.keyCode < 58)  || (event.keyCode > 7 && event.keyCode < 9)"></td>
</tr>
<tr>
	<td><label style="font-weight: bold; font-family: sans-serif;">Action By : </label></td> 
	<td><select name="auditAction" class="form-control" id="audite" style="background-color: #FFECEC;" data-live-search="true" data-live-search-style="startsWith" class="selectpicker bd"  data-size="10">
		<option value="">Select Auditee Name</option>
		<%
			ArrayList dep1234=db.selectAudeteeName(prog_id);
			String auditeeId="";
			String auditeeFullName="";
			for(int j=0;j<dep1234.size();j++){
			ArrayList depUn=(ArrayList)dep1234.get(j);
			auditeeId=""+depUn.get(0);
			auditeeFullName=""+depUn.get(1)+" "+depUn.get(2)+" "+depUn.get(3);  
		%>
		<option value="<%=auditeeId%>"><%=auditeeFullName %></option>
		<%} %>
	</select></td>
</tr>

<tr>
	<td><label style="font-weight: bold; font-family: sans-serif;">
	Closure Date </label>
	</td>
	<td>
    	<input type="text" name="date" id="date" class="form-control" style="background-color:#FFECEC ;" onfocus="javascript:popUpCalendar(this,document.AuditModule.date,'dd-mmmm-yyyy');">
    </td>
</tr>
<tr>
	<%String ref=db.selectScheduleRefNo(auditId);
	System.out.println("referenceNumber "+ref==null+" OR ");
	System.out.println("referenceNumber or "+ref=="");
	if(ref.isEmpty()) {%>
	<td><label style="font-weight: bold; font-family: sans-serif;">Reference Number</label></td>
	<td><input type="text" name="RefNo" id="refNo" class="form-control" style="background-color:#FFECEC ;" size="30" value=""  maxlength="25" style="border-style: solid; border-width: 1px" tabindex="1" onkeypress="return (event.keyCode!=39)">
		<a href="../referencenumber/Gen_reference_number.jsp">Click Here To Generate Reference Number</a></td>
	<%} %>
</tr>
<tr>
	<td colspan="2">
	<%if(!prePoint.isEmpty()){ %>
	<center><input type="button" value="Submit Point" id="btn_dis" class="btn" onclick="addData()">
	<%}else{ %>
	<input type="button" value="Add To List" id="btn_dis" class="btn" onclick="addData()">
	<%} %>
	<input type="reset" value="Reset" class="btn" id="btn_reset"></center></td>
</tr>
</table>
</div>
</div>

</form>
<!-- Add Form Data -->
<%
	ArrayList ListData=db.SelectAuditObservation(email, prog_id);
	System.out.println("List Data "+ListData);
 %>
<form  name="ListOfAudit" method="post" action="../../VisitorServlet">
<input type="hidden" name="beanIndicator" value="auditorSchedule">
<input type="hidden" name="task" value="ADD">
<input type="hidden" name="op" value="5">
<div class="container-fluid" style="">
<div style="overflow-x:auto;">
<center><h5>List Of Audit Points</h5></center>
	<table>
		<tr>
			<th>Sr. No.</th>
			<th width="2px">Point</th>
			<th>Marks</th>
			<th>Action By</th>
			<th>Closure Date</th>
		</tr>
		<%int count=1;
		for(int k=0; k<ListData.size(); k++)
		{%>
		<tr>
		<%
		  ArrayList List=(ArrayList)ListData.get(k);%>
			<td><%=count %>
			<input type="hidden" name="AuditObservId" value="<%=List.get(0)%>">
			 <input type="hidden" name="AuditId" value="<%=List.get(1)%>">
			<input type="hidden" name="status" value="<%=List.get(2)%>">
			<input type="hidden" name="prog_id" value="<%=List.get(3)%>"> 
			<input type="hidden" name="createdBy" value="<%=email%>">
			<input type="hidden" name="auditType" value="<%=auditType%>">
			<input type="hidden" name="ActionBy" value="<%=List.get(6)%>">
			</td>
			<td><%=List.get(4) %></td>
			<td><%=List.get(5) %></td>
			<td><%
			ArrayList dep=db.SelectEmployeName(List.get(6).toString());
			for(int j=0;j<dep.size();j++)
			{
				ArrayList depUn=(ArrayList)dep.get(j);
				auditeeId=""+depUn.get(0); 
				auditeeFullName=""+depUn.get(1)+" "+depUn.get(2)+" "+depUn.get(3); %>
		  <%} %><%=auditeeFullName %></td>
			<td><%=List.get(8)  %></td>
		</tr>
		<%count=count+1; 
		}%>
		<tr>
			<td colspan="6"><center><input type="submit" value="create" class="btn"></center></td>
		</tr>
	</table>
</div>
</div>
</form>
<form  name="pendingPoints" method="post" >
<input type="hidden" name="beanIndicator" value="auditorSchedule">
<input type="hidden" name="task" value="ADD">
<input type="hidden" name="op" value="7">
<input type="hidden" name="progId" value="<%=prog_id%>">
<input type="hidden" name="auditId" value="<%=auditId%>">
<input type="hidden" name="auditType" value="<%=auditType%>"> 
<input type="hidden" name="cmp" value="">
<input type="hidden" name="auditObId" value="">
<div class="container-fluid" id="pending" style="width: 80%;">
<div style="float: left; overflow-x:auto;">
<center><h5>Pending Points</h5></center>
	<table>
		<tr>
		<th>Sl No</th>
		<th>Points</th>
		<th>Closure Date</th>
		<th>Complete Date</th>
		<th>Action By</th>
		<th>Status</th>
		<th>Approve</th></tr>
			<%ArrayList arr=db.AuditselectPendingPoints(email, prog_id); %>
			<%
			int n=1; 
			 System.out.println("Pending Point "+arr);
			for(int k=0;k<arr.size();k++){ 
			ArrayList pending=(ArrayList)arr.get(k);
			ArrayList points=db.SelectAuditPoints(pending.get(2).toString());
			System.out.println("Points whcih are pending "+points);
			for(int j=0;j<points.size();j++){
			ArrayList Li=(ArrayList)points.get(j);
			System.out.println("ArrayList "+Li.get(0));
			%>
		<tr>
		<td><%=n %></td>
		<td><%=Li.get(0) %></td>
		<td><%=Li.get(1) %></td>
		<td><%=pending.get(11) %></td>
			<%
			ArrayList dep=db.SelectEmployeName(pending.get(10).toString());
			for(int p=0;p<dep.size();p++)
			{
				ArrayList depUn=(ArrayList)dep.get(p);
				auditeeId=""+depUn.get(0); 
				auditeeFullName=""+depUn.get(1)+" "+depUn.get(2)+" "+depUn.get(3); %>
		  <%} %>
		<td><%=auditeeFullName %></td>
		<%if(pending.get(4).equals("0")) {%>
		<td>Pending</td>
		<%}else if(pending.get(4).equals("1")){ %>
		<td>Completed</td>
		<%} %>
		<%-- <td>Completed: <input type="radio" name="Complt" id="rd<%=pending.get(0)%>" value="1" onclick="radioButton('<%=pending.get(0) %>')">&nbsp; &nbsp; &nbsp; &nbsp; Not-Completed <input type="radio" name="Complt" id="rd1<%=pending.get(0)%>" value="0" onclick="radioButton('<%=pending.get(0) %>')"></td> --%>
		<td>
		<%-- <input type="hidden" name="auditObId123" value="<%=pending.get(0)%>"> --%>
		<%if(pending.get(4).toString().equals("1")){ %>
			Completed: <input type="radio" name="Complt" id="rd<%=pending.get(0)%>" value="1" onclick="radioButton('<%=pending.get(0) %>')">&nbsp; &nbsp; &nbsp; &nbsp;
			 Not-Completed <input type="radio" name="Complt" id="rd1<%=pending.get(0)%>" value="0" onclick="radioButton('<%=pending.get(0) %>')">
		 <%}else { %>
		 	<h5>Not Completed</h5>
		 <%} %></td>
		</tr>
		<%}
		n=n+1;} %>
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