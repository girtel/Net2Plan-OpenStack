package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.image;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.FileChooserNetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.image.OpenStackImageV2;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.net2plan.internal.Constants;
import com.net2plan.internal.SystemUtils;
import org.json.JSONObject;

import javax.swing.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedJTable_imagesV2 extends AdvancedJTable_networkElement<OpenStackImageV2>
{
    public AdvancedJTable_imagesV2(GUINetworkDesign callback,OpenStackClient openStackClient)
    {
        super(callback, ViewEditTopologyTablesPane.AJTableType.IMAGES , true,openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackImageV2>> getNonBasicUserDefinedColumnsVisibleOrNot()
    {

        final List<AjtColumnInfo<OpenStackImageV2>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackImageV2>(this, Object.class, null, "ID", "Image ID", null, n -> n.getId(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackImageV2>(this, String.class, null, "Name", " Image Name", null, n -> n.getName(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo()
    {
        final List<AjtRcMenu> res = new ArrayList<>();


        res.add(new AjtRcMenu("Create image", e ->addImage(), (a, b) -> b >=0, null));

        res.add(new AjtRcMenu("Refresh", e ->updateTab(), (a, b) -> b >=0, null));
        return res;

    }
    public void addImage(){
        // File chooser default directory.
        final File currentDir = SystemUtils.getCurrentDir();


        // File chooser
        JFileChooser chooser = new JFileChooser();

        int returnVal = chooser.showOpenDialog(this);
        if(returnVal == JFileChooser.APPROVE_OPTION) {
            System.out.println("You chose to open this file: " +
                    chooser.getSelectedFile().getName());
            JSONObject information = new JSONObject();
            information.put("PATH",chooser.getSelectedFile().getPath());
            information.put("NAME",chooser.getSelectedFile().getName());
            openStackClient.getOpenStackNetCreate().createOpenStackImage(information);
            updateTab();
        }
    }




}