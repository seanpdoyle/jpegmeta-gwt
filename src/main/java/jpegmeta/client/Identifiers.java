package jpegmeta.client;

import com.google.common.base.Strings;

public enum Identifiers {
	JFIF("JFIF"),
	JFXX("JFXX"),

	/* EXIF idents */
	EXIF("EXIF");
	
	private final String value;
	
	private Identifiers(String value)
	{
		this.value = value;
	}
	
	public boolean matches(String value) {
		return this.value.toLowerCase().trim().equals(Strings.nullToEmpty(value).toLowerCase().trim());
	}
	
	public String getValue() {
		return this.value;
	}
}
