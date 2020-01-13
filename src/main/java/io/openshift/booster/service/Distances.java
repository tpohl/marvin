package io.openshift.booster.service;

class Distances {
    public Integer front;
    public Integer frontleft;
    public Integer left;
    public Integer frontright;
    public Integer right;

    public Integer fullright;
    @Override
    public String toString() {
        return "Links " + left + " front " + front + " right " + right;
    }
}
