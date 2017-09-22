package io.fundrequest.web.request.dto;

import java.util.Map;

public class ChartDto {
    private String label;
    public Map<String, String> data;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public Map<String, String> getData() {
        return data;
    }

    public void setData(Map<String, String> data) {
        this.data = data;
    }
}
