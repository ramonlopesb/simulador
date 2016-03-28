package test;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import info.ImagesSize;
import junit.framework.TestCase;

public class DedupedSizeTest extends TestCase {
	
	private ImagesSize instance;
	private Map<Integer, Double> info;
	
	
	@Before
	public void setUp() {
		instance = ImagesSize.getInstance();
		info = new HashMap<Integer, Double>();
		info.put(1, 5.2);
		info.put(2, 3.0);
		info.put(3, 1.3);
	}
	
	@Test
	public void testAddInfo() {
		instance.addInformation(info);
		assertNotNull(instance.getDedupedImages());
		assertTrue(!instance.getDedupedImages().isEmpty());
	}
	
	@Test
	public void testGetSize() {
		instance.addInformation(info);
		assertEquals(3.0, instance.getSizeOf(new int[] {1, 3}, 2));
		assertEquals(1.3, instance.getSizeOf(new int[] {1, 2}, 3));
		assertEquals(5.2, instance.getSizeOf(new int[] {3, 2}, 1)); 
	}

}
