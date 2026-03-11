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
            int nRows = Integer.parseInt(parts[0]); //n of rows
            int nColumns = Integer.parseInt(parts[1]); //n of columns
            int nConsJumps = Integer.parseInt(parts[2]); //max n of connsecutive jumps
            int nJumps = Integer.parseInt(parts[3]); //maximum jumps
            char [][] gameGrid = new char[nRows][nColumns]; //grid to store the values

            for (int j = 0; j < nRows; j++){
                // Read the grid values
                String row = in.readLine();                
                for (int k = 0; k < nColumns; k++) {
                    gameGrid[j][k] = row.charAt(k);
                }
            }
            crystalCastle game = new crystalCastle(gameGrid, nRows, nColumns, nConsJumps, nJumps);
            System.out.println(game.solve());
        }
        in.close();
    }
}
