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

package in.co.praveenkumar.mdroid.sqlite.databases;

import in.co.praveenkumar.mdroid.models.ForumThread;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqliteTbForums {
	private final String DEBUG_TAG = "MDroid Sqlite Forum functions";
	private Sqlite Sqldb;

	// Table Name and column tags
	private static final String TABLE_FORUMS = Sqlite.TABLE_FORUMS;
	private static final String KEY_COURSE_ID = Sqlite.KEY_COURSE_ID;
	private static final String KEY_FORUM_POST_ID = Sqlite.KEY_FORUM_POST_ID;
	private static final String KEY_FORUM_NUM_RESP = Sqlite.KEY_FORUM_NUM_RESP;

	public SqliteTbForums(Context context) {
		// Sqlite database handler
		Sqldb = new Sqlite(context);
	}

	// Add forum to list
	public void addThread(String cId, String threadId) {
		SQLiteDatabase db = Sqldb.getReadableDatabase();

		// Check the course is not already in the list. If not, add.
		String selectQuery = "SELECT  * FROM " + TABLE_FORUMS + " WHERE "
				+ KEY_COURSE_ID + " = " + cId + " AND " + KEY_FORUM_POST_ID
				+ " = " + threadId;
		Log.d(DEBUG_TAG, selectQuery);
		Cursor c = db.rawQuery(selectQuery, null);
		if (!c.moveToFirst()) {
			c.close();
			ContentValues values = new ContentValues();
			values.put(KEY_COURSE_ID, cId);
			values.put(KEY_FORUM_POST_ID, threadId);
			db.insert(TABLE_FORUMS, null, values);
			Log.d(DEBUG_TAG, "Inserted !");
		}
	}

	// Add threads
	public void addThreads(String cId, ArrayList<ForumThread> threads) {
		for (int i = 0; i < threads.size(); i++) {
			// Updating count will also add the thread if necessary
			updateResponseCount(cId, threads.get(i).getId(),
					Integer.parseInt(threads.get(i).getReplyCount()));
		}
	}

	// Update reply count
	public void updateResponseCount(String cId, String threadId, int respCount) {
		SQLiteDatabase db = Sqldb.getReadableDatabase();
		ContentValues values = new ContentValues();

		// Check if it is already in the db. Else Insert.
		addThread(cId, threadId);

		values.put(KEY_FORUM_NUM_RESP, respCount);
		db.update(TABLE_FORUMS, values, KEY_FORUM_POST_ID + " = ?",
				new String[] { String.valueOf(threadId) });
	}

	// Get thread reply count
	public int getResponseCount(String cId, String threadId) {
		String selectQuery = "SELECT  " + KEY_FORUM_NUM_RESP + " FROM "
				+ TABLE_FORUMS + " WHERE " + KEY_COURSE_ID + " = " + cId
				+ " AND " + KEY_FORUM_POST_ID + " = " + threadId;

		Log.d(DEBUG_TAG, selectQuery);

		SQLiteDatabase db = Sqldb.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		if (c.moveToFirst())
			return c.getInt(c.getColumnIndex(KEY_FORUM_NUM_RESP));
		else
			return 0;
	}
}
