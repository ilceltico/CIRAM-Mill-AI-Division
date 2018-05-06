package it.unibo.ai.mulino.CIRAMill.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BitBoardUtils {
	
	public static final HashMap<String, Byte> positionsDictionary = new HashMap<String, Byte>() {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

	{
		//Outer square
		put("a7", (byte) 0);
		put("d7", (byte) 1);
		put("g7", (byte) 2);
		put("g4", (byte) 3);
		put("g1", (byte) 4);
		put("d1", (byte) 5);
		put("a1", (byte) 6);
		put("a4", (byte) 7);
		
		//Middle square
		put("b6", (byte) 8);
		put("d6", (byte) 9);
		put("f6", (byte) 10);
		put("f4", (byte) 11);
		put("f2", (byte) 12);
		put("d2", (byte) 13);
		put("b2", (byte) 14);
		put("b4", (byte) 15);
		
		//Inner square
		put("c5", (byte) 16);
		put("d5", (byte) 17);
		put("e5", (byte) 18);
		put("e4", (byte) 19);
		put("e3", (byte) 20);
		put("d3", (byte) 21);
		put("c3", (byte) 22);
		put("c4", (byte) 23);
	}};
	
	public static int boardFromPosition(String position) {
		return 1 << positionsDictionary.get(position);
	}
	
	/*
	 * attualmente questi qui sotto prendono interi in base 10
	 * pure il secondo ritorna un intero in base 10
	 */	
	
	//TODO
	public static boolean hasCompletedMorris(int[] board, int position, byte player) {
		return true;
	}
	
	//TODO
	public static int[] getAdjacentPositions(int position) {
		return null;
	}
}
