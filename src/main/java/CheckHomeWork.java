import tests.*;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CheckHomeWork {
    String lastName;
    Class currentClass;
    Method[] allMethods;
    Object newObj;

    CheckHomeWork(String lastName){
        this.lastName = lastName;
    }

    public static void main(String[] args) throws MalformedURLException, ClassNotFoundException, NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        File file = new File("HomeWorks");
        List<File> fileList = Arrays.asList(file.listFiles());
        ArrayList<CheckHomeWork> allStudentsWorks = new ArrayList<CheckHomeWork>(fileList.size());
        int count = 0;

        // инициализируем ArrayList объектов каждого класса учеников
        for (File o: fileList) {
           if (o.getName().endsWith(".class")) {
               allStudentsWorks.add(new CheckHomeWork(o.getName().split(".class")[0]));
                   allStudentsWorks.get(count).currentClass = URLClassLoader.newInstance(new URL[]{file.toURL()}).loadClass(o.getName().split(".class")[0]);
                   allStudentsWorks.get(count).newObj = allStudentsWorks.get(count).currentClass.getConstructor(String.class, int.class).newInstance("I'm " + o.getName().split(".class")[0], count + 10);
                   allStudentsWorks.get(count).allMethods = allStudentsWorks.get(count).currentClass.getDeclaredMethods();
                   allStudentsWorks.get(count).doMethod();
                   count++;
           }
        }
    }

    private void doMethod() throws IllegalAccessException, InvocationTargetException {
        for (Method o: this.allMethods) {
            System.out.println("Результат выполнения метода " + o.getName() + " ученика " + this.lastName + ":");
            if (o.getReturnType().toString().equals("void")) {
                o.invoke(this.newObj);
            } else {
                System.out.println(o.invoke(this.newObj));
            }
        }
        System.out.println();
    }
}



