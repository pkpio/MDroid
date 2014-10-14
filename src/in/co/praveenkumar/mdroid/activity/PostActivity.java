package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.mdroid.helper.AppInterface.DiscussionIdInterface;
import in.co.praveenkumar.mdroid.helper.AppNavigationDrawer;
import in.co.praveenkumar.mdroid.helper.SessionSetting;
import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleDiscussion;

import java.util.List;

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

		// Set title
		SessionSetting session = new SessionSetting(this);
		List<MoodleDiscussion> mDiscussions = MoodleDiscussion.find(
				MoodleDiscussion.class, "discussionid = ? and siteid = ?",
				discussionid + "", session.getCurrentSiteId() + "");
		if (mDiscussions.size() > 0)
			setTitle(mDiscussions.get(0).getName());
	}

	@Override
	public int getDiscussionId() {
		return this.discussionid;
	}
}
