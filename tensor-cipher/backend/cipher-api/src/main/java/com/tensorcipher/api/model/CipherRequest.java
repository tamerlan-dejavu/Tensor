package com.tensorcipher.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class CipherRequest {

    @NotBlank(message = "Text must not be blank")
    private String text;

    @NotBlank(message = "Key must not be blank")
    @Size(min = 1, message = "Key must have at least 1 character")
    private String key;

    public CipherRequest() {}

    public CipherRequest(String text, String key) {
        this.text = text;
        this.key = key;
    }

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
}
