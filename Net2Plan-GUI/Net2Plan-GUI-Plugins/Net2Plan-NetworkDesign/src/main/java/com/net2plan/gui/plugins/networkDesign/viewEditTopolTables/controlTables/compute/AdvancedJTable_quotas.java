package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackLimits;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackQuotas;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackProject;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.net2plan.gui.plugins.utils.GeneralForm;
import org.openstack4j.api.Builders;
import org.openstack4j.model.compute.QuotaSet;

import javax.swing.*;
import java.util.*;

public class AdvancedJTable_quotas extends AdvancedJTable_networkElement<OpenStackQuotas> {
    public AdvancedJTable_quotas(GUINetworkDesign callback, OpenStackClient openStackClient) {
        super(callback, ViewEditTopologyTablesPane.AJTableType.QUOTAS, true, openStackClient);
    }

    @Override
    public List<AjtColumnInfo<OpenStackQuotas>> getNonBasicUserDefinedColumnsVisibleOrNot() {

        final List<AjtColumnInfo<OpenStackQuotas>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackQuotas>(this, String.class, null, "OpenStack", "OpenStack ID", null, n -> n.getOpenStackClient().getName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuotas>(this, String.class, null, "Project ", " Project name", null, n -> callback.getOpenStackNet().getOpenStackNetworkElementByOpenStackId(n.getOpenStackProject().getId()), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuotas>(this, String.class, null, "Cores", "Project Cores", null, n -> n.getQuotaCores(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuotas>(this, String.class, null, "Instances", "Project Instances", null, n -> n.getQuotaInstances(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackQuotas>(this, String.class, null, "Ram", "Project Ram", null, n -> n.getQuotaRam(), AGTYPE.NOAGGREGATION, null, null));
        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo() {
        final List<AjtRcMenu> res = new ArrayList<>();


        res.add(new AjtRcMenu("Adjust manual", e -> getSelectedElements().forEach(n -> {

            manualAdjust(n);

        }), (a, b) -> b == 1, null));
        res.add(new AjtRcMenu("Create instances", e -> getSelectedElements().forEach(n -> {

            createInstances(n);

        }), (a, b) -> b == 1, null));

        res.add(new AjtRcMenu("Adjust default", e -> defaultAdjust( getSelectedElements()), (a, b) -> b > 0, null));

        res.add(new AjtRcMenu("Refresh", e ->updateTab(), (a, b) -> b >=0, null));

        return res;

    }

    public void defaultAdjust(ArrayList<OpenStackQuotas> openStackQuotas){

        List<String> openStack = new ArrayList<>();
        boolean adminProjectIsSelected = false;

        for(Iterator iterator = openStackQuotas.iterator();iterator.hasNext();){
            OpenStackQuotas openStackQuota = (OpenStackQuotas)iterator.next();
            String name = openStackQuota.getOpenStackClient().getName();
            if(!openStack.contains(name))openStack.add(name);
            System.out.println(openStackQuota.getProject_id() +"  " +openStackQuota.getOpenStackClient().getProjectId());
            if(openStackQuota.getProject_id().equals(openStackQuota.getOpenStackClient().getProjectId())) adminProjectIsSelected=true;

        }
        System.out.println(openStack);
        //System.out.println(openStack + "" +openStackQuotas.size());

        OpenStackProject openStackProjectAdmin = openStackQuotas.get(0).getOpenStackClient().openStackProjects.stream().filter(n->n.getProjectName().equals("admin")).findFirst().get();
        openStackQuotas.get(0).getOpenStackClient().updateClient();
        QuotaSet quotaSetAdmin = openStackQuotas.get(0).getOpenStackClient().getClient().compute().quotaSets().get(openStackProjectAdmin.getId());

        int ram = (quotaSetAdmin.getRam()/2)/openStackQuotas.size();
        int cores = (quotaSetAdmin.getCores()/2)/openStackQuotas.size();
        int gigabytes = (quotaSetAdmin.getGigabytes()/2)/openStackQuotas.size();
        int instances = (quotaSetAdmin.getInstances()/2)/openStackQuotas.size();

        if(openStack.size()>1 || adminProjectIsSelected) {
            JOptionPane.showMessageDialog(null, "Project openstack must be equal and admin project dont be selected");
        }else {
             for(Iterator iterator = openStackQuotas.iterator();iterator.hasNext();){
             OpenStackQuotas openStackQuota= (OpenStackQuotas)iterator.next();
             OpenStackClient openStackClient = openStackQuota.getOpenStackClient();
             openStackClient.updateClient();

             openStackClient.getClient().compute().quotaSets().updateForTenant(openStackQuota.getProject_id(),
                     Builders.quotaSet()
                             .ram(ram)
                             .cores(cores)
                             .instances(instances)
                             .build());

            }
            updateTab();
        }


    }
    public void manualAdjust(OpenStackQuotas openStackQuotas){

        Map<String,String> headers = new HashMap<>();
        headers.put("Cores","");
        headers.put("Ram","");
        headers.put("Instances","");
        GeneralForm generalTableForm = new GeneralForm("Change quotas for project",headers,this.ajtType,openStackQuotas.getOpenStackClient(),this,openStackQuotas);


    }
    public void createInstances(OpenStackQuotas openStackQuotas){
        openStackQuotas.getOpenStackClient().updateClient();
        //JS
        //for(int i=0;i<15;i++)
       // openStackClient.getOpenStackNetCreate().createOpenStackServer();
    }
}





