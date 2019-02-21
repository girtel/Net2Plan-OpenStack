package com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.compute;

import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackHostResource;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackImage;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.OpenStackLimits;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.ViewEditTopologyTablesPane;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AdvancedJTable_networkElement;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtColumnInfo;
import com.net2plan.gui.plugins.networkDesign.viewEditTopolTables.controlTables.AjtRcMenu;
import com.net2plan.gui.plugins.utils.GeneralForm;
import org.openstack4j.api.Builders;
import org.openstack4j.model.compute.AbsoluteLimit;
import org.openstack4j.model.compute.Image;

import java.util.*;

public class AdvancedJTable_limits extends AdvancedJTable_networkElement<OpenStackLimits> {
    public AdvancedJTable_limits(GUINetworkDesign callback, OpenStackClient openStackClient) {
        super(callback, ViewEditTopologyTablesPane.AJTableType.LIMITS, true, openStackClient);
       // System.out.println("Creating advancedjtable Limits");
    }

    @Override
    public List<AjtColumnInfo<OpenStackLimits>> getNonBasicUserDefinedColumnsVisibleOrNot() {

        final List<AjtColumnInfo<OpenStackLimits>> res = new LinkedList<>();
        res.add(new AjtColumnInfo<OpenStackLimits>(this, String.class, null, "OpenStack", "OpenStack ID", null, n -> n.getOpenStackClient().getName(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackLimits>(this, String.class, null, "URL", "OpenStack URL", null, n -> n.getOpenStackClient().os_auth_url, AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackLimits>(this, String.class, null, " Total Cores", " Total Cores", null, n -> n.getLimitCores(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackLimits>(this, Image.Status.class, null, "Used Cores", "Used Cores", null, n -> n.getLimitCoresUsed(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackLimits>(this, Image.Status.class, null, "Total Ram", "Total Ram", null, n -> n.getLimitRam(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackLimits>(this, Image.Status.class, null, "Used Ram", "Used ram", null, n -> n.getLimitRamUsed(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackLimits>(this, Image.Status.class, null, "Total instances", "Total instances", null, n -> n.getLimitInstances(), AGTYPE.NOAGGREGATION, null, null));
        res.add(new AjtColumnInfo<OpenStackLimits>(this, Image.Status.class, null, "Used instances", "Used instances", null, n -> n.getLimitInstancesUsed(), AGTYPE.NOAGGREGATION, null, null));

        return res;
    }


    @Override
    public List<AjtRcMenu> getNonBasicRightClickMenusInfo() {
        final List<AjtRcMenu> res = new ArrayList<>();


        res.add(new AjtRcMenu("Adjust quotas with hosts", e -> getSelectedElements().forEach(n -> {

         getAllHostAndTakeQuotasForAdmin(n);

        }), (a, b) -> b == 1, null));
        res.add(new AjtRcMenu("Adjust quotas manual", e -> getSelectedElements().forEach(n -> {

            manualAdjust(n);

        }), (a, b) -> b == 1, null));
/*
        res.add(new AjtRcMenu("Adjust percentage of quotas", e -> getSelectedElements().forEach(n -> {

            int numeroDeProyectos = n.getOpenStackClient().openStackProjects.size();
            AbsoluteLimit absoluteLimit = n.getOpenStackClient().getClient().compute().quotaSets().limits().getAbsolute();

            n.getOpenStackClient().openStackProjects.stream().forEach(r-> n.getOpenStackClient().getClient().compute().quotaSets()
                    .updateForTenant(r.getId(), Builders.quotaSet()
                    .cores(absoluteLimit.getMaxTotalCores()/numeroDeProyectos)
                    .keyPairs(absoluteLimit.getMaxTotalKeypairs()/numeroDeProyectos)
                    .instances(absoluteLimit.getMaxTotalInstances()/numeroDeProyectos)
                    .build()));

        }), (a, b) -> b == 1, null));
        */

        return res;

    }
    public void getAllHostAndTakeQuotasForAdmin(OpenStackLimits openStackLimits){

        OpenStackClient openStackClientFromLimit = openStackLimits.getOpenStackClient();
        List<OpenStackHostResource> openStackHostResourcesFromClient = openStackClientFromLimit.openStackHostResources;
        String adminProjectId = openStackClientFromLimit.openStackProjects.stream().filter(n->n.getProjectName().equals("admin")).findFirst().get().getId();
        int memory=0;
        int cpu=0;
        int disk=0;

        for(OpenStackHostResource openStackHostResource: openStackHostResourcesFromClient){
            if(openStackHostResource.getHostProject().equals("(total)")){
                memory+=openStackHostResource.getHostMemory();
                cpu+=openStackHostResource.getHostCpu();
                disk+=openStackHostResource.getHostDisk();
            }
        }


        openStackClientFromLimit.getClient().compute().quotaSets()
                .updateForTenant(adminProjectId, Builders.quotaSet()
                        .cores(cpu)
                        .ram(memory)
                        .build());

        updateTab();
    }
    public void manualAdjust(OpenStackLimits openStackLimits){

        Map<String,String> headers = new HashMap<>();
        headers.put("Cores","");
        headers.put("Ram","");
        headers.put("Instances","");
        GeneralForm generalTableForm = new GeneralForm("Change quotas limit",headers,this.ajtType,openStackLimits.getOpenStackClient(),this,openStackLimits);


    }
}




