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

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.google.common.collect.Sets;
import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.FileChooserNetworkDesign;
import com.net2plan.gui.plugins.networkDesign.interfaces.ITopologyCanvas;
import com.net2plan.gui.plugins.networkDesign.interfaces.ITopologyCanvasPlugin;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.topologyPane.jung.plugins.AddLinkGraphPlugin;
import com.net2plan.gui.plugins.networkDesign.topologyPane.jung.JUNGCanvas;
import com.net2plan.gui.plugins.networkDesign.topologyPane.jung.state.CanvasOption;
import com.net2plan.gui.plugins.networkDesign.topologyPane.plugins.MoveNodePlugin;
import com.net2plan.gui.plugins.networkDesign.topologyPane.plugins.PanGraphPlugin;
import com.net2plan.gui.plugins.networkDesign.topologyPane.plugins.PopupMenuPlugin;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.VisualizationState;
import com.net2plan.gui.plugins.utils.CipherClass;
import com.net2plan.gui.utils.FileDrop;
import com.net2plan.interfaces.networkDesign.*;
import com.net2plan.internal.Constants.DialogType;
import com.net2plan.internal.Constants.NetworkElementType;
import com.net2plan.internal.ErrorHandling;
import com.net2plan.internal.SystemUtils;
import com.net2plan.utils.Pair;
import com.net2plan.utils.SwingUtils;
import org.apache.commons.collections15.BidiMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.*;
import java.util.List;

@SuppressWarnings("unchecked")
public class TopologyPanel extends JPanel implements ActionListener
{
    private final GUINetworkDesign callback;
    private final ITopologyCanvas canvas;

    private final JPanel canvasPanel;

    private final TopologyTopBar topBar;
    private final TopologySideBar sideBar;

    private final CipherClass cc = new CipherClass();

    private FileChooserNetworkDesign fc_netPlan;
    private JComboBox systemList;
    private String[] systems = { "ubuntu", "rhel", "centOs"};
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

        callback.addKeyCombinationAction("Load design", new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent e)
            {
                topologyPanel.loadCredentials();
            }
        }, KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK));

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

    public void addCredentials(){

        JPanel jp1;
        JButton jbP1;
        JTextField os_username, os_auth_url,os_project_id,os_user_domain_name;
        JPasswordField os_password;
        JFrame jfM;
        JLabel labelUser,labelPassword,labelUrl,labelProject,labelUDomain;

        jfM = new JFrame("Credentials");
        jp1 = new JPanel(new GridLayout(5, 2, 30, 10));//filas, columnas, espacio entre filas, espacio entre columnas

        jfM.setLayout(null);

        labelUser = new JLabel("OS_USERNAME",  SwingConstants.LEFT);
        labelPassword = new JLabel("OS_PASSWORD",  SwingConstants.LEFT);
        labelUrl = new JLabel("OS_AUTH_URL",  SwingConstants.LEFT);
        labelProject = new JLabel("OS_PROJECT_NAME",  SwingConstants.LEFT);
        labelUDomain = new JLabel("OS_U_DOMAIN_NAME", SwingConstants.LEFT);


        os_username = new JTextField(10);
        os_password = new JPasswordField();
        os_auth_url = new JTextField();
        os_project_id = new JTextField();
        os_user_domain_name = new JTextField();

        jp1.add(labelUser);
        jp1.add(os_username);

        jp1.add(labelPassword);
        jp1.add(os_password);

        jp1.add(labelUrl);
        jp1.add(os_auth_url);

        jp1.add(labelProject);
        jp1.add(os_project_id);

        jp1.add(labelUDomain);
        jp1.add(os_user_domain_name);

        jp1.setVisible(true);

        jbP1 = new JButton("Enter");

        jp1.setBounds(10, 10, 250, 200);

        jbP1.setBounds(100, 225, 100, 30);

        jbP1.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e)
            {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("os_auth_url",os_auth_url.getText());
                jsonObject.put("os_username",os_username.getText());
                jsonObject.put("os_password",String.valueOf(os_password.getPassword()));
                jsonObject.put("os_project_id",os_project_id.getText());
                jsonObject.put("os_user_domain_name",os_user_domain_name.getText());

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("Credentials",jsonArray);

                System.out.println("TopologyPanel adding new OSClientV3 " + jsonObject1);
                callback.getOpenStackNet().AddJSONObjectOsClients(jsonObject1);

                   //callback.getAboutIt().updateText();
                jfM.dispose();
            }
        });

        jfM.add(jp1);
        jfM.add(jbP1);


        ImageIcon img = new ImageIcon(getClass().getResource("/resources/common/openstack_logo.png"));
        jfM.setIconImage(img.getImage());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        jfM.setSize(300, 300);
        jfM.setLocation(dim.width/2-jfM.getSize().width/2, dim.height/2-jfM.getSize().height/2);

        jfM.setResizable(false);
        jfM.setVisible(true);
        jfM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }
    public void loadCredentials(){


        assert fc_netPlan != null;

        int rc = fc_netPlan.showOpenDialog(null);
        if (rc != JFileChooser.APPROVE_OPTION) return;


        try{
            byte [] bytes = Files.readAllBytes(fc_netPlan.getSelectedFile().toPath());
            String everything = cc.descifra(bytes);
            JSONObject jsonObject = new JSONObject(everything);
            //System.out.println("TopologyPanel loading  OSClientV3s " + jsonObject);
            callback.getOpenStackNet().AddJSONObjectOsClients(jsonObject);

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }

    }
    public void generateCredentials(){


        assert fc_netPlan != null;

        int rc = fc_netPlan.showOpenDialog(null);
        if (rc != JFileChooser.APPROVE_OPTION) return;

        try  {

            FileUtils.writeByteArrayToFile(new File(fc_netPlan.getSelectedFile().getAbsolutePath()), cc.cifra(callback.getOpenStackNet().getJSONObjectOsClients().toString()));
            System.out.println("TopologyPanel generated  OSClientV3s " + callback.getOpenStackNet().getJSONObjectOsClients());
        }catch(Exception ex){
            System.out.println(ex.toString());

        }

    }
    @Override
    public void actionPerformed(ActionEvent e) {

        Object src = e.getSource();

    }
}
