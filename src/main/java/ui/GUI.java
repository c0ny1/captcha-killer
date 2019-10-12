package ui;

import burp.BurpExtender;
import burp.IHttpRequestResponse;
import entity.CaptchaEntity;
import utils.HttpClient;
import utils.Util;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private JLabel lbImage;
    private JTextArea taResponse;

    //接口配置编码
    private JPanel plInterfaceReq;
    private JTextArea taInterfaceReq;
    private JLabel lbInterfaceURL;
    private JTextField tfInterfaceURL;
    private JButton btnIdentify;
    private JPanel plInterfaceRsq;
    private JTextArea taInterfaceRsq;
    private JLabel lbRegular;
    private JTextField tfRegular;
    private JButton btnSaveTmpl;

    //识别结果面板
    private JPanel plResult;
    private JLabel lbResultTitle;
    private JButton btnClear;
    private JTable table;
    private JScrollPane spTable;
    private TableModel model;

    //一些公共变量
    private IHttpRequestResponse captchaReqRsp;
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

    public void setCaptchaReqRsp(IHttpRequestResponse captchaReqRsp){
        this.captchaReqRsp = captchaReqRsp;
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

    public String getInterfaceURL(){
        return this.tfInterfaceURL.getText();
    }

    public String getInterfaceReqRaw(){
        return this.taInterfaceReq.getText();
    }

    public String getRegular(){
        return this.tfRegular.getText();
    }

    public GUI(){
        MainPanel = new JPanel();
        MainPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
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
        GBC gbc_lburl = new GBC(0,0,1,1).setFill(GBC.HORIZONTAL).setInsets(5,5,0,0);
        GBC gbc_tfurl = new GBC(1,0,1,1).setFill(GBC.HORIZONTAL).setWeight(100,1).setInsets(5,5,0,0);
        GBC gbc_btngetcaptcha = new GBC(2,0,11,1).setInsets(5,5,0,5);
        GBC gbc_tarequst = new GBC(0,1,100,100).setFill(GBC.BOTH).setWeight(100,100).setInsets(5,5,5,5);
        imgLeftPanel.add(lbURL,gbc_lburl);
        imgLeftPanel.add(tfURL,gbc_tfurl);
        imgLeftPanel.add(btnGetCaptcha,gbc_btngetcaptcha);
        imgLeftPanel.add(spRequest,gbc_tarequst);

        JPanel imgRigthPanel = new JPanel();
        imgRigthPanel.setLayout(new GridBagLayout());
        taResponse = new JTextArea();
        taResponse.setLineWrap(true);
        taResponse.setWrapStyleWord(true);//断行不断字
        JScrollPane spResponse = new JScrollPane(taResponse);
        lbImage = new JLabel("");
        GBC gbc_lbimage1 = new GBC(0,0,1,1).setFill(GBC.BOTH).setInsets(5,5,0,0);
        GBC gbc_lbimage2 = new GBC(1,0,1,1).setFill(GBC.BOTH).setWeight(100,1).setInsets(5,5,0,0);
        GBC gbc_lbimage3 = new GBC(2,0,1,1).setFill(GBC.BOTH).setInsets(5,5,0,5);
        GBC gbc_taresponse = new GBC(0,2,100,100).setFill(GBC.BOTH).setWeight(100,100).setInsets(5,5,5,5);
        imgRigthPanel.add(new JLabel("验证码:"),gbc_lbimage1);
        imgRigthPanel.add(lbImage,gbc_lbimage2);
        imgRigthPanel.add(new JLabel(""),gbc_lbimage3);
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
        GBC gbc_lbinterfaceurl = new GBC(1,0,1,1).setFill(GBC.HORIZONTAL).setInsets(5,5,0,0);
        GBC gbc_tfinterfaceurl = new GBC(2,0,1,1).setFill(GBC.HORIZONTAL).setInsets(5,5,0,0).setWeight(100,1);
        GBC gbc_btnidentify = new GBC(3,0,1,1).setFill(GBC.HORIZONTAL).setInsets(5,5,0,5);
        GBC gbc_tpinterfacereq = new GBC(0,1,100,100).setFill(GBC.BOTH).setWeight(100,100).setInsets(5,5,5,5);
        plInterfaceReq.add(lbInterfaceURL,gbc_lbinterfaceurl);
        plInterfaceReq.add(tfInterfaceURL,gbc_tfinterfaceurl);
        plInterfaceReq.add(btnIdentify,gbc_btnidentify);
        plInterfaceReq.add(spInterfaceReq,gbc_tpinterfacereq);

        plInterfaceRsq = new JPanel();
        plInterfaceRsq.setLayout(new GridBagLayout());
        lbRegular = new JLabel("匹配正则:");
        tfRegular = new JTextField(30);
        btnSaveTmpl = new JButton("识别");
        taInterfaceRsq = new JTextArea();
        taInterfaceRsq.setLineWrap(true);
        taInterfaceRsq.setWrapStyleWord(true);
        JScrollPane spInterfaceRsq = new JScrollPane(taInterfaceRsq);
        GBC gbc_lbregular = new GBC(0,0,1,1).setFill(GBC.HORIZONTAL).setInsets(5,5,0,0);
        GBC gbc_tfregular = new GBC(1,0,1,1).setFill(GBC.HORIZONTAL).setInsets(5,5,0,0).setWeight(100,1);
        GBC gbc_btnsavetmpl = new GBC(2,0,1,1).setFill(GBC.HORIZONTAL).setInsets(5,5,0,5);
        GBC gbc_tpinterfacersq = new GBC(0,1,100,100).setFill(GBC.BOTH).setWeight(100,100).setInsets(5,5,5,5);
        plInterfaceRsq.add(lbRegular,gbc_lbregular);
        plInterfaceRsq.add(tfRegular,gbc_tfregular);
        plInterfaceRsq.add(btnSaveTmpl,gbc_btnsavetmpl);
        plInterfaceRsq.add(spInterfaceRsq,gbc_tpinterfacersq);
        spInterface.setLeftComponent(plInterfaceReq);
        spInterface.setRightComponent(plInterfaceRsq);

        //识别结果面板
        plResult = new JPanel();
        lbResultTitle = new JLabel("识别结果");
        btnClear = new JButton("清空");
        btnClear.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                synchronized (captcha){
                    captcha.clear();
                    model.fireTableDataChanged();
                }
            }
        });
        table = new JTable();
        model = new TableModel(table);
        table.setModel(model);
        table.setAutoCreateRowSorter(true);
        spTable = new JScrollPane(table);
        plResult.setLayout(new GridBagLayout());
        GBC gbc_lbresulttitle = new GBC(0,0,1,1).setFill(GBC.HORIZONTAL).setInsets(5,5,0,0);
        GBC gbc_btnclear = new GBC(1,0,1,1).setFill(GBC.HORIZONTAL).setInsets(5,5,0,5);
        GBC gbc_taresult = new GBC(0,1,100,100).setFill(GBC.BOTH).setWeight(100,100).setInsets(5,5,5,5);
        plResult.add(lbResultTitle,gbc_lbresulttitle);
        plResult.add(btnClear,gbc_btnclear);
        plResult.add(spTable,gbc_taresult);

        //面板合并
        spOption = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
        spOption.setResizeWeight(0.5);
        spOption.setTopComponent(spImg);
        spOption.setBottomComponent(spInterface);
        spAll = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
        spAll.setResizeWeight(0.90);
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
        HttpClient http = new HttpClient(url,raw,null);
        byte[] rsp = http.doReust();
        BurpExtender.gui.getTaResponse().setText(new String(rsp));
        int BodyOffset = BurpExtender.helpers.analyzeResponse(rsp).getBodyOffset();
        int body_length = rsp.length -BodyOffset;
        byte[] byteImg = Util.subBytes(rsp,BodyOffset,body_length);
        return byteImg;
    }

    public class GetImageThread extends Thread {
        private String url;
        private String raw;

        public GetImageThread(String url,String raw) {
            this.url = url;
            this.raw = raw;
        }

        public void run() {
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
        HttpClient http = new HttpClient(url,raw,byteImg);
        byte[] rsp = http.doReust();
        String rspRaw = new String(rsp);
        String res = Util.match(rspRaw,pattern);
        //排查请求速度过快可能会导致
        BurpExtender.stdout.println("---------------------------------------------");
        BurpExtender.stdout.println(rspRaw);
        BurpExtender.stdout.println("[+] res = " + res);
        return res;
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
            HttpClient http = new HttpClient(url,raw,byteImg);
            String service = http.getHttpService();
            tfInterfaceURL.setText(service);
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


    public Component getComponet(){
        return MainPanel;
    }
}

