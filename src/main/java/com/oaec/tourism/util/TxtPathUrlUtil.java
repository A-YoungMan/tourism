package com.oaec.tourism.util;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

/*解析或创建txt文本 */
public class TxtPathUrlUtil {
    /*读取文本*/
    public static String getPathTxt(String url) {
        StringBuffer sb = new StringBuffer();
        BufferedReader br = null;
        try {
            File file = new File(url);
            if (file.exists()) {//存在则开始解析
                br = new BufferedReader(new InputStreamReader(new FileInputStream(file), "UTF-8"));
                String end = null;
                while ((end = br.readLine()) != null) {
                    sb.append(end);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }

    /*写入方法*/
    public static void outTxt(String url, String txt) {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(
                    new OutputStreamWriter(
                            new FileOutputStream(
                                    new File(url)), "UTF-8"));
            bw.write(txt);
            bw.newLine();
            bw.flush();
            bw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("创建或写入文本：" + url);
    }

    /*判断是否是读取或新增*/
    public static String inpOrOutIo(String url, String txt) {
        if (url != null && !url.equals("")) {
            if (url.endsWith(".txt")) {//原有数据库删除后判断当前路径是否是.txt文本为后缀
                File file = new File(url);
                if (file.exists()) {//判断文件是否存在存在读取，
                    String text = getPathTxt(url);
                    return text;
                } else {//新增
                    outTxt(url, txt);
                    String text =getPathTxt(url);//重新解析
                    return text;
                }
            }else{//为空则外部提示启用新增
               return null;
            }
        }else{
            return null;
        }
    }
}
