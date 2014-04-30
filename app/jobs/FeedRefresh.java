package jobs;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import controllers.LiveController;
import fr.feedreader.buisness.FeedBuisness;
import fr.feedreader.models.Feed;
import fr.feedreader.models.FeedItem;
import akka.actor.UntypedActor;
import play.db.jpa.JPA;
import play.libs.Akka;
import play.libs.F;
import play.libs.WS;

public class FeedRefresh extends UntypedActor {

	private static final Logger logger = LoggerFactory.getLogger(FeedRefresh.class);
	
	public void onReceive(Object message) throws Exception {
		
		logger.info(new Date() + ": Debut de la recherche des mise à jours de flux");
		JPA.withTransaction(new F.Callback0() {
			@Override
			public void invoke() throws Throwable {
				logger.info(new Date() + ": Recherche des mise à jours de flux dans la transaction JPA");
				Map<Feed, List<FeedItem>> feedUpdated = FeedBuisness.updateAllFeed(JPA.em());
				Iterator<Feed> feedIterator = feedUpdated.keySet().iterator();
				while(feedIterator.hasNext())
				{
					feedIterator.next();
				}
			}
		});
		
		logger.info(new Date() + ": Fin de la recherche des mise à jours de flux");
	}
	
}
