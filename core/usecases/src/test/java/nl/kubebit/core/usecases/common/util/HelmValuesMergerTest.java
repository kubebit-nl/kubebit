package nl.kubebit.core.usecases.common.util;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Map;

import org.junit.jupiter.api.Test;

/**
 * 
 */
public class HelmValuesMergerTest {


    /**
     * 
     */
    @Test
    void merge_when_then() {

        // user input values
        Map<String, Object> inputValues = Map.of(
            "key1", "value1", 
            "key2", "value2",
            "key4", "value4", //<--- must not be copied (not in chartValues or overlayValues)
            "key5", "value5"); 
 
        // system overlay values
        Map<String, Object> overlayValues = Map.of(
            "key2", "overlay",  // <--- must be overridden by user input
            "key3", "overlay",  // <--- must be added
            "key5", "overlay"); 

        // default chart values
        Map<String, Object> chartValues = Map.of(
            "key1", "default", 
            "key2", "default");

        //
        Map<String, Object> result = HelmValuesMerger.merge(chartValues, overlayValues, inputValues);

        //
        assertEquals(result.get("key1"), "value1");
        assertEquals(result.get("key5"), "value5");
        assertTrue(result.containsKey("key3"));
        assertFalse(result.containsKey("key4"));

        //
        System.out.println(result);
    }

}