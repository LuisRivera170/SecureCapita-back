package com.lara.securecapita.enumeration;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum VerificationType {

    ACCOUNT("ACCOUNT"),
    PASSWORD("PASSWORD");

    private final String type;

    public String getType() {
        return this.type.toLowerCase();
    }

}
