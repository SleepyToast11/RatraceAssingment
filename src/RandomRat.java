import java.util.Random;
import java.util.Stack;

public class RandomRat implements Animal {

        static Random rnd = new Random();

        // constructors
        public RandomRat() {
        }

        int        startCol   = 0;
        int        startRow   = 0;
        int        currentCol = 0;
        int        currentRow = 0;
        String     name       = "NoName";
        int        numMoves   = 0;
        int        prevMove = 5;        //previous move to be compared to
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

    /*converts direction into int[] coordinate(row, col) and returns it
    up = 0, right = 1, down = 2, left = 3 */
    private int[] directionConverter(int direction){

        int[] result = new int[2];
        switch (direction){
            case 0:
                result[0] = currentRow - 1;
                result[1] = currentCol;         //up
                return result;
            case 1:
                result[0] = currentRow;
                result[1] = currentCol + 1;       //right
                return result;
            case 2:
                result[0] = currentRow + 1;
                result[1] = currentCol;         //DOWN
                return result;
            case 3:
                result[0] = currentRow;
                result[1] = currentCol - 1;       //Left
                return result;
        }
        return result;
    }

        //count how many direction animal can move to and returns the result as an int
        private int freeSquare(Maze maz){
            int result = 0;
            for (int i = 0; i < 4; i++) {
                if (maz.canMove(directionConverter(i)))
                    result++;
            }
            System.out.println("counter " + result);
            return result;
        }

        // asks animal to make a move in this maze. This is called by the Maze
        public void move(Maze maz){
            int thisMove = -1;
            int random;
            boolean noMoveFound = true;

            while (noMoveFound){

                random = rnd.nextInt(4);    //picks a random direction to move to
                System.out.println(random);

                /*if move chosen at random is not the same as previous move or if rat
                has only one direction to go
                */
                if (prevMove != random || freeSquare(maz) == 1){

                    switch (random) {

                        case 0:
                            if (maz.canMove(currentRow - 1, currentCol)) {
                                currentRow--;                         //up
                                noMoveFound = false;
                            }
                            break;

                        case 1:
                            if (maz.canMove(currentRow, currentCol + 1)) {
                                currentCol++;                         //RIGHT
                                noMoveFound = false;
                            }
                            break;

                        case 2:
                            if (maz.canMove(currentRow + 1, currentCol)) {
                                currentRow++;                         //DOWN
                                noMoveFound = false;
                            }
                            break;

                        case 3:
                            if (maz.canMove(currentRow, currentCol - 1)) {
                                currentCol--;                         //Left
                                noMoveFound = false;
                            }
                            break;
                    }
                }
                thisMove = random;
                }
            prevMove = (thisMove + 2)%4;
            numMoves++;
        }

        // moves animal back to starting row/column, wipes # moves to 0
        public void reset() {
            currentRow = startRow;
            currentCol = startCol;
            numMoves = 0;
        }

        // sets column where animal started in maze
        public void setStartColumn(int col) { startCol = col; }

        // sets row where animal started in maze
        public void setStartRow(int row){ startRow = row; }


    }
