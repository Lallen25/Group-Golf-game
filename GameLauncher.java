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
*	Main launcher file, acts as a hub that all other levels are called from.
*/
public class GameLauncher
{
	public static int mainFramerate = 60;
	public static void main(String [] args)
	{
		
		int screenWidth  = 1920;
		int screenHeight = 1080;
		int centreX = screenWidth /2;
		int centreY = screenHeight/2;
		RenderWindow window;
		String themeMusic = "./assets/audio/menuMusic1.ogg";

		
		//Create new window
		String WindowTitle   = "Golfmania v1.0.3";
		
		window = new RenderWindow( );
		window.create(new VideoMode(screenWidth, screenHeight), WindowTitle, WindowStyle.FULLSCREEN);

		window.setFramerateLimit(mainFramerate);

		//Theme Music
		SoundObject theme = new SoundObject(themeMusic, 100);
		
		//Main Game Loop
		//gameState determines where the game will go next
		int gameState = 0;
		while (gameState != -1)
		{
			System.out.println("GL: GAME STATE = " + gameState);
			switch (gameState)
			{
				case 0 :
					//Menu
					Menu m = new Menu(window, gameState);
					gameState = m.run(window, gameState);
					break;
				case 1 :
					//Settings
					break;
				case 2 :
					//Credits
					Credits c = new Credits(window, gameState);
					gameState = c.run(window, gameState);
					break;
				case 3:
					//Level Editor
					LevelEditor le = new LevelEditor(window,gameState);
					gameState = le.run(window, gameState);
					break;
				case 10 : 
					//World One Level Select
					LevelSelect one = new LevelSelect(window, gameState,(gameState/10));
					gameState = one.run(window, gameState);
					break;
				case 20:
					//World Two Level Select
					LevelSelect two = new LevelSelect(window, gameState,(gameState/10));
					gameState = two.run(window, gameState);
					break;

				default:
					System.out.println("GL: Loading Level " + gameState);
					Game level = new Game(window, gameState);
					gameState = level.run(window, gameState);
					break;
					
			}
		
		}
		window.close();
	}
}