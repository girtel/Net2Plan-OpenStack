/*******************************************************************************
 * Copyright (c) 2017 Pablo Pavon Marino and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the 2-clause BSD License 
 * which accompanies this distribution, and is available at
 * https://opensource.org/licenses/BSD-2-Clause
 *
 * Contributors:
 *     Pablo Pavon Marino and others - initial API and implementation
 *******************************************************************************/


package com.net2plan.gui.plugins.networkDesign.topologyPane;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.FileChooserNetworkDesign;
import com.net2plan.gui.plugins.networkDesign.interfaces.ITopologyCanvas;
import com.net2plan.gui.plugins.networkDesign.interfaces.ITopologyCanvasPlugin;
import com.net2plan.gui.plugins.networkDesign.topologyPane.plugins.MoveNodePlugin;
import com.net2plan.gui.plugins.networkDesign.topologyPane.plugins.PanGraphPlugin;
import com.net2plan.gui.plugins.networkDesign.topologyPane.plugins.PopupMenuPlugin;
import com.net2plan.gui.plugins.utils.OpenStackInitalButtonFunctionalities;
import com.net2plan.gui.plugins.utils.OpenStackFileChooser;
import com.net2plan.internal.Constants.DialogType;
import com.net2plan.internal.ErrorHandling;
import com.net2plan.internal.SystemUtils;
import com.net2plan.utils.SwingUtils;
import net.miginfocom.swing.MigLayout;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings("unchecked")
public class TopologyPanel extends JPanel implements ActionListener
{
    private final GUINetworkDesign callback;
    private final ITopologyCanvas canvas;

    private final JPanel canvasPanel;

    private final TopologyTopBar topBar;
    private final TopologySideBar sideBar;

    private FileChooserNetworkDesign fc_netPlan;

    /**
     * Simplified constructor that does not require to indicate default locations
     * for {@code .n2p} files.
     *
     * @param callback   Topology callback listening plugin events
     * @param canvasType Canvas type (i.e. JUNG)
     */
    public TopologyPanel(GUINetworkDesign callback, Class<? extends ITopologyCanvas> canvasType)
    {
        this(callback, canvasType, null);
    }

    /**
     * Simplified constructor that does not require to indicate default locations
     * for {@code .n2p} files.
     *
     * @param callback   Topology callback listening plugin events
     * @param canvasType Canvas type (i.e. JUNG)
     * @param plugins    List of plugins to be included (it may be null)
     */
    public TopologyPanel(GUINetworkDesign callback, Class<? extends ITopologyCanvas> canvasType, List<ITopologyCanvasPlugin> plugins)
    {
        this(callback, null, null, canvasType, plugins);
    }

