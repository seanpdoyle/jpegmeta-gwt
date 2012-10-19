package jpegmeta.client;

import java.util.Map;

import com.google.common.collect.Maps;


public enum Markers implements MetaProperty {
    /* Start Of Frame markers, non-differential, Huffman coding */
	SOF0("SOF0", "Baseline DCT", 0xc0),
	SOF1("SOF1", "Extended sequential DCT", 0xc1),
	SOF2("SOF2", "Progressive DCT", 0xc2),
	SOF3("SOF3", "Lossless (sequential)", 0xc3),

    /* Start Of Frame markers, differential, Huffman coding */
	SOF5("SOF5", "Differential sequential DCT", 0xc5),
	SOF6("SOF6", "Differential progressive DCT", 0xc6),
	SOF7("SOF7", "Differential lossless (sequential)", 0xc7),
    
    /* Start Of Frame markers, non-differential, arithmetic coding */
	JPG("JPG","Reserved for JPEG extensions", 0xc8),
	SOF9("SOF9", "Extended sequential DCT", 0xc9),
	SOF10("SOF10", "Progressive DCT", 0xca),
	SOF11("SOF11", "Lossless (sequential)", 0xcb),
    
    /* Start Of Frame markers, differential, arithmetic coding */
	SOF13("SOF13", "Differential sequential DCT", 0xcd),
	SOF14("SOF14", "Differential progressive DCT", 0xce),
	SOF15("SOF15", "Differential lossless (sequential)", 0xcf),
    
    /* Huffman table specification */
    DHT("DHT", "Define Huffman table(s)",0xc4),
    DAC("DAC", "Define arithmetic coding conditioning(s)", 0xcc),
    
    /* Restart interval termination" */
    RST0("RST0", "Restart with modulo 8 count “0”",0xd0),
    RST1("RST1", "Restart with modulo 8 count “1”", 0xd1),
    RST2("RST2", "Restart with modulo 8 count “2”", 0xd2),
    RST3("RST3", "Restart with modulo 8 count “3”", 0xd3),
    RST4("RST4", "Restart with modulo 8 count “4”", 0xd4),
    RST5("RST5", "Restart with modulo 8 count “5”", 0xd5),
    RST6("RST6", "Restart with modulo 8 count “6”", 0xd6),
    RST7("RST7", "Restart with modulo 8 count “7”", 0xd7),
    
    /* Other markers */
    SOI("SOI", "Start of image", 0xd8),
    EOI("EOI", "End of image", 0xd9),
    SOS("SOS", "Start of scan", 0xda),
    DQT("DQT", "Define quantization table(s)", 0xdb),
    DNL("DNL", "Define number of lines", 0xdc),
    DRI("DRI", "Define restart interval", 0xdd),
    DHP("DHP", "Define hierarchical progression", 0xde),
    EXP("EXP", "Expand reference component(s)", 0xdf),
    APP0("APP0", "Reserved for application segments", 0xe0),
    APP1("APP1", 0xE1),
    APP2("APP2", 0xE2),
    APP3("APP3", 0xE3),
    APP4("APP4", 0xE4),
    APP5("APP5", 0xE5),
    APP6("APP6", 0xE6),
    APP7("APP7", 0xE7),
    APP8("APP8", 0xE8),
    APP9("APP9", 0xE9),
    APP10("APP10", 0xEE),
    APP11("APP11", 0xEB),
    APP12("APP12", 0xEC),
    APP13("APP13", 0xED),
    APP14("APP14", 0xEE),
    APP15("APP15", 0xEF),
    JPG0("JPG0", 0xf0), /* Reserved for JPEG extensions */
    JPG1("JPG1", 0xf1),
    JPG2("JPG2", 0xf2),
    JPG3("JPG3", 0xf3),
    JPG4("JPG4", 0xf4),
    JPG5("JPG5", 0xf5),
    JPG6("JPG6", 0xf6),
    JPG7("JPG7", 0xf7),
    JPG8("JPG8", 0xf8),
    JPG9("JPG9", 0xf9),
    JPG10("JPG10", 0xfa),
    JPG11("JPG11", 0xfb),
    JPG12("JPG12", 0xfc),
    JPG13("JPG13", 0xfd),
    COM("COM", 0xfe);

    private final String fieldName;
    private final String description;
	private final int key;
	
	private static final Map<Integer, Markers> MAP = Maps.newHashMap();
	static
	{
		for (Markers marker : Markers.values())
		{
			MAP.put(marker.getKey(), marker);
		}
	}
	
	public static Markers fromKey(int keyCode)
	{
		return MAP.get(keyCode);
	}
    
	Markers(String fieldName, int key) {
		this(fieldName, "", key);
	}
	
	Markers(String fieldName, String description, int key) {
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
