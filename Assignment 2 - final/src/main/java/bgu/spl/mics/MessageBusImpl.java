package bgu.spl.mics;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.Vector;
/**
 * The {@link MessageBusImpl class is the implementation of the MessageBus interface.
 * Write your implementation here!
 * Only private fields and methods can be added to this class.
 */
public class MessageBusImpl implements MessageBus {
	private static class MessageBussHolder{
		private static MessageBusImpl instance=new MessageBusImpl();
	}
	public static MessageBusImpl getInstance() {
		return MessageBussHolder.instance;
	}

	private HashMap<MicroService, Vector<Message>> messagesQs;
	private HashMap<Class<? extends Message>,Vector<MicroService>> subscriptions;
	private HashMap<Message,Future> expectations;

	private MessageBusImpl(){
		messagesQs = new HashMap<MicroService, Vector<Message>>();
		subscriptions=new HashMap<Class<? extends Message>,Vector<MicroService>>();
		expectations=new HashMap<Message,Future>();
	}

	@Override
	public <T> void subscribeEvent(Class<? extends Event<T>> type, MicroService m) {
		if(subscriptions.get(type)==null)
			subscriptions.put(type,new Vector<MicroService>());
		subscriptions.get(type).add(m);
	}

	@Override
	public void subscribeBroadcast(Class<? extends Broadcast> type, MicroService m) {
		if(!subscriptions.containsKey(type))
			subscriptions.put(type,new Vector<MicroService>());
		subscriptions.get(type).add(m);
    }

	@Override @SuppressWarnings("unchecked")
	public <T> void complete(Event<T> e, T result) {
		expectations.get(e).resolve(result);
	}

	@Override
	public synchronized void sendBroadcast(Broadcast b) {
		for (MicroService m:subscriptions.get(b.getClass())) {
			messagesQs.get(m).add(b);
		}
		notifyAll();
	}

	private MicroService roundRobin(Class<? extends Event> type){
	if(subscriptions.get(type)!=null) {
		if(!subscriptions.get(type).isEmpty()) {
			MicroService microService = subscriptions.get(type).get(0);
			subscriptions.get(type).remove(0);
			subscriptions.get(type).add(microService);
			return microService;
			}
		}
		return  null;
	}

	private boolean isRegistered(MicroService microService){
		return messagesQs.containsKey(microService);
	}

	@Override
	public synchronized <T> Future<T> sendEvent(Event<T> e) {
		MicroService m = roundRobin(e.getClass());
		if(m!=null) {
			messagesQs.get(m).add(e);
			Future<T> f = new Future<>();
			expectations.put(e, f);
			notifyAll();
			return f;
		}
		return null;
	}

	@Override
	public void register(MicroService m) {
		messagesQs.put(m, new Vector<Message>());
	}

	@Override
	public void unregister(MicroService m) {
		messagesQs.remove(m);
		for (Map.Entry<Class<? extends Message>,Vector<MicroService>>pair:subscriptions.entrySet()){
			for (int i = 0; i < pair.getValue().size(); i++) {
				pair.getValue().remove(m);
			}
		}
	}

	@Override
	public synchronized Message awaitMessage(MicroService m) throws InterruptedException {
		while(messagesQs.get(m).isEmpty())
			wait();

		Message output=messagesQs.get(m).firstElement();
		messagesQs.get(m).remove(output);
		return output;
	}
}
