package org.hisp.grid.csv;

public class CsvWriteOptions
{
    private char delimiter;

    private boolean forceQualifier;

    public char getDelimiter()
    {
        return delimiter;
    }

    public CsvWriteOptions()
    {
        delimiter = ',';
        forceQualifier = false;
    }

    public CsvWriteOptions setDelimiter( char delimiter )
    {
        this.delimiter = delimiter;
        return this;
    }

    public boolean isForceQualifier()
    {
        return forceQualifier;
    }

    public CsvWriteOptions setForceQualifier( boolean forceQualifier )
    {
        this.forceQualifier = forceQualifier;
        return this;
    }
}
