package com.simulador.financiero.infrastructure.email;

import java.util.Map;

import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Component
public class EmailTemplateRenderer {

    private static final String EMAIL_TEMPLATE_PATH = "emails/";
    private final SpringTemplateEngine templateEngine;

    public EmailTemplateRenderer(SpringTemplateEngine templateEngine) {
        this.templateEngine = templateEngine;
    }

    public String render(String templateName, Map<String, Object> model) {
        Context context = new Context();
        context.setVariables(model);
        return templateEngine.process(EMAIL_TEMPLATE_PATH + templateName, context);
    }
}
