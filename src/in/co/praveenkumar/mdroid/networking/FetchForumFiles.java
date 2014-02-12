/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	28-12-2013
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

package in.co.praveenkumar.mdroid.networking;

import in.co.praveenkumar.mdroid.FilesActivity.UIupdater;
import in.co.praveenkumar.mdroid.models.Mfile;
import in.co.praveenkumar.mdroid.models.ForumThread;
import in.co.praveenkumar.mdroid.parser.FilesInForumsParser;

import java.util.ArrayList;

public class FetchForumFiles {
	private ArrayList<ForumThread> threads = new ArrayList<ForumThread>();
	private ArrayList<Mfile> files = new ArrayList<Mfile>();
	private UIupdater UU;

	public FetchForumFiles() {
		// Using in service.
		UU = null;
	}

	public FetchForumFiles(UIupdater UU) {
		this.UU = UU;
	}

	public void fetchFiles(String cId) {
		// Get forum threadIds. Use FetchForum class can do this.
		// We will each thread and looking for files in them.
		FetchForum FF = new FetchForum();
		FF.fetchForum(cId);
		threads = FF.getThreads();

		// Get html content of each Thread. FetchForumThread class can do this.
		for (int i = 0; i < threads.size(); i++) {
			// Display progress on UI.
			// This is a bg thread task. This msg will be pushed
			// to UI thread by UIupdater class.
			// If this is initiated by a UI thread and not service.
			if (UU != null)
				UU.setForumProgress(i + 1, threads.size(), files);

			FetchForumThread FFT = new FetchForumThread();
			FFT.fetchThread(threads.get(i).getId());
			String tHtml = FFT.getThreadContent();

			// Now get files and ids from each thread
			FilesInForumsParser FFP = new FilesInForumsParser(tHtml);
			files.addAll(FFP.getFiles());
		}

	}

	public ArrayList<Mfile> getFiles() {
		return files;
	}

	// Will be used for forum data caching from file listing activity
	public ArrayList<ForumThread> getThreads() {
		return threads;
	}

}
