package com.net2plan.gui.plugins.networkDesign.openStack;


import com.google.common.collect.Lists;
import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.*;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackProject;
import com.net2plan.gui.plugins.networkDesign.openStack.network.*;
import com.net2plan.gui.plugins.utils.MyRunnable;
import com.net2plan.gui.plugins.utils.OpenStackProgressBar;
import com.net2plan.gui.plugins.utils.OpenStackUtils;
import com.net2plan.interfaces.networkDesign.NetPlan;

import java.awt.*;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import org.json.JSONArray;
import org.json.JSONObject;
import org.openstack4j.api.Builders;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.*;
import org.openstack4j.model.compute.FloatingIP;
import org.openstack4j.model.identity.v3.*;
import org.openstack4j.model.network.*;
import org.openstack4j.openstack.OSFactory;


import static edu.emory.mathcs.utils.ConcurrencyUtils.setNumberOfThreads;
import static edu.emory.mathcs.utils.ConcurrencyUtils.submit;

/**
 *
 * @author Manuel
 */
public class OpenStackNet
{

    private GUINetworkDesign callback;
    private NetPlan np;

    private List<OpenStackClient> osClients = new ArrayList<>();
    private JSONArray loginInformation = new JSONArray();

    public List<OpenStackQuotas> openStackQuotas = new ArrayList<>();
    public List<OpenStackQuotasUsage> openStackQuotasUsage = new ArrayList<>();
    public List<OpenStackLimits> openStackLimits = new ArrayList<>();


    public OpenStackNet()
    {
        this.np = new NetPlan();
    }
    public OpenStackNet (GUINetworkDesign callback) {
        this.callback = callback;
        this.np = callback.getDesign();
    }

    public GUINetworkDesign getCallback(){
        return this.callback;
    }
    public NetPlan getNetPlan () { return this.np; }
    public List<OpenStackClient> getOsClients(){return this.osClients;}

    public JSONObject getLoginInformationOfNet(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("information",loginInformation);
        return jsonObject;
    }
    public void addNewLoginInformationToNet(OpenStackProgressBar openStackProgressBar,JSONObject jsonObject){


       //System.out.println("Adding new information");
        JSONArray jsonArray = jsonObject.getJSONArray("information");
        openStackProgressBar.getSwingWorker().incrementProgressBar("Getting clients");

        for(Object object: jsonArray){
            this.addNewOpenStackClientToNet(openStackProgressBar,(JSONObject) object,this.loginInformation.length());
          }

        openStackProgressBar.getSwingWorker().incrementProgressBar("Getting clients completed");
        openStackProgressBar.getSwingWorker().incrementProgressBar("Filling slicing components");
        this.fillSlicingTabTablesOfNet();
        openStackProgressBar.getSwingWorker().incrementProgressBar("Filling slicing components completed");
        openStackProgressBar.getSwingWorker().incrementProgressBar("Updating view");
        callback.getViewEditTopTables().updateView();
        openStackProgressBar.getSwingWorker().incrementProgressBar("Updating view complete");

    }
    public void addNewOpenStackClientToNet(OpenStackProgressBar openStackProgressBar,JSONObject information,int index){

        openStackProgressBar.getSwingWorker().incrementProgressBar("Adding client");
          if(determinesIfExistInNet(information))
              return;

            OpenStackClient openStackClient = new OpenStackClient().create(this,information,"Openstack " + index);

            if(openStackClient.isConnected()) {
                openStackClient
                        .clearClientListsAndTopology()
                        .fillClientListsAndTopology();
                osClients.add(openStackClient);
                loginInformation.put(information);
                openStackProgressBar.getSwingWorker().incrementProgressBar("Adding client completed");
             }else {
                OpenStackUtils.openStackLogDialog("The connection was not possible. Please check the input data or the network. For more information look console. ");
            }

    }

    public boolean determinesIfExistInNet(JSONObject information){
        boolean answer = false;

        for(Object o: loginInformation){
            JSONObject client = (JSONObject)o;
            if(client.get("os_auth_url").equals(information.get("os_auth_url")))
                 if(client.get("os_user_domain_name").equals(information.get("os_user_domain_name")))
                         if(client.get("os_username").equals(information.get("os_username")))
                                if(client.get("os_project_id").equals(information.get("os_project_id"))) answer=true;

        }

        return answer;
    }
    public void fillSlicingTabTablesOfNet(){

        openStackQuotasUsage.clear();
        openStackQuotas.clear();
        openStackLimits.clear();

        try {

            //System.out.println("Clear quotas");
            for(OpenStackClient openStackClient: getOsClients()){
               // System.out.println("OpenStack " + openStackClient.getName());
                addOpenStackLimit(openStackClient.getClient().compute().quotaSets().limits().getAbsolute(), openStackClient);
                openStackClient.openStackProjects.stream().forEach(r -> {
                 //   System.out.println("Project " + r.getName());
                    addOpenStackQuota(openStackClient.getClient().compute().quotaSets().get(r.getId()), openStackClient, r);
                    addOpenStackQuotaUsage(openStackClient.getClient().compute().quotaSets().getTenantUsage(r.getId()), openStackClient, r);
                });

            }
/*
            getOsClients().stream().forEach(n -> addOpenStackLimit(OSFactory.clientFromToken(n.getToken()).compute().quotaSets().limits().getAbsolute(), n));
            getOsClients().stream().forEach(n -> {n.openStackProjects.stream().forEach(r -> addOpenStackQuota(OSFactory.clientFromToken(n.getToken()).compute().quotaSets().get(r.getId()), n, r));});
            getOsClients().stream().forEach(n -> n.openStackProjects.stream().forEach(r -> addOpenStackQuotaUsage(OSFactory.clientFromToken(n.getToken()).compute().quotaSets().getTenantUsage(r.getId()), n, r)));
*/
        }catch(Exception ex){
            ex.printStackTrace();
        }
    }

