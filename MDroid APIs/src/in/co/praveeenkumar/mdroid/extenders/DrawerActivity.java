/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 28th May, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveeenkumar.mdroid.extenders;

import in.co.praveenkumar.mdroid.apis.R;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class DrawerActivity extends Activity {
	private String[] mMenuItems;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private CharSequence mTitle;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTitle = "test";

		mMenuItems = new String[] { "My Courses", "All Courses", "Favourites" };
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_nav_listview);

		// Set the adapter for the list view
		mDrawerList.setAdapter(new CustomLeftNavAdapter(
				getApplicationContext(), mMenuItems));

		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	/**
	 * Swaps fragments in the main content view
	 */
	private void selectItem(int position) {
		Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();

		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mMenuItems[position]);
		mDrawerLayout.closeDrawer(mDrawerList);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		// Respond to the action bar's Up/Home button
		case android.R.id.home:
			Intent upIntent = NavUtils.getParentActivityIntent(this);
			NavUtils.navigateUpTo(this, upIntent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

	// private class DrawerItemClickListener implements
	// ListView.OnItemClickListener {
	// @Override
	// public void onItemClick(AdapterView<?> parent, View view, int position,
	// long id) {
	// selectItem(position);
	// }
	// }

	private class CustomLeftNavAdapter extends ArrayAdapter<String> {
		private final Context context;

		public CustomLeftNavAdapter(Context context, String[] mMenuItems) {
			super(context, R.layout.list_item_left_drawer, mMenuItems);
			this.context = context;

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.list_item_left_drawer,
					parent, false);
			final TextView mItemView = (TextView) findViewById(R.id.left_menu_item);
			if (rowView.isSelected())
				mItemView.setTypeface(null, Typeface.BOLD);

			rowView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mItemView.setTypeface(null, Typeface.BOLD);
					selectItem(position);

				}
			});

			return rowView;
		}

	}

}
