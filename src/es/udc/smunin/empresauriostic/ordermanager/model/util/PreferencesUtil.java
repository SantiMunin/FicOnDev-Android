package es.udc.smunin.empresauriostic.ordermanager.model.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import android.util.Log;
import es.udc.smunin.empresauriostic.ordermanager.model.objectmodels.LoginInfo;

/**
 * Provides a simple way to edit application's preferences. Naive
 * implementation, should be improved.
 * 
 * @author Santiago Mun’n <burning1@gmail.com>
 * 
 */
public class PreferencesUtil {
	public static SharedPreferences prefs;
	private final static String TAG = "Preferences";
	private final static String SESSION_ID = "sessionId";
	private final static String PHONE = "phone";
	private final static String MAIL = "mail";
	private final static String ID_STORY = "id_story";
	private final static String CURRENT_CHECKPOINT = "current_checkpoint";

	public static void setLoginInfo(Context context, LoginInfo loginInfo) {
		setSessionId(context, loginInfo.getSessionId());
		setMail(context, loginInfo.getEmail());
	}

	public static void deleteLoginInfo(Context context, LoginInfo loginInfo) {
		setSessionId(context, "");
		setMail(context, "");
	}

	public static void setSessionId(Context context, String sessionId) {
		putString(context, SESSION_ID, sessionId);
	}

	public static String getSessionId(Context context) {
		return getString(context, SESSION_ID);
	}

	public static void setPassword(Context context, String sessionId) {
		putString(context, "password", sessionId);
	}

	public static String getPassword(Context context) {
		return getString(context, "password");
	}

	public static void setMail(Context context, String mail) {
		putString(context, MAIL, mail);
	}

	public static String getMail(Context context) {
		return getString(context, MAIL);
	}

	public static void saveStory(Context context, long storyId, int checkpoint) {
		putInteger(context, ID_STORY, storyId);
		putInteger(context, CURRENT_CHECKPOINT, checkpoint);
	}

	public static long getCurrentStory(Context context) {
		return getInteger(context, ID_STORY);
	}

	public static int getCurrentCheckpoint(Context context) {
		return (int) getInteger(context, CURRENT_CHECKPOINT);
	}

	public static void removeStory(Context context, long storyId, int checkpoint) {
		putString(context, ID_STORY, "");
		putString(context, CURRENT_CHECKPOINT, "");
	}

	private static void putInteger(Context context, String key, long value) {
		Log.i(TAG, "Saving pair: (" + key + "," + value + ").");
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putLong(key, value);
		editor.commit();
	}

	private static long getInteger(Context context, String key) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		long result = prefs.getLong(key, 0);
		Log.i(TAG, "Obtained pair (" + key + "," + result + ").");
		return result;
	}

	// TODO refactor to allow multiple pairs
	private static void putString(Context context, String key, String value) {
		Log.i(TAG, "Saving pair: (" + key + "," + value + ").");
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		Editor editor = prefs.edit();
		editor.putString(key, value);
		editor.commit();
	}

	private static String getString(Context context, String key) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		String result = prefs.getString(key, "");
		Log.i(TAG, "Obtained pair (" + key + "," + result + ").");
		return result;
	}

	public static void setTime(Context context, long time) {
		Log.d(TAG, "Updatig time" + time);
		putInteger(context, "time", time);
	}

	public static long getTime(Context context) {
		prefs = PreferenceManager.getDefaultSharedPreferences(context);
		long result = prefs.getLong("time", 0);
		return result;
	}

}
