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
            AddOsClient((JSONObject) object,num);
            num++;
        }
        Inicialice();
    }
    public void AddOsClient(JSONObject credential,int index){

            OpenStackClient openStackClient =new OpenStackClient()
                    .create(this,credential,"OPENSTACK " + index)
                    .clearList()
                    .fillList();

            osClients.add(openStackClient);
            credentiales.put(credential);

    }

    public OpenStackNetworkElement getOpenStackNetworkElementByOpenStackId (String openStackId) {

        final List<OpenStackNetworkElement> allOpenStackNetworkElements = Lists.newArrayList();

        /*OpenStackNetworkElements of Neutron*/
        allOpenStackNetworkElements.addAll(osClients.get(0).openStackNetworks);
        allOpenStackNetworkElements.addAll(osClients.get(0).openStackNetworks);
        allOpenStackNetworkElements.addAll(osClients.get(0).openStackNetworks);
        allOpenStackNetworkElements.addAll(osClients.get(0).openStackNetworks);

        /*OpenStackNetworkElements of Neutron*/
        allOpenStackNetworkElements.addAll(osClients.get(0).openStackNetworks);
        allOpenStackNetworkElements.addAll(osClients.get(0).openStackNetworks);
        allOpenStackNetworkElements.addAll(osClients.get(0).openStackNetworks);
        allOpenStackNetworkElements.addAll(osClients.get(0).openStackNetworks);
        allOpenStackNetworkElements.addAll(osClients.get(0).openStackNetworks);
        allOpenStackNetworkElements.addAll(osClients.get(0).openStackNetworks);
        allOpenStackNetworkElements.addAll(osClients.get(0).openStackNetworks);

        Optional<OpenStackNetworkElement> element = allOpenStackNetworkElements.stream().filter(n->n.getId() == openStackId).findFirst();
        if (element.isPresent()) return element.get();
        else return null;
    }
    public List<OpenStackClient> getOsClients(){return this.osClients;}



}