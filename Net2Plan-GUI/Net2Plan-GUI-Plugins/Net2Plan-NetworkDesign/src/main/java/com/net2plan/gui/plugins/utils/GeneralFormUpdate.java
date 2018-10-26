package com.net2plan.gui.plugins.utils;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackNetwork;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackPort;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackRouter;
import com.net2plan.gui.plugins.networkDesign.openStack.network.OpenStackSubnet;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import org.json.JSONObject;
import org.openstack4j.api.types.Facing;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.NetworkType;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeneralFormUpdate extends JFrame implements ActionListener{

    private String key;
    private OpenStackNetworkElement openStackNetworkElement;
    private String title;
    private ViewEditTopologyTablesPane.AJTableType ajTableType;
    private OpenStackClient openStackClient;
    private JButton enterButton;
    private JPanel propertiesPanel;
    private JLabel propertiesLabel;
    private Map<String,String> headers;
    AdvancedJTable_networkElement advancedJTable_networkElement;

    public GeneralFormUpdate(String title, Map<String,String> headers, String key, OpenStackNetworkElement osne, ViewEditTopologyTablesPane.AJTableType ajTableType, OpenStackClient openStackClient, AdvancedJTable_networkElement advancedJTable_networkElement){

        this.title = title;
        this.headers = headers;
        this.key=key;
        this.openStackNetworkElement = osne;
        this.ajTableType = ajTableType;
        this.openStackClient = openStackClient;
        this.advancedJTable_networkElement=advancedJTable_networkElement;


        init();
        recomput();
    }
    public void init(){

        setTitle(title);
        setLayout(null);
        setResizable(false);
        setVisible(true);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(250, 160);
        setLocation(getWidth()/2-getSize().width/2, getHeight()/2-getSize().height/2);
        ImageIcon img = new ImageIcon(getClass().getResource("/resources/common/openstack_logo.png"));
        setIconImage(img.getImage());
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);

        propertiesPanel = new JPanel(new GridLayout(6, 2, 30, 10));//filas, columnas, espacio entre filas, espacio entre columnas
        propertiesPanel.setVisible(true);
        propertiesPanel.setBounds(10, 10, 200, 200);


        enterButton = new JButton("Enter");
        enterButton.setBounds(75, 90, 90, 25);
        enterButton.addActionListener(this);

        add(enterButton);
        add(propertiesPanel);
        getRootPane().setDefaultButton(enterButton);




    }
    public void recomput(){
        for(String header : headers.keySet()){
            JLabel jlabel = new JLabel(header, SwingConstants.LEFT);
            propertiesPanel.add(jlabel);
            switch (headers.get(header)){

                case "Boolean":
                    JCheckBox jCheckBox = new JCheckBox();
                    propertiesPanel.add(jCheckBox);
                    break;
                case "Select":
                    JComboBox jComboBox;
                    jComboBox = new JComboBox(getSelectItems(key,header));
                    propertiesPanel.add(jComboBox);
                    break;

                case "ip":
                    JTextField jtextField2 = new JTextField();
                    jtextField2.setToolTipText("8.8.8.8");
                    propertiesPanel.add(jtextField2);
                    break;
                default:
                    JTextField jtextField = new JTextField();
                    propertiesPanel.add(jtextField);
                    break;
            }

        }


    }

    public Object [] getSelectItems(String key,String header){
        JComboBox jComboBox;
        Object[] stockArr = new String[1];
        stockArr[0] = "empty";
        List<String> stockList = new ArrayList<>() ;
        switch (header){
            case "Tenant ID":
                stockList = openStackClient.openStackProjects.stream().map(n -> (String)n.getId()).collect(Collectors.toList());
                stockArr = new String[stockList.size()];
                stockArr = stockList.toArray(stockArr);
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

        return  stockArr;
    }
    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        Component [] components = propertiesPanel.getComponents();
        JSONObject jsonObject = new JSONObject();
        System.out.println("JSOOOOOOOOONDAIE "+components.length);
            System.out.println("JSOOOOOOOOOOOOOOOOOOOOOOOOOON" + ((JLabel)components[0]).getText());
            switch (headers.get(((JLabel)components[0]).getText())){
                case "Select":
                    jsonObject.put( ((JLabel)components[0]).getText(),((JComboBox)components[1]).getSelectedItem().toString());
                    break;
                case "Boolean":
                    jsonObject.put( ((JLabel)components[0]).getText(),((JCheckBox)components[1]).isSelected());
                    break;
                case "IP version":
                    jsonObject.put( ((JLabel)components[0]).getText(),((JComboBox)components[1]).getSelectedItem());
                    break;
                default:
                    jsonObject.put( ((JLabel)components[0]).getText(),((JTextField)components[1]).getText());
                    break;
            }


        openStackClient.updateClient();
        switch (ajTableType){

            /*NETWORK*/
            case PORTS:
                switch(key){
                    case "Name":
                        ((OpenStackPort)openStackNetworkElement).setName(((JTextField)components[1]).getText());
                        break;

                }
                break;
            case NETWORKS:
                switch(key){
                    case "Name":
                        ((OpenStackNetwork)openStackNetworkElement).setName(jsonObject);
                        break;

                }
                break;
            case ROUTERS:
                switch(key){
                    case "Name":
                        ((OpenStackRouter)openStackNetworkElement).setName(jsonObject);
                        break;
                    case "External Gateway":
                        ((OpenStackRouter)openStackNetworkElement).setRouterExternalGatewayInfo(jsonObject);
                        break;
                    case "Tenant ID":
                        ((OpenStackRouter)openStackNetworkElement).setRouterTenantId(jsonObject);
                        break;
                    case "Route":
                        ((OpenStackRouter)openStackNetworkElement).addRoute(jsonObject);
                        break;


                }
                break;
            case SUBNETS:
                switch(key){
                    case "Name":
                        ((OpenStackSubnet)openStackNetworkElement).setName(jsonObject);
                        break;
                    case "DNS":
                        ((OpenStackSubnet)openStackNetworkElement).addSubnetDns(jsonObject.put("DNS",jsonObject.getString("DNS")));
                        break;
                    case "Pool":
                        ((OpenStackSubnet)openStackNetworkElement).addPool(jsonObject);
                        break;
                    case "CIDR":
                        ((OpenStackSubnet)openStackNetworkElement).changeSubnetCidr(jsonObject);
                        break;
                    case "Route":
                        ((OpenStackSubnet)openStackNetworkElement).addRoute(jsonObject);
                        break;
                    case "No gateway":
                        ((OpenStackSubnet)openStackNetworkElement).subnetNoGateway();
                        break;
                    case "Gateway":
                        ((OpenStackSubnet)openStackNetworkElement).setSubnetGateway(jsonObject);
                        break;
                    case "Network ID":
                        ((OpenStackSubnet)openStackNetworkElement).setSubnetNetworkId(jsonObject);
                        break;
                    case "Project ID":
                        ((OpenStackSubnet)openStackNetworkElement).setSubnetTenantId(jsonObject);
                        break;
                    case "IP version":
                        ((OpenStackSubnet)openStackNetworkElement).setSubnetIpVersion(jsonObject);
                        break;
                }
                break;
        }
        advancedJTable_networkElement.updateTab();
        dispose();
    }


}
