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

    public static OpenStackPolicy createFromAddPolicy (OpenStackNet osn , String policyId, String policyUserId, String policyProjectId, String policyType, String policyBlob, Map<String,String> policyLinks)
    {
        final OpenStackPolicy res = new OpenStackPolicy(osn);
        res.policyId= policyId;
        res.policyUserId=policyUserId;
        res.policyProjectId=policyProjectId;
        res.policyType=policyType;
        res.policyBlob=policyBlob;
        res.policyLinks=policyLinks;
        return res;
    }

    private OpenStackPolicy(OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osPolicies);
        if(this.policyId != null)
            this.osPolicy = this.osn.getOs().identity().policies().get(this.policyId);
    }

    @Override
    public String getId () { return this.policyId; }
    public String getPolicyUserId () { return this.policyUserId; }
    public String getPolicyProjectId () { return this.policyProjectId; }
    public String getPolicyType () { return this.policyType; }
    public String getPolicyBlob () { return this.policyBlob; }
    public Map<String,String> getPolicyLinks () { return this.policyLinks; }

    public void setPolicyUserId (String value) { this.osn.getOs().identity().policies().update(osPolicy.toBuilder().userId(value).build());  }
    public void setPolicyProjectId (String value) { this.osn.getOs().identity().policies().update(osPolicy.toBuilder().projectId(value).build());  }
    public void setPolicyType (String value) { this.osn.getOs().identity().policies().update(osPolicy.toBuilder().type(value).build());  }
    public void setPolicyBlob (String value) { this.osn.getOs().identity().policies().update(osPolicy.toBuilder().blob(value).build());  }

    @Override
    public String get50CharactersDescription()
    {
        return "Police" + this.getId();
    }


}
