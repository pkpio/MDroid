package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.mdroid.apis.R;
import in.co.praveenkumar.mdroid.helper.AppInterface.DiscussionIdInterface;
import in.co.praveenkumar.mdroid.helper.AppNavigationDrawer;
import android.os.Bundle;

public class PostActivity extends AppNavigationDrawer implements
		DiscussionIdInterface {
	int discussionid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		discussionid = getIntent().getExtras().getInt("discussionid");
		setContentView(R.layout.activity_post);
		setUpDrawer();
	}

	@Override
	public int getDiscussionId() {
		return this.discussionid;
	}
}
