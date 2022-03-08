import java.util.ArrayList;
import java.util.Stack;

public class BreadFirstSearch implements Animal{

    int        startCol   = 0;
    int        startRow   = 0;
    int        currentCol = 0;
    int        currentRow = 0;
    String     name       = "NoName";
    int        numMoves   = 0;
    int        moveDirection = -1;
    private final Stack<Stack> tileMoves = new Stack<Stack>();
    private boolean learning      = true;
    private Stack<Object> learntMoves     = new Stack<Object>();
    private Stack<Integer> learntMovesTemp  = new Stack<Integer>();
    private ArrayList<int[]> tilesVisited = new ArrayList<int[]>();
    private Stack<Integer> startingTileMoves= new Stack<Integer>();


    // constructors
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

    private boolean hasVisited(){
        for (int i = 0; i < tilesVisited.size() - 1; i++){
            if (equalPosition(currentPos(), tilesVisited.get(i)))
                return true;
        }
        return false;
    }


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

    private void learn(){
        while (!learntMoves.isEmpty())
            learntMoves.pop();
        learntMoves.push(moveDirection);
        do{

            learntMoves.push((goBack()+2)%4);
        }while((!tileMoves.isEmpty()));
        resetKeepLearnt();
        learning = false;
    }

    private void startingTile(Maze maz){
        if (numMoves == 0){
            for (int i = 0; i < 4 ; i++) {
                if (maz.canMove(directionConverter(i))){
                    startingTileMoves.push(i);
                }
            }
            System.out.println(startingTileMoves.toString());
        }
        else {
            startingTileMoves.pop();
            tileMoves.pop();
        }
        moveDirection = (int) startingTileMoves.peek();
        moveAnimal((int) startingTileMoves.peek());
        addCurrentPos(maz);
    }

    private int goBack(){
        int tempInt = 0;
        Stack tempStack = tileMoves.pop();
        while (!tempStack.isEmpty()){
            tempInt = (int) tempStack.pop();
        }
        return tempInt;
    }

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

    private void goneBack(Maze maz){
        tileMoves.pop();
        System.out.println("going back");
        tileMove(maz);
    }

    private void previouslyVisitedTile(){
        moveAnimal((moveDirection + 2) % 4 );
        tileMoves.push(new Stack());
    }

    private void pathLearned(){
        System.out.println(learntMoves.toString());
        System.out.println(learntMovesTemp.toString());
        if (!learntMovesTemp.empty()){
        moveAnimal((int) learntMovesTemp.pop());
        numMoves++;}
    }

    private void tileMove(Maze maz){
        System.out.println(tileMoves.toString());
        Stack temp = new Stack<>();
        temp = tileMoves.pop();
        System.out.println(temp.toString());
        moveDirection = (int) temp.pop();
        System.out.println("moving " + moveDirection);
        tileMoves.push(temp);
        if(maz.getSquare(directionConverter(moveDirection)) == Maze.FINISH){
            learn();
            System.out.println("learning");
            return;
        }
        moveAnimal(moveDirection);
        System.out.println(temp.toString());
    }

    // asks animal to make a move in this maze. This is called by the Maze
    public void move(Maze maz){
        if (!learning){
            pathLearned();
        }

        else{
            if (equalPosition(currentPos(), startPos())){
                System.out.println("start pos");
                startingTile(maz);}
            else if (hasVisited() && !tileMoves.peek().empty()){
                System.out.println("prev");
                previouslyVisitedTile();}
            else if ((!hasVisited() && tileMoves.empty()) || (!hasVisited() && !tileMoves.peek().empty())){
                firstVisitTile(maz);
                System.out.println("first");}
            else{
                System.out.println("gone back");
                goneBack(maz);
                }
            System.out.println(tileMoves.toString());
        }
    }


    public void copyStack(Stack stack1, Stack stack2){
        Stack temp = new Stack<>();
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

    // moves animal back to starting row/column, wipes # moves to 0
    public void reset() {
        currentRow = startRow;
        currentCol = startCol;
        numMoves = 0;
        copyStack(learntMoves, learntMovesTemp);
        tilesVisited = new ArrayList<>();
        learning = true;
    }


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


