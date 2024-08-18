package nl.kubebit.core.infrastructure.template.gateway;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.template.ChartWrapper;
import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.infrastructure.template.datasource.IndexRepository;
import nl.kubebit.core.infrastructure.template.datasource.TemplateMapper;
import nl.kubebit.core.infrastructure.template.datasource.TemplateRepository;
import nl.kubebit.core.usecases.common.annotation.Gateway;

/**
 * 
 */
@Gateway
public class TemplateGatewayImpl implements TemplateGateway {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateRepository templateRepository;
    private final IndexRepository indexRepository;

    /**
     * 
     * @param repository
     * @param indexRepository
     */
    public TemplateGatewayImpl(TemplateRepository repository, IndexRepository indexRepository) {
        this.templateRepository = repository;
        this.indexRepository = indexRepository;
    }

    /**
     * 
     */
    @Override
    public List<Template> findAll() {
        log.trace("fetch templates");
        return templateRepository.findAll().stream().map(TemplateMapper::toEntity).toList();
    }

    /**
     * 
     */
    @Override
    public Optional<Template> findById(String templateId) {
        log.trace("get template: {}", templateId);
        return templateRepository.findById(templateId).map(TemplateMapper::toEntity);
    }

    /**
     * 
     */
    @Override
    public Optional<Template> save(Template template) {
        log.trace("save template: {}", template.id());
        return templateRepository.save(TemplateMapper.toSchema(template)).map(TemplateMapper::toEntity);
    }

    /**
     * 
     * @param template
     * @return
     */
    @Override
    public Optional<Template> update(Template template) {
        log.trace("update template: {}", template.id());
        return templateRepository.update(TemplateMapper.toSchema(template)).map(TemplateMapper::toEntity);
    }

    /**
     * 
     * @param template
     * @return
     */
    @Override
    public Optional<Template> updateStatus(Template template) {
        log.trace("update template: {}", template.id());
        return templateRepository.updateStatus(TemplateMapper.toSchema(template)).map(TemplateMapper::toEntity);
    }

    // -----------------------------------------------------------------

    /**
     * 
     */
    @Override
    public File pullIndex(String repositoryUrl) throws IOException, URISyntaxException {
        return indexRepository.pullIndex(repositoryUrl);
    }

    /**
     * @throws URISyntaxException 
     * 
     */
    @Override
    public File pullChart(URI chartUri) throws IOException {
        return indexRepository.pullChart(chartUri);
    }

    /**
     * 
     */
    @Override
    public Optional<File> getChart(String templateId) throws IOException {
        return indexRepository.getChart(templateId);
    }

    /**
     * @throws IOException 
     * 
     */
    @Override
    public ChartWrapper parseIndexFile(File file) throws IOException {
        return indexRepository.parseIndexFile(file);
    }

    /**
     * 
     */
    @Override
    public Map<String, Object> parseYaml(File file) throws IOException {
        return indexRepository.parseYaml(file);
    }
   

}
