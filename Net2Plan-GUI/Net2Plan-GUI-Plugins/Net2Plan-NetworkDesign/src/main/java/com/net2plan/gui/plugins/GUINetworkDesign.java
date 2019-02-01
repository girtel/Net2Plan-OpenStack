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

package com.net2plan.gui.plugins;

  import com.net2plan.gui.GUINet2Plan;
import com.net2plan.gui.plugins.networkDesign.GUIWindow;
import com.net2plan.gui.plugins.networkDesign.NetworkDesignWindow;
import com.net2plan.gui.plugins.networkDesign.focusPane.FocusPane;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.interfaces.ITopologyCanvas;
import com.net2plan.gui.plugins.networkDesign.topologyPane.TopologyPanel;
import com.net2plan.gui.plugins.networkDesign.topologyPane.jung.CanvasFunction;
import com.net2plan.gui.plugins.networkDesign.topologyPane.jung.GUILink;
import com.net2plan.gui.plugins.networkDesign.topologyPane.jung.GUINode;
import com.net2plan.gui.plugins.networkDesign.topologyPane.jung.JUNGCanvas;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane.AJTableType;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.UndoRedoManager;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.VisualizationState;
import com.net2plan.gui.utils.ProportionalResizeJSplitPaneListener;
import com.net2plan.interfaces.networkDesign.*;
import com.net2plan.internal.Constants.NetworkElementType;
import com.net2plan.internal.ErrorHandling;
import com.net2plan.internal.plugins.IGUIModule;
import com.net2plan.internal.plugins.PluginSystem;
  import com.net2plan.utils.Pair;
