/*
 * Copyright 2016-2017 Red Hat, Inc, and individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.openshift.booster.service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Path("/my_robot")
@Component
public class RobotEndpoint {
    
    //private static final Logger log = LoggerFactory.getLogger(Application.class);

    @Value("${hub.controller.uri}")
    private String hubControllerEndpoint;
    
    private RestTemplate restTemplate = new RestTemplate();
    
    @GET   
    public Object ping() {        
        
        String response = restTemplate.getForObject(edgeControllerEndpoint, String.class);        
        return response;
    }

    @POST
    @Path("/run")
    public Object run() {        
        
        String response = null;

        // Example GET        
        //response = restTemplate.getForObject(edgeControllerEndpoint, String.class);  
        
        // Example POST        
        //HttpEntity<String> request = new HttpEntity<>(new String(""));
        //response = restTemplate.postForObject(edgeControllerEndpoint +"/forward/" + lengthInCm, request, String.class);        
        
        return response;
    }

    
}
