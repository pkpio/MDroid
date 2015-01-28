package in.co.praveenkumar.mdroid.model;

import com.orm.SugarRecord;

public class MoodleFunction extends SugarRecord<MoodleFunction> {
	private String name;
	private String version;

	/**
	 * function name
	 */
	public String getName() {
		return name;
	}

	/**
	 * The version number of the component to which the function belongs
	 */
	public String getVersion() {
		return version;
	}

}
