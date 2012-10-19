package jpegmeta.client;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;

import jpegmeta.client.GpsTags;
import jpegmeta.client.ImageParsingException;
import jpegmeta.client.JpegMeta;
import jpegmeta.client.Metadata;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.google.common.io.ByteStreams;

public class JpegMetaTest {

	private InputStream image;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
		this.image = JpegMetaTest.class.getResourceAsStream("/atlBankToLedge.jpg");
	}

	@After
	public void tearDown() throws Exception {
		this.image.close();
	}

	//correct
// -1, -40,  -1,  -31, 36, 18, 69, 120, 105, 102, 0, 0, 77, 77,
	//asBinaryString
//-40, -81, -40, -121, 36, 18, 69, 120, 105, 102, 0, 0, 77, 77,
	@Test
	public final void testReadMetadata() throws IOException, ImageParsingException {
		byte[] data = ByteStreams.toByteArray(this.image);
		Metadata meta = new JpegMeta().readMetadata(data);
		assertTrue(meta.size() > 0);
		Double lat = meta.getProperty(GpsTags.DECIMAL_LATITUDE, Double.class);
		Double lng = meta.getProperty(GpsTags.DECIMAL_LONGITUDE, Double.class);
		System.out.println(lat + ":" + lng);
	}

//	@Test
	public final void testParseImageFileDirectory() {
		fail("Not yet implemented"); // TODO
	}

//	@Test
	public final void testSlice() {
		fail("Not yet implemented"); // TODO
	}

//	@Test
	public final void testApp0Handler() {
		fail("Not yet implemented"); // TODO
	}

//	@Test
	public final void testApp1Handler() {
		fail("Not yet implemented"); // TODO
	}

//	@Test
	public final void testExifHandler() {
		fail("Not yet implemented"); // TODO
	}

//	@Test
	public final void testHasReadGroup() {
		fail("Not yet implemented"); // TODO
	}

//	@Test
	public final void testHasReadProperty() {
		fail("Not yet implemented"); // TODO
	}

//	@Test
	public final void testJfifHandler() {
		fail("Not yet implemented"); // TODO
	}

//	@Test
	public final void testSofHandler() {
		fail("Not yet implemented"); // TODO
	}

//	@Test
	public final void testParseNumber() {
		fail("Not yet implemented"); // TODO
	}

//	@Test
	public final void testParseHMS() {
		fail("Not yet implemented"); // TODO
	}

//	@Test
	public final void testParseSignedNumber() {
		fail("Not yet implemented"); // TODO
	}

}
