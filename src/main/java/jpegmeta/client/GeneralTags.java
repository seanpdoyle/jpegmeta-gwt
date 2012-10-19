package jpegmeta.client;

public enum GeneralTags implements MetaProperty {
	DEPTH("Depth"),
	PIXEL_HEIGHT("Pixel Height"),
	PIXEL_WIDTH("Pixel Width"),
	TYPE("Type");
	
	private final String description;
	
	GeneralTags(String description) {
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
