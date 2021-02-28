package bgu.spl.mics;

import bgu.spl.mics.application.services.C3POMicroservice;
import bgu.spl.mics.application.services.HanSoloMicroservice;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/*
NOTE:
we only wrot a test for sentEvent method and not subscribeEvent method,
since the way we see to test subscribeEvent is to call the sentEvent method and check if the subscribed members got the message.
this is exactly the way we test the sentEvent method ,which causes duplication in the code.
same goes for sendBrodcast and subscribeBroadcast.
 */
class MessageBusImplTest {

    MessageBus mb;
    MicroService m1;
    ExampleEvent event = new ExampleEvent(false);
    ExampleBrodcast broadcast = new ExampleBrodcast();
    Callback<String> callback;
    @BeforeEach
    void setUp() {
        mb =  MessageBusImpl.getInstance();
        m1 = new C3POMicroservice();
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void sendBroadcast() {
        Broadcast b = new ExampleBrodcast();
        MicroService m1 = new HanSoloMicroservice();
        MicroService m2 = new C3POMicroservice();
        mb.subscribeBroadcast(b.getClass(),m1);
        m2.sendBroadcast(b);
        try{
            assertEquals(mb.awaitMessage(m1),b);
        } catch (InterruptedException e) {}
    }
    @Test
    void sendEvent() {
        mb.register(m1);
        mb.subscribeEvent(ExampleEvent.class, m1);
        mb.sendEvent(event);
        try{
            assertEquals(mb.awaitMessage(m1), event);
        } catch (InterruptedException e){}
    }

    @Test
    void awaitMessage() {
        mb.register(m1);
        mb.subscribeBroadcast(ExampleBrodcast.class, m1);
        try{
            assertEquals(mb.awaitMessage(m1),broadcast);
        } catch (InterruptedException e){ System.out.println("No message provided");}
        mb.sendBroadcast(broadcast);
        try{
            assertEquals(mb.awaitMessage(m1), broadcast);
        }catch (InterruptedException e){}
    }

    @Test
    void complete(){

    }
}