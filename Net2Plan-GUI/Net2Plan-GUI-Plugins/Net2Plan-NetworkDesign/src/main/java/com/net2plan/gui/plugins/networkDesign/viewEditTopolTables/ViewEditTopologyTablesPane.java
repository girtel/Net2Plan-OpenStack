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
package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables;

import java.awt.BorderLayout;
import java.awt.LayoutManager;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.*;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity.*;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_networks;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_ports;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_routers;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_subnets;
import com.net2plan.gui.plugins.utils.FilteredTablePanel;
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.internal.ErrorHandling;
import com.net2plan.utils.Pair;

@SuppressWarnings("unchecked")
public class ViewEditTopologyTablesPane extends JPanel
{
    public enum AJTableType


    {
        //Identity (Keystone)
        USERS("USERS"),
        PROJECTS("PROJECTS"),
        DOMAINS("DOMAINS"),
        ENDPOINTS("ENDPOINTS"),
        SERVICES("SERVICES"),
        REGIONS("REGIONS"),
        CREDENTIALS("CREDENTIALS"),
        GROUPS("GROUPS"),
        POLICIES("POLICIES"),
        ROLES("ROLES"),

        //Network (Neutron)
        NETWORKS("NETWORKS"),
        ROUTERS("ROUTERS"),
        PORTS("PORTS"),
        SUBNETS("SUBNETS");

        private final String tabName;

        private AJTableType(String tabName)
        {
            this.tabName = tabName;
        }

        public String getTabName()
        {
            return tabName;
        }

    }

    private final GUINetworkDesign callback;
    private final Map<AJTableType, Pair<AdvancedJTable_networkElement, FilteredTablePanel>> ajTables = new EnumMap<>(AJTableType.class);
    private final JTabbedPane viewEditHighLevelTabbedPane;

    //Identity (Keystone) TabbedPane
    private final JTabbedPane identityTabbedPane;
    //Network (Neutron) TabbedPane
    private final JTabbedPane networkTabbedPane;

    private final JMenuBar menuBar;
    private JTextArea upperText;

    final String NEWLINE = String.format("%n");

