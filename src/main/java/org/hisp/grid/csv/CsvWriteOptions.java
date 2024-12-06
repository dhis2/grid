package org.hisp.grid.csv;

/**
 * CSV writing options.
 */
public class CsvWriteOptions
{
    /**
     * Column delimiter.
     */
    private char delimiter;

    /**
     * Whether to force a qualifier.
     */
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
