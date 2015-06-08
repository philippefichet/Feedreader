package controllers;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.feedreader.models.Feed;
import fr.feedreader.models.FeedItem;
import play.mvc.Controller;
import play.mvc.WebSocket;
import play.mvc.WebSocket.In;
import play.mvc.WebSocket.Out;
import play.libs.F.Callback;
import play.libs.F.Callback0;
import play.libs.Json;

public class LiveController extends Controller {

    private static Map<WebSocket.In<JsonNode>, WebSocket.Out<JsonNode>> users = new ConcurrentHashMap<>();

    public static void sendFeedItems(List<FeedItem> feedItems) {
        ObjectNode json = Json.newObject();
        json.put("type", "feedItems");
        json.put("feedItems", json);
        ArrayNode feedItemsJson = json.arrayNode();
        for (FeedItem feedItem : feedItems) {
            feedItemsJson.add(FeedItemController.feedItemToJson(feedItem));
        }

        Iterator<In<JsonNode>> iterator = users.keySet().iterator();
        while (iterator.hasNext()) {
            In<JsonNode> in = iterator.next();
            Out<JsonNode> out = users.get(in);
            out.write(json);
        }
    }

    public static void sendFeeds(List<Feed> feeds, Map<Feed, Long> counter, List<FeedItem> feedItemsUpdate) {
        ArrayNode feedsJson = FeedController.feedToJsonUnread(feeds, counter);
        ArrayNode feedItemsJson = FeedItemController.feedItemToJson(feedItemsUpdate);
        ObjectNode json = Json.newObject();
        json.put("type", "feeds");
        json.put("feeds", feedsJson);
        json.put("feedItems", feedItemsJson);
        Iterator<In<JsonNode>> iterator = users.keySet().iterator();
        while (iterator.hasNext()) {
            In<JsonNode> in = iterator.next();
            Out<JsonNode> out = users.get(in);

            out.write(json);
        }
    }

    public WebSocket<JsonNode> feed() {
        return new WebSocket<JsonNode>() {

            // Called when the Websocket Handshake is done.
            public void onReady(final In<JsonNode> in, final Out<JsonNode> out) {

                users.put(in, out);

                // For each event received on the socket,
                in.onMessage(new Callback<JsonNode>() {
                    public void invoke(JsonNode event) {
                        // Log events to the console
                        System.out.println(event);
                    }
                });

                // When the socket is closed.
                in.onClose(new Callback0() {
                    public void invoke() {
                        users.remove(in);
                        System.out.println("Disconnected : " + new Date());
                    }
                });

                ObjectNode json = Json.newObject();
                json.put("message", "Hello!;");
                // Send a single 'Hello!' message
                out.write(json);
                System.out.println("Connect : " + new Date());
            }
        };
    }
}
