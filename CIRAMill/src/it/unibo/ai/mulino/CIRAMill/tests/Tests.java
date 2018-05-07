package it.unibo.ai.mulino.CIRAMill.tests;

import java.util.ArrayList;

import it.unibo.ai.mulino.CIRAMill.domain.BitBoardAction;
import it.unibo.ai.mulino.CIRAMill.domain.BitBoardState;

public class Tests {

	public static void main(String[] args) {
		BitBoardState state = new BitBoardState(7, 7, (1 << 0) | (1 << 1), (1 << 5) | (1 << 13));
		
		System.out.println(state);
		
		try {
			ArrayList<BitBoardAction> actions = state.getFollowingMoves();
			
			for(BitBoardAction action : actions) {
				System.out.println(action);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
