/*
 * Copyright (c) 2004-2024, University of Oslo
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * Redistributions of source code must retain the above copyright notice, this
 * list of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * Neither the name of the HISP project nor the names of its contributors may
 * be used to endorse or promote products derived from this software without
 * specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 * ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 * ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.hisp.grid;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import org.hisp.grid.serializer.JacksonRowDataSerializer;

/**
 * Implementation of the {@link Grid} interface using {@link ArrayList}. This implementation is
 * annotated with {@code Jackson} annotations and hence can be rendered as JSON and XML using
 * Jackson.
 */
public class ListGrid implements Grid {
  private static final String CUMULATIVE_SUFFIX = "_cumulative";

  /** The title of the grid. */
  private String title;

  /** The subtitle of the grid. */
  private String subtitle;

  /** The name of a potential corresponding table. */
  private String table;

  /** A List which represents the mn headers of the grid. */
  private List<GridHeader> headers;

  /** A Map which can hold arbitrary meta-data. */
  private Map<String, Object> metaData;

  /**
   * A two dimensional List which simulates a grid where the first list represents rows and the
   * second represents columns.
   */
  private List<List<Object>> grid;

  /** Indicating the current row in the grid for writing data. */
  private int currentRowWriteIndex = -1;

  /** Represents a mapping between column names and the index of the column in the grid. */
  private Map<String, Integer> columnIndexMap = new HashMap<>();

  /** Default constructor. */
  public ListGrid() {
    this.headers = new ArrayList<>();
    this.metaData = new HashMap<>();
    this.grid = new ArrayList<>();
  }

  /**
   * @param metaData meta data.
   */
  public ListGrid(Map<String, Object> metaData) {
    this.headers = new ArrayList<>();
    this.metaData = metaData;
    this.grid = new ArrayList<>();
  }

  // ---------------------------------------------------------------------
  // Public methods
  // ---------------------------------------------------------------------

  @Override
  @JsonProperty
  public String getTitle() {
    return title;
  }

  @Override
  public Grid setTitle(String title) {
    this.title = title;

    return this;
  }

  @Override
  @JsonProperty
  public String getSubtitle() {
    return subtitle;
  }

  @Override
  public Grid setSubtitle(String subtitle) {
    this.subtitle = subtitle;

    return this;
  }

  @Override
  @JsonProperty
  public String getTable() {
    return table;
  }

  @Override
  public Grid setTable(String table) {
    this.table = table;

    return this;
  }

  @Override
  public Grid addHeader(GridHeader header) {
    headers.add(header);

    updateColumnIndexMap();

    return this;
  }

  @Override
  public Grid addHeader(String name) {
    return addHeader(new GridHeader(name));
  }

  @Override
  public Grid addHeader(int headerIndex, GridHeader header) {
    headers.add(headerIndex, header);

    updateColumnIndexMap();

    return this;
  }

  @Override
  public Grid addHeaders(int headerIndex, List<GridHeader> gridHeaders) {
    if (gridHeaders == null || gridHeaders.isEmpty()) {
      return this;
    }

    for (int i = gridHeaders.size() - 1; i >= 0; i--) {
      headers.add(headerIndex, gridHeaders.get(i));
    }

    updateColumnIndexMap();

    return this;
  }

  @Override
  public Grid addEmptyHeaders(int number) {
    for (int i = 0; i < number; i++) {
      headers.add(new GridHeader("", false, false));
    }

    updateColumnIndexMap();

    return this;
  }

  @Override
  @JsonProperty
  public List<GridHeader> getHeaders() {
    return headers;
  }

  @Override
  @JsonIgnore
  public List<GridHeader> getVisibleHeaders() {
    List<GridHeader> tempHeaders = new ArrayList<>();

    for (GridHeader header : headers) {
      if (!header.isHidden()) {
        tempHeaders.add(header);
      }
    }

    return tempHeaders;
  }

  @Override
  @JsonIgnore
  public int getIndexOfHeader(String name) {
    return headers.indexOf(new GridHeader(name, null));
  }

  @Override
  @JsonProperty
  public int getHeight() {
    return grid != null && grid.size() > 0 ? grid.size() : 0;
  }

  @Override
  @JsonProperty
  public int getWidth() {
    verifyGridState();

    return grid != null && grid.size() > 0 ? grid.get(0).size() : 0;
  }

  @Override
  @JsonProperty
  public Map<String, Object> getMetaData() {
    return metaData;
  }

  @Override
  public Grid setMetaData(Map<String, Object> metaData) {
    this.metaData = metaData;
    return this;
  }

  @Override
  public Grid addMetaData(String key, Object value) {
    this.metaData.put(key, value);
    return this;
  }

