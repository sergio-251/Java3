import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainClass {

    public static void main(String[] args) {

        // Задание № 1 (без учета кодировки - просто вывод байтового массива в консоль)
        System.out.println("Задание № 1");
        FileClass.readAndPrintFile("textSource/Source1.txt");

        // Задание № 2
        System.out.println();
        System.out.println();
        System.out.println("Задание № 2");
        String[] files = {"textSource/files/file1.txt",
                "textSource/files/file2.txt",
                "textSource/files/file3.txt",
                "textSource/files/file4.txt",
                "textSource/files/file5.txt"};

        try {
            FileClass.oneBigFile(files);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Задание № 3
        System.out.println("Задание № 3");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            long numList = Long.parseLong(reader.readLine());
            FileClass.printList("textSource/Source2.txt", numList);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
