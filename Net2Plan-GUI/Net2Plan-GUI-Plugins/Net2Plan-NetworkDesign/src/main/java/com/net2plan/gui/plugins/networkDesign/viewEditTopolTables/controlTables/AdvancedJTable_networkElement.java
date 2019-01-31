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
import java.awt.event.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.Policy;
import java.util.*;

import javax.swing.*;
import javax.swing.text.MaskFormatter;


import com.google.common.collect.Lists;
import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackNetwork;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackPort;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackRouter;
import com.net2plan.gui.plugins.networkDesign.interfaces.ITableRowFilter;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackSubnet;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackMeter;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane.AJTableType;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.VisualizationState;
import com.net2plan.gui.utils.JScrollPopupMenu;
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.interfaces.networkDesign.NetworkElement;
import com.net2plan.interfaces.networkDesign.NetworkLayer;
import com.net2plan.utils.Pair;
import org.apache.commons.collections15.BidiMap;
import org.json.JSONObject;
import org.openstack4j.api.types.Facing;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.NetworkType;
import org.openstack4j.openstack.senlin.domain.SenlinPolicyType;

import java.util.List;
import java.util.stream.Collectors;


/**
 */
@SuppressWarnings("unchecked")
public abstract class AdvancedJTable_networkElement<T extends OpenStackNetworkElement> extends AdvancedJTable_abstractElement<T>
{
    protected final AJTableType ajtType;
    protected final OpenStackClient openStackClient;

    public AdvancedJTable_networkElement(GUINetworkDesign networkViewer, AJTableType ajtType , boolean hasAggregationRow,OpenStackClient openStackClient)
    {
        super(networkViewer, ajtType.getTabName() , hasAggregationRow);
        this.ajtType = ajtType;
        this.openStackClient = openStackClient;
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
                final Set<T> selectedElements = new TreeSet<>(AdvancedJTable_networkElement.this.getSelectedElements());
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
        return rf == null ? (List<T>) ITableRowFilter.getAllElements(this.openStackClient,this.openStackClient.getOsn(), ajtType) : (List<T>) rf.getVisibleElements(this.openStackClient,this.openStackClient.getOsn(), ajtType);
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
        allPopupMenusButView.add(new AjtRcMenu("Pick selection", e-> SwingUtilities.invokeLater(() -> pickSelection(new TreeSet<>(this.getSelectedElements()))), (a,b)->b>0, null));
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
        final SortedSet<T> selectedElements = new TreeSet<>(this.getSelectedElements());
        if (numClicks == 1)
        {
            if (selectedElements.isEmpty()) callback.resetPickedStateAndUpdateView();
            if (rowModelIndexOfClickOrMinus1IfOut == -1) return;
            final Object value = getModel().getValueAt(rowModelIndexOfClickOrMinus1IfOut, columnModelIndexOfClickOrMinus1IfOut);

            /*Update focus panel with object description*/
            this.callback.getViewEditTopTables().updateText(((OpenStackNetworkElement) selectedElements.iterator().next()).get50CharactersDescription());

            if (value instanceof OpenStackNetworkElement)
            {
                callback.getViewEditTopTables().selectTabAndGivenItems(this.openStackClient, Arrays.asList((OpenStackNetworkElement) value));
            }
            else if (value instanceof Collection)
            {
                if (((Collection) value).isEmpty()) return;

                if (((Collection) value).iterator().next() instanceof OpenStackNetworkElement)
                    callback.getViewEditTopTables().selectTabAndGivenItems(this.openStackClient, (List<OpenStackNetworkElement>) value);
            }
            else if (selectedElements.isEmpty()) callback.resetPickedStateAndUpdateView();
        } else if (numClicks >= 2)
        {
            if (rowModelIndexOfClickOrMinus1IfOut == -1) return;

            final Object value = getModel().getValueAt(rowModelIndexOfClickOrMinus1IfOut, columnModelIndexOfClickOrMinus1IfOut);

            if(value != null) {

                if (value.getClass().equals(Boolean.class)) changeValueOfBoolean(columnModelIndexOfClickOrMinus1IfOut);

                pickSelectionHyperLink((OpenStackNetworkElement)selectedElements.iterator().next(),value, columnModelIndexOfClickOrMinus1IfOut);


                SwingUtilities.invokeLater(() -> pickSelection(selectedElements));
            }
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
        return completeListIncludingCommonColumns;
    }

    public void pickSelectionHyperLink(OpenStackNetworkElement openStackNetworkElement,Object value , int columnModelIndexOfClickOrMinus1IfOut){

        switch (ajtType) {
            /*NETWORK*/
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
            case RESOURCES:

                callback.getOpenStackNet().getOsClients().forEach(n->n.updateMeterList(value.toString()));

                break;
            case METERS:

                callback.getOpenStackNet().getOsClients().forEach(n->n.updateMeasuresList((OpenStackMeter)openStackNetworkElement,value.toString()));

                break;




        }
    }


    @Override
    public void updateTab(){

        callback.getDesign().removeAllNodes();
        openStackClient.clearList();
        openStackClient.fillList();

        final VisualizationState vs = callback.getVisualizationState();
        Pair<BidiMap<NetworkLayer, Integer>, Map<NetworkLayer, Boolean>> res =
                vs.suggestCanvasUpdatedVisualizationLayerInfoForNewDesign(new HashSet<>(callback.getDesign().getNetworkLayers()));
        vs.setCanvasLayerVisibilityAndOrder(callback.getDesign(), res.getFirst(), res.getSecond());
        callback.updateVisualizationAfterNewTopology();
        callback.addNetPlanChange();
        callback.getViewEditTopTables().updateView();

    }

    public void changeValueOfBoolean(int columnModelIndexOfClickOrMinus1IfOut){
        System.out.println("Boolean"+columnModelIndexOfClickOrMinus1IfOut);
        openStackClient.updateClient();
        final SortedSet<T> selectedElements = new TreeSet<>(this.getSelectedElements());
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
        //updateTab();
        callback.getViewEditTopTables().updateView();
    }

}
