import java.util.concurrent.TimeUnit;
import scala.concurrent.duration.Duration;

import play.Configuration;
import jobs.FeedRefresh;
import akka.actor.ActorRef;
import akka.actor.Props;
import play.Application;
import play.GlobalSettings;
import play.libs.Akka;

public class Global extends GlobalSettings {

	@Override
	public void onStart(Application app) {
		try {
			super.onStart(app);
			Long feedResfreshStart = Configuration.root().getLong("jobs.feedrefresh.delay.start");
			if (feedResfreshStart == null) {
				feedResfreshStart = 30000L;
			}
			Long feedResfreshExecution = Configuration.root().getLong("jobs.feedrefresh.delay.exectution");
			if (feedResfreshExecution == null) {
				feedResfreshExecution = 15L;
			}
			ActorRef feedActor = Akka.system().actorOf(new Props(FeedRefresh.class));
			Akka.system().scheduler().schedule(
				Duration.create(feedResfreshStart, TimeUnit.MILLISECONDS),
				Duration.create(feedResfreshExecution, TimeUnit.MINUTES),
				feedActor,
				"tick",
				Akka.system().dispatcher(),
				null
			);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
