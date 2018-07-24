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
*	Represents a sound, combining SoundBuffer and Sound into one package
**/
public class SoundObject extends Object
{
	/**
	*	Creates a new instance of a sound
	*	@param path The location in storage of the file to be played
	*	@param volume The volume (with a range of 0 - 100) of the sound
	*/
	public SoundObject(String path, int volume)
	{
		SoundBuffer newBuffer = new SoundBuffer();
		try {

			newBuffer.loadFromFile(Paths.get(path));
			
			}
			catch(IOException ex) {
			System.err.println("Failed to load the sound:");
			ex.printStackTrace();
		}
		//Create a sound and set its buffer
		Sound audio = new Sound();
		
		audio.setBuffer(newBuffer);

		audio.setLoop(true);
		audio.setVolume(volume);
		audio.play();

	}
}