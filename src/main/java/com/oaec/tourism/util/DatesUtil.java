package com.oaec.tourism.util;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
/*解析两个日期之间的日期*/
public class DatesUtil {
    public static void main(String[] args) {
        List<Date> list = getDays("2015-05-05", "2015-06-01");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        for(Date str :list){
            System.out.println(dateFormat.format(str));
        }
    }
    /**
     * 获取两个日期之间的所有日期
     *
     * @param startTime
     *            开始日期
     * @param endTime
     *            结束日期
     * @return
     */
    public static List<Date> getDays(String startTime, String endTime) {

        // 返回的日期集合
        List<Date> days = new ArrayList<Date>();

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date start = dateFormat.parse(startTime);
            Date end = dateFormat.parse(endTime);

            Calendar tempStart = Calendar.getInstance();
            tempStart.setTime(start);

            Calendar tempEnd = Calendar.getInstance();
            tempEnd.setTime(end);
            tempEnd.add(Calendar.DATE, +1);// 日期加1(包含结束)
            while (tempStart.before(tempEnd)) {
                days.add(tempStart.getTime());
                tempStart.add(Calendar.DAY_OF_YEAR, 1);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return days;
    }
}
