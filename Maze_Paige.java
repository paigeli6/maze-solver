/*
=========================================================================================
Shortest Path Maze Program
Paige Li
November 18, 2022
Java, version: 2021-12 (4.22.0)
=========================================================================================
Problem Definition – Required to find the shortest path from rat to cheese, then from cheese to exit
Input – a name of a file that contains a maze, until the word exit is input
Output – the original maze inputted, the shortest path and distance from rat to cheese, and the shortest path 
		and distance from cheese to exit. 
Process – ask user for filename of maze, and read file inputted by user
             – find the positions of the rat, cheese, and exit within the maze. 
             – find shortest path from rat to cheese through recursion
             – find shortest path from cheese to exit through recursion
             – recursion will test all possible pathways from each of the four sides of mouse position to a destination
             – show entire path taken by mouse
             – prompt user to enter another file until they enter “exit”
=========================================================================================
List of Identifiers
*STATIC VARIABLES*
R_SIZE - a static variable constant that stores the number of rows in the maze <type int>
C_SIZE - a static variable constant that stores the number of columns in the maze<type int>
SENTINEL - a static variable constant that allows user to to exit program by typing “exit” <type String>
mousePos[] - a one-dimensional array that stores the row and column number of the rat in the maze <type int[]>
cheesePos[]-  a one-dimensional array that stores the row and column number of the cheese in the maze <type int[]>
exitPos[] -  a one-dimensional array that stores the row and column number of the exit in the maze <type int[]>
maze[][] - a two-dimensional array that stores the original maze inputted by the user from a file <type String[][]>
solution[][] -  a two-dimensional array that stores the shortest path from a start point and an
 				end point within the maze <type String[][]>
finalPath [][] -  a two-dimensional array that stores the full path taken by mouse, including path from 
				start to cheese, and then from cheese to exit <type String[][]>
visited [][]-  a two-dimensional array that keeps tracks of the positions that the rat has already been in <type String[][]>
-----------------------------------------------------------------------------------------
*NON-STATIC VARIABLES* 
br - a BufferedReader object used for keyboard input <type BufferedReader>
fileName - the name of the file inputted by the user <type String>
input - a BufferedReader object used for keyboard input to get file name <type BufferedReader>
line - the values of one line of the maze file <type String>
rowNum - the number of rows in the maze from file inputted by user <type int>
temp - a one-dimensional array containing each value/character in one row of the maze file <type String>
dist - an integer value that keeps track of the distance the rat travels <type int>
distToCheese - an integer value that stores the shortest distance from start position to cheese <type int>
distToExit - an integer value that stores the shortest distance from cheese position to exit <type int>
=========================================================================================
*/

import java.io.*;
public class Maze_Paige {
	static final int R_SIZE = 8;
	static final int C_SIZE = 12;
	static final String SENTINEL = "exit";
	/*Instantiating class constants */
	static int mousePos[] = new int[2];
	static int cheesePos[] = new int[2];
	static int exitPos[] = new int[2];
	static String maze[][] = new String [R_SIZE][C_SIZE];
	static String solution[][] = new String [R_SIZE][C_SIZE];
	static String finalPath [][] = new String[R_SIZE][C_SIZE];		
	static String visited [][] = new String[R_SIZE][C_SIZE];		
	/*Declares and instantiates static variables */
	
