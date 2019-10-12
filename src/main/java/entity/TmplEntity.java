package entity;

public class TmplEntity {
    private HttpService service;
    private String reqpacke;
    private String matchRule;

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

    public String getMatchRule() {
        return matchRule;
    }

    public void setMatchRule(String matchRule) {
        this.matchRule = matchRule;
    }
}
