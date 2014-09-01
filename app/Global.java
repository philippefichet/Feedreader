
import java.io.File;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.duration.Duration;
import play.Configuration;
import jobs.FeedRefresh;
import akka.actor.ActorRef;
import akka.actor.Props;
import play.Application;
import play.GlobalSettings;
import play.api.libs.Files;
import play.libs.Akka;

public class Global extends GlobalSettings {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void onStart(Application app) {
        jobsInit(app);
    }

    public void jobsInit(Application app) {
        try {
            super.onStart(app);
            Long feedResfreshStart = app.configuration().getLong("jobs.feedrefresh.delay.start");
            if (feedResfreshStart == null) {
                feedResfreshStart = 30000L;
            }
            Long feedResfreshExecution = app.configuration().getLong("jobs.feedrefresh.delay.exectution");
            if (feedResfreshExecution == null) {
                feedResfreshExecution = 15L;
            }
            ActorRef feedActor = Akka.system().actorOf(Props.create(FeedRefresh.class));
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
