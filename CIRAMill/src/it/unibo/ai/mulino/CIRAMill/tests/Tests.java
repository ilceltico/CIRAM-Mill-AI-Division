package it.unibo.ai.mulino.CIRAMill.tests;


import it.unibo.ai.mulino.CIRAMill.domain.BitBoardButterflyTable;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardButterflyTableColor;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardHistoryTable;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardHistoryTableColor;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardState;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardTieChecker;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardTranspositionTable;
import it.unibo.ai.mulino.CIRAMill.minimax.AlphaBeta;
import it.unibo.ai.mulino.CIRAMill.minimax.AlphaBetaKiller;
import it.unibo.ai.mulino.CIRAMill.minimax.AlphaBetaKillerVariant;
import it.unibo.ai.mulino.CIRAMill.minimax.AlphaBetaQuiescent;
import it.unibo.ai.mulino.CIRAMill.minimax.AlphaBetaTransposition;
import it.unibo.ai.mulino.CIRAMill.minimax.HistoryAlphaBeta;
import it.unibo.ai.mulino.CIRAMill.minimax.IMinimax;
import it.unibo.ai.mulino.CIRAMill.minimax.ITieChecker;
import it.unibo.ai.mulino.CIRAMill.minimax.IterativeDeepeningRunnable;
import it.unibo.ai.mulino.CIRAMill.minimax.MTD;
import it.unibo.ai.mulino.CIRAMill.minimax.MTDHistory;
import it.unibo.ai.mulino.CIRAMill.minimax.MTDRelativeHistory;
import it.unibo.ai.mulino.CIRAMill.minimax.MTDTransposition;
import it.unibo.ai.mulino.CIRAMill.minimax.MTDTranspositionHistory;
import it.unibo.ai.mulino.CIRAMill.minimax.MTDVariant;
import it.unibo.ai.mulino.CIRAMill.minimax.MTDVariantHistory;
import it.unibo.ai.mulino.CIRAMill.minimax.MTDVariantRelativeHistory;
import it.unibo.ai.mulino.CIRAMill.minimax.MTDVariantTransposition;
import it.unibo.ai.mulino.CIRAMill.minimax.MiniMax;
import it.unibo.ai.mulino.CIRAMill.minimax.Negascout;
import it.unibo.ai.mulino.CIRAMill.minimax.NegascoutHistory;
import it.unibo.ai.mulino.CIRAMill.minimax.NegascoutRelativeHistory;
import it.unibo.ai.mulino.CIRAMill.minimax.NegascoutTransposition;
import it.unibo.ai.mulino.CIRAMill.minimax.NegascoutTraspositionHistory;
import it.unibo.ai.mulino.CIRAMill.minimax.RelativeHistoryAlphaBeta;
import it.unibo.ai.mulino.CIRAMill.minimax.RelativeHistoryAlphaBetaColor;
import it.unibo.ai.mulino.CIRAMill.minimax.RelativeHistoryAlphaBetaTransposition;
import it.unibo.ai.mulino.CIRAMill.minimax.ValuedAction;

public class Tests {

	public static final int DEPTH = 6;
	public static final int KNUM = 10;
	public static final int STATE = 0;
	
	public static final boolean minimax = false;
	public static final boolean alphabeta = false;
	public static final boolean alphabeta_killer = false;
	public static final boolean alphabeta_killer_variant = false;
	public static final boolean alphabeta_quiescent = false;
	public static final boolean alphabeta_transp = false;
	public static final boolean mtd = false;
	public static final boolean mtd_variant = false;
	public static final boolean negascout = false;
	public static final boolean history = false;
	public static final boolean relative_history = false;
	public static final boolean relative_history_color = false;
	public static final boolean relative_history_transposition = false;
	public static final boolean mtd_transposition = false;
	public static final boolean mtd_variant_transposition = false;
	public static final boolean negascout_transposition = false;
	public static final boolean mtd_history = false;
	public static final boolean mtd_relative_history = false;
	public static final boolean mtd_variant_history = false;
	public static final boolean mtd_variant_relative_history = false;
	public static final boolean negascout_history = false;
	public static final boolean negascout_relative_history = false;
	public static final boolean negascout_transposition_history = false;
	public static final boolean mtd_transposition_history = false;
	public static final boolean mtd_transposition_relative_history = false;
	
		
	public static final int seconds = 60;
	public static final int startingDepth = 1;
	
