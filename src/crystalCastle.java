public class crystalCastle {

    private static final long MOD = 1_000_000_000L + 7; //output it modulo 10^9 + 7
    private final char[][] gameGrid;
    private final int nRows;
    private final int nColumns;
    private final int nConsJumps;
    private final int nJumps;

    // memo[row][col][consec][total]


    public crystalCastle(char[][] gameGrid, int nRows, int nColumns, int nConsJumps, int nJumps) {
        this.gameGrid = gameGrid;
        this.nRows = nRows;
        this.nColumns = nColumns;
        this.nConsJumps = nConsJumps;
        this.nJumps = nJumps;
    }

    public long solve() {
        long[][][][] nWays = new long[nRows][nColumns][nConsJumps + 1][nJumps + 1];

        for (int x = nRows - 1; x >= 0; x--) {
            for (int y = nColumns - 1; y >= 0; y--) {
                if (isBlocked(x, y)) continue;
                for (int k = nConsJumps; k >= 0; k--) {
                    for (int l = nJumps; l >= 0; l--) {
                        if (isDestination(x, y)) {
                            nWays[x][y][k][l] = 1;
                            continue;
                        }

                        long ways = 0;

                        // Move right (R)
                        if (insideGrid(x, y + 1) && !isBlocked(x, y + 1)) {
                            ways = (ways + nWays[x][y + 1][0][l]) % MOD;
                        }

                        // Move down (D)
                        if (insideGrid(x + 1, y) && !isBlocked(x + 1, y)) {
                            ways = (ways + nWays[x + 1][y][0][l]) % MOD;
                        }

                        // Jumps
                        if (k < nConsJumps && l < nJumps && canJumpFrom(x, y)) {
                            // double down (DD)
                            if (insideGrid(x + 2, y) && !isBlocked(x + 2, y)) {
                                ways = (ways + nWays[x + 2][y][k + 1][l + 1]) % MOD;
                            }

                            if (canDiagonallyJumpFrom(x, y)) {
                                // right down (RD)
                                if (insideGrid(x + 1, y + 1) && !isBlocked(x + 1, y + 1)) {
                                    ways = (ways + nWays[x + 1][y + 1][k + 1][l + 1]) % MOD;
                                }

                                // left down (LD)
                                if (insideGrid(x + 1, y - 1) && !isBlocked(x + 1, y - 1)) {
                                    ways = (ways + nWays[x + 1][y - 1][k + 1][l + 1]) % MOD;
                                }
                            }
                        }

                        nWays[x][y][k][l] = ways;

                    }
                }
            }
        }
        return nWays[0][0][0][0];

    }

    private boolean insideGrid(int x, int y) {
        return x >= 0 && x < nRows && y >= 0 && y < nColumns;
    }

    private boolean isBlocked(int x, int y) {
        return gameGrid[x][y] == '#';
    }

    private boolean isDestination(int x, int y) {
        return x == nRows - 1 && y == nColumns - 1;
    }

    private boolean canJumpFrom(int x, int y) {
        return gameGrid[x][y] != 'J';
    }

    private boolean canDiagonallyJumpFrom(int x, int y) {
        return gameGrid[x][y] != 'X' && gameGrid[x][y] != 'J';
    }
}