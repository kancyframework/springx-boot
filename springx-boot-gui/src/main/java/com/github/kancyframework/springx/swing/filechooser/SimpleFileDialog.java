package com.github.kancyframework.springx.swing.filechooser;

import com.github.kancyframework.springx.utils.ObjectUtils;
import com.github.kancyframework.springx.utils.StringUtils;

import java.awt.*;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * SimpleFileDialog
 * 只能选文件，不能选择文件夹
 * @author kancy
 * @date 2020/2/16 17:15
 */
public class SimpleFileDialog {

    private Window window;
    private String title = "\u6d4f\u89c8\u6587\u4ef6";
    private FileDialog fileDialog;

    public SimpleFileDialog() {
        initFileDialog();
    }

    public SimpleFileDialog(String title) {
        this();
        this.title = title;
    }

    public SimpleFileDialog(Window window) {
        this.window = window;
        initFileDialog();
    }

    public SimpleFileDialog(Window window, String title) {
        this(window);
        this.title = title;
    }

    public boolean isMultiSelectionEnabled() {
        return fileDialog.isMultipleMode();
    }

    /**
     * 多选功能
     * @param enabled
     * @return
     */
    public SimpleFileDialog setMultiSelectionEnabled(boolean enabled){
        this.fileDialog.setMultipleMode(enabled);
        return this;
    }

    /**
     * 设置当前路径为浏览的文件夹
     * @return
     */
    public SimpleFileDialog setCurrentDirectory(){
        this.fileDialog.setDirectory(".");
        return this;
    }
    public SimpleFileDialog setCurrentDirectory(File file){
        if (file.isDirectory()){
            this.fileDialog.setDirectory(file.getAbsolutePath());
        }else {
            this.fileDialog.setDirectory(file.getParent());
        }
        return this;
    }
    public SimpleFileDialog setCurrentDirectory(String filePath){
        return setCurrentDirectory(new File(filePath));
    }

    /**
     * 设置过滤器
     * Filename filters do not function in Sun's reference implementation for Microsoft Windows.
     * FilenameFilter 不生效了
     * @param filenameFilter
     * @return
     */
    @Deprecated
    public SimpleFileDialog setFileFilter(FilenameFilter filenameFilter){
        fileDialog.setFilenameFilter(filenameFilter);
        return this;
    }

    /**
     * 设置文件过滤
     * FilenameFilter 不生效了
     * @param extensions
     * @return
     */
    @Deprecated
    public SimpleFileDialog setFileNameExtensionFilter(String... extensions){
        if (ObjectUtils.isNotEmpty(extensions)){
            fileDialog.setFile(StringUtils.join(extensions, ";"));
        }
        return this;
    }

    /**
     * 设置文件
     * @param file
     * @return
     */
    public SimpleFileDialog setFile(String file){
        if (ObjectUtils.isNotEmpty(file)){
            fileDialog.setFile(file);
        }
        return this;
    }

    /**
     * 打开 Open Dialog
     */
    public void showOpenDialog(){
        this.fileDialog.setMode(FileDialog.LOAD);
        show();
    }

    /**
     * 打开 Open Dialog
     */
    public void showOpenDialog(Consumer<SimpleFileDialog> consumer){
        showOpenDialog();
        if (hasSelectedFile()){
            consumer.accept(this);
        }
    }

    /**
     * 打开 Save Dialog
     */
    public void showSaveDialog(){
        this.fileDialog.setMode(FileDialog.SAVE);
        show();
    }

    /**
     * 打开 Save Dialog
     */
    public void showSaveDialog(Consumer<SimpleFileDialog> consumer){
        showSaveDialog();
        if (hasSelectedFile()){
            consumer.accept(this);
        }
    }

    private void show(){
        setCurrentDirectory();
        this.fileDialog.setTitle(title);
        this.fileDialog.setLocationRelativeTo(window);
        this.fileDialog.setVisible(true);
    }

    public boolean hasSelectedFile() {
        return Objects.nonNull(fileDialog.getFile()) || Objects.nonNull(fileDialog.getDirectory()) ;
    }

    public File getSelectedFile() {
        if (hasSelectedFile()){
            return new File(getSelectedFilePath());
        }
        return null;
    }

    public String getSelectedFilePath() {
        if (hasSelectedFile()){
            String filePath = String.format("%s%s", fileDialog.getDirectory(), fileDialog.getFile());
            return filePath.replaceAll("\\+","/");
        }
        return null;
    }

    public File[] getSelectedFiles() {
        if (hasSelectedFile()){
            return fileDialog.getFiles();
        }
        return null;
    }

    public List<String> getSelectedFilePaths() {
        File[] selectedFiles = getSelectedFiles();
        List<String> list = new ArrayList<>();
        if (Objects.isNull(selectedFiles)){
            return list;
        }
        for (File selectedFile : selectedFiles) {
            list.add(selectedFile.getAbsolutePath().replaceAll("\\+","/"));
        }
        return list;
    }

    /**
     * 初始化FileDialog
     */
    private void initFileDialog() {
        if (Objects.isNull(window)){
            this.fileDialog = new FileDialog((Frame) null);
        }
        if (window instanceof Dialog){
            this.fileDialog = new FileDialog((Dialog) window);
        }
        if (window instanceof Frame){
            this.fileDialog = new FileDialog((Frame) window);
        }
    }
}
