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
public class TopologyPanel extends JPanel
{
    private final GUINetworkDesign callback;
    private final ITopologyCanvas canvas;

    private final JPanel canvasPanel;

    private final TopologyTopBar topBar;
    private final TopologySideBar sideBar;

    private final CipherClass cc = new CipherClass();
    JPanel jp1;
    JButton jbP1, jbP2, jbP3;
    JTextField os_username, os_auth_url,os_project_name,os_user_domain_name,os_project_domain_id;
    JPasswordField os_password;

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
            if (callback.getVisualizationState().isNetPlanEditable() && this.getCanvas() instanceof JUNGCanvas)
                addPlugin(new AddLinkGraphPlugin(callback, canvas, MouseEvent.BUTTON1_MASK, MouseEvent.BUTTON1_MASK | MouseEvent.SHIFT_MASK));

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
    public void loadCredentials(){

        JFrame jfM = new JFrame("Credentials");
        jfM.setLayout(null);


        openStackFormView();  //invocamos los metodos que contienen los paneles

        jbP1 = new JButton("Enter");
        jbP2 = new JButton("Load");
        jbP3 = new JButton("Generate");

        jp1.setBounds(10, 10, 200, 200);

        jbP1.setBounds(75, 250, 90, 20);
        jbP2.setBounds(25, 225, 90, 20);
        jbP3.setBounds(125, 225, 90, 20);
        jbP1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                callback.connectToOpenStack(os_auth_url.getText(), os_username.getText(), String.valueOf(os_password.getPassword()), os_project_name.getText(), os_user_domain_name.getText(), os_project_domain_id.getText());

                 final VisualizationState vs = callback.getVisualizationState();
                Pair<BidiMap<NetworkLayer, Integer>, Map<NetworkLayer, Boolean>> res =
                        vs.suggestCanvasUpdatedVisualizationLayerInfoForNewDesign(new HashSet<>(callback.getDesign().getNetworkLayers()));
                vs.setCanvasLayerVisibilityAndOrder(callback.getDesign(), res.getFirst(), res.getSecond());


                callback.updateVisualizationAfterNewTopology();
                callback.addNetPlanChange();
                callback.getWhat().updateText();
                jfM.dispose();

            }});
        jbP2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                assert fc_netPlan != null;

                int rc = fc_netPlan.showOpenDialog(null);
                if (rc != JFileChooser.APPROVE_OPTION) return;


                try{
                    byte [] bytes = Files.readAllBytes(fc_netPlan.getSelectedFile().toPath());
                    String everything = cc.descifra(bytes);
                    JSONObject jsonObject = new JSONObject(everything);
                    os_username.setText(jsonObject.getString("os_username"));
                    String password = "";
                    JSONArray jA = jsonObject.getJSONArray("os_password");

                    for(int i = 0; i < jA.length(); i++) {
                        password = password.concat(jA.getString(i));
                    }

                    os_password.setText(password);
                    os_auth_url.setText(jsonObject.getString("os_auth_url"));
                    os_project_name.setText(jsonObject.getString("os_project_name"));
                    os_user_domain_name.setText(jsonObject.getString("os_user_domain_name"));
                    os_project_domain_id.setText(jsonObject.getString("os_project_domain_id"));


                } catch (Exception ex) {
                    System.out.println(ex.toString());
                }



            }});
        jbP3.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                assert fc_netPlan != null;

                int rc = fc_netPlan.showOpenDialog(null);
                if (rc != JFileChooser.APPROVE_OPTION) return;


                JSONObject jsonObject = new JSONObject();
                jsonObject.put("os_username",os_username.getText());
                jsonObject.put("os_password",os_password.getPassword());
                jsonObject.put("os_auth_url",os_auth_url.getText());
                jsonObject.put("os_project_name",os_project_name.getText());
                jsonObject.put("os_user_domain_name",os_user_domain_name.getText());
                jsonObject.put("os_project_domain_id",os_project_domain_id.getText());

                try  {

                    FileUtils.writeByteArrayToFile(new File(fc_netPlan.getSelectedFile().getAbsolutePath()), cc.cifra(jsonObject.toString()));

                }catch(Exception ex){
                    System.out.println(ex.toString());

                }

            }});

        jfM.add(jp1);
        jfM.add(jbP1);
        jfM.add(jbP2);
        jfM.add(jbP3);

        ImageIcon img = new ImageIcon(getClass().getResource("/resources/common/openstack_logo.png"));
        jfM.setIconImage(img.getImage());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        jfM.setSize(250, 320);
        jfM.setLocation(dim.width/2-jfM.getSize().width/2, dim.height/2-jfM.getSize().height/2);

        jfM.setResizable(false);
        jfM.setVisible(true);
        jfM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }
    public void openStackFormView(){

        jp1 = new JPanel(new GridLayout(6, 2, 30, 10));//filas, columnas, espacio entre filas, espacio entre columnas

        JLabel l = new JLabel("OS_USERNAME",  SwingConstants.LEFT);
        jp1.add(l);
        os_username = new JTextField(10);
        l.setLabelFor(os_username);
        jp1.add(os_username);

        JLabel l2 = new JLabel("OS_PASSWORD",  SwingConstants.LEFT);
        jp1.add(l2);
        os_password = new JPasswordField();
        jp1.add(os_password);

        JLabel l3 = new JLabel("OS_AUTH_URL",  SwingConstants.LEFT);
        jp1.add(l3);
        os_auth_url = new JTextField();
        jp1.add(os_auth_url);

        JLabel l4 = new JLabel("OS_PROJECT_NAME",  SwingConstants.LEFT);
        jp1.add(l4);
        os_project_name = new JTextField();
        jp1.add(os_project_name);

        JLabel l5 = new JLabel("OS_U_DOMAIN_NAME", SwingConstants.LEFT);
        jp1.add(l5);
        os_user_domain_name = new JTextField();
        jp1.add(os_user_domain_name);

        JLabel l6 = new JLabel("OS_P_DOMAIN_ID ", SwingConstants.LEFT);
        jp1.add(l6);
        os_project_domain_id = new JTextField();
        jp1.add(os_project_domain_id);

        jp1.setVisible(true);
    }

}
