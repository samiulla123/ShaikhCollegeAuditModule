<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<HTML>
<HEAD>
<%@ page language="java" import="java.util.*"
	contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ include file="../cookieTracker/CookieTrackerTop.jsp"%>
<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/css/bootstrap-select.css" />
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/bootstrap-select/1.12.1/js/bootstrap-select.js"></script>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="../../stylesheet/RedLeft.css" rel="stylesheet"
	type="text/css">
<script src="../../javascript/ForAll.js" language="javascript">
</script>

<%@ page import="com.gen.cms.faculty.dao.FacultyManager,com.gen.cms.admin.dao.AdminManager,com.gen.cms.performance.dao.PerformanceManager"%>
<%@ page import="com.gen.cms.faculty.dao.FacultyDAO,com.gen.cms.admin.dao.DmsManager"%>

<TITLE>Admission Menu</TITLE>
<%
 	DmsManager dm=new DmsManager();
 	String progid=dm.getProgId(email);
    String staffId = email;	
     
	session.setAttribute("user","guest");
	ArrayList modules=(ArrayList)session.getAttribute("modules");	
	String userFname=""+session.getAttribute("userFname");
	String initials=""+session.getAttribute("initials");
	String url="../../gateWay.jsp?initials="+initials+"&userFname="+userFname+"&modules="+modules; 
%>
<script language="JavaScript">
<!--
function FP_preloadImgs() {//v1.0
 var d=document,a=arguments; if(!d.FP_imgs) d.FP_imgs=new Array();
 for(var i=0; i<a.length; i++) { d.FP_imgs[i]=new Image; d.FP_imgs[i].src=a[i]; }
} 
// -->
</script>
<base target="main">
<!-- <script type="text/javascript">
var img1, img2;
	img1 = new Image();
	img1.src = "../../images/closedfolder.gif";
	img2 = new Image();
	img2.src = "../../images/openfolder.gif";
	var targetElementPrevious ,srcElementPrevious, targetIdPrevious;
	function doOutline() {
	  var targetId, srcElement, targetElement;
	  srcElement = window.event.srcElement;
	  if (srcElement.className == "LEVEL1" || srcElement.className == "LEVEL3" ) 
	  {
	    srcElement = srcElement.id
	    srcElement = srcElement.substr(0, srcElement.length-1);
	    targetId = srcElement + "s";
	    srcElement = srcElement + "i";
	    srcElement = document.all(srcElement);
        targetElement = document.all(targetId);
	   if (targetElementPrevious) 
		{
	      if (targetElementPrevious != targetElement) 
		  {  
				if (targetElementPrevious.style.display == "" && srcElement.className != "LEVEL3") 
		 		 {  
			        targetElementPrevious.style.display = "none"; 
	        		srcElementPrevious.src = "../../images/down.gif";	
	      		 }
      	  }
	    }
	    if (targetElement) {
	      if (targetElement.style.display == "none") {
	        targetElement.style.display = "";   
	        srcElement.src = "../../images/up.gif";
			targetElementPrevious=targetElement;
			srcElementPrevious=srcElement;
	      } else {
	        targetElement.style.display = "none";  
	        srcElement.src = "../../images/down.gif";
	      }
		    }
		  }
		}
	document.onclick = doOutline;

function FP_swapImg() {//v1.0
 var doc=document,args=arguments,elm,n; doc.$imgSwaps=new Array(); for(n=2; n<args.length;
 n+=2) { elm=FP_getObjectByID(args[n]); if(elm) { doc.$imgSwaps[doc.$imgSwaps.length]=elm;
 elm.$src=elm.src; elm.src=args[n+1]; } }
}
function FP_getObjectByID(id,o) {//v1.0
 var c,el,els,f,m,n; if(!o)o=document; if(o.getElementById) el=o.getElementById(id);
 else if(o.layers) c=o.layers; else if(o.all) el=o.all[id]; if(el) return el;
 if(o.id==id || o.name==id) return o; if(o.childNodes) c=o.childNodes; if(c)
 for(n=0; n<c.length; n++) { el=FP_getObjectByID(id,c[n]); if(el) return el; }
 f=o.forms; if(f) for(n=0; n<f.length; n++) { els=f[n].elements;
 for(m=0; m<els.length; m++){ el=FP_getObjectByID(id,els[n]); if(el) return el; } }
 return null;
}


</script> -->
<script language=javascript>
function register(){
	document.f.submit();
}
</script>
<style>
	 ul {
  list-style-type: none;
  margin: 0;
  padding: 0;
}
li a {
  display: block;
  color: black;
  padding: 5px 7px;
  text-decoration: none;
  font-family: Calibri;
  font-size: 13;
  width: 100%;
  font-weight:bold;
  padding: 5px, 7px;
   list-style-position: outside;
}

