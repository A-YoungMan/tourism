package com.oaec.tourism.util;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Decoder;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/*文件上传工具类*/
public class ImgUploadUtil {
    /*单文件上传*/
    public static Map<String, Object> imgUpload(String absolutePath, String relativePath, MultipartFile file) {
        Map<String, Object> map = new HashMap<>();
        map.put("ok", false);
        if (file.isEmpty()) {
            map.put("error", "请选择文件!");
            return map;
        }
        //所有文件的后缀
        String[] imgsEnd = new String[]{".jpg", ".jpeg", ".ren", ".gif", ".gif", ".png"};
        boolean bimg = false;
        //判断是否是图片
        //获取文件的类型.是否是图片类型
        String end = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
        for (String str : imgsEnd) {
            if (end.equalsIgnoreCase(str)) {
                bimg = true;
                break;
            }
        }
        try {
            if (bimg) {
                byte[] bytes = file.getBytes();
                //绝对路径
                String name = System.currentTimeMillis() + end; //新的文件名
                String fileName = absolutePath + name;//拼接文件名
                Path path = Paths.get(fileName);
                Files.write(path, bytes);//上传
                //拼接相对路径
                String rPath = relativePath + name;
                map.put("ok", true);
                map.put("path", rPath);//获取相对路径
            } else {
                map.put("error", "上传文件类型不匹配!类型必须是:" + Arrays.toString(imgsEnd));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * base64字符串转化成图片
     * @param imgStr //Base字符串
     * @param endFileName //文件后缀
     * @param uploadImgUrl //上传路径
     * @return
     */
    public static boolean getImgBase64(String imgStr,String endFileName,String uploadImgUrl) {
        //对字节数组字符串进行Base64解码并生成图片
        if (imgStr == null) //图像数据为空
            return false;
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            //Base64解码
            byte[] b = decoder.decodeBuffer(imgStr);
            for (int i = 0; i < b.length; ++i) {
                if (b[i] < 0) {//调整异常数据
                    b[i] += 256;
                }
            }
            //生成jpeg图片
            String imgFilePath = uploadImgUrl + System.currentTimeMillis() + endFileName;//新生成的图片
            OutputStream out = new FileOutputStream(imgFilePath);
            out.write(b);
            out.flush();
            out.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
