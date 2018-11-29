import entities.PrivateChatMessage;
import org.junit.Test;
import org.junit.Assert;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DaoTests {

    @Test
    public void getPrivateMessages() throws InterruptedException{
        Timestamp time1 = new Timestamp(System.currentTimeMillis());
        Thread.sleep(1000);
        Timestamp time2 = new Timestamp(System.currentTimeMillis());
        Thread.sleep(1000);
        Timestamp time3 = new Timestamp(System.currentTimeMillis());
        Thread.sleep(1000);
        Timestamp time4 = new Timestamp(System.currentTimeMillis());
        Thread.sleep(1000);
        Timestamp time5 = new Timestamp(System.currentTimeMillis());
        Thread.sleep(1000);
        Timestamp time6 = new Timestamp(System.currentTimeMillis());
        List res1 = new ArrayList();
        List res2 = new ArrayList();
        Collections.addAll(res1, time1, time2, time3);
        Collections.addAll(res2, time4, time5, time6);
        Set set = (Set<PrivateChatMessage>) Stream.concat(res1.stream(), res2.stream()).collect(Collectors.toCollection(TreeSet::new));
        Assert.assertEquals(set.size(), 6);
        set.stream().forEach(System.out::println);
    }
}
