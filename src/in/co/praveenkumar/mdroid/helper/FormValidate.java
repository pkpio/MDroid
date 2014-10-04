package in.co.praveenkumar.mdroid.helper;

import org.apache.commons.validator.routines.UrlValidator;

public class FormValidate {
	String username;
	String password;
	String token;
	String url;

	/**
	 * Check field validity for login fields
	 * 
	 * @param username
	 * @param password
	 * @param url
	 * @return validity true if valid. false, otherwise.
	 */
	public Boolean valid(String username, String password, String url) {
		this.username = username;
		this.password = password;
		this.url = url;
		if (getUsernameError(username) != null
				|| getPasswordError(password) != null
				|| getUrlError(url) != null)
			return false;
		return true;
	}

	/**
	 * Check field validity for login fields
	 * 
	 * @param token
	 * @param url
	 * @return validity true if valid. false, otherwise.
	 */
	public Boolean valid(String token, String url) {
		this.token = token;
		this.url = url;
		if (getTokenError(token) != null || getUrlError(url) != null)
			return false;
		return true;
	}

	/**
	 * Get errors while validating username field.
	 * 
	 * @param username
	 *            Username to check
	 * @return Error message (null if valid)
	 */
	public String getUsernameError(String username) {
		if (username.contentEquals(""))
			return "Username can't be empty";
		return null;
	}

	/**
	 * Check the validity of the password field.
	 * 
	 * @param password
	 *            password to check
	 * @return Error message (null if valid)
	 */
	public String getPasswordError(String password) {
		if (password.contentEquals(""))
			return "Password can't be empty";
		return null;
	}

	/**
	 * Check the validity of the token field.
	 * 
	 * @param token
	 *            token to check
	 * @return Error message (null if valid)
	 */
	public String getTokenError(String token) {
		if (token.contentEquals(""))
			return "Token can't be empty";
		return null;
	}

	/**
	 * Check the validity of the url field.
	 * 
	 * @param url
	 *            url to check
	 * @return Error message (null if valid)
	 */
	public String getUrlError(String url) {
		if (url.contentEquals(""))
			return "url can't be empty";
		UrlValidator urlValidator = new UrlValidator();
		if (!urlValidator.isValid(url))
			return "Invalid moodle url";
		return null;
	}

}
