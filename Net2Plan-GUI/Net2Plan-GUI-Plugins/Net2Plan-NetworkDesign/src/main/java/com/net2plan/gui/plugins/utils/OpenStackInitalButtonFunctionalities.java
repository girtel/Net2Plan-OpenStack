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

            JSONArray jsonArray = jsonObject.getJSONArray("information");
            OpenStackProgressBar openStackProgressBar = new OpenStackProgressBar(callback,jsonArray.length(),8,jsonObject);
            openStackProgressBar.incrementProgressBar("Reading file completed");

            //callback.getOpenStackNet().addNewLoginInformationToNet(openStackProgressBar,jsonObject);

        } catch (Exception ex) {
            System.out.println(ex.toString());
        }
    }

    public static void addLoginUserInformationDialog(GUINetworkDesign callback,JButton button){

        if(callback.getOpenStackLoginDialogCreator()!=null) {
            callback.getOpenStackLoginDialogCreator().setVisible();
        }else{
            callback.setOpenStackLoginDialogCreator(button);
            callback.getOpenStackLoginDialogCreator().setVisible();
        }
    }

    public static void addInformationPanelOfTopology(GUINetworkDesign callback,JButton jButton){

        if(callback.getOpenStackInformationDialogCreator()!=null) {
            callback.getOpenStackInformationDialogCreator().recomput();
            callback.getOpenStackInformationDialogCreator().setVisible();
        }else{
            callback.setOpenStackInformationDialogCreator(jButton);
            callback.getOpenStackInformationDialogCreator().recomput();
            callback.getOpenStackInformationDialogCreator().setVisible();
        }
    }
}
