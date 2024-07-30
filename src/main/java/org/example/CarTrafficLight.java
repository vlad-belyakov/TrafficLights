package org.example;

public class CarTrafficLight extends TrafficLight{

    private final String[] carTLState;

    CarTrafficLight(int id){
        super(id);
        carTLState = new String[]{"RED", "YELLOW", "GREEN"};
    }

    //изменение цвета светофора
    @Override
    public void changeTLState(){
        try {
            if (getTLState().equals(carTLState[0])) {
                setTLState(carTLState[1]);
                Thread.sleep(1);
                setTLState(carTLState[2]);
                decrementQ();
            } else if (getTLState().equals(carTLState[2])) {
                setTLState(carTLState[1]);
                Thread.sleep(1);
                setTLState(carTLState[0]);
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}

