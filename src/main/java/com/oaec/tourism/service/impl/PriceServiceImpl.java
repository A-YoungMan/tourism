package com.oaec.tourism.service.impl;

import com.oaec.tourism.entity.Price;
import com.oaec.tourism.entity.Trip;
import com.oaec.tourism.mapper.PriceMapper;
import com.oaec.tourism.service.PriceService;
import com.oaec.tourism.util.DatesUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class PriceServiceImpl  implements PriceService {

    @Autowired
    private PriceMapper priceMapper;
    @Override
    public Map<String, Object> createPrice(Integer tripId,String dateBetween, float price) {
        Map<String,Object> map = new HashMap<>();
        map.put("ok",false);
        //用于记录已存在的日期价格
        List<String> dateStrList = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(dateBetween.contains("—")){//解析包含则是时间区间
            String [] strArry = dateBetween.split("—");//截取
            List<Date> dateList = DatesUtil.getDays(strArry[0], strArry[1]);//读取两个日期之间的日期
            int row = 0;
            Price price1 = new Price();
            price1.setPrice(price);
            Trip trip = new Trip();
            trip.setId(tripId);
            price1.setTrip(trip);
            for(Date date :dateList){
                //判断当前当前日期价格是否存在
                Price byDateAndTripId = priceMapper.findByDateAndTripId(date, tripId);
                if( byDateAndTripId == null){//新增
                    price1.setDate(date);
                    row =priceMapper.create(price1);
                }else{
                    dateStrList.add(sdf.format(date));
                }
            }
            if(row>0){
                System.out.println("多条插入成功!");
                map.put("ok",true);
            }
            map.put("errorList",dateStrList);//通知用户有那几个日期存在
        }else{//单日期添加
            Date date =null;
            try {
                date = sdf.parse(dateBetween);
                //判断当前当前日期价格是否存在
                Price byDateAndTripId = priceMapper.findByDateAndTripId(date, tripId);
                if(byDateAndTripId ==null){
                    Price price1 = new Price();
                    price1.setPrice(price);
                    Trip trip = new Trip();
                    trip.setId(tripId);
                    price1.setTrip(trip);
                    price1.setDate(date);
                    int row = priceMapper.create(price1);
                    if(row>0){
                        System.out.println("单条插入成功!");
                        map.put("ok",true);
                    }
                }else{
                    map.put("error","当前日期的价格已存在!");
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return map;
    }

    @Override
    public int update(Integer id, Float price) {
        return priceMapper.update(id, price);
    }

    @Override
    public int delete(int id) {
        return priceMapper.delete(id);
    }

    @Override
    public int deleteDate(Integer year, Integer month, Integer tripId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();//获取当前日期
        String startDate ="'"+sdf.format(date)+"'";//获取起始日期

        Calendar cale = Calendar.getInstance();
        cale.set(year,month,1); //存入年月份，当前一号起
        cale.set(Calendar.DAY_OF_MONTH, 0); //设置当月的最后一天
        String endDate = "'"+sdf.format(cale.getTime())+"'";;//获取结尾日期
        return priceMapper.deleteDate(tripId,startDate,endDate);
    }

    @Override
    public Price findById(int id) {
        return priceMapper.findById(id);
    }

    @Override
    public Price findByDateAndTripId(String dateBetween, Integer tripId) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(dateBetween);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return priceMapper.findByDateAndTripId(date,tripId);
    }

    @Override
    public List<Price> findByAllTripId(int id) {
        return priceMapper.findByAllTripId(id);
    }

    @Override
    public List<Price> findByAllTripIdAndYearAndMonth(int tripId, String year, String month) {
        String yearAndMonth = null;
        if(year !=null && !year.equals("")){//年份不为空时
            yearAndMonth = year+"-"+(month.length()==1?"0"+month:month); //拼接 2019-07
        }else{//为空时，默认获取当前系统年份
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
            Date date = new Date();
            yearAndMonth = sdf.format(date)+"-"+(month.length()==1?"0"+month:month);
        }
        return priceMapper.findByAllTripIdAndYearAndMonth(tripId,yearAndMonth);
    }
}
