package it.unibo.ai.mulino.CIRAMill.minimax;

public interface IMinimax {
	
	public ValuedAction minimaxDecision(IState state, int maxDepth); 

}
