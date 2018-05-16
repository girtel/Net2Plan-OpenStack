package com.net2plan.gui.plugins.utils;

import com.net2plan.gui.GUINet2Plan;
import com.net2plan.interfaces.networkDesign.Net2PlanException;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

/**
 * Created by Jorge San Emeterio on 06/10/2016.
 */
public abstract class GUIWindow extends JFrame
{
    private final JComponent component;

    public GUIWindow(final JComponent component)
    {
        super();
        this.component = component;
        this.buildWindow();
    }

    public JComponent getInnerComponent()
    {
        return component;
    }

    public void showWindow(final boolean doGainFocus)
    {
        if (component != null)
        {
            this.setVisible(true);
            if (doGainFocus) this.setState(JFrame.NORMAL);
        } else
        {
            throw new Net2PlanException("Window does not contain a component. Nothing to show...");
        }
    }

    private void buildWindow()
    {
        this.setTitle(this.getTitle());
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setLocationRelativeTo(SwingUtilities.getRoot(component));
        this.setLayout(new BorderLayout());
        this.setMinimumSize(new Dimension(600, 700));
        this.setVisible(false);

        this.add(this.component, BorderLayout.CENTER);

        URL iconURL = GUINet2Plan.class.getResource("/resources/gui/icon.png");
        ImageIcon icon = new ImageIcon(iconURL);
        this.setIconImage(icon.getImage());
    }

    public abstract String getTitle();
}
