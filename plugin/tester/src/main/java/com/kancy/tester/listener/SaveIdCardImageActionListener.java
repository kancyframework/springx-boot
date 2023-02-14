package com.kancy.tester.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.JFrameApplicationEvent;
import com.github.kancyframework.springx.swing.action.JFrameApplicationListener;
import com.github.kancyframework.springx.swing.action.KeyStroke;
import com.github.kancyframework.springx.swing.filechooser.SimpleDirectoryChooser;
import com.github.kancyframework.springx.swing.filechooser.SimpleFileChooser;
import com.github.kancyframework.springx.swing.utils.ImageUtils;
import com.github.kancyframework.springx.utils.FileUtils;
import com.github.kancyframework.springx.utils.PathUtils;
import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.tester.ui.IdCardPanel;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

/**
 * SettingDefaultNationActionListener
 *
 * @author huangchengkang
 * @date 2021/8/27 18:51
 */
@Action({"身份证另存为"})
@KeyStroke("alt 4")
@Component
public class SaveIdCardImageActionListener extends JFrameApplicationListener {
    @Autowired
    private IdCardPanel idCardPanel;
    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(JFrameApplicationEvent event) {
        String idCardNo = idCardPanel.getIdCardNoTextField().getText();
        if (StringUtils.isBlank(idCardNo)){
            Swing.msg(event.getSource(), "身份证信息为空，请先生成！");
            return;
        }

        boolean selected = idCardPanel.getIdCardImageMergeCheckBoxMenuItem().isSelected();

        if (selected){
            SimpleFileChooser dialog = new SimpleFileChooser(event.getSource(), "身份证图片另存为");
            dialog.setFileNameExtensionFilter("支持png，jpg格式", new String[]{"png", "jpg"});
            dialog.setSelectedFile(new File(String.format("身份证_%s_%s.png",
                    idCardPanel.getNameTextField().getText(),
                    idCardPanel.getIdCardNoTextField().getText().replace(" ",""))
            ));

            dialog.showSaveDialog(fileChooser -> {
                String selectedFilePath = fileChooser.getSelectedFilePath();
                try {
                    mergeAndWriteIdCardImages(selectedFilePath);
                    Swing.msg(idCardPanel, "身份证影像保存成功！");
                } catch (IOException e) {
                    Swing.msg(idCardPanel, "身份证影像保存失败！");
                }
            });
        }else {
            SimpleDirectoryChooser dialog = new SimpleDirectoryChooser(event.getSource(), "身份证图片另存为");
            dialog.setOnlyDirectorySelection();
            dialog.showSaveDialog(fileChooser -> {
                String selectedFilePath = fileChooser.getSelectedFilePath();
                try {
                    ImageUtils.writeComponentImage(idCardPanel.getIdCardFrontImagePanel(),
                            PathUtils.path(selectedFilePath, String.format("身份证正面_%s_%s.png",
                                    idCardPanel.getNameTextField().getText(),
                                    idCardPanel.getIdCardNoTextField().getText().replace(" ",""))));
                    ImageUtils.writeComponentImage(idCardPanel.getIdCardBackImagePanel(),
                            PathUtils.path(selectedFilePath, String.format("身份证反面_%s_%s.png",
                                    idCardPanel.getNameTextField().getText(),
                                    idCardPanel.getIdCardNoTextField().getText().replace(" ",""))));
                    Swing.msg(idCardPanel, "保存成功！");
                } catch (IOException e) {
                    Swing.msg(idCardPanel, "保存失败！");
                }
            });
        }

    }

    public void mergeAndWriteIdCardImages(String filePath) throws IOException {
        BufferedImage bi_0 = ImageIO.read(new ByteArrayInputStream(
                ImageUtils.getComponentImage(idCardPanel.getIdCardFrontImagePanel())));
        BufferedImage bi_1 = ImageIO.read(new ByteArrayInputStream(
                ImageUtils.getComponentImage(idCardPanel.getIdCardBackImagePanel())));

        //假设图片0 和图片1 宽度相同，上下合成
        //new 一个新的图像
        int w = bi_0.getWidth();
        int h_0 = bi_0.getHeight();
        int h_1 = bi_1.getHeight();
        int h = h_0 + h_1;
        BufferedImage bi=new BufferedImage(w,h,BufferedImage.TYPE_4BYTE_ABGR);
        //像素一个一个复制过来
        for(int y=0; y<h_0; y++){
            for(int x=0;x<w;x++){
                bi.setRGB(x,y,bi_0.getRGB(x,y));
            }
        }
        for(int y=0;y<h_1;y++){
            for(int x=0;x<w;x++){
                bi.setRGB(x,h_0+y,bi_1.getRGB(x,y));
            }
        }
        ImageIO.write(bi, "PNG", FileUtils.createNewFile(filePath));
    }
}
