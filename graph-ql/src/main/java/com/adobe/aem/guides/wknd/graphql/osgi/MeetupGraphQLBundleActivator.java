package com.adobe.aem.guides.wknd.graphql.osgi;

import org.osgi.annotation.bundle.Header;
import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;


@Header(name = Constants.BUNDLE_ACTIVATOR, value = "${@class}")
public class MeetupGraphQLBundleActivator implements BundleActivator, BundleListener {

    private static final String COMPONENT_NAME = "com.adobe.aem.graphql.sites.adapters.SlingSchemaServlet";

    @Override
    public void start(BundleContext bundleContext) {
        bundleContext.addBundleListener(this);
        OSGIComponentUtil.refreshComponent(bundleContext, COMPONENT_NAME);
    }

    @Override
    public void stop(BundleContext bundleContext) {
        bundleContext.removeBundleListener(this);
        OSGIComponentUtil.refreshComponent(bundleContext, COMPONENT_NAME);
    }

    @Override
    public void bundleChanged(BundleEvent bundleEvent) {
        final var bundleContext = bundleEvent.getBundle().getBundleContext();
        final int eventType = bundleEvent.getType();
        if (BundleEvent.STARTED == eventType) {
            OSGIComponentUtil.refreshComponent(bundleContext, COMPONENT_NAME);
        }
    }
}