import com.net2plan.utils.Triple;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.collections15.BidiMap;
import org.apache.commons.collections15.bidimap.DualHashBidiMap;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.util.List;

    /**
     * Targeted to evaluate the network designs generated by built-in or user-defined
     * static planning algorithms, deciding on aspects such as the network topology,
     * the traffic routing, link capacities, protection routes and so on. Algorithms
     * based on constrained optimization formulations (i.e. ILPs) can be fast-prototyped
     * using the open-source Java Optimization Modeler library, to interface
     * to a number of external solvers such as GPLK, CPLEX or IPOPT.
     *
     * @author Pablo
     */
    public class GUINetworkDesign extends IGUIModule
    {
        private final static String TITLE = "OpenStack Management Plugin";
        private final static int MAXSIZEUNDOLISTCHANGES = 0; // deactivate, not robust yet
        private final static int MAXSIZEUNDOLISTPICK = 10;

        private TopologyPanel topologyPanel;

        private FocusPane focusPanel;

        private ViewEditTopologyTablesPane viewEditTopTables;
        //private AboutIt aboutIt;

        private VisualizationState vs;
        private UndoRedoManager undoRedoManager;

        private NetPlan currentNetPlan;
        private OpenStackNet currentOpenStackNet;
        private WindowController windowController;
        private GUIWindow tableControlWindow;

        /**
         * Default constructor.
         *
         * @since 0.2.0
         */
        public GUINetworkDesign()
        {
            this(TITLE);
        }


        /**
         * Constructor that allows set a title for the tool in the top section of the panel.
         *
         * @param title Title of the tool (null or empty means no title)
         * @since 0.2.0
         */
        public GUINetworkDesign(String title)
        {
            super(title);
        }

        @Override
        public void start()
        {
            // Default start
            super.start();

            // Additional commands
            this.tableControlWindow.setLocationRelativeTo(this);
            this.tableControlWindow.showWindow(false);
        }



        @Override
        public void stop()
        {
            tableControlWindow.setVisible(false);
            windowController.hideAllWindows();
        }

        @Override
        public void configure(JPanel contentPane)
        {
            // Configuring PluginSystem for this plugin...
            try
            {
                // Add canvas plugin
                PluginSystem.addExternalPlugin(ITopologyCanvas.class);

                /* Add default canvas systems */
                PluginSystem.addPlugin(ITopologyCanvas.class, JUNGCanvas.class);
            } catch (RuntimeException ignored)
            {
                // NOTE: ITopologyCanvas has already been added. Meaning that JUNGCanvas has already been too.
            }
            this.currentNetPlan = new NetPlan();
            this.currentOpenStackNet = new OpenStackNet(this);


            BidiMap<NetworkLayer, Integer> mapLayer2VisualizationOrder = new DualHashBidiMap<>();
            Map<NetworkLayer, Boolean> layerVisibilityMap = new HashMap<>();
            for (NetworkLayer layer : currentNetPlan.getNetworkLayers())
            {
                mapLayer2VisualizationOrder.put(layer, mapLayer2VisualizationOrder.size());
                layerVisibilityMap.put(layer, true);
            }
            this.vs = new VisualizationState(currentNetPlan, mapLayer2VisualizationOrder, layerVisibilityMap, MAXSIZEUNDOLISTPICK);

            topologyPanel = new TopologyPanel(this, JUNGCanvas.class);

            JPanel leftPane = new JPanel(new BorderLayout());
            JPanel logSection = configureLeftBottomPanel();
            logSection = null;
            if (logSection == null)
            {
                leftPane.add(topologyPanel, BorderLayout.CENTER);
            } else
            {
                JSplitPane splitPaneTopology = new JSplitPane(JSplitPane.VERTICAL_SPLIT);
                splitPaneTopology.setTopComponent(topologyPanel);
                splitPaneTopology.setBottomComponent(logSection);
                splitPaneTopology.addPropertyChangeListener(new ProportionalResizeJSplitPaneListener());
                splitPaneTopology.setBorder(new LineBorder(contentPane.getBackground()));
                splitPaneTopology.setOneTouchExpandable(true);
                splitPaneTopology.setDividerSize(7);
                leftPane.add(splitPaneTopology, BorderLayout.CENTER);
            }
            contentPane.add(leftPane, "grow");

            viewEditTopTables = new ViewEditTopologyTablesPane(GUINetworkDesign.this, new BorderLayout());

            setDesign(currentNetPlan);
            Pair<BidiMap<NetworkLayer, Integer>, Map<NetworkLayer, Boolean>> res = VisualizationState.generateCanvasDefaultVisualizationLayerInfo(getDesign());
            vs.setCanvasLayerVisibilityAndOrder(getDesign(), res.getFirst(), res.getSecond());

            /* Initialize the undo/redo manager, and set its initial design */
            this.undoRedoManager = new UndoRedoManager(this, MAXSIZEUNDOLISTCHANGES);
            this.undoRedoManager.addNetPlanChange();

           // aboutIt = new AboutIt(this);

            final JTabbedPane tabPane = new JTabbedPane();
            tabPane.add(NetworkDesignWindow.getWindowName(NetworkDesignWindow.network), viewEditTopTables);
            //tabPane.add(NetworkDesignWindow.getWindowName(NetworkDesignWindow.whatif), aboutIt);

            // Installing customized mouse listener
            MouseListener[] ml = tabPane.getListeners(MouseListener.class);

            for (MouseListener mouseListener : ml)
            {
                tabPane.removeMouseListener(mouseListener);
            }

            // Left click works as usual, right click brings up a pop-up menu.
            tabPane.addMouseListener(new MouseAdapter()
            {
                public void mousePressed(MouseEvent e)
                {
                    JTabbedPane tabPane = (JTabbedPane) e.getSource();

                    int tabIndex = tabPane.getUI().tabForCoordinate(tabPane, e.getX(), e.getY());

                    if (tabIndex >= 0 && tabPane.isEnabledAt(tabIndex))
                    {
                        if (tabIndex == tabPane.getSelectedIndex())
                        {
                            if (tabPane.isRequestFocusEnabled())
                            {
                                tabPane.requestFocus();

                                tabPane.repaint(tabPane.getUI().getTabBounds(tabPane, tabIndex));
                            }
                        } else
                        {
                            tabPane.setSelectedIndex(tabIndex);
                        }

                        if (!tabPane.isEnabled() || SwingUtilities.isRightMouseButton(e))
                        {
                            final JPopupMenu popupMenu = new JPopupMenu();

                            final JMenuItem popWindow = new JMenuItem("Pop window out");
                            popWindow.addActionListener(e1 ->
                            {
                                final int selectedIndex = tabPane.getSelectedIndex();
                                final String tabName = tabPane.getTitleAt(selectedIndex);

                                // Pops up the selected tab.
                                final NetworkDesignWindow networkDesignWindow = NetworkDesignWindow.parseString(tabName);

                                if (networkDesignWindow != null)
                                {
                                    switch (networkDesignWindow)
                                    {
                                        case whatif:
                                            windowController.showAboutItWindow(true);
                                            break;
                                        default:
                                            return;
                                    }
                                }

                                tabPane.setSelectedIndex(0);
                            });

                            // Disabling the pop up button for the network state tab.
                            if (NetworkDesignWindow.parseString(tabPane.getTitleAt(tabPane.getSelectedIndex())) == NetworkDesignWindow.network)
                            {
                                popWindow.setEnabled(false);
                            }

                            popupMenu.add(popWindow);

                            popupMenu.show(e.getComponent(), e.getX(), e.getY());
                        }
                    }
                }
            });

            // Building windows
            this.tableControlWindow = new GUIWindow(tabPane)
            {
                @Override
                public String getTitle()
                {
                    return "Net2Plan - Design tables and control window";
                }
            };

            // Building tab controller
           // this.windowController = new WindowController(aboutIt);

            addKeyCombinationActions();
            updateVisualizationAfterNewTopology();
        }



        public OpenStackNet getOpenStackNet() { return this.currentOpenStackNet; }
        public void clearDesign(){
            this.currentNetPlan = new NetPlan();
            this.currentOpenStackNet = new OpenStackNet(this);
            this.updateTopologyAndTables();
        }
        private JPanel configureLeftBottomPanel()
        {
            this.focusPanel = new FocusPane(this);
            final JPanel focusPanelContainer = new JPanel(new BorderLayout());
            final JToolBar navigationToolbar = new JToolBar(JToolBar.VERTICAL);
            navigationToolbar.setRollover(true);
            navigationToolbar.setFloatable(false);
            navigationToolbar.setOpaque(false);

            final JButton btn_pickNavigationUndo, btn_pickNavigationRedo;

            btn_pickNavigationUndo = new JButton("");
            btn_pickNavigationUndo.setIcon(new ImageIcon(TopologyPanel.class.getResource("/resources/gui/undoPick.png")));
            btn_pickNavigationUndo.setToolTipText("Navigate back to the previous element picked");
            btn_pickNavigationRedo = new JButton("");
            btn_pickNavigationRedo.setIcon(new ImageIcon(TopologyPanel.class.getResource("/resources/gui/redoPick.png")));
            btn_pickNavigationRedo.setToolTipText("Navigate forward to the next element picked");

            final ActionListener action = e ->
            {
                Object backOrForward;
                do
                {
                    backOrForward = (e.getSource() == btn_pickNavigationUndo) ? GUINetworkDesign.this.getVisualizationState().getPickNavigationBackElement() : GUINetworkDesign.this.getVisualizationState().getPickNavigationForwardElement();
                    if (backOrForward == null) break;
                    final NetworkElement ne = backOrForward instanceof NetworkElement ? (NetworkElement) backOrForward : null; // For network elements
                    final Pair<Demand, Link> fr = backOrForward instanceof Pair ? (Pair) backOrForward : null; // For forwarding rules
                    if (ne != null)
                    {
                        if (ne.getNetPlan() != GUINetworkDesign.this.getDesign()) continue;
                        if (ne.getNetPlan() == null) continue;
                        break;
                    } else if (fr != null)
                    {
                        if (fr.getFirst().getNetPlan() != GUINetworkDesign.this.getDesign()) continue;
                        if (fr.getFirst().getNetPlan() == null) continue;
                        if (fr.getSecond().getNetPlan() != GUINetworkDesign.this.getDesign()) continue;
                        if (fr.getSecond().getNetPlan() == null) continue;
                        break;
                    } else break; // null,null => reset picked state
                } while (true);
                if (backOrForward != null)
                {
                    final NetworkElement ne = backOrForward instanceof NetworkElement ? (NetworkElement) backOrForward : null; // For network elements
                    final Pair<Demand, Link> fr = backOrForward instanceof Pair ? (Pair) backOrForward : null; // For forwarding rules

                    if (ne != null)
                        GUINetworkDesign.this.getVisualizationState().pickElement(ne);
                    else if (fr != null)
                        GUINetworkDesign.this.getVisualizationState().pickForwardingRule(fr);
                    else GUINetworkDesign.this.getVisualizationState().resetPickedState();

                    GUINetworkDesign.this.updateVisualizationAfterPick();
                }
            };

            btn_pickNavigationUndo.addActionListener(action);
            btn_pickNavigationRedo.addActionListener(action);

            btn_pickNavigationRedo.setFocusable(false);
            btn_pickNavigationUndo.setFocusable(false);

            navigationToolbar.add(btn_pickNavigationUndo);
            navigationToolbar.add(btn_pickNavigationRedo);

            final JScrollPane scPane = new JScrollPane(focusPanel, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
            scPane.getVerticalScrollBar().setUnitIncrement(20);
            scPane.getHorizontalScrollBar().setUnitIncrement(20);
            scPane.setBorder(BorderFactory.createEmptyBorder());

            // Control the scroll
            scPane.getHorizontalScrollBar().addAdjustmentListener(e ->
            {
                // Repaints the panel each time the horizontal scroll bar is moves, in order to avoid ghosting.
                focusPanelContainer.revalidate();
                focusPanelContainer.repaint();
            });

            focusPanelContainer.add(navigationToolbar, BorderLayout.WEST);
            focusPanelContainer.add(scPane, BorderLayout.CENTER);

            JPanel pane = new JPanel(new MigLayout("fill, insets 0 0 0 0"));
            pane.setBorder(BorderFactory.createTitledBorder(new LineBorder(Color.BLACK), "Focus panel"));

            pane.add(focusPanelContainer, "grow");
            return pane;
        }

        @Override
        public String getDescription()
        {
            return getName();
        }

        @Override
        public KeyStroke getKeyStroke()
        {
            return KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.ALT_DOWN_MASK);
        }

        @Override
        public String getMenu()
        {

            return "Tools|" + TITLE;
        }

        @Override
        public String getName()
        {
            return TITLE + " (GUI)";
        }

        @Override
        public List<Triple<String, String, String>> getParameters()
        {
            return null;
        }

        @Override
        public int getPriority()
        {
            return Integer.MAX_VALUE;
        }


        public NetPlan getDesign()
        {
            return currentNetPlan;
        }

        public NetPlan getInitialDesign()
        {
            return null;
        }

        public void addNetPlanChange()
        {
            undoRedoManager.addNetPlanChange();
        }

        public void requestUndoAction()
        {

            final Triple<NetPlan, Map<NetworkLayer, Integer>, Map<NetworkLayer, Boolean>> back = undoRedoManager.getNavigationBackElement();
            if (back == null) return;
            this.currentNetPlan = back.getFirst();
            this.vs.setCanvasLayerVisibilityAndOrder(this.currentNetPlan, back.getSecond(), back.getThird());
            updateVisualizationAfterNewTopology();
        }

        public void requestRedoAction()
        {

            final Triple<NetPlan, Map<NetworkLayer, Integer>, Map<NetworkLayer, Boolean>> forward = undoRedoManager.getNavigationForwardElement();
            if (forward == null) return;
            this.currentNetPlan = forward.getFirst();
            this.vs.setCanvasLayerVisibilityAndOrder(this.currentNetPlan, forward.getSecond(), forward.getThird());
            updateVisualizationAfterNewTopology();
        }

        public void setDesign(NetPlan netPlan)
        {
            if (ErrorHandling.isDebugEnabled()) netPlan.checkCachesConsistency();
            this.currentNetPlan = netPlan;
        }


        public VisualizationState getVisualizationState()
        {
            return vs;
        }

        public void showTableControlWindow()
        {
            tableControlWindow.showWindow(true);
        }

        private void resetButton()
        {
            try
            {
                final int result = JOptionPane.showConfirmDialog(null, "Are you sure you want to reset? This will remove all unsaved data", "Reset", JOptionPane.YES_NO_OPTION);
                if (result != JOptionPane.YES_OPTION) return;


                setDesign(new NetPlan());

            } catch (Throwable ex)
            {
                ErrorHandling.addErrorOrException(ex, GUINetworkDesign.class);
                ErrorHandling.showErrorDialog("Unable to reset");
            }
            Pair<BidiMap<NetworkLayer, Integer>, Map<NetworkLayer, Boolean>> res = VisualizationState.generateCanvasDefaultVisualizationLayerInfo(getDesign());
            vs.setCanvasLayerVisibilityAndOrder(getDesign(), res.getFirst(), res.getSecond());
            updateVisualizationAfterNewTopology();
            undoRedoManager.addNetPlanChange();
        }

        public ViewEditTopologyTablesPane getViewEditTopTables(){
            return this.viewEditTopTables;
        }
        public void updateTopologyAndTables(){
            getDesign().removeAllNodes();

            final VisualizationState vs = getVisualizationState();
            Pair<BidiMap<NetworkLayer, Integer>, Map<NetworkLayer, Boolean>> res =
                    vs.suggestCanvasUpdatedVisualizationLayerInfoForNewDesign(new HashSet<>(getDesign().getNetworkLayers()));
            vs.setCanvasLayerVisibilityAndOrder(getDesign(), res.getFirst(), res.getSecond());
            updateVisualizationAfterNewTopology();
            addNetPlanChange();
            getViewEditTopTables().updateView();
        }
       /* public AboutIt getAboutIt(){
            return this.aboutIt;
        }*/
        public void resetPickedStateAndUpdateView()
        {
            vs.resetPickedState();
            topologyPanel.getCanvas().cleanSelection();
            viewEditTopTables.resetPickedState();
        }

        /**
         * Indicates whether or not the initial {@code NetPlan} object is stored to be
         * compared with the current one (i.e. after some simulation steps).
         *
         * @return {@code true} if the initial {@code NetPlan} object is stored. Otherwise, {@code false}.
         * @since 0.3.0
         */


        private void addKeyCombinationActions()
        {
            addKeyCombinationAction("Resets the tool", new AbstractAction()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    resetButton();
                }
            }, KeyStroke.getKeyStroke(KeyEvent.VK_R, InputEvent.CTRL_DOWN_MASK));

            addKeyCombinationAction("Outputs current design to console", new AbstractAction()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    System.out.println(getDesign().toString());
                }
            }, KeyStroke.getKeyStroke(KeyEvent.VK_F11, InputEvent.CTRL_DOWN_MASK));


            // Windows
            addKeyCombinationAction("Show control window", new AbstractAction()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    tableControlWindow.showWindow(true);
                }
            }, KeyStroke.getKeyStroke(KeyEvent.VK_1, InputEvent.SHIFT_MASK | InputEvent.ALT_MASK));

            GUINet2Plan.addGlobalActions(this.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW), this.getActionMap());

            viewEditTopTables.setInputMap(WHEN_IN_FOCUSED_WINDOW, this.getInputMap(WHEN_IN_FOCUSED_WINDOW));
            viewEditTopTables.setActionMap(this.getActionMap());


            //aboutIt.setInputMap(WHEN_IN_FOCUSED_WINDOW, this.getInputMap(WHEN_IN_FOCUSED_WINDOW));
            //aboutIt.setActionMap(this.getActionMap());
        }

        public void putTransientColorInElementTopologyCanvas(Collection<? extends NetworkElement> linksAndNodes, Color color)
        {
            for (NetworkElement e : linksAndNodes)
            {
                if (e instanceof Link)
                {
                    final GUILink gl = vs.getCanvasAssociatedGUILink((Link) e);
                    if (gl != null)
                    {
                        gl.setEdgeDrawPaint(color);
                    }
                } else if (e instanceof Node)
                {
                    for (GUINode gn : vs.getCanvasVerticallyStackedGUINodes((Node) e))
                    {
                        gn.setBorderPaint(color);
                        gn.setFillPaint(color);
                    }
                } else throw new RuntimeException();
            }

            resetPickedStateAndUpdateView();
        }

        public void updateVisualizationAfterPick()
        {
            final NetworkElementType type = vs.getPickedElementType();
            topologyPanel.getCanvas().refresh(); // needed with or w.o. pick, since maybe you unpick with an undo
            topologyPanel.updateTopToolbar();
            focusPanel.updateView();
        }

        public void updateVisualizationAfterNewTopology()
        {
            vs.updateTableRowFilter(null, null);
            topologyPanel.updateMultilayerPanel();
            topologyPanel.getCanvas().rebuildGraph();
            topologyPanel.getCanvas().zoomAll();
            //viewEditTopTables.updateView();
           // aboutIt.updateView();
            focusPanel.updateView();
        }

        public void updateVisualizationAfterCanvasState()
        {
            topologyPanel.updateTopToolbar();
        }

        public void updateVisualizationJustCanvasLinkNodeVisibilityOrColor()
        {
            topologyPanel.getCanvas().refresh();
        }

        public void updateVisualizationAfterChanges(Set<NetworkElementType> modificationsMade)
        {
            if (modificationsMade == null)
            {
                throw new RuntimeException("Unable to update non-existent network elements");
            }

            if (modificationsMade.contains(NetworkElementType.LAYER))
            {
                topologyPanel.updateMultilayerPanel();
                topologyPanel.getCanvas().rebuildGraph();
                viewEditTopTables.updateView();
               // aboutIt.updateView();
                focusPanel.updateView();
            } else if ((modificationsMade.contains(NetworkElementType.LINK) || modificationsMade.contains(NetworkElementType.NODE) || modificationsMade.contains(NetworkElementType.LAYER)))
            {
                topologyPanel.getCanvas().rebuildGraph();
                viewEditTopTables.updateView();

                //aboutIt.updateView();
                focusPanel.updateView();
            } else
            {
                viewEditTopTables.updateView();

                //aboutIt.updateView();
                focusPanel.updateView();
            }
        }

        public void runCanvasOperation(CanvasFunction operation)
        {
            switch (operation)
            {
                case ZOOM_ALL:
                    topologyPanel.getCanvas().zoomAll();
                    break;
                case ZOOM_IN:
                    topologyPanel.getCanvas().zoomIn();
                    break;
                case ZOOM_OUT:
                    topologyPanel.getCanvas().zoomOut();
                    break;
            }
        }

        public void updateVisualizationJustTables()
        {
            viewEditTopTables.updateView();
        }


        private class WindowController
        {
            private GUIWindow aboutItWindow;

             private final JComponent aboutItWindowComponent;

            WindowController(final JComponent aboutIt)
            {

                this.aboutItWindowComponent = aboutIt;
            }




            private void buildAboutItfWindow(final JComponent component)
            {
                final String tabName = NetworkDesignWindow.getWindowName(NetworkDesignWindow.whatif);

                aboutItWindow = new GUIWindow(component)
                {
                    @Override
                    public String getTitle()
                    {
                        return "Net2Plan - " + tabName;
                    }
                };

                aboutItWindow.addWindowListener(new CloseWindowAdapter(tabName, component));
            }

            void showAboutItWindow(final boolean gainFocus)
            {
                buildAboutItfWindow(aboutItWindowComponent);
                if (aboutItWindow != null)
                {
                    aboutItWindow.showWindow(gainFocus);
                    aboutItWindow.setLocationRelativeTo(tableControlWindow);
                }
            }




            void hideAllWindows()
            {
                if (aboutItWindow != null)
                    aboutItWindow.dispatchEvent(new WindowEvent(aboutItWindow, WindowEvent.WINDOW_CLOSING));
            }

            private class CloseWindowAdapter extends WindowAdapter
            {
                private final String tabName;
                private final JComponent component;

                private final NetworkDesignWindow[] tabCorrectOrder =
                        {NetworkDesignWindow.network, NetworkDesignWindow.whatif};

                CloseWindowAdapter(final String tabName, final JComponent component)
                {
                    this.tabName = tabName;
                    this.component = component;
                }

                @Override
                public void windowClosing(WindowEvent e)
                {
                    addTabToControlWindow(tabName, component);
                }

                private void addTabToControlWindow(final String newTabName, final JComponent newTabComponent)
                {
                    final JTabbedPane tabPane = (JTabbedPane) tableControlWindow.getInnerComponent();

                    final Map<String, Component> toSortTabs = new HashMap<>();
                    toSortTabs.put(newTabName, newTabComponent);

                    for (int i = 0; i < tabPane.getTabCount(); i = 0)
                    {
                        toSortTabs.put(tabPane.getTitleAt(i), tabPane.getComponentAt(i));
                        tabPane.remove(i);
                    }

                    for (int i = 0; i < tabCorrectOrder.length; i++)
                    {
                        final String tabName = NetworkDesignWindow.getWindowName(tabCorrectOrder[i]);

                        if (toSortTabs.containsKey(tabName))
                        {
                            final Component tabComponent = toSortTabs.get(tabName);

                            tabPane.addTab(tabName, tabComponent);
                        }
                    }
                }
            }
        }
    }
