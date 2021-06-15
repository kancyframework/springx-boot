/*
 * Created by JFormDesigner on Mon Jun 14 08:24:20 CST 2021
 */

package com.kancy.imageconvertor.ui;

import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.filechooser.SimpleFileDialog;
import com.github.kancyframework.springx.swing.utils.DropTargetUtils;
import com.github.kancyframework.springx.swing.utils.ImageUtils;
import lombok.Data;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.List;
import java.util.function.Function;

/**
 * @author kancy
 */
@Data
@Component
public class ImageConvertor extends JFrame {
    public ImageConvertor() {
        initComponents();
        init();
    }

    private void init() {
        DropTargetUtils.addJavaFileDropTarget(new Function<List<File>, Boolean>() {
            @Override
            public Boolean apply(List<File> files) {
                File selectedFile = files.get(0);
                return setLabelImage(selectedFile);
            }
        }, label);
        label.addMouseListener(new MouseAdapter() {
            /**
             * {@inheritDoc}
             * @param e
             */
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2 && e.getButton() == 1){
                    SimpleFileDialog dialog = new SimpleFileDialog();
                    dialog.showOpenDialog(fileDialog -> {
                        setLabelImage(fileDialog.getSelectedFile());
                    });
                }
            }
        });
    }

    public Boolean setLabelImage(File selectedFile) {
        try {
            String path = selectedFile.getAbsolutePath().toLowerCase();
            if (path.endsWith("png") || path.endsWith("jpg") || path.endsWith("ico") || path.endsWith("bpm")){
                label.setIcon(ImageUtils.createImageIcon(selectedFile));
                label.putClientProperty("path", selectedFile.getAbsolutePath());
                return true;
            }
            Swing.msg("该文件格式不支持！");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private void initComponents() {
        // JFormDesigner - Component initialization - DO NOT MODIFY  //GEN-BEGIN:initComponents
        button = new JButton();
        comboBox = new JComboBox<>();
        label = new JLabel();

        //======== this ========
        Container contentPane = getContentPane();
        contentPane.setLayout(new BorderLayout());

        //---- button ----
        button.setText("\u8f6c\u6362");
        contentPane.add(button, BorderLayout.SOUTH);

        //---- comboBox ----
        comboBox.setModel(new DefaultComboBoxModel<>(new String[] {
            "png",
            "ioc",
            "jpg",
            "bpm"
        }));
        comboBox.setActionCommand("null");
        contentPane.add(comboBox, BorderLayout.NORTH);

        //---- label ----
        label.setIcon(null);
        contentPane.add(label, BorderLayout.CENTER);
        pack();
        setLocationRelativeTo(getOwner());
        // JFormDesigner - End of component initialization  //GEN-END:initComponents
    }

    // JFormDesigner - Variables declaration - DO NOT MODIFY  //GEN-BEGIN:variables
    private JButton button;
    private JComboBox<String> comboBox;
    private JLabel label;
    // JFormDesigner - End of variables declaration  //GEN-END:variables
}
