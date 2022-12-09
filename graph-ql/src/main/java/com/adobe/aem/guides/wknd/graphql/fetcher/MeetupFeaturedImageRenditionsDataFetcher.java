package com.adobe.aem.guides.wknd.graphql.fetcher;

import com.day.cq.dam.api.Asset;
import com.day.cq.dam.api.Rendition;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.graphql.api.SlingDataFetcher;
import org.apache.sling.graphql.api.SlingDataFetcherEnvironment;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.osgi.service.component.annotations.Component;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Component(service = SlingDataFetcher.class,
        property = {"name=meetup/featuredImageRenditions"})
@Slf4j
public class MeetupFeaturedImageRenditionsDataFetcher implements SlingDataFetcher<Object> {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    public @Nullable Object get(@NotNull SlingDataFetcherEnvironment env) throws JsonProcessingException {
        final String cftResourcePath = getCFResourcePathFromSlingEnv(env);
        final ResourceResolver resourceResolver = Objects.requireNonNull(env.getCurrentResource())
                .getResourceResolver();
        final String cfJcrContentResourcePath = cftResourcePath + "/jcr:content/data/master";
        final List<Rendition> renditionList = Optional.ofNullable(
                        resourceResolver.getResource(cfJcrContentResourcePath))
                .map(Resource::getValueMap)
                .map(valueMap -> valueMap.get("featuredImage", String.class))
                .map(resourceResolver::getResource)
                .map(imageString -> imageString.adaptTo(Asset.class))
                .map(Asset::getRenditions)
                .orElse(Collections.emptyList());

        final Map<String, String> renditionsMap = renditionList.stream()
                .collect(Collectors.toMap(Rendition::getName, Rendition::getPath));

        return OBJECT_MAPPER.writeValueAsString(renditionsMap);
    }

    private static String getCFResourcePathFromSlingEnv(SlingDataFetcherEnvironment env) {
        return Optional.ofNullable(env.getParentObject())
                .map(parentObject -> (Map<?, ?>) parentObject)
                .map(propertiesMap -> (String) propertiesMap.get("_path"))
                .map(cfPath -> URLDecoder.decode(cfPath, StandardCharsets.UTF_8))
                .orElse(StringUtils.EMPTY);
    }


}
