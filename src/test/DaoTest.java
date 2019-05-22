import com.fasterxml.jackson.databind.ObjectMapper;
import esn.entities.Department;
import esn.entities.secondary.PrivateChatMessage;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DaoTest {

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

    @Test
    public void jsonParser(){
        ObjectMapper om = new ObjectMapper();
        try {
            Department department = om.readValue("{\"name\":\"второй\",\"id\":1549044556665,\"parentId\":0,\"children\":[{\"name\":\"третий\",\"id\":1549044560617,\"parentId\":1549044556665,\"children\":[]},{\"name\":\"четвертый\",\"id\":1549044565449,\"parentId\":1549044556665,\"children\":[]}]}", Department.class);
            department.setParent(null);
            System.out.println(department);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }




}
