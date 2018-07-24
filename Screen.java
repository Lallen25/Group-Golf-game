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
*	Is used as a base class for other game areas, so stores a number of variables here.
*/
public class Screen 
{
	int screenWidth  = 1920;
	int screenHeight = 1080;
	int centreX = screenWidth /2;
	int centreY = screenHeight/2;
	RenderWindow window;
	//
	// The Java install comes with a set of fonts but these will
	// be on different filesystem paths depending on the version
	// of Java and whether the JDK or JRE version is being used.
	//
	String JavaVersion = Runtime.class.getPackage( ).getImplementationVersion( );
	String JdkFontPath = "C:\\Program Files\\Java\\jdk" + JavaVersion +"\\jre\\lib\\fonts\\";
	String JreFontPath ="C:\\Program Files\\Java\\jre" + JavaVersion +"\\lib\\fonts\\";

	int fontSize     = 48;
	
	String WindowTitle   = "Golfmania v0.3.4";
	
	String FontPath;	// Where fonts were found
	
	//Arrays that most objects are found in throughout the game
	ArrayList<Object> objects = new ArrayList<Object>( );
	ArrayList<Button> buttons = new ArrayList<Button>( );

	int xMouse;
	int yMouse;
	
	public Screen()
	{
	}
	int clicked(String name)
	{
		return 0;
	}
	
	int run (RenderWindow window, int gameStateIn)
	{
		
		return 0;
	}
	
}