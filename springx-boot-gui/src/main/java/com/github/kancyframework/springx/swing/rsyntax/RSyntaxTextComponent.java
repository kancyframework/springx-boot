package com.github.kancyframework.springx.swing.rsyntax;

import com.github.kancyframework.springx.utils.CollectionUtils;
import com.github.kancyframework.springx.utils.RandomUtils;
import com.github.kancyframework.springx.utils.ReflectionUtils;
import com.github.kancyframework.springx.utils.StringUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rsyntaxtextarea.Theme;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

/**
 * RSyntaxTextComponent
 *
 * @author kancy
 * @date 2021/1/9 19:50
 */
public class RSyntaxTextComponent extends RTextScrollPane {

    private RSyntaxTextArea textArea;

    public RSyntaxTextComponent() {
        init();
    }

    private void init() {
        textArea = new RSyntaxTextArea();
        textArea.setRows(1);
        textArea.setCodeFoldingEnabled(true);
        textArea.setAntiAliasingEnabled(true);
        setViewportView(textArea);
        setLineNumbersEnabled(true);
    }

    public void addSyntaxEditingStylePopMenu() {
        Set<String> allSyntaxStyles = getAllSyntaxStyles();
        addSyntaxEditingStylePopMenu(getPopupMenu(),
                CollectionUtils.toStrArray(allSyntaxStyles));
    }

    public void addSyntaxEditingStylePopMenu(String ... styles) {
        addSyntaxEditingStylePopMenu(getPopupMenu(), styles);
    }

    public void addSyntaxEditingStylePopMenu(JPopupMenu popupMenu, String ... styles) {
        ButtonGroup bg = new ButtonGroup();
        JMenu menu = new JMenu("语法");
        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                setSyntaxEditingStyle(actionEvent.getActionCommand().split("@", 2)[1]);
            }
        };

        Set<String> syntaxSets = styles.length == 0 ? getAllSyntaxStyles() : CollectionUtils.toSet(styles);
        for (String syntaxStyle : syntaxSets) {
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(action);
            item.setText(syntaxStyle);
            item.setSelected(Objects.equals(textArea.getSyntaxEditingStyle(), syntaxStyle));
            item.setActionCommand(String.format("syntaxStyle@%s", syntaxStyle));
            bg.add(item);
            menu.add(item);
        }
        popupMenu.add(menu);
    }

    private Set<String> getAllSyntaxStyles() {
        Set<String> syntaxSets = new HashSet<>();
        ReflectionUtils.doWithLocalFields(SyntaxConstants.class, field -> {
            try {
                syntaxSets.add(String.valueOf(field.get(SyntaxConstants.class)));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        });
        return syntaxSets;
    }
    public void addThemePopMenu() {
        addThemePopMenu(getPopupMenu());
    }
    public void addThemePopMenu(JPopupMenu popupMenu, String ... themes) {
        Action action = new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String themeFileName =  e.getActionCommand();
                setSyntaxEditingTheme(themeFileName);
            }
        };

        Set<String> themeSet = CollectionUtils.toSet(themes);
        ButtonGroup bg = new ButtonGroup();
        JMenu menu = new JMenu("主题");
        Map<String, String> map = new TreeMap<>();
        map.put("Default", "default.xml");
        map.put("Default (System Selection)", "default-alt.xml");
        map.put("Dark", "dark.xml");
        map.put("Druid", "druid.xml");
        map.put("Monokai", "monokai.xml");
        map.put("Eclipse", "eclipse.xml");
        map.put("IDEA", "idea.xml");
        map.put("Visual Studio", "vs.xml");
        map.forEach((key, value) -> {
            if (!themeSet.isEmpty() && !themeSet.contains(key)){
                return;
            }
            JRadioButtonMenuItem item = new JRadioButtonMenuItem(action);
            item.setSelected(key.contains("(System Selection)"));
            item.setText(key);
            item.setActionCommand(value);
            bg.add(item);
            menu.add(item);
        });
        popupMenu.add(menu);
    }

    public void useDefaultTheme() {
        setSyntaxEditingTheme("default.xml");
    }

    public void useIdeaTheme() {
        setSyntaxEditingTheme("idea.xml");
    }

    public void useVisualStudioTheme() {
        setSyntaxEditingTheme("vs.xml");
    }

    public void useMonokaiTheme() {
        setSyntaxEditingTheme("monokai.xml");
    }

    public void useDarkTheme() {
        setSyntaxEditingTheme("dark.xml");
    }

    public void useEclipseTheme() {
        setSyntaxEditingTheme("eclipse.xml");
    }

    public void useRandomTheme() {
        String[] themes = new String[]{
                "default.xml","idea.xml","monokai.xml","vs.xml","dark.xml","eclipse.xml"
        };
        String themeFileName = RandomUtils.nextString(themes);
        setSyntaxEditingTheme(themeFileName);
    }

    /**
     * 设置主题
     * @param themeFileName
     */
    public void setSyntaxEditingTheme(String themeFileName) {
        InputStream in = getClass().
                getResourceAsStream(String.format("/org/fife/ui/rsyntaxtextarea/themes/%s" , themeFileName));
        try {
            Theme theme = Theme.load(in);
            theme.apply(getRSyntaxTextArea());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @see SyntaxConstants
     * @param style
     */
    public void setSyntaxEditingStyle(String style) {
        textArea.setSyntaxEditingStyle(style);
    }

    /**
     * Sets whether code folding is enabled.  Note that only certain
     * languages will support code folding out of the box.  Those languages
     * which do not support folding will ignore this property.<p>
     * This method fires a property change event of type
     * .
     *
     * @param enabled Whether code folding should be enabled.
     */
    public void setCodeFoldingEnabled(boolean enabled) {
        textArea.setCodeFoldingEnabled(enabled);
    }

    /**
     * Sets whether anti-aliasing is enabled in this editor.  This method
     * fires a property change event of type .
     *
     * @param enabled Whether anti-aliasing is enabled.
     */
    public void setAntiAliasingEnabled(boolean enabled) {
        textArea.setAntiAliasingEnabled(enabled);
    }
    /**
     * 设置大小
     * @param width
     * @param height
     */
    public void setTextAreaSize(int width, int height) {
        textArea.setSize(new Dimension(width, height));
    }

    /**
     * 设置内容
     * @param text
     */
    public void setText(String text) {
        textArea.setText(text);
    }

    /**
     * 设置JPopupMenu
     * @param popupMenu
     */
    public void setPopupMenu(JPopupMenu popupMenu) {
        textArea.setPopupMenu(popupMenu);
    }

    /**
     * getPopupMenu
     * @return
     */
    public JPopupMenu getPopupMenu() {
        return textArea.getPopupMenu();
    }

    /**
     * getRSyntaxTextArea
     * @return
     */
    public RSyntaxTextArea getRSyntaxTextArea() {
        return textArea;
    }
}
