package org.finance.models.data.mongo;

import io.quarkus.mongodb.panache.PanacheMongoEntity;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Ticker extends PanacheMongoEntity {
    @BsonProperty("companyName")
    private String companyName;
    @BsonProperty("dynamicPrice")
    private double dynamicPrice;
    @BsonProperty("ticker")
    private String ticker;
    @BsonProperty("logoUrl")
    private String logoUrl;
    @BsonProperty("dataCotacao")
    private LocalDateTime dataCotacao;
}
