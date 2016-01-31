package tp1.src;

public class Constants {

	// Commands
	public static final String CMD_USER = "USER";
	public static final String CMD_PASS = "PASS";
	public static final String CMD_RETR = "RETR";
	public static final String CMD_STOR = "STOR";
	public static final String CMD_LIST = "LIST";
	public static final String CMD_QUIT = "QUIT";

	// Return codes
	public static final int CODE_CONNECTION_SUCC = 220;
	public static final int CODE_TRANSFER_SUCC = 226;
	public static final int CODE_DISCONNECTION = 221;
	public static final int CODE_AUTH_SUCC = 230;
	public static final int CODE_WAITING_PASS = 331;
	public static final int CODE_AUTH_FAILED = 430;
	public static final int CODE_INVALID_CMD = 500;
	public static final int CODE_INVALID_PARAM = 501;

	// Messages
    public static final String MSG_CONNECTION_SUCC = "%d Successfully connected to server\n";
	public static final String MSG_QUIT = "%d Goodbye!\n";
	public static final String MSG_WAITING_PASS = "%d Waiting for user password...\n";
	public static final String MSG_AUTH_FAILED = "%d Authentication failed\n";
    public static final String MSG_AUTH_NOLOGIN = "%d No username provided\n";
	public static final String MSG_AUTH_SUCC = "%d Authentication successful\n";
}
