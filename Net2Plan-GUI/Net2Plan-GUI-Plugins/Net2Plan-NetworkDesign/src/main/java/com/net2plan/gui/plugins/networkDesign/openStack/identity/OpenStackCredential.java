package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.json.JSONObject;
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

    public void setCredentialUserId (JSONObject jsonObject) {
try{
        this.osn.getOSClientV3().identity().credentials().update(osCredential.toBuilder().userId(jsonObject.getString("User ID")).build());
}catch(Exception ex){

    logPanel();
    System.out.println(ex.toString());

}

    }
    public void setCredentialProjectId (JSONObject jsonObject) {
        try{
        this.osn.getOSClientV3().identity().credentials().update(osCredential.toBuilder().projectId(jsonObject.getString("Project ID")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
        }
    public void setCredentialBlob (JSONObject jsonObject) {
        try{
        this.osn.getOSClientV3().identity().credentials().update(osCredential.toBuilder().blob(jsonObject.getString("Blob")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }

        }
    public void setCredentialType (JSONObject jsonObject) {
        try{
        this.osn.getOSClientV3().identity().credentials().update(osCredential.toBuilder().type(jsonObject.getString("Type")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }

    }

    @Override
    public String get50CharactersDescription()
    {
        String description = "Credential: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "User ID " + this.getCredentialUserId() +
                this.NEWLINE + "Project/Tenant ID " + this.getCredentialProjectId() +
                this.NEWLINE + "Blob " + this.getCredentialBlob() +
                this.NEWLINE + "Type " + this.getCredentialType() +
                this.NEWLINE + "Links" + this.NEWLINE;
        for(String key : this.getCredentialLinks().keySet()) {
            description += key + " " + this.getCredentialLinks().get(key) + NEWLINE;
        }
        return description;
    }


}
