package controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.client.ClientProtocolException;

import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Security;
import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import fr.feedreader.buisness.FeedBuisness;
import fr.feedreader.models.Feed;
import fr.feedreader.models.FeedItem;
import java.text.SimpleDateFormat;
import play.api.Application;
import play.db.jpa.JPA;
import play.db.jpa.Transactional;

public class FeedController extends Controller {
	
    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
	@Transactional(readOnly=true)
	@Security.Authenticated(SecuredUser.class)
	public static Result show() {
		return ok(views.html.angularjs.feedv2.render());
	}
	
	@Transactional(readOnly=true)
	@Security.Authenticated(SecuredUser.class)
	public static Result get() {
		List<Feed> feeds = FeedBuisness.findAll(JPA.em());
		Map<Feed, Long> counter = FeedBuisness.countUnread(JPA.em());
		return ok(feedToJsonUnread(feeds, counter));
	}
	
	public static ArrayNode feedToJsonUnread(List<Feed> feeds, Map<Feed, Long> counter) {
		ArrayNode result = Json.newObject().arrayNode();
		for (Feed feed : feeds) {
			ObjectNode feedToJson = feedToJson(feed);
			feedToJson.put("unread", counter.get(feed));
			result.add(feedToJson);
		}
		return result;
	}
	
	@Transactional
	@Security.Authenticated(SecuredAdmin.class)
	public static Result add() {
		JsonNode jsonNode = request().body().asJson();
		Feed feed = new Feed();
		jsonToFeed(jsonNode, feed);
		Feed feedSaved = FeedBuisness.add(JPA.em(), feed);
		try {
			FeedBuisness.refreshFeedItems(JPA.em(), feedSaved.getId());
		} catch(Exception e) {
			
		}
		
		return ok(feedToJson(feedSaved));
	}
	
	private static Map<String, String> jsonToFeed(JsonNode jsonNode, Feed feed) {
		feed.setName(jsonNode.get("name").asText().trim());
		feed.setDescription(jsonNode.get("description").asText().trim());
		feed.setUrl(jsonNode.get("url").asText().trim());
		return null;
	}
	
	public static ObjectNode feedToJson(Feed feed) {
		ObjectNode feedJson = Json.newObject();
		feedJson.put("description", feed.getDescription());
		feedJson.put("id", feed.getId());
		feedJson.put("name", feed.getName());
		feedJson.put("url", feed.getUrl());
        if (feed.getLastUpdate() != null) {
            feedJson.put("lastUpdate", sdf.format(feed.getLastUpdate()));
        }
		return feedJson;
	}

	@Transactional
	@Security.Authenticated(SecuredAdmin.class)
	public static Result update(Integer id) {
		JsonNode jsonNode = request().body().asJson();
		Feed feed = FeedBuisness.find(JPA.em(), id);
		jsonToFeed(jsonNode, feed);
		Feed feedUpdated = FeedBuisness.update(JPA.em(), feed);
		
		return ok(feedToJson(feedUpdated));
	}


	@Transactional
	@Security.Authenticated(SecuredAdmin.class)
	public static Result remove(Integer id) {
		Feed feed = FeedBuisness.find(JPA.em(), id);
		FeedBuisness.delete(JPA.em(), feed);

		return ok("{\"result\":\"success\"}");
	}

}
