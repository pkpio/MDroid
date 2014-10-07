package in.co.praveenkumar.mdroid.dialog;

import in.co.praveenkumar.mdroid.legacy.R;
import in.co.praveenkumar.mdroid.moodlemodel.MoodleContact;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Window;

public class MessageDialog extends Dialog {
	Context context;
	MoodleContact contact = new MoodleContact();

	public MessageDialog(Context context) {
		super(context);
		this.context = context;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.dialog_message);
	}

	public void setContact(MoodleContact contact) {
		this.contact = contact;
	}

}
