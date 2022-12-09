package com.adobe.aem.guides.wknd.graphql.fetcher;


import lombok.extern.slf4j.Slf4j;
import org.apache.sling.graphql.api.SlingDataFetcher;
import org.apache.sling.graphql.api.SlingDataFetcherEnvironment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.service.component.annotations.Component;

@Component(service = SlingDataFetcher.class,
        property = {"name=meetup/additionalProperty"})
@Slf4j
public class MeetupAdditionalPropertyDataFetcher implements SlingDataFetcher<Object> {

    @Override
    public @Nullable Object get(@NotNull SlingDataFetcherEnvironment env) {
        return "test";
    }

}
