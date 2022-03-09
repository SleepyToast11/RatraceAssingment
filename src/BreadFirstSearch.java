import java.util.ArrayList;
import java.util.Stack;


public class BreadFirstSearch implements Animal{

	int        startCol   = 0;
	int        startRow   = 0;
	int        currentCol = 0;
	int        currentRow = 0;
	String     name       = "NoName";
	int        numMoves   = 0;
	int        moveDirection;

	private final Stack<Stack> tileMoves = new Stack<Stack>();  //stack of stacks, each stack is one tile and these stack contain every possible direction that rat can move to

	private boolean learning      = true;                       //flag to indicate if rat is at the learning or learnt phase

	private Stack<Object> learntMoves     = new Stack<Object>();    //stack of learnt move, from start to finish without. this stack is preserved through resets. isn't directly used to use
	private Stack<Integer> learntMovesTemp  = new Stack<Integer>(); //copy of learnt moves to be used to move animal, as stack needs to be popped and therefore will be deleted

	private ArrayList<int[]> tilesVisited = new ArrayList<int[]>(); //list of all tile visited, but start

	private Stack<Integer> startingTileMoves= new Stack<Integer>(); //list of moves of the starting tile


	// constructor
	public BreadFirstSearch() {
	}

	// returns current row animal is in
	public int getRow(){ return currentRow; }
	// returns current column animal is in
	public int getColumn() { return currentCol; }
	// returns one letter to represent animal
	public char getLetter() { return name.charAt(0); }
	// returns animal's name
	public String getName(){ return name; }
	// returns # moves animal has made in maze so far
	public int getNumMoves() { return numMoves;  }
	// returns column where animal started in maze
	public int getStartColumn(){  return startCol;  }
	// returns row where animal started in maze
	public int getStartRow(){ return startRow; }

	//adds current position to list of visited tile if not starting tile
	private void addCurrentPos(Maze maz){
		if (maz.getSquare(currentRow, currentCol) != Maze.START)
			tilesVisited.add(currentPos());
	}

	private int[] currentPos(){
		return new int[]{currentRow, currentCol};
	}

	private int[] startPos(){
		return new int[]{startRow, startCol};
	}


	/*Returns true if two position (in the form of int[] size 2 = {row, col}) are equal
	* and false otherwise*/
	private boolean equalPosition(int[] pos1, int[] pos2){
		if (pos1.length != 2 || pos2.length != 2)
			return false;
		else
			for (int i = 0; i < 2; i++) {
				if (pos1[i] != pos2[i])
					return false;
			}
		return true;
	}


	//returns if animal has previously visited the current tile
	private boolean hasVisited(){
		for (int i = 0; i < tilesVisited.size() - 1; i++){
			if (equalPosition(currentPos(), tilesVisited.get(i)))
				return true;
		}
		return false;
	}


	/*converts direction into int[] coordinate(row, col) and returns it
	up = 0, right = 1, down = 2, left = 3 */
	private int[] directionConverter(int direction){
		int[] result = new int[2];

		switch (direction){
			case 0:
				result[0] = currentRow - 1;
				result[1] = currentCol;         //up
				return result; case 1: result[0] = currentRow;
				result[1] = currentCol + 1;       //right
				return result; case 2: result[0] = currentRow + 1;
				result[1] = currentCol;         //DOWN
				return result; case 3: result[0] = currentRow;
				result[1] = currentCol - 1;       //Left
				return result;
		}

		return result;
	}

	//move animals coordinate with provided direction. up = 0, right = 1, down = 2, left = 3
	private void moveAnimal(int direction){
		switch (direction){
			case 0:
				currentRow--;                         //up
				break;
			case 1:
				currentCol++;                         //RIGHT
				break;
			case 2:
				currentRow++;                         //DOWN
				break;
			case 3:
				currentCol--;                         //Left
				break;
		}
		numMoves++;
	}

	//Takes the path, taken to get to finish and reverses it up to the start
	private void learn(){

		while (!learntMoves.isEmpty())  //clear learntMoves, if not already empty
			learntMoves.pop();

		learntMoves.push(moveDirection);    //pushes moveDirection to the bottom of the stack, as it wasn't used

		do{
			learntMoves.push((goBack()+2)%4);   //pushes the reverse path from finish to start
		}while((!tileMoves.isEmpty()));

		resetKeepLearnt();      //resets position and numMoves to show optimized path
		learning = false;       //sets flag to false,
	}

	//called when animal is on starting tile
	private void startingTile(Maze maz){

		if (numMoves == 0){     //when starting, checks every possible move and stores in stack startingTileMoves
			for (int i = 0; i < 4 ; i++) {
				if (maz.canMove(directionConverter(i))){
					startingTileMoves.push(i);
				}
			}
			System.out.println(startingTileMoves.toString());
		}

		else {                          //when returning to the starting tile
			startingTileMoves.pop();    //removes the first possible direction
			tileMoves.pop();            //sanitizes the main stack
		}

		moveDirection = startingTileMoves.peek();
		moveAnimal(startingTileMoves.peek());
		addCurrentPos(maz);
	}

	//return the direction from which rat has come from for the first tile on the main stack
	private int goBack(){
		int tempInt = 0;
		Stack tempStack = tileMoves.pop();
		while (!tempStack.isEmpty()){
			tempInt = (int) tempStack.pop();
		}
		return tempInt;
	}

