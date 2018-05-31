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


package com.net2plan.gui.plugins.networkDesign.aboutItPane;

import java.awt.BorderLayout;
import java.util.*;

import javax.swing.*;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
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

/**
 * Targeted to evaluate network designs from the offline tool simulating the
 * network operation. Different aspects such as network resilience,
 * connection-admission-control and time-varying traffic resource allocation,
 * or even mix of them, can be analyzed using the online simulator.
 *
 * @author Pablo Pavon-Marino, Jose-Luis Izquierdo-Zaragoza
 * @since 0.3.0
 */
public class AboutIt extends JPanel
{
    private final GUINetworkDesign callback;
    private  JTextArea upperText;
    final String NEWLINE = String.format("%n");
    private final Map<ViewEditTopologyTablesPane.AJTableType, Pair<AdvancedJTable_networkElement, FilteredTablePanel>> ajTables = new EnumMap<>(ViewEditTopologyTablesPane.AJTableType.class);
    private final JTabbedPane aboutIt;
    private final JMenuBar menuBar;

    public AboutIt(GUINetworkDesign callback)
    {
        super();
        this.callback = callback;

        upperText = new JTextArea();
        upperText.setFont(new JLabel().getFont());
        upperText.setBackground(new JLabel().getBackground());
        upperText.setLineWrap(true);
        upperText.setEditable(false);
        upperText.setWrapStyleWord(true);
        upperText.setText("What is OpenStack4j?" +NEWLINE +NEWLINE
                +"OpenStack4j is an open source library that helps you manage an OpenStack deployment. "+NEWLINE+NEWLINE
                +"It is a fluent based API giving you full control over the various OpenStack services." + NEWLINE+NEWLINE
                +"OpenStack4j is broken out into several major API abstractions as Java libraries.");

        this.setLayout(new BorderLayout());

        this.aboutIt = new JTabbedPane();

        final JSplitPane splitPane = new JSplitPane();
        splitPane.setBottomComponent(upperText);
        splitPane.setLeftComponent(aboutIt);

        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.3);
        splitPane.setEnabled(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0.4);

        this.add(splitPane, BorderLayout.CENTER);

        for (ViewEditTopologyTablesPane.AJTableType ajType : ViewEditTopologyTablesPane.AJTableType.values())
            ajTables.put(ajType, createPanelComponentInfo(ajType));


        /* The rest of high level tabs */
       /* for (ViewEditTopologyTablesPane.AJTableType type : Arrays.asList(ViewEditTopologyTablesPane.AJTableType.INFORMATION))
            aboutIt.addTab(type.getTabName(), ajTables.get(type).getSecond());*/


        menuBar = new JMenuBar();

        this.add(menuBar, BorderLayout.SOUTH);
    }

    private Pair<AdvancedJTable_networkElement, FilteredTablePanel> createPanelComponentInfo(ViewEditTopologyTablesPane.AJTableType type)
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

    public void updateView()
    {
        /* Load current network state */
        final NetPlan currentState = callback.getDesign();
        if (ErrorHandling.isDebugEnabled()) currentState.checkCachesConsistency();

        ajTables.values().stream().map(t -> t.getFirst()).forEach(t -> t.updateView());

        // Update filter header
        for (ViewEditTopologyTablesPane.AJTableType type : ViewEditTopologyTablesPane.AJTableType.values())
            ajTables.get(type).getSecond().updateHeader();



        if (ErrorHandling.isDebugEnabled()) currentState.checkCachesConsistency();
    }


    public void updateText(){
        upperText.setText("In this tab you can see the information about the project and the user who has connected OpenStack"+NEWLINE
                        + "Project information: " + NEWLINE
                        + "ID : " + callback.getOpenStackNet().getOSClientV3().getToken().getProject().getId()+ NEWLINE
                        + "DESCRIPTION : " + callback.getOpenStackNet().getOSClientV3().getToken().getProject().getDescription()+ NEWLINE
                        + "DOMAIN ID : " + callback.getOpenStackNet().getOSClientV3().getToken().getProject().getDomainId()+ NEWLINE
                        + "NAME : " + callback.getOpenStackNet().getOSClientV3().getToken().getProject().getName()+ NEWLINE
                        + "PARENT ID : " + callback.getOpenStackNet().getOSClientV3().getToken().getProject().getParentId()+ NEWLINE
        );
    }


}
