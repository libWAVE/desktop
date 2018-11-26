set JRE_HOME=c:\tools\Java\jdk1.8.0_45\jre
set JAVA_HOME=c:\tools\Java\jdk1.8.0_45
set PATH=%PATH%;c:\tools\Java\jdk8.0_45\bin;

set MAVEN_OPTS=-Xmx2G
mvn clean install -DskipTests=true