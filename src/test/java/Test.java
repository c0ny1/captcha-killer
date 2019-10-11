import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Test {
    public static void main(String[] args) {
        String  str = "lkjhgf<a herf=\" fdsdadf, \" title=\"韩国和规范\">yfffgfg<a herf=\"gfytffjhhjg\" title=\"预付费更改\">iuiuyuytf<a herf=\"fhgytfddtr\" title=\"了解客户感觉\">uigfg";
        Pattern p = Pattern.compile("title=\"(.+?)\"");
        Matcher m = p.matcher(str);
        while(m.find()) {
            System.out.println(m.group(1));
        }
    }
}
