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


package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.*;

import javax.swing.*;


import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackNetwork;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackPort;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackRouter;
import com.net2plan.gui.plugins.networkDesign.interfaces.ITableRowFilter;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackSubnet;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane.AJTableType;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.VisualizationState;
import com.net2plan.gui.utils.JScrollPopupMenu;
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.interfaces.networkDesign.NetworkElement;
import com.net2plan.interfaces.networkDesign.NetworkLayer;
import com.net2plan.utils.Pair;
import org.apache.commons.collections15.BidiMap;
import org.json.JSONObject;

import java.util.List;
import java.util.stream.Collectors;


/**
 */
@SuppressWarnings("unchecked")
public abstract class AdvancedJTable_networkElement<T extends OpenStackNetworkElement> extends AdvancedJTable_abstractElement<T>
{
    protected final AJTableType ajtType;

    public AdvancedJTable_networkElement(GUINetworkDesign networkViewer, AJTableType ajtType , boolean hasAggregationRow)
    {
        super(networkViewer, ajtType.getTabName() , hasAggregationRow);
        this.ajtType = ajtType;
        updateView();
    }

    @Override
    protected void addExtendedKeyboardActions()
    {
        final InputMap inputMap = this.getInputMap();
        final ActionMap actionMap = this.getActionMap();
        final AbstractAction pickElementAction = new AbstractAction()
        {
            @Override
            public void actionPerformed(ActionEvent actionEvent)
            {
                final Set<T> selectedElements = AdvancedJTable_networkElement.this.getSelectedElements();
                if (selectedElements.isEmpty()) return;
                SwingUtilities.invokeLater(() -> pickSelection(selectedElements));
            }
        };

        inputMap.put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, KeyEvent.VK_UNDEFINED), "pickElements");
        actionMap.put("pickElements", pickElementAction);
    }


    @Override
    protected final List<T> getAllAbstractElementsInTable()
    {
        final ITableRowFilter rf = callback.getVisualizationState().getTableRowFilter();
        return rf == null ? (List<T>) ITableRowFilter.getAllElements(callback.getOpenStackNet(), ajtType) : (List<T>) rf.getVisibleElements(callback.getOpenStackNet(), ajtType);
    }

    public final AJTableType getAjType () { return ajtType; }

    protected void pickSelection(Set<T> selection)
    {
        assert selection != null;

        final NetPlan np = callback.getDesign();

        final List<NetworkElement> elementsToPick = new ArrayList<>();

        if (ajtType == AJTableType.ROUTERS)
        {
            elementsToPick.addAll(selection.stream().map(s->np.getNodeFromId(((OpenStackRouter) s).getInternalId())).collect(Collectors.toList()));

        }

        if (!elementsToPick.isEmpty())
        {
            callback.getVisualizationState().pickElement(elementsToPick);
            callback.updateVisualizationAfterPick();
        }
    }

    @Override
    protected void showPopup(MouseEvent me)
    {
        // Debug mode removes the scroll bar so that tests do not have conflicts with it.
        final JScrollPopupMenu popup = new JScrollPopupMenu(20);
        final Optional<AjtRcMenu> viewMenu = getViewMenuAndSubmenus();
        final List<T> visibleElementsInTable = this.getAllAbstractElementsInTable();
        final List<AjtRcMenu> allPopupMenusButView = new ArrayList<> ();
        if (viewMenu.isPresent()) allPopupMenusButView.add(viewMenu.get());
        allPopupMenusButView.add(new AjtRcMenu("Pick selection", e-> SwingUtilities.invokeLater(() -> pickSelection(this.getSelectedElements())), (a,b)->b>0, null));
        allPopupMenusButView.add(AjtRcMenu.createMenuSeparator());
        allPopupMenusButView.addAll(getNonBasicRightClickMenusInfo());
        for (AjtRcMenu popupInfo : allPopupMenusButView)
        {
            if (popupInfo.isSeparator())  { popup.addSeparator(); continue; }
            final JComponent menu = processMenu (popupInfo , visibleElementsInTable);
            popup.add(menu);
        }
        popup.show(me.getComponent(), me.getX(), me.getY());
    }

    /**
     * Under testing, do not use just yet
     **/
    @Override
    public T getElementAtModelRowIndex(int rowModelIndex)
    {
        try
        {
            return (T) callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId((String) this.getModel().getValueAt(rowModelIndex, 0));
        } catch (Exception ex)
        {
            return null;
        }
    }

    protected abstract List<AjtRcMenu> getNonBasicRightClickMenusInfo ();

    protected abstract List<AjtColumnInfo<T>> getNonBasicUserDefinedColumnsVisibleOrNot ();

    @Override
    protected void reactToMouseClickInTable (int numClicks , int rowModelIndexOfClickOrMinus1IfOut , int columnModelIndexOfClickOrMinus1IfOut)
    {
        final SortedSet<T> selectedElements = this.getSelectedElements();
        if (numClicks == 1)
        {
            if (selectedElements.isEmpty()) callback.resetPickedStateAndUpdateView();
            if (rowModelIndexOfClickOrMinus1IfOut == -1) return;
            final Object value = getModel().getValueAt(rowModelIndexOfClickOrMinus1IfOut, columnModelIndexOfClickOrMinus1IfOut);

            /*Update focus panel with object description*/
            this.callback.getViewEditTopTables().updateText(((OpenStackNetworkElement) selectedElements.iterator().next()).get50CharactersDescription());

            if (value instanceof OpenStackNetworkElement)
            {

            }
            else if (value instanceof Collection)
            {
                if (((Collection) value).isEmpty()) return;

                final Object firstElement = ((Collection) value).iterator().next();


            }
            else if (selectedElements.isEmpty()) callback.resetPickedStateAndUpdateView();
        } else if (numClicks >= 2)
        {
            final Object value = getModel().getValueAt(rowModelIndexOfClickOrMinus1IfOut, columnModelIndexOfClickOrMinus1IfOut);

            if(value.getClass().equals(Boolean.class)) changeValueOfBoolean(columnModelIndexOfClickOrMinus1IfOut);

            pickSelectionHyperLink(value,columnModelIndexOfClickOrMinus1IfOut);

            SwingUtilities.invokeLater(() -> pickSelection(selectedElements));
        }

    }

    @Override
    protected void reactToMouseMoveInTable (int rowModelIndexOfClickOrMinus1IfOut , int columnModelIndexOfClickOrMinus1IfOut)
    {
        if (rowModelIndexOfClickOrMinus1IfOut == -1) return;
        final Object value = getModel().getValueAt(rowModelIndexOfClickOrMinus1IfOut,
                columnModelIndexOfClickOrMinus1IfOut);
        if (value instanceof OpenStackNetworkElement)
        {
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return;
        } else if (value instanceof Collection && !((Collection) value).isEmpty())
        {
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
            return;
        }
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }


    @Override
    protected final List<AjtColumnInfo<T>> getAllColumnsVisibleOrNot ()
    {
        final List<AjtColumnInfo<T>> completeListIncludingCommonColumns = new ArrayList<> ();
        completeListIncludingCommonColumns.add((AjtColumnInfo<T>) (AjtColumnInfo<?>) AjtColumnInfo.createIdColumn(this));
        completeListIncludingCommonColumns.add((AjtColumnInfo<T>) (AjtColumnInfo<?>) AjtColumnInfo.createIndexColumn(this));
        completeListIncludingCommonColumns.addAll(getNonBasicUserDefinedColumnsVisibleOrNot());
        completeListIncludingCommonColumns.add((AjtColumnInfo<T>) (AjtColumnInfo<?>) AjtColumnInfo.createTagsColumn(this));
        completeListIncludingCommonColumns.add((AjtColumnInfo<T>) (AjtColumnInfo<?>) AjtColumnInfo.createAttributesColumn(this));
        return completeListIncludingCommonColumns;
    }

    public void pickSelectionHyperLink(Object value , int columnModelIndexOfClickOrMinus1IfOut){

        switch (ajtType) {
            case NETWORKS:
                if (columnModelIndexOfClickOrMinus1IfOut == 10) {
                    callback.getViewEditTopTables().updateViewOfTabAfterDoubleClick(ajtType,value,"String",3);
                }
                break;
            case PORTS:
                if (columnModelIndexOfClickOrMinus1IfOut == 10) {
                    callback.getViewEditTopTables().updateViewOfTabAfterDoubleClick(ajtType,value,"String",0);
                }
                break;
            case SUBNETS:
                if (columnModelIndexOfClickOrMinus1IfOut == 6)
                    callback.getViewEditTopTables().updateViewOfTabAfterDoubleClick(ajtType,value,"String",0);
                break;

        }
    }

    @Override
    public void generalTableForm(String title,List<String> headers){
        JFrame jfM = new JFrame(title);
        jfM.setLayout(null);

        JButton jbP1 = new JButton("Enter");

        JPanel jp1 = new JPanel(new GridLayout(headers.size()+1, 2, 15, 10));//filas, columnas, espacio entre filas, espacio entre columnas

        JLabel l6 = new JLabel("Properties", SwingConstants.LEFT);
        jp1.add(l6);
        JLabel label = new JLabel("", SwingConstants.LEFT);
        jp1.add(label);
        for(int i= 0; i < headers.size(); i ++){
            JLabel jlabel = new JLabel(headers.get(i), SwingConstants.LEFT);
            jp1.add(jlabel);
            JTextField jtextField = new JTextField();
            jp1.add(jtextField);
        }


        jp1.setVisible(true);
        jp1.setBounds(10, 10, 200, 50*headers.size());
        jbP1.setBounds(75, 60*headers.size(), 90, 25);

        jbP1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Component [] components = jp1.getComponents();
                JSONObject jsonObject = new JSONObject();

                for(int i = 3;i< components.length;i=i+2){

                    jsonObject.put( ((JLabel)components[i-1]).getText(),((JTextField)components[i]).getText());
                 }

                switch (ajtType){
                    case PORTS:
                        callback.getOpenStackNet().getOpenStackNetCreate().createOpenStackPort(jsonObject);
                        break;
                    case NETWORKS:
                        callback.getOpenStackNet().getOpenStackNetCreate().createOpenStackNetwork(jsonObject);
                        break;
                    case SUBNETS:
                        callback.getOpenStackNet().getOpenStackNetCreate().createOpenStackSubnet(jsonObject);
                        break;
                    case ROUTERS:
                        callback.getOpenStackNet().getOpenStackNetCreate().createOpenStackRouter(jsonObject);
                        break;

                }

                updateTab();
                jfM.dispose();
            }});

        jfM.add(jbP1);
        jfM.add(jp1);

        ImageIcon img = new ImageIcon(getClass().getResource("/resources/common/openstack_logo.png"));
        jfM.setIconImage(img.getImage());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        if(headers.size()<3) {
            jfM.setSize(250, 130 * headers.size());
        }else {
            jfM.setSize(250, 80 * headers.size());
        }

        jfM.setLocation(dim.width/2-jfM.getSize().width/2, dim.height/2-jfM.getSize().height/2);

        jfM.setResizable(false);
        jfM.setVisible(true);
        jfM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    @Override
    public void updateTab(){

        callback.getDesign().removeAllNodes();
        callback.getOpenStackNet().refreshListTable();
        callback.getOpenStackNet().distributeTopologyOverCircle();
        final VisualizationState vs = callback.getVisualizationState();
        Pair<BidiMap<NetworkLayer, Integer>, Map<NetworkLayer, Boolean>> res =
                vs.suggestCanvasUpdatedVisualizationLayerInfoForNewDesign(new HashSet<>(callback.getDesign().getNetworkLayers()));
        vs.setCanvasLayerVisibilityAndOrder(callback.getDesign(), res.getFirst(), res.getSecond());
        callback.updateVisualizationAfterNewTopology();
        callback.addNetPlanChange();

    }

    @Override
    public void generalTableUpdate(String key, OpenStackNetworkElement osne){
        JFrame jfM = new JFrame(key);
        jfM.setLayout(null);

        JButton jbP1 = new JButton("Enter");

        JPanel jp1 = new JPanel(new GridLayout(6, 2, 30, 10));//filas, columnas, espacio entre filas, espacio entre columnas
        JLabel l6 = new JLabel(key, SwingConstants.LEFT);
        jp1.add(l6);
        JTextField os_text_change = new JTextField();
        jp1.add(os_text_change);


        jp1.setVisible(true);
        jp1.setBounds(10, 10, 200, 200);
        jbP1.setBounds(75, 90, 90, 25);

        jbP1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                switch(ajtType){
                    case PORTS:
                        switch(key){
                            case "Name":
                                ((OpenStackPort)osne).setName(os_text_change.getText());
                                break;

                        }
                        break;
                    case NETWORKS:
                        switch(key){
                            case "Name":
                                ((OpenStackNetwork)osne).setName(os_text_change.getText());
                                break;

                        }
                        break;
                    case ROUTERS:
                        switch(key){
                            case "Name":
                                ((OpenStackRouter)osne).setName(os_text_change.getText());
                                break;

                        }
                        break;
                    case SUBNETS:
                        switch(key){
                            case "Name":
                                ((OpenStackSubnet)osne).setName(os_text_change.getText());
                                break;

                        }
                        break;
                }
                updateTab();
                jfM.dispose();
            }});

        jfM.add(jbP1);
        jfM.add(jp1);

        ImageIcon img = new ImageIcon(getClass().getResource("/resources/common/openstack_logo.png"));
        jfM.setIconImage(img.getImage());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();

        jfM.setSize(250, 160);
        jfM.setLocation(dim.width/2-jfM.getSize().width/2, dim.height/2-jfM.getSize().height/2);

        jfM.setResizable(false);
        jfM.setVisible(true);
        jfM.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    public void changeValueOfBoolean(int columnModelIndexOfClickOrMinus1IfOut){
        System.out.println("Boolean"+columnModelIndexOfClickOrMinus1IfOut);
        final SortedSet<T> selectedElements = this.getSelectedElements();
        switch (ajtType){
            case NETWORKS:
                OpenStackNetwork network = ((OpenStackNetwork)selectedElements.iterator().next());
                if(columnModelIndexOfClickOrMinus1IfOut == 11){
                    network.isNetworkIsAdminStateUp(!network.isNetworkIsAdminStateUp());
                }else if(columnModelIndexOfClickOrMinus1IfOut == 12){
                    //network.i(!network.isNetworkIsRouterExternal());
                }else if(columnModelIndexOfClickOrMinus1IfOut == 13){
                    network.isNetworkIsShared(!network.isNetworkIsShared());
                }else{
                    System.out.println("No boolean avaliable");
                }
                break;
            case SUBNETS:
                OpenStackSubnet subnet = ((OpenStackSubnet)selectedElements.iterator().next());
                if(columnModelIndexOfClickOrMinus1IfOut == 12){
                  subnet.isSubnetIsDHCPEnabled(!subnet.isSubnetIsDHCPEnabled());
                }else{
                    System.out.println("No boolean avaliable");
                }
                break;
            case ROUTERS:
                OpenStackRouter router = ((OpenStackRouter)selectedElements.iterator().next());
                if(columnModelIndexOfClickOrMinus1IfOut == 7){
                    router.isAdminStateUp(!router.isRouterIsAdminStateUp());
                }else if(columnModelIndexOfClickOrMinus1IfOut == 8){
                    router.isDistributed(!router.isRouterIsDistributed());
                } else {
                    System.out.println("No boolean avaliable");
                }
                break;
            case PORTS:
                OpenStackPort port = ((OpenStackPort)selectedElements.iterator().next());
                if(columnModelIndexOfClickOrMinus1IfOut == 15){
                    port.isAdminStateUp(!port.isAdminStateUp());
                }else if(columnModelIndexOfClickOrMinus1IfOut == 16){
                    port.isPortSecurityEnable(!port.isPortSecurityEnable());
                } else {
                    System.out.println("No boolean avaliable");
                }
                break;
        }
        updateTab();
    }


}
