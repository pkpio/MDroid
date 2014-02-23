/*
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created:	15-02-2014
 * 
 * © 2013-2014 Praveen Kumar Pendyala. 
 * Licensed under the Creative Commons Attribution-NonCommercial-ShareAlike 
 * 3.0 Unported license, http://creativecommons.org/licenses/by-nc-sa/3.0/ 
 * 
 * This is a part of the MDroid project. You may use the contents of this file
 * or the project only if you comply with license of the project, available at the 
 * Github repo: https://github.com/praveendath92/MDroid/blob/master/README.md
 * 
 */

package in.co.praveenkumar.mdroid.sqlite.databases;

import in.co.praveenkumar.mdroid.models.Mnotification;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqliteTbNotifications {
	private final String DEBUG_TAG = "MDroid Sqlite notifications functions";
	private Sqlite Sqldb;

	// Table name and column tags
	private static final String TABLE_NOTIFICATIONS = Sqlite.TABLE_NOTIFICATIONS;
	public static final String KEY_NOTIFICATION_TYPE = Sqlite.KEY_NOTIFICATION_TYPE;
	public static final String KEY_NOTIFICATION_COURSE_NAME = Sqlite.KEY_NOTIFICATION_COURSE_NAME;
	public static final String KEY_NOTIFICATION_POST_SUBJECT = Sqlite.KEY_NOTIFICATION_POST_SUBJECT;
	public static final String KEY_NOTIFICATION_COUNT = Sqlite.KEY_NOTIFICATION_COUNT;
	public static final String KEY_NOTIFICATION_READ = Sqlite.KEY_NOTIFICATION_READ;

	public static final String KEY_ID = Sqlite.KEY_ID;
	public static final String KEY_COURSE_ID = Sqlite.KEY_COURSE_ID;
	public static final String KEY_FORUM_POST_ID = Sqlite.KEY_FORUM_POST_ID;

	public static final int KEY_NOTIFICATION_TYPE_FILE = Sqlite.KEY_NOTIFICATION_TYPE_FILE;
	public static final int KEY_NOTIFICATION_TYPE_FORUM = Sqlite.KEY_NOTIFICATION_TYPE_FORUM;

	public SqliteTbNotifications(Context context) {
		// Sqlite database handler
		Sqldb = new Sqlite(context);
	}

	public void addFileNotification(String cId, String cName, int count) {
		SQLiteDatabase db = Sqldb.getReadableDatabase();

		// Add values to query
		ContentValues values = new ContentValues();
		values.put(KEY_COURSE_ID, cId);
		values.put(KEY_NOTIFICATION_COURSE_NAME, cName);
		values.put(KEY_NOTIFICATION_TYPE, KEY_NOTIFICATION_TYPE_FILE);
		values.put(KEY_NOTIFICATION_COUNT, count);
		values.put(KEY_NOTIFICATION_READ, 0);

		// Insert into database
		db.insert(TABLE_NOTIFICATIONS, null, values);
		Log.d(DEBUG_TAG, "Notification added!");
	}

	public void addForumNotification(String cId, String cName, String threadId,
			String threadSubject, int count) {
		SQLiteDatabase db = Sqldb.getReadableDatabase();

		// Add values to query
		ContentValues values = new ContentValues();
		values.put(KEY_COURSE_ID, cId);
		values.put(KEY_NOTIFICATION_COURSE_NAME, cName);
		values.put(KEY_NOTIFICATION_TYPE, KEY_NOTIFICATION_TYPE_FORUM);
		values.put(KEY_FORUM_POST_ID, threadId);
		values.put(KEY_NOTIFICATION_POST_SUBJECT, threadSubject);
		values.put(KEY_NOTIFICATION_COUNT, count);
		values.put(KEY_NOTIFICATION_READ, 0);

		// Insert into database
		db.insert(TABLE_NOTIFICATIONS, null, values);
		Log.d(DEBUG_TAG, "Notification added!");
	}

	public void deleteNotification(String notificationId) {
		SQLiteDatabase db = Sqldb.getReadableDatabase();

		String selectQuery = "DELETE  * FROM " + TABLE_NOTIFICATIONS
				+ " WHERE " + KEY_ID + " = " + notificationId;

		Log.d(DEBUG_TAG, selectQuery);
		db.rawQuery(selectQuery, null);
	}

	public void markNotificationAsRead(String notificationId) {
		SQLiteDatabase db = Sqldb.getReadableDatabase();

		// Add values to query
		ContentValues values = new ContentValues();
		values.put(KEY_NOTIFICATION_READ, 1);
		db.update(TABLE_NOTIFICATIONS, values, KEY_ID + " = ?",
				new String[] { String.valueOf(notificationId) });
		Log.d(DEBUG_TAG, "notification marked as read : " + notificationId);

	}

	public ArrayList<Mnotification> getAllUnreadNotifications() {
		// Query to get all fields in notifications table
		String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS
				+ " WHERE " + KEY_NOTIFICATION_READ + " = 0";
		Log.d(DEBUG_TAG, selectQuery);

		return getNotificationsFromQuery(selectQuery);

	}

	public ArrayList<Mnotification> getAllReadNotifications() {
		// Query to get all fields in notifications table
		String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS
				+ " WHERE " + KEY_NOTIFICATION_READ + " = 1";
		Log.d(DEBUG_TAG, selectQuery);

		return getNotificationsFromQuery(selectQuery);

	}

	private ArrayList<Mnotification> getNotificationsFromQuery(String query) {
		ArrayList<Mnotification> notifications = new ArrayList<Mnotification>();

		SQLiteDatabase db = Sqldb.getReadableDatabase();
		Cursor c = db.rawQuery(query, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				Mnotification notification = new Mnotification();

				// Fill notification with all details from database for an entry
				notification.setId(c.getString(c.getColumnIndex(KEY_ID)));
				notification.setType(c.getInt(c
						.getColumnIndex(KEY_NOTIFICATION_TYPE)));
				notification.setCourseId(c.getString(c
						.getColumnIndex(KEY_COURSE_ID)));
				notification.setCourseName(c.getString(c
						.getColumnIndex(KEY_NOTIFICATION_COURSE_NAME)));
				notification.setPostId(c.getString(c
						.getColumnIndex(KEY_FORUM_POST_ID)));
				notification.setPostSubject(c.getString(c
						.getColumnIndex(KEY_NOTIFICATION_POST_SUBJECT)));
				notification.setCount(c.getInt(c
						.getColumnIndex(KEY_NOTIFICATION_COUNT)));
				notification.setRead(c.getInt(c
						.getColumnIndex(KEY_NOTIFICATION_READ)));

				// Add to the list of notifications
				notifications.add(notification);
			} while (c.moveToNext());
		}
		c.close();

		return notifications;
	}

}
