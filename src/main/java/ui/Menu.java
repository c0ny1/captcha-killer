package ui;

import burp.*;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

/**
 * 菜单类，负责显示菜单，处理菜单事件
 */
public class Menu implements IContextMenuFactory {
    public List<JMenuItem> createMenuItems(final IContextMenuInvocation invocation) {
        List<JMenuItem> menus = new ArrayList();
        JMenuItem menu = new JMenuItem("Send to captcha-killer");

        final IHttpRequestResponse iReqResp = invocation.getSelectedMessages()[0];
        IRequestInfo reqInfo = BurpExtender.helpers.analyzeRequest(iReqResp.getRequest());

        menu.addActionListener(new ActionListener(){

            public void actionPerformed(ActionEvent arg0) {
                try {
                    IHttpService httpservice = iReqResp.getHttpService();
                    String url = String.format("%s://%s:%d",httpservice.getProtocol(),httpservice.getHost(),httpservice.getPort());
                    BurpExtender.gui.getTfURL().setText(url);
                    BurpExtender.gui.getTaRequest().setText(new String(iReqResp.getRequest()));
                    BurpExtender.gui.setCaptchaReqRsp(iReqResp);
                }catch (Exception e){
                    e.printStackTrace(BurpExtender.stderr);
                }
            }
        });

        menus.add(menu);
        return menus;
    }
}
