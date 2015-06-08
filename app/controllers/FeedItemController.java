package controllers;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.feedreader.buisness.FeedBuisness;
import fr.feedreader.buisness.FeedItemBuisness;
import fr.feedreader.models.FeedItem;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;
import play.libs.Json;
import play.mvc.Controller;
import play.mvc.Result;

/**
 * Created by FICHET Philippe on 01/02/14.
 */
public class FeedItemController extends Controller {

    @Transactional
    public Result setReadedTrue(Integer feedItemId) {
        return setReaded(feedItemId, Boolean.TRUE);
    }

    @Transactional
    public Result setReadedFalse(Integer feedItemId) {
        return setReaded(feedItemId, Boolean.FALSE);
    }

    @Transactional
    public Result setReaded(Integer feedItemId, Boolean readed) {
        FeedItem feedItem = FeedItemBuisness.setReaded(JPA.em(), feedItemId, readed);
        ObjectNode feedItemJson = Json.newObject();
        feedItemJson.put("readed", feedItem.getReaded());
        if (feedItem.getReaded()) {
            feedItemJson.put("reverseReaded", routes.FeedItemController.setReadedFalse(feedItem.getId()).toString());
        } else {
            feedItemJson.put("reverseReaded", routes.FeedItemController.setReadedTrue(feedItem.getId()).toString());
        }

        List<FeedItem> feedItemUpdate = new ArrayList<>();
        feedItemUpdate.add(feedItem);
        LiveController.sendFeeds(FeedBuisness.findAll(JPA.em()), FeedBuisness.countUnread(JPA.em()), feedItemUpdate);

        return ok(feedItemJson);
    }

    @Transactional(readOnly = true)
    public Result feedItem(Integer feedId, Integer page) {
        ObjectNode json = Json.newObject();
        ArrayNode feedItemsJson = json.arrayNode();
        json.put("pages", FeedItemBuisness.getTotalPage(JPA.em(), feedId));
        List<FeedItem> feedItems = FeedItemBuisness.findAll(JPA.em(), feedId, page);
        json.put("feedItems", feedItemToJson(feedItems));
        return ok(json);
    }

    public static ArrayNode feedItemToJson(List<FeedItem> feedItems) {
        ArrayNode feedItemsJson = Json.newObject().arrayNode();
        for (FeedItem feedItem : feedItems) {
            feedItemsJson.add(feedItemToJson(feedItem));
        }
        return feedItemsJson;
    }

    public static ObjectNode feedItemToJson(FeedItem feedItem) {
        ObjectNode feedItemJson = Json.newObject();
        feedItemJson.put("enclosure", feedItem.getEnclosure());
        feedItemJson.put("feedItemId", feedItem.getFeedItemId());
        feedItemJson.put("id", feedItem.getId());
        feedItemJson.put("link", feedItem.getLink());
        feedItemJson.put("readed", feedItem.getReaded());
        feedItemJson.put("summary", feedItem.getSummary());
        feedItemJson.put("title", feedItem.getTitle());
        if (feedItem.getUpdated() != null) {
            feedItemJson.put("updated", feedItem.getUpdated().getTime());
        } else {
            feedItemJson.put("updated", "null");
        }
        ObjectNode url = Json.newObject();
        feedItemJson.put("url", url);
        url.put("toRead", routes.FeedItemController.setReadedTrue(feedItem.getId()).toString());
        if (feedItem.getReaded()) {
            url.put("reverseReaded", routes.FeedItemController.setReadedFalse(feedItem.getId()).toString());
        } else {
            url.put("reverseReaded", routes.FeedItemController.setReadedTrue(feedItem.getId()).toString());
        }

        return feedItemJson;
    }
}
