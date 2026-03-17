import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Entry point of the program.
 *
 * Reads multiple test cases, builds the corresponding CrystalCastle
 * instances and prints the number of valid paths for each case.
 *
 * Input format:
 * - First line: number of test cases
 * - For each test case:
 *   - integers R, C, M, N
 *   - R lines describing the grid
 *
 * Output:
 * - For each test case, prints the number of valid paths modulo 1e9+7
 *
 * @author Ilia Taitsel 67258
 * @author Oleksandra Kozlova 68739
 */
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