	/*when current tile is visited for first time, check for possible movement direction
	starting with the one towards the previous tile and going clockwise and pushing as a stack to the main stack.
	then calls to move animal*/
	private void firstVisitTile(Maze maz){

		Stack possibleMoves = new Stack();
		for (int i = 0; i < 4; i++) {

			if (maz.canMove(directionConverter((i+ 2 + moveDirection)%4))) {
				possibleMoves.push((i+ 2 + moveDirection)%4);
			}
		}
		System.out.println(possibleMoves.toString());
		tileMoves.push(possibleMoves);
		tileMove(maz);
		addCurrentPos(maz);
	}

	//sanitizes main stack and moves animal to next direction of tile
	private void goneBack(Maze maz){
		tileMoves.pop();
		System.out.println("going back");
		tileMove(maz);
	}

	//called to go back when current tile has been visited
	private void previouslyVisitedTile(){
		moveAnimal((moveDirection + 2) % 4 );   //moves animal back to previous tile
		tileMoves.push(new Stack());           //pushes empty stack to indicate rat has gone back
	}

	//called to move rat on the learnt path
	private void pathLearned(){
		System.out.println(learntMoves.toString());
		System.out.println(learntMovesTemp.toString());
		if (!learntMovesTemp.empty()){
			moveAnimal((int) learntMovesTemp.pop());
			numMoves++;}
	}

	//called to verify if current direction to be moved at is inside the maze boundaries
	private boolean goingOutOfBound(Maze maz){
		int row = directionConverter(moveDirection)[0];
		int col = directionConverter(moveDirection)[1];
		return !maz.contains(row, col);
	}

	/*called to pull a direction from the stack to store it in moveDirection, moves to that direction
	and pushes remaining stack back even if empty which would indicates rat has gone back as the last
	direction in a stack is always the direction to the tile it arrived from at first
	 */
	private void tileMove(Maze maz){

		System.out.println(tileMoves.toString());
		Stack temp = new Stack<>();             //temp stack for operations to be made on
		temp = tileMoves.pop();                 //gets all direction possible at the current tile

		System.out.println(temp.toString());    //debug message of possible all direction rat can move

		moveDirection = (int) temp.pop();       //direction that wil be moved
		System.out.println("moving " + moveDirection);  //debug message indicating which direction to be moved

		if (goingOutOfBound(maz)){                  //check if current direction is going out of bound
			tileMoves.push(temp);           //if yes, pushes the temp stack back, the stack is free from direction going oob
			tileMove(maz);                  //recursively calls the function until all out of bounds directions are removed
			return;                         //exits function after recursion has occurred
		}
		tileMoves.push(temp);               //pushes back borrowed stack to main stack

		/*intercept move direction to avoid rat getting on finish first time around
		 * as this would lead to the end of the program instead of showing the optimized path*/
		if(maz.getSquare(directionConverter(moveDirection)) == Maze.FINISH){
			learn();        //calls function extract path used
			System.out.println("learning");
			return;     //exits function to avoid rat being moved to finish
		}

		moveAnimal(moveDirection);
		System.out.println(temp.toString());
	}

	/*** asks animal to make a move in this maze. This is called by the Maze
	 * @param maz current maze to be solved*/
	public void move(Maze maz){

		if (!learning){         //when path is learnt, move() will use pathlearnt() to traverse instead of relearning
			pathLearned();
		}

		else{
			if (equalPosition(currentPos(), startPos())){       //if at starting tile. calls special function startingTile()
				System.out.println("start pos");
				startingTile(maz);}


            /*if current tile has been visited before
             and there isn;t an empty stack indicating that rat has gone back from a previous tile*/
			else if ((hasVisited() && !tileMoves.peek().empty())){
				System.out.println("prev");
				previouslyVisitedTile();}

            /* when first time at tile and stack doesn't indicate that is has gone back,
            calls function to check possible direction and moves rat */
			else if ((!hasVisited() && tileMoves.empty()) || (!hasVisited() && !tileMoves.peek().empty())){
				firstVisitTile(maz);
				System.out.println("first");}

            /*if an empty stack is present on top of main stack,
            will call gone back to sanitize the stack to prepare for next movement*/
			else{
				System.out.println("gone back");
				goneBack(maz);
			}
			System.out.println(tileMoves.toString());
		}
	}

	/**
	 * clears content of stack2 and
	 * Copy the content of stack 1 to stack2
	 * @param stack1 Stack to bbe copied, unchanged
	 * @param stack2 Stack to be emptied and copied to*/
	public void copyStack(Stack stack1, Stack stack2){
		Stack temp = new Stack();
		while(!stack2.isEmpty())
			stack2.pop();
		while (!stack1.empty()) {
			temp.push(stack1.pop());
		}
		System.out.println(stack1.toString());
		while(!temp.empty()){
			stack1.push(temp.peek());
			stack2.push(temp.pop());
		}
	}

	// moves animal back to starting row/column, wipes # moves to 0, removes learnt path from memory and resets learning flag
	public void reset() {
		currentRow = startRow;
		currentCol = startCol;
		numMoves = 0;
		copyStack(learntMoves, learntMovesTemp);
		tilesVisited = new ArrayList<>();
		learning = true;
	}

	/**reset position and move number of rat
	 * while keeping the learning flag set*/
	public void resetKeepLearnt() {
		currentRow = startRow;
		currentCol = startCol;
		numMoves = 0;
		copyStack(learntMoves, learntMovesTemp);
		tilesVisited = new ArrayList<>();
	}

	// sets column where animal started in maze
	public void setStartColumn(int col) { startCol = col; }
	// sets row where animal started in maze
	public void setStartRow(int row){ startRow = row;}
}