package it.unibo.ai.mulino.CIRAMill.domain;

import org.apache.commons.collections4.map.LRUMap;

import it.unibo.ai.mulino.CIRAMill.domain.BitBoardState.BitBoardHash;
import it.unibo.ai.mulino.CIRAMill.minimax.IState;
import it.unibo.ai.mulino.CIRAMill.minimax.ITranspositionTable;
import it.unibo.ai.mulino.CIRAMill.minimax.ValuedAction;

public class BitBoardTranspositionTable implements ITranspositionTable {
	public static final int DEFAULT_SIZE = 1; //TODO
	
	private LRUMap<Long, BitBoardEntry> tranpositionTable;
	
	public BitBoardTranspositionTable(int size) {
		tranpositionTable = new LRUMap<>(size, size);
	}
	
	public BitBoardTranspositionTable() {
		this(DEFAULT_SIZE);
	}

	@Override
	public ValuedAction[] getValuedActions(IState state) {
		BitBoardState bState = (BitBoardState) state;
		BitBoardHash bHash = bState.getHash();
		BitBoardEntry entry = tranpositionTable.get(bHash.getHash());
		if (entry == null)
			return new ValuedAction[0];
		
		ValuedAction[] results = new ValuedAction[2];
		for (int i=0; i<results.length; i++) {			
			//TODO Applica simmetrie all'azione in ordine inverso
			//Occhio a creare una valued action nuova, niente riferimenti.
		}
		
		if (results[0].getAction().equals(results[1].getAction())) {
			return new ValuedAction[] {results[0]};
		}
		return results;
	}

	@Override
	public void putValuedAction(IState state, ValuedAction valuedAction, int depth) {
		BitBoardState bState = (BitBoardState) state;
		BitBoardHash bHash = bState.getHash();
		
		BitBoardAction actionToPut = (BitBoardAction) valuedAction.getAction();
		//TODO Applica simmetrie all'azione
		

		BitBoardEntry entry = tranpositionTable.get(bHash.getHash());
		if (entry == null) {
			entry = new BitBoardEntry();
			entry.setDepth(depth);
			entry.setValuedActions(new ValuedAction[]{new ValuedAction(valuedAction.getAction(), valuedAction.getValue()), 
				new ValuedAction(valuedAction.getAction(), valuedAction.getValue())});
			
			tranpositionTable.put(bHash.getHash(), entry);
		} else {
			if (entry.getDepth() <= depth) {
				entry.getValuedActions()[0] = new ValuedAction(valuedAction.getAction(), valuedAction.getValue());
			} else {
				entry.getValuedActions()[1] = new ValuedAction(valuedAction.getAction(), valuedAction.getValue());
			}
		}
	}
	
	class BitBoardEntry {
		private ValuedAction[] valuedActions = new ValuedAction[2];
		private int depth;
		
		BitBoardEntry() {
		}

		ValuedAction[] getValuedActions() {
			return valuedActions;
		}

		void setValuedActions(ValuedAction[] valuedActions) {
			this.valuedActions = valuedActions;
		}

		int getDepth() {
			return depth;
		}

		void setDepth(int depth) {
			this.depth = depth;
		}
	}

}
