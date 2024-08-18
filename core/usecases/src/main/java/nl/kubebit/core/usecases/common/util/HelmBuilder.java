package nl.kubebit.core.usecases.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
     * @return
     */
    public static HelmBuilder init() {
        return new HelmBuilder();
    }

    /**
     * 
     * @param release
     * @param file
     * @return
     */
    public HelmBuilder template(String release, File file) {
        commandStrings.addAll(List.of("template", release, file.getAbsolutePath()));
        return this;
    }
    
    /**
     * 
     * @param additionalArg
     * @return
     */
    public HelmBuilder additionalArg(String additionalArg) {
        commandStrings.add(additionalArg);
        return this;
    }

    /**
     * 
     * @param additionalArgs
     * @return
     */
    public HelmBuilder additionalArgs(List<String> additionalArgs) {
        commandStrings.addAll(additionalArgs);
        return this;
    }

    /**
     * 
     * @param namespaceName
     * @return
     */
    public HelmBuilder namespace(String namespaceName) {
        commandStrings.add("--namespace");
        commandStrings.add(namespaceName);
        return this;
    }

    /**
     * 
     * @param valuesPath
     * @return
     */
    public HelmBuilder values(Path valuesPath) {
        commandStrings.add("--values");
        commandStrings.add(valuesPath.toAbsolutePath().toString());
        return this;
    }

    /**
     * 
     * @return
     */
    public HelmBuilder outputJson() {
        commandStrings.add("--output");
        commandStrings.add("json");
        return this;
    }

    // --------------------------------------------------------------------------------------------

    /**
     * 
     * @return
     * @throws IOException
     */
    public Process executeAs() throws IOException {
        log.trace("execution: {}", commandStrings.stream().collect(Collectors.joining(" ")));
        ProcessBuilder builder = new ProcessBuilder();
        builder.command(commandStrings);
        return builder.start();
    }

    /**
     * 
     * @return
     * @throws IOException
     * @throws RuntimeException
     * @throws InterruptedException 
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
     * @param stream
     * @return
     * @throws IOException
     */
    private String fetchStream(InputStream stream) throws IOException {
        var output = new StringBuilder();
        try(var input = new InputStreamReader(stream)) {
            try(var reader = new BufferedReader(input)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    output.append(line + "\n");
                }
            }
        }
        return output.toString().trim();
    }
    
}


