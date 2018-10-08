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


import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
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
        System.out.println("Advanced_network getAllAbstractElementsInTable " + openStackClient.getName());
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

            if (value instanceof String)
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

            if(value != null) {
                if (value.getClass().equals(Boolean.class)) changeValueOfBoolean(columnModelIndexOfClickOrMinus1IfOut);

                pickSelectionHyperLink(value, columnModelIndexOfClickOrMinus1IfOut);


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
        completeListIncludingCommonColumns.add((AjtColumnInfo<T>) (AjtColumnInfo<?>) AjtColumnInfo.createTagsColumn(this));
        completeListIncludingCommonColumns.add((AjtColumnInfo<T>) (AjtColumnInfo<?>) AjtColumnInfo.createAttributesColumn(this));
        return completeListIncludingCommonColumns;
    }

    public void pickSelectionHyperLink(Object value , int columnModelIndexOfClickOrMinus1IfOut){

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

                callback.getOpenStackNet().getOsClients().forEach(n->n.updateMeasuresList(value.toString()));

                break;




        }
    }

    @Override
    public void generalTableForm(String title,Map<String,String> headers){
        JFrame jfM = new JFrame(title);
        jfM.setLayout(null);

        JButton jbP1 = new JButton("Enter");

        JPanel jp1 = new JPanel(new GridLayout(headers.size()+1, 2, 15, 10));//filas, columnas, espacio entre filas, espacio entre columnas

        JLabel l6 = new JLabel("Properties", SwingConstants.LEFT);
        jp1.add(l6);
        JLabel label = new JLabel("", SwingConstants.LEFT);
        jp1.add(label);

        for(String key : headers.keySet()){
            JLabel jlabel = new JLabel(key, SwingConstants.LEFT);
            jp1.add(jlabel);
            switch (headers.get(key)){

                case "Boolean":
                    JCheckBox jCheckBox = new JCheckBox();
                    jp1.add(jCheckBox);
                    break;
                case "Select":
                    JComboBox jComboBox;
                    Object[] stockArr = new String[1];
                    stockArr[0] = "empty";
                    List<String> stockList = new ArrayList<>() ;
                    switch (key){
                        case "Tenant ID":
                            stockArr[0] = openStackClient.getProjectId();
                            break;
                        case "Network ID":
                            stockList = openStackClient.openStackNetworks.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                            stockArr = new String[stockList.size()];
                            stockArr = stockList.toArray(stockArr);
                            break;
                        case "Subnet ID":
                            stockList = openStackClient.openStackSubnets.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                            stockArr = new String[stockList.size()];
                            stockArr = stockList.toArray(stockArr);
                            break;
                        case "Flavor ID":
                            stockList = openStackClient.openStackFlavors.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                            stockArr = new String[stockList.size()];
                            stockArr = stockList.toArray(stockArr);
                            break;
                        case "Image ID":
                            stockList = openStackClient.openStackImages.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                            stockArr = new String[stockList.size()];
                            stockArr = stockList.toArray(stockArr);
                            break;
                        case "Port ID":
                            stockList = openStackClient.openStackPorts.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                            stockArr = new String[stockList.size()];
                            stockArr = stockList.toArray(stockArr);
                            break;
                        case "Router ID":
                            stockList = openStackClient.openStackRouters.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                            stockArr = new String[stockList.size()];
                            stockArr = stockList.toArray(stockArr);
                            break;
                        case "Server ID":
                            stockList = openStackClient.openStackServers.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                            stockArr = new String[stockList.size()];
                            stockArr = stockList.toArray(stockArr);
                            break;
                        case "IP version":
                            stockArr = IPVersionType.values();
                            break;
                        case "Network type":
                            stockArr = NetworkType.values();
                            break;
                        case "Service type":
                            stockArr = ServiceType.values();
                            break;
                        case "Facing":
                            stockArr = Facing.values();
                            break;
                        case "Pool Name":
                            stockList = openStackClient.getClient().compute().floatingIps().getPoolNames();
                            stockArr = new String[stockList.size()];
                            stockArr = stockList.toArray(stockArr);
                            break;
                    }

                    jComboBox = new JComboBox(stockArr);
                    jp1.add(jComboBox);
                    break;
                case "Special-ipv4masc":
                    try {
                        MaskFormatter mf = new MaskFormatter("###-###-###-###/##");
                        JFormattedTextField f = new JFormattedTextField(mf);
                        jp1.add(f);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "Special-ipv4":
                    try {
                        MaskFormatter mf = new MaskFormatter("###-###-###-###");
                        JFormattedTextField f = new JFormattedTextField(mf);
                        jp1.add(f);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;
                    default:
                        JTextField jtextField = new JTextField();
                        jp1.add(jtextField);
                        break;
            }

        }


        jp1.setVisible(true);
        jp1.setBounds(10, 10, 200, 50*headers.size());
        jbP1.setBounds(75, 60*headers.size(), 90, 25);

        jbP1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Component [] components = jp1.getComponents();
                JSONObject jsonObject = new JSONObject();

                for(int i = 3;i< components.length;i=i+2){

                    switch (headers.get(((JLabel)components[i-1]).getText())){
                        case "Select":
                            jsonObject.put( ((JLabel)components[i-1]).getText(),((JComboBox)components[i]).getSelectedItem().toString());
                            break;
                        case "Boolean":
                            jsonObject.put( ((JLabel)components[i-1]).getText(),((JCheckBox)components[i]).isSelected());
                            break;
                        case "IP version":
                            jsonObject.put( ((JLabel)components[i-1]).getText(),((JComboBox)components[i]).getSelectedItem());
                            break;
                            default:
                                jsonObject.put( ((JLabel)components[i-1]).getText(),((JTextField)components[i]).getText());
                                break;
                    }
                 }

                switch (ajtType){

                    /*NETWORK*/
                    case PORTS:
                        openStackClient.getOpenStackNetCreate().createOpenStackPort(jsonObject);
                        break;
                    case NETWORKS:
                        openStackClient.getOpenStackNetCreate().createOpenStackNetwork(jsonObject);
                        break;
                    case SUBNETS:
                        openStackClient.getOpenStackNetCreate().createOpenStackSubnet(jsonObject);
                        break;
                    case ROUTERS:
                        openStackClient.getOpenStackNetCreate().createOpenStackRouter(jsonObject);
                        break;

                    /*COMPUTE*/
                    case SERVERS:
                        openStackClient.getOpenStackNetCreate().createOpenStackServer(jsonObject);
                        break;
                    case FLAVORS:
                        openStackClient.getOpenStackNetCreate().createOpenStackFlavor(jsonObject);
                        break;
                    case FLOATINGIPS:
                        openStackClient.getOpenStackNetCreate().createOpenStackFloatingIp(jsonObject);
                        break;
                    case KEYPAIRS:
                        openStackClient.getOpenStackNetCreate().createOpenStackKeypair(jsonObject);
                        break;
                    case SECURITYGROUPS:
                        openStackClient.getOpenStackNetCreate().createOpenStackSecurityGroup(jsonObject);
                        break;

                }

                try {
                    updateTab();

                }catch(Exception ex){
                    ex.printStackTrace();
                }

                jfM.dispose();
            }});

        jfM.add(jbP1);
        jfM.add(jp1);
        jfM.getRootPane().setDefaultButton(jbP1);

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
    public void generalTableFormUpdate(String title,Map<String,String> headers,String key, OpenStackNetworkElement osne){
        JFrame jfM = new JFrame(title);
        jfM.setLayout(null);

        JButton jbP1 = new JButton("Enter");

        JPanel jp1 = new JPanel(new GridLayout(headers.size()+1, 2, 15, 10));//filas, columnas, espacio entre filas, espacio entre columnas

        JLabel l6 = new JLabel("Properties", SwingConstants.LEFT);
        jp1.add(l6);
        JLabel label = new JLabel("", SwingConstants.LEFT);
        jp1.add(label);

        for(String header : headers.keySet()){
            JLabel jlabel = new JLabel(header, SwingConstants.LEFT);
            jp1.add(jlabel);
            switch (headers.get(header)){

                case "Boolean":
                    JCheckBox jCheckBox = new JCheckBox();
                    jp1.add(jCheckBox);
                    break;
                case "Select":
                    JComboBox jComboBox;
                    Object[] stockArr = new String[1];
                    stockArr[0] = "empty";
                    List<String> stockList = new ArrayList<>() ;
                    switch (header){
                        case "Tenant ID":
                            stockArr[0]=openStackClient.getProjectId();
                            break;
                        case "Network ID":
                            stockList = openStackClient.openStackNetworks.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                            stockArr = new String[stockList.size()];
                            stockArr = stockList.toArray(stockArr);
                            break;
                        case "Subnet ID":
                            stockList = openStackClient.openStackSubnets.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                            stockArr = new String[stockList.size()];
                            stockArr = stockList.toArray(stockArr);
                            break;
                        case "Flavor ID":
                            stockList = openStackClient.openStackFlavors.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                            stockArr = new String[stockList.size()];
                            stockArr = stockList.toArray(stockArr);
                            break;
                        case "Image ID":
                            stockList = openStackClient.openStackImages.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                            stockArr = new String[stockList.size()];
                            stockArr = stockList.toArray(stockArr);
                            break;
                        case "Port ID":
                            stockList = openStackClient.openStackPorts.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                            stockArr = new String[stockList.size()];
                            stockArr = stockList.toArray(stockArr);
                            break;
                        case "Router ID":
                            stockList = openStackClient.openStackRouters.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                            stockArr = new String[stockList.size()];
                            stockArr = stockList.toArray(stockArr);
                            break;
                        case "IP version":
                            stockArr = IPVersionType.values();
                            break;
                        case "Network type":
                            stockArr = NetworkType.values();
                            break;
                        case "Service type":
                            stockArr = ServiceType.values();
                            break;
                        case "Facing":
                            stockArr = Facing.values();
                            break;
                    }

                    jComboBox = new JComboBox(stockArr);
                    jp1.add(jComboBox);
                    break;
                case "Special-ipv4masc":
                    try {
                        MaskFormatter mf = new MaskFormatter("###-###-###-###/##");
                        JFormattedTextField f = new JFormattedTextField(mf);
                        jp1.add(f);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "Special-ipv4":
                    try {
                        MaskFormatter mf = new MaskFormatter("###-###-###-###");
                        JFormattedTextField f = new JFormattedTextField(mf);
                        jp1.add(f);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                    break;
                default:
                    JTextField jtextField = new JTextField();
                    jp1.add(jtextField);
                    break;
            }

        }


        jp1.setVisible(true);
        jp1.setBounds(10, 10, 200, 50*headers.size());
        jbP1.setBounds(75, 60*headers.size(), 90, 25);

        jbP1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Component [] components = jp1.getComponents();
                JSONObject jsonObject = new JSONObject();

                for(int i = 3;i< components.length;i=i+2){

                    switch (headers.get(((JLabel)components[i-1]).getText())){
                        case "Select":
                            jsonObject.put( ((JLabel)components[i-1]).getText(),((JComboBox)components[i]).getSelectedItem().toString());
                            break;
                        case "Boolean":
                            jsonObject.put( ((JLabel)components[i-1]).getText(),((JCheckBox)components[i]).isSelected());
                            break;
                        case "IP version":
                            jsonObject.put( ((JLabel)components[i-1]).getText(),((JComboBox)components[i]).getSelectedItem());
                            break;
                        default:
                            jsonObject.put( ((JLabel)components[i-1]).getText(),((JTextField)components[i]).getText());
                            break;
                    }
                }

                switch (ajtType){

                    /*NETWORK*/
                    case PORTS:
                        switch(key){
                            case "Name":
                                ((OpenStackPort)osne).setName(((JTextField)components[1]).getText());
                                break;

                        }
                        break;
                    case NETWORKS:
                        switch(key){
                            case "Name":
                                ((OpenStackNetwork)osne).setName(jsonObject);
                                break;

                        }
                        break;
                    case ROUTERS:
                        switch(key){
                            case "Name":
                                ((OpenStackRouter)osne).setName(jsonObject);
                                break;
                            case "External Gateway":
                                ((OpenStackRouter)osne).setRouterExternalGatewayInfo(jsonObject);
                                break;
                            case "Tenant ID":
                                ((OpenStackRouter)osne).setRouterTenantId(jsonObject);
                                break;
                            case "Route":
                                ((OpenStackRouter)osne).addRoute(jsonObject);
                                break;


                        }
                        break;
                    case SUBNETS:
                        switch(key){
                            case "Name":
                                ((OpenStackSubnet)osne).setName(jsonObject);
                                break;
                            case "DNS":
                                ((OpenStackSubnet)osne).addSubnetDns(jsonObject.put("DNS",prepareIp(jsonObject.getString("DNS"))));
                                break;
                            case "Pool":
                                ((OpenStackSubnet)osne).addPool(jsonObject);
                                break;
                            case "CIDR":
                                ((OpenStackSubnet)osne).changeSubnetCidr(jsonObject);
                                break;
                            case "Route":
                                ((OpenStackSubnet)osne).addRoute(jsonObject);
                                break;
                            case "No gateway":
                                ((OpenStackSubnet)osne).subnetNoGateway();
                                break;
                            case "Gateway":
                                ((OpenStackSubnet)osne).setSubnetGateway(jsonObject);
                                break;
                            case "Network ID":
                                ((OpenStackSubnet)osne).setSubnetNetworkId(jsonObject);
                                break;
                            case "Project ID":
                                ((OpenStackSubnet)osne).setSubnetTenantId(jsonObject);
                                break;
                            case "IP version":
                                ((OpenStackSubnet)osne).setSubnetIpVersion(jsonObject);
                                break;
                        }
                        break;
                }

                try {
                    updateTab();
                }catch(Exception ex){
                    ex.printStackTrace();
                }

                jfM.dispose();
            }});

        jfM.add(jbP1);
        jfM.add(jp1);
        jfM.getRootPane().setDefaultButton(jbP1);

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
        openStackClient.clearList();
        openStackClient.fillList();

        final VisualizationState vs = callback.getVisualizationState();
        Pair<BidiMap<NetworkLayer, Integer>, Map<NetworkLayer, Boolean>> res =
                vs.suggestCanvasUpdatedVisualizationLayerInfoForNewDesign(new HashSet<>(callback.getDesign().getNetworkLayers()));
        vs.setCanvasLayerVisibilityAndOrder(callback.getDesign(), res.getFirst(), res.getSecond());
        callback.updateVisualizationAfterNewTopology();
        callback.addNetPlanChange();

    }

    @Override
    public void generalTableUpdate(String key, OpenStackNetworkElement osne,String type){
        JFrame jfM = new JFrame(key);
        jfM.setLayout(null);

        JButton jbP1 = new JButton("Enter");

        JPanel jp1 = new JPanel(new GridLayout(6, 2, 30, 10));//filas, columnas, espacio entre filas, espacio entre columnas
        JLabel l6 = new JLabel(key, SwingConstants.LEFT);
        jp1.add(l6);

        switch(type){
            case "Select":
                JComboBox jComboBox;
                Object[] stockArr = new String[1];
                stockArr[0] = "empty";
                List<String> stockList = new ArrayList<>() ;
                switch (key){
                    case "Network ID":
                        stockList = openStackClient.openStackNetworks.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                        stockArr = new String[stockList.size()];
                        stockArr = stockList.toArray(stockArr);
                        break;
                    case "Subnet ID":
                        stockList = openStackClient.openStackSubnets.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                        stockArr = new String[stockList.size()];
                        stockArr = stockList.toArray(stockArr);
                        break;
                    case "Flavor ID":
                        stockList = openStackClient.openStackFlavors.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                        stockArr = new String[stockList.size()];
                        stockArr = stockList.toArray(stockArr);
                        break;
                    case "Image ID":
                        stockList = openStackClient.openStackImages.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                        stockArr = new String[stockList.size()];
                        stockArr = stockList.toArray(stockArr);
                        break;
                    case "Port ID":
                        stockList = openStackClient.openStackPorts.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                        stockArr = new String[stockList.size()];
                        stockArr = stockList.toArray(stockArr);
                        break;
                    case "Router ID":
                        stockList = openStackClient.openStackRouters.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                        stockArr = new String[stockList.size()];
                        stockArr = stockList.toArray(stockArr);
                        break;
                    case "IP version":
                        stockArr = IPVersionType.values();
                        break;
                    case "Network type":
                        stockArr = NetworkType.values();
                        break;
                    case "Service type":
                        stockArr = ServiceType.values();
                        break;
                    case "Facing":
                        stockArr = Facing.values();
                        break;
                }

                jComboBox = new JComboBox(stockArr);
                jp1.add(jComboBox);
                break;
                default:
                    JTextField os_object = new JTextField();
                    jp1.add(os_object);

                    break;
        }



        jp1.setVisible(true);
        jp1.setBounds(10, 10, 200, 200);
        jbP1.setBounds(75, 90, 90, 25);

        jbP1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                Component [] components = jp1.getComponents();
                switch(ajtType){
                    /*NETWORK*/
                    case PORTS:
                        switch(key){
                            case "Name":
                                ((OpenStackPort)osne).setName(((JTextField)components[1]).getText());
                                break;

                        }
                        break;
                    case NETWORKS:
                        switch(key){
                            case "Name":
                                ((OpenStackNetwork)osne).setName(null);
                                break;

                        }
                        break;
                    case ROUTERS:
                        switch(key){
                            case "Name":
                                ((OpenStackRouter)osne).setName(null);
                                break;

                        }
                        break;
                    case SUBNETS:
                        switch(key){
                            case "Name":
                                ((OpenStackSubnet)osne).setName(null);
                                break;

                        }
                        break;
                }
                updateTab();
                jfM.dispose();
            }});

        jfM.add(jbP1);
        jfM.add(jp1);
        jfM.getRootPane().setDefaultButton(jbP1);
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

    public String prepareIp(String ip){
        String[] parts = ip.split("-");
        String part1 = String.valueOf(Integer.parseInt(parts[0])); // ###
        String part2 = String.valueOf(Integer.parseInt(parts[1])); //###
        String part3 = String.valueOf(Integer.parseInt(parts[2])); // ###
        String part4 =  String.valueOf(Integer.parseInt(parts[3])); // ###
        String response = part1+"."+part2+"."+part3+"."+part4;

        return response;
    }
}
