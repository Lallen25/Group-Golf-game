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
*	Used to represnt a standard button tha can be clicked on
*/
public class Button extends Object
{
	//Different states of the buttpn
	public boolean clickable;
	public boolean mouseOver;
	public boolean isClicked;
	
	//Properties of the button
	Sprite img;
	Texture normal;
	Texture hover;
	String name;
	
	/**
	*	Used to create an instance of a button. 
	*	@param x The horizontal location of the centre of the button
	*	@param y The vertical location of the centre of the button
	*	@param buttonName The last part of the path of the button, referenced from the button folder.
	*/
	public Button(int x, int y, String buttonName)
	{
		clickable = true;
		mouseOver = false;
		isClicked = false;
		name = buttonName;
		
		int height;
		int width;
		Vector2i size;
		//
		// Load image textures
		//
		normal = new Texture( );
		hover = new Texture( );
		try
		{
			normal.loadFromFile(Paths.get("./assets/images/buttons/" + buttonName + "/NORMAL.png"));
			hover.loadFromFile(Paths.get("./assets/images/buttons/" + buttonName + "/HOVER.png"));
		}
		catch (IOException ex)
		{
			System.out.println(buttonName);
			ex.printStackTrace( );
		}
		normal.setSmooth(true);
		hover.setSmooth(true);

		Vector2i normalTextureSize = normal.getSize();
		height = normalTextureSize .x;
		width = normalTextureSize .y;
		
		img = new Sprite(normal);
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
	public boolean checkMouse(Vector2i mousePos, Button button)
	{
		//Gets the bounds of the button
		FloatRect buttonBound = this.img.getGlobalBounds();
		
		//Converts the mouse position to use float cooridnates
		Vector2f mousePosFloat = new Vector2f(mousePos);
		
		//Checks to see if th mouse cursor is within the bounds of the button
		return buttonBound.contains(mousePosFloat);
	}
	//Sets the texture of the button and onHover state to true
	/**
	*	Sets the texture of the button depending on its state
	*	@param isOver If the button currently has the mouse cursor over it
	*/
	public void setState(boolean isOver)
	{
		if (isOver == true & clickable == true)
		{
			mouseOver = true;
			//Switch to Hover Image
			img.setTexture(hover);
		}
		else
		{
			mouseOver = false;
			//Switch to Normal Image
			img.setTexture(normal);
		}	
	}

	
}