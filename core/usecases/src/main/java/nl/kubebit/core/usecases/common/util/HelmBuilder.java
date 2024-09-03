package nl.kubebit.core.usecases.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
public class HelmBuilder {
    // --------------------------------------------------------------------------------------------

    /**
     *
     */
    public enum GetCommand {
        HOOKS("hooks"),
        MANIFEST("manifest"),
        METADATA("metadata"),
        NOTES("notes"),
        VALUES("values");

        private final String value;

        GetCommand(String value) {
            this.value = value;
        }

        public String value() {
            return value;
        }
    }

    //
    private static final Logger log = LoggerFactory.getLogger(HelmBuilder.class);

    //
    private final List<String> commandStrings = new ArrayList<>();

    /**
     *
     */
    public HelmBuilder() {
        commandStrings.add("helm");
    }

    /**
     *
     */
    public static HelmBuilder init() {
        return new HelmBuilder();
    }

    /**
     *
     */
    public HelmBuilder template(String release, File file) {
        commandStrings.addAll(List.of("template", release, file.getAbsolutePath()));
        return this;
    }

    /**
     * helm get <function>
     *
     * @param function helm get function
     * @param release  release name
     * @return the builder
     */
    public HelmBuilder get(GetCommand function, String release) {
        commandStrings.addAll(List.of("get", function.value(), release));
        return this;
    }

    /**
     *
     */
    public HelmBuilder additionalArgs(String... additionalArgs) {
        Collections.addAll(commandStrings, additionalArgs);
        return this;
    }

    /**
     *
     */
    public HelmBuilder additionalArgs(List<String> additionalArgs) {
        commandStrings.addAll(additionalArgs);
        return this;
    }

    /**
     *
     */
    public HelmBuilder namespace(String namespaceName) {
        commandStrings.add("--namespace");
        commandStrings.add(namespaceName);
        return this;
    }

    /**
     *
     */
    public HelmBuilder values(Path valuesPath) {
        commandStrings.add("--values");
        commandStrings.add(valuesPath.toAbsolutePath().toString());
        return this;
    }

    /**
     *
     */
    public HelmBuilder outputJson() {
        commandStrings.add("--output");
        commandStrings.add("json");
        return this;
    }

    // --------------------------------------------------------------------------------------------

    /**
     * execute the command
     *
     * @return the process
     * @throws IOException if an I/O error occurs
     */
    public Process executeAs() throws IOException {
        log.trace("execution: {}", String.join(" ", commandStrings));
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commandStrings);
        return builder.start();
    }

    /**
     * execute the command
     *
     * @return the input stream
     */
    public InputStream execute() {
        try {
            var process = this.executeAs();

            // wait for the process to finish
            process.waitFor(60, TimeUnit.SECONDS);

            // check if the process was not successful
            if (process.exitValue() != 0) {
                try (var errorStream = process.getErrorStream()) {
                    throw new RuntimeException(this.fetchStream(errorStream));
                }
            }
            return process.getInputStream();
        } catch (Exception e) {
            throw new RuntimeException("error in process: " + e.getMessage());
        }
    }

    // --------------------------------------------------------------------------------------------
    // private methods

    /**
     *
     */
    private String fetchStream(InputStream stream) throws IOException {
        var output = new StringBuilder();
        try (var input = new InputStreamReader(stream)) {
            try (var reader = new BufferedReader(input)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
        }
        return output.toString().trim();
    }

}


