package nl.kubebit.core.entities.release.exception;

import nl.kubebit.core.entities.common.exception.EntityNotFoundException;

/**
 * ManifestNotFoundException
 */
public class ManifestNotFoundException extends EntityNotFoundException {

    /**
     * Constructor
     * @param file file
     */
    public ManifestNotFoundException(String file) {
        super("Manifest '" + file + "' not found");
    }

}
