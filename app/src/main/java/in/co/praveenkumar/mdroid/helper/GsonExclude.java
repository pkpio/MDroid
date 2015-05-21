package in.co.praveenkumar.mdroid.helper;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;
import com.google.gson.annotations.SerializedName;

/**
 * Why?
 * Because some of the field names in a model may conflict with those from Sugar class. 
 * Simply adding a serialize command to those fields will throw exception listed below.
 * 
 * So, we add serializedName to all fields in such models and decode only those with 
 * serializedName using this Gson exclusion strategy. 
 * 
 * Exception: http://stackoverflow.com/questions/19315431/gson-tostring-gives-error-illegalargumentexception-multiple-json-fields-name
 */

/**
 * This ignores all the fields without a serializeName from decoding
 * 
 * @author Praveen Kumar Pendyala (praveen@praveenkumar.co.in)
 * 
 */
public class GsonExclude implements ExclusionStrategy {

	@Override
	public boolean shouldSkipClass(Class<?> arg0) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean shouldSkipField(FieldAttributes field) {
		SerializedName ns = field.getAnnotation(SerializedName.class);
		if (ns != null)
			return false;
		return true;
	}

}
