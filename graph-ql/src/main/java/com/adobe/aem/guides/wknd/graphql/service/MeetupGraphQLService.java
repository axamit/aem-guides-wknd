package com.adobe.aem.guides.wknd.graphql.service;

import com.adobe.aem.graphql.sites.api.BuilderException;
import com.adobe.aem.graphql.sites.api.Decorator;
import com.adobe.aem.graphql.sites.api.GraphQLService;
import com.adobe.aem.graphql.sites.api.Schema;
import com.adobe.aem.graphql.sites.api.SchemaError;
import com.adobe.aem.graphql.sites.api.SchemaSDL;
import com.adobe.aem.guides.wknd.graphql.MeetupDecorator;
import org.apache.sling.api.resource.Resource;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.propertytypes.ServiceRanking;


@Component(
        immediate = true,
        service = GraphQLService.class)
@ServiceRanking(Integer.MAX_VALUE)
public class MeetupGraphQLService implements GraphQLService {

    @Reference(target = "(component.name=com.adobe.aem.graphql.sites.base.GraphQLServiceImpl)")
    private GraphQLService originalGraphQLService;

    @Override
    public Schema getSchema(Resource resource) throws BuilderException {
        return originalGraphQLService.getSchema(resource);
    }

    @Override
    public Iterable<SchemaError> getSchemaErrors(Resource resource) throws BuilderException {
        return originalGraphQLService.getSchemaErrors(resource);
    }

    @Override
    public SchemaSDL getSchemaSDL(Resource resource) throws BuilderException {
        return originalGraphQLService.getSchemaSDL(resource);
    }

    @Override
    public SchemaSDL getSchemaSDL(Resource resource, Decorator decorator) throws BuilderException {
        return originalGraphQLService.getSchemaSDL(resource, new MeetupDecorator(decorator));
    }
}
