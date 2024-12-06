package org.hisp.grid.serializer;

import java.io.IOException;
import java.util.List;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

/**
 * Serializer for grid rows.
 */
public class JacksonRowDataSerializer
    extends JsonSerializer<List<List<Object>>>
{    
    @Override
    public void serialize( List<List<Object>> values, JsonGenerator jgen, SerializerProvider provider ) throws IOException
    {
        jgen.writeStartArray();
        
        for ( List<Object> row : values )
        {
            jgen.writeStartArray();
            
            for ( Object field : row )
            {
                jgen.writeObject( field );
            }
            
            jgen.writeEndArray();
        }
        
        jgen.writeEndArray();
    }
}
