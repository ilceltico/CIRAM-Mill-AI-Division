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
		BitBoardEntry entry = tranpositionTable.get(bHash.getHash());
		if (entry == null)
			return new BitBoardAction[0];
		
		BitBoardAction[] results = new BitBoardAction[2];
		for (int i=0; i<results.length; i++) {
			//TODO Applica simmetrie all'azione in ordine inverso
			//Occhio a creare una valued action nuova, niente riferimenti.
			results[i] = entry.actions[i];
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
		
		BitBoardAction actionToPut = (BitBoardAction) action;
		//TODO Applica simmetrie all'azione
		

		BitBoardEntry entry = tranpositionTable.get(bHash.getHash());
		if (entry == null) {
			entry = new BitBoardEntry();
			entry.setDepth(depth);
			entry.setActions(new BitBoardAction[]{actionToPut, 
					actionToPut});
			
			tranpositionTable.put(bHash.getHash(), entry);
		} else {
			if (entry.getDepth() <= depth) {
				entry.getActions()[0] = actionToPut;
			} else {
				entry.getActions()[1] = actionToPut;
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
