package in.co.praveenkumar.mdroid.model;

import com.google.gson.annotations.SerializedName;
import com.orm.SugarRecord;
import com.orm.dsl.Ignore;

public class MDroidFeature extends SugarRecord<MDroidFeature> {

	@Ignore
	public static final int NOT_IMPLEMENTED = 0;

	@Ignore
	public static final int PARTIALLY_IMPLEMENTED = 1;

	@Ignore
	public static final int IMPLEMENTED = 2;

	@SerializedName("featureid")
	int featureid;

	@SerializedName("productid")
	String productid;

	@SerializedName("name")
	String name;

	@SerializedName("status")
	int status;

	@SerializedName("votestarget")
	int votestarget;

	@SerializedName("votescasted")
	int votescasted;

	@SerializedName("description")
	String description;

	/**
	 * Get id given to the feature. This has to be mapped with buying ids for
	 * proper vote mapping.
	 * 
	 * @return featureid
	 */
	public int getFeatureid() {
		return featureid;
	}

	/**
	 * Get the productid of this feature for donating.
	 * 
	 * @return productid
	 */
	public String getProductid() {
		return productid;
	}

	/**
	 * Get feature name or title
	 * 
	 * @return name
	 */
	public String getName() {
		return name;
	}

	/**
	 * Get the current implementation status of the feature. Available status
	 * are:<br/>
	 * NOT_IMPLEMENTED<br/>
	 * PARTIALLY_IMPLEMENTED<br/>
	 * IMPLEMENTED
	 * 
	 * @return status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * Get the set target number of votes for this feature.
	 * 
	 * @return votestarget
	 */
	public int getVotestarget() {
		return votestarget;
	}

	/**
	 * Votes casted so far. Not realtime. Updated periodically by developer
	 * manually.
	 * 
	 * @return votescasted
	 */
	public int getVotescasted() {
		return votescasted;
	}

	/**
	 * Description of the feature
	 * 
	 * @return description
	 */
	public String getDescription() {
		return description;
	}

}
