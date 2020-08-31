# Todo-Service

## Start running

1. First thing first
Run maven build. By default it will run goals "clean source-generate compile" defined in pom.xml
This step will make sure generating java code from proto file present at [todo.proto](src/main/proto/todo.proto) 

1. Start server. go to file [Server](src/main/java/com/aakash/grpcdemo/Server.java) and run it as java application

1. Start client to consume server methods. go to file [Client](src/main/java/com/aakash/grpcdemo/Client.java) and run it as java application


## Issues and solutions 

### After maven generate source not able to access generated classes
Check if you have added target path into your build path in eclipse. Add folder containing package in your build path

### Generated code has error related to javax not found or must override superclass method
Put default java-compiler to 1.8. Windows->preferences->java->compiler