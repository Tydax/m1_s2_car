package ftpserver;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.file.Paths;
import java.util.Calendar;

/**
 * FTPClientHandler handles connection and dialogs with a client.
 * The client's commands are handled here.
 * 
 * Each User has its own directory with its name, these directories are ther root directory to them.
 * To control they don't reach over the root dir, each path has to contain their name.
 * The folder is automaticaly created.
 * 
 * Before use commands that need a data socket, the cmd EPRT is needed.
 * Argument has format : |2|ip|port|   
 * 2 is for iPv4
 * 
 * @author Axel Antoine <ax.antoine@gmail.com> <axel.antoine@etudiant.univ-lille1.fr>
 *
 */
public class FTPClientHandler extends Thread 
{
	/**
	 * Maximum length of data buffer 
	 */
	public final static int DATA_SIZE = 256;

	/**
	 * The client socket
	 */
	private Socket client_socket;

	/**
	 * FTPRequestParser which parses request sent to the server
	 */
	private FTPRequestParser ftp_request_parser;

	/**
	 * Boolean to know if the user is actually identified on the server
	 */
	private boolean authentified = false;

	/**
	 * User of the session opened
	 */
	private String user;

	/**
	 * User's password of the session opened
	 */
	private String pass;

	/**
	 * Instance of the main classe, the server
	 */
	private FTPServer server;

	/**
	 * User current dircetory while session;
	 */
	private String user_current_dir = "/";

	/**
	 * The type of encoding actually chosen to transfer data
	 * A for ASCII
	 * I for Binary
	 */
	private char type = 'A';

	/**
	 * The socket used to transfer data
	 */
	private Socket s_data_client;

	/**
	 * The Inet Adresse to use to open the client socket to transfer data
	 */
	private InetAddress addr_data_client;

	/**
	 * The port to use to open the client socket to transfer data
	 */
	private int port_data_client;

	/**
	 * The writer used to write on the command socket of the client
	 */
	private BufferedWriter client_writer;

	/**
	 * The reader used to read on the command socket of the client 
	 */
	private BufferedReader client_reader;

	/**
	 * The writer used to write on the data socket of the client
	 */
	private BufferedWriter client_data_writer;

	/**
	 * The reader used to read on the data socket of the client
	 */
	private BufferedReader client_data_reader;

