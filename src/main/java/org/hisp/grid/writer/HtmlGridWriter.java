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
package org.hisp.grid.writer;

import java.io.IOException;
import java.io.Writer;
import java.util.List;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;
import org.hisp.grid.Grid;
import org.hisp.grid.GridHeader;

public class HtmlGridWriter implements GridWriter {

  @Override
  public void write(Grid grid, Writer writer) throws IOException {
    writer.write(getHtmlDocument(grid));
  }

  private String getHtmlDocument(Grid grid) {
    return new StringBuilder()
        .append("""
        <!DOCTYPE html>
        <html>
        <head>""")
        .append(getHtmlStyle())
        .append("""
        </head>
        <body>""")
        .append(toHtmlTable(grid))
        .append("""
        </body>
        </html""")
        .toString();
  }

  private String getHtmlStyle() {
    return """
        <style type="text/css">
        .gridDiv {
          font-family: sans-serif, arial;
        }

        table.gridTable {
          border-collapse: collapse;
          font-size: 11pt;
        }

        .gridTable th, .gridTable td {
          padding: 8px 4px 7px 4px;
          border: 1px solid #e7e7e7;
        }

        .gridTable th {
          background-color: #f3f3f3;
          font-weight: bold;
        }
        </style>
        """;
  }

  private String toHtmlTable(Grid grid) {
    StringBuilder b = new StringBuilder();

    b.append(
        String.format(
            """
            <div class="gridDiv">
            <h3>$!encoder.htmlEncode( $grid.title )</h3>
            <h4>$!encoder.htmlEncode( $grid.subtitle )</h4>
            <table class="listTable gridTable">
            <thead>
            <tr>
            """,
            escapeHtml(grid.getTitle()),
            escapeHtml(grid.getSubtitle())));

    for (GridHeader header : grid.getVisibleHeaders()) {
      b.append("<th>").append(escapeHtml(header.getColumn())).append("</th>");
    }

    b.append("""
        </tr>
        </thead>
        <tbody>""");

    for (List<Object> row : grid.getVisibleRows()) {
      b.append("<tr>");
      for (Object value : row) {
        b.append("<td>").append(escapeHtml(value)).append("<td>");
      }
      b.append("</tr>");
    }

    return b.append("""
        </tbody>
        </table>
        </div>""").toString();
  }

  private String escapeHtml(Object input) {
    String value = String.valueOf(ObjectUtils.firstNonNull(input, StringUtils.EMPTY));
    return StringEscapeUtils.escapeHtml4(StringUtils.trimToEmpty(value));
  }
}