    /**
     * Main Panel with CENTER: high level tabbed pane (Network, layer, nodes, ...), SOUTH: Export as Excel mention
     *
     * @param callback
     * @param layout
     */
    public ViewEditTopologyTablesPane(GUINetworkDesign callback, LayoutManager layout)
    {
        super(layout);

        this.callback = callback;

        upperText = new JTextArea();
        upperText.setFont(new JLabel().getFont());
        upperText.setBackground(new JLabel().getBackground());
        upperText.setLineWrap(true);
        upperText.setEditable(false);
        upperText.setWrapStyleWord(true);
        upperText.setText("Identity Service (Keystone) V3"+ NEWLINE+NEWLINE
        +"The Identity (Keystone) V3 service provides the central directory of users, groups, region, service, endpoints, role management and authorization."+ NEWLINE
                +"This API is responsible for authenticating and providing access to all the other OpenStack services. "+NEWLINE
                +"The API also enables administrators to configured centralized access policies, users, domains and projects."+ NEWLINE
                +NEWLINE+NEWLINE
                +"Network (Neutron)" + NEWLINE+NEWLINE
                +"Neutron is the Network service for OpenStack. Unlike Nova Networking, Neutron is broken up into the following abstractions: Networks, Subnets and Routers."+NEWLINE
                +"Each has functionality that mimics the physical layers.");

        this.viewEditHighLevelTabbedPane = new JTabbedPane();

        //Inicialice network-identity tabbed pane
        this.networkTabbedPane = new JTabbedPane();
        this.identityTabbedPane = new JTabbedPane();

        viewEditHighLevelTabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updateText("");
            }
        });
        final JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(viewEditHighLevelTabbedPane);

        splitPane.setBottomComponent(upperText);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.3);
        splitPane.setEnabled(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0.4);

        this.add(splitPane, BorderLayout.CENTER);

        for (AJTableType ajType : AJTableType.values())
            ajTables.put(ajType, createPanelComponentInfo(ajType));


        /* Identity tabs */
        for (AJTableType type : Arrays.asList(AJTableType.USERS, AJTableType.PROJECTS,AJTableType.DOMAINS,AJTableType.ENDPOINTS
                ,AJTableType.SERVICES,AJTableType.REGIONS,AJTableType.CREDENTIALS,AJTableType.GROUPS,AJTableType.POLICIES,AJTableType.ROLES))
            identityTabbedPane.addTab(type.getTabName(), ajTables.get(type).getSecond());

        /* Network tabs */
        for (AJTableType type : Arrays.asList(AJTableType.NETWORKS, AJTableType.SUBNETS,AJTableType.ROUTERS,AJTableType.PORTS))
            networkTabbedPane.addTab(type.getTabName(), ajTables.get(type).getSecond());

        viewEditHighLevelTabbedPane.addTab("IDENTITY",identityTabbedPane);
        viewEditHighLevelTabbedPane.addTab("NETWORK",networkTabbedPane);

        menuBar = new JMenuBar();

        this.add(menuBar, BorderLayout.SOUTH);
    }

    private Pair<AdvancedJTable_networkElement, FilteredTablePanel> createPanelComponentInfo(AJTableType type)
    {
        AdvancedJTable_networkElement table = null;
        switch (type)
        {
            /*IDENTITY*/
            case USERS:
                table = new AdvancedJTable_users(callback);
                break;
            case PROJECTS:
                table = new AdvancedJTable_projects(callback);
                break;
            case DOMAINS:
                table = new AdvancedJTable_domains(callback);
                break;
            case ENDPOINTS:
                table = new AdvancedJTable_endpoints(callback);
                break;
            case SERVICES:
                table = new AdvancedJTable_services(callback);
                break;
            case REGIONS:
                table = new AdvancedJTable_regions(callback);
                break;
            case CREDENTIALS:
                table = new AdvancedJTable_credentials(callback);
                break;
            case GROUPS:
                table = new AdvancedJTable_groups(callback);
                break;
            case POLICIES:
                table = new AdvancedJTable_policies(callback);
                break;
            case ROLES:
                table = new AdvancedJTable_roles(callback);
                break;

            /*NETWORK*/
            case NETWORKS:
                table = new AdvancedJTable_networks(callback);
                break;
            case ROUTERS:
                table = new AdvancedJTable_routers(callback);
                break;
            case PORTS:
                table = new AdvancedJTable_ports(callback);
                break;
            case SUBNETS:
                table = new AdvancedJTable_subnets(callback);
                break;
            default:
                assert false;
        }


        return Pair.of(table, new FilteredTablePanel(callback, table.getTableScrollPane()));
    }

    public void resetPickedState()
    {
        ajTables.values().stream().filter(q -> q.getFirst() != null).forEach(q -> q.getFirst().clearSelection());

    }

    public void updateView()
    {
        /* Load current network state */
        final NetPlan currentState = callback.getDesign();
        if (ErrorHandling.isDebugEnabled()) currentState.checkCachesConsistency();

        ajTables.values().stream().map(t -> t.getFirst()).forEach(t -> t.updateView());

        // Update filter header
        for (AJTableType type : AJTableType.values())
            ajTables.get(type).getSecond().updateHeader();



        if (ErrorHandling.isDebugEnabled()) currentState.checkCachesConsistency();
    }

    /**
     * Shows the tab corresponding associated to a network element.
     *
     * @param type   Network element type
     */
    public void selectItemTab(AJTableType type)
    {
        switch (type)
        {
            case USERS:
            case PROJECTS:
            case DOMAINS:
            case ENDPOINTS:
            case SERVICES:
            case REGIONS:
            case CREDENTIALS:
            case GROUPS:
            case POLICIES:
            case ROLES:

            case NETWORKS:
            case SUBNETS:
            case ROUTERS:
            case PORTS:
                viewEditHighLevelTabbedPane.setSelectedComponent(ajTables.get(type).getSecond());
                break;
            default:
                System.out.println(type);
                assert false;
        }
    }

    public void updateText(String text){ this.upperText.setText(text);}

    public void updateViewOfTabAfterDoubleClick (AJTableType ajTableType,Object value, String type, Integer indexSubTab){

        final FilteredTablePanel filteredTablePanel;

        switch (ajTableType){

            case USERS:
            case PROJECTS:
            case DOMAINS:
            case ENDPOINTS:
            case SERVICES:
            case REGIONS:
            case CREDENTIALS:
            case GROUPS:
            case POLICIES:
            case ROLES:
                this.identityTabbedPane.setSelectedIndex(indexSubTab);
                filteredTablePanel = (FilteredTablePanel) this.identityTabbedPane.getSelectedComponent();
                break;

            case NETWORKS:
            case PORTS:
            case SUBNETS:
            case ROUTERS:
                this.networkTabbedPane.setSelectedIndex(indexSubTab);
                filteredTablePanel = (FilteredTablePanel) this.networkTabbedPane.getSelectedComponent();
                break;

            default:
                filteredTablePanel = (FilteredTablePanel) this.networkTabbedPane.getSelectedComponent();
        }

        updateView();
        filteredTablePanel.updateTableSelection(type,value);
    }

}
