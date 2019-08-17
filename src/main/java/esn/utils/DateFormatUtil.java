package esn.utils;

import java.sql.Timestamp;
import java.util.Date;

public class DateFormatUtil {

    public static Timestamp parseDate(String time){
        String[] arr = time.split(":| / ");
        Date date = new Date();
        date.setHours(Integer.valueOf(arr[0]));
        date.setMinutes(Integer.valueOf(arr[1]));
        date.setSeconds(Integer.valueOf(arr[2]));
        Timestamp timestamp = new Timestamp(date.getTime());
        return new Timestamp(date.getTime());
    }
}
