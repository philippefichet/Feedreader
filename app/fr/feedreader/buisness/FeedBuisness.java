package fr.feedreader.buisness;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import fr.feedreader.models.Feed;
import fr.feedreader.models.FeedItem;
import fr.feedreader.models.FeedUnreadCounter;
import java.util.Date;

public class FeedBuisness {

    private static Logger logger = LoggerFactory.getLogger(FeedBuisness.class);

    public static List<Feed> findAll(EntityManager em) {
        TypedQuery<Feed> query = em.createNamedQuery(Feed.findAll, Feed.class);
        return query.getResultList();
    }

    public static Feed add(EntityManager em, Feed feed) {
        em.persist(feed);
        return feed;
    }

    public static Feed find(EntityManager em, Integer id) {
        return em.find(Feed.class, id);
    }

    public static Feed update(EntityManager em, Feed feed) {
        return em.merge(feed);
    }

    public static void delete(EntityManager em, Feed feed) {
        em.remove(feed);
    }

    /**
     * Met à jour les articles du flux à partir de son url
     *
     * @param em Unité de persistance
     * @param id Identifiant du flux
     * @return Liste des articles mise à jour
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     */
    public static List<FeedItem> refreshFeedItems(EntityManager em, Integer id) throws ParserConfigurationException, SAXException, IOException {
        Feed feed = find(em, id);
        List<FeedItem> updatedFeedItem = new ArrayList<>();
        if (feed != null) {
            List<FeedItem> feedItems = getFeedItems(feed.getUrl());
            for (FeedItem feedItem : feedItems) {
                TypedQuery<FeedItem> query = em.createNamedQuery(FeedItem.searchByFeedIdAndFeedItemId, FeedItem.class);
                query.setParameter("feedId", id);
                query.setParameter("feedItemId", feedItem.getFeedItemId());
                try {
                    FeedItem existing = query.getSingleResult();
                    // Mise a jour d'article existant
                    if (existing.getUpdated() != null && feedItem.getUpdated() != null && existing.getUpdated().before(feedItem.getUpdated())) {
                        updatedFeedItem.add(existing);
                        existing.setReaded(false);
                    }
                    existing.setLink(feedItem.getLink());
                    existing.setSummary(feedItem.getSummary());
                    existing.setTitle(feedItem.getTitle());
                    existing.setUpdated(feedItem.getUpdated());
                    existing.setEnclosure(feedItem.getEnclosure());
                    FeedItemBuisness.update(em, existing);
                // Nouveau articles
                } catch (NoResultException e) {
                    feedItem.setFeed(feed);
                    feedItem.setReaded(false);
                    FeedItem feedItemCreate = FeedItemBuisness.create(em, feedItem);
                    updatedFeedItem.add(feedItemCreate);
                } catch (Exception e) {
                    LoggerFactory.getLogger(FeedBuisness.class).error("Erreur dans la récupértion d'un flux " + id + ", \"" + feedItem.getFeedItemId() + "\n", e);
                }
            }
            // Mise à jour de la date de récupération du flux
            feed.setLastUpdate(new Date());
            update(em, feed);
        }
        return updatedFeedItem;
    }

    public static List<FeedItem> getFeedItems(EntityManager em, Integer id) throws ParserConfigurationException, SAXException, IOException {
        Feed feed = find(em, id);
        return feed.getFeedItems();
    }

    public static List<FeedItem> getFeedItems(String url) throws ParserConfigurationException, SAXException, IOException {
        logger.info("Récupération du flux \"" + url + "\" ...");
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        FeedProxyHandler feedHandler = new FeedProxyHandler();
        parser.parse(url, feedHandler);
        logger.info("Récupération du flux \"" + url + "\" Terminer");
        return feedHandler.getFeedItems();
    }

    public static Map<Feed, Long> countUnread(EntityManager em) {
        Map<Feed, Long> counter = new HashMap<>();
        Query query = em.createQuery("SELECT NEW fr.feedreader.models.FeedUnreadCounter(fi.feed, count(fi)) FROM FeedItem fi WHERE (fi.readed = FALSE OR fi.readed IS NULL) GROUP BY fi.feed.id");
        List<FeedUnreadCounter> counters = query.getResultList();
        for (FeedUnreadCounter feedUnreadCounter : counters) {
            counter.put(feedUnreadCounter.getFeed(), feedUnreadCounter.getCounter());
        }
        return counter;
    }

    public static Map<Feed, List<FeedItem>> updateAllFeed(EntityManager em) {
        List<Feed> feeds = findAll(em);
        // Liste des nouveaux flux avec les articles
        Map<Feed, List<FeedItem>> feedsUpdated = new HashMap<>();
        for (Feed feed : feeds) {
            // Récupération de l'article le plus récent
            TypedQuery<FeedItem> lastItemQuery = em.createNamedQuery(FeedItem.findAllByFeedId, FeedItem.class);
            lastItemQuery.setParameter("feedId", feed.getId());
            lastItemQuery.setMaxResults(1);
            try {
                FeedItem feedItem = lastItemQuery.getSingleResult();
                if (feedItem.getUpdated() != null) {
                    logger.info("Dernier flux récupérer pour \"" + feed.getName() + "\" : " + feedItem.getUpdated().toString());
                } else {
                    logger.info("Dernier flux récupérer pour \"" + feed.getName() + "\" : null");
                }

                // Récupération des de nouveau article 
                List<FeedItem> feedItems = refreshFeedItems(em, feed.getId());
                feedsUpdated.put(feed, feedItems);
            } catch (javax.persistence.NoResultException e) {
                logger.info("Premier flux récupérer pour \"" + feed.getName() + "\"");
                try {
                    List<FeedItem> feedItems = refreshFeedItems(em, feed.getId());
                    feedsUpdated.put(feed, feedItems);
                } catch (Exception e1) {
                    logger.info("Erreur lors de la récumération du premier flux pour \"" + feed.getName() + "\"");
                    e.printStackTrace();
                }
            } catch (Exception e) {
                logger.error("Erreur lors de la mise à jour du flux : \"" + feed.getName() + "\"");
                e.printStackTrace();
            }
        }
//		FeedBuisness.countUnread(JPA.em());
        return feedsUpdated;
    }
}
