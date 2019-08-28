public class test {

	public static int max = 9, min = 1, i, j, random, magik;
	public static int[][] puzzle = new int[max][max];

	public static int[][] Generate() {

		for (i = 0; i < max; i++) {
			for (j = 0; j < max; j++) {
				puzzle[i][j] = 0;
			}
		}

		for (i = 0; i < max; i++) {
			for (j = 0; j < max; j++) {
				random = (int) (Math.random() * (max - min + 1) + min);

				for (magik = 0; magik < 25;) {

					while (checkRow(random) || checkCol(random) || checkSq(random)) {
						magik++;
						random = (int) (Math.random() * (max - min + 1) + min);

						if (magik == 25) {
							for (i = 0; i < max; i++) {
								for (j = 0; j < max; j++) {
									puzzle[i][j] = 0;
								}
							}
							i = 0;
							j = 0;
							break;
						}
					}
					break;
				}
				puzzle[i][j] = random;
			}
		}
		return puzzle;
	}

	public static boolean checkRow(int random) {
		int rowCount = i, colCount;

		for (colCount = 0; colCount < max; colCount++) {
			if (puzzle[rowCount][colCount] == random) {
				// System.out.println("checkRow");
				return true;
			}
		}
		return false;
	}

	public static boolean checkCol(int random) {
		int rowCount, colCount = j;

		for (rowCount = 0; rowCount < max; rowCount++) {
			if (puzzle[rowCount][colCount] == random) {
				// System.out.println("checkCol");
				return true;
			}
		}
		return false;

	}

	public static boolean checkSq(int random) {
		int rowCount, colCount;
		// System.out.println("checkSq");
		
		for (rowCount = (i/3) * 3; rowCount <= ((i/3) * 3) + 2 ; rowCount++) {
			for (colCount = (j/3) * 3; colCount <= ((j/3) * 3) + 2; colCount++) {
			if (puzzle[rowCount][colCount] == random) {
				return true;
			}
			}
		}
		
		/*if ((i >= 0 && i <= 2) && (j >= 0 && j <= 2)) {
			for (rowCount = 0; rowCount <= 2; rowCount++) {
				for (colCount = 0; colCount <= 2; colCount++) {
					if (puzzle[rowCount][colCount] == random) {
						return true;
					}
				}
			}
		} else if ((i >= 0 && i <= 2) && (j >= 3 && j <= 5)) {
			for (rowCount = 0; rowCount <= 2; rowCount++) {
				for (colCount = 3; colCount <= 5; colCount++) {
					if (puzzle[rowCount][colCount] == random) {
						return true;
					}
				}
			}
		} else if ((i >= 0 && i <= 2) && (j >= 6 && j <= 8)) {
			for (rowCount = 0; rowCount <= 2; rowCount++) {
				for (colCount = 6; colCount <= 8; colCount++) {
					if (puzzle[rowCount][colCount] == random) {
						return true;
					}
				}
			}
		} else if ((i >= 3 && i <= 5) && (j >= 0 && j <= 2)) {
			for (rowCount = 3; rowCount <= 5; rowCount++) {
				for (colCount = 0; colCount <= 2; colCount++) {
					if (puzzle[rowCount][colCount] == random) {
						return true;
					}
				}
			}
		} else if ((i >= 3 && i <= 5) && (j >= 3 && j <= 5)) {
			for (rowCount = 3; rowCount <= 5; rowCount++) {
				for (colCount = 3; colCount <= 5; colCount++) {
					if (puzzle[rowCount][colCount] == random) {
						return true;
					}
				}
			}
		} else if ((i >= 3 && i <= 5) && (j >= 6 && j <= 8)) {
			for (rowCount = 3; rowCount <= 5; rowCount++) {
				for (colCount = 6; colCount <= 8; colCount++) {
					if (puzzle[rowCount][colCount] == random) {
						return true;
					}
				}
			}
		} else if ((i >= 6 && i <= 8) && (j >= 0 && j <= 2)) {
			for (rowCount = 6; rowCount <= 8; rowCount++) {
				for (colCount = 0; colCount <= 2; colCount++) {
					if (puzzle[rowCount][colCount] == random) {
						return true;
					}
				}
			}
		} else if ((i >= 6 && i <= 8) && (j >= 3 && j <= 5)) {
			for (rowCount = 6; rowCount <= 8; rowCount++) {
				for (colCount = 3; colCount <= 5; colCount++) {
					if (puzzle[rowCount][colCount] == random) {
						return true;
					}
				}
			}
		} else if ((i >= 6 && i <= 8) && (j >= 6 && j <= 8)) {
			for (rowCount = 6; rowCount <= 8; rowCount++) {
				for (colCount = 6; colCount <= 8; colCount++) {
					if (puzzle[rowCount][colCount] == random) {
						return true;
					}
				}
			}
		}*/
		return false;

	}
}
