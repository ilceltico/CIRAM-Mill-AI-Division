package it.unibo.ai.mulino.CIRAMill.main;

import java.util.ArrayList;

import it.unibo.ai.didattica.mulino.actions.Action;
import it.unibo.ai.didattica.mulino.actions.Phase1Action;
import it.unibo.ai.didattica.mulino.actions.Phase2Action;
import it.unibo.ai.didattica.mulino.actions.PhaseFinalAction;
import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.didattica.mulino.domain.State.Checker;
import it.unibo.ai.didattica.mulino.domain.State.Phase;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardButterflyTable;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardHistoryTable;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardState;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardTieChecker;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardTranspositionTable;
import it.unibo.ai.mulino.CIRAMill.minimax.AlphaBetaTransposition;
import it.unibo.ai.mulino.CIRAMill.minimax.IMinimax;
import it.unibo.ai.mulino.CIRAMill.minimax.IterativeDeepeningRunnable;
import it.unibo.ai.mulino.CIRAMill.minimax.RelativeHistoryAlphaBetaTransposition;

public class Program {
	
	public static String USAGE = "Usage: (white | black) --options\n" + 
								"Options (very incomplete list):"	+
								"\tNo idle time resource wasting: --noidle";
	public static final int STARTINGDEPTH = 1;
	public static final int MAXMILLIS = 60000;
	public static final int SECURITYMILLIS = 5000;
	
	
	public static int startingDepth = STARTINGDEPTH;
	public static int maxMillis = MAXMILLIS;
	public static int securityMillis = SECURITYMILLIS;
	
	public static byte playerMe;
	public static byte playerOpponent;
	
	public static boolean idle = true;

