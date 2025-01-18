package net.ddns.wadosm;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.IOException;
import java.time.Instant;

@SpringBootApplication
public class XtbTicksDumpApplication {

    public static void main(String[] args) {
        SpringApplication.run(XtbTicksDumpApplication.class, args);
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addDeserializer(Instant.class, new MillisecondsToInstantDeserializer());
        objectMapper.registerModule(javaTimeModule);
        return objectMapper;
    }

    public static class MillisecondsToInstantDeserializer extends JsonDeserializer<Instant> {
        @Override
        public Instant deserialize(JsonParser jsonParser, DeserializationContext context) throws IOException {
            long milliseconds = jsonParser.getLongValue();
            return Instant.ofEpochMilli(milliseconds);
        }
    }
}
