package com.github.kancyframework.springx.demo.ui;

import com.github.kancyframework.springx.context.annotation.Component;
import com.github.kancyframework.springx.swing.rsyntax.RSyntaxTextComponent;
import com.github.kancyframework.springx.swing.rsyntax.SearchToolBar;
import lombok.Data;

import javax.swing.*;
import java.awt.*;

@Data
@Component
public class MainUI extends JFrame {

    private static final long serialVersionUID = 1L;

    private RSyntaxTextComponent rSyntaxTextComponent;

    private JButton jButton = new JButton("测试");

    public MainUI() {
        JPanel cp = new JPanel(new BorderLayout());
        rSyntaxTextComponent = new RSyntaxTextComponent();
        cp.add(rSyntaxTextComponent);

        // Create a toolbar with searching options.
        SearchToolBar toolBar = new SearchToolBar(rSyntaxTextComponent.getTextArea());
        cp.add(toolBar, BorderLayout.NORTH);

        setContentPane(cp);
        setTitle("Find and Replace Demo");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();
        setLocationRelativeTo(null);

    }
}
