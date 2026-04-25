[![License](https://img.shields.io/github/license/toolarium/toolarium-jwebserver)](https://github.com/toolarium/toolarium-jwebserver/blob/master/LICENSE)
[![Maven Central](https://img.shields.io/maven-central/v/com.github.toolarium/toolarium-jwebserver/1.2.8)](https://search.maven.org/artifact/com.github.toolarium/toolarium-jwebserver/1.2.8/jar)
[![javadoc](https://javadoc.io/badge2/com.github.toolarium/toolarium-jwebserver/javadoc.svg)](https://javadoc.io/doc/com.github.toolarium/toolarium-jwebserver)

# toolarium-jwebserver

Implements a simple webserver based on the undertow.

## Features
 * Deliver static content either from a file directory or from classpath
 * Listening mode to bring a file directory to the web
 * Reverse proxy: a comma-separated list of URLs are load balanced, called (the list can contain environment variables or system properties in notation: ${...}).
 * SSL support for proxy: If no certificate is defined, a self-signed certificate is created.
 * Basic authentication support (via `--basicauth user:password` or `--basicauth env:ENV_VAR_NAME` for secure credential loading).
 * Health check endpoint (configurable path, protected by auth when enabled).
 * Path traversal protection for resource access.
 * Graceful shutdown on SIGTERM/SIGINT signals.
 * The jwebserver.properties inside the jar file can be used for static configurations (environment variables and system properties are resolved in notation: ${...}).

## Built With

* [cb](https://github.com/toolarium/common-build) - The toolarium common build

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/toolarium/toolarium-jwebserver/tags). 


### Gradle:

```groovy
dependencies {
    implementation "com.github.toolarium:toolarium-jwebserver:1.2.8"
}
```

### Maven:

```xml
<dependency>
    <groupId>com.github.toolarium</groupId>
    <artifactId>toolarium-jwebserver</artifactId>
    <version>1.2.8</version>
</dependency>
```

## Usage

### Provide included webpage (classpath)
```
java -jar toolarium-jwebserver-1.2.8.jar
```

### Listening on a directory
```
java -jar toolarium-jwebserver-1.2.8.jar -l
```

### Listening on a specific directory
```
java -jar toolarium-jwebserver-1.2.8.jar -d src/test/resources -l
```

### Sample setting welcome files
```
java -jar toolarium-jwebserver-1.2.8.jar -d src/test/resources --welcomeFiles=index.json,testfile.json
curl -v http://localhost:8080/mypath/subpath/addition
{ "a": "b" }
```

### Sample setting welcome files and disable resolve parent resource if not found
```
java -jar toolarium-jwebserver-1.2.8.jar -d src/test/resources --welcomeFiles=index.json,testfile.json --disableResolveParentResourceIfNotFound
curl -v http://localhost:8080/mypath/subpath/addition
(returns a 403)
```

### Basic authentication with environment variable
```
export JWEBSERVER_AUTH="user:password"
java -jar toolarium-jwebserver-1.2.8.jar -d src/test/resources --basicauth env:JWEBSERVER_AUTH
```

### Proxy demo: start first webserver to listen on local directory on secure port
```
# start first webserver to listen on local directory on secure port with self-signed certificate
java -jar toolarium-jwebserver-1.2.8.jar -s 8443 -l

# start proxy to listen on previous started webserver (trustAll because of self-signed certificate)
java -jar toolarium-jwebserver-1.2.8.jar -s 8444 --trustAll --proxy https://localhost:8443
```
