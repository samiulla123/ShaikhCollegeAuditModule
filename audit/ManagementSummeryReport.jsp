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


<TITLE>Audit Management Report</TITLE>
<script type="text/javascript">
function addData()
{
	if(document.getElementById("type").value=="")
	{
		alert("Select Audit Type");
		document.AuditModule.type.focus();
		return false;
	}
}
function reLoad()
{
	document.ManagmentReport.action="";
	document.ManagmentReport.submit();
}
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
  border: 2px solid green;
  padding: 8px;
  font-weight: bold;
  font-size: 100%;
}

tr:nth-child(even) {
  background-color: #dddddd;
  border: 2px solid black;
}
</style>
<%
String audit=request.getParameter("type");
System.out.println("Audit Report "+audit);
AuditDBQuery db=new AuditDBQuery();
ArrayList ar=new ArrayList();
int i=0;
DateManager dt=new DateManager();
String date=dt.getCurrentDate();
System.out.println("Date "+date);
int year=Integer.parseInt(date.substring(8,10));
System.out.println("Year "+year);
int previous=year-1;
int lastYr=previous-1;
System.out.println("Previous Year "+previous);
%>
</HEAD>
<body>
<form  name="ManagmentReport" method="post" >
<input type="hidden" name="beanIndicator" value="auditorSchedule">
<input type="hidden" name="task" value="ADD">
<div class="container">
<h5>Audit Report</h5>
<center>
	<div style="width: 50%; border: 2px solid black; background-color: #DB9796; ">
		<label style="font-size: 13px; font-family: sans-serif; font-weight: bold; color: white;">Audit Type : </label>
		<select name="type" class="form-control" id="type"  style="background-color: #FFECEC;" data-live-search="true" data-live-search-style="startsWith" class="selectpicker bd"  data-size="10" onchange="reLoad()">
			<option value="">Select Audit Type</option>
			<%ar=db.selectAuditType();
			for(i=0;i<ar.size();i++){
				ArrayList res=(ArrayList)ar.get(i);
			%>
			<option value="<%=res.get(0)%>" <%if(res.get(0).equals(audit)){ %>selected<%} %>><%=res.get(1)%></option>
			<%} %>
		</select>
	</div></center>
	<div style="border: 3px solid green; ">
	<table>
		<tr>
			<th width="1%" style="color: white;">Sl.No.</th>
			<th width="3%" style="color: white;">Unit/Dept</th>
			<th width="8%" style="color: white;">Avg. <%=lastYr%>-<%=previous %>
			<table>
					<tr>
						<td width="2%" style="color: white;">O+</td>
						<td width="2%" style="color: white;">AOCs</td>
						<td width="2%" style="color: white;">OFIs</td>
						<td width="2%" style="color: white;">Marks %</td>
					</tr>
				</table></th>
			<%ArrayList quater=db.SelectAuditSchduleDate();
			for(int q=0;q<quater.size();q++)
			{%>
			<th width="8%" style="color: white;">Q<%=q+1%> <%=previous %>-<%=year %>
				<table>
					<tr>
						<td width="2%" style="color: white;">O+</td>
						<td width="2%" style="color: white;">AOCs</td>
						<td width="2%" style="color: white;">OFIs</td>
						<td width="2%" style="color: white;">Marks %</td>
					</tr>
				</table></th>
			<%} %>
			<th width="8%" style="color: white;">Avg. <%=previous%>-<%=year%>
			<table>
					<tr>
						<td width="2%" style="color: white;">O+</td>
						<td width="2%" style="color: white;">AOCs</td>
						<td width="2%" style="color: white;">OFIs</td>
						<td width="2%" style="color: white;">Marks %</td>
					</tr>
				</table></th>
		</tr>
		<%ArrayList are=db.SelectDepartmentProgId(audit);
		System.out.println("Array data Found Details===== "+are);
		int count=1;
		for(int r=0;r<are.size();r++)
		{
			int totalOplus=0;
			int ttlAOC=0;
			int ttlOFI=0;
			int ttlmarks=0;
			ArrayList dep=(ArrayList)are.get(r);%>
		<tr>
			<td><%=count %></td>
			<%String shName=db.DepartmentShortName(dep.get(0).toString()); %>
			<td><%=shName %>
			<%ArrayList preYr=db.ArrayListCalPreVal(audit, dep.get(0).toString()); 
			System.out.println("Previous Year "+preYr);
			int preOpl=0;
			int preAOC=0;
			int preOFI=0;
			int preMark=0;
			for(int p=0;p<preYr.size();p++)
			{
				ArrayList markPr=(ArrayList)preYr.get(p);%>
				<%if(markPr.get(0).equals("1")){ 
					preOpl+=Integer.parseInt(markPr.get(1).toString());%>
				<%}else{ %>
				<%} if(markPr.get(0).equals("2")){
					preAOC+=Integer.parseInt(markPr.get(1).toString());%>
				<%}else{ %>
				<%} if(markPr.get(0).equals("3")){
				preOFI+=Integer.parseInt(markPr.get(1).toString());%>
				<%}else{ %>
				<%} %>
			<%
				} ArrayList Size=db.SelectAuditPreSchduleDate();
				if(Size.size()!=0)
				{
					preMark+=preOpl+preAOC+preOFI;
					preOpl/=Size.size();
					preAOC/=Size.size();
					preOFI/=Size.size();
					preMark/=Size.size();
				}
			%>
			<td>
				<table>
					<tr>
						<td><%=preOpl %></td>
						<td><%=preAOC %></td>
						<td><%=preOFI %></td>
						<td><%=preMark %></td>
					</tr>
				</table>	
			</td>
			<%for(int q=0;q<quater.size();q++)
			{ 
			ArrayList dates=(ArrayList)quater.get(q);
			 ArrayList auditObData=db.selectAuditObservationDetails(dep.get(0).toString(),dates.get(0).toString()); %>
			<td>
				<table>
				<%
					int Oplus=0;
					int AOC=0;
					int OFI=0;
					int marks=0;
					for(int a=0;a<auditObData.size();a++){ 
					ArrayList li=(ArrayList)auditObData.get(a);%>
						<%if(li.get(0).equals("1")){ 
							Oplus+=Integer.parseInt(li.get(1).toString());%>
						<%}else{ %>
						<%} if(li.get(0).equals("2")){
							AOC+=Integer.parseInt(li.get(1).toString());%>
						<%}else{ %>
						<%} if(li.get(0).equals("3")){
						OFI+=Integer.parseInt(li.get(1).toString());%>
						<%}else{ %>
						<%} %>
					<%}marks=Oplus+AOC+OFI;
					totalOplus+=Oplus;
					ttlAOC+=AOC;
					ttlOFI+=OFI;
					ttlmarks+=marks;%>
					<tr>
						<td><%=Oplus %></td>
						<td><%=AOC %></td>
						<td><%=OFI %></td>
						<td><%=marks %></td>
					</tr>
				</table>	
			</td>
			<%} %>
			<td>
				<table>
						<tr>
							<%
							float OplusAvg=totalOplus / quater.size(); %>
							<td><%=OplusAvg %></td>
							<%float AOCAvg=ttlAOC / quater.size(); %>
							<td><%=AOCAvg %></td>
							<%float OFIAvg=ttlOFI / quater.size(); %>
							<td><%=OFIAvg %></td>
							<%float MarkAvg=ttlmarks / quater.size(); %>
							<td><%=MarkAvg %></td>
						</tr>
				</table>
			</td>
		</tr>
		<%count++;
		} %>
	</table>
	</div>
</div>
</form>
</BODY>
<%}else{ %>
<%@include file="../cookieTracker/CookieTrackerBottom.jsp" %>
<%} %>
</HTML>