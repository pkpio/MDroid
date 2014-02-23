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

import in.co.praveenkumar.mdroid.models.Course;
import in.co.praveenkumar.mdroid.models.ForumThread;
import in.co.praveenkumar.mdroid.networking.FetchForum;
import in.co.praveenkumar.mdroid.networking.FetchForumFiles;
import in.co.praveenkumar.mdroid.networking.FetchResourceFiles;
import in.co.praveenkumar.mdroid.sqlite.databases.SqliteTbCourses;
import in.co.praveenkumar.mdroid.sqlite.databases.SqliteTbForums;
import in.co.praveenkumar.mdroid.sqlite.databases.SqliteTbNotifications;

import java.util.ArrayList;

import android.content.Context;
import android.util.Log;

public class UpdatesChecker {
	final String DEBUG_TAG = "MDroid services course updates checker";
	Course course;
	SqliteTbCourses stc;
	SqliteTbForums stf;
	SqliteTbNotifications stn;

	private int fileUpdateCount = 0;
	private int forumThreadUpdateCount = 0;
	private int forumRepliesUpdateCount = 0;

	public UpdatesChecker(Context context, Course course) {
		this.course = course;
		stc = new SqliteTbCourses(context);
		stf = new SqliteTbForums(context);
		stn = new SqliteTbNotifications(context);
	}

	public void checkForUpdates() {

		// Checking new files
		FetchResourceFiles frf = new FetchResourceFiles();
		frf.fetchFiles(course.getId());

		FetchForumFiles fff = new FetchForumFiles();
		fff.fetchFiles(course.getId());

		int mCourseFilesCount = frf.getFiles().size() + fff.getFiles().size();
		int dbCourseFilesCount = stc.getFileCount(course.getId());

		fileUpdateCount = mCourseFilesCount - dbCourseFilesCount;

		// Update 'file update count' for the course to database
		if (fileUpdateCount > 0) {
			stn.addFileNotification(course.getId(), course.getName(),
					fileUpdateCount);
			stc.updateFileCount(course.getId(), mCourseFilesCount);
		}

		Log.d(DEBUG_TAG, "Files update done. Found " + fileUpdateCount);

		// Checking new forum threads
		FetchForum ff = new FetchForum();
		ff.fetchForum(course.getId());

		int mForumThreadCount = ff.getThreads().size();
		int dbForumThreadCount = stc.getForumCount(course.getId());

		forumThreadUpdateCount = mForumThreadCount - dbForumThreadCount;

		// Update threads count to avoid future duplicates
		stc.updateForumCount(course.getId(), dbForumThreadCount
				+ forumThreadUpdateCount);

		Log.d(DEBUG_TAG, "Forums update done. Found " + forumThreadUpdateCount);

		// Checking for updates in each thread
		ArrayList<ForumThread> mThreads = ff.getThreads();
		for (int i = 0; i < mThreads.size(); i++) {
			int mReplyCount = Integer.parseInt(mThreads.get(i).getReplyCount());
			int dbReplyCount = stf.getResponseCount(course.getId(), mThreads
					.get(i).getId());

			int threadUpdateCount = mReplyCount - dbReplyCount;

			// Update replies count to database - notification and forum tables
			// This is to avoid future duplicates
			// If it is a new thread use updateResponseCount as '0'
			// This will also add the thread to database.
			if (threadUpdateCount > 0
					|| !stf.isThreadInDb(course.getId(), mThreads.get(i).getId())) {
				stn.addForumNotification(course.getId(), course.getName(),
						mThreads.get(i).getId(), mThreads.get(i).getSubject(),
						threadUpdateCount);
				stf.updateResponseCount(course.getId(),
						mThreads.get(i).getId(), mReplyCount);
			}

			// Increase forums update count
			forumRepliesUpdateCount += threadUpdateCount;
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
