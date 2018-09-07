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
import java.util.*;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.*;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute.*;import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_networks;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_ports;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_routers;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_subnets;import com.net2plan.gui.plugins.utils.FilteredTablePanel;
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.interfaces.networkDesign.NetworkLayer;
import com.net2plan.internal.ErrorHandling;
import com.net2plan.utils.Pair;
import org.openstack4j.api.OSClient;

@SuppressWarnings("unchecked")
public class ViewEditTopologyTablesPane extends JPanel
{
    public enum AJTableType
    {

        NEUTRON("Neutron"),
        NOVA("Nova"),
        //Network (Neutron)
        NETWORKS("NETWORKS"),
        ROUTERS("ROUTERS"),
        PORTS("PORTS"),
        SUBNETS("SUBNETS"),

        //Compute (Nova)
        SERVERS("SERVERS"),
        FLAVORS("FLAVORS"),
        IMAGES("IMAGES"),
        FLOATINGIPS("FLOATINGIPS"),
        KEYPAIRS("KEYPAIRS"),
        SECURITYGROUPS("SEGURITYGROUPS"),
        HOSTRESOURCES("HOST RESOURCES")
        ;

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
    private final Map<OpenStackClient,Map<AJTableType, Pair<AdvancedJTable_abstractElement, FilteredTablePanel>>> netPlanViewTable = new HashMap<> (); //new EnumMap<>(AJTableType.class);
    private final Map<AJTableType, Pair<AdvancedJTable_abstractElement, FilteredTablePanel>> ajTables = new EnumMap<>(AJTableType.class);
    private final JTabbedPane viewEditHighLevelTabbedPane;
    private final Map<OpenStackClient, JTabbedPane> layerSubTabbedPaneMap = new HashMap<>();

      private final Map<OpenStackClient,JTabbedPane> networkTabbedPane = new HashMap<>();
      private final Map<OpenStackClient,JTabbedPane> computeTabbedPane = new HashMap<>();

    private  JMenuBar menuBar;
    private JTextArea upperText;
    private JScrollPane jScrollPane;
    private JTabbedPane openstack;

    final String NEWLINE = String.format("%n");

    /**
     * Main Panel with CENTER: high level tabbed pane (Network, layer, nodes, ...), SOUTH: Export as Excel mention
     *
     * @param callback
     * @param layout
     */
    public ViewEditTopologyTablesPane(GUINetworkDesign callback, LayoutManager layout) {
        super(layout);

        this.callback = callback;
        this.viewEditHighLevelTabbedPane = new JTabbedPane();

        upperText = new JTextArea();
        jScrollPane = new JScrollPane(upperText);
        upperText.setFont(new JLabel().getFont());
        upperText.setBackground(new JLabel().getBackground());
        upperText.setAutoscrolls(true);
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



        final JSplitPane splitPane = new JSplitPane();
        splitPane.setLeftComponent(viewEditHighLevelTabbedPane);

        splitPane.setBottomComponent(jScrollPane);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.3);
        splitPane.setEnabled(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0.4);

        this.add(splitPane, BorderLayout.CENTER);
        for (AJTableType ajType : AJTableType.values())
            ajTables.put(ajType, createPanelComponentInfo(ajType,new OpenStackClient()));

        this.recomputNetPlanView ();

        this.add(viewEditHighLevelTabbedPane,BorderLayout.CENTER);

       //JTabbedPane ee = new JTabbedPane();
      /*  viewEditHighLevelTabbedPane.addTab("KEYSTONE",identityTabbedPane);
        viewEditHighLevelTabbedPane.addTab("NEUTRON",networkTabbedPane);
        viewEditHighLevelTabbedPane.addTab("NOVA",computeTabbedPane);
        viewEditHighLevelTabbedPane.addTab("GLANCE",glanceTabbedPane);
        viewEditHighLevelTabbedPane.addTab("HEAT",heatTabbedPane);*/
        //viewEditHighLevelTabbedPane.addTab("OPENSTACK",ee);
     //   updateView();
        menuBar = new JMenuBar();

