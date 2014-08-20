/**************************************************************
 * Author: 	Praveen Kumar Pendyala
 * Project: MDroid
 * Created: 28th May, 2014.
 * License: http://creativecommons.org/licenses/by-nc-sa/3.0/
 **************************************************************/

package in.co.praveenkumar.mdroid.extenders;

import in.co.praveenkumar.mdroid.apis.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class HomeDrawerActivity extends Activity {
	private String[] mMenuItems;
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private CharSequence mTitle;
	private ActionBarDrawerToggle mDrawerToggle;
	private LinearLayout mDrawerView;
	private CustomLeftNavAdapter navListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTitle = "test";

		mMenuItems = new String[] { "My Courses", "All Courses", "Favourites" };
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_nav_listview);
		mDrawerView = (LinearLayout) findViewById(R.id.left_drawer);

		// Set the adapter for the list view
		navListAdapter = new CustomLeftNavAdapter(getApplicationContext(),
				mMenuItems);
		mDrawerList.setAdapter(navListAdapter);

		mDrawerToggle = new ActionBarDrawerToggle(this, /* host Activity */
		mDrawerLayout, /* DrawerLayout object */
		R.drawable.ic_drawer, /* nav drawer icon to replace 'Up' caret */
		R.string.drawer_open, /* "open drawer" description */
		R.string.drawer_close /* "close drawer" description */
		) {

			/** Called when a drawer has settled in a completely closed state. */
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
			}

			/** Called when a drawer has settled in a completely open state. */
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mTitle);
			}
		};

		// Set the drawer toggle as the DrawerListener
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

	}

	public void setUpMenu(String[] menuItems, int curPos) {
		mMenuItems = menuItems;
		navListAdapter.notifyDataSetChanged();

		if (curPos < menuItems.length) {
			mDrawerList.setItemChecked(curPos, true);
			navListAdapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Pass the event to ActionBarDrawerToggle, if it returns
		// true, then it has handled the app icon touch event
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	/**
	 * Swaps fragments in the main content view
	 */
	private void selectItem(int position) {
		Toast.makeText(this, R.string.app_name, Toast.LENGTH_SHORT).show();

		// Highlight the selected item, update the title, and close the drawer
		mDrawerList.setItemChecked(position, true);
		setTitle(mMenuItems[position]);
		mDrawerLayout.closeDrawer(mDrawerView);
	}

	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
		getActionBar().setTitle(mTitle);
	}

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
			final View rowView = inflater.inflate(
					R.layout.list_item_left_drawer, parent, false);
			final TextView mItemView = (TextView) rowView
					.findViewById(R.id.left_menu_item);
			mItemView.setText(mMenuItems[position]);
			if (mDrawerList.isItemChecked(position))
				mItemView.setTypeface(Typeface.create("sans-serif-bold",
						Typeface.BOLD));
			else
				mItemView.setTypeface(Typeface.create("sans-serif-light",
						Typeface.NORMAL));

			rowView.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					mItemView.setTypeface(Typeface.create("sans-serif-bold",
							Typeface.BOLD));
					selectItem(position);

				}
			});

			return rowView;
		}

	}

}
