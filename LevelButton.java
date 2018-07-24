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
/*
*	An extension of Button that allows levels to be shown as lockable
*/
public class LevelButton extends Button
{
	public boolean clickable;
	public boolean mouseOver;
	public boolean isClicked;
	public boolean levelLocked;
	Sprite img;
	
	Texture normal;
	Texture hover;
	Texture locked;
	
	String name;
	
	/**
	*	Used to create an instance of a level button. 
	*	@param x The horizontal location of the centre of the button
	*	@param y The vertical location of the centre of the button
	*	@param buttonName The last part of the path of the button, referenced from the button folder.
	*/
	public LevelButton(int x, int y, String buttonName, int isLevelLocked)
	{
		super(x,y,buttonName);
		
		if (isLevelLocked == 0)
		{
			levelLocked = false;
		}
		else
		{
			levelLocked = true;
		}
		
		mouseOver = false;
		isClicked = false;
		name = buttonName;
		
		int height;
		int width;
		Vector2i size;
		//
		// Load image/ texture
		//
		normal = new Texture( );
		hover = new Texture( );
		locked = new Texture( );

		try
		{

			normal.loadFromFile(Paths.get("./assets/images/buttons/" + buttonName + "/NORMAL.png"));

			hover.loadFromFile(Paths.get("./assets/images/buttons/" + buttonName + "/HOVER.png"));

			locked.loadFromFile(Paths.get("./assets/images/buttons/" + buttonName + "/LOCKED.png"));

		}
		catch (IOException ex)
		{
			System.out.println(buttonName);
			ex.printStackTrace( );
		}
		
		normal.setSmooth(true);
		hover.setSmooth(true);
		locked.setSmooth(true);
		
		Vector2i normalTextureSize = normal.getSize();
		height = normalTextureSize .x;
		width = normalTextureSize .y;
		if (levelLocked)
		{			
			img = new Sprite(locked);
		}
		else
		{
			img = new Sprite(normal);
		}
		
		img.setOrigin(Vector2f.div(new Vector2f(normal.getSize()), 2));

		Vector2f position = new Vector2f((float)x,(float)y);
		img.setPosition(position);

		this.x = x;
		this.y = y;

		obj = img;
		
	}
	
	/**
	*	Checks to see if the mouse cursor is over a specified button
	*	@param mousePos A 2D vector that represents the current mouse position
	*	@param butotn The button being checked
	*	@return A boolean value that is true if the mouse is within the boudsn of the button
	*/
	public boolean checkMouse(Vector2i mousePos, LevelButton leveButton)
	{
		FloatRect buttonBound = this.img.getGlobalBounds();

		Vector2f mousePosFloat = new Vector2f(mousePos);
		
		boolean mouseIsOver = (buttonBound.contains(mousePosFloat) & !levelLocked);
		return mouseIsOver;
	}
	
	/**
	*	Sets the texture of the button depending on its state
	*	@param isOver If the button currently has the mouse cursor over it
	*/
	//Sets the texture of the button and onHover state to true
	@Override
	public void setState(boolean isOver)
	{
		if (isOver == true & levelLocked == false)
		{
			mouseOver = true;
			//Switch to Hover Image
			img.setTexture(hover);
		}
		else if(levelLocked)
		{
			img.setTexture(locked);
		}
		else
		{
			mouseOver = false;
			//Switch to Normal Image
			img.setTexture(normal);
		}	
	}
	/**
	*	Unlock the level
	*/
	public void unlockLevel()
	{
		this.levelLocked = false;
	}

}