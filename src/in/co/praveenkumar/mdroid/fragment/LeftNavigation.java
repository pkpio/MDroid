package in.co.praveenkumar.mdroid.fragment;

import in.co.praveenkumar.mdroid.apis.R;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class LeftNavigation extends Fragment {
	ListView navListView;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View rootView = inflater.inflate(R.layout.frag_left_navigation,
				container, false);
		navListView = (ListView) rootView.findViewById(R.id.left_nav_list);

		String[] values = new String[] { "Android", "iPhone", "WindowsMobile",
				"Blackberry", "WebOS", "Ubuntu", "Windows7", "Max OS X",
				"Linux", "OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux",
				"OS/2", "Ubuntu", "Windows7", "Max OS X", "Linux", "OS/2",
				"Android", "iPhone", "WindowsMobile" };

		final LeftNavListAdapter adapter = new LeftNavListAdapter(
				getActivity(), values);
		navListView.setAdapter(adapter);

		return rootView;
	}

	public class LeftNavListAdapter extends ArrayAdapter<String> {
		private final Context context;
		private final String[] values;

		public LeftNavListAdapter(Context context, String[] values) {
			super(context, R.layout.list_item_account, values);
			this.context = context;
			this.values = values;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			View rowView = inflater.inflate(R.layout.list_item_account, parent,
					false);

			return rowView;
		}
	}

}
