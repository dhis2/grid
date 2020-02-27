package org.hisp.grid;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Two-dimensional grid structure with a set of methods for
 * manipulation.
 */
public interface Grid
{
    /**
     * Returns the grid title.
     *
     * @return the title.
     */
    String getTitle();

    /**
     * Sets the grid title.
     *
     * @param title the title.
     * @return this grid.
     */
    Grid setTitle( String title );

    /**
     * Returns the grid subtitle.
     *
     * @return the subtitle.
     */
    String getSubtitle();

    /**
     * Sets the grid subtitle.
     *
     * @param subtitle the subtitle.
     * @return this grid.
     */
    Grid setSubtitle( String subtitle );

    /**
     * Returns the grid table.
     *
     * @return the table.
     */
    String getTable();

    /**
     * Sets the grid table.
     *
     * @param table the table.
     * @return this grid.
     */
    Grid setTable( String table );

    /**
     * Returns all {@link GridHeader}.
     *
     * @return a list of grid headers.
     */
    List<GridHeader> getHeaders();

    /**
     * Returns map of meta data.
     *
     * @return the meta data as a map.
     */
    Map<String, Object> getMetaData();

    /**
     * Sets map of meta data.
     *
     * @param metaData the meta data as a map.
     * @return this grid.
     */
    Grid setMetaData( Map<String, Object> metaData );

    /**
     * Adds a key-value pair to meta-data.
     *
     * @param key the key.
     * @param value the value.
     * @return this grid.
     */
    Grid addMetaData( String key, Object value );

    /**
     * Returns all visible {@link GridHeader}, i.e. headers which are not hidden.
     *
     * @return a list of grid headers.
     */
    List<GridHeader> getVisibleHeaders();

    /**
     * Returns the index of the header with the given name.
     *
     * @param name the name of the grid header.
     * @return the index.
     */
    int getIndexOfHeader( String name );

    /**
     * Adds a header value.
     *
     * @param header the grid header.
     * @return this grid.
     */
    Grid addHeader( GridHeader header );

    /**
     * Adds a header value. Short-hand for {@code addHeader( new GridHeader( String ) )}.
     *
     * @param name the grid header name.
     * @return this grid.
     */
    Grid addHeader( String name );

    /**
     * Adds a header value at the given column index.
     *
     * @param columnIndex the column index to insert the grid header at.
     * @param header the grid header.
     * @return this grid.
     */
    Grid addHeader( int columnIndex, GridHeader header );

    /**
     * Adds a list of headers.
     *
     * @param headerIndex the index to insert the first grid header at.
     * @param headers list of headers.
     * @return this grid.
     */
    Grid addHeaders( int headerIndex, List<GridHeader> headers );

    /**
     * Adds a number of empty values to the Grid.
     *
     * @param number the number of columns to add.
     * @return this grid.
     */
    Grid addEmptyHeaders( int number );

    /**
     * Returns the current height / number of rows in the grid.
     *
     * @return the grid height.
     */
    int getHeight();

    /**
     * Returns the current width / number of columns in the grid.
     *
     * @return the grid width.
     */
    int getWidth();

    /**
     * Returns the current width / number of visible columns in the grid.
     *
     * @return the visible grid width.
     */
    int getVisibleWidth();

    /**
     * Adds a new row the the grid and moves the cursor accordingly.
     *
     * @return this grid.
     */
    Grid addRow();

    /**
     * Adds all rows of the given grid to this grid.
     *
     * @param grid the grid to add to this grid.
     * @return this grid.
     */
    Grid addRows( Grid grid );

    /**
     * Adds the value to the end of the current row.
     *
     * @param value the value to add.
     * @return this grid.
     */
    Grid addValue( Object value );

    /**
     * Adds values in the given array to the end of the current row in the
     * specified order.
     *
     * @param values the values to add.
     * @return this grid.
     */
    Grid addValues( Object[] values );

    /**
     * Adds the given values to the end of the current row in the specified
     * order.
     *
     * @param values the values to add.
     * @return this grid.
     */
    Grid addValuesVar( Object... values );

    /**
     * Adds values in the given array to the end of the current row in the
     * specified order.
     *
     * @param values the values to add.
     * @return this grid.
     */
    Grid addValuesAsList( List<Object> values );

    /**
     * Adds an empty value to the Grid at the current row.
     *
     * @return this grid.
     */
    Grid addEmptyValue();

    /**
     * Adds a number of empty values to the Grid at the current row.
     *
     * @param number the number of empty values to add.
     * @return this grid.
     */
    Grid addEmptyValues( int number );

    /**
     * Adds a number of null values to the Grid at the current row.
     *
     * @param number the number of null values to add.
     * @return this grid.
     */
    Grid addNullValues( int number );

    /**
     * Returns the row with the given index.
     *
     * @param rowIndex the index of the row.
     * @return a list of values for the row.
     */
    List<Object> getRow( int rowIndex );

    /**
     * Returns all rows.
     *
     * @return a list of list of values.
     */
    List<List<Object>> getRows();

    /**
     * Returns all visible rows, ie. rows with a corresponding header that is
     * not hidden.
     *
     * @return a list of list of values.
     */
    List<List<Object>> getVisibleRows();

    /**
     * Returns the column with the given index.
     *
     * @param columnIndex the index of the column.
     * @return a list of values.
     */
    List<Object> getColumn( int columnIndex );

