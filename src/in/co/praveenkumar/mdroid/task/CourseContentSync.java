package in.co.praveenkumar.mdroid.task;

public class CourseContentSync {
	String mUrl;
	String token;
	int courseid;
	long siteid;
	String error;

	public CourseContentSync(String mUrl, String token, int courseid,
			long siteid) {
		this.mUrl = mUrl;
		this.token = token;
		this.courseid = courseid;
		this.siteid = siteid;
	}

}
