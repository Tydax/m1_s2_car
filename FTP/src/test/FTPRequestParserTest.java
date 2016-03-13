package test;

import static org.junit.Assert.*;

import org.junit.Test;

import ftpserver.FTPRequest;
import ftpserver.FTPRequestParser;
import ftpserver.FTPRequestType;

public class FTPRequestParserTest 
{
	@Test

	public void testParseStringFTPRequest()
	{
		FTPRequestParser parser = new FTPRequestParser();
		FTPRequest req;

		// Test cmd USER :
		req = parser.parseStringFTPRequest("USER arg");
		assertTrue(req.isValid());
		assertTrue(req.getType() == FTPRequestType.FTP_USER_REQ);
		assertTrue(req.getArg().equals("arg"));

		req = parser.parseStringFTPRequest("USER");
		assertFalse(req.isValid());

		// Test cmd PASS :
		req = parser.parseStringFTPRequest("PASS arg");
		assertTrue(req.isValid());
		assertTrue(req.getType() == FTPRequestType.FTP_PASS_REQ);
		assertTrue(req.getArg().equals("arg"));

		req = parser.parseStringFTPRequest("PASS");
		assertFalse(req.isValid());

		// Test cmd EPRT :
		req = parser.parseStringFTPRequest("EPRT arg");
		assertTrue(req.isValid());
		assertTrue(req.getType() == FTPRequestType.FTP_EPRT_REQ);
		assertTrue(req.getArg().equals("arg"));

		req = parser.parseStringFTPRequest("EPRT");
		assertFalse(req.isValid());

		// Test cmd STOR :
		req = parser.parseStringFTPRequest("STOR arg");
		assertTrue(req.isValid());
		assertTrue(req.getType() == FTPRequestType.FTP_STOR_REQ);
		assertTrue(req.getArg().equals("arg"));

		req = parser.parseStringFTPRequest("STOR");
		assertFalse(req.isValid());

		// Test cmd RETR :
		req = parser.parseStringFTPRequest("RETR arg");
		assertTrue(req.isValid());
		assertTrue(req.getType() == FTPRequestType.FTP_RETR_REQ);
		assertTrue(req.getArg().equals("arg"));

		req = parser.parseStringFTPRequest("RETR");
		assertFalse(req.isValid());

		// Test cmd QUIT :
		req = parser.parseStringFTPRequest("QUIT");
		assertTrue(req.isValid());
		assertTrue(req.getType() == FTPRequestType.FTP_QUIT_REQ);

		req = parser.parseStringFTPRequest("QUIT arg");
		assertFalse(req.isValid());

		// Test cmd  CDUP:
		req = parser.parseStringFTPRequest("CDUP");
		assertTrue(req.isValid());
		assertTrue(req.getType() == FTPRequestType.FTP_CDUP_REQ);

		req = parser.parseStringFTPRequest("CDUP arg");
		assertFalse(req.isValid());

		// Test cmd PWD :
		req = parser.parseStringFTPRequest("PWD");
		assertTrue(req.isValid());
		assertTrue(req.getType() == FTPRequestType.FTP_PWD_REQ);

		req = parser.parseStringFTPRequest("PWD arg");
		assertFalse(req.isValid());


	}

}
