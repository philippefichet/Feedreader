package jobs;

import java.util.Date;
import java.util.List;

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

public class FeedRefresh extends UntypedActor {

	public void onReceive(Object message) throws Exception {
		final Logger logger = LoggerFactory.getLogger(getClass());
		logger.info(new Date() + "Recherche des mise à jours de flux");
		JPA.withTransaction(new F.Callback0() {
			
			@Override
			public void invoke() throws Throwable {
				EntityManager em = JPA.em();
				List<Feed> feeds = FeedBuisness.findAll(em);
				for (Feed feed : feeds) {
					TypedQuery<FeedItem> lastItemQuery = em.createNamedQuery(FeedItem.findAllByFeedId, FeedItem.class);
					lastItemQuery.setParameter("feedId", feed.getId());
					lastItemQuery.setMaxResults(1);
					try {
						FeedItem feedItem = lastItemQuery.getSingleResult();
						logger.info("Dernier flux récupérer pour \"" + feed.getName() + "\" : " + feedItem.getUpdated().toString());
						List<FeedItem> feedItems = FeedBuisness.refreshFeedItems(em, feed.getId());
					} catch(javax.persistence.NoResultException e) {
						logger.info("Permier flux récupérer pour \"" + feed.getName() + "\"");
						List<FeedItem> feedItems = FeedBuisness.refreshFeedItems(em, feed.getId());
					}
				}
				FeedBuisness.countUnread(JPA.em());
			}
		});

		
	}
	
}
