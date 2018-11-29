import entities.PrivateChatMessage;
import org.junit.Test;
import org.junit.Assert;

import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DaoTests {

    @Test
    public void getPrivateMessages(){
        Timestamp time1 = new Timestamp(5345123124212312L);
        Timestamp time2 = new Timestamp(93451236124212312L);
        Timestamp time3 = new Timestamp(53451231242612312L);
        Timestamp time4 = new Timestamp(5364533124212312L);
        Timestamp time5 = new Timestamp(53451224212312L);
        Timestamp time6 = new Timestamp(534512316212312L);
        List res1 = new ArrayList();
        List res2 = new ArrayList();
        Collections.addAll(res1, time1, time2, time3);
        Collections.addAll(res2, time4, time5, time6);
        Set set = (Set<PrivateChatMessage>) Stream.concat(res1.stream(), res2.stream()).collect(Collectors.toCollection(TreeSet::new));
        Assert.assertEquals(set.size(), 6);
        set.stream().forEach(System.out::println);
    }
}
