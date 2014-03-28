package customlized_gson;

import android.graphics.Bitmap;
import android.location.Location;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * A class able to Constructs a Gson with a custom serializer / deserializer registered for Bitmap/Location.
 * getted from https://github.com/zjullion/PicPosterComplete/blob/master/src/ca/ualberta/cs/picposter/network/ElasticSearchOperations.java
 */
public class GsonConstructor {
	/**
	 * Construct a Gson_Constructor object.
	 */
	public GsonConstructor(){}
	/**
	 * @return a Gson object with a custom serializer / desserializer registered for Bitmaps.
	 */
	public Gson getGson(){
		GsonBuilder builder = new GsonBuilder();
		builder.registerTypeAdapter(Bitmap.class,new BitmapConverter());
		builder.registerTypeAdapter(Location.class,new LocationConverter());
		return builder.create();
	}
}
