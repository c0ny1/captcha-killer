package entity;

public class CaptchaEntity {
    private byte[] image;
    private byte[] reqRaw;
    private byte[] rsqRaw;
    private String result;

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public byte[] getReqRaw() {
        return reqRaw;
    }

    public void setReqRaw(byte[] reqRaw) {
        this.reqRaw = reqRaw;
    }

    public byte[] getRsqRaw() {
        return rsqRaw;
    }

    public void setRsqRaw(byte[] rsqRaw) {
        this.rsqRaw = rsqRaw;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
 }
