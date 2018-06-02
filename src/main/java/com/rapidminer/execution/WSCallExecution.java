package com.rapidminer.execution;

import org.jboss.resteasy.client.jaxrs.ResteasyClient;
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

public class WSCallExecution implements Function<Map<String, Map<String, List<Double>>>, Map<String, Map<String, Double>>> {

    private final String wsUrl;

    public WSCallExecution(String wsUrl) {
        this.wsUrl = wsUrl;
    }

    @Override
    public Map<String, Map<String, Double>> apply(Map<String, Map<String, List<Double>>> stringListMap) {
        ResteasyClient client = new ResteasyClientBuilder().build();
        Response response = client.target(wsUrl).request().post(Entity.entity(stringListMap, MediaType.APPLICATION_JSON_TYPE));

        return response.readEntity((new GenericType<Map<String, Map<String, Double>>>() {}));
    }
}
