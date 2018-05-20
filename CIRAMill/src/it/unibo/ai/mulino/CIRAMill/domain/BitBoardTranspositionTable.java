package it.unibo.ai.mulino.CIRAMill.domain;

import java.util.List;

import org.apache.commons.collections4.map.LRUMap;

import it.unibo.ai.mulino.CIRAMill.domain.BitBoardState.BitBoardHash;
import it.unibo.ai.mulino.CIRAMill.minimax.IAction;
import it.unibo.ai.mulino.CIRAMill.minimax.IState;
import it.unibo.ai.mulino.CIRAMill.minimax.ITranspositionTable;

public class BitBoardTranspositionTable implements ITranspositionTable {
	public static final int DEFAULT_SIZE = 3600000; //MAX SIZE is 5600000
	
	private LRUMap<Long, BitBoardEntry> transpositionTable;
	
	public BitBoardTranspositionTable(int size) {
		transpositionTable = new LRUMap<>(size, size);
	}
	
	public BitBoardTranspositionTable() {
		this(DEFAULT_SIZE);
	}

	@Override
	public IAction[] getActions(IState state) {
		BitBoardState bState = (BitBoardState) state;
		BitBoardHash bHash = bState.getHash();
		byte symms = bHash.getSymms();
		BitBoardEntry entry = transpositionTable.get(bHash.getHash());
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
			
//			if((symms & BitBoardState.COLOR_INVERSION) != 0) {
//				/*
//				 * per l'azione penso non serva
//				 */
//				if (!bState.equals(entry.getState()[i])) {
//					System.out.println("Color");
//					bState.getHash();
//				}
//			}
			
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
				from = BitBoardUtils.rotationAnticlockwise90(from);
				to = BitBoardUtils.rotationAnticlockwise90(to);
				remove = BitBoardUtils.rotationAnticlockwise90(remove);
				break;
			case BitBoardState.ROTATION_180:
				from = BitBoardUtils.rotationClockwise180(from);
				to = BitBoardUtils.rotationClockwise180(to);
				remove = BitBoardUtils.rotationClockwise180(remove);
				break;
			case BitBoardState.ROTATION_270:
				from = BitBoardUtils.rotationClockwise90(from);
				to = BitBoardUtils.rotationClockwise90(to);
				remove = BitBoardUtils.rotationClockwise90(remove);
				break;
			default:
			}
			
			results[i] = new BitBoardAction(from, to, remove);
			
//			results[i] = new BitBoardAction(entry.actions[i].getFrom(), entry.actions[i].getTo(), entry.actions[i].getRemove());
		}
		
//		List<IAction> actions = state.getFollowingMoves();
//		for (int i=0; i<results.length; i++) {
//			if (!actions.contains(results[i]))
//				throw new IllegalArgumentException();
//		}
		
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
			from = BitBoardUtils.rotationClockwise90(from);
			to = BitBoardUtils.rotationClockwise90(to);
			remove = BitBoardUtils.rotationClockwise90(remove);
			break;
		case BitBoardState.ROTATION_180:
			from = BitBoardUtils.rotationClockwise180(from);
			to = BitBoardUtils.rotationClockwise180(to);
			remove = BitBoardUtils.rotationClockwise180(remove);
			break;
		case BitBoardState.ROTATION_270:
			from = BitBoardUtils.rotationAnticlockwise90(from);
			to = BitBoardUtils.rotationAnticlockwise90(to);
			remove = BitBoardUtils.rotationAnticlockwise90(remove);
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

//		if((symms & BitBoardState.COLOR_INVERSION) != 0) {
//			/*
//			 * per l'azione penso non serva
//			 */
//		}
		
		BitBoardAction transformedActionToPut = new BitBoardAction(from, to, remove);		

//		BitBoardAction transformedActionToPut = new BitBoardAction(actionToPut.getFrom(), actionToPut.getTo(), actionToPut.getRemove());		
		
		BitBoardEntry entry = transpositionTable.get(bHash.getHash());
		if (entry == null) {
			entry = new BitBoardEntry();
			entry.setDepth((byte) depth);
			entry.setActions(new BitBoardAction[]{transformedActionToPut, 
					transformedActionToPut});
			
//			entry.setState(new BitBoardState[] {(BitBoardState) bState.clone(), (BitBoardState) bState.clone()});
			
			transpositionTable.put(bHash.getHash(), entry);
		} else {
			if (entry.getDepth() <= depth) {
				entry.getActions()[0] = transformedActionToPut;
				
//				entry.getState()[0] = (BitBoardState) bState.clone();
				
			} else {
				entry.getActions()[1] = transformedActionToPut;
				
//				entry.getState()[1] = (BitBoardState) bState.clone();
			}
		}
	}
	
	public void clear() {
		transpositionTable = new LRUMap<>(DEFAULT_SIZE, DEFAULT_SIZE);
//		transpositionTable.clear();
	}
	
	class BitBoardEntry {
		private BitBoardAction[] actions;
		private byte depth;
//		private BitBoardState[] state;
		
		BitBoardEntry() {
		}

		BitBoardAction[] getActions() {
			return actions;
		}

		void setActions(BitBoardAction[] actions) {
			this.actions = actions;
		}

		byte getDepth() {
			return depth;
		}

		void setDepth(byte depth) {
			this.depth = depth;
		}

//		public BitBoardState[] getState() {
//			return state;
//		}
//
//		public void setState(BitBoardState[] state) {
//			this.state = state;
//		}
	}

}
