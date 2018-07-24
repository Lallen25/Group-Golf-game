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
*	Class to create a rectangle that can be drawn to screen
*	@param xLen Width of the rectangle to be drawn
*	@param yLen height of the reectangle to be drawn
*	@param xPos x position for the top left corner to be placed
*	@param yPos y position for the top left corner to be placed
*	@param col Color of the tile, used in checking
**/
public class Rectangle extends Tiles
{
	public Rectangle(float xLen, float yLen, float xPos, float yPos, Color col)
	{
		Vector2f vect = new Vector2f(xLen, yLen);
		RectangleShape rect = new RectangleShape(vect);
		rect.setFillColor(col);
		rect.setPosition(xPos, yPos);
		c = col;
		obj = rect;
	}
}