import org.jsfml.system.*;
import org.jsfml.window.*;
import org.jsfml.window.event.*;
import org.jsfml.graphics.*;
/**
* The Tile class creates a 200 x 200 pixel tile which other items can be built on.
*/
public class Tile extends Tiles{
	/**
	* @param x The x location of the tile.
	* @param y The y location of the tile.
	*/
	public Tile(int x, int y){
		this.x = x;
		this.y = y;
		RectangleShape t = new RectangleShape(new Vector2f(200,200));
		t.setPosition(x,y);
		t.setFillColor(DGREEN);
		obj = t;
	}
}	