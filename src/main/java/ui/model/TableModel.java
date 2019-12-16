/**
 * Copyright (c) 2019 c0ny1 (https://github.com/c0ny1/captcha-killer)
 * License: MIT
 */
package ui.model;

import entity.CaptchaEntity;
import ui.GUI;

import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;
import static utils.Util.byte2img;

public class TableModel extends AbstractTableModel {
    private List<String> title = new ArrayList<String>();
    private JTable table;
    public TableModel(JTable table){
        this.table = table;
        title.add(0,"验证码");
        title.add(1,"识别结果");
    }

    public String getColumnName(int column) {
        // TODO Auto-generated method stub
        return title.get(column);
    }

    @Override
    public int getRowCount() {
        return GUI.captcha.size();
    }

    @Override
    public int getColumnCount() {
        return title.size();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex)
    {
        if(columnIndex == 0){
            return Icon.class;
        }
        return String.class;
    }

    @Override
    public Object getValueAt(int row, int column) {
        CaptchaEntity captcha = GUI.captcha.get(row);
        switch (column) {
            case 0:
                ImageIcon icon = byte2img(captcha.getImage());
                table.setRowHeight(row,icon.getIconHeight()+5);//让行高自动适应图片高
                return icon;
            case 1:
                return captcha.getResult();
            default:
                return "";
        }
    }
}
