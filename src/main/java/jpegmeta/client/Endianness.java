package jpegmeta.client;

import java.util.Map;

import com.google.common.collect.Maps;

public enum Endianness {
    /* Trivia: This 'I' is for Intel, the 'M' is for Motorola */
	BIG("MM"),
	LITTLE("II");
	private static final Map<String, Endianness> MAP = Maps.newHashMap();
	static {
		for (Endianness endian : values()) {
			MAP.put(endian.key.toLowerCase().trim(), endian);
		}
	}
	private final String key;
	
	Endianness(String key) {
		this.key = key;
	}
	
	public static Endianness fromKey(String key) {
		return MAP.get(key.toLowerCase().trim());
	}
}
