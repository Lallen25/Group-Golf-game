import java.lang.Math;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

import java.nio.file.*;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.function.*;
import java.util.Iterator;
import java.util.Scanner;


import org.jsfml.system.*;
import org.jsfml.window.*;
import org.jsfml.window.event.*;
import org.jsfml.graphics.*;

/**
* The level editor class contains the functionality required for the level editor the function.
*/
class LevelEditor extends Screen
{
	public ArrayList<Tiles> tiles = new ArrayList<Tiles>();	//ArrayList used to store overall tiles
	ArrayList<Tiles> keep = new ArrayList<Tiles>();			//ArrayList used to store tiles which should be kept when arrays are being reset
	ArrayList<BorderRectangle> horizBorders = new ArrayList<BorderRectangle>(); //ArrayList used to store the horizontal border tiles
	ArrayList<BorderRectangle> vertBorders = new ArrayList<BorderRectangle>();	//ArrayList used to store vertical border tiles
	ArrayList<ConflictTiles> conflict = new ArrayList<ConflictTiles>();			//ArrayList used to store tiles which conflict with other tiles
	ArrayList<Object> backgrounds = new ArrayList<Object>();
	
	public boolean showPause = false;
	String pauseMenuBackground = "./assets/images/backgrounds/pause menu/BACKGROUND.png"; //Loads the pause menu background
	String LEBackground = "./assets/images/backgrounds/level editor/background.jpg";
	RenderWindow window;

	int framerate = 180;	//Sets the framerate which the game runs at
	int level = 0;
	
	int tiler[] = {1,2,11,2,11,7,17,17,16,16,16,16,4,14,17,17,13,16,16,16,16,4,17,17,8,17,17,17,17,17,17,17}; //Initializes the default level constructor array
	int currentTile = 0; //CurrentTile represents the currently selected tile to be cycled through 
	
	Color BROWN = new Color(139,69,19);
	Color DGREEN = new Color(34,139,34);
	
	Color HILLUPH = new Color(34,139,34);
	Color HILLDOWNH = new Color(33,139,34);
	Color HILLLEFT = new Color(34,138,34);
	Color HILLRIGHT= new Color(34,139,35);
	
	Color DGREY = new Color(105,105,105);
	Color PIPE = new Color(115,116,114);
	Color WATER = new Color(104,157,255);
	Color GRASS = new Color(91,127,0);
	
	/**
	* Constructor for the level editor that takes a RenderWindow and a gamestate from the menu.
	* @param inputWindow The RenderWindow used to display the editor.
	* @param gameStateIn The value used to travel and return to the correct menus/levels.
	*/
	public LevelEditor(RenderWindow inputWindow, int gameStateIn)
	{
		System.out.println("LE: Gets into game level " + gameStateIn);
		this.window = inputWindow;
		
		objects.add(new Image(centreX, centreY, pauseMenuBackground));
		buttons.add(new Button(50, 1030,"pause menu/BACK"));
		buttons.add(new Button(120, 50, "level editor/LOAD"));
		buttons.add(new Button(300, 50, "level editor/SAVE"));
	}
	
	/**
	* The powerbar method is used to calculate where the mouse is in relation to the ball on click.
	* This is used to determine the direction and speed of the ball.
	* @return Nothing.
	*/
	public void Powerbar(Tiles ball)
	{
		double tempX = (double)Mouse.getPosition().x-(double)ball.getX()-10;
		double tempY = (double)Mouse.getPosition().y-(double)ball.getY()-10;
		float degrees = (float)Math.atan2(tempY, tempX)*57.2958f;
		float xV, yV;
		xV = (float)tempX*-0.05f;
		yV = (float)tempY*-0.05f;
		ball.setInitialVelocity(xV, yV);
	}
	
	/**
	* The clearArrays method is used to clear all the arrays used, which allows the level to be redrawn.
	* @return Nothing.
	*/
	public void clearArrays()
	{
		tiles.clear();
		horizBorders.clear();
		vertBorders.clear();
		conflict.clear();
		System.out.println("LE:  - ARRAYS CLEARED - ");
	}
	/**
	* The boundaries method detects if the ball has collided with a boundary and if so, inverts the relevant axis of the  direction of the ball.
	* @param tile the tile to be checked for collision
	*/
	public void boundaries(Tiles tile)
	{
		for(BorderRectangle xBorder : horizBorders)
		{
			if(xBorder.within(tile.getX(),tile.getY()))
			{
				tile.invertYV();
			}
		}
		for(BorderRectangle yBorder : vertBorders)
		{
			if(yBorder.within(tile.getX(),tile.getY()))
			{
				tile.invertXV();
			}
		}
	}
	
