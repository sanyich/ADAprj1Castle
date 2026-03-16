import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


class Main {
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

        // number of test cases
        int nTests = Integer.parseInt(in.readLine());

        for (int i = 0; i < nTests; i++) {

            // read parameters of the test case
            String line = in.readLine();
            String[] parts = line.trim().split(" ");
            int nRows = Integer.parseInt(parts[0]);
            int nColumns = Integer.parseInt(parts[1]);
            int nConsJumps = Integer.parseInt(parts[2]);
            int nJumps = Integer.parseInt(parts[3]);

            // create problem instance
            CrystalCastle game = new CrystalCastle(nRows, nColumns, nConsJumps, nJumps);

            // read the grid
            for (int j = 0; j < nRows; j++){
                String row = in.readLine();
                for (int k = 0; k < nColumns; k++) {
                    game.setCell(j, k, row.charAt(k));
                }
            }

            // compute and print the result
            System.out.println(game.solve());
        }
        in.close();
    }
}
