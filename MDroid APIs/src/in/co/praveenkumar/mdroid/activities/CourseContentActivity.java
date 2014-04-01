package in.co.praveenkumar.mdroid.activities;

import android.os.Bundle;
import in.co.praveeenkumar.mdroid.extenders.DrawerActivity;
import in.co.praveenkumar.mdroid.apis.R;

public class CourseContentActivity extends DrawerActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.activity_course_content);
		super.onCreate(savedInstanceState);
	}

}
