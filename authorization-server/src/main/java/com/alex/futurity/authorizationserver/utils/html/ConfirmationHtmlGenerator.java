package com.alex.futurity.authorizationserver.utils.html;

import com.alex.futurity.authorizationserver.utils.FileReader;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class ConfirmationHtmlGenerator implements HtmlGenerator {
    private String html;

    public static final String CONFIRMATION_PATH = "html_templates/confirmation.html";

    @PostConstruct
    private void loadHtmlFile() {
        html = FileReader.readFileToString(CONFIRMATION_PATH);
    }

    @Override
    public String generateHtml(String... values) {
        return html.replaceAll("\\{(.*?)}", values[0]);
    }
}
