package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.mdroid.helper.AppInterface.ForumIdInterface;
import in.co.praveenkumar.mdroid.helper.AppNavigationDrawer;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleForum;

import java.util.List;

import android.os.Bundle;

public class DiscussionActivity extends AppNavigationDrawer implements
		ForumIdInterface {
	int forumid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		forumid = getIntent().getExtras().getInt("forumid");
		setContentView(R.layout.activity_discussion);
		setUpDrawer();

		// Set title
		SessionSetting session = new SessionSetting(this);
		List<MoodleForum> mForums = MoodleForum.find(MoodleForum.class,
				"forumid = ? and siteid = ?", forumid + "",
				session.getCurrentSiteId() + "");
		if (mForums.size() > 0)
			getSupportActionBar().setTitle(mForums.get(0).getName());
		getSupportActionBar().setIcon(R.drawable.icon_forum);
	}

	@Override
	public int getForumId() {
		return this.forumid;
	}

}