        this.add(menuBar, BorderLayout.SOUTH);
    }

    private Pair<AdvancedJTable_abstractElement, FilteredTablePanel> createPanelComponentInfo(AJTableType type, OpenStackClient openStackClient) {
        AdvancedJTable_networkElement table = null;
        switch (type)
        {

            /*NETWORK*/
            case NETWORKS:
                table = new AdvancedJTable_networks(callback,openStackClient);
                break;
            case ROUTERS:
                table = new AdvancedJTable_routers(callback,openStackClient);
                break;
            case PORTS:
                table = new AdvancedJTable_ports(callback,openStackClient);
                break;
            case SUBNETS:
                table = new AdvancedJTable_subnets(callback,openStackClient);
                break;
            case NEUTRON:
                table = new AdvancedJTable_subnets(callback,openStackClient);
                break;
            case NOVA:
                table = new AdvancedJTable_subnets(callback,openStackClient);
                break;

            /*COMPUTE*/
            case SERVERS:
                table = new AdvancedJTable_servers(callback,openStackClient);
                break;
            case FLAVORS:
                table = new AdvancedJTable_flavors(callback,openStackClient);
                break;
            case IMAGES:
                table = new AdvancedJTable_images(callback,openStackClient);
                break;
            case FLOATINGIPS:
                table = new AdvancedJTable_floatingIp(callback,openStackClient);
                break;
            case KEYPAIRS:
                table = new AdvancedJTable_keypairs(callback,openStackClient);
                break;
            case SECURITYGROUPS:
                table = new AdvancedJTable_securityGroups(callback,openStackClient);
                break;
            case HOSTRESOURCES:
                table = new AdvancedJTable_hostResources(callback,openStackClient);
                break;

            default:
                assert false;
        }


        return Pair.of(table, new FilteredTablePanel(callback, table.getTableScrollPane(),openStackClient));
    }

    public void resetPickedState() {
        ajTables.values().stream().filter(q -> q.getFirst() != null).forEach(q -> q.getFirst().clearSelection());

    }

    public void updateView() {
        System.out.println("ViewEditTopologu Update view");
        /* Load current network state */
        final NetPlan currentState = callback.getDesign();
        if (ErrorHandling.isDebugEnabled()) currentState.checkCachesConsistency();

        this.recomputNetPlanView();

        ajTables.values().stream().map(t -> t.getFirst()).forEach(t -> t.updateView());

        // Update filter header
        for (AJTableType type : AJTableType.values())
            ajTables.get(type).getSecond().updateHeader();

        //this.recomput();

        if (ErrorHandling.isDebugEnabled()) currentState.checkCachesConsistency();


    }

