package it.unibo.ai.mulino.CIRAMill.minimax;

public class IterativeDeepeningRunnable implements Runnable {
	
	private IMinimax minimax;
	private IState state;
	private int startingDepth;
	
	private ValuedAction selectedValuedAction;
	
	public IterativeDeepeningRunnable(IMinimax minimax, IState state, int startingDepth) {
		if (minimax == null)
			throw new IllegalArgumentException("Minimax can't be null");
		if (state == null)
			throw new IllegalArgumentException("State can't be null");
		if (startingDepth < 0)
			throw new IllegalArgumentException("Starting depth can't be negative");
		
		this.minimax = minimax;
		this.state = state;
		this.startingDepth = startingDepth;
		
		selectedValuedAction = null;
	}

	@Override
	public void run() {
		int depth = startingDepth;
		while (true) {
			selectedValuedAction = minimax.minimaxDecision(state, depth);
			System.out.println("Level " + depth + " completed\n");
			if (selectedValuedAction.getValue() >= Integer.MAX_VALUE-3 || selectedValuedAction.getValue() <= Integer.MIN_VALUE+3)
				break;
			depth++;
		}
	}
	
	public ValuedAction getSelectedValuedAction() {
		return this.selectedValuedAction;
	}
	
	public IMinimax getMinimax() {
		return this.minimax;
	}

}
