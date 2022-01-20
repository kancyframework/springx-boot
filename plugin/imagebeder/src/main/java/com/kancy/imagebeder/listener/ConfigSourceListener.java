package com.kancy.imagebeder.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.action.AbstractActionApplicationListener;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.ActionApplicationEvent;
import com.kancy.imagebeder.config.ImagebedConfig;
import com.kancy.imagebeder.config.Settings;
import com.kancy.imagebeder.ui.ConfigDialog;
import com.kancy.imagebeder.ui.ConfigSourceItem;
import com.kancy.imagebeder.ui.Imagebeder;

import java.util.Objects;

/**
 * ConfigSourceListener
 *
 * @author huangchengkang
 * @date 2022/1/18 23:36
 */
@Action({"配置源"})
@Component
public class ConfigSourceListener extends AbstractActionApplicationListener<ActionApplicationEvent<Imagebeder>> {

    @Autowired
    private Settings settings;
    @Autowired
    private Imagebeder imagebeder;

    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ActionApplicationEvent<Imagebeder> event) {
        ConfigDialog configDialog = null;
        ConfigSourceItem selectedItem = (ConfigSourceItem) imagebeder.getComboBox_config().getSelectedItem();
        if (Objects.nonNull(selectedItem)){
            String key = selectedItem.getConfigKey();
            ImagebedConfig imagebedConfig = settings.getConfigs().get(key);
            configDialog = new ConfigDialog(event.getSource(), imagebedConfig);
        }else {
            configDialog = new ConfigDialog(event.getSource());
        }

        configDialog.setModal(true);
        configDialog.setVisible(true);
    }
}
