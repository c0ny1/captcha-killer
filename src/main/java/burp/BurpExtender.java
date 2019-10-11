package burp;

import ui.GUI;
import ui.Menu;

import javax.swing.*;
import java.awt.*;
import java.io.PrintWriter;

public class BurpExtender implements IBurpExtender,ITab {
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
}
