package ca.ualberta.cs.providers;

import com.google.gson.Gson;

public class GeoChanGsonOffline extends GeoChanGson {
	protected static GeoChanGsonOffline singleton = null;

	/**
	 * Get the gson object
	 * 
	 * @return the GeoChanGsonOffline object
	 */
	public static Gson getGson() {
		if (singleton == null) {
			singleton = new GeoChanGsonOffline();
		}
		return singleton.gson;
	}

}
