package burp;

import entity.CaptchaEntity;
import ui.GUI;
import ui.Menu;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;

public class BurpExtender implements IBurpExtender,ITab,IIntruderPayloadGeneratorFactory, IIntruderPayloadGenerator{
    public static IBurpExtenderCallbacks callbacks;
    public static IExtensionHelpers helpers;
    private String extensionName = "captcha-killer";
    private String version ="0.1";
    public static PrintWriter stdout;
    public static PrintWriter stderr;
    public static GUI gui;

    @Override
    public void registerExtenderCallbacks(IBurpExtenderCallbacks calllbacks) {
        this.callbacks = calllbacks;
        this.helpers = calllbacks.getHelpers();
        this.stdout = new PrintWriter(calllbacks.getStdout(),true);
        this.stderr = new PrintWriter(calllbacks.getStderr(),true);
        gui = new GUI();
        callbacks.setExtensionName(String.format("%s %s",extensionName,version));
        calllbacks.registerContextMenuFactory(new Menu());
        calllbacks.registerIntruderPayloadGeneratorFactory(this);

        stdout = new PrintWriter(callbacks.getStdout(),true);
        stderr = new PrintWriter(callbacks.getStderr(),true);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BurpExtender.this.callbacks.addSuiteTab(BurpExtender.this);
            }
        });
        stdout.println(getBanner());

    }

    /**
     * 插件Banner信息
     * @return
     */
    public String getBanner(){
        String bannerInfo =
                "[+]\n"
                        + "[+] ##############################################\n"
                        + "[+]    " + extensionName + " v" + version +"\n"
                        + "[+]    anthor: c0ny1\n"
                        + "[+]    email:  root@gv7.me\n"
                        + "[+]    github: http://github.com/c0ny1/captcha-killer\n"
                        + "[+] ##############################################";
        return bannerInfo;
    }

    @Override
    public String getTabCaption() {
        return extensionName;
    }

    @Override
    public Component getUiComponent() {
        return gui.getComponet();
    }

    @Override
    public boolean hasMorePayloads() {
        return true;
    }

    @Override
    public byte[] getNextPayload(byte[] bytes) {
        String result = "";
        int count = 0;
        try {
            byte[] byteImg = GUI.requestImage(gui.getCaptchaURL(),gui.getCaptchaReqRaw());
            //遗留问题：burp自带的发包，无法指定超时。如果访问速度过快，这里可能为空。
            while (count < 3){
                result = GUI.identifyCaptcha(gui.getInterfaceURL().getText(),gui.getInterfaceReqRaw().getText(),byteImg,gui.getRegular().getText());
                if(result == null || result.trim().equals("")){
                    Thread.sleep(1000);
                    count += 1;
                }else{
                    break;
                }
            }
            CaptchaEntity cap = new CaptchaEntity();
            cap.setImage(byteImg);
            cap.setResult(result);
            synchronized (gui.captcha){
                int row = gui.captcha.size();
                gui.captcha.add(cap);
                gui.getModel().fireTableRowsInserted(row,row);
            }
        } catch (Exception e) {
            result = e.getMessage();
        }



        return result.getBytes();
    }

    @Override
    public void reset() {

    }

    @Override
    public String getGeneratorName() {
        return this.extensionName;
    }

    @Override
    public IIntruderPayloadGenerator createNewInstance(IIntruderAttack iIntruderAttack) {
        return this;
    }
}
