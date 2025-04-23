package com.tiendaweb.strategys.config;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BoletaConfig {
    private String filename;

    public static BoletaConfig defaultBoletaConfig() {
        return BoletaConfig.builder()
                .filename("boleta.pdf")
                .build();
    }
}
