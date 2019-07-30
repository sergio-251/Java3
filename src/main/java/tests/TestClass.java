package tests;

import tests.classes.Class1ForTest;
import tests.classes.Class2ForTest;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;



public class TestClass {
    public static void main(String[] args) {
        Class1ForTest class1 = new Class1ForTest(4, 7);
        start(class1);
        System.out.println();
        Class2ForTest class2 = new Class2ForTest("Michael", 25);
        start(class2);
    }

    public static void start(Object obj){
        System.out.println("Начинаем тестирование объекта класса " + obj.getClass().getSimpleName());
        Method[] allMethods = obj.getClass().getDeclaredMethods();
        Method[] allTestMethod;
        // Формируем порядок методов-тестов с аннотацией @Test в порядке возрастания приоритетов (метод пузырька)
        int k = 0;
        for (int i = 0; i < allMethods.length; i++) {
            if (!allMethods[i].isAnnotationPresent(Test.class)) continue;
            k++;
        }
        allTestMethod = new Method[k];
        k = 0;
        for (int i = 0; i < allMethods.length; i++) {
            if (!allMethods[i].isAnnotationPresent(Test.class)) continue;
            allTestMethod[k] = allMethods[i];
            k++;
        }

        int min = allTestMethod[0].getAnnotation(Test.class).priority();
        Method m;
        for (int i = 0; i < allTestMethod.length - 1; i++) {
            for (int j = 0; j < allTestMethod.length; j++) {
                if (min >= allTestMethod[j].getAnnotation(Test.class).priority()){
                    m = allTestMethod[j];
                    allTestMethod[j] = allTestMethod[i];
                    allTestMethod[i] = m;
                }
            }
        }

        //@BeforeSuite
        boolean countBefore = false;
        for (Method o: allMethods) {
            if (o.isAnnotationPresent(BeforeSuite.class)) {
                if (!countBefore) {
                    countBefore = true;
                    try {
                        o.invoke(obj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    continue;
                } else try {
                    throw new Exception("BeforeSuite Annotation more than one!!!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
            // Методы @Test
        for (Method o: allTestMethod) {
            System.out.println("Выполняем тест сравнением ожидаемой величины - класс " + obj.getClass().getSimpleName() +
                    " метод " + o.getName() + " (приоритет " + o.getAnnotation(Test.class).priority() + ")");
        }

        //@AfterSuite
        boolean countAfter = false;
        for (Method o: allMethods) {
            if (o.isAnnotationPresent(AfterSuite.class)) {
                if (!countAfter) {
                    countAfter = true;
                    try {
                        o.invoke(obj);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    }
                    continue;
                } else try {
                    throw new Exception("AfterSuite Annotation more than one!!!");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Тестирование завершено!");
    }
}
