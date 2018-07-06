# word-wrap
<a href="https://travis-ci.org/davidmoten/word-wrap"><img src="https://travis-ci.org/davidmoten/word-wrap.svg"/></a><br/>
[![Maven Central](https://maven-badges.herokuapp.com/maven-central/com.github.davidmoten/word-wrap/badge.svg?style=flat)](https://maven-badges.herokuapp.com/maven-central/com.github.davidmoten/word-wrap)<br/>
[![codecov](https://codecov.io/gh/davidmoten/word-wrap/branch/master/graph/badge.svg)](https://codecov.io/gh/davidmoten/word-wrap)

Java library for wrapping text.

Maven site reports are [here](https://davidmoten.github.io/word-wrap) including [javadoc](https://davidmoten.github.io/word-wrap/apidocs/index.html).

## Features
* Designed for use with normally formatted English (spaces after commas, periods for example)
* Can specify custom string width function (for example `FontMetrics.stringWidth`)
* Treats special characters appropriately (don't wrap a comma to the next line for example)
* Conserves leading whitespace on lines
* Optionally insert hyphens
* Easy to use and read builder

## Motivation
@davidmoten needed to render text for display in a PDF using [PDFBox](https://pdfbox.apache.org/) but PDFBox didn't offer word wrapping. He searched for libraries to do it and found Apache *commons-text* and *commons-lang* `WordUtils` but it didn't conserve leading spaces on lines and didn't allow for a customizable string width function. With a bit of luck this library will help those who are on a similar search!

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
String text = "hi there how are you going?";
System.out.println(
  WordWrap.from(text).maxWidth(10).wrap());
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
  .insertHyphens(true)
  .stringWidth(s -> s.length())
  .wrap(out);
```

Note that the `WordWrap` builder used above is quite flexible and allows you to take input from a `Reader`, `InputStream`, classpath resource, `File`, `String` and has similar options for output.

## Rewrapping classic fiction
A good test is to rewrap novels downloaded from Gutenberg (copied from the html form). When you run `mvn test` the output of *The Importance of Being Earnest*, *The Black Gang*, and *Treasure Island* are rewrapped into the directory `target` with line lengths of 20, 20 and 80 respectively. Part of the testing process for this library in addition to lots of unit tests is inspecting the wrapped output from these novels.

## Performance
The library has been built with correctness in mind rather than performance (although streaming options in the library does mean that memory use can be kept down for large texts). Later releases may include performance enhancements. If it's a priority for you then let me know!

17 July 2018:
* increased throughput by 3x
* decreased allocations by 90%

## Build
Use maven:
```bash
maven clean install
```

