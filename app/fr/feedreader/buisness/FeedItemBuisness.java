package fr.feedreader.buisness;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import fr.feedreader.models.FeedItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FeedItemBuisness {
    private static Logger logger = LoggerFactory.getLogger(FeedBuisness.class);
	private static Integer itemPerPage = 20;
	
	public static FeedItem create(EntityManager em, FeedItem feedItem) {
        try {
            em.persist(feedItem);
        } catch (Exception e ) {
            e.printStackTrace();
            logger.debug("**********************");
            logger.debug("feedItem.getEnclosure : " + feedItem.getEnclosure());
            logger.debug("feedItem.getFeedItemId : " + feedItem.getFeedItemId());
            logger.debug("feedItem.getLink : " + feedItem.getLink());
            logger.debug("feedItem.getSummary : " + feedItem.getSummary());
            logger.debug("feedItem.getTitle : " + feedItem.getTitle());
            logger.debug("feedItem.getId : " + feedItem.getId());
            logger.debug("feedItem.getReaded : " + feedItem.getReaded());
            logger.debug("feedItem.getUpdated : " + feedItem.getUpdated());
            logger.debug("feedItem.getFeed : " + feedItem.getFeed());
        }
		return feedItem;
	}
	
	public static FeedItem update(EntityManager em, FeedItem feedItem) {
		return em.merge(feedItem);
	}
	
	public static FeedItem setReaded(EntityManager em, Integer feedItemId, Boolean readed) {
		FeedItem feedItem = find(em, feedItemId);
		feedItem.setReaded(readed);
		return update(em, feedItem);
	}
	
	public static FeedItem find(EntityManager em, Integer feedItemId) {
		return em.find(FeedItem.class, feedItemId);
	}

	public static Long getTotalPage(EntityManager em, Integer feedId) {
		TypedQuery<Long> query = em.createNamedQuery(FeedItem.countByFeedId, Long.class);
		query.setParameter("feedId", feedId);
		return (query.getSingleResult() / itemPerPage) + 1;
	}

	
	public static List<FeedItem> findAll(EntityManager em, Integer feedId, Integer page) {
		TypedQuery<FeedItem> query = em.createNamedQuery(FeedItem.findAllByFeedId, FeedItem.class);
		query.setParameter("feedId", feedId);
		query.setMaxResults(itemPerPage);
		query.setFirstResult((page - 1) * itemPerPage);
		return query.getResultList();
	}

}
