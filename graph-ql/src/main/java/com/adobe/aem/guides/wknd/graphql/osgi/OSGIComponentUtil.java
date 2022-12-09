package com.adobe.aem.guides.wknd.graphql.osgi;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.service.component.runtime.ServiceComponentRuntime;
import org.osgi.service.component.runtime.dto.ComponentDescriptionDTO;
import org.osgi.util.promise.Deferred;
import org.osgi.util.promise.Promise;

import java.util.Optional;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OSGIComponentUtil {

    public static void refreshComponent(BundleContext bundleContext, String componentName) {
        final ServiceComponentRuntime serviceComponentRuntime = getServiceComponentRuntime(bundleContext);
        final ComponentDescriptionDTO componentDescriptionDTO =
                OSGIComponentUtil.getComponentDTO(bundleContext, serviceComponentRuntime, componentName);
        OSGIComponentUtil.disableComponent(componentDescriptionDTO, serviceComponentRuntime)
                .then(promise -> OSGIComponentUtil.enableComponent(componentDescriptionDTO, serviceComponentRuntime));
    }

    private static ServiceComponentRuntime getServiceComponentRuntime(BundleContext bundleContext) {
        final ServiceReference<ServiceComponentRuntime> scrReference = bundleContext
                .getServiceReference(ServiceComponentRuntime.class);
        return bundleContext.getService(scrReference);
    }

    private static ComponentDescriptionDTO getComponentDTO(BundleContext bundleContext,
                                                           ServiceComponentRuntime scr, String componentName) {
        for (Bundle bundle : bundleContext.getBundles()) {
            ComponentDescriptionDTO dto = scr.getComponentDescriptionDTO(bundle, componentName);
            if (dto != null) {
                return dto;
            }
        }
        return null;
    }

    private static Promise<Void> enableComponent(ComponentDescriptionDTO dto, ServiceComponentRuntime scr) {
        return Optional.ofNullable(dto)
                .map(scr::enableComponent)
                .orElse(getFailedPromise());
    }

    private static Promise<Void> disableComponent
            (ComponentDescriptionDTO dto, ServiceComponentRuntime scr) {
        return Optional.ofNullable(dto)
                .map(scr::disableComponent)
                .orElse(getFailedPromise());
    }

    private static Promise<Void> getFailedPromise() {
        final Deferred<Void> deferred = new Deferred<>();
        deferred.fail(new Exception());
        return deferred.getPromise();
    }

}