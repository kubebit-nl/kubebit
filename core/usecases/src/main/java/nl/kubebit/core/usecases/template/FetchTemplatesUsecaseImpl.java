package nl.kubebit.core.usecases.template;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.annotation.Usecase;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

/**
 * 
 */
@Usecase
class FetchTemplatesUsecaseImpl implements FetchTemplatesUsecase {
    // --------------------------------------------------------------------------------------------
    
    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateGateway gateway;
    
    /**
     *
     */
    public FetchTemplatesUsecaseImpl(TemplateGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 
     */
    @Override
    public List<TemplateResponse> execute() {
        log.info("fetch templates");
        return gateway.findAll().stream().map(TemplateResponse::new).toList();
    }
   
    
}
