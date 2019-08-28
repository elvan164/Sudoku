public class SudokuGenerator {

	public static int max = 9, min = 1, row, col, random, randCount;
	public static int[][] puzzle = new int[max][max];

	public static int[][] Generate() {

		for (row = 0; row < max; row++) {
			for (col = 0; col < max; col++) {
				puzzle[row][col] = 0;
			}
		}

		for (row = 0; row < max; row++) {
			for (col = 0; col < max; col++) {
				random = (int) (Math.random() * (max - min + 1) + min);
				for (randCount = 0; randCount < 25;) {
					while (checkRow(random) || checkCol(random) || checkSq(random)) {
						randCount++;
						random = (int) (Math.random() * (max - min + 1) + min);
						if (randCount == 25) {
							for (row = 0; row < max; row++) {
								for (col = 0; col < max; col++) {
									puzzle[row][col] = 0;
								}
							}
							row = 0;
							col = 0;
							break;
						}
					}
					break;
				}
				puzzle[row][col] = random;
			}
		}
		return puzzle;
	}

	public static boolean checkRow(int random) {
		int rowCount = row, colCount;

		for (colCount = 0; colCount < max; colCount++) {
			if (puzzle[rowCount][colCount] == random) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkCol(int random) {
		int rowCount, colCount = col;

		for (rowCount = 0; rowCount < max; rowCount++) {
			if (puzzle[rowCount][colCount] == random) {
				return true;
			}
		}
		return false;
	}

	public static boolean checkSq(int random) {
		int rowCount, colCount;

		for (rowCount = (row / 3) * 3; rowCount <= ((row / 3) * 3) + 2; rowCount++) {
			for (colCount = (col / 3) * 3; colCount <= ((col / 3) * 3) + 2; colCount++) {
				if (puzzle[rowCount][colCount] == random) {
					return true;
				}
			}
		}
		return false;
	}
}
