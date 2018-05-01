package everythingelse;

public class FromAndToAreEqualsException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FromAndToAreEqualsException(Action action) {
		super("The from and to position are the same: " + action.toString());
	}
}
