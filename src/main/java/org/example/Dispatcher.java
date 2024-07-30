package org.example;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Dispatcher {

    static Map<Integer, TrafficLight> tLMap;

    public static synchronized Map<Integer, TrafficLight> getTLMap(){
        return tLMap;
    }

    public static void main(String[] args) {
        //создание всех светофоров
        tLMap = new HashMap<>();
        try (ExecutorService service = Executors.newFixedThreadPool(12)) {
            int queue = 1;
            for (int i = 1; i <= 4; i++) {
                tLMap.put(i * 10, new CarTrafficLight(i * 10));
                for (int j = 1; j <= 2; j++) {
                    tLMap.put(i * 10 + j, new PedestrianTrafficLight(i * 10 + j));
                }
            }

            //создание контроллеров и их использование до опустошения очередей
            while (queue > 0) {
                List<Future<Integer>> futures = new ArrayList<>();
                for (int i = 1; i <= 4; i++){
                    futures.add(service.submit(new CarTrafficLightController(tLMap.get(i * 10))));
                    for (int j = 1; j <= 2; j++){
                        futures.add(service.submit(new PedestrianTrafficLightController(tLMap.get(i * 10 + j))));
                    }
                }
                for (Future<Integer> future : futures){
                    try {
                        future.get();
                    } catch (InterruptedException | ExecutionException e) {
                        e.printStackTrace();
                    }
                }
                queue = futures.get(11).get();
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
