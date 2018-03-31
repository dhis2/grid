package org.hisp.grid;

import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;

import com.csvreader.CsvWriter;

/**
 * Utility methods for {@link Grid}.
 */
public class GridUtils
{
    private static final char CSV_DELIMITER = ',';
    private static final String EMPTY = "";

    /**
     * Renders the given {@link Grid} in CSV format. Writes the
     * content to the given {@link Writer}.
     * 
     * @param grid the grid.
     * @param writer the writer.
     * @throws IOException for errors during rendering.
     */
    public static void toCsv( Grid grid, Writer writer )
        throws IOException
    {
        if ( grid == null )
        {
            return;
        }

        CsvWriter csvWriter = new CsvWriter( writer, CSV_DELIMITER );

        Iterator<GridHeader> headers = grid.getHeaders().iterator();

        if ( !grid.getHeaders().isEmpty() )
        {
            while ( headers.hasNext() )
            {
                csvWriter.write( headers.next().getName() );
            }

            csvWriter.endRecord();
        }

        for ( List<Object> row : grid.getRows() )
        {
            Iterator<Object> columns = row.iterator();

            while ( columns.hasNext() )
            {
                Object value = columns.next();

                csvWriter.write( value != null ? String.valueOf( value ) : EMPTY );
            }

            csvWriter.endRecord();
        }
    }
}
