package com.net2plan.gui.plugins.networkDesign.openStack.orchestration;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;

import java.util.List;
import java.util.Map;

public class OpenStackTemplate extends OpenStackNetworkElement
{

    private String stackId;
    private String stackName ;
    private Map<String,Object> template ;


    public static OpenStackTemplate createFromAddTemplate (OpenStackNet osn , Map<String,Object> template )
    {
        final OpenStackTemplate res = new OpenStackTemplate(osn,template);
        res.stackId= template.toString();
        res.stackName=template.toString();


        return res;
    }

    private OpenStackTemplate (OpenStackNet osn , Map<String,Object> template )
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) osn.openStackTemplates);
        this.template = template;


    }

    @Override
    public String getId () { return this.stackId; }
    public String getStackName () { return this.stackName; }



    @Override
    public String get50CharactersDescription()
    {
        return "Template: " + this.getId();
    }


}

