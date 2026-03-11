public class Game {

    private static final long MOD = 1_000_000_000L + 7; //output it modulo 10^9 + 7
    private char[][] grid;
    private int R;
    private int C;
    private int M;
    private int N;

    // memo[row][col][consec][total]
    private final long[][][][] memo;
    private final boolean[][][][] seen;

    public Game(char[][] grid, int R, int C, int M, int N) {
        this.grid = grid;
        this.R = R;
        this.C = C;
        this.M = M;
        this.N = N;

        this.memo = new long[R][C][M + 1][N + 1];
        this.seen = new boolean[R][C][M + 1][N + 1];
    }

    public long play() {
        return countPaths(0, 0, 0, 0);
    }
    // note: x is the row index and y is the column index
     private long countPaths(int x, int y, int consecJumps, int totalJumps) {
        
        if (!insideGrid(x, y) || blocked(x, y)) {
            return 0; // Out of bounds, blocked
        }
        if ((totalJumps > N) || (consecJumps > M)) {
            return 0; // Exceeded jumps
        }
        if (isDestination(x, y)) {
            return 1; // Found a valid path
        }
        if (seen[x][y][consecJumps][totalJumps]) {
            return memo[x][y][consecJumps][totalJumps];
        }
        long paths = 0;
        // Move right
        paths = (paths + countPaths(x + 1, y, 0, totalJumps)) % MOD; // Move right resets consecutive jumps
        // Move down
        paths = (paths + countPaths(x, y+1, 0, totalJumps)) % MOD; // Move down resets consecutive jumps
        
        // Jumps
        if (totalJumps < N && consecJumps < M && canJumpFrom(x, y)) {
            // Down jump
            paths = (paths + countPaths(x + 2, y, consecJumps + 1, totalJumps + 1)) % MOD;
            // Diagonal jump
            if (canDiagonallyJumpFrom(x, y)) {
                // Diagonal jump right
                paths = (paths + countPaths(x + 1, y + 1, consecJumps + 1, totalJumps + 1)) % MOD;
                // Diagonal jump left
                paths = (paths + countPaths(x + 1, y - 1, consecJumps + 1, totalJumps + 1)) % MOD;
            }
        }
        seen[x][y][consecJumps][totalJumps] = true;
        memo[x][y][consecJumps][totalJumps] = paths;
        return paths;
    }

    private boolean insideGrid(int x, int y) {
        return x >= 0 && x < R && y >= 0 && y < C;
    }

    private boolean blocked(int x, int y) {
        return grid[x][y] == '#';
    }

    private boolean isDestination(int x, int y) {
        return x == R - 1 && y == C - 1;
    }

    private boolean canJumpFrom(int x, int y) {
        return grid[x][y] != 'J';
    }

    private boolean canDiagonallyJumpFrom(int x, int y) {
        return grid[x][y] =='.';
    }
}