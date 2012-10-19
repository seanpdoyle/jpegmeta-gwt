package jpegmeta.client;

public enum JfifTags implements MetaProperty {
	VERSION_MAJOR("Version Major"),
	VERSION_MINOR("Version Minor"),
	VERSION("JFIF Version"), 
	UNITS("Density Units"),
	X_DENSITY("X Density"),
	Y_DENSITY("Y Density"),
	X_THUMBNAIL("X Thumbnail"),
	Y_THUMBNAIL("Y Thumbnail");
	
	private final String description;
	
	JfifTags(String description) {
		this.description = description;
	}
	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public String getFieldName() {
		return this.toString();
	}

	@Override
	public int getKey() {
		return ordinal();
	}

}
