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
*	Represents a cloud where it's texture is randomly pulled form a pool, where it will then move across the screen
*/
public class Cloud extends Object
{
	double cloudSpeed;
	/**
	*	Creates an instance of a cloud that can be added to the screen.
	*/
	public Cloud()
	{
		//Generating a number to represent a cloud from file.
		Random rand = new Random(); 
		int cloudNum = (rand.nextInt(7) + 1); 
		String textureFile = "./assets/images/backgrounds/clouds/" + cloudNum + ".png";
		
		//Randomly generates the speed of the cloud
		this.cloudSpeed = (1 + rand.nextDouble())/1.5;

		//Load Cloud texture from file
		Texture imgTexture = new Texture( );
		try
		{
			imgTexture.loadFromFile(Paths.get(textureFile));
		}
		catch (IOException ex)
		{
			ex.printStackTrace( );
		}
		imgTexture.setSmooth(true);
		//Assign texture to sprite
		Sprite img = new Sprite(imgTexture);
		img.setOrigin(Vector2f.div(new Vector2f(imgTexture.getSize()), 2));
		
		//Set random position on screen
		this.x = rand.nextInt(2500);
		this.y = rand.nextInt(1080);

		obj = img;
	}
	
	/**
	*	Calsulates the new position of the cloud as it moves accross the screen
	*	@param minx The leftmost location the clouds can move to
	*	@param miny The topmost location the clouds can move to
	*	@param maxx UNUSED
	*	@param maxy UNUSED
	*/
	@Override
	void calcMove(int minx, int miny, int maxx, int maxy)
	{
		Random rand = new Random(); 
		//
		// Add deltas to x and y position
		//
		if (x <= minx)
		{
			x += 2500 + rand.nextInt(1000);
			y = y -200;
		}
		if (y <= miny)
		{
			y = y + 1000;
		}
		x += -cloudSpeed;//this.cloudSpeed;//this.cloudSpeed;
		//y += dy;
	}
}