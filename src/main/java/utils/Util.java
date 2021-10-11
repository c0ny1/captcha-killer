/**
 * Copyright (c) 2019 c0ny1 (https://github.com/c0ny1/captcha-killer)
 * License: MIT
 */
package utils;

import burp.BurpExtender;
import burp.IResponseInfo;
//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
    /**
     * 插件Banner信息
     * @return
     */
    public static String getBanner(String extName,String version){
        String bannerInfo =
                          "[*] \n"
                        + "[*] ###################################################\n"
                        + "[*]   " + extName + " v" + version +"\n"
                        + "[*]   anthor: c0ny1\n"
                        + "[*]   email:  root@gv7.me\n"
                        + "[*]   github: http://github.com/c0ny1/captcha-killer\n"
                        + "[*] ###################################################\n"
                        + "[!] Install " + extName + " successful!\n"
                        + "[*] Please enjoy it ^_^\n"
                        + "[*] ";
        return bannerInfo;
    }

    public static boolean isImage(byte[] img){
        // Reference: https://www.cnblogs.com/shihaiming/p/10404700.html
        boolean isImg = false;
        InputStream buffin = new ByteArrayInputStream(img);
        try {
            //两种判断方式只能选中一种
            //第一种方式
//            ImageInputStream iis = ImageIO.createImageInputStream(buffin);
//            Iterator iter = ImageIO.getImageReaders(iis);
//            if (!iter.hasNext()) {
//                isImg = false;
//            }else {
//                isImg = true;
//            }
            //第二方式
            BufferedImage image = ImageIO.read(buffin);
            if(image == null){
                isImg = false;
            }else {
                isImg = true;
            }
        } catch (IOException e) {
            BurpExtender.stderr.println(e.getMessage());
            isImg = false;
        }
        return isImg;
    }

    public static ImageIcon byte2img(byte[] img) {
        InputStream buffin = new ByteArrayInputStream(img);
        Image image = null;
        ImageIcon icon = null;
        try {
            image = ImageIO.read(buffin);
            icon = new ImageIcon(image);
        } catch (IOException e) {
            BurpExtender.stderr.println(e.getMessage());
            icon = null;
        }
        return icon;
    }

    public static byte[] subBytes(byte[] src, int begin, int count) {
        byte[] bs = new byte[count];
        for (int i=begin; i<begin+count; i++) bs[i-begin] = src[i];
        return bs;
    }

    public static String matchByStartEndPosition(String str,String rule){
        int nStart = 0;
        int nEnd = 0;
        if(nStart > 0 && nStart < nEnd){
            return "Rules of the error: start should >0 and <end";
        }

        if(nEnd <= str.length()){
            return String.format("Rules of the error: end should < response.length(%s)",str.length());
        }
        return str.substring(nStart,nEnd);
    }

    public static String URLEncode(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            BurpExtender.stdout.println(e.getMessage());
        }
        return result;
    }

    public static String URLDecode(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            BurpExtender.stdout.println(e.getMessage());
        }
        return result;
    }

//    public static String base64Encode(byte[] byteArray){
//        //https://www.cnblogs.com/alter888/p/9140732.html
//        final BASE64Encoder encoder = new BASE64Encoder();
//        String res = encoder.encode(byteArray);
//        res = res.replace(System.lineSeparator(),"");
//        return res;
//    }
    public static String base64Encode(byte[] byteArray){
        //https://www.cnblogs.com/alter888/p/9140732.html
        String res = java.util.Base64.getEncoder().encodeToString(byteArray);
        res = res.replace(System.lineSeparator(),"");
        return res;
    }

//    public static String base64Encode(String str){
//        final BASE64Encoder encoder = new BASE64Encoder();
//        byte[] b = new byte[]{};
//        try {
//            b = str.getBytes("UTF-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }
//        String res = encoder.encode(b);
//        //去除base64结果中的换行符，java base64编码默认会76个字母换行一次
//        res = res.replace(System.lineSeparator(),"");
//        return res;
//    }

    public static String base64Encode(String str){

        String res = java.util.Base64.getEncoder().encodeToString(str.getBytes());
        //去除base64结果中的换行符，java base64编码默认会76个字母换行一次
        res = res.replace(System.lineSeparator(),"");
        return res;
    }


