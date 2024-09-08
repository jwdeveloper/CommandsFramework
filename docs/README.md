#### 1. Install the dependencies

> Make sure you are installing the latest version!

#### [Check out the current version](https://central.sonatype.com/artifact/io.github.jwdeveloper.spigot.commands/CommandsFramework)

```xml

<dependency>
    <groupId>io.github.jwdeveloper.spigot.commands</groupId>
    <artifactId>commands-framework-core</artifactId>
    <version>1.0.8</version>
    <scope>compile</scope>
</dependency>
```

<details>

<summary>
How to fix this error?
<br>
java.lang.NoClassDefFoundError: io/github/jwdeveloper/spigot/commands/CommandsFramework
</summary>


This is the common error while using the maven dependencies to your plugin.
The error means that the dependency classes `has not been included` in the output jar file.

#### Fix it by using the `Maven Shade Plugin`

> Maven shade plugin is a "script" that runs during the compilation time and copy all the dependencies
> classes into the output jar

Copy this into the `pom.xml`

```xml

<build>
    <plugins>
        <plugin>
            <groupId>org.apache.maven.plugins</groupId>
            <artifactId>maven-shade-plugin</artifactId>
            <version>3.5.3</version>
            <executions>
                <execution>
                    <phase>package</phase>
                    <goals>
                        <goal>shade</goal>
                    </goals>
                </execution>
            </executions>
        </plugin>
    </plugins>
</build>
```

After do it, reload the `pom.xml` and compile the project with maven `install` action

The output jar should be into the folder `/yourproject/target`
</details>

____

#### 2. Create a command

There are three different ways of registering commands

- [Builder](#builder) - default way of creating commands it has the most features
- [Pattern](#pattern) - simpler way of creating command, create them as string `/hello <arg1:Text> <arg2:Number>`
- [Annotations](#annotations) - the simplest way, it uses `Annotations` over methods to register new command

## Builder

`Builder` - The essential approach of a command registration.
A command is created by using chained fluent builder.
It gives you access to all the properties and features.

> The name `builder` comes from the design pattern [Builder](https://refactoring.guru/design-patterns/builder)

[filename](code/basic-builder.md ':include')

## Pattern

`Pattern` - The pattern is higher abstraction. It parses string
pattern into a builder, that can be later adjusted.

[filename](code/basic-pattern.md ':include')

## Annotations

`Annotations` - The highest abstraction, very simple to use. By using the `@FCommand` over
methods or class you can simply create new command

[filename](code/basic-annotations.md ':include')
