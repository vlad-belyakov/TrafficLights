/*
package org.example;

import java.util.HashMap;
import java.util.Map;

public class TrafficLightSystem {

    private final Map<Integer, TrafficLight> carTrafficLightMap;
    private final Map<Integer, TrafficLight> pedestrianTrafficLightMap;

    private int carsOverallNumber;
    private int pedestriansOverallNumber;

    public TrafficLightSystem(){
        carTrafficLightMap = new HashMap<>();
        pedestrianTrafficLightMap = new HashMap<>();
        for (int i = 1; i <= 4; i++){
            carTrafficLightMap.put(i * 10, new CarTrafficLight(i * 10));
            for (int j = 1; j <= 2; j++){
                pedestrianTrafficLightMap.put(i * 10 + j, new PedestrianTrafficLight(i * 10 + j));
            }
        }
        getOverallNumber(carTrafficLightMap, pedestrianTrafficLightMap);
    }

    public void getOverallNumber(Map<Integer, TrafficLight> carTrafficLightMap, Map<Integer, TrafficLight> pedestrianTrafficLightMap) {
        for (TrafficLight trafficLight : carTrafficLightMap.values()) {
            carsOverallNumber += trafficLight.getQueue();
        }
        for (TrafficLight trafficLight : pedestrianTrafficLightMap.values()) {
            pedestriansOverallNumber += trafficLight.getQueue();
        }
    }


    public void runSystem(){
        while (carsOverallNumber + pedestriansOverallNumber > 0){
            if (carsOverallNumber >= pedestriansOverallNumber && carsOverallNumber > 0){
                for (TrafficLight trafficLight : carTrafficLightMap.values()){
                    carsOverallNumber = trafficLight.
                            changeTrafficLightState().decrementQueue(carsOverallNumber);
                }
            } else if (carsOverallNumber < pedestriansOverallNumber && pedestriansOverallNumber > 0){
                for (TrafficLight trafficLight : pedestrianTrafficLightMap.values()){
                    pedestriansOverallNumber = trafficLight.
                            changeTrafficLightState().decrementQueue(pedestriansOverallNumber);
                }
            }
        }
    }

}
*/
