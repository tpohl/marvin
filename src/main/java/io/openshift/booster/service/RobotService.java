package io.openshift.booster.service;

import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static io.openshift.booster.service.Action.*;

@Component
public class RobotService {

    private static final Logger log = LoggerFactory.getLogger(RobotService.class);

    @Value("${hub.controller.uri}")
    private String hubControllerEndpoint;

    // and 3Scale userkey
    @Value("${3scale.token}")
    private String userKey;

    private RestTemplate restTemplate = new RestTemplate();

    public Distances guckmal() {
        // Example POST invokation of the Robot API
        Distances d = new Distances();
        act("/servo/0");
        d.fullright = distance();
        act("/servo/20");
        d.right = distance();
        act("/servo/55");
        d.frontright = distance();
        act("/servo/90");
        d.front = distance();
        act("/servo/125");
        d.frontleft = distance();
        act("/servo/160");
        d.left = distance();
        return d;

    }

    public void party() {


        this.act("/right/180");

        this.act("/left/180");

        this.act("/right/180");

        this.act("/left/180");

    }
    public final static Integer LIMIT_FREE = 200;
    public final static Integer LIMIT_GO = 300;
    public final static Integer LIMIT_WALL = 150;

    private int checkRight = 0;
    public int noMoveCount = 0;

    private int lastDescision;

    public List<String> actions = new LinkedList<>();

    private boolean checkRight(Distances d){
        if (d.right > LIMIT_FREE){
            checkRight ++;
            if ( checkRight > 3) {
                this.checkRight = 0;
                return true;
            }
        }
        else {
            checkRight = 0;
        }
        return false;
    }

    public int bestWay(Distances d){

        if (d.right < LIMIT_FREE && d.frontright < (LIMIT_FREE+200) && d.front > LIMIT_GO && d.frontleft > LIMIT_FREE && d.frontright > LIMIT_FREE){
            this.actions.add(decisionForward(d));
            return 0;
        } else if (d.right < LIMIT_FREE && d.frontright < (LIMIT_FREE+200) && d.frontleft > (d.frontright +2) && d.frontleft > (d.right +2)){
            this.actions.add(decistionLeft(d));
            return -22;
        } else if (d.fullright > LIMIT_WALL) {
             this.actions.add(decistionRight(d));
            return d.frontright > d.right ? 44 : 22;
        } else {
            this.actions.add(decistionLeftFallBack(d));
            return -11;
        }

    }

    public int canGo(int limit){
        int minDistance = Integer.MAX_VALUE;
        for (int i = 50; i <= 140 ; i = i+18){
            act("/servo/"+i);
            float factor = Math.abs(90.0f-i) / 4.0f;
            int distance = distance() - Math.round(factor);

            minDistance = Math.min(minDistance, distance);
            if ( distance < limit){
                this.actions.add(noMove(limit / 10));
                return -1;
            }
        }
        this.actions.add(moveOk(limit / 10));
        return minDistance;
    }

    public String stategy2(){
        final Distances d = this.guckmal();
        if ( d.right > 400 && d.left > 400 && d.front > 400){
            this.driveForward(30);
            this.party();
            this.actions.add("Party");
            return "OK";
        }
        String protocol = "";

        if (checkRight(d)){
            this.actions.add("Forced right turn");
            this.turnRight(55);

        } else {

            int bestWay = this.bestWay(d);
            if (bestWay == 0){
                protocol = protocol + "F";
                int canGo = canGo(60);
                if (canGo > 0){
                  this.driveForward(Math.min(10, Math.max(1, (canGo / 10) - 2)));
                  noMoveCount = 0;
                } else{
                    System.out.println("No Move " + noMoveCount);
                    if (noMoveCount++ > 5){
                        this.actions.add("U-Turn");
                        this.turnRight(180);
                    }
                }
            } else {
                if (bestWay < 0){
                protocol = protocol + "L";
                this.turnLeft(-1 * bestWay);
                } else {
                    protocol = protocol + "R";
                    this.turnRight(bestWay);
                }


                // Distances d2 = this.guckmal();
                int canGo = canGo(60);
                if (canGo > 0){
                    this.driveForward(Math.min(10, Math.max(1, (canGo / 10) - 2)));
                    noMoveCount = 0;

                } else {
                System.out.println("No Move " + noMoveCount);
                    if (noMoveCount++ > 5){
                        System.out.println("No Move " + noMoveCount);
                        this.turnRight(180);
                    }
                }
            }
        }

        return protocol + stategy2();

    }

 public String go(){
     this.driveForward(30);
     return this.go(0, 0);
 }

    public String go(int wasFreeCount, int iteration){
        if (iteration > 500) return "NOK";
        String protocol = "";
        Distances d = guckmal();
        boolean isFree = d.right > LIMIT_FREE;
        boolean wasFree = wasFreeCount > 3;
        if (wasFree && d.right > LIMIT_FREE && d.left > LIMIT_FREE && d.front > LIMIT_FREE){
            this.party();
            return "OK";
        }

        else if (isFree){
            if (wasFree){
                protocol = protocol + "R";
                this.turnRight();
                wasFreeCount = 0;
            }

        }
        if (d.front > LIMIT_WALL){
        this.driveForward();
        } else {

        }
        protocol = protocol + "F";
        System.out.println("PROTOCOL :"+protocol);
        return protocol + this.go(wasFreeCount+1, iteration + 1);

    }

    public void driveForward() {
        this.driveForward(10);
    }
        public void driveForward(int number) {
        System.out.println("Vorwärts immer, rückwärts nimmer " +  number);
        this.act("/forward/" + number);
        this.actions.add(forward(number));
    }

    public void turnRight() {
        this.turnRight(95);
    }

        public void turnRight(int degrees) {
        System.out.println("Es geht nach Rechts. Ich drehe mal um "+ degrees);
        this.act("/right/"+degrees);
        this.actions.add(turnedRight(degrees));
    }

    public void turnLeft(int degrees) {
        System.out.println("Es geht nach Links. Ich drehe mal um "+ degrees);
        this.act("/left/"+degrees);
        this.actions.add(turnedLeft(degrees));
    }

    private Integer distance(){
        return Integer.parseInt( measure("/distance"));
    }

    private String measure(String what) {

        return restTemplate.getForObject(hubControllerEndpoint + what + "?user_key=" + userKey, String.class);
    }

    private String act(String what) {
        MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>();
        paramMap.add("user_key", userKey);
        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<MultiValueMap<String, String>>(paramMap,
                new LinkedMultiValueMap<String, String>());
        String url = hubControllerEndpoint + what;
        //System.out.println("URL " + url + "User Key " + userKey);
        return restTemplate.postForObject(url, request, String.class);
    }
}
