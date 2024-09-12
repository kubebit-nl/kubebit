package nl.kubebit.core.infrastructure.common.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

/**
 *
 */
@RestController
@RequestMapping("/api/v1/debug")
public class DebugController {
    // --------------------------------------------------------------------------------------------

    //
    private static final Logger log = LoggerFactory.getLogger(DebugController.class);

    //
    private final JsonSchema jsonSchema = JsonSchemaFactory
            .getInstance(SpecVersion.VersionFlag.V202012)
            .getSchema("""
                    {
                      "properties": {
                        "replicaCount": {
                          "type": "integer",
                          "minimum": 2,
                          "maximum": 7
                        }
                      },
                      "required": [
                        "replicaCount"
                      ],
                      "type": "object"
                    }
                    """);

    /**
     * validate json
     *
     * @param jsonNode json
     * @return validation result
     */
    @PostMapping("/json")
    public String submitJson(@RequestBody JsonNode jsonNode) {

        Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
        //if errors have a single miss match, there would be a value in the errors set.
        if (errors.isEmpty()) {
            //event is valid.
            log.info("event is valid");
        } else {
            //event is in_valid.
            log.info("event is invalid");
        }
        return errors.toString();
    }

}