    public void recomputNetPlanView() {
        System.out.println(" ViewEdit.. Recomput");
        /* Save current selected tab */
        final int selectedIndexFirstLevel = viewEditHighLevelTabbedPane.getSelectedIndex() == -1 ? 0 : viewEditHighLevelTabbedPane.getSelectedIndex();
        int selectedIndexSecondLevel = -1;
        if (viewEditHighLevelTabbedPane.getSelectedComponent() instanceof JTabbedPane)
            selectedIndexSecondLevel = ((JTabbedPane) viewEditHighLevelTabbedPane.getSelectedComponent()).getSelectedIndex();


        final NetPlan np = callback.getDesign();
        netPlanViewTable.clear();
        layerSubTabbedPaneMap.clear();

        viewEditHighLevelTabbedPane.removeAll();

        viewEditHighLevelTabbedPane.addTab("Network", new JTabbedPane());

        for (OpenStackClient openStackClient : callback.getOpenStackNet().getOsClients()) {
            System.out.println(" ViewEdit.. Recomput.. OpenStackClient");
            layerSubTabbedPaneMap.put(openStackClient, new JTabbedPane());
            final JTabbedPane subpaneThisLayer = layerSubTabbedPaneMap.get(openStackClient);
            netPlanViewTable.put (openStackClient , new HashMap<> ());
            networkTabbedPane.put(openStackClient, new JTabbedPane ());
            computeTabbedPane.put(openStackClient, new JTabbedPane ());

            ajTables.clear();

            for (AJTableType ajType : AJTableType.values())
                ajTables.put(ajType, createPanelComponentInfo(ajType,openStackClient));

            for (AJTableType ajType : AJTableType.values())
            {

                if (ajType == ajType.NEUTRON)
                {

                    for (AJTableType type : Arrays.asList(AJTableType.NETWORKS, AJTableType.SUBNETS,AJTableType.ROUTERS,AJTableType.PORTS))
                        networkTabbedPane.get(openStackClient).addTab(type.getTabName(), ajTables.get(type).getSecond());

                    subpaneThisLayer.addTab(ajType.getTabName(), networkTabbedPane.get(openStackClient));


                }else if(ajType == ajType.NOVA){


                    for (AJTableType type : Arrays.asList(AJTableType.SERVERS, AJTableType.FLAVORS,AJTableType.IMAGES,AJTableType.FLOATINGIPS,AJTableType.KEYPAIRS,AJTableType.SECURITYGROUPS,AJTableType.HOSTRESOURCES))
                        computeTabbedPane.get(openStackClient).addTab(type.getTabName(), ajTables.get(type).getSecond());

                    subpaneThisLayer.addTab(ajType.getTabName(), computeTabbedPane.get(openStackClient));

                }
                else
                {

                }
            }
            viewEditHighLevelTabbedPane.addTab(openStackClient.getName().equals("")? openStackClient.getName() : openStackClient.getName() , subpaneThisLayer);
        }


        viewEditHighLevelTabbedPane.setSelectedIndex(selectedIndexFirstLevel);
        if (viewEditHighLevelTabbedPane.getSelectedComponent() instanceof JTabbedPane && selectedIndexSecondLevel >= 0)
            ((JTabbedPane) viewEditHighLevelTabbedPane.getSelectedComponent()).setSelectedIndex(selectedIndexSecondLevel);


        }

/*
    public void recomput(){

        for(int i=0;i<1;i++){
            this.openstack = new JTabbedPane();
            this.networkTabbedPane = new JTabbedPane();
            this.computeTabbedPane = new JTabbedPane();





            /* Network tabs
            for (AJTableType type : Arrays.asList(AJTableType.NETWORKS, AJTableType.SUBNETS,AJTableType.ROUTERS,AJTableType.PORTS))
                networkTabbedPane.addTab(type.getTabName(), ajTables.get(type).getSecond());

            /* Compute tabs
            for (AJTableType type : Arrays.asList(AJTableType.SERVERS, AJTableType.FLAVORS,AJTableType.IMAGES,AJTableType.FLOATINGIPS,AJTableType.KEYPAIRS,AJTableType.SECURITYGROUPS,AJTableType.HOSTRESOURCES))
                computeTabbedPane.addTab(type.getTabName(), ajTables.get(type).getSecond());


            openstack.addTab("NEUTRON",networkTabbedPane);
            openstack.addTab("NOVA",computeTabbedPane);

            viewEditHighLevelTabbedPane.addTab("OPENSTACK " + i,openstack);

            // updateView();
        }
        viewEditHighLevelTabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent changeEvent) {
                System.out.println("ViewEditTopology  state changed" + viewEditHighLevelTabbedPane.getSelectedIndex());
                if(viewEditHighLevelTabbedPane.getSelectedIndex()!=-1)
                    callback.getOpenStackNet().Inicialice(viewEditHighLevelTabbedPane.getSelectedIndex());

            }
        });
        int numberOfOpenStack = callback.getOpenStackNet().getOsClients().size();
        if(numberOfOpenStack < 2) return;

        System.out.println("Recomput" +numberOfOpenStack );
        for(int i=0;i<numberOfOpenStack;i++){

            this.networkTabbedPane = new JTabbedPane();
            this.computeTabbedPane = new JTabbedPane();

            for (AJTableType ajType : AJTableType.values())
                ajTables.put(ajType, createPanelComponentInfo(ajType));

                   /* Network tabs
            for (AJTableType type : Arrays.asList(AJTableType.NETWORKS, AJTableType.SUBNETS,AJTableType.ROUTERS,AJTableType.PORTS))
                networkTabbedPane.addTab(type.getTabName(), ajTables.get(type).getSecond());

            /* Compute tabs
            for (AJTableType type : Arrays.asList(AJTableType.SERVERS, AJTableType.FLAVORS,AJTableType.IMAGES,AJTableType.FLOATINGIPS,AJTableType.KEYPAIRS,AJTableType.SECURITYGROUPS,AJTableType.HOSTRESOURCES))
                computeTabbedPane.addTab(type.getTabName(), ajTables.get(type).getSecond());


            openstack.addTab("NEUTRON",networkTabbedPane);
            openstack.addTab("NOVA",computeTabbedPane);

            viewEditHighLevelTabbedPane.addTab("OPENSTACK" + i,openstack);

        }
    }
    */
    /**
     * Shows the tab corresponding associated to a network element.
     *
     * @param type   Network element type
     */
    public void selectItemTab(AJTableType type) {
        switch (type)
        {
            /*network*/
            case NETWORKS:
            case SUBNETS:
            case ROUTERS:
            case PORTS:

            /*compute*/
            case SERVERS:
            case FLAVORS:
            case IMAGES:
            case FLOATINGIPS:
            case KEYPAIRS:
            case SECURITYGROUPS:
            case HOSTRESOURCES:

                viewEditHighLevelTabbedPane.setSelectedComponent(ajTables.get(type).getSecond());
                break;

            default:
                System.out.println(type);
                assert false;
        }
    }

