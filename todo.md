- The Api/Core modules should not have dependencies to the Spigot


```cmd
#Bump version in the CommandsFramework project!
mvn versions:set -DnewVersion=1.0.2
mvn deploy

#Update version in the example project
mvn clean install -Dcommands.version=2.0.0
```
  