package com.oaec.tourism.controller.admin.comment;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oaec.tourism.entity.Comment;
import com.oaec.tourism.entity.Place;
import com.oaec.tourism.service.CommentService;
import com.oaec.tourism.service.PlaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 管理员管理用评论
 */
@Controller
@RequestMapping("/admin/trip/comment")
public class AdminCommentController {
    @Autowired
    private CommentService commentService;

    @Autowired
    private PlaceService placeService;

    @RequestMapping(value = "list")
    public String findAllComment(Model model) {
        List<Comment> commentList = commentService.findAll();
        model.addAttribute("commentList", commentList);
        List<Place> placeList = placeService.findAll(0);
        model.addAttribute("placeList",placeList);
        return "/admin/comment/comment_list";
    }
    @RequestMapping(value = "delete",method = RequestMethod.GET)
    public String deleteComment(int id, RedirectAttributes attributes){
        int delete = commentService.delete(id);
        if(delete > 0 ){
            attributes.addFlashAttribute("success","删除成功!");
        }else{
            attributes.addFlashAttribute("error","删除失败!");
        }
        return "redirect:/admin/trip/comment/list";
    }
    /**
     * Ajax操作评论
     *
     * @return
     */
    @RequestMapping(value = "ajax", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> ajaxCommentEdit(String act,
                                               @RequestParam(value = "id",required = false) Integer id,
                                               @RequestParam(value = "placeId",required = false) Integer placeId,
                                               @RequestParam(value = "type",required = false) Integer type,
                                               @RequestParam(value = "tripType",required = false) Integer tripType) {
        Map<String, Object> map = new HashMap<>();
        Comment comment = null;
        switch (act) {
            case "query":
                comment = commentService.findById(id);
                if (comment != null) {
                    map.put("comment", comment);
                }
                break;
            case "type":
                List<Comment> commentList = commentService.findByPlaceIdAndTypeAndTripType(placeId, type, tripType);
                map.put("list",commentList);
                break;
        }
        return map;
    }
}
