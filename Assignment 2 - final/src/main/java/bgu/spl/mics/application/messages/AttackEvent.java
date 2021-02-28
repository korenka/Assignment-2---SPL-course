package bgu.spl.mics.application.messages;
import bgu.spl.mics.Event;

import java.util.Comparator;
import java.util.List;

public class AttackEvent implements Event<Boolean> {
    final List<Integer> serials;
    final int duration;

    public AttackEvent(List<Integer> serialNumbers, int duration) {
        serialNumbers.sort(new Comparator<Integer>(){
            public int compare(Integer o1, Integer o2) {
                return o1 - o2;}});
        serials=serialNumbers;
        this.duration = duration;
    }

    public List<Integer> getSerials() {
        return serials;
    }

    public int getDuration() {
        return duration;
    }
}