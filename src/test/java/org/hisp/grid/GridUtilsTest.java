package org.hisp.grid;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringWriter;

import org.hisp.grid.csv.CsvWriteOptions;
import org.junit.jupiter.api.Test;

class GridUtilsTest
{
    @Test
    public void testToCsv()
        throws IOException
    {
        Grid grid = new ListGrid()
            .addHeader( "Name" )
            .addHeader( "Period" )
            .addHeader( "Organisation unit" )
            .addHeader( "Value" )
            .addRow().addValuesVar( "Penta1 doses given", "Apr to Jun 2019", "Bombali", 5128.0 )
            .addRow().addValuesVar( "Fully Immunized child", "Oct to Dec 2019", "Moyamba", "3017.0" )
            .addRow().addValuesVar( "Penta1 doses given", "Oct to Dec 2019", "Bombali", "2873.0" )
            .addRow().addValuesVar( "Fully Immunized child", "Jul to Sep 2019", "Moyamba", "3357.0" );

        StringWriter writer = new StringWriter();

        CsvWriteOptions options = new CsvWriteOptions()
            .setDelimiter( ',' )
            .setForceQualifier( false );

        GridUtils.toCsv( grid, writer, options );

        String output = writer.toString();

        assertNotNull( output );
        assertTrue( output.startsWith( "Name,Period,Organisation unit,Value" ) );

    }
}
