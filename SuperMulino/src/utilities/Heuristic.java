package utilities;

import everythingelse.State;
import everythingelse.WrongPositionException;
import everythingelse.State.Checker;
import utilities.Util;

public class Heuristic {
	public static int heuristic(State state, String position, Checker p) throws Exception {
		// // controllo se vado in pareggio
		// if (statesAlreadySeen.contains(state))
		// return 0;
		// NON NECESSARIO, GIA' PRESENTE IN MIN E MAX

		switch (state.getCurrentPhase()) {
		case FIRST:
			return heuristicPhase1(state, position, p);
		case SECOND:
			return heuristicPhase2(state, position, p);
		case FINAL:
			return heuristicPhaseFinalOr2(state, position, p);
		default:
			throw new Exception("Illegal Phase");
		}
	}

	private static int heuristicPhaseFinalOr2(State state, String position, Checker p) {
		if (p == Checker.WHITE) {
			if (state.getWhiteCheckersOnBoard() > 3)
				return heuristicPhase2(state, position, p);
			else {
				return heuristicPhaseFinal(state, position, p);
			}
		}
		// Player is BLACK
		else {
			if (state.getBlackCheckersOnBoard() > 3)
				return heuristicPhase2(state, position, p);
			else {
				return heuristicPhaseFinal(state, position, p);
			}
		}
	}

	private static int heuristicPhaseFinal(State state, String position, Checker p) {
		int result = 0;

		// number of 2 pieces configuration
		int twoPiecesConfigurationPlayer = 0;
		int twoPiecesConfigurationOtherPlayer = 0;

		// number of 3 pieces configuration
		int threePiecesConfigurationPlayer = 0;
		int threePiecesConfigurationOtherPlayer = 0;

		for (String pos : state.positions) {
			// number of 2 pieces configuration
			if (state.getBoard().get(pos) == player) {
				int emptyPosH = 0;
				int occupiedPosH = 0;

				String[] hSet = Util.getHSet(pos);

				for (String hPos : hSet) {
					if (state.getBoard().get(hPos) == player)
						occupiedPosH++;
					if (state.getBoard().get(hPos) == Checker.EMPTY)
						emptyPosH++;
				}

				if (emptyPosH == 1 && occupiedPosH == 2)
					twoPiecesConfigurationPlayer++;

				int emptyPosV = 0;
				int occupiedPosV = 0;

				String[] vSet = Util.getVSet(pos);

				for (String vPos : vSet) {
					if (state.getBoard().get(vPos) == player)
						occupiedPosV++;
					if (state.getBoard().get(vPos) == Checker.EMPTY)
						emptyPosV++;
				}

				if (emptyPosV == 1 && occupiedPosV == 2)
					twoPiecesConfigurationPlayer++;
			}

			if (state.getBoard().get(pos) == otherPlayer) {
				int emptyPosH = 0;
				int occupiedPosH = 0;

				String[] hSet = Util.getHSet(pos);

				for (String hPos : hSet) {
					if (state.getBoard().get(hPos) == otherPlayer)
						occupiedPosH++;
					if (state.getBoard().get(hPos) == Checker.EMPTY)
						emptyPosH++;
				}

				if (emptyPosH == 1 && occupiedPosH == 2)
					twoPiecesConfigurationOtherPlayer++;

				int emptyPosV = 0;
				int occupiedPosV = 0;

				String[] vSet = Util.getVSet(pos);

				for (String vPos : vSet) {
					if (state.getBoard().get(vPos) == otherPlayer)
						occupiedPosV++;
					if (state.getBoard().get(vPos) == Checker.EMPTY)
						emptyPosV++;
				}

				if (emptyPosV == 1 && occupiedPosV == 2)
					twoPiecesConfigurationOtherPlayer++;
			}

			// number of 3 pieces configuration
			if (state.getBoard().get(pos) == player) {
				int emptyPosH = 0;
				int occupiedPosH = 0;

				String[] hSet = Util.getHSet(pos);

				for (String hPos : hSet) {
					if (state.getBoard().get(hPos) == player)
						occupiedPosH++;
					if (state.getBoard().get(hPos) == Checker.EMPTY)
						emptyPosH++;
				}

				int emptyPosV = 0;
				int occupiedPosV = 0;

				String[] vSet = Util.getVSet(pos);

				for (String vPos : vSet) {
					if (state.getBoard().get(vPos) == player)
						occupiedPosV++;
					if (state.getBoard().get(vPos) == Checker.EMPTY)
						emptyPosV++;
				}

				if (emptyPosH == 1 && occupiedPosH == 2 && emptyPosV == 1 && occupiedPosV == 2)
					threePiecesConfigurationPlayer++;
			}

			if (state.getBoard().get(pos) == otherPlayer) {
				int emptyPosH = 0;
				int occupiedPosH = 0;

				String[] hSet = Util.getHSet(pos);

				for (String hPos : hSet) {
					if (state.getBoard().get(hPos) == otherPlayer)
						occupiedPosH++;
					if (state.getBoard().get(hPos) == Checker.EMPTY)
						emptyPosH++;
				}

				int emptyPosV = 0;
				int occupiedPosV = 0;

				String[] vSet = Util.getVSet(pos);

				for (String vPos : vSet) {
					if (state.getBoard().get(vPos) == otherPlayer)
						occupiedPosV++;
					if (state.getBoard().get(vPos) == Checker.EMPTY)
						emptyPosV++;
				}

				if (emptyPosH == 1 && occupiedPosH == 2 && emptyPosV == 1 && occupiedPosV == 2)
					threePiecesConfigurationOtherPlayer++;
			}
		}
		result += 10 * (twoPiecesConfigurationPlayer / 2);
		result -= 10 * (twoPiecesConfigurationOtherPlayer / 2);

		result += threePiecesConfigurationPlayer;
		result -= threePiecesConfigurationOtherPlayer;

		// closed morris
		if (p == player) {
			if (Util.hasCompletedTriple(state, position, p)) {
				result += 16;
			}
		} else { // other player
			if (Util.hasCompletedTriple(state, position, p)) {
				result -= 16;
			}
		}

		return result;
	}

