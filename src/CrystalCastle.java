/**
 * Represents a single instance of the Crystal Castle problem.
 *
 * The goal is to compute the number of valid paths from the top-left
 * tile to the bottom-right tile of a grid, under constraints on jumps.
 *
 * The solution uses dynamic programming where each state is defined as:
 * (row, col, consec, total), representing the number of ways to reach
 * the destination from position (row, col), given that:
 * - consec: number of consecutive jumps already used
 * - total: total number of jumps already used
 *
 * To improve efficiency:
 * - A padded grid is used to simplify boundary checks
 * - Only three rows are stored (sliding array)
 *
 * Time complexity: O(R * C * M * N)
 * Space complexity: O(C * M * N)
 *
 * @author Ilia Taitsel 67258
 * @author Oleksandra Kozlova 68739
 */
public class CrystalCastle {

    private static final long MOD = 1_000_000_007L; //output it modulo 10^9 + 7
    private final char[][] gameGrid;
    private final int nRows;
    private final int nColumns;
    private final int nConsJumps;
    private final int nJumps;
    // constants for tiles
    private static final char BLOCKED_TILE = '#';
    private static final char JUMP_FORBIDDEN_TILE = 'J';
    private static final char DIAGONAL_JUMP_FORBIDDEN_TILE = 'X';

    public CrystalCastle(int nRows, int nColumns, int nConsJumps, int nJumps) {
        this.nRows = nRows;
        this.nColumns = nColumns;
        this.nConsJumps = nConsJumps;
        this.nJumps = nJumps;
        this.gameGrid = new char[nRows + 3][nColumns + 2];

        // fill borders with blocked cells
        for (int r = 0; r < nRows + 3; r++) {
            for (int c = 0; c < nColumns + 2; c++) {
                gameGrid[r][c] = BLOCKED_TILE;
            }
        }
    }


    /**
     * Sets the value of the tile with given coordinates
     * @param j number of the row of the grid
     * @param k number of the column
     * @param value the value that should be obtained by the tile
     */
    public void setCell(int j, int k, char value) {
        gameGrid[j+1][k+1] = value;
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
     * @return number of valid paths modulo {@value #MOD}
     */
    public long solve() {

        // buffers for three rows
        long[][][] nWaysCurrentRow = new long[nColumns+2][nConsJumps + 1][nJumps + 1];
        long[][][] nWaysNextRow = new long[nColumns+2][nConsJumps + 1][nJumps + 1];
        long[][][] nWaysAfterNextRow = new long[nColumns+2][nConsJumps + 1][nJumps + 1];

        // rows processed bottom -> top
        for (int x = nRows; x >= 1; x--) {

            // reuse buffer for the new current row
            for (int y = 0; y < nColumns+2; y++) {
                for (int k = 0; k <= nConsJumps; k++) {
                    for (int l = 0; l <= nJumps; l++) {
                        nWaysCurrentRow[y][k][l] = 0;
                    }
                }
            }

            // columns processed right -> left
            for (int y = nColumns; y >= 1; y--) {
                if (isBlocked(x, y)) continue;

                if (isDestination(x, y)) {
                    for (int k = 0; k <= nConsJumps; k++) {
                        for (int l = 0; l <= nJumps; l++) {
                            nWaysCurrentRow[y][k][l] = 1;
                        }
                    }
                    continue;
                }
                boolean canMoveRight = !isBlocked(x, y + 1);
                boolean canMoveDown = !isBlocked(x + 1, y);

                boolean canJump = canJumpFrom(x, y);
                boolean canDiagJump = canDiagonallyJumpFrom(x, y);

                boolean canJumpDoubleDown = !isBlocked(x + 2, y);
                boolean canJumpRightDown = !isBlocked(x + 1, y + 1);
                boolean canJumpLeftDown = !isBlocked(x + 1, y - 1);
                
                
                for (int k = 0; k <= nConsJumps; k++) {
                    for (int l = 0; l <= nJumps; l++) {
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
        // start position (1,1) with no jumps used
        return nWaysNextRow[1][0][0];

    }

    /**
     * Checks if a tile can't be stepped on.
     *
     * @param x number of the row
     * @param y number of the column
     * @return true if tile (x,y) has quicksand (#), false otherwise
     */
    private boolean isBlocked(int x, int y) {
        return gameGrid[x][y] == BLOCKED_TILE;
    }

    /**
     * Checks if the tile given is a destination
     * @param x number of the row
     * @param y number of the column
     * @return true if tile (x,y) is a bottom-right tile, false otherwise
     */
    private boolean isDestination(int x, int y) {
        return x == nRows && y == nColumns;
    }

    /**
     * Checks if any jump can start from the given tile.
     *
     * @param x number of the row
     * @param y number of the column
     * @return true if tile (x,y) does not forbid jumps
     */
    private boolean canJumpFrom(int x, int y) {
        return gameGrid[x][y] != JUMP_FORBIDDEN_TILE;
    }

    /**
     * Checks if diagonal jumps are allowed from the tile.
     *
     * @param x number of the row
     * @param y number of the column
     * @return true if tile (x,y) allows diagonal jumps
     */
    private boolean canDiagonallyJumpFrom(int x, int y) {
        return gameGrid[x][y] != DIAGONAL_JUMP_FORBIDDEN_TILE && gameGrid[x][y] != JUMP_FORBIDDEN_TILE;
    }

}