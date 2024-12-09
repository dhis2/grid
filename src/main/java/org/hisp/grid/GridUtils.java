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

import java.io.IOException;
import java.io.Writer;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.hisp.grid.options.CsvWriteOptions;
import org.hisp.grid.options.HtmlWriteOptions;
import org.hisp.grid.util.MapBuilder;
import org.hisp.grid.writer.CsvGridWriter;
import org.hisp.grid.writer.HtmlGridWriter;

/** Utility methods for {@link Grid}. */
public class GridUtils {
  private static final Map<Integer, ValueType> SQL_VALUE_TYPE_MAP =
      new MapBuilder<Integer, ValueType>()
          .put(Types.BIT, ValueType.BOOLEAN)
          .put(Types.TINYINT, ValueType.SMALLINT)
          .put(Types.SMALLINT, ValueType.SMALLINT)
          .put(Types.INTEGER, ValueType.INTEGER)
          .put(Types.BIGINT, ValueType.BIGINT)
          .put(Types.FLOAT, ValueType.DOUBLE)
          .put(Types.REAL, ValueType.REAL)
          .put(Types.DOUBLE, ValueType.DOUBLE)
          .put(Types.NUMERIC, ValueType.NUMERIC)
          .put(Types.DECIMAL, ValueType.DOUBLE)
          .put(Types.CHAR, ValueType.CHAR)
          .put(Types.VARCHAR, ValueType.TEXT)
          .put(Types.LONGVARCHAR, ValueType.TEXT)
          .put(Types.DATE, ValueType.DATE)
          .put(Types.TIME, ValueType.TIMESTAMP)
          .put(Types.TIMESTAMP, ValueType.TIMESTAMP)
          .put(Types.BINARY, ValueType.BOOLEAN)
          .put(Types.VARBINARY, ValueType.BOOLEAN)
          .put(Types.LONGVARBINARY, ValueType.BOOLEAN)
          .put(Types.BOOLEAN, ValueType.BOOLEAN)
          .put(Types.TIME_WITH_TIMEZONE, ValueType.TIMESTAMPTZ)
          .put(Types.TIMESTAMP_WITH_TIMEZONE, ValueType.TIMESTAMPTZ)
          .build();

  private GridUtils() {}

  /**
   * Creates a {@link Grid} based on the given SQL {@link ResultSet}.
   *
   * @param rs the {@link ResultSet}.
   * @return a {@link Grid}.
   */
  public static Grid fromResultSet(ResultSet rs) {
    Grid grid = new ListGrid();
    addHeaders(grid, rs);
    addRows(grid, rs);
    return grid;
  }

  /**
   * Renders the given {@link Grid} in CSV format. Writes the content to the given {@link Writer}.
   *
   * @param grid the grid.
   * @param writer the writer.
   * @throws IOException for errors during rendering.
   */
  public static void toCsv(Grid grid, Writer writer) throws IOException {
    toCsv(grid, writer, new CsvWriteOptions());
  }

  /**
   * Renders the given {@link Grid} in CSV format. Writes the content to the given {@link Writer}.
   *
   * @param grid the grid.
   * @param writer the writer.
   * @param options the {@link CsvWriteOptions}.
   * @throws IOException for errors during rendering.
   */
  public static void toCsv(Grid grid, Writer writer, CsvWriteOptions options) throws IOException {
    if (grid == null) {
      return;
    }
    new CsvGridWriter(options).write(grid, writer);
  }

  /**
   * Renders the given {@link Grid} in HTML format. Writes the content to the given {@link Writer}.
   *
   * @param grid the grid.
   * @param writer the writer.
   * @throws IOException for errors during rendering.
   */
  public static void toHtml(Grid grid, Writer writer) throws IOException {
    toHtml(grid, writer, new HtmlWriteOptions());
  }

  /**
   * Renders the given {@link Grid} in HTML format. Writes the content to the given {@link Writer}.
   *
   * @param grid the grid.
   * @param writer the writer.
   * @throws IOException for errors during rendering.
   */
  public static void toHtml(Grid grid, Writer writer, HtmlWriteOptions options) throws IOException {
    if (grid == null) {
      return;
    }

    new HtmlGridWriter(options).write(grid, writer);
  }

  /**
   * Returns a list based on the given variable arguments.
   *
   * @param items the items.
   * @param <T> type.
   * @return a list of the given items.
   */
  @SafeVarargs
  public static <T> List<T> getList(T... items) {
    List<T> list = new ArrayList<>();
    Collections.addAll(list, items);
    return list;
  }

  // ---------------------------------------------------------------------
  // Supportive methods
  // ---------------------------------------------------------------------

  private static void addHeaders(Grid grid, ResultSet rs) {
    try {
      ResultSetMetaData rsmd = rs.getMetaData();

      int columnNo = rsmd.getColumnCount();

      for (int i = 1; i <= columnNo; i++) {
        String label = rsmd.getColumnLabel(i);
        String name = rsmd.getColumnName(i);
        ValueType valueType = fromSqlType(rsmd.getColumnType(i));

        grid.addHeader(new GridHeader(label, name, valueType, false, false));
      }
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  private static void addRows(Grid grid, ResultSet rs) {
    try {
      int cols = rs.getMetaData().getColumnCount();

      while (rs.next()) {
        grid.addRow();

        for (int i = 1; i <= cols; i++) {
          grid.addValue(rs.getObject(i));
        }
      }
    } catch (SQLException ex) {
      throw new RuntimeException(ex);
    }
  }

  private static ValueType fromSqlType(Integer sqlType) {
    return SQL_VALUE_TYPE_MAP.getOrDefault(sqlType, ValueType.TEXT);
  }
}
