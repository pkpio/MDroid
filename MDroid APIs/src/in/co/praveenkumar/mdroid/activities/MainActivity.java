/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 29th May, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.activities;

import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helpers.Database;
import in.co.praveenkumar.mdroid.models.MoodleCourse;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestCourseContents;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestCourses;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestDiscussions;
import in.co.praveenkumar.mdroid.moodlerest.MoodleToken;

import java.util.ArrayList;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity {
	final String DEBUG_TAG = "MainActivity";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Testing
		Database db = new Database(this);
		db.setToken("9bd96dc343e76a041729aa3e602de8c4");
		db.setmUrl("http://moodle.praveenkumar.co.in/");
		new tryAsyncLogin().execute("");

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);

		return true;
	}

	private class tryAsyncLogin extends AsyncTask<String, Integer, Long> {

		protected Long doInBackground(String... credentials) {
			MoodleToken mt = new MoodleToken("praveendath92", "praveen92",
					"http://moodle.praveenkumar.co.in");
			Log.d(DEBUG_TAG, mt.getToken());
			for (int i = 0; i < mt.getErrors().size(); i++)
				Log.d(DEBUG_TAG, mt.getErrors().get(i));

			MoodleRestCourses mrc = new MoodleRestCourses(
					getApplicationContext());
			ArrayList<MoodleCourse> mCourses = mrc.getCourses();
			if (mCourses != null)
				Log.d(DEBUG_TAG, mCourses.get(1).getFullname());

			MoodleRestCourseContents mrcc = new MoodleRestCourseContents(
					getApplicationContext());
			mrcc.getCourseContent("2");

			MoodleRestDiscussions mrd = new MoodleRestDiscussions(
					getApplicationContext());
			ArrayList<String> forumIds = new ArrayList<String>();
			forumIds.add("1");
			mrd.getDiscussions(forumIds);

			return null;

		}

		protected void onPostExecute(Long result) {
			System.out.println("Post exec");
		}
	}

}