    /**
     * Return the value at the given row index and the given column index.
     *
     * @param rowIndex    the row index.
     * @param columnIndex the column index.
     * @return the column value.
     * @throws IllegalArgumentException if the grid does not contain the requested row / column.
     */
    Object getValue( int rowIndex, int columnIndex );

    /**
     * Adds a new column at the end of the grid.
     *
     * @param columnValues the column values to add.
     * @throws IllegalStateException if the columnValues has different length
     *          than the rows in grid, or if the grid rows are not of the same length.
     * @return this grid.
     */
    Grid addColumn( List<Object> columnValues );

    /**
     * Adds a new column at the end of the grid.
     *
     * @param columnIndex  the index at where to insert the column.
     * @param columnValues the column values to add.
     * @throws IllegalStateException if the columnValues has different length
     *          than the rows in grid, or if the grid rows are not of the same length.
     * @return this grid.
     */
    Grid addColumn( int columnIndex, List<Object> columnValues );

    /**
     * Adds a new column at the end of the grid and populates it with the given
     * value.
     *
     * @param columnValue the value to populate the grid column with.
     * @return this grid.
     */
    Grid addAndPopulateColumn( Object columnValue );

    /**
     * Adds the given number of columns at the end of the grid and populates
     * them with the given value.
     *
     * @param columns the number of columns.
     * @param columnValue the value to populate the grid column with.
     * @return this grid.
     */
    Grid addAndPopulateColumns( int columns, Object columnValue );

    /**
     * Removes the header and column at the given index.
     *
     * @param columnIndex the column index.
     * @return this grid.
     */
    Grid removeColumn( int columnIndex );

    /**
     * Removes the header and the column at the index of the given header if it
     * exists.
     *
     * @param header the grid header.
     * @return this grid.
     */
    Grid removeColumn( GridHeader header );

    /**
     * Removes from the grid columns with corresponding headers which only contain
     * null values.
     *
     * @return this grid.
     */
    Grid removeEmptyColumns();

    /**
     * Indicates whether the column with the given index only contains null values.
     *
     * @param columnIndex the column index.
     * @return true if the column with the given index only contains null values.
     */
    boolean columnIsEmpty( int columnIndex );

    /**
     * Removes the current row from the grid.
     *
     * @return this grid.
     */
    Grid removeCurrentWriteRow();

    /**
     * Indicates whether meta data exists and contains the given key.
     *
     * @param key the meta data key.
     * @return true if meta data exists and contains the given key.
     */
    boolean hasMetaDataKey( String key );

    /**
     * Limits the grid from top by the given argument number.
     *
     * @param limit the top limit, must be greater than zero to have an effect.
     * @return this grid.
     */
    Grid limitGrid( int limit );

    /**
     * Limits the grid by the given start and end position.
     *
     * @param startPos the start position.
     * @param endPos the end position.
     * @return this grid.
     */
    Grid limitGrid( int startPos, int endPos );

    /**
     * Sorts the grid ascending on the column at the given columnIndex.
     *
     * @param columnIndex the column index, starting on 1.
     * @param order a negative value indicates ascending order, a positive value
     *         indicates descending order, zero value indicates no sorting.
     * @return this grid.
     */
    Grid sortGrid( int columnIndex, int order );

    /**
     * Adds a cumulative column to the grid. Column must hold numeric data.
     *
     * @param columnIndex the index of the base column.
     * @param addHeader indicates whether to add a grid header for the regression column.
     * @return this grid.
     */
    Grid addCumulativeColumn( int columnIndex, boolean addHeader );

    /**
     * Adds columns with cumulative values to the given grid.
     *
     * @param startColumnIndex the index of the first data column.
     * @param numberOfColumns the number of data columns.
     * @return this grid.
     */
    Grid addCumulativesToGrid( int startColumnIndex, int numberOfColumns );

    /**
     * Substitutes the grid header names and the grid values for meta type columns
     * based on the given mapping. Values are left unchanged when there is not match
     * in the given meta data map.
     *
     * @param metaDataMap meta data map of keys and value substitutions.
     * @return this grid.
     */
    Grid substituteMetaData( Map<?, ?> metaDataMap );

    /**
     * Substitutes the grid values based on the given mapping. Substitutes values
     * per row based on the given source column index and target column index.
     * Values are left unchanged when there is not match in the given meta data
     * map.
     *
     * @param sourceColumnIndex the index of the column to read values.
     * @param targetColumnIndex the index of the column to substitute values.
     * @param metaDataMap meta-data map of keys and substitutions.
     * @return this grid.
     */
    Grid substituteMetaData( int sourceColumnIndex, int targetColumnIndex, Map<?, ?> metaDataMap );

    /**
     * Returns indexes of the meta grid headers.
     *
     * @return a list of indexes.
     */
    List<Integer> getMetaColumnIndexes();

    /**
     * Returns the unique set of values from the grid column with the given name.
     * The name refers to the name of the grid header of the column.
     *
     * @param columnName name of the column grid header.
     * @return a set of unique values.
     */
    Set<Object> getUniqueValues( String columnName );

    /**
     * Returns a map of each row in the grid.
     *
     * @param valueIndex the index of the column to use as map values.
     * @param keySeparator the separator to use to concatenate the map key.
     * @param <T> type.
     * @return a map of each row.
     */
    <T> Map<String, T> getAsMap( int valueIndex, String keySeparator );
}
