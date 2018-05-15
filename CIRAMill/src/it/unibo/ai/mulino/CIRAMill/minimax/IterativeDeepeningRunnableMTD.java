package it.unibo.ai.mulino.CIRAMill.minimax;

public class IterativeDeepeningRunnableMTD implements Runnable {
	
	private ITieChecker tieChecker;
	private IState state;
	private int startingDepth;
	
	private ValuedAction selectedValuedAction;
	
	public IterativeDeepeningRunnableMTD(ITieChecker tieChecker, IState state, int startingDepth) {
		if (state == null)
			throw new IllegalArgumentException("State can't be null");
		if (startingDepth < 0)
			throw new IllegalArgumentException("Starting depth can't be negative");
		
		this.tieChecker = tieChecker;
		this.state = state;
		this.startingDepth = startingDepth;
		
		selectedValuedAction = null;
	}

	@Override
	public void run() {
		int depth = startingDepth;
		int firstGuess = 0;
		while (true) {
			IMinimax minimax = new MTD(tieChecker, firstGuess);
			selectedValuedAction = minimax.minimaxDecision(state, depth);
			System.out.println("Level " + depth + " completed\n");
			if (selectedValuedAction.getValue() >= Integer.MAX_VALUE-1 || selectedValuedAction.getValue() <= Integer.MIN_VALUE+1)
				break;
			depth++;
			firstGuess = selectedValuedAction.getValue();
		}
	}
	
	public ValuedAction getSelectedValuedAction() {
		return this.selectedValuedAction;
	}
}