	private static int heuristicPhase2(State state, String position, Checker p) {
		int result = 0;

		// closed morris
		if (p == player) {
			if (Util.hasCompletedTriple(state, position, p)) {
				result += 14;
			}
		} else { // other player
			if (Util.hasCompletedTriple(state, position, p)) {
				result -= 14;
			}
		}

		// morrises number
		int morrisClosed3player = 0;
		int morrisClosed3OtherPlayer = 0;

		// number of blocked oppenent pieces
		int blockedPiecesPlayer = 0;
		int blockedPiecesOtherPlayer = 0;

		// double morris
		int doubleMorrisPlayer = 0;
		int doubleMorrisOtherPlayer = 0;
		for (String pos : state.positions) {
			// morrises number
			if (state.getBoard().get(pos) == player) {
				if (Util.hasCompletedTriple(state, pos, player)) {
					morrisClosed3player++;
				}
			}

			if (state.getBoard().get(pos) == otherPlayer) {
				if (Util.hasCompletedTriple(state, pos, otherPlayer)) {
					morrisClosed3OtherPlayer++;
				}
			}

			// number of blocked oppenent pieces
			if (state.getBoard().get(pos) == otherPlayer) {
				boolean isBlocked = true;
				try {
					for (String adjPos : Util.getAdiacentTiles(pos)) {
						if (state.getBoard().get(adjPos) == Checker.EMPTY) {
							isBlocked = false;
						}
					}
				} catch (WrongPositionException e) {
					e.printStackTrace();
				}
				if (isBlocked) {
					blockedPiecesPlayer++;
				}
			}

			if (state.getBoard().get(pos) == player) {
				boolean isBlocked = true;
				try {
					for (String adjPos : Util.getAdiacentTiles(pos)) {
						if (state.getBoard().get(adjPos) == Checker.EMPTY) {
							isBlocked = false;
						}
					}
				} catch (WrongPositionException e) {
					e.printStackTrace();
				}
				if (isBlocked) {
					blockedPiecesOtherPlayer++;
				}
			}

			// double morris
			if (state.getBoard().get(pos) == player) {
				if (Util.isInHTriple(state, pos) && Util.isInVTriple(state, pos))
					doubleMorrisPlayer++;
			}

			if (state.getBoard().get(pos) == otherPlayer) {
				if (Util.isInHTriple(state, pos) && Util.isInVTriple(state, pos))
					doubleMorrisOtherPlayer++;

			}
		}
		result += 43 * (morrisClosed3player / 3);
		result -= 43 * (morrisClosed3OtherPlayer / 3);

		result += 10 * blockedPiecesPlayer;
		result -= 10 * blockedPiecesOtherPlayer;

		result += 42 * doubleMorrisPlayer;
		result -= 42 * doubleMorrisOtherPlayer;

		// pieces number
		if (player == Checker.WHITE)
			result += 6 * (state.getWhiteCheckersOnBoard() - state.getBlackCheckersOnBoard());
		else
			result += 6 * (state.getBlackCheckersOnBoard() - state.getWhiteCheckersOnBoard());

		// opened morris (che cazzo �???)

		return result;
	}

