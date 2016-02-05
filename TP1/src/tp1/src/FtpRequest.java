package tp1.src;

import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class FtpRequest extends Thread {

    /** The socket which represents the connexion with the client */
    private final Socket socket;

    /** The input reader associated with the socket. */
    private final BufferedReader input;

    /** The output writer associated with the socket. */
    private final PrintWriter output;

    /** The folder path of the server */
    private final File workingDirectory;

    /** The username */
    private String username;

    /** <code>true</code> if the username is authenticated, <code>false</code> else. */
    private boolean authenticated;

    /** List of possible commands */
    private static final List<String> LIST_CMDS = Arrays.asList(
            Constants.CMD_USER,
            Constants.CMD_PASS,
            Constants.CMD_RETR,
            Constants.CMD_STOR,
            Constants.CMD_LIST,
            Constants.CMD_QUIT,
            Constants.CMD_SYST
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
                    processUnrecognisedCmd(cmd);
                } else {
                    // Check if a parameter is needed
                    if (isParametrable(cmd)) {
                        // Check if a parameter is provided
                        if (ind == -1) {
                            // No parameter, return error
                            processNoParamCmd(cmd);
                        } else {
                            final String param = request.substring(ind + 1);
                            System.out.println("Commande : " + cmd);
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

                        default:
                            processUnrecognisedCmd(cmd);
                            break;
                        }
                    }
                }
            }
        }

        socket.close();
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
            return false;
        default:
            return true;
        }
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
     * @param cmd
     *            The unrecognised request.
     */
    protected void processUnrecognisedCmd(final String cmd) {
        String msg = String.format(Constants.MSG_INVALID_CMD, Constants.CODE_INVALID_CMD);
        this.output.println(msg);
        this.output.flush();
        logOutput(msg);
    }

    /**
     * Processes a command that needs a parameter but was not provided one
     *
     * @param cmd
     *            The command
     */
    protected void processNoParamCmd(final String cmd) {
        // TODO: implement
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

        this.output.println(msg);
        this.output.flush();
        logOutput(msg);
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
    protected void processRETR(final String path) throws IOException {
        if (!checkLoggedIn()) {
            return;
        }

        final File file = new File (path);
        final byte [] byteArray  = new byte [(int)file.length()];

        BufferedInputStream buffFile = new BufferedInputStream(new FileInputStream(file));
        buffFile.read(byteArray, 0, byteArray.length);
        final OutputStream os = socket.getOutputStream();
        System.out.println("Sending " + path + "(" + byteArray.length + " bytes)");
        os.write(byteArray, 0, byteArray.length);
        os.flush();
        System.out.println("Done.");

        final String msg = String.format(Constants.CMD_RETR, Constants.CODE_TRANSFER_SUCC);
        logOutput(msg);
    }

    /**
     * Processes a STOR type request, used to upload a file to the server.
     *
     * @param path
     *            The path to the file to store.
     */
    protected void processSTOR(final String path) throws IOException {
        if (!checkLoggedIn()) {
            return;
        }

        int bytesRead;

        System.out.println("Connecting...");

        // receive file
        File file = new File(path);
        byte [] mybytearray  = new byte [(int)file.length()];
        InputStream is = socket.getInputStream();
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
        bytesRead = is.read(mybytearray,0,mybytearray.length);
        int current = bytesRead;

        do {
            bytesRead =
                    is.read(mybytearray, current, (mybytearray.length-current));
            if(bytesRead >= 0) current += bytesRead;
        } while(bytesRead > -1);

        bos.write(mybytearray, 0 , current);
        bos.flush();

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

        for (final File file : files) {
            if (file.isDirectory()) {
                builder.append("(D) ");
            }
            builder.append(file.getName());
            builder.append("\n");
        }

        processRequestBase(null, builder.toString());
    }

    /**
     * Processes a QUIT type request, used to quit from the server.
     */
    protected void processQUIT() {
        processRequestBase(Constants.CODE_DISCONNECTION, Constants.MSG_QUIT);
    }

    protected void processCWD(final String folderPath) throws IOException {
        String tmpPath = workingDirectory.getPath() + "/" + folderPath;
        /*  if(!Files.exists(tmpPath)){

	    }*/

        // processRequestBase(Constants.CODE_SYST_INFO, Constants.MSG_SYST_INFO);
    }
}

