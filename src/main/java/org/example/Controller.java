package org.example;

import lombok.Getter;

import java.util.*;

public abstract class Controller {

    @Getter
    private int maxQId;

    private TrafficLight tl;

    //получение id светофора с максимальной очередью
    public void setMaxQId(){
        maxQId = getTLMap()
                .values()
                .stream()
                .filter(n -> n.getQueue() > 0)
                .max(Comparator.comparingInt(TrafficLight::getQueue))
                .get()
                .getId();
    }

    //нахождение очереди по id
    public int findQById(int id){
        return getTLById(id).getQueue();
    }

    //сравнение маскимальной очереди со средним значением по очереди, умноженное на i
    public boolean isMaxQBiggerThanMedian(double i){
        return findQById(maxQId) > countMedianQNumber() * i;
    }

    //нахождение пешеходного светофора с максимальной очередью из двух пешеходных светофоров на одной дороге
    public TrafficLight findMaxPedById(int id){
        if (id > 42){
            id = id - 40;
        }
        if (findQById(id + 1) > findQById(id + 2)){
            return getTLById(id + 1);
        } else {
            return getTLById(id + 2);
        }
    }

    //нахождение очереди пешеходного светофора с максимальной очередью из двух пешеходных светофоров на одной дороге
    public int findMaxPedQById(int id){
        return findMaxPedById(id).getQueue();
    }

    //нахождение суммы максимальных очередей пещеходных светофоров
    public int findMaxPedQSum(int... id){
        int result = 0;
        for (int i : id){
            result += findMaxPedQById(i);
        }
        return result;
    }

    //получение мапы светофоров с их id
    public Map<Integer, TrafficLight> getTLMap(){
        return Dispatcher.getTLMap();
    }

    //получение светофора по его id
    public TrafficLight getTLById(int id){
        if (id > 42) {
            id = id - 40;
        }
        return getTLMap().get(id);
    }

    //нахождение среднего значения очереди
    public int countMedianQNumber(){
        return getTLMap()
                .values()
                .stream()
                .mapToInt(TrafficLight::getQueue)
                .sum() / 12;
    }

    //блокировка (отправка красного сигнала светофора) всех светофоров по их причастности к автомобильным или пешеходным
    public void blockTL(boolean isCar){
        if (isCar) {
            getTLMap()
                    .values()
                    .stream()
                    .filter(n -> n.getId() % 10 == 0)
                    .forEach(n -> n.addEventToQueue(new Event(tl.getId(), "RED")));
        } else {
            getTLMap()
                    .values()
                    .stream()
                    .filter(n -> n.getId() % 10 != 0)
                    .forEach(n -> n.addEventToQueue(new Event(tl.getId(), "RED")));
        }
    }

    //разблокировка (отправка зеленого сигнала светофора) всех светофоров по их причастности к автомобильным или пешеходным
    //если надо разблокировать пешеходные светофоры, то блокирует светофоры с наименьшей очередью и наоборот
    public void unblockTL(boolean isCar){
        if (isCar) {
            getTLMap()
                    .values()
                    .stream()
                    .filter(n -> n.getId() % 10 == 0)
                    .forEach(n -> n.addEventToQueue(new Event(tl.getId(), "GREEN")));
        } else {
            for (int i = 1; i <= 4; i++) {
                if (findQById(i * 10 + 1) < findQById(i * 10 + 2)) {
                    getTLById(i * 10 + 1).addEventToQueue(new Event(tl.getId(), "RED"));
                    getTLById(i * 10 + 2).addEventToQueue(new Event(tl.getId(), "GREEN"));
                } else {
                    getTLById(i * 10 + 2).addEventToQueue(new Event(tl.getId(), "RED"));
                    getTLById(i * 10 + 1).addEventToQueue(new Event(tl.getId(), "GREEN"));
                }
            }
        }
    }

    //округление числа
    public int round(int id){
        return id / 10 * 10;
    }

    //сравнение значения очереди с другими значениями очередей
    public boolean isBigger(int i, int... j) {
        boolean check = true;
        for (int k : j) {
            if ((i < k)) {
                check = false;
                break;
            }
        }
        return check;
    }

