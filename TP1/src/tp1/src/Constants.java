package tp1.src;

public class Constants {

    // Commands
    public static final String CMD_USER = "USER";
    public static final String CMD_PASS = "PASS";
    public static final String CMD_RETR = "RETR";
    public static final String CMD_STOR = "STOR";
    public static final String CMD_LIST = "LIST";
    public static final String CMD_QUIT = "QUIT";
    public static final String CMD_SYST = "SYST";
    public static final String CMD_PWD = "PWD";
    public static final String CMD_CWD = "CWD";
    public static final String CMD_CDUP = "CDUP";
    public static final String CMD_PORT = "PORT";
    

    // Return codes
    public static final int CODE_BEGIN_TRANSFERT = 125;
    public static final int CODE_SERVICE_OK = 200;
    public static final int CODE_SYST_INFO = 215;
    public static final int CODE_CONNECTION_SUCC = 220;
    public static final int CODE_TRANSFER_SUCC = 226;
    public static final int CODE_DISCONNECTION = 221;
    public static final int CODE_AUTH_SUCC = 230;
    public static final int CODE_FILEOP_COMPLETED = 250;
    public static final int CODE_WAITING_PASS = 331;
    public static final int CODE_TRANS_IMPO = 425;
    public static final int CODE_AUTH_FAILED = 430;
    public static final int CODE_INVALID_CMD = 500;
    public static final int CODE_INVALID_PARAM = 501;
    public static final int CODE_NOT_LOGGED = 530;
    public static final int CODE_REQUEST_NO_EXECUTED = 530;

    // Messages

    public static final String MSG_SYST_INFO = "%d Java Server";
    public static final String MSG_CONNECTION_SUCC = "%d Successfully connected to server";
    public static final String MSG_BASE = "%d ";
    public static final String MSG_QUIT = "%d Goodbye! Stay safe out there!";
    public static final String MSG_WAITING_PASS = "%d Waiting for user password...";
    public static final String MSG_AUTH_FAILED = "%d Authentication failed";
    public static final String MSG_AUTH_NOLOGIN = "%d No username provided";
    public static final String MSG_AUTH_SUCC = "%d Authentication successful";
    public static final String MSG_INVALID_CMD = "%d Command unrecognised";
    public static final String MSG_NOT_LOGGED = "%d Not logged in";
    public static final String MSG_NO_PARAM = "%d Parameter is required, none is provided";
    public static final String MSG_NO_SUCH_FOLDER = "%d Folder does not exist";
    public static final String MSG_TRANSFER_STARTED = "%d Transfer started";
    public static final String MSG_TRANSFER_SUCCESSFUL = "%d Transfer successfully completed";
    public static final String MSG_TRANSFER_UNCOMPLETED = "%d Unable to complete the transfer";
    public static final String MSG_OPERATION_FORBIDDEN = "%d Unable to move through the asked directory. Forbidden";



    // Format
    public static final String FORM_FILE_LIST = "%s    1 %-10s %-10s %10lu %s %s\r\n";
}
