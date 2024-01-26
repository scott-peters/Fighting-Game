package the_game;

import java.io.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;

/**
 * This class Attempts to create a <T> instance of for every file within a
 * provided folder then stores each instance in an array to be used later. The
 * provided class MUST have a constructor with the only parameter being a string
 * which is then presumed to use that string to load said file in anyway the
 * class dictates.
 * 
 * The Class object provided in the constructor is necessary for creation of the
 * array of <T> objects since typically, creation of a generic array is
 * prohibited. If an object throws an exception while attempting to create a
 * file an InvocationTargetException is caught and nothing is 'added' to the
 * array.
 * 
 * 
 * @author Jess
 * @author Lucas
 * 
 * @param <T> The type of object being being loaded.
 */
public final class ObjectLoader<T> {

	private final T[] allObjects;
	private final Class<T> clazz;

	/**
	 * Creates an instance for every valid file loaded at the given file path.
	 * 
	 * @param folderPath the location of the folder within the file system.
	 * @param c          the class of the type being created.
	 */
	@SuppressWarnings("unchecked")
	public ObjectLoader(String folderPath, Class<T> c) {
		this.clazz = c; // Store the class for later.
		// Collect all available files in the given folder.
		File sourceFolder = new File(folderPath);
		File[] fileList = sourceFolder.listFiles();
		ArrayList<T> loadedList = new ArrayList<T>();

		// Test each possible folder/file to see if it's able to be instantiated as a T
		// object.
		T obj;
		for (int i = 0; i < fileList.length; i++) {
			try {
				// Create a new object of the type T with a parameter that only takes one string
				// as an argument.
				obj = this.clazz.getDeclaredConstructor(String.class).newInstance(fileList[i].getAbsolutePath());
				// If the object is not null and an exception is not thrown then add it to the
				// list.
				if (obj != null) {
					loadedList.add(obj);
				}
			} catch (InstantiationException | IllegalAccessException | IllegalArgumentException | NoSuchMethodException
					| SecurityException ex) {
				// These Exceptions are not expected and should be brought to our attention.
				ex.printStackTrace();
			} catch (InvocationTargetException ex) {
				// This exception is the result of attempting to load incompatible files and is
				// expected in most cases.
			}
		}

		// Convert the list to an array.
		this.allObjects = (T[]) loadedList.toArray((T[]) Array.newInstance(this.clazz, loadedList.size()));
	}

	/**
	 * An array of non-null objects representing every successfully loaded object.
	 * 
	 * @return every successfully loaded object.
	 */
	public T[] getAllObjects() {
		return Arrays.copyOf(this.allObjects, this.allObjects.length);
	}
}
