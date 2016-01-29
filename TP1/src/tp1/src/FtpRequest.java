package tp1.src;

import java.util.Arrays;
import java.util.List;

public class FtpRequest extends Thread {
	
	/** The request to process. */
	private final String request;
	
	/** List of possible commands */
	private static final List<String> LIST_CMDS = Arrays.asList(
		Constants.CMD_USER,
		Constants.CMD_PASS,
		Constants.CMD_RETR,
		Constants.CMD_STOR,
		Constants.CMD_LIST,
		Constants.CMD_QUIT
	);
	
	/**
	 * Constructor with parameter.
	 * 
	 * @param request The request to process.
	 */
	public FtpRequest(final String request) {
		super();
		this.request = request;
	}
	
	/**
	 * Calls the {@link #processRequest(String)} method.
	 */
	@Override
	public void run() {
		processRequest(this.request);
	}

	/**
	 * Processes the received request by the server and calls the
	 * relevant method to treat it.
	 * 
	 * @param request The request received by the server.
	 */
	public void processRequest(final String request) {
		int ind = request.indexOf(" ");
		final String cmd = ind != -1
						 ? request.substring(0, ind)
						 : request;
		
		
		if (cmd == null) {
			// Invalid cmd
			processNoCmd();
		} else if (!LIST_CMDS.contains(cmd)) {
			// Unrecognised command
			processUnrecognisedCmd(cmd);
		} else {
			// Checks if a parameter is needed
			if (isParametrable(cmd)) {
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
				}
			}
		}
	}
	
	protected static boolean isParametrable(final String cmd) {
		// TODO: implement
		return false;
	}
	
	/**
	 * Processes when an invalid request was provided.
	 */
	protected void processNoCmd() {
		// TODO: implement
	}
	
	/**
	 * Processes an unrecognised request.
	 *
	 * @param cmd The unrecognised request.
	 */
	protected void processUnrecognisedCmd(final String cmd) {
		// TODO: implement
	}
	
	/**
	 * Processes a USER type request, used to provide an user name to login.
	 * 
	 * @param user The provided user name.
	 */
	protected void processUSER(final String user) {
		// TODO: implement
	}
	
	/**
	 * Processes a PASS type request, used to provide a password to login.
	 * 
	 * @param pass The provided password.
	 */
	protected void processPASS(final String pass) {
		// TODO: implement
	}
	
	/**
	 * Processes a RETR type request, used to retrieve a file from the server.
	 * 
	 * @param path The path to the file to retrieve.
	 */
	protected void processRETR(final String path) {
		// TODO: implement
	}
	
	/**
	 * Processes a STOR type request, used to upload a file to the server.
	 * 
	 * @param path The path to the file to store.
	 */
	protected void processSTOR(final String path) {
		// TODO: implement
	}
	
	/**
	 * Processes a LIST type request, used to list files in the working directory.
	 */
	protected void processLIST() {
		// TODO: implement
	}
	
	/**
	 * Processes a QUIT type request, used to quit from the server.
	 */
	protected void processQUIT() {
		// TODO: implement
	}
}
