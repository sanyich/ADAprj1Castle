import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
        
        int T = Integer.parseInt(in.readLine()); //number of test cases

        for (int i = 0; i < T; i++) {
            String line = in.readLine();
            String[] parts = line.trim().split(" ");
            int R = Integer.parseInt(parts[0]); //n of rows
            int C = Integer.parseInt(parts[1]); //n of columns
            int M = Integer.parseInt(parts[2]); //max n of connsecutive jumps
            int N = Integer.parseInt(parts[3]); //maximum jumps
            String [][] grid = new String[R][C]; //grid to store the values

            for (int j = 0; j < R; j++){
                // Read the grid values
                String row = in.readLine();                
                for (int k = 0; k < C; k++) {
                    grid[j][k] = String.valueOf(row.charAt(k));
                }
            }
            Game game = new Game(grid, R, C, M, N);
            System.out.println(game.play());
        }
        in.close();
    }
}
