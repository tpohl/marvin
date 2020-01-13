package io.openshift.booster.service;

public class Action {
    public static final String turnedRight(int degrees){
        return "I turned "+ degrees+ " degrees to the right.";
    }
    public static final String turnedLeft(int degrees){
        return "I turned "+ degrees+ " degrees to the left.";
    }
    public static final String forward(int cm){
        return "I moved forward  "+ cm+ " cm.";
    }
    public static final String noMove(int cm){
        return "I did not move forward because there is something in the range of  "+ cm+ " cm.";
    }
     public static final String moveOk(int cm){
        return "I can move forward because there is nothing in the range of  "+ cm+ " cm.";
    }

    public static final String decisionForward(Distances d){
        return "I decided to go FORWARD: right " + d.right +" frontRight " + d.frontright + " front "+ d.front + " leftFront " + d.frontleft + " left "+ d.left;
    }

     public static final String decistionLeft(Distances d){
        return "I decided to go LEFT: right " + d.right +" frontRight " + d.frontright + " front "+ d.front + " leftFront " + d.frontleft + " left "+ d.left;
    }
     public static final String decistionRight(Distances d){
        return "I decided to go RIGHT: right " + d.right +" frontRight " + d.frontright + " front "+ d.front + " leftFront " + d.frontleft + " left "+ d.left;
    }
    public static final String decistionLeftFallBack(Distances d){
        return "I decided to go a bit LEFT: right " + d.right +" frontRight " + d.frontright + " front "+ d.front + " leftFront " + d.frontleft + " left "+ d.left;
    }
}
