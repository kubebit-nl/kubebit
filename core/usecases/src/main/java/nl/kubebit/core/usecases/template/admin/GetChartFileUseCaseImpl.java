package nl.kubebit.core.usecases.template.admin;

import nl.kubebit.core.entities.template.exception.ChartNotFoundException;
import nl.kubebit.core.entities.template.exception.TemplateNotFoundException;
import nl.kubebit.core.entities.template.gateway.TemplateGateway;
import nl.kubebit.core.usecases.common.annotation.UseCase;
import nl.kubebit.core.usecases.common.util.HelmChartUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 */
@UseCase
class GetChartFileUseCaseImpl implements GetChartFileUseCase {
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
    public GetChartFileUseCaseImpl(TemplateGateway gateway) {
        this.gateway = gateway;
    }

    /**
     * Getting chart file from a helm chart
     *
     * @param templateId template id
     * @param fileName   file name
     * @return content of the file
     * @throws TemplateNotFoundException if the template is not found
     * @throws ChartNotFoundException    if the chart is not found
     */
    @Override
    public String execute(String templateId, String fileName) throws TemplateNotFoundException, ChartNotFoundException {
        log.info("get template: {}", templateId);
        var template = gateway.findById(templateId).orElseThrow(TemplateNotFoundException::new);
        var tarFile = gateway.getChart(template.id()).orElseThrow(ChartNotFoundException::new);
        try {
            return HelmChartUtils.getChartFileAsString(tarFile, fileName);
        } catch (Exception e) {
            log.warn("failed to get values: {}", e.getMessage());
        }
        return "";
    }

}
