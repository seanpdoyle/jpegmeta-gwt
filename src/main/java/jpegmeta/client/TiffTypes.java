package jpegmeta.client;

import java.util.Map;
import java.util.Set;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.SetMultimap;

public enum TiffTypes {
	/**
	 * byte - 1 byte
	 */
	BYTE(1, 1),
	/**
	 * ascii char - 1 byte
	 */
	ASCII(2, 1),
	/**
	 * short - 2 bytes
	 */
	SHORT(3, 2),
	/**
	 * long - 4 bytes
	 */
	LONG(4, 4),
	/**
	 * rational - 8 bytes
	 */
	RATIONAL(5, 8),
	/**
	 * signed byte - 1 byte
	 */
	SBYTE(6, 1),
	/**
	 * undefined - 1 byte
	 */
	UNDEFINED(7, 1),
	/**
	 * signed short - 2 bytes
	 */
	SSHORT(8, 2),
	/**
	 * signed long - 2 bytes
	 */
	SLONG(9, 4),
	/**
	 * signed rational number - 8 bytes
	 */
	SRATIONAL(10, 8),
	/**
	 * floating point number - 4 bytes
	 */
	FLOAT(11, 4),
	/**
	 * double - 8 bytes
	 */
	DOUBLE(12, 8);
	
	private static final SetMultimap<Integer, TiffTypes> SIZE_MAP = HashMultimap.create();
	private static final Map<Integer, TiffTypes> MAP = Maps.newHashMap();
	static
	{
		for (TiffTypes tag : TiffTypes.values())
		{
			SIZE_MAP.put(tag.getSizeInBytes(), tag);
			MAP.put(tag.getKeyCode(), tag);
		}
	}

	public static TiffTypes fromKey(int keyCode) {
		return MAP.get(keyCode);
	}

	public static Set<TiffTypes> fromSizeInBytes(int sizeInBytes)
	{
		return SIZE_MAP.get(sizeInBytes);
	}

	private final int sizeInBytes;
	private final int keyCode;
	
	TiffTypes(int keyCode, int sizeInBytes) {
		this.keyCode = keyCode;
		this.sizeInBytes = sizeInBytes;
	}
	
	/**
	 * get the type's keyCode
	 * @return the keyCode for the TiffType
	 */
	public int getKeyCode() {
		return this.keyCode;
	}

	/**
	 * get the size of this type in bytes
	 * @return the size of this type
	 */
	public int getSizeInBytes() {
		return this.sizeInBytes;
	}
}
