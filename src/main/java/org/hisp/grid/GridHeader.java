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

import com.fasterxml.jackson.annotation.JsonProperty;

/** Grid header. */
public class GridHeader {
  /** Header name. */
  private String name;

  /** Header column. */
  private String column;

  /** Header value type. */
  private ValueType valueType;

  /** Whether column is hidden in rendering. */
  private boolean hidden;

  /** Whether column is representing metadata. */
  private boolean meta;

  // -------------------------------------------------------------------------
  // Constructors
  // -------------------------------------------------------------------------

  /** Constructor. */
  public GridHeader() {}

  /**
   * Constructor.
   *
   * @param name name
   */
  public GridHeader(String name) {
    this.name = name;
    this.column = name;
    this.valueType = ValueType.TEXT;
    this.hidden = false;
    this.meta = false;
  }

  /**
   * Constructor.
   *
   * @param name name
   * @param column column
   */
  public GridHeader(String name, String column) {
    this(name);
    this.column = column;
  }

  /**
   * Constructor.
   *
   * @param name name
   * @param hidden hidden
   * @param meta meta
   */
  public GridHeader(String name, boolean hidden, boolean meta) {
    this(name);
    this.hidden = hidden;
    this.meta = meta;
  }

  /**
   * Constructor.
   *
   * @param name name
   * @param column column
   * @param valueType valueType
   * @param hidden hidden
   * @param meta meta
   */
  public GridHeader(String name, String column, ValueType valueType, boolean hidden, boolean meta) {
    this(name, column);
    this.valueType = valueType;
    this.hidden = hidden;
    this.meta = meta;
  }

  // -------------------------------------------------------------------------
  // Getters and setters
  // -------------------------------------------------------------------------

  @JsonProperty
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @JsonProperty
  public String getColumn() {
    return column;
  }

  public void setColumn(String column) {
    this.column = column;
  }

  @JsonProperty
  public ValueType getValueType() {
    return valueType;
  }

  public void setValueType(ValueType valueType) {
    this.valueType = valueType;
  }

  @JsonProperty
  public boolean isHidden() {
    return hidden;
  }

  public void setHidden(boolean hidden) {
    this.hidden = hidden;
  }

  @JsonProperty
  public boolean isMeta() {
    return meta;
  }

  public void setMeta(boolean meta) {
    this.meta = meta;
  }

  // -------------------------------------------------------------------------
  // hashCode, equals, toString
  // -------------------------------------------------------------------------

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object object) {
    if (this == object) {
      return true;
    }

    if (object == null) {
      return false;
    }

    if (getClass() != object.getClass()) {
      return false;
    }

    final GridHeader other = (GridHeader) object;

    return name.equals(other.name);
  }

  @Override
  public String toString() {
    return "[Name: " + name + ", column: " + column + ", value type: " + valueType + "]";
  }
}
