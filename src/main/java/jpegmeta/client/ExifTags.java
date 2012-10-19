package jpegmeta.client;

import java.util.Map;

import com.google.common.collect.Maps;

public enum ExifTags implements MetaProperty {
	EXIF_VERSION("ExifVersion", "Exif Version", 36864),
	FLASHPIX_VERSION("FlashpixVersion", "FlashPix Version", 40960),

    /* B. Tag Relating to Image Data Characteristics */
    COLOR_SPACE("ColorSpace", "Color Space", 40961),
    /* C. Tags Relating to Image Configuration */
    COMPONENT_CONFIGURATION("ComponentsConfiguration", "Meaning of each component", 37121),
    COMPRESSED_BITS_PER_PIXEL("CompressedBitsPerPixel", "Compressed Bits Per Pixel", 37122),
    PIXEL_X_DIMENSION("PixelXDimension", "Pixel X Dimension", 40962),
    PIXEL_Y_DIMENSION("PixelYDimension", "Pixel Y Dimension", 40963),
    /* D. Tags Relating to User Information */
    MARKER_NOTE("MakerNote", "Manufacturer notes", 37500),
    USER_COMMENT("UserComment", "User comments", 37510),
    /* E. Tag Relating to Related File Information */
    RELATED_SOUND_FILE("RelatedSoundFile", "Related audio file", 40964),
    /* F. Tags Relating to Date and Time */
    DATE_TIME_ORIGINAL("DateTimeOriginal", "Date Time Original", 36867),
    DATE_TIME_DIGITIZED("DateTimeDigitized", "Date Time Digitized", 36868),
    SUB_SEC_TIME("SubSecTime", "DateTime subseconds", 37520),
    SUB_SEC_TIME_ORIGINAL("SubSecTimeOriginal", "DateTimeOriginal subseconds", 37521),
    SUB_SEC_TIME_DIGITIZED("SubSecTimeDigitized", "DateTimeDigitized subseconds", 37522),
    /* G. Tags Relating to Picture-Taking Conditions */
    EXPOSURE_TIME("ExposureTime", "Exposure Time", 33434),
    F_NUMBER("FNumber", "FNumber", 33437),
    EXPOSURE_PROGRAM("ExposureProgram", "Exposure Program", 34850),
    SPECTRAL_SENSITIVITY("SpectralSensitivity", "Spectral Sensitivity", 34852),
    ISO_SPEED_RATINGS("ISOSpeedRatings", "ISO Speed Ratings", 34855),
    OECF("OECF", "Optoelectric coefficient", 34856),
    SHUTTER_SPEED_VALUE("ShutterSpeedValue", "Shutter Speed", 37377),
    APERTURE_VALUE("ApertureValue", "Aperture Value", 37378),
    BRIGHTNESS_VALUE("BrightnessValue", "Brightness", 37379),
    EXPOSURE_BIAS_VALUE("ExposureBiasValue", "Exposure Bias Value", 37380),
    MAX_APERTURE_VALUE("MaxApertureValue", "Max Aperture Value", 37381),
    SUBJECT_DISTANCE("SubjectDistance", "Subject Distance", 37382),
    METERING_MODE("MeteringMode", "Metering Mode", 37383),
    LIGHT_SOURCE("LightSource", "Light Source", 37384),
    FLASH("Flash", "Flash", 37385),
    FOCAL_LENGTH("FocalLength", "Focal Length", 37386),
    SUBJECT_AREA("SubjectArea", "Subject Area", 37396),
    FLASH_ENERGY("FlashEnergy", "Flash Energy", 41483),
    SPATIAL_FREQUENCY_RESPONSE("SpatialFrequencyResponse", "Spatial Frequency Response", 41484),
    FOCAL_PLANE_X_RESOLUTION("FocalPlaneXResolution", "Focal Plane X Resolution", 41486),
    FOCAL_PLANE_Y_RESOLUTION("FocalPlaneYResolution", "Focal Plane X Resolution", 41487),
    FOCAL_PLANE_RESOLUTION_UNIT("FocalPlaneResolutionUnit", "Focal Plane Resolution Unit", 41488),
    SUBJECT_LOCATION("SubjectLocation", "Subject Location", 41492),
    EXPOSURE_INDEX("ExposureIndex", "Exposure Index", 41493),
    SENSING_METHOD("SensingMethod", "Sensing Method", 41495),
    FILE_SOURCE("FileSource", "File Source", 41728),
    SCENE_TYPE("SceneType", "Scene Type", 41729),
    CFA_PATTERN("CFAPattern", "CFA Pattern", 41730),
    CUSTOM_RENDERED("CustomRendered", "Custom Rendered", 41985),
    EXPOSURE_MODE("Exposure Mode", "Exposure Mode", 41986),
    WHITE_BALANCE("WhiteBalance", "White Balance", 41987),
    DIGITAL_ZOOM_RATIO("DigitalZoomRatio", "Digital Zoom Ratio", 41988),
    SCENE_CAPTURE_TYPE("SceneCaptureType", "Scene Capture Type", 41990),
    GAIN_CONTROL("GainControl", "Gain Control", 41991),
    CONTRAST("Contrast", "Contrast", 41992),
    SATURATION("Saturation", "Saturation", 41993),
    SHARPNESS("Sharpness", "Sharpness", 41994),
    DEVICE_SETTING_DESCRIPTION("DeviceSettingDescription", "Device settings description", 41995),
    SUBJECT_DISTANCE_RANGE("SubjectDistanceRange", "Subject distance range", 41996);

	private final String fieldName;
	private final String description;
	private final int key;
	
	private static final Map<Integer, ExifTags> MAP = Maps.newHashMap();
	static
	{
		for (ExifTags tag : ExifTags.values())
		{
			MAP.put(tag.getKey(), tag);
		}
	}
	
	public static ExifTags fromKey(int keyCode)
	{
		return MAP.get(keyCode);
	}

	ExifTags(String fieldName, String description, int key) {
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
