package utils;

import burp.BurpExtender;
import sun.misc.BASE64Encoder;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Util {
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

    public static String match(String str,String pattern){
        Pattern r = Pattern.compile(pattern);
        Matcher m = r.matcher(str);
        if (m.find()) {
            return m.group(1);//0会获取多余的内容
        } else {
            return null;
        }
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

    public static String base64Encode(byte[] byteArray){
        //https://www.cnblogs.com/alter888/p/9140732.html
        final BASE64Encoder encoder = new BASE64Encoder();
        return encoder.encode(byteArray);
    }

    public static String base64Encode(String str){
        final BASE64Encoder encoder = new BASE64Encoder();
        byte[] b = new byte[]{};
        try {
            b = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return encoder.encode(b);
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

}
