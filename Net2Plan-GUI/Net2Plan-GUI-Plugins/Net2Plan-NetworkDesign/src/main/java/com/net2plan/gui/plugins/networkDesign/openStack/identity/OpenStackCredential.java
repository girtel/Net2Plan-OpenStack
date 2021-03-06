package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.api.Builders;
import org.openstack4j.model.identity.v3.Credential;

import java.awt.image.BufferedImage;
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
    public static OpenStackCredential createFromAddCredential (OpenStackNet osn , Credential credential)
    {
        final OpenStackCredential res = new OpenStackCredential(osn,credential);
        res.credentialId= credential.getId();
        res.credentialUserId=credential.getUserId();
        res.credentialProjectId=credential.getProjectId();
        res.credentialBlob=credential.getBlob();
        res.credentialType=credential.getType();
        res.credentialLinks=credential.getLinks();

        return res;
    }

    private OpenStackCredential (OpenStackNet osn, Credential credential )
    {
        super (osn ,  null, (List<OpenStackNetworkElement>) (List<?>) osn.openStackCredentials);

            this.osCredential = credential;
    }

    @Override
    public String getId () { return this.credentialId; }
    public String getCredentialUserId () { return this.credentialUserId; }
    public String getCredentialProjectId () { return this.credentialProjectId; }
    public String getCredentialBlob () { return this.credentialBlob; }
    public String getCredentialType () { return this.credentialType; }
    public Map<String,String> getCredentialLinks () { return this.credentialLinks; }

    public void setCredentialUserId (String value) {

        this.osn.getOSClientV3().identity().credentials().update(osCredential.toBuilder().userId(value).build());


    }
    public void setCredentialProjectId (String value) { this.osn.getOSClientV3().identity().credentials().update(osCredential.toBuilder().projectId(value).build()); }
    public void setCredentialBlob (String value) { this.osn.getOSClientV3().identity().credentials().update(osCredential.toBuilder().blob(value).build()); }
    public void setCredentialType (String value) { this.osn.getOSClientV3().identity().credentials().update(osCredential.toBuilder().type(value).build()); }

    @Override
    public String get50CharactersDescription()
    {
        return "Credentials: " + this.getId();
    }


}
