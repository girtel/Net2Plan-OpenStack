package com.net2plan.gui.plugins.networkDesign.openStack;

import com.net2plan.interfaces.networkDesign.Link;
import org.openstack4j.model.identity.v3.User;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class OpenStackGeneralInformation extends OpenStackNetworkElement {

    private String type;
    private Integer number ;
    private String projectId="";


    static OpenStackGeneralInformation createFromNetPlan (OpenStackNet osn , Link l)
    {
        final OpenStackGeneralInformation frqLink = new OpenStackGeneralInformation(osn, l);
        return frqLink;
    }

    static OpenStackGeneralInformation createFromAddInformation (OpenStackNet osn ,String projectId,String type, Integer number)
    {
        final OpenStackGeneralInformation res = new OpenStackGeneralInformation(osn,null);
        res.type= type;
        res.number= number;
        res.projectId= projectId;
        return res;
    }

    private OpenStackGeneralInformation (OpenStackNet osn , Link npLink)
    {
        super (osn , npLink , (List<OpenStackNetworkElement>) (List<?>) osn.list_osInformation);

    }

    @Override
    public String getId () { return this.projectId; }
    public String getType () { return this.type; }
    public Integer getNumber () { return this.number; }

    OpenStackGeneralInformation getOpenStackGeneralInformation () { return this; }

    @Override
    public String get50CharactersDescription()
    {
        return "User-" + this.getId();
    }

}
