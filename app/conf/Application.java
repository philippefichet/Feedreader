package conf;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import play.api.libs.Files;
import play.libs.Json;

import com.fasterxml.jackson.databind.JsonNode;
import play.Play;

public class Application {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	protected String bootswatch = "bootswatch-cyborg/3.1.1";
	protected Map<String, String> bootswatchAvailable = new TreeMap<>();
	
	
	private static Application instance = null;
	
	protected Application() {
		bootswatchAvailable.put("amelia", "bootswatch-amelia/3.1.1");
		bootswatchAvailable.put("cerulean", "bootswatch-cerulean/3.1.1");
		bootswatchAvailable.put("cosmo", "bootswatch-cosmo/3.1.1");
		bootswatchAvailable.put("cupid", "bootswatch-cupid/3.1.1");
		bootswatchAvailable.put("cyborg", "bootswatch-cyborg/3.1.1");
		bootswatchAvailable.put("flatly", "bootswatch-flatly/3.1.1");
		bootswatchAvailable.put("journal", "bootswatch-journal/3.1.1");
		bootswatchAvailable.put("lumen", "bootswatch-lumen/3.1.1");
		bootswatchAvailable.put("readable", "bootswatch-readable/3.1.1");
		bootswatchAvailable.put("simplex", "bootswatch-simplex/3.1.1");
		bootswatchAvailable.put("slate", "bootswatch-slate/3.1.1");
		bootswatchAvailable.put("spacelab", "bootswatch-spacelab/3.1.1");
		bootswatchAvailable.put("superhero", "bootswatch-superhero/3.1.1");
		bootswatchAvailable.put("united", "bootswatch-united/3.1.1");
		bootswatchAvailable.put("yeti", "bootswatch-yeti/3.1.1");
        load();
	}

	static public synchronized Application getInstance() {
		if (instance == null) {
			instance = new Application();
		}
		return instance;
	}
	
	public void load() {
        String path = getHomeConfiguration();
		File pathfile = new File(path);
		if (!pathfile.exists()) {
			logger.warn("la Configuration de l'application est inexistante : \""
					+ pathfile.getAbsolutePath() + "\"");
			return;
		}
		if (!pathfile.isFile()) {
			logger.warn("La configuration de l'application n'est pas un fichier : \""
					+ pathfile.getAbsolutePath() + "\"");
			return;
		}
		if (!pathfile.canRead()) {
			logger.warn("Impossible de lire la configuration de l'application : \""
					+ pathfile.getAbsolutePath() + "\"");
			return;
		}
		String fileConfig = Files.readFile(pathfile);
		JsonNode jsonNode = Json.parse(fileConfig);
		JsonNode bootswatch = jsonNode.get("bootswatch");
		if (bootswatch != null) {
			setBootswatch(bootswatch.asText());
		}
		JsonNode bootswatchAvailable = jsonNode.get("bootswatchAvailable");
		if (bootswatchAvailable != null) {
			getBootswatchAvailable().clear();
			Iterator<String> i = bootswatchAvailable.fieldNames();
			while(i.hasNext()) {
				String name = i.next();
				String value = bootswatchAvailable.get(name).asText();
				getBootswatchAvailable().put(name, value);
			}
		}
		
	}
    
    /**
     * Récupére le chemin de la configuration de l'application (pas du play
     * framework)
     *
     * @param app Application lancé
     * @return Chemin de la configuration de l'application (pas du play
     * framework)
     */
    public String getHomeConfiguration() {
        String application =  Play.application().configuration().getString(
                "configuration.application");
        if (application == null) {
            String userDir = System.getProperty("user.dir") + File.separator;
            if (userDir == null) {
                userDir = "~/";
            }
            application = userDir + ".feedreader/application.json";
        }
        return application;
    }
	
	public void save() {
        String path = getHomeConfiguration();
		File pathfile = new File(path);
		if (!pathfile.canWrite()) {
			logger.error("Impossible d'ecrire la configuration de l'application \"" + pathfile.getAbsolutePath() + "\"");
		}
		if (!pathfile.exists()) {
			// @TODO création des dossiers
			String absolutePath = pathfile.getAbsolutePath();
			Files.createDirectory(new File(absolutePath.substring(0, absolutePath.lastIndexOf(File.separator))));
		}
		
		String applicationJson = Json.toJson(this).toString();
		Files.writeFile(pathfile, applicationJson);
	}
	
	public String getBootswatch() {
		return bootswatch;
	}

	public void setBootswatch(String bootswatch) {
		this.bootswatch = bootswatch;
	}

	public Map<String, String> getBootswatchAvailable() {
		return bootswatchAvailable;
	}

	public void setBootswatchAvailable(Map<String, String> bootswatchAvailable) {
		this.bootswatchAvailable = bootswatchAvailable;
	}
}
