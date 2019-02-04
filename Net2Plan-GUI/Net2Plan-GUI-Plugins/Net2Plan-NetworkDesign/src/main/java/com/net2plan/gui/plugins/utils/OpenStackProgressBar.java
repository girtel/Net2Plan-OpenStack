package com.net2plan.gui.plugins.utils;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.visualizationControl.GUILayer;
import org.json.JSONObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OpenStackProgressBar{

    JProgressBar jProgressBar;
    int numClients=0,numSteps=0;
    JDialog jDialog;
    JPanel jPanel;

    JButton cancelButton;
    JSONObject jsonObject;
    GUINetworkDesign callback;
    SwingWorker swingWorker;

    public OpenStackProgressBar(GUINetworkDesign callback,int numClients, int numSteps, JSONObject jsonObject)
    {
        this.numClients = numClients;
        this.numSteps = numSteps;
        this.jsonObject = jsonObject;
        this.callback = callback;

        jDialog = new JDialog();
        jPanel = new JPanel();
        cancelButton = new JButton("Cancel");
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent actionEvent) {
                swingWorker.cancel(true);
                 cancelAction();
            }
        });
        jProgressBar = new JProgressBar(0, numClients*numSteps);
        jProgressBar.setValue(0);
        jProgressBar.setStringPainted(true);
        jProgressBar.setString("Initialization");

        jPanel.add(jProgressBar);
        jPanel.add(cancelButton);
        jDialog.add(jPanel);

        //jDialog.setUndecorated(true);
        jDialog.setModal(true);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        jDialog.pack();
        jDialog.setLocation(dim.width/2-jDialog.getSize().width/2, dim.height/2-jDialog.getSize().height/2);

        swingWorker = new SwingWorker() {
            @Override
            protected Object doInBackground() throws Exception {

                Boolean finish = false;
                jDialog.setVisible(true);
                callback.getOpenStackNet().addNewLoginInformationToNet(getThis(),jsonObject);
                if(swingWorker.isCancelled()){
                    System.out.println("CANCEL1");
                }
                //jDialog.dispose();
                return finish;
            }
        };

        swingWorker.execute();

        if(swingWorker.isCancelled()){
            System.out.println("CANCEL2");
        }


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

    public void cancelAction(){
        Thread currentThread = Thread.currentThread();
        currentThread.interrupt();
        swingWorker.cancel(true);

       // jDialog.dispose();
        return;
    }
}
