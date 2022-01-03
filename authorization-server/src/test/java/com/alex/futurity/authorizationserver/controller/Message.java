package com.alex.futurity.authorizationserver.controller;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Data
@AllArgsConstructor
public class Message {
    private String content;
    private String subject;

    public boolean contains(String text) {
        return content.contains(text);
    }

    public String getTextFromMessage(String regex) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);
        if (!matcher.find()) {
            return null;
        } else {
            return matcher.group();
        }
    }
}
