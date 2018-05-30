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


package com.net2plan.gui.plugins.networkDesign.topologyPane.plugins;

import com.google.common.collect.Sets;
import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.interfaces.ITopologyCanvas;
import com.net2plan.gui.plugins.networkDesign.interfaces.ITopologyCanvasPlugin;
import com.net2plan.gui.plugins.networkDesign.topologyPane.jung.CanvasFunction;
import com.net2plan.gui.plugins.networkDesign.topologyPane.jung.GUILink;
import com.net2plan.gui.plugins.networkDesign.topologyPane.jung.GUINode;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.VisualizationState;
import com.net2plan.interfaces.networkDesign.Link;
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.interfaces.networkDesign.NetworkLayer;
import com.net2plan.interfaces.networkDesign.Node;
import com.net2plan.internal.Constants.NetworkElementType;
import com.net2plan.utils.Pair;
import org.apache.commons.collections15.BidiMap;
import org.json.JSONObject;
import org.openstack4j.api.Builders;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Plugin for the popup menu of the canvas.
 *
 * @author Pablo Pavon-Marino, Jose-Luis Izquierdo-Zaragoza
 * @since 0.3.1
 */
public class PopupMenuPlugin extends MouseAdapter implements ITopologyCanvasPlugin
{
    private final GUINetworkDesign callback;
    private final ITopologyCanvas canvas;

    /**
     * Default constructor.
     *
     * @param callback Reference to the class handling change events.
     * @since 0.3.1
     */
    public PopupMenuPlugin(GUINetworkDesign callback , ITopologyCanvas canvas)
    {
        this.callback = callback;
        this.canvas = canvas;
    }

    @Override
    public boolean checkModifiers(MouseEvent e) {
        return SwingUtilities.isRightMouseButton(e);
    }


    @Override
    public int getModifiers() {
        throw new UnsupportedOperationException("Not supported yet");
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
        if (checkModifiers(e))
        {
            final Point p = e.getPoint();
            final Point2D positionInNetPlanCoordinates = canvas.getCanvasPointFromNetPlanPoint(p);
            final GUINode gn = canvas.getVertex(e);
            final Node node = gn == null ? null : gn.getAssociatedNode();
            final GUILink gl = canvas.getEdge(e);
            final Link link = gl == null ? null : gl.isIntraNodeLink()? null : gl.getAssociatedNetPlanLink();

            List<JComponent> actions;
            if (node != null) {
                actions = getNodeActions(node, positionInNetPlanCoordinates);
            } else if (link != null) {
                actions = getLinkActions(link, positionInNetPlanCoordinates);
            } else {
            	callback.resetPickedStateAndUpdateView();
                actions = getCanvasActions(positionInNetPlanCoordinates);
            }

            if (actions == null || actions.isEmpty()) return;

            final JPopupMenu popup = new JPopupMenu();
            for (JComponent action : actions)
                popup.add(action);

            popup.show(canvas.getCanvasComponent(), e.getX(), e.getY());
            e.consume();
        }
    }

    @Override
    public void setModifiers(int modifiers)
    {
        throw new UnsupportedOperationException("Not supported yet");
    }

    private List<JComponent> getNodeActions(Node node , Point2D pos)
    {
        final List<JComponent> actions = new LinkedList<JComponent>();
        final VisualizationState vs = callback.getVisualizationState();
        if (!vs.isNetPlanEditable()) return actions;
        if (vs.isWhatIfAnalysisActive()) return actions;

    	final NetPlan netPlan = callback.getDesign();
        actions.add(new JMenuItem(new RemoveNodeAction("Remove node", node)));

        if (netPlan.getNumberOfNodes() > 1)
        {
            actions.add(new JPopupMenu.Separator());
            /*JMenu unidirectionalMenu = new JMenu("Create unidirectional link");
            JMenu bidirectionalMenu = new JMenu("Create bidirectional link");*/

            String nodeName = node.getName() == null ? "" : node.getName();
            String nodeString = Long.toString(node.getId()) + (nodeName.isEmpty() ? "" : " (" + nodeName + ")");

            final NetworkLayer layer = netPlan.getNetworkLayerDefault();
            for (Node auxNode : netPlan.getNodes())
            {
                if (auxNode == node) continue;

                String auxNodeName = auxNode.getName() == null ? "" : auxNode.getName();
                String auxNodeString = Long.toString(auxNode.getId()) + (auxNodeName.isEmpty() ? "" : " (" + auxNodeName + ")");

                AbstractAction unidirectionalAction = new AddLinkAction(nodeString + " => " + auxNodeString, layer, node, auxNode);
                //unidirectionalMenu.add(unidirectionalAction);

                AbstractAction bidirectionalAction = new AddLinkBidirectionalAction(nodeString + " <=> " + auxNodeString, layer, node, auxNode);
               // bidirectionalMenu.add(bidirectionalAction);
            }

          /*  actions.add(unidirectionalMenu);
            actions.add(bidirectionalMenu);*/
        }

        return actions;
    }

