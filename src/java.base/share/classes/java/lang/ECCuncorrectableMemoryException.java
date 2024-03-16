// @Author Kosuke OKAZAKI

package java.lang;

/**
 * Thrown when an application encounters
 * ECC-uncorrectable memory error.
 *
 * This is defined as exception (not error)
 * since application may recover from this error.
 */

public class ECCuncorrectableMemoryException extends RuntimeException {
	@java.io.Serial
	private static final long serialVersionUID = 2026602320266023L;

	/**
	 * Constructs an EME with no detail message.
	 */
	public ECCuncorrectableMemoryException() {
	 	super();
	}

	/**
	 * Constructs an EME with the specified detail message.
	 *
	 * @param   s   the detailed message.
	 */
	public ECCuncorrectableMemoryException(String s) {
		super(s);
	}

	/**
	 * Returns hashCode of broken Object.
	 *
	 * @return hashCode
	*/
	public native int getBrokenObjectHash();
}
