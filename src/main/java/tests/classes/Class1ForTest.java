package tests.classes;

import tests.BeforeSuite;
import tests.Test;

public class Class1ForTest {
    int num1;
    int num2;

    public Class1ForTest(int a, int b){
        this.num1 = a;
        this.num2 = b;
    }

    @BeforeSuite
    public void info(){
        System.out.println("Параметры класса a = " + this.num1 + " и b = " + this.num2);
    }

    @BeforeSuite
    public void info2(){
        System.out.println("Параметры класса a = " + this.num1); // Будем Exception, т.к. второй метод @BeforeSuite
    }

    @Test (priority = 5)
    public int summ(){
        return this.num1 + this.num2;
    }

    @Test (priority = 1)
    public int multi(){
        return this.num1 * this.num2;
    }
}