    private List<JComponent> getLinkActions(Link link, Point2D pos)
    {
        List<JComponent> actions = new LinkedList<JComponent>();
        final VisualizationState vs = callback.getVisualizationState();
        if (!vs.isNetPlanEditable()) return actions;
        if (vs.isWhatIfAnalysisActive()) return actions;

        actions.add(new JMenuItem(new RemoveLinkAction("Remove link", link)));

        return actions;
    }

    private class AddLinkAction extends AbstractAction
    {
        private final NetworkLayer layer;
        private final Node originNode;
        private final Node destinationNode;

        public AddLinkAction(String name, NetworkLayer layer, Node originNode, Node destinationNode)
        {
            super(name);
            this.layer = layer;
            this.originNode = originNode;
            this.destinationNode = destinationNode;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
        	originNode.getNetPlan().addLink(originNode , destinationNode , 0 , 0 , 200000 , null , layer);
        	callback.getVisualizationState().recomputeCanvasTopologyBecauseOfLinkOrNodeAdditionsOrRemovals();
            callback.updateVisualizationAfterChanges(Sets.newHashSet(NetworkElementType.LINK));
            callback.addNetPlanChange();
        }
    }

    private class AddLinkBidirectionalAction extends AbstractAction
    {
        private final NetworkLayer layer;
        private final Node originNode;
        private final Node destinationNode;

        public AddLinkBidirectionalAction(String name, NetworkLayer layer, Node originNode, Node destinationNode)
        {
            super(name);
            this.layer = layer;
            this.originNode = originNode;
            this.destinationNode = destinationNode;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
        	originNode.getNetPlan().addLinkBidirectional(originNode , destinationNode , 0 , 0 , 200000 , null , layer);
        	callback.getVisualizationState().recomputeCanvasTopologyBecauseOfLinkOrNodeAdditionsOrRemovals();
            callback.updateVisualizationAfterChanges(Sets.newHashSet(NetworkElementType.NODE));
            callback.addNetPlanChange();
        }

    }

    public List<JComponent> getCanvasActions(Point2D positionInNetPlanCoordinates)
    {
        final List<JComponent> actions = new LinkedList<>();
        final VisualizationState vs = callback.getVisualizationState();
        if (!vs.isNetPlanEditable()) return actions;
        if (vs.isWhatIfAnalysisActive()) return actions;

            JMenuItem addNode = new JMenuItem(new AddNodeAction("Add router here", positionInNetPlanCoordinates));
             actions.add(addNode);
             actions.add(new JPopupMenu.Separator());

        JMenu topologySettingMenu = new JMenu("Change topology layout");

        JMenuItem circularSetting = new JMenuItem("Circular");
        circularSetting.addActionListener(e ->
        {
        	final List<Node> nodes = callback.getDesign().getNodes();
        	final double angStep = 360.0 / nodes.size();
        	final double radius = 10; // PABLO: THIS SHOUD BE SET IN OTHER COORDINATES?
            for (int i = 0; i < nodes.size(); i++)
            	nodes.get(i).setXYPositionMap(new Point2D.Double(positionInNetPlanCoordinates.getX() + radius * Math.cos(Math.toRadians(angStep*i)) , positionInNetPlanCoordinates.getY() + radius * Math.sin(Math.toRadians(angStep*i))));
        	callback.getVisualizationState().recomputeCanvasTopologyBecauseOfLinkOrNodeAdditionsOrRemovals();
            callback.updateVisualizationAfterChanges(Sets.newHashSet(NetworkElementType.NODE));
            callback.runCanvasOperation(CanvasFunction.ZOOM_ALL);
            callback.addNetPlanChange();
         });

        topologySettingMenu.add(circularSetting);

        actions.add(topologySettingMenu);

        return actions;
    }

