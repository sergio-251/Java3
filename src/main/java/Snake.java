import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Snake {
    private static int n;
    private static int m;
    private static int[][] snakeArray;

    public static void main(String[] args) {

        try {
            inputSize();
        } catch (IOException e) {
            e.printStackTrace();
        }
        SnakeGenerate();



    }

    public static void inputSize() throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите рамерность матрицы через пробел:");
        String[] size = reader.readLine().split(" ");
        n = Integer.parseInt(size[0]); // номер ряда
        m = Integer.parseInt(size[1]); // номер колонки
    }

    public static void SnakeGenerate() {
        int currentN_max = n;
        int currentM_max = m;
        int currentN_min = 1;
        int currentM_min = 1;
        snakeArray = new int[n][m];
        int k = 1;
        while (k <= m * n) {

            for (int i = currentM_min - 1; i < currentM_max; i++) {
                if (k > m * n) break;
                snakeArray[currentN_min - 1][i] = k;
                k++;
            }
            if(currentN_min < currentN_max) currentN_min++;

            for (int i = currentN_min - 1; i < currentN_max; i++) {
                if (k > m * n) break;
                snakeArray[i][currentM_max - 1] = k;
                k++;
            }
            if(currentM_max > currentM_min) currentM_max--;

            for (int i = currentM_max - 1; i >= currentM_min - 1; i--) {
                if (k > m * n) break;
                snakeArray[currentN_max - 1][i] = k;
                k++;
            }
            if (currentN_max > currentN_min) currentN_max--;

            for (int i = currentN_max - 1; i >= currentN_min - 1; i--) {
                if (k > m * n) break;
                snakeArray[i][currentM_min - 1] = k;
                k++;
            }
            if (currentM_min < currentM_max) currentM_min++;
        }


        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (snakeArray[i][j] > 9) {
                    System.out.print(snakeArray[i][j] + " ");
                } else {
                    System.out.print(snakeArray[i][j] + "  ");
                }
                k++;
            }
            System.out.println();
        }


    }
}
