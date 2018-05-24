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

import java.awt.*;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.*;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute.*;
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
        INFORMATION("INFORMATION"),
        USERS("USERS"),
        ROUTERS("ROUTERS"),
        NETWORKS("NETWORKS"),
        PORTS("PORTS"),
        DOMAINS("DOMAINS"),
        CREDENTIALS("CREDENTIALS"),
        POLICIES("POLICIES"),
        ENDPOINTS("ENDPOINTS"),
        PROJECTS("PROJECTS"),
        REGIONS("REGIONS"),
        ROLES("ROLES"),
        SERVICES("SERVICES"),
        GROUPS("GROUPS"),
        EXTENSIONS("EXTENSIONS"),
        FLAVORS("FLAVORS"),
        FLOATINGIPS("FLOATINGIPS"),
        IMAGES("IMAGES"),
        KEYPAIRS("KEYPAIRS"),
        LIMITS("LIMITS"),
        QUOTAS("QUOTAS"),
        SECURITYGROUPS("SECURITYGROUPS"),
        SERVERS("SERVERS"),
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

    private JTabbedPane identityDifferentTypesLevel2Pane;
    private JTabbedPane computeDifferentTypesLevel2Pane;
    private JTabbedPane networkDifferentTypesLevel2Pane;

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
        viewEditHighLevelTabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                updateText(viewEditHighLevelTabbedPane.getSelectedIndex());
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

        for (AJTableType ajType : AJTableType.values())
            ajTables.put(ajType, createPanelComponentInfo(ajType));


        this.identityDifferentTypesLevel2Pane = new JTabbedPane();
        this.computeDifferentTypesLevel2Pane = new JTabbedPane();
        this.networkDifferentTypesLevel2Pane = new JTabbedPane();




        for(AJTableType type : Arrays.asList(AJTableType.USERS,AJTableType.CREDENTIALS,AJTableType.DOMAINS,AJTableType.ENDPOINTS,AJTableType.GROUPS,AJTableType.POLICIES,AJTableType.PROJECTS,AJTableType.REGIONS,AJTableType.ROLES,AJTableType.SERVICES)){
            identityDifferentTypesLevel2Pane.addTab(type.getTabName(), null, ajTables.get(type).getSecond(), "Does nothing");
        }
        for(AJTableType type : Arrays.asList(AJTableType.EXTENSIONS,AJTableType.FLAVORS,AJTableType.FLOATINGIPS,AJTableType.IMAGES,AJTableType.KEYPAIRS,AJTableType.LIMITS,AJTableType.QUOTAS,AJTableType.SECURITYGROUPS,AJTableType.SERVERS)){
            computeDifferentTypesLevel2Pane.addTab(type.getTabName(), null, ajTables.get(type).getSecond(), "Does nothing");
        }
        for(AJTableType type : Arrays.asList(AJTableType.ROUTERS,AJTableType.NETWORKS,AJTableType.PORTS,AJTableType.SUBNETS)){
            networkDifferentTypesLevel2Pane.addTab(type.getTabName(), null, ajTables.get(type).getSecond(), "Does nothing");
        }

        viewEditHighLevelTabbedPane.addTab("IDENTITY", identityDifferentTypesLevel2Pane);
        viewEditHighLevelTabbedPane.addTab("COMPUTE", computeDifferentTypesLevel2Pane);
        viewEditHighLevelTabbedPane.addTab("NETWORK", networkDifferentTypesLevel2Pane);

        this.add(splitPane, BorderLayout.CENTER);


        menuBar = new JMenuBar();

        this.add(menuBar, BorderLayout.SOUTH);
    }

    private Pair<AdvancedJTable_networkElement, FilteredTablePanel> createPanelComponentInfo(AJTableType type)
    {
        AdvancedJTable_networkElement table = null;

        switch (type)
        {

            case USERS:
                table = new AdvancedJTable_users(callback);
                break;
            case ROUTERS:
                table = new AdvancedJTable_routers(callback);
                break;
            case NETWORKS:
                table = new AdvancedJTable_networks(callback);
                break;
            case SUBNETS:
                table = new AdvancedJTable_subnets(callback);
                break;
            case PORTS:
                table = new AdvancedJTable_ports(callback);
                break;
            case CREDENTIALS:
                table = new AdvancedJTable_credentials(callback);
                break;
            case DOMAINS:
                table = new AdvancedJTable_domains(callback);
                break;
            case ENDPOINTS:
                table = new AdvancedJTable_endpoints(callback);
                break;
            case GROUPS:
                table = new AdvancedJTable_groups(callback);
                break;
            case POLICIES:
                table = new AdvancedJTable_policies(callback);
                break;
            case PROJECTS:
                table = new AdvancedJTable_projects(callback);
                break;
            case REGIONS:
                table = new AdvancedJTable_regions(callback);
                break;
            case ROLES:
                table = new AdvancedJTable_roles(callback);
                break;
            case SERVICES:
                table = new AdvancedJTable_services(callback);
                break;
            case INFORMATION:
                table = new AdvancedJTable_informationProject(callback);
                break;
            case EXTENSIONS:
                table = new AdvancedJTable_extensions(callback);
                break;
            case FLAVORS:
                table = new AdvancedJTable_flavors(callback);
                break;
            case FLOATINGIPS:
                table = new AdvancedJTable_floatingIpDns(callback);
                break;
            case IMAGES:
                table = new AdvancedJTable_images(callback);
                break;
            case KEYPAIRS:
                table = new AdvancedJTable_keypairs(callback);
                break;
            case QUOTAS:
                table = new AdvancedJTable_quotas(callback);
                break;
            case LIMITS:
                table = new AdvancedJTable_limits(callback);
                break;
            case SECURITYGROUPS:
                table = new AdvancedJTable_securityGroups(callback);
                break;
            case SERVERS:
                table = new AdvancedJTable_servers(callback);
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

    public JTabbedPane getNetworkDifferentTypesLevel2Pane (){
        return this.networkDifferentTypesLevel2Pane;
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
            case ROUTERS:
            case USERS:
            case NETWORKS:
            case INFORMATION:
            case SUBNETS:
            case PORTS:
            case CREDENTIALS:
            case GROUPS:
            case DOMAINS:
            case POLICIES:
            case PROJECTS:
            case REGIONS:
            case ROLES:
            case SERVICES:
            case EXTENSIONS:
            case FLAVORS:
            case FLOATINGIPS:
            case IMAGES:
            case KEYPAIRS:
            case LIMITS:
            case QUOTAS:
            case SECURITYGROUPS:
            case SERVERS:
                viewEditHighLevelTabbedPane.setSelectedComponent(ajTables.get(type).getSecond());
                break;
            default:
                System.out.println(type);
                assert false;
        }
    }

    public void updateText(int type){
        if(callback.getOpenStackNet().getOpenStackUsers().size() == 0) return;
        switch (type){
            case 0:
                upperText.setText("In this tab you can see the information about the routers of OpenStack"+NEWLINE
                        + "Table description: " + NEWLINE
                        + callback.getOpenStackNet().getOpenStackRouters().get(0).get50CharactersDescription()
                );
                break;
            case 1:
                upperText.setText("In this tab you can see the information about the users of OpenStack"+NEWLINE
                        + "Table description: " + NEWLINE
                        + callback.getOpenStackNet().getOpenStackUsers().get(0).get50CharactersDescription()
                );
                break;
            case 2:
                upperText.setText("In this tab you can see the information about the networks of OpenStack"+NEWLINE
                        + "Table description: " + NEWLINE
                        + callback.getOpenStackNet().getOpenStackNetworks().get(0).get50CharactersDescription()
                );
                break;
            case 3:
                upperText.setText("In this tab you can see the information about the subnets of OpenStack"+NEWLINE
                        + "Table description: " + NEWLINE
                        + callback.getOpenStackNet().getOpenStackSubnets().get(0).get50CharactersDescription()
                );
                break;
            case 4:
            case 5:

                break;
        }

    }
}
