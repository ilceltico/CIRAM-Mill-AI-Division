package it.unibo.ai.mulino.CIRAMill.tests;

import java.util.ArrayList;
import java.util.List;

import it.unibo.ai.mulino.CIRAMill.domain.BitBoardAction;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardState;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardTieChecker;
import it.unibo.ai.mulino.CIRAMill.minimax.AlphaBeta;
import it.unibo.ai.mulino.CIRAMill.minimax.AlphaBetaKiller;
import it.unibo.ai.mulino.CIRAMill.minimax.AlphaBetaKillerVariant;
import it.unibo.ai.mulino.CIRAMill.minimax.AlphaBetaQuiescent;
import it.unibo.ai.mulino.CIRAMill.minimax.IAction;
import it.unibo.ai.mulino.CIRAMill.minimax.IMinimax;
import it.unibo.ai.mulino.CIRAMill.minimax.ITieChecker;
import it.unibo.ai.mulino.CIRAMill.minimax.IterativeDeepeningRunnable;
import it.unibo.ai.mulino.CIRAMill.minimax.MiniMax;
import it.unibo.ai.mulino.CIRAMill.minimax.ValuedAction;

public class Tests {
	
	public static final int DEPTH = 10;
	public static final int KNUM = 10;
	public static final int STATE = 0;
	
	public static final boolean minimax = false;
	public static final boolean alphabeta = false;
	public static final boolean alphabeta_killer = false;
	public static final boolean alphabeta_killer_variant = false;
	public static final boolean alphabeta_quiescent = false;
	
	public static final int seconds = 60;
	public static final int startingDepth = 1;
	public static final boolean it_minimax = false;
	public static final boolean it_alphabeta = true;
	public static final boolean it_alphabeta_killer = false;
	public static final boolean it_alphabeta_killer_variant = true;
	public static final boolean it_alphabeta_quiescent = false;
	

	public static void main(String[] args) {
//		BitBoardState state = new BitBoardState(7, 7, (1 << 0) | (1 << 1), (1 << 5) | (1 << 13), BitBoardState.WHITE);
//		BitBoardState state = new BitBoardState(0, 0, (1 << 0) | (1 << 1) | (1 << 2) | (1 << 15) | (1 << 17) | (1 << 21) | (1 << 23),
//				(1 << 4) | (1 << 5) | (1 << 6) | (1 << 7) | (1 << 12) | (1 << 14) | (1 << 19), BitBoardState.WHITE);
//		BitBoardState state = new BitBoardState(0, 0, (1 << 0) | (1 << 2) | (1 << 9) | (1 << 15) | (1 << 17) | (1 << 21) | (1 << 23),
//				(1 << 4) | (1 << 5) | (1 << 6) | (1 << 7) | (1 << 12) | (1 << 14) | (1 << 19), BitBoardState.WHITE);
//		BitBoardState state = new BitBoardState(0, 0, (1 << 0) | (1 << 2) | (1 << 9) | (1 << 15) | (1 << 17) | (1 << 21) | (1 << 23),
//				(1 << 4) | (1 << 5) | (1 << 6), BitBoardState.WHITE);
//		BitBoardState state = new BitBoardState(0, 0, (1 << 0) | (1 << 2) | (1 << 9) | (1 << 15) | (1 << 17) | (1 << 21) | (1 << 23),
//				(1 << 4) | (1 << 5) | (1 << 6), BitBoardState.BLACK);
		
//		System.out.println(state);
//		
//		try {
//			List<IAction> actions = state.getFollowingMoves();
//			
//			for(IAction action : actions) {
//				System.out.println(action);
//			}
//			
//			System.out.println(actions.size());
//			
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
//		BitBoardState state = new BitBoardState(0, 0, 0b11111 , 0b11000000000000, BitBoardState.WHITE);
//		BitBoardState state = new BitBoardState(0, 0, 0b11000000000000, 0b11111 , BitBoardState.BLACK);
//		BitBoardState state = new BitBoardState(0, 0, (1 << 9) | (1 << 11) | (1 << 13) | (1 << 15), 0b111111110000000000000000 , BitBoardState.WHITE);
//		BitBoardState state = new BitBoardState(0, 0, (1 << 0) | (1 << 9) | (1 << 11) | (1 << 5), (1 << 1) | (1 << 2) | (1 << 3) | (1 << 4), BitBoardState.WHITE);
		
//		BitBoardState state = new BitBoardState(5, 5, (1 << 0) |  (1 << 1) |  (1 << 9) |  (1 << 15),  (1 << 2) |  (1 << 13) |  (1 << 17) |  (1 << 23), BitBoardState.WHITE);
//		
//		System.out.println(state);
//		System.out.println(state.isWinningState());
		
//		BitBoardState state = new BitBoardState(whiteCheckersToPut, blackCheckersToPut, whiteBitBoard, blackBitBoard, playerToMove)
		
//		System.out.println(state.getHeuristicEvaluation());
		
		
//		BitBoardState state = new BitBoardState(5, 5, (1 << 0) |  (1 << 1) |  (1 << 9) |  (1 << 15),  (1 << 2) |  (1 << 13) |  (1 << 18) |  (1 << 23), BitBoardState.WHITE);
				
//		System.out.println(state);
		
//		BitBoardAction action = new BitBoardAction(0, (1 << 17), (1 << 2));
		
//		state.move(action);
//		
//		System.out.println("\n\n" + state);
//		
//		state.unmove(action);
//		
//		System.out.println("\n\n" + state);
		
//		BitBoardState newState = (BitBoardState) state.applyMove(action);
//		
//		System.out.println("\n\n" + newState);
//		
//		System.out.println(state);
		
//		BitBoardState state = new BitBoardState(0, 1, (1 << 0) |  (1 << 2) |  (1 << 4) |  (1 << 6) | (1 << 9) |  (1 << 11) |  (1 << 13) |  (1 << 15) | (1 << 17), (1 << 1) |  (1 << 3) |  (1 << 5) |  (1 << 7) | (1 << 8) |  (1 << 10) |  (1 << 12) |  (1 << 14) ,BitBoardState.BLACK);
//		
//		System.out.println(state);
//		
//		BitBoardAction action = new BitBoardAction(0, (1 << 23), 0);
//		
//		state.move(action);
//		
//		System.out.println(state);
//		
//		state.unmove(action);
//		
//		System.out.println(state);
		

//		System.out.println(state + "\n\n");
		
		if (minimax) {
			BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			new Thread(new MinimaxTestRunnable(new MiniMax(tieChecker), tieChecker)).start();
		}
		if (alphabeta) {
			BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			new Thread(new MinimaxTestRunnable(new AlphaBeta(tieChecker), tieChecker)).start();
		}
		if (alphabeta_killer) {
			BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			new Thread(new MinimaxTestRunnable(new AlphaBetaKiller(tieChecker, KNUM), tieChecker)).start();
		}
		if (alphabeta_killer_variant) {
			BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			new Thread(new MinimaxTestRunnable(new AlphaBetaKillerVariant(tieChecker, KNUM), tieChecker)).start();
		}
		if(alphabeta_quiescent) {
			BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			new Thread(new MinimaxTestRunnable(new AlphaBetaQuiescent(tieChecker), tieChecker)).start();

		}
		
		if (it_minimax) {
			BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			IMinimax minimax = new MiniMax(tieChecker);
			new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();;
		}
		if (it_alphabeta) {
			BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			IMinimax minimax = new AlphaBeta(tieChecker);
			new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();;
		}
		if (it_alphabeta_killer) {
			BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			IMinimax minimax = new AlphaBetaKiller(tieChecker, KNUM);
			new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();;
		}
		if (it_alphabeta_killer_variant) {
			BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			IMinimax minimax = new AlphaBetaKillerVariant(tieChecker, KNUM);
			new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();;
		}
		if (it_alphabeta_quiescent) {
			BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			IMinimax minimax = new AlphaBetaQuiescent(tieChecker);
			new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();;
		}
//		System.out.println(state.getHeuristicEvaluation());
	}
	
