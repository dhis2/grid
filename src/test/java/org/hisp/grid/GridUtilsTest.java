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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.io.StringWriter;
import org.hisp.grid.csv.CsvWriteOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GridUtilsTest {

  private Grid grid;

  @BeforeEach
  public void beforeEach() {
    grid =
        new ListGrid()
            .setTitle("Immunization")
            .setSubtitle("By district 2019")
            .addHeader("Name")
            .addHeader("Period")
            .addHeader("Organisation unit")
            .addHeader("Value")
            .addRow()
            .addValuesVar("Penta1 doses given", "Apr to Jun 2019", "Bombali", 5128.0)
            .addRow()
            .addValuesVar("Fully Immunized child", "Oct to Dec 2019", "Moyamba", "3017.0")
            .addRow()
            .addValuesVar("Penta1 doses given", "Oct to Dec 2019", "Bombali", "2873.0")
            .addRow()
            .addValuesVar("Fully Immunized child", "Jul to Sep 2019", "Moyamba", "3357.0");
  }

  @Test
  void testToCsv() throws IOException {
    StringWriter writer = new StringWriter();
    CsvWriteOptions options = new CsvWriteOptions().setDelimiter(',').setForceQualifier(false);

    GridUtils.toCsv(grid, writer, options);
    String output = writer.toString();

    assertNotNull(output);
    assertTrue(output.startsWith("Name,Period,Organisation unit,Value"));
  }

  @Test
  void testToHtml() throws IOException {
    StringWriter writer = new StringWriter();

    GridUtils.toHtml(grid, writer);
    String output = writer.toString();

    assertNotNull(output);
    assertTrue(
        output.startsWith(
            """
        <!DOCTYPE html>
        <html>
        <head>
        <meta charset="UTF-8">
        <title>Immunization</title>
        <style type="text/css">"""));
  }
}
