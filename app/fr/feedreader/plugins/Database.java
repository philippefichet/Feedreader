/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.feedreader.plugins;

import com.google.inject.Inject;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import play.Application;
import play.Plugin;
import play.Logger;
import play.db.jpa.JPA;
import fr.feedreader.models.FeedItem;

/**
 *
 * @author philippe
 */
public class Database extends Plugin {

    private final Application application;
    
    @Inject
    public Database(Application application) {
        this.application = application;
    }

    
    @Override
    public boolean enabled() {
        return true;
    }

    @Override
    public void onStart() {
        if (Logger.isInfoEnabled()) {
            Logger.info("Mise à jour de la base de données");
        }
        try (Connection connection = play.db.DB.getDataSource().getConnection(); Statement createStatement = connection.createStatement();) {
            createStatement.executeUpdate("ALTER TABLE FEEDITEM ALTER COLUMN SUMMARY VARCHAR(1048576)");
        } catch (SQLException ex) {
            Logger.error("Impossible de mettre à jour la base de données à partir du plugin", ex);
        }
        if (Logger.isInfoEnabled()) {
            Logger.info("Fin de la mise à jour de la base de données");
        }
    }
}
