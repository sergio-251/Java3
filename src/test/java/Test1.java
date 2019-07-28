import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class Test1 {

    MainArray mainArray;

    @Before
    public void start(){
        mainArray = new MainArray();
    }

    @Test
    public void test1() throws Exception {
        Assert.assertArrayEquals(new Integer[]{5,7}, mainArray.changeArray(new Integer[]{1, 3, 5, 6, 4, 5, 7}));
    }

    @Test
    public void test2() throws Exception {
        Assert.assertArrayEquals(new Integer[]{5,8}, mainArray.changeArray(new Integer[]{1, 3, 5, 6, 4, 5, 7}));
    }

    @Test(expected = MyException.class)
    public void test3() throws Exception {
        Assert.assertArrayEquals(new Integer[]{5,7}, mainArray.changeArray(new Integer[]{1, 3, 5, 6, 1, 5, 7}));
    }

    @Test
    public void test4() throws Exception {
        Assert.assertArrayEquals(new Integer[]{5,7,0,1}, mainArray.changeArray(new Integer[]{1, 4, 5, 6, 4, 5, 7, 0 ,1}));
    }

    @After
    public void end(){}
}
