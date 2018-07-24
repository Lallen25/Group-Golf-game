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
*	Represents a set of images that form a set of credits (i.e. A persons name and two job titles)
*/
public class CreditImage extends Object
{
	//Base coordinates that the images will be arranged around
	int anchorPositionY;
	int anchorPositionX;
	String baseFilepath = "./assets/images/credits/";
	
	/**
	*	Creates a set of images arranged around each other, comprising of a new and two job titles.
	*	@param personName The name of the person being credited, must match their foldername.
	*	@param listOrder The position of the person in the credits
	*	@param objects An arraylist that the credit images can be added to
	*/
	public CreditImage(String personName, int listOrder, ArrayList<Object> objects)
	{
		//Alternates top and bottom row of credits based on position in credits i.e. ' . ' . ' . '
		if (listOrder % 2 == 1)
		{
			anchorPositionY = 335;
			anchorPositionX = 200;
		}
		else
		{
			anchorPositionY = 670;
			anchorPositionX = 200;
		}
		
		//X axis spacing between people
		anchorPositionX += listOrder * 500;

		
		System.out.println("CR: X Position: " + anchorPositionX + ". Y Position: " + anchorPositionY);
		
		//Name
		objects.add(new Image(anchorPositionX, anchorPositionY, (baseFilepath + personName + "/0.png"), 1 ));
		//Titles
		objects.add(new Image(anchorPositionX - 200, anchorPositionY + 100, (baseFilepath + personName + "/1.png"),1 ));
		objects.add(new Image(anchorPositionX + 100, anchorPositionY + 100, (baseFilepath + personName + "/2.png"), 1 ));
	}



}

