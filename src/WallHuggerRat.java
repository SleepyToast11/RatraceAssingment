public class WallHuggerRat implements Animal{

    int        startCol   = 0;
    int        startRow   = 0;
    int        currentCol = 0;
    int        currentRow = 0;
    String     name       = "Wall Hugger";
    int        numMoves   = 0;

    int        relativeDirection = 0;   //value of direction to be stored across

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

    public void setStartColumn(int col) { startCol = col; }

    // sets row where animal started in maze
    public void setStartRow(int row){ startRow = row; }

    // moves animal back to starting row/column, wipes # moves to 0
    public void reset() {
        currentRow = startRow;
        currentCol = startCol;
        numMoves = 0;
    }

    /*converts direction into int[] coordinate(row, col) and returns it
    up = 0, right = 1, down = 2, left = 3 */
    private int[] directionConverter(int direction){
        int[] result = new int[2];
        switch ((direction + relativeDirection)%4){
            case 0:
                result[0] = currentRow - 1;
                result[1] = currentCol;         //up
                return result;
                case 1:
                    result[0] = currentRow;
                    result[1] = currentCol + 1;       //right
                    return result; case 2: result[0] = currentRow + 1;
                    result[1] = currentCol;         //DOWN
                return result;
                case 3:
                    result[0] = currentRow;
                    result[1] = currentCol - 1;       //Left
                    return result;
        }
        return result;
    }

    /*called to apply logic of wallHugger algorithm, checks if rat can move in up, right, left, down order, stores direction
    * and move direction as an int[]
    * */
    private int directionToMove(Maze maz){

        int[] moveDirection = new int[2];

        for (int i = 0; i <= 4; i++) {

            switch (i){
                case 0:
                    moveDirection = directionConverter(1);
                    if (maz.canMove(moveDirection)){
                        currentRow = moveDirection[0];
                        currentCol = moveDirection[1];
                        return 1;
                    }

                    break; case 1: moveDirection = directionConverter(0);
                    if (maz.canMove(moveDirection)){
                        System.out.println("going relative up");
                        currentRow = moveDirection[0];
                        currentCol = moveDirection[1];
                        return 0;
                    }

                    break; case 3: moveDirection = directionConverter(2);
                    if (maz.canMove(moveDirection)){
                        System.out.println("going relative down");
                        currentRow = moveDirection[0];
                        currentCol = moveDirection[1];
                        return 2;
                    }

                    break; case 2: moveDirection = directionConverter(3);
                    if (maz.canMove(moveDirection)){
                        System.out.println("going relative left");
                        currentRow = moveDirection[0];
                        currentCol = moveDirection[1];
                        return 3;
                    }

                    break;
            }
        }
        return -1;
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
    }

    //called by gui to move animal
    public void move(Maze maz) {
        int relative = directionToMove(maz);
        relativeDirection = (relativeDirection + relative)%4;
        System.out.println(relativeDirection);
        numMoves++;
    }
}
