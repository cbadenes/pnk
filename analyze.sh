#!/bin/bash
export MAVEN_OPTS="-Xmx64g"
mvn exec:java -Dexec.mainClass="es.upm.oeg.pnk.Analyze" -Dexec.classpathScope="test"
