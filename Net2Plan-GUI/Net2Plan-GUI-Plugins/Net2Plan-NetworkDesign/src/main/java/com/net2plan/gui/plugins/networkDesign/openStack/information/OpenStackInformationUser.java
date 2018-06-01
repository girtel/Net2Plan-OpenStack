package com.net2plan.gui.plugins.networkDesign.openStack.information;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.User;

import java.util.List;
import java.util.Map;

public class OpenStackInformationUser extends OpenStackNetworkElement {

    private String userId;
    private String userName ;
    private String userDomainId ;
    private String userEmail ;
    private String userDescription ;
    private Domain userDomain;
    private Boolean userEnable;
    private Map<String,String> userLinks;
    private User user ;


    public static OpenStackInformationUser createFromAddInformationUser (OpenStackNet osn , User user)
    {
        final OpenStackInformationUser res = new OpenStackInformationUser(osn,user);
        res.userId= user.getId();
        res.userName=user.getName();
        res.userDomain = user.getDomain();
        res.userDomainId=user.getDomainId();
        res.userEmail=user.getEmail();
        res.userDescription=user.getDescription();
        res.userEnable = user.isEnabled();
        res.userLinks = user.getLinks();

        return res;
    }

    private OpenStackInformationUser (OpenStackNet osn , User user)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) osn.openStackInformationUser);
        this.user = user;

    }

    @Override
    public String getId () { return this.userId; }
    public String getName () { return this.userName; }
    public Domain getUserDomain() {return this.userDomain;}
    public String getDomainId () { return this.userDomainId; }
    public String getEmail () { return this.userEmail; }
    public String getDescription () { return this.userDescription; }
    public Boolean isUserEnable(){return  this.userEnable;}
    public Map<String,String> getUserLinks(){return this.userLinks;}


    @Override
    public String get50CharactersDescription()
    {
        return "User: " + this.getId();
    }

    public void isUserEnable(Boolean value){

        try{

            this.osn.getOSClientV3().identity().users().update(user.toBuilder().enabled(value).build());

        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void updateUserDescription(String userDescription){

        this.osn.getOSClientV3().identity().users().update(this.user.toBuilder().description(userDescription).build());


    }

    public void updateUserName(String userName){

        this.osn.getOSClientV3().identity().users().update(this.user.toBuilder().name(userName).build());


    }

    public void updateUserEmail(String userEmail){

        this.osn.getOSClientV3().identity().users().update(this.user.toBuilder().email(userEmail).build());


    }

    public void deleteUser(){
        this.osn.getOSClientV3().identity().users().delete(this.userId);
    }
}


