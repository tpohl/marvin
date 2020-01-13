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

import java.util.List;

import javax.annotation.PostConstruct;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

//This class provides a RESTful API with a /run method that can be triggered through the main app webpage

@Path("/my_robot")
@Component
public class RobotEndpoint {

    @Autowired
    RobotService service;

    // private static final Logger log = LoggerFactory.getLogger(Application.class);

    // This properties contains the uri adn the userkey of the Robot APi that this application is invoking. The
    // value is defined in application.properties.
    // Set it to your specific API
    @Value("${hub.controller.uri}")
    private String hubControllerEndpoint;

    // and 3Scale userkey
    @Value("${3scale.token}")
    private String userKey;

    private RestTemplate restTemplate = new RestTemplate();

    // This method checks if the HubController can be reached
    @GET
    public Object ping() {

        System.out.println("Ping method invoked");
        String response = restTemplate.getForObject(hubControllerEndpoint, String.class);
        return response;
    }

    @GET
    @Path("/actions")
    public List<String> actions() {
        return this.service.actions;
    }

    @GET
    @Path("/nomove")
    public int nomoves() {
        return this.service.noMoveCount;
    }
    @POST
    @Path("/run")
    public Object two() {
        this.service.actions.clear();

             System.out.println("Run method invoked");

        String response = "";

        // Example GET invokation of the Robot API
        // response = restTemplate.getForObject(hubControllerEndpoint +
        // "/power?user_key="+userKey, String.class);

        // Example POST invokation of the Robot API
        //response = this.service.guckmal().toString();
        this.service.driveForward(30);
        response = this.service.stategy2();
        System.out.println(response);
        return response;
    }
    // This method should execute the program steps for the robot. It can be invoked
    // by the main application website
    @POST
    @Path("/run2")
    public Object run() {

        System.out.println("Run method invoked");

        String response = "";

        // Example GET invokation of the Robot API
        // response = restTemplate.getForObject(hubControllerEndpoint +
        // "/power?user_key="+userKey, String.class);

        // Example POST invokation of the Robot API
        //response = this.service.guckmal().toString();
        response = this.service.go();
        System.out.println(response);
        return response;
    }

}
