package nl.kubebit.core.usecases.template.util;

import java.io.*;
import java.nio.file.Files;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.kubebit.core.entities.template.exception.ChartFileNotFoundException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.springframework.util.FileSystemUtils;

import static nl.kubebit.core.usecases.common.vars.GlobalVars.YAML_EXT;

/**
 *
 */
public abstract class HelmChartUtils {
    // --------------------------------------------------------------------------------------------

    //
    private static final Logger log = LoggerFactory.getLogger(HelmChartUtils.class);

    /**
     * Save chart file to destination
     * @param tarPath path to tar file
     * @param destination destination file
     * @param fileName name of the file to extract
     * @throws FileNotFoundException if the file is not found
     * @throws IOException if an I/O error occurs
     */
    public static void saveChartFile(@NotNull File tarPath, @NotNull File destination, @NotBlank String fileName) throws FileNotFoundException, IOException {
        log.trace("save values: {} - {}", fileName, destination);
        boolean found = false;
        try (var writer = new BufferedWriter(new FileWriter(destination))) {
            try (var tarInput = new TarArchiveInputStream(
                    new GzipCompressorInputStream(new FileInputStream(tarPath)))) {
                TarArchiveEntry entry;
                while ((entry = tarInput.getNextEntry()) != null) {
                    if (entry.getName().endsWith(fileName)) {
                        log.trace("found: {}", entry.getName());
                        found = true;
                        var reader = new BufferedReader(new InputStreamReader(tarInput));
                        String line;
                        while ((line = reader.readLine()) != null) {
                            writer.append(line).append("\n");
                        }
                        break;
                    }
                }
            }
        }
        if (!found) {
            throw new ChartFileNotFoundException(fileName);
        }
    }

    /**
     * Get chart file content
     * @param tarFile tar file
     * @param fileName name of the file to extract
     * @return content of the file
     * @throws IOException if an I/O error occurs
     * @throws ChartFileNotFoundException if the file is not found
     */
    public static String getChartValues(@NotNull File tarFile, @NotBlank String fileName) throws IOException, ChartFileNotFoundException {
        log.trace("getting content of file: {} - {}", tarFile, fileName);
        boolean found = false;
        var sb = new StringBuilder();
        try (var tarInput = new TarArchiveInputStream(
                new GzipCompressorInputStream(new FileInputStream(tarFile)))) {
            TarArchiveEntry entry;
            while ((entry = tarInput.getNextEntry()) != null) {
                log.trace("tar file: {}", entry.getName());
                if (entry.getName().endsWith(fileName)) {
                    found = true;
                    var reader = new BufferedReader(new InputStreamReader(tarInput));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line).append("\n");
                    }
                }
            }
        }
        if (!found) {
            throw new ChartFileNotFoundException(fileName);
        }
        return sb.toString();
    }

    /**
     * Check if the file contains the search string
     * @param sourceFile source file
     * @param searchString search string
     * @return true if the file contains the search string
     */
    public static boolean checkDynamicValues(File sourceFile, String searchString) {
        try {
            try (var reader = new BufferedReader(new FileReader(sourceFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(searchString)) {
                        return true;
                    }
                }
            }
        } catch (IOException e) {
            log.warn("failed check dynamic values: {}", e.getMessage());
        }
        return false;
    }

    /**
     * Get annotation value
     * @param sourceFile source file
     * @param searchString search string
     * @return annotation value
     */
    public static Optional<String> getAnnotationValue(File sourceFile, String searchString) {
        try {
            try (var reader = new BufferedReader(new FileReader(sourceFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.contains(searchString)) {
                        return Optional.of(line.split(":")[1].trim());
                    }
                }
            }
        } catch (IOException e) {
            log.warn("failed get annotation value: {}", e.getMessage());
        }
        return Optional.empty();
    }

    /**
     * Replace variables in the file
     * @param sourceFile source file
     * @param replacements map of replacements
     */
    public static void replaceVariables(File sourceFile, Map<String, Object> replacements) {
        try {
            File targetFile = Files.createTempFile("", YAML_EXT).toFile();
            try (var reader = new BufferedReader(new FileReader(sourceFile))) {
                try (var writer = new BufferedWriter(new FileWriter(targetFile))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        for (var entry : replacements.entrySet()) {
                            line = line.replace(entry.getKey(), entry.getValue().toString());
                        }
                        writer.write(line);
                        writer.newLine();
                    }
                }
            }
            FileSystemUtils.deleteRecursively(sourceFile);
            if(targetFile.renameTo(sourceFile)) {
                log.error("failed to rename file: {}", sourceFile.getAbsolutePath());
            }
        } catch (IOException e) {
            log.warn("failed to replace strings: {}", e.getMessage());
        }
    }

}