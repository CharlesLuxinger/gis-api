package com.github.charlesluxinger.gisapi.controller.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.io.IOException;

/**
 * @author Charles Luxinger
 * @version 1.0.0 12/12/20
 */
public class GeoJsonPointDeserializer extends StdDeserializer<GeoJsonPoint> {

    public GeoJsonPointDeserializer() {
        this(null);
    }

    protected GeoJsonPointDeserializer(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public GeoJsonPoint deserialize(final JsonParser parser, final DeserializationContext ctxt) throws IOException {
        JsonNode node = parser.getCodec().readTree(parser);
        JsonNode coordinates = node.get("coordinates");

        return new GeoJsonPoint(coordinates.get(0).asDouble(), coordinates.get(1).asDouble());
    }
}
