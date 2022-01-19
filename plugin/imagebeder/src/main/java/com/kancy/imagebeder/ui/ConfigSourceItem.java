package com.kancy.imagebeder.ui;

import com.github.kancyframework.springx.utils.StringUtils;
import com.kancy.imagebeder.config.ImagebedConfig;
import lombok.Data;

/**
 * ConfigSoureItem
 *
 * @author huangchengkang
 * @date 2022/1/19 15:28
 */
@Data
public class ConfigSourceItem {
    private String configKey;
    private String showName;
    public ConfigSourceItem(ImagebedConfig config){
        this.configKey = config.toString();
        this.showName = String.format("%s（%s）",this.configKey, config.getRemark());
        if (StringUtils.isBlank(config.getRemark())){
            this.showName = this.configKey;
        }
    }

    @Override
    public String toString() {
        return this.showName;
    }
}
