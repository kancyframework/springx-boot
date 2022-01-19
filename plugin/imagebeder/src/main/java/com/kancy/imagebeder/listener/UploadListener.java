package com.kancy.imagebeder.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.AbstractActionApplicationListener;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.ActionApplicationEvent;
import com.github.kancyframework.springx.swing.action.KeyStroke;
import com.kancy.imagebeder.service.UploadResult;
import com.kancy.imagebeder.service.Uploader;
import com.kancy.imagebeder.ui.ConfigSourceItem;
import com.kancy.imagebeder.ui.Imagebeder;
import com.kancy.imagebeder.ui.UploadDialog;

import javax.swing.*;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * ConfigSourceListener
 *
 * @author huangchengkang
 * @date 2022/1/18 23:36
 */
@Action({"上传"})
@KeyStroke("ctrl 1")
@Component
public class UploadListener extends AbstractActionApplicationListener<ActionApplicationEvent<Imagebeder>> {
    private static ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Autowired
    private Imagebeder imagebeder;
    @Autowired
    private Uploader uploader;

    private volatile boolean upload = false;


    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ActionApplicationEvent<Imagebeder> event) {


        Object path = imagebeder.getLabel_img().getClientProperty("path");
        if (Objects.isNull(path)){
            Swing.msg(event.getSource(), "请选择需要上传的图片！");
            return;
        }

        if (upload){
            Swing.msg(event.getSource(), "正在上传其他资源，请稍后重试！");
            return;
        }

        executorService.execute(() -> {
            upload = true;
            try {
                JProgressBar progressBar = imagebeder.getProgressBar();
                progressBar.setIndeterminate(true);
                progressBar.setVisible(true);

                ConfigSourceItem selectedItem = (ConfigSourceItem) imagebeder.getComboBox_config().getSelectedItem();

                String configKey = selectedItem.getConfigKey();

                UploadResult uploadResult = null;
                if (Objects.equals(path, "bytes")){
                    byte[] bytes = (byte[]) imagebeder.getLabel_img().getClientProperty("bytes");
                    uploadResult = uploader.upload(configKey, bytes);
                }else {
                    uploadResult = uploader.upload(configKey, path.toString());
                }

                progressBar.setVisible(false);

                if (Objects.nonNull(uploadResult)){
                    UploadDialog configDialog = new UploadDialog(event.getSource(), uploadResult);
                    configDialog.setModal(true);
                    configDialog.setVisible(true);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                upload = false;
            }
        });
    }
}
