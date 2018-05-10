package it.unibo.ai.mulino.CIRAMill.domain;

import java.util.ArrayList;

import it.unibo.ai.mulino.CIRAMill.minimax.IState;
import it.unibo.ai.mulino.CIRAMill.minimax.ITieChecker;

public class BitBoardTieChecker implements ITieChecker {
	private ArrayList<BitBoardState> statesAlreadySeen = new ArrayList<>(10);
	
	@Override
	public boolean isTie(IState state) {
//		System.out.println(statesAlreadySeen.size());
//		return statesAlreadySeen.contains(((BitBoardState) state).getHash());
//		return statesAlreadySeen.contains(state);
		
		int index = statesAlreadySeen.indexOf(state);
		return index < statesAlreadySeen.size()-1 && index >= 0;
	}
	
	public void addState(IState state) {
		// probabilmente non serve il controllo perche'
		// aggiungere un doppione equivale al pareggio
		//
		// come controllare la fase dello stato (e' privato) (getter suppongo)
		BitBoardState bState = (BitBoardState) state;
		if(bState.getGamePhase() == BitBoardState.MIDGAME && !statesAlreadySeen.contains(bState))
			statesAlreadySeen.add(bState);
		
//		if(((BitBoardState) state).getGamePhase() == BitBoardState.MIDGAME && !statesAlreadySeen.contains(((BitBoardState) state).getHash())) {
//			statesAlreadySeen.add(((BitBoardState) state).getHash());
//		}
	}
	
	public void removeState(IState state) {
//		statesAlreadySeen.remove(((BitBoardState) state).getHash());
		statesAlreadySeen.remove(state);
	}

}
