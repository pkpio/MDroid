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

	public static final String KEY_ID = Sqlite.KEY_ID;
	public static final String KEY_COURSE_ID = Sqlite.KEY_COURSE_ID;
	public static final String KEY_FORUM_POST_ID = Sqlite.KEY_FORUM_POST_ID;

	public static final int KEY_NOTIFICATION_TYPE_FILE = Sqlite.KEY_NOTIFICATION_TYPE_FILE;
	public static final int KEY_NOTIFICATION_TYPE_FORUM = Sqlite.KEY_NOTIFICATION_TYPE_FORUM;

	public SqliteTbNotifications(Context context) {
		// Sqlite database handler
		Sqldb = new Sqlite(context);
	}

	public void addFileNotification(String cId, String cName, String count) {
		SQLiteDatabase db = Sqldb.getReadableDatabase();

		// Add values to query
		ContentValues values = new ContentValues();
		values.put(KEY_COURSE_ID, cId);
		values.put(KEY_NOTIFICATION_COURSE_NAME, cName);
		values.put(KEY_NOTIFICATION_TYPE, KEY_NOTIFICATION_TYPE_FILE);
		values.put(KEY_NOTIFICATION_COUNT, count);

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

		// Insert into database
		db.insert(TABLE_NOTIFICATIONS, null, values);
		Log.d(DEBUG_TAG, "Notification added!");
	}

	public void clearNotification(String notificationId) {
		SQLiteDatabase db = Sqldb.getReadableDatabase();

		String selectQuery = "DELETE  * FROM " + TABLE_NOTIFICATIONS
				+ " WHERE " + KEY_ID + " = " + notificationId;

		Log.d(DEBUG_TAG, selectQuery);
		db.rawQuery(selectQuery, null);
	}

	public ArrayList<Mnotification> getAllNotifications() {
		ArrayList<Mnotification> notifications = new ArrayList<Mnotification>();

		// Query to get all fields in notifications table
		String selectQuery = "SELECT  * FROM " + TABLE_NOTIFICATIONS;

		Log.d(DEBUG_TAG, selectQuery);

		SQLiteDatabase db = Sqldb.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

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

				// Add to the list of notifications
				notifications.add(notification);
			} while (c.moveToNext());
		}
		c.close();

		return notifications;

	}

}
