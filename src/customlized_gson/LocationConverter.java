package customlized_gson;

import java.lang.reflect.Type;

import android.location.Location;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

/**
 * Provides custom serialization / deserialization for Location
 * Adapted from http://stackoverflow.com/questions/13944346/runtimeexception-in-gson-parsing-json-failed-to-invoke-protected-java-lang-class
 * @author xuping
 */
public class LocationConverter implements JsonSerializer<Location>,JsonDeserializer<Location>{
	/**
	 * Encode the Location.
	 */
	@Override
	public JsonElement serialize(Location t,Type type,JsonSerializationContext jsc){
		JsonObject jo = new JsonObject();
		jo.addProperty("mProvider", t.getProvider());
		jo.addProperty("mAccuracy", t.getAccuracy());
		jo.addProperty("mAltitude",t.getAltitude());
		jo.addProperty("mLatitude",t.getLatitude());
		jo.addProperty("mLongitude",t.getLongitude());
		return jo;
	}

	/**
	 * Decode the Location.
	 */
	@Override
	public Location deserialize(JsonElement je, Type type, JsonDeserializationContext jdc) throws JsonParseException{
		JsonObject jo = je.getAsJsonObject();
		Location l = new Location(jo.getAsJsonPrimitive("mProvider").getAsString());
		l.setAccuracy(jo.getAsJsonPrimitive("mAccuracy").getAsFloat());
		l.setAltitude(jo.getAsJsonPrimitive("mAltitude").getAsDouble());
		l.setLongitude(jo.getAsJsonPrimitive("mLongitude").getAsDouble());
		l.setLatitude(jo.getAsJsonPrimitive("mLatitude").getAsDouble());
		return l;
	}

}
