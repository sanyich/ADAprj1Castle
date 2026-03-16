public class CrystalCastle {

    private static final long MOD = 1_000_000_000L + 7; //output it modulo 10^9 + 7
    private final char[][] gameGrid;
    private final int nRows;
    private final int nColumns;
    private final int nConsJumps;
    private final int nJumps;
    //constants for tiles
    //find a way to do less checking to save time. ex, create a row/collum outside of the grid so we don't overtep it?

    public CrystalCastle(char[][] gameGrid, int nRows, int nColumns, int nConsJumps, int nJumps) {
        this.gameGrid = gameGrid;
        this.nRows = nRows;
        this.nColumns = nColumns;
        this.nConsJumps = nConsJumps;
        this.nJumps = nJumps;
    }


    /**
     * Solves the problem of the given test case
     *
     * Each state is a quadruple (R,C,CJ,J) which represents
     * the number of possible ways to reach destination from (R,C), given
     * that number of consecutive jumps CJ and total jumps J are used.
     *
     * Space complexity optimization:
     * Only three rows are stored (current, next, after next)
     * Since moves only depend on three rows
     * @return number of possible ways to reach bottom-right tile {@value #MOD}
     */
    public long solve() {

        // buffers for three rows
        long[][][] nWaysCurrentRow = new long[nColumns][nConsJumps + 1][nJumps + 1];
        long[][][] nWaysNextRow = new long[nColumns][nConsJumps + 1][nJumps + 1];
        long[][][] nWaysAfterNextRow = new long[nColumns][nConsJumps + 1][nJumps + 1];

        // rows processed bottom -> top
        for (int x = nRows - 1; x >= 0; x--) {

            // reuse buffer for the new current row
            for (int y = 0; y < nColumns; y++) {
                for (int k = 0; k <= nConsJumps; k++) {
                    for (int l = 0; l <= nJumps; l++) {
                        nWaysCurrentRow[y][k][l] = 0;
                    }
                }
            }

            // columns processed right -> left
            for (int y = nColumns - 1; y >= 0; y--) {
                boolean isBlocked = isBlocked(x, y);
                boolean canMoveRight = insideGrid(x, y + 1) && !isBlocked(x, y + 1);
                boolean canMoveDown = insideGrid(x + 1, y) && !isBlocked(x + 1, y);
                boolean canJump = canJumpFrom(x, y);
                boolean canDiagJump = canDiagonallyJumpFrom(x, y);
                boolean canJumpDoubleDown = insideGrid(x + 2, y) && !isBlocked(x + 2, y);
                boolean canJumpRightDown = insideGrid(x + 1, y + 1) && !isBlocked(x + 1, y + 1);
                boolean canJumpLeftDown = insideGrid(x + 1, y - 1) && !isBlocked(x + 1, y - 1);
                if (isBlocked) continue;
                if (isDestination(x, y)) {
                    for (int k = nConsJumps; k >= 0; k--) {
                        for (int l = nJumps; l >= 0; l--) {
                            nWaysCurrentRow[y][k][l] = 1;
                        }
                    }
                    continue;
                }
                for (int k = nConsJumps; k >= 0; k--) {
                    for (int l = nJumps; l >= 0; l--) {
                        long ways = 0;

                        // normal moves reset consecutive jumps
                        // move right (R)
                        if (canMoveRight) {
                            ways = (ways + nWaysCurrentRow[y + 1][0][l]) % MOD;
                        }

                        if (canMoveDown) {
                            // move down (D)
                            ways = (ways + nWaysNextRow[y][0][l]) % MOD;
                        }

                        // jump moves increase both counters
                        if (k < nConsJumps && l < nJumps && canJump) {
                            // double down (DD)
                            if (canJumpDoubleDown) {
                                ways = (ways + nWaysAfterNextRow[y][k + 1][l + 1]) % MOD;
                            }

                            if (canDiagJump) {
                                // right down (RD)
                                if (canJumpRightDown) {
                                    ways = (ways + nWaysNextRow[y + 1][k + 1][l + 1]) % MOD;
                                }

                                // left down (LD)
                                if (canJumpLeftDown) {
                                    ways = (ways + nWaysNextRow[y - 1][k + 1][l + 1]) % MOD;
                                }
                            }
                        }

                        nWaysCurrentRow[y][k][l] = ways;

                    }
                }
            }
            // shift the sliding rows
            long[][][] temp = nWaysAfterNextRow;
            nWaysAfterNextRow = nWaysNextRow;
            nWaysNextRow = nWaysCurrentRow;
            nWaysCurrentRow = temp;
        }
        // start position (0,0) with no jumps used
        return nWaysNextRow[0][0][0];

    }

    /**
     * Checks if the position given belongs to the grid
     * @param x number of the row
     * @param y number of the column
     * @return true if tile (x,y) is in the grid, false otherwise
     */
    private boolean insideGrid(int x, int y) {
        return x >= 0 && x < nRows && y >= 0 && y < nColumns;
    }

    /**
     * Checks if a tile can't be stepped on.
     *
     * @param x number of the row
     * @param y number of the column
     * @return true if tile (x,y) has quicksand (#), false otherwise
     */
    private boolean isBlocked(int x, int y) {
        return gameGrid[x][y] == '#';
    }

    /**
     * Checks if the tile given is a destination
     * @param x number of the row
     * @param y number of the column
     * @return true if tile (x,y) is a bottom-right tile, false otherwise
     */
    private boolean isDestination(int x, int y) {
        return x == nRows - 1 && y == nColumns - 1;
    }

    /**
     * Checks if any jump can start from the given tile.
     *
     * @param x number of the row
     * @param y number of the column
     * @return true if tile (x,y) does not forbid jumps
     */
    private boolean canJumpFrom(int x, int y) {
        return gameGrid[x][y] != 'J';
    }

    /**
     * Checks if diagonal jumps are allowed from the tile.
     *
     * @param x number of the row
     * @param y number of the column
     * @return true if tile (x,y) allows diagonal jumps
     */
    private boolean canDiagonallyJumpFrom(int x, int y) {
        return gameGrid[x][y] != 'X' && gameGrid[x][y] != 'J';
    }
}

//include reading time in report to calculate complexity of reading
// also check O or Θ for each operation