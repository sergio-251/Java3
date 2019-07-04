package fruits;

public class Apple extends Fruit{
    public Float weight = 1.0f;
    private int nums;
    private Class currentClass = this.getClass();

    public Apple(int nums) {
        super(nums);
    }

    @Override
    public Float getWeight() {
        return weight;
    }




}
