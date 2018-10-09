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
import java.util.stream.Collectors;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.*;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute.*;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.identity.*;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.image.AdvancedJTable_imagesV2;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_networks;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_ports;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_routers;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.network.AdvancedJTable_subnets;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.telemetry.AdvancedJTable_measures;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.telemetry.AdvancedJTable_meters;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.telemetry.AdvancedJTable_resources;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.VisualizationState;
import com.net2plan.gui.plugins.utils.FilteredTablePanel;
import com.net2plan.interfaces.networkDesign.Net2PlanException;
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.interfaces.networkDesign.NetworkLayer;
import com.net2plan.internal.ErrorHandling;
import com.net2plan.utils.Pair;
import org.apache.commons.collections15.BidiMap;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.compute.AbsoluteLimit;

@SuppressWarnings("unchecked")
public class ViewEditTopologyTablesPane extends JPanel
{
    public enum AJTableType
    {

        //Identity (Keystone)
        KEYSTONE("Keystone"),
        USERS("Users"),
        PROJECTS("Projects"),
        DOMAINS("Domains"),
        ENDPOINTS("Endpoints"),
        SERVICES("Services"),
        REGIONS("Regions"),
        CREDENTIALS("Credentials"),
        GROUPS("Groups"),
        POLICIES("Policies"),
        ROLES("Roles"),

        //Network (Neutron)
        NEUTRON("Neutron"),
        NETWORKS("Networks"),
        ROUTERS("Routers"),
        PORTS("Ports"),
        SUBNETS("Subnets"),

        //Compute (Nova)
        NOVA("Nova"),
        SERVERS("Servers"),
        FLAVORS("Flavors"),
        FLOATINGIPS("Floating IPs"),
        KEYPAIRS("Keypaairs"),
        SECURITYGROUPS("Security groups"),
        HOSTRESOURCES("Host resources"),

        //Image (Glance)
        GLANCE("Glance"),
        IMAGES("Images"),

        //Telemetry (Ceilometer)
        GNOCCHI("Ceilometer"),
        RESOURCES("Resources"),
        METERS("Metrics"),
        MEASURES("Measures"),

        //SLICING
        SLICING("Slicing"),
        QUOTAS("Quotas"),
        LIMITS("Limits"),
        QUOTASUSAGE("Quota Usage")
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

    private final JTabbedPane slicingTabbedPane = new JTabbedPane();
    private final Map<OpenStackClient,JTabbedPane> identityTabbedPane = new HashMap<>();
    private final Map<OpenStackClient,JTabbedPane> networkTabbedPane = new HashMap<>();
    private final Map<OpenStackClient,JTabbedPane> computeTabbedPane = new HashMap<>();
    private final Map<OpenStackClient,JTabbedPane> imageTabbedPane = new HashMap<>();
    private final Map<OpenStackClient,JTabbedPane> telemetryTabbedPane = new HashMap<>();

    private JMenuBar menuBar;
    private JTextArea upperText;
    private JScrollPane jScrollPane;
    private JTabbedPane openstack;
    public Boolean myboolean= false;

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



       /*final JSplitPane splitPane = new JSplitPane();

        splitPane.setLeftComponent(viewEditHighLevelTabbedPane);

        splitPane.setBottomComponent(jScrollPane);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.3);
        splitPane.setEnabled(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0.4);

        this.add(splitPane, BorderLayout.CENTER);*/

        for (AJTableType ajType : AJTableType.values())
            ajTables.put(ajType, createPanelComponentInfo(ajType,new OpenStackClient()));


