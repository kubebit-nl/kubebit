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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
public class HelmBuilder {
    // --------------------------------------------------------------------------------------------

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
     *
     */
    public Process executeAs() throws IOException {
        log.trace("execution: {}", String.join(" ", commandStrings));
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commandStrings);
        return builder.start();
    }

    /**
     *
     */
    public String execute() throws IOException, RuntimeException, InterruptedException {
        var process = this.executeAs();        
        var response = fetchStream(process.getInputStream());
        var error = fetchStream(process.getErrorStream());
        process.waitFor();
        if(process.exitValue() != 0) {
            throw new RuntimeException(error);
        }
        return response;
    }

    /**
     *
     */
    public static String fetchStream(InputStream stream) throws IOException {
        var output = new StringBuilder();
        try(var input = new InputStreamReader(stream)) {
            try(var reader = new BufferedReader(input)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line).append("\n");
                }
            }
        }
        return output.toString().trim();
    }
    
}


