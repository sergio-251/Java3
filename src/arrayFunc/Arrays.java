package arrayFunc;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class Arrays {

   public static Object[] changeArrElements(Object[] arr, int index1, int index2){
        Object[] result = arr.clone();
        Object k = result[index2];
        result[index2] = result[index1];
        result[index1] = k;
        return result;
    }

    public static <T> T[] changeArrElementsGen(T[] arr, int index1, int index2){
        T[] result = arr.clone();
        T k = result[index2];
        result[index2] = result[index1];
        result[index1] = k;
        return result;
    }

    public static void printChangedArray(Object[] arr, int index1, int index2){
        Object[] result = changeArrElements(arr, index1, index2);
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + " ");
        }
        System.out.println();
    }

    public static <T> void printChangedArrayGen(T[] arr, int index1, int index2){
        T[] result = changeArrElementsGen(arr, index1, index2);
        for (int i = 0; i < result.length; i++) {
            System.out.print(result[i] + " ");
        }
        System.out.println();
    }



    public static ArrayList getArrayListFromArray(Object[] array){
        ArrayList result = new ArrayList(array.length);
        for (int i = 0; i < array.length; i++) {
            result.add(array[i]);
        }
        return result;
    }

    public static void getInfoAL(ArrayList arrayList){
        System.out.println("Класс объекта " + arrayList.getClass().getSimpleName() + " - это " + arrayList.getClass().toString());
        if(arrayList.size() > 0) {
            System.out.println("Объект содержит следующие элементы:");
            for (int i = 0; i < arrayList.size(); i++) {
                System.out.print(arrayList.get(i)+ " ");

            }
        } else {
            System.out.print("Объект не содержит элементов");
        }
        System.out.println();
    }
}


