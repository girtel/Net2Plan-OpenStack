package com.net2plan.gui.plugins.utils;

import com.net2plan.gui.plugins.GUINetworkDesign;
import net.miginfocom.swing.MigLayout;
import org.apache.commons.io.FileUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenStackInitalButtonFunctionalities {

    public static final CipherClass cc = new CipherClass();
    public static final OpenStackFileChooser fc_netPlan = new OpenStackFileChooser();
    public static String name="",url="",project="",domain="",password="";


    public static void generatedLoginUserFile(GUINetworkDesign callback){
        assert fc_netPlan != null;

        int rc = fc_netPlan.showOpenDialog(null);
        if (rc != JFileChooser.APPROVE_OPTION) return;

        try  {

            FileUtils.writeByteArrayToFile(new File(fc_netPlan.getSelectedFile().getAbsolutePath()+".n2p"), cc.cifra(callback.getOpenStackNet().getLoginInformationOfNet().toString()));

        }catch(Exception ex){
            System.out.println(ex.toString());

        }
    }

    public static void loadLoginUserFile(GUINetworkDesign callback){
        assert fc_netPlan != null;

        int rc = fc_netPlan.showOpenDialog(null);
        if (rc != JFileChooser.APPROVE_OPTION) return;


        try{
            byte [] bytes = Files.readAllBytes(fc_netPlan.getSelectedFile().toPath());
            String everything = cc.descifra(bytes);
            JSONObject jsonObject = new JSONObject(everything);
            //System.out.println("TopologyPanel loading  OSClientV3s " + jsonObject);
            callback.getOpenStackNet().addNewLoginInformationToNet(jsonObject);

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public static void addLoginUserInformationDialog(GUINetworkDesign callback,JButton button){

        JDialog jDialog = new JDialog();
        jDialog.setBackground(Color.black);

        Point point = button.getLocationOnScreen();
        jDialog.setLocation(point.x,point.y + button.getHeight() + 7);
        //jDialog.setLocationRelativeTo(button);
        jDialog.setUndecorated(true);
        jDialog.getRootPane().setBorder(BorderFactory.createLineBorder(Color.BLACK));
        jDialog.addWindowFocusListener(new WindowFocusListener() {
            @Override
            public void windowGainedFocus(WindowEvent windowEvent) {

            }

            @Override
            public void windowLostFocus(WindowEvent windowEvent) {
                jDialog.setVisible(false);
            }
        });

        JPanel jp1;
        JButton jbP1,jbP2;
        JTextField os_username, os_auth_url,os_project_id,os_user_domain_name;
        JPasswordField os_password;
        JLabel labelUser,labelPassword,labelUrl,labelProject,labelUDomain;


//        jp1 = new JPanel(new GridLayout(5, 2, 30, 10));//filas, columnas, espacio entre filas, espacio entre columnas

        jp1 = new JPanel(new MigLayout("fillx, wrap 2"));

        labelUser = new JLabel("OS_USERNAME",  SwingConstants.LEFT);
        labelPassword = new JLabel("OS_PASSWORD",  SwingConstants.LEFT);
        labelUrl = new JLabel("OS_AUTH_URL",  SwingConstants.LEFT);
        labelProject = new JLabel("OS_PROJECT_NAME",  SwingConstants.LEFT);
        labelUDomain = new JLabel("OS_U_DOMAIN_NAME", SwingConstants.LEFT);


        if(name.length()==0) {
            os_username = new JTextField(20);
            os_password = new JPasswordField(20);
            os_auth_url = new JTextField(20);
            os_project_id = new JTextField(20);
            os_user_domain_name = new JTextField(20);
        }else{
            os_username = new JTextField(name);
            os_password = new JPasswordField(password);
            os_auth_url = new JTextField(url);
            os_project_id = new JTextField(project);
            os_user_domain_name = new JTextField(domain);
        }

        jp1.add(labelUser, "align label");
        jp1.add(os_username, "growx");

        jp1.add(labelPassword,  "align label");
        jp1.add(os_password, "growx");

        jp1.add(labelUrl,  "align label");
        jp1.add(os_auth_url, "growx");

        jp1.add(labelProject,  "align label");
        jp1.add(os_project_id, "growx");

        jp1.add(labelUDomain,  "align label");
        jp1.add(os_user_domain_name, "growx");

        jbP1 = new JButton("Enter");
        jbP2 = new JButton("Load RC file");

        jbP1.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e)
            {

                setLastValues(os_username.getText(),os_auth_url.getText(),String.valueOf(os_password.getPassword()),os_project_id.getText(),os_user_domain_name.getText());
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("os_auth_url",os_auth_url.getText());
                jsonObject.put("os_username",os_username.getText());
                jsonObject.put("os_password",String.valueOf(os_password.getPassword()));
                jsonObject.put("os_project_id",os_project_id.getText());
                jsonObject.put("os_user_domain_name",os_user_domain_name.getText());

                JSONArray jsonArray = new JSONArray();
                jsonArray.put(jsonObject);
                JSONObject jsonObject1 = new JSONObject();
                jsonObject1.put("information",jsonArray);

                System.out.println("TopologyPanel adding new OSClientV3 " + jsonObject1);
                callback.getOpenStackNet().addNewLoginInformationToNet(jsonObject1);

                //callback.getAboutIt().updateText();
                jDialog.dispose();
            }
        });

        jbP2.addActionListener(new ActionListener() {
            public void actionPerformed (ActionEvent e)
            {

                assert fc_netPlan != null;

                // fc_netPlan.setFileFilter(new FileNameExtensionFilter("All files","*"));

                JFileChooser jFileChooser = new OpenStackFileChooser();
                jFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);

                int rc = jFileChooser.showOpenDialog(null);
                if (rc != JFileChooser.APPROVE_OPTION) return;

                Pattern r_url = Pattern.compile("(export OS_AUTH_URL=)(.*)");
                Pattern r_project_id = Pattern.compile("(export OS_PROJECT_ID=)(.*)");
                Pattern r_project_domain = Pattern.compile("(export OS_USER_DOMAIN_NAME=)(.*)");
                Pattern r_user = Pattern.compile("(export OS_USERNAME=)(.*)");
                ArrayList<Pattern> patterns = new ArrayList<>();
                patterns.add(r_url);
                patterns.add(r_project_id);
                patterns.add(r_project_domain);
                patterns.add(r_user);
                JSONObject jsonObject = new JSONObject();
                try (BufferedReader br = new BufferedReader(new FileReader(jFileChooser.getSelectedFile()))) {
                    String line;

                    while ((line = br.readLine()) != null) {
                           for (Pattern r: patterns) {
                              Matcher m = r.matcher(line);
                            if (m.find()) {
                                  jsonObject.put(m.group(1),m.group(2));
                            } else {

                            }
                        }

                    }
                    os_username.setText(jsonObject.get("export OS_USERNAME=").toString().replaceAll(Character.toString('"'),""));
                    os_auth_url.setText(jsonObject.get("export OS_AUTH_URL=").toString().replaceAll(Character.toString('"'),""));
                    os_project_id.setText(jsonObject.get("export OS_PROJECT_ID=").toString().replaceAll(Character.toString('"'),""));
                    os_user_domain_name.setText(jsonObject.get("export OS_USER_DOMAIN_NAME=").toString().replaceAll(Character.toString('"'),""));

                    setLastValues(os_username.getText(),os_auth_url.getText(),String.valueOf(os_password.getPassword()),os_project_id.getText(),os_user_domain_name.getText());

                    os_username.setEnabled(false);
                    os_auth_url.setEnabled(false);
                    os_project_id.setEnabled(false);
                    os_user_domain_name.setEnabled(false);

                    jDialog.pack();
                    jDialog.setVisible(true);
                }catch(Exception ex){
                    ex.printStackTrace();
                    jDialog.dispose();
                }
            }
        });

        final JPanel buttonsPanel = new JPanel();
        buttonsPanel.add(jbP1);
        buttonsPanel.add(jbP2);

        final JPanel aux = new JPanel(new BorderLayout());
        aux.add(jp1, BorderLayout.CENTER);
        aux.add(buttonsPanel, BorderLayout.SOUTH);

        jDialog.add(aux);
        jDialog.pack();

        jDialog.setResizable(false);
        jDialog.setVisible(true);
        jDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    }

    public static void setLastValues(String nombre,String urls, String contrasenia, String proyecto, String dominio){

           url = urls;
           name = nombre;
           password = contrasenia;
           project = proyecto;
           domain = dominio;

   }
}
