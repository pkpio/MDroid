package in.co.praveenkumar.mdroid.activities;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import in.co.praveeenkumar.mdroid.extenders.HomeDrawerActivity;
import in.co.praveeenkumar.mdroid.extenders.StickyListViewAdapter;
import in.co.praveenkumar.mdroid.apis.R;
import se.emilsjolander.stickylistheaders.StickyListHeadersListView;
import android.content.Context;
import android.os.Bundle;

public class TestActivity extends HomeDrawerActivity {
	private listViewTesting myListAdapter;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.test);
		super.onCreate(savedInstanceState);

		// StickyHeader List view
		String[] mCountries;
		mCountries = getResources().getStringArray(R.array.countries);
		ArrayList<String> dataSet = new ArrayList<String>(
				Arrays.asList(mCountries));
		myListAdapter = new listViewTesting(this, dataSet);

		StickyListHeadersListView listview;
		listview = (StickyListHeadersListView) findViewById(R.id.list);
		listview.setAreHeadersSticky(true);
		listview.setAdapter(myListAdapter);

	}

	private class listViewTesting extends StickyListViewAdapter {

		public listViewTesting(Context context, ArrayList<String> dataSet) {
			super(context, dataSet);
			// TODO Auto-generated constructor stub
		}
	}

}
