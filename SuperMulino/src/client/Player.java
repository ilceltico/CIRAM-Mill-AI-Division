package client;

import everythingelse.ValuedAction;
import everythingelse.Action;
import everythingelse.State;

public interface Player {
	
	Action minimaxDecision(State state, int maxDepth);
	
	ValuedAction max(State state, int maxDepth, int currentDepth, int alpha, int beta);
	
	ValuedAction min(State state, int maxDepth, int currentDepth, int alpha, int beta);
}