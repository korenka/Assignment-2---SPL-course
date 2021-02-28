package bgu.spl.mics.application.services;


import bgu.spl.mics.MicroService;
import bgu.spl.mics.application.messages.AttackEvent;
import bgu.spl.mics.application.messages.AttackFinishedEvent;
import bgu.spl.mics.application.messages.TerminationBroadcast;
import bgu.spl.mics.application.passiveObjects.Ewoks;
import bgu.spl.mics.application.passiveObjects.Task;

import javax.xml.crypto.Data;
import java.util.concurrent.TimeUnit;

/**
 * HanSoloMicroservices is in charge of the handling {@link AttackEvent}.
 * This class may not hold references for objects which it is not responsible for:
 * {@link AttackEvent}.
 *
 * You can add private fields and public methods to this class.
 * You MAY change constructor signatures and even add new public constructors.
 */
public class HanSoloMicroservice extends MicroService {

    public HanSoloMicroservice() {
        super("Han");
        initialize();
    }


    @Override
    protected void initialize() {
        Task init = new Task(name, "init", System.currentTimeMillis());
        mb.register(this);
        diary.addTask(init);
        Ewoks ewoks = Ewoks.getInstance();
        subscribeEvent(AttackEvent.class, (attack) ->
        {ewoks.acquire(attack.getSerials()); long duration = TimeUnit.MILLISECONDS.toMillis(attack.getDuration());
            try {
                Task startedAttack = new Task(name, "Started", System.currentTimeMillis());
                diary.addTask(startedAttack);
                Thread.sleep(duration);
            } catch (InterruptedException e) {};
            diary.updateTotalAttacks();
            Task finishedAttack = new Task(name, "Started", System.currentTimeMillis());
            diary.addTask(finishedAttack);
            ewoks.realse(attack.getSerials());
            sendEvent(new AttackFinishedEvent());
        });
        subscribeBroadcast(TerminationBroadcast.class,(bool)->{terminate();});
    }
}
