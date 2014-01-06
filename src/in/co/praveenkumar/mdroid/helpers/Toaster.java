package in.co.praveenkumar.mdroid.helpers;

import in.co.praveenkumar.R;
import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class Toaster {
	private Toast toast;
	private View layout;

	public Toaster(Context context, View layout) {
		this.layout = layout;
		toast = new Toast(context);
		toast.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 50);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
	}

	public void showToast(String msg) {
		TextView text = (TextView) layout.findViewById(R.id.text);
		text.setText(msg);
		toast.show();
	}

}
