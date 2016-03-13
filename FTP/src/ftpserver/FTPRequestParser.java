package ftpserver;

public class FTPRequestParser 
{	
	public FTPRequestParser()
	{
		
	}
	
	public FTPRequest parseStringFTPRequest(String req)
	{
		FTPRequest ftp_req = new FTPRequest();
			
		String arg[];
				
		arg = req.split(" ",2);
				
		if(arg.length>1)			
			ftp_req.setArg(arg[1]);
		
		if(arg[0].equals("USER"))
		{
			ftp_req.setType(FTPRequestType.FTP_USER_REQ);
			if(arg.length != 2)
				ftp_req.setValid(false);
		}
		else if(arg[0].equals("PASS"))
		{
			ftp_req.setType(FTPRequestType.FTP_PASS_REQ);
			if(arg.length != 2)
				ftp_req.setValid(false);
		}
		else if(arg[0].equals("LIST"))
		{
			ftp_req.setType(FTPRequestType.FTP_LIST_REQ);
		}
		else if(arg[0].equals("RETR"))
		{
			ftp_req.setType(FTPRequestType.FTP_RETR_REQ);
			if(arg.length != 2)
				ftp_req.setValid(false);
		}
		else if(arg[0].equals("STOR"))
		{
			ftp_req.setType(FTPRequestType.FTP_STOR_REQ);
			if(arg.length != 2)
				ftp_req.setValid(false);
		}
		else if(arg[0].equals("QUIT"))
		{
			ftp_req.setType(FTPRequestType.FTP_QUIT_REQ);
			if(arg.length > 1)
				ftp_req.setValid(false);
		}
		else if(arg[0].equals("CDUP"))
		{
			ftp_req.setType(FTPRequestType.FTP_CDUP_REQ);
			if(arg.length > 1)
				ftp_req.setValid(false);
		}
		else if(arg[0].equals("CWD"))
		{
			ftp_req.setType(FTPRequestType.FTP_CWD_REQ);
			if(arg.length != 2)
				ftp_req.setValid(false);
		}
		else if(arg[0].equals("PWD"))
		{
			ftp_req.setType(FTPRequestType.FTP_PWD_REQ);
			if(arg.length > 1)
				ftp_req.setValid(false);
		}
		else if(arg[0].equals("TYPE"))
		{
			ftp_req.setType(FTPRequestType.FTP_TYPE_REQ);
			if(arg.length != 2)
				ftp_req.setValid(false);
		}
		else if(arg[0].equals("EPRT"))
		{
			ftp_req.setType(FTPRequestType.FTP_EPRT_REQ);
			if(arg.length != 2)
				ftp_req.setValid(false);
		}
		else if(arg[0].equals("MKD"))
		{
			ftp_req.setType(FTPRequestType.FTP_MKD_REQ);
		}
//		else if(arg[0].equals("EPSV"))
//		{
//			ftp_req.setType(FTPRequestType.FTP_EPSV_REQ);
//		}
		else if(arg[0].equals("PORT"))
		{
			ftp_req.setType(FTPRequestType.FTP_PORT_REQ);
		}
		else if(arg[0].equals("SYST"))
		{
			ftp_req.setType(FTPRequestType.FTP_SYST_REQ);
		}
		else if(arg[0].equals("NLST"))
		{
			ftp_req.setType(FTPRequestType.FTP_NLST_REQ);
		}
		
		else if(arg[0].equals("PASV"))
		{
			ftp_req.setType(FTPRequestType.FTP_PASV_REQ);
		}
		else
		{
			ftp_req.setValid(false);
		}
		
		return ftp_req;
	}
}
