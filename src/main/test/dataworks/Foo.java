package dataworks;

import org.junit.Test;

import java.util.Random;

/**
 * @Description TODO
 * @Auther jerry
 * @Date 2022/3/16 16:27
 */
public class Foo {

    @Test
    public void test1(){

        Random random = new Random();
        int i,k;
        for (int j = 0; j < 20; j++) {
            i = random.nextInt(33);
            k = random.nextInt(2);
            System.out.println(i+"\t"+k);
        }

    }

}
