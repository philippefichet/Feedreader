package fr.feedreader.buisness;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
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

import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import fr.feedreader.models.Feed;
import fr.feedreader.models.FeedItem;
import fr.feedreader.models.FeedUnreadCounter;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import play.Logger;

public class FeedBuisness {

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
    public static List<FeedItem> refreshFeedItems(EntityManager em, Integer id) throws IOException, IllegalArgumentException, FeedException, URISyntaxException {
        Feed feed = find(em, id);
        List<FeedItem> updatedFeedItem = new ArrayList<>();
        if (feed != null) {
            List<FeedItem> feedItems = getFeedItems(feed.getUrl());
            for (FeedItem feedItem : feedItems) {
                TypedQuery<FeedItem> query = em.createNamedQuery(FeedItem.searchByFeedIdAndFeedItemId, FeedItem.class);
                query.setParameter("feedId", id);
                query.setParameter("feedItemId", feedItem.getFeedItemId());
                FeedItem existing = null;
                try {
                    existing = query.getSingleResult();

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
                    Logger.error("Erreur dans la récupération d'un flux " + id + ", \"" + feedItem.getFeedItemId() + "\"");
                    Logger.error(e.getLocalizedMessage());
                    logFeedItem(existing);
                    logFeedItem(feedItem);
                    
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
    public static List<FeedItem> getFeedItems(URI uri) throws IOException, IllegalArgumentException, FeedException, URISyntaxException {
        return getFeedItems(uri.toURL().toString());
    }

    public static List<FeedItem> getFeedItems(String uri) throws IOException, IllegalArgumentException, FeedException, URISyntaxException {
        Logger.info("Récupération du flux \"" + uri + "\" ...");
        SyndFeedInput input = new SyndFeedInput();
        XmlReader xmlreader = new XmlReader(new URI(uri).toURL());
        SyndFeed build = input.build(xmlreader);
        List<SyndEntry> syndEntries = build.getEntries();
        List<FeedItem> feedItems = syndEntries.stream().map((SyndEntry syndEntry) -> {
            return new FeedItem(
                syndEntry.getUri(),
                syndEntry.getTitle(),
                syndEntry.getLink(),
                syndEntry.getEnclosures().size() > 0 ? syndEntry.getEnclosures().get(0).getUrl() : null,
                syndEntry.getContents().size() > 0 ? syndEntry.getContents().get(0).getValue() : null,
                syndEntry.getUpdatedDate()
            );
        }).collect(Collectors.toList());
        return feedItems;
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
            FeedItem feedItem = null;
            try {
                feedItem = lastItemQuery.getSingleResult();
                if (feedItem.getUpdated() != null) {
                    Logger.info("Dernier flux récupérer pour \"" + feed.getName() + "\" : " + feedItem.getUpdated().toString());
                } else {
                    Logger.info("Dernier flux récupérer pour \"" + feed.getName() + "\" : null");
                }

                // Récupération des de nouveau article 
                List<FeedItem> feedItems = refreshFeedItems(em, feed.getId());
                feedsUpdated.put(feed, feedItems);
            } catch (javax.persistence.NoResultException e) {
                Logger.info("Premier flux récupérer pour \"" + feed.getName() + "\"");
                try {
                    List<FeedItem> feedItems = refreshFeedItems(em, feed.getId());
                    feedsUpdated.put(feed, feedItems);
                } catch (Exception e1) {
                    Logger.error("Erreur lors de la récumération du premier flux pour \"" + feed.getName() + "\"");
                    Logger.error(e1.getLocalizedMessage());
                    logFeed(feed);
                    logFeedItem(feedItem);
                }
            } catch (Exception e) {
                Logger.error("Erreur lors de la mise à jour du flux : \"" + feed.getName() + "\"");
                Logger.error(e.getLocalizedMessage());
                logFeed(feed);
                logFeedItem(feedItem);
            }
        }
//		FeedBuisness.countUnread(JPA.em());
        return feedsUpdated;
    }
    
    private static void logFeed(Feed feed) {
        if (feed == null) {
            Logger.error("feed null");
        } else {
            Logger.error("feedItem :");
            Logger.error("\t - getUrl() : " + StringUtils.length(feed.getUrl()));
            Logger.error("\t - getName() : " + StringUtils.length(feed.getName()));
            Logger.error("\t - getDescription() : " + StringUtils.length(feed.getDescription()));
        }
    }
    
    private static void logFeedItem(FeedItem feedItem) {
        if (feedItem == null) {
            Logger.error("feedItem null");
        } else {
            Logger.error("feedItem :");
            Logger.error("\t - getTitle() : " + StringUtils.length(feedItem.getTitle()));
            Logger.error("\t - getEnclosure() : " + StringUtils.length(feedItem.getEnclosure()));
            Logger.error("\t - getFeedItemId() : " + StringUtils.length(feedItem.getFeedItemId()));
            Logger.error("\t - getLink() : " + StringUtils.length(feedItem.getLink()));
            Logger.error("\t - getSummary() : " + StringUtils.length(feedItem.getSummary()));
        }
    }
}

