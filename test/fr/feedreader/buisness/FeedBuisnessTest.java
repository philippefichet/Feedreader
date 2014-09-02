/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.feedreader.buisness;

import fr.feedreader.models.Feed;
import fr.feedreader.models.FeedItem;
import java.io.File;
import java.util.List;
import org.junit.Test;
import play.db.jpa.JPA;
import static fr.feedreader.test.Application.configForTest;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.running;
import static org.junit.Assert.*;

/**
 *
 * @author philippe
 */
public class FeedBuisnessTest {
    
    @Test
    public void AddJPAWithTransaction() {
        running(fakeApplication(configForTest()), () ->  {
            JPA.withTransaction(() -> {
                Feed feed = new Feed();
                feed.setDescription("Test Description");
                feed.setName("test Name");
                feed.setUrl("http://www.developpez.com/index/atom");
                FeedBuisness.add(JPA.em(), feed);
                assertTrue("feed.getId() : " + feed.getId(), feed.getId() != null);
            });
        });
    }
    
    @Test
    public void getFeedAtom() {
        running(fakeApplication(configForTest()), () ->  {
            JPA.withTransaction(() -> {
                List<FeedItem> feedItems = FeedBuisness.getFeedItems(new File("./test/atom.atom").toURI());
                int size = feedItems.size();
                assertTrue("Total : " + size + " au lieu de 20", size == 20);
                int i = 0;
                assertNotNull(feedItems.get(i).getUpdated());
                assertEquals("Tor veut créer une messagerie instantanée anonyme, une première version bêta pour le 31 mars 2014", feedItems.get(i++).getTitle());
                assertEquals("Crytek met à disposition Renderdoc, un outil de débogage graphique intégrant un débogueur de shaders et de multiples outils d'analyse", feedItems.get(i++).getTitle());
                assertEquals("Qt 5.3 sort en version alpha, avec des améliorations de la stabilité, des performances et facilite la première utilisation du framework", feedItems.get(i++).getTitle());
                assertEquals("Windows XP gagne encore des parts à quelques semaines de la fin de son support, Windows 8.1 progresse lentement", feedItems.get(i++).getTitle());
                assertEquals("Google intègre les menus des restaurants dans les résultats de recherche,  une fonction disponible sur mobile et PC", feedItems.get(i++).getTitle());
                assertEquals("Espionnage : Nokia divulguerait les informations privées à la police et à l'étranger, selon des enquêtes de Helsingin Sanomat", feedItems.get(i++).getTitle());
                assertEquals("Boeing veut concevoir des smartphones espions qui s'autodétruisent lorsqu'on tente de démonter le boîtier", feedItems.get(i++).getTitle());
                assertEquals("Stephen Wolfram présente le langage Wolfram, multi-paradigme et de haut niveau, basé sur plus de 30 ans de recherche", feedItems.get(i++).getTitle());
                assertEquals("ASP.NET : Microsoft dévoile le projet Helios, qui combine les avantages offerts par l'hébergement IIS et Self-Host", feedItems.get(i++).getTitle());
                assertEquals("Netflix hack day : Fitbit détecte lorsque vous dormez,  et met automatiquement vos vidéos sur pause", feedItems.get(i++).getTitle());
                assertEquals("Intégration d'Eclipse Mylyn avec Redmine et Jenkins, un tutoriel de Régis Pouiller", feedItems.get(i++).getTitle());
                assertEquals("Apple : un lancement d'iOS in the Car prévu pour la semaine prochaine ?  Trois constructeurs automobiles en auraient la primeur", feedItems.get(i++).getTitle());
                assertEquals("Telegram Messenger : le nouveau concurrent de WhatsApp ?  L'application mise sur la vitesse et la sécurité des données", feedItems.get(i++).getTitle());
                assertEquals("La génération Y peu soucieuse des données de l'entreprise, d'après une enquête comparative de Fortinet", feedItems.get(i++).getTitle());
                assertEquals("GitHub présente son éditeur de texte pour les développeurs,  Atom est une «variante spécialisée de Chromium»", feedItems.get(i++).getTitle());
                assertEquals("Windows 8 serait plus vulnérable que Windows XP,  d'après un rapport de Secunia", feedItems.get(i++).getTitle());
                assertEquals("83% de possesseurs de smartphones l'utilisent en attendant quelqu'un, d'après une étude de InMobi", feedItems.get(i++).getTitle());
                assertEquals("Google Glass : une femme du programme Explorer se fait agresser,  ses assaillants n'ont pas supporté d'être filmés", feedItems.get(i++).getTitle());
                assertEquals("Chrome 34 bêta met le cap sur le responsive design avec le support de srcset,  Google améliore au passage son API Web audio", feedItems.get(i++).getTitle());
                assertEquals("Apprentissage de Qemu/LibVirt par l'exemple, par Nicolas Hennion", feedItems.get(i++).getTitle());
            });
        });
    }
    