	public static final boolean it_minimax = false;
	public static final boolean it_alphabeta = false;
	public static final boolean it_alphabeta_killer = false;
	public static final boolean it_alphabeta_killer_variant = false;
	public static final boolean it_alphabeta_quiescent = false;
	public static final boolean it_alphabeta_transp = true; ////
	public static final boolean it_mtd = false;
	public static final boolean it_mtd_variant = false;
	public static final boolean it_negascout = false;
	public static final boolean it_history = false;
	public static final boolean it_relative_history = false;
	public static final boolean it_relative_history_color = false;
	public static final boolean it_relative_history_transposition = false; ////
	public static final boolean it_mtd_transposition = false; //
	public static final boolean it_mtd_variant_transposition = false; ////
	public static final boolean it_negascout_transposition = false; //
	public static final boolean it_mtd_history = false;
	public static final boolean it_mtd_relative_history = false;
	public static final boolean it_mtd_variant_history = false;
	public static final boolean it_mtd_variant_relative_history = false;
	public static final boolean it_negascout_history = false;
	public static final boolean it_negascout_relative_history = false;
	public static final boolean it_negascout_transposition_history = false; //
	public static final boolean it_mtd_transposition_history = false; //
	public static final boolean it_mtd_transposition_relative_history = false;
	
	
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
		if (alphabeta_transp) {
			BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			new Thread(new MinimaxTestRunnable(new AlphaBetaTransposition(tieChecker, new BitBoardTranspositionTable()), tieChecker)).start();;
		}
        if (mtd) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            new Thread(new MinimaxTestRunnable(new MTD(tieChecker), tieChecker)).start();
        }
        if (mtd_variant) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            new Thread(new MinimaxTestRunnable(new MTDVariant(tieChecker), tieChecker)).start();
        }
        if (negascout) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            new Thread(new MinimaxTestRunnable(new Negascout(tieChecker), tieChecker)).start();
        }
        if (history) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
            new Thread(new MinimaxTestRunnable(new HistoryAlphaBeta(tieChecker, historyTable), tieChecker)).start();
        }
        if (relative_history) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
			BitBoardButterflyTable butterflyTable = new BitBoardButterflyTable();
            new Thread(new MinimaxTestRunnable(new RelativeHistoryAlphaBeta(tieChecker, historyTable, butterflyTable), tieChecker)).start();
        }
        if (relative_history_color) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			BitBoardHistoryTableColor historyTable = new BitBoardHistoryTableColor();
			BitBoardButterflyTableColor butterflyTable = new BitBoardButterflyTableColor();
            new Thread(new MinimaxTestRunnable(new RelativeHistoryAlphaBetaColor(tieChecker, historyTable, butterflyTable), tieChecker)).start();
        }
        if (relative_history_transposition) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
			BitBoardButterflyTable butterflyTable = new BitBoardButterflyTable();
			BitBoardTranspositionTable transpositionTable = new BitBoardTranspositionTable();
            new Thread(new MinimaxTestRunnable(new RelativeHistoryAlphaBetaTransposition(tieChecker, transpositionTable, historyTable, butterflyTable), tieChecker)).start();
        }
        if(mtd_transposition) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            new Thread(new MinimaxTestRunnable(new MTDTransposition(tieChecker, new BitBoardTranspositionTable()), tieChecker)).start();
        }
        if(mtd_variant_transposition) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            new Thread(new MinimaxTestRunnable(new MTDVariantTransposition(tieChecker, new BitBoardTranspositionTable()), tieChecker)).start();
        }
        if(negascout_transposition) {
        	BitBoardTieChecker tieChecker = new BitBoardTieChecker();
        	new Thread(new MinimaxTestRunnable(new NegascoutTransposition(tieChecker, new BitBoardTranspositionTable()), tieChecker)).start();
        }
        if (mtd_history) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
			new Thread(new MinimaxTestRunnable(new MTDHistory(tieChecker, historyTable), tieChecker)).start();
        }
        if(mtd_relative_history) {
        	BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
			BitBoardButterflyTable butterflyTable = new BitBoardButterflyTable();
			new Thread(new MinimaxTestRunnable(new MTDRelativeHistory(tieChecker, historyTable, butterflyTable), tieChecker)).start();
        }
        if(mtd_variant_history) {
        	BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
			new Thread(new MinimaxTestRunnable(new MTDVariantHistory(tieChecker, historyTable), tieChecker)).start();
        }
        if(mtd_variant_relative_history) {
        	BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
			BitBoardButterflyTable butterflyTable = new BitBoardButterflyTable();
			new Thread(new MinimaxTestRunnable(new MTDVariantRelativeHistory(tieChecker, historyTable, butterflyTable), tieChecker)).start();
        }
        if (negascout_history) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
            new Thread(new MinimaxTestRunnable(new NegascoutHistory(tieChecker, historyTable), tieChecker)).start();
        }
        if (negascout_relative_history) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
            BitBoardButterflyTable butterflyTable = new BitBoardButterflyTable();
            new Thread(new MinimaxTestRunnable(new NegascoutRelativeHistory(tieChecker, historyTable, butterflyTable), tieChecker)).start();
        }
        if (negascout_transposition_history) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
            BitBoardTranspositionTable transpositionTable = new BitBoardTranspositionTable();
            new Thread(new MinimaxTestRunnable(new NegascoutTraspositionHistory(tieChecker, historyTable, transpositionTable), tieChecker)).start();
        }
        if (mtd_transposition_history) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
            BitBoardTranspositionTable transpositionTable = new BitBoardTranspositionTable();
            new Thread(new MinimaxTestRunnable(new MTDTranspositionHistory(tieChecker, transpositionTable, historyTable), tieChecker)).start();
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
		if (it_alphabeta_transp) {
			BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			IMinimax minimax = new AlphaBetaTransposition(tieChecker, new BitBoardTranspositionTable());
			new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();;
		}
        if (it_mtd) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            IMinimax minimax = new MTD(tieChecker);
            new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();
        }
        if (it_mtd_variant) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            IMinimax minimax = new MTDVariant(tieChecker);
            new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();
        }
        if (it_negascout) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            IMinimax minimax = new Negascout(tieChecker);
            new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();
        }
        if (it_history) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
            IMinimax minimax = new HistoryAlphaBeta(tieChecker, historyTable);
            new Thread(new IterativeTestRunnable(minimax ,tieChecker, seconds)).start();
        }
        if (it_relative_history) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
            BitBoardButterflyTable butterflyTable = new BitBoardButterflyTable();
            IMinimax minimax = new RelativeHistoryAlphaBeta(tieChecker, historyTable, butterflyTable);
            new Thread(new IterativeTestRunnable(minimax ,tieChecker, seconds)).start();
        }
        if (it_relative_history_color) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            BitBoardHistoryTableColor historyTable = new BitBoardHistoryTableColor();
            BitBoardButterflyTableColor butterflyTable = new BitBoardButterflyTableColor();
            IMinimax minimax = new RelativeHistoryAlphaBetaColor(tieChecker, historyTable, butterflyTable);
            new Thread(new IterativeTestRunnable(minimax ,tieChecker, seconds)).start();
        }
        if (it_relative_history_transposition) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
            BitBoardButterflyTable butterflyTable = new BitBoardButterflyTable();
			BitBoardTranspositionTable transpositionTable = new BitBoardTranspositionTable();
            IMinimax minimax = new RelativeHistoryAlphaBetaTransposition(tieChecker, transpositionTable, historyTable, butterflyTable);
            new Thread(new IterativeTestRunnable(minimax ,tieChecker, seconds)).start();
        }
        if(it_mtd_transposition) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            IMinimax minimax = new MTDTransposition(tieChecker, new BitBoardTranspositionTable());
            new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();
        }
        if(it_mtd_variant_transposition) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            IMinimax minimax = new MTDVariantTransposition(tieChecker, new BitBoardTranspositionTable());
            new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();
        }
        if(it_negascout_transposition) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            IMinimax minimax = new NegascoutTransposition(tieChecker, new BitBoardTranspositionTable());
            new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();
        }
        if (it_mtd_history) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
			IMinimax minimax = new MTDHistory(tieChecker, historyTable);
			new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();
        }
        if(it_mtd_relative_history) {
        	BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
			BitBoardButterflyTable butterflyTable = new BitBoardButterflyTable();
			IMinimax minimax = new MTDRelativeHistory(tieChecker, historyTable, butterflyTable);
			new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();
        }
        if(it_mtd_variant_history) {
        	BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
			IMinimax minimax = new MTDVariantHistory(tieChecker, historyTable);
			new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();
        }
        if(it_mtd_variant_relative_history) {
        	BitBoardTieChecker tieChecker = new BitBoardTieChecker();
			BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
			BitBoardButterflyTable butterflyTable = new BitBoardButterflyTable();
			IMinimax minimax = new MTDVariantRelativeHistory(tieChecker, historyTable, butterflyTable);
			new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();
        }
        if (it_negascout_history) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
            IMinimax minimax = new NegascoutHistory(tieChecker, historyTable);
            new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();
        }
        if (it_negascout_relative_history) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
            BitBoardButterflyTable butterflyTable = new BitBoardButterflyTable();
            IMinimax minimax = new NegascoutRelativeHistory(tieChecker, historyTable, butterflyTable);
            new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();
        }
        if (it_negascout_transposition_history) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
            IMinimax minimax = new NegascoutTraspositionHistory(tieChecker, historyTable, new BitBoardTranspositionTable());
            new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();
        }
        if (it_mtd_transposition_history) {
            BitBoardTieChecker tieChecker = new BitBoardTieChecker();
            BitBoardHistoryTable historyTable = new BitBoardHistoryTable();
            IMinimax minimax = new MTDTranspositionHistory(tieChecker, new BitBoardTranspositionTable(), historyTable);
            new Thread(new IterativeTestRunnable(minimax, tieChecker, seconds)).start();
        }
        
				
