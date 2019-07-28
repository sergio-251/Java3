import java.util.ArrayList;
import java.util.Collections;

public class MainArray {
    public static void main(String[] args) {
        MainArray ma = new MainArray();
        // Задание № 2
        Integer[] inArray = {1, 3, 5, 6, 4, 7, 1};
        Integer[] outArray = new Integer[0];
        try {
            outArray = ma.changeArray(inArray);
            for (int i = 0; i < outArray.length; i++) {
                System.out.print(outArray[i] + " ");
            }
        } catch (MyException e) {
            e.printStackTrace();
        }


        // Задание № 3
        System.out.println();
        System.out.println(ma.isOneAndFourDigit(inArray));

    }

    public Integer[] changeArray(Integer[] inArr) throws MyException {
        int pos = 0;
        for (int i = 0; i < inArr.length; i++) {
            if(inArr[i] == 4) {
                pos = i;
            }
        }
        if (pos == 0 || pos == (inArr.length - 1)){
            throw new MyException("В массиве нет числа 4 или оно стоит на последнем месте!");
        }
        Integer[] outArr = new Integer[inArr.length - pos - 1];
        System.arraycopy(inArr, pos + 1, outArr, 0, inArr.length - pos - 1);
        return outArr;
    }

    public boolean isOneAndFourDigit(Integer[] inArr){
        boolean isOneOrFour = true;
        for (int i = 0; i < inArr.length; i++) {
            if (inArr[i] != 1 && inArr[i] != 4) isOneOrFour = false;
        }
        if(isOneOrFour){
            return true;
        } else {
            return false;
        }
    }
    
}