    @Test
    public void getFeedAtomLinuxFr() {
        running(fakeApplication(configForTest()), () ->  {
            JPA.withTransaction(() -> {
                List<FeedItem> feedItems = FeedBuisness.getFeedItems(new File("./test/linuxfr.atom").toURI());
                int size = feedItems.size();
                assertTrue("Total : " + size + " au lieu de 15", size == 15);
                int i = 0;
                assertNotNull(feedItems.get(i).getUpdated());
                assertEquals("Apache-OFBiz s'invite dans l'usine", feedItems.get(i++).getTitle());
                assertEquals("PSES2014 - proposez vos hacks", feedItems.get(i++).getTitle());
                assertEquals("L'ERP OpenConcerto disponible en version 1.3", feedItems.get(i++).getTitle());
                assertEquals("ChiffrofÃªte Ã  Numa Paris le 29 mars 2014", feedItems.get(i++).getTitle());
                assertEquals("Install Party GNU/Linux le 29 mars 2014 Ã  Marseille", feedItems.get(i++).getTitle());
                assertEquals("Chiffrement, vie privÃ©e et logiciel libre", feedItems.get(i++).getTitle());
                assertEquals("DÃ©ploiement automatisÃ© dâ€™applications Ruby on Rails Ã  Mons le 20 mars", feedItems.get(i++).getTitle());
                assertEquals("Appel Ã  dons pour le logiciel de montage vidÃ©o Pitivi", feedItems.get(i++).getTitle());
                assertEquals("Caranille 4.5.1, un Ã©diteur de MMORPG en PHP", feedItems.get(i++).getTitle());
                assertEquals("Un DevCamp Alternc Ã  Paris du 26 au 28 mars", feedItems.get(i++).getTitle());
                assertEquals("Sortie de Beyond Linux From Scratch v7.5 (et elle est traduite !)", feedItems.get(i++).getTitle());
                assertEquals("Le Translathon 2014, session de traduction de GNOME Ã  Paris", feedItems.get(i++).getTitle());
                assertEquals("Nouveau site communautaire de la forge libre Tuleap", feedItems.get(i++).getTitle());
                assertEquals(" FOSS4G-fr 2014 - appel Ã  propositions", feedItems.get(i++).getTitle());
                assertEquals("ApÃ©ro Python/PHP Ã  Lyon le mardi 25 mars", feedItems.get(i++).getTitle());
            });
        });
    }

    
    @Test
    public void refresh() {
        running(fakeApplication(configForTest()), () ->  {
            JPA.withTransaction(() -> {
                Feed diablo3 = new Feed();
                diablo3.setDescription("Diablo 3");
                diablo3.setUrl("http://eu.battle.net/d3/fr/feed/news");
                FeedBuisness.add(JPA.em(), diablo3);
                assertTrue(diablo3.getId() > 0);
                List<FeedItem> refreshFeedItems = FeedBuisness.refreshFeedItems(JPA.em(), diablo3.getId());
                assertTrue(refreshFeedItems.size() > 0);
            });
        });
    }
}
