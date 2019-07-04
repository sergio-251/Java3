package fruits;

public abstract class Fruit {
    private Float weight;
    private int nums;

    Fruit(int nums) {
        this.nums = nums;
    }

    public abstract Float getWeight();

    public int getNums(){
        return nums;
    }

}
