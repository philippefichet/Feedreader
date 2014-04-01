name := """feedreaderplay"""

version := "1.0-SNAPSHOT"

libraryDependencies ++= Seq(
  // Select Play modules
  jdbc,      // The JDBC connection pool and the play.api.db API
  //anorm,     // Scala RDBMS Library
  javaJdbc,  // Java database API
  //javaEbean, // Java Ebean plugin
  javaJpa,   // Java JPA plugin
  //filters,   // A set of built-in filters
  javaCore,  // The core Java API
  // WebJars pull in client-side web libraries
  "org.webjars" %% "webjars-play" % "2.2.1-2",
  "org.hibernate" % "hibernate-entitymanager" % "4.3.4.Final",
  "com.h2database" % "h2" % "1.3.175",
  "org.hsqldb" % "hsqldb" % "2.3.2",
  //"org.apache.httpcomponents" % "httpclient" % "4.3.2",
  //"org.apache.directory.studio" % "org.apache.commons.io" % "2.4",
  // webjars
  "org.webjars" % "angularjs" % "1.2.9",
  // webjars bootstrap & theme
  "org.webjars" % "bootstrap" % "3.1.1",
  "org.webjars" % "bootswatch-amelia" % "3.1.1",
  "org.webjars" % "bootswatch-cerulean" % "3.1.1",
  "org.webjars" % "bootswatch-cosmo" % "3.1.1",
  "org.webjars" % "bootswatch-cupid" % "3.1.1",
  "org.webjars" % "bootswatch-cyborg" % "3.1.1",
  "org.webjars" % "bootswatch-flatly" % "3.1.1",
  "org.webjars" % "bootswatch-journal" % "3.1.1",
  "org.webjars" % "bootswatch-lumen" % "3.1.1",
  "org.webjars" % "bootswatch-readable" % "3.1.1",
  "org.webjars" % "bootswatch-simplex" % "3.1.1",
  "org.webjars" % "bootswatch-slate" % "3.1.1",
  "org.webjars" % "bootswatch-spacelab" % "3.1.1",
  "org.webjars" % "bootswatch-superhero" % "3.1.1",
  "org.webjars" % "bootswatch-united" % "3.1.1",
  "org.webjars" % "bootswatch-yeti" % "3.1.1"
  // Add your own project dependencies in the form:
  // "group" % "artifact" % "version"
)

play.Project.playScalaSettings
