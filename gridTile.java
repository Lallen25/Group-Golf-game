import java.util.ArrayList;
/**
* The gridTile class contains the information required for each individual tile.
*/
public class gridTile extends Tiles{
		/**
		* @param n The chosen tile to display.
		* @param x The x location of the tile.
		* @param y The y location of the tile.
		* @param tiles The arraylist used to store the base tile information.
		* @param vertBorders The ArrayList used to store the vertical border information.
		* @param horizBorder The ArrayList used to store the horizontal border information.
		*/
		public gridTile(int n, int x, int y, ArrayList<Tiles> tiles, ArrayList<BorderRectangle> vertBorders, ArrayList<BorderRectangle> horizBorders){
			switch(n){
				case 1:
					tiles.add(new Tile(x,y));
					horizBorders.add(new BorderRectangle(200,20,x,y));
					tiles.add(new Rectangle(200,20,x,y,BROWN));
					horizBorders.add(new BorderRectangle(200,20,x,y+180));
					tiles.add(new Rectangle(200,20,x,y+180,BROWN));
					vertBorders.add(new BorderRectangle(20,200,x,y));
					tiles.add(new Rectangle(20,200,x,y,BROWN));
					break;
				case 2:
					tiles.add(new Tile(x,y));
					horizBorders.add(new BorderRectangle(200,20,x,y));
					tiles.add(new Rectangle(200,20,x,y,BROWN));
					horizBorders.add(new BorderRectangle(200,10,x,y+180));
					tiles.add(new Rectangle(200,20,x,y+180,BROWN));
					break;
				case 3:
					tiles.add(new Tile(x,y));
					horizBorders.add(new BorderRectangle(200,20,x,y));
					tiles.add(new Rectangle(200,20,x,y,BROWN));
					vertBorders.add(new BorderRectangle(20,200,x,y));
					tiles.add(new Rectangle(20,200,x,y,BROWN));
					break;	
				case 4:
					tiles.add(new Tile(x,y));
					horizBorders.add(new BorderRectangle(200,20,x,y+180));
					tiles.add(new Rectangle(200,20,x,y+180,BROWN));
					vertBorders.add(new BorderRectangle(20,200,x+180,y));
					tiles.add(new Rectangle(20,200,x+180,y,BROWN));
					break;
				case 5:
					tiles.add(new Tile(x,y));
					horizBorders.add(new BorderRectangle(200,20,x,y+180));
					tiles.add(new Rectangle(200,20,x,y+180,BROWN));
					vertBorders.add(new BorderRectangle(20,200,x,y));
					tiles.add(new Rectangle(20,200,x,y,BROWN));
					break;
				case 6:
					tiles.add(new Tile(x,y));
					horizBorders.add(new BorderRectangle(200,20,x,y));
					tiles.add(new Rectangle(200,20,x,y,BROWN));
					horizBorders.add(new BorderRectangle(200,20,x,y+180));
					tiles.add(new Rectangle(200,20,x,y+180,BROWN));
					vertBorders.add(new BorderRectangle(20,200,x+180,y));
					tiles.add(new Rectangle(20,200,x+180,y,BROWN));
					break;
				case 7:
					tiles.add(new Tile(x,y));
					horizBorders.add(new BorderRectangle(200,20,x,y));
					tiles.add(new Rectangle(200,20,x,y,BROWN));
					vertBorders.add(new BorderRectangle(20,200,x+180,y));
					tiles.add(new Rectangle(20,200,x+180,y,BROWN));	
					break;
				case 8:
					tiles.add(new Tile(x,y));
					horizBorders.add(new BorderRectangle(200,20,x,y+180));
					tiles.add(new Rectangle(200,20,x,y+180,BROWN));
					vertBorders.add(new BorderRectangle(20,200,x,y));
					tiles.add(new Rectangle(20,200,x,y,BROWN));
					vertBorders.add(new BorderRectangle(20,200,x+180,y));
					tiles.add(new Rectangle(20,200,x+180,y,BROWN));
					break;
				case 9:
					tiles.add(new Tile(x,y));
					horizBorders.add(new BorderRectangle(200,20,x,y));
					tiles.add(new Rectangle(200,20,x,y,BROWN));
					vertBorders.add(new BorderRectangle(20,200,x,y));
					tiles.add(new Rectangle(20,200,x,y,BROWN));
					vertBorders.add(new BorderRectangle(20,200,x+180,y));
					tiles.add(new Rectangle(20,200,x+180,y,BROWN));
					break;
				case 10:
					tiles.add(new Tile(x,y));
					vertBorders.add(new BorderRectangle(20,200,x,y));
					tiles.add(new Rectangle(20,200,x,y,BROWN));
					vertBorders.add(new BorderRectangle(20,200,x+180,y));
					tiles.add(new Rectangle(20,200,x+180,y,BROWN));
					break;
				case 11:
					tiles.add(new Tile(x,y));
					horizBorders.add(new BorderRectangle(200,20,x,y));
					tiles.add(new Rectangle(200,20,x,y,BROWN));
					horizBorders.add(new BorderRectangle(200,20,x,y+180));
					tiles.add(new Rectangle(200,20,x,y+180,BROWN));
					vertBorders.add(new BorderRectangle(20,90,x+50,y));
					tiles.add(new Rectangle(20,80,x+50,y,BROWN));	
					vertBorders.add(new BorderRectangle(20,90,x+150,y+100));
					tiles.add(new Rectangle(20,80,x+150,y+100,BROWN));	
					break;
				case 12:
					tiles.add(new Tile(x,y));
					horizBorders.add(new BorderRectangle(200,20,x,y));
					tiles.add(new Rectangle(200,20,x,y,BROWN));
					break;
				case 13:
					tiles.add(new Tile(x,y));
					vertBorders.add(new BorderRectangle(20,200,x,y));
					tiles.add(new Rectangle(20,200,x,y,BROWN));
					break;
				case 14:
					tiles.add(new Tile(x,y));
					vertBorders.add(new BorderRectangle(20,200,x+180,y));
					tiles.add(new Rectangle(20,200,x+180,y,BROWN));
					break;
				case 15:
					tiles.add(new Tile(x,y));
					break;
				case 16:
					tiles.add(new Tile(x,y));
					horizBorders.add(new BorderRectangle(200,20,x,y+180));
					tiles.add(new Rectangle(200,20,x,y+180,BROWN));
					break;
			}
		}
	}