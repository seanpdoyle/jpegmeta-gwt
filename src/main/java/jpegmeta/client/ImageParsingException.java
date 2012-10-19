package jpegmeta.client;

public class ImageParsingException extends Exception {

	private static final long serialVersionUID = 1L;
	private final int errorOffset;
	
	public ImageParsingException(String message, int errorOffset) {
		super(message);
		this.errorOffset = errorOffset;
	}
	
	public int getErrorOffset() {
		return this.errorOffset;
	}
}
