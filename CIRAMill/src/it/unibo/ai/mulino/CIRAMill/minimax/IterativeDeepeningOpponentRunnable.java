package it.unibo.ai.mulino.CIRAMill.minimax;

public class IterativeDeepeningOpponentRunnable extends IterativeDeepeningRunnable {
	
	private ValuedAction opponentValuedAction = null;
	
	private IMinimaxOpponent minimax;
	
	public IterativeDeepeningOpponentRunnable(IMinimaxOpponent minimax, IState state, int startingDepth) {
		super(minimax, state, startingDepth);
		this.minimax = minimax;
	}

	@Override
	public void run() {
		int depth = startingDepth;
		while (true) {
			selectedValuedAction = minimax.minimaxDecision(state, depth);
			opponentValuedAction = minimax.getOpponentValuedAction();
			System.out.println("Level " + depth + " completed\n");
			if (selectedValuedAction.getValue() >= Integer.MAX_VALUE-3 || selectedValuedAction.getValue() <= Integer.MIN_VALUE+3)
				break;
			depth++;
		}
	}
	
	public ValuedAction getOpponentValuedAction() {
		return this.opponentValuedAction;
	}

}
