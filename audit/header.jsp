<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
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
<%String browserType=(String)request.getHeader("User-Agent");
	System.out.println("Browser Type "+browserType); 
	Assets_DBQueries dbVer=new Assets_DBQueries();
	String version=dbVer.BrowserVersion(browserType);
	float ver=11;
	if(!version.isEmpty())
	{
	ver=Float.parseFloat(version);
	}
	System.out.println("Version of IE "+ver);
	if(ver<11.0){%>
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
<jsp:useBean id="assetManager" class="com.gen.cms.assets.beans.Assets_DBQueries"></jsp:useBean>


<TITLE>Create New Audit Type</TITLE>
<script type="text/javascript">
function Onload()
{
	val=document.getElementsByName("FileCre")
	for(i=0;i<val.length;i++)
	{
		if(val[i].value=="")
		{
			alert("Enter Document Details");
			document.AuditTypeCreation.val[i].value.focus();
		}
	}
	document.AuditTypeCreation.action="../../AssetsServlet";
	document.AuditTypeCreation.submit();  
}
</script>
<style>
table{
	width: 100%;
	padding: 10px;
}
td{
	border-bottom: 1px soild white;
	padding: 10px;
}
caption{
	text-align:center;
	font-family: bold;
	font-size: 20px;	
}
.text
{
	background-color: #FFECEC;
}
#Layer5{
	width: 40%;
	height: 50%;
	margin: 50px auto;
	border-radius: 10px;
	background: linear-gradient(
      53deg,
      #FFECEC,
      #ffcccf,
      #BD0102
    );
    margin-top: 50px;
    animation-name: tabl;
    animation-duration:5s;
    animation-iteration-count: infinite;
}
@keyframes tabl{
	0%{
	background: linear-gradient(
      53deg,
      #FFECEC,
      #ffcccf,
      #BD0102
    );
    }
    33.33%{
	background: linear-gradient(
      53deg,
      #BD0102,
      #FFECEC,
      #ffcccf
    );
    }
    66.66%{
	background: linear-gradient(
      53deg,
      #FFECEC,
      #BD0102,
      #ffcccf
    );
     99.99%{
	background: linear-gradient(
      53deg,
      #ffcccf,
      #BD0102,
      #FFECEC
    );
}
#buton{
	border: 2px solid black;
}
.animation{
    /* background: linear-gradient(
      35deg,
      white,
      black
    ); */
    animation-name: bounce;
    animation-duration: 2s;
    animation-iteration-count: infinite;
}

@keyframes bounce {
  0% {
    color: #FFECEC;
  } 
  25% {
    color: #ffcccf;
  }
  50% {
  	color: #BD0102;
  }
  75% {
  	color: #ffcccf;
  }
  100% {
  	color: #FFECEC;
  }
} 

</style>
</HEAD>

<body>
<% 
	String msg=(String)session.getAttribute("error"); 
 	session.removeAttribute("error");
 	ArrayList mainCatArr=assetManager.getMainAssetCategory();
	%>
<form name="AuditTypeCreation" method="post">
<input type="hidden" name="ActionId" value="AuditCreation">
<input type="hidden" name="Task" value="Add">
<input type="hidden" name="op" value="1">
<input type="hidden" name="email" value="<%=email%>">
  <div class="container"> 
	<table id=Layer5  class="tbl" class="table table-striped">
		<caption>Audit Type Creation</caption>
		<tr>
			<td>
				<font size="2px">Name Audit Type</font>
			</td>
			<td>
				<input type="text" class="text form-control" placeholder="Enter File Name" name="FileCre" onkeypress="return (event.charCode!=39)" required>
			</td>
		</tr>
		<tr>
			<td style="text-align:center; vertical-align:middle;"><input type="Submit" id="buton" class="btn" value="Create" onclick="Onload()"></td>
			<td style="text-align:center; vertical-align: middle;"><input type="reset" id="buton" class="btn" value="Cancel"></td>
		</tr>
		<%if(msg!=null){ %>
			<caption><font color="red" class="animation" id="anim"> <%=msg %></font></caption>
			<%} %>
	</table>
</div>


</form>
</BODY>
<%}else{ %>
<%@include file="../cookieTracker/CookieTrackerBottom.jsp" %>
<%} %>
</HTML>