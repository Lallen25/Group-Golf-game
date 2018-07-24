import java.lang.Math;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.function.*;

import org.jsfml.system.*;
import org.jsfml.window.*;
import org.jsfml.window.event.*;
import org.jsfml.graphics.*;
/**
*	Class to create a conflict tile, which interacts with the ball, invisible and cast over those drawn to screen
**/
public class ConflictTiles extends Tiles
{
	FloatRect fRect;
	
	/**
	*	Constructor for conflict tiles
	*	@param xLen Width of the rectangle to be drawn
	*	@param yLen height of the reectangle to be drawn
	*	@param xPos x position for the top left corner to be placed
	*	@param yPos y position for the top left corner to be placed
	*	@param col Color of the tile, used in checking
	**/
	public ConflictTiles(float xLen, float yLen, float xPos, float yPos, Color col)
	{
		fRect = new FloatRect(xPos, yPos, xLen, yLen);
		c = col;
	}
	
	/**
	*	Checks whether the coordinates checked are within the rectangle
	*	@param x x coordinate to be checked
	*	@param y y coordinate to be checked
	*	@return boolean Whether the coordinates are inside the rectangle or not
	**/
	public boolean within(float x, float y)
	{
		return fRect.contains(x,y);
	}
}