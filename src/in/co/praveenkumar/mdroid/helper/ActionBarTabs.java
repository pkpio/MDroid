package in.co.praveenkumar.mdroid.helper;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 
 * Code courtesy: http://stackoverflow.com/a/22528277/925767
 * 
 * @author Praveen Kumar Pendyala
 * 
 */
public class ActionBarTabs {

	/**
	 * Set if the tabs have to embedded in the action bar when the device enters
	 * landscape mode.
	 * 
	 * @param inActionBar
	 *            ActionBar object
	 * @param inHasEmbeddedTabs
	 *            true: embedded in ActionBar
	 */
	public static void setHasEmbeddedTabs(Object inActionBar,
			final boolean inHasEmbeddedTabs) {
		// get the ActionBar class
		Class<?> actionBarClass = inActionBar.getClass();

		// if it is a Jelly Bean implementation (ActionBarImplJB), get the super
		// class (ActionBarImplICS)
		if ("android.support.v7.app.ActionBarImplJB".equals(actionBarClass
				.getName())) {
			actionBarClass = actionBarClass.getSuperclass();
		}

		try {
			// try to get the mActionBar field, because the current ActionBar is
			// probably just a wrapper Class
			// if this fails, no worries, this will be an instance of the native
			// ActionBar class or from the ActionBarImplBase class
			final Field actionBarField = actionBarClass
					.getDeclaredField("mActionBar");
			actionBarField.setAccessible(true);
			inActionBar = actionBarField.get(inActionBar);
			actionBarClass = inActionBar.getClass();
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		} catch (NoSuchFieldException e) {
		}

		try {
			final Method method = actionBarClass.getDeclaredMethod(
					"setHasEmbeddedTabs", new Class[] { Boolean.TYPE });
			method.setAccessible(true);
			method.invoke(inActionBar, new Object[] { inHasEmbeddedTabs });
		} catch (NoSuchMethodException e) {
		} catch (InvocationTargetException e) {
		} catch (IllegalAccessException e) {
		} catch (IllegalArgumentException e) {
		}
	}

}
