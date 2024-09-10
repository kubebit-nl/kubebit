package nl.kubebit.core.usecases.template.admin;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import nl.kubebit.core.entities.template.Template;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.exception.TemplateNotUpdatedException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.template.dto.TemplateResponse;

/**
 * 
 */
@UseCase
class UpdateTemplateValuesUseCaseImpl implements UpdateTemplateValuesUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateGateway gateway;
    
    /**
     *
     */
    public UpdateTemplateValuesUseCaseImpl(TemplateGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * 
     */
    @Override
    public TemplateResponse execute(String templateId, TemplateValueType type, Map<String, Object> values) throws TemplateNotFoundException {
        log.info("update template values: {}", templateId);
        var template = gateway.findById(templateId).orElseThrow(TemplateNotFoundException::new);
        
        // change values
        var update = new Template(
            template.id(),
            template.chart(),
            template.version(),
            template.repository(),
            template.type(),
            template.category(),
            template.icon(),
            template.schema(),
            TemplateValueType.BASE.equals(type) ? values : template.baseValues(),
            TemplateValueType.STAGING.equals(type) ? values : template.stagingValues(),
            TemplateValueType.PRODUCTION.equals(type) ? values : template.productionValues(),
            template.status(),
            template.message(),
            template.values(),
            template.appVersion(),
            template.description(),
            template.keywords(),
            template.namespaceId());  

        //
        return gateway.update(update).map(TemplateResponse::new).orElseThrow(TemplateNotUpdatedException::new);
    }
    
}
