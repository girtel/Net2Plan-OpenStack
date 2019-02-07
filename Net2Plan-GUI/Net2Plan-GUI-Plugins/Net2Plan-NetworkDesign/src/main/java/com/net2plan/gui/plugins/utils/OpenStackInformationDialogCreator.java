package com.net2plan.gui.plugins.utils;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.topologyPane.TopologyPanel;
import net.miginfocom.swing.MigLayout;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OpenStackInformationDialogCreator {
    public final OpenStackFileChooser fc_netPlan = new OpenStackFileChooser();
    public  String name="",url="",project="",domain="",password="";
    JDialog jDialog;
    JButton enterButton,loadButton,clearButton;
    final JPanel projectsPanel;
    final JPanel firstPanel;
    final JPanel iconsPanel;

    GUINetworkDesign callback;
    JComponent os_username, os_auth_url,os_project_id,os_user_domain_name;
    JPasswordField os_password;
    JLabel labelUser,labelPassword,labelUrl,labelProject,labelUDomain;

    String state  ="init";


    public OpenStackInformationDialogCreator(GUINetworkDesign callback,JButton buttonPositionReference){

        this.callback = callback;
        jDialog = new JDialog();

        Point point = buttonPositionReference.getLocationOnScreen();
        jDialog.setLocation(point.x + buttonPositionReference.getWidth(),point.y);
        jDialog.setUndecorated(true);
        jDialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));

        jDialog.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent windowEvent) {

            }

            @Override
            public void windowLostFocus(WindowEvent windowEvent) {
                jDialog.dispose();
            }
        });


        iconsPanel = new JPanel(new MigLayout("fillx, wrap 2"));
        projectsPanel = new JPanel(new MigLayout("fillx, wrap 2"));

        recomput();

        firstPanel = new JPanel(new BorderLayout());
        firstPanel.add(iconsPanel, BorderLayout.NORTH);
        firstPanel.add(projectsPanel, BorderLayout.CENTER);

        jDialog.add(firstPanel);
        jDialog.pack();

        jDialog.setResizable(false);
        jDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    public void setVisible(){
        jDialog.pack();
        jDialog.setVisible(true);
    }
    public void recomput(){

        iconsPanel.removeAll();
        projectsPanel.removeAll();


        callback.getOpenStackNet().getOsClients().stream().filter(n-> n.getNetPlanDesign().equals(callback.getDesign())).collect(Collectors.toList());

        if(callback.getOpenStackNet().getOsClients().stream().filter(n-> n.getNetPlanDesign().equals(callback.getDesign())).collect(Collectors.toList()).size()>0) {
            System.out.println("in panel projects");
            callback.getOpenStackNet().getOsClients().stream().filter(n -> n.getNetPlanDesign().equals(callback.getDesign())).collect(Collectors.toList()).get(0).openStackProjects.forEach(n -> {
                JLabel jLabel = new JLabel(n.getProjectName());
                JLabel jLabelColor = new JLabel("     ");
                System.out.println(n.getColor());
                jLabelColor.setBackground(n.getColor());
                jLabelColor.setOpaque(true);
                projectsPanel.add(jLabel, "align label");
                projectsPanel.add(jLabelColor, "growx");
            });
        }
        JLabel jLabelRouter = new JLabel("Router");
        JLabel jLabelNetwork = new JLabel("Network");
        JLabel jLabelExternalNetwork = new JLabel("External Network");
        JLabel jLabelSubnet = new JLabel("Subnet");
        JLabel jLabelExternalSubnet = new JLabel("External Subnet");
        JLabel jLabelInstance = new JLabel("Instance");


        /*JLabel jLabelIcon = new JLabel("     ");
        jLabelIcon.setOpaque(true);
        try {
            URL url = new URL("https://cdn0.iconfinder.com/data/icons/network-database-1/65/34-512.png");
            jLabelIcon.setIcon(new ImageIcon(ImageIO.read(url)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        jLabelIcon.setSize(20,20);
        iconsPanel.add(jLabelRouter, "align label");
        iconsPanel.add(jLabelIcon, "growx");
        JLabel jLabelIcon2 = new JLabel("     ");
        jLabelIcon2.setOpaque(true);
        jLabelIcon2.setIcon(new ImageIcon(TopologyPanel.class.getResource("/resources/gui/redoButton.png")));
        iconsPanel.add(jLabelNetwork,  "align label");
        iconsPanel.add(jLabelIcon2, "growx");
        JLabel jLabelIcon3 = new JLabel("     ");
        jLabelIcon3.setOpaque(true);
        jLabelIcon3.setIcon(new ImageIcon(TopologyPanel.class.getResource("/resources/gui/redoButton.png")));
        iconsPanel.add(jLabelExternalNetwork,  "align label");
        iconsPanel.add(jLabelIcon3, "growx");
        JLabel jLabelIcon4 = new JLabel("     ");
        jLabelIcon4.setOpaque(true);
        jLabelIcon4.setIcon(new ImageIcon(TopologyPanel.class.getResource("/resources/gui/redoButton.png")));
        iconsPanel.add(jLabelSubnet,  "align label");
        iconsPanel.add(jLabelIcon4, "growx");
        JLabel jLabelIcon5 = new JLabel("     ");
        jLabelIcon5.setOpaque(true);
        jLabelIcon5.setIcon(new ImageIcon(TopologyPanel.class.getResource("/resources/gui/redoButton.png")));
        iconsPanel.add(jLabelExternalSubnet,  "align label");
        iconsPanel.add(jLabelIcon5, "growx");
        JLabel jLabelIcon6 = new JLabel("     ");
        jLabelIcon6.setOpaque(true);
        jLabelIcon6.setIcon(new ImageIcon(TopologyPanel.class.getResource("/resources/gui/redoButton.png")));
        iconsPanel.add(jLabelInstance,  "align label");
        iconsPanel.add(jLabelIcon6, "growx");*/
    }




}
