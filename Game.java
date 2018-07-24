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
*	Game class contains all the levels, the way to build the levels, the arrays for the levels
*	Much of games functionality, and window settings
**/
class Game extends Screen
{
	//Array lists contain the building blocks of each level
	public ArrayList<Tiles> tiles = new ArrayList<Tiles>();
	ArrayList<BorderRectangle> horizBorders = new ArrayList<BorderRectangle>();
	ArrayList<BorderRectangle> vertBorders = new ArrayList<BorderRectangle>();
	ArrayList<ConflictTiles> conflict = new ArrayList<ConflictTiles>();

	//Graphical assets
	public String backgroundImagesPath = "./assets/images/backgrounds/levels/world1/";
	String pauseMenuBackground = "./assets/images/backgrounds/pause menu/BACKGROUND.png";
	String pauseMenuScores = "./assets/images/backgrounds/pause menu/SCOREBOARD.png";
	String pauseMenuTitle = "./assets/images/backgrounds/pause menu/GAMEPAUSED.png";
	
	public boolean showPause = false;
	
	//Help texts
	String ballHelp = "Click on the screen with the mouse to move the ball";
	String pauseHelp = "You can press P to bring up the pause menu";
	String resetHelp = "You can press R to reset the ball";
	String hillHelp = "Hills will speed up or slow down your ball depending on their direction";
	String waterHelp = "Avoid the water! If your balls goes in the water it will be reset to the start";
	String sandHelp = "If your ball goes in the sand it will be slowed down";
	
	RenderWindow window;

	int framerate = 180;
	int level = 0;
	
	Color BROWN = new Color(139,69,19); //Borders
	Color DGREEN = new Color(34,139,34); //Hills
	
	//Hill specifics
	Color HILLUPH = new Color(34,139,34);
	Color HILLDOWNH = new Color(33,139,34);
	Color HILLUPV = new Color(34,138,34);
	Color HILLDOWNV= new Color(34,139,35);
	
	//Ball conflicting colours
	Color DGREY = new Color(105,105,105);
	Color PIPE = new Color(115,116,114);
	Color WATER = new Color(104,157,255);
	Color GRASS = new Color(91,127,0);
	Color RED = new Color(255,255,255);
	
	/**
	*	Constructor for game
	*	@param inputWindow the window that will have everything input onto
	*	@param gameStateIn current game state
	**/
	public Game(RenderWindow inputWindow, int gameStateIn)
	{
		System.out.println("GM: Gets into game level " + gameStateIn);
		this.window = inputWindow;
		
		objects.add(new Image(centreX, centreY, pauseMenuBackground));
		objects.add(new Image(centreX, centreY + 50, pauseMenuScores));
		objects.add(new Image(centreX, centreY -400, pauseMenuTitle));
		buttons.add(new Button(50, 1030,"pause menu/BACK"));
		objects.add(new TextMessage (centreX,centreY, 15, "W . I . P", RED));
		objects.add(new TextMessage (50,50, 0, ballHelp, RED));
		objects.add(new TextMessage (1200,50, 0, pauseHelp, RED));
		objects.add(new TextMessage (300,1030, 0, resetHelp, RED));
		objects.add(new TextMessage (300,1000, 0, hillHelp, RED));
		objects.add(new TextMessage (300,970, 0,waterHelp, RED));
	}
	
	/**
	*	Method calculates the factors for velocities in both x and y-
	*	@param ball Takes the white ball, as it is what will be moving
	**/
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
	*	Empty all arrays
	**/
	public void clearArrays()
	{
		tiles.clear();
		horizBorders.clear();
		vertBorders.clear();
		conflict.clear();
		System.out.println("GM:  - ARRAYS CLEARED - ");
	}

	/**
	*	Detects ball within constraints of boundary and reacts by inverting either x velocity or y velocity
	*	@param tile takes the ball in to compare to boundaries
	**/
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
	*	Calculates interaction with a hill
	*	@param xInc x velocity incrementation, the change in x
	*	@param yInc y velocity incrementation, the change in y
	*	@param ball the white ball, as acting on the hill
	**/
	public void hill(float xInc, float yInc, Tiles ball)
	{
		ball.setInitialVelocity(ball.getXV()+xInc,ball.getYV()+yInc);
	}	
	
