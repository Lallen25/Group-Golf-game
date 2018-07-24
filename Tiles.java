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
*	Abstract class to create a basic tile, containing functionality
**/
public abstract class Tiles
{
	Color BROWN = new Color(139,69,19); //Borders
	Color DGREEN = new Color(34,139,34); //Hills
	
	//Hill specifics
	Color HILLUPH = new Color(34,139,34);
	Color HILLDOWNH = new Color(33,139,34);
	Color HILLLEFT = new Color(34,138,34);
	Color HILLRIGHT= new Color(34,139,35);
	Color HILLUPV = new Color(34,138,34);
	Color HILLDOWNV= new Color(34,139,35);
	
	//Ball conflicting colours
	Color DGREY = new Color(105,105,105);
	Color PIPE = new Color(115,116,114);
	Color WATER = new Color(104,157,255);
	Color GRASS = new Color(91,127,0);

	Transformable obj;

	float x, y, xOrigin, yOrigin;
	float xVelocity, yVelocity;
	float coefF = 0.99f;		//Coefficient of Friction
	float xMouse, yMouse;
	Color c;
	
	/**
	*	Method to calculate the amount the ball will move in the coming refresh
	*	If ball modulus falls below 0.25 in both the x and y it will stop
	**/
	public void moveDifferential()
	{
		if((xVelocity < 0.25 && xVelocity > -0.25)&&(yVelocity < 0.25 && yVelocity > -0.25))
		{
			xVelocity = 0;
			yVelocity = 0;
		}
		else
		{
			xVelocity = xVelocity*coefF;
			yVelocity = yVelocity*coefF;
		}
	}
	
	/**
	*	Method to move the ball having calculated the displacement in moveDifferential, adding displacement to the current position
	**/
	public void move()
	{
		x = x + xVelocity;
		y = y + yVelocity;
		obj.move(xVelocity, yVelocity);
	}

	/**
	*	Method to draw the tiles to the renderWindow
	*	@param RenderWindow Render window being drawn to
	**/
	public void draw(RenderWindow r)
	{
		r.draw((Drawable)obj);
	}

	/**
	*	Method to set the velocity immediately after the mouse has been clicked
	*	@param xV The x velocity component
	*	@parm yV The y velocity component
	**/
	public void setInitialVelocity(float xV, float yV)
	{
		xVelocity = xV;
		yVelocity = yV;
	}

	/**
	*	Method to detect whether the white ball is inside any of the holes placed on the levels
	*	@param ball Takes the white ball in question to compare location to that of other balls
	*	@param inTiles Takes the array list to be checked through for collision
	*	@return int Returns a value which can be used to detect what type of hole has been collided with
	**/
	public int isInHole(Tiles ball, ArrayList<Tiles> inTiles)
	{
		for(Tiles tile : inTiles)
		{
			if(tile.getColor() == Color.GREEN)
			{
				if(ball.getX()+10 >= tile.getX() && ball.getY()+10 >= tile.getY() && ball.getX()+10 <= tile.getX()+30 && ball.getY()+10 <= tile.getY()+30) return 2;
			}
			if(tile.getColor() == Color.BLACK)
			{
				if(ball.getX()+10 >= tile.getX()-10 && ball.getY()+10 >= tile.getY()-10 && ball.getX()+10 <= tile.getX()+40 && ball.getY()+10 <= tile.getY()+40) return 1;
			}
			if(tile.getColor() == Color.MAGENTA)
			{
				if(ball.getX()+10 >= tile.getX() && ball.getY()+10 >= tile.getY() && ball.getX()+10 <= tile.getX()+30 && ball.getY()+10 <= tile.getY()+30) return 3;
			}	
		}
		return 0;
	}
	
	/**
	*	Method resets the ball to the start of the course
	**/
	public void resetBall()
	{
		x = xOrigin;
		y = yOrigin;
		xVelocity = 0;
		yVelocity = 0;
		obj.setPosition(xOrigin,yOrigin);
	}
	
	/**
	*	Method inverts the x velocity and adds a small increasing factor so it doesnt get stuck inside the wall
	**/
	public void invertXV()
	{
		xVelocity = xVelocity*-1.03f;
	}
	
	/**
	*	Method inverts the y velocity and adds a small increasing factor so it doesnt get stuck inside the wall
	**/
	public void invertYV()
	{
		yVelocity = yVelocity*-1.03f;
	}
	
	/**
	*	@return float x location
	**/
	public float getX()
	{
		return x;
	}
	
	/**
	*	@return float y location
	**/
	public float getY()
	{
		return y;
	}
	
	/**
	*	@return float x velocity
	**/
	public float getXV()
	{
		return xVelocity;
	}
	
	/**
	*	@return float y velocity
	**/
	public float getYV()
	{
		return yVelocity;
	}
	
	/**
	*	@return boolean whether ball is in motion
	**/
	public boolean isInMotion()
	{
		return(xVelocity != 0 || yVelocity != 0);
	}
	
	/**
	*	@return Color color of the tile
	**/
	public Color getColor()
	{
		return c;
	}
	
	/**
	*	Sets the location of the tile, calculated from the top left of the tile
	*	@param xPos the x position to be set to
	*	@param yPos the y position to be set to
	**/
	public void setLocation(float xPos, float yPos)
	{
		x = xPos;
		y = yPos;
		obj.setPosition(xPos,yPos);
	}
	
	/**
	*	@param f the coefficient of friction the tile has
	**/
	public void setFrict(float f)
	{
		coefF = f;
	}
}