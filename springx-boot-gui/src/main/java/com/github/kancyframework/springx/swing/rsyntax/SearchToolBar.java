package com.github.kancyframework.springx.swing.rsyntax;

import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * SearchToolBar
 *
 * @author kancy
 * @date 2020/12/13 16:19
 */
public class SearchToolBar extends JToolBar implements ActionListener {
    private JTextArea textArea;
    private JTextField searchField;
    private JCheckBox regexCheckBox;
    private JCheckBox matchCaseCheckBox;
    /**
     * Creates a new <code>JPanel</code> with a double buffer
     * and a flow layout.
     */
    public SearchToolBar(JTextArea textArea) {
        this.textArea = textArea;

        // Create a toolbar with searching options.
        JToolBar toolBar = new JToolBar();
        searchField = new JTextField(30);
        toolBar.add(searchField);
        final JButton nextButton = new JButton("查找下一个");
        nextButton.setActionCommand("FindNext");
        nextButton.addActionListener(this);
        toolBar.add(nextButton);
        searchField.addActionListener(e -> nextButton.doClick(0));
        JButton prevButton = new JButton("查找上一个");
        prevButton.setActionCommand("FindPrev");
        prevButton.addActionListener(this);
        toolBar.add(prevButton);
        regexCheckBox = new JCheckBox("正则匹配");
        toolBar.add(regexCheckBox);
        matchCaseCheckBox = new JCheckBox("忽略大小写");
        matchCaseCheckBox.setSelected(true);
        toolBar.add(matchCaseCheckBox);
        add(toolBar, BorderLayout.NORTH);
    }

    /**
     * Invoked when an action occurs.
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        // "FindNext" => search forward, "FindPrev" => search backward
        String command = e.getActionCommand();
        boolean forward = "FindNext".equals(command);

        // Create an object defining our search parameters.
        SearchContext context = new SearchContext();
        String text = searchField.getText();
        if (text.length() == 0) {
            return;
        }
        context.setSearchFor(text);
        context.setMatchCase(!matchCaseCheckBox.isSelected());
        context.setRegularExpression(regexCheckBox.isSelected());
        context.setSearchForward(forward);
        context.setWholeWord(false);

        boolean found = SearchEngine.find(textArea, context).wasFound();
        if (!found) {
            textArea.setSelectionStart(0);
            textArea.setSelectionEnd(0);
            SearchEngine.find(textArea, context).wasFound();
        }
    }

    public JTextArea getTextArea() {
        return textArea;
    }

    public JTextField getSearchField() {
        return searchField;
    }

    public JCheckBox getRegexCheckBox() {
        return regexCheckBox;
    }

    public JCheckBox getMatchCaseCheckBox() {
        return matchCaseCheckBox;
    }
}
