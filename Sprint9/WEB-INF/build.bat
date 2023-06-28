#compile
cd "D:/Logiciel/Tomcat/webapps/Sprint8_jar/src"
javac -d D:/Logiciel/Tomcat/webapps/Sprint8_jar/classes annotation/Anno_Url.java  
javac -d D:/Logiciel/Tomcat/webapps/Sprint8_jar/classes etu1896/framework/*.java  
javac -d D:/Logiciel/Tomcat/webapps/Sprint8_jar/classes etu1896/framework/servlet/FrontServlet.java  

# se mettre dans la repertoire contenant les packages et les .class
cd "D:/Logiciel/Tomcat/webapps/Sprint8_jar/classes"

# construire le fichier jar
jar -cvf ../framework_etu1896.jar .

# copie du jar dans le projet de test
cd ../
copy framework_etu1896.jar "D:/Logiciel/Tomcat/webapps/Sprint8/WEB-INF/lib/"


cd "D:/Logiciel/Tomcat/webapps/Sprint8"

jar -cvf test_frame.war .

move /Y test_frame.war "D:/Logiciel/Tomcat/webapps"