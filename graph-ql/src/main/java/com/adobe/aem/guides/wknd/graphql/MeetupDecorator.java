package com.adobe.aem.guides.wknd.graphql;


import com.adobe.aem.graphql.sites.api.Decoration;
import com.adobe.aem.graphql.sites.api.Decorator;
import com.adobe.aem.graphql.sites.api.ElementDecoration;
import com.adobe.aem.graphql.sites.api.Field;
import com.adobe.aem.graphql.sites.api.FieldDecoration;
import com.adobe.aem.graphql.sites.api.SchemaElement;


public class MeetupDecorator implements Decorator {

    private Decorator originalDecorator;

    public MeetupDecorator(Decorator originalDecorator) {
        this.originalDecorator = originalDecorator;
    }


    @Override
    public void decorate(Decoration decoration, StringBuilder stringBuilder) {
        originalDecorator.decorate(decoration, stringBuilder);
    }

    @Override
    public void decorateElement(ElementDecoration elementDecoration, StringBuilder stringBuilder,
                                SchemaElement schemaElement) {
        originalDecorator.decorateElement(elementDecoration, stringBuilder, schemaElement);
    }

    @Override
    public void decorateField(FieldDecoration fieldDecoration, StringBuilder stringBuilder, Field field) {
        if (FieldDecoration.AFTER_FIELD_SAME_LINE == fieldDecoration && "String".equals(field.getType().getName())) {
            if ("additionalProperty".equals(field.getName())) {
                stringBuilder.append(" @fetcher(name:\"meetup/additionalProperty\")");
            }
            if ("featuredImageRenditions".equals(field.getName())) {
                stringBuilder.append(" @fetcher(name:\"meetup/featuredImageRenditions\")");
            }
        } else {
            originalDecorator.decorateField(fieldDecoration, stringBuilder, field);
        }
    }
}
