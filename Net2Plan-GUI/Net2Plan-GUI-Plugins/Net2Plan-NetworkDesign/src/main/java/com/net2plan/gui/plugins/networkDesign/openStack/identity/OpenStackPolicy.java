package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
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

    public static OpenStackPolicy createFromAddPolicy (OpenStackNet osn , Policy policy)
    {
        final OpenStackPolicy res = new OpenStackPolicy(osn,policy);
        res.policyId= policy.getId();
        res.policyUserId=policy.getUserId();
        res.policyProjectId=policy.getProjectId();
        res.policyType=policy.getType();
        res.policyBlob=policy.getBlob();
        res.policyLinks=policy.getLinks();
        return res;
    }

    private OpenStackPolicy(OpenStackNet osn , Policy policy)
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.openStackPolicies);
        this.osPolicy = policy;
    }

    @Override
    public String getId () { return this.policyId; }
    public String getPolicyUserId () { return this.policyUserId; }
    public String getPolicyProjectId () { return this.policyProjectId; }
    public String getPolicyType () { return this.policyType; }
    public String getPolicyBlob () { return this.policyBlob; }
    public Map<String,String> getPolicyLinks () { return this.policyLinks; }

    public void setPolicyUserId (String value) { this.osn.getOSClientV3().identity().policies().update(osPolicy.toBuilder().userId(value).build());  }
    public void setPolicyProjectId (String value) { this.osn.getOSClientV3().identity().policies().update(osPolicy.toBuilder().projectId(value).build());  }
    public void setPolicyType (String value) { this.osn.getOSClientV3().identity().policies().update(osPolicy.toBuilder().type(value).build());  }
    public void setPolicyBlob (String value) { this.osn.getOSClientV3().identity().policies().update(osPolicy.toBuilder().blob(value).build());  }

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