	/**
	* The hill method changes the speed and direction of the ball if it's on a hill tile.
	* @param xInc the amount to increment x by.
	* @param yInc the amount to increment y by.
	* @param ball the current ball being operated on.
	*/
	public void hill(float xInc, float yInc, Tiles ball)
	{
		ball.setInitialVelocity(ball.getXV()+xInc,ball.getYV()+yInc);
	}	
	/**
	* The interactions method checks the tile the ball is on and performs relevant actions depending on the result.
	* @param ball The tile to check for interactions.
	*/
	public void interactions(Tiles ball)
	{
		for(ConflictTiles con : conflict)
		{
			if(con.within(ball.getX(),ball.getY()))
			{
				
				if(con.getColor()==GRASS) ball.setFrict(0.99f);
				if(con.getColor()==Color.YELLOW) ball.setFrict(0.95f);
				if(con.getColor()==WATER) ball.resetBall();
				if(con.getColor()==HILLUPH) hill(-0.25f,0,ball);
				if(con.getColor()==HILLDOWNH) hill(0.25f,0,ball);
			}
		}
	}
	/**
	* The clicked method converts the button clicks to the relevant gameState
	* @param name The action being performed
	* @param The updated gameState 
	*/
	int clicked(String name, int gameState)
	{
		switch (name)
		{
			case "pause menu/BACK":
				return -1;
			case "level editor/LOAD":
				loadLevel();
				update();
				return gameState;
			case "level editor/SAVE":
				saveLevel();
				return gameState;

			
			default:
				System.out.println("LE: Error on Click");
				break;
		}
		return 0;
	}
	/**
	* The castToScreen method outputs all actions to the screen.
	* @param levelIn checks that the gamestate has not been updated and a new menu needs to be called.
	*/
	public void castToScreen(int levelIn)
	{
		int gameState = levelIn;
		while(window.isOpen() && (levelIn == gameState))		
		{
			window.clear();
			/*for (Object background : backgrounds)
			{
				background.calcMove(0, 0, screenWidth, screenHeight);
				background.performMove( );
				background.draw(window);
			}*/
			
			for(Tiles tile : tiles)
			{
				tile.draw(window);
				if(tile.getColor() == Color.WHITE)
				{
					
					if(tile.isInHole(tile, tiles)==1)
					{
						//If the ball gets in the hole
						saveLevel();
						gameState = -1;
					}
					
					if(tile.isInHole(tile, tiles)==2)
					{
						System.out.println("LE: in grey hole");
						tile.setInitialVelocity(0,-2);
						tile.setLocation(tile.getX(),tile.getY()-100);
					}
					tile.moveDifferential();
					interactions(tile);
					boundaries(tile);
					tile.move();
				}
				if(tile.getColor() == Color.MAGENTA)
				{
					tile.setLocation(Mouse.getPosition().x-10,Mouse.getPosition().y);
				}
			}
			if (showPause) //Displays the pause menu
			{
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
				
			}
			window.display();
			
			/**
			* Performs all scheduled events.
			*/
			for (Event event : window.pollEvents( ))
			{
				switch(event.type)
				{
					case CLOSED:
						System.out.println("LE: The user pressed the close button!");
						return;

					case MOUSE_BUTTON_PRESSED:
						System.out.println("#############################################");
						if(Mouse.isButtonPressed(Mouse.Button.LEFT))
						{
							if (showPause)
							{
								xMouse = Mouse.getPosition().x;
								yMouse = Mouse.getPosition().y;
								System.out.println("MN: Click Event at: X " + xMouse + " Y " + yMouse);

								for (Button button : buttons)
								{
									if(button.mouseOver == true)
									{
										System.out.println(button.name);
										int buttonClicked = clicked(button.name, gameState);
										if (buttonClicked == -1)
										{
											return;
										}
										
									}
								}
								for (Object object : objects)
								{
									object.calcMove(0, 0, screenWidth, screenHeight);
									object.performMove( );
									object.draw(window);

								}								
							}
							else
							{
							for(Iterator<Tiles> itr = tiles.iterator(); itr.hasNext();){
									Tiles ball = itr.next();
									if(ball.getColor()==Color.WHITE && !ball.isInMotion())
									{
										Powerbar(ball);
									}
								}
							}
							
						}
						break;
						
					case MOUSE_WHEEL_MOVED:
						System.out.println("LE: The user moved the mouse wheel!");
						break;
						
					case KEY_PRESSED:
						KeyEvent keyEvent = event.asKeyEvent();
						System.out.println("LE: The user pressed the following key: " + keyEvent.key);
						if(Keyboard.isKeyPressed(Keyboard.Key.ESCAPE))	//Takes the user back to the menu
						{
							gameState = -1;
						}
						if(Keyboard.isKeyPressed(Keyboard.Key.Q))
						{
							System.out.println("LE: Switch flipped");	//Pauses the game
							showPause = !showPause;
						}
						if(Keyboard.isKeyPressed(Keyboard.Key.P))
						{
							System.out.println("LE: Switch flipped"); 	//Pauses the game
							showPause = !showPause;
						}
						if(Keyboard.isKeyPressed(Keyboard.Key.R))		//Resets the ball to the default position
						{
							for(Tiles tile : tiles)
							{
								if(tile.getColor() == Color.WHITE)
								{
									tile.resetBall();
								}
							}
						}
						if(Keyboard.isKeyPressed(Keyboard.Key.ADD))		//Calls the method which cycles through tiles
						{
							incTile(currentTile);						
						}
						
						if(Keyboard.isKeyPressed(Keyboard.Key.SUBTRACT))//Calls the method which cycles through tiles
						{
							decTile(currentTile);			
						}
						if(Keyboard.isKeyPressed(Keyboard.Key.LCONTROL) & Keyboard.isKeyPressed(Keyboard.Key.M))
						{
							System.out.println("LE: Mouse Location: X " + Mouse.getPosition().x + " Y " + Mouse.getPosition().y );
						}
						
						if(Keyboard.isKeyPressed(Keyboard.Key.NUMPAD2)){	//Changes the currently selected tile
							if(currentTile < 24)
							{
								currentTile+=8;
							}
							sleepit();
							update();
						}
					
						if(Keyboard.isKeyPressed(Keyboard.Key.NUMPAD4)){	//Changes the currently selected tile
							if(currentTile%8!=0)
							{
								currentTile--;
							}
							sleepit();
							update();
						}
						
						if(Keyboard.isKeyPressed(Keyboard.Key.NUMPAD6)){	//Changes the currently selected tile
							if((currentTile+1)%8!=0)
							{
								currentTile++;
							}
							sleepit();
							update();
						}
						if(Keyboard.isKeyPressed(Keyboard.Key.NUMPAD8)){ 	//Changes the currently selected tile
							if(currentTile >7){
								currentTile-=8;
							}
							sleepit();
							update();
						}
						if(Keyboard.isKeyPressed(Keyboard.Key.S)){			//Calls the method which saves the level
							saveLevel();
							System.out.println("Level Saved");
						}
						if(Keyboard.isKeyPressed(Keyboard.Key.L)){			//Calls the method which loads the level
							loadLevel();
							update();
							System.out.println("Level Loaded");
						}
						if(Keyboard.isKeyPressed(Keyboard.Key.UP)){			//Moves the hole
							for(Iterator<Tiles> itr = tiles.iterator(); itr.hasNext();){
								Tiles ball = itr.next();
								if(ball.getColor()==Color.BLACK){
									ball.setLocation(ball.getX(),ball.getY()-5);
								}
							}
						}	
						if(Keyboard.isKeyPressed(Keyboard.Key.RIGHT)){		//Moves the hole
							for(Iterator<Tiles> itr = tiles.iterator(); itr.hasNext();){
								Tiles ball = itr.next();
								if(ball.getColor()==Color.BLACK){
									ball.setLocation(ball.getX()+5,ball.getY());
								}
							}
						}		
						if(Keyboard.isKeyPressed(Keyboard.Key.LEFT)){		//Moves the hole
							for(Iterator<Tiles> itr = tiles.iterator(); itr.hasNext();){
								Tiles ball = itr.next();
								if(ball.getColor()==Color.BLACK){
									ball.setLocation(ball.getX()-5,ball.getY());
								}
							}
						}		
						if(Keyboard.isKeyPressed(Keyboard.Key.DOWN)){		//Moves the hole
							for(Iterator<Tiles> itr = tiles.iterator(); itr.hasNext();){
								Tiles ball = itr.next();
								if(ball.getColor()==Color.BLACK){
									ball.setLocation(ball.getX(),ball.getY()+5);
								}
							}
						}
						break;
					default:						
				}
			}
		}
	}
	/**
	* The incTile method cycles the selected tile through the available tile designs.
	* @param i This is the currently displayed tile variant, which are represented in numbers 1 to 16.
	*/
	public void incTile(int i){
		System.out.println(tiler[i]);
			if(tiler[i] < 16){
				tiler[i]++; 
			}
			else tiler[i] = 0;
			sleepit();
			update();
	}
	/**
	* The decTile method cycles the selected tile backwards through the available tile designs.
	* @param i This is the currently displayed tile variant, which are represented in numbers 1 to 16.
	*/
	public void decTile(int i){
			if(tiler[i] >= 1){
				tiler[i]--; 
			}
			else tiler[i] = 16;
			sleepit();
			update();
	}
	/**
	* The sleepit method pauses the program for 300 milliseconds, which prevents anything cycling too quickly.
	*/
	public void sleepit(){
		try{
			Thread.sleep(300);
		} catch(InterruptedException ex){}
	}
	/**
	* The loadLevel method scans a text file for level data, which can also be saved in the level editor. 
	* The level data is stored in grid.txt
	* Throws a FileNotFoundException is the file isn't available.
	*/
	public void loadLevel(){
		try{
			Scanner scanner = new Scanner(new File("./levels/editor/grid.txt"));
			int j = 0;
			while(scanner.hasNextInt()){
				tiler[j] = scanner.nextInt();
				j++;
			} 
		} 
		catch(FileNotFoundException e){
				System.out.print("The system cannot find the specified file");
		}
		defaultGrid();
	}
	/**
	* The saveLevel method saves the currently created level to a text document for later loading.
	* The data is stored in grid.txt.
	* Throws an IOException if errors occur.
	*/
	public void saveLevel(){
		try{
			FileWriter fw = new FileWriter("./levels/editor/grid.txt");
			for (int i = 0; i < tiler.length; i++)
			{
				fw.write(tiler[i] + " ");
			}
			fw.close();
		}
		catch(IOException e)
		{
			System.out.println("Failed to save");
		}
	}
	/**
	* The defaultGrid method is used to create and position the tiles on the screen.
	* gridTiles are created and then positioned in their correct locations.
	*/	
	public void defaultGrid(){
		int counter1 = 180;
		int counter2 = 100;
		for (int i = 1; i <= 32; i++){
			gridTile t = new gridTile(tiler[i-1],counter1,counter2, tiles, vertBorders, horizBorders);
			counter1 += 200;
			if((float)i%8 == 0){
				counter2 += 200;
				counter1 = 180;
			}
		}
	}
	/**
	* The setGlow method provides the mathematics for the glow that surrounds the currently selected tile to move relative to the users input.
	* @param g This is a glow class which needs to be initiated.
	*/
	public void setGlow(Glow g){
		if(currentTile >= 0 && currentTile <=7){
			g.setLocation((currentTile*200+180),100);
		}
		else if(currentTile >= 8 && currentTile <=15){
			g.setLocation(((currentTile-8)*200+180),300);
		}
		else if(currentTile >= 16 && currentTile <=23){
			g.setLocation(((currentTile-16)*200+180),500);
		}
		else if(currentTile >= 24 && currentTile <=32){
			g.setLocation(((currentTile-24)*200+180),700);
		}
	}
	/**
	* The update method is used to redraw the screen, starting by clearing the arrays and then generating the updated level.
	* The glow is also initiated in this method.
	*/
	public void update(){
		clearArrays();
		tiles.add(new GameImage(-200,-50,1.25f,1.20f, LEBackground,GRASS));
		Glow g1 = new Glow();
		defaultGrid();
		setGlow(g1);
		tiles.add(g1);
		tiles.addAll(keep);
		castToScreen(2);
	}
	/**
	* The run method is used to initialise the level editor, and establishes the frame rate and hole for the level.
	* @param window The RenderWindow used to display.
	* @param gameStateIn The value used to travel and return to the correct menus/levels.
	*/
	public int run(RenderWindow window, int gameStateIn)
	{
		System.out.println("LE: Gets into level editor");
		window.setFramerateLimit(framerate);
		keep.add(new Circle(10,280,200,Color.WHITE));
		keep.add(new Circle(15,280,800,Color.BLACK));
		clearArrays();
		update();
		return 0;
	}


}