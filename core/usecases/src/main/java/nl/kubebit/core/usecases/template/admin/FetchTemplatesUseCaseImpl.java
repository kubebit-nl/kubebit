package nl.kubebit.core.usecases.template.admin;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.template.dto.TemplateItemResponse;

/**
 * 
 */
@UseCase
class FetchTemplatesUseCaseImpl implements FetchTemplatesUseCase {
    // --------------------------------------------------------------------------------------------
    
    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateGateway gateway;
    
    /**
     *
     */
    public FetchTemplatesUseCaseImpl(TemplateGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 
     */
    @Override
    public List<TemplateItemResponse> execute() {
        log.info("fetch templates");
        return gateway.findAll().stream().map(TemplateItemResponse::new).toList();
    }
   
    
}
