package entity;

public class TmplEntity {
    private HttpService service;
    private String reqpacke;
    private Rule rule;

    public HttpService getService() {
        return service;
    }

    public void setService(HttpService service) {
        this.service = service;
    }

    public String getReqpacke() {
        return reqpacke;
    }

    public void setReqpacke(String reqpacke) {
        this.reqpacke = reqpacke;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }
}