        viewEditHighLevelTabbedPane.addChangeListener(new ChangeListener() {
            public void stateChanged(ChangeEvent e) {
                if(viewEditHighLevelTabbedPane.getSelectedIndex() > 0) {

                    System.out.println("Tab: " + viewEditHighLevelTabbedPane.getTitleAt(viewEditHighLevelTabbedPane.getSelectedIndex()));
                    OpenStackClient openStackClient = callback.getOpenStackNet().getOsClients().stream().filter(n -> n.getName().equals(viewEditHighLevelTabbedPane.getTitleAt(viewEditHighLevelTabbedPane.getSelectedIndex()))).collect(Collectors.toList()).get(0);
                    System.out.println("Net2plan nodes "+ openStackClient.getNetPlanDesign().getNodes().size());
                    callback.setDesign(openStackClient.getNetPlanDesign());

                    final VisualizationState vs = callback.getVisualizationState();
                    Pair<BidiMap<NetworkLayer, Integer>, Map<NetworkLayer, Boolean>> res =
                            vs.suggestCanvasUpdatedVisualizationLayerInfoForNewDesign(new HashSet<>(callback.getDesign().getNetworkLayers()));
                    vs.setCanvasLayerVisibilityAndOrder(callback.getDesign(), res.getFirst(), res.getSecond());
                    callback.updateVisualizationAfterNewTopology();
                    callback.addNetPlanChange();

                }

            }
        });
        this.recomputNetPlanView ();

        this.add(viewEditHighLevelTabbedPane,BorderLayout.CENTER);

        menuBar = new JMenuBar();

        this.add(menuBar, BorderLayout.SOUTH);
    }

