package com.github.kancyframework.springx.maven.generator.listener;

import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.log.Log;
import com.github.kancyframework.springx.maven.generator.ui.SpringxBootMavenGenerator;
import com.github.kancyframework.springx.swing.Swing;
import com.github.kancyframework.springx.swing.action.AbstractActionApplicationListener;
import com.github.kancyframework.springx.swing.action.Action;
import com.github.kancyframework.springx.swing.action.ActionApplicationEvent;
import com.github.kancyframework.springx.swing.action.KeyStroke;
import com.github.kancyframework.springx.utils.*;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Action({"生成"})
@KeyStroke("ctrl 1")
@Component
public class GeneratorActionListener extends AbstractActionApplicationListener<ActionApplicationEvent<SpringxBootMavenGenerator>> {

    /**
     * 处理事件
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ActionApplicationEvent<SpringxBootMavenGenerator> event) {
        String projectPath = event.getSource().getTextField().getText();
        Swing.assertNotBlank(projectPath, "请选择项目生成路径！");

        File projectFile = FileUtils.createNewDirectory(projectPath);
        projectPath = PathUtils.format(projectFile.getAbsolutePath());

        String artifactId = projectFile.getName();
        String projectName = artifactId.replace("-", "").replace("_", "");

        // 创建文件夹
        Set<String> dirs = new HashSet<>();
        dirs.add(PathUtils.path(projectPath, "src/main/java/com/kancy/", projectName));
        dirs.add(PathUtils.path(projectPath, "src/main/java/com/kancy/", projectName, "listener"));
        dirs.add(PathUtils.path(projectPath, "src/main/java/com/kancy/", projectName, "ui"));
        dirs.add(PathUtils.path(projectPath, "src/main/resources"));
        dirs.add(PathUtils.path(projectPath, "src/main/resources/images"));
        dirs.add(PathUtils.path(projectPath, "src/test/java"));
        dirs.add(PathUtils.path(projectPath, "src/test/resources"));
        dirs.add(PathUtils.path(projectPath, "dist"));
        dirs.stream().forEach(dir->{
            FileUtils.createNewDirectory(dir);
        });


        // 创建文件数据
        Map<String, Object> param = new HashMap<>();
        param.put("packageName", String.format("com.kancy.%s", projectName));
        param.put("datetime", DateUtils.getNowStr());
        param.put("projectName", projectName);
        param.put("artifactId", artifactId);
        param.put("projectPath", projectPath);

        Map<String, Object> files = new HashMap<>();
        files.put(PathUtils.path(projectPath, "dist/pack.exe4j"), FreemarkerUtils.render("pack.ftl", param).get());
        files.put(PathUtils.path(projectPath, "dist/package.bat"), FreemarkerUtils.render("package.ftl", param).get());
        files.put(PathUtils.path(projectPath, "dist/package.sh"), FreemarkerUtils.render("package.ftl", param).get());
        files.put(PathUtils.path(projectPath, "pom.xml"), FreemarkerUtils.render("pom.ftl", param).get());
        files.put(PathUtils.path(projectPath, "src/main/java/com/kancy/", projectName, "Application.java"), FreemarkerUtils.render("application.ftl", param).get());
        files.put(PathUtils.path(projectPath, "src/main/java/com/kancy/", projectName,"ui", "FrameStarter.java"), FreemarkerUtils.render("frame.ftl", param).get());
        files.put(PathUtils.path(projectPath, "src/main/resources/", "application.properties"), FreemarkerUtils.render("application_properties.ftl", param).get());
        files.put(PathUtils.path(projectPath, "src/test/java/com/kancy/", projectName, "Tests.java"), FreemarkerUtils.render("test.ftl", param).get());

        try {
            files.put(PathUtils.path(projectPath, "dist/icon.ico"), IoUtils.toByteArray(GeneratorActionListener.class.getResourceAsStream("/images/icon.ico")));
            files.put(PathUtils.path(projectPath, "src/main/resources/images/icon.png"), IoUtils.toByteArray(GeneratorActionListener.class.getResourceAsStream("/images/icon.png")));
        } catch (Exception e) {
            Log.error(e.getMessage());
        }

        files.entrySet().forEach(e->{
            try {
                FileUtils.createNewFile(e.getKey());
                if (e.getValue() instanceof String){
                    FileUtils.writeByteArrayToFile(String.valueOf(e.getValue()).getBytes(), new File(e.getKey()));
                } else{
                    FileUtils.writeByteArrayToFile((byte[]) e.getValue(), new File(e.getKey()));
                }
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
        });
        Swing.msg(event.getSource(), "创建Springx Boot项目：{} 成功！", projectName);
    }

}
