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
*	Represents a piece of text that can be shown on screen
*/
public class TextMessage extends Object
{
	//
	// The Java install comes with a set of fonts but these will
	// be on different filesystem paths depending on the version
	// of Java and whether the JDK or JRE version is being used.
	//
	private static String JavaVersion = Runtime.class.getPackage( ).getImplementationVersion( );
	private static String JdkFontPath ="C:\\Program Files\\Java\\jdk" + JavaVersion +"\\jre\\lib\\fonts\\";
	private static String JreFontPath ="C:\\Program Files\\Java\\jre" + JavaVersion +"\\lib\\fonts\\";
	private static String FontPath = "./assets/font/";
	int sizeCount = 0;
	int fontSize  = 30;
	/**
	*	Creates an object that represents a text string that can be then displayed on a screen.
	*	@param x The horizontal location of the centre of the text (referenced from the top left hand corner of the window)
	*	@param y The vertical location of the centre of the text (referenced from the top left hand corner of the window)
	*	@param r The clockwise rotation of the text
	*	@param message String showing the message to be displayed
	*	@param c The colour of the text
	*	@see Object
	*/
	public TextMessage(int x, int y, int r, String message, Color c)
	{
		String FontFile  = "MyriadProRegular.ttf";
		Font proRegular = new Font( );
		
		try
		{
			proRegular.loadFromFile(Paths.get(FontPath+FontFile));
		}
		catch (IOException ex)
		{
			ex.printStackTrace( );
		}

		Text text = new Text (message, proRegular, fontSize);
		text.setColor(c);
		text.setStyle(Text.BOLD);

		FloatRect textBounds = text.getLocalBounds( );
		text.setPosition(x, y);

		this.x = x;
		this.y = y;
		this.r = r;

		obj = text;
	}
	}