package nl.kubebit.core.usecases.template.admin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.template.dto.TemplateItemResponse;
import nl.kubebit.core.usecases.common.annotation.UseCase;

/**
 * 
 */
@UseCase
class CreateTemplateUseCaseImpl implements CreateTemplateUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateGateway gateway;
    
    /**
     *
     */
    public CreateTemplateUseCaseImpl(TemplateGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 
     */
    @Override
    public TemplateItemResponse execute(TemplateCreateRequest request) {
        log.info("create template: {} - {} - {}", request.chart(), request.version(), request.repository());
        var template = new Template(
            generateId(request),
            request.chart(),            
            request.version(),
            request.repository(),
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null,
            null
        );
        return gateway.save(template).map(TemplateItemResponse::new).orElseThrow();
    }

    /**
     *
     */
    private String generateId(TemplateCreateRequest request) {
        return request.chart() + "-" + request.version();
    }

}