	/** Main Method - a procedural method
	 * calls other methods to execute code in a specific order
	 * 
	 * @param args <type String[]>
	 * @return void
	 * @throws IOException
	 * */
	public static void main(String[] args) throws IOException {		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));		//Declaration and Instantiation of a BufferedReader object
		String fileName; 																//Declare String variable fileName
		System.out.println("\n\nWelcome to the Shortest Path of Maze Program!"); 		//Welcome message tells user what program will do
		System.out.println("\nThis program will ask you to input a maze file "); 
		System.out.println("within the java project folder until you enter <exit>");
		System.out.println("\nIt will then display the shortest path the rat takes ");
		System.out.println("from the starting point to cheese, then from cheese to exit of maze. ");

		askFileName(); 																	//method prints out user prompt, asking for file input
		fileName = br.readLine(); 														//get user input through br object
		
		while(!fileName.equals(SENTINEL)) { 											//while loop that continues until user enters exit condition "exit"
			try{ 																		//try catch to prevent errors when user enters invalid input
				readMaze(fileName);														//stores the maze from inputted file into a 2D array in the program		
				initializeSol(solution); 												//initializes solution path
				visited=maze.clone();													//maze is copied into another 2D array to keep track of where rat has been
				getPosition(cheesePos, "C");											//get coordinates of cheese, pass cheese position and object corresponding to cheese into method
				getPosition(mousePos, "R", "M");										//get coordinates of mouse, pass rat position and object corresponding to rat into method
				getPosition(exitPos, "X");									  			//get coordinates of exit, pass exit position and object corresponding to exit into method

				if(cheesePos[0] != -1 && cheesePos[1] != -1 && mousePos[0] != -1 && 
						mousePos[1] != -1 && exitPos[0] != -1 && exitPos[1] != -1) {	//checks that the maze has a mouse, cheese, and an exit
					
					int distToCheese = getShortestDist(maze, mousePos, cheesePos, "C");	//finds shortest path, will print out the path if there is one
				    if (distToCheese != -1) { 											//if there is a valid distance from start to cheese, path will be printed
				    	System.out.println("\n\nDisplays path from Rat/Mouse to Cheese");
				    	printPath(solution);											//shortest path from start to cheese is printed 
				    	System.out.println("\nShortest distance from Rat to Cheese is: " + distToCheese);	
				    	getFinalPath();													//final path that keeps track of rat's entire path is updated				
				    }else {																//if there is no distance returned for distance between start and mouse, error message is printed
				      System.out.println("Shortest Path to cheese doesn't exist");
				    }
				    
				    int distToExit = getShortestDist(maze, cheesePos, exitPos, "X");	//finds shortest path, will print out the path if there is one
				    if (distToExit != -1 && distToCheese != -1) {						//if there is a path from rat to cheese, and there is a valid distance from cheese to exit, path will be printed
				    	System.out.println("\n\nDisplays path from Cheese to Exit");
				    	printPath(solution);											//shortest path from cheese to exit is printed 
				    	System.out.println("\nShortest distance from Cheese to Exit is: " + distToExit);
				    	getFinalPath();													//final path that keeps track of rat's entire path is updated
				    }else {																//if there is no distance returned for distance between start and mouse, error message is printed
				      System.out.println("Shortest Path to exit doesn't exist");
				    }
				   
				    if (distToCheese != -1 && distToExit != -1) { 						//if valid distance from start to cheese and cheese to exit are both returned, full path taken 			    	
				    	System.out.println("\n\nDisplays full path taken by Rat");		//by rat is printed out
				
				    	printPath(finalPath);	
				    	System.out.println("\nDistance for FULL path is: " + (distToExit + distToCheese));
				    }
				    
				}else {
					if(cheesePos[0] == -1 && cheesePos[1] == -1) {						//if there is no cheese in the maze, an error message will print
						System.out.println("\nThere is no cheese in this maze");
					}
					if(mousePos[0] == -1 && mousePos[1] == -1) {						//if there is no mouse in the maze, an error message will print
						System.out.println("\nThere is no mouse in this maze");
					}		
					if(exitPos[0] == -1 && exitPos[1] == -1) {							//if there is no exit in the maze, an error message will print
						System.out.println("\nThere is no exit in this maze");
					}	
				}
				askFileName();															//method prints out user prompt, asking for file input
				fileName = br.readLine();												//get user input through br object
			
			}catch (Exception e) {														//catch for errors when user inputs name of file
				System.out.println("Error. Please try again");							//error message is displayed
				askFileName();															//method prints out user prompt, asking for file input
				fileName = br.readLine();												//get user input through br object
			}//end of try catch	
			
		}//end of while
		System.out.println("Thank you for using this program!");						//Goodbye message
	}//end of main method
	
	/**askFileName method:
	 * This procedural method outputs information on the limitations on the file
	 * that the user may input, and asks user to input the name of their maze file.
	 * It includes the exit condition for the program.
	 * 
	 * @param none
	 * @return void
	 * */
	public static void askFileName() {
		System.out.println("\n------------------------------------------------------------");
		System.out.println("\nPlease enter the name of your maze file. Enter <exit> to end the program");
		System.out.println("*This file must be a text file.");
		System.out.println("*This file must be located within the java project file.");
		System.out.println("*Do NOT include .txt when inputting the file name");

	}//end of method
	
	/**readMaze method:
	 * This functional method uses FileReader to read the file inputted from the user.
	 * It uses a String variable to store the values of each row of the maze, and
	 * using the split() function allows the row of values to be stored in a 1D array
	 * (each index is one character of the maze)
	 * the number of rows increments each time the file reader reads the next line, and
	 * it is not null.
	 * the values of the maze from the inputted file is then stored into a 2D array using a nested for loop.
	 * the maze for the final path taken by mouse is also initialized here, as it takes place in the same maze. 
	 * This method also outputs a legend, and what the original maze looks like from the file. 
	 * 
	 * @param fileName - name of file entered by user <type String>
	 * @throws IO Exception 
	 * @return maze - two-dimensional array which stores the original maze entered from file by user<type String[][]>
	 * */
	public static String[][] readMaze(String fileName) throws IOException {
		BufferedReader input;													//created BufferedReader object named input
		String line;															//declaring String variable
		input = new BufferedReader(new FileReader(fileName + ".txt"));			//get input of file name that must already be in java project folder
		int rowNum=0;															//set row number in maze to 0
		String temp[] = new String[C_SIZE];										//create a temporary array to store values of each row
		line=input.readLine();													//read each line of file
		
		while(line!=null) {														//read lines of file while there is text (while the file is not null)
			temp=line.split(" ");												//split each line into individual letters of the maze and store into 1D array
			for(int j=0; j<C_SIZE; j++) {										//set values of each row in maze array to the row values of maze obtained through split function
				maze[rowNum][j]=temp[j];										//initializing maze 2D array
				finalPath[rowNum][j]=temp[j]	;								//initializing final path rat takes in the maze
			}//end of for loop
			rowNum++;															//increment i so values of next row of maze can be filled
			line=input.readLine();												//read next line of file		
		}//end of while loop
		for(int r=0; r<R_SIZE; r++) {											//nested for loop goes through all values of the maze and changes lowercase symbols to uppercase
			for(int c=0; c<C_SIZE; c++) {
				if(maze[r][c].equalsIgnoreCase("C")) {
					maze[r][c]="C";												//position of cheese in the maze is shown by a capital C
					finalPath[r][c]="C";
				}else if(maze[r][c].equalsIgnoreCase("X")) {					//position of exit in the maze is shown by a capital X
					maze[r][c]="X";
					finalPath[r][c]="X";
				}else if(maze[r][c].equalsIgnoreCase("R")) {					//position of mouse in the maze is shown by a capital R or M
					maze[r][c]="R";
					finalPath[r][c]="R";
				}else if(maze[r][c].equalsIgnoreCase("M")) {
					maze[r][c]="M";
					finalPath[r][c]="M";										//positions of mouse, rat, and cheese in final path array is also changed to all capitals
				}//end of if/else statement
			}//end of inner for loop
		}//end of outer for loop
		printLegend();															//print legend so user can see what symbols of maze mean
		printPath(maze);														//print out what the original maze looks like from the inputted file
		input.close();															//close stream
		return maze;															//return values of the maze
	}
	
	/**getPosition method:
	 * This procedural method uses accessor and mutator to get the value
	 * of the coordinates of either cheese or exit within the maze. 
	 * It creates and object to access another class named MazeCoords
	 * 
	 * @param objectPos - 1D array containing the coordinates of the object <type int[]>
	 * @param object - a String value which is a letter corresponding to an object of the maze <type String>
	 * @return void
	 * @see MazeCoords class
	 * */
	static void getPosition(int[] objectPos, String object){
		MazeCoords mC = new MazeCoords();										//create object to access another class mazeCoords
		mC.setMazeCoords(maze, R_SIZE, C_SIZE, object);							//use object to call method from another class mazeCoords and pass in the letter corresponding to the object being located
		objectPos[0]=mC.getXPos();												//set row value of the object by calling method from other class to get the row value
		objectPos[1]=mC.getYPos();												//set column value of the object by calling method from other class to get the column value
	}//end of method
	 
	/**getPosition method:
	 * This overridden procedural method uses accessor and mutator to get the value
	 * of the coordinates of the rat within the maze. 
	 * An override method is needed to get the rat position because the Letter
	 * corresponding to the rat in the maze can be either "R" OR "M"
	 * It creates and object to access another class named MazeCoords
	 * 
	 * @param objectPos - 1D array containing the coordinates of the object <type int[]>
	 * @param object - a String value which is a letter corresponding to an object of the maze <type String>
	 * @param subobject - a String value which is another letter corresponding to the same object of the maze <type String>
	 * @return void
	 * @see MazeCoords class
	 * */
	static void getPosition(int[] objectPos, String object, String subObject){
		MazeCoords mC = new MazeCoords();										//create object to access another class mazeCoords
		mC.setMazeCoords(maze, R_SIZE, C_SIZE, object, subObject);				//use object to call method from another class mazeCoords and pass in the two letters corresponding to the object being located
		objectPos[0]=mC.getXPos();												//set row value of the object by calling method from other class to get the row value
		objectPos[1]=mC.getYPos();												//set column value of the object by calling method from other class to get the column value
	}//end of method
	
	/**printLegend method:
	 * This procedural method outputs the Legend that the maze follows
	 * 
	 * @param none
	 * @return void
	 * */
	static void printLegend() {
		System.out.println("\n------------LEGEND------------");
		System.out.println("<B> : Barrier");
		System.out.println("<C> : Cheese");
		System.out.println("<X> : Exit");
		System.out.println("<M> | <R> : Mouse/Rat");
		System.out.println("<.> : Possible path");
		System.out.println("<+> : Path taken by mouse");
		System.out.println("<#> : Start point of mouse");
		System.out.println("<@> : Cheese eaten by mouse");
		System.out.println("</> : Mouse exited the maze");
		System.out.println("\n Display of original maze: ");
	}//end of method 
	
	/**printPath method:
	 * This procedural method outputs the maze, possibly along with a path 
	 * Ex original maze, path from cheese to exit, path from start to cheese, etc. 
	 * 
	 * @param pathName - 2D array which contains the values of the maze, or possibly path of maze <type String[][]> 
	 * @return void
	 * */
	static void printPath(String[][]pathName) {
		System.out.println("------------------------------");
		for (int row=0; row<R_SIZE; row++) {										//nested for loop goes through all values of the 2D array being passed into the method
			for(int column=0; column<C_SIZE; column++) {
				System.out.print(" " + pathName[row][column]);						//print out the values of the maze
			}//end of inner for loop
			System.out.println("");
		}//end of outer for loop
	}//end of method
	
	/**initializeSol method:
	 * This functional method initializes the solution 2D array
	 * by setting the entire array to 0's
	 * 
	 * @param sol - 2D array that contains the position of where shortest path is in the maze <type String[][]>
	 * @return sol - 2D array that contains the position of where shortest path is in the maze<type String[][]>
	 * */
	public static String[][] initializeSol (String sol[][]) {					//initializes solution path 2D array
		for (int x = 0; x < R_SIZE; x++) {										//nested for loop goes through all values of 2D array
			for (int y = 0; y < C_SIZE; y++) {
				sol[x][y] = "0";												//set entire 2D array to 0 for initializing
			}//end of inner for loop
		}//end of outer for loop
		return sol;																//return the 2D array after initializing
	}//end of method
	
	/**getFinalPath method:
	 * This procedural method updates what the final entire path taken by rat looks like
	 * by changing any position from any shortest path taken by mouse to a "+", which
	 * indicates that it is a path taken by the mouse during the entire process. 
	 * The final path will display where the mouse at the cheese, and also where it exited the maze.
	 * 
	 * @param none
	 * @return void
	 * */
	static void getFinalPath() {
		for (int x = 0; x < R_SIZE; x++) {											//nested for loop goes through all values of 2D array maze
			for (int y = 0; y < C_SIZE; y++) {
				if(solution[x][y].equals("+")) {									//if a position of the maze has been visited by rat when going from start to cheese or cheese to exit, 
					finalPath[x][y]=("+");											//change the symbol of that position in the finalPath to show that it was a place traveled by rat
				}//end of if statement
			}//end of inner for loop
		}//end of outer for loop
		finalPath[exitPos[0]][exitPos[1]]="/";										//indicate where the rat exited from in the maze by changing exit position to a "/"
    	finalPath[cheesePos[0]][cheesePos[1]]="@";									//indicate where the rat ate cheese from in the maze by changing cheese position to a "@"
    	finalPath[mousePos[0]][mousePos[1]]="#";									//indicate where the rat started from in the maze by changing rat starting position to a "#"
	}//end of method
	
	/**isValid method:
	 * This functional method returns a boolean that shows if a position of the maze is
	 * valid for the rat to travel. A position is valid if it is within the maze, 
	 * if the position is a path or object and not a barrier, and it cannot be a 
	 * position that the rat has already traveled to. 
	 * 
	 * @param maze - original maze from file input by user <type String[][]>
	 * @param visited - 2D array that stores the positions where the mouse has been before <type String[][]>
	 * @param x - the row value of where the object is <type int>
	 * @param y - the column value of where the object is <type int>
	 * @param object - The letter that corresponds to the object <type String>
	 * @return true (conditions are met) <type boolean>
	 * @return false (conditions are not met) <type boolean>
	 * */
	static boolean isValid(String[][] maze, String[][] visited, int x, int y, String object)			//checks if space is safe 
	  {
		return (x >= 0 && x < R_SIZE && y >= 0 && y < C_SIZE && (maze[x][y].equals(".") || maze[x][y].equals(object))  && !visited[x][y].equals("+"));
		//position being checked must be within the maze, must be a possible path (".") or an object in the maze (rat, cheese, or exit), and
		//must be a position that rat has NOT gone to yet in the path it is currently traveling. If the position is valid for all these conditions, 
		//then boolean true is returned and rat can proceed to check other pathways. If position is not valid, false is returned. 
	  }
	
	/**getShortestDist method:
	 * This functional  method returns the integer value of the shortest distance
	 * the rat traveled between two points in the maze. It initializes the integer dist
	 * to max value of an integer, and using the findShortestPath method it will
	 * compare this value to other distances taken by the mouse and find the smallest one
	 * 
	 * @param maze - original maze from file input by user <type String[][]>
	 * @param startPos - 1D array that stores the row and column positions of the object you are starting from <type int[]>
	 * @param destPos - 1D array that stores the row and column positions of the object you are starting from <type int[]>
	 * @param object - The letter that corresponds to the object <type String>
	 * @return dist - distance the mouse traveled <type int>
	 * @return -1 - if no distance is returned, a value of -1 is returned to indicate there is no distance taken by mouse
	 **/
	static int getShortestDist(String[][] maze, int[] startPos, int[] destPos, String object) {
	 
	    int dist = Integer.MAX_VALUE; 										//initialize integer dist to max integer value so it can be compared later to find smallest distance
	    dist = findShortestPath(maze, startPos[0], startPos[1], destPos[0], destPos[1], dist, 0, object);
	    //call method by passing in starting and ending coordinates, distance, current distance traveled (0), and letter corresponding to object being traveled to
	    
	    if (dist != Integer.MAX_VALUE) {//if distance does not equal what it was set to (max value) that means shortest path WAS found
	      return dist; //if dist does not equal max integer value, return the shortest distance that was found in the method. 
	    }//end of if statement
	    return -1; //else if distance equals max integer value, return -1 to show that a shortest distance was not found. 
	  }//end of method
	
	/**findShortestPath method:
	 * This functional  method uses recursion to test all the possible pathways the rat can take
	 * between a starting position and an ending position. It does this through recursion, where
	 * it will test all four sides of the rat and the possible pathways from there until destination
	 * is reached within the shortest distance possible. 
	 * 
	 * @param maze - original maze from file input by user <type String[][]>
	 * @param startPosX - row position of the object you are starting from <type int>
	 * @param startPosX - column position of the object you are starting from <type int>
	 * @param destPosX - row position of the object you are ending at <type int>
	 * @param destPosX - row position of the object you are ending at <type int>
	 * @param dist - distance the mouse traveled <type int>
	 * @param min_dist - shortest distance possible the mouse traveled between two points on the maze <type int>
	 * @param object - The letter that corresponds to the object <type String>
	 * @return min_dist - shortest distance possible the mouse traveled between two points on the maze <type int>
	 **/
	static int findShortestPath(String[][] maze, int startPosX, int startPosY, int destPosX, int destPosY, int min_dist, int dist, String object) //recursive method
	  {
		//min_dist starts at max integer Value and will change in value when a distance smaller than it is found. 
	    if (startPosX == destPosX && startPosY == destPosY) {//if the position of the rat equals the position of the destination point, distance must then be compared											
	    	if(dist < min_dist) { //if this path from start to destination was smaller than the current minimum distance, this distance is the new minimum distance. 
	    	  min_dist=dist;	//set minimum distance to the distance rat traveled in the current path
	    	  initializeSol(solution);	//initialize the solution path so values will not overlap
		      for (int row=0; row<R_SIZE; row++) {	//nested for loop goes through all values of array										
					for(int column=0; column<C_SIZE; column++) {
						solution[row][column]=visited[row][column]; //store path taken by mouse into another array. This other array is used because it's only purpose is to store the final path of the mouse
					}//end of inner for loop
		      }//end of outer for loop
		      if(object.equals("C")) { //if the cheese is destination point, then must show where the rat ate the cheese by changing that position to "@"
		    	  solution[destPosX][destPosY]="@";	//shows that rat ate cheese	
		      }else if(object.equals("X")){ //if the exit is destination point, then must show where the rat exited the maze by changing that position to "/"
		    	  solution[cheesePos[0]][cheesePos[1]]="@";	//shows where rat ate cheese	
		    	  solution[destPosX][destPosY]="/";	//shows that rat exited maze 
		      }//end of if/else statement
		      
	    	}//end of if statement
		    return min_dist; //return minimum distance found 
	    }//end of if statement
	 
	    visited[startPosX][startPosY] = "+";// set the position rat is at as visited, meaning rat been there before
	    
	    // check bottom position from where mouse is
	    if (isValid(maze, visited, startPosX + 1, startPosY, object)) { //checks that the bottom cell of the rat's current position is a valid path
	      min_dist = findShortestPath(maze, startPosX + 1, startPosY, destPosX, destPosY, min_dist, dist + 1, object);
	      //recursion calls findShortestPath for the position that is one square to the bottom of the rat's current position
	      //distance is incremented by 1. 
	      
	    }//end of if statement
	    // check right position from where mouse is
	    if (isValid(maze, visited, startPosX, startPosY + 1, object)) {//checks that the bottom cell of the rat's current position is a valid path
	      min_dist = findShortestPath(maze, startPosX, startPosY + 1, destPosX, destPosY, min_dist, dist + 1, object);
	    //recursion calls findShortestPath for the position that is one square to the right of the rat's current position
	    //distance is incremented by 1. 
	      
	    }//end of if statement
	    // check top position from where mouse is
	    if (isValid(maze, visited, startPosX - 1, startPosY, object)) {//checks that the bottom cell of the rat's current position is a valid path
	      min_dist = findShortestPath(maze, startPosX - 1, startPosY, destPosX, destPosY, min_dist, dist + 1, object);
	    //recursion calls findShortestPath for the position that is one square to the right of the rat's current position
		//distance is incremented by 1.
	      
	    }//end of if statement
	    // check left position from where mouse is
	    if (isValid(maze, visited, startPosX, startPosY - 1, object)) {//checks that the bottom cell of the rat's current position is a valid path
	      min_dist = findShortestPath(maze, startPosX, startPosY - 1, destPosX, destPosY, min_dist, dist + 1, object);
	    //recursion calls findShortestPath for the position that is one square to the right of the rat's current position
		//distance is incremented by 1.
	      
	    }//end of if statement
	    visited[startPosX][startPosY]="."; // backtrack: if rat has no where else to go because all four directions are invalid, 
	    //remove rat's current position from the visited array by changing the symbol back to "."    
	    return min_dist;
	    
	  }//end of method
}//end of class
	
