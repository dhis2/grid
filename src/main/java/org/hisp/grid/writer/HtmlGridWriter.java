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
import org.hisp.grid.Grid;
import org.hisp.grid.GridHeader;
import org.owasp.encoder.Encode;

public class HtmlGridWriter implements GridWriter {

  @Override
  public void write(Grid grid, Writer writer) throws IOException {
    writer.write(getHtmlDocument(grid));
  }

  /**
   * Returns a HTML document.
   * 
   * @param grid the {@link Grid}.
   * @return a HTML document.
   */
  private String getHtmlDocument(Grid grid) {
    return String.format(
        """
        <!DOCTYPE html>
        <html>
        <head>
        <meta charset="UTF-8">
        <title>%s</title>
        %s
        </head>
        <body>
        %s
        </body>
        </html""", 
          escape(grid.getTitle()), 
          getHtmlStyle(grid), 
          getHtmlTable(grid));
  }

  /**
   * Returns a HTML style section.
   * 
   * @param grid the {@link Grid}.
   * @return a HTML style section.
   */
  private String getHtmlStyle(Grid grid) {
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
        </style>""";
  }

  /**
   * Returns a HTML table section.
   * 
   * @param grid the {@link Grid}.
   * @return a HTML table section.
   */
  private String getHtmlTable(Grid grid) {
    StringBuilder b = new StringBuilder();

    b.append(
        String.format(
            """
            <div class="gridDiv">
            <h2>%s</h2>
            <h3>%s</h3>
            <table class="gridTable">
            <thead>
            <tr>
            """,
            escape(grid.getTitle()),
            escape(grid.getSubtitle())));

    for (GridHeader header : grid.getVisibleHeaders()) {
      b.append("<th>").append(escape(header.getColumn())).append("</th>");
    }

    b.append("""
        </tr>
        </thead>
        <tbody>""");

    for (List<Object> row : grid.getVisibleRows()) {
      b.append("<tr>");
      for (Object value : row) {
        b.append("<td>").append(escape(value)).append("</td>");
      }
      b.append("</tr>");
    }

    return b.append("""
        </tbody>
        </table>
        </div>""").toString();
  }

  /**
   * Returns the given input object as an HTML-escaped string value.
   * 
   * @param input the input object.
   * @return a string value.
   */
  private String escape(Object input) {
    String value = String.valueOf(ObjectUtils.firstNonNull(input, StringUtils.EMPTY));
    return Encode.forHtml(StringUtils.trimToEmpty(value));
  }
}
