package com.whg.chess.config;

import com.whg.chess.model.enums.PieceName;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Data
@Configuration
@ConfigurationProperties(prefix = "chess.pieces")
public class PieceNameMapping {
    private Map<PieceName, String> nameMappings = new HashMap<>();
}