    public void updateText(String text){ this.upperText.setText(text);}

    public void updateViewOfTabAfterDoubleClick (AJTableType ajTableType,Object value, String type, Integer indexSubTab){

        /*final FilteredTablePanel filteredTablePanel;

        switch (ajTableType){

            case NETWORKS:
            case PORTS:
            case SUBNETS:
            case ROUTERS:
                this.networkTabbedPane.setSelectedIndex(indexSubTab);
                filteredTablePanel = (FilteredTablePanel) this.networkTabbedPane.getSelectedComponent();
                break;

            /*compute
            case SERVERS:
            case FLAVORS:
            case IMAGES:
            case FLOATINGIPS:
            case KEYPAIRS:
            case SECURITYGROUPS:
            case HOSTRESOURCES:
                this.computeTabbedPane.setSelectedIndex(indexSubTab);
                filteredTablePanel = (FilteredTablePanel) this.computeTabbedPane.getSelectedComponent();
                break;

            default:
                filteredTablePanel = (FilteredTablePanel) this.networkTabbedPane.getSelectedComponent();
        }

        updateView();
        filteredTablePanel.updateTableSelection(type,value);*/
    }

    public void changeViewOfTable(AJTableType ajTableType, Integer indexSubTab){

        /*switch (ajTableType){

            case NETWORKS:
            case PORTS:
            case SUBNETS:
            case ROUTERS:
                this.networkTabbedPane.setSelectedIndex(indexSubTab);
                break;

            /*compute
            case SERVERS:
            case FLAVORS:
            case IMAGES:
            case FLOATINGIPS:
            case KEYPAIRS:
            case SECURITYGROUPS:
            case HOSTRESOURCES:
                this.computeTabbedPane.setSelectedIndex(indexSubTab);
                break;

            default:
        }

        updateView();*/
    }
}
