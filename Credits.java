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
*	Represents a screen that displays a set of credits to the user
*/
public class Credits extends Screen
{
	
	String Background = "./assets/images/backgrounds/main menu/BLANKBACKGROUND.png";
	
	/**
	*	Creates a screen that displayes credits to the user
	*	@param window The window that objects will be drawn to
	*	@param gameStateIn The current state of the game
	*/
	public Credits(RenderWindow window, int gameStateIn)
	{
		System.out.println("Gets into Credits");
	
		objects.add(new Image(centreX, centreY, Background));

		buttons.add(new Button(50, 1030,"level selection/LEVELBUTTONS/BACK"));
		
		//Could be more dynamic and extensible
		CreditImage tom = new CreditImage("TOM", 1, objects);
		CreditImage sam = new CreditImage("SAM", 2, objects);
		CreditImage joseph = new CreditImage("JOSEPH", 3, objects);
		CreditImage alex = new CreditImage("ALEX", 4, objects);
		CreditImage chris = new CreditImage("CHRIS", 5, objects);
		CreditImage lewis = new CreditImage("LEWIS", 6, objects);
	
		//Clouds here
		for(int cloudCount = 0; cloudCount < 25; cloudCount++)
		{
			objects.add(new Cloud());
		}
		//End of Clouds
	
	}
	/**
	*	Handles the translation between the button that's clicked and the action associated with it
	*	@param name String representing the last part of the file path of the button
	*	@param gameStateIn Represents the current state of the game, and is used to alter values by a given amount
	*	@return The new state of the game, where the menu system will go next.
	*/
	int clicked(String name)
	{
		switch (name)
		{
			case "level selection/LEVELBUTTONS/BACK":
				return 0;
			default:
				System.out.println("Error on Click");
				break;
		}
		return 0;
	}
	
	/**
	*	Acts as the main game loop for this part of the game
	*	@param window The window that any objects will be drawn to
	*	@param gameStateIn Represents the current state of the game, and is used to alter values by a given amount
	*	@return The next place the game will go to.
	*/	
	int run (RenderWindow window, int gameStateIn)
	{
		int gameState = gameStateIn;
		System.out.println("Gets into levelSelect");
		//
		// Main loop
		//
		while (window.isOpen( ) && gameStateIn == gameState)
		{
			// Clear the screen
			window.clear(Color.WHITE);
			
			// Move all the actors around
			//Images
			for (Object object : objects)
			{
				object.calcMove(0, 0, screenWidth, screenHeight);
				object.performMove( );
				object.draw(window);
			}
			//Buttons
			for (Button button : buttons)
			{
				boolean isOver = button.checkMouse(Mouse.getPosition(), button);

				button.setState(isOver);
				button.draw(window);
			}
			// Update the display with any changes
			window.display( );

			// Handle any events
			for (Event event : window.pollEvents( ))
			{
				switch(event.type)
				{
					case CLOSED:
						System.out.println("The user pressed the close button!");

						return -1;
					case MOUSE_BUTTON_PRESSED:
					System.out.println("#############################################");
						if(Mouse.isButtonPressed(Mouse.Button.LEFT))
						{
							xMouse = Mouse.getPosition().x;
							yMouse = Mouse.getPosition().y;
							System.out.println("Click Event at: X " + xMouse + " Y " + yMouse);

							for (Button button : buttons)
							{
								if(button.mouseOver == true)
								{
									System.out.println(button.name);
									//Return value here handles where the game goes next
									int result =  clicked(button.name);
									System.out.println("Result of click: " + result);
									return result;									
								}
							}
							
						}
						break;
					case KEY_PRESSED:
						KeyEvent keyEvent = event.asKeyEvent();
						System.out.println("The user pressed the following key: " + keyEvent.key);
						if(Keyboard.isKeyPressed(Keyboard.Key.ESCAPE))
						{
							return 0;
						}							
						break;
						
					default:	
				}
			}
		}
		return gameStateIn;
	}
	
	
	

}