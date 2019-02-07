package com.net2plan.gui.plugins.utils;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackQuotas;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackSecurityGroup;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackServer;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import net.miginfocom.swing.MigLayout;
import org.json.JSONObject;
import org.openstack4j.api.Builders;
import org.openstack4j.api.types.Facing;
import org.openstack4j.api.types.ServiceType;
import org.openstack4j.model.compute.HostResource;
import org.openstack4j.model.compute.IPProtocol;
import org.openstack4j.model.network.IPVersionType;
import org.openstack4j.model.network.NetworkType;
import org.openstack4j.model.network.Router;

import javax.swing.*;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GeneralForm extends JDialog implements ActionListener{

    JButton enterButton;
    JPanel propertiesPanel,buttonPanel,firstPanel;
    JLabel propertiesLabel;
    String title;
    Map<String,String> headers;
    ViewEditTopologyTablesPane.AJTableType ajTableType;
    OpenStackClient openStackClient;
    AdvancedJTable_networkElement advancedJTable_networkElement;
    OpenStackNetworkElement openStackNetworkElement;

    public GeneralForm(String title, Map<String,String> headers, ViewEditTopologyTablesPane.AJTableType ajTableType, OpenStackClient openStackClient, AdvancedJTable_networkElement advancedJTable_networkElement,OpenStackNetworkElement openStackNetworkElement){

        this.title=title;
        this.headers=headers;
        this.ajTableType= ajTableType;
        this.openStackClient=openStackClient;
        this.advancedJTable_networkElement=advancedJTable_networkElement;
        this.openStackNetworkElement=openStackNetworkElement;
        openStackClient.getClient();
        init();



    }

    public void init(){

        setTitle(title);
        addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent windowEvent) {

            }

            @Override
            public void windowLostFocus(WindowEvent windowEvent) {
                dispose();
            }
        });
        enterButton = new JButton("Enter");

        propertiesPanel = new JPanel(new MigLayout("fillx, wrap 2"));//filas, columnas, espacio entre filas, espacio entre columnas
        buttonPanel = new JPanel();//filas, columnas, espacio entre filas, espacio entre columnas
        firstPanel = new JPanel(new BorderLayout());//filas, columnas, espacio entre filas, espacio entre columnas

        propertiesLabel = new JLabel("Properties");
        propertiesPanel.add(propertiesLabel,"align label");

        JLabel label = new JLabel("", SwingConstants.LEFT);
        propertiesPanel.add(label,"growx");

        recomputFields();

        enterButton.addActionListener(this);

        buttonPanel.add(enterButton);
        firstPanel.add(buttonPanel,BorderLayout.SOUTH);
        firstPanel.add(propertiesPanel,BorderLayout.NORTH);
        add(firstPanel);
        getRootPane().setDefaultButton(enterButton);

        ImageIcon img = new ImageIcon(getClass().getResource("/resources/common/openstack_logo.png"));
        setIconImage(img.getImage());


        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(dim.width/2-getSize().width/2, dim.height/2-getSize().height/2);
        setVisible(true);
        pack();
    }
    public void recomputFields(){

        for(String key : headers.keySet()){
            JLabel jlabel = new JLabel(key, SwingConstants.LEFT);
            propertiesPanel.add(jlabel,"align label");
            switch (headers.get(key)){
                case "Boolean":
                    JCheckBox jCheckBox = new JCheckBox();
                    propertiesPanel.add(jCheckBox,"growx");
                    break;
                case "Select":
                    JComboBox jComboBox;
                    jComboBox = new JComboBox(getSelectItems(key));
                    propertiesPanel.add(jComboBox,"growx");
                    break;
                case "Cidr":
                    JTextField jtextField = new JTextField();
                    jtextField.setToolTipText("192.168.0.0/24");
                    propertiesPanel.add(jtextField,"growx");
                    break;
                default:
                    JTextField textField = new JTextField(headers.get(key));
                    propertiesPanel.add(textField,"growx");
                    break;
            }
        }


    }
    public Object [] getSelectItems(String key){
        Object[] stockArr = new String[1];
        stockArr[0] = "empty";
        List<String> stockList = new ArrayList<>() ;
        switch (key){
            case "Domain ID":
                stockList = openStackClient.openStackDomains.stream().map(n -> (String)n.getDomainName()).collect(Collectors.toList());
                stockArr = new String[stockList.size()];
                stockArr = stockList.toArray(stockArr);
                break;
            case "Tenant ID":
                stockList = openStackClient.openStackProjects.stream().map(n -> (String)n.getProjectName()).collect(Collectors.toList());
                stockArr = new String[stockList.size()];
                stockArr = stockList.toArray(stockArr);
                break;
            case "Network ID":
                stockList = openStackClient.openStackNetworks.stream().map(n -> (String)n.getName()).collect(Collectors.toList());
                stockArr = new String[stockList.size()];
                stockArr = stockList.toArray(stockArr);
                break;
            case "Subnet ID":
                stockList = openStackClient.openStackSubnets.stream().map(n -> (String)n.getName()).collect(Collectors.toList());
                stockArr = new String[stockList.size()];
                stockArr = stockList.toArray(stockArr);
                break;
            case "Flavor ID":
                stockList = openStackClient.openStackFlavors.stream().map(n -> (String)n.getFlavorName()).collect(Collectors.toList());
                stockArr = new String[stockList.size()];
                stockArr = stockList.toArray(stockArr);
                break;
            case "Image ID":
                stockList = openStackClient.openStackImages.stream().map(n -> (String)n.getName()).collect(Collectors.toList());
                stockArr = new String[stockList.size()];
                stockArr = stockList.toArray(stockArr);
                break;
            case "Port ID":
                stockList = openStackClient.openStackPorts.stream().map(n -> (String)n.getName()).collect(Collectors.toList());
                stockArr = new String[stockList.size()];
                stockArr = stockList.toArray(stockArr);
                break;
            case "Router ID":
                stockList = openStackClient.openStackRouters.stream().map(n -> (String)n.getRouterName()).collect(Collectors.toList());
                stockArr = new String[stockList.size()];
                stockArr = stockList.toArray(stockArr);
                break;
            case "Server ID":
                stockList = openStackClient.openStackServers.stream().map(n -> (String)n.getServerName()).collect(Collectors.toList());
                stockArr = new String[stockList.size()];
                stockArr = stockList.toArray(stockArr);
                break;
            case "Role ID":
                stockList = openStackClient.openStackRoles.stream().map(n -> (String)n.getRoleName()).collect(Collectors.toList());
                stockArr = new String[stockList.size()];
                stockArr = stockList.toArray(stockArr);
                break;
            case "IP version":
                stockArr = IPVersionType.values();
                break;
            case "IP Protocol":
                System.out.println("ippprotocol");
                stockArr = IPProtocol.values();
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
            case "Hostname":
                stockList = openStackClient.getClient().compute().host().list().stream().map(n->((HostResource) n).getHostName()).distinct().collect(Collectors.toList());
                stockArr = new String[stockList.size()];
                stockArr = stockList.toArray(stockArr);
                break;
            case "Migration Type":

                stockList.add("Normal");
                stockList.add("Block");
                stockList.add("Disk");
                stockArr = new  String[stockList.size()];
                stockArr = stockList.toArray(stockArr);
                break;

            case "Security group ID":
                stockList = openStackClient.openStackSecurityGroups.stream().map(n -> (String)n.getName()).collect(Collectors.toList());
                stockArr = new String[stockList.size()];
                stockArr = stockList.toArray(stockArr);
                break;
        }
        return stockArr;
    }
    public void actionPerformed(ActionEvent e) {
        Component [] components = propertiesPanel.getComponents();
        JSONObject jsonObject = new JSONObject();

        System.out.println("aciton");
        for(int i = 3;i< components.length;i=i+2){
            System.out.println(headers.get(((JLabel)components[i-1]).getText()));
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
        switch (ajTableType){

            case USERS:
                openStackClient.getOpenStackNetCreate().createOpenStackNetworkElement(advancedJTable_networkElement.getAjType(),jsonObject);
                break;
            case PROJECTS:
                openStackClient.getOpenStackNetCreate().createOpenStackNetworkElement(advancedJTable_networkElement.getAjType(),jsonObject);
                break;

            /*NETWORK*/
            case PORTS:
                openStackClient.getOpenStackNetCreate().createOpenStackNetworkElement(advancedJTable_networkElement.getAjType(), jsonObject);

                break;
            case NETWORKS:
                openStackClient.getOpenStackNetCreate().createOpenStackNetworkElement(advancedJTable_networkElement.getAjType(),jsonObject);
                break;
            case SUBNETS:
                openStackClient.getOpenStackNetCreate().createOpenStackNetworkElement(advancedJTable_networkElement.getAjType(),jsonObject);
                break;
            case ROUTERS:
                openStackClient.getOpenStackNetCreate().createOpenStackNetworkElement(advancedJTable_networkElement.getAjType(),jsonObject);
                //callback.selectNetPlanViewItem(ajTableType);
                //if (router != null) advancedJTable_networkElement.pi
                break;

            /*COMPUTE*/
            case SERVERS:
                if(title.equals("Live migration")){
                    ((OpenStackServer)openStackNetworkElement).doLiveMigration(jsonObject);
                }else {
                    openStackClient.getOpenStackNetCreate().createOpenStackNetworkElement(advancedJTable_networkElement.getAjType(),jsonObject);
                }

                break;

            case FLOATINGIPS:
                openStackClient.getOpenStackNetCreate().createOpenStackNetworkElement(advancedJTable_networkElement.getAjType(),jsonObject);
                break;

            case LIMITS:
                String noAdminProjectId = openStackClient.openStackProjects.stream().filter(n->n.getProjectName().equals("admin")).findFirst().get().getId();
                openStackClient.getClient().compute().quotaSets()
                        .updateForTenant(noAdminProjectId, Builders.quotaSet()
                                .cores(Integer.valueOf(jsonObject.getString("Cores")))
                                .ram(Integer.valueOf(jsonObject.getString("Ram")))
                                .instances(Integer.valueOf(jsonObject.getString("Instances")))
                                .build());
                break;
            case QUOTAS:
                System.out.println(openStackClient.isThisClientAdmin());
                System.out.println(openStackClient.getName());
               openStackClient.getClient().compute().quotaSets()
                        .updateForTenant(((OpenStackQuotas)openStackNetworkElement).getOpenStackProject().getId(), Builders.quotaSet()
                                .cores(Integer.valueOf(jsonObject.getString("Cores")))
                                .ram(Integer.valueOf(jsonObject.getString("Ram")))
                                .instances(Integer.valueOf(jsonObject.getString("Instances")))
                                .build());
                break;
            case SECURITYGROUPS:
                System.out.println("adding");
                if(title.equals("Add rule")){
                    System.out.println("adding");
                    ((OpenStackSecurityGroup)openStackNetworkElement).addRule(jsonObject);
                }else {
                    openStackClient.getOpenStackNetCreate().createOpenStackNetworkElement(advancedJTable_networkElement.getAjType(),jsonObject);
                }


                    break;
            case RULES:

                openStackClient.openStackSecurityGroups.stream().filter(n->n.getName().equals(jsonObject.get("Security group ID"))).findFirst().get().addRule(jsonObject);

                    break;
        }

        //openStackClient.getOsn().getCallback().getViewEditTopTables().updateView();
        advancedJTable_networkElement.updateThisTab();
        dispose();

    }
}