  @Override
  @JsonIgnore
  public int getVisibleWidth() {
    verifyGridState();

    return grid != null && grid.size() > 0 ? getVisibleRows().get(0).size() : 0;
  }

  @Override
  public Grid addRow() {
    grid.add(new ArrayList<>());

    currentRowWriteIndex++;

    return this;
  }

  @Override
  public Grid addRows(Grid grid) {
    List<List<Object>> rows = grid.getRows();

    for (List<Object> row : rows) {
      this.grid.add(row);

      currentRowWriteIndex++;
    }

    return this;
  }

  @Override
  public Grid addValue(Object value) {
    grid.get(currentRowWriteIndex).add(value);

    return this;
  }

  @Override
  public Grid addValues(Object[] values) {
    List<Object> row = grid.get(currentRowWriteIndex);

    for (Object value : values) {
      row.add(value);
    }

    return this;
  }

  @Override
  public Grid addValuesVar(Object... values) {
    return addValues(values);
  }

  @Override
  public Grid addValuesAsList(List<Object> values) {
    return addValues(values.toArray());
  }

  @Override
  public Grid addEmptyValue() {
    addValue("");

    return this;
  }

  @Override
  public Grid addEmptyValues(int number) {
    for (int i = 0; i < number; i++) {
      addEmptyValue();
    }

    return this;
  }

  @Override
  public Grid addNullValues(int number) {
    for (int i = 0; i < number; i++) {
      addValue(null);
    }

    return this;
  }

  @Override
  public List<Object> getRow(int rowIndex) {
    return grid.get(rowIndex);
  }

  @Override
  @JsonProperty
  @JsonSerialize(using = JacksonRowDataSerializer.class)
  public List<List<Object>> getRows() {
    return grid;
  }

  @Override
  @JsonIgnore
  public List<List<Object>> getVisibleRows() {
    verifyGridState();

    List<List<Object>> tempGrid = new ArrayList<>();

    if (headers != null && headers.size() > 0) {
      for (List<Object> row : grid) {
        List<Object> tempRow = new ArrayList<>();

        for (int i = 0; i < row.size(); i++) {
          if (!headers.get(i).isHidden()) {
            tempRow.add(row.get(i));
          }
        }

        tempGrid.add(tempRow);
      }
    }

    return tempGrid;
  }

  @Override
  public List<Object> getColumn(int columnIndex) {
    List<Object> column = new ArrayList<>();

    for (List<Object> row : grid) {
      column.add(row.get(columnIndex));
    }

    return column;
  }

  @Override
  public Object getValue(int rowIndex, int columnIndex) {
    if (grid.size() < rowIndex
        || grid.get(rowIndex) == null
        || grid.get(rowIndex).size() < columnIndex) {
      throw new IllegalArgumentException("Grid does not contain the requested row / column");
    }

    return grid.get(rowIndex).get(columnIndex);
  }

  @Override
  public Grid addColumn(List<Object> columnValues) {
    verifyGridState();

    int currentRowIndex = 0;
    int currentColumnIndex = 0;

    if (grid.size() != columnValues.size()) {
      throw new IllegalStateException(
          String.format(
              "Number of column values (%d) is not equal to number of rows (%d)",
              columnValues.size(), grid.size()));
    }

    for (int i = 0; i < grid.size(); i++) {
      grid.get(currentRowIndex++).add(columnValues.get(currentColumnIndex++));
    }

    return this;
  }

  @Override
  public Grid addColumn(int columnIndex, List<Object> columnValues) {
    verifyGridState();

    int currentRowIndex = 0;
    int currentColumnIndex = 0;

    if (grid.size() != columnValues.size()) {
      throw new IllegalStateException(
          String.format(
              "Number of column values (%d) is not equal to number of rows (%d)",
              columnValues.size(), grid.size()));
    }

    for (int i = 0; i < grid.size(); i++) {
      grid.get(currentRowIndex++).add(columnIndex, columnValues.get(currentColumnIndex++));
    }

    return this;
  }

  @Override
  public Grid addAndPopulateColumn(Object columnValue) {
    verifyGridState();

    for (int i = 0; i < getHeight(); i++) {
      grid.get(i).add(columnValue);
    }

    return this;
  }

  @Override
  public Grid addAndPopulateColumns(int columns, Object columnValue) {
    verifyGridState();

    for (int i = 0; i < columns; i++) {
      addAndPopulateColumn(columnValue);
    }

    return this;
  }

  @Override
  public Grid removeEmptyColumns() {
    if (getWidth() == 0) {
      return this;
    }

    int lastCol = getWidth() - 1;

    for (int i = lastCol; i >= 0; i--) {
      if (columnIsEmpty(i)) {
        removeColumn(i);
      }
    }

    return this;
  }

