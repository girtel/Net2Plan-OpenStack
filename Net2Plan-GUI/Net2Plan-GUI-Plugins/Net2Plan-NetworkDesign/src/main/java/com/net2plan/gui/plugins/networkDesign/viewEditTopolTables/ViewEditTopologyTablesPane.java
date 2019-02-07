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
import javax.swing.table.TableModel;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.net2plan.gui.plugins.networkDesign.openStack.blockstorage.OpenStackVolume;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.*;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.*;
import com.net2plan.gui.plugins.networkDesign.openStack.image.OpenStackImageV2;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackNetwork;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackPort;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackRouter;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackSubnet;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackGnocchiMeasure;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackMeter;
import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackResource;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.*;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.blockstorage.AdvandecJTable_volume;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute.*;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.extra.AdvancedJTable_summaries;
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

@SuppressWarnings("unchecked")
public class ViewEditTopologyTablesPane extends JPanel
{
    public enum AJTableType
    {

        //Identity (Keystone)
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
        NETWORKS("Networks"),
        ROUTERS("Routers"),
        PORTS("Ports"),
        SUBNETS("Subnets"),

        //Compute (Nova)
        SERVERS("Servers"),
        FLAVORS("Flavors"),
        FLOATINGIPS("Floating IPs"),
        KEYPAIRS("Keypairs"),
        SECURITYGROUPS("Security groups"),
        HOSTRESOURCES("Host resources"),
        RULES("Rules"),

        //Image (Glance)
        IMAGES("Images"),

        VOLUMES("Volumes"),

        //Telemetry (Ceilometer)
        RESOURCES("Resources"),
        METERS("Metrics"),
        MEASURES("Measures"),

        //SLICING
        SLICING("Slicing"),
        QUOTAS("Quotas"),
        LIMITS("Limits"),
        QUOTASUSAGE("Quota Usage"),

        SUMMARY("Summary")
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
    private final Map<OpenStackClient,JTabbedPane> blockStorageTabbedPane = new HashMap<>();
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

                  //  System.out.println("Tab: " + viewEditHighLevelTabbedPane.getTitleAt(viewEditHighLevelTabbedPane.getSelectedIndex()));
                    OpenStackClient openStackClient = callback.getOpenStackNet().getOsClients().stream().filter(n -> n.getName().equals(viewEditHighLevelTabbedPane.getTitleAt(viewEditHighLevelTabbedPane.getSelectedIndex()))).collect(Collectors.toList()).get(0);
                   // System.out.println("Net2plan nodes "+ openStackClient.getNetPlanDesign().getNodes().size());
                    callback.setActualOpenStackClient(openStackClient);
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

