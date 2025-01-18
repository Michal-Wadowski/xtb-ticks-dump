package net.ddns.wadosm.process;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import net.ddns.wadosm.process.dto.TickRecordDto;
import net.ddns.wadosm.process.entity.TickRecordEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Service
@Log4j2
@RequiredArgsConstructor
public class TickRecordsInMongoSnapshotService {

    private final ObjectMapper objectMapper;
    private final TickRecordMapper tickRecordMapper;
    private final TickRecordRepository tickRecordRepository;

    private final ExecutorService executorService = Executors.newFixedThreadPool(5);

    @SneakyThrows
    @KafkaListener(id = "db.snapshotIntoMongo", topics = "${xtb.topicName}")
    public void listenRecords(String content) {
        log.debug("received kafka message");
        processRecord(content);
        log.debug("done");
    }

    @SneakyThrows
    public void processRecord(String content) {
        TickRecordDto tickRecordDto = objectMapper.readValue(content, TickRecordDto.class);
        log.debug("Received tick record <<< {}", tickRecordDto);

        TickRecordEntity entity = tickRecordMapper.mapTickRecord(tickRecordDto);

        Optional<TickRecordEntity> found = tickRecordRepository.findBySymbolAndTimestampAndQuoteIdAndLevel(entity.getSymbol(), entity.getTimestamp(), entity.getQuoteId(), entity.getLevel());

        if (found.isEmpty()) {
            TickRecordEntity saved = tickRecordRepository.save(entity);
            log.debug("saved, entity: {}", saved);
        } else {
            log.debug("skipped, entity {} exists", entity);
        }
    }
}
