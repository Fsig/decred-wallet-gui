package com.deadendgine;

/**
 * A simple Enum to define each of the game's states.
 * We use these to determine what state the game is 
 * currently in.
 * 
 * @author Troy
 * @version 1.00
 *
 */
public enum GameState {
	INITILISING,
	MAIN_MENU,
	PAUSE_MENU,
	OPTIONS_MENU,
	GRAPHICS_MENU,
	SOUND_MENU,
	INVENTORY_MENU,
	PLAYING,
	GAME_OVER,
	CREDITS;
}
