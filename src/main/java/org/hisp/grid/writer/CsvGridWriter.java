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

import com.csvreader.CsvWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.hisp.grid.Grid;
import org.hisp.grid.GridHeader;
import org.hisp.grid.csv.CsvWriteOptions;

/** {@link GridWriter} implementation for CSV format. */
public class CsvGridWriter implements GridWriter {
  /** CSV writing options. */
  private final CsvWriteOptions options;

  /**
   * Constructor.
   *
   * @param options the {@link CsvWriteOptions}.
   */
  public CsvGridWriter(CsvWriteOptions options) {
    this.options = options;
  }

  @Override
  public void write(Grid grid, Writer writer) throws IOException {
    CsvWriter csvWriter = getCsvWriter(writer, options);

    Iterator<GridHeader> headers = grid.getHeaders().iterator();

    if (!grid.getHeaders().isEmpty()) {
      while (headers.hasNext()) {
        csvWriter.write(headers.next().getName());
      }

      csvWriter.endRecord();
    }

    for (List<Object> row : grid.getRows()) {
      Iterator<Object> columns = row.iterator();

      while (columns.hasNext()) {
        Object value = columns.next();

        csvWriter.write(value != null ? String.valueOf(value) : StringUtils.EMPTY);
      }

      csvWriter.endRecord();
    }
  }

  /**
   * Returns a CSV writer based on the given writer and options.
   *
   * @param writer the {@link Writer}.
   * @param options the {@link CsvWriteOptions}.
   * @return a {@link CsvWriter}.
   */
  private CsvWriter getCsvWriter(Writer writer, CsvWriteOptions options) {
    CsvWriter csvWriter = new CsvWriter(writer, options.getDelimiter());
    csvWriter.setForceQualifier(options.isForceQualifier());
    return csvWriter;
  }
}
