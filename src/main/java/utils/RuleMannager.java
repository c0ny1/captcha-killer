/**
 * Copyright (c) 2016-2019 c0ny1 (https://github.com/c0ny1/captcha-killer)
 * License: MIT
 */
package utils;

import burp.BurpExtender;
import entity.Rule;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static utils.Util.escapeExprSpecialWord;

/**
 * autor: c0ny1
 * date: 2019-10-14
 * description: 匹配规则管理类
 */
public class RuleMannager {
    public static String match(String str, Rule rule){
        switch (rule.getType()){
            case Rule.RULE_TYPE_RESPONSE_DATA:
                String res = new String(Util.getRspBody(str.getBytes()));
                return res;
            case Rule.RULE_TYPE_REGULAR:
                return matchByRegular(str,rule.getRule());
            case Rule.RULE_TYPE_POSISTION:
                return matchByPosistion(str,rule.getnStart(),rule.getnEnd());
            case Rule.RULE_TYPE_START_END_STRING:
                return matchByStartEndString(str,rule.getStrStart(),rule.getStrEnd());
            default:
                return "unkown rule type";
        }
    }

    public static String matchByRegular(String str,String regular){
        Pattern r = Pattern.compile(regular);
        Matcher m = r.matcher(str);
        if (m.find()) {
            return m.group(1);//0会获取多余的内容
        } else {
            return null;
        }
    }

    public static String matchByPosistion(String str,int start,int end){
        return str.substring(start,end);
    }

    public static String matchByStartEndString(String str,String start,String end){
        int nStart = str.indexOf(start) + start.length();
        int nEnd = str.indexOf(end);
        return str.substring(nStart,nEnd);
    }

    public static String generateRegular(String raw,int start,int end){
        int newStart = start;
        int newEnd = end;
        String startStr = "";
        String endStr = "";
        String target = raw.substring(start,end);
        String strReg = "";

        if(start>3){
            newStart -= 3;
        }else if(start > 1){
            newStart -= 1;
        }

        if(end<raw.length()-3){
            newEnd += 3;
        }

        for(int i=0;i<raw.length();i++){
            startStr = raw.substring(newStart,start);
            startStr = escapeExprSpecialWord(startStr);
            endStr = raw.substring(end,newEnd);
            endStr = escapeExprSpecialWord(endStr);
            strReg = String.format("%s(.*?)%s",startStr,endStr);
            System.out.println("---------------------------------------");
            System.out.println(strReg);
            BurpExtender.stderr.println("------------------------------");
            BurpExtender.stderr.println("[+] " + target);
            BurpExtender.stderr.println(strReg);

            if(matchByRegular(raw,strReg).equals(target)){
                break;
            }

            if(newStart == 0 && newEnd == raw.length()){
                break;
            }

            if(newStart>0){
                newStart -= 1;
            }
            if(newEnd<raw.length()) {
                newEnd += 1;
            }
        }
        //当匹配只有一个结果时，证明通过
        return strReg;
    }

    public static String generatePositionRule(int start,int end){
        String rule = String.format("{\"start\":%d,\"end\":%d}",start,end);
        return rule;
    }

    public static String generateStartEndRule(String raw,int start,int end){
        String keyword = raw.substring(start,end);
        int s = start;
        String strStart = "";
        int e = end;
        String strEnd = "";
        while (s >= 0){
            strStart = raw.substring(s,start);
            int startPosition = raw.indexOf(strStart) + strStart.length();
            if(startPosition == start){
                break;
            }
            s -= 1;
        }

        while (e <= raw.length()) {
            strEnd = raw.substring(end,e);
            int endPosition = raw.indexOf(strEnd);
            if(endPosition == end){
                break;
            }
            e += 1;
        }
        strStart = Util.escapeJsonString(strStart);
        strEnd = Util.escapeJsonString(strEnd);
        String rule = String.format("{\"start\":\"%s\",\"end\":\"%s\"}",strStart,strEnd);

        //验证最终生成的规则是否适用
        if(matchByStartEndString(raw,strStart,strEnd).equals(keyword)){
            return rule;
        }else{
            return "generate rule fail,try again";
        }
    }

    public static void main(String[] args) {
        String str = generateStartEndRule("absdsdsdbsdsfwewwfwfwdsdddcdesdfsdffghijkweweefewffsadfssdgslmnopqrsdsdst",12,34);
        System.out.println(str);
    }

}
