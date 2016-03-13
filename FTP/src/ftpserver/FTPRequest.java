package ftpserver;

/**
 * Class representing a FTPRequest
 * @author Axel Antoine <ax.antoine@gmail.com> <axel.antoine@etudiant.univ-lille1.fr>
 *
 */
public class FTPRequest 
{	
	/**
	 * Type of the request
	 */
	private FTPRequestType type;

	/**
	 * Arg of the request
	 */
	private String arg;

	/**
	 * Tell is the request is valid or not
	 */
	private boolean isValid = true;

	/**
	 * Know if the request is valid
	 * @return true if valid, false otherwise
	 */
	public boolean isValid() {
		return isValid;
	}

	/**
	 * Set the validity of a request
	 * @param isValid, the validity of the request
	 */
	public void setValid(boolean isValid) {
		this.isValid = isValid;
	}

	/**
	 * Contruct a FTPRequest
	 */
	public FTPRequest()
	{

	}

	/**
	 * Get the first Argument of the Command
	 * @return
	 */
	public String getArg() {
		return arg;
	}


	/**
	 * Set the argument of the command
	 * @param arg
	 */
	public void setArg(String arg) {
		this.arg = arg;
	}

	/**
	 * Get the type of the command
	 * @return type
	 */
	public FTPRequestType getType() {
		return type;
	}

	/**
	 * Set the type of the command 
	 * @param type
	 */
	public void setType(FTPRequestType type) {
		this.type = type;
	}


}