  @Override
  public boolean columnIsEmpty(int columnIndex) {
    verifyGridState();

    for (List<Object> row : grid) {
      Object val = row.get(columnIndex);

      if (val != null) {
        return false;
      }
    }

    return true;
  }

  @Override
  public Grid removeColumn(int columnIndex) {
    verifyGridState();

    if (headers.size() > 0) {
      headers.remove(columnIndex);
    }

    for (List<Object> row : grid) {
      row.remove(columnIndex);
    }

    updateColumnIndexMap();

    return this;
  }

  @Override
  public Grid removeColumn(GridHeader header) {
    int index = headers.indexOf(header);

    if (index != -1) {
      removeColumn(index);
    }

    return this;
  }

  @Override
  public Grid removeCurrentWriteRow() {
    grid.remove(currentRowWriteIndex);

    currentRowWriteIndex--;

    return this;
  }

  @Override
  public boolean hasMetaDataKey(String key) {
    return metaData != null && metaData.containsKey(key);
  }

  @Override
  public Grid limitGrid(int limit) {
    if (limit < 0) {
      throw new IllegalStateException("Illegal limit: " + limit);
    }

    if (limit > 0 && limit <= getHeight()) {
      grid = grid.subList(0, limit);
    }

    return this;
  }

  @Override
  public Grid limitGrid(int startPos, int endPos) {
    if (startPos < 0 || endPos < startPos || endPos > getHeight()) {
      throw new IllegalStateException(
          "Illegal start or end pos: " + startPos + ", " + endPos + ", " + getHeight());
    }

    grid = grid.subList(startPos, endPos);

    return this;
  }

  @Override
  public Grid sortGrid(int columnIndex, int order) {
    if (order == 0 || !hasValues()) {
      return this;
    }

    columnIndex--;

    if (columnIndex < 0 || columnIndex >= getWidth()) {
      throw new IllegalArgumentException("Column index out of bounds: " + columnIndex);
    }

    Collections.sort(grid, new GridRowComparator(columnIndex, order));

    return this;
  }

  @Override
  public Grid addCumulativeColumn(int columnIndex, boolean addHeader) {
    verifyGridState();

    List<Object> column = getColumn(columnIndex);

    List<Object> cumulativeColumn = new ArrayList<>();

    double sum = 0d;

    for (Object value : column) {
      double number = value != null ? Double.parseDouble(String.valueOf(value)) : 0d;

      sum += number;

      cumulativeColumn.add(sum);
    }

    addColumn(cumulativeColumn);

    if (addHeader && columnIndex < headers.size()) {
      GridHeader header = headers.get(columnIndex);

      if (header != null) {
        GridHeader regressionHeader =
            new GridHeader(
                header.getName() + CUMULATIVE_SUFFIX,
                header.getColumn() + CUMULATIVE_SUFFIX,
                header.getValueType(),
                header.isHidden(),
                header.isMeta());

        addHeader(regressionHeader);
      }
    }

    return this;
  }

  @Override
  public Grid addCumulativesToGrid(int startColumnIndex, int numberOfColumns) {
    for (int i = 0; i < numberOfColumns; i++) {
      int columnIndex = i + startColumnIndex;

      this.addCumulativeColumn(columnIndex, true);
    }

    return this;
  }

  @Override
  public Grid substituteMetaData(Map<? extends Object, ? extends Object> metaDataMap) {
    if (metaDataMap == null || headers == null || headers.isEmpty()) {
      return this;
    }

    for (int colIndex = 0; colIndex < headers.size(); colIndex++) {
      GridHeader header = headers.get(colIndex);

      // Header

      Object headerMetaName = metaDataMap.get(header.getName());

      if (headerMetaName != null) {
        header.setName(String.valueOf(headerMetaName));
      }

      if (header.isMeta()) {
        // Column cells

        substituteMetaData(colIndex, colIndex, metaDataMap);
      }
    }

    return this;
  }

  @Override
  public Grid substituteMetaData(
      int sourceColumnIndex,
      int targetColumnIndex,
      Map<? extends Object, ? extends Object> metaDataMap) {
    if (metaDataMap == null) {
      return this;
    }

    List<Object> sourceColumn = getColumn(sourceColumnIndex);

    for (int rowIndex = 0; rowIndex < sourceColumn.size(); rowIndex++) {
      Object sourceValue = sourceColumn.get(rowIndex);

      Object metaValue = metaDataMap.get(sourceValue);

      if (metaValue != null) {
        grid.get(rowIndex).set(targetColumnIndex, metaValue);
      }
    }

    return this;
  }

  @Override
  @JsonIgnore
  public List<Integer> getMetaColumnIndexes() {
    List<Integer> indexes = new ArrayList<>();

    for (int i = 0; i < headers.size(); i++) {
      GridHeader header = headers.get(i);

      if (header != null && header.isMeta()) {
        indexes.add(i);
      }
    }

    return indexes;
  }

