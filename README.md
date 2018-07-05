# word-wrap
<a href="https://travis-ci.org/davidmoten/word-wrap"><img src="https://travis-ci.org/davidmoten/word-wrap.svg"/></a><br/>
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.davidmoten/word-wrap/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.github.davidmoten/word-wrap)<br/>
[![codecov](https://codecov.io/gh/davidmoten/word-wrap/branch/master/graph/badge.svg)](https://codecov.io/gh/davidmoten/word-wrap)

Java library for wrapping text.

## Features
* Can specify custom string width function (for example `FontMetrics.stringWidth`)
* Treats special characters appropriately (don't wrap a comma to the next line for example)
* Conserves leading whitespace on lines

## Getting started
Add this to your pom:

```xml
<dependency>
  <groupId>com.github.davidmoten</groupId>
  <artifactId>word-wrap</artifactId>
  <version>VERSION_HERE</version>
</dependency>
```

## Usage
```java
String text="hi there how are you going?";
System.out.println(WordWrap.from(text).maxWidth(10).wrap());
```
Output:
```
hi there
how are
you going?
```
More options:

```java
Reader in = ...
Writer out = ...
String newLine = "\r\n";
WordWrap
  .from(in)
  .maxWidth(4.5)
  .newLine("\r\n")
  .includeWordChars("~")
  .excludeWordChars("_")
  .stringWidth(s -> s.length())
  .wrap(out);
```

Note that the `WordWrap` builder used above is quite flexible and allows you to take input from a `Reader`, `InputStream`, classpath resource, `File`, `String` and has similar options for output.

## Build
Use maven:
```bash
maven clean install
```

