package net.ddns.wadosm.process;

import net.ddns.wadosm.process.entity.TickRecordEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.time.Instant;
import java.util.Optional;

public interface TickRecordRepository extends MongoRepository<TickRecordEntity, String> {

    Optional<TickRecordEntity> findBySymbolAndTimestampAndQuoteIdAndLevel(String symbol, Instant timestamp, Integer quoteId, Integer level);

}