	static class IterativeTestRunnable implements Runnable {
		IMinimax minimax;
		ITieChecker tieChecker;
		int maxSeconds;
		
		public IterativeTestRunnable(IMinimax minimax, ITieChecker tieChecker, int maxSeconds) {
			this.minimax = minimax;
			this.tieChecker = tieChecker;
			this.maxSeconds = maxSeconds;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			BitBoardState state;
			switch (STATE) {
			case 0: state =  new BitBoardState(tieChecker); break;
			case 1: state = new BitBoardState(0, 0, (1 << 9) | (1 << 11) | (1 << 13) | (1 << 15), 0b110010110000000101000010 , BitBoardState.WHITE, tieChecker); break;
			default: throw new IllegalArgumentException("Invalid STATE number");
			}
			
			IterativeDeepeningRunnable runnable = new IterativeDeepeningRunnable(minimax, state, startingDepth);
			Thread iterativeThread = new Thread(runnable);
			iterativeThread.start();
			
			while (maxSeconds > 1) {
				try {
					Thread.sleep(1000);
				} catch (InterruptedException e) {
					System.out.println("Shouldn't happen...");
					e.printStackTrace();
				}
				maxSeconds--;
			}
			
			iterativeThread.stop();
			
			ValuedAction vAction = runnable.getSelectedValuedAction();
			state.move(vAction.getAction());
			System.out.println("\n\n" + state);
		}
	}
	
	static class MinimaxTestRunnable implements Runnable {
		
		IMinimax minimax;
		ITieChecker tieChecker;
		
		public MinimaxTestRunnable(IMinimax minimax, ITieChecker tieChecker) {
			this.minimax = minimax;
			this.tieChecker = tieChecker;
		}

		@Override
		public void run() {
			BitBoardState state;
			switch (STATE) {
			case 0: state =  new BitBoardState(tieChecker); break;
			case 1: state = new BitBoardState(0, 0, (1 << 9) | (1 << 11) | (1 << 13) | (1 << 15), 0b110010110000000101000010 , BitBoardState.WHITE, tieChecker); break;
			default: throw new IllegalArgumentException("Invalid STATE number");
			}
			
			ValuedAction vAction = minimax.minimaxDecision(state, DEPTH);
			state.move(vAction.getAction());
			System.out.println("\n\n" + state);
		}
		
	}

}
