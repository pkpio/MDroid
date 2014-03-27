package in.co.praveenkumar.mdroid.apis;

import in.co.praveenkumar.mdroid.helpers.Database;
import in.co.praveenkumar.mdroid.moodlerest.MoodleRestCourses;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;

public class MainActivity extends Activity {

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
			MoodleRestCourses mrc = new MoodleRestCourses(
					getApplicationContext());
			mrc.getCourses();
			// MoodleToken mToken = new MoodleToken("praveendath92",
			// "praveen92",
			// "http://moodle.praveenkumar.co.in");
			// System.out.println(mToken.getToken());
			return null;

		}

		protected void onPostExecute(Long result) {
			System.out.println("Post exec");
		}
	}

}
