package org.example;

public class PedestrianTrafficLight extends TrafficLight{

    private final String[] pedTLState;

    public PedestrianTrafficLight(int id){
        super(id);
        pedTLState = new String[]{"RED", "GREEN"};
    }

    //изменение цвета светофора
    @Override
    public void changeTLState(){
        try {
            if (getTLState().equals(pedTLState[0])) {
                Thread.sleep(1);
                setTLState(pedTLState[1]);
                decrementQ();
            } else if (getTLState().equals(pedTLState[1])) {
                Thread.sleep(1);
                setTLState(pedTLState[0]);
            }
        } catch (InterruptedException e){
            e.printStackTrace();
        }
    }
}
