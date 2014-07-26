/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 15th April, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.sqllite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SqlLite extends SQLiteOpenHelper {
	private static final String DEBUG_TAG = "MDroid SQLite Helper";
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "MDroid";

	public SqlLite(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		// creating required tables
		// db.execSQL(CREATE_TABLE_COURSES);
		// db.execSQL(CREATE_TABLE_FORUMS);
		// db.execSQL(CREATE_TABLE_NOTIFICATIONS);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.d(DEBUG_TAG, "Upgrading Database..");

		// on upgrade drop older tables
		// db.execSQL("DROP TABLE IF EXISTS " + TABLE_COURSES);
		// db.execSQL("DROP TABLE IF EXISTS " + TABLE_FORUMS);
		// db.execSQL("DROP TABLE IF EXISTS " + TABLE_NOTIFICATIONS);

		// create new tables
		onCreate(db);
	}
}
