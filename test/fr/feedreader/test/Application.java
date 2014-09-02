/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.feedreader.test;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author philippe
 */
public class Application {
    public static Map<String, String> configForTest() {
        Map<String, String> config = new HashMap<>();
        config.put("logger.root", "ERROR");
        config.put("logger.jobs", "ERROR");
        config.put("logger.play", "ERROR");
        config.put("logger.application", "ERROR");
        config.put("db.default.driver", "org.hsqldb.jdbc.JDBCDriver");
        config.put("db.default.url", "jdbc:hsqldb:mem:feedreader");
        config.put("db.default.user", "feedreader");
        config.put("db.default.password", "a5tY6d4u7");
        config.put("db.default.jndiName", "feedreader");
        return config;
    }
}
