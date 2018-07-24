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
*	An image that can be used inside the game proper
*/
public class GameImage extends Tiles
{
	/**
	*	Creates an image that can be used inside the game itself
	*	@param xPos The horizzontal position of the image
	*	@param yPos The vertical position of the image
	*	@param scaleX The horizontal scaling of the game
	*	@param scaleY The vertical scaling of the game
	*	@param texture The position on disk of the image texture
	*	@param col The reference colour of the image, used in the main game loop
	*/
	public GameImage(float xPos, float yPos, float scaleX, float scaleY, String texture, Color col)
	{
		Texture tex = new Texture();
		try
		{
			tex.loadFromFile(Paths.get(texture));
		}
		catch (IOException ex)
		{
			ex.printStackTrace();
		}
		tex.setSmooth(true);
		Sprite sprite = new Sprite(tex);
		sprite.setPosition(xPos, yPos);
		sprite.setScale(scaleX, scaleY);
		c = col;
		this.x = xPos;
		this.y = yPos;
		obj = sprite;
	}
}