/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Test;
import play.mvc.Http;
import play.mvc.Result;
import play.test.FakeRequest;
import play.libs.Json;
import static org.junit.Assert.*;
import static play.test.Helpers.*;

/**
 *
 * @author philippe
 */
public class FeedControllerTest {

    @Test
    public void testIndex() {
        running(fakeApplication(), new Runnable() {
            public void run() {
                String jsonFeedToAdd = "{\"name\":\"fakeName\",\"description\":\"fake description\",\"url\":\"http://www.developpez.com/index/atom\"}";
                ObjectNode newObject = Json.newObject();
                newObject.put("name", "fakename");
                newObject.put("description", "fakedescription");
                newObject.put("url", "http://www.developpez.com/index/atom");
                FakeRequest fakeRequest = new FakeRequest(PUT, "/feed");
                fakeRequest.withJsonBody(newObject);
                fakeRequest.withHeader("Authorization", "Basic YWRtaW46YWRtaW4=");
                Result result = callAction(
                    controllers.routes.ref.FeedController.add(),
                    fakeRequest
                );
                assertEquals(Http.Status.OK, status(result));
            }
        });
    }
}
