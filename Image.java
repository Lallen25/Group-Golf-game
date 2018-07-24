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
* The image class creates and controls the graphics used in the game.
*/
public class Image extends Object
{
	Sprite img;
	int height;
	int width;
	Vector2i size;
	double speed;
	/**
	* @param x The x location of the image.
	* @param y The y location of the image.
	* @param textureFile the location of the file to be loaded.
	*/
	public Image(int x, int y, String textureFile)
	{
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
		Vector2i imgTextureSize = imgTexture.getSize();
		height = imgTextureSize .x;
		width = imgTextureSize .y;

		img = new Sprite(imgTexture);
		img.setOrigin(Vector2f.div(new Vector2f(imgTexture.getSize()), 2));
		
		this.speed = 0;
		this.x = x;
		this.y = y;
		
		
		obj = img;
	}
	/**
	* Creates the images specifically used in the credits.
	* @param x The x location of the image.
	* @param y The y location of the image.
	* @param textureFile The location of the file to be loaded.
	* @param creditSpeed The speed of the credit scrolling
	*/
	public Image(int x, int y, String textureFile, double creditSpeed)
	{
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
		Vector2i imgTextureSize = imgTexture.getSize();
		height = imgTextureSize .x;
		width = imgTextureSize .y;

		img = new Sprite(imgTexture);
		img.setOrigin(Vector2f.div(new Vector2f(imgTexture.getSize()), 2));
		
		this.speed = creditSpeed;
		this.x = x;
		this.y = y;

		obj = img;
	}
	
	/**
	* The calcMove method calculates the speed at which images move.
	*/
	@Override
	void calcMove(int minx, int miny, int maxx, int maxy)
	{
		if (this.speed != 0)
		{
			if (x <= minx)
			{
				x += 3000;
			}
			x -= this.speed;
		}
	}
}