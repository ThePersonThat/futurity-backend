package com.alex.futurity.authorizationserver.utils.html;

import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ConfirmationHtmlGenerator extends HtmlGenerator {
    private final String PATH = "html_templates/confirmation.html";

    @PostConstruct
    private void loadHtmlFile() {
        readHtmlFile(PATH);
    }

    @Override
    public String generateHtml(String... values) {
        return html.replaceAll("\\{(.*?)}", values[0]);
    }
}
