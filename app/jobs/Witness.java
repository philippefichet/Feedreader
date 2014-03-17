package jobs;

import java.util.Date;

import akka.actor.UntypedActor;

public class Witness extends UntypedActor {
	public void onReceive(Object message) throws Exception {
		System.out.println(new Date() + " : Test : " + message.toString());
	}
}
