package com.kancy.imagebeder.listener;

import com.github.kancyframework.springx.context.annotation.Autowired;
import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.action.AbstractActionApplicationListener;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.ActionApplicationEvent;
import com.kancy.imagebeder.config.Settings;
import com.kancy.imagebeder.ui.Imagebeder;

/**
 * ConfigSourceListener
 *
 * @author huangchengkang
 * @date 2022/1/18 23:36
 */
@Action({"开启超链接"})
@Component
public class LinkEnabledListener extends AbstractActionApplicationListener<ActionApplicationEvent<Imagebeder>> {

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
        settings.setLinkEnabled(imagebeder.getCheckBox_link_enabled().isSelected());
        settings.save();
    }
}
