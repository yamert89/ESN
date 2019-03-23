import org.junit.Test;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Stream {

    @Test
    public void stream(){
        Map<Timestamp, String> map = new HashMap<>();
        map.put(new Timestamp(System.currentTimeMillis()), "text1");
        map.put(new Timestamp(System.currentTimeMillis() + 123L), "text2");
        map.put(new Timestamp(System.currentTimeMillis() + 5442L), "text3");

        List<String> nodes = map.entrySet().stream().map(el -> {
            Timestamp time = el.getKey();
            StringBuilder sb = new StringBuilder("{m:");
            sb.append(time.getMonth()).append(", d:").append(time.getDay()).append(", t:\"").append(el.getValue())
                    .append("\"}");
            el.setValue(sb.toString());
            return el;
        }).map(Map.Entry::getValue).collect(Collectors.toList());
        nodes.forEach(System.out::println);

        //Assert.assertEquals(nodes.size(), 3);
    }
}
