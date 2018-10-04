package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.json.JSONObject;
import org.openstack4j.model.identity.v3.Policy;

import java.util.List;
import java.util.Map;

public class OpenStackPolicy extends OpenStackNetworkElement
{

    private String policyId;
    private String policyUserId;
    private String policyProjectId;
    private String policyType;
    private String policyBlob;
    private Map<String,String> policyLinks;

    private Policy osPolicy;

    public static OpenStackPolicy createFromAddPolicy (OpenStackNet osn , Policy policy, OpenStackClient openStackClient)
    {
        final OpenStackPolicy res = new OpenStackPolicy(osn,policy,openStackClient);
        res.policyId= policy.getId();
        res.policyUserId=policy.getUserId();
        res.policyProjectId=policy.getProjectId();
        res.policyType=policy.getType();
        res.policyBlob=policy.getBlob();
        res.policyLinks=policy.getLinks();
        return res;
    }

    private OpenStackPolicy(OpenStackNet osn , Policy policy, OpenStackClient openStackClient)
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackPolicies,openStackClient);
        this.osPolicy = policy;
    }

    @Override
    public String getId () { return this.policyId; }
    public String getPolicyUserId () { return this.policyUserId; }
    public String getPolicyProjectId () { return this.policyProjectId; }
    public String getPolicyType () { return this.policyType; }
    public String getPolicyBlob () { return this.policyBlob; }
    public Map<String,String> getPolicyLinks () { return this.policyLinks; }

    public void setPolicyUserId (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().policies().update(osPolicy.toBuilder().userId(jsonObject.getString("User ID")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }

        }
    public void setPolicyProjectId (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().policies().update(osPolicy.toBuilder().projectId(jsonObject.getString("Project ID")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }
    public void setPolicyType (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().policies().update(osPolicy.toBuilder().type(jsonObject.getString("Type")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }
    public void setPolicyBlob (JSONObject jsonObject) {
        try{
        this.openStackClient.getClient().identity().policies().update(osPolicy.toBuilder().blob(jsonObject.getString("Blob")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }

    @Override
    public String get50CharactersDescription()
    {
        String description = "Policy: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "User ID " + this.getPolicyUserId() +
                this.NEWLINE + "Project/Tenant ID " + this.getPolicyProjectId() +
                this.NEWLINE + "Blob " + this.getPolicyBlob() +
                this.NEWLINE + "Type " + this.getPolicyType() +
                this.NEWLINE + "Links" + this.NEWLINE;
        for(String key : this.getPolicyLinks().keySet()) {
            description += key + " " + this.getPolicyLinks().get(key) + NEWLINE;
        }
        return description;
    }


}
