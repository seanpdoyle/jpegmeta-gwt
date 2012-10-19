package jpegmeta.client;

import java.util.List;
import java.util.Map;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.Objects;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class JpegMeta {
	private enum MainGroups {
		GENERAL, EXIF, JFIF, TIFF, GPS
	}

	private final Map<MainGroups, MetaGroup> groups = Maps.newEnumMap(MainGroups.class);

	boolean hasReadGroup(MainGroups group) {
		return group != null && this.groups.containsKey(group);
	}

	boolean hasReadProperty(MainGroups group, MetaProperty property) {
		return group != null && property != null && this.groups.get(group).contains(property);
	}

	static int unsigned(byte data) {
		return data & 0xFF;
	}

	public Metadata readMetadata(byte[] data) throws ImageParsingException {
		int position = 0;
		/* Check to see if this looks like a JPEG file */
		int[] magicNumberBytes = new int[] { unsigned(data[0]), unsigned(data[1]) };
		if (magicNumberBytes[0] != JpegConstants.DELIM.getValue()
				&& magicNumberBytes[1] != JpegConstants.SOI.getValue()) {
			throw new ImageParsingException("Doesn't look like a JPEG file. First two bytes are "
					+ magicNumberBytes[0] + "," + magicNumberBytes[1] + ".", 2);
		}

		position += 2;

		while (position < data.length) {
			int delimiter = unsigned(data[position++]);
			int mark = unsigned(data[position++]);

			int pos_start_of_segment = position;

			if (delimiter != JpegConstants.DELIM.getValue() || mark == JpegConstants.SOS.getValue()) {
				break;
			}

			int headerSize = parseNumber(Endianness.BIG, data, position, 2);

			/* Find the end */
			position += headerSize;
			while (position < data.length) {
				delimiter = unsigned(data[position++]);
				if (delimiter == JpegConstants.DELIM.getValue()) {
					int _mark = unsigned(data[position++]);
					if (_mark != 0x0) {
						position -= 2;
						break;
					}
				}
			}

			int segmentSize = position - pos_start_of_segment;
			Markers marker = Markers.fromKey(mark);
			switch (marker) {
				case SOF0:
				case SOF1:
				case SOF2:
				case SOF3:
				case SOF5:
				case SOF6:
				case SOF7:
				case SOF9:
				case SOF10:
				case SOF11:
				case SOF13:
				case SOF14:
				case SOF15:
					// run sofHandler
					sofHandler(data, marker, pos_start_of_segment + 2);
					break;
				case APP0:
					// run app0Handler
					app0Handler(data, marker, pos_start_of_segment + 2);
					break;
				case APP1:
					// run app1Handler
					app1Handler(data, marker, pos_start_of_segment + 2);
				default:
					break;
			}
		}

		if (!hasReadGroup(MainGroups.GENERAL)) {
			throw new ImageParsingException("Invalid JPEG file.", position);
		}

		return new Metadata(groups.values());
	}

	MetaGroup parseImageFileDirectory(Endianness endian, byte[] data, int base,
			int imageFileDirOffset, PropertyLookup lookup, String name, String description)
			throws ImageParsingException {
		Preconditions.checkNotNull(lookup, "Cannot parse IFD with null lookup!");
		int num_fields = parseNumber(endian, data, base + imageFileDirOffset, 2);

		MetaGroup group = new MetaGroup(name, description);
		for (int i = 0; i < num_fields; i++) {
			Object value = null;
			/* parse the field */
			int tag_base = base + imageFileDirOffset + 2 + (i * 12);
			int tag_field = parseNumber(endian, data, tag_base, 2);
			int type_field = parseNumber(endian, data, tag_base + 2, 2); // TODO
																			// returned
																			// a
																			// Float
																			// for
																			// SUB
																			// IFD
																			// pointer
			int num_values = parseNumber(endian, data, tag_base + 4, 4);
			int value_offset = parseNumber(endian, data, tag_base + 8, 4);
			TiffTypes type = TiffTypes.fromKey(type_field);
			if (type != null) {
				if (type.getSizeInBytes() * num_values <= 4) {
					/* Data is in-line */
					value_offset = tag_base + 8;
				} else {
					value_offset = base + value_offset;
				}

				/* Read the value */
				switch (type) {
					case UNDEFINED:
						value = slice(data, value_offset, value_offset + num_values);
						break;
					case ASCII:
						/* strip trail nul/ */
						String slice = slice(data, value_offset, value_offset + num_values);
						value = slice.substring(0, slice.indexOf((char) 0x0));
						break;
					default:
						List<Number> numbers = Lists.newArrayList();
						for (int j = 0; j < num_values; j++, value_offset += type.getSizeInBytes()) {
							switch (type) {
								case BYTE:
								case SHORT:
								case LONG:
									numbers.add(parseNumber(endian, data, value_offset,
											type.getSizeInBytes()));
									break;
								case SBYTE:
								case SSHORT:
								case SLONG:
									numbers.add(parseSignedNumber(endian, data, value_offset,
											type.getSizeInBytes()));
									break;
								case RATIONAL:
									int num = parseNumber(endian, data, value_offset, 4);
									int den = parseNumber(endian, data, value_offset + 4, 4);
									numbers.add(num / (double) den);
									break;
								case SRATIONAL:
									int snum = parseSignedNumber(endian, data, value_offset, 4);
									int sden = parseSignedNumber(endian, data, value_offset + 4, 4);
									numbers.add(snum / (double) sden);
									break;
								case FLOAT:
									// TODO rewrite int bits to float (in
									// image-metadata-gwt)
									int intBits = parseNumber(endian, data, value_offset,
											type.getSizeInBytes());
									numbers.add(intBitsToFloat(intBits));
									break;
								case DOUBLE:
									// TODO rewrite int bits to float (in
									// image-metadata-gwt)
									int longBits = parseNumber(endian, data, value_offset,
											type.getSizeInBytes());
									numbers.add(longBitsToDouble(longBits));
									break;
								default:
									break;
							}
						}
						if (num_values == 1 && !numbers.isEmpty()) {
							value = numbers.get(0);
						} else {
							value = numbers;
						}
				}
				MetaProperty prop = lookup.getByKeyCode(tag_field);
				if (prop != null) {
					group.addProperty(prop, value);
				}
			}
		}
		return group;
	}

	float intBitsToFloat(int bits) {
		float value;
		if (bits == 0x7f800000) {
			value = Float.POSITIVE_INFINITY;
		} else if (bits == 0xff800000) {
			value = Float.NEGATIVE_INFINITY;
		} else if ((0x7fffffff >= bits && bits >= 0x7f800001)
				|| (0xffffffff >= bits && bits >= 0xff800001)) {
			value = Float.NaN;
		} else {
			int sign = ((bits >> 31) == 0) ? 1 : -1;
			int exponent = ((bits >> 23) & 0xFF);
			int mantissa = (exponent == 0) ? (bits & 0x7fffff) << 1 : (bits & 0x7fffff) | 0x800000;
			value = (float) (sign * mantissa * Math.pow(2, exponent - 150));
		}
		return value;
	}

	double longBitsToDouble(long bits) {
		double value;
		if (bits == 0x7ff0000000000000L) {
			value = Double.POSITIVE_INFINITY;
		} else if (bits == 0xfff0000000000000L) {
			value = Double.NEGATIVE_INFINITY;
		} else if ((0x7fffffffffffffffL <= bits && bits >= 0x7ff0000000000001L)
				|| (0xffffffffffffffffL >= bits && bits >= 0xfff0000000000001L)) {
			value = Double.NaN;
		} else {
			int sign = ((bits >> 63) == 0) ? 1 : -1;
			int exponent = (int) ((bits >> 52) & 0x7ffL);
			long mantissa = (exponent == 0) ? (bits & 0xFFFFFFFFFFFFFL) << 1
					: (bits & 0xfffffffffffffL) | 0x10000000000000L;

			value = sign * mantissa * Math.pow(2, exponent - 1075);
		}
		return value;
	}

	static String slice(byte[] data, int from, int to) {
		Preconditions.checkArgument(from < to, "to slice, from < to");
		StringBuilder builder = new StringBuilder(to - from);
		for (int i = from; i < to; i++) {
			int u = unsigned(data[i]);
			char c = (char) u;
			builder.append(c);
		}
		return builder.toString();
	}

	void app0Handler(byte[] data, Markers marker, int position) throws ImageParsingException {
		String identifier = slice(data, position, position + 5);
		if (Identifiers.JFIF.getValue().equals(identifier)) {
			jfifHandler(data, marker, position);
		} else if (Identifiers.JFXX.getValue().equals(identifier)) {
			/* Don't handle JFXX Ident yet */
		} else {
			/* Don't know about other idents */
		}
	}

	void app1Handler(byte[] data, Markers marker, int position) throws ImageParsingException {
		String identifier = slice(data, position, position + 5);
		if (Identifiers.EXIF.matches(identifier)) {
			exifHandler(data, marker, position + 6);
		} else {
			/* Don't know about other idents */
		}
	}

	void exifHandler(byte[] data, Markers marker, int position) throws ImageParsingException {
		if (this.groups.containsKey(MainGroups.EXIF)) {
			throw new ImageParsingException("Multiple JFIF segments found", position);
		}
		/* Parse this TIFF header */
		String endianField = slice(data, position, position + 2);
		Endianness endian = Endianness.fromKey(endianField);
		if (endian == null) {
			throw new ImageParsingException("Malformed TIFF meta-data. Unknown endianess: "
					+ endianField, position + 2);
		}

		int magic_field = parseNumber(endian, data, position + 2, 2);

		if (magic_field != 42) {
			throw new ImageParsingException("Malformed TIFF meta-data. Bad magic number: "
					+ magic_field, position + 2);
		}

		int imageFileDirOffset = parseNumber(endian, data, position + 4, 4);

		/* Parse 0th ImageFileDirectory */
		MetaGroup tiffGroup = parseImageFileDirectory(endian, data, position, imageFileDirOffset,
				Lookup.TIFF, "tiff", MainGroups.TIFF.toString());
		this.groups.put(MainGroups.TIFF, tiffGroup);

		if (containsExif()) {
			Integer exifIfOffset = this.groups.get(MainGroups.TIFF).getValue(
					TiffTags.EXIF_IFD_POINTER, Integer.class);
			if (exifIfOffset != null) {
				MetaGroup exifGroup = parseImageFileDirectory(endian, data, position, exifIfOffset,
						Lookup.EXIF, "exif", MainGroups.EXIF.toString());
				this.groups.put(MainGroups.EXIF, exifGroup);
			}
		}

		if (containsGps()) {
			Integer gpsIfdOffset = this.groups.get(MainGroups.TIFF).getValue(
					TiffTags.GPS_INFO_IFD_POINTER, Integer.class);
			if (gpsIfdOffset != null) {
				MetaGroup gps = parseImageFileDirectory(endian, data, position, gpsIfdOffset,
						Lookup.GPS, "gps", MainGroups.GPS.toString());
				if (gps.contains(GpsTags.LATITUDE)) {
					double latitude = parseHMS((List<Number>) gps.getValue(GpsTags.LATITUDE,
							List.class));
					String gpsLatRef = gps.getValue(GpsTags.LATITUDE_REF, String.class);
					if (Objects.equal(gpsLatRef, "S")) {
						latitude = -latitude;
					}
					gps.addProperty(GpsTags.DECIMAL_LATITUDE, latitude);
				}
				if (gps.contains(GpsTags.LONGITUDE)) {
					double longitude = parseHMS((List<Number>) gps.getValue(GpsTags.LONGITUDE,
							List.class));
					String gpsLngRef = gps.getValue(GpsTags.LONGITUDE_REF, String.class);
					if (Objects.equal(gpsLngRef, "W")) {
						longitude = -longitude;
					}
					gps.addProperty(GpsTags.DECIMAL_LONGITUDE, longitude);
				}
				this.groups.put(MainGroups.GPS, gps);
			}
		}
	}

	boolean containsGps() {
		return hasReadProperty(MainGroups.TIFF, TiffTags.GPS_INFO_IFD_POINTER)
				&& this.groups.containsKey(MainGroups.TIFF);
	}

	boolean containsExif() {
		return hasReadProperty(MainGroups.TIFF, TiffTags.EXIF_IFD_POINTER)
				&& this.groups.containsKey(MainGroups.EXIF);
	}

	void jfifHandler(byte[] data, Markers marker, int position) throws ImageParsingException {
		if (hasReadGroup(MainGroups.JFIF)) {
			throw new ImageParsingException("Multiple JFIF segments found", position);
		}
		MetaGroup jfif = new MetaGroup("jfif", MainGroups.JFIF.toString());
		int major = unsigned(data[position + 5]);
		int minor = unsigned(data[position + 6]);
		jfif.addProperty(JfifTags.VERSION_MAJOR, major);
		jfif.addProperty(JfifTags.VERSION_MINOR, minor);
		jfif.addProperty(JfifTags.VERSION, major + "." + minor);
		jfif.addProperty(JfifTags.UNITS, unsigned(data[position + 7]));
		jfif.addProperty(JfifTags.X_DENSITY, parseNumber(Endianness.BIG, data, position + 8, 2));
		jfif.addProperty(JfifTags.Y_DENSITY, parseNumber(Endianness.BIG, data, position + 10, 2));
		jfif.addProperty(JfifTags.X_THUMBNAIL, unsigned(data[position + 12]));
		jfif.addProperty(JfifTags.Y_THUMBNAIL, unsigned(data[position + 13]));
		this.groups.put(MainGroups.JFIF, jfif);
	}

	void sofHandler(byte[] data, Markers marker, int position) throws ImageParsingException {
		if (hasReadGroup(MainGroups.GENERAL)) {
			throw new ImageParsingException("Unexpected multiple-frame image", position);
		}
		MetaGroup general = new MetaGroup("general", "General");
		general.addProperty(GeneralTags.DEPTH, unsigned(data[position]));
		general.addProperty(GeneralTags.PIXEL_HEIGHT,
				parseNumber(Endianness.BIG, data, position + 1, 2));
		general.addProperty(GeneralTags.PIXEL_WIDTH,
				parseNumber(Endianness.BIG, data, position + 3, 2));
		general.addProperty(GeneralTags.TYPE, marker);
		this.groups.put(MainGroups.GENERAL, general);
	}

/** 
	   parse an unsigned number of size bytes at offset in some binary string data.
	   If endian
	   is "<" parse the data as little endian, if endian
	   is ">" parse as big-endian.
	*/
	int parseNumber(Endianness endian, byte[] data, int offset, int size)
			throws ImageParsingException {
		Preconditions.checkNotNull(endian);
		Preconditions.checkNotNull(data);
		Preconditions.checkArgument(offset >= 0);
		Preconditions.checkArgument(size >= 0);

		return Endianness.BIG == endian ? new BigEndianParser().parseUnsignedNumber(data, offset,
				size) : new LittleEndianParser().parseUnsignedNumber(data, offset, size);
	}

	interface NumberParser {
		int parseSignedNumber(byte[] data, int offset, int length) throws ImageParsingException;

		int parseUnsignedNumber(byte[] data, int offset, int length) throws ImageParsingException;
	}

	class BigEndianParser implements NumberParser {

		@Override
		public int parseSignedNumber(byte[] data, int offset, int length)
				throws ImageParsingException {
			Preconditions.checkNotNull(data);
			Preconditions.checkArgument(offset >= 0);
			Preconditions.checkArgument(length >= 0);

			int ret = 0;
			boolean negative = false;
			for (int i = offset; i < offset + length; i++) {
				/* Negative if top bit is set */
				negative = (unsigned(data[i]) & 0x80) == 0x80;
				ret <<= 8;
				/* If it is negative we invert the bits */
				ret += negative ? ~unsigned(data[i]) & 0xff : unsigned(data[i]);
			}
			if (negative) {
				/* If it is negative we do two's complement */
				ret += 1;
				ret *= -1;
			}
			return ret;
		}

		@Override
		public int parseUnsignedNumber(byte[] data, int offset, int length)
				throws ImageParsingException {
			Preconditions.checkNotNull(data);
			Preconditions.checkArgument(offset >= 0);
			Preconditions.checkArgument(length >= 0);

			int ret = 0;
			for (int i = offset; i < offset + length; i++) {
				ret <<= 8;
				ret += unsigned(data[i]);
			}
			return ret;
		}

	}

	class LittleEndianParser implements NumberParser {
/** 
		parse an signed number of size bytes at offset in some binary string data.
		   If endian
		   is "<" parse the data as little endian, if endian
		   is ">" parse as big-endian.
		*/
		@Override
		public int parseSignedNumber(byte[] data, int offset, int length)
				throws ImageParsingException {
			Preconditions.checkNotNull(data);
			Preconditions.checkArgument(offset >= 0);
			Preconditions.checkArgument(length >= 0);

			int ret = 0;
			boolean negative = false;
			for (int i = offset + length - 1; i >= offset; i--) {
				/* Negative if top bit is set */
				negative = (unsigned(data[i]) & 0x80) == 0x80;
				ret <<= 8;
				/* If it is negative we invert the bits */
				ret += negative ? ~unsigned(data[i]) & 0xff : unsigned(data[i]);
			}
			if (negative) {
				/* If it is negative we do two's complement */
				ret += 1;
				ret *= -1;
			}
			return ret;
		}

		@Override
		public int parseUnsignedNumber(byte[] data, int offset, int length) {
			Preconditions.checkNotNull(data);
			Preconditions.checkArgument(offset >= 0);
			Preconditions.checkArgument(length >= 0);

			int ret = 0;
			for (int i = offset + length - 1; i >= offset; i--) {
				ret <<= 8;
				ret += unsigned(data[i]);
			}
			return ret;
		}

	}

/** 
	parse an signed number of size bytes at offset in some binary string data.
	   If endian
	   is "<" parse the data as little endian, if endian
	   is ">" parse as big-endian.
	*/
	int parseSignedNumber(Endianness endian, byte[] data, int offset, int size)
			throws ImageParsingException {
		Preconditions.checkNotNull(endian);
		Preconditions.checkNotNull(data);
		Preconditions.checkArgument(offset >= 0);
		Preconditions.checkArgument(size >= 0);

		return Endianness.BIG == endian ? new BigEndianParser().parseSignedNumber(data, offset,
				size) : new LittleEndianParser().parseSignedNumber(data, offset, size);
	}

	@VisibleForTesting
	static double parseHMS(List<Number> hms) {
		Preconditions.checkArgument(hms != null && !hms.isEmpty(),
				"HoursMinutesSeconds cannot be empty or null!");
		Preconditions.checkArgument(hms.size() >= 3,
				"HoursMinutesSeconds cannot have less than 3 componenets!");
		return hms.get(0).doubleValue() + (hms.get(1).doubleValue() / 60.0)
				+ (hms.get(2).doubleValue() / 60.0 / 60.0);
	}
}
