package tp1.src;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

public class FtpRequest extends Thread {

	/** The socket which represents the connexion with the client */
	private final Socket socket;

	/** The socket which is used to send and store files from to client */
	private Socket socketData;

	/** The input reader associated with the socket. */
	private final BufferedReader input;

	/** The output writer associated with the socket. */
	private final PrintWriter output;

	/** The folder path of the server */
	private File workingDirectory;

	/** The username */
	private String username;

	/** True if user want to quit, false otherwise */
	private boolean wantToQuit;

	/** <code>true</code> if the username is authenticated, <code>false</code> else. */
	private boolean authenticated;

	/** The port on which we will send the files */
	final private int port = 4096;

	/** List of possible commands */
	private static final List<String> LIST_CMDS = Arrays.asList(
			Constants.CMD_USER,
			Constants.CMD_PASS,
			Constants.CMD_RETR,
			Constants.CMD_STOR,
			Constants.CMD_LIST,
			Constants.CMD_QUIT,
			Constants.CMD_SYST,
			Constants.CMD_PWD,
			Constants.CMD_CWD,
			Constants.CMD_CDUP
			);

	/**
	 * Creates a new FtpRequest handling a client by listening to the input received.
	 * @param socket The socket used to listen.
	 * @param workingDirectoryPath The path to working directory.
	 * @throws IOException
	 */
	public FtpRequest(final Socket socket, final String workingDirectoryPath) throws IOException {
		super();
		this.socket = socket;
		this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		this.output = new PrintWriter(socket.getOutputStream(),
				true);
		this.workingDirectory = new File(workingDirectoryPath);
		this.authenticated = false;
		this.wantToQuit = false;
	}

