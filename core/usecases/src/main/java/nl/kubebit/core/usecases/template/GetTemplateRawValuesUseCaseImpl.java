package nl.kubebit.core.usecases.template;

import nl.kubebit.core.entities.template.exception.ChartNotFoundException;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.template.util.HelmChartUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 */
@UseCase
class GetTemplateRawValuesUseCaseImpl implements GetTemplateRawValuesUseCase {
    // --------------------------------------------------------------------------------------------

    //
    private final Logger log = LoggerFactory.getLogger(getClass());

    //
    private final TemplateGateway gateway;

    /**
     * Constructor
     * @param gateway template gateway
     */
    public GetTemplateRawValuesUseCaseImpl(TemplateGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * Getting template raw chart values
     * @param templateId template id
     * @return template raw values
     * @throws TemplateNotFoundException if the template is not found
     */
    @Override
    public String execute(String templateId) throws TemplateNotFoundException, ChartNotFoundException {
        log.info("get template: {}", templateId);
        var template = gateway.findById(templateId).orElseThrow(() -> new TemplateNotFoundException(templateId));
        var tarFile = gateway.getChart(template.id()).orElseThrow(() -> new ChartNotFoundException(template.id()));
        try {
            return HelmChartUtils.getChartValues(tarFile, "values.yaml");
        }
        catch (Exception e) {
            log.warn("failed to get values: {}", e.getMessage());
        }
        return "";
    }

}
