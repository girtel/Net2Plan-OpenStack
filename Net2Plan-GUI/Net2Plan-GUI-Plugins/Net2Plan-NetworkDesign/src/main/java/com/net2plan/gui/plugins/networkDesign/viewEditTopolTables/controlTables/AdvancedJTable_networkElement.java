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
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.SortedSet;

import javax.swing.*;


import com.google.common.collect.Sets;
import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.net2plan.gui.plugins.networkDesign.interfaces.ITableRowFilter;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackRouter;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackSubnet;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane.AJTableType;
import com.net2plan.gui.plugins.utils.FilteredTablePanel;
import com.net2plan.gui.utils.AdvancedJTable;
import com.net2plan.gui.utils.JScrollPopupMenu;
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.interfaces.networkDesign.NetworkElement;

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
         System.out.println(rowModelIndexOfClickOrMinus1IfOut +"   "+ columnModelIndexOfClickOrMinus1IfOut);
         final SortedSet<T> selectedElements = this.getSelectedElements();
        if (numClicks == 1)
        {
            if (selectedElements.isEmpty()) callback.resetPickedStateAndUpdateView();
            if (rowModelIndexOfClickOrMinus1IfOut == -1) return;
            pickSelectionOneClickUpdateText(rowModelIndexOfClickOrMinus1IfOut);

        } else if (numClicks >= 2)
        {

            final Object value = getModel().getValueAt(rowModelIndexOfClickOrMinus1IfOut, columnModelIndexOfClickOrMinus1IfOut);
            pickSelectionHyperLink(value,columnModelIndexOfClickOrMinus1IfOut);
            SwingUtilities.invokeLater(() -> pickSelection(selectedElements));

           }

    }

    public void pickSelectionOneClickUpdateText(int rowModelIndexOfClickOrMinus1IfOut ){

        final Object value = getModel().getValueAt(rowModelIndexOfClickOrMinus1IfOut, getModel().getColumnCount() - 5);
        if (value instanceof OpenStackNetworkElement)
        {
            callback.getViewEditTopTables().updateViewOfTextAfterOneClick(((OpenStackNetworkElement) value).get50CharactersDescription());
        }
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
                    callback.getViewEditTopTables().updateViewOfTabAfterDoubleClick(ajtType,value,"String",1);
                }
                break;
            case SUBNETS:
                if (columnModelIndexOfClickOrMinus1IfOut == 6)
                    callback.getViewEditTopTables().updateViewOfTabAfterDoubleClick(ajtType,value,"String",1);
                break;
            case USERS:
                if (columnModelIndexOfClickOrMinus1IfOut == 4) {
                    callback.getViewEditTopTables().updateViewOfTabAfterDoubleClick(ajtType,value,"String",2);
                }
                break;
            case ENDPOINTS:
                if (columnModelIndexOfClickOrMinus1IfOut == 7) {
                    callback.getViewEditTopTables().updateViewOfTabAfterDoubleClick(ajtType,value,"String",7);
                }
                if (columnModelIndexOfClickOrMinus1IfOut == 8) {
                    callback.getViewEditTopTables().updateViewOfTabAfterDoubleClick(ajtType,value,"String",9);
                }
                break;
            case CREDENTIALS:
                if (columnModelIndexOfClickOrMinus1IfOut == 3)
                    callback.getViewEditTopTables().updateViewOfTabAfterDoubleClick(ajtType,value,"String",6);
                break;
            case GROUPS:
                if (columnModelIndexOfClickOrMinus1IfOut == 5) {
                    callback.getViewEditTopTables().updateViewOfTabAfterDoubleClick(ajtType,value,"String",2);
                }
                break;
            case POLICIES:
                if (columnModelIndexOfClickOrMinus1IfOut == 3) {
                    callback.getViewEditTopTables().updateViewOfTabAfterDoubleClick(ajtType,value,"String",0);
                }
                if (columnModelIndexOfClickOrMinus1IfOut == 4) {
                    callback.getViewEditTopTables().updateViewOfTabAfterDoubleClick(ajtType,value,"String",6);
                }
                break;
            case PROJECTS:
                if (columnModelIndexOfClickOrMinus1IfOut == 6) {
                    callback.getViewEditTopTables().updateViewOfTabAfterDoubleClick(ajtType,value,"String",2);
                }
                break;
            case ROLES:
                if (columnModelIndexOfClickOrMinus1IfOut == 4) {
                    callback.getViewEditTopTables().updateViewOfTabAfterDoubleClick(ajtType,value,"String",2);
                }
                break;
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



}