	/**
	 * Calls the {@link #processRequest()} method.
	 */
	@Override
	public void run() {
		try {
			processRequest();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Processes the received request by the server and calls the relevant
	 * method to treat it.
	 * 
	 * @throws IOException
	 */
	public void processRequest() throws IOException {
		String request;
		boolean keepOpen = true;
		while (keepOpen) {
			request = this.input.readLine();
			int ind = request.indexOf(" ");
			final String cmd = ind != -1 ? request.substring(0, ind) : request;

			System.out.println("\"" + cmd + "\"");

			if (cmd == null) {
				// Invalid cmd
				processNoCmd();
			} else {
				System.out.println(String.format("[Server] Command %s received", cmd));
				if (!LIST_CMDS.contains(cmd)) {
					// Unrecognised command
					processUnrecognisedCmd();
				} else {
					// Check if a parameter is needed
					if (isParametrable(cmd)) {
						// Check if a parameter is provided
						if (ind == -1) {
							// No parameter, return error
							processNoParamCmd();
						} else {
							final String param = request.substring(ind + 1);
							switch (cmd) {
							case Constants.CMD_USER:
								processUSER(param);
								break;

							case Constants.CMD_PASS:
								processPASS(param);
								break;

							case Constants.CMD_STOR:
								processSTOR(param);
								break;

							case Constants.CMD_RETR:
								processRETR(param);
								break;

							case Constants.CMD_CWD:
								processCWD(param);
								break;
							case Constants.CMD_PORT:
								processPORT(param);

							default:
								processUnrecognisedCmd();
								break;
							}
						}
					} else {
						// No parameter is needed
						switch (cmd) {
						case Constants.CMD_LIST:
							processLIST();
							break;

						case Constants.CMD_QUIT:
							processQUIT();
							keepOpen = false;
							break;

						case Constants.CMD_SYST:
							processSYST();
							break;

						case Constants.CMD_PWD:
							processPWD();
							break;

						case Constants.CMD_CDUP:
							processCDUP();
							break;

						default:
							processUnrecognisedCmd();
							break;
						}
					}
				}
			}
		}

		socket.close();
	}

	/**
	 * Logs a sent message under the following format: "[Server] Sent message '%s'"
	 * @param msg The sent message.
	 */
	protected static void logOutput(final String msg) {
		final String display = String.format("[Server] Sent message '%s'", msg);
		System.out.println(display);
	}

	/**
	 * Checks if a command needs a parameter or not.
	 *
	 * @param cmd
	 *            The command to check.
	 * @return <code>true</code> if the command needs a parameter; <br>
	 *         <code>false</code> else.
	 */
	protected static boolean isParametrable(final String cmd) {
		switch (cmd) {
		case Constants.CMD_LIST:
		case Constants.CMD_QUIT:
		case Constants.CMD_PWD:
			return false;
		default:
			return true;
		}
	}

	/**
	 * Checks if the user is logged in. Sent an error message if he is not.
	 * @return <code>true</code> if the user is logged in; <br>
	 *         <code>false</code> else.
	 */
	protected boolean checkLoggedIn() {
		if (!authenticated) {
			processRequestBase(Constants.CODE_NOT_LOGGED, Constants.MSG_NOT_LOGGED);
		}

		return authenticated;
	}


	/**
	 * Send a message to the data port of the client.
	 * @param msg message send to the client.
	 */
	private void sendData(String msg) {
		processRequestBase(Constants.CODE_BEGIN_TRANSFERT, "%d");
		try {
			OutputStreamWriter outputWriterData = new OutputStreamWriter(socketData.getOutputStream());
			outputWriterData.write(msg);
			outputWriterData.flush();
			socketData.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		processRequestBase(Constants.CODE_TRANSFER_SUCC, Constants.MSG_TRANSFERT_SUCCESSFULL);
	}


	private String receiveData() {
		processRequestBase(Constants.CODE_BEGIN_TRANSFERT, Constants.MSG_TRANSFERT_STARTED);
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(socketData.getInputStream()));
			String res = "", tmp;
			while ((tmp = br.readLine()) != null) {
				res += tmp + "\n";
			}
			processRequestBase(Constants.CODE_TRANSFER_SUCC, Constants.MSG_TRANSFERT_SUCCESSFULL);
			return res;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}


	/**
	 * Base method to send a generated message with a code and a message.
	 * @param code The code to send, may be null.
	 * @param msg The String message to send (must contain a '%d ' to be formatted).
	 */
	protected void processRequestBase(final Integer code, final String msg) {
		final String finalMsg = code != null
				? String.format(msg, code)
						: msg.substring(3);
				this.output.println(finalMsg);
				this.output.flush();

				final String logMsg = String.format("[Server] Sent message '%s'", finalMsg);
				logOutput(logMsg);
	}

	/**
	 * Treats case where an invalid request was provided.
	 */
	protected void processNoCmd() {
		// TODO: implement
	}

	/**
	 * Processes an unrecognised request.
	 *
	 */
	protected void processUnrecognisedCmd() {
		String msg = String.format(Constants.MSG_INVALID_CMD, Constants.CODE_INVALID_CMD);
		this.output.println(msg);
		this.output.flush();
		logOutput(msg);
	}

	/** 
	 * Processes the PORT command. Used to get the ports on which to send and receive the data
	 */
	private void processPORT(String cmd) {
		String[] tmpPort = cmd.split(",");
		String tmp = "";
		int port = Integer.parseInt(tmpPort[4]) * 256
				+ Integer.parseInt(tmpPort[5]);
		for (int i = 0; i < 4; i++)
			tmp += tmpPort[i] + ".";
		tmp = tmp.substring(0, tmp.length() - 1);
		System.out.println("addr : \'" + tmp + "\', port : \'" + port + "\'");
		try {
			socketData = new Socket(tmp, port);
			processRequestBase(Constants.CODE_SERVICE_OK, "%d");
		} catch (Exception e) {
			processRequestBase(Constants.CODE_INVALID_PARAM, "%d");
		}

	}

	/**
	 * Processes a command that needs a parameter but was not provided one
	 *
	 */
	protected void processNoParamCmd() {
		processRequestBase(Constants.CODE_INVALID_PARAM, Constants.MSG_NO_PARAM);
	}

	/**
	 * Processes a USER type request, used to provide an username to login.
	 *
	 * @param user
	 *            The provided username name.
	 * @throws IOException
	 */
	protected void processUSER(final String user) throws IOException {
		final String msg;
		final int code;

		if (Serveur.users.containsKey(user)) {
			this.username = user;
			code = Constants.CODE_WAITING_PASS;
			msg = Constants.MSG_WAITING_PASS;
		} else {
			code = Constants.CODE_AUTH_FAILED;
			msg = Constants.MSG_AUTH_FAILED;
		}

		processRequestBase(code, msg);
	}

	/**
	 * Processes a PASS type request, used to provide a password to login.
	 *
	 * @param pass
	 *            The provided password.
	 * @throws IOException
	 */
	protected void processPASS(final String pass) throws IOException {
		if (this.username != null) {
			if (Serveur.users.get(username).equals(pass)) {
				processRequestBase(Constants.CODE_AUTH_SUCC, Constants.MSG_AUTH_SUCC);
				authenticated = true;
			} else {
				processRequestBase(Constants.CODE_AUTH_FAILED, Constants.MSG_AUTH_FAILED);
			}
		} else {
			// Username not provided
			processRequestBase(Constants.CODE_AUTH_FAILED, Constants.MSG_AUTH_NOLOGIN);
		}
	}

	protected void processSYST() throws IOException {
		processRequestBase(Constants.CODE_SYST_INFO, Constants.MSG_SYST_INFO);
	}


	/**
	 * Processes a RETR type request, used to retrieve a file from the server.
	 *
	 * @param path
	 *            The path to the file to retrieve.
	 * @throws IOException 
	 */
	protected void processRETR(final String filename) throws IOException {
		if (!checkLoggedIn()) {
			return;
		}

		try {
			InputStream flux = new FileInputStream(workingDirectory.getPath() + "/" + filename);
			InputStreamReader lecture = new InputStreamReader(flux);
			BufferedReader buff = new BufferedReader(lecture);
			String tmp = "", line;
			while ((line = buff.readLine()) != null) {
				tmp += line + "\n";
			}
			buff.close();
			sendData(tmp);
		} catch (Exception e) {

		}
		final String msg = String.format(Constants.CMD_RETR, Constants.CODE_TRANSFER_SUCC);
		logOutput(msg);

	}

	/**
	 * Processes a STOR type request, used to upload a file to the server.
	 *
	 * @param path
	 *            The path to the file to store.
	 */
	protected void processSTOR(final String filename) throws IOException {
		if (!checkLoggedIn()) {
			return;
		}

		File fichier = new File(workingDirectory.getPath() + "/" + filename);
		String data = receiveData();
		if (data == null)
			return;
		try {
			fichier.createNewFile();
			FileOutputStream output = new FileOutputStream(fichier);
			output.write(data.getBytes());
			output.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		String msg = String.format(Constants.CMD_STOR, Constants.CODE_TRANSFER_SUCC);
		logOutput(msg);
	}

	/**
	 * Processes a LIST type request, used to list files in the working
	 * directory.
	 */
	protected void processLIST() {
		if (!checkLoggedIn()) {
			return;
		}

		final StringBuilder builder = new StringBuilder();
		final File[] files = this.workingDirectory.listFiles();
		//this.workingDirectory.listFiles()

		if (files != null) {
			for (final File file : files) {
				if (file.isDirectory()) {
					builder.append("(D) ");
				}
				builder.append(file.getName());
				builder.append("\n");
			}
		}

		processRequestBase(Constants.CODE_FILEOP_COMPLETED, builder.toString());
	}

	/**
	 * Processes a QUIT type request, used to quit from the server.
	 */
	protected void processQUIT() {
		processRequestBase(Constants.CODE_DISCONNECTION, Constants.MSG_QUIT);
	}

	/**
	 * Processes a PWD type request, used to display the working directory.
	 */
	protected void processPWD() {
		if (!checkLoggedIn()) {
			return;
		}

		final StringBuilder msg = new StringBuilder(Constants.MSG_BASE);
		msg.append(this.workingDirectory.getAbsolutePath());
		processRequestBase(Constants.CODE_FILEOP_COMPLETED, msg.toString());
	}

	protected void processCWD(final String folderPath) throws IOException {
		Path tmpPath = Paths.get(workingDirectory.getPath() + "/" + folderPath);

		if(Files.exists(tmpPath, LinkOption.NOFOLLOW_LINKS)){
			workingDirectory = new File(tmpPath.toString());
			processRequestBase(Constants.CODE_FILEOP_COMPLETED, "%d " + workingDirectory.getAbsolutePath());

		} else {
			processRequestBase(Constants.CODE_REQUEST_NO_EXECUTED, Constants.MSG_NO_SUCH_FOLDER);
		}
	}

	protected void processCDUP() throws IOException {
		processCWD("..");
	}
}

