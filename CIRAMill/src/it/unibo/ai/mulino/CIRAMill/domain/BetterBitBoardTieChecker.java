package it.unibo.ai.mulino.CIRAMill.domain;

import java.util.HashSet;

import it.unibo.ai.mulino.CIRAMill.minimax.IState;

public class BetterBitBoardTieChecker extends BitBoardTieChecker {
	private HashSet<BitBoardState> statesAlreadySeen = new HashSet<>(30);
	private BitBoardState lastState;
	
	@Override
	public boolean isTie(IState state) {
//		System.out.println(statesAlreadySeen.size());
//		return statesAlreadySeen.contains(((BitBoardState) state).getHash());
//		return statesAlreadySeen.contains(state);
		
		return statesAlreadySeen.contains(state);
	}
	
	public void addState(IState state) {
		// probabilmente non serve il controllo perche'
		// aggiungere un doppione equivale al pareggio
		//
		// come controllare la fase dello stato (e' privato) (getter suppongo)
		BitBoardState bState = (BitBoardState) state;
		if(bState.getGamePhase() == BitBoardState.MIDGAME && !statesAlreadySeen.contains(bState)) {
			if (lastState != null && statesAlreadySeen.contains(lastState))
				throw new IllegalArgumentException("lastState");
			if (lastState != null)
				statesAlreadySeen.add(lastState);
			lastState = (BitBoardState) state;
			
		}
		
//		if(((BitBoardState) state).getGamePhase() == BitBoardState.MIDGAME && !statesAlreadySeen.contains(((BitBoardState) state).getHash())) {
//			statesAlreadySeen.add(((BitBoardState) state).getHash());
//		}
	}
	
	public void removeState(IState state) {
//		statesAlreadySeen.remove(((BitBoardState) state).getHash());
//		if (lastState != null && !lastState.equals(state))
//			throw new IllegalArgumentException("lastState\n" + state + "\n" + lastState);
		if (lastState != null)
			lastState = null;
		else
			statesAlreadySeen.remove(state);
	}

}
