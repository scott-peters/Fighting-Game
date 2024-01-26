package game_objects;

/**
 * A more viable substitute for cloning, typically for use when using a
 * copy-constructor is not viable due to inheritance of an abstract class or
 * interface.
 *
 * @param <T> return Type of copy.
 * @author Lucas Brown
 */
public interface Copyable<T> {

	/**
	 * Creates a copy of the object.
	 * 
	 * @return a deep copy of the object.
	 */
	public T copy();
}
