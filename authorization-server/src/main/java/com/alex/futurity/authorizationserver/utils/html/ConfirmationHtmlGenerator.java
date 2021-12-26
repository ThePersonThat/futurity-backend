package com.alex.futurity.authorizationserver.utils.html;

import com.alex.futurity.authorizationserver.utils.FileReader;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class ConfirmationHtmlGenerator implements HtmlGenerator {
    private final FileReader fileReader;
    private String html;

    public ConfirmationHtmlGenerator(FileReader fileReader) {
        this.fileReader = fileReader;
    }

    @PostConstruct
    private void loadHtmlFile() {
        html = fileReader.readFileToString("html_templates/confirmation.html");
    }

    @Override
    public String generateHtml(String... values) {
        return html.replaceAll("\\{(.*?)}", values[0]);
    }
}
