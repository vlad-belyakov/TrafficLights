package org.example;

public record Event(int sourceId, String state) {


    //переопределенный equals и hashCode для сравнения объектов класса Event
    @Override
    public int hashCode(){
        return state().hashCode();
    }

    @Override
    public boolean equals(Object e){
        if (this == e) return true;
        if (e == null || getClass() != e.getClass()) return false;
        Event event = (Event) e;
        return state.equals(event.state());
    }
}
