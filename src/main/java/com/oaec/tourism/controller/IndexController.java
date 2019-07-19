package com.oaec.tourism.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oaec.tourism.entity.*;
import com.oaec.tourism.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DecimalFormat;
import java.util.*;

@Controller
public class IndexController {
    @Autowired
    private ThemeService themeService; //主题
    @Autowired
    private PlaceService placeService;//景区
    @Autowired
    private TripService tripService; //详细景点
    @Autowired
    private TripPictureService tripPictureService;

    @Autowired
    private DetailService detailService;
    @Autowired
    private DatesService datesService;
    @Autowired
    private CommentService commentService;
    /**
     * 首页访问
     * @param model
     * @return
     */
    @RequestMapping(value = "/")
    public String index(Model model,
                        @RequestParam(value = "act",required = false) String act,
                        @RequestParam(value = "id",required = false) Integer id,
                        @RequestParam(value = "type",required = false) Integer type,
                        @RequestParam(value = "key",required = false) String key) {

        PageHelper.startPage(0, 10);//设置显示最新留十条数据
        List<Trip> domesticTripList = tripService.findAllType(1);//境内游
        PageInfo<Trip> pageInfo3 = new PageInfo<Trip>(domesticTripList);
        model.addAttribute("domesticTripList", pageInfo3.getList());

        PageHelper.startPage(0, 10);//设置显示最新留十条数据
        List<Trip> foreignTripList = tripService.findAllType(2);//境外游
        PageInfo<Trip> pageInfo4 = new PageInfo<Trip>(foreignTripList);
        model.addAttribute("foreignTripList", pageInfo4.getList());

        PageHelper.startPage(0, 10);//设置显示最新留十条数据
        List<Trip> hotList = tripService.findAll(0, 0);//最新
        PageInfo<Trip> hotPageInfo = new PageInfo<Trip>(hotList);
        model.addAttribute("hotList", hotPageInfo.getList());

        List<Theme> themeList = themeService.findAll(0);//主题
        model.addAttribute("themeList", themeList);

        PageHelper.startPage(0, 6);//设置显示最新留六条数据
        List<Place> placeList = placeService.findAll(0);//景点
        PageInfo<Place> pageInfo = new PageInfo<>(placeList);
        model.addAttribute("placeList", pageInfo.getList());

        PageHelper.startPage(0, 10);//设置显示最新留十条数据
        List<Trip> sinceTripList = tripService.findAllType(0);//自行游
        PageInfo<Trip> pageInfo2 = new PageInfo<Trip>(sinceTripList);
        model.addAttribute("sinceTripList", pageInfo2.getList());

        if(act == null || act.equals("")) {

            /*以下是站内地图*/
            //度假旅游
            PageHelper.startPage(0, 8);
            List<Place> placeFindAllList = placeService.findAll(0);
            PageInfo<Place> placePageInfo = new PageInfo<>(placeFindAllList);
            model.addAttribute("placeFindAllList", placePageInfo.getList());//查询所有

            PageHelper.startPage(8, 16);
            List<Place> placeFindAllList2 = placeService.findAll(0);
            PageInfo<Place> placePageInfo2 = new PageInfo<>(placeFindAllList2);
            model.addAttribute("placeFindAllList2", placePageInfo2.getList());//查询所有

            //牛人专线themeId = 12 的所有景区，set不可重复
            List<Trip> allThemeId = tripService.findAllThemeId(12);
            Set<Place> placeSet = new HashSet<>();
            for (Trip trip : allThemeId) {
                placeSet.add(trip.getPlace());
            }
            model.addAttribute("placeSet", placeSet);//查询所有
            //热门景区
            List<Trip> all = tripService.findAll(0, null);
            Set<Place> hotPlaceSet = new HashSet<>();
            for (Trip trip : all) {
                hotPlaceSet.add(trip.getPlace());
            }
            model.addAttribute("hotPlaceSet", hotPlaceSet);
            //境内游
            List<Place> chinaPlace = placeService.findAllCityNameAndType(0, "中国");
            model.addAttribute("chinaPlace", chinaPlace);
            //境外游
            List<Place> externalPlaceList = placeService.findAll(0);
            Set<Place> externalPlaceSet = new HashSet<>();
            for (Place place : externalPlaceList) {
                if (place.getCity() != null) {
                    if (!place.getCity().getName().equals("中国")) {//不等于中国的全部属于境外游
                        externalPlaceSet.add(place);
                    }
                }
            }
            model.addAttribute("externalPlaceSet", externalPlaceSet);//查询所有
        }else{ //操作
            List<Trip> tripList = null;
            switch (act){
                case"type"://出游类型查询
                    switch (type){
                        case 0://自行游
                            model.addAttribute("title","自行游");
                            tripList = tripService.findAllType(0);
                            break;
                        case 1://国内游
                            model.addAttribute("title","国内游");
                            tripList = tripService.findAllType(1);
                            break;
                        case 2: //境外游
                            model.addAttribute("title","境外游");
                            tripList = tripService.findAllType(2);
                            break;
                    }
                    model.addAttribute("tripList",tripList);
                    return "place_details";
                case"trip"://进入详情页
                    Trip trip = tripService.findById(id);
                    List<TripPicture> list = tripPictureService.findByAllTripId(trip.getId());
                    model.addAttribute("trip",trip);
                    //产品详情
                    Detail detail = detailService.findByIdOrTrip(null, trip.getId(), true);//解析
                    model.addAttribute("detail",detail);
                    //日程
                    List<Dates> datesList = datesService.findByTripId(trip.getId());
                    if( datesList !=null){
                        datesList = datesList.subList(0,trip.getTime());//截取输出
                    }
                    model.addAttribute("datesList",datesList);

                    /*评论*/
                    List<Comment> commentList = commentService.findByTripOrType(trip.getId(), null);
                    if(commentList.size()!=0){
                        int sum =0;//一般与满意个数
                        int satisfied=0; //满意个数
                        int general =0; //一般个数
                        int difference =0;//差个数
                        int place =0;//景点评分
                        int hotel =0;
                        int service =0;
                        int traffic =0;
                        for(Comment comment:commentList){
                            place = place +comment.getPlace();
                            hotel = hotel +comment.getHotel();
                            service = service +comment.getService();
                            traffic = traffic +comment.getTraffic();
                            if(comment.getType() == 0){
                                satisfied = satisfied+1;
                                sum =sum+1;
                                continue;
                            }
                            if(comment.getType() == 1){
                                general =general+1;
                                sum =sum+1;
                                continue;
                            }
                            if(comment.getType() == 2){
                                difference = difference+1;
                                continue;
                            }
                        }
                        DecimalFormat df = new DecimalFormat( "0.00");
                        //计算满意度满意度
                        double number = (double) sum/commentList.size();
                        model.addAttribute("number",df.format(number*100));
                        model.addAttribute("count",commentList.size());//获取总数
                        //计算满意个数
                        model.addAttribute("satisfied",satisfied);
                        model.addAttribute("satisfiedNum",df.format(((double)satisfied/commentList.size())*100));
                        //计算一般个数
                        model.addAttribute("general",general);
                        model.addAttribute("generalNum",df.format((double)general/commentList.size()*100));
                        //计算差个数
                        model.addAttribute("difference",difference);
                        model.addAttribute("differenceNum",df.format((double)difference/commentList.size()*100));

                        /*获取平均值*/
                        model.addAttribute("place",getAverage(place,commentList.size()));
                        model.addAttribute("hotel",getAverage(hotel,commentList.size()));
                        model.addAttribute("service",getAverage(service,commentList.size()));
                        model.addAttribute("traffic",getAverage(traffic,commentList.size()));
                    }
                    /*分页*/
                    PageHelper.startPage(0,5);
                    commentList =commentService.findByTripOrType(trip.getId(),null);
                    PageInfo<Comment> commentPageInfo = new PageInfo<>(commentList);
                    model.addAttribute("commentList",commentPageInfo.getList());
                    return "trip_details";
                case"key"://名称模糊
                    List<Place> keyPlaceList = null;
                    List<Trip> findAllTripList = new ArrayList<>();
                    switch (type){
                        case 0: //国家
                            model.addAttribute("title","按国家查询："+key);
                            keyPlaceList = placeService.findAllCityNameAndType(0, key);
                            if(keyPlaceList !=null){
                                for(Place place: keyPlaceList){
                                    List<Trip> trips = tripService.findAllPlaceById(place.getId(), 0, null);
                                    for(Trip trip1: trips){
                                        findAllTripList.add(trip1);
                                    }
                                }
                            }
                            break;
                        case 1: // 景点名称
                            model.addAttribute("title","按景点名称查询："+key);
                            keyPlaceList = placeService.findAllName(0, key);
                            if(keyPlaceList !=null){
                                for(Place place: keyPlaceList){
                                    List<Trip> trips = tripService.findAllPlaceById(place.getId(), 0, null);
                                    for(Trip trip1: trips){
                                        findAllTripList.add(trip1);
                                    }
                                }
                            }
                            break;
                        case 2: //主题
                            model.addAttribute("title","按主题查询："+key);
                            List<Theme> themes = themeService.findAllKey(key, 0);
                            if(themes != null){
                                for (Theme theme:themes){
                                    List<Trip> trips = tripService.findAllThemeId(theme.getId());
                                    for(Trip trip1: trips){
                                        findAllTripList.add(trip1);
                                    }
                                }
                        }
                            break;
                    }
                    if(findAllTripList.size() == 0){
                        model.addAttribute("error","抱歉！没有相关字“"+key +"”的信息!");
                    }else{
                        model.addAttribute("tripList",findAllTripList);
                    }
                    return "place_details";
                case"theme": //主题查找
                    //tripService.findb
                    Theme theme = themeService.findById(id, 0);
                    model.addAttribute("title",theme.getName());
                    tripList = tripService.findAllThemeId(id);
                    model.addAttribute("tripList",tripList);
                    return "place_details";
                case"place": //景区查找
                    Place place = placeService.findById(id);
                    model.addAttribute("title",place.getName());
                    tripList = tripService.findAllPlaceById(id,0,null);
                    model.addAttribute("tripList",tripList);
                    return "place_details";
                    default:
                        return "redirect:/";
            }
        }
        return "index";
    }

