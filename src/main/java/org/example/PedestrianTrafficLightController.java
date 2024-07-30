package org.example;

import java.util.Map;
import java.util.concurrent.Callable;

public class PedestrianTrafficLightController extends Controller implements Callable<Integer> {

    private final TrafficLight ptl;

    public PedestrianTrafficLightController(TrafficLight tl){
        this.ptl = tl;
    }


    @Override
    public Integer call() {
        return toRun(ptl);
    }
}
