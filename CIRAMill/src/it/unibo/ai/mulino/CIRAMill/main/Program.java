package it.unibo.ai.mulino.CIRAMill.main;

import it.unibo.ai.didattica.mulino.actions.Action;
import it.unibo.ai.didattica.mulino.actions.Phase1Action;
import it.unibo.ai.didattica.mulino.actions.Phase2Action;
import it.unibo.ai.didattica.mulino.actions.PhaseFinalAction;
import it.unibo.ai.didattica.mulino.domain.State;
import it.unibo.ai.didattica.mulino.domain.State.Checker;
import it.unibo.ai.didattica.mulino.domain.State.Phase;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardAction;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardState;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardTieChecker;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardTranspositionTable;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardUtils;
import it.unibo.ai.mulino.CIRAMill.minimax.AlphaBetaTransposition;
import it.unibo.ai.mulino.CIRAMill.minimax.IMinimax;
import it.unibo.ai.mulino.CIRAMill.minimax.IterativeDeepeningRunnable;
import it.unibo.ai.mulino.CIRAMill.minimax.MiniMax;

public class Program {
	
	public static String USAGE = "Usage: (white | black) -options\n" + 
								"";
	public static final int STARTINGDEPTH = 1;
	public static final int MAXMILLIS = 60000;
	public static final int SECURITYMILLIS = 2000;
	
	
	public static int startingDepth = STARTINGDEPTH;
	public static int maxMillis = MAXMILLIS;
	public static int securityMillis = SECURITYMILLIS;
	
	public static byte playerMe;
	public static byte playerOpponent;

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
		
		
		//SCELTA ALGORITMO, EURISTICA, TRANSP, HIST, TIECHECKER
		BitBoardTieChecker tieChecker = new BitBoardTieChecker();
		BitBoardTieChecker tieCheckerCopy;
		IMinimax minimax = new AlphaBetaTransposition(tieChecker, new BitBoardTranspositionTable());
		//FINE SCELTA
		

		IterativeDeepeningRunnable iterativeDeepening;
		Thread iterativeThread;
		State currentState;
		BitBoardState bCurrentState;
		do {

			if (playerMe == BitBoardState.WHITE) {
				currentState = client.read();
				bCurrentState = BitBoardState.fromStateToBitBoard(currentState, playerMe, tieChecker);
				tieChecker.addState(bCurrentState);
				tieCheckerCopy = tieChecker.clone();
				System.out.println("Current state is:");
				System.out.println(currentState.toString());
				
				iterativeDeepening = new IterativeDeepeningRunnable(minimax, bCurrentState, startingDepth);
				iterativeThread = new Thread(iterativeDeepening);
				iterativeThread.start();
				
				long curMillis;
				long millis = maxMillis;
				while (millis > securityMillis) {
					curMillis = System.currentTimeMillis();
					try {
						Thread.sleep(58000);
					} catch (InterruptedException e) {
						System.out.println("Shouldn't happen...");
						e.printStackTrace();
					}
					millis -= System.currentTimeMillis() - curMillis;
					if (!iterativeThread.isAlive())
						break;
					System.out.println(millis);
				}
				
				if (iterativeThread.isAlive())
					iterativeThread.stop();
				System.out.println(millis);
				
				Action a = stringToAction(iterativeDeepening.getSelectedValuedAction().getAction().toString(), currentState.getCurrentPhase());
				tieChecker = tieCheckerCopy;
				System.out.println("TIE CHECKER \n" + tieChecker.toString());
				
				client.write(a);

				currentState = client.read();
				bCurrentState = BitBoardState.fromStateToBitBoard(currentState, BitBoardState.BLACK, tieChecker);
				tieChecker.addState(bCurrentState);
				tieCheckerCopy = tieChecker.clone();
				System.out.println("New state is:");
				System.out.println(currentState.toString());

				System.out.println("\nWaiting for opponent move...");

			} else if (playerMe == BitBoardState.BLACK) {
				currentState = client.read();
				bCurrentState = BitBoardState.fromStateToBitBoard(currentState, BitBoardState.WHITE, tieChecker);
				tieChecker.addState(bCurrentState);
				tieCheckerCopy = tieChecker.clone();
				System.out.println("New state is:");
				System.out.println(currentState.toString());

				System.out.println("\nWaiting for opponent move...");

				currentState = client.read();
				bCurrentState = BitBoardState.fromStateToBitBoard(currentState, BitBoardState.BLACK, tieChecker);
				tieChecker.addState(bCurrentState);
				tieCheckerCopy = tieChecker.clone();
				System.out.println("Current state is:");
				System.out.println(currentState.toString());

				iterativeDeepening = new IterativeDeepeningRunnable(minimax, bCurrentState, startingDepth);
				iterativeThread = new Thread(iterativeDeepening);
				iterativeThread.start();
				
				long curMillis;
				long millis = maxMillis;
				while (millis > securityMillis) {
					curMillis = System.currentTimeMillis();
					try {
						Thread.sleep(58000);
					} catch (InterruptedException e) {
						System.out.println("Shouldn't happen...");
						e.printStackTrace();
					}
					millis -= System.currentTimeMillis() - curMillis;
					if (!iterativeThread.isAlive())
						break;
					System.out.println(millis);
				}
				
				if (iterativeThread.isAlive())
					iterativeThread.stop();
				System.out.println(millis);
				
				Action a = stringToAction(iterativeDeepening.getSelectedValuedAction().getAction().toString(), currentState.getCurrentPhase());
				tieChecker = tieCheckerCopy;
				System.out.println("TIE CHECKER \n" + tieChecker.toString());
				
				client.write(a);
			} else {
				System.out.println("Wrong checker");
				System.exit(-1);
			}

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
