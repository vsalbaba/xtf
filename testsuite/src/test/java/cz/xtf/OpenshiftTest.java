package cz.xtf;

import cz.xtf.builder.builders.ApplicationBuilder;
import cz.xtf.builder.builders.DeploymentConfigBuilder;
import cz.xtf.builder.builders.PodBuilder;
import cz.xtf.core.config.OpenShiftConfig;
import cz.xtf.core.config.XTFConfig;
import cz.xtf.core.openshift.OpenShift;
import cz.xtf.core.openshift.OpenShiftWaiters;
import cz.xtf.core.openshift.OpenShifts;
import cz.xtf.core.openshift.helpers.ResourceFunctions;
import cz.xtf.core.waiting.SupplierWaiter;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.openshift.api.model.Build;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class OpenshiftTest {
    @Test
    public void testGetNamespace()  {
        OpenShift openShift = OpenShifts.master();
        openShift.clean().waitFor();

        openShift.addRoleToServiceAccount("view", "default");

        Pod wildflyPod = openShift.createPod(new PodBuilder("wildfly").container("wildfly").fromImage(XTFConfig.get("xtf.wildfly.image")).port(8080).pod().build());

        openShift.waiters().areExactlyNPodsRunning(1).waitFor();

        Assertions.assertThat(XTFConfig.get(OpenShiftConfig.OPENSHIFT_NAMESPACE)).isEqualTo(OpenShifts.master().getNamespace());

    }

}
