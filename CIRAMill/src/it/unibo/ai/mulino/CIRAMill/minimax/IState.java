package it.unibo.ai.mulino.CIRAMill.minimax;

import java.util.List;

public interface IState {
	
	public List<IAction> getFollowingMoves();
	public void move(IAction action);
	public void unmove(IAction action);
//	public IState applyMove(IAction action);
	public IState clone();
	public int getHeuristicEvaluation();
	public boolean isWinningState();
	
}
