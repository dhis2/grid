package org.hisp.grid;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Grid header.
 */
public class GridHeader
{
    private String name;

    private String column;
    
    private ValueType valueType;

    private boolean hidden;

    private boolean meta;

    // -------------------------------------------------------------------------
    // Constructors
    // -------------------------------------------------------------------------

    public GridHeader()
    {
    }

    /**
     * @param name name
     */
    public GridHeader( String name )
    {
        this.name = name;
        this.column = name;
        this.valueType = ValueType.TEXT;
        this.hidden = false;
        this.meta = false;
    }

    /**
     * @param name name
     * @param column column
     */
    public GridHeader( String name, String column )
    {
        this( name );
        this.column = column;
    }

    /**
     * @param name name
     * @param hidden hidden
     * @param meta meta
     */
    public GridHeader( String name, boolean hidden, boolean meta )
    {
        this( name );
        this.hidden = hidden;
        this.meta = meta;
    }

    /**
     * @param name name
     * @param column column
     * @param valueType valueType
     * @param hidden hidden
     * @param meta meta
     */
    public GridHeader( String name, String column, ValueType valueType, boolean hidden, boolean meta )
    {
        this( name, column );
        this.valueType = valueType;
        this.hidden = hidden;
        this.meta = meta;
    }

    // -------------------------------------------------------------------------
    // Getters and setters
    // -------------------------------------------------------------------------

    @JsonProperty
    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    @JsonProperty
    public String getColumn()
    {
        return column;
    }

    public void setColumn( String column )
    {
        this.column = column;
    }

    @JsonProperty
    public ValueType getValueType()
    {
        return valueType;
    }

    public void setValueType( ValueType valueType )
    {
        this.valueType = valueType;
    }

    @JsonProperty
    public boolean isHidden()
    {
        return hidden;
    }

    public void setHidden( boolean hidden )
    {
        this.hidden = hidden;
    }

    @JsonProperty
    public boolean isMeta()
    {
        return meta;
    }

    public void setMeta( boolean meta )
    {
        this.meta = meta;
    }

    // -------------------------------------------------------------------------
    // hashCode, equals, toString
    // -------------------------------------------------------------------------

    @Override
    public int hashCode()
    {
        return name.hashCode();
    }

    @Override
    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null )
        {
            return false;
        }

        if ( getClass() != object.getClass() )
        {
            return false;
        }

        final GridHeader other = (GridHeader) object;

        return name.equals( other.name );
    }

    @Override
    public String toString()
    {
        return "[Name: " + name + ", column: " + column + ", value type: " + valueType + "]";
    }
}
