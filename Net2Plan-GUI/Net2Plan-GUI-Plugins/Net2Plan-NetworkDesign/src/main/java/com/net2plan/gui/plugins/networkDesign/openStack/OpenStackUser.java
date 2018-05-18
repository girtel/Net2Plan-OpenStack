package com.net2plan.gui.plugins.networkDesign.openStack;

import com.net2plan.interfaces.networkDesign.Link;
import org.openstack4j.api.Builders;
import org.openstack4j.model.identity.v3.User;

import java.util.List;

/**
 *
 * @author Manuel
 */
public class OpenStackUser extends OpenStackNetworkElement
{

    private String userId = "";
    private String userName = "";
    private String userDomainId = "";
    private String userEmail = "";
    private String userDescription = "";
    private User user ;


    static OpenStackUser createFromAddUser (OpenStackNet osn , User user, String userId, String userName, String userDomainId, String userEmail, String userDescription)
    {
        final OpenStackUser res = new OpenStackUser(osn,null,user);
        res.userId= userId;
        res.userName=userName;
        res.userDomainId=userDomainId;
        res.userEmail=userEmail;
        res.userDescription=userDescription;

        return res;
    }

    private OpenStackUser (OpenStackNet osn , Link npLink,User user)
    {
        super (osn , npLink , (List<OpenStackNetworkElement>) (List<?>) osn.list_osUsers);
        this.user =user;

    }

    @Override
    public String getId () { return this.userId; }
    public String getName () { return this.userName; }
    public String getDomainId () { return this.userDomainId; }
    public String getEmail () { return this.userEmail; }
    public String getDescription () { return this.userDescription; }
    OpenStackUser getOpenStackUser () { return this; }

    @Override
    public String get50CharactersDescription()
    {
        return "User-" + this.getId();
    }

    public void updateUserDescription(String userDescription){

        this.osn.getOs().identity().users().update(this.user.toBuilder().description(userDescription).build());


    }

    public void updateUserName(String userName){

        this.osn.getOs().identity().users().update(this.user.toBuilder().name(userName).build());


    }

    public void updateUserEmail(String userEmail){

        this.osn.getOs().identity().users().update(this.user.toBuilder().email(userEmail).build());


    }

    public void createNewUser (String userName, String userDescription,String password,String email){

        this.osn.getOs().identity().users().create(Builders.user()
                .name(userName)
                .description(userDescription)
                .password(password)
                .email(email)
                .domainId(this.osn.getOs().getToken().getUser().getDomainId())
                .build());
    }

    public void deleteUser(){
        this.osn.getOs().identity().users().delete(this.userId);
    }
}

