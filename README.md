### Grid

Grid is a Java library which provides a grid structure. The grid is annotated with Jackson annotations and can be rendered to JSON and XML using a Jackson rendered. It can be rendered as CSV using the GridUtils utility class.

Sample usage:

```
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
