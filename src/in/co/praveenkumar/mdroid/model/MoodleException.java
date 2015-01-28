package in.co.praveenkumar.mdroid.model;

import com.google.gson.annotations.SerializedName;

public class MoodleException {
	@SerializedName("exception")
	String exception;

	@SerializedName("errorcode")
	String errorcode;

	@SerializedName("message")
	String message;

	@Override
	public String toString() {
		return "exception: " + exception + "\n\n errorcode: " + errorcode
				+ "\n\n message: " + message;
	}

	public String getException() {
		return exception;
	}

	public String getErrorcode() {
		return errorcode;
	}

	public String getMessage() {
		return message;
	}
}
