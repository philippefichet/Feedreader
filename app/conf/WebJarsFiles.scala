package conf

object WebJarsFiles {
	def locate(file: String) : String = {
	  return controllers.WebJarAssets.locate(file);
	}
}