# Grid

Grid is a Java library which provides a two-dimensional grid structure. A grid has a set of rows and columns. Each column can have a corresponding header. The grid implementation is annotated with Jackson annotations and can be rendered to JSON and XML using a Jackson renderer. It can be rendered to CSV using the GridUtils utility class.

## Maven

You can use Grid with Maven:

```xml
<dependency>
  <groupId>org.hisp</groupId>
  <artifactId>grid</artifactId>
  <version>1.0.0</version>
</dependency>
```

## Sample Usage

To create a grid and add some headers and rows:

```java
Grid grid = new ListGrid()
  .setTitle( "Clients" )
  .addHeader( new GridHeader( "Name", "name" ) )
  .addHeader( new GridHeader( "Email", "email" ) )
  .addHeader( new GridHeader( "Phone", "phone" ) );

for ( Person person : people )
{
  grid.addRow()
    .addValue( person.getName() )
    .addValue( person.getEmail() )
    .addValue( person.getPhone() );
}
```

Render to CSV (Writer retrieved e.g. from HttpServletResponse):

```java
Writer writer = response.getWriter();
GridUtils.toCsv( grid, writer );
```

## Javadoc

You can find [Javadoc here](https://ci.dhis2.org/job/grid-javadoc/javadoc/). The main interface is [Grid](https://ci.dhis2.org/job/grid-javadoc/javadoc/org/hisp/grid/Grid.html).
