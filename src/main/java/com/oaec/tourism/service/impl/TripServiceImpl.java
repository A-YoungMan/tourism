package com.oaec.tourism.service.impl;

import com.oaec.tourism.entity.City;
import com.oaec.tourism.entity.Trip;
import com.oaec.tourism.mapper.CityMapper;
import com.oaec.tourism.mapper.RegionMapper;
import com.oaec.tourism.mapper.TripMapper;
import com.oaec.tourism.service.CityService;
import com.oaec.tourism.service.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TripServiceImpl implements TripService {
    @Autowired
    private TripMapper tripMapper;
    @Autowired
    private CityMapper cityMapper;
    @Autowired
    private RegionMapper regionMapper;

    @Override
    //@CacheEvict(value="trip-key") //清除缓存
    public Map<String, Object> addAjaxTrip(Trip trip, int placeId,String province,String cityName,String area) {
        Map<String, Object> map = new HashMap<>();
        map.put("ok", false);
        /*创建省*/
        City city =null;
        if(province!=null && !province.equals("")){ //创建省份
            city = new City();
            city.setName(province);
            city.setType(1);
            city.setCity(cityMapper.findById(1));//指向国家
            System.out.println("创建省份!");
            int i = cityMapper.create(city);
            if(i>0){
                if(cityName!=null && !cityName.equals("")){
                    city.setType(2);
                    city.setName(cityName);
                    city.setCity(cityMapper.findById(city.getId()));
                    System.out.println("创建市级");
                    i =cityMapper.create(city);
                    if(i>0){
                        if(area!=null && !area.equals("")){
                            city.setType(3);
                            city.setName(area);
                            city.setCity(cityMapper.findById(city.getId()));
                            System.out.println("创建县级");
                            i =cityMapper.create(city);
                        }
                    }
                }

            }
        }else{
            map.put("error","省份不能为空!请重新选择!");
        }
        trip.setCity(city);
        if (trip.getTitle() != null && !trip.getTitle().equals("") && trip.getSTitle() != null && !trip.getSTitle().equals("")) {
            if (trip.getCity() != null) {
                trip.setCreateTime(new Date());
                trip.setDType(0);
                int i = tripMapper.create(trip);
                if (i > 0) {
                    map.put("placeId", placeId);
                    map.put("tripId", trip.getId());
                    i = tripMapper.addTripOnPlace(map);
                    if(i>0){
                        System.out.println("景点AND旅游桥表插入成功!");
                        map.put("ok",true);
                        map.put("success", "插入一条数据成功!");
                    }
                }
            } else {
                map.put("error", "请选择城市!");
            }
        } else {
            map.put("error", "请填写完整信息!");
        }
        return map;
    }

    @Override
    @CacheEvict(value="trip-key") //清除缓存
    public Map<String, Object> addTrip(Trip trip, int placeId, Integer provinceId, Integer cityId, Integer areaId) {
        Map<String, Object> map = new HashMap<>();
        map.put("ok", false);
        /*创建省*/
        City city =null;
        if(provinceId!=null && !provinceId.equals("")){ //创建省份
            city = new City();
            city.setName(regionMapper.findById(provinceId).getName());
            city.setType(1);
            city.setCity(cityMapper.findById(1));//指向国家
            System.out.println("创建省份!");
            int i = cityMapper.create(city);
            if(i>0){
                if(cityId!=null && !cityId.equals("")){
                    city.setType(2);
                    city.setName(regionMapper.findById(cityId).getName());
                    city.setCity(cityMapper.findById(city.getId()));
                    System.out.println("创建市级");
                    i =cityMapper.create(city);
                    if(i>0){
                        if(areaId!=null && !areaId.equals("")){
                            city.setType(3);
                            city.setName(regionMapper.findById(areaId).getName());
                            city.setCity(cityMapper.findById(city.getId()));
                            System.out.println("创建县级");
                            i =cityMapper.create(city);
                        }
                    }
                }

            }
        }else{
            map.put("error","省份不能为空!请重新选择!");
        }
        trip.setCity(city);
        if (trip.getTitle() != null && !trip.getTitle().equals("") && trip.getSTitle() != null && !trip.getSTitle().equals("")) {
            if (trip.getCity() != null) {
                trip.setCreateTime(new Date());
                trip.setDType(0);
                int i = tripMapper.create(trip);
                if (i > 0) {
                    map.put("placeId", placeId);
                    map.put("tripId", trip.getId());
                    i = tripMapper.addTripOnPlace(map);
                    if(i>0){
                        System.out.println("景点AND旅游桥表插入成功!");
                        map.put("ok",true);
                    }
                }
            } else {
                map.put("error", "请选择城市!");
            }
        } else {
            map.put("error", "请填写完整信息!");
        }
        return map;
    }

    @Override
    @CacheEvict(value="trip-key") //清除缓存
    public int update(Trip trip,Integer province,Integer name,Integer area) {
        /*新增景点*/
        City city =null;
        int i=0;
        if(province!=null && !province.equals("")){
            city = new City();
            city.setName(regionMapper.findById(province).getName());
            city.setType(1);
            city.setCity(cityMapper.findById(1));//指向中国
             i= cityMapper.create(city);
             if(i>0){
                 if(name!=null && !name.equals("")){
                     city.setType(2);
                     city.setName(regionMapper.findById(name).getName());
                     city.setCity(city);
                     i= cityMapper.create(city);
                     if(i>0){
                         if(area!=null && !area.equals("")){
                             city.setType(3);
                             city.setName(regionMapper.findById(area).getName());
                             city.setCity(city);
                             i= cityMapper.create(city);
                         }
                     }
                 }
             }
        }
        trip.setCity(city!=null?null:city);
        return tripMapper.update(trip);
    }

    @Override
    public int updateIsOk(Integer id, int is) {
        return tripMapper.updateIsOk(id, is);
    }

    @Override
    public Trip findByIdAndNum(Integer id, Integer num, Integer is) {
        Trip trip = tripMapper.findByIdAndNum(id, num, is);
        String p ="";
        String city ="";
        String a = "";

        if(trip.getCity()!=null){ //获取层级地址
            a = trip.getCity().getName();
            if(trip.getCity().getCity()!=null){
                city = city +trip.getCity().getCity().getName();
                if(trip.getCity().getCity().getCity()!=null){
                    p = city +trip.getCity().getCity().getCity().getName();
                }
            }
        }
        trip.setTemporary(p+city+a);
        return trip;
    }

    @Override
    public Trip findById(int id) {
        return tripMapper.findById(id);
    }

    @Override
    public List<Trip> findAllPlaceById(int placeId, Integer is, Integer typeId) {
        return tripMapper.findAllPlaceById(placeId, is, typeId);
    }

    @Override
    public List<Trip> findAllPlaceByName(String placeName, Integer is, Integer type) {
        /*判断查询出游类型 0自驾游 1国内游 2境外游*/
        if(type !=null ){
            if(type % 3 ==0){
                type = 0;
            }else if(type % 3 ==1){
                type = 1;
            }else if(type % 3 ==2){
                type = 2;
            }
        }
        return tripMapper.findAllPlaceByName(placeName, is, type);
    }

    @Override
    public List<Trip> findAllCityById(int cityId, int is) {
        return tripMapper.findAllCityById(cityId, is);
    }

    @Override
   @Cacheable(value="trip-key")
    public List<Trip> findAll(int dType, Integer is) {
        return tripMapper.findAll(dType, is);
    }

    @Override
    public List<Trip> findAllType(int type) {
        return tripMapper.findAllType(type);
    }

    @Override
    public List<Trip> findAllThemeId(int themeId) {

        return tripMapper.findAllThemeId(themeId);
    }

    @CacheEvict(value="trip-key") //清除缓存
    @Override
    public int addRecovery(Integer id) {
        return tripMapper.addRecovery(id);
    }

   // @CacheEvict(value="trip-key") //清除缓存
    @Override
    public int closeRecovery(Integer id) {
        return tripMapper.closeRecovery(id);
    }
   @CacheEvict(value="trip-key") //清除缓存
    @Override
    public int returnList(Integer id) {
        return tripMapper.returnList(id);
    }
}
