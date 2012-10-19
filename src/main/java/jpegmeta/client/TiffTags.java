package jpegmeta.client;

import java.util.Map;

import com.google.common.collect.Maps;

public enum TiffTags implements MetaProperty
{
	IMAGE_WIDTH("ImageWidth", "Image Width", 256),
	IMAGE_LENGTH("ImageLength", "Image height", 257),
	BITS_PER_SAMPLE( "BitsPerSample", "Number of bits per component", 258),
	//COMPRESSION_SCHEME
	//PIXEL_COMPOSITION
	//ORIENTATION
	SAMPLES_PER_PIXEL("SamplesPerPixel", "Number of Components", 277),
	//PLANAR_CONFIGURATION
	YCB_CR_SUB_SAMPLING("YCbCrSubSampling", "Subsampling Ratio of Y to C", 530),
	//YCB_CR_POSITIONING
	X_RESOLUTION("XResolution", "X Resolution", 283),
	Y_RESOLUTION("YResolution", "Y Resolution", 282),
	//RESOLUTION_UNIT
    /* B. Tags realting to recording offset */
	STRIP_OFFSETS("StripOffsets", "Image data location", 273),
	ROWS_PER_STRIP("RowsPerStrip", "Number of rows per strip", 278),
	STRIP_BYTE_COUNTS("StripByteCounts", "Bytes per compressed strip",  279),
	JPEG_INTERCHANGE_FORMAT("JPEGInterchangeFormat", "Offset to JPEG SOI", 513),
	JPEG_INTERCHANGE_FORMAT_LENGTH("JPEGInterchangeFormatLength", "Bytes of JPEG Data", 514),
	
    /* C. Tags relating to image data characteristics */
	TRANSFER_FUNCTION("TransferFunction", "Transfer function", 301),
	WHITE_POINT("WhitePoint", "White Point Chromaticity", 318),
	PRIMARY_CHROMATICITIES("PrimaryChromaticites", "Chromaticities of Primary Colors", 319),
	YCB_CR_COEFFICIENTS("YCbCrCoefficients", "Color space transformation matrix coefficients", 529),
	REFERENCE_BLACK_WHITE("ReferenceBlackWhite", "Pair of black and white reference values", 532),
    
    /* D. Other tags */
	DATE_TIME("DateTime", "Date and Time", 306),
	IMAGE_DESCRIPTION("ImageDescription", "Image Title", 270),
	MAKE("Make", "Camera Make", 271),
	MODEL("Model", "Camera Model", 272),
	SOFTWARE("Software", "Camera Software", 305),
	ARTIST("Artist", "Person who created the image", 315),
	HOST_COMPUTER("HostComputer", "Host Computer", 316),
	COPYRIGHT("Copyright", "Copyright holder", 33432),
    EXIF_IFD_POINTER("ExifIfdPointer", "Exif ImageFileDirectory pointer tag", 34665),
    GPS_INFO_IFD_POINTER("GPSInfoIfdPointer", "GPS ImageFileDirectory pointer tag", 34853);

//    259 : ["Compression scheme", "Compression", {1 : "uncompressed", 6 : "JPEG compression" }],
//    262 : ["Pixel composition", "PhotmetricInerpretation", {2 : "RGB", 6 : "YCbCr"}],
	   /* FIXME: Check the mirror-image / reverse encoding and rotation */
//    274 : ["Orientation of image", "Orientation", {1 : "Normal", 2 : "Reverse?", 3 : "Upside-down", 4 : "Upside-down Reverse", 5 : "90 degree CW", 6 : "90 degree CW reverse", 7 : "90 degree CCW", 8 : "90 degree CCW reverse",}],
//    284 : ["Image data arrangement", "PlanarConfiguration", {1 : "chunky format", 2 : "planar format"}],
//    531 : ["Y and C positioning", "YCbCrPositioning", {1 : "centered", 2 : "co-sited"}],
//    296 : ["Resolution Unit", "ResolutionUnit", {2 : "inches", 3 : "centimeters"}],

	private final String fieldName;
	private final String description;
	private final int key;
	
	private static final Map<Integer, TiffTags> MAP = Maps.newHashMap();
	static
	{
		for (TiffTags tag : TiffTags.values())
		{
			MAP.put(tag.getKey(), tag);
		}
	}
	
	public static TiffTags fromKey(int keyCode)
	{
		return MAP.get(keyCode);
	}
	
	TiffTags(String fieldName, String description, int key)
	{
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
