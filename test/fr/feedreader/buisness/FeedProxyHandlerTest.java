package fr.feedreader.buisness;

import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.junit.Test;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import fr.feedreader.models.FeedItem;

import com.rometools.rome.io.XmlReader;
import com.rometools.rome.io.SyndFeedInput;

import static org.junit.Assert.*;

public class FeedProxyHandlerTest {

    @Test
    public void testAtom() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        FeedProxyHandler feedHandler = new FeedProxyHandler();
        File file = new File("./test/atom.atom");
        assertTrue(file.exists());
        parser.parse(file.getAbsolutePath(), feedHandler);
        assertEquals("atom", feedHandler.getType());
        List<FeedItem> feedItems = feedHandler.getFeedItems();
        int size = feedItems.size();
        assertTrue("Total : " + size + " au lieu de 20", size == 20);
        int i = 0;
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
    }

    @Test
    public void testRss() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        FeedProxyHandler feedHandler = new FeedProxyHandler();
        File file = new File("./test/rss.rss");
        assertTrue(file.exists());
        parser.parse(file.getAbsolutePath(), feedHandler);
        assertEquals("rss", feedHandler.getType());
        List<FeedItem> feedItems = feedHandler.getFeedItems();
        int size = feedItems.size();
        assertTrue("Total : " + size + " au lieu de 20", size == 20);
        int i = 0;
        assertNotNull(feedItems.get(i).getUpdated());
        assertEquals("La notion de BOM avec Maven", feedItems.get(i++).getTitle());
        assertEquals("Le Protocole Direct de Sockets sous Java 7 - Ecrit une fois, exécuté partout...", feedItems.get(i++).getTitle());
        assertEquals("Repenser la propagation des exceptions avec Java 8", feedItems.get(i++).getTitle());
        assertEquals("Comment marchent les annotations et à quoi servent-elles ?", feedItems.get(i++).getTitle());
        assertEquals("Release Candidates Disponibles, Nouveaux Atomic Numbers et Stripped Implementations Abandonées", feedItems.get(i++).getTitle());
        assertEquals("[Java 8] Comment le compilateur pour Nashorn fonctionne ?", feedItems.get(i++).getTitle());
        assertEquals("JDK 8 & Lambdas, Streams et Collectors", feedItems.get(i++).getTitle());
        assertEquals("Comment fonctionne le garbage collector de Java", feedItems.get(i++).getTitle());
        assertEquals("Première publication du projet de garbage collector Shenandoah pour OpenJDK", feedItems.get(i++).getTitle());
        assertEquals("Les standards de codage de Google", feedItems.get(i++).getTitle());
        assertEquals("Questions/Réponses: Java Coding Guidelines", feedItems.get(i++).getTitle());
        assertEquals("Ceylon : un java moderne et sans legacy", feedItems.get(i++).getTitle());
        assertEquals("JHipster v0.9.0 nouvelle version du générateur Yeoman pour Spring/AngularJS", feedItems.get(i++).getTitle());
        assertEquals("[Clojure] Implémentation de structure de donnée : Liste simplement chainée", feedItems.get(i++).getTitle());
        assertEquals("Unsafe à tout prix; Oracle sonde la communauté à propos de sun.misc.Unsafe", feedItems.get(i++).getTitle());
        assertEquals("Eclipse Scout avec Jérémie Bresson", feedItems.get(i++).getTitle());
        assertEquals("Sortie de JArchitect v4.0", feedItems.get(i++).getTitle());
        assertEquals("[Multithreading] Comment choisir le bon ExecutorService ?", feedItems.get(i++).getTitle());
        assertEquals("Lambda Java 8 et scala :  Comment sont t'ils implémentés au niveau du bytecode ?", feedItems.get(i++).getTitle());
        assertEquals("Arrête de jouer, deviens un ninja ! (web framework)", feedItems.get(i++).getTitle());
    }
    
        @Test
    public void testLinuxFr() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        SAXParser parser = parserFactory.newSAXParser();
        FeedProxyHandler feedHandler = new FeedProxyHandler();
        File file = new File("./test/linuxfr.atom");
        assertTrue(file.exists());
        parser.parse(file.getAbsolutePath(), feedHandler);
        assertEquals("atom", feedHandler.getType());
        List<FeedItem> feedItems = feedHandler.getFeedItems();
        int size = feedItems.size();
        int i = 0;
        assertTrue("Total : " + size + " au lieu de 15", size == 15);
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
    }
    
    @Test
    public void testAtomRome() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, FeedException {
        SyndFeedInput input = new SyndFeedInput();
        XmlReader xmlreader = new XmlReader(new File("./test/atom.atom"));
        SyndFeed build = input.build(xmlreader);
        List<SyndEntry> feedItems = build.getEntries();
        int size = feedItems.size();
        assertTrue("Total : " + size + " au lieu de 20", size == 20);
        int i = 0;
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
    }
    
    @Test
    public void testRssRome() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, FeedException {
        SyndFeedInput input = new SyndFeedInput();
        XmlReader xmlreader = new XmlReader(new File("./test/rss.rss"));
        SyndFeed build = input.build(xmlreader);
        List<SyndEntry> feedItems = build.getEntries();
        int size = feedItems.size();
        assertTrue("Total : " + size + " au lieu de 20", size == 20);
        int i = 0;
        assertEquals("La notion de BOM avec Maven", feedItems.get(i++).getTitle());
        assertEquals("Le Protocole Direct de Sockets sous Java 7 - Ecrit une fois, exécuté partout...", feedItems.get(i++).getTitle());
        assertEquals("Repenser la propagation des exceptions avec Java 8", feedItems.get(i++).getTitle());
        assertEquals("Comment marchent les annotations et à quoi servent-elles ?", feedItems.get(i++).getTitle());
        assertEquals("Release Candidates Disponibles, Nouveaux Atomic Numbers et Stripped Implementations Abandonées", feedItems.get(i++).getTitle());
        assertEquals("[Java 8] Comment le compilateur pour Nashorn fonctionne ?", feedItems.get(i++).getTitle());
        assertEquals("JDK 8 & Lambdas, Streams et Collectors", feedItems.get(i++).getTitle());
        assertEquals("Comment fonctionne le garbage collector de Java", feedItems.get(i++).getTitle());
        assertEquals("Première publication du projet de garbage collector Shenandoah pour OpenJDK", feedItems.get(i++).getTitle());
        assertEquals("Les standards de codage de Google", feedItems.get(i++).getTitle());
        assertEquals("Questions/Réponses: Java Coding Guidelines", feedItems.get(i++).getTitle());
        assertEquals("Ceylon : un java moderne et sans legacy", feedItems.get(i++).getTitle());
        assertEquals("JHipster v0.9.0 nouvelle version du générateur Yeoman pour Spring/AngularJS", feedItems.get(i++).getTitle());
        assertEquals("[Clojure] Implémentation de structure de donnée : Liste simplement chainée", feedItems.get(i++).getTitle());
        assertEquals("Unsafe à tout prix; Oracle sonde la communauté à propos de sun.misc.Unsafe", feedItems.get(i++).getTitle());
        assertEquals("Eclipse Scout avec Jérémie Bresson", feedItems.get(i++).getTitle());
        assertEquals("Sortie de JArchitect v4.0", feedItems.get(i++).getTitle());
        assertEquals("[Multithreading] Comment choisir le bon ExecutorService ?", feedItems.get(i++).getTitle());
        assertEquals("Lambda Java 8 et scala :  Comment sont t'ils implémentés au niveau du bytecode ?", feedItems.get(i++).getTitle());
        assertEquals("Arrête de jouer, deviens un ninja ! (web framework)", feedItems.get(i++).getTitle());
    }
    
    @Test
    public void testLinuxFrRome() throws ParserConfigurationException, SAXException, IOException, IllegalArgumentException, FeedException {
        SyndFeedInput input = new SyndFeedInput();
        XmlReader xmlreader = new XmlReader(new File("./test/linuxfr.atom"));
        SyndFeed build = input.build(xmlreader);
        List<SyndEntry> feedItems = build.getEntries();
        int size = feedItems.size();
        assertTrue("Total : " + size + " au lieu de 15", size == 15);
        int i = 0;
        assertNotNull(feedItems.get(i).getContents());
        assertTrue(feedItems.get(i).getContents().size() > 0);
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
    }
    
}