	public static void main(String args[]) throws Exception {
		MulinoClient client = null;
		if (args.length < 1) {
			System.out.println(USAGE);
			System.exit(1);
		}
		if (args[0].toLowerCase().equals("white")) {
			playerMe = BitBoardState.WHITE;
			playerOpponent = BitBoardState.BLACK;
			client = new MulinoClient(Checker.WHITE);
		} else if (args[0].toLowerCase().equals("black")) {
			playerMe = BitBoardState.BLACK;
			playerOpponent = BitBoardState.WHITE;
			client = new MulinoClient(Checker.BLACK);
		} else {
			System.out.println(USAGE);
			System.exit(1);
		}
		
		if (args.length > 1) {
			if (args[1].equals("--noidle"))
				idle = false;
		}
		
		
		//SCELTA ALGORITMO, EURISTICA, TRANSP, HIST, TIECHECKER
		BitBoardTieChecker tieChecker = new BitBoardTieChecker();
//		BitBoardTieChecker[] tieCheckers = new BitBoardTieChecker[7];
//		for (int i=0; i<tieCheckers.length; i++) {
//			tieCheckers[i] = new BitBoardTieChecker();
//		}
		ArrayList<BitBoardState> statesAlreadySeen = new ArrayList<>();
		BitBoardTranspositionTable transpositionTable = new BitBoardTranspositionTable();
//		BitBoardTranspositionTable[] tts = new BitBoardTranspositionTable[7];
//		for (int i=0; i<tts.length; i++) {
//			tts[i] = new BitBoardTranspositionTable();
//		}
		IMinimax minimax = new RelativeHistoryAlphaBetaTransposition(tieChecker, transpositionTable, new BitBoardHistoryTable(), new BitBoardButterflyTable());
//		IMinimax[] minimaxes = new AlphaBetaTransposition[7];
//		for (int i=0; i<minimaxes.length; i++) {
//			minimaxes[i] = new AlphaBetaTransposition(tieCheckers[i], tts[i]);
//		}
		//FINE SCELTA
		

		IterativeDeepeningRunnable iterativeDeepening;
		IterativeDeepeningRunnable[] opponentRunnables = new IterativeDeepeningRunnable[2];
		Thread[] opponentThreads = new Thread[2];
//		ParallelIterativeDeepeningRunnableTT iterativeDeepening;
		Thread iterativeThread;
		State currentState;
		BitBoardState bCurrentState;
		BitBoardState lastState = null;
		int turn = 0;
		do {

			if (playerMe == BitBoardState.WHITE) {
				//Compute during opponent time
				if (idle) {
					for (int i=0; i<opponentRunnables.length; i++) {
						BitBoardTieChecker t = new BitBoardTieChecker();
						IMinimax m = new AlphaBetaTransposition(t, new BitBoardTranspositionTable(100000));
						opponentRunnables[i] = new IterativeDeepeningRunnable(m, new BitBoardState(t), 10);
						opponentThreads[i] = new Thread(opponentRunnables[i]);
						opponentThreads[i].start();
					}
				}
				
				currentState = client.read();
				long curMillis = System.currentTimeMillis();
				if (idle) {
					for (int i=0; i<opponentThreads.length; i++) {
						if (opponentThreads[i] != null && opponentThreads[i].isAlive())
							opponentThreads[i].stop();
					}
				}
				bCurrentState = BitBoardState.fromStateToBitBoard(currentState, playerMe, tieChecker);
//				bCurrentState = BitBoardState.fromStateToBitBoard(currentState, playerMe, null);
				if (bCurrentState.getGamePhase() == BitBoardState.MIDGAME) {
					if (bCurrentState.getCheckersOnBoard(playerMe) < lastState.getCheckersOnBoard(playerMe) ||
							bCurrentState.getCheckersOnBoard(playerOpponent) < lastState.getCheckersOnBoard(playerOpponent))
						statesAlreadySeen = new ArrayList<BitBoardState>();
					statesAlreadySeen.add((BitBoardState) bCurrentState.clone());
				}
//				for (int i=0; i<tieCheckers.length; i++) {
//					tieCheckers[i].swapList(statesAlreadySeen);
//				}
				tieChecker.swapList(statesAlreadySeen);
				lastState = (BitBoardState) bCurrentState.clone();
				System.out.println("Current state is:");
				System.out.println(currentState.toString());
				
				if (turn==0) {
					turn = 1;
					Action a = stringToAction("a7", State.Phase.FIRST);
					
					client.write(a);

					currentState = client.read();
					bCurrentState = BitBoardState.fromStateToBitBoard(currentState, playerMe, tieChecker);
//					bCurrentState = BitBoardState.fromStateToBitBoard(currentState, BitBoardState.BLACK, null);
					if (bCurrentState.getGamePhase() == BitBoardState.MIDGAME) {
						if (bCurrentState.getCheckersOnBoard(playerMe) < lastState.getCheckersOnBoard(playerMe) ||
								bCurrentState.getCheckersOnBoard(playerOpponent) < lastState.getCheckersOnBoard(playerOpponent))
							statesAlreadySeen = new ArrayList<BitBoardState>();
						statesAlreadySeen.add((BitBoardState) bCurrentState.clone());
					}
//					for (int i=0; i<tieCheckers.length; i++) {
//						tieCheckers[i].swapList(statesAlreadySeen);
//					}
					tieChecker.swapList(statesAlreadySeen);
					lastState = (BitBoardState) bCurrentState.clone();
//					System.out.println("TIE CHECKER \n" + tieChecker.toString());
					System.out.println("New state is:");
					System.out.println(currentState.toString());

					System.out.println("\nWaiting for opponent move...");
					continue;
				}
				
				iterativeDeepening = new IterativeDeepeningRunnable(minimax, bCurrentState, startingDepth);
				iterativeThread = new Thread(iterativeDeepening);
				iterativeThread.start();
				
				long millis = maxMillis- (System.currentTimeMillis()-curMillis);
				while (millis > securityMillis) {
					curMillis = System.currentTimeMillis();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.out.println("Shouldn't happen...");
						e.printStackTrace();
					}
					millis -= System.currentTimeMillis() - curMillis;
					if (!iterativeThread.isAlive())
						break;
//					System.out.println(millis);
				}

				if (iterativeThread.isAlive())
					iterativeThread.stop();
//				if (iterativeThread.isAlive()) {
//					iterativeThread.interrupt();
//					iterativeThread.join(500);
//				}
				System.out.println(millis);
				
				Action a = stringToAction(iterativeDeepening.getSelectedValuedAction().getAction().toString(), currentState.getCurrentPhase());
				
				client.write(a);

				currentState = client.read();
				bCurrentState = BitBoardState.fromStateToBitBoard(currentState, playerMe, tieChecker);
//				bCurrentState = BitBoardState.fromStateToBitBoard(currentState, BitBoardState.BLACK, null);
				if (bCurrentState.getGamePhase() == BitBoardState.MIDGAME) {
					if (bCurrentState.getCheckersOnBoard(playerMe) < lastState.getCheckersOnBoard(playerMe) ||
							bCurrentState.getCheckersOnBoard(playerOpponent) < lastState.getCheckersOnBoard(playerOpponent))
						statesAlreadySeen = new ArrayList<BitBoardState>();
					statesAlreadySeen.add((BitBoardState) bCurrentState.clone());
				}
//				for (int i=0; i<tieCheckers.length; i++) {
//					tieCheckers[i].swapList(statesAlreadySeen);
//				}
				tieChecker.swapList(statesAlreadySeen);
				lastState = (BitBoardState) bCurrentState.clone();
//				System.out.println("TIE CHECKER \n" + tieChecker.toString());
				System.out.println("New state is:");
				System.out.println(currentState.toString());

				System.out.println("\nWaiting for opponent move...");

			} else if (playerMe == BitBoardState.BLACK) {
				currentState = client.read();
				bCurrentState = BitBoardState.fromStateToBitBoard(currentState, playerMe, tieChecker);
//				bCurrentState = BitBoardState.fromStateToBitBoard(currentState, BitBoardState.WHITE, null);
				if (bCurrentState.getGamePhase() == BitBoardState.MIDGAME) {
					if (bCurrentState.getCheckersOnBoard(playerMe) < lastState.getCheckersOnBoard(playerMe) ||
							bCurrentState.getCheckersOnBoard(playerOpponent) < lastState.getCheckersOnBoard(playerOpponent))
						statesAlreadySeen = new ArrayList<BitBoardState>();
					statesAlreadySeen.add((BitBoardState) bCurrentState.clone());
				}
//				for (int i=0; i<tieCheckers.length; i++) {
//					tieCheckers[i].swapList(statesAlreadySeen);
//				}
				tieChecker.swapList(statesAlreadySeen);
				lastState = (BitBoardState) bCurrentState.clone();
//				System.out.println("TIE CHECKER \n" + tieChecker.toString());
				System.out.println("New state is:");
				System.out.println(currentState.toString());

				System.out.println("\nWaiting for opponent move...");

				//Compute during opponent time
				if (idle) {
					for (int i=0; i<opponentRunnables.length; i++) {
						BitBoardTieChecker t = new BitBoardTieChecker();
						IMinimax m = new AlphaBetaTransposition(t, new BitBoardTranspositionTable(100000));
						opponentRunnables[i] = new IterativeDeepeningRunnable(m, new BitBoardState(t), 10);
						opponentThreads[i] = new Thread(opponentRunnables[i]);
						opponentThreads[i].start();
					}
				}
				
				currentState = client.read();
				long curMillis = System.currentTimeMillis();
				if (idle) {
					for (int i=0; i<opponentThreads.length; i++) {
						if (opponentThreads[i] != null && opponentThreads[i].isAlive())
							opponentThreads[i].stop();
					}
				}
				bCurrentState = BitBoardState.fromStateToBitBoard(currentState, playerMe, tieChecker);
//				bCurrentState = BitBoardState.fromStateToBitBoard(currentState, BitBoardState.BLACK, null);
				if (bCurrentState.getGamePhase() == BitBoardState.MIDGAME) {
					if (bCurrentState.getCheckersOnBoard(playerMe) < lastState.getCheckersOnBoard(playerMe) ||
							bCurrentState.getCheckersOnBoard(playerOpponent) < lastState.getCheckersOnBoard(playerOpponent))
						statesAlreadySeen = new ArrayList<BitBoardState>();
					statesAlreadySeen.add((BitBoardState) bCurrentState.clone());
				}
//				for (int i=0; i<tieCheckers.length; i++) {
//					tieCheckers[i].swapList(statesAlreadySeen);
//				}
				tieChecker.swapList(statesAlreadySeen);
				lastState = (BitBoardState) bCurrentState.clone();
				System.out.println("Current state is:");
				System.out.println(currentState.toString());

				iterativeDeepening = new IterativeDeepeningRunnable(minimax, bCurrentState, startingDepth);
				iterativeThread = new Thread(iterativeDeepening);
				iterativeThread.start();
				
				
				long millis = maxMillis- (System.currentTimeMillis()-curMillis);
				while (millis > securityMillis) {
					curMillis = System.currentTimeMillis();
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						System.out.println("Shouldn't happen...");
						e.printStackTrace();
					}
					millis -= System.currentTimeMillis() - curMillis;
					if (!iterativeThread.isAlive())
						break;
//					System.out.println(millis);
				}

				if (iterativeThread.isAlive())
					iterativeThread.stop();
//				if (iterativeThread.isAlive()) {
//					iterativeThread.interrupt();
//					iterativeThread.join(500);
//				}
				System.out.println(millis);
				
				Action a = stringToAction(iterativeDeepening.getSelectedValuedAction().getAction().toString(), currentState.getCurrentPhase());
				
				client.write(a);
			} else {
				System.out.println("Wrong checker");
				System.exit(-1);
			}
			
//			for (int i=0; i<tts.length; i++) {
//				tts[i].clear();
//			}
			transpositionTable.clear();
			System.gc();
			
		} while (true);
		
	}
	
	private static Action stringToAction(String actionString, Phase fase) {
		if (fase == Phase.FIRST) { // prima fase
			Phase1Action action;
			action = new Phase1Action();
			action.setPutPosition(actionString.substring(0, 2));
			if (actionString.length() == 4)
				action.setRemoveOpponentChecker(actionString.substring(2, 4));
			else
				action.setRemoveOpponentChecker(null);
			return action;
		} else if (fase == Phase.SECOND) { // seconda fase
			Phase2Action action;
			action = new Phase2Action();
			action.setFrom(actionString.substring(0, 2));
			action.setTo(actionString.substring(2, 4));
			if (actionString.length() == 6)
				action.setRemoveOpponentChecker(actionString.substring(4, 6));
			else
				action.setRemoveOpponentChecker(null);
			return action;
		} else { // ultima fase
			PhaseFinalAction action;
			action = new PhaseFinalAction();
			action.setFrom(actionString.substring(0, 2));
			action.setTo(actionString.substring(2, 4));
			if (actionString.length() == 6)
				action.setRemoveOpponentChecker(actionString.substring(4, 6));
			else
				action.setRemoveOpponentChecker(null);
			return action;
		}
	}
}
