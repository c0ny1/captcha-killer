package ui;

import burp.BurpExtender;
import burp.IHttpRequestResponse;
import entity.CaptchaEntity;
import entity.HttpService;
import utils.HttpClient;
import utils.Util;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

public class GUI {
    private JPanel MainPanel;
    public JSplitPane spImg;
    private JSplitPane spInterface;
    private JSplitPane spOption;
    private JSplitPane spAll;

    //获取验证码面板
    private JLabel lbURL;
    private JTextField tfURL;
    private JButton btnGetCaptcha;
    private JTextArea taRequest;
    private JLabel lbCaptcha;
    private JLabel lbImage;
    private JToggleButton tlbLock;
    private JTextArea taResponse;

    //接口配置编码
    private JPanel plInterfaceReq;
    private JPopupMenu pmInterfaceMenu;
    private JTextArea taInterfaceReq;
    private JLabel lbInterfaceURL;
    private JTextField tfInterfaceURL;
    private JButton btnIdentify;
    private JPanel plInterfaceRsq;
    private JTextArea taInterfaceRsq;
    JMenu menuTmplManager = new JMenu("模版");
    JMenuItem miGeneralTmpl = new JMenuItem("通用模版");
    JMenuItem miTesseract = new JMenuItem("tesseract-ocr-web");
    JMenuItem miBaiduOCR = new JMenuItem("百度OCR");
    JMenuItem miCNNCaptcha = new JMenuItem("cnn_captcha");
    JMenuItem miImageRaw = new JMenuItem("验证码图片二进制内容标签");
    JMenuItem miBase64Encode = new JMenuItem("Base64编码标签");
    JMenuItem miURLEncode = new JMenuItem("URL编码标签");
    private JLabel lbRegular;
    private JTextField tfRegular;
    private JButton btnSaveTmpl;

    //识别结果面板
    private JPanel plResult;
    private JTable table;
    private JScrollPane spTable;
    private TableModel model;

    //一些公共变量
    private byte[] byteImg = new byte[]{};
    public static final List<CaptchaEntity> captcha = new ArrayList<CaptchaEntity>();


    public JTextField getTfURL(){
        return tfURL;
    }

    public JTextArea getTaRequest(){
        return taRequest;
    }

    public JTextArea getTaResponse(){
        return taResponse;
    }

    public JButton getBtnGetCaptcha(){
        return this.btnGetCaptcha;
    }

    public JTable getTable(){
        return table;
    }

    public TableModel getModel(){
        return this.model;
    }

    public String getCaptchaURL(){
        return this.tfURL.getText();
    }

    public String getCaptchaReqRaw(){
        return this.taRequest.getText();
    }

    public JTextField getInterfaceURL(){
        return this.tfInterfaceURL;
    }

    public JTextArea getInterfaceReqRaw(){
        return this.taInterfaceReq;
    }

    public JTextField getRegular(){
        return this.tfRegular;
    }

