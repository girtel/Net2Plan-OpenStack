package com.net2plan.gui.plugins.networkDesign.openStack.telemetry;


import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import org.openstack4j.model.telemetry.Meter;

import java.util.List;

public class OpenStackGnocchiMeasure extends OpenStackNetworkElement
{

    private String timestamp;
    private String value ;
    private String id ;
    private String granularity ;


    public static OpenStackGnocchiMeasure createFromAddMeasure (OpenStackNet osn ,String timestamp,String granularity, String value, OpenStackClient openStackClient)
    {
        final OpenStackGnocchiMeasure res = new OpenStackGnocchiMeasure(osn,timestamp,granularity,value,openStackClient);
        res.id= timestamp + value;
        res.timestamp= timestamp;
        res.value=value;
        res.granularity = granularity;


        return res;
    }

    private OpenStackGnocchiMeasure (OpenStackNet osn , String timestamp,String granularity, String value,OpenStackClient openStackClient)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackMeasures,openStackClient);

    }

    @Override
    public String getId () { return this.id; }
    public String getTimestamp () { return this.timestamp; }
    public String getValue() {return this.value;}
    public String getGranularity() {return this.granularity;}


    @Override
    public String get50CharactersDescription()
    {
    /*    String description = "User: " +
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
        return description;*/
        return "Measure";
    }


}


