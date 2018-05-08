package it.unibo.ai.mulino.CIRAMill.tests;

import java.util.ArrayList;

import it.unibo.ai.mulino.CIRAMill.domain.BitBoardAction;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardState;

public class Tests {

	public static void main(String[] args) {
//		BitBoardState state = new BitBoardState(7, 7, (1 << 0) | (1 << 1), (1 << 5) | (1 << 13), BitBoardState.WHITE);
//		BitBoardState state = new BitBoardState(0, 0, (1 << 0) | (1 << 1) | (1 << 2) | (1 << 15) | (1 << 17) | (1 << 21) | (1 << 23),
//				(1 << 4) | (1 << 5) | (1 << 6) | (1 << 7) | (1 << 12) | (1 << 14) | (1 << 19), BitBoardState.WHITE);
//		BitBoardState state = new BitBoardState(0, 0, (1 << 0) | (1 << 2) | (1 << 9) | (1 << 15) | (1 << 17) | (1 << 21) | (1 << 23),
//				(1 << 4) | (1 << 5) | (1 << 6) | (1 << 7) | (1 << 12) | (1 << 14) | (1 << 19), BitBoardState.WHITE);
//		BitBoardState state = new BitBoardState(0, 0, (1 << 0) | (1 << 2) | (1 << 9) | (1 << 15) | (1 << 17) | (1 << 21) | (1 << 23),
//				(1 << 4) | (1 << 5) | (1 << 6), BitBoardState.WHITE);
		BitBoardState state = new BitBoardState(0, 0, (1 << 0) | (1 << 2) | (1 << 9) | (1 << 15) | (1 << 17) | (1 << 21) | (1 << 23),
				(1 << 4) | (1 << 5) | (1 << 6), BitBoardState.BLACK);
		
		System.out.println(state);
		
		try {
			ArrayList<BitBoardAction> actions = state.getFollowingMoves();
			
			for(BitBoardAction action : actions) {
				System.out.println(action);
			}
			
			System.out.println(actions.size());
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
