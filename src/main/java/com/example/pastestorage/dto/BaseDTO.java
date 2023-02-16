package com.example.pastestorage.dto;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public abstract class BaseDTO {
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(getClass().getSimpleName());
        builder.append(" [");

        // Get all public methods declared in the subclass
        Method[] methods = getClass().getMethods();
        for (int i = 0; i < methods.length; i++) {
            String methodName = methods[i].getName();
            if (methodName.startsWith("get") && !methodName.equals("getClass")) {
                String fieldName = methodName.substring(3);
                String fieldValue = "?";
                if (!sensitiveFields.contains(fieldName.toLowerCase())) {
                    try {
                        Object value = methods[i].invoke(this);
                        if (value != null) {
                            fieldValue = value.toString();
                        }
                    } catch (Exception ex) {
                        // Ignore exceptions
                    }
                }
                builder.append(fieldName);
                builder.append("=");
                builder.append(fieldValue);

                if (i < methods.length - 1) {
                    builder.append(", ");
                }
            }
        }

        builder.append("]");
        return builder.toString();
    }

    protected void addSensitiveField(String fieldName) {
        sensitiveFields.add(fieldName);
    }

    private static Set<String> sensitiveFields = new HashSet<>(Arrays.asList("password"));
}
