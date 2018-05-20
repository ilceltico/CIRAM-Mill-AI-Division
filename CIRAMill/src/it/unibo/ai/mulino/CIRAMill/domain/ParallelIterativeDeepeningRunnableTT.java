package it.unibo.ai.mulino.CIRAMill.domain;

import it.unibo.ai.mulino.CIRAMill.minimax.IMinimax;
import it.unibo.ai.mulino.CIRAMill.minimax.IState;
import it.unibo.ai.mulino.CIRAMill.minimax.ValuedAction;

public class ParallelIterativeDeepeningRunnableTT implements Runnable {
	
	private IMinimax[] minimax;
	private BitBoardTieChecker[] tieCheckers;
	private BitBoardState state;
	private int startingDepth;
	
	private int completedDepth = -1;
	
	private ValuedAction selectedValuedAction;
	
	public ParallelIterativeDeepeningRunnableTT(IMinimax minimax[], BitBoardTieChecker[] tieCheckers, BitBoardState state, int startingDepth) {
		if (minimax == null || minimax.length == 0)
			throw new IllegalArgumentException("Minimax can't be null or empty");
		if (state == null)
			throw new IllegalArgumentException("State can't be null");
		if (startingDepth < 0)
			throw new IllegalArgumentException("Starting depth can't be negative");
		
		this.minimax = minimax;
		this.state = state;
		this.startingDepth = startingDepth;
		this.tieCheckers = tieCheckers;
		
		selectedValuedAction = null;
	}

	@Override
	public void run() {
		MinimaxThread[] minimaxes = new MinimaxThread[minimax.length];
		for (int i=0; i<minimax.length; i++) {
			BitBoardState temp = (BitBoardState) state.clone();
			temp.setTieChecker(tieCheckers[i]);
			minimaxes[i] = new MinimaxThread(minimax[i], temp, startingDepth++);
			minimaxes[i].start();
			System.out.println("Started minimax with depth " + (startingDepth-1));
		}
		
		while (true) {
			for (int i=0; i<minimaxes.length; i++) {
				if (!minimaxes[i].isAlive()) {
					if (minimaxes[i].getDepth() > completedDepth) {
						this.selectedValuedAction = minimaxes[i].getSelectedValuedAction();
						completedDepth = minimaxes[i].getDepth();
					}
					System.out.println("Level " + completedDepth + " completed\n");
					BitBoardState temp = (BitBoardState) state.clone();
					temp.setTieChecker(tieCheckers[i]);
					minimaxes[i] = new MinimaxThread(minimax[i], temp, startingDepth++);
					minimaxes[i].start();
					System.out.println("Started minimax with depth " + (startingDepth-1));
				}
			}

			if ( (selectedValuedAction != null && 
					(selectedValuedAction.getValue() >= Integer.MAX_VALUE-1 || selectedValuedAction.getValue() <= Integer.MIN_VALUE+1)) ||
					Thread.interrupted() ) {
				System.out.println("Interrupted or WON/LOST");
				for (int i=0; i<minimaxes.length; i++) {
					if (minimaxes[i].isAlive())
						minimaxes[i].stop();
				}
				break;
			}
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
		}
	}
	
	public ValuedAction getSelectedValuedAction() {
		return this.selectedValuedAction;
	}
	
	
	class MinimaxThread extends Thread {
		
		private IMinimax minimax;
		private IState state;
		private int maxdepth;
		private ValuedAction selectedValuedAction = null;
		
		public MinimaxThread(IMinimax minimax, IState state, int maxdepth) {
			this.minimax = minimax;
			this.state = state;
			this.maxdepth = maxdepth;
		}
		
		@Override
		public void run() {
			selectedValuedAction = minimax.minimaxDecision(state, maxdepth);
		}
		
		public ValuedAction getSelectedValuedAction() {
			return this.selectedValuedAction;
		}
		
		public int getDepth() {
			return this.maxdepth;
		}
		
		public int setDepth(int depth) {
			return this.maxdepth = depth;
		}
		
	}

}
