package jpegmeta.client;

public enum JpegConstants {
	DELIM(0xff), SOI(0xd8), EOI(0xd9), SOS(0xda);

	private final int value;

	private JpegConstants(int value) {
		this.value = value;
	}

	public int getValue() {
		return this.value;
	}
}
