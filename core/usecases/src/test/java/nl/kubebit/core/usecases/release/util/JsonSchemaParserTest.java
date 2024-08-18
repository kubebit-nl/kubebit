package nl.kubebit.core.usecases.release.util;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.same;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;

/**
 * 
 */
public class JsonSchemaParserTest {



    /**
     * 
     * @throws IOException
     */
    @Test
    @SuppressWarnings("unchecked")
    void parse_when_then() throws IOException {
        var jsonParser = new JsonSchemaParser();

        //
        try(var inputStream = getClass().getClassLoader().getResourceAsStream("values.schema.json")) {            
            Map<String, Object> schema = new ObjectMapper().readValue(inputStream, Map.class);
            
            
            //
            Map<String, Object> propertiesKeys = jsonParser.getPropertiesKeys(schema);

            //
            jsonParser.printPropertiesKeys(propertiesKeys, 0);
        }
    }

  

}