	/**
	 * Create a Client Handler with the socket client and the instance of the server
	 * @param s, the socket client
	 * @param server, instance of the server
	 */
	public FTPClientHandler(Socket s, FTPServer server)
	{
		this.client_socket = s;
		this.server = server;
		this.ftp_request_parser = new FTPRequestParser(); // Singleton possible, but only one request will be handle at once

		try {
			this.client_writer = new BufferedWriter(new OutputStreamWriter(this.client_socket.getOutputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}

		try 
		{
			this.client_reader = new BufferedReader(new InputStreamReader(this.client_socket.getInputStream()));
		} catch (IOException e) 
		{
			e.printStackTrace();
		}

	}

	/**
	 * Start the handler to listening commands from the client socket
	 */
	public void run()
	{
		String string_req = "";
		FTPRequest ftp_req;

		// Welcome message
		this.writeClient("220 Service prêt pour un nouvel utilisateur.");

		do
		{
			// ReadLine from the socket
			try {
				string_req = this.client_reader.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}

			// If the request is not empty or null
			if(string_req != null && !string_req.equals(""))
			{
				// Transform the byte Request into a String request
				System.out.println("REQ:" + string_req);

				// Parse the string request to build an FTPRequest Object
				ftp_req = this.ftp_request_parser.parseStringFTPRequest(string_req);

				// Test if the request returned is valid
				if(!ftp_req.isValid())
				{
					this.writeClient("502 Unknown Request");
				}
				else
				{
					this.handleFtpRequest(ftp_req);
				}
			}
			else
			{
				try {
					this.client_socket.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		while(!this.client_socket.isClosed());
		System.out.println("Client disconnected");
	}

	/**
	 * Handle a request sent from the client. The function is a dispatch to other function in function of the type of request sent.
	 * @param req, the req to handle
	 */
	public void handleFtpRequest(FTPRequest req)
	{
		// Control the authentification before any actions different from USER and PASS
		if(req.getType() != FTPRequestType.FTP_USER_REQ && req.getType() != FTPRequestType.FTP_PASS_REQ && !this.authentified)
		{
			this.writeClient("332 Need authentification.");
			return;
		}

		switch(req.getType())
		{
		case FTP_LIST_REQ :
			this.processLIST(req);
			break;
		case FTP_USER_REQ :
			this.processUSER(req);
			break;
		case FTP_PASS_REQ :
			this.processPASS(req);
			break;
		case FTP_STOR_REQ :
			this.processSTOR(req);
			break;
		case FTP_RETR_REQ :
			this.processRETR(req);
			break;
		case FTP_QUIT_REQ :
			this.processQUIT(req);
			break;
		case FTP_PWD_REQ :
			this.processPWD(req);
			break;
		case FTP_CWD_REQ :
			this.processCWD(req);
			break;
		case FTP_CDUP_REQ :
			this.processCDUP(req);
			break;
		case FTP_TYPE_REQ :
			this.processTYPE(req);
			break;
		case FTP_EPRT_REQ :
			this.processEPRT(req);
			break;
		case FTP_MKD_REQ :
			this.processMKD(req);
			break;
		case FTP_SYST_REQ :
			this.processSYST(req);
			break;
		case FTP_PORT_REQ :
			this.processPORT(req);
			break;
		case FTP_PASV_REQ :
			this.processPASV(req);
			break;
		case FTP_DELE_REQ :
			this.processDELE(req);
			break;
		}
	}

	/**
	 * Write the String in parameter to the command socket of the client
	 * @param s
	 */
	public void writeClient(String s)
	{
		System.out.println("REP:"+s); // Debugging
		try {
			this.client_writer.write(s+"\r\n");
			this.client_writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Handle the USER Request
	 * @param req, the request to handle with paramaters
	 */
	public void processUSER(FTPRequest req)
	{
		this.user = req.getArg();
		if(this.server.userExists(this.user))
			this.writeClient("331 Identifiant reconnu. En attente du mot de passe.");
		else
			this.writeClient("430 Identifiant inconnu.");
	}

	/**
	 * Handle the PASS request
	 * @param req, the request to handle with paramaters
	 */
	public void processPASS(FTPRequest req)
	{
		this.pass = req.getArg();
		this.authentified = this.server.checkUserPass(this.user, this.pass);
		if(this.authentified)
		{
			this.writeClient("230 Authentification réussie.");
			//this.writeClient("220 Bienvenue.");

			File f = new File(this.server.getServerDir() + this.user);
			if(!f.exists())
				f.mkdirs();
		}
		else
		{
			this.writeClient("430 Identifiant ou mot de passe incorrect.");
		}

	}

	/**
	 * Handle the RETR Request
	 * @param req, the request to handle with paramaters
	 */
	public void processRETR(FTPRequest req)
	{
		File f = new File(this.server.getServerDir() + this.user + this.user_current_dir + "/" + req.getArg());

		System.out.println("Check :" + f.getAbsolutePath());
		if(f.exists())
		{					
			String new_current_dir = Paths.get(f.getAbsolutePath()).normalize().toString();

			if(new_current_dir.indexOf(this.user) == -1)
			{
				this.writeClient("??? You can't go forward your user directory.");
			}

			// Read the file from the socket
			this.connect_data_client_socket();

			// Create a reader from the file
			BufferedReader file_reader = null;
			try 
			{
				file_reader = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
			} catch (FileNotFoundException e1) {
				e1.printStackTrace();
			}

			String line;
			char[] buffer = new char[256];
			int nb = 0;
			try 
			{
				// read line from file
				while((nb=file_reader.read(buffer, 0, 256)) != -1 )
				{
					// write line in the data socket
					this.client_data_writer.write(buffer, 0, nb);
				}
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
			}

			//Close data socket and file reader
			this.close_data_client_socket();

			try {
				file_reader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			this.writeClient("250 ok.");

		}
		else
		{
			this.writeClient("550 No such file." + req.getArg());
		}	
	}

	/**
	 * Handle the STOR request
	 * @param req, the request to handle with paramaters
	 */
	public void processSTOR(FTPRequest req)
	{
		File f = new File(this.server.getServerDir() + this.user + this.user_current_dir+ "/"+ req.getArg());

		System.out.println("STOR :" + f.getAbsolutePath());

		try {
			f.createNewFile();
		} catch (IOException e2) {
			e2.printStackTrace();
		}

		// Connect data socket with args from EPRT cmd
		this.connect_data_client_socket();

		// Create a writer to the file
		BufferedWriter file_writer = null;
		try 
		{
			file_writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(f)));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}


		String line;
		try 
		{
			// Read lines from data socket
			while((line = this.client_data_reader.readLine()) != null)
			{
				// Write line in the file
				file_writer.write(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		//Close the data socket and the fileWriter
		this.close_data_client_socket();
		try {
			file_writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}

		this.writeClient("250 ok.");
	}

	public void processMKD(FTPRequest req)
	{
		File f = new File(this.server.getServerDir() + this.user + "/"+ req.getArg());
		if(!f.exists())
			f.mkdirs();
		this.writeClient("257 "+req.getArg()+" created.");
	}

	/**
	 * Handle the LIST request
	 * 
	 * EPRT cmd is needed.
	 * 
	 * @param req, the request to handle with paramaters
	 */
	public void processLIST(FTPRequest req)
	{		
		String arg = req.getArg(); 
		File file;
		if(arg != null && arg != "")
			file = new File(this.server.getServerDir()+this.user+this.user_current_dir + arg);
		else
			file = new File(this.server.getServerDir()+this.user+this.user_current_dir);

		File[] files  = file.listFiles();
		
		
		if (files != null) 
		{
			// Connect data socket with args from EPRT cmd
			this.connect_data_client_socket();
			this.writeClient("150 Directory listing.");

			String name = "";
			long size = 0;
			String type;
			Calendar calendar = null;


			for (int i = 0; i < files.length; i++) 
			{
				if (files[i].isDirectory()) 
				{
					type = "d";
				}
				else
				{
					type = "-";
				}

				name = files[i].getName();
				size = files[i].length();
				long l = files[i].lastModified();
				calendar = Calendar.getInstance();
				calendar.setTimeInMillis(l);

				String[] dateSplited = calendar.getTime().toString().split(" ");

				String time = dateSplited[1] + " " + dateSplited[2] + " " + dateSplited[3].split(":")[0] + ":" + dateSplited[3].split(":")[1]; 

				this.write_data_client(type+"rwxrwxrwx 1 durey durey " + size + " " + time + " " + name);  
			}
			// Close the data socket
			this.close_data_client_socket();

			this.writeClient("226 Directory transmitted.");
		}
		else
		{
			this.writeClient("??? Error loading current directory.");
		}
	}

	/**
	 * Handle the QUIT request
	 * @param req, the request to handle with paramaters
	 */
	public void processQUIT(FTPRequest req)
	{
		try 
		{
			this.client_socket.close();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		this.authentified = false;
		this.user_current_dir = "/";
	}

	/**
	 * Handle the PWD request
	 * @param req, the request to handle with paramaters
	 */
	public void processPWD(FTPRequest req)
	{
		this.writeClient("257 \""+this.user_current_dir + "\" is current directory.");
	}

	/**
	 * Handle the CWD request
	 * @param req, the request to handle with paramaters
	 */
	public void processCWD(FTPRequest req)
	{
		File f = new File(this.server.getServerDir() + this.user + this.user_current_dir + "/" + req.getArg());

		System.out.println("Check :" + f.getAbsolutePath());
		if(f.exists() && f.isDirectory())
		{					
			String new_current_dir = Paths.get(f.getAbsolutePath()).normalize().toString();

			// All path have to contain the user name
			if(new_current_dir.indexOf(this.user) == -1)
			{
				this.writeClient("??? You can't go forward your user directory.");
			}			

			this.user_current_dir = new_current_dir.replace(this.server.getServerDir() + this.user, "") + "/";	

			this.writeClient("250 ok.");

		}
		else
		{
			this.writeClient("550 No such directory \"" + req.getArg()+"\".");
		}
	}

	/**
	 * Handle the CDUP request
	 * @param req, the request to handle with paramaters
	 */
	public void processCDUP(FTPRequest req)
	{
		String s;
		if(req.getArg() == null)
			s = "";
		else
			s = req.getArg();


		File f = new File(this.server.getServerDir() + this.user + "/" + s);

		System.out.println("Check :" + f.getAbsolutePath());
		if(f.exists())
		{					
			String new_current_dir = Paths.get(f.getAbsolutePath()).normalize().toString();

			if(new_current_dir.indexOf(this.user) == -1)
			{
				this.writeClient("??? You can't go forward your user directory.");
			}

			this.user_current_dir = new_current_dir.replace(this.server.getServerDir() + this.user, "") + "/";	

			this.writeClient("250 ok.");

		}
		else
		{
			this.writeClient("550 No such directory." + s);
		}
	}

	/**
	 * Handle the TYPE request
	 * @param req, the request to handle with paramaters
	 */
	public void processTYPE(FTPRequest req)
	{
		this.type = req.getArg().charAt(0);
		this.writeClient("200 Type set to "+ this.type+".");
	}
	
	public void processDELE(FTPRequest req)
	{
		File file = new File(this.server.getServerDir() + this.user + "/" + req.getArg());
		System.out.println(file.getAbsolutePath());
		System.out.println(file.exists());
		file.delete();
		
		this.writeClient("250 File system exited successfully");

	}

	/**
	 * Handle the EPRT request
	 * @param req, the request to handle with paramaters
	 */
	public void processEPRT(FTPRequest req)
	{
		String arg = "";


		// Clean the string
		char c;
		for(int i=0; i< req.getArg().length(); i++)
		{
			c = req.getArg().charAt(i);
			if(c == 124)
				c = ' ';
			arg += c;
		}

		// Split

		if(arg.charAt(0) == ' ') arg = arg.substring(1);
		if(arg.charAt(arg.length()-1) == ' ') arg = arg.substring(0, arg.length()-1);

		String[] tab = arg.split(" ");

		// 3 args : type address port

		if(tab.length != 3) 
		{
			this.writeClient("502 Unknow Request.");
			return;
		}

		// Get the address

		try {
			this.addr_data_client = Inet4Address.getByName(tab[1]);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		System.out.println("Data addr :"+this.addr_data_client.getHostAddress());


		// Get the port
		this.port_data_client = Integer.parseInt(tab[2]);

		System.out.println("Port ="+this.port_data_client);

		this.writeClient("200 Done.");
	}

	/**
	 * Connect the server to the client (Need EPRT before use)
	 */
	public void connect_data_client_socket()
	{
		// Connect to the client data port
		try 
		{
			this.s_data_client = new Socket(this.addr_data_client,this.port_data_client);
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		try 
		{
			this.client_data_writer = new BufferedWriter(new OutputStreamWriter(this.s_data_client.getOutputStream()));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}

		try 
		{
			this.client_data_reader = new BufferedReader(new InputStreamReader(this.s_data_client.getInputStream()));
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}

	/**
	 * Close the data client socket
	 */
	public void close_data_client_socket()
	{
		try {
			this.s_data_client.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Write the string s on the data socket
	 * @param s, the string to write
	 */
	public void write_data_client(String s)
	{
		System.out.println("-> :"+s);
		try 
		{
			this.client_data_writer.write(s);
			this.client_data_writer.newLine();
			this.client_data_writer.flush();
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	public void processSYST(FTPRequest req)
	{
		this.writeClient("215 UNIX");
	}

	public void processPASV(FTPRequest req)
	{
		this.writeClient("227 Passive mode");
	}

	public void processPORT(FTPRequest req)
	{
		String arg = "";

		// Split

		arg = req.getArg();

		String[] tab = arg.split(",");

		if(tab.length != 6) 
		{
			this.writeClient("502 Unknow Request.");
			return;
		}

		// Get the address

		try 
		{
			this.addr_data_client = Inet4Address.getByName(tab[0]+"."+tab[1]+"."+tab[2]+"."+tab[3]);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}

		System.out.println("Data addr :"+this.addr_data_client.getHostAddress());


		// Get the port
		this.port_data_client = Integer.parseInt(tab[4])*256+Integer.parseInt(tab[5]);

		System.out.println("Port ="+this.port_data_client);

		this.writeClient("200 Done.");
	}

}