	private static int heuristicPhase1(State state, String position, Checker p) {
		int result = 0;

		// closed morris
		if (p == player) {
			if (Util.hasCompletedTriple(state, position, p)) {
				result += 18;
			}
		} else { // other player
			if (Util.hasCompletedTriple(state, position, p)) {
				result -= 18;
			}
		}

		// morrises number
		int morrisClosed3player = 0;
		int morrisClosed3OtherPlayer = 0;

		// number of blocked oppenent pieces
		int blockedPiecesPlayer = 0;
		int blockedPiecesOtherPlayer = 0;

		// number of 2 pieces configuration
		int twoPiecesConfigurationPlayer = 0;
		int twoPiecesConfigurationOtherPlayer = 0;

		// number of 3 pieces configuration
		int threePiecesConfigurationPlayer = 0;
		int threePiecesConfigurationOtherPlayer = 0;
		for (String pos : state.positions) {
			// morrises number
			if (state.getBoard().get(pos) == player) {
				if (Util.hasCompletedTriple(state, pos, player)) {
					morrisClosed3player++;
				}
			}

			if (state.getBoard().get(pos) == otherPlayer) {
				if (Util.hasCompletedTriple(state, pos, otherPlayer)) {
					morrisClosed3OtherPlayer++;
				}
			}

			// number of blocked oppenent pieces
			if (state.getBoard().get(pos) == otherPlayer) {
				boolean isBlocked = true;
				try {
					for (String adjPos : Util.getAdiacentTiles(pos)) {
						if (state.getBoard().get(adjPos) == Checker.EMPTY) {
							isBlocked = false;
						}
					}
				} catch (WrongPositionException e) {
					e.printStackTrace();
				}
				if (isBlocked) {
					blockedPiecesPlayer++;
				}
			}

			if (state.getBoard().get(pos) == player) {
				boolean isBlocked = true;
				try {
					for (String adjPos : Util.getAdiacentTiles(pos)) {
						if (state.getBoard().get(adjPos) == Checker.EMPTY) {
							isBlocked = false;
						}
					}
				} catch (WrongPositionException e) {
					e.printStackTrace();
				}
				if (isBlocked) {
					blockedPiecesOtherPlayer++;
				}
			}

			// number of 2 pieces configuration
			if (state.getBoard().get(pos) == player) {
				int emptyPosH = 0;
				int occupiedPosH = 0;

				String[] hSet = Util.getHSet(pos);

				for (String hPos : hSet) {
					if (state.getBoard().get(hPos) == player)
						occupiedPosH++;
					if (state.getBoard().get(hPos) == Checker.EMPTY)
						emptyPosH++;
				}

				if (emptyPosH == 1 && occupiedPosH == 2)
					twoPiecesConfigurationPlayer++;

				int emptyPosV = 0;
				int occupiedPosV = 0;

				String[] vSet = Util.getVSet(pos);

				for (String vPos : vSet) {
					if (state.getBoard().get(vPos) == player)
						occupiedPosV++;
					if (state.getBoard().get(vPos) == Checker.EMPTY)
						emptyPosV++;
				}

				if (emptyPosV == 1 && occupiedPosV == 2)
					twoPiecesConfigurationPlayer++;
			}

			if (state.getBoard().get(pos) == otherPlayer) {
				int emptyPosH = 0;
				int occupiedPosH = 0;

				String[] hSet = Util.getHSet(pos);

				for (String hPos : hSet) {
					if (state.getBoard().get(hPos) == otherPlayer)
						occupiedPosH++;
					if (state.getBoard().get(hPos) == Checker.EMPTY)
						emptyPosH++;
				}

				if (emptyPosH == 1 && occupiedPosH == 2)
					twoPiecesConfigurationOtherPlayer++;

				int emptyPosV = 0;
				int occupiedPosV = 0;

				String[] vSet = Util.getVSet(pos);

				for (String vPos : vSet) {
					if (state.getBoard().get(vPos) == otherPlayer)
						occupiedPosV++;
					if (state.getBoard().get(vPos) == Checker.EMPTY)
						emptyPosV++;
				}

				if (emptyPosV == 1 && occupiedPosV == 2)
					twoPiecesConfigurationOtherPlayer++;
			}

			// number of 3 pieces configuration
			if (state.getBoard().get(pos) == player) {
				int emptyPosH = 0;
				int occupiedPosH = 0;

				String[] hSet = Util.getHSet(pos);

				for (String hPos : hSet) {
					if (state.getBoard().get(hPos) == player)
						occupiedPosH++;
					if (state.getBoard().get(hPos) == Checker.EMPTY)
						emptyPosH++;
				}

				int emptyPosV = 0;
				int occupiedPosV = 0;

				String[] vSet = Util.getVSet(pos);

				for (String vPos : vSet) {
					if (state.getBoard().get(vPos) == player)
						occupiedPosV++;
					if (state.getBoard().get(vPos) == Checker.EMPTY)
						emptyPosV++;
				}

				if (emptyPosH == 1 && occupiedPosH == 2 && emptyPosV == 1 && occupiedPosV == 2)
					threePiecesConfigurationPlayer++;
			}

			if (state.getBoard().get(pos) == otherPlayer) {
				int emptyPosH = 0;
				int occupiedPosH = 0;

				String[] hSet = Util.getHSet(pos);

				for (String hPos : hSet) {
					if (state.getBoard().get(hPos) == otherPlayer)
						occupiedPosH++;
					if (state.getBoard().get(hPos) == Checker.EMPTY)
						emptyPosH++;
				}

				int emptyPosV = 0;
				int occupiedPosV = 0;

				String[] vSet = Util.getVSet(pos);

				for (String vPos : vSet) {
					if (state.getBoard().get(vPos) == otherPlayer)
						occupiedPosV++;
					if (state.getBoard().get(vPos) == Checker.EMPTY)
						emptyPosV++;
				}

				if (emptyPosH == 1 && occupiedPosH == 2 && emptyPosV == 1 && occupiedPosV == 2)
					threePiecesConfigurationOtherPlayer++;
			}
		}
		result += 26 * (morrisClosed3player / 3);
		result -= 26 * (morrisClosed3OtherPlayer / 3);

		result += blockedPiecesPlayer;
		result -= blockedPiecesOtherPlayer;

		result += 12 * (twoPiecesConfigurationPlayer / 2);
		result -= 12 * (twoPiecesConfigurationOtherPlayer / 2);

		result += 7 * threePiecesConfigurationPlayer;
		result -= 7 * threePiecesConfigurationOtherPlayer;

		// pieces number
		if (player == Checker.WHITE)
			result += 6 * (state.getWhiteCheckersOnBoard() - state.getBlackCheckersOnBoard());
		else
			result += 6 * (state.getBlackCheckersOnBoard() - state.getWhiteCheckersOnBoard());

		return result;
	}

}
