package com.gen.cms.controllers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.*;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import javazoom.upload.MultipartFormDataRequest;
import javazoom.upload.UploadBean;
import javazoom.upload.UploadFile;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;

import com.gen.cms.admission.dao.AdmissionManager;
import com.gen.cms.admin.dao.AdminManager;
import com.gen.cms.assets.beans.AssetsMasterVal;
import com.gen.cms.assets.beans.Assets_DBQueries;
import com.gen.cms.assets.beans.AuditDBQuery;
import com.gen.cms.assets.beans.Digital_signDBQuery;
import com.gen.cms.assets.beans.ReferenceNo_DBQuery;
import com.gen.cms.assets.beans.Sub_Cat_GS;
import com.gen.cms.director.beans.Meeting;
import com.gen.cms.visitor.dao.VisitorDAO;
import com.gen.cms.visitor.dao.VisitorManager;
import com.gen.cms.exceptions.CmsGeneralException;
import com.gen.cms.exceptions.CmsNamingException;
import com.gen.cms.exceptions.CmsSQLException;
import java.util.Date;
import com.gen.cms.util.*;
import com.ibm.crypto.pkcs11impl.provider.Session;

/**
 * @version 	1.0
 * @author
 */
public class VisitorServlet extends HttpServlet {

	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	private final int NO_SUCH_BEAN = 111;
	private final int NO_SUCH_TASK = 222;
	private String[] tasks = { "", "ADD", "VIEW", "UPDATE", "DELETE" };
	String[] Char={"A","B","C","D","E","F","G","H","I","J","K","L","M","N","O","P","Q","R","S","T","U","V","W","X","Y","Z"};
	String[] numb={"0","1","2","3","4","5","6","7","8","9"};
	List<String> character = Arrays.asList(Char);
	List<String> number = Arrays.asList(numb);

	private String[] beans = { "", //case 0
		"RegVisitor", //case1 add
		"getStates", //case 2 view
		"getCity", //case 3 view
		"updateVisitor", //case 4 update
		"getVisitorFromTo", //case 5 view
		"viewVisitorMon", //case 6 view
		"viewVisitorDept", //case 7 view
		"visitorDaily",//case 8 view
		"CreateNewFile", //case 9 add
 		"GenerateRefNo", //case 10 add
		"DoanloadFile", //case 11 add
 		"auditorSchedule", //case 12 add
		"category_master", //case 13 add
		"sub_category_master", //case 14 add
		"UpdateAssets", //case 15 add
		"UpdateAssetsReg", //case 16 add
		"ScrapSale", //case 17 add
		"AssetsTransfer", //case 18 add
		"DigitalSignatureMaster", //case 19 add,update
		"DigitalSignatureActivition1", //case 20 add
		"DigitalSignMastch1", //case 21 add
		"DigitalSignatureReset", //case 22 add
	};
	//ReferenceNumber Generation Code
	ArrayList<String> ar=new ArrayList<String>();
	//-----------------------------
	VisitorManager visMngr = new VisitorManager();
	AdminManager admMgr = new AdminManager();
	DateManager dateMgr=new DateManager();
	
