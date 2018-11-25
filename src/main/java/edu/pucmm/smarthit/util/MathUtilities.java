package edu.pucmm.smarthit.util;

public class MathUtilities {
    public Float getAverage(String... values) {
        float result = 0.0f;

        for (String value : values) {
            result += Float.parseFloat(value);
        }

        result = result / values.length;

        return result;
    }
}
