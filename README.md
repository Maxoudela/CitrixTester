JavaFX Maven Plugin
===================

Simple application that prints the content of the System clipboard using JAVAFX API each time the "Paste from clipboard" button is clicked.


Requirements
============
* Maven 3.6 (older versions might work too)
* Java Developer Kit 8 with at least Update 202

Run
===

You can build the product with Maven
```xml
mvn clean install
```

You also can generate a fully package bundle :
```xml
mvn jfx:native
```

More information on the bundle process can be found in : https://github.com/javafx-maven-plugin/javafx-maven-plugin


