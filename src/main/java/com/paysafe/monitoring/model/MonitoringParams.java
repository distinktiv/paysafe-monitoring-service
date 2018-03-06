package com.paysafe.monitoring.model;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MonitoringParams {

    @NotNull
    @Pattern(regexp = "\\b(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]", message = "invalid hostname")
    private String url;

    private int interval;

    @Override
    public int hashCode(){
        return url.hashCode();
    }
}