    private Pair<AdvancedJTable_abstractElement, FilteredTablePanel> createPanelComponentInfo(AJTableType type, OpenStackClient openStackClient) {
        AdvancedJTable_networkElement table = null;
         switch (type)
        {

            /*IDENTITY*/
            case KEYSTONE:
                table = new AdvancedJTable_users(callback,openStackClient);
                break;
            case USERS:
                 table = new AdvancedJTable_users(callback,openStackClient);
                break;
            case PROJECTS:
                table = new AdvancedJTable_projects(callback,openStackClient);
                break;
            case DOMAINS:
                table = new AdvancedJTable_domains(callback,openStackClient);
                break;
            case ENDPOINTS:
                table = new AdvancedJTable_endpoints(callback,openStackClient);
                break;
            case SERVICES:
                table = new AdvancedJTable_services(callback,openStackClient);
                break;
            case REGIONS:
                table = new AdvancedJTable_regions(callback,openStackClient);
                break;
            case CREDENTIALS:
                table = new AdvancedJTable_credentials(callback,openStackClient);
                break;
            case GROUPS:
                table = new AdvancedJTable_groups(callback,openStackClient);
                break;
            case POLICIES:
                table = new AdvancedJTable_policies(callback,openStackClient);
                break;
            case ROLES:
                table = new AdvancedJTable_roles(callback,openStackClient);
                break;

            /*NETWORK*/
            case NEUTRON:
                table = new AdvancedJTable_subnets(callback,openStackClient);
                break;
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


            /*COMPUTE*/
            case NOVA:
                table = new AdvancedJTable_subnets(callback,openStackClient);
                break;
            case SERVERS:
                table = new AdvancedJTable_servers(callback,openStackClient);
                break;
            case FLAVORS:
                table = new AdvancedJTable_flavors(callback,openStackClient);
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

            /*IMAGE*/
            case GLANCE:
                table = new AdvancedJTable_imagesV2(callback,openStackClient);
                break;
            case IMAGES:
                table = new AdvancedJTable_imagesV2(callback,openStackClient);
                break;

            /*TELEMETRY*/
            case GNOCCHI:
                table = new AdvancedJTable_subnets(callback,openStackClient);
              /*  final FilteredTablePanel filteredTablePanel =  new FilteredTablePanel(callback, table.getTableScrollPane(),openStackClient);
                final JSplitPane sp = new JSplitPane();
                final JPanel bottomPanel = new JPanel();
                sp.setTopComponent(filteredTablePanel);
                sp.setBottomComponent(bottomPanel);
                return Pair.of(table, sp);*/
                break;
            case METERS:
                table = new AdvancedJTable_meters(callback,openStackClient);
                break;
            case RESOURCES:
                table = new AdvancedJTable_resources(callback,openStackClient);
                break;
            case MEASURES:
                table = new AdvancedJTable_measures(callback,openStackClient);
                break;

            /*SLICING*/
            case SLICING:
                table = new AdvancedJTable_subnets(callback,openStackClient);
                break;
            case LIMITS:
                table = new AdvancedJTable_limits(callback,openStackClient);
                break;
            case QUOTAS:
                table = new AdvancedJTable_quotas(callback,openStackClient);
                break;
            case QUOTASUSAGE:
                table = new AdvancedJTable_quotasUsage(callback,openStackClient);
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

         /* Load current network state */
        this.callback.getOpenStackNet().getOsClients().forEach(n-> {n.clearList();n.fillList();});
        this.callback.getOpenStackNet().fillQuotasAndLimits();

        final NetPlan currentState = callback.getDesign();
        if (ErrorHandling.isDebugEnabled()) currentState.checkCachesConsistency();

        this.recomputNetPlanView();


        for(OpenStackClient openStackClient: netPlanViewTable.keySet()){
            netPlanViewTable.get(openStackClient).values().stream().forEach(t -> {t.getFirst().updateView(); t.getSecond().updateHeader();});
        }

        System.out.println("Problem with de problem" + callback.getOpenStackNet().openStackLimits.size() +" " + ajTables.size());

        ajTables.values().stream().map(t -> t.getFirst()).forEach(t -> t.updateView());
        ajTables.values().stream().map(t -> t.getSecond()).forEach(t -> t.updateHeader());


        // Update filter header
       /* for (AJTableType type :  Arrays.asList(AJTableType.LIMITS, AJTableType.QUOTAS,AJTableType.QUOTASUSAGE))
        {
            ((FilteredTablePanel) ajTables.get(type).getSecond()).updateHeader();
            ((AdvancedJTable_abstractElement<Object>) ajTables.get(type).getFirst()).updateView();
            /*if (ajTables.get(type).getSecond() instanceof FilteredTablePanel)
                ((FilteredTablePanel) ajTables.get(type).getSecond()).updateHeader();
            if (ajTables.get(type).getSecond() instanceof JSplitPane)
            {
                if (!(((JSplitPane) ajTables.get(type).getSecond()).getTopComponent() instanceof FilteredTablePanel))
                    throw new Net2PlanException("Bad");
                ((FilteredTablePanel)((JSplitPane) ajTables.get(type).getSecond()).getTopComponent()).updateHeader();
            }
            else
                throw new Net2PlanException("Bad");
        }*/



        if (ErrorHandling.isDebugEnabled()) currentState.checkCachesConsistency();


    }

    public void recomputNetPlanView() {

        /* Save current selected tab */
        final int selectedIndexFirstLevel = viewEditHighLevelTabbedPane.getSelectedIndex() == -1 ? 0 : viewEditHighLevelTabbedPane.getSelectedIndex();
        int selectedIndexSecondLevel = -1;
        if (viewEditHighLevelTabbedPane.getSelectedComponent() instanceof JTabbedPane)
            selectedIndexSecondLevel = ((JTabbedPane) viewEditHighLevelTabbedPane.getSelectedComponent()).getSelectedIndex();


        final NetPlan np = callback.getDesign();
        netPlanViewTable.clear();
        layerSubTabbedPaneMap.clear();

        viewEditHighLevelTabbedPane.removeAll();
        slicingTabbedPane.removeAll();

        ajTables.clear();

        if(callback.getOpenStackNet().getOsClients().size() >0) {
            for (AJTableType ajType : Arrays.asList(AJTableType.LIMITS, AJTableType.QUOTAS, AJTableType.QUOTASUSAGE))
                ajTables.put(ajType, createPanelComponentInfo(ajType, callback.getOpenStackNet().getOsClients().get(0)));

            for (AJTableType type : Arrays.asList(AJTableType.LIMITS, AJTableType.QUOTAS, AJTableType.QUOTASUSAGE))
                slicingTabbedPane.addTab(type.getTabName(), ajTables.get(type).getSecond());
        }
        viewEditHighLevelTabbedPane.addTab(AJTableType.SLICING.tabName, slicingTabbedPane);

         for (OpenStackClient openStackClient : callback.getOpenStackNet().getOsClients()) {

             layerSubTabbedPaneMap.put(openStackClient, new JTabbedPane());

            final JTabbedPane subpaneThisLayer = layerSubTabbedPaneMap.get(openStackClient);

            identityTabbedPane.put(openStackClient, new JTabbedPane ());
            networkTabbedPane.put(openStackClient, new JTabbedPane ());
            computeTabbedPane.put(openStackClient, new JTabbedPane ());
            imageTabbedPane.put(openStackClient, new JTabbedPane ());
            telemetryTabbedPane.put(openStackClient, new JTabbedPane ());

            //ajTables.clear();

            final Map<AJTableType, Pair<AdvancedJTable_abstractElement, FilteredTablePanel>> ajTables_prub = new EnumMap<>(AJTableType.class);

            for (AJTableType ajType : AJTableType.values())
                ajTables_prub.put(ajType, createPanelComponentInfo(ajType,openStackClient));

            netPlanViewTable.put(openStackClient,ajTables_prub);

            for (AJTableType ajType : AJTableType.values())
            {

                if (ajType == ajType.KEYSTONE)
                {

                     for (AJTableType type : Arrays.asList(AJTableType.USERS, AJTableType.PROJECTS,AJTableType.DOMAINS,AJTableType.ENDPOINTS,AJTableType.SERVICES,AJTableType.REGIONS,AJTableType.CREDENTIALS,AJTableType.GROUPS,AJTableType.POLICIES,AJTableType.ROLES))
                        identityTabbedPane.get(openStackClient).addTab(type.getTabName(), ajTables_prub.get(type).getSecond());

                    subpaneThisLayer.addTab(ajType.getTabName(), identityTabbedPane.get(openStackClient));


                }
                else if (ajType == ajType.NEUTRON)
                {

                    for (AJTableType type : Arrays.asList(AJTableType.NETWORKS, AJTableType.SUBNETS,AJTableType.ROUTERS,AJTableType.PORTS))
                        networkTabbedPane.get(openStackClient).addTab(type.getTabName(), ajTables_prub.get(type).getSecond());

                    subpaneThisLayer.addTab(ajType.getTabName(), networkTabbedPane.get(openStackClient));


                }else if(ajType == ajType.NOVA){


                    for (AJTableType type : Arrays.asList(AJTableType.SERVERS, AJTableType.FLAVORS,AJTableType.FLOATINGIPS,AJTableType.KEYPAIRS,AJTableType.SECURITYGROUPS,AJTableType.HOSTRESOURCES))
                        computeTabbedPane.get(openStackClient).addTab(type.getTabName(), ajTables_prub.get(type).getSecond());

                    subpaneThisLayer.addTab(ajType.getTabName(), computeTabbedPane.get(openStackClient));

                } else if(ajType == ajType.GLANCE){


                    for (AJTableType type : Arrays.asList(AJTableType.IMAGES))
                        imageTabbedPane.get(openStackClient).addTab(type.getTabName(), ajTables_prub.get(type).getSecond());

                    subpaneThisLayer.addTab(ajType.getTabName(), imageTabbedPane.get(openStackClient));

                }else if(ajType == ajType.GNOCCHI){


                    for (AJTableType type : Arrays.asList(AJTableType.RESOURCES,AJTableType.METERS, AJTableType.MEASURES))
                        telemetryTabbedPane.get(openStackClient).addTab(type.getTabName(), ajTables_prub.get(type).getSecond());

                    subpaneThisLayer.addTab(ajType.getTabName(), telemetryTabbedPane.get(openStackClient));

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