li a:hover
{
	list-style-type: none;
}

ul li ul.dropdown{
        min-width: 100%; /* Set width of the dropdown */
        background: #DB9796;
        display: none;
        position: absolute;
        z-index: 999;
        left: 10px;
    }
    ul li:hover ul.dropdown{
        display: block;	/* Display the dropdown */
    }
    ul li ul.dropdown li{
        display: block;
    }
</style>
</head>

<body style="">
<input type="hidden" name="staffId" value="<%=staffId%>">

<div>
<%-- 	<%
	AdminManager admmgr=new AdminManager(); 
	FacultyManager fm=new FacultyManager();
	int batch=fm.getCurrBatchId();
	FacultyDAO fd=new FacultyDAO();
	ArrayList cotype=new ArrayList();
	session.setAttribute("email",email);
	String sql="select distinct(co_type) from dir_asn_co where admsn_fac_id='"+email+"'" 
				+" and co_status=1 and batch_mst_id="+batch+" and co_acpt_status=2";
	try{
	cotype=fd.select(sql);
	}catch(Exception e){
	e.printStackTrace();
	}
	String sql1="select kra_id from pa_key_result_area where user_id='"+email+"' and batch_id="+batch;
	String kraId="";
	try{
	kraId=fd.selectText(sql1);
	}catch(Exception e){
	e.printStackTrace();
	}
	String sql2="select apr_status from pa_key_result_area where kra_id="+kraId;
	String aprStat="";
	try{
	aprStat=fd.selectText(sql2);
	}catch(Exception e){
	e.printStackTrace();
	}
	%>
		<%
		Iterator ritr=cotype.iterator();
     	while(ritr.hasNext()){
      		 ArrayList tArr=(ArrayList)ritr.next();
      		 Iterator citr=tArr.iterator();
      		 if(citr.hasNext()){
      		 	String ctype=""+citr.next();
      		 	System.out.println("hhhh "+ctype);
      		 	if(ctype.equals("1")){
		%>
	<div class="topnav" id="myTopnav">
	<ul>
		<%}if(ctype.equals("3")){%>
		<li><input type="hidden" value="staff" name="desig"> 
			<a href="../../CmsController?moduleIndicator=FACULTY&task=VIEW&beanIndicator=GOTOVIEW&COTYPE=3&desig=staff"
				target="_parent" onclick="change(this)"><img src="../../images/left.gif" width="12" height="12" /> Coordinator View </a></td>
		</li>
				<% }
			}
		}%>
		<li><a href="Add_New_File.jsp" 
				onclick="change(this)">Create New Document Type File</a></li>
		<li><a href="Gen_reference_number.jsp" 
				onclick="change(this)">Generate Reference Number</a></li>
		<li><a href="Reference_Register.jsp"
				onclick="change(this)">Reference Report</a></li>
		<li> <a href="#"><img src="../../images/left.gif" width="12" height="12" />Products &#9662;</a>
			<ul class="dropdown">
				<li><a><img src="../../images/left.gif" width="12" height="12" />Hi</a></li>
				<li><a><img src="../../images/left.gif" width="12" height="12" />Hello</a></li>
				<li><a><img src="../../images/left.gif" width="12" height="12" />World</a></li>
			</ul>
		</li>
	</ul>
	</div> --%>
 <TABLE >
	<TBODY>
		<%
	AdminManager admmgr=new AdminManager(); 
	FacultyManager fm=new FacultyManager();
	int batch=fm.getCurrBatchId();
	FacultyDAO fd=new FacultyDAO();
	ArrayList cotype=new ArrayList();
	session.setAttribute("email",email);
	String sql="select distinct(co_type) from dir_asn_co where admsn_fac_id='"+email+"'" 
				+" and co_status=1 and batch_mst_id="+batch+" and co_acpt_status=2";
	try{
	cotype=fd.select(sql);
	}catch(Exception e){
	e.printStackTrace();
	}
	String sql1="select kra_id from pa_key_result_area where user_id='"+email+"' and batch_id="+batch;
	String kraId="";
	try{
	kraId=fd.selectText(sql1);
	}catch(Exception e){
	e.printStackTrace();
	}
	String sql2="select apr_status from pa_key_result_area where kra_id="+kraId;
	String aprStat="";
	try{
	aprStat=fd.selectText(sql2);
	}catch(Exception e){
	e.printStackTrace();
	}
	%>
		<%
		Iterator ritr=cotype.iterator();
     	while(ritr.hasNext()){
      		 ArrayList tArr=(ArrayList)ritr.next();
      		 Iterator citr=tArr.iterator();
      		 if(citr.hasNext()){
      		 	String ctype=""+citr.next();
      		 	System.out.println("hhhh "+ctype);
      		 	if(ctype.equals("1")){
		%>
		<%}if(ctype.equals("3")){%>
		<tr>
			<td nowrap="" style="width: 240px">&nbsp;<input type="hidden" value="staff" name="desig"> <img
				src="../../images/left.gif" width="12" height="12" /> <!--<a href="../../CmsController?moduleIndicator=FACULTY&task=VIEW&beanIndicator=COORDINATORVIEW&COTYPE=3&FACID=<%//=email%>" target="indexCenter" onclick="change(this)">-->
			<a href="../../CmsController?moduleIndicator=FACULTY&task=VIEW&beanIndicator=GOTOVIEW&COTYPE=3&desig=staff"
				target="_parent" onclick="change(this)"> Coordinator View </a></td>
		</tr>
				<% }
			}
		}%>
		<TR>
			<TD nowrap height="0">&nbsp; <IMG border="0"
				src="../../images/left.gif" width="12" height="12">&nbsp; <!--<A
				href="MasterCreation.jsp" target="indexCenter" onclick="change(this)">View Master Creation of Assets</A></TD>-->
			<A href="AuditTypeCreation.jsp" target="indexCenter"
				onclick="change(this)">Create Audit Type</A></TD>
		</TR>
 		<TR>
			<TD nowrap height="0">&nbsp; <IMG border="0"
				src="../../images/left.gif" width="12" height="12">&nbsp; <!--<A
				href="MasterCreation.jsp" target="indexCenter" onclick="change(this)">View Master Creation of Assets</A></TD>-->
			<A href="AuditSchedule.jsp" target="indexCenter"
				onclick="change(this)">Audit Schedule</A></TD>
		</TR>
		<TR>
			<TD nowrap height="0">&nbsp; <IMG border="0"
				src="../../images/left.gif" width="12" height="12">&nbsp; <!--<A
				href="AuditObservationSheet.jsp" target="indexCenter" onclick="change(this)">View Master Creation of Assets</A></TD>-->
			<A href="AuditScheduleCreated.jsp" target="indexCenter"
				onclick="change(this)">Audit Points</A></TD>
		</TR>
		<!-- <TR>
			<TD nowrap height="0">&nbsp; <IMG border="0"
				src="../../images/left.gif" width="12" height="12">&nbsp; <A
				href="AuditObservationSheet.jsp" target="indexCenter" onclick="change(this)">View Master Creation of Assets</A></TD>
			<A href="AuditObservationSheet.jsp" target="indexCenter"
				onclick="change(this)">Audit Observation Sheet</A></TD>
		</TR> -->
		<TR>
			<TD nowrap height="0">&nbsp; <IMG border="0"
				src="../../images/left.gif" width="12" height="12">&nbsp; <!--<A
				href="MasterCreation.jsp" target="indexCenter" onclick="change(this)">View Master Creation of Assets</A></TD>-->
			<A href="audit_status_close.jsp" target="indexCenter"
				onclick="change(this)">Audit Pending point</A></TD>
		</TR>
		<TR>
			<TD nowrap height="0">&nbsp; <IMG border="0"
				src="../../images/left.gif" width="12" height="12">&nbsp; <!--<A
				href="MasterCreation.jsp" target="indexCenter" onclick="change(this)">View Master Creation of Assets</A></TD>-->
			<A href="AuditReportDepartmentWise.jsp" target="indexCenter"
				onclick="change(this)">Audit Report</A></TD>
		</TR>
		<!-- <TR>
			<TD nowrap height="0">&nbsp; <IMG border="0"
				src="../../images/left.gif" width="12" height="12">&nbsp; <A
				href="MasterCreation.jsp" target="indexCenter" onclick="change(this)">View Master Creation of Assets</A></TD>
			<A href="AuditSummaryReport.jsp" target="indexCenter"
				onclick="change(this)">Audit Summary Report</A></TD>
		</TR> -->
		<TR>
			<TD nowrap height="0">&nbsp; <IMG border="0"
				src="../../images/left.gif" width="12" height="12">&nbsp; <!--<A
				href="MasterCreation.jsp" target="indexCenter" onclick="change(this)">View Master Creation of Assets</A></TD>-->
			<A href="ManagementSummeryReport.jsp" target="indexCenter"
				onclick="change(this)">Management Report</A></TD>
		</TR>
		<!-- <TR>
			<TD nowrap height="0">&nbsp; <IMG border="0"
				src="../../images/left.gif" width="12" height="12">&nbsp; <A
				href="MasterCreation.jsp" target="indexCenter" onclick="change(this)">View Master Creation of Assets</A></TD>
			<A href="main.jsp" target="indexCenter"
				onclick="change(this)">Main</A></TD>
		</TR> -->
	</TBODY>
</TABLE> 
</div>
</BODY>
<%} else {%>
<%@ include file="../cookieTracker/CookieTrackerBottom.jsp"%>
<%}
%>
</HTML>