package box;

import fruits.Fruit;

import java.util.ArrayList;

public class Box<T extends Fruit> {
    private ArrayList<T> allFruits = new ArrayList<>();
    private Class classOfBox;

    public Box(T fruit) {
        this.allFruits.add(fruit);
        this.classOfBox = fruit.getClass();

    }

    public Float getWeight(){
        Float resultWeight = 0f;
        for (int i = 0; i < allFruits.size(); i++) {
            resultWeight += allFruits.get(i).getWeight() * allFruits.get(i).getNums();
        }
        return resultWeight;
    }

    public void addFruit(T fruit){
        this.allFruits.add(fruit);
    }

    public boolean compare(Box box){
       return (this.getWeight().equals(box.getWeight())) ? true : false;
    }

    public void moveToBox(Box box){
        if(this.getClassOfBox().equals(box.getClassOfBox())){
           box.allFruits.addAll(this.allFruits);
           this.allFruits.clear();
           System.out.println("Фрукты удачно пересыпаны!");
        } else
            System.out.println("Коробки содержат разные фрукты. Пересыпать нельзя!");
    }

    public Class getClassOfBox(){
        return classOfBox;
    }


}

