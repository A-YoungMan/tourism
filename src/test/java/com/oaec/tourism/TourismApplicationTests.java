package com.oaec.tourism;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.oaec.tourism.entity.Comment;
import com.oaec.tourism.entity.Detail;
import com.oaec.tourism.entity.Place;
import com.oaec.tourism.mapper.DetailMapper;
import com.oaec.tourism.mapper.PlaceMapper;
import com.oaec.tourism.mapper.TripMapper;
import com.oaec.tourism.service.CommentService;
import com.oaec.tourism.util.TxtPathUrlUtil;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TourismApplicationTests {
    @Autowired
    CommentService commentService;
    @Test
    public void test(){
        String[] s = {"abc","bcd","def"};
       // s.length
    }
    class t{

    }
    class  a extends t{
        a(){
        }
    }
}
