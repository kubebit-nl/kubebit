package nl.kubebit.core.usecases.release.chore;

import nl.kubebit.core.entities.release.Manifest;

import java.io.Closeable;

/**
 *
 */
public interface ManifestAction extends Closeable {

    void execute(Manifest manifest) throws Exception;

}

