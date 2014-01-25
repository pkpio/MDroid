/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	01-25-2014
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

package in.co.praveenkumar.mdroid.services;

import in.co.praveenkumar.mdroid.networking.FetchForum;
import in.co.praveenkumar.mdroid.networking.FetchForumFiles;
import in.co.praveenkumar.mdroid.networking.FetchResourceFiles;
import in.co.praveenkumar.mdroid.sqlite.databases.SqliteTbCourses;
import in.co.praveenkumar.mdroid.sqlite.databases.SqliteTbForums;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class UpdatesChecker {
	private final String DEBUG_TAG = "MDroid services course updates checker";
	private String cId;
	SqliteTbCourses stc;
	SqliteTbForums stf;

	private int fileUpdateCount = 0;
	private int forumThreadUpdateCount = 0;
	private int forumRepliesUpdateCount = 0;

	public UpdatesChecker(Context context, String cId) {
		this.cId = cId;
		stc = new SqliteTbCourses(context);
		stf = new SqliteTbForums(context);
	}

	public void checkForUpdates() {

		// Checking new files
		FetchResourceFiles frf = new FetchResourceFiles();
		frf.fetchFiles(cId);
		FetchForumFiles fff = new FetchForumFiles();
		fff.fetchFiles(cId);
		int mCourseFilesCount = frf.getFilesCount() + fff.getFilesCount();
		int dbCourseFilesCount = stc.getFileCount(cId);
		fileUpdateCount = mCourseFilesCount - dbCourseFilesCount;
		Log.d(DEBUG_TAG, "Files update done. Found " + fileUpdateCount);

		// Checking new forum threads
		FetchForum ff = new FetchForum();
		ff.fetchForum(cId);
		int mForumThreadCount = ff.getThreadsCount();
		int dbForumThreadCount = stc.getForumCount(cId);
		forumThreadUpdateCount = mForumThreadCount - dbForumThreadCount;
		Log.d(DEBUG_TAG, "Forums update done. Found " + forumThreadUpdateCount);

		// Checking for updates in each thread
		ArrayList<String> mThreadIds = ff.getThreadIds();
		ArrayList<String> mThreadReplyCounts = ff.getThreadReplyCounts();
		for (int i = 0; i < mThreadIds.size(); i++) {
			int mReplyCount = Integer.parseInt(mThreadReplyCounts.get(i));
			int dbReplyCount = stf.getResponseCount(cId, mThreadIds.get(i));
			forumRepliesUpdateCount += mReplyCount - dbReplyCount;
		}
		Log.d(DEBUG_TAG, "Forum reply update done. Found "
				+ forumRepliesUpdateCount);
	}

	public int getTotalUpdatesCount() {
		return fileUpdateCount + forumRepliesUpdateCount
				+ forumThreadUpdateCount;
	}

	public int getFilesUpdatesCount() {
		return fileUpdateCount;
	}

	public int getForumsUpdatesCount() {
		return forumThreadUpdateCount;
	}

	public int getRepliesUpdatesCount() {
		return forumRepliesUpdateCount;
	}

}
