package net.ddns.wadosm.process.entity;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.Instant;

@Document
@Builder
@Data
@CompoundIndexes({
        @CompoundIndex(name = "unique_symbol_timestamp_quoteId_level",
                def = "{'symbol': 1, 'timestamp': 1, 'quoteId': 1, 'level': 1}",
                unique = true)
})
public class TickRecordEntity {

    @Id
    private String id;

    private BigDecimal ask;

    private BigDecimal bid;

    private Long askVolume;

    private Long bidVolume;

    private BigDecimal high;

    private BigDecimal low;

    private BigDecimal spreadRaw;

    private BigDecimal spreadTable;

    @Indexed
    private String symbol;

    private int quoteId;

    private int level;

    @Indexed
    private Instant timestamp;

}