	private static String task = null;
	private static String bean = null;
	private int getBeanId() {
		for (int index = 1; index < beans.length; index++) {
			if (bean.equalsIgnoreCase(beans[index]))
				return index;
		}
		return NO_SUCH_BEAN;
	}
	/**
	 * This method returns the index of tasks identified
	 * @return index int
	 */
	private int getTaskId() {
		for (int index = 1; index < tasks.length; index++) {
			if (task.equalsIgnoreCase(tasks[index]))
				return index;
		}
		return NO_SUCH_TASK;
	}
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		doPost(req, resp);

	}

	/**
	* @see javax.servlet.http.HttpServlet#void (javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	*/
	public void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
		processRequest(req, resp);
		//ReferenceNumber Generation Code
	}

	private void processRequest(
		HttpServletRequest req,
		HttpServletResponse resp) {
		MultipartFormDataRequest mRequest = null;
		mRequest =(MultipartFormDataRequest) req.getSession().getAttribute("multipartrequest");
		System.out.println("Multipart request "+mRequest+" T/F "+ServletFileUpload.isMultipartContent(req));
		if(ServletFileUpload.isMultipartContent(req)){
			//check that the requested action is uploading or other
			System.out.println("Inside If");
			if(mRequest!=null)
			{
				task = mRequest.getParameter("task");
				System.out.println("Value of task 1="+task);
			}
			else
			{
				try {
					MultipartUploadFileRequest(req, resp);
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		} else {
			task = req.getParameter("task");
			System.out.println("Value of task 2="+task);
		}
		//task = req.getParameter("task");
		System.out.println(
			"SecuritysystemServlet: processrequest task:" + task);
		switch (getTaskId()) {
			case 1 :
				performAddAction(req, resp);
				break;
			case 2 :
				performViewAction(req, resp);
				break;
			case 3 :
				performUpdateAction(req, resp);
				break;
			default :
				System.out.println("Invalid Task Name");
		}
	}

	public void performAddAction(
		HttpServletRequest req,
		HttpServletResponse resp) {
		System.out.println(">>> Reached in performAddAction <<<");
		//fileUpload Option
		
		//File Upload Declaration
		MultipartFormDataRequest mRequest = null;
		//File Upload Code Ends Here
		
		if (ServletFileUpload.isMultipartContent(req)) {
			//check that the requested action is uploading or other 
			mRequest =
				(MultipartFormDataRequest) req.getSession().getAttribute(
					"multipartrequest");
			bean = mRequest.getParameter("beanIndicator");
		} else {
			bean = req.getParameter("beanIndicator");
		}
		System.out.println("bean Indecater "+bean);
		//bean = req.getParameter("beanIndicator");
		System.out.println("Bean ID:" + getBeanId());
		HttpSession session = req.getSession();
		try {
			switch (getBeanId()) {
				case 1 :
					String name = req.getParameter("visitname");
					String add = req.getParameter("visit_add");
					String countryId = req.getParameter("visit_country");
					String stateid = req.getParameter("visit_state");
					String cityid = req.getParameter("visit_city");
					String phone = req.getParameter("visit_ph");
					String mob = req.getParameter("visit_mob_no");
					String facId = req.getParameter("toMeet");
					String detMeetPerson = "";
					if (facId.equals("others")) {
						detMeetPerson = req.getParameter("txtDetMeet");
					}
					String purpose = req.getParameter("visit_purpose");
					String vehno = req.getParameter("vehicleno");
					String batno = req.getParameter("batchNo");
//					Date d = new Date();
//					System.out.println("date =>" + d);
//					int mon = d.getMonth() + 1;
//					int day = d.getDay();
//					int y = d.getYear() + 1900;
//					int sdate = d.getDate();
//					String mon1 = "";
//					if (mon < 10)
//						mon1 = "0" + mon;
//					else
//						mon1 = "" + mon;
//					String sdate1 = "";
//					if (sdate < 10)
//						sdate1 = "0" + sdate;
//					else
//						sdate1 = "" + sdate;
//					System.out.println(
//						" date=>" + mon1 + "-" + sdate + "-" + y);
					//String currentDate = mon1 + "-" + sdate1 + "-" + y;
					String currentDate = dateMgr.getCurrentDate();
					//System.out.println("curr date=>" + sdate);
//					int hours = d.getHours();
//					int minutes = d.getMinutes();
//					int seconds = d.getSeconds();
//					String hours1 = "", secs1 = "";
//					String mins1 = "";
//					if (hours < 10)
//						hours1 = "0" + hours;
//					else
//						hours1 = "" + hours;
//					if (minutes < 10)
//						mins1 = "0" + minutes;
//					else
//						mins1 = "" + minutes;
//					if (seconds < 10)
//						secs1 = "0" + seconds;
//					else
//						secs1 = "" + seconds; 
				//	String stime = hours1 + ":" + mins1 + ":" + secs1;
					String stime = dateMgr.getCurrentTime();
					System.out.println("curr time=>" + stime);
					String prgid = "";
					if (facId.equals("others")) {
						prgid = "";
					} else {
						prgid = visMngr.getDeptId(facId);
						System.out.println("prgname" + prgid);
					}

					boolean b =
						visMngr.addVisitor(
							name,
							add,
							countryId,
							stateid,
							cityid,
							phone,
							mob,
							facId,
							purpose,
							vehno,
							stime,
							currentDate,
							prgid,
							batno,
							detMeetPerson);
					System.out.println("boolean" + b);

					String msg = "";
					if (b == true) {
						ArrayList list = visMngr.getRegisterdDetails();
						session.setAttribute(
							"msg",
							"<font color=blue><b>Visitor Registered Successfully</b></font>");
						session.setAttribute("list", list);
					} else {
						session.setAttribute(
							"msg",
							"<font color=red><b>Visitor Is Not Registered Successfully</b></font>");
					}
					session.setAttribute("name", name);
					session.setAttribute("add", add);
					session.setAttribute("country", countryId);
					session.setAttribute("state", stateid);
					session.setAttribute("ctyid1", cityid);
					session.setAttribute("phone", phone);
					session.setAttribute("mob", mob);
					session.setAttribute("facId", facId);
					session.setAttribute("purpose", purpose);
					session.setAttribute("vehicleNo", vehno);
					session.setAttribute("batNo", batno);
					session.setAttribute("othersDet", detMeetPerson);
					String cont = "visit_country";
					resp.sendRedirect("jsp/frontdesk/VisitorRegistration.jsp");
					break;
				case 9:
					GenerateNewDocument(req,resp);
					break;
				case 10: 
					GenerateReferenceNumber(req, resp);
					break;
				case 11:
					DoanloadFile(req, resp);
					break;
				case 12:
					AuditCreation(req, resp);
					break;
				case 13:
					AddMainAssetCategory(req, resp);
					break;
				case 14:
					AddAssetsSubCategory(req, resp);
					break;
				case 15:
					UpdateAssetsDetails(req, resp);
					break;
				case 16:
					UpdateAssetsRegister(req, resp);
					break;
				case 17:
					ScrapSaleAsset(req, resp);
					break;
				case 18:
					AssetsTransfer(req, resp);
					break;
				case 19:
					addDigitalSignature(req,resp,mRequest);
					break;
				case 20:
					addDigitalPassword(req,resp);
					break;
				case 21:
					compareSign(req,resp);
					break;
				case 22:
					DigitalSignatureResetPassword(req,resp);
					break;
				default :

					System.err.println(
						"Please verify the bean name specified in the jsp file...");
					break;
			} //End of Switch
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void performUpdateAction(
		HttpServletRequest req,
		HttpServletResponse resp) {
		System.out.println(">>> Reached in performUpdateAction <<<");
		MultipartFormDataRequest mRequest = null;
		mRequest =
			(MultipartFormDataRequest) req.getSession().getAttribute(
				"multipartrequest");
		if (mRequest != null) {
			//check that the requested action is uploading or other 
			bean = mRequest.getParameter("beanIndicator");
		} else {
			bean = req.getParameter("beanIndicator");
		}
		System.out.println("bean Indecater "+bean);
		//bean = req.getParameter("beanIndicator");
		System.out.println("Bean ID:" + getBeanId());
		HttpSession session = req.getSession();

		try {
			switch (getBeanId()) {
				case 4 :
					System.out.println("logout visitor");
					String barcode = req.getParameter("visitId");
					System.out.println("barcode" + barcode);
					String s[] = barcode.split("/");
					System.out.println("len" + s.length);
					if (s.length > 1) {

						System.out.println("id" + s[0]);
						System.out.println("date" + s[1]);
						String vistid = s[0];
						String visdt = s[1];
						Date d = new Date();
						int hours = d.getHours();
						int minutes = d.getMinutes();
						int seconds = d.getSeconds();
						String hours1 = "", secs1 = "";
						String mins1 = "";
						if (hours < 10)
							hours1 = "0" + hours;
						else
							hours1 = "" + hours;
						if (minutes < 10)
							mins1 = "0" + minutes;
						else
							mins1 = "" + minutes;
						if (seconds < 10)
							secs1 = "0" + seconds;
						else
							secs1 = "" + seconds;
						String stime = hours1 + ":" + mins1 + ":" + secs1;
						System.out.println("curr time=>" + stime);
						String b1 = visMngr.addLogOut(vistid, visdt, stime);
						System.out.println("serb1" + b1);
						if (b1.equals("success")) {
							session.setAttribute(
								"msg",
								"<font color=blue><b>Visitor LogOut Successfully.</b></font>");
							session.setAttribute("barcode", vistid);
						}
						if (b1.equals("al")) {
							session.setAttribute(
								"msg",
								"<font color=red><b>Visitor is already logout.</b></font>");
							session.setAttribute("barcode", vistid);
						}
						if (b1.equals("invalid")) {
							session.setAttribute(
								"msg",
								"<font color=red><b>Invalid user.</b></font>");
							session.setAttribute("barcode", vistid);
						}
						resp.sendRedirect(
							"jsp/frontdesk/UpadateVisitorRegistration.jsp");
					} else {
						session.setAttribute(
							"msg",
							"<font color=red><b>Enter the correct visitor barcode.</b></font>");
						resp.sendRedirect(
							"jsp/frontdesk/UpadateVisitorRegistration.jsp");
					}
					break;
				case 19: System.out.println("Inside update of Digital signature..!");
						updateDigitalSignatureData(req,resp,mRequest);	
						break;
				default :

					System.err.println(
						"Please verify the bean name specified in the jsp file...");
			} //End of Switch
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
	public void MultipartUploadFileRequest(HttpServletRequest req, HttpServletResponse resp)throws ServletException, IOException
	{
		System.out.println("In Servlet Upload file");
		List<FileItem> multiparts=null;
		if(ServletFileUpload.isMultipartContent(req))
		{
			System.out.println("In Action Button");
			ar.add("UploadFile");
			ar.add("PersonalFile");
			ar.add("AddAssets");
			ar.add("DigitalSign");
			ServletFileUpload sf=new ServletFileUpload(new DiskFileItemFactory());
			try {
				 multiparts=sf.parseRequest(req);
				 System.out.println("Multipart REquest "+multiparts);
				 Dictionary dict12=new Hashtable();
				 for(FileItem file : multiparts)
					{
					 	System.out.println("InSide Data");
					 	String desc = file.getString();
			             int f=ar.indexOf(desc);
			             switch(f)
			             {
			             case 0:
							 UploadRefFile(req, resp, multiparts);
			            	 break;
			             case 1:
			            	 UploadPersonalFile(req, resp, multiparts);
			            	 break;
			             case 2:
			            	 System.out.println("In Case 3 For Add New Assets");
							 try {
								AddAssetsDetails(req, resp, multiparts);
							} catch (CmsSQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (CmsGeneralException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							} catch (SQLException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
			            	 break;
			             case 3:
			            	 UploadDigitalSign(req, resp, multiparts);
			            	 break;
			             default:
			            	System.out.println("Not a Valid Option");
			            	break;
			             }
					}
				 System.out.println("Dictionary val "+dict12); 
			} catch (FileUploadException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//Password Reset 
	private void DigitalSignatureResetPassword(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		System.out.println("Digital Signature Password reset");
		Digital_signDBQuery dbMgr=new Digital_signDBQuery();
		HttpSession session=req.getSession();
		String email=req.getParameter("email");
		String det_id=req.getParameter("det_id");
		boolean b=dbMgr.ResetPassword(det_id, email);
		if(b)
		{
			System.out.println("Reset Password");
			session.setAttribute("msg" +
					"" +
					"" +
					"" +
					"", "Password is been reseted");
		}
		else
		{
			System.out.println("Not reset");
			session.setAttribute("msg", "Unable to reset password");
		}
		resp.sendRedirect("jsp/digitalSignature/DigitalSignatureReport.jsp");
	}
	
	//Function compare Password
	private void compareSign(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		System.out.println("Comapre Password");
		Digital_signDBQuery dbMgr=new Digital_signDBQuery();
		String pass=req.getParameter("pass1");
		String userId=req.getParameter("uploadBy");
		System.out.println("Password "+pass );
		String text=dbMgr.selectPassword(userId);
		System.out.
		println("password "+text);
		int j=4;
		StringBuffer result=new StringBuffer();
		int pos=0;
		int res1=0;
		for(int i=0;i<text.length();i++)
		{
			if((text.charAt(i)>=65 && text.charAt(i)<=90) || (text.charAt(i)>=97 && text.charAt(i)<=122))
			{
				String str1=Character.toString(text.charAt(i));
				pos=character.indexOf(str1.toUpperCase());
				System.out.println("Character char descr"+pos);
				int d=pos;
				pos-=j;
				if(pos<0)
				{
					res1=26+pos;
					System.out.println("position "+res+"");
					if(Character.isUpperCase(text.charAt(i)))
					{
						result.append(character.get(res1));
					}
					else if(!Character.isUpperCase(text.charAt(i)))
					{
						result.append(character.get(res1).toLowerCase());
					}
				}
				else
				{
					if(Character.isUpperCase(text.charAt(i)))
					{
						result.append(character.get(pos));
					}
					else if(!Character.isUpperCase(text.charAt(i)))
					{
						result.append(character.get(pos).toLowerCase());
					}
				}
			}
			if((text.charAt(i)>= 48 && text.charAt(i)<= 57))
			{
				String str1=Character.toString(text.charAt(i));
				pos=number.indexOf(str1);
				System.out.println("Character number descr "+pos);
				int d=pos;
				pos-=j;
				if(pos<0)
				{
					res1=10+pos;
					result.append(number.get(res1));
				}
				else
				{
					result.append(number.get(pos));
				}
				
			}
			else if(text.charAt(i) <48 || (text.charAt(i) > 57 && text.charAt(i) < 65) || (text.charAt(i) > 90 && text.charAt(i) < 97) || text.charAt(i) > 122)
			{
				result.append(text.charAt(i));
			}
		}
	   System.out.println("Decription "+result);
	   String passwrd=result.toString();
	   System.out.println("decrypt pass "+passwrd+" Enter password "+pass+" Equals "+passwrd.equals(pass) );
	   HttpSession session=req.getSession();
	   if(passwrd.equals(pass))
	   {
		   session.setAttribute("msg", "Password Matched");
	   }
	   else
	   {
		   session.setAttribute("msg", "Password Does Not Match");
	   }
	   res.sendRedirect("jsp/digitalSignature/DigitalSignatureOLMSample.jsp");
	}
	
	//Function to add digital signature
	
	private void addDigitalSignature(HttpServletRequest req,
			HttpServletResponse resp, MultipartFormDataRequest mRequest) throws IOException{
			System.out.println("Added New Digital Signature ");
			Digital_signDBQuery dbMgr=new Digital_signDBQuery();
			String fileName="";
			String msg="";
			String[] progArr=mRequest.getParameterValues("sub");
			String respPer=mRequest.getParameter("respPer");
			String fName=mRequest.getParameter("signFile");
			String uploadBy=mRequest.getParameter("uploadBy");
			try{
				HttpSession session = req.getSession();
				String realPath = null;
				String destinationPath = null;
				realPath = session.getServletContext().getRealPath("digitalsignature");
				
				UploadBean uploadBean = new UploadBean();
				//uploadBean.setFolderstore(destinationPath);
				Hashtable fileTable = mRequest.getFiles();
				if ((fileTable != null) && (!fileTable.isEmpty())) {
					UploadFile file = (UploadFile) fileTable.get("signFile");
					fileName = file.getFileName();
					System.out.println("fileName...." + fileName);
				}
				if(fileName!=null){
						int id=dbMgr.addDigitalSignatureMasterNew(progArr,respPer,fileName.trim(),uploadBy);
						destinationPath=realPath+ "\\"+respPer+ "\\"+id;
						uploadBean.setFolderstore(destinationPath);
						msg="<font color='blue'>Digital Signature Created Successfull.</font>";
						uploadBean.store(mRequest, "signFile");
					}else{
						msg="<font color='red'>Error...Please Try Again...</font>";
					}
				req.getSession().removeAttribute("multipartrequest");
				mRequest = null;
			}catch (Exception e) {
				// TODO: handle exception
				System.out.println("Error occured while creating digital signature.!");
			}
			HttpSession session=req.getSession();
			session.setAttribute("msg", msg);
			resp.sendRedirect("jsp/digitalSignature/DigitalSignatureMaster.jsp?msg="+msg);
			/*resp.sendRedirect("jsp/digitalSignature/DigitalSignatureMaster.jsp?msg=");*/
	}

	private void addDigitalPassword(HttpServletRequest req, HttpServletResponse res) throws IOException
	{
		System.out.println("Password Setting");
		Digital_signDBQuery dbMgr=new Digital_signDBQuery();
		HttpSession session=req.getSession();
		String det_id=req.getParameter("det_id");
		String text=req.getParameter("pass2");
		String created_by=req.getParameter("uploadBy");
		System.out.println("Password Data Id "+det_id+" Password "+text.length()+" Uploaded By "+created_by);
		System.out.println(" Password "+text);
		//Ecryption Code
		int j=4;
		StringBuffer result=new StringBuffer();
		int pos=0;
		int res1=0;
		for(int i=0;i<text.length();i++)
		{
			if((text.charAt(i)>=65 && text.charAt(i)<=90) || (text.charAt(i)>=97 && text.charAt(i)<=122))
			{
				String str1=Character.toString(text.charAt(i));
				pos=character.indexOf(str1.toUpperCase());
				int d=pos;
				pos+=j;
				if(pos>=26)
				{
					res1=pos-26;
					if(Character.isUpperCase(text.charAt(i)))
					{
						result.append(character.get(res1));
					}
					else if(!Character.isUpperCase(text.charAt(i)))
					{
						result.append(character.get(res1).toLowerCase());
					}
				}
				else
				{
					if(Character.isUpperCase(text.charAt(i)))
					{
						result.append(character.get(pos));
					}
					else if(!Character.isUpperCase(text.charAt(i)))
					{
						result.append(character.get(pos).toLowerCase());
					}
				}
			}
			if((text.charAt(i)>= 48 && text.charAt(i)<= 57))
			{
				String str1=Character.toString(text.charAt(i));
				pos=number.indexOf(str1.toUpperCase());
				int d=pos;
				pos+=j;
				if(pos>=10)
				{
					res1=pos-10;
					result.append(number.get(res1));
				}
				else 
				{
					result.append(number.get(pos));
				}
				
			}
			else if(text.charAt(i) <48 || (text.charAt(i) > 57 && text.charAt(i) < 65) || (text.charAt(i) > 90 && text.charAt(i) < 97) || text.charAt(i) > 122)
			{
				result.append(text.charAt(i));
			}
		}
		System.out.println("Password String "+result);
		boolean flag=dbMgr.AddActivitionPassword(det_id, result, created_by);
		if(flag)
		{
			session.setAttribute("msg", "Password Is configured Successfully");
		}
		else
		{
			session.setAttribute("msg", "Uanable to Configure Password");
		}
		res.sendRedirect("jsp/digitalSignature/digitalSigantureActivition.jsp");
	}
	
	private void updateDigitalSignatureData(HttpServletRequest req,
			HttpServletResponse resp, MultipartFormDataRequest mRequest) throws IOException{
		System.out.println("In Update Signature");
		Digital_signDBQuery dbMgr=new Digital_signDBQuery();
		HttpSession session=req.getSession();
		String fileName="";
		String msg="";
		String Comments="";
		String[] progArr=mRequest.getParameterValues("sub");
		String respPer=mRequest.getParameter("respPer");
		String fName=mRequest.getParameter("newFile");
		String oldFile=mRequest.getParameter("oldFile");
		String uploadBy=mRequest.getParameter("uploadBy");
		Comments=mRequest.getParameter("cmnts");
		String det_id=mRequest.getParameter("det_id");
		try
		{
			String realPath = session.getServletContext().getRealPath("digitalsignature");
			String destinationPath="";   //realPath+ "\\"+respPer;
			UploadBean uploadBean = new UploadBean();
			Hashtable fileTable = mRequest.getFiles();
			if(!(fName==null || fName.equals("null") || fName.equals(""))){
				UploadFile file = (UploadFile) fileTable.get("signFile");
				fileName = file.getFileName();
				boolean upbol=dbMgr.updateOldrecord12345(respPer, det_id, progArr, Comments);
				int id=dbMgr.addDigitalSignatureMasterNew(progArr,respPer,fileName.trim(),uploadBy);
				destinationPath=realPath+ "\\"+respPer+ "\\"+id;
				uploadBean.setFolderstore(destinationPath);
				msg=upbol ? "<font color='blue'>Digital Signature Updated Successfull.</font>" : "<font color='blue'>Failed to update please try again</font>";
				uploadBean.store(mRequest, "signFile");
				
			}/*else if(!(oldFile==null || oldFile.equals("null") || oldFile.equals(""))){
				File file1=new File(oldFile);
				fileName = file1.getName();
				boolean bol=dbMgr.updateMasterTable1(progArr,respPer,fName,oldFile,uploadBy,Comments); 
				msg=bol ? "<font color='blue'>Digital Signature Updated Successfull.</font>" : "<font color='blue'>Failed to update please try again</font>";
			}*/
		}catch(Exception e)
		{
			System.out.println("Error occured while Updating digital signature.! "+e);
		}
		System.out.println("Message "+msg);
		session.setAttribute("msg", msg);
		resp.sendRedirect("jsp/digitalSignature/DigitalSignatureMaster.jsp");
	}
	 
	public void performViewAction(
		HttpServletRequest req,
		HttpServletResponse resp) {
		System.out.println(">>> Reached in performViewAction <<<");
		MultipartFormDataRequest mRequest = null;
		mRequest =
			(MultipartFormDataRequest) req.getSession().getAttribute(
				"multipartrequest");
		if (mRequest != null) {
			//check that the requested action is uploading or other 
			bean = mRequest.getParameter("beanIndicator");
			System.out.println("bean Indecater Multimedia "+bean);
		} else {
			bean = req.getParameter("beanIndicator");
			System.out.println("bean Indecater "+bean);
		}
		System.out.println("bean Indecater "+bean);
		//bean = req.getParameter("beanIndicator");
		System.out.println("Bean ID:" + getBeanId());
		HttpSession session = req.getSession();
		try {
			switch (getBeanId()) {

				case 2 :
					getStates(req, resp);
					break;
				case 3 :
					getCities(req, resp);
					break;
				case 5 :
					getVisitorFromTo(req, resp);
					break;
				case 6 :
					viewVisitorMon(req, resp);
					break;
				case 7 :
					viewVisitorDept(req, resp);
					break;
				case 8 :
					visitorDaily(req, resp);
					break;
				case 19:System.out.println("inside view function for digitl Signature..!");
						LoadDataforDigitalSignatureMaster(req,resp,mRequest);
				break;	
				default :
					System.err.println(
						"Please verify the bean name specified in the jsp file...");
			} //End of Switch
		} catch (Exception e) {
			System.out.println(e);
		}
	}
	
//Function to load Digital signature data.
	
	private void LoadDataforDigitalSignatureMaster(HttpServletRequest req,
			HttpServletResponse resp, MultipartFormDataRequest mRequest) throws IOException{
		String mainProg=""+mRequest.getParameter("progNew");
		String respPer=""+mRequest.getParameter("respPer");
		resp.sendRedirect("jsp/digitalSignature/DigitalSignatureMaster.jsp?progNew="+mainProg+"&respPer="+respPer);
	}
	
	private void getStates(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		String url = "";
		String ctname = req.getParameter("visitname");
		System.out.println("name" + ctname);
		String ctadd = req.getParameter("visit_add");
		System.out.println("add" + ctadd);
		String ctcountryId = req.getParameter("visit_country");
		System.out.println("countryId" + ctcountryId);
		//String ctstateid=req.getParameter("visit_state");
		//System.out.println("stateid"+ctstateid);
		//String ctcityid=req.getParameter("visit_city");
		//System.out.println("cityid"+ctcityid);
		String ctphone = req.getParameter("visit_ph");
		System.out.println("phone" + ctphone);
		String ctmob = req.getParameter("visit_mob_no");
		System.out.println("mob" + ctmob);
		String ctfacId = req.getParameter("toMeet");
		System.out.println("facId" + ctfacId);
		String ctpurpose = req.getParameter("visit_purpose");
		System.out.println("purpose" + ctpurpose);
		String ctvehno = req.getParameter("vehicleno");
		System.out.println("vehno" + ctvehno);
		String ctbatno = req.getParameter("batchNo");
		System.out.println("batno" + ctbatno);
		url = "jsp/frontdesk/VisitorRegistration.jsp";
		session.setAttribute("name", ctname);
		session.setAttribute("add", ctadd);
		session.setAttribute("country", ctcountryId);
		session.setAttribute("phone", ctphone);
		session.setAttribute("mob", ctmob);
		session.setAttribute("facId", ctfacId);
		session.setAttribute("purpose", ctpurpose);
		session.setAttribute("vehicleNo", ctvehno);
		session.setAttribute("batNo", ctbatno);
		String cont = "visit_country";
		session.setAttribute("focus", cont);
		try {
			System.out.println("hello  --");
			resp.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getCities(HttpServletRequest req, HttpServletResponse resp) {
		HttpSession session = req.getSession();
		String url = "";
		String ctname = req.getParameter("visitname");
		System.out.println("name" + ctname);
		String ctadd = req.getParameter("visit_add");
		System.out.println("add" + ctadd);
		String ctcountryId = req.getParameter("visit_country");
		System.out.println("countryId" + ctcountryId);
		String ctstateid = req.getParameter("visit_state");
		System.out.println("stateid" + ctstateid);
		//String ctcityid=req.getParameter("visit_city");
		//System.out.println("cityid"+ctcityid);
		String ctphone = req.getParameter("visit_ph");
		System.out.println("phone" + ctphone);
		String ctmob = req.getParameter("visit_mob_no");
		System.out.println("mob" + ctmob);
		String ctfacId = req.getParameter("toMeet");
		System.out.println("facId" + ctfacId);
		String ctpurpose = req.getParameter("visit_purpose");
		System.out.println("purpose" + ctpurpose);
		String ctvehno = req.getParameter("vehicleno");
		System.out.println("vehno" + ctvehno);
		String ctbatno = req.getParameter("batchNo");
		System.out.println("batno" + ctbatno);
		url = "jsp/frontdesk/VisitorRegistration.jsp";
		session.setAttribute("name", ctname);
		session.setAttribute("add", ctadd);
		session.setAttribute("country", ctcountryId);
		session.setAttribute("phone", ctphone);
		session.setAttribute("mob", ctmob);
		session.setAttribute("facId", ctfacId);
		session.setAttribute("purpose", ctpurpose);
		session.setAttribute("vehicleNo", ctvehno);
		session.setAttribute("state", ctstateid);
		session.setAttribute("batNo", ctbatno);
		String cont = "visit_state";
		session.setAttribute("focus", cont);
		try {
			System.out.println("hello  --");
			resp.sendRedirect(url);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void getVisitorFromTo(
		HttpServletRequest req,
		HttpServletResponse resp) {
		HttpSession session = req.getSession();
		String from = req.getParameter("fromdt");
		System.out.println("from" + from);
		String to = req.getParameter("todt");
		System.out.println("to" + to);
		ArrayList list = visMngr.getVisitorFromTo(from, to);
		System.out.println("list" + list);
		session.setAttribute("from", from);
		session.setAttribute("to", to);
		session.setAttribute("list", list);
		try {

			resp.sendRedirect("jsp/frontdesk/VisitorReportOfTwodate.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void viewVisitorMon(
		HttpServletRequest req,
		HttpServletResponse resp) {
		HttpSession session = req.getSession();
		String bid = req.getParameter("batch");
		System.out.println("bid" + bid);
		String year = admMgr.getBatchName(bid);
		System.out.println("year" + year);
		String mon = req.getParameter("month");
		System.out.println("mon" + mon);
		ArrayList list = visMngr.getMonthlyVisitorDet(mon, year);
		System.out.println("listservlet" + list);

		try {
			session.setAttribute("mon", mon);
			session.setAttribute("batch", bid);
			session.setAttribute("sermon", mon);
			session.setAttribute("list", list);
			resp.sendRedirect("jsp/frontdesk/VisitorMonthlyReport.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void viewVisitorDept(
		HttpServletRequest req,
		HttpServletResponse resp) {
		HttpSession session = req.getSession();
		//String bid=req.getParameter("batId");
		String bid = req.getParameter("batch");
		System.out.println("bid" + bid);
		String year = admMgr.getBatchName(bid);
		System.out.println("year" + year);
		String pid = req.getParameter("prgId");
		System.out.println("pid" + pid);
		ArrayList list = visMngr.getDepartmentVisitorDet(pid, year);
		System.out.println("listservlet" + list);

		try {
			session.setAttribute("pid", pid);
			session.setAttribute("batch", bid);
			session.setAttribute("list", list);
			resp.sendRedirect("jsp/frontdesk/visitorDepartmentWiseReport.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void visitorDaily(
		HttpServletRequest req,
		HttpServletResponse resp) {
		HttpSession session = req.getSession();
		//String bid=req.getParameter("batId");
		String dt = req.getParameter("fromdt");
		System.out.println("dt" + dt);
		DateManager dmt = new DateManager();
		String dbdt = dmt.getDBDate(dt);
		System.out.println("---VISITOR SERVLET DATE---"+dbdt);
		ArrayList list = visMngr.getTodaysVisitorDetails(dbdt);
		System.out.println("listservlet" + list);
		try {
			session.setAttribute("from", dt);
			session.setAttribute("varr", list);
			resp.sendRedirect("jsp/frontdesk/visitorDailyReport.jsp");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//ReferenceNumber Generation Code
	
	private void GenerateReferenceNumber(HttpServletRequest request, HttpServletResponse response) throws IOException, ParseException
	{
		System.out.println(">>>Generate Reference Number<<<");
		HttpSession session=request.getSession();
		ReferenceNo_DBQuery ref=new ReferenceNo_DBQuery();
		String refer12=request.getParameter("referencNum");
		System.out.println("Rerference Number length "+refer12.length()+" Reference number "+refer12);
		session=request.getSession();
		System.out.println("Null Value 2 "+refer12.equals("null"));
		if(refer12.equals("null"))
		{
			int count=1;
			String ref_gen[]=request.getParameterValues("ge_fName");
			String status=request.getParameter("stat");
			String file_id=request.getParameter("f_type");
			String prep_by=request.getParameter("assoc");
			String comments=request.getParameter("cmnts");
			String name[]={"prog_id","unit","department","created_by","file_type","fileType_shName","prep_byName","prep_byShName","folderName","fileName","Description","submitted_to","gen_byShName"};
			Dictionary dict=new Hashtable();
			dict.put("prep_byId", prep_by);
			dict.put("File_TypeID", file_id);
			dict.put("cmnts", comments);
			System.out.println("Status "+status );
			for(int i=0;i<ref_gen.length;i++)
			{
				dict.put(name[i], ref_gen[i]);
			}
			System.out.println("Document Data "+dict);
			String OriginalDepartment=dict.get("department").toString();
			System.out.println("Department "+dict.get("department"));
			//
			if(status.isEmpty())
			{
				ArrayList depChange=ref.selectUpdateDepartment(dict.get("prog_id").toString());
				String empty=depChange.get(0).toString().replace("[", "").replace("]", "");
				System.out.println("Department init "+empty );
				System.out.println("Description "+dict.get("Description"));
				dict.put("department", empty);
			}
			
			//Fetch Month and year to identify weather its october 1 
			DateManager dt=new DateManager();
			int day=dt.readDay();
			int month=dt.readMonth();
			int year1=dt.readYear();
			String md=""+year1;
			String modate=""+day+"/"+month;
			String yr=md.substring(2, 4);
			String year="";
			int a=0;
			String dptmnt="";
			int b=0;
			System.out.println("Department "+dict.get("department"));
			System.out.println("Date "+modate+" False/True "+modate.equals("1/10"));
			/*if(modate.equals("1/10"))*/
			if(day==1 && month==10)
			{
				System.out.println("Inside if cond..");	
				int sum=Integer.parseInt(yr);
				b=sum+1;
				year=""+yr+"-"+b;
				dict.put("month_year", year);
				DateManager CDate=new DateManager();
				ArrayList date=ref.getDepUpdatedDate(dict.get("prog_id").toString());
				ArrayList date1=(ArrayList)date.get(0);
				String PDate=""+date1.get(0);
				//String PDate=date.get(0).toString().replace("[", "").replace("]", "");
				System.out.println("Date "+PDate+"Size "+!PDate.equals("null"));
				if(!(PDate==null||PDate.equals("null") || PDate.equals("")))
				{
					System.out.println("inside a1");
					DateManager dat=new DateManager();
					String crDate=dat.getCurrentDate();
					Date dNow1 = new SimpleDateFormat("yyyy-MM-dd").parse(PDate); 
			        SimpleDateFormat ft =new SimpleDateFormat ("MM/dd/yyyy");
			        String format=ft.format(dNow1);
					//if(!format.equals(crDate))
			        String tempdate1=dateMgr.getDBDate(dateMgr.getReadableDate(PDate));
			        String tempDate2=crDate;
			        System.out.println("Value b1="+dateMgr.compareDates(tempDate2, tempdate1));
			        if(!(dateMgr.compareDates(tempDate2, tempdate1)==0)) // For Starts with 1 after 1st of Oct every month
					{
						System.out.println("Compare date");
						boolean monthyear=ref.updateYearMonth(year, dict.get("created_by").toString());
						boolean department=ref.InsertDepartment(dict.get("prog_id").toString(),OriginalDepartment);
						boolean updt=ref.updateRefNumb(dict.get("prog_id").toString(),"0");
						ArrayList depChange=ref.selectUpdateDepartment(dict.get("prog_id").toString());
						String empty=depChange.get(0).toString().replace("[", "").replace("]", "");
						System.out.println("Department init "+empty );
						dict.put("department", empty);
					}
				}
			}
			else
			{
				ArrayList moyr=ref.selectMonthYear();
				year=moyr.get(0).toString().replace("[", "").replace("]", "");
				dict.put("month_year", year);
			}
			
			//Fetch the max value from table to generate next reference number
			String ref_no="";
			int k=0;
			int n=1;
			if(status.equals("as"))
			{
				dptmnt=dict.get("department").toString();
				ArrayList prog=ref.MaxNumberDepartmentWise(dict.get("prog_id").toString(), dict.get("department").toString());
				String str=prog.get(0).toString().replace("[", "").replace("]", "");
				if(str.equals("null"))
				{
					ref_no=String.format("%03d", n);
				}
				else
				{
					k=Integer.parseInt(str);
					n=k+1;
					ref_no=String.format("%03d", n);
				}
				dict.put("numb", n);
				dict.put("ref_number", ref_no);
			}
			else if(status.equals("sbs"))
			{
				dptmnt=dict.get("department").toString();
				ArrayList prog=ref.MaxNumberDepartmentWise(dict.get("prog_id").toString(), dict.get("department").toString());
				String str=prog.get(0).toString().replace("[", "").replace("]", "");
				if(str.equals("null"))
				{
					ref_no=String.format("%03d", n);
				}
				else
				{
					k=Integer.parseInt(str);
					n=k+1;
					ref_no=String.format("%03d", n);
				}
				dict.put("numb", n);
				dict.put("ref_number", ref_no);
			}
			else
			{
				ArrayList prog=ref.MaxNumber(dict.get("prog_id").toString());
				String str=prog.get(0).toString().replace("[", "").replace("]", "");
				if(str.equals("null"))
				{
					ref_no=String.format("%03d", n);
				}
				else
				{
					k=Integer.parseInt(str);
					n=k+1;
					ref_no=String.format("%03d", n);
				}
				dict.put("numb", n);
				dict.put("ref_number", ref_no);
			}
			//This code check weather particular department has reach to 999 reference number if it is than change department eq. EEC to EEC2
			int total_ref=Integer.parseInt(ref_no);
			
			if(total_ref>999)
			{
				System.out.println("--------Reference number Greater than 999---------- ");
				String dep1=""+dict.get("department");
				dep1=dep1+"2";
				String ref_no1="";
				boolean updt=ref.updateRefNumb(dict.get("prog_id").toString(), "1");
				if(updt)
				{
					ArrayList prog1=ref.MaxNumber(dict.get("prog_id").toString());
					String str1=prog1.get(0).toString().replace("[", "").replace("]", "");
					dict.put("numb", str1);
					k=Integer.parseInt(str1);
					ref_no=String.format("%03d", k);
				}
				boolean updep=ref.InsertDepartment(dict.get("prog_id").toString(),dep1);
				dict.put("department", dep1);
				dict.put("ref_number", ref_no);
			}
			
			//Ex.: SGCS/ADM/MIN-015/09-10
			String ref_number_gen=""+dict.get("unit").toString()+"/"+dict.get("department")+"/"+dict.get("fileType_shName").toString().toUpperCase()+"-"+dict.get("ref_number").toString()+"/"+dict.get("month_year").toString();
			System.out.println("Final Refernce number "+ref_number_gen);
			dict.put("ref_gene_no", ref_number_gen);
			count+=1;
			System.out.println("Dictionary : "+dict);
			boolean bol=ref.insertNewReference(dict, dptmnt);
			System.out.println("false/True "+bol);
			if(bol)
			{
				session.setAttribute("error", "Reference Number Generated Sucessfully");
				session.setAttribute("name", dict.get("prep_byName"));
				session.setAttribute("ref_no", ref_number_gen);
				session.setAttribute("file_type", dict.get("file_type"));
				response.sendRedirect("jsp/referencenumber/Gen_reference_number.jsp");
			}
			else
			{
				session.setAttribute("error", "Faild To Generate an Reference Number Plese Try Again");
				response.sendRedirect("jsp/referencenumber/Gen_reference_number.jsp");
			}
		}
		else if(!refer12.equals("null"))
		{
			String prep_by=request.getParameter("assoc");
			String ref_gen[]=request.getParameterValues("ge_fName");
			String name[]={"prog_id","unit","department","created_by","file_type","fileType_shName","prep_byName","prep_byShName","folderName","fileName","Description","submitted_to","gen_byShName"};
			Dictionary dict=new Hashtable();
			for(int i=0;i<ref_gen.length;i++)
			{
				dict.put(name[i], ref_gen[i]);
			}
			dict.put("prepById", prep_by);
			boolean bol=ref.updateReferenceNumber(dict, refer12);
			System.out.println("false/True "+bol);
			if(bol)
			{
				session.setAttribute("error", "Reference Number Details Updated");
				session.setAttribute("ref_no", refer12);
				session.setAttribute("name", dict.get("prep_byName"));
				session.setAttribute("file_type", dict.get("file_type"));
				response.sendRedirect("jsp/referencenumber/Gen_reference_number.jsp");
			}
			else
			{
				session.setAttribute("error", "Failed To Updated. Please Try Again");
				response.sendRedirect("jsp/referencenumber/Gen_reference_number.jsp");
			}
		}
	}
	
	private void UploadRefFile(HttpServletRequest request, HttpServletResponse response,List<FileItem> request1) throws IOException 
	{
		System.out.println(" Upload File ");
		HttpSession session=request.getSession();
		Dictionary dict=new Hashtable();
		ReferenceNo_DBQuery ref=new ReferenceNo_DBQuery();
		for(FileItem file : request1)
		{
		 	String fieldName = file.getFieldName();
		 	String desc = file.getString();
            dict.put(fieldName, desc);
		}
		 String fileName1="";
		String msg1 = "";
		boolean status1 = false;
		String destinationPath = null;
		try {
			String refer=dict.get("ref").toString().replace("/", "_");
			destinationPath=upLoadCourseOutlineForPedagogy(request, dict, refer, 1);
			UploadBean uploadBean = new UploadBean();
			uploadBean.setFolderstore(destinationPath);
			dict.put("ServerPath", destinationPath);
			for(FileItem item : request1){
				if(!item.isFormField())
				{
					String name = new File(item.getName()).getName();
					dict.put("FilName", name);
		            item.write( new File(destinationPath+ File.separator + name));
				}
	         }
		}catch(Exception e)
		{
			System.out.println("Exception "+e);
		}
		boolean ar=false;
		ar=ref.updateFilePath1(dict);
		if(ar)
		{
			session.setAttribute("msg", "File Uploaded Sucessfully");
		}
		else
		{
			session.setAttribute("msg", "Unable to upload file, please try again");
		}
		response.sendRedirect("jsp/referencenumber/Reference_Register.jsp");
	}
	
	private String upLoadCourseOutlineForPedagogy(HttpServletRequest req, Dictionary dict, String refer, int a)throws CmsGeneralException 
	{
		System.out.println("Get File Path");
		try {
			HttpSession session = req.getSession();
			AdminManager admManager = new AdminManager();
			//String fileName = null;
			String realPath = null;
			String destinationPath = null;
			//System.out.println("forFile>>>" + forFile);
			realPath = session.getServletContext().getRealPath("resources");
			if(a==1)
			{
				destinationPath =realPath + "\\Reference_Number\\"+ dict.get("unit") + "\\" + dict.get("depar") + "\\" + refer;
			}
			else if(a==2)
			{
				destinationPath =realPath + "\\ReferenceNumberImpLinkFile\\"+ dict.get("Units") + "\\" + dict.get("departShName") + "\\" + refer;
			}
			else if(a==3)
			{
				destinationPath = realPath + "\\Assets_Register_File\\"+ dict.get("Unita") + "\\" + dict.get("depaName") + "\\" + refer;
			}
			else if(a==4)
			{
				destinationPath = realPath + "\\DigitalSign\\"+ dict.get("unit") + "\\" + dict.get("depName") + "\\" + refer;
			}
			req.getSession().removeAttribute("multipartrequest");
			session = null;
			return destinationPath;
		} catch (Exception ex) {
			throw new CmsGeneralException(
				"Problem in setUploadFile function of Examination Servlet :"
					+ ex.getMessage());
		}
	}
	
	
	private void UploadPersonalFile(HttpServletRequest request, HttpServletResponse response,List<FileItem> request1) throws IOException 
	{
		System.out.println("Upload Personal File");
		HttpSession session=request.getSession();
		Dictionary dict=new Hashtable();
		ReferenceNo_DBQuery ref=new ReferenceNo_DBQuery();
		for(FileItem file : request1)
		{
		 	String fieldName = file.getFieldName();
		 	String desc = file.getString();
            int f=ar.indexOf(desc);
            dict.put(fieldName, desc);
		}
	 	ArrayList depart=ref.DepartmentUnit(dict.get("Associate").toString());
		String deprmt="";
		for(int i=0;i<depart.size();i++)
		{
			ArrayList ar=(ArrayList)depart.get(i);
			deprmt=""+ar.get(2);
			dict.put("departShName", deprmt);
		}
		String destinationPath = null;
		try {
			String refer=dict.get("referenceNumber").toString().replace("/", "_");
			destinationPath=upLoadCourseOutlineForPedagogy(request, dict, refer, 2);
			UploadBean uploadBean = new UploadBean();
			uploadBean.setFolderstore(destinationPath);
			dict.put("ServerPath", destinationPath);
			for(FileItem item : request1){
				if(!item.isFormField())
				{
					String name = new File(item.getName()).getName();
					System.out.println("File Upload option "+name);
					dict.put("FilName", name);
		            item.write( new File(destinationPath+ File.separator + name));
				}
	         }
		}catch(Exception e)
		{
			System.out.println("Exception "+e);
		}
		boolean ar=false;
		ar=ref.inserPersonalFileDetails(dict);
		if(ar)
		{
			session.setAttribute("msg", "Personal File Uploaded Sucessfully");
		}
		else
		{
			session.setAttribute("msg", "Unable to upload personal file, please try again");
		}
		response.sendRedirect("jsp/referencenumber/Reference_Register.jsp");
	}
	
	private void DoanloadFile(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		System.out.println("In download");
		response.setContentType("text/html");  
		PrintWriter out = response.getWriter();  
		String op=request.getParameter("op");
		String filepath="";
		if(op.equals("2"))
		{
			filepath=request.getParameter("PathDocs");
		}
		else if(op.equals("1"))
		{
			filepath = request.getParameter("path");   
		}
		response.setContentType("APPLICATION/OCTET-STREAM");
		PrintWriter out1 = response.getWriter();
		String filename = request.getParameter("FileName"); 
		String fileData=filepath +"\\"+ filename;
		response.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
	    FileInputStream fl = new FileInputStream(filepath +"\\"+ filename);
	    int i;
	    while ((i = fl.read()) != -1) 
	    {
	        out1.write(i);
	    }
	    fl.close();
	    out.close();
	}
	
	private void GenerateNewDocument(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		HttpSession session=request.getSession();
		System.out.println("In Document File Creation");
		ReferenceNo_DBQuery ref=new ReferenceNo_DBQuery();
		String name[]=request.getParameterValues("FileCre");
		String str[]={"created_by","document_name","document_sh_name"};
		Dictionary dict=new Hashtable();
		for(int i=0;i<name.length;i++)
		{
			System.out.println("Value of "+name[i]);
			dict.put(str[i], name[i]);
		}
		int ar=ref.CheckDocuSHName(dict);
		if(ar==1)
		{
			session.setAttribute("error", "Document already exists");
		}
		else if(ar==2)
		{
			session.setAttribute("error", "Document short name already exists");
		}
		else if(ar==0)
		{
			boolean bol=ref.insertNewDocument(dict);
			if(bol==true)
			{
				session.setAttribute("error", "Document File Is Created Thank You");
			}
			else{
				session.setAttribute("error", "Document File Is Not Created Thank You");
			}
		}
		response.sendRedirect("jsp/referencenumber/Add_New_File.jsp");
	}
	
	private void AuditCreation(HttpServletRequest req, HttpServletResponse resp) throws IOException
	{
		System.out.println("In Audit Creation");
		String str=req.getParameter("op");
		HttpSession session=req.getSession();
		boolean flag=false;
		AuditDBQuery db=new AuditDBQuery();
		int option=Integer.parseInt(str);
		switch(option)
		{
		case 1:
			System.out.println("In Audit Type Creation");
			String auditName=req.getParameter("FileCre");
			String email=req.getParameter("email");
			System.out.println("Audit Type Name "+auditName+" Created By "+email);
			flag=db.insertNewAuditType(email, auditName);
			if(flag)
			{
				session.setAttribute("error", "New Audit File Is Created");
			}
			else
			{
				session.setAttribute("error", "Sorry Unable To Create File Please Try Again");
			}
			resp.sendRedirect("jsp/audit/AuditTypeCreation.jsp");
			break;
		case 2:
			System.out.println("In Audit Schedule Creation");
			String Audit[]={"createdBy","AuditType","ProgId","AuditorId","AuditeeId","date","From","to","PreviousPoint"};
			Dictionary dict=new Hashtable();
			String names[]={"email","type","dep","facId","auditee","audit_date","from","to","PreviousPoint"};
			for(int i=0;i<names.length;i++)
			{
				dict.put(Audit[i], req.getParameter(names[i]));
			}
			System.out.println("Audit Data "+dict);
			flag=db.AddAuditScheduleData(dict);
			resp.sendRedirect("jsp/audit/AuditSchedule.jsp");
			break;
		case 3:
			String AuditID[]=req.getParameterValues("AuditId");
			System.out.println("");
			boolean flag1=false;
			for(int i=0;i<AuditID.length;i++)
			{
				System.out.println("Audit Id "+AuditID[i]);
				flag1=db.updateAuditScheduleStatus(AuditID[i]);
			}
			if(flag1)
			{
				session.setAttribute("error", "Audit Schedule Craeted Sucessfully");
			}
			else
			{
				session.setAttribute("error", "Unable to create Audit Schedule ");
			}
			resp.sendRedirect("jsp/audit/AuditSchedule.jsp");
			break;
		case 4:
			System.out.println("Audit Case 4");
			String audit[]={"Prog_Mst_Id","auditId","auditType","createdBy","audit_mark","auditorName","ClosureDate"};
			String data[]={"progId","auditId","auditType","email","auditMark","auditAction","date"};
			String prepoint=req.getParameter("prePoint");
			System.out.println("Previous Value "+prepoint);
			String auditPreVal=req.getParameter("auditPreId");
			String auditPoint=req.getParameter("auditTextArea");
			String auditReferenceNumb=req.getParameter("RefNo");
			String auditId122=req.getParameter("auditId");
			String marksType=req.getParameter("markType");
			String auditCrDate=req.getParameter("uniqueDate");
			System.out.println("Marks Type "+marksType);
			/*String auditObserId=req.getParameter("auditObserId");*/
			System.out.println("Audit REference Number 1 "+auditReferenceNumb);
			System.out.println("REf Null 3 "+auditReferenceNumb=="");
			System.out.println("Audit Id "+auditId122);
			System.out.println("Audit schedule Date "+auditCrDate);
			Dictionary dic=new Hashtable();
			dic.put("markType", marksType);
			dic.put("uniqueDate", auditCrDate);
			dic.put("auditobId", prepoint);
			if(auditPreVal!=null)
			{
				dic.put("auditsId", auditPreVal);
			}
			if(auditPoint!=null)
			{
				dic.put("auditTextArea", auditPoint);
			}
			/*dic.put("auditObId", auditObserId);*/
			for(int i=0;i<data.length;i++)
			{
				dic.put(audit[i], req.getParameter(data[i]));
			}
			System.out.println("Null value ");
			try{
				if(!(auditReferenceNumb==null) || !(auditReferenceNumb==""))
				{
					boolean b=db.updateAuditScheduleRef(auditReferenceNumb, auditId122);
					System.out.println("Inserted Or no "+b);
				}
			}catch(NullPointerException e)
			{
				System.out.println("Null Pointer Exception "+e);
			}
			if(prepoint.equals("1"))
			{
				if(dic.get("ClosureDate").toString().isEmpty())
				{
					dic.put("status", "1");
				}
				else
				{
					dic.put("status", "0");
				}
				boolean flg=db.updateAuditObservationFinal(dic.get("auditsId").toString());
				System.out.println("Flag After Updating Value "+flg);
				if(flg)
				{
					
				}
			}
			else
			{
				if(dic.get("ClosureDate").toString().isEmpty())
				{
					dic.put("status", "1");
				}
				else
				{
					dic.put("status", "0");
				}
				boolean f=db.AuditObservationSheet(dic);
			}
			System.out.println("Dictionary value "+dic);
			session.setAttribute("progID", dic.get("Prog_Mst_Id"));
			session.setAttribute("audtId", dic.get("auditId"));
			session.setAttribute("auditType", dic.get("auditType"));
			session.setAttribute("auditCrDate", dic.get("uniqueDate"));
			resp.sendRedirect("jsp/audit/AuditObservationSheet.jsp");
			break;
		case 5:
			System.out.println("Audit Case 5");
			String AuditPoint[]=req.getParameterValues("AuditObservId");
			String status[]=req.getParameterValues("status");
			String ActionBy[]=req.getParameterValues("ActionBy");
			String prog_mst_id=req.getParameter("prog_id");
			String auditId=req.getParameter("AuditId");
			String createdBy=req.getParameter("createdBy");
			String AuditType=req.getParameter("auditType");
			System.out.println("");
			boolean flg=false;
			boolean fl2=false;
			for(int i=0;i<AuditPoint.length;i++)
			{
				System.out.println("Audit ID "+AuditPoint[i]+" Status "+status[i]);
				System.out.println("Prog_mst_id "+prog_mst_id+" auditId "+auditId);
				flg=db.updateAuditScheduleStatus123(auditId);
				
				System.out.println("flag "+flg );
				if(flg==true)
				{
					boolean bbb=db.updateAuditObservationStatusData(AuditPoint[i]);
					System.out.println("Boolean Value "+bbb);
					System.out.println("Action By Id "+ActionBy[i]);
					if(status[i].equals("0"))
					{
						fl2=db.inserObsSheetStatus(auditId,AuditPoint[i],prog_mst_id,status[i],createdBy, AuditType, ActionBy[i]);
						session.setAttribute("error", "Audit Observation Sheet Created Sucessfully");
					}
				}
				else
				{
					session.setAttribute("error", "Unable to create Audit Schedule ");
				}
			}
			resp.sendRedirect("jsp/audit/AuditScheduleCreated.jsp");
			break;
		case 6:
			String cmplt=req.getParameter("cmpt");
			String auditSts=req.getParameter("auditSts");
			String cmltDate=req.getParameter("audit_date");
			String StatusCmnt=req.getParameter("cmnt");
			System.out.println("Complted Or no "+cmplt);
			boolean pen=db.Updatepending(auditSts, cmplt, cmltDate, StatusCmnt);
			System.out.println("Status "+pen);
			if(pen)
			{
				session.setAttribute("error", "Status Updated");
			}
			else
			{
				session.setAttribute("error", "Failed To Update Status");
			}
			resp.sendRedirect("jsp/audit/audit_status_close.jsp");
			break;
		case 7:
			System.out.println("In Case 7");
			String cmple=req.getParameter("cmp");
			String auditObsId=req.getParameter("auditObId");
			String progID=req.getParameter("progId");
			String audtId=req.getParameter("auditId");
			String auditType=req.getParameter("auditType");
			System.out.println("Prog ID "+progID+" AuditorId "+audtId+" AuditType "+auditType);
			pen=db.UpdatePendingAuditStatus(auditObsId, cmple);
			if(pen==false)
			{
				session.setAttribute("error", "Failed To Update Status");
			}
			session.setAttribute("progID", progID);
			session.setAttribute("audtId", audtId);
			session.setAttribute("auditType", auditType);
			resp.sendRedirect("jsp/audit/AuditObservationSheet.jsp");
			break;
		default:
			System.out.println("Invalid Option");
			break;
		}
	}
	
	//Assets Servlet 
	private void AddMainAssetCategory(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		System.out.println("In Servlet dopost AsssetsResgister Class");
		String user_id=request.getParameter("user_id");
		System.out.print("USer id is "+user_id+" \n");
		String cat=request.getParameter("txtCata");
		String CatShort=request.getParameter("txtShort");

		System.out.println("Enter Register Catagory : "+cat);
		System.out.println("Short form : "+CatShort);
		AssetsMasterVal am=new AssetsMasterVal();	
		HttpSession session=request.getSession();
		am.setUser_id(user_id);
		am.setCate(cat.toUpperCase());
		am.setShCat(CatShort.toUpperCase());
		ArrayList st=new ArrayList();
		st=Assets_DBQueries.selectMainCat1();
		System.out.println("Data is : "+st);
		ArrayList arr=new ArrayList();
		ArrayList resultSet = null;
		resultSet=Assets_DBQueries.selectMainCat1();
		arr=Assets_DBQueries.checkForExistingCategory(am);
		System.out.println("Array List value = "+arr);
		System.out.println("main Category "+am.getCate());
		if(!arr.isEmpty())
		{
	
				for(int i=0;i<st.size();i++)
				{
					ArrayList arr1=(ArrayList)st.get(i);
					System.out.println("main category "+arr1.get(1).equals(am.getCate()));
					if (arr1.get(1).equals(am.getCate()))
					{
						session.setAttribute("error", "Catagory Name Already Exists "+cat);
						System.out.println("main category 1  "+arr1.get(1).equals(am.getCate()));
					}
					else if(arr1.get(2).equals(am.getShCat()))
					{
						System.out.println("Exist.. = " +arr1.get(2).equals(am.getShCat()));
						session.setAttribute("error", "Catagory Short Name Already Exists "+CatShort);
					}
				}
		}
		else
		{
			System.out.println("Data Does not exists"+arr.equals(cat)+" and "+arr.equals(CatShort));
			boolean flag=Assets_DBQueries.addAssets(am);
			if (flag == false)   
			{
				session.setAttribute("error","Catagory not Inserted");
				System.out.println("Value not insterted");
			}
			else{
				session.setAttribute("error","Catagory inserted sucessefull");
				System.out.println("Inserted Sucessfully");
			}
		}
		response.sendRedirect("jsp/assetmanagement/MasterCreation.jsp");
	}
	
	private void AddAssetsSubCategory(HttpServletRequest request, HttpServletResponse response) throws IOException, CmsSQLException, CmsGeneralException
	{
		String ar=request.getParameter("main_cat");
		String user_id=request.getParameter("user_id");
		String assets_sub1 = request.getParameter("txtSubCata");
		String assets_short1=request.getParameter("txtSubShort");
		String assets_sub=assets_sub1.toUpperCase();
		String assets_short=assets_short1.toUpperCase();
		Assets_DBQueries ad=new Assets_DBQueries();
		Sub_Cat_GS sc=new Sub_Cat_GS();
		HttpSession session=request.getSession();
		sc.setMain_cat(ar);
		sc.setSub_cat(assets_sub);
		sc.setSub_cat_short(assets_short);
		sc.setUser_id(user_id);
		ArrayList arr=new ArrayList();
		ArrayList resultSet=new ArrayList();
		resultSet=Assets_DBQueries.select_sub_cat1();
		arr=Assets_DBQueries.checkForExistingSubCategory(sc);
		if(!arr.isEmpty())
		{
			for(int i=0;i<resultSet.size();i++)
				{
						ArrayList arr1=(ArrayList)resultSet.get(i);
						if (arr1.get(2).equals(sc.getSub_cat()))
						{
							session.setAttribute("error", " Sub Catagory Name Already Exists "+assets_sub);
						}
						else if(arr1.get(3).equals(sc.getSub_cat_short()))
						{
							session.setAttribute("error", " Sub Catagory Short Name Already Exists "+assets_short);
						}
					}
		}			
		else
		{
			boolean flag=Assets_DBQueries.addSubAssets(sc);
			if(flag == true)
			{
				session.setAttribute("error", "Value Is inserted sucessfully");
			}
			else
			{
				session.setAttribute("error", "Value Not Inserted");
			}
		}
		response.sendRedirect("jsp/assetmanagement/Assets_Sub_Category.jsp");
	}
	
	private void AddAssetsDetails(HttpServletRequest request, HttpServletResponse response, List<FileItem> request1) throws IOException, CmsGeneralException, CmsSQLException, SQLException
	{
		System.out.println("Add New Assets Details");
		String UPLOAD_DIRECTORY="";
		DateManager dm = new DateManager();	
		HttpSession session=request.getSession();
		Dictionary dict=new Hashtable();
		ReferenceNo_DBQuery ref=new ReferenceNo_DBQuery();
		ArrayList amnt=new ArrayList();
		int count=0;
		for(FileItem file : request1)
		{
		 	String fieldName = file.getFieldName();
		 	String desc = file.getString();
            dict.put(fieldName, desc);
            if(fieldName.compareToIgnoreCase("IndAmnt")==1)
            {
            	amnt.add(desc);
            	count+=1;
            }
            continue;
		}
		Assets_DBQueries as=new Assets_DBQueries();
		ArrayList hod_shName=as.HODShortNames(dict.get("department").toString());
		String hod_sh_name=hod_shName.get(0).toString().replace("[", "").replace("]", "");
		dict.put("hod_shName", hod_sh_name);
		ArrayList ar=as.getDepUserId(dict.get("Associate").toString());
		ArrayList list=as.selectMainName(dict.get("mainCat").toString());
		String ref_no="";
		String Main_cat_name="";
		String sub_cat_name="";
		for(int i=0;i<list.size();i++){
			ArrayList arr=(ArrayList)list.get(i);
			Main_cat_name=""+arr.get(1);
		}
		ArrayList list2=as.selectSubNameForAddAssets(dict.get("mainCat").toString(), dict.get("subCat").toString());
		for(int i=0;i<list2.size();i++){
			ArrayList arr=(ArrayList)list2.get(i);
		   sub_cat_name=""+arr.get(2);
		}
		
		for (int i=0;i<ar.size();i++)
		{
			ArrayList arr=(ArrayList)ar.get(i);
			String dep1=""+arr.get(2);
			dict.put("depaName",dep1);
		}
		
		//File Data
		boolean flag=false;
		boolean fff=false;
		String refNId="";
		int size=Integer.parseInt(dict.get("quantity").toString());
		
		//Generate Reference Number Code
		for(int t=0;t<amnt.size();t++)
		{
			String arr=Assets_DBQueries.getRef_id(dict);
			if(arr==null||arr.equals("null"))
			{
				arr="0";
			}
			int rNo=Integer.parseInt(arr)+1;
			String ref_id = String.format("%03d", rNo); //Convert Integer number to 001, 002..... infinite
			dict.put("ref_id", ref_id);
		
			ArrayList sub_sh=Assets_DBQueries.fetchSubCatShortName(dict);
			ArrayList arr3=(ArrayList)sub_sh.get(0);
			String sub_sh2=""+arr3.get(0);
			String sub_name=String.valueOf(arr3.get(0));
			String bDate1=dm.format(dict.get("bill_date").toString(),"MM-dd-yyyy");
			String mm=bDate1.substring(0,2);
			String yy=bDate1.substring(8, 10);
			String ref1_no=dict.get("Units")+"/"+dict.get("depaName")+"/"+sub_sh2.toLowerCase()+"-"+ref_id+"/"+mm+"-"+yy;
			String refDir=dict.get("Units")+"_"+dict.get("depaName")+"_"+sub_sh2.toLowerCase()+"_"+ref_id+"_"+mm+"_"+yy;
			flag=as.addRegister(dict, amnt.get(t).toString(),ref_id, ref1_no, sub_cat_name, Main_cat_name,  bDate1);
			
			
			//File Store Copy Code in Server
			if(flag==true)
			{
				String fileName1="";
				String msg1 = "";
				boolean status1 = false;
				String destinationPath = null;
				try {
					destinationPath=upLoadCourseOutlineForPedagogy(request, dict, refDir, 3);
					System.out.println("File Path "+destinationPath);
					UploadBean uploadBean = new UploadBean();
					uploadBean.setFolderstore(destinationPath);
					dict.put("ServerPath", destinationPath);
					for(FileItem item : request1){
						if(!item.isFormField())
						{
							String name = new File(item.getName()).getName();
							dict.put("FilName", name);
				            item.write( new File(destinationPath+ File.separator + name));
						}
			         }
				}catch(Exception e)
				{
					System.out.println("Exception "+e);
				}
				fff=as.updateAssetsRegisterFilePath(dict, ref1_no);
			}
			refNId=refNId.concat(ref1_no+", ");
		}
		if(fff)
		{
			session.setAttribute("error", "Value Is inserted sucessfully and data is send to HOD/HOI approval");
			session.setAttribute("ref_no", refNId);
		}
		else
		{
			session.setAttribute("error", "Value Not Inserted");
		}
		response.sendRedirect("jsp/assetmanagement/AddAssets.jsp");
	}
	
	private void UpdateAssetsDetails(HttpServletRequest request, HttpServletResponse response) throws IOException, CmsSQLException, CmsNamingException, CmsGeneralException
	{
		HttpSession session=request.getSession();
		System.out.println("In Update assets operation");
		String stat=request.getParameter("status123");
		String comment=request.getParameter("txtMsg");
		System.out.println("Comment = "+comment);
		String asset_reg_id=request.getParameter("Sub_id");
		String user_id=request.getParameter("email");
		String sub_name=request.getParameter("sub_name");
		String hod_shName="";
		String status="";
		if(stat.equals("0"))
		{
			status="1";
		}
		else
		{
			status="2";
			boolean flag1=Assets_DBQueries.insertAssetsStatus(asset_reg_id, sub_name, status, comment, user_id);
				if(flag1==true)
				{
					session.setAttribute("status_msg", "Rejected report send to user, he/she get back to you soon,thank you");
				}
				else
				{
					session.setAttribute("status_msg","Failed to send rejected report to user please contact user through call or phone");
				}
		}
		boolean flag=Assets_DBQueries.updateAssets(status,comment,asset_reg_id, user_id);
		if(flag==true)
		{
			System.out.println("Inserted sucessfully "+flag);
			session.setAttribute("msg", "Status updated. Thank You");
		}
		else{
			System.out.println("Not inserted "+flag);
			session.setAttribute("msg", "Sorry, Status is not updated ");
		}
		response.sendRedirect("jsp/assetmanagement/AssetsHodHoiApprovals.jsp");	
	}
	
	private void UpdateAssetsRegister(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		// TODO Auto-generated method stub
		Assets_DBQueries ad=new Assets_DBQueries();
		HttpSession session=request.getSession();
		System.out.println("In Update assets register");
		String descrp=request.getParameter("des");
		String Bill_no=request.getParameter("bill_no");
		String Supplier_name=request.getParameter("sup_name");
		String Quantity=request.getParameter("quant");
		String amount=request.getParameter("amnt");
		String as_id=request.getParameter("as_id");
		String cancel=request.getParameter("cnl");
		if(cancel.equals("1"))
		{
			boolean flag=ad.updateAssetsStatus(as_id);
			if(flag==true)
			{
				session.setAttribute("msg", "Assets has been canceled");
			}
			else
			{
				session.setAttribute("msg", "Assets not canceled");
			}
		}
		else
		{
			boolean flag=ad.updateAssetsReg(descrp, Bill_no, Supplier_name, Quantity, amount,as_id);
			if(flag==true)
			{
				session.setAttribute("msg", "Update sucessfully please wait while HOD/HOI Response back");
			}
			else
			{
				session.setAttribute("msg", "Update faild please try again");
			}
		}
		response.sendRedirect("jsp/assetmanagement/Assets_Rejected_Status_Report.jsp");
	}

	private void ScrapSaleAsset(HttpServletRequest request, HttpServletResponse response) throws Exception 
	{
		String cas=request.getParameter("op");
		System.out.println("Assets Scrap Sale "+cas);
		Assets_DBQueries db=new Assets_DBQueries();
		int num=Integer.parseInt(cas);
		System.out.println("Scrap number "+num);
		switch(num)
		{
		case 1:
			System.out.println("in case 1");
			break; 
		case 2:
			HttpSession session=request.getSession();
			Dictionary dict=new Hashtable();
			System.out.println("In sale scrap assets operation case 2");
			String sub_id[]=request.getParameterValues("catValue");
			String main_cat=request.getParameter("main_cat");
			String reason=request.getParameter("reason");
			String prog_id=request.getParameter("prg_id");
			String depart=request.getParameter("depart");
			String unit=request.getParameter("unit");
			String email=request.getParameter("email");
			System.out.println("Main Category ");
			for(int i=0;i<sub_id.length;i++)
			{
				System.out.println("prog id "+sub_id[i]);
			}
			dict.put("main_cat", main_cat);
			dict.put("reason", reason);
			dict.put("prog_mst_id", prog_id);
			dict.put("depart", depart);
			dict.put("unit", unit);
			dict.put("created_by", email);
			ArrayList transfer_dep_hod=db.HODShortNames(dict.get("prog_mst_id").toString());
			String cur_hod_sh_name=transfer_dep_hod.get(0).toString().replace("[", "").replace("]", "").toUpperCase();
			dict.put("hod_sh_name", cur_hod_sh_name);
			ArrayList tran_dep=db.Assets_Sale_scrap_req_id(dict);
			String req_id=tran_dep.get(0).toString().replace("[", "").replace("]", "");
			dict.put("req_id", req_id);
				
			if(tran_dep.get(1).equals("true"))
			{
				for(int i=0;i<sub_id.length;i++)
				{
					boolean bol=db.Assets_Sale_scrap_Data(dict, sub_id[i]);
					if(bol==true)
					{
						session.setAttribute("error", "Request is send HOD/HOI Approvel");
					}
					else
					{
						session.setAttribute("error", "Failed To Send HOD/HOI please try again");
					}	
				}	
			}
			else
			{
				session.setAttribute("error", "Failed To Send HOD/HOI please try again");
				
			}
			response.sendRedirect("jsp/assetmanagement/Assets_Scrap_Sale.jsp");
			break;
		case 3:
			System.out.println("Assets Scrap case 3");
			session=request.getSession();
			String comment=request.getParameter("txtMsg");
			String status=request.getParameter("status123");
			req_id=request.getParameter("reg_id");
			String user_id=request.getParameter("email");
			String stat="";
			if(status.equals("1"))
			{
				stat="1";
			}
			else{
				stat="2";
			}
			boolean flag2=db.updateAssetsSSStatus(req_id, stat, comment);
			if(flag2==true)
			{
				session.setAttribute("error", " Assets Sale/Scrap Status updated");
			}
			else
			{
				session.setAttribute("error", "failed to update status");
			}
			response.sendRedirect("jsp/assetmanagement/Assets_HOD_Sale_Scrap.jsp");	
			break;
		case 4:
			System.out.println("In If Condition 4");
			session=request.getSession();
			String requ_id=request.getParameter("req_id");
			String optn=request.getParameter("option");
			if(optn.equals("1"))
			{
				String reqst_id=request.getParameter("req_id");
				ArrayList al=db.UpdateAssetRegSaleS(reqst_id);
				for(int i=0;i<al.size();i++)
				{
					int j=0;
					ArrayList arr=(ArrayList)al.get(i);
					while(j<arr.size())
					{
						String Asset_reg=arr.get(0).toString().replace("[", "").replace("]", "");
						boolean b=db.SaleScrapFinal(Asset_reg, reqst_id);
						j=j+1;
					}
				}
				response.sendRedirect("jsp/assetmanagement/Assets_Sale_Srap_reject_status.jsp");
			}
			else if(optn.equals("2"))
			{
				String stata="0";
				boolean upd=db.updateAssetsSSStatusUp(requ_id, stata, "");
				if(upd==true)
				{
					session.setAttribute("error", " Assets Sale/Scrap Status updated Send HOD approval");
				}
				else
				{
					session.setAttribute("error", "failed to update status Try again");
				}
				response.sendRedirect("jsp/assetmanagement/Assets_Sale_Srap_reject_status.jsp");
			}
			else if(optn.equals("3"))
			{
				String reqst_id=request.getParameter("req_id");
				String stata="0";
				boolean upd=db.updateAssetsSSStatusMTUPUp(requ_id, stata, "");
				if(upd==true)
				{
					session.setAttribute("error", " Assets Sale/Scrap Status updated Send Mt approval");
				}
				else
				{
					session.setAttribute("error", "failed to update status Try again");
				}
				response.sendRedirect("jsp/assetmanagement/Assets_Sale_Srap_reject_status.jsp");
			}
			break;
		case 5:
			System.out.println("Assets Scrap case 5");
			session=request.getSession();
			comment=request.getParameter("txtMsg");
			status=request.getParameter("status123");
			req_id=request.getParameter("reg_id");
			user_id=request.getParameter("email");
			stat="";
			if(status.equals("1"))
			{
				stat="1";
			}
			else
			{
				stat="2";
			}
			System.out.println("Comment value "+comment);
			flag2=db.updateAssetsSSStatusMT(req_id, stat, comment);
			if(flag2==true)
			{
				session.setAttribute("error", " Assets Sale/Scrap Status updated");
			}
			else
			{
				session.setAttribute("error", "failed to Send Report To Associate");
			}
			response.sendRedirect("jsp/assetmanagement/MT_sale_Scrap.jsp");	
			break;
			
			default :
				System.out.println("Invalid Option");
				break;
		}
	}
	
	private void AssetsTransfer(HttpServletRequest request, HttpServletResponse response) throws CmsSQLException, CmsNamingException, CmsGeneralException, IOException 
	{
		DateManager dm = new DateManager();	
		Assets_DBQueries db=new Assets_DBQueries();
		HttpSession session=request.getSession();
		String str[]={"1","2","3","4","5","6"};
		String str2="";
		String opt=request.getParameter("op");
		for(String str1: str)
		{
			if(str1.equals(opt))
			{
				switch(Integer.parseInt(opt))
				{
				case 1:
					Dictionary dict=new Hashtable();
					String cur_details[]={"depart","prog_mst_id","unit","created_by"};
					System.out.println("In case 1 Insert method");
					String sub_id[]=request.getParameterValues("catValue");
					String cur_dep[]=request.getParameterValues("Cur_dep");
					String tran_dep_id=request.getParameter("dep_name");
					String main_cat=request.getParameter("main_cat");
					String reason=request.getParameter("reason");
					dict.put("reason", reason);
					for(int i=0;i<cur_dep.length;i++)
					{
						dict.put(cur_details[i], cur_dep[i]);
					}
					ArrayList tran_dep=db.Asset_transfer(1, dict, tran_dep_id, "");
					dict.put("trans_proc_mst_id", tran_dep_id);
					for(int i=0;i<tran_dep.size();i++)
					{
						ArrayList arr=(ArrayList)tran_dep.get(i);
						dict.put("tran_unit", arr.get(0));
						dict.put("tran_dep", arr.get(1));
					}
					
					//fetch hod short name
					String cur_proc=dict.get("prog_mst_id").toString();
					ArrayList transfer_dep_hod=db.HODShortNames(tran_dep_id);
					String tran_hod_sh_name=transfer_dep_hod.get(0).toString().replace("[", "").replace("]", "").toUpperCase();
					transfer_dep_hod=db.HODShortNames(cur_proc);
					String cur_hod_sh_name=transfer_dep_hod.get(0).toString().replace("[", "").replace("]", "").toUpperCase();
					
					dict.put("cur_hod_sh_name", cur_hod_sh_name);
					dict.put("trans_hod_sh_name", tran_hod_sh_name);
					tran_dep=db.Asset_transfer(2, dict, "", "");
					String req_id=tran_dep.get(0).toString().replace("[", "").replace("]", "");
					dict.put("req_id", req_id);
					
					if(tran_dep.get(1).equals("true"))
					{
						for(int i=0;i<sub_id.length;i++)
						{
							tran_dep=db.Asset_transfer(3, dict, sub_id[i], "");
							if(tran_dep.get(0).equals("true"))
							{
								session.setAttribute("error", "Request Is send HOD/HOI Approvel");
							}
							else
							{
								session.setAttribute("error", "Failed To Send HOD/HOI please try again");
							}	
						}	
					}
					else
					{
						session.setAttribute("error", "Failed To Send HOD/HOI please try again");
						
					}
					response.sendRedirect("jsp/assetmanagement/Assets_transfer_Form.jsp");
					break;
				
				case 2:
					System.out.println("in case 2");
					str2=request.getParameter("TrStatus");
					String id=request.getParameter("id");
					String msg=request.getParameter("txtMsg");
					String imp=request.getParameter("imp");
					String hod=request.getParameter("hod");
					String bol="";
					String status="";
					System.out.println("Transfer value "+str2);
					if(str2.equals("0"))
					{
						status="1";
					}
					else
					{
						status="2";
					}
					if(hod.equals("1"))
					{
						ArrayList fl=db.selectAssetTransfer(4,status,id, msg);
						bol=fl.toString().replace("[", "").replace("]", "");
					}
					else if(hod.equals("2"))
					{
						String fac_names[]=request.getParameterValues("Owner");
						String NewLoc[]=request.getParameterValues("newLoc");
						String reg_nu[]=request.getParameterValues("reg_no");
						String name_id[]=new String[reg_nu.length];
						ArrayList fl=db.Asset_trans_hod_appr(fac_names,reg_nu,status,id, msg, NewLoc);
						bol=fl.toString().replace("[", "").replace("]", "");
					}
					if(bol.equals("true"))
					{
						session.setAttribute("error", "Updated status ");
					}
					else
					{
						session.setAttribute("error", "Failed To Update ");
					}
					if(imp.equals("1"))
					{
						response.sendRedirect("jsp/assetmanagement/Assets_Transfer_Appr.jsp");
					}
					else if(imp.equals("2"))
					{
						response.sendRedirect("jsp/assetmanagement/Assets_transfer_Hod_appr.jsp");
					}
					break;
					
				case 3:
						String depart1=request.getParameter("reg_id");
						String depart2=request.getParameter("stat");
						break;
				case 4:
					String swap=request.getParameter("swap");
					String tr_hod_sh_name=request.getParameter("tr_sh_name");
					String newR_id=request.getParameter("req_id");
					if(swap.equals("1"))
					{
						boolean tf=db.MT_status_update(newR_id);
						if(tf==true)
						{
							session.setAttribute("error", "Request has been send to MT");
						}
						else
						{
							session.setAttribute("error", "Failed To resend");
						}
						response.sendRedirect("jsp/assetmanagement/Assets_Transfer_Status_HOD.jsp");
					}
					else
					{
					ArrayList reg_own=db.selectAssetTransfer(7, "", newR_id, "");
					ArrayList depart_det=db.selectAssetTransfer(17, "", newR_id, "");
					ArrayList sql1=new ArrayList();
					boolean bolVal=false;
					String prog_id="";
					String unit="";
					String depa="";
					for(int i=0;i<reg_own.size();i++)
					{
						ArrayList sql=(ArrayList)reg_own.get(i);
					}
					for(int i=0;i<depart_det.size();i++)
					{
						sql1=(ArrayList)depart_det.get(i);
						prog_id=""+sql1.get(2);
						unit=""+sql1.get(0);
						depa=""+sql1.get(1);
					}
					Dictionary dic=new Hashtable();
					String ref_no;
					String r_id;
					ArrayList reg=new ArrayList();
					ArrayList sub_sh_name=new ArrayList();
					ArrayList ref_numb=new ArrayList();
					int n=1;
					Dictionary dict1=new Hashtable();
					int ref_id=1;
					for(int j=0;j<reg_own.size();j++)
					{
						//Fetch sub category Id
						ArrayList sql=(ArrayList)reg_own.get(j);
						ArrayList new_id=db.selectAssetTransfer(9, "", sql.get(0).toString(), "");
						String asse_reg[]={"main_cat1","sub_cat","entry_date","Descrip","bill_no","bill_date","depart","quantityt","amount","main_name","sub_name"};
						for(int h=0;h<new_id.size();h++)
						{
							ArrayList sub_s=(ArrayList)new_id.get(h);
							for(int f=0;f<sub_s.size();f++)
							{
								dict1.put(asse_reg[f],sub_s.get(f));
							}
						}
						String catId=new_id.get(0).toString().replace("[", "").replace("]", "");
						
						//Fetch sub category short name
						ArrayList val=db.updateAssetsRegister(2, dic, dict1.get("sub_cat").toString(), "");
						String short_name=val.get(0).toString().replace("[", "").replace("]", "");	
						dict1.put("short_name", short_name);
						
						//Fetch Max reference number
						ArrayList ref=new ArrayList();
						ArrayList val1=db.updateAssetsRegister(1, dic,  dict1.get("sub_cat").toString(), prog_id);
						String st=val1.get(0).toString().replace("[", "").replace("]", "");
						int k=0;
						if(st.equals("null"))
						{
							r_id=String.format("%03d", n);
						}
						else
						{
							k=Integer.parseInt(st);
							n=k+1;
							r_id=String.format("%03d", n);
						}
						String dt=dm.getCurrentDate();
						String bDate1=dm.format(dt,"MM-dd-yyyy");
						String mm=bDate1.substring(0,2);
						String yy=bDate1.substring(8, 10);
						String new_ref_no=""+unit+"/"+depa+"/"+short_name.toLowerCase()+"-"+r_id+"/"+mm+"-"+yy;
						bolVal=db.UpdateAssetsRegisterTable(n, new_ref_no, prog_id, depa, unit, sql.get(1).toString(), sql.get(0).toString(), newR_id, dict1, tr_hod_sh_name, sql.get(2).toString());
					}
					if(bolVal==true)
					{
						session.setAttribute("error", "Assets Transfered Process Sucessfully Completed");
					}
					else
					{
						session.setAttribute("error", "Assets Transfered Failed Please Try Again");
					}
					response.sendRedirect("jsp/assetmanagement/Assets_Transfer_Status_HOD.jsp");
					}
					break;
				case 5:
					System.out.println("Case 5 Here");
					str2=request.getParameter("TrStatus");
					id=request.getParameter("id");
					msg=request.getParameter("txtMsg");
					System.out.println("Mt status "+str2);
					if(str2.equals("0"))
					{
						status="1";
					}
					else
					{
						status="3";
					}
					ArrayList fl=db.selectAssetTransfer(15,status,id, msg);
					bol=fl.toString().replace("[", "").replace("]", "");
					if(bol.equals("true"))
					{
						session.setAttribute("error", "Status Updated Sucessfully Thank You");					
					}
					else
					{
						session.setAttribute("error", "Rejected Report Send To Requested Department");
					}
					response.sendRedirect("jsp/assetmanagement/ManagementAppr.jsp");
					break;
				case 6:
					System.out.println("in case 6");
					boolean bol1=false;
					String fac_names[]=request.getParameterValues("Owner");
					String reg_nu[]=request.getParameterValues("reg_no");
					String name_id[]=new String[reg_nu.length];
					boolean f2=db.AssetRegisterNewAssocAssign(fac_names,reg_nu);
					if(f2==true)
					{
						session.setAttribute("error", "Updated status ");
					}
					else
					{
						session.setAttribute("error", "Failed To Update ");
					}
					response.sendRedirect("jsp/assetmanagement/Assets_HOD_Assign_new_Associate.jsp");
					break;
				default:
					System.out.println("Invalid option");
					break;
				}
			}
			else
			{
				System.out.println("Not exists else part");
			}
		}
		
	}
	private void UploadDigitalSign(HttpServletRequest request, HttpServletResponse response, List<FileItem> request1) throws IOException 
	{
		
		
		 System.out.println("Digital Sign");
		HttpSession session=request.getSession();
		Dictionary dict=new Hashtable();
		Dictionary progId=new Hashtable();
		Digital_signDBQuery dg=new Digital_signDBQuery();
		ReferenceNo_DBQuery ref=new ReferenceNo_DBQuery();
		int count=0;
		for(FileItem file : request1)
		{
		 	String fieldName = file.getFieldName();
		 	if(fieldName.equals("chk"))
		 	{
		 		String desc1 = file.getString();
		 		progId.put(count, desc1);
		 		count++;
		 	}
		 	else
		 	{
		 		String desc = file.getString();
	            int f=ar.indexOf(desc);
	            dict.put(fieldName, desc);
		 	}
		}
		System.out.println("Prog Id "+progId.size());
		boolean ar=false;
		String str="";
		if(dict.get("Update").toString().equals("1"))
		{
			for(int p=0;p<progId.size();p++)
			{
				String exist=dg.ExistsData(progId.get(p).toString());
				if(exist.isEmpty())
				{
					dict=updateData(request, dict, request1, progId.get(p).toString());
					ar=dg.inserPersonalFileDetails(dict, progId.get(p).toString());
					if(ar)
					{
						str="HOD/HOI Sign Uploaded ";
					}
					else
					{
						str="Unable to upload HOD/HOI Signature Try Again";
					}
				}
				else
				{
					str="HOD/HOI Already Uploaded";
				}
			}
		}
		else if(dict.get("Update").toString().equals("2"))
		{
			System.out.println("Size "+progId.size());
			for(int p=0;p<progId.size();p++)
			{
				dict=updateData(request, dict, request1, progId.get(p).toString());
				ar=dg.updateDigitalSign(dict, progId.get(0).toString());
				if(ar)
				{
					str="HOD/HOI Sign Updated ";
				}
				else
				{
					str="Unable to update HOD/HOI Signature Try Again";
				}
			}
		}
		session.setAttribute("msg", str);
		response.sendRedirect("jsp/referencenumber/Digital_Sign_configuration.jsp");
	}
	
	public Dictionary updateData(HttpServletRequest request, Dictionary dict, List<FileItem> request1, String progId)
	{
		Digital_signDBQuery dg=new Digital_signDBQuery();
		String destinationPath = null;
		String depName=dg.DepartmentName(progId);
		System.out.println("Department data "+depName);
		dict.put("depName", depName);
		try {
			destinationPath=upLoadCourseOutlineForPedagogy(request, dict, dict.get("HodId").toString(), 4);
			System.out.println("Destination Path "+destinationPath);
			UploadBean uploadBean = new UploadBean();
			uploadBean.setFolderstore(destinationPath);
			dict.put("ServerPath", destinationPath);
			for(FileItem item : request1){
				if(!item.isFormField())
				{
					String name = new File(item.getName()).getName();
					System.out.println("File Upload option "+name);
					dict.put("imageName", name);
		            item.write( new File(destinationPath+ File.separator + name));
				}
	         }
		}catch(Exception e)
		{
			System.out.println("Exception "+e);
		}
		return dict;
	}
}
