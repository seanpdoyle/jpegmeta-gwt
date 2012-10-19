package jpegmeta.client;

import java.util.Map;

import com.google.common.collect.Maps;


public enum GpsTags implements MetaProperty {
	VERSION_ID("GPSVersionID", "GPS tag version", 0),
	LATITUDE_REF("GPSLatitudeRef", "North or South Latitude", 1),
	LATITUDE("GPSLatitude", "Latitude", 2),
	LONGITUDE_REF("GPSLongitudeRef", "East or West Longitude", 3),
	LONGITUDE("GPSLongitude", "Longitude", 4),
	ALTITUDE_REF("GPSAltitudeRef", "Altitude reference", 5),
	ALTITUDE("GPSAltitude", "Altitude", 6),
	TIME_STAMP("GPSTimeStamp", "GPS time (atomic clock)", 7),
	SATELLITES("GPSSatellites", "GPS satellites usedd for measurement", 8),
	STATUS("GPSStatus", "GPS receiver status", 9),
	MEASURE_MODE("GPSMeasureMode", "GPS mesaurement mode", 10),
	DOP("GPSDOP", "Measurement precision", 11),
	SPEED_REF("GPSSpeedRef", "Speed unit", 12),
	SPEED("GPSSpeed", "Speed of GPS receiver", 13),
	TRACK_REF("GPSTrackRef", "Reference for direction of movement", 14),
	TRACK("GPSTrack", "Direction of movement", 15),
	IMG_DIRECTION_REF("GPSImgDirectionRef", "Reference for direction of image", 16),
	IMG_DIRECTION("GPSImgDirection", "Direction of image", 17),
	MAP_DATUM("GPSMapDatum", "Geodetic survey data used", 18),
	DEST_LATITUDE_REF("GPSDestLatitudeRef", "Reference for latitude of destination", 19),
	DEST_LATITUDE("GPSDestLatitude", "Latitude of destination", 20),
	DEST_LONGITUDE_REF("GPSDestLongitudeRef", "Reference for longitude of destination", 21),
	DEST_LONGITUDE("GPSDestLongitude", "Longitude of destination", 22),
	DEST_BEARING_REF("GPSDestBearingRef", "Reference for bearing of destination", 23),
	DEST_BEARING("GPSDestBearing", "Bearing of destination", 24),
	DEST_DISTANCE_REF("GPSDestDistanceRef", "Reference for distance to destination", 25),
	DEST_DISTANCE("GPSDestDistance", "Distance to destination", 26),
	PROCESSING_METHOD("GPSProcessingMethod", "Name of GPS processing method", 27),
	AREA_INFORMATION("GPSAreaInformation", "Name of GPS area", 28),
	DATE_STAMP("GPSDateStamp", "GPS Date", 29),
	DIFFERENTIAL("GPSDifferential", "GPS differential correction", 30),
	
	//Custom
	DECIMAL_LATITUDE("DecimalLatitude", "Latitude in decimal form", -1),
	DECIMAL_LONGITUDE("DecimalLongitude", "Longitude in decimal form", -2);

	private final String fieldName;
	private final String description;
	private final int key;
	
	private static final Map<Integer, GpsTags> MAP = Maps.newHashMap();
	static
	{
		for (GpsTags tag : GpsTags.values())
		{
			MAP.put(tag.getKey(), tag);
		}
	}
	
	public static GpsTags fromKey(int keyCode)
	{
		return MAP.get(keyCode);
	}

	GpsTags(String fieldName, String description, int key) {
		this.fieldName = fieldName;
		this.description = description;
		this.key = key;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public String getFieldName() {
		return this.fieldName;
	}

	@Override
	public int getKey() {
		return this.key;
	}

}
