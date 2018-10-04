package com.net2plan.gui.plugins.networkDesign.openStack.identity;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.json.JSONObject;
import org.openstack4j.api.Builders;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.User;

import java.util.List;
import java.util.Map;

public class OpenStackUser extends OpenStackNetworkElement
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


    public static OpenStackUser createFromAddUser (OpenStackNet osn , User user, OpenStackClient openStackClient)
    {
        final OpenStackUser res = new OpenStackUser(osn,user,openStackClient);
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

    private OpenStackUser (OpenStackNet osn , User user, OpenStackClient openStackClient)
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
    public Boolean isUserEnable(){return  this.userEnable;}
    public Map<String,String> getUserLinks(){return this.userLinks;}


    @Override
    public String get50CharactersDescription()
    {
        String description = "User: " +
                this.NEWLINE + "ID " + this.getId() +
                this.NEWLINE + "Name " + this.getName() +
                this.NEWLINE + "Domain ID " + this.getDomainId() +
                this.NEWLINE + "Email " + this.getEmail() +
                this.NEWLINE + "Description " + this.getDescription() +
                this.NEWLINE + "Enable " + this.isUserEnable() +
                this.NEWLINE + "Links" + this.NEWLINE;
        for(String key : this.getUserLinks().keySet()) {
        description += key + " " + this.getUserLinks().get(key) + NEWLINE;
        }
        return description;
    }

    public void isUserEnable(Boolean value){

        try{

            this.openStackClient.getClient().identity().users().update(user.toBuilder().enabled(value).build());

        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }
    }
    public void updateUserDescription(JSONObject jsonObject){

        try{
        this.openStackClient.getClient().identity().users().update(this.user.toBuilder().description(jsonObject.getString("Description")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }

    }

    public void updateUserName(JSONObject jsonObject){

        try{
        this.openStackClient.getClient().identity().users().update(this.user.toBuilder().name(jsonObject.getString("Name")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }

    }

    public void updateUserEmail(JSONObject jsonObject){

        try{
        this.openStackClient.getClient().identity().users().update(this.user.toBuilder().email(jsonObject.getString("Email")).build());
        }catch(Exception ex){

            logPanel();
            System.out.println(ex.toString());

        }

    }

    public void deleteUser(){
        this.openStackClient.getClient().identity().users().delete(this.userId);
    }
}


