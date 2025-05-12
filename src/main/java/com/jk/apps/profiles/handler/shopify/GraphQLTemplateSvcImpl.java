package com.jk.apps.profiles.handler.shopify;
import groovy.text.markup.MarkupTemplateEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.view.groovy.GroovyMarkupConfigurer;

import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;

import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;

@Service
public class GraphQLTemplateSvcImpl {

    private static final Logger LOGGER = LoggerFactory.getLogger(GraphQLTemplateSvcImpl.class);

    private final MarkupTemplateEngine engine;

    @Autowired
    public GraphQLTemplateSvcImpl(GroovyMarkupConfigurer configurer) {
        this.engine = configurer.getTemplateEngine();
    }

    public String render(String templateName, Map<String, Object> model) {
        try {
            LOGGER.info("Rendering template {}", templateName);
            // Load template from classpath
            try (InputStreamReader reader = new InputStreamReader(
                    Objects.requireNonNull(getClass().getResourceAsStream("/templates/graphql/" + templateName)))) {

                SimpleTemplateEngine engine = new SimpleTemplateEngine();
                Template template = engine.createTemplate(reader);

                LOGGER.info("Rendering template {} reader {} template {}", templateName, reader, template);

                return template.make(model).toString();
            }
        } catch (Exception e) {
            throw new RuntimeException("Error rendering GraphQL template: " + templateName, e);
        }
    }
}
