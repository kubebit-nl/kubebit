package nl.kubebit.core.usecases.template.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import nl.kubebit.core.entities.template.exception.ChartFileNotFoundException;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;

/**
 * 
 */
public abstract class HelmChartUtils {
    // --------------------------------------------------------------------------------------------

    //
    private static final Logger log = LoggerFactory.getLogger(HelmChartUtils.class);

    //
    public static final String YAML_EXT = ".yaml";

    //
    private HelmChartUtils() {
    }

    /**
     * 
     * @param tarPath
     * @param destination
     * @param fileName
     * @throws FileNotFoundException
     * @throws IOException
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
                            writer.append(line + "\n");
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


    // public static String getChartFile(@NotNull File tarPath, @NotBlank String fileName) throws FileNotFoundException, IOException {
    //     log.trace("get values: {} - {}", tarPath, fileName);
    //     boolean found = false;
    //     var sb = new StringBuilder();
    //     try (var tarInput = new TarArchiveInputStream(new GzipCompressorInputStream(new FileInputStream(tarPath)))) {
    //         TarArchiveEntry entry;
    //         while ((entry = tarInput.getNextEntry()) != null) {
    //             log.trace("tarfile: {}", entry.getName());
    //             if (entry.getName().endsWith(fileName)) {
    //                 found = true;
    //                 var reader = new BufferedReader(new InputStreamReader(tarInput));
    //                 String line;
    //                 while ((line = reader.readLine()) != null) {
    //                     sb.append(line + "\n");
    //                 }
    //             }
    //         }
    //     }
    //     if (!found) {
    //         throw new ChartFileNotFoundException(fileName);
    //     }
    //     return sb.toString();
    // }





    // /**
    //  * 
    //  * @param tarPath
    //  * @param destination
    //  * @param type
    //  * @throws IOException
    //  * @throws ChartFileNotFoundException
    //  */
    // public static void saveChartValues(@NotNull Path tarPath, @NotNull File destination, @NotNull HelmValueTypes type)
    //         throws IOException, ChartFileNotFoundException {
    //     log.trace("save values: {} - {}", type, destination);
    //     boolean found = false;
    //     try (var writer = new BufferedWriter(new FileWriter(destination))) {
    //         try (var tarInput = new TarArchiveInputStream(
    //                 new GzipCompressorInputStream(new FileInputStream(tarPath.toFile())))) {
    //             TarArchiveEntry entry;
    //             while ((entry = tarInput.getNextEntry()) != null) {
    //                 log.trace("tarfile: {}", entry.getName());
    //                 if (entry.getName().endsWith(type.getFilename())) {
    //                     found = true;
    //                     var reader = new BufferedReader(new InputStreamReader(tarInput));
    //                     String line;
    //                     while ((line = reader.readLine()) != null) {
    //                         writer.append(line + "\n");
    //                     }
    //                 }
    //             }
    //         }
    //     }
    //     if (!found) {
    //         throw new ChartFileNotFoundException(type.getFilename());
    //     }
    // }

    // /**
    //  * 
    //  * @param tarPath
    //  * @param type
    //  * @return
    //  * @throws IOException
    //  * @throws ChartFileNotFoundException
    //  */
    // public static String getChartValues(@NotNull Path tarPath, HelmValueTypes type)
    //         throws IOException, ChartFileNotFoundException {
    //     log.trace("get values: {} - {}", tarPath, type);
    //     boolean found = false;
    //     var sb = new StringBuilder();
    //     try (var tarInput = new TarArchiveInputStream(
    //             new GzipCompressorInputStream(new FileInputStream(tarPath.toFile())))) {
    //         TarArchiveEntry entry;
    //         while ((entry = tarInput.getNextEntry()) != null) {
    //             log.trace("tarfile: {}", entry.getName());
    //             if (entry.getName().endsWith(type.getFilename())) {
    //                 found = true;
    //                 var reader = new BufferedReader(new InputStreamReader(tarInput));
    //                 String line;
    //                 while ((line = reader.readLine()) != null) {
    //                     sb.append(line + "\n");
    //                 }
    //             }
    //         }
    //     }
    //     if (!found) {
    //         throw new ChartFileNotFoundException(type.getFilename());
    //     }
    //     return sb.toString();
    // }

    // /**
    //  * 
    //  * @param sourceFile
    //  * @param searchString
    //  * @return
    //  */
    // public static boolean checkDynamicValues(File sourceFile, String searchString) {
    //     try {
    //         try (var reader = new BufferedReader(new FileReader(sourceFile))) {
    //             String line;
    //             while ((line = reader.readLine()) != null) {
    //                 if (line.contains(searchString)) {
    //                     return true;
    //                 }
    //             }
    //         }
    //     } catch (IOException e) {
    //         log.warn("failed check dynamic values: {}", e.getMessage());
    //     }
    //     return false;
    // }

    // /**
    //  * 
    //  * @param sourceFile
    //  * @param searchString
    //  * @return
    //  */
    // public static Optional<String> getAnnotationValue(File sourceFile, String searchString) {
    //     try {
    //         try (var reader = new BufferedReader(new FileReader(sourceFile))) {
    //             String line;
    //             while ((line = reader.readLine()) != null) {
    //                 if (line.contains(searchString)) {
    //                     return Optional.ofNullable(line.split(":")[1].trim());
    //                 }
    //             }
    //         }
    //     } catch (IOException e) {
    //         log.warn("failed get annotation value: {}", e.getMessage());
    //     }
    //     return Optional.empty();
    // }

    // /**
    //  * 
    //  * @param sourceFile
    //  * @param replacements
    //  */
    // public static void replaceVariables(File sourceFile, Map<String, Object> replacements) {
    //     try {
    //         File targetFile = Files.createTempFile("", YAML_EXT).toFile();
    //         try (var reader = new BufferedReader(new FileReader(sourceFile))) {
    //             try (var writer = new BufferedWriter(new FileWriter(targetFile))) {
    //                 String line;
    //                 while ((line = reader.readLine()) != null) {
    //                     for (var entry : replacements.entrySet()) {
    //                         line = line.replace(entry.getKey(), entry.getValue().toString());
    //                     }
    //                     writer.write(line);
    //                     writer.newLine();
    //                 }
    //             }
    //         }
    //         FileSystemUtils.deleteRecursively(sourceFile);
    //         targetFile.renameTo(sourceFile);
    //     } catch (IOException e) {
    //         log.warn("failed to replace strings: {}", e.getMessage());
    //     }
    // }

    // /**
    //  * 
    //  * @param files
    //  */
    // public static void deleteFiles(File... files) {
    //     if (files != null && files.length > 0) {
    //         for (var file : files) {
    //             if (file != null) {
    //                 FileSystemUtils.deleteRecursively(file);
    //             }
    //         }
    //     }
    // }
}