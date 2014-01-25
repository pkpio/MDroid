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

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class SqliteTbCourses {
	private final String DEBUG_TAG = "MDroid Sqlite Course functions";
	private Sqlite Sqldb;

	// Table Name and column tags
	private static final String TABLE_COURSES = Sqlite.TABLE_COURSES;
	private static final String KEY_COURSE_ID = Sqlite.KEY_COURSE_ID;
	private static final String KEY_COURSE_IS_FAV = Sqlite.KEY_COURSE_IS_FAV;
	private static final String KEY_COURSE_NUM_FILES = Sqlite.KEY_COURSE_NUM_FILES;
	private static final String KEY_COURSE_NUM_FORUMS = Sqlite.KEY_COURSE_NUM_FORUMS;

	public SqliteTbCourses(Context context) {
		// Sqlite database handler
		Sqldb = new Sqlite(context);
	}

	// Add course to list
	public void addCourse(String cId) {
		SQLiteDatabase db = Sqldb.getReadableDatabase();

		// Check the course is not already in the list. If not, add.
		String selectQuery = "SELECT  * FROM " + TABLE_COURSES + " WHERE "
				+ KEY_COURSE_ID + " = " + cId;
		Log.d(DEBUG_TAG, selectQuery);
		Cursor c = db.rawQuery(selectQuery, null);
		if (!c.moveToFirst()) {
			ContentValues values = new ContentValues();
			values.put(KEY_COURSE_ID, cId);
			values.put(KEY_COURSE_IS_FAV, 0);
			db.insert(TABLE_COURSES, null, values);
			Log.d(DEBUG_TAG, "Inserted !");
		}
	}

	// Make a course as fav
	public void favCourse(String cId) {
		SQLiteDatabase db = Sqldb.getReadableDatabase();
		ContentValues values = new ContentValues();

		// Check if it is already in the db. Else Insert.
		addCourse(cId);

		values.put(KEY_COURSE_IS_FAV, 1);
		db.update(TABLE_COURSES, values, KEY_COURSE_ID + " = ?",
				new String[] { String.valueOf(cId) });
	}

	// Make a course as fav
	public void unFavCourse(String cId) {
		SQLiteDatabase db = Sqldb.getReadableDatabase();
		ContentValues values = new ContentValues();

		// Check if it is already in the db. Else Insert.
		addCourse(cId);

		values.put(KEY_COURSE_IS_FAV, 0);
		db.update(TABLE_COURSES, values, KEY_COURSE_ID + " = ?",
				new String[] { String.valueOf(cId) });
	}

	// Check if a course is fav or not
	public boolean isFav(String cId) {
		String selectQuery = "SELECT  * FROM " + TABLE_COURSES + " WHERE "
				+ KEY_COURSE_ID + " = " + cId + " AND " + KEY_COURSE_IS_FAV
				+ " = 1";
		Log.d(DEBUG_TAG, selectQuery);

		SQLiteDatabase db = Sqldb.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		if (!c.moveToFirst())
			return false;
		else
			return true;
	}

	// Get all courses from list
	public ArrayList<String> getFavCourses() {
		ArrayList<String> courses = new ArrayList<String>();
		String selectQuery = "SELECT  * FROM " + TABLE_COURSES + " WHERE "
				+ KEY_COURSE_IS_FAV + " = 1";

		Log.d(DEBUG_TAG, selectQuery);

		SQLiteDatabase db = Sqldb.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);

		// looping through all rows and adding to list
		if (c.moveToFirst()) {
			do {
				courses.add(c.getString(c.getColumnIndex(KEY_COURSE_ID)));
			} while (c.moveToNext());
		}

		return courses;
	}

	// Update file counts
	public void updateFileCount(String cId, int fileCount) {
		SQLiteDatabase db = Sqldb.getReadableDatabase();
		ContentValues values = new ContentValues();

		// Check if it is already in the db. Else Insert.
		addCourse(cId);

		values.put(KEY_COURSE_NUM_FILES, fileCount);
		db.update(TABLE_COURSES, values, KEY_COURSE_ID + " = ?",
				new String[] { String.valueOf(cId) });
	}

	// Update forum counts
	public void updateForumCount(String cId, int forumCount) {
		SQLiteDatabase db = Sqldb.getReadableDatabase();
		ContentValues values = new ContentValues();

		// Check if it is already in the db. Else Insert.
		addCourse(cId);

		values.put(KEY_COURSE_NUM_FORUMS, forumCount);
		db.update(TABLE_COURSES, values, KEY_COURSE_ID + " = ?",
				new String[] { String.valueOf(cId) });
	}

	// Get files count
	public int getFileCount(String cId) {
		String selectQuery = "SELECT  " + KEY_COURSE_NUM_FILES + " FROM "
				+ TABLE_COURSES + " WHERE " + KEY_COURSE_ID + " = " + cId;

		Log.d(DEBUG_TAG, selectQuery);

		SQLiteDatabase db = Sqldb.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		c.moveToFirst();
		return c.getInt(c.getColumnIndex(KEY_COURSE_NUM_FILES));
	}

	// Get forums count
	public int getForumCount(String cId) {
		String selectQuery = "SELECT  " + KEY_COURSE_NUM_FORUMS + " FROM "
				+ TABLE_COURSES + " WHERE " + KEY_COURSE_ID + " = " + cId;

		Log.d(DEBUG_TAG, selectQuery);

		SQLiteDatabase db = Sqldb.getReadableDatabase();
		Cursor c = db.rawQuery(selectQuery, null);
		c.moveToFirst();
		return c.getInt(c.getColumnIndex(KEY_COURSE_NUM_FORUMS));
	}

}
