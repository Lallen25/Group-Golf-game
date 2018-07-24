import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.function.*;
import java.util.Random;
import java.io.FileReader;

import org.jsfml.audio.*;
import org.jsfml.system.*;
import org.jsfml.window.*;
import org.jsfml.window.event.*;
import org.jsfml.graphics.*;
/**
*	Represents a screen that displayes a series of levels that the user can choose from.
*	These levels can be locked and unlocked as needed.
*	Class is almost fully dynamic, however does use a variable to store how many worlds there are.
*/
public class LevelSelect extends Screen
{
	
	//Background File
	String Background;
	String Title  = "";
	String LeftArrow = "";
	String RightArrow = "";
	String Level = "";
	//Buttons that act as the levels that can be selected
	ArrayList<LevelButton> levelButtons = new ArrayList<LevelButton>( );
	
	public FileReader fileReader;
	/**
	*	Creates a screen that displays nince levels to the user.
	*	@param window Represents the window object that the objects will be drawn to
	*	@param gameStatein Integer representing the current state of the game. Level select screens are multiples of 10, with their 9 levels following them incrementally (i.e world one is state 10, with it's levels being represented by numbers 11 to 19. Note that 0-9 is used internally)
	*	@param worldNum A more concrete representation of the current selected world
	*/
	public LevelSelect(RenderWindow window, int gameStateIn, int worldNum)
	{
		String baseFilepath = "./assets/images/level_selection/world_" + worldNum + "/";
		
		//Background
		Background = "./assets/images/backgrounds/WORLD" + worldNum + "LEVELSELECT.jpg";
		objects.add(new Image(centreX, centreY, Background));
		
		int numWorlds = 2;

		//Loads the file that stores if levels are locked
		String lockedFilePath = "./levels/" + worldNum + "/lockedList.txt";
		try
		{
			fileReader = new FileReader(lockedFilePath);
		}
		catch(IOException ex)
		{
			System.out.println("File does not exist");
		}
		
		//Displays the levels on the screen
		for (int y = 0; y < 3; y ++)
		{
			for (int x = 0; x < 3; x ++)
			{
				int courseLocked = 0;
				try
				{
					//0 represents unlocked, 1 is locked
					courseLocked = (fileReader.read() - 48);
				}
				catch(IOException ex)
				{
					System.out.println("LS: Error outputting character");
				}
				
				int levelNum = ((y*3) + (x+1));
				String levelFilePath = "level selection/W" + worldNum + "/L" + levelNum;
				
				//Loading the button
				levelButtons.add(new LevelButton((320 + (640*x)),(180 + (360*(y))),levelFilePath, courseLocked));
				//levelLocked = true;
			}
			
		}

		//Arrows Left and Right to move between worlds
		if (worldNum != 1)
		{
			buttons.add(new Button(40, 540,"level selection/LEVELBUTTONS/LEFT"));
		}
		else
		{
			//If on world 1 don't draw left arrow
		}
		
		if (worldNum != numWorlds)
		{		
			buttons.add(new Button(1875, 540,"level selection/LEVELBUTTONS/RIGHT"));
		}
		else
		{
			//If at max levels don't draw right arrow
		}
		
		//Back button
		buttons.add(new Button(50, 1030,"level selection/LEVELBUTTONS/BACK"));
		
		//Clouds over the top of the levels
		for(int cloudCount = 0; cloudCount < 15; cloudCount++)
		{
			objects.add(new Cloud());
		}
		//Title at top
		objects.add(new Image (centreX, 50,	"./assets/images/backgrounds/level_selection/" + worldNum + ".png"));
		
	}
	
	
	/**
	*	Handles the translation between the button that's clicked and the action associated with it
	*	@param name String representing the last part of the file path of the button
	*	@param gameStateIn Represents the current state of the game, and is used to alter values by a given amount
	*	@return The new state of the game, where the menu system will go next.
	*/
	int clicked(String name, int gameStateIn)
	{
		System.out.println("LS: Button Pressed in Clicked: " + name);
		if (!name.substring(18, 20).equals("/L"))
		{
			//A level select button
			switch (name)
			{
				case "level selection/LEVELBUTTONS/LEFT":
					return (gameStateIn -10);
				case "level selection/LEVELBUTTONS/RIGHT":
					return (gameStateIn +10);
				case "level selection/LEVELBUTTONS/BACK":
					return 0;
			}
			
		}
		//A NORMAL BUTTON
		else
		{
			//Dealing with level select
			int levelSelected = Integer.parseInt(name.substring(20, 21));
			int currentWorld =  Integer.parseInt(name.substring(17, 18));

			System.out.println("LS: Course " + levelSelected + " has been selected");
			return ((currentWorld * 10) + levelSelected);
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
		System.out.println("LS: Gets into levelSelect");
		//
		// Main loop
		//
		while (window.isOpen( ) && gameStateIn == gameState)
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
			
			for (LevelButton levelButton : levelButtons)
			{
				boolean isOver = levelButton.checkMouse(Mouse.getPosition(), levelButton);

				levelButton.setState(isOver);
				levelButton.draw(window);
			}

			// Update the display with any changes
			window.display( );

			// Handle any events
			for (Event event : window.pollEvents( ))
			{
				switch(event.type)
				{
					//Close Button
					case CLOSED:
						System.out.println("LS: The user pressed the close button!");
						//Exit the game completely
						return -1;
					case MOUSE_BUTTON_PRESSED:
					System.out.println("#############################################");
						if(Mouse.isButtonPressed(Mouse.Button.LEFT))
						{
							xMouse = Mouse.getPosition().x;
							yMouse = Mouse.getPosition().y;
							System.out.println("LS: Click Event at: X " + xMouse + " Y " + yMouse);
							//Buttons are in two arrays so that the menu buttons take priority over the level select buttons
							//Buttons that don't select a level
							for (Button button : buttons)
							{
								if(button.mouseOver == true)
								{
									System.out.println(button.name);
									//Return value here handles where the game goes next
									int result =  clicked(button.name, gameStateIn);
									return result;									
								}
							}
							//Buttons that select a level
							for (LevelButton levelButton : levelButtons)
							{
								if(levelButton.mouseOver == true)
								{
									System.out.println(levelButton.name);
									//Return value here handles where the game goes next
									int result = clicked(levelButton.name, gameStateIn);
									System.out.println("LS: Result of level click: " + result);
									return result;	
								}
							}
						}
						break;
					case KEY_PRESSED:
						KeyEvent keyEvent = event.asKeyEvent();
						System.out.println("LS: The user pressed the following key: " + keyEvent.key);
						if(Keyboard.isKeyPressed(Keyboard.Key.ESCAPE))
						{
							//Go back to the menu
							return 0;	
						}
					default:
						break;	
				}
			}
			//Otherwise loop back to top
			//return gameStateIn;
		}
		return 0;
	}
}