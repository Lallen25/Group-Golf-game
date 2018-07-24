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
*	Class to create a rectangle which is invisible and acts as a border, covering that of the others drawn on screen
**/
public class BorderRectangle
{
	FloatRect fRect;
	
	/**
	*	Constructor to make the Border, drawn from the top left
	*	@param xLen the width
	*	@param yLen the height
	*	@param xPos place for x position of top left corner
	*	@param yPos place for y position of top left corner
	**/
	public BorderRectangle(float xLen, float yLen, float xPos, float yPos)
	{
		fRect = new FloatRect(xPos, yPos, xLen, yLen);
	}
	
	/**
	*	Checks whether the coordinates input are within the float rectangles
	*	@param x x position being checked
	*	@param y y position being checked
	*	@return boolean whether or not the coordinates are inside the rectangle
	**/
	public boolean within(float x, float y)
	{
		return fRect.contains(x,y);
	}
}