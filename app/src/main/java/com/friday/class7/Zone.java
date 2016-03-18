package com.friday.class7;

/**
 * Created by SAM on 17/3/2016.
 */
public class Zone {

    private String name = "";
    private int maxCar = 0;
    private int curCar = 0;

    public Zone(String name, int maxCar, int curCar) {
        this.name = name;
        this.maxCar = maxCar;
        this.curCar = curCar;
    }


    public String getName() {
        return name;
    }

    public int getMaxCar() {
        return maxCar;
    }

    public int getCurCar() {
        return curCar;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMaxCar(int maxCar) {
        this.maxCar = maxCar;
    }

    public void setCurCar(int curCar) {
        this.curCar = curCar;
    }
}
