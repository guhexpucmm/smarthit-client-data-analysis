package edu.pucmm.smarthit.util;

import java.util.UUID;

public class Utilities {
    public String generateCode() {
        return UUID.randomUUID().toString().replace("-", "");
    }
}
