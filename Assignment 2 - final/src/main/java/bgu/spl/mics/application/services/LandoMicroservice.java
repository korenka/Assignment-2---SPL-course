package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Task;

import java.util.concurrent.TimeUnit;

/**
 * LandoMicroservice
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class LandoMicroservice  extends MicroService {
    private long duration;
    public LandoMicroservice(long duration) {
        super("Lando");
        this.duration= TimeUnit.MILLISECONDS.toMillis(duration);
        initialize();
    }

    @Override
    protected void initialize() {
        Task init = new Task(name, "init", System.currentTimeMillis());
        mb.register(this);
        diary.addTask(init);
        Diary diary = Diary.getInstance();
        subscribeBroadcast(TerminationBroadcast.class,(bool)->{terminate();});
       subscribeEvent(BombDestroyerEvent.class,(duration)->{try {
               Thread.sleep(this.duration);
           } catch (InterruptedException e) {}
           sendBroadcast(new TerminationBroadcast());
        });

    }
}
