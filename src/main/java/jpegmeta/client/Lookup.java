package jpegmeta.client;

import java.util.Map;

import com.google.common.collect.Maps;

public enum Lookup implements PropertyLookup {
	/**
	 * TiffTags lookup
	 */
	TIFF(TiffTags.values()),
	/**
	 * GpsTags lookup
	 */
	GPS(GpsTags.values()),
	/**
	 * ExifTags lookup
	 */
	EXIF(ExifTags.values());
	
	private final Map<Integer, MetaProperty> lookup = Maps.newHashMap();
	
	Lookup(MetaProperty... metaProperties) {
		for (MetaProperty prop : metaProperties) {
			this.lookup.put(prop.getKey(), prop);
		}
	}

	@Override
	public MetaProperty getByKeyCode(int keyCode) {
		return this.lookup.get(keyCode);
	}
}
