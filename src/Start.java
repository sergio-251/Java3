import arrayFunc.Arrays;
import box.Box;
import com.sun.org.apache.xerces.internal.util.SynchronizedSymbolTable;
import fruits.Apple;
import fruits.Fruit;
import fruits.Orange;

import java.util.ArrayList;

public class Start {
    public static void main(String[] args) {

        // Задание 1
        String[] arrayStr = {"-1", "2", "three", "4"};
        Integer[] arrayInt = {2, -9, 4, 1};

        // 1 способ - через создание объектов Object[]
        // Этот статический метод возвращает измененный массив (для вывода создал отдельный метод (см. ниже))
        Object[] arrayStrChanged = Arrays.changeArrElements(arrayStr, 1,3);
        Object[] arrayIntChanged = Arrays.changeArrElements(arrayStr, 0,2);

        // Этот статический метод выводит в консоль элементы измененного массива
        Arrays.printChangedArray(arrayStr, 1,3);
        Arrays.printChangedArray(arrayInt, 0,2);

        // 2 способ - через обощенный статический метод
        String[] arrayStrChangedGen = Arrays.changeArrElementsGen(arrayStr, 1, 3);
        Integer[] arrayIntChangedGen = Arrays.changeArrElementsGen(arrayInt, 0, 2);
        Arrays.printChangedArrayGen(arrayStr, 1, 3);
        Arrays.printChangedArrayGen(arrayInt, 0, 2);


        // Задание 2 (используем массивы, созданные в рамках Задания № 1)

        // Через статический метод получаем объект класса ArrayList, при этом тип ArrayList'а формируется автоматически
        ArrayList alStr = Arrays.getArrayListFromArray(arrayStr);
        ArrayList alInt = Arrays.getArrayListFromArray(arrayInt);

        //Вывод в консоль информации об полученных ArrayList'ах
        Arrays.getInfoAL(alStr);
        System.out.println();
        Arrays.getInfoAL(alInt);

        // 3 задание
        // Создаем коробки с яблоками и апельсинами
        Box<Apple> appleBox1 = new Box<>(new Apple(5)); // Изначально кладем в эту коробку 5 яблок
        Box<Orange> orangeBox1 = new Box<>(new Orange(3)); // Изначально кладем в эту коробку 3 апельсина
        System.out.println("Масса коробки равна " + appleBox1.getWeight()); // Текущий вес коробки с яблоками
        appleBox1.addFruit(new Apple(7));
        System.out.println("Масса коробки после добавления равна " + appleBox1.getWeight()); // Текущий вес коробки с яблоками
        System.out.println("Массы коробок равны? " + appleBox1.compare(orangeBox1));
        orangeBox1.addFruit(new Orange(5)); // Выравниваем массы коробок
        System.out.println("Массы коробок равны? " + appleBox1.compare(orangeBox1));

        Box<Apple> appleBox2 = new Box<>(new Apple(9)); // Создаем еще коробку с яблоками
        appleBox1.moveToBox(appleBox2); // Пересыпаем яблоки
        System.out.println("Масса коробки 1 после пересыпания равна " + appleBox1.getWeight()); // Текущий вес коробки 1
        System.out.println("Масса коробки 2 после пересыпания равна " + appleBox2.getWeight()); // Текущий вес коробки 2

        appleBox2.moveToBox(orangeBox1); // пытаемся пересыпать яблоки в апельсины (будет сообщение о невозможности пересыпания)


    }



}
