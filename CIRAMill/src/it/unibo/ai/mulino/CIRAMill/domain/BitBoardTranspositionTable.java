package it.unibo.ai.mulino.CIRAMill.domain;

import org.apache.commons.collections4.map.LRUMap;

import it.unibo.ai.mulino.CIRAMill.domain.BitBoardState.BitBoardHash;
import it.unibo.ai.mulino.CIRAMill.minimax.IAction;
import it.unibo.ai.mulino.CIRAMill.minimax.IState;
import it.unibo.ai.mulino.CIRAMill.minimax.ITranspositionTable;

public class BitBoardTranspositionTable implements ITranspositionTable {
	public static final int DEFAULT_SIZE = 5600000; //MAX SIZE is 5600000
	
	private LRUMap<Long, BitBoardEntry> tranpositionTable;
	
	public BitBoardTranspositionTable(int size) {
		tranpositionTable = new LRUMap<>(size, size);
	}
	
	public BitBoardTranspositionTable() {
		this(DEFAULT_SIZE);
	}

	@Override
	public IAction[] getActions(IState state) {
		BitBoardState bState = (BitBoardState) state;
		BitBoardHash bHash = bState.getHash();
		byte symms = bHash.getSymms();
		BitBoardEntry entry = tranpositionTable.get(bHash.getHash());
		if (entry == null)
			return new BitBoardAction[0];
		
		BitBoardAction[] results = new BitBoardAction[2];
		int from;
		int to;
		int remove;
		
		for (int i=0; i<results.length; i++) {			
			from = entry.actions[i].getFrom();
			to = entry.actions[i].getTo();
			remove = entry.actions[i].getRemove();
			
			if((symms & BitBoardState.COLOR_INVERSION) != 0) {
				/*
				 * per l'azione penso non serva
				 */
			}
			
			if((symms & BitBoardState.INSIDE_OUT) != 0) {
				from = BitBoardUtils.insideOut(from);
				to = BitBoardUtils.insideOut(to);
				remove = BitBoardUtils.insideOut(remove);
			}
			
			if((symms & BitBoardState.VERTICAL_FLIP) != 0) {
				from = BitBoardUtils.verticalFlip(from);
				to = BitBoardUtils.verticalFlip(to);
				remove = BitBoardUtils.verticalFlip(remove);
			}
			
			switch(symms & 0b00011) {
			case BitBoardState.ROTATION_90:
				from = BitBoardUtils.rotationAnticlockwise(from);
				to = BitBoardUtils.rotationAnticlockwise(to);
				remove = BitBoardUtils.rotationAnticlockwise(remove);
				break;
			case BitBoardState.ROTATION_180:
				from = BitBoardUtils.rotationAnticlockwise(BitBoardUtils.rotationAnticlockwise(from));
				to = BitBoardUtils.rotationAnticlockwise(BitBoardUtils.rotationAnticlockwise(to));
				remove = BitBoardUtils.rotationAnticlockwise(BitBoardUtils.rotationAnticlockwise(remove));
				break;
			case BitBoardState.ROTATION_270:
				from = BitBoardUtils.rotationClockwise(from);
				to = BitBoardUtils.rotationClockwise(to);
				remove = BitBoardUtils.rotationClockwise(remove);
				break;
			default:
			}
			
			results[i] = new BitBoardAction(from, to, remove);
		}
		
		if (results[0].equals(results[1])) {
			return new BitBoardAction[] {results[0]};
		}
		
		return results;
	}

	@Override
	public void putAction(IState state, IAction action, int depth) {
		if (action == null)
			throw new IllegalArgumentException("action\n" + state);
		BitBoardState bState = (BitBoardState) state;
//		System.out.println(bState);
		BitBoardHash bHash = bState.getHash();
		byte symms = bHash.getSymms();
		
		BitBoardAction actionToPut = (BitBoardAction) action;
		
		int from = actionToPut.getFrom();
		int to = actionToPut.getTo();
		int remove = actionToPut.getRemove();
		
		switch(symms & 0b00011) {
		case BitBoardState.ROTATION_90:
			from = BitBoardUtils.rotationClockwise(from);
			to = BitBoardUtils.rotationClockwise(to);
			remove = BitBoardUtils.rotationClockwise(remove);
			break;
		case BitBoardState.ROTATION_180:
			from = BitBoardUtils.rotationClockwise(BitBoardUtils.rotationClockwise(from));
			to = BitBoardUtils.rotationClockwise(BitBoardUtils.rotationClockwise(to));
			remove = BitBoardUtils.rotationClockwise(BitBoardUtils.rotationClockwise(remove));
			break;
		case BitBoardState.ROTATION_270:
			from = BitBoardUtils.rotationAnticlockwise(from);
			to = BitBoardUtils.rotationAnticlockwise(to);
			remove = BitBoardUtils.rotationAnticlockwise(remove);
			break;
		default:
		}
		
		if((symms & BitBoardState.VERTICAL_FLIP) != 0) {
			from = BitBoardUtils.verticalFlip(from);
			to = BitBoardUtils.verticalFlip(to);
			remove = BitBoardUtils.verticalFlip(remove);
		}
		
		if((symms & BitBoardState.INSIDE_OUT) != 0) {
			from = BitBoardUtils.insideOut(from);
			to = BitBoardUtils.insideOut(to);
			remove = BitBoardUtils.insideOut(remove);
		}

		if((symms & BitBoardState.COLOR_INVERSION) != 0) {
			/*
			 * per l'azione penso non serva
			 */
		}
		
		BitBoardAction transformedActionToPut = new BitBoardAction(from, to, remove);		

		BitBoardEntry entry = tranpositionTable.get(bHash.getHash());
		if (entry == null) {
			entry = new BitBoardEntry();
			entry.setDepth(depth);
			entry.setActions(new BitBoardAction[]{transformedActionToPut, 
					transformedActionToPut});
			
			tranpositionTable.put(bHash.getHash(), entry);
		} else {
			if (entry.getDepth() <= depth) {
				entry.getActions()[0] = transformedActionToPut;
			} else {
				entry.getActions()[1] = transformedActionToPut;
			}
		}
	}
	
	class BitBoardEntry {
		private BitBoardAction[] actions;
		private int depth;
		
		BitBoardEntry() {
		}

		BitBoardAction[] getActions() {
			return actions;
		}

		void setActions(BitBoardAction[] actions) {
			this.actions = actions;
		}

		int getDepth() {
			return depth;
		}

		void setDepth(int depth) {
			this.depth = depth;
		}
	}

}
