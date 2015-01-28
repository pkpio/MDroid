package in.co.praveenkumar.mdroid.activity;

import in.co.praveenkumar.R;
import in.co.praveenkumar.mdroid.helper.ApplicationClass;
import in.co.praveenkumar.mdroid.helper.GsonExclude;
import in.co.praveenkumar.mdroid.helper.Param;
import in.co.praveenkumar.mdroid.model.MDroidFeature;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

public class DonationActivity extends BaseNavigationActivity {
	private final String DEBUG_TAG = "DonationActivity";
	List<MDroidFeature> features = new ArrayList<MDroidFeature>();
	Context context;
	FeatureListAdapter featureListAdapter;

	@SuppressLint("InflateParams")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_donations);
		setUpDrawer();

		// Send a tracker
		((ApplicationClass) getApplication())
				.sendScreen(Param.GA_SCREEN_DONATION);

		setTitle("Request features");
		this.context = this;

		// Get features from local db to start with
		// features = MDroidModelFeature.listAll(MDroidModelFeature.class);
		// if (features.size() == 0)
		parseFeaturesFromAssetFile();

		ListView featureList = (ListView) findViewById(R.id.list_donations);

		// Added header and footers to the list
		LayoutInflater inflater = this.getLayoutInflater();
		LinearLayout listHeaderView = (LinearLayout) inflater.inflate(
				R.layout.list_header_donations, null);
		featureList.addHeaderView(listHeaderView);

		// Set the list adapter
		featureListAdapter = new FeatureListAdapter(this);
		featureList.setAdapter(featureListAdapter);

		// Setup listitem clicklisteners
		featureList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				position--; // Because there is a header
				if (position < 0)
					return;
				startPurchase(features.get(position).getProductid());

				// Send a tracker event
				((ApplicationClass) getApplication()).sendEvent(
						Param.GA_EVENT_CAT_DONATION,
						Param.GA_EVENT_DONATION_BUTTON);
			}
		});

	}

	private void startPurchase(String product) {
		billing.purchase(this, product);
	}

	public class FeatureListAdapter extends BaseAdapter {
		private final Context context;

		public FeatureListAdapter(Context context) {
			this.context = context;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			final ViewHolder viewHolder;

			if (convertView == null) {
				viewHolder = new ViewHolder();
				LayoutInflater inflater = (LayoutInflater) context
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

				convertView = inflater.inflate(R.layout.list_item_feature,
						parent, false);

				viewHolder.featureimage = (ImageView) convertView
						.findViewById(R.id.feature_image);
				viewHolder.featurename = (TextView) convertView
						.findViewById(R.id.feature_name);
				viewHolder.featurevoteslayout = (LinearLayout) convertView
						.findViewById(R.id.feature_votes_layout);
				viewHolder.featurevotestarget = (TextView) convertView
						.findViewById(R.id.feature_votestarget);
				viewHolder.featurevotescasted = (TextView) convertView
						.findViewById(R.id.feature_votescasted);
				viewHolder.featuredescription = (TextView) convertView
						.findViewById(R.id.feature_description);

				// Save the holder with the view
				convertView.setTag(viewHolder);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// Assign values
			viewHolder.featurename.setText(features.get(position).getName());
			viewHolder.featurevotestarget.setText(features.get(position)
					.getVotestarget() + "");
			viewHolder.featurevotescasted.setText(features.get(position)
					.getVotescasted() + "");
			viewHolder.featuredescription.setText(features.get(position)
					.getDescription());

			// Feature image
			switch (features.get(position).getStatus()) {
			case MDroidFeature.IMPLEMENTED:
				viewHolder.featureimage
						.setImageResource(R.drawable.icon_done_green);
				break;
			case MDroidFeature.NOT_IMPLEMENTED:
				viewHolder.featureimage
						.setImageResource(R.drawable.icon_plusone_red);
				break;
			case MDroidFeature.PARTIALLY_IMPLEMENTED:
				viewHolder.featureimage
						.setImageResource(R.drawable.icon_plusone_yellow);
				break;
			}

			return convertView;
		}

		@Override
		public int getCount() {
			return features.size();
		}

		@Override
		public Object getItem(int position) {
			return features.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}
	}

	static class ViewHolder {
		ImageView featureimage;
		TextView featurename;
		LinearLayout featurevoteslayout;
		TextView featurevotestarget;
		TextView featurevotescasted;
		TextView featuredescription;
	}

	private void parseFeaturesFromAssetFile() {
		Log.d(DEBUG_TAG, "Reading from assets");
		AssetManager assetManager = this.getAssets();
		try {
			InputStreamReader reader = new InputStreamReader(
					assetManager.open("features.json"));
			GsonExclude ex = new GsonExclude();
			Gson gson = new GsonBuilder()
					.addDeserializationExclusionStrategy(ex)
					.addSerializationExclusionStrategy(ex).create();
			features = gson.fromJson(reader,
					new TypeToken<List<MDroidFeature>>() {
					}.getType());
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// for (int i = 0; i < features.size(); i++)
		// features.get(i).save();
	}

}
