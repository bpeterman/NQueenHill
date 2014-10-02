import java.util.Random;
import java.util.Scanner;

public class Main {

    static int boardSize = 8;
	public static int[][] board = null;
	public static int heuristic[][] = null;
	public static int printCount = 0;
	public static int restartCount = 0;

	public static void main(String[] args) {
		System.out.println("Enter Board Size:");
		Scanner in = new Scanner(System.in);
		String s = in.nextLine();
		boardSize=Integer.parseInt(s);
		board = new int[boardSize][boardSize];
		heuristic = new int[boardSize][boardSize];
		in.close();
		start();

	}

	public static void start() {
		System.out.println("Starting");
		boolean goOn = false;
		while (!goOn) {
			initBoard();
			initHeuristic();
			initQueens();
			printBoard();
			if (solve()) {
				goOn = true;
			} else {
				restartCount++;
			}
		}
		System.out.println("Moves: " + printCount);
		System.out.println("Restarts: " + restartCount);
	}

	public static boolean solve() {
		int x;
		int y;
		int newY;
		int stuckCount = 0;
		int[] whereQueen;
		int j = 1;
		int[][] temp = board;
		while (true) {
			genHeuristic(j);
			whereQueen = findQueen(j);
			x = whereQueen[0];
			y = whereQueen[1];
			newY = getSmallest(x);
			pickUpQueen(j, x, y);
			placeQueen(j, x, newY);
			printBoard();
			if (j == boardSize)
				j = 1;
			j++;

			if (temp.equals(board)) {
				stuckCount++;
				if(stuckCount>(boardSize*boardSize))
					return false; //local maxima
			}
			temp=board;
			if (checkIfGood()) {
				System.out.println("Solved!");
				return true;
			}

		}
	}

	public static boolean checkIfGood() {
		int values[];

		for (int i = 1; i <= boardSize; i++) {
			values = findQueen(i);
			if (getConflicts(values[0], values[1], values[0], values[1]) > 0)
				return false;
		}
		return true;
	}

	public static int getSmallest(int col) {
		int smallest = boardSize;
		int smallI = 0;
		for (int i = 0; i < boardSize; i++) {
			if (heuristic[col][i] <= smallest) {
				smallest = heuristic[col][i];
				smallI = i;
			}
		}
		return smallI;
	}

	public static void genHeuristic(int queenNum) {
		int x = findQueen(queenNum)[0];
		int y = findQueen(queenNum)[1];
		for (int i = 0; i < boardSize; i++) {
			heuristic[x][i] = getConflicts(x, i, x, y);
		}
	}

	public static void initBoard() {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				board[i][j] = 0;
			} // j for end
		}// i end
	}

	public static void initHeuristic() {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				heuristic[i][j] = 0;
			} // j for end
		}// i end
	}

	public static void printBoard() {
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				System.out.print(board[j][i] + "|");
			} // j for end
			System.out.println();
		} // i for end
		System.out.println("----------------");
		printCount++;
	}

	public static void initQueens() {
		Random rand = new Random();
		for (int i = 0; i < boardSize; i++) {
			placeQueen(i + 1, i, rand.nextInt(boardSize));
		}

	}

	public static void placeQueen(int queenNum, int x, int y) {
		board[x][y] = queenNum;
	}

	public static void pickUpQueen(int queenNum, int x, int y) {
		board[x][y] = 0;
	}

	public static int[] findQueen(int queenNum) {
		int[] result = new int[2];
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				if (board[i][j] == queenNum) {
					result[0] = i;
					result[1] = j;
					return result;
				}
			}
		}
		return null;
	}

	public static int getConflicts(int x, int y, int curX, int curY) {

		int count = 0;
		count += searchUp(x, y, curX, curY);
		count += searchDown(x, y, curX, curY);
		count += searchLeft(x, y, curX, curY);
		count += searchRight(x, y, curX, curY);
		count += searchUpLeft(x, y, curX, curY);
		count += searchUpRight(x, y, curX, curY);
		count += searchDownLeft(x, y, curX, curY);
		count += searchDownRight(x, y, curX, curY);

		return count;
	}

	public static int searchUp(int x, int y, int curX, int curY) {
		for (int i = y - 1; i < boardSize && i >= 0; i--) {
			if (board[x][i] > 0 && i != curY && i < boardSize) {
				return 1;
			}
		}

		return 0;
	}

	public static int searchDown(int x, int y, int curX, int curY) {
		for (int i = y + 1; i < boardSize && i >= 0; i++) {
			if (board[x][i] > 0 && i != curY && i < boardSize) {
				return 1;
			}
		}

		return 0;
	}

	public static int searchLeft(int x, int y, int curX, int curY) {
		for (int i = x - 1; i < boardSize && i >= 0; i--) {
			if (board[i][y] > 0 && i != curX && i < boardSize) {
				return 1;
			}
		}

		return 0;
	}

	public static int searchRight(int x, int y, int curX, int curY) {
		for (int i = x + 1; i < boardSize && i >= 0; i++) {
			if (board[i][y] > 0 && i != curX && i < boardSize) {
				return 1;
			}
		}

		return 0;
	}

	public static int searchUpLeft(int x, int y, int curX, int curY) {
		for (int i = x - 1, j = y - 1; i < boardSize && j < boardSize && i >= 0
				&& j >= 0; i--, j--) {
			if (board[i][j] > 0 && !(i == curX && j == curY) && i < boardSize
					&& j < boardSize) {
				return 1;
			}
		}

		return 0;
	}

	public static int searchUpRight(int x, int y, int curX, int curY) {
		for (int i = x + 1, j = y - 1; i < boardSize && j < boardSize && i >= 0
				&& j >= 0; i++, j--) {
			if (board[i][j] > 0 && !(i == curX && j == curY) && i < boardSize
					&& j < boardSize) {
				return 1;
			}
		}

		return 0;
	}

	public static int searchDownLeft(int x, int y, int curX, int curY) {
		for (int i = x - 1, j = y + 1; i < boardSize && j < boardSize && i >= 0
				&& j >= 0; i--, j++) {
			if (board[i][j] > 0 && !(i == curX && j == curY) && i < boardSize
					&& j < boardSize) {
				return 1;
			}
		}

		return 0;
	}

	public static int searchDownRight(int x, int y, int curX, int curY) {
		for (int i = x + 1, j = y + 1; i < boardSize && j < boardSize && i >= 0
				&& j >= 0; i++, j++) {
			if (board[i][j] > 0 && !(i == curX && j == curY) && i < boardSize
					&& j < boardSize) {
				return 1;
			}
		}

		return 0;
	}

}
