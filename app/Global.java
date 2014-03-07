import java.util.concurrent.TimeUnit;
import scala.concurrent.duration.Duration;

import jobs.FeedRefresh;
import akka.actor.ActorRef;
import akka.actor.Props;
import play.Application;
import play.GlobalSettings;
import play.libs.Akka;

public class Global extends GlobalSettings {

	@Override
	public void onStart(Application app) {
		super.onStart(app);
		ActorRef feedActor = Akka.system().actorOf(new Props(FeedRefresh.class));
		Akka.system().scheduler().schedule(
			Duration.create(0, TimeUnit.MILLISECONDS),
			Duration.create(15, TimeUnit.MINUTES),
			feedActor,
			"tick",
			Akka.system().dispatcher(),
			null
		);
	}
	
}
