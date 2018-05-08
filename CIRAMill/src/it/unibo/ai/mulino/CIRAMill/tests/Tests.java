package it.unibo.ai.mulino.CIRAMill.tests;

import java.util.List;

import it.unibo.ai.mulino.CIRAMill.domain.BitBoardState;
import it.unibo.ai.mulino.CIRAMill.minimax.IAction;

public class Tests {

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
//

		BitBoardState state = new BitBoardState(5, 5, (1 << 0) |  (1 << 1) |  (1 << 9) |  (1 << 15),  (1 << 2) |  (1 << 13) |  (1 << 17) |  (1 << 23), BitBoardState.WHITE);
		
		System.out.println(state);
//		System.out.println(state.isWinningState());
		
//		BitBoardState state = new BitBoardState(whiteCheckersToPut, blackCheckersToPut, whiteBitBoard, blackBitBoard, playerToMove)
		
		System.out.println(state.getHeuristicEvaluation());
	}

}
