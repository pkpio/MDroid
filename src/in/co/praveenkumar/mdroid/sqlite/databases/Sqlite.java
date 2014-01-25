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

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class Sqlite extends SQLiteOpenHelper {

	// Logcat tag
	private static final String DEBUG_TAG = "MDroid SQLite Helper";

	// Database Version
	private static final int DATABASE_VERSION = 3;

	// Database Name
	private static final String DATABASE_NAME = "MDroid";

	// Table Names
	public static final String TABLE_COURSES = "courses";
	public static final String TABLE_FORUMS = "forums";

	// Common column names
	public static final String KEY_ID = "id";
	public static final String KEY_COURSE_ID = "course_id";

	// TABLE_COURSES - column names
	public static final String KEY_COURSE_IS_FAV = "course_is_fav";
	public static final String KEY_COURSE_NUM_FILES = "num_files";
	public static final String KEY_COURSE_NUM_FORUMS = "num_forum_posts";

	// TABLE_FORUMS - column names
	public static final String KEY_FORUM_POST_ID = "forum_post_id";
	public static final String KEY_FORUM_NUM_RESP = "forum_num_responses";

	// Table Create Statements
	// courses table create statement
	private static final String CREATE_TABLE_COURSES = "CREATE TABLE "
			+ TABLE_COURSES + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_COURSE_ID + " TEXT, " + KEY_COURSE_IS_FAV + " INTEGER, "
			+ KEY_COURSE_NUM_FILES + " INTEGER," + KEY_COURSE_NUM_FORUMS
			+ " INTEGER" + ")";

	// course forum info table create statement
	private static final String CREATE_TABLE_FORUMS = "CREATE TABLE "
			+ TABLE_FORUMS + "(" + KEY_ID + " INTEGER PRIMARY KEY,"
			+ KEY_COURSE_ID + " TEXT," + KEY_FORUM_POST_ID + " TEXT,"
			+ KEY_FORUM_NUM_RESP + " INTEGER" + ")";

	public Sqlite(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// creating required tables
		db.execSQL(CREATE_TABLE_COURSES);
		db.execSQL(CREATE_TABLE_FORUMS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(DEBUG_TAG, "Upgrading Database..");

		// on upgrade drop older tables
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORUMS);

		// create new tables
		onCreate(db);
	}
}
