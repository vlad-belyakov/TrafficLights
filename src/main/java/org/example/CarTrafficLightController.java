package org.example;

import java.util.concurrent.Callable;

public class CarTrafficLightController extends Controller implements Callable<Integer> {

    private final TrafficLight ctl;

    public CarTrafficLightController(TrafficLight tl){
        this.ctl = tl;
    }

    @Override
    public Integer call(){
        return toRun(ctl);
    }
}
