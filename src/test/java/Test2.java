import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;



public class Test2 {
    MainArray mainArray;

    @Before
    public void start(){
        mainArray = new MainArray();
    }

    @Test
    public void test1() throws Exception {
        Assert.assertArrayEquals(new boolean[]{false}, new boolean[]{mainArray.isOneAndFourDigit(new Integer[]{1,4,4,3})});
    }

    @Test
    public void test2() throws Exception {
        Assert.assertArrayEquals(new boolean[]{true}, new boolean[]{mainArray.isOneAndFourDigit(new Integer[]{1,4,4,1,1})});
    }

    @Test
    public void test3() throws Exception {
        Assert.assertArrayEquals(new boolean[]{true}, new boolean[]{mainArray.isOneAndFourDigit(new Integer[]{1})});
    }

    @Test
    public void test4() throws Exception {
        Assert.assertArrayEquals(new boolean[]{true}, new boolean[]{mainArray.isOneAndFourDigit(new Integer[]{4})});
    }

    @After
    public void end(){}
}
