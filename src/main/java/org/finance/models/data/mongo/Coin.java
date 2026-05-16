package org.finance.models.data.mongo;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.quarkus.mongodb.panache.PanacheMongoEntity;
import lombok.*;
import org.bson.codecs.pojo.annotations.BsonProperty;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Coin extends PanacheMongoEntity {
    @BsonProperty("code")
    private String code;
    @JsonProperty("codein")
    private String codeIn;
    @JsonProperty("name")
    private String name;
    @JsonProperty("high")
    private double high;
    @JsonProperty("low")
    private double low;
    @JsonProperty("bid")
    private double priceBid;
    @BsonProperty("quotationDate")
    private LocalDateTime quotationDate;
}
