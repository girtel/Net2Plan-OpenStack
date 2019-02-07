package com.net2plan.gui.plugins.networkDesign.openStack.compute;

import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackClient;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNet;
import com.net2plan.gui.plugins.networkDesign.openStack.OpenStackNetworkElement;
import com.net2plan.gui.plugins.networkDesign.openStack.identity.OpenStackUser;
import org.json.JSONObject;
import org.openstack4j.model.compute.IPProtocol;
import org.openstack4j.model.compute.SecGroupExtension;
import org.openstack4j.model.identity.v3.Domain;
import org.openstack4j.model.identity.v3.User;

import java.util.List;
import java.util.Map;

public class OpenStackRule  extends OpenStackNetworkElement
{

    private String ruleId;
    private String ruleName ;
    private String ruleParentGroupId ;
    private SecGroupExtension.Rule.Group ruleGroupId ;
    private IPProtocol ruleIPProtocol ;
    private SecGroupExtension.Rule.IpRange ruleRangeIP;
    private int ruleFromPort;
    private SecGroupExtension.Rule rule ;
    private int ruleToPort;


    public static OpenStackRule createFromAddRule (OpenStackNet osn , SecGroupExtension.Rule rule, OpenStackClient openStackClient)
    {
        final OpenStackRule res = new OpenStackRule(osn,rule,openStackClient);
        res.ruleId= rule.getId();
        res.ruleName=rule.getName();
        res.ruleParentGroupId = rule.getParentGroupId();
        res.ruleGroupId=rule.getGroup();
        res.ruleIPProtocol=rule.getIPProtocol();
        res.ruleRangeIP=rule.getRange();
        res.ruleFromPort = rule.getFromPort();
        res.ruleToPort = rule.getToPort();

        return res;
    }

    private OpenStackRule (OpenStackNet osn , SecGroupExtension.Rule rule, OpenStackClient openStackClient)
    {
        super (osn , null , (List<OpenStackNetworkElement>) (List<?>) openStackClient.openStackRules,openStackClient);
        this.rule = rule;


    }

    @Override
    public String getId () { return this.ruleId; }
    public String getName () { return this.ruleName; }
    public String getRuleParentGroupId() {return this.ruleParentGroupId;}
    public SecGroupExtension.Rule.Group getRuleGroupId () { return this.ruleGroupId; }
    public IPProtocol getRuleIPProtocol () { return this.ruleIPProtocol; }
    public SecGroupExtension.Rule.IpRange getRuleRangeIP () { return this.ruleRangeIP; }
    public int getRuleFromPort () { return this.ruleFromPort; }
    public int getRuleToPort(){return  this.ruleToPort;}


    @Override
    public String get50CharactersDescription()
    {
        String description = "Rule:";
        return description;
    }

}