    public OpenStackNetworkElement getOpenStackNetworkElementByOpenStackId (String openStackId) {

        final List<OpenStackNetworkElement> allOpenStackNetworkElements = Lists.newArrayList();

        for(OpenStackClient openStackClient:osClients) {

            /*OpenStackNetworkElements of Keystone*/
            allOpenStackNetworkElements.addAll(openStackClient.openStackUsers);
            allOpenStackNetworkElements.addAll(openStackClient.openStackProjects);
            allOpenStackNetworkElements.addAll(openStackClient.openStackDomains);
            allOpenStackNetworkElements.addAll(openStackClient.openStackEndpoints);
            allOpenStackNetworkElements.addAll(openStackClient.openStackServices);
            allOpenStackNetworkElements.addAll(openStackClient.openStackRegions);
            allOpenStackNetworkElements.addAll(openStackClient.openStackCredentials);
            allOpenStackNetworkElements.addAll(openStackClient.openStackGroups);
            allOpenStackNetworkElements.addAll(openStackClient.openStackPolicies);
            allOpenStackNetworkElements.addAll(openStackClient.openStackRoles);

            /*OpenStackNetworkElements of Neutron*/
            allOpenStackNetworkElements.addAll(openStackClient.openStackNetworks);
            allOpenStackNetworkElements.addAll(openStackClient.openStackSubnets);
            allOpenStackNetworkElements.addAll(openStackClient.openStackRouters);
            allOpenStackNetworkElements.addAll(openStackClient.openStackPorts);

            /*OpenStackNetworkElements of Nova*/
            allOpenStackNetworkElements.addAll(openStackClient.openStackServers);
            allOpenStackNetworkElements.addAll(openStackClient.openStackFlavors);
            allOpenStackNetworkElements.addAll(openStackClient.openStackHostResources);
            allOpenStackNetworkElements.addAll(openStackClient.openStackKeypairs);
            allOpenStackNetworkElements.addAll(openStackClient.openStackSecurityGroups);
            allOpenStackNetworkElements.addAll(openStackClient.openStackFloatingIps);
            allOpenStackNetworkElements.addAll(openStackClient.openStackRules);

            /*OpenStackNetworkElements of Glance*/
            allOpenStackNetworkElements.addAll(openStackClient.openStackImages);

            allOpenStackNetworkElements.addAll(openStackClient.openStackVolumes);

            /*OpenStackNetworkElements of Ceilometer*/
            allOpenStackNetworkElements.addAll(openStackClient.openStackResources);
            allOpenStackNetworkElements.addAll(openStackClient.openStackMeters);
            allOpenStackNetworkElements.addAll(openStackClient.openStackMeasures);
            allOpenStackNetworkElements.addAll(openStackClient.openStackSummaries);


        }

        allOpenStackNetworkElements.addAll(openStackLimits);
        allOpenStackNetworkElements.addAll(openStackQuotas);
        allOpenStackNetworkElements.addAll(openStackQuotasUsage);

        Optional<OpenStackNetworkElement> element = allOpenStackNetworkElements.stream().filter(n->n.getId().equals(openStackId)).findFirst();

        if (element.isPresent()) return element.get();
        else return null;
    }

    public OpenStackQuotasUsage addOpenStackQuotaUsage (SimpleTenantUsage simpleTenantUsage, OpenStackClient openStackClient, OpenStackProject project){
        final OpenStackQuotasUsage res = OpenStackQuotasUsage.createFromAddQuotaUsage(this ,simpleTenantUsage,openStackClient,project);
        if(openStackQuotasUsage.contains(res)) return res;
        openStackQuotasUsage.add(res);
        return res;
    }
    public OpenStackQuotas addOpenStackQuota (QuotaSet quotaSet, OpenStackClient openStackClient, OpenStackProject project){
        final OpenStackQuotas res = OpenStackQuotas.createFromAddQuota(this ,quotaSet,openStackClient,project);
        if(openStackQuotas.contains(res)) return res;
        openStackQuotas.add(res);
        return res;
    }
    public OpenStackLimits addOpenStackLimit (AbsoluteLimit absoluteLimit, OpenStackClient openStackClient){
        //System.out.println(absoluteLimit);
        final OpenStackLimits res = OpenStackLimits.createFromAddLimit(this ,absoluteLimit,openStackClient);
        if(openStackLimits.contains(res)) return res;
        openStackLimits.add(res);
        return res;
    }

    public List<OpenStackLimits> getOpenStackLimits (){ return Collections.unmodifiableList(openStackLimits); }
    public List<OpenStackQuotas> getOpenStackQuotas (){ return Collections.unmodifiableList(openStackQuotas); }
    public List<OpenStackQuotasUsage> getOpenStackQuotasUsage (){ return Collections.unmodifiableList(openStackQuotasUsage); }


}