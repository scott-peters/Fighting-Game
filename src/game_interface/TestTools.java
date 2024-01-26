package game_interface;

import static org.junit.Assert.assertArrayEquals;

/*
 * All additional generic helper methods for use strictly while running JUnit tests.
 */
class TestTools {

	/**
	 * This checks that the char[][] array of drawBackground is the same as the
	 * char[][] of the JUnit test.
	 * 
	 * @param a1 the char[][] of drawBackground
	 * @param a2 the char[][] of JUnit Test
	 */
	public static void assertDoubleArrayEquals(char[][] a1, char[][] a2) {
		assert a1.length == a2.length;
		for (int i = 0; i < a1.length; i++) {
			assertArrayEquals(a1[i], a2[i]);
		}
	}

}