    public GUI(){
        MainPanel = new JPanel();
        MainPanel.setBorder(new EmptyBorder(2, 2, 2, 2));
        MainPanel.setLayout(new BorderLayout(0, 0));

        //图片获取面板
        lbURL = new JLabel("验证码URL:");
        tfURL = new JTextField(30);
        btnGetCaptcha = new JButton("获取");
        btnGetCaptcha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GetImageThread thread = new GetImageThread(tfURL.getText(),taRequest.getText());
                thread.start();
                btnGetCaptcha.setEnabled(false);
            }
        });
        taRequest = new JTextArea();
        taRequest.setLineWrap(true);
        taRequest.setWrapStyleWord(true);//断行不断字
        JScrollPane spRequest = new JScrollPane(taRequest);


        JPanel imgLeftPanel = new JPanel();
        imgLeftPanel.setLayout(new GridBagLayout());
        GBC gbc_lburl = new GBC(0,0,1,1).setFill(GBC.HORIZONTAL).setInsets(3,3,0,0);
        GBC gbc_tfurl = new GBC(1,0,1,1).setFill(GBC.HORIZONTAL).setWeight(100,1).setInsets(3,3,0,0);
        GBC gbc_btngetcaptcha = new GBC(2,0,11,1).setInsets(3,3,0,3);
        GBC gbc_tarequst = new GBC(0,1,100,100).setFill(GBC.BOTH).setWeight(100,100).setInsets(3,3,3,3);
        imgLeftPanel.add(lbURL,gbc_lburl);
        imgLeftPanel.add(tfURL,gbc_tfurl);
        imgLeftPanel.add(btnGetCaptcha,gbc_btngetcaptcha);
        imgLeftPanel.add(spRequest,gbc_tarequst);

        JPanel imgRigthPanel = new JPanel();
        imgRigthPanel.setLayout(new GridBagLayout());
        lbImage = new JLabel("");
        lbCaptcha = new JLabel("验证码:");
        tlbLock = new JToggleButton("锁定");
        tlbLock.setToolTipText("当配置好所有选项后，请锁定防止配置被改动！");
        tlbLock.addChangeListener(new ChangeListener(){
            public void stateChanged(ChangeEvent e) {
                boolean isSelected = tlbLock.isSelected();
                if(isSelected){
                    tlbLock.setText("解锁");
                    tfURL.setEnabled(false);
                    taRequest.setEnabled(false);
                    btnGetCaptcha.setEnabled(false);
                    taResponse.setEnabled(false);
                    tfInterfaceURL.setEnabled(false);
                    btnIdentify.setEnabled(false);
                    taInterfaceReq.setEnabled(false);
                    tfRegular.setEnabled(false);
                    btnSaveTmpl.setEnabled(false);
                    taInterfaceRsq.setEnabled(false);
                }else{
                    tlbLock.setText("锁定");
                    tfURL.setEnabled(true);
                    taRequest.setEnabled(true);
                    btnGetCaptcha.setEnabled(true);
                    taResponse.setEnabled(true);
                    tfInterfaceURL.setEnabled(true);
                    btnIdentify.setEnabled(true);
                    taInterfaceReq.setEnabled(true);
                    tfRegular.setEnabled(true);
                    btnSaveTmpl.setEnabled(true);
                    taInterfaceRsq.setEnabled(true);
                }
                tlbLock.setSelected(isSelected);
            }
        });
        taResponse = new JTextArea();
        taResponse.setLineWrap(true);
        taResponse.setWrapStyleWord(true);//断行不断字
        taResponse.setEditable(false);
        JScrollPane spResponse = new JScrollPane(taResponse);

        GBC gbc_lbcaptcha = new GBC(0,0,1,1).setFill(GBC.BOTH).setInsets(3,3,0,0);
        GBC gbc_lbimage = new GBC(1,0,1,100).setFill(GBC.BOTH).setWeight(100,1).setInsets(3,3,0,0);
        GBC gbc_tlblock = new GBC(2,0,1,1).setFill(GBC.BOTH).setInsets(3,3,0,3);
        GBC gbc_taresponse = new GBC(0,100,100,100).setFill(GBC.BOTH).setWeight(100,100).setInsets(3,3,3,3);

        imgRigthPanel.add(lbCaptcha,gbc_lbcaptcha);
        imgRigthPanel.add(lbImage,gbc_lbimage);
        imgRigthPanel.add(tlbLock,gbc_tlblock);
        imgRigthPanel.add(spResponse,gbc_taresponse);
        spImg = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        spImg.setResizeWeight(0.5);
        spImg.setLeftComponent(imgLeftPanel);
        spImg.setRightComponent(imgRigthPanel);

        // 识别接口配置面板
        spInterface = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        spInterface.setResizeWeight(0.5);
        plInterfaceReq = new JPanel();
        plInterfaceReq.setLayout(new GridBagLayout());

        lbInterfaceURL = new JLabel("接口URL:");
        tfInterfaceURL = new JTextField(30);
        btnIdentify = new JButton("识别");
        btnIdentify.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(byteImg == null){
                    JOptionPane.showMessageDialog(null,"请先获取要识别的图片","captcha-killer alert",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(tfInterfaceURL.getText().trim() == null || tfInterfaceURL.getText().trim().equals("")){
                    JOptionPane.showMessageDialog(null,"请设置好接口的url","captcha-killer alert",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(taInterfaceReq.getText().trim() == null|| taInterfaceReq.getText().trim().equals("")){
                    JOptionPane.showMessageDialog(null,"请设置好接口的请求数据包","captcha-killer alert",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if(tfRegular.getText().trim() == null|| tfRegular.getText().trim().equals("")){
                    JOptionPane.showMessageDialog(null,"请设置好匹配结果的正则","captcha-killer alert",JOptionPane.WARNING_MESSAGE);
                    return;
                }

                IdentifyCaptchaThread thread = new IdentifyCaptchaThread(tfInterfaceURL.getText(),taInterfaceReq.getText(),byteImg);
                thread.start();
                taInterfaceRsq.setText("");
                //btnIdentify.setEnabled(false);
            }
        });
        taInterfaceReq = new JTextArea();
        taInterfaceReq.setLineWrap(true);
        taInterfaceReq.setWrapStyleWord(true);
        JScrollPane spInterfaceReq = new JScrollPane(taInterfaceReq);
        pmInterfaceMenu = new JPopupMenu();
        menuTmplManager.add(miGeneralTmpl);
        menuTmplManager.add(miTesseract);
        menuTmplManager.add(miBaiduOCR);
        menuTmplManager.add(miCNNCaptcha);
        pmInterfaceMenu.add(menuTmplManager);
        pmInterfaceMenu.addSeparator();
        pmInterfaceMenu.add(miImageRaw);
        pmInterfaceMenu.add(miBase64Encode);
        pmInterfaceMenu.add(miURLEncode);
        miGeneralTmpl.addActionListener(new MenuActionManger());
        miTesseract.addActionListener(new MenuActionManger());
        miBaiduOCR.addActionListener(new MenuActionManger());
        miCNNCaptcha.addActionListener(new MenuActionManger());
        menuTmplManager.addActionListener(new MenuActionManger());
        miImageRaw.addActionListener(new MenuActionManger());
        miBase64Encode.addActionListener(new MenuActionManger());
        miURLEncode.addActionListener(new MenuActionManger());



        taInterfaceReq.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                    pmInterfaceMenu.show(taInterfaceReq, e.getX(), e.getY());
                }
            }
        });


        GBC gbc_lbinterfaceurl = new GBC(1,0,1,1).setFill(GBC.HORIZONTAL).setInsets(3,3,0,0);
        GBC gbc_tfinterfaceurl = new GBC(2,0,1,1).setFill(GBC.HORIZONTAL).setInsets(3,3,0,0).setWeight(100,1);
        GBC gbc_btnidentify = new GBC(3,0,1,1).setFill(GBC.HORIZONTAL).setInsets(3,3,0,3);
        GBC gbc_tpinterfacereq = new GBC(0,1,100,100).setFill(GBC.BOTH).setWeight(100,100).setInsets(3,3,3,3);
        plInterfaceReq.add(lbInterfaceURL,gbc_lbinterfaceurl);
        plInterfaceReq.add(tfInterfaceURL,gbc_tfinterfaceurl);
        plInterfaceReq.add(btnIdentify,gbc_btnidentify);
        plInterfaceReq.add(spInterfaceReq,gbc_tpinterfacereq);

        plInterfaceRsq = new JPanel();
        plInterfaceRsq.setLayout(new GridBagLayout());
        lbRegular = new JLabel("匹配正则:");
        tfRegular = new JTextField(30);
        btnSaveTmpl = new JButton("匹配");
        taInterfaceRsq = new JTextArea();
        taInterfaceRsq.setLineWrap(true);
        taInterfaceRsq.setWrapStyleWord(true);
        taInterfaceRsq.setEditable(false);
        JScrollPane spInterfaceRsq = new JScrollPane(taInterfaceRsq);
        GBC gbc_lbregular = new GBC(0,0,1,1).setFill(GBC.HORIZONTAL).setInsets(3,3,0,0);
        GBC gbc_tfregular = new GBC(1,0,1,1).setFill(GBC.HORIZONTAL).setInsets(3,3,0,0).setWeight(100,1);
        GBC gbc_btnsavetmpl = new GBC(2,0,1,1).setFill(GBC.HORIZONTAL).setInsets(3,3,0,3);
        GBC gbc_tpinterfacersq = new GBC(0,1,100,100).setFill(GBC.BOTH).setWeight(100,100).setInsets(3,3,3,3);
        plInterfaceRsq.add(lbRegular,gbc_lbregular);
        plInterfaceRsq.add(tfRegular,gbc_tfregular);
        plInterfaceRsq.add(btnSaveTmpl,gbc_btnsavetmpl);
        plInterfaceRsq.add(spInterfaceRsq,gbc_tpinterfacersq);
        spInterface.setLeftComponent(plInterfaceReq);
        spInterface.setRightComponent(plInterfaceRsq);

        //识别结果面板
        plResult = new JPanel();
        final JPopupMenu pppMenu = new JPopupMenu();
        JMenuItem miClear = new JMenuItem("清空");
        miClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                synchronized (captcha){
                    captcha.clear();
                    model.fireTableDataChanged();
                }
            }
        });
        pppMenu.add(miClear);

        table = new JTable();
        model = new TableModel(table);
        table.setModel(model);
        table.setAutoCreateRowSorter(true);
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == java.awt.event.MouseEvent.BUTTON3) {
                    pppMenu.show(table, e.getX(), e.getY());
                }
            }
        });
        spTable = new JScrollPane(table);
        plResult.setLayout(new GridBagLayout());
        GBC gbc_taresult = new GBC(0,0,100,100).setFill(GBC.BOTH).setWeight(100,100).setInsets(3,3,3,3);
        plResult.add(spTable,gbc_taresult);

        //面板合并
        spOption = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        spOption.setResizeWeight(0.5);
        spOption.setTopComponent(spImg);
        spOption.setBottomComponent(spInterface);
        spAll = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        spAll.setResizeWeight(0.85);
        spAll.setLeftComponent(spOption);
        spAll.setRightComponent(plResult);

        MainPanel.add(spAll);
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                BurpExtender.callbacks.customizeUiComponent(spAll);
                BurpExtender.callbacks.customizeUiComponent(MainPanel);
            }
        });
    }

    public static byte[] requestImage(String url,String raw){
        if(Util.isURL(url)) {
            HttpClient http = new HttpClient(url, raw, null);
            byte[] rsp = http.doReust();
            BurpExtender.gui.getTaResponse().setText(new String(rsp));
            int BodyOffset = BurpExtender.helpers.analyzeResponse(rsp).getBodyOffset();
            int body_length = rsp.length - BodyOffset;
            byte[] byteImg = Util.subBytes(rsp, BodyOffset, body_length);
            return byteImg;
        }else{
            BurpExtender.stderr.println("[-] captcha URL format invalid");
            return null;
        }
    }

    public class GetImageThread extends Thread {
        private String url;
        private String raw;

        public GetImageThread(String url,String raw) {
            this.url = url;
            this.raw = raw;
        }

        public void run() {
            if(!Util.isURL(url)){
                JOptionPane.showMessageDialog(null,"验证码URL不合法！","captcha-killer提示",JOptionPane.WARNING_MESSAGE);
                return;
            }
            //清洗验证码URL
            HttpService service = new HttpService(url);
            tfURL.setText(service.toString());

            try {
                byteImg = requestImage(url,raw);
                ImageIcon icon = Util.byte2img(byteImg);
                lbImage.setIcon(icon);
                lbImage.setText("");
            } catch (Exception e) {
                BurpExtender.stderr.println(e.getMessage());
            }finally {
                BurpExtender.gui.getBtnGetCaptcha().setEnabled(true);
            }
        }
    }

    public static String identifyCaptcha(String url,String raw,byte[] byteImg,String pattern){
        if(Util.isURL(url)) {
            HttpClient http = new HttpClient(url, raw, byteImg);
            byte[] rsp = http.doReust();
            String rspRaw = new String(rsp);
            String res = Util.match(rspRaw, pattern);
            //排查请求速度过快可能会导致
            BurpExtender.stdout.println("---------------------------------------------");
            BurpExtender.stdout.println(rspRaw);
            BurpExtender.stdout.println("[+] res = " + res);
            return res;
        }else{
            return "Interface URL format invalid";
        }
    }

    public class IdentifyCaptchaThread extends Thread{
        private String url;
        private String raw;
        private byte[] img;
        private String pattern;

        public IdentifyCaptchaThread(String url,String raw,byte[] img){
            this.url = url;
            this.raw = raw;
            this.img = img;
            this.pattern = tfRegular.getText();
        }

        @Override
        public void run() {
            if(!Util.isURL(url)){
                JOptionPane.showMessageDialog(null,"接口URL不合法！","captcha-killer提示",JOptionPane.WARNING_MESSAGE);
                return;
            }
            //清洗接口URL
            HttpService service = new HttpService(url);
            tfInterfaceURL.setText(service.toString());

            HttpClient http = new HttpClient(url,raw,byteImg);
            byte[] rsp = http.doReust();
            String rspRaw = new String(rsp);
            taInterfaceRsq.setText(rspRaw);
            btnIdentify.setEnabled(true);

            String res = Util.match(rspRaw,pattern);
            CaptchaEntity cap = new CaptchaEntity();
            cap.setImage(img);
            cap.setResult(res);

            synchronized (captcha){
                int row = captcha.size();
                captcha.add(cap);
                model.fireTableRowsInserted(row,row);
            }
        }
    }


    public class MenuActionManger implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            try {
                if (e.getSource() == miBaiduOCR) {
                    tfInterfaceURL.setText("https://aip.baidubce.com:443");
                    taInterfaceReq.setText("POST /rest/2.0/ocr/v1/accurate?access_token=24.77f0182dc1c96e633010712ab483f123.2592000.1573295509.282335-17479921 HTTP/1.1\n" +
                            "Host: aip.baidubce.com\n" +
                            "Connection: close\n" +
                            "Cache-Control: max-age=0\n" +
                            "Upgrade-Insecure-Requests: 1\n" +
                            "User-Agent: Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36\n" +
                            "Sec-Fetch-Mode: navigate\n" +
                            "Sec-Fetch-User: ?1\n" +
                            "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3\n" +
                            "Sec-Fetch-Site: none\n" +
                            "Accept-Encoding: gzip, deflate\n" +
                            "Accept-Language: zh-CN,zh;q=0.9\n" +
                            "Content-Type: application/x-www-form-urlencoded\n" +
                            "Content-Length: 55\n" +
                            "\n" +
                            "image=<urlencode><base64>{IMG_RAW}</base64></urlencode>");
                    tfRegular.setText("\"words\"\\: \"(.*?)\"\\}");
                }else if(e.getSource() == miImageRaw){
                    int n = taInterfaceReq.getSelectionStart();
                    taInterfaceReq.insert("{IMG_RAW}",n);
                }else if(e.getSource() == miBase64Encode){
                    int start = taInterfaceReq.getSelectionStart();
                    int end = taInterfaceReq.getSelectionEnd();
                    String newStr = String.format("<base64>%s</base64>",taInterfaceReq.getSelectedText());
                    StringBuffer sbRaw = new StringBuffer(taInterfaceReq.getText());
                    sbRaw.replace(start,end,newStr);
                    taInterfaceReq.setText(sbRaw.toString());
                }else if(e.getSource() == miURLEncode){
                    int start = taInterfaceReq.getSelectionStart();
                    int end = taInterfaceReq.getSelectionEnd();
                    String newStr = String.format("<URLEncode>%s</URLEncode>",taInterfaceReq.getSelectedText());
                    StringBuffer sbRaw = new StringBuffer(taInterfaceReq.getText());
                    sbRaw.replace(start,end,newStr);
                    taInterfaceReq.setText(sbRaw.toString());
                }

            } catch (Exception ex) {
            }
        }
    }
    public Component getComponet(){
        return MainPanel;
    }
}