//		LRUMap<Long, ValuedAction> map = new LRUMap<>();
//		map.put(1L, new ValuedAction(new BitBoardAction(), 30));
//		
//		ValuedAction va = map.get(1L);
//		System.out.println(va);
//		va.set(va.getAction(), 50);
//		System.out.println(va);
//		
//
//		System.out.println(map.get(1L));
		
		
//		System.out.println(state.getHeuristicEvaluation());
		
//		BitBoardState state = new BitBoardState(5, 5, (1 << 3) | (1 << 4) | (1 << 6), (1 << 5) | (1 << 17) | (1 << 20), BitBoardState.WHITE , null);
//	
//		System.out.println(state);
//		
//		BitBoardState state2 = new BitBoardState(5, 5, BitBoardUtils.rotationClockwise180((1 << 3) | (1 << 4) | (1 << 6)), BitBoardUtils.rotationClockwise180((1 << 5) | (1 << 17) | (1 << 20)), BitBoardState.WHITE , null);
//
//		System.out.println(state2);
	}
	
	static class IterativeTestRunnable implements Runnable {
		IMinimax minimax;
		ITieChecker tieChecker;
		long millis;
		
		public IterativeTestRunnable(IMinimax minimax, ITieChecker tieChecker, int maxSeconds) {
			this.minimax = minimax;
			this.tieChecker = tieChecker;
			this.millis = ((long)maxSeconds) * 1000;
		}

		@SuppressWarnings("deprecation")
		@Override
		public void run() {
			BitBoardState state, stateClone;
			switch (STATE) {
			case 0: state =  new BitBoardState(tieChecker); break;
			case 1: state = new BitBoardState(0, 0, (1 << 9) | (1 << 11) | (1 << 13) | (1 << 15), 0b110010110000000101000010 , BitBoardState.WHITE, tieChecker); break;
			case 2: state = new BitBoardState(0, 0, (1 << 0) | (1 << 2) | (1 << 9) | (1 << 15) | (1 << 17) | (1 << 21) | (1 << 23),
					(1 << 4) | (1 << 5) | (1 << 6) | (1 << 7) | (1 << 12) | (1 << 13) | (1 << 14) | (1 << 19), BitBoardState.BLACK, tieChecker); break;
			
			case 3: state = new BitBoardState(0, 0, (1 << 9) | (1 << 12) | (1 << 15), (1 << 8) | (1 << 22) | (1 << 23), BitBoardState.WHITE, tieChecker); break;
			default: throw new IllegalArgumentException("Invalid STATE number");
			}
			stateClone = (BitBoardState) state.clone();
//			System.out.println(state);
			IterativeDeepeningRunnable runnable = new IterativeDeepeningRunnable(minimax, stateClone, startingDepth);
			Thread iterativeThread = new Thread(runnable);
			iterativeThread.start();
			
			long curMillis;
			while (millis > 2000) {
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
				System.out.println(millis);
			}
			
			if (iterativeThread.isAlive())
				iterativeThread.stop();
			System.out.println(millis);
			
			ValuedAction vAction = runnable.getSelectedValuedAction();
			state.move(vAction.getAction());
			System.out.println("\n\nSelected action: " + vAction + "\n" + state);
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
			case 2: state = new BitBoardState(0, 0, (1 << 0) | (1 << 2) | (1 << 9) | (1 << 15) | (1 << 17) | (1 << 21) | (1 << 23),
													(1 << 4) | (1 << 5) | (1 << 6) | (1 << 7) | (1 << 12) | (1 << 13) | (1 << 14) | (1 << 19), BitBoardState.BLACK, tieChecker); break;
			case 3: state = new BitBoardState(0, 0, (1 << 9) | (1 << 12) | (1 << 15), (1 << 8) | (1 << 22) | (1 << 23), BitBoardState.WHITE, tieChecker); break;
			default: throw new IllegalArgumentException("Invalid STATE number");
			}
			
			ValuedAction vAction = minimax.minimaxDecision(state, DEPTH);
			state.move(vAction.getAction());
			System.out.println("\n\nSelected action: " + vAction + "\n" + state);
		}
		
	}

}
