package com.net2plan.gui.plugins.networkDesign.openStack;


import com.google.common.collect.Lists;
import com.net2plan.gui.plugins.GUINetworkDesign;
import com.net2plan.gui.plugins.networkDesign.openStack.compute.*;
import com.net2plan.gui.plugins.networkDesign.openStack.network.*;
import com.net2plan.gui.plugins.utils.MyRunnable;
import com.net2plan.interfaces.networkDesign.NetPlan;
import java.util.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openstack4j.api.OSClient.OSClientV3;
import org.openstack4j.api.types.Facing;
import org.openstack4j.model.common.Identifier;
import org.openstack4j.model.compute.*;
import org.openstack4j.model.compute.FloatingIP;
import org.openstack4j.model.identity.v3.*;
import org.openstack4j.model.network.*;
import org.openstack4j.openstack.OSFactory;


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
    private JSONArray credentiales = new JSONArray();

    public final  NetPlan getNetPlan () { return np; }
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
    public void Inicialice(){

        System.out.println("Inicialice");
        callback.getViewEditTopTables().updateView();

    }
    public JSONObject getJSONObjectOsClients(){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("Credentials",credentiales);
        System.out.println("OpenStackNet Getting credentiales "+ jsonObject);
        return jsonObject;
    }
    public void AddJSONObjectOsClients(JSONObject jsonObject){

        System.out.println("OpenStackNet adding credentiales "+ jsonObject);
        JSONArray jsonArray = jsonObject.getJSONArray("Credentials");
        int num = 0;
        for(Object object: jsonArray){
            AddOsClient((JSONObject) object,credentiales.length());
            num++;
        }
        Inicialice();
    }
    public void AddOsClient(JSONObject credential,int index){

            OpenStackClient openStackClient =new OpenStackClient().create(this,credential,"Openstack " + index);
            if(openStackClient.isConnected()) {
                openStackClient
                        .clearList()
                        .fillList();
                osClients.add(openStackClient);
            }
            credentiales.put(credential);

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

            /*OpenStackNetworkElements of Glance*/
            allOpenStackNetworkElements.addAll(openStackClient.openStackImages);

            /*OpenStackNetworkElements of Ceilometer*/
            allOpenStackNetworkElements.addAll(openStackClient.openStackResources);
            allOpenStackNetworkElements.addAll(openStackClient.openStackMeters);
            allOpenStackNetworkElements.addAll(openStackClient.openStackMeasures);
        }
        Optional<OpenStackNetworkElement> element = allOpenStackNetworkElements.stream().filter(n->n.getId() == openStackId).findFirst();
        if (element.isPresent()) return element.get();
        else return null;
    }
    public List<OpenStackClient> getOsClients(){return this.osClients;}



}