package org.example;

import lombok.Getter;
import lombok.Setter;

import java.util.*;

public abstract class TrafficLight {

    @Getter
    private final int id;
    @Getter
    @Setter
    private long sleepTime;
    @Getter
    private final Queue<Event> eventQueue;
    @Setter
    private volatile String tLState;
    @Getter
    private int queue;
    private boolean isReady;

    TrafficLight(int id){
        sleepTime = 1;
        tLState = "RED";
        isReady = false;
        this.id = id;
        queue = (int) (Math.random() * 10 + 1);
        eventQueue = new LinkedList<>();
        getTLMap();
    }

    public boolean isReady(){
        return isReady;
    }

    public void setReady(boolean b){
        isReady = b;
    }

    //вытаскиваие сообщения и выполнение смены цвета, если цвета на светофоре и в сообщении не совпадают
    public void executeEvent(){
        if (!getEventQueue().isEmpty()) {
            changeTLState();
        } else {
            System.out.println("поломка " + id);
            getEventQueue().remove();
        }
    }

    //увеличение очереди
    public void incrementQ() {
        this.queue++;
    }

    //получение мапы светофоров по айди
    public Map<Integer, TrafficLight> getTLMap(){
        return Dispatcher.getTLMap();
    }

    //получение светофора по его id
    public TrafficLight findTLById(int id){
        if (id > 42) {
            id = id - 40;
        }
        return getTLMap().get(id);
    }

    //уменьшение очереди
    public void decrementQ() {
        if (queue > 0) {
            queue--;
        }
    }

    //изменение цвета светофора
    abstract void changeTLState();

    //добавление ивента в очередь
    public synchronized void addEventToQueue(Event event){
        if(!getEventQueue().contains(event)) {
            getEventQueue().add(event);
        }
    }

    //метод, который требовался в реализации, но оказался не нужен
    public void sendDelayedEvent(int id, String state, int time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        findTLById(id).addEventToQueue(new Event(this.id, state));
    }

   public synchronized String getTLState(){
        return tLState;
    }
}