    /**
     * Default constructor.
     *
     * @param callback               Topology callback listening plugin events
     * @param defaultDesignDirectory Default location for design {@code .n2p} files (it may be null, then default is equal to {@code net2planFolder/workspace/data/networkTopologies})
     * @param defaultDemandDirectory Default location for design {@code .n2p} files (it may be null, then default is equal to {@code net2planFolder/workspace/data/trafficMatrices})
     * @param canvasType             Canvas type (i.e. JUNG)
     * @param plugins                List of plugins to be included (it may be null)
     */
    public TopologyPanel(final GUINetworkDesign callback, File defaultDesignDirectory, File defaultDemandDirectory, Class<? extends ITopologyCanvas> canvasType, List<ITopologyCanvasPlugin> plugins)
    {
        this.callback = callback;

        try
        {
            // File chooser default directory.
            final File currentDir = SystemUtils.getCurrentDir();

            File defaultDesignDirectoryFilter = defaultDesignDirectory == null ? new File(currentDir + SystemUtils.getDirectorySeparator() + "workspace" + SystemUtils.getDirectorySeparator() + "data" + SystemUtils.getDirectorySeparator() + "networkTopologies") : defaultDesignDirectory;

            // File chooser
            this.fc_netPlan = new FileChooserNetworkDesign(defaultDesignDirectoryFilter, DialogType.NETWORK_DESIGN);

            // Declare canvas : Reflections
            this.canvas = canvasType.getDeclaredConstructor(GUINetworkDesign.class, TopologyPanel.class).newInstance(callback, this);

            // Add given plugins
            if (plugins != null)
                for (ITopologyCanvasPlugin plugin : plugins)
                    addPlugin(plugin);

            this.setLayout(new BorderLayout());

            // Top bar menu
            this.topBar = new TopologyTopBar(callback, this, canvas);
            this.add(topBar, BorderLayout.NORTH);

            // Canvas panel
            final JComponent canvasComponent = canvas.getCanvasComponent();
            canvasComponent.setBorder(LineBorder.createBlackLineBorder());

            this.canvasPanel = new JPanel(new BorderLayout());

            this.sideBar = new TopologySideBar(callback, this, canvas);

            this.canvasPanel.add(canvasComponent, BorderLayout.CENTER);
            this.canvasPanel.add(sideBar, BorderLayout.WEST);

            this.add(canvasPanel, BorderLayout.CENTER);

            // Buttons cannot be focusable
            List<Component> children = SwingUtils.getAllComponents(this);
            for (Component component : children)
                if (component instanceof AbstractButton)
                    component.setFocusable(false);

            // Action controllers
            this.addPlugin(new PanGraphPlugin(callback, canvas, MouseEvent.BUTTON1_MASK)); // Panning
            this.addPlugin(new PopupMenuPlugin(callback, this.canvas)); // Right button pop-up

            // Create links
            //if (callback.getVisualizationState().isNetPlanEditable() && this.getCanvas() instanceof JUNGCanvas)
                //(new AddLinkGraphPlugin(callback, canvas, MouseEvent.BUTTON1_MASK, MouseEvent.BUTTON1_MASK | MouseEvent.SHIFT_MASK));

            // Move nodes
            if (callback.getVisualizationState().isNetPlanEditable())
                addPlugin(new MoveNodePlugin(callback, canvas, MouseEvent.BUTTON1_MASK | MouseEvent.CTRL_MASK));

            this.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.BLACK), "Network topology"));

            // Zoom all on resize
            this.addComponentListener(new ComponentAdapter()
            {
                @Override
                public void componentResized(ComponentEvent e)
                {
                    if (e.getComponent().getSize().getHeight() != 0 && e.getComponent().getSize().getWidth() != 0)
                    {
                        canvas.zoomAll();
                    }
                }
            });

            // Key actions
            this.addKeyCombinationActions();


            // DEBUG
            if (ErrorHandling.isDebugEnabled())
            {
                final JLabel position = new JLabel();

                canvas.getCanvasComponent().addMouseMotionListener(new MouseMotionAdapter()
                {
                    @Override
                    public void mouseMoved(MouseEvent e)
                    {
                        Point point = e.getPoint();
                        position.setText("view = " + point + ", NetPlan coord = " + canvas.getCanvasPointFromNetPlanPoint(point));
                    }
                });

                add(position, BorderLayout.SOUTH);
            }
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

    }

    /**
     * Adds a new plugin to the canvas.
     *
     * @param plugin Plugin to be added
     * @since 0.3.0
     */
    public void addPlugin(ITopologyCanvasPlugin plugin)
    {
        canvas.addPlugin(plugin);
    }

    private void addKeyCombinationActions()
    {
        final TopologyPanel topologyPanel = TopologyPanel.this;

        callback.addKeyCombinationAction("Add credentials", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                OpenStackInitalButtonFunctionalities.addLoginUserInformationDialog(callback,topBar.btn_add);
                //topologyPanel.loadCredentials();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_A, InputEvent.CTRL_DOWN_MASK));

        callback.addKeyCombinationAction("Load credentials", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                OpenStackInitalButtonFunctionalities.loadLoginUserFile(callback);
                //topologyPanel.loadCredentials();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_L, InputEvent.CTRL_DOWN_MASK));

        callback.addKeyCombinationAction("Generate credentials", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                OpenStackInitalButtonFunctionalities.generatedLoginUserFile(callback);
                //topologyPanel.loadCredentials();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_DOWN_MASK));

        callback.addKeyCombinationAction("Clear design", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                callback.clearDesign();
                //topologyPanel.loadCredentials();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_D, InputEvent.CTRL_DOWN_MASK));

        callback.addKeyCombinationAction("Zoom in", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (topologyPanel.getSize().getWidth() != 0 && topologyPanel.getSize().getHeight() != 0)
                    topologyPanel.getCanvas().zoomIn();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_ADD, InputEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, InputEvent.CTRL_DOWN_MASK));

        callback.addKeyCombinationAction("Zoom out", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (topologyPanel.getSize().getWidth() != 0 && topologyPanel.getSize().getHeight() != 0)
                    topologyPanel.getCanvas().zoomOut();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, InputEvent.CTRL_DOWN_MASK), KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, InputEvent.CTRL_DOWN_MASK));

        callback.addKeyCombinationAction("Zoom all", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                if (topologyPanel.getSize().getWidth() != 0 && topologyPanel.getSize().getHeight() != 0)
                    topologyPanel.getCanvas().zoomAll();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_MULTIPLY, InputEvent.CTRL_DOWN_MASK));

        callback.addKeyCombinationAction("Take snapshot", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                canvas.takeSnapshot();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_F12, InputEvent.CTRL_DOWN_MASK));

    }

    public final void updateTopToolbar()
    {
        topBar.update();
    }

    public final void updateMultilayerPanel()
    {
        sideBar.refresh();
    }

    public JPanel getCanvasPanel()
    {
        return canvasPanel;
    }

    /**
     * Returns a reference to the topology canvas.
     *
     * @return Reference to the topology canvas
     * @since 0.2.3
     */
    public ITopologyCanvas getCanvas()
    {
        return canvas;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        Object src = e.getSource();

    }
}
