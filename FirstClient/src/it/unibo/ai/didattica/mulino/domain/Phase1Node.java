package it.unibo.ai.didattica.mulino.domain;

import it.unibo.ai.didattica.mulino.actions.Phase1Action;

//A Phase1Node is a node reached with a Phase1Action.
//State in a Phase1Node can imply that we are now in Phase2.
public class Phase1Node {
	
	State state;
	Phase1Action action;
	
	public Phase1Node(State state, Phase1Action action) {
		this.state = state;
		this.action = action;
	}
	
	

}
