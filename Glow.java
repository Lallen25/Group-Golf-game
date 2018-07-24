import org.jsfml.system.*;
import org.jsfml.window.*;
import org.jsfml.window.event.*;
import org.jsfml.graphics.*;
/**
* The Glow class represents a glowing square which encircles the currently selected tile.
*/
public class Glow extends Tiles{
		public Glow(){
			RectangleShape re = new RectangleShape(new Vector2f(200,200));
			re.setFillColor(new Color(255,255,0,50));
			obj = re;
		}
	}