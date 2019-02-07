package com.net2plan.gui.plugins.networkDesign.openStack.blockstorage;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackUser;
import org.json.JSONObject;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.User;

import java.util.List;
import java.util.Map;

public class OpenStackSnapshot extends OpenStackNetworkElement
{

    private String userId;
    private String userName ;
    private String userDomainId ;
    private String userEmail ;
    private String userDescription ;
    private Domain userDomain;
    private Boolean userEnable;
    private Map<String,String> userLinks;
    private User user ;
    private String userDefaultProjectId;


    public static OpenStackSnapshot createFromAddUser (OpenStackNet osn , User user, OpenStackClient openStackClient)
    {
        final OpenStackSnapshot res = new OpenStackSnapshot(osn,user,openStackClient);
        res.userId= user.getId();
        res.userName=user.getName();
        res.userDomain = user.getDomain();
        res.userDomainId=user.getDomainId();
        res.userEmail=user.getEmail();
        res.userDescription=user.getDescription();
        res.userEnable = user.isEnabled();
        res.userLinks = user.getLinks();
        res.userDefaultProjectId = user.getDefaultProjectId();

        return res;
    }

    private OpenStackSnapshot (OpenStackNet osn , User user, OpenStackClient openStackClient)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackUsers,openStackClient);
        this.user = user;


    }

    @Override
    public String getId () { return this.userId; }
    public String getName () { return this.userName; }
    public Domain getUserDomain() {return this.userDomain;}
    public String getDomainId () { return this.userDomainId; }
    public String getEmail () { return this.userEmail; }
    public String getDescription () { return this.userDescription; }
    public String getUserDefaultProjectId () { return this.userDefaultProjectId; }
    public Boolean isUserEnable(){return  this.userEnable;}
    public Map<String,String> getUserLinks(){return this.userLinks;}


    @Override
    public String get50CharactersDescription()
    {
        String description = "Snapshot: " ;
        return description;
    }

}


