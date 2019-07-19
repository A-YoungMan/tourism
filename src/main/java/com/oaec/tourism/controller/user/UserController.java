package com.oaec.tourism.controller.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oaec.tourism.entity.*;
import com.oaec.tourism.mapper.ContactMapper;
import com.oaec.tourism.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 用户操作
 */
@Controller
@RequestMapping(value = "user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private SequenceService sequenceService;
    @Autowired
    private TripService tripService;
    @Autowired
    private PriceService priceService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private OrderService orderService;
    @Autowired
    private CommentService commentService;
    /**
     * 跳转到用户首页
     * @param session //用于判断用户是否登录
     * @return
     */
    @RequestMapping(value = "index",method = RequestMethod.GET)
    public String returnUserIndex(HttpSession session,Model model){
       User user =(User) session.getAttribute("userLogin");
       if(user !=null){ //用户已登录
           List<Sequence> sequenceList = sequenceService.findByKeying(user.getPhone());
           List<Sequence> seqEditList = new ArrayList<>();
           for(Sequence sequence:sequenceList){
               Map<String, Integer> keyingMap = sequenceKeying(sequence.getValue());
               String title = tripService.findById(keyingMap.get("tripId")).getTitle();
               Price price = priceService.findById(keyingMap.get("priceId"));
               sequence.setValue(title);
               sequence.setStartDate(price.getDate());
               sequence.setPrice(price.getPrice());
               sequence.setNumber(keyingMap.get("adult")+keyingMap.get("child"));
               seqEditList.add(sequence);
           }
           model.addAttribute("sequenceList",seqEditList);
           /*查询当前用户的所有联系人*/
           List<Contact> contactList = contactService.findByUserId(user.getId());
           model.addAttribute("contactList",contactList);

           /*查询订单*/
           PageHelper.startPage(0,6);
           List<Order> orderList = orderService.findByUserId(user.getId());
           PageInfo<Order> pageInfo = new PageInfo<>(orderList);
           model.addAttribute("orderList",pageInfo.getList());
           /*查询评论*/
           PageHelper.startPage(0,3);
           List<Comment> commentList = commentService.findByUserIdOrTripId(user.getId(), null);
           PageInfo<Comment> commentPageInfo = new PageInfo<Comment>(commentList);
           model.addAttribute("commentList",commentPageInfo.getList());
           return "/user/user_index";

       }else{//跳转到登录页面
           return "redirect:/login";
       }
    }

    /**
     * Ajax修改个人信息
     * @param act //操作类型
     * @param session
     * @param base64
     * @param username
     * @param city
     * @param birthday
     * @return
     */
    @RequestMapping(value = "user",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> userDataEdit(String act,
                                           HttpSession session,
                                           @RequestParam(value = "base64",required = false) String base64,
                                           @RequestParam(value = "username",required = false) String username,
                                           @RequestParam(value = "city",required = false) String city,
                                           @RequestParam(value = "birthday",required = false) String birthday){
        Map<String,Object> map = new HashMap<>();
        User user = (User) session.getAttribute("userLogin");
        switch (act){
            case "img"://修改用户头像
                User userTow = new User();
                userTow.setId(user.getId());
                userTow.setImgPath(base64);
                int row = userService.updateUserInfo(userTow);
                if(row>0){
                    session.setAttribute("userLogin",userService.findById(user.getId()));//重新存入session中
                    map.put("ok",true);
                }
                break;
            case "update"://修改其他信息
                break;
        }
        return map;
    }
    /**
     * 添加进入序列提醒用户用户操作下单
     * @param tripId //景点id
     * @param cityId //发车城市
     * @param datesId //出行日期 与价格
     * @param adult //成人数
     * @param child // 儿童数
     * @Session 用于或唯一用户的标识 手机号码 作为序列的keying
     * @RedirectAttributes 错误时从重定向带参数提示
     * @return
     */
    @RequestMapping(value = "save",method = RequestMethod.POST)
    public String UserAddOrder(Integer tripId, @RequestParam(value = "city")Integer cityId,
                               @RequestParam(value = "dateShow") Integer datesId,
                               Integer adult, Integer child, HttpSession session,
                               RedirectAttributes attributes){
        User user = (User)session.getAttribute("userLogin"); //获取当前登录用户的手机号码作为序列
        String value = "tripId="+tripId+"&cityId="+cityId+"&priceId="+datesId+"&adult="+adult+"&child="+child;
        int row = sequenceService.create(value, user.getPhone());
        if(row>0){
            return "redirect:/user/index";
        }else{
            attributes.addFlashAttribute("error","抢购失败!");
            return "redirect:/?act=trip?id="+tripId;
        }
    }

    /**
     * Ajax对订单请求操作，
     * @param act //操作类型
     * @return
     */
    @RequestMapping(value = "order",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> ajaxOrderEdit(String act,
                                            @RequestParam(value = "id",required = false)Integer id,
                                            @RequestParam(value = "sequenceId",required = false) Integer sequenceId,
                                            @RequestParam(value = "adult",required = false)Integer adult,
                                            @RequestParam(value = "child",required = false)Integer child,
                                            @RequestParam(value = "total",required = false)Double total,
                                            @RequestParam(value = "contactId",required = false)Integer [] contactId,
                                            HttpSession session,
                                            @RequestParam(value = "orderId",required = false)Integer orderId){
        Map<String,Object> map =new HashMap<>();
        User user = (User) session.getAttribute("userLogin");
        switch (act){
            case"list"://查询列表
                break;
            case"query": //查询订单操作
                Order order = orderService.findById(orderId);
                List<Comment> commentList = commentService.findByUserIdOrTripId(user.getId(), order.getTrip().getId());
                if(commentList.size() == 0){//不存在则可以新增评论
                    map.put("comment",true);
                }
                commentList.clear();
                map.put("order",order);
                break;
            case"save"://保存订单
                Sequence sequenceOrder =sequenceService.findById(sequenceId);
                int row = orderService.saveOrder(sequenceOrder, user, total, adult, child, contactId);
                if(row>0){
                     sequenceService.delete(sequenceId);//删除预序列订单
                    map.put("ok",true);
                }
                break;
            case"querySequence": //查询序列
                Sequence sequence = sequenceService.findById(sequenceId);
                Map<String, Integer> keyingMap = sequenceKeying(sequence.getValue());
                Price price = priceService.findById(keyingMap.get("priceId"));
                sequence.setStartDate(price.getDate());
                sequence.setPrice(price.getPrice());
                map.put("sequence",sequence);
                map.put("adult",keyingMap.get("adult"));
                map.put("child",keyingMap.get("child"));
                break;
        }
        return map;
    }

    /**
     * Ajax 请求添加用户评论
     * @act //操作类型
     * @return
     */
    @RequestMapping(value = "comment",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> userCommentEdit(String act,HttpSession session,
                                              @RequestParam(value = "orderId",required = false)Integer orderId,
                                              @RequestParam(value = "type",required = false)Integer type,
                                              @RequestParam(value = "service",required = false)Integer service,
                                              @RequestParam(value = "place",required = false)Integer place,
                                              @RequestParam(value = "hotel",required = false)Integer hotel,
                                              @RequestParam(value = "traffic",required = false)Integer traffic,
                                              @RequestParam(value = "content",required = false)String content,
                                              @RequestParam(value  ="id",required = false)Integer id){
        User user = (User) session.getAttribute("userLogin");
        Map<String,Object> map =new HashMap<>();
        switch (act){
            case"list":
                break;
            case"delete"://删除评论
                int delete = commentService.delete(id);
                if(delete >0){
                    map.put("ok",true);
                }
                break;
            case"save"://保存
                Order order = orderService.findById(orderId);
                Comment comment = new Comment();
                comment.setType(type);
                comment.setTraffic(traffic);
                comment.setCreateTime(new Date());
                comment.setService(service);
                comment.setPlace(place);
                comment.setHotel(hotel);
                comment.setOrder(order);
                comment.setTrip(order.getTrip());
                comment.setUser(user);
                comment.setContent(content);
                int row = commentService.createComment(comment);
                if(row>0){
                    map.put("ok",true);
                }
                break;
        }
        return map;
    }
    /**
     * ajax请求操作用户订单序列的数据
     * @param act // 操作类型
     * @param sequenceId
     * @return
     */
    @RequestMapping(value = "ajax",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> ajaxUserEdit(String act,
                                           @RequestParam(value = "sequenceId",required = false) Integer sequenceId,
                                           HttpSession session){
        Map<String,Object> map =new HashMap<>();
        User user = (User) session.getAttribute("userLogin");
        switch (act){
            case"querySequence": //查找序列是否存在，存在则提示用户
                List<Sequence> sequenceList = sequenceService.findByKeying(user.getPhone());
                if(sequenceList.size()!=0){
                    map.put("sequence",sequenceList.size());
                }
                break;
            case"deleteSequence":// 删除序列预订单
                int row = sequenceService.delete(sequenceId);
                if(row>0){
                    map.put("ok",true);
                }else{
                    map.put("ok",false);
                }
                break;
        }
        return  map;
    }

    /**
     * Ajax创建联系人
     * @param act //操作类型
     * @return
     */
    @RequestMapping(value = "contact",method = RequestMethod.POST)
    @ResponseBody
    public Map<String,Object> contactEdit(String act,
                                          String name,
                                          String phone,
                                          String email,
                                          String cardno,
                                          Integer sex,
                                          String birthday,
                                          Integer id,HttpSession session){
        Map<String,Object> map = new HashMap<>();
        User user =(User) session.getAttribute("userLogin");
        Contact contact =null;
        switch (act){
            case"list"://查询
                List<Contact> list = contactService.findByUserId(user.getId());
                map.put("list",list);
                break;
            case"query": //按名字查询判断是否存在该用户下的联系人名称
                contact = contactService.findByName(user.getId(),name);
                if(cardno == null){
                    map.put("ok",true);
                }
                break;
            case"save"://保存
                contact = new Contact();
                contact.setName(name);
                contact.setPhone(phone);
                if(email!=null &&!email.equals("")){
                    contact.setEmail(email);
                }
                if(cardno!=null &&!cardno.equals("")){
                    contact.setCardno(cardno);
                }
                if(sex!=null &&!sex.equals("")){
                    contact.setSex(sex);
                }
                if(birthday!=null &&!birthday.equals("")){
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    try {
                        Date date = sdf.parse(birthday);
                        contact.setBirthday(date);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    contact.setSex(sex);
                }
                contact.setUser(user);
                int row = contactService.create(contact);
                if(row>0){
                    map.put("ok",true);
                    contact = contactService.findById(row);
                    map.put("contact",contact);
                }else{
                    map.put("ok",false);
                }
                break;
            case"update"://修改
                contact =new Contact();
                contact.setId(id);
                if(email!=null &&!email.equals("")){
                    contact.setEmail(email);
                }
                if(cardno!=null &&!cardno.equals("")){
                    contact.setCardno(cardno);
                }
                if(name!=null &&!name.equals("")){
                    contact.setName(name);
                }
                if(phone!=null &&!phone.equals("")){
                    contact.setPhone(phone);
                }
                int update = contactService.update(contact);
                if(update >0){
                    map.put("ok",true);
                }
                break;
            case"delete"://删除
                int delete = contactService.delete(id);
                if(delete>0){
                    map.put("ok",true);
                }else{
                    map.put("ok",false);
                }
                break;
        }
        return map;
    }
    /**
     * 用于解析 sequenceKeying 里面的value
     * @param value
     * @return
     */
    public static Map<String,Integer> sequenceKeying(String value){
        Map<String,Integer> map = new HashMap<>();
        String[] split = value.split("&");
        for(int i =0;i<split.length;i++){
            String[] strings = split[i].split("=");
            map.put(strings[0],Integer.parseInt(strings[1]));
        }
        return map;
    }
}
