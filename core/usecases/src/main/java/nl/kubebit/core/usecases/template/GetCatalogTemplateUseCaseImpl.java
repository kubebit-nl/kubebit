package nl.kubebit.core.usecases.template;

import nl.kubebit.core.entities.template.TemplateStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.template.dto.TemplateCatalogResponse;

/**
 * 
 */
@UseCase
class GetCatalogTemplateUseCaseImpl implements GetCatalogTemplateUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateGateway gateway;
    
    /**
     *
     */
    public GetCatalogTemplateUseCaseImpl(TemplateGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 
     */
    @Override
    public TemplateCatalogResponse execute(String templateId) throws TemplateNotFoundException {
        log.info("get template: {}", templateId);
        return gateway.findById(templateId)
                .filter(t -> TemplateStatus.AVAILABLE.equals(t.status()))
                .map(TemplateCatalogResponse::new)
                .orElseThrow(TemplateNotFoundException::new);
    }

}
