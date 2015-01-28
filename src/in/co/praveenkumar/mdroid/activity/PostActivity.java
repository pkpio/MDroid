package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.mdroid.helper.AppInterface.DiscussionIdInterface;
import in.co.praveenkumar.mdroid.helper.ApplicationClass;
import in.co.praveenkumar.mdroid.helper.Param;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.model.MoodleDiscussion;

import java.util.List;

import android.os.Bundle;

public class PostActivity extends BaseNavigationActivity implements
		DiscussionIdInterface {
	int discussionid;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		discussionid = getIntent().getExtras().getInt("discussionid");
		setContentView(R.layout.activity_post);
		setUpDrawer();

		// Send a tracker
		((ApplicationClass) getApplication()).sendScreen(Param.GA_SCREEN_POST);

		// Set title
		SessionSetting session = new SessionSetting(this);
		List<MoodleDiscussion> mDiscussions = MoodleDiscussion.find(
				MoodleDiscussion.class, "discussionid = ? and siteid = ?",
				discussionid + "", session.getCurrentSiteId() + "");
		if (mDiscussions.size() > 0)
			getSupportActionBar().setTitle(mDiscussions.get(0).getName());
		getSupportActionBar().setIcon(R.drawable.icon_forum);
	}

	@Override
	public int getDiscussionId() {
		return this.discussionid;
	}
}
