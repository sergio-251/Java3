package tests.classes;

import tests.AfterSuite;
import tests.BeforeSuite;
import tests.Test;

public class Class2ForTest {
    String firstName;
    int age;

    public Class2ForTest(String a, int b){
        this.firstName = a;
        this.age = b;
    }

    @AfterSuite
    public void info(){
        System.out.println("Параметры класса имя: " + this.firstName + " и возраст " + this.age + " лет");
    }

    @Test (priority = 1)
    public String nextYears(int age){
        return "Через " + age + " лет будет " + (this.age + age) + " лет" ;
    }

    @Test
    public String getFirstName(){
        return this.firstName;
    }


}
