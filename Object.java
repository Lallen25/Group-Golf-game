import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.function.*;
import java.util.Random;

import org.jsfml.audio.*;
import org.jsfml.system.*;
import org.jsfml.window.*;
import org.jsfml.window.event.*;
import org.jsfml.graphics.*;
/**
*	Abstract classs that acts as a base class for other objects
*/
public abstract class Object
{
	Transformable obj;   // Base object

	int x  = 0;	// Current X-coordinate
	int y  = 0;	// Current Y-coordinate

	int r = 0;

	int dx = 0;	// Change in X-coordinate per cycle
	int dy = 0;	// Change in Y-coordinate per cycle
	/**
	*	Returns false
	*/
	boolean within (int x, int y)
	{
		// Should check object bounds here
		// -- we'd normally assume a simple rectangle
		//    ...and override as necessary
		return false;
	}

	/**
	*	Calculates the next location for the images
	*	@param minx The leftmost location the clouds can move to
	*	@param miny The topmost location the clouds can move to
	*	@param maxx The rightmost location the clouds can move to
	*	@param maxy The bottommost location the clouds can move to
	*/
	void calcMove(int minx, int miny, int maxx, int maxy)
	{
		//
		// Add deltas to x and y position
		//
		x += dx;
		y += dy;

	}
	/**
	*	Updates the position of the object on screen	
	*/
	void performMove( )
	{
		obj.setPosition(x,y);
	}
	
 	/**
	*	Renders the object on screen
	*/	
	void draw(RenderWindow w)
	{
		w.draw((Drawable)obj);
	}
}