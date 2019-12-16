/**
 * Copyright (c) 2019 c0ny1 (https://github.com/c0ny1/captcha-killer)
 * License: MIT
 */
package utils;

import burp.BurpExtender;
import burp.IHttpRequestResponse;
import burp.IRequestInfo;
import entity.HttpService;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {
    private String url;
    private String protocol;
    private String method;
    private String httpversion;
    private String host;
    private int port;
    private String path;
    private HttpService service;
    private Map<String,String> headers = new HashMap<String, String>();
    private String data;
    private String raw;
    private byte[] byteImg;

    public HttpClient(String url,String raw,byte[] byteImg){
        this.url = url;
        this.raw = raw;
        this.byteImg = byteImg;
        //解析标签
        parseLabel();
        //解析Request各个属性
        parserRequest();
        //更新Content-Length
        updateContentLength();
    }

    public String getHttpService(){
        return service.toString();
    }

    public String getRaw(){
        return this.raw;
    }


    /**
     * 解析标签，可以参考下
     */
    public void parseLabel(){
        LableParser parser = new LableParser(byteImg);
        raw = parser.parseAllLable(raw);
    }

    private void parserRequest(){
        if(Util.isURL(this.url)){
            service = new HttpService(this.url);
            try {
                IRequestInfo requestInfo = BurpExtender.helpers.analyzeRequest(service, this.raw.getBytes());
                requestInfo.getBodyOffset();
                this.method = requestInfo.getMethod();
                for (String header : requestInfo.getHeaders()) {
                    if (header.indexOf(this.method) >= 0 && header.indexOf("HTTP/") >= 0) {
                        this.path = header.split(" ")[1];
                        this.httpversion = header.split(" ")[2];
                        continue;
                    }

                    if (header.indexOf(": ") > 0) {
                        String key = header.split(": ")[0];
                        String value = header.split(": ")[1];
                        this.headers.put(key, value);
                    }
                    System.out.println(header);
                }

                if (this.method.equals("POST")) {
                    this.data = this.raw.substring(requestInfo.getBodyOffset(), this.raw.length());
                }
            }catch (Exception e){
                BurpExtender.stderr.println(e.getMessage());
            }
        }
    }

    private void parserRequestOld(){
        if(Util.isURL(this.url)){
            service = new HttpService(this.url);
            String[] rawsArray = this.raw.split(System.lineSeparator());
            try {
                for (int i = 0; i < rawsArray.length; i++) {
                    if (i == 0) {
                        this.method = rawsArray[0].split(" ")[0];
                        this.path = rawsArray[0].split(" ")[1];
                        this.httpversion = rawsArray[0].split(" ")[2];
                    } else if (this.method.equals("POST") && i == rawsArray.length - 1) {
                        this.data = rawsArray[i].trim();
                    } else {
                        if (rawsArray[i].indexOf(": ") > 0) {
                            String key = rawsArray[i].split(": ")[0];
                            String value = rawsArray[i].split(": ")[1];
                            this.headers.put(key.trim(), value.trim());
                        }
                    }
                }
            }catch (Exception e){
                BurpExtender.stdout.println(e.getMessage());
            }
        }
    }





    /**
     * 更新请求包的Content-Length头
     * 注意：不更新该头部，可能会导致服务端无法获取完整的请求信息。
     * @return
     */
    public void updateContentLength(){
        /**
         * 在处理GET数据包时,要注意包结果严格来讲最后要有两个\r\n。有的web服务器对数据包要求比较严格，可能会导致请求识别。
         * 该问题曾出现在请求某网站的验证码时，返回了403状态。
         */
        if(method.equals("POST")) {
            int length = data.length();
            headers.put("Content-Length", String.valueOf(length));
            String reqLine = String.format("%s %s %s",method,path,httpversion);
            reqLine += System.lineSeparator();
            for(Map.Entry<String,String> header:headers.entrySet()){
                String line = String.format("%s: %s",header.getKey(),header.getValue());
                reqLine += line;
                reqLine += System.lineSeparator();
            }

            reqLine += System.lineSeparator();
            reqLine += data;
            this.raw = reqLine;
        }
    }


    public byte[] doReust(){
        byte[] req = raw.getBytes();
        try {
            IHttpRequestResponse reqrsp = BurpExtender.callbacks.makeHttpRequest(service, req);
            byte[] response = reqrsp.getResponse();
            return response;
        }catch (Exception e){
            e.printStackTrace();
            BurpExtender.stderr.println(e);
        }
        return null;

    }
}
