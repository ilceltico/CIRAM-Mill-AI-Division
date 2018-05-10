package it.unibo.ai.mulino.CIRAMill.domain;

import java.util.ArrayList;

import it.unibo.ai.mulino.CIRAMill.minimax.IState;
import it.unibo.ai.mulino.CIRAMill.minimax.ITieChecker;

public class ListTieChecker implements ITieChecker {
	private ArrayList<BitBoardState> statesAlreadySeen = new ArrayList<>();
	
	@Override
	public boolean isTie(IState state) {
//		System.out.println(statesAlreadySeen.size());
		return statesAlreadySeen.contains(state);
	}
	
	public void addState(IState state) {
		// probabilmente non serve il controllo perche'
		// aggiungere un doppione equivale al pareggio
		//
		// come controllare la fase dello stato (e' privato) (getter suppongo)
		if(!statesAlreadySeen.contains(state))
			statesAlreadySeen.add((BitBoardState) state);
	}
	
	public void removeState(IState state) {
		statesAlreadySeen.remove(state);
	}

}
