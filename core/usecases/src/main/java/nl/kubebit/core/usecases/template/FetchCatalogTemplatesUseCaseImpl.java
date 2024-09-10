package nl.kubebit.core.usecases.template;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.template.TemplateStatus;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

/**
 * 
 */
@UseCase
class FetchCatalogTemplatesUseCaseImpl implements FetchCatalogTemplatesUseCase {
    // --------------------------------------------------------------------------------------------
    
    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateGateway gateway;
    
    /**
     *
     */
    public FetchCatalogTemplatesUseCaseImpl(TemplateGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 
     */
    @Override
    public List<TemplateResponse> execute() {
        log.info("fetch templates catalog");
        return gateway.findAll().stream()
            .filter(t -> TemplateStatus.AVAILABLE.equals(t.status()))
            .map(TemplateResponse::new)
            .toList();
    }

}
