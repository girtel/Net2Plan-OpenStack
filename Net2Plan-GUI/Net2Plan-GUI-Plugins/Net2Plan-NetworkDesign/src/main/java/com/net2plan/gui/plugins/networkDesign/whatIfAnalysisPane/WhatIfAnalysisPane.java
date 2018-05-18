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


package com.net2plan.gui.plugins.networkDesign.whatIfAnalysisPane;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.*;

import javax.swing.*;

import com.google.common.collect.Sets;
import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.*;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.VisualizationState;
import com.net2plan.gui.plugins.utils.FilteredTablePanel;
import com.net2plan.gui.utils.ParameterValueDescriptionPanel;
import com.net2plan.gui.utils.RunnableSelector;
import com.net2plan.interfaces.networkDesign.Configuration;
import com.net2plan.interfaces.networkDesign.Demand;
import com.net2plan.interfaces.networkDesign.Link;
import com.net2plan.interfaces.networkDesign.MulticastDemand;
import com.net2plan.interfaces.networkDesign.Net2PlanException;
import com.net2plan.interfaces.networkDesign.NetPlan;
import com.net2plan.interfaces.networkDesign.Node;
import com.net2plan.interfaces.simulation.IEventGenerator;
import com.net2plan.interfaces.simulation.SimEvent;
import com.net2plan.internal.ErrorHandling;
import com.net2plan.internal.IExternal;
import com.net2plan.internal.SystemUtils;
import com.net2plan.internal.plugins.IGUIModule;
import com.net2plan.internal.sim.EndSimulationException;
import com.net2plan.internal.sim.IGUISimulationListener;
import com.net2plan.internal.sim.SimCore;
import com.net2plan.internal.sim.SimCore.SimState;
import com.net2plan.internal.sim.SimKernel;
import com.net2plan.utils.ClassLoaderUtils;
import com.net2plan.utils.Pair;
import com.net2plan.utils.Triple;
import org.openstack4j.model.identity.v3.Project;

/**
 * Targeted to evaluate network designs from the offline tool simulating the
 * network operation. Different aspects such as network resilience,
 * connection-admission-control and time-varying traffic resource allocation,
 * or even mix of them, can be analyzed using the online simulator.
 *
 * @author Pablo Pavon-Marino, Jose-Luis Izquierdo-Zaragoza
 * @since 0.3.0
 */
public class WhatIfAnalysisPane extends JPanel
{
    private final GUINetworkDesign callback;
    private Thread simThread;
    private ParameterValueDescriptionPanel simulationConfigurationPanel;
    private SimKernel simKernel;
    private Throwable lastWhatIfExecutionException;
    private  JTextArea upperText;
    final String NEWLINE = String.format("%n");
    private final Map<ViewEditTopologyTablesPane.AJTableType, Pair<AdvancedJTable_networkElement, FilteredTablePanel>> ajTables = new EnumMap<>(ViewEditTopologyTablesPane.AJTableType.class);
    private final JTabbedPane whatIfAnalysisPane;
    private final JMenuBar menuBar;
    public WhatIfAnalysisPane(GUINetworkDesign callback)
    {
        super();
        this.callback = callback;

        upperText = new JTextArea();
        upperText.setFont(new JLabel().getFont());
        upperText.setBackground(new JLabel().getBackground());
        upperText.setLineWrap(true);
        upperText.setEditable(false);
        upperText.setWrapStyleWord(true);
        upperText.setText("No available");
        this.setLayout(new BorderLayout());

        this.whatIfAnalysisPane = new JTabbedPane();

        final JSplitPane splitPane = new JSplitPane();
        splitPane.setBottomComponent(upperText);
        splitPane.setLeftComponent(whatIfAnalysisPane);

        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setResizeWeight(0.3);
        splitPane.setEnabled(true);
        splitPane.setOneTouchExpandable(true);
        splitPane.setDividerLocation(0.4);

        this.add(splitPane, BorderLayout.CENTER);

        for (ViewEditTopologyTablesPane.AJTableType ajType : ViewEditTopologyTablesPane.AJTableType.values())
            ajTables.put(ajType, createPanelComponentInfo(ajType));


        /* The rest of high level tabs */
        for (ViewEditTopologyTablesPane.AJTableType type : Arrays.asList(ViewEditTopologyTablesPane.AJTableType.INFORMATION))
            whatIfAnalysisPane.addTab(type.getTabName(), ajTables.get(type).getSecond());


        menuBar = new JMenuBar();

        this.add(menuBar, BorderLayout.SOUTH);
    }

    private Pair<AdvancedJTable_networkElement, FilteredTablePanel> createPanelComponentInfo(ViewEditTopologyTablesPane.AJTableType type)
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
            case INFORMATION:
                table = new AdvancedJTable_informationProject(callback);
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
        for (ViewEditTopologyTablesPane.AJTableType type : ViewEditTopologyTablesPane.AJTableType.values())
            ajTables.get(type).getSecond().updateHeader();



        if (ErrorHandling.isDebugEnabled()) currentState.checkCachesConsistency();
    }

    /**
     * Shows the tab corresponding associated to a network element.
     *
     * @param type   Network element type
     */
    public void selectItemTab(ViewEditTopologyTablesPane.AJTableType type)
    {
        switch (type)
        {
            case ROUTERS:
            case USERS:
            case NETWORKS:
            case INFORMATION:
            case SUBNETS:
                whatIfAnalysisPane.setSelectedComponent(ajTables.get(type).getSecond());
                break;
            default:
                System.out.println(type);
                assert false;
        }
    }


    /**
     * Runs a short simulation to perform the what-if analysis. At the end, the resulting netplan is set
     *
     *
     */


    public void updateText(){
        upperText.setText("In this tab you can see the information about the project and the user who has connected OpenStack"+NEWLINE
                        + "Project information: " + NEWLINE
                        + "ID : " + callback.getOpenStackNet().getOs().getToken().getProject().getId()+ NEWLINE
                        + "DESCRIPTION : " + callback.getOpenStackNet().getOs().getToken().getProject().getDescription()+ NEWLINE
                        + "DOMAIN ID : " + callback.getOpenStackNet().getOs().getToken().getProject().getDomainId()+ NEWLINE
                        + "NAME : " + callback.getOpenStackNet().getOs().getToken().getProject().getName()+ NEWLINE
                        + "PARENT ID : " + callback.getOpenStackNet().getOs().getToken().getProject().getParentId()+ NEWLINE
        );
    }


}