  @Override
  public Set<Object> getUniqueValues(String columnName) {
    int columnIndex = getIndexOfHeader(columnName);

    Set<Object> values = new HashSet<>();

    if (columnIndex != -1) {
      List<Object> column = getColumn(columnIndex);
      values.addAll(column);
    }

    return values;
  }

  @Override
  @SuppressWarnings("unchecked")
  public <T> Map<String, T> getAsMap(int valueIndex, String keySeparator) {
    Map<String, T> map = new HashMap<>();

    for (List<Object> row : grid) {
      List<Object> metaDataRow = new ArrayList<>(row);

      metaDataRow.remove(valueIndex);

      String key = join(metaDataRow, keySeparator);

      T value = (T) row.get(valueIndex);

      map.put(key, value);
    }

    return map;
  }

  // -------------------------------------------------------------------------
  // Supportive methods
  // -------------------------------------------------------------------------

  /** Verifies that all grid rows are of the same length. */
  private void verifyGridState() {
    Integer rowLength = null;

    int rowPos = 0;

    for (List<Object> row : grid) {
      if (rowLength != null && rowLength != row.size()) {
        throw new IllegalStateException(
            String.format(
                "Grid rows do not have the same number of cells, previous: %d, this: %d, at row: %d",
                rowLength, row.size(), rowPos));
      }

      rowPos++;
      rowLength = row.size();
    }
  }

  /**
   * Updates the mapping between header columns and grid indexes. This method should be invoked
   * whenever the columns are manipulated.
   */
  private void updateColumnIndexMap() {
    columnIndexMap.clear();

    for (int i = 0; i < headers.size(); i++) {
      columnIndexMap.put(headers.get(i).getColumn(), i);
    }
  }

  /**
   * Indicates whether the grid has any values, i.e. at least one column and one row.
   *
   * @return true if grid has any values.
   */
  private boolean hasValues() {
    return getWidth() > 0 && getHeight() > 0;
  }

  /**
   * Joins the elements of the given iterator using the given separator to separate the items.
   *
   * @param iterator the iterator.
   * @param separator the separator.
   * @return the joined string.
   */
  private static String join(final Collection<?> collection, final String separator) {
    if (collection == null) {
      return null;
    }

    Iterator<?> iterator = collection.iterator();

    if (iterator == null || !iterator.hasNext()) {
      return "";
    }

    final Object first = iterator.next();

    if (!iterator.hasNext()) {
      final String result = Objects.toString(first, "");
      return result;
    }

    // Two or more elements

    final StringBuilder buffer = new StringBuilder();

    if (first != null) {
      buffer.append(first);
    }

    while (iterator.hasNext()) {
      if (separator != null) {
        buffer.append(separator);
      }

      final Object obj = iterator.next();

      if (obj != null) {
        buffer.append(obj);
      }
    }

    return buffer.toString();
  }

  // -------------------------------------------------------------------------
  // toString
  // -------------------------------------------------------------------------

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder("[\n");

    if (headers != null && headers.size() > 0) {
      List<String> headerNames = new ArrayList<>();

      for (GridHeader header : headers) {
        headerNames.add(header.getName());
      }

      builder.append(headerNames).append("\n");
    }

    for (List<Object> row : grid) {
      builder.append(row).append("\n");
    }

    return builder.append("]").toString();
  }

  // -------------------------------------------------------------------------
  // Comparator
  // -------------------------------------------------------------------------

  public static class GridRowComparator implements Comparator<List<Object>> {
    private int columnIndex;
    private int order;

    protected GridRowComparator(int columnIndex, int order) {
      this.columnIndex = columnIndex;
      this.order = order;
    }

    @Override
    @SuppressWarnings("unchecked")
    public int compare(List<Object> list1, List<Object> list2) {
      boolean list1Invalid =
          list1 == null
              || list1.get(columnIndex) == null
              || !(list1.get(columnIndex) instanceof Comparable<?>);
      boolean list2Invalid =
          list2 == null
              || list2.get(columnIndex) == null
              || !(list2.get(columnIndex) instanceof Comparable<?>);

      if (list1Invalid && list2Invalid) {
        return 0;
      } else if (list1Invalid) {
        return order > 0 ? 1 : -1;
      } else if (list2Invalid) {
        return order > 0 ? -1 : 1;
      }

      final Comparable<Object> value1 = (Comparable<Object>) list1.get(columnIndex);
      final Comparable<Object> value2 = (Comparable<Object>) list2.get(columnIndex);

      return order > 0 ? value2.compareTo(value1) : value1.compareTo(value2);
    }
  }
}