    //ожидание потока, пока все потоки не закончат свою работу
    public void busySleep(){
        while (!getTLMap().values().stream().allMatch(TrafficLight::isReady)){
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //отправление всем светофорам сообщения
    public void sendEventsToAll(Integer... ids){
        for (int id = 0; id < ids.length; id++){
            if (ids[id] > 42) {
                ids[id] = ids[id] - 40;
            }
        }
        ArrayList<Integer> list = new ArrayList<>(List.of(ids));
        for (Integer tLId : getTLMap().keySet()) {
            if (!list.contains(tLId)) {
                if (tl.getId() == tLId){
                    tl.addEventToQueue(new Event(tl.getId(), "RED"));
                } else {
                    getTLById(tLId).addEventToQueue(new Event(tl.getId(), "RED"));
                }
            } else {
                if (tl.getId() == tLId){
                    tl.addEventToQueue(new Event(tl.getId(), "GREEN"));
                } else {
                    getTLById(tLId).addEventToQueue(new Event(tl.getId(), "GREEN"));
                }
            }
        }
    }

    //запуск алгоритма по уменьшению очередей
    public Integer toRun(TrafficLight tl){
        this.tl = tl;
        String currentTLState = tl.getTLState();
        if (tl.getQueue() > 0) {
            //инициализация всех в будущем нужных переменных
            tl.setReady(false);
            setMaxQId();
            int leftRoadId = round(maxQId) + 10;
            int forwardRoadId = round(maxQId) + 20;
            int rightRoadId = round(maxQId) + 30;
            TrafficLight maxLeftPed = findMaxPedById(leftRoadId);
            if (isMaxQBiggerThanMedian(1.5)) {
                if (maxQId % 10 == 0) {
                    if (maxLeftPed.getQueue() < findQById(forwardRoadId)) {
                        sendEventsToAll(maxQId, forwardRoadId);
                    } else {
                        sendEventsToAll(maxLeftPed.getId(), maxQId);
                    }
                } else {
                    if ((findMaxPedQSum(forwardRoadId, leftRoadId, rightRoadId)) / 3
                            > findQById(rightRoadId)) {
                        blockTL(true);
                        unblockTL(false);
                    } else {
                        sendEventsToAll(rightRoadId, maxQId);
                    }
                }
            } else {
                //инициализация переменных для этого цикла
                int firstCase = findQById(10) + findQById(30);
                int secondCase = findQById(20) + findQById(40);
                int thirdCase = findMaxPedQSum(10, 20, 30, 40);
                int fourthCase = maxQId;
                Integer[] ids;
                if (maxQId % 10 == 0) {
                    if (maxLeftPed.getQueue() < findQById(forwardRoadId)) {
                        fourthCase += findQById(forwardRoadId);
                        ids = new Integer[]{maxQId, forwardRoadId};
                    } else {
                        fourthCase += maxLeftPed.getQueue();
                        ids = new Integer[]{maxQId, maxLeftPed.getId()};
                    }
                } else {
                    if ((findMaxPedQSum(leftRoadId, forwardRoadId, rightRoadId) / 3) > findQById(rightRoadId)) {
                        fourthCase += findMaxPedQSum(leftRoadId, forwardRoadId, rightRoadId);
                        ids = new Integer[]{maxQId, findMaxPedById(forwardRoadId).getId()
                                , findMaxPedById(rightRoadId).getId(), findMaxPedById(leftRoadId).getId()};
                    } else {
                        fourthCase += findQById(rightRoadId);
                        ids = new Integer[]{maxQId, rightRoadId};
                    }
                }

                if (isBigger(firstCase, secondCase, thirdCase, fourthCase)) {
                    sendEventsToAll(10, 30);
                } else if (isBigger(secondCase, firstCase, thirdCase, fourthCase)) {
                    sendEventsToAll(20, 40);
                } else if (isBigger(thirdCase, firstCase, secondCase, fourthCase)) {
                    ids = new Integer[4];
                    for (int i = 0; i < ids.length; i++) {
                        ids[i] = findMaxPedById((i + 1) * 10).getId();
                    }
                    sendEventsToAll(ids);
                } else {
                    sendEventsToAll(ids);
                }
            }
            tl.setReady(true);
            busySleep();
            tl.executeEvent();
        } else {
            tl.setReady(true);
            tl.getEventQueue().clear();
            tl.setTLState("RED");
        }
        busySleep();
        return getTLMap().values().stream().mapToInt(TrafficLight::getQueue).sum();
    }
}
