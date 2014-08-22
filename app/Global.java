
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
        applicationInit(app);
    }

    /**
     * Récupére le chemin de la configuration de l'application (pas du play
     * framework)
     *
     * @param app Application lancé
     * @return Chemin de la configuration de l'application (pas du play
     * framework)
     */
    protected String getHomeConfiguration(Application app) {
        String application = app.configuration().getString(
                "configuration.application");
        if (application == null) {
            String userDir = System.getProperty("user.dir") + File.separator;
            if (userDir == null) {
                userDir = "~/";
            }
            application = userDir + ".feedreader/application.json";
        }
        return application;
    }

    /**
     * Executed when the application stops.
     */
    public void onStop(Application app) {
        String application = getHomeConfiguration(app);
        conf.Application.getInstance().saveFromPath(application);
    }

    /**
     * Initialisation de la configuration de l'application
     *
     * @param app Application lancé
     */
    public void applicationInit(Application app) {
        String application = getHomeConfiguration(app);
        conf.Application.getInstance().loadFromPath(application);
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
