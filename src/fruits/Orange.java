package fruits;

public class Orange extends Fruit{
    private Float weight = 1.5f;

    public Orange(int nums) {
        super(nums);
    }

    @Override
    public Float getWeight() {
        return weight;
    }


}