//    public static byte[] base64Decode(String str){
//        final BASE64Decoder decoder = new BASE64Decoder();
//        byte[] byteRes = new byte[]{};
//        try {
//            byteRes = decoder.decodeBuffer(str);
//        } catch (IOException e) {
//            BurpExtender.stderr.println("[-] " + e.getMessage());
//        }
//        return byteRes;
//    }

    public static byte[] base64Decode(String str){
        byte[] byteRes = java.util.Base64.getDecoder().decode(str);
        return byteRes;
    }

    public static boolean isURL(String url){
        if (url ==  null ){
            return   false ;
        }
        String regEx =  "^(http|https|ftp)\\://([a-zA-Z0-9\\.\\-]+(\\:[a-zA-"
                +  "Z0-9\\.&%\\$\\-]+)*@)?((25[0-5]|2[0-4][0-9]|[0-1]{1}[0-9]{"
                +  "2}|[1-9]{1}[0-9]{1}|[1-9])\\.(25[0-5]|2[0-4][0-9]|[0-1]{1}"
                +  "[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-4][0-9]|"
                +  "[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[1-9]|0)\\.(25[0-5]|2[0-"
                +  "4][0-9]|[0-1]{1}[0-9]{2}|[1-9]{1}[0-9]{1}|[0-9])|([a-zA-Z0"
                +  "-9\\-]+\\.)*[a-zA-Z0-9\\-]+\\.[a-zA-Z]{2,4})(\\:[0-9]+)?(/"
                +  "[^/][a-zA-Z0-9\\.\\,\\?\\'\\\\/\\+&%\\$\\=~_\\-@]*)*$" ;
        Pattern p = Pattern.compile(regEx);
        Matcher matcher = p.matcher(url);
        return  matcher.matches();
    }

    /**
     * 转义正则特殊字符 $()*+.[]?\^{},|
     * Reference:https://www.cnblogs.com/lovehansong/p/7874337.html
     * @param keyword
     * @return
     */
    public static String escapeExprSpecialWord(String keyword) {
        if (!keyword.equals(null) && !keyword.trim().equals("")) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|",":"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
            keyword = keyword.replace("\r","\\r");
            keyword = keyword.replace("\n","\\n");
        }
        return keyword;
    }

    /**
     * 转移json特殊字符 $()*+.[]?{}/^-|"
     * Renference: https://www.cnblogs.com/javalanger/p/10913838.html
     * @param keyword
     * @return
     */
    public static String escapeJsonString(String keyword){
        if (!keyword.equals(null) && !keyword.trim().equals("")) {
            String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|",":","-"};
            for (String key : fbsArr) {
                if (keyword.contains(key)) {
                    keyword = keyword.replace(key, "\\" + key);
                }
            }
            keyword = keyword.replace("\r","\\r");
            keyword = keyword.replace("\n","\\n");
        }
        return keyword;
    }


    public static byte[] getRspBody(byte[] response){
        IResponseInfo responseInfo = BurpExtender.helpers.analyzeResponse(response);
        int bodyOffset = responseInfo.getBodyOffset();
        int body_length = response.length - bodyOffset;
        return subBytes(response,bodyOffset,body_length);
    }

    /**
     * 统计字符串str中有多少个字符串keyword
     * @param str
     * @param keyword
     * @return 返回存在的个数
     */
    public static int getStringCount(String str,String keyword){
        int len = keyword.length();
        int count = 0;
        int pos = str.indexOf(keyword);
        if(pos != -1){
            count += 1;
            count += getStringCount(str.substring(pos + len,str.length()),keyword);
        }else{
            return count;
        }

        return count;
    }

    /**
     * 统计字符串中有多少个换行符号
     * @param str
     * @return
     */
    public static int getRNCount(String str){
        int count = 0;
        int rCount = Util.getStringCount(str,"\r");
        int nCount = Util.getStringCount(str,"\n");
        if(rCount > 0){
            count = rCount;
        }
        if(nCount > 0){
            count = nCount;
        }
        return count;
    }

    public static byte[] requestImage(String url,String raw){
        if(Util.isURL(url)) {
            HttpClient http = new HttpClient(url, raw, null);
            byte[] rsp = http.doReust();
            BurpExtender.gui.getTaResponse().setText(new String(rsp));
            int BodyOffset = BurpExtender.helpers.analyzeResponse(rsp).getBodyOffset();
            int body_length = rsp.length - BodyOffset;
            byte[] byteImg = Util.subBytes(rsp, BodyOffset, body_length);
            return byteImg;
        }else{
            BurpExtender.stderr.println("[-] captcha URL format invalid");
            return null;
        }
    }

    public static String matchByRegular(String str,String reg){
        String res = "";
        int start = 0;
        int end = 0;
        Pattern r = Pattern.compile(reg);
        Matcher m = r.matcher(str);
        if (m.find()) {
            res = m.group(1);//0会获取多余的内容
            start = m.start();
            int n = str.substring(start,str.length()).indexOf(res);
            start += n;
            end = start + res.length();
        }
        return res;
    }

    public static String matchByRegular(String str,String reg,int n){
        String res = "";
        Pattern r = Pattern.compile(reg,Pattern.MULTILINE);
        Matcher m = r.matcher(str);
        if (m.find()) {
            res = m.group(n);//0会获取多余的内容
        }
        return res;
    }

    /**
     * 将字符串开头空格去掉
     * @param str
     * @return
     */
    public static String trimStart(String str) {
        if (str == "" || str == null) {
            return str;
        }

        final char[] value = str.toCharArray();
        int start = 0, last = 0 + str.length();
        int end = last;
        while ((start <= end) && (value[start] <= ' ')) {
            start++;
        }
        if (start == 0 && end == last) {
            return str;
        }
        if (start >= end) {
            return "";
        }
        return str.substring(start, end);
    }
}
