package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.fragment.MessageListingFragment;
import in.co.praveenkumar.mdroid.fragment.MessagingFragment;
import in.co.praveenkumar.mdroid.helper.AppInterface;
import in.co.praveenkumar.mdroid.helper.AppInterface.UserIdInterface;
import in.co.praveenkumar.mdroid.helper.ApplicationClass;
import in.co.praveenkumar.mdroid.helper.Param;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MessagingActivity extends BaseNavigationActivity implements
        UserIdInterface, AppInterface.FragmentChanger {
    public static final int FRAG_MESSAGE_LIST = 1;
    public static final int FRAG_MESSAGING = 2;
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
        changeFragment(FRAG_MESSAGE_LIST, false);

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

    @Override
    public void changeFragment(int FragmentId, Boolean animations) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        // Enable animations if required
        if(animations)
            transaction.setCustomAnimations(R.anim.enter, R.anim.exit,
                    R.anim.pop_enter, R.anim.pop_exit);

        switch(FragmentId){
            default:
            case FRAG_MESSAGE_LIST:
                transaction.replace(R.id.messaging_layout, new MessageListingFragment());
                break;
            case FRAG_MESSAGING:
                transaction.replace(R.id.messaging_layout, new MessagingFragment());
                break;
        }
        transaction.addToBackStack("null").commit();
    }
}
