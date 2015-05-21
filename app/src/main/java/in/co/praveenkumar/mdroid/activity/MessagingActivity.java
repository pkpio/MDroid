package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.fragment.MessageListingFragment;
import in.co.praveenkumar.mdroid.helper.AppInterface.UserIdInterface;
import in.co.praveenkumar.mdroid.helper.ApplicationClass;
import in.co.praveenkumar.mdroid.helper.Param;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;

public class MessagingActivity extends BaseNavigationActivity implements
        UserIdInterface {
    int userid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        setUpDrawer();

        // Send a tracker
        ((ApplicationClass) getApplication())
                .sendScreen(Param.GA_SCREEN_MESSAGE_LISTING);

        // Set fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager
                .beginTransaction()
                .replace(R.id.messaging_layout, new MessageListingFragment())
                .commit();

        getSupportActionBar().setTitle("Messaging");
        getSupportActionBar().setIcon(R.drawable.icon_message);
    }

    @Override
    public int getUserId() {
        return this.userid;
    }

    @Override
    public void setUserId(int userid) {
        this.userid = userid;
    }

}