    public Pair<AdvancedJTable_abstractElement, FilteredTablePanel> createPanelComponentInfo(AJTableType type, OpenStackClient openStackClient) {
        AdvancedJTable_networkElement table = null;
         switch (type)
        {
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
            case RULES:
                table = new AdvancedJTable_rules(callback,openStackClient);
                break;

            /*IMAGE*/
            case IMAGES:
                table = new AdvancedJTable_imagesV2(callback,openStackClient);
                break;

            /*IMAGE*/
            case VOLUMES:
                table = new AdvandecJTable_volume(callback,openStackClient);
                break;

            /*TELEMETRY*/
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

            case SUMMARY:
                table = new AdvancedJTable_summaries(callback,openStackClient);
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
        this.callback.getOpenStackNet().getOsClients().forEach(n-> {n.clearClientListsAndTopology();n.fillClientListsAndTopology();});
        this.callback.getOpenStackNet().fillSlicingTabTablesOfNet();

        final NetPlan currentState = callback.getDesign();
        if (ErrorHandling.isDebugEnabled()) currentState.checkCachesConsistency();

        this.recomputNetPlanView();


        for(OpenStackClient openStackClient: netPlanViewTable.keySet()){
            netPlanViewTable.get(openStackClient).values().stream().forEach(t -> {t.getFirst().updateView(); t.getSecond().updateHeader();});
        }

        //System.out.println("Problem with de problem" + callback.getOpenStackNet().openStackLimits.size() +" " + ajTables.size());

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

    public void updateViewForDeterminateAjtableAndOpenStackClient(List<AJTableType> ajTableTypeTables,OpenStackClient openStackClient) {

        //openStackClient.updateThisList();
        this.callback.getOpenStackNet().fillSlicingTabTablesOfNet();


       // this.recomputNetPlanView();

        for(AJTableType ajTableType: ajTableTypeTables) {
            netPlanViewTable.get(openStackClient).get(ajTableType).getFirst().updateView();
            netPlanViewTable.get(openStackClient).get(ajTableType).getSecond().updateHeader();
        }

        ajTables.values().stream().map(t -> t.getFirst()).forEach(t -> t.updateView());
        ajTables.values().stream().map(t -> t.getSecond()).forEach(t -> t.updateHeader());


    }

    public void recomputNetPlanView() {

       // System.out.println("recomput");
        /*
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
         */
        /* Save current selected tab */
        final int selectedIndexFirstLevel = viewEditHighLevelTabbedPane.getSelectedIndex() == -1 ? 0 : viewEditHighLevelTabbedPane.getSelectedIndex();
        int selectedIndexSecondLevel = -1;
        int selectedIndexThirdLevel = -1;
        if (viewEditHighLevelTabbedPane.getSelectedComponent() instanceof JTabbedPane)
        {
            selectedIndexSecondLevel = ((JTabbedPane) viewEditHighLevelTabbedPane.getSelectedComponent()).getSelectedIndex();
            if (((JTabbedPane) viewEditHighLevelTabbedPane.getSelectedComponent()).getSelectedComponent() instanceof  JTabbedPane)
                selectedIndexThirdLevel = ((JTabbedPane) ((JTabbedPane) viewEditHighLevelTabbedPane.getSelectedComponent()).getSelectedComponent()).getSelectedIndex();
        }



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
            blockStorageTabbedPane.put(openStackClient,new JTabbedPane());
            //ajTables.clear();

            final Map<AJTableType, Pair<AdvancedJTable_abstractElement, FilteredTablePanel>> ajTables_prub = new EnumMap<>(AJTableType.class);

            for (AJTableType ajType : AJTableType.values())
                ajTables_prub.put(ajType, createPanelComponentInfo(ajType,openStackClient));

            netPlanViewTable.put(openStackClient,ajTables_prub);


            for (AJTableType type : Arrays.asList(AJTableType.USERS, AJTableType.PROJECTS,AJTableType.DOMAINS,AJTableType.ENDPOINTS,AJTableType.SERVICES,AJTableType.REGIONS,AJTableType.CREDENTIALS,AJTableType.GROUPS,AJTableType.POLICIES,AJTableType.ROLES))
                identityTabbedPane.get(openStackClient).addTab(type.getTabName(), ajTables_prub.get(type).getSecond());

            subpaneThisLayer.addTab("Keystone", identityTabbedPane.get(openStackClient));

            for (AJTableType type : Arrays.asList(AJTableType.NETWORKS, AJTableType.SUBNETS,AJTableType.ROUTERS,AJTableType.PORTS))
                networkTabbedPane.get(openStackClient).addTab(type.getTabName(), ajTables_prub.get(type).getSecond());
            subpaneThisLayer.addTab("Neutron", networkTabbedPane.get(openStackClient));


            for (AJTableType type : Arrays.asList(AJTableType.SERVERS, AJTableType.FLAVORS,AJTableType.FLOATINGIPS,AJTableType.KEYPAIRS,AJTableType.SECURITYGROUPS,AJTableType.RULES,AJTableType.HOSTRESOURCES))
                computeTabbedPane.get(openStackClient).addTab(type.getTabName(), ajTables_prub.get(type).getSecond());
            subpaneThisLayer.addTab("Nova", computeTabbedPane.get(openStackClient));

            for (AJTableType type : Arrays.asList(AJTableType.IMAGES))
                imageTabbedPane.get(openStackClient).addTab(type.getTabName(), ajTables_prub.get(type).getSecond());
            subpaneThisLayer.addTab("Glance", imageTabbedPane.get(openStackClient));

             for (AJTableType type : Arrays.asList(AJTableType.VOLUMES))
                 blockStorageTabbedPane.get(openStackClient).addTab(type.getTabName(), ajTables_prub.get(type).getSecond());
             subpaneThisLayer.addTab("Cinder", blockStorageTabbedPane.get(openStackClient));

            for (AJTableType type : Arrays.asList(AJTableType.RESOURCES))
                telemetryTabbedPane.get(openStackClient).addTab(type.getTabName(), ajTables_prub.get(type).getSecond());
            subpaneThisLayer.addTab("Ceilometer", telemetryTabbedPane.get(openStackClient));



            viewEditHighLevelTabbedPane.addTab(openStackClient.getName().equals("")? openStackClient.getName() : openStackClient.getName() , subpaneThisLayer);
        }


        viewEditHighLevelTabbedPane.setSelectedIndex(selectedIndexFirstLevel);
        if (viewEditHighLevelTabbedPane.getSelectedComponent() instanceof JTabbedPane && selectedIndexSecondLevel >= 0) {
            ((JTabbedPane) viewEditHighLevelTabbedPane.getSelectedComponent()).setSelectedIndex(selectedIndexSecondLevel);
            if (((JTabbedPane) viewEditHighLevelTabbedPane.getSelectedComponent()).getSelectedComponent() instanceof  JTabbedPane && selectedIndexThirdLevel >= 0)
                ((JTabbedPane) ((JTabbedPane) viewEditHighLevelTabbedPane.getSelectedComponent()).getSelectedComponent()).setSelectedIndex(selectedIndexThirdLevel);
        }
        }

    /**
     * Shows the tab corresponding associated to a network element.
     *
     */

    public void selectTabAndGivenItems( OpenStackClient openStackClient , List<OpenStackNetworkElement> elements)
    {
        if (elements == null) return;
        if (elements.isEmpty()) return;
        
        AJTableType tableType = getType(elements.get(0));

      //  private final Map<OpenStackClient,Map<AJTableType, Pair<AdvancedJTable_abstractElement, FilteredTablePanel>>> netPlanViewTable = new HashMap<> (); //new EnumMap<>(AJTableType.class);

        final AdvancedJTable_abstractElement table = netPlanViewTable.get(openStackClient).get(tableType).getFirst();

        table.clearSelection();

        final TableModel model = table.getModel();
        final int numRows = table.getRowCount();
        selectItemTab(openStackClient, tableType);

        final Set<String> idsToSelect = elements.stream().map(e->e.getId()).collect(Collectors.toSet());

        for (int row = 0; row < numRows; row++)
        {
            try
            {
                final Object obj = model.getValueAt(row, 0);
                if (obj == null) continue;

                final String idThisRow = (String) obj;
                if (idsToSelect.contains(idThisRow))
                {
                    final int viewRow = table.convertRowIndexToView(row);

                    table.addRowSelectionInterval(viewRow, viewRow);
                    table.scrollRectToVisible(table.getCellRect(viewRow, 0, true));

                    if (table.getSelectedRowCount() == elements.size()) return;
                }
            } catch (ClassCastException e)
            {
                e.printStackTrace();
            }
        }
    }

   
    public void selectItemTab(OpenStackClient openStackClient, AJTableType type)
    {
        if (!layerSubTabbedPaneMap.containsKey(openStackClient))
            throw new Net2PlanException("Wrong openstack client");

        final FilteredTablePanel pane = netPlanViewTable.get(openStackClient).get(type).getSecond();

        viewEditHighLevelTabbedPane.setSelectedComponent(layerSubTabbedPaneMap.get(openStackClient));

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
                layerSubTabbedPaneMap.get(openStackClient).setSelectedComponent(identityTabbedPane.get(openStackClient));
                identityTabbedPane.get(openStackClient).setSelectedComponent(pane);
                break;

            case NETWORKS:
            case ROUTERS:
            case PORTS:
            case SUBNETS:
                layerSubTabbedPaneMap.get(openStackClient).setSelectedComponent(networkTabbedPane.get(openStackClient));
                networkTabbedPane.get(openStackClient).setSelectedComponent(pane);
                break;

            case SERVERS:
            case FLAVORS:
            case FLOATINGIPS:
            case KEYPAIRS:
            case SECURITYGROUPS:
            case HOSTRESOURCES:
            case RULES:
                layerSubTabbedPaneMap.get(openStackClient).setSelectedComponent(computeTabbedPane.get(openStackClient));
                computeTabbedPane.get(openStackClient).setSelectedComponent(pane);
            break;
            case IMAGES:
                layerSubTabbedPaneMap.get(openStackClient).setSelectedComponent(imageTabbedPane.get(openStackClient));
                imageTabbedPane.get(openStackClient).setSelectedComponent(pane);
                break;
            case RESOURCES:
            case METERS:
            case MEASURES:
                layerSubTabbedPaneMap.get(openStackClient).setSelectedComponent(telemetryTabbedPane.get(openStackClient));
                telemetryTabbedPane.get(openStackClient).setSelectedComponent(pane);
                break;
            default:
                System.out.println(type+"este");
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

    public AJTableType getType(OpenStackNetworkElement openStackNetworkElement){

        if(openStackNetworkElement instanceof OpenStackNetwork) return AJTableType.NETWORKS;
        if(openStackNetworkElement instanceof OpenStackRouter)  return AJTableType.ROUTERS;
        if(openStackNetworkElement instanceof OpenStackSubnet) return  AJTableType.SUBNETS;
        if(openStackNetworkElement instanceof OpenStackPort) return  AJTableType.PORTS;

        if(openStackNetworkElement instanceof OpenStackFlavor) return  AJTableType.FLAVORS;
        if(openStackNetworkElement instanceof OpenStackRule) return  AJTableType.RULES;
        if(openStackNetworkElement instanceof OpenStackFloatingIp) return  AJTableType.FLOATINGIPS;
        if(openStackNetworkElement instanceof OpenStackHostResource) return  AJTableType.HOSTRESOURCES;
        if(openStackNetworkElement instanceof OpenStackImageV2) return  AJTableType.IMAGES;
        if(openStackNetworkElement instanceof OpenStackKeypair) return  AJTableType.KEYPAIRS;
        if(openStackNetworkElement instanceof OpenStackQuotas) return  AJTableType.QUOTAS;
        if(openStackNetworkElement instanceof OpenStackQuotasUsage) return  AJTableType.QUOTASUSAGE;
        if(openStackNetworkElement instanceof OpenStackSecurityGroup) return  AJTableType.SECURITYGROUPS;
        if(openStackNetworkElement instanceof OpenStackServer) return  AJTableType.SERVERS;

        if(openStackNetworkElement instanceof OpenStackCredential) return  AJTableType.CREDENTIALS;
        if(openStackNetworkElement instanceof OpenStackDomain) return  AJTableType.DOMAINS;
        if(openStackNetworkElement instanceof OpenStackEndpoint) return  AJTableType.ENDPOINTS;
        if(openStackNetworkElement instanceof OpenStackGroup) return  AJTableType.GROUPS;
        if(openStackNetworkElement instanceof OpenStackPolicy) return  AJTableType.POLICIES;
        if(openStackNetworkElement instanceof OpenStackProject) return  AJTableType.PROJECTS;
        if(openStackNetworkElement instanceof OpenStackRegion) return  AJTableType.REGIONS;
        if(openStackNetworkElement instanceof OpenStackRole) return  AJTableType.ROLES;
        if(openStackNetworkElement instanceof OpenStackService) return  AJTableType.SERVICES;
        if(openStackNetworkElement instanceof OpenStackUser) return  AJTableType.USERS;

        if(openStackNetworkElement instanceof OpenStackVolume) return  AJTableType.VOLUMES;

        if(openStackNetworkElement instanceof OpenStackMeter) return  AJTableType.METERS;
        if(openStackNetworkElement instanceof OpenStackResource) return  AJTableType.RESOURCES;
        if(openStackNetworkElement instanceof OpenStackGnocchiMeasure) return  AJTableType.MEASURES;

        return  AJTableType.NETWORKS;
    }
}
