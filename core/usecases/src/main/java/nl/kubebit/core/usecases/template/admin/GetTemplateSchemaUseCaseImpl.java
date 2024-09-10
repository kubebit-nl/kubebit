package nl.kubebit.core.usecases.template.admin;

import nl.kubebit.core.entities.template.exception.ChartNotFoundException;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.common.util.HelmChartUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 *
 */
@UseCase
class GetTemplateSchemaUseCaseImpl implements GetTemplateSchemaUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateGateway gateway;

    /**
     * Constructor
     *
     * @param gateway template gateway
     */
    public GetTemplateSchemaUseCaseImpl(TemplateGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * Getting template values schema
     *
     * @param templateId template id
     * @return values schema
     * @throws TemplateNotFoundException if the template is not found
     */
    @Override
    public Map<String, Object> execute(String templateId) throws TemplateNotFoundException, ChartNotFoundException {
        log.info("get template: {}", templateId);
        var template = gateway.findById(templateId).orElseThrow(TemplateNotFoundException::new);
        var tarFile = gateway.getChart(template.id()).orElseThrow(ChartNotFoundException::new);
        try {
            return HelmChartUtils.getChartSchema(tarFile);
        } catch (Exception e) {
            log.warn("failed to get values: {}", e.getMessage());
        }
        return Map.of();
    }

}
