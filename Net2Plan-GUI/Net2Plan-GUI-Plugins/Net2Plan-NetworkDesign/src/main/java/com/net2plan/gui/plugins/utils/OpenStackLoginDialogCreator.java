package com.net2plan.gui.plugins.utils;

import com.net2plan.gui.plugins.GUINetworkDesign;
import net.miginfocom.swing.MigLayout;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OpenStackLoginDialogCreator implements ActionListener{

    public final OpenStackFileChooser fc_netPlan = new OpenStackFileChooser();
    public  String name="",url="",project="",domain="",password="";
    JDialog jDialog;
    JButton enterButton,loadButton,clearButton;
    final JPanel buttonsPanel;
    final JPanel firstPanel;
    final JPanel fieldPanel;

    GUINetworkDesign callback;
    JComponent os_username, os_auth_url,os_project_id,os_user_domain_name;
    JPasswordField os_password;
    JLabel labelUser,labelPassword,labelUrl,labelProject,labelUDomain;

    String state  ="init";


    public OpenStackLoginDialogCreator(GUINetworkDesign callback,JButton buttonPositionReference){

        this.callback = callback;
        jDialog = new JDialog();

        Point point = buttonPositionReference.getLocationOnScreen();
        jDialog.setLocation(point.x,point.y + buttonPositionReference.getHeight() + 7);
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


        fieldPanel = new JPanel(new MigLayout("fillx, wrap 2"));
        recomput();

        enterButton = new JButton("Enter");
        enterButton.addActionListener(this);

        loadButton = new JButton("Load RC file");
        loadButton.addActionListener(this);

        clearButton = new JButton("Clear");
        clearButton.addActionListener(this);

        buttonsPanel= new JPanel();
        buttonsPanel.add(enterButton);
        buttonsPanel.add(loadButton);
        buttonsPanel.add(clearButton);

        firstPanel = new JPanel(new BorderLayout());
        firstPanel.add(fieldPanel, BorderLayout.CENTER);
        firstPanel.add(buttonsPanel, BorderLayout.SOUTH);

        jDialog.add(firstPanel);
        jDialog.pack();

        jDialog.setResizable(false);
        jDialog.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

    }

    public void setVisible(){
        jDialog.setVisible(true);
    }
    public void recomput(){

        labelUser = new JLabel("OS_USERNAME",  SwingConstants.LEFT);
        labelPassword = new JLabel("OS_PASSWORD",  SwingConstants.LEFT);
        labelUrl = new JLabel("OS_AUTH_URL",  SwingConstants.LEFT);
        labelProject = new JLabel("OS_PROJECT_NAME",  SwingConstants.LEFT);
        labelUDomain = new JLabel("OS_U_DOMAIN_NAME", SwingConstants.LEFT);
        fieldPanel.removeAll();
         switch(state){
            case "init":
                os_username = new JTextField(20);
                os_password = new JPasswordField(20);
                os_auth_url = new JTextField(20);
                os_project_id = new JTextField(20);
                os_user_domain_name = new JTextField(20);
                break;
            case "normal":
                os_username = new JTextField(name);
                os_password = new JPasswordField(password);
                os_auth_url = new JTextField(url);
                os_project_id = new JTextField(project);
                os_user_domain_name = new JTextField(domain);
                break;
            case "file":
                os_username = new JLabel(name);
                os_password = new JPasswordField(password);
                os_auth_url = new JLabel(url);
                os_project_id = new JLabel(project);
                os_user_domain_name = new JLabel(domain);
                break;
        }

        fieldPanel.add(labelUser, "align label");
        fieldPanel.add(os_username, "growx");

        fieldPanel.add(labelPassword,  "align label");
        fieldPanel.add(os_password, "growx");

        fieldPanel.add(labelUrl,  "align label");
        fieldPanel.add(os_auth_url, "growx");

        fieldPanel.add(labelProject,  "align label");
        fieldPanel.add(os_project_id, "growx");

        fieldPanel.add(labelUDomain,  "align label");
        fieldPanel.add(os_user_domain_name, "growx");
    }


    public void setLastValues(String nombre,String urls, String contrasenia, String proyecto, String dominio){

        url = urls;
        name = nombre;
        password = contrasenia;
        project = proyecto;
        domain = dominio;

    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {


        Object src = actionEvent.getSource();

        if(src == enterButton){
            JSONObject jsonObject = new JSONObject();
            if (os_username instanceof JTextField) {
                setLastValues(((JTextField)os_username).getText(),((JTextField)os_auth_url).getText(),String.valueOf(os_password.getPassword()),((JTextField)os_project_id).getText(),((JTextField)os_user_domain_name).getText());

                jsonObject.put("os_auth_url",((JTextField)os_auth_url).getText());
                jsonObject.put("os_username",((JTextField)os_username).getText());
                jsonObject.put("os_password",String.valueOf(os_password.getPassword()));
                jsonObject.put("os_project_id",((JTextField)os_project_id).getText());
                jsonObject.put("os_user_domain_name",((JTextField)os_user_domain_name).getText());
            }else{
                setLastValues(((JLabel)os_username).getText(),((JLabel)os_auth_url).getText(),String.valueOf(os_password.getPassword()),((JLabel)os_project_id).getText(),((JLabel)os_user_domain_name).getText());

                jsonObject.put("os_auth_url",((JLabel)os_auth_url).getText());
                jsonObject.put("os_username",((JLabel)os_username).getText());
                jsonObject.put("os_password",String.valueOf(os_password.getPassword()));
                jsonObject.put("os_project_id",((JLabel)os_project_id).getText());
                jsonObject.put("os_user_domain_name",((JLabel)os_user_domain_name).getText());
            }


            state = "normal";
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(jsonObject);

            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("information",jsonArray);

            callback.getOpenStackNet().addNewLoginInformationToNet(jsonObject1);
            recomput();
            jDialog.pack();
            jDialog.dispose();

        }else if(src == loadButton){

            assert fc_netPlan != null;

            // fc_netPlan.setFileFilter(new FileNameExtensionFilter("All files","*"));

            JFileChooser jFileChooser = new OpenStackFileChooser();
            jFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);

            int rc = jFileChooser.showOpenDialog(null);
            if (rc != JFileChooser.APPROVE_OPTION) return;


            JSONObject jsonObject = new JSONObject();
            ArrayList<Pattern> patterns =getPatterns();

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
                if(os_username instanceof JTextField) {
                    ((JTextField) os_username).setText(jsonObject.get("export OS_USERNAME=").toString().replaceAll(Character.toString('"'), ""));
                    ((JTextField) os_auth_url).setText(jsonObject.get("export OS_AUTH_URL=").toString().replaceAll(Character.toString('"'), ""));
                    ((JTextField) os_project_id).setText(jsonObject.get("export OS_PROJECT_ID=").toString().replaceAll(Character.toString('"'), ""));
                    ((JTextField) os_user_domain_name).setText(jsonObject.get("export OS_USER_DOMAIN_NAME=").toString().replaceAll(Character.toString('"'), ""));
                }else{
                    ((JTextField) os_username).setText(jsonObject.get("export OS_USERNAME=").toString().replaceAll(Character.toString('"'), ""));
                    ((JTextField) os_auth_url).setText(jsonObject.get("export OS_AUTH_URL=").toString().replaceAll(Character.toString('"'), ""));
                    ((JTextField) os_project_id).setText(jsonObject.get("export OS_PROJECT_ID=").toString().replaceAll(Character.toString('"'), ""));
                    ((JTextField) os_user_domain_name).setText(jsonObject.get("export OS_USER_DOMAIN_NAME=").toString().replaceAll(Character.toString('"'), ""));

                }
                if (os_username instanceof JTextField) {
                    setLastValues(((JTextField) os_username).getText(), ((JTextField) os_auth_url).getText(), String.valueOf(os_password.getPassword()), ((JTextField) os_project_id).getText(), ((JTextField) os_user_domain_name).getText());
                }else {
                    setLastValues(((JLabel) os_username).getText(), ((JLabel) os_auth_url).getText(), String.valueOf(os_password.getPassword()), ((JLabel) os_project_id).getText(), ((JLabel) os_user_domain_name).getText());
                }
                    state="file";
                recomput();


                jDialog.pack();
                jDialog.setVisible(true);
            }catch(Exception ex){
                OpenStackUtils.openStackLogDialog("The file could not be opened");
                ex.printStackTrace();
                jDialog.dispose();
            }

        } else if (src == clearButton){
            state="init";
            recomput();
            jDialog.pack();
            jDialog.setVisible(true);
        }
    }
    public ArrayList<Pattern> getPatterns(){

        ArrayList<Pattern> patterns = new ArrayList<>();

        Pattern r_url = Pattern.compile("(export OS_AUTH_URL=)(.*)");
        Pattern r_project_id = Pattern.compile("(export OS_PROJECT_ID=)(.*)");
        Pattern r_project_domain = Pattern.compile("(export OS_USER_DOMAIN_NAME=)(.*)");
        Pattern r_user = Pattern.compile("(export OS_USERNAME=)(.*)");

        patterns.add(r_url);
        patterns.add(r_project_id);
        patterns.add(r_project_domain);
        patterns.add(r_user);

        return  patterns;
    }
}
