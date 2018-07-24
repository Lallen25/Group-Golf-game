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
*	Class to create a circle to be drawn onto the screen
**/
public class Circle extends Tiles
{
	/**
	*	Constructor to make a circle of specific colour, size, and location
	*	@param radius Radius of the circle
	*	@param xPos the x position of the top left corner to be placed
	*	@param yPos the y position of the top left corner to be placed
	*	@param col Color to make the circle
	**/
	public Circle(int radius, float xPos, float yPos, Color col)
	{
		CircleShape circ = new CircleShape(radius);
		circ.setFillColor(col);
		circ.setPosition(xPos, yPos);
		this.x = xPos;
		this.y = yPos;
		this.xOrigin = xPos;
		this.yOrigin = yPos;
		c = col;
		obj = circ;
	}
}