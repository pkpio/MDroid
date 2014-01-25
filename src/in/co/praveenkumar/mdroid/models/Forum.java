/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	25-13-2013
 * 
 * © 2013, Praveen Kumar Pendyala. 
 * Licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 
 * 3.0 Unported license, http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * 
 * This is a part of the MDroid project. You may use the contents of this file
 * or the project only if you comply with license of the project, available at the 
 * Github repo: https://github.com/praveendath92/MDroid/blob/master/README.md
 * 
 */

package in.co.praveenkumar.mdroid.models;

public class Forum {
	int fId;
	String fSubject;
	int fPostCount;

	// constructors
	public Forum() {
	}

	public Forum(int fId, String fSubject, int fPostsCount) {
		this.fId = fId;
		this.fSubject = fSubject;
		this.fPostCount = fPostsCount;
	}

	// setters
	public void setId(int fId) {
		this.fId = fId;
	}

	public void setSubject(String fSubject) {
		this.fSubject = fSubject;
	}

	public void setPostCount(int fPostCount) {
		this.fPostCount = fPostCount;
	}

	// getters
	public int getId() {
		return this.fId;
	}

	public String getSubject() {
		return this.fSubject;
	}

	public int getPostCount() {
		return this.fPostCount;
	}

}