    private class AddNodeAction extends AbstractAction
   {
            private final Point2D positionInNetPlanCoordinates;

                    public AddNodeAction(String name, Point2D positionInNetPlanCoordinates)
                    {
                        super(name);
                       this.positionInNetPlanCoordinates = positionInNetPlanCoordinates;
           }

                   @Override
           public void actionPerformed(ActionEvent e)
           {
                        //canvas.addNode(positionInNetPlanCoordinates);
                       addNewRouter();
           }
        }
    private class RemoveNodeAction extends AbstractAction
    {
        private final Node node;

        public RemoveNodeAction(String name, Node node)
        {
            super(name);
            this.node = node;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
           // canvas.removeNode(node);
            removeRouter(node);
        }
    }
    private class RemoveLinkAction extends AbstractAction
    {
        private final Link link;

        public RemoveLinkAction(String name, Link link)
        {
            super(name);
            this.link = link;
        }

        @Override
        public void actionPerformed(ActionEvent e)
        {
            link.remove();
        	callback.getVisualizationState().recomputeCanvasTopologyBecauseOfLinkOrNodeAdditionsOrRemovals();
        	callback.updateVisualizationAfterChanges(Sets.newHashSet(NetworkElementType.LINK));
        	callback.addNetPlanChange();
        }
    }
    public void addNewRouter(){
        JFrame jfM = new JFrame("Add router");
        jfM.setLayout(null);

        JButton jbP1 = new JButton("Enter");

        JPanel jp1 = new JPanel(new GridLayout(6, 2, 30, 10));//filas, columnas, espacio entre filas, espacio entre columnas
        JLabel l6 = new JLabel("Properties", SwingConstants.LEFT);
        jp1.add(l6);
        JLabel label = new JLabel("", SwingConstants.LEFT);
        jp1.add(label);
        JLabel labelName = new JLabel("Name", SwingConstants.LEFT);
        jp1.add(labelName);
        JTextField os_name_change = new JTextField();
        jp1.add(os_name_change);

        jp1.setVisible(true);
        jp1.setBounds(10, 10, 200, 200);
        jbP1.setBounds(75, 200, 90, 25);

        jbP1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Name",os_name_change.getText());
                callback.getOpenStackNet().getOpenStackNetCreate().createOpenStackRouter(jsonObject);

                callback.getOpenStackNet().refreshListTable();
                callback.getViewEditTopTables().updateView();

                final VisualizationState vs = callback.getVisualizationState();
                Pair<BidiMap<NetworkLayer, Integer>, Map<NetworkLayer, Boolean>> res =
                        vs.suggestCanvasUpdatedVisualizationLayerInfoForNewDesign(new HashSet<>(callback.getDesign().getNetworkLayers()));
                vs.setCanvasLayerVisibilityAndOrder(callback.getDesign(), res.getFirst(), res.getSecond());
                callback.updateVisualizationAfterNewTopology();
                callback.addNetPlanChange();
                jfM.dispose();
            }});

        jfM.add(jbP1);
        jfM.add(jp1);

        ImageIcon img = new ImageIcon(getClass().getResource("/resources/common/openstack_logo.png"));
        jfM.setIconImage(img.getImage());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        jfM.setSize(250, 275);
        jfM.setLocation(dim.width/2-jfM.getSize().width/2, dim.height/2-jfM.getSize().height/2);

        jfM.setResizable(false);
        jfM.setVisible(true);
        jfM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }
    public void removeRouter(Node node){


        callback.getOpenStackNet().getOpenStackNetDelete().deleteOpenStackRouter(node.getName());

        callback.getOpenStackNet().refreshListTable();
        callback.getViewEditTopTables().updateView();

        final VisualizationState vs = callback.getVisualizationState();
                Pair<BidiMap<NetworkLayer, Integer>, Map<NetworkLayer, Boolean>> res =
                        vs.suggestCanvasUpdatedVisualizationLayerInfoForNewDesign(new HashSet<>(callback.getDesign().getNetworkLayers()));
                vs.setCanvasLayerVisibilityAndOrder(callback.getDesign(), res.getFirst(), res.getSecond());
                callback.updateVisualizationAfterNewTopology();
                callback.addNetPlanChange();

    }
}
