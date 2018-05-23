package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.net2plan.interfaces.networkDesign.Link;
import org.openstack4j.model.identity.v3.Credential;

import java.util.List;
import java.util.Map;

public class OpenStackCredential extends OpenStackNetworkElement
{

    private String credentialId ;
    private String credentialUserId ;
    private String credentialProjectId ;
    private String credentialBlob ;
    private String credentialType;
    private Map<String,String> credentialLinks;
    private Credential osCredential;
    public static OpenStackCredential createFromAddCredential (OpenStackNet osn ,  String credentialId, String credentialUserId, String credentialProjectId, String credentialBlob, String credentialType, Map<String,String> credentialLinks)
    {
        final OpenStackCredential res = new OpenStackCredential(osn);
        res.credentialId= credentialId;
        res.credentialUserId=credentialUserId;
        res.credentialProjectId=credentialProjectId;
        res.credentialBlob=credentialBlob;
        res.credentialType=credentialType;
        res.credentialLinks=credentialLinks;
        return res;
    }

    private OpenStackCredential (OpenStackNet osn )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.list_osCredentials);
        if(this.credentialId != null)
            this.osCredential = this.osn.getOs().identity().credentials().get(this.credentialId);
    }

    @Override
    public String getId () { return this.credentialId; }
    public String getCredentialUserId () { return this.credentialUserId; }
    public String getCredentialProjectId () { return this.credentialProjectId; }
    public String getCredentialBlob () { return this.credentialBlob; }
    public String getCredentialType () { return this.credentialType; }
    public Map<String,String> getCredentialLinks () { return this.credentialLinks; }

    public void setCredentialUserId (String value) { this.osn.getOs().identity().credentials().update(osCredential.toBuilder().userId(value).build());  }
    public void setCredentialProjectId (String value) { this.osn.getOs().identity().credentials().update(osCredential.toBuilder().projectId(value).build()); }
    public void setCredentialBlob (String value) { this.osn.getOs().identity().credentials().update(osCredential.toBuilder().blob(value).build()); }
    public void setCredentialType (String value) { this.osn.getOs().identity().credentials().update(osCredential.toBuilder().type(value).build()); }

    @Override
    public String get50CharactersDescription()
    {
        return "Credentials" + this.getId();
    }


}
