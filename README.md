# README

To run this project, compile it using `mvn package` and then execute this command (with line breaks removed):

```
java -cp "$HOME/.m2/repository/org/apache/logging/log4j/log4j-api/2.17.1/log4j-api-2.17.1.jar:
    $HOME/.m2/repository/org/apache/logging/log4j/log4j-core/2.17.1/log4j-core-2.17.1.jar:
    $HOME/.m2/repository/org/apache/logging/log4j/log4j-jpl/2.17.1/log4j-jpl-2.17.1.jar:
    target/log4j-jpl-repo-1.0-SNAPSHOT.jar" \
    org.example.App
```

Be sure to use Java 11.

The output will be something like this:

```
Hello World!
Object has been serialized
ObjectInputFilter {0}: {1}, array length: {2}, nRefs: {3}, depth: {4}, bytes: {5}, ex: {6}
Object has been deserialized
object1 equals object2? true
```

The bug is the log message from `ObjectInputFilter` which is part of OpenJDK. The JPL module does not seem to
expand the parameters correctly. This seems to be the case when you look at the source code of that class where
you find this:

```java
Logging.filterLogger.log(status == null || status == ObjectInputFilter.Status.REJECTED
            ? Logger.Level.DEBUG
            : Logger.Level.TRACE,
    "ObjectInputFilter {0}: {1}, array length: {2}, nRefs: {3}, depth: {4}, bytes: {5}, ex: {6}",
    status, clazz, arrayLength, totalObjectRefs, depth, bytesRead,
    Objects.toString(ex, "n/a"));
```

# Copyright

All code and information in this repository is released into the public domain. Where such thing is not possible,
it is released by the copyright holder, Curity AB, under the MIT license.
