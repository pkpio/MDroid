package in.co.praveenkumar.mdroid.apis;

import in.co.praveenkumar.mdroid.moodlerest.MoodleToken;

import java.io.IOException;
import java.net.ProtocolException;

import net.beaconhillcott.moodlerest.MoodleCallRestWebService;

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
			RestJsonMoodleClient rjmc = new RestJsonMoodleClient();
			try {
				rjmc.doRestCall();
			} catch (ProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
