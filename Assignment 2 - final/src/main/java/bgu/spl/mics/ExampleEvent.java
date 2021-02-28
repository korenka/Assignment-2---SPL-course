package bgu.spl.mics;


public class ExampleEvent implements Event<Boolean> {
    private boolean handled;
    public  ExampleEvent(boolean handled) {this.handled = handled;}
    public boolean getEventName() {return handled;}
}
