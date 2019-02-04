package com.net2plan.gui.plugins.utils;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.GUILayer;
import org.json.JSONObject;

import javax.swing.*;

public class OpenStackProgressBar{

    JProgressBar jProgressBar;
    int numClients=0,numSteps=0;
    JDialog jDialog;
    JPanel jPanel;

    JSONObject jsonObject;
    GUINetworkDesign callback;

    public OpenStackProgressBar(GUINetworkDesign callback,int numClients, int numSteps, JSONObject jsonObject)
    {
        this.numClients = numClients;
        this.numSteps = numSteps;
        this.jsonObject = jsonObject;
        this.callback = callback;

        jDialog = new JDialog();
        jPanel = new JPanel();
        jProgressBar = new JProgressBar(0, numClients*numSteps);
        jProgressBar.setValue(0);
        jProgressBar.setStringPainted(true);
        jProgressBar.setString("Initialization");

        jPanel.add(jProgressBar);
        jDialog.add(jPanel);
        jDialog.pack();

        final SwingWorker swingWorker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {

                Boolean finish = false;
                jDialog.setVisible(true);
                callback.getOpenStackNet().addNewLoginInformationToNet(getThis(),jsonObject);

                jDialog.dispose();
                return finish;
            }
        };

        swingWorker.execute();

        /*try{
        if((Boolean)swingWorker.get()){
            jDialog.dispose();
        }}catch (Exception ex){
            OpenStackUtils.openStackLogDialog(ex.getMessage());
        }*/

    }

    public OpenStackProgressBar getThis(){return this;}

    public void incrementProgressBar(String information){
        jProgressBar.setValue(jProgressBar.getValue() + 1);
        jProgressBar.setString(information);
    }
}
