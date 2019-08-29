package esn.utils;

import java.sql.Timestamp;
import java.util.Date;

public class DateFormatUtil {

    public static Timestamp parseDate(String time, boolean needDate){
        String[] arr = time.split(":| / ");
        Date date = new Date();
        date.setHours(Integer.valueOf(arr[0]));
        date.setMinutes(Integer.valueOf(arr[1]));
        date.setSeconds(Integer.valueOf(arr[2]));
        if (!needDate) return new Timestamp(date.getTime());
        String[] arr2 = arr[3].split("\\.");
        date.setDate(Integer.valueOf(arr2[0]));
        date.setMonth(Integer.valueOf(arr2[1]) - 1);
        return new Timestamp(date.getTime());
    }
}
