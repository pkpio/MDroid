package in.co.praveenkumar.mdroid.activity;

import android.os.Bundle;
import in.co.praveenkumar.mdroid.legacy.R;
import in.co.praveenkumar.mdroid.helper.AppNavigationDrawer;
import in.co.praveenkumar.mdroid.helper.AppInterface.ForumIdInterface;

public class DiscussionActivity extends AppNavigationDrawer implements
		ForumIdInterface {
	int forumid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		forumid = getIntent().getExtras().getInt("forumid");
		setContentView(R.layout.activity_discussion);
		setUpDrawer();
	}

	@Override
	public int getForumId() {
		return this.forumid;
	}

}
