package com.es2.bridge;

import java.util.LinkedHashMap;

public class APIMoodle implements APIServiceInterface {

    protected LinkedHashMap<String, String> content;

    public APIMoodle() {
        this.content = new LinkedHashMap<>();
    }

    @Override
    public String getContent(String contentId) {
        if ("0".equals(contentId)) {
            StringBuilder aggregatedContent = new StringBuilder();
            for (String value : content.values()) {
                aggregatedContent.append(value);
            }
            return aggregatedContent.toString();
        }

        return content.get(contentId);
    }

    @Override
    public String setContent(String content) {
        String contentId = String.valueOf(this.content.size());
        this.content.put(contentId, content);
        return contentId;
    }
}
