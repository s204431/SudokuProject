#How to run this program
##Before running the program

####Check the version of your JDK
Depending on your JDK version, the Java Swing library might
be different in terms of size in the GUI. 
We recommend that you use a java version that is at least version
8 or higher. 
> We used version 17 for development, if possible, use 
this version.
##Convert project to maven project (Necessary for multiplayer and UI testing)
You need to have added _Maven_ to the project, otherwise
it won't work.
###Download Maven 
Go to the website https://maven.apache.org/download.cgi and download
the zip file that ends with _apache-maven-3.8.6-bin.zip_

Follow the rest of the download guide and set up maven correctly.

If the project is not already converted to a maven project use these guides to do so:

For IntelliJ:
> https://www.jetbrains.com/help/idea/convert-a-regular-project-into-a-maven-project.html

For eclipse: 
> https://crunchify.com/how-to-convert-existing-java-project-to-maven-in-eclipse/

##Download jSpace (Necessary for multiplayer)
jSpace makes multiplayer accessible.
The GitHub repository can be seen below, and the installation guide
is included in this link:
> https://github.com/pSpaces/jSpace

##Download abbot (Necessary for UI tests)
If running UI tests is desired, you have to download the abbot
library

> https://mvnrepository.com/artifact/abbot/abbot/1.4.0

Follow this guide and set up your project.

##Starting the program
The program can be started in your IDE by running the Main.java file.
The UI tester can be started in your IDE by running the UITester.java file.
