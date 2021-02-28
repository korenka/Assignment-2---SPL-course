package bgu.spl.mics.application.services;

import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.BombDestroyerEvent;
import bgu.spl.mics.application.messages.DeactivationEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Diary;
import bgu.spl.mics.application.passiveObjects.Task;

import java.util.concurrent.TimeUnit;

/**
 * R2D2Microservices is in charge of the handling {@link DeactivationEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link DeactivationEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class R2D2Microservice extends MicroService {
    private long duration;
    public R2D2Microservice(long duration) {
        super("R2D2");
        this.duration= TimeUnit.MILLISECONDS.toMillis(duration);
        initialize();
    }

    @Override
    protected void initialize() {
        Task init = new Task(name, "init", System.currentTimeMillis());
        mb.register(this);
        diary.addTask(init);
        subscribeEvent(DeactivationEvent.class,(duration)->{try {
            Thread.sleep(this.duration);
            Task deactivatedShield = new Task(name, "Deactivated", System.currentTimeMillis());
            diary.addTask(deactivatedShield);
        } catch (InterruptedException e) {}
        sendEvent(new BombDestroyerEvent<>());
        });
        subscribeBroadcast(TerminationBroadcast.class,(bool)->{terminate();});
    }
}
