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
*	Represents a screen that displays a set of menu options to the user that they can then click on and interact with
*
*/
class Menu extends Screen
{

	//Asset locations
	String FontFile  = "LucidaSansRegular.ttf";
	String Background = "./assets/images/backgrounds/main menu/BLANKBACKGROUND.png";
	String Title = "./assets/images/backgrounds/main menu/HOMETITLE.png";
	String MenuBackground = "./assets/images/backgrounds/main menu/HOMEMENU.png";
	String themeMusic = "./assets/audio/menuMusic.flac";
	String ambientBirds = "./assets/audio/ambient_birds.wav";
	
	//If the pause menu should be shown
	boolean showPause = false;
	boolean muteAudio = false;

	/**
	*	Creates an instance of a menu
	*	@param window Represents the window object that the objects will be drawn to
	*	@param gameStatein Integer representing the current state of the game.
	*
	*/
	public Menu(RenderWindow window, int gameStateIn)
	{

		System.out.println("MN: Gets into Menu");

		//Background
		objects.add(new Image(centreX, centreY, Background));
		
		//Clouds here
		for(int cloudCount = 0; cloudCount < 25; cloudCount++)
		{
			objects.add(new Cloud());
		}
		//End of Clouds
		
		//Static
		objects.add(new Image(centreX, centreY - 200 , Title));
		objects.add(new Image(centreX, centreY + 100 , MenuBackground));

		//Buttons
		buttons.add(new Button(centreX, centreY + 20, "main menu/PLAY"));
		buttons.add(new Button(centreX, centreY + 100, "main menu/LEVELEDITOR"));
		buttons.add(new Button(centreX - 182, centreY + 180, "main menu/SETTINGS"));
		buttons.add(new Button(centreX + 40, centreY + 180,"main menu/EXIT"));
		buttons.add(new Button(1785, 1030,"main menu/CREDITS"));
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
			case "main menu/PLAY":
			System.out.println("MN: User Clicked PLAY");
				return 10;
			case "main menu/LEVELEDITOR":
				return 3;
			case "main menu/SETTINGS":
				break;
			case "main menu/EXIT":
				return -1;
			case "main menu/CREDITS":
				return 2;
			default:
				System.out.println("MN: Error on Click");
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
		System.out.println("MN: Gets into run");

		//Main loop for this pasrt of the game
		while (window.isOpen( ) && gameStateIn == 0)
		{
			// Clear the screen
			window.clear(Color.WHITE);
			
			// Move all the actors around
			for (Object object : objects)
			{
				object.calcMove(0, 0, screenWidth, screenHeight);
				object.performMove( );
				object.draw(window);

			}
			for (Button button : buttons)
			{
				boolean isOver = button.checkMouse(Mouse.getPosition(), button);
				button.setState(isOver);
				button.draw(window);
			}
			
			// Update the display with any changes
			window.display( );

			// Handle events
			for (Event event : window.pollEvents( ))
			{
				switch(event.type)
				{
					//Exits the game completely
					case CLOSED:
						System.out.println("MN: The user pressed the close button!");
						return -1;
					case MOUSE_BUTTON_PRESSED:
					System.out.println("#############################################");
						if(Mouse.isButtonPressed(Mouse.Button.LEFT))
						{
							xMouse = Mouse.getPosition().x;
							yMouse = Mouse.getPosition().y;
							System.out.println("MN: Click Event at: X " + xMouse + " Y " + yMouse);
							for (Button button : buttons)
							{
								if(button.mouseOver == true)
								{
									System.out.println(button.name);
									return clicked(button.name);
								}
							}
						}
						break;
					case MOUSE_WHEEL_MOVED:
						System.out.println("MN: The user moved the mouse wheel!");
						break;		
					case KEY_PRESSED:
						KeyEvent keyEvent = event.asKeyEvent();
						System.out.println("MN: The user pressed the following key: " + keyEvent.key);
						if(Keyboard.isKeyPressed(Keyboard.Key.ESCAPE))
						{
							return -1;
						}						
						break;
					default:	
				}
			}
		}
		return 0;
	}
}