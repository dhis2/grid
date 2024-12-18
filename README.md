# Grid

## Overview

Grid is a Java library which provides a two-dimensional grid structure. A grid has a set of rows and columns. Each column can have a corresponding header. The grid implementation is annotated with Jackson annotations and can be rendered to JSON and XML using a Jackson renderer. A grid can be rendered to CSV and HTML using the `GridUtils` utility class.

## Requirements

Java 17 is required.

## Maven

You can use Grid with [Maven](https://central.sonatype.com/artifact/org.hisp/grid):

```xml
<dependency>
  <groupId>org.hisp</groupId>
  <artifactId>grid</artifactId>
  <version>${grid.version}</version>
</dependency>
```

## Get started

Create a grid and add headers and rows in fluent style:

```java
Grid grid = new ListGrid()
  .setTitle("People")
  .addHeader(new GridHeader("Name", "name"))
  .addHeader(new GridHeader("Email", "email"))
  .addHeader(new GridHeader("Phone", "phone"));

for (Person person : people) {
  grid.addRow()
    .addValue(person.getName())
    .addValue(person.getEmail())
    .addValue(person.getPhone());
}
```

Render to CSV (`Writer` retrieved e.g. from `HttpServletResponse`):

```java
Writer writer = response.getWriter();
response.setContentType("text/csv");

GridUtils.toCsv(grid, writer);
```

Render to CSV with options:

```java
CsvWriteOptions options = CsvWriteOptions.standard()
  .withDelimiter(',')
  .withForceQualifier();

GridUtils.toCsv(grid, writer, options);
```

Render to HTML:

```java
Writer writer = reponse.getWriter();
response.setContentType("text/html");

GridUtils.toHtml(grid, writer);
```

Render to HTML with options:

```java
HtmlWriteOptions options = HtmlWriteOptions.standard()
  .withLineNumbers();

GridUtils.toHtml(grid, writer, options);
```
