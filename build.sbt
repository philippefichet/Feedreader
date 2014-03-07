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
  "org.webjars" %% "webjars-play" % "2.2.0",
  "org.hibernate" % "hibernate-entitymanager" % "3.6.9.Final",
  "com.h2database" % "h2" % "1.3.175",
  //"org.apache.httpcomponents" % "httpclient" % "4.3.2",
  //"org.apache.directory.studio" % "org.apache.commons.io" % "2.4",
  // webjars
  "org.webjars" % "angularjs" % "1.2.9",
  "org.webjars" % "bootstrap" % "3.0.3"
  // Add your own project dependencies in the form:
  // "group" % "artifact" % "version"
)

play.Project.playScalaSettings
