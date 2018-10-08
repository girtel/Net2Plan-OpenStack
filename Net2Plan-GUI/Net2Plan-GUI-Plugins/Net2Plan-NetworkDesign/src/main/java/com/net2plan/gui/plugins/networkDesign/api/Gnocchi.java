package com.net2plan.gui.plugins.networkDesign.api;

import com.net2plan.gui.plugins.networkDesign.openStack.telemetry.OpenStackGnocchiMeasure;
import org.json.JSONArray;
import org.json.JSONObject;
import org.openstack4j.api.OSClient;
import org.openstack4j.model.telemetry.Meter;
import org.openstack4j.model.telemetry.Resource;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class Gnocchi extends Api {

    private final OSClient.OSClientV3 osClientV3;
    public final String url;

    public enum GnocchiOption {
        METRIC("metric/"),
        MEASURE("/measures"),
        RESOURCE("resource/generic")
        ;

        private final String tabName;
        private GnocchiOption(String tabName)
        {
            this.tabName = tabName;
        }
    }

    public Gnocchi(String url, OSClient.OSClientV3 osClientV3){
        this.url=url;
        this.osClientV3=osClientV3;
    }

    public List<Meter> metersList(){

        final List<Meter> meters = new ArrayList<>();

        Object responseObject = this.Get(url+GnocchiOption.METRIC.tabName,osClientV3.getToken().getId());

        JSONArray jsonArray = new JSONArray((String) responseObject.toString());

        for(Object object: jsonArray){

            JSONObject jsonObject = (JSONObject) object;

            Meter meter = new Meter() {
                @Override
                public String getId() {
                    return jsonObject.getString("id");
                }

                @Override
                public String getName() {
                    return jsonObject.getString("name");
                }

                @Override
                public String getResourceId() {
                    return jsonObject.getString("resource_id");
                }

                @Override
                public String getProjectId() {
                    return jsonObject.getString("created_by_project_id");
                }

                @Override
                public Type getType() {
                    return Type.GAUGE;
                }

                @Override
                public String getUnit() {
                    return jsonObject.get("unit").toString();
                }

                @Override
                public String getUserId() {
                    return jsonObject.getString("created_by_user_id");
                }
            };

            meters.add(meter);
        }
        return meters;
    }
    public List<Resource> resourcesList(){

        final List<Resource> resources = new ArrayList<>();

        Object responseObject = this.Get(url+GnocchiOption.RESOURCE.tabName,osClientV3.getToken().getId());

        JSONArray jsonArray = new JSONArray((String) responseObject.toString());

        for(Object object: jsonArray){

            JSONObject jsonObject = (JSONObject) object;

           // System.out.println(jsonObject);
            Resource resource = new Resource() {
                @Override
                public String getId() {
                    return jsonObject.get("id").toString();
                }

                @Override
                public String getUserId() {
                    return jsonObject.get("created_by_user_id").toString();
                }

                @Override
                public String getSource() {
                    return jsonObject.get("type").toString();
                }

                @Override
                public Date getFirstSampleTimestamp() {
                    return null;
                }

                @Override
                public Date getLastSampleTimestamp() {
                    return null;
                }

                @Override
                public String getProjectId() {
                    return jsonObject.get("created_by_project_id").toString();
                }

                @Override
                public Map<String, Object> getMeataData() {
                    return null;
                }
            };

            resources.add(resource);
        }
        return resources;
    }
    public JSONArray measuresList(String metricId){

        //System.out.println ("Meausres for "  + metricId + " token " + osClientV3.getToken());
        final List<OpenStackGnocchiMeasure> gnocchiMeasures = new ArrayList<>();

        Object responseObject = this.Get(url+GnocchiOption.METRIC.tabName+metricId+GnocchiOption.MEASURE.tabName,osClientV3.getToken().getId());

        JSONArray jsonArray = new JSONArray((String) responseObject.toString());

        /*for(Object object: jsonArray){

            JSONObject jsonObject = (JSONObject) object;

            System.out.println(jsonObject);


            gnocchiMeasures.add(new OpenStackGnocchiMeasure(jsonObject.get("timestamp").toString(),jsonObject.get("value").toString()));
        }*/
        return jsonArray;
    }
}