    /**
     * Ajax请求操作查询特推旅游项目
     *
     * @param placeId //按景点id查询
     * @return
     */
    @RequestMapping(value = "ajax", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> findPushNew(Integer placeId) {
        Map<String, Object> map = new HashMap<>();
        PageHelper.startPage(0, 4);//显示4条
        List<Trip> list = tripService.findAllPlaceById(placeId, 0, null);
        PageInfo<Trip> pageInfo = new PageInfo<>(list);
        map.put("list", pageInfo.getList());
        return map;
    }

    /**
     * AJax请求查询景区图片名称
     *
     * @param act //查找类型
     * @name  景区名称查询
     * @themeId 按主题id查询
     * @placeId 热门景点 id查询
     * @return
     * @type //查询精选的类型 0 欧洲 1 亚洲
     */
    @RequestMapping(value = "ajax/type", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> findAllTypeSwim(String act,
                                               @RequestParam(value = "type", required = false) Integer type,
                                               @RequestParam(value = "name",required = false)String name,
                                               @RequestParam(value = "themeId",required = false)Integer themeId,
                                               @RequestParam(value = "placeId",required = false)Integer placeId) {
        Map<String, Object> map = new HashMap<>();
        List<Trip> list = null;
        switch (act) {
            case "foreign"://境外游查询
                PageHelper.startPage(0, 5);
                list = tripService.findAllType(2);
                break;
            case "domestic"://国内游
                PageHelper.startPage(0, 5);
                list = tripService.findAllType(1);
                break;
            case "since"://自行游
                PageHelper.startPage(0, 5);
                list = tripService.findAllType(0);
                break;
            case "handpick"://精选
                if(name.equals("马代")){
                    name = "马尔代夫";
                }
                PageHelper.startPage(0, 5);
                if (type == 0) {//欧洲
                    list =tripService.findAllPlaceByName(name,0,2);
                } else { //亚洲
                    list =tripService.findAllPlaceByName(name,0,2);
                }
                break;
            case"theme": //主题查询
                PageHelper.startPage(0, 8);
                if(themeId==null || themeId.equals("")){
                    list = tripService.findAll(0,null);
                }else{
                    list = tripService.findAllThemeId(themeId);
                }
                break;
            case"hot"://热门景点id查询
                PageHelper.startPage(0, 6);
                if(placeId==null || placeId.equals("")){//不存在则刷新马尔代夫
                    Place all = placeService.findAll(0).get(0); //获取当前最新第一条
                    list = tripService.findAllPlaceById(all.getId(),0,0);
                }else{
                    list = tripService.findAllPlaceById(placeId, 0, 0);
                }
                break;
        }
        PageInfo<Trip> pageInfo =new PageInfo<>(list);
        map.put("list",pageInfo.getList());
        return map;
    }

    /**
     * 登录成功后重定向到首页
     * @return
     */
    @RequestMapping(value = "index",method = RequestMethod.GET)
    public String index(){
        return "redirect:/";
    }

    //求平均值
    public double getAverage(int count,int num){
        double d = count / num;
        return  d;
    }
}
