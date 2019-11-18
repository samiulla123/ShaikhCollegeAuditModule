package com.gen.cms.assets.beans;

import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;

import com.gen.cms.exceptions.CmsGeneralException;
import com.gen.cms.exceptions.CmsNamingException;
import com.gen.cms.exceptions.CmsSQLException;
import com.gen.cms.util.DateManager;
public class AuditDBQuery {
	AssetsDAO ad=new AssetsDAO();
	DateManager dt=new DateManager();
	String date=dt.getCurrentDate();
	String time=dt.getCurrentTime();
	ArrayList ar=new ArrayList();
	String sql="";
	boolean flag=false;
	public boolean insertNewAuditType(String email, String auditName)
	{
		sql="insert into auditype(auditTypeName, created_by, created_on, created_at) values('"+auditName+"'," +
				"'"+email+"','"+date+"','"+time+"')";
		System.out.println("SQL Query For create new audit type "+sql);
		flag=ad.insert(sql);
		return flag;
	}
	
	public ArrayList selectAuditType()
	{
		sql="select * from auditype";
		try {
			ar=ad.select(sql);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ar;
	}
	
	public String selectAuditSingleType(String auditType)
	{
		String str="";
		sql="select audittypename from auditype where audit_type='"+auditType+"'";
		System.out.println("Sql Query "+sql);
		try {
			/*ar=ad.select(sql);*/
			str=ad.selectText(sql);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("String Fetch Value "+str);
		return str;
	}
	
	public ArrayList selectDepartment()
	{
		sql="select * from adm_program_master";
		try {
			ar=ad.select(sql);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ar;
	}
	
	public ArrayList selectAudeteeName(String prog_id)
	{
		ArrayList fac=new ArrayList();
		sql="select ADMSN_FAC_ID  from admsn_fac_curr_job_info where PROG_MST_ID='"+prog_id+"' and not exists (select * from hr_resignation where admsn_fac_curr_job_info.ADMSN_FAC_ID=hr_resignation.ADM_FAC_ID)";

		try {
			fac=ad.select(sql);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList faculty=new ArrayList();
		ArrayList arr=new ArrayList();
		ArrayList autd=new ArrayList();
		for(int i=0;i<fac.size();i++)
		{
			ArrayList prg = (ArrayList)fac.get(i);
			for(int j=0;j<prg.size();j++)
			{
				System.out.println("Id "+prg.get(j));
				sql="select ADMSN_FAC_ID, ADMSN_FAC_FNAME, ADMSN_FAC_MNAME, ADMSN_FAC_LNAME, ADMSN_FAC_SH_NAME from ADMSN_FACULTY_INFO where ADMSN_FAC_ID='"+prg.get(j)+"'";
				try {
					ArrayList data=ad.select(sql);
					if(!data.isEmpty())
					{
						for(int k=0;k<data.size();k++)
						{
							ArrayList dt=(ArrayList)data.get(k);
							autd.add(dt);
						}
					}
				} catch (CmsSQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CmsNamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CmsGeneralException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		return autd;
	}
	
	
	public ArrayList SlectDepAssociate(String email)
	{
		ArrayList ar=new ArrayList();
		String sql="select PROG_MST_ID from ADMSN_FAC_CURR_JOB_INFO where admsn_fac_id='"+email+"'";
		System.out.println("Prog Mst id select query "+sql);
		ArrayList prg=new ArrayList();
		try {
			prg=ad.select(sql);
			System.out.println("Id found "+prg);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String mst_id=prg.get(0).toString().replace("[", "").replace("]", "");
		System.out.println("Prog Mst Id="+mst_id);
		ArrayList fac=new ArrayList();
		sql="select ADMSN_FAC_ID  from admsn_fac_curr_job_info where PROG_MST_ID='"+mst_id+"' and not exists (select * from hr_resignation where admsn_fac_curr_job_info.ADMSN_FAC_ID=hr_resignation.ADM_FAC_ID)";
		System.out.println("Faculty id select query "+sql);
		try {
			fac=ad.select(sql);
			System.out.println("Select Query facultyt data "+fac);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList faculty=new ArrayList();
		for(int i=0;i<fac.size();i++)
		{
			prg=(ArrayList)fac.get(i);
			for(int j=0;j<prg.size();j++)
			{
				System.out.println("Id "+prg.get(j));
				sql="select ADMSN_FAC_ID, ADMSN_FAC_FNAME, ADMSN_FAC_MNAME, ADMSN_FAC_LNAME, ADMSN_FAC_SH_NAME from ADMSN_FACULTY_INFO where ADMSN_FAC_ID='"+prg.get(j)+"'";
				System.out.println("Faculty info query "+sql);
				try {
					ArrayList data=ad.select(sql);
					System.out.println("Data Found is "+data);
					if(!data.isEmpty())
					{
						for(int k=0;k<data.size();k++)
						{
							ArrayList dt=(ArrayList)data.get(k);
							ar.add(dt);
						}
					}
				} catch (CmsSQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CmsNamingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (CmsGeneralException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
		}
		System.out.println("Array List data is "+ar);
		return ar;
	}
	
	//Insert Audit Schedule
	public boolean AddAuditScheduleData(Dictionary dict)
	{
		boolean flag=false;
		DateManager dt=new DateManager();
		String date=dt.getCurrentDate();
		AssetsDAO ad=new AssetsDAO();
		String refNo="";
		String time=dt.getCurrentTime();
		String sql="insert into auditschedule(Audit_type, Status, DepUnit_id, auditee_id, auditor_id, audit_date, audit_from, audit_to, created_by, created_on, created_at, auditCompleteSts, audittypeprepoint, pointreferencenumb)" +
				"values('"+dict.get("AuditType")+"','"+0+"', '"+dict.get("ProgId")+"','"+dict.get("AuditeeId")+"', '"+dict.get("AuditorId")+"','"+dict.get("date")+"','"+dict.get("From")+"','"+dict.get("to")+"'" +
						",'"+dict.get("createdBy")+"','"+date+"','"+time+"','"+0+"','"+dict.get("PreviousPoint")+"','"+refNo+"')";
		System.out.println("SQL Query "+sql);
		flag=ad.insert(sql);
		return flag;
	}
	
	//Audit Observation sheet
	public boolean AuditObservationSheet(Dictionary dict)
	{
		boolean flag=false;
		DateManager dt=new DateManager();
		String date=dt.getCurrentDate();
		AssetsDAO ad=new AssetsDAO();
		String time=dt.getCurrentTime();
		String sql="insert into AUDITOBSERVATION(AUDIT_ID, STATUS, PROG_ID, AUDITPOINT, AUDITMARKS, ACTIONBY, FinalSubmit, CLOSUREDATE, CREATEDBY, CREATEDON, CREATEDAT, audittypeid, marks_type, auditScheduleDateDate)" +
				"values('"+dict.get("auditId")+"','"+dict.get("status")+"', '"+dict.get("Prog_Mst_Id")+"','"+dict.get("auditTextArea")+"', '"+dict.get("audit_mark")+"','"+dict.get("auditorName")+"','"+0+"'," +
						"'"+dict.get("ClosureDate")+"','"+dict.get("createdBy")+"','"+date+"','"+time+"','"+dict.get("auditType")+"','"+dict.get("markType")+"','"+dict.get("uniqueDate")+"')";
		System.out.println("SQL Query "+sql);
		flag=ad.insert(sql);
		return flag;
	}
	
	//Audit Fetch For List
	public ArrayList FetchAuditList(String email)
	{
		ArrayList list=new ArrayList();
		ArrayList audit2=new ArrayList();
		String date=dt.getCurrentDate();
		String audirType="";
		String Sql="select * from AUDITSCHEDULE where created_by='"+email+"' and status='"+0+"'";
		System.out.println("Fetch Data Query is "+Sql);
		try {
			list=ad.select(Sql);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList FetchUnitName(String progId)
	{
		ArrayList un=new ArrayList();
		String sql1="select prog_mst_name from adm_program_master where prog_mst_id='"+progId+"'";
		System.out.println("Department Name "+sql1);
		try {
			un=ad.select(sql1);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return un;
	}
	
	public ArrayList FetchFacultyShName(String email)
	{
		ArrayList faName=new ArrayList();
		String sql2="select admsn_fac_sh_name from admsn_faculty_info where admsn_fac_id='"+email+"'";
		System.out.println("Faculty Short Name "+sql2);
		try {
			faName=ad.select(sql2);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return faName;
	}
	
	public boolean updateAuditScheduleStatus(String data)
	{
		boolean flag=false;
		sql="update AUDITSCHEDULE set status='"+1+"' where audit_id='"+data+"'";
		System.out.println("Schedule Id "+sql);
		flag=ad.insert(sql);
		return flag;
	}
	
	public boolean updateAuditScheduleStatus123(String data)
	{
		boolean flag=false;
		sql="update AUDITSCHEDULE set status='"+1+"', auditCompleteSts='"+1+"' where audit_id='"+data+"'";
		System.out.println("Schedule Id "+sql);
		flag=ad.insert(sql);
		return flag;
	}
	
	public boolean updateAuditObservationStatusData(String data)
	{
		boolean flag=false;
		sql="update auditobservation set finalsubmit='"+1+"' where AUDITOB_ID='"+data+"'";
		System.out.println("Schedule Id "+sql);
		flag=ad.insert(sql);
		return flag;
	}

	public boolean updateAuditObservationFinal(String id)
	{
		boolean flag=false;
		String auditobId="";
		String sql1="select auditob_id from auditpreviousstatus where audits_id='"+id+"'";
		System.out.println("Query "+sql1);
		try {
			auditobId=ad.selectText(sql1);
			System.out.println("Audit Observation ID "+auditobId);
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String sql="update AUDITOBSERVATION set FinalSubmit='"+0+"' where auditob_id='"+auditobId+"'";
		flag=ad.insert(sql);
		return flag;
	}
	
	public boolean updateAuditObser(Dictionary dict)
	{
		boolean flag=false;
		boolean flag12=false;
		String sql2="update auditpreviousstatus set status='"+dict.get("status")+"', created_by='"+dict.get("createdBy")+"', created_on='"+date+"', created_at='"+time+"' where audits_id='"+dict.get("auditsId")+"'";
		System.out.println("Update Data "+sql2);
		flag=ad.insert(sql2);
		String auditobId="";
		if(flag)
		{
			String sql1="select auditob_id from auditpreviousstatus where audits_id='"+dict.get("auditsId")+"'";
			System.out.println("Query "+sql1);
			try {
				auditobId=ad.selectText(sql1);
				System.out.println("Audit Observation ID "+auditobId);
			} catch (CmsNamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CmsSQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CmsGeneralException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			sql="update auditobservation set status='"+dict.get("status")+"', createdby='"+dict.get("createdBy")+"', closuredate='"+dict.get("ClosureDate")+"', createdon='"+date+"', createdat='"+time+"' where auditob_id='"+auditobId+"'";
			System.out.println("Update Data "+sql);
			flag=ad.insert(sql);
			/*if(flag)
			{
				String empty="";
				String sql3="update auditschedule set audittypeprepoint='"+empty+"'";
				flag12=ad.insert(sql3);
			}*/
		}
		return flag;
	}
	
	public int auditObsrStatus(String ProgId, String typeId)
	{
		ArrayList n=new ArrayList();
		String str="select count(audit_id) from auditpreviousstatus where progmstid='"+ProgId+"' and AUDITOR_STATUS='"+0+"' and audittype='"+typeId+"'";
		System.out.println("Schedule Id "+str);
		try {
			n=ad.select(str);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		int value=Integer.parseInt(n.get(0).toString().replace("[", "").replace("]", ""));
		System.out.println("Size Of audit "+n+" value = "+value);
		return value;
	}
	
	public ArrayList auditScheduleLink(String email)
	{
		ArrayList link=new ArrayList();
		String sql="select * from AUDITSCHEDULE where auditor_id='"+email+"' and status='"+1+"' and auditCompleteSts='"+0+"'";
		System.out.println("Faculty Short Name "+sql);
		try {
			link=ad.select(sql);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return link;
	}
	
	public ArrayList SelectAuditObservation(String email, String progId)
	{
		ArrayList ob=new ArrayList();
		String sql="select * from AUDITOBSERVATION where CREATEDBY='"+email+"' and FinalSubmit='"+0+"' and PROG_ID='"+progId+"'";
		System.out.println("Faculty Short Name "+sql);
		try {
			ob=ad.select(sql);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ob;
	}
	
	public ArrayList SelectEmployeName(String id)
	{
		ArrayList ob=new ArrayList();
		sql="select ADMSN_FAC_ID, ADMSN_FAC_FNAME, ADMSN_FAC_MNAME, ADMSN_FAC_LNAME, ADMSN_FAC_SH_NAME from ADMSN_FACULTY_INFO where ADMSN_FAC_ID='"+id+"'";
		System.out.println("Faculty info query "+sql);
		try {
			ob=ad.select(sql);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ob;
	}
	
	public boolean inserObsSheetStatus(String auditId, String auditObId, String progId, String sts, String createdBy, String auditType, String actionBy)
	{
		boolean flag=false;
		sql="insert into AUDITPREVIOUSSTATUS(AUDIT_ID, AUDITOB_ID, PROGMSTID, STATUS, CREATED_BY, CREATED_ON, CREATED_AT, audittype, auditor_status, actionBy) values('"+auditId+"','"+auditObId+"','"+progId+"','"+sts+"','"+createdBy+"','"+date+"','"+time+"','"+auditType+"','"+0+"','"+actionBy+"')";
		System.out.println("Action by Query "+sql);
		flag=ad.insert(sql);
		return flag;
	}
	
	public ArrayList SelectAuditPoints(String auditId)
	{
		ArrayList ar=new ArrayList();
		String str[]=null;
		sql="select auditpoint, closuredate from auditobservation where auditob_id='"+auditId+"'";
		System.out.println("Action by Query "+sql);
		try {
			ar=ad.select(sql);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ar;
	}
	
	
	public ArrayList selectPendingPoints(String email)
	{
		ArrayList pend=new ArrayList();
		String sql="select * from auditpreviousstatus where actionby='"+email+"' and status='"+0+"'";
		System.out.println("select pending point Query "+sql);
		try {
			pend=ad.select(sql);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pend;
	}
	
	public ArrayList AuditselectPendingPoints(String email, String prgId)
	{
		ArrayList pend=new ArrayList();
		String sql="select * from auditpreviousstatus where status='"+0+"' or status='"+1+"' and progmstid='"+prgId+"' and auditor_status='"+0+"'";
		System.out.println("select pending point Query "+sql);
		try {
			pend=ad.select(sql);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return pend;
	}
	
	public String selectPrePoint(String Prog_Id)
	{
		String str="";
		sql="select audittypeprepoint from auditschedule where depunit_id='"+Prog_Id+"'";
		try {
			str=ad.selectText(sql);
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	public String selectScheduleRefNo(String auditId)
	{
		String str="";
		sql="select pointreferencenumb from auditschedule where audit_id='"+auditId+"'";
		try {
			str=ad.selectText(sql);
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return str;
	}
	
	public ArrayList PreviousPointData(String progId, String Type)
	{
		ArrayList ar=new ArrayList();
		int count=0;
		ArrayList ara=new ArrayList();
		ArrayList data=new ArrayList();
		sql="select auditob_id, audits_id from auditpreviousstatus where progmstid='"+progId+"' and audittype='"+Type+"' and not created_on='"+date+"'";
		System.out.println("AuditPoint Query123 "+sql);
		try {
			ar=ad.select(sql);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Value "+ar);
		System.out.println("Size "+ar.size());
		for(int i=0;i<ar.size();i++)
		{
			ArrayList d=(ArrayList)ar.get(i);
			System.out.println("Audit Id 1"+d.get(0));
			String sql1="select auditob_id, auditpoint, closuredate from auditobservation where auditob_id='"+d.get(0)+"'";
			System.out.println("AuditPoint Query "+sql1);
			try {
				 /*ara=ad.select(sql1);*/
				ArrayList ddd=ad.select(sql1);
				for(int k=0;k<ddd.size();k++)
				{
					data = (ArrayList)ddd.get(k);
					data.add(d.get(1));
				}
				System.out.println("Data Value "+data);
				ara.add(data);
				/*System.out.println("BEfore data "+ddd);
				ddd.add(d.get(1));
				System.out.println("After data "+ddd);*/
			/*	ara.add(0, d.get(1));*/
				System.out.println("Data Array List123 "+ara);
			} catch (CmsSQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CmsNamingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CmsGeneralException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			count=count+1;
		}
		System.out.println("Data Array List "+ara);
		return ara;
	}
	
	public boolean Updatepending(String auditSts, String sts, String cmpdate, String cmnts)
	{
		boolean flag=false;
		String sql="update auditpreviousstatus set status='"+sts+"', actual_completDate='"+cmpdate+"', status_comments='"+cmnts+"' where audits_id='"+auditSts+"'";
		System.out.println("Audit Status Query "+sql );
		flag=ad.insert(sql);
		return flag;
	}
	
	public boolean UpdatePendingAuditStatus(String auditSts, String sts)
	{
		boolean flag=false;
		String sql="update auditpreviousstatus set status='"+sts+"', auditor_status='"+sts+"' where audits_id='"+auditSts+"'";
		System.out.println("Audit Status Query "+sql );
		flag=ad.insert(sql);
		return flag;
	}
	
	public ArrayList selectAuditObservation(String progId, String auditId)
	{
		AuditDBQuery db=new AuditDBQuery();
		ArrayList ar=new ArrayList();
		String auditData="select * from auditObservation where audit_id='"+auditId+"'";
		System.out.println("Prog Mst Id "+auditData);
		ar=selectQuery(auditData);
		System.out.println("Select Query Data "+ar);
		return ar;
	}
	
	public ArrayList selectPreviousStatus(String auditId)
	{
		AuditDBQuery db=new AuditDBQuery();
		ArrayList ar=new ArrayList();
		String auditData="select ACTUAL_COMPLETDATE, STATUS_COMMENTS from auditpreviousstatus where auditob_id='"+auditId+"'";
		System.out.println("Prog Mst Id "+auditData);
		ar=selectQuery(auditData);
		System.out.println("Select Query Data "+ar);
		return ar;
	}
	
	public ArrayList selectAuditDetails()
	{
		AuditDBQuery db=new AuditDBQuery();
		ArrayList ar=new ArrayList();
		String auditData="select * from auditschedule";
		System.out.println("Prog Mst Id "+auditData);
		ar=selectQuery(auditData);
		System.out.println("Select Query Data "+ar);
		return ar;
	}
	
	public ArrayList selectQuery(String query)
	{
		ArrayList data=new ArrayList();
		try {
			data=ad.select(query);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public String selectString(String query)
	{
		String data="";
		try {
			data=ad.selectText(query);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return data;
	}
	
	public String SelectProgId(String email)
	{
		System.out.println("In Select ProgID ");
		ArrayList pgID=new ArrayList();
		String sql="select PROG_MST_ID from ADMSN_FAC_CURR_JOB_INFO where admsn_fac_id='"+email+"'";
		System.out.println("Prog Mst id select query "+sql);
		try {
			pgID=ad.select(sql);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String prgId=pgID.get(0).toString().replace("[", "").replace("]", "");
		System.out.println("Id found ProgID "+pgID);
		return prgId;
	}
	
	public boolean updateAuditScheduleRef(String refNo, String auditId)
	{
		boolean b=false;
		String updt="update auditschedule set pointreferencenumb='"+refNo+"' where audit_id='"+auditId+"'";
		System.out.println("Update Audit REference Number "+updt);
		b=ad.insert(updt);
		System.out.println("Sql Pass Bolean Value "+b);
		return b;
	}
	
	public String selecttype(String Atype)
	{
		String type="";
		String tp="select audittypename from auditype where audit_type='"+Atype+"'";
		type=selectString(tp);
		return type;
	}
	
	public String DepartmentName(String progId)
	{
		String name="";
		String fetchName="select prog_mst_name from adm_program_master where prog_mst_id='"+progId+"'";
		name=selectString(fetchName);
		return name;
	}
	
	public String DepartmentShortName(String progId)
	{
		String name="";
		String fetchName="select prog_mst_sh_name from adm_program_master where prog_mst_id='"+progId+"'";
		name=selectString(fetchName);
		return name;
	}
	
	public ArrayList SelectAuditSchduleDate()
	{
		ArrayList ar=new ArrayList();
		Date d=new Date();
		int year=d.getYear();
		int currentYear=year+1900;
		String sql="select distinct created_on from auditschedule where status='"+1+"' and auditcompletests='"+1+"' and created_on like '"+currentYear+"%'";
		System.out.println("Query "+sql);
		ar=selectQuery(sql);
		return ar;
	}
	
	public ArrayList SelectAuditPreSchduleDate()
	{
		ArrayList ar=new ArrayList();
		Date d=new Date();
		int year=d.getYear();
		int currentYear=year+1900;
		int previousYear=currentYear-1;
		String sql="select distinct created_on from auditschedule where status='"+1+"' and auditcompletests='"+1+"' and created_on like '"+previousYear+"%'";
		System.out.println("Query "+sql);
		ar=selectQuery(sql);
		return ar;
	}
	
	/*public ArrayList SelectDepartmentProgId(String auditType)
	{
		ArrayList ar=new ArrayList();
		Date d=new Date();
		int year=d.getYear();
		int currentYear=year+1900;
		ArrayList demo=new ArrayList();
		String sql="select distinct depunit_id, audit_id, created_on from auditschedule where auditcompletests='"+1+"' and created_on like '"+currentYear+"%'";
		System.out.println("Query "+sql);
		ar=selectQuery(sql);
		for(int i=0;i<ar.size();i++)
		{
			ArrayList ar2=(ArrayList)ar.get(i);
			String data=DepartmentShortName(ar2.get(0).toString());
			ar2.add(0, data);
			ArrayList at=selectauditOBDetails(ar2.get(1).toString(),ar2.get(2).toString(), auditType, ar2.get(3).toString());
			System.out.println("Array Index size "+at.size());
			for(int m=0;m<at.size();m++)
			{
				System.out.println("array index "+m);
				ArrayList n=(ArrayList)at.get(m);
				System.out.println("N value "+n.size());
				for(int l=0;l<n.size();l++)
				{
					System.out.println("list value "+n.get(l));
					ar2.add(l, n.get(l));
				}
				System.out.println("Array Data "+ar2);
			}
			demo.add(ar2);
		}
		System.out.println("Array Data "+demo);
		return demo;
	}*/
	
	public ArrayList SelectDepartmentProgId(String auditType)
	{
		ArrayList ar=new ArrayList();
		Date d=new Date();
		int year=d.getYear();
		int currentYear=year+1900;
		ArrayList demo=new ArrayList();
		String sql="select distinct depunit_id from auditschedule where audit_type='"+auditType+"' and auditcompletests='"+1+"' and created_on like '"+currentYear+"%'";
		System.out.println("Created Depunit Data query "+sql);
		ar=selectQuery(sql);
		/*for(int i=0;i<ar.size();i++)
		{
			ArrayList audSche=(ArrayList)ar.get(i);
			ArrayList val=selectAuditData(audSche.get(0).toString());
			demo.add(0, val.get(0));
			System.out.println("Val Data "+val);
			for(int j=0;j<val.size();j++)
			{
				ArrayList dnd=(ArrayList)val.get(j);
				System.out.println("Do not Disturb Data "+dnd);
				demo.add(dnd);
			}
		}*/
		return ar;
	}
	
	public ArrayList selectAuditData(String createdOn)
	{
		ArrayList ar=new ArrayList();
		String sql="select distinct prog_id from auditobservation where auditScheduleDateDate='"+createdOn+"'";
		System.out.println("Audit Observation Mngmnt Data "+sql);
		ar=selectQuery(sql);
		return ar;
	}
	
	public ArrayList selectauditOBDetails(String progId, String auditId, String auditType, String date)
	{
		ArrayList ar=new ArrayList();
		String sql="select * from auditobservation where audit_id='"+auditId+"' and prog_id='"+progId+"' and audittypeid='"+auditType+"' and auditScheduleDateDate='"+date+"'";
		System.out.println("Query Data beta "+sql);
		ar=selectQuery(sql);
		System.out.println("Details of audit of Date "+ar);
		return ar;
	}
	
	public ArrayList selectmarks(String auditId, String prog_id, String auditTypeId)
	{
		ArrayList mar=new ArrayList();
		String sql="select * from auditobservation where audit_id='"+auditId+"' and prog_id='"+prog_id+"' and audittypeid='"+auditTypeId+"'";
		mar=selectQuery(sql);
		System.out.println("Details of audit of Date "+mar);
		return mar;
	}
	
	public ArrayList selectmarksData(String created_On)
	{
		String sql1="select * from auditschedule where created_on='"+created_On+"'";
		ArrayList data=selectQuery(sql1);
		System.out.println("Details of audit of Date "+data);
		return data;
	}
	
	public ArrayList selectAuditObservationDetails(String prgId, String createdOn)
	{
		String sql="select marks_type, auditmarks from auditobservation where prog_id='"+prgId+"' and auditScheduleDateDate='"+createdOn+"'";
		ArrayList det=selectQuery(sql);
		return det;
	}
	
	public ArrayList SelectPreviousYearData(String auditType, String progID)
	{
		ArrayList ar=new ArrayList();
		Date d=new Date();
		int year=d.getYear();
		int currentYear=year+1900;
		int previousYear=currentYear-1;
		System.out.println("Previous Year "+previousYear);
		ArrayList demo=new ArrayList();
		String sql="select distinct depunit_id, created_on from auditschedule where audit_type='"+auditType+"' and depunit_id='"+progID+"' and auditcompletests='"+1+"' and created_on like '"+previousYear+"%'";
		System.out.println("Query for the data "+sql);
		ar=selectQuery(sql);
		return ar;
	}
	
	public ArrayList ArrayListCalPreVal(String auditType, String progId)
	{  
		ArrayList ar=new ArrayList();
		ArrayList PreData=SelectPreviousYearData(auditType, progId);
		System.out.println("Previous Data "+PreData);
		for(int i=0;i<PreData.size();i++)
		{
			ArrayList ar2=(ArrayList)PreData.get(i);
			ar=selectAuditObservationDetails(progId, ar2.get(1).toString());
			System.out.println("details Marks "+ar);
		}
		return ar;
	}
	
	public ArrayList AuditReportMessage(String email)
	{
		ArrayList ar=new ArrayList();
		
		String progId=SelectProgId(email);
		
		String sql="select * from auditschedule where depunit_id='"+progId+"' and status='"+1+"' and auditcompletests='"+0+"' and auditee_id='"+email+"'";
		System.out.println("Query "+sql);
		try {
			ar=ad.select(sql);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ar;
		
	}
	
	public ArrayList AuditSchedule(String email)
	{
		ArrayList ar=new ArrayList();
		String progId=SelectProgId(email);
		String sql="select audit_type, audit_date from auditschedule where depunit_id='"+progId+"' and auditcompletests='"+0+"'";
		try {
			ar=ad.select(sql);
		} catch (CmsSQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsNamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CmsGeneralException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return ar;
	}
	
}
