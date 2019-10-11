package entity;

import burp.IHttpService;

public class HttpServer implements IHttpService {
    private String protocol;
    private String host;
    private int port;

    public HttpServer(String protocol,String host,int port){
        this.protocol = protocol;
        this.host = host;
        this.port = port;
    }

    @Override
    public String getProtocol() {
        return protocol;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }
}
