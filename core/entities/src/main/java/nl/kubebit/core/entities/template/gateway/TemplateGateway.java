package nl.kubebit.core.entities.template.gateway;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import nl.kubebit.core.entities.template.ChartWrapper;
import nl.kubebit.core.entities.template.Template;

/**
 * 
 */
public interface TemplateGateway {
    
    List<Template> findAll();

    Optional<Template> findById(String templateId);

    Optional<Template> save(Template template);

    Optional<Template> update(Template template);

    Optional<Template> updateStatus(Template template);

    // -----------------------------------------------
    
    File pullIndex(String repositoryUrl) throws IOException;

    File pullChart(URI chartUri);

    Optional<File> getChart(String templateId);

    ChartWrapper parseIndexFile(File index) throws IOException;

    Map<String, Object> parseYaml(File index) throws IOException;

    
    
}
