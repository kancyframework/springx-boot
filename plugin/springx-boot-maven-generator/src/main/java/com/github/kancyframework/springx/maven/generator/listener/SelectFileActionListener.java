package com.github.kancyframework.springx.maven.generator.listener;

import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.maven.generator.ui.SpringxBootMavenGenerator;
import com.github.kancyframework.springx.swing.action.AbstractActionApplicationListener;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.ActionApplicationEvent;
import com.github.kancyframework.springx.swing.action.KeyStroke;
import com.github.kancyframework.springx.swing.filechooser.SimpleFileDialog;

import java.util.function.Consumer;

@Action({"选择路径"})
@KeyStroke("ctrl 2")
@Component
public class SelectFileActionListener extends AbstractActionApplicationListener<ActionApplicationEvent<SpringxBootMavenGenerator>> {

    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ActionApplicationEvent<SpringxBootMavenGenerator> event) {
        SimpleFileDialog simpleFileDialog = new SimpleFileDialog(event.getSource(), "选择项目文件夹");
        simpleFileDialog.setCurrentDirectory("test");
        simpleFileDialog.setFile("test");
        simpleFileDialog.showOpenDialog(dialog -> {
            if (dialog.hasSelectedFile()){
                event.getSource().getTextField().setText(dialog.getSelectedFilePath());
            }
        });
    }

}
