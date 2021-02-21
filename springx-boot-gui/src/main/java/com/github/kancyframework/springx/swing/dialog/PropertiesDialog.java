package com.github.kancyframework.springx.swing.dialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author kancy
 */
public class PropertiesDialog extends JDialog {

    private JTable table;

    private final Map<?, ?> properties;

    public PropertiesDialog(Window owner, Map<?, ?> properties) {
        super(owner);
        this.properties = properties;
        initComponents();
        initTableData();
    }

    private void initComponents() {
        JScrollPane scrollPane = new JScrollPane();
        table = new JTable();
        setTitle(getDialogTitle());
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());
        scrollPane.setViewportView(table);
        contentPane.add(scrollPane, BorderLayout.CENTER);
        pack();
        setSize(900, 600);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(getOwner());
    }

    protected String getDialogTitle() {
        return "\u5c5e\u6027\u9762\u677f";
    }

    private void initTableData() {
        Map<String, String> map = new TreeMap(properties);
        Object[][] data = new Object[map.size()][2];
        AtomicInteger index = new AtomicInteger(0);
        map.forEach((k,v) ->{
            int row = index.getAndIncrement();
            data[row][0] = k;
            data[row][1] = v;
        });
        table.setModel(new DefaultTableModel(data, new String[] {"\u952e", "\u503c"}){
            boolean[] columnEditable = new boolean[] {
                    false, false
            };
            @Override
            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return columnEditable[columnIndex];
            }
        });
    }

    public Map<?, ?> getProperties() {
        return properties;
    }

    protected JTable getTable() {
        return table;
    }
}
