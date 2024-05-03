/*
=========================================================================================
MazeCoords Class
Paige Li
November 18, 2022
Java, version: 2021-12 (4.22.0)
=========================================================================================
Class
This classes find the coordinates (row and column position) of an object within the maze 
(rat, cheese, exit)
=========================================================================================
List of Identifiers
xPos - the row number of the object <type int>
yPos - the column number of the object <type int>
=========================================================================================
*/
class MazeCoords {

	private int xPos=-1, yPos=-1; //initialize private integers xPos and yPos to -1 
	//if the object cannot be found within the maze, these values are returned which
	//results in an error message being printed out
		
	/**setMazeCoords method:
	 * This procedural overload method is a setter method which sets the xPos and yPos 
	 * values to the row and column values of where the object is located in the maze.
	 * This method is used when the object in the maze has only one corresponding letter. 
	 * 
	 * @param maze - 2D array storing the original maze entered from file <type String>
	 * @param R_SIZE - constant that stores number of rows in the maze <type int>
	 * @param C_SIZE - constant that stores number of columns in the maze <type int>
	 * @param object - letter that corresponds to the object being searched for ("C" for cheese, "R" or "M" for rat, "X" for exit) <type String>
	 * @return void
	 * @see Maze_Paige class
	 * */
	void setMazeCoords(String[][]maze, int R_SIZE, int C_SIZE, String object) {
		for (int x = 0; x < R_SIZE; x++) { 										//nested for loop goes through each value of the maze
			for (int y = 0; y < C_SIZE; y++) {
				if(maze[x][y].equals(object)) {									//if coordinate in maze equals the letter, set coordinates to xPos and yPos
					xPos=x;
					yPos=y;
				}//end of if statement
			}//end of inner for loop
		}//end of outer for loop
	}//end of method
	
	/**setMazeCoords method:
	 * This procedural overload method is a setter method which sets the xPos and yPos 
	 * values to the row and column values of where the object is located in the maze.
	 * An overload method is needed because rat has two possibilities of symbols, 
	 * meanwhile cheese and exit only have one symbol. 
	 * 
	 * @param maze - 2D array storing the original maze entered from file <type String>
	 * @param R_SIZE - constant that stores number of rows in the maze <type int>
	 * @param C_SIZE - constant that stores number of columns in the maze <type int>
	 * @param object - letter that corresponds to the object being searched for (letter for rat is "R" OR "M") <type String>
	 * @param object2 - the other letter that corresponds to the object being searched for (letter for rat is "R" OR "M") <type String>
	 * @return void
	 * @see Maze_Paige class
	 * */
	void setMazeCoords(String[][]maze, int R_SIZE, int C_SIZE, String object, String object2) {
		for (int x = 0; x < R_SIZE; x++) {										//nested for loop goes through each value of the maze
			for (int y = 0; y < C_SIZE; y++) {
				if(maze[x][y].equalsIgnoreCase(object) || maze[x][y].equalsIgnoreCase(object2)) {	//if coordinate in maze equals either of the two letters,  set coordinates to xPos and yPos
					xPos=x;
					yPos=y;
				}//end of if statement
			}//end of inner for loop
		}//end of outer for loop
	}//end of method
	
	
	/**getXPos method:
	 * This functional method is the getter method for the row value of the object
	 * 
	 * @param none
	 * @return xPos - the row of the original maze where the object is located <type int>
	 * */
	public int getXPos() {
		return xPos;
	}//end of method
	
	/**getYPos method:
	 * This functional method is the getter method for the column value of the object
	 * 
	 * @param none
	 * @return yPos - the column of the original maze where the object is located <type int>
	 * */
	public int getYPos() {
		return yPos;
	}//end of method
	
}


