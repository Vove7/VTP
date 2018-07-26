# VTP

> - Vove's Tools Package
> - Android个人开发工具包

## Usage

* Step 1. Add the JitPack repository to your build file

gradle:
```groovy
allprojects {
    repositories {
        //...
        maven { url 'https://jitpack.io' }
    }
}
```
maven:
```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>
```

* Step 2. Add the dependency
gradle:
```groovy
dependencies {
    implementation 'com.github.Vove7:VTP:0.0.3'
}
```
maven:
```xml
<dependency>
    <groupId>com.github.Vove7</groupId>
    <artifactId>VTP</artifactId>
    <version>0.0.3</version>
</dependency>
```