	/**
	*
	**/
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
				if(con.getColor()==HILLUPV) hill(0,-0.25f,ball);
				if(con.getColor()==HILLDOWNV) hill(0,0.25f,ball);
			}
		}
	}
	
	/**
	*	Method to translate between the button clicked and the game state
	*	@param name the button clicked
	*	@return int the game state
	**/
	public int clicked(String name)
	{
		switch (name)
		{
			case "pause menu/BACK":
				return -1;
			default:
				System.out.println("GM: Error on Click");
				break;
		}
		return 0;
	}

	/**
	*	Casts the arrays to screen to build
	*	@param levelIn The current level to be built on screen
	**/
	public void castToScreen(int levelIn)
	{
		int gameState = levelIn;
		while(window.isOpen() && (levelIn == gameState))
		{
			window.clear();
			for(Tiles tile : tiles)
			{
				tile.draw(window);
				if(tile.getColor() == Color.WHITE)
				{
					
					if(tile.isInHole(tile, tiles)==1)
					{
						gameState = -1;
					}
					if(tile.isInHole(tile, tiles)==2)
					{
						System.out.println("GM: in grey hole");
						tile.setInitialVelocity(0,-2);
						tile.setLocation(tile.getX(),tile.getY()-100);
					}
					if(tile.isInHole(tile, tiles)==3)
					{
						System.out.println("GM: in MAGENTA");
						tile.setInitialVelocity(2,0);
						tile.setLocation(tile.getX()+150,tile.getY());
					}
					tile.moveDifferential();
					interactions(tile);
					boundaries(tile);
					tile.move();
				}
			}
			//Pause Menu
			if (showPause)
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
			for (Event event : window.pollEvents( ))
			{
				switch(event.type)
				{
					
					case CLOSED:
						System.out.println("GM: The user pressed the close button!");
						gameState = -1;
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
										int buttonClicked = clicked(button.name);
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
								for(Tiles ball : tiles)
								{
									if(ball.getColor()==Color.WHITE && !ball.isInMotion())
									{
										Powerbar(ball);
										castToScreen(levelIn);
									}
								}
							}
							
						}
						break;
						
					case MOUSE_WHEEL_MOVED:
						System.out.println("GM: The user moved the mouse wheel!");
						break;
						
					case KEY_PRESSED:
						KeyEvent keyEvent = event.asKeyEvent();
						System.out.println("GM: The user pressed the following key: " + keyEvent.key);
						if(Keyboard.isKeyPressed(Keyboard.Key.ESCAPE))
						{
							gameState = -1;
						}
						if(Keyboard.isKeyPressed(Keyboard.Key.A))
						{
							//theme.pause();
						}
						if(Keyboard.isKeyPressed(Keyboard.Key.B))
						{
							//theme.play();
						}
						if(Keyboard.isKeyPressed(Keyboard.Key.Q))
						{
							System.out.println("GM: Switch flipped");
							showPause = !showPause;
						}
						if(Keyboard.isKeyPressed(Keyboard.Key.P))
						{
							System.out.println("GM: Switch flipped");
							showPause = !showPause;
						}
						if(Keyboard.isKeyPressed(Keyboard.Key.R))
						{
							for(Tiles tile : tiles)
							{
								if(tile.getColor() == Color.WHITE)
								{
									tile.resetBall();
								}
							}
						}
						if(Keyboard.isKeyPressed(Keyboard.Key.LCONTROL) & Keyboard.isKeyPressed(Keyboard.Key.M))
						{
							System.out.println("GM: Mouse Location: X " + Mouse.getPosition().x + " Y " + Mouse.getPosition().y );
						}							
						break;
					default:
				}
			}
		}
	}
	
	/**
	*	Core method which runs the game
	*	@param window Window being drawn onto
	*	@param gameStateIn Current game state
	*	@return int Level to play
	**/
	public int run(RenderWindow window, int gameStateIn)
	{
		int worldNum = gameStateIn/10;
		int levelNum = gameStateIn - (10 * worldNum);
		int level = ((worldNum -1)*9) + levelNum;
		System.out.println("GM: Gets into run, level " + level);
		window.setFramerateLimit(framerate);
		//levelSelect();
		clearArrays();
		switch(level)
		{
			case 1:
				level1();
				//Save Score
				break;
			case 2:
				level2();
				//Save Score
				break;
			case 3:
				level3();
				//Save Score
				break;
			case 4:
				level4();
				//Save Score
				break;
			case 5:
				level5();
				//Save Score
				break;
			case 6:
				level6();
				//Save Score
				break;
			case 7:
				level7();
				//Save Score
				break;
			case 8:
				level8();
				//Save Score
				break;
			case 9:
				level9();
				//Save Score
				break;
			default:
				System.out.println("GM: Error Loading Level");	
		}
		castToScreen(level);
		return (worldNum * 10);
	}

	/**
	*	Game level 1
	*	Basic level with no obstacles
	**/
	public void level1()
	{
		tiles.add(new GameImage(-220, -350,1.22f,1.45f, (backgroundImagesPath + "1.jpg"),GRASS));
		tiles.add(new Rectangle(1520,500,200,200,GRASS));
		
		conflict.add(new ConflictTiles(1520,500,200,200,GRASS));

		tiles.add(new Rectangle(1520,30,200,170,BROWN));
		tiles.add(new Rectangle(1520,30,200,700,BROWN));
		tiles.add(new Rectangle(30,560,170,170,BROWN));
		tiles.add(new Rectangle(30,560,1720,170,BROWN));
		
		horizBorders.add(new BorderRectangle(1520,40,200,160));
		horizBorders.add(new BorderRectangle(1520,50,200,680));
		vertBorders.add(new BorderRectangle(40,580,160,160));
		vertBorders.add(new BorderRectangle(50,580,1700,160));
		
		tiles.add(new Circle(15,1620,435,Color.BLACK));
		tiles.add(new Circle(10,300,440,Color.WHITE));
		return;
	}
	
	/**
	*	Level 2 Basic level but has introduction to boundaries, and moving the ball at angles
	**/
	public void level2()
	{
		tiles.add(new GameImage(-50, 0,1.08f,1f,(backgroundImagesPath + "2.jpg"),GRASS));
		tiles.add(new Rectangle(1520,300,200,200,GRASS));
		tiles.add(new Rectangle(620,200,1100,500,GRASS));
		
		tiles.add(new Rectangle(1520,8,200,192,BROWN));
		tiles.add(new Rectangle(900,8,200,500,BROWN));
		tiles.add(new Rectangle(8,316,192,192,BROWN));
		tiles.add(new Rectangle(8,516,1720,192,BROWN));
		tiles.add(new Rectangle(8,208,1092,500,BROWN));
		tiles.add(new Rectangle(620,8,1100,700,BROWN));
		
		tiles.add(new Rectangle(8,200,900,300,BROWN));
		tiles.add(new Rectangle(8,200,1092,200,BROWN));
		tiles.add(new Rectangle(310,8,1092,500,BROWN));

		horizBorders.add(new BorderRectangle(1520,40,200,160));
		horizBorders.add(new BorderRectangle(900,28,200,480));
		horizBorders.add(new BorderRectangle(620,28,1100,680));
		horizBorders.add(new BorderRectangle(310,28,1092,480));
		vertBorders.add(new BorderRectangle(8,28,1396,480));
		
		vertBorders.add(new BorderRectangle(28,316,172,192));
		vertBorders.add(new BorderRectangle(28,516,1700,192));
		vertBorders.add(new BorderRectangle(28,208,1072,500));
		vertBorders.add(new BorderRectangle(28,200,880,300));
		horizBorders.add(new BorderRectangle(28,8,880,294));
		vertBorders.add(new BorderRectangle(28,200,1072,200));
		horizBorders.add(new BorderRectangle(28,8,1072,392));
		
		tiles.add(new Circle(15,1150,585,Color.BLACK));
		tiles.add(new Circle(10,300,440,Color.WHITE));
		return;
	}

	/**
	*	level 3
	*	Introduces water
	**/
	public void level3()
	{
		tiles.add(new GameImage(-50, -70,1.08f,1.1f,(backgroundImagesPath + "3.jpg"),GRASS));
		tiles.add(new Rectangle(1520,300,200,200,GRASS));
		tiles.add(new Rectangle(620,300,1100,500,GRASS));
		tiles.add(new Rectangle(620,100,1100,200,WATER)); //water here
		
		conflict.add(new ConflictTiles(640,100,1080,200,WATER));
		
		tiles.add(new Rectangle(1520,8,200,192,BROWN));
		tiles.add(new Rectangle(900,8,200,500,BROWN));
		tiles.add(new Rectangle(8,316,192,192,BROWN));
		tiles.add(new Rectangle(8,616,1720,192,BROWN));
		tiles.add(new Rectangle(8,308,1092,500,BROWN));
		tiles.add(new Rectangle(620,8,1100,800,BROWN));
		
		
		horizBorders.add(new BorderRectangle(1520,28,200,172));
		horizBorders.add(new BorderRectangle(900,28,200,480));
		horizBorders.add(new BorderRectangle(620,28,1100,780));
		
		vertBorders.add(new BorderRectangle(28,316,172,192));
		vertBorders.add(new BorderRectangle(28,616,1700,192));	
		vertBorders.add(new BorderRectangle(28,308,1072,500));			
		

		tiles.add(new Circle(15,1140,635,Color.BLACK));
		tiles.add(new Circle(10,300,340,Color.WHITE));
		return;
	}

	/**
	*	Level 4
	*	Introduces hills
	**/
	public void level4()
	{
		tiles.add(new GameImage(-155,-250,1.2f,1.3f,(backgroundImagesPath + "4.jpg"),GRASS));
		tiles.add(new Rectangle(1520,350,200,200,GRASS));
		tiles.add(new Rectangle(450,350,600,200,DGREEN));
		
		conflict.add(new ConflictTiles(225,350,600,200,HILLUPH));
		conflict.add(new ConflictTiles(225,350,800,200,HILLDOWNH));
		
		tiles.add(new Rectangle(1520,8,200,192,BROWN));
		tiles.add(new Rectangle(1520,8,200,550,BROWN));
		tiles.add(new Rectangle(8,366,192,192,BROWN));
		tiles.add(new Rectangle(8,366,1720,192,BROWN));
		
		tiles.add(new Rectangle(12,250,450,200,Color.RED));
		vertBorders.add(new BorderRectangle(32,250,430,200));
		horizBorders.add(new BorderRectangle(22,12,435,445));
		
		tiles.add(new Rectangle(12,250,800,300,Color.RED));
		vertBorders.add(new BorderRectangle(32,250,780,300));
		horizBorders.add(new BorderRectangle(22,12,785,294));
		
		horizBorders.add(new BorderRectangle(1520,28,200,172));
		horizBorders.add(new BorderRectangle(1520,28,200,530));
		
		vertBorders.add(new BorderRectangle(28,366,172,192));
		vertBorders.add(new BorderRectangle(28,366,1700,192));	
		
		tiles.add(new Circle(15,1600,450,Color.BLACK));
		
		tiles.add(new GameImage(725,375,1,1,("./assets/images/levels/HILL_ARROWS/LEFT.png"),GRASS));
		tiles.add(new GameImage(625,375,1,1,("./assets/images/levels/HILL_ARROWS/LEFT.png"),GRASS));
		tiles.add(new GameImage(675,375,1,1,("./assets/images/levels/HILL_ARROWS/LEFT.png"),GRASS));
		tiles.add(new GameImage(725,275,1,1,("./assets/images/levels/HILL_ARROWS/LEFT.png"),GRASS));
		tiles.add(new GameImage(625,275,1,1,("./assets/images/levels/HILL_ARROWS/LEFT.png"),GRASS));
		tiles.add(new GameImage(675,275,1,1,("./assets/images/levels/HILL_ARROWS/LEFT.png"),GRASS));
		tiles.add(new GameImage(725,475,1,1,("./assets/images/levels/HILL_ARROWS/LEFT.png"),GRASS));
		tiles.add(new GameImage(625,475,1,1,("./assets/images/levels/HILL_ARROWS/LEFT.png"),GRASS));
		tiles.add(new GameImage(675,475,1,1,("./assets/images/levels/HILL_ARROWS/LEFT.png"),GRASS));
		
		tiles.add(new GameImage(860,375,1,1,("./assets/images/levels/HILL_ARROWS/RIGHT.png"),GRASS));
		tiles.add(new GameImage(910,375,1,1,("./assets/images/levels/HILL_ARROWS/RIGHT.png"),GRASS));
		tiles.add(new GameImage(960,375,1,1,("./assets/images/levels/HILL_ARROWS/RIGHT.png"),GRASS));
		tiles.add(new GameImage(860,275,1,1,("./assets/images/levels/HILL_ARROWS/RIGHT.png"),GRASS));
		tiles.add(new GameImage(910,275,1,1,("./assets/images/levels/HILL_ARROWS/RIGHT.png"),GRASS));
		tiles.add(new GameImage(960,275,1,1,("./assets/images/levels/HILL_ARROWS/RIGHT.png"),GRASS));
		tiles.add(new GameImage(860,475,1,1,("./assets/images/levels/HILL_ARROWS/RIGHT.png"),GRASS));
		tiles.add(new GameImage(910,475,1,1,("./assets/images/levels/HILL_ARROWS/RIGHT.png"),GRASS));
		tiles.add(new GameImage(960,475,1,1,("./assets/images/levels/HILL_ARROWS/RIGHT.png"),GRASS));		
		tiles.add(new Circle(10,300,340,Color.WHITE));
		return;
	}
	
	/**
	*	Level 5
	**/
	public void level5()
	{
		tiles.add(new GameImage(-250,-30,1.3f,1.3f,(backgroundImagesPath + "5.jpg"),GRASS));
		tiles.add(new Rectangle(1520,250,200,200,GRASS));
		tiles.add(new Rectangle(250,450,1470,450,GRASS));
		tiles.add(new Rectangle(560,250,970,650,GRASS));
		tiles.add(new Rectangle(300,250,700,200,Color.YELLOW));//sand here
		
		conflict.add(new ConflictTiles(300,250,700,200,Color.YELLOW));
		conflict.add(new ConflictTiles(500,250,200,200,GRASS));
		conflict.add(new ConflictTiles(300,250,1000,200,GRASS));
		
		tiles.add(new Rectangle(1520,8,200,192,BROWN));
		tiles.add(new Rectangle(1270,8,200,450,BROWN));
		tiles.add(new Rectangle(8,266,192,192,BROWN));
		tiles.add(new Rectangle(8,716,1720,192,BROWN));
		tiles.add(new Rectangle(8,258,962,650,BROWN));
		tiles.add(new Rectangle(750,8,970,900,BROWN));
		tiles.add(new Rectangle(508,8,962,642,BROWN));
		tiles.add(new Rectangle(8,200,1470,450,BROWN));
		
		
		horizBorders.add(new BorderRectangle(1520,28,200,172));
		horizBorders.add(new BorderRectangle(1270,28,200,430));
		horizBorders.add(new BorderRectangle(750,28,970,880));
		horizBorders.add(new BorderRectangle(508,28,962,622));
		
		vertBorders.add(new BorderRectangle(28,266,172,192));
		vertBorders.add(new BorderRectangle(28,716,1700,192));	
		vertBorders.add(new BorderRectangle(28,258,942,650));
		vertBorders.add(new BorderRectangle(28,200,1450,450));		
		
		tiles.add(new Circle(15,1000,775,Color.BLACK));
		tiles.add(new Circle(10,300,325,Color.WHITE));
		return;
	}

	/**
	*	Level 6
	**/
	public void level6()
	{
		tiles.add(new GameImage(-700, -30,1.80f,1.28f,(backgroundImagesPath + "6.jpg"),GRASS));
		tiles.add(new Rectangle(1520,300,200,200,GRASS));
		tiles.add(new Rectangle(300,500,200,500,GRASS));
		
		//These are the drawing of the blocks that'll create a gap inbetween, including the borders.
		tiles.add(new Rectangle(700,115,500,200,Color.RED));
		tiles.add(new Rectangle(700,115,500,385,Color.RED));
		vertBorders.add(new BorderRectangle(28,125,480,190));
		vertBorders.add(new BorderRectangle(28,125,480,375));
		vertBorders.add(new BorderRectangle(28,125,1172,190));
		vertBorders.add(new BorderRectangle(28,125,1172,375));
		horizBorders.add(new BorderRectangle(705,135,485,180));
		horizBorders.add(new BorderRectangle(705,135,485,365));
		
		tiles.add(new Rectangle(8,816,192,192,BROWN));
		tiles.add(new Rectangle(1528,8,200,192,BROWN));
		tiles.add(new Rectangle(8,316,1720,192,BROWN));
		tiles.add(new Rectangle(1228,8,500,500,BROWN));
		tiles.add(new Rectangle(308,8,200,1000,BROWN));
		tiles.add(new Rectangle(8,508,500,500,BROWN));
		
		horizBorders.add(new BorderRectangle(1528,28,200,172));
		horizBorders.add(new BorderRectangle(1228,28,500,480));
		horizBorders.add(new BorderRectangle(308,28,200,980));
		
		vertBorders.add(new BorderRectangle(28,816,172,192));
		vertBorders.add(new BorderRectangle(28,316,1700,192));	
		vertBorders.add(new BorderRectangle(28,508,480,500));	
		
		tiles.add(new Circle(15,1600,250,Color.BLACK));
		tiles.add(new Circle(10,350,950,Color.WHITE));
		return;
	}
	
	/**
	*	Level 7
	*	Sand, water, bridge, hill
	**/
	public void level7()
	{
		tiles.add(new GameImage(-470,-430 ,1.5f,1.5f,(backgroundImagesPath + "7.jpg"),GRASS));

		tiles.add(new Rectangle(1520,350,200,200,GRASS));
		tiles.add(new Rectangle(200,350,500,200,Color.YELLOW)); //sand here
		tiles.add(new Rectangle(100,350,700,200,DGREEN));
		
		conflict.add(new ConflictTiles(190,350,500,200,Color.YELLOW));
		conflict.add(new ConflictTiles(70,350,690,200,HILLUPH));
		conflict.add(new ConflictTiles(40,350,760,200,HILLDOWNH));
		conflict.add(new ConflictTiles(300,350,200,200,GRASS));
		conflict.add(new ConflictTiles(200,350,800,200,GRASS));
		
		tiles.add(new Rectangle(250,125,950,200,Color.RED));
		tiles.add(new Rectangle(250,175,950,380,Color.RED));
		
		horizBorders.add(new BorderRectangle(248,28,945,297));
		horizBorders.add(new BorderRectangle(248,28,945,360));
		vertBorders.add(new BorderRectangle(28,120,930,200));
		vertBorders.add(new BorderRectangle(28,120,1172,200));
		vertBorders.add(new BorderRectangle(28,170,930,372));
		vertBorders.add(new BorderRectangle(28,170,1172,372));
		
		tiles.add(new Rectangle(100,350,1350,200,WATER));		//water here
		tiles.add(new Rectangle(110,50,1345,450,BROWN));		//bridge here
		
		conflict.add(new ConflictTiles(100,250,1340,200,WATER));
		conflict.add(new ConflictTiles(100,60,1340,490,WATER));
		
		tiles.add(new Rectangle(1520,8,200,192,BROWN));
		tiles.add(new Rectangle(1520,8,200,550,BROWN));
		tiles.add(new Rectangle(8,366,192,192,BROWN));
		tiles.add(new Rectangle(8,366,1720,192,BROWN));
		
		horizBorders.add(new BorderRectangle(1520,28,200,172));
		horizBorders.add(new BorderRectangle(1520,28,200,530));
		
		vertBorders.add(new BorderRectangle(28,366,172,192));
		vertBorders.add(new BorderRectangle(28,366,1700,192));	

		tiles.add(new Circle(15,1620,435,Color.BLACK));
		
		tiles.add(new GameImage(710,375,1,1,("./assets/images/levels/HILL_ARROWS/LEFT.png"),GRASS));
		tiles.add(new GameImage(710,275,1,1,("./assets/images/levels/HILL_ARROWS/LEFT.png"),GRASS));
		tiles.add(new GameImage(710,475,1,1,("./assets/images/levels/HILL_ARROWS/LEFT.png"),GRASS));
		tiles.add(new GameImage(760,275,1,1,("./assets/images/levels/HILL_ARROWS/RIGHT.png"),GRASS));
		tiles.add(new GameImage(760,375,1,1,("./assets/images/levels/HILL_ARROWS/RIGHT.png"),GRASS));
		tiles.add(new GameImage(760,475,1,1,("./assets/images/levels/HILL_ARROWS/RIGHT.png"),GRASS));		
		
		tiles.add(new Circle(10,300,440,Color.WHITE));
		return;
	}
	
	/**
	*	Level 8
	*	Water, barriers, pipes, tight passage
	**/
	public void level8()
	{
		tiles.add(new GameImage(-510, -100,1.5f,1.38f,(backgroundImagesPath + "8.jpg"),GRASS));
		tiles.add(new Rectangle(300,500,200,200,GRASS));
		tiles.add(new Rectangle(1100,250,200,700,GRASS));
		tiles.add(new Rectangle(520,750,1200,200,GRASS));
		
		tiles.add(new Rectangle(100,100,300,400,Color.RED));
		tiles.add(new Rectangle(100,100,550,775,Color.RED));
		
		horizBorders.add(new BorderRectangle(110,28,285,380));
		horizBorders.add(new BorderRectangle(110,28,285,472));
		vertBorders.add(new BorderRectangle(28,110,280,385));
		vertBorders.add(new BorderRectangle(28,110,370,385));
		
		horizBorders.add(new BorderRectangle(110,28,535,755));
		horizBorders.add(new BorderRectangle(110,28,535,847));
		vertBorders.add(new BorderRectangle(28,110,530,762));
		vertBorders.add(new BorderRectangle(28,110,620,762));
		
		tiles.add(new Rectangle(275,75,725,875,WATER));//water
		tiles.add(new Rectangle(375,75,925,700,WATER));//water
		
		conflict.add(new ConflictTiles(295,95,705,855,WATER));
		conflict.add(new ConflictTiles(395,95,905,680,WATER));
		
		
		tiles.add(new Rectangle(204,8,1516,550,BROWN));
		tiles.add(new Rectangle(316,12,1200,688,BROWN));
		tiles.add(new Rectangle(12,250,1504,450,BROWN));
		horizBorders.add(new BorderRectangle(224,28,1516,530));
		horizBorders.add(new BorderRectangle(316,33,1200,667));
		vertBorders.add(new BorderRectangle(32,247,1484,450));
		
		tiles.add(new Rectangle(250,100,1354,350,BROWN));
		tiles.add(new Rectangle(520,100,1200,200,BROWN));
		tiles.add(new Rectangle(100,200,1200,300,BROWN));
		tiles.add(new Rectangle(50,200,1670,300,BROWN));
		
		horizBorders.add(new BorderRectangle(262,28,1340,330));
		horizBorders.add(new BorderRectangle(262,28,1340,420));
		vertBorders.add(new BorderRectangle(28,107,1335,345));
		vertBorders.add(new BorderRectangle(28,107,1575,345));
		
		horizBorders.add(new BorderRectangle(50,28,1665,472));
		
		vertBorders.add(new BorderRectangle(28,200,1180,300));
		vertBorders.add(new BorderRectangle(28,225,1650,275));
		
		horizBorders.add(new BorderRectangle(100,28,1200,472));
		horizBorders.add(new BorderRectangle(450,28,1275,270));
		vertBorders.add(new BorderRectangle(28,230,1270,260));
		
		tiles.add(new Rectangle(316,8,192,192,BROWN));
		tiles.add(new Rectangle(700,8,500,692,BROWN));
		tiles.add(new Rectangle(1528,8,200,950,BROWN));
		tiles.add(new Rectangle(528,8,1192,192,BROWN));
		
		horizBorders.add(new BorderRectangle(336,28,192,172));
		horizBorders.add(new BorderRectangle(720,28,485,672));
		horizBorders.add(new BorderRectangle(1548,28,180,930));
		horizBorders.add(new BorderRectangle(548,28,1172,172));
		
		tiles.add(new Rectangle(8,508,500,192,BROWN));
		tiles.add(new Rectangle(8,766,192,192,BROWN));
		tiles.add(new Rectangle(8,766,1720,192,BROWN));
		tiles.add(new Rectangle(8,500,1192,200,BROWN));
		
		vertBorders.add(new BorderRectangle(28,500,480,192));
		vertBorders.add(new BorderRectangle(28,766,172,192));
		vertBorders.add(new BorderRectangle(28,766,1700,192));
		vertBorders.add(new BorderRectangle(28,500,1172,200));
		
		tiles.add(new Circle(10,350,250,Color.WHITE));
		tiles.add(new Circle(15,1400,600,Color.BLACK));
		tiles.add(new Circle(15,1620,600,Color.GREEN));
		tiles.add(new Circle(15,1620,600,DGREY));
		tiles.add(new Rectangle(30,50,1620,500,DGREY));
		return;
	}

	/**
	*	Level 9
	*	Mulitple holes, sand, hill, covers
	**/
	public void level9()
	{
		tiles.add(new GameImage(-150, 0,1.15f,1f,(backgroundImagesPath + "9.jpg"),GRASS));
		tiles.add(new GameImage(-150, 0,1.15f,1f,"HOLE9BG.jpg",GRASS));
		tiles.add(new Rectangle(1520,250,200,200,GRASS));
		tiles.add(new Rectangle(250,450,1470,450,GRASS));
		tiles.add(new Rectangle(250,90,1470,530,Color.YELLOW));
		tiles.add(new Rectangle(250,70,1470,620,HILLDOWNH));
		
		conflict.add(new ConflictTiles(250,90,1470,530,Color.YELLOW));
		conflict.add(new ConflictTiles(250,70,1470,620,HILLUPV));
		conflict.add(new ConflictTiles(450,250,200,200,GRASS));
		conflict.add(new ConflictTiles(250,200,1470,330,GRASS));
		conflict.add(new ConflictTiles(1520,250,200,200,GRASS));
		conflict.add(new ConflictTiles(250,450,1470,690,GRASS));
		
		tiles.add(new Rectangle(125,250,950,200,BROWN));
		tiles.add(new Circle(15,1220,220,Color.MAGENTA));
		tiles.add(new Circle(15,1220,310,Color.MAGENTA));
		tiles.add(new Circle(15,1220,400,Color.MAGENTA));
		tiles.add(new Circle(15,1220,220,DGREY));
		tiles.add(new Circle(15,1220,310,DGREY));
		tiles.add(new Circle(15,1220,400,DGREY));
		
		tiles.add(new Rectangle(40,30,1308,400,PIPE));
		tiles.add(new Rectangle(40,30,1308,310,PIPE));
		tiles.add(new Rectangle(40,30,1308,220,PIPE));
		
		tiles.add(new Rectangle(8,250,1300,200,BROWN));
		vertBorders.add(new BorderRectangle(28,250,1280,200));
		
		tiles.add(new Rectangle(1520,8,200,192,BROWN));
		tiles.add(new Rectangle(1270,8,200,450,BROWN));
		tiles.add(new Rectangle(8,266,192,192,BROWN));
		tiles.add(new Rectangle(8,716,1720,192,BROWN));
		tiles.add(new Rectangle(8,258,1470,650,BROWN));
		tiles.add(new Rectangle(250,8,1470,900,BROWN));
		tiles.add(new Rectangle(8,200,1470,450,BROWN));
		
		
		horizBorders.add(new BorderRectangle(1520,28,200,172));
		horizBorders.add(new BorderRectangle(1272,28,200,430)); //this
		horizBorders.add(new BorderRectangle(250,28,1470,880));
		
		vertBorders.add(new BorderRectangle(28,266,1720,192));
		vertBorders.add(new BorderRectangle(28,716,1700,192));	
		vertBorders.add(new BorderRectangle(28,258,942,650));
		vertBorders.add(new BorderRectangle(28,612,1452,438)); //this
		vertBorders.add(new BorderRectangle(28,266,172,192));
		
		
		tiles.add(new GameImage(1515,640,1,1,("./assets/images/levels/HILL_ARROWS/UP.png"),GRASS));
		tiles.add(new GameImage(1590,640,1,1,("./assets/images/levels/HILL_ARROWS/UP.png"),GRASS));
		tiles.add(new GameImage(1665,640,1,1,("./assets/images/levels/HILL_ARROWS/UP.png"),GRASS));		
		
		tiles.add(new Circle(10,300,325,Color.WHITE));
		tiles.add(new Circle(15,1550,800,Color.BLACK));
		
		tiles.add(new GameImage(500,200,1.25f,1.25f,"./assets/images/levels/COVERS/TUNNELOVERLAY.png",GRASS));
		tiles.add(new GameImage(875,200,1.25f,1.25f,"./assets/images/levels/COVERS/TUNNELOVERLAY.png",GRASS));
		return;
	}
}