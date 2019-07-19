import java.io.*;
import java.util.ArrayList;
import java.util.Collections;

public class FileClass {
    static long numLists;
    static final int countCharInList = 1800;

    public static void readAndPrintFile(String nameFile){
        try(FileInputStream in = new FileInputStream(nameFile)) {
            byte[] charArr = new byte[128];
            int k;
            while((k = in.read(charArr)) > 0){
                System.out.print(new String(charArr,0,k));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void oneBigFile(String[] filesName) throws IOException {
        if (filesName.length == 0) return;
        ArrayList<InputStream> arFiles = new ArrayList<>();
        for (int i = 0; i < filesName.length; i++) {
            arFiles.add(new FileInputStream(filesName[i]));
        }
        SequenceInputStream oneData = new SequenceInputStream(Collections.enumeration(arFiles));
        int k;
        File allFilesData = new File("textSource/files/allFiles.txt");
        if (!allFilesData.exists()) {
            allFilesData.createNewFile();
            System.out.println("Файл allFiles.txt успешно создан!");
        } else {
            System.out.println("Файл allFiles.txt успешно обновлен!");
        }
        BufferedReader br = new BufferedReader(new FileReader(allFilesData));
        FileWriter writeData = new FileWriter(allFilesData);
        String s = "";
        while ((k = oneData.read()) != -1){
           s += String.valueOf((char)k); // ФОрмируем данные для запись в файл
        }
        writeData.write(s);
        writeData.flush();
        oneData.close();
    }

    public static void printList(String fileName, long num) throws IOException {
        InputStreamReader isr = new InputStreamReader(new FileInputStream(fileName),"Utf-8");
        int k, count=0;
        while ((k = isr.read()) != -1){
            count++;
        }
        numLists = count;
        if (numLists / num  <=0) System.out.println("Такой страницы нет в тексте!");
        else {
            RandomAccessFile raf = new RandomAccessFile(fileName, "r");
            raf.seek((num - 1) * countCharInList);
            count = 0;
            while((k = raf.read()) != -1 && count <= countCharInList){
                System.out.print((char) k);
                count++;
            }
            System.out.println();
            System.out.println();
            System.out.println("Была выведена страница " + num);
        }
    }
}
