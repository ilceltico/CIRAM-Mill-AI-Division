package everythingelse;

public class NullActionException extends Exception {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NullActionException() {
		super("State is null!");
	}
}
