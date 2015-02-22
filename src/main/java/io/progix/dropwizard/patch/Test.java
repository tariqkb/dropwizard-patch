package io.progix.dropwizard.patch;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        ObjectMapper mapper = Jackson.newObjectMapper();

        JsonNode node1 = mapper.readTree("1");
        JsonNode node2 = mapper.readTree("[1, 2]");
        JsonNode node3 = mapper.readTree("{\"1\": \"2\"}");

    }
}
