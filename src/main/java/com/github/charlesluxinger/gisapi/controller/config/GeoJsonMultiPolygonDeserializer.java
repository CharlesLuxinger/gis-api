package com.github.charlesluxinger.gisapi.controller.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Charles Luxinger
 * @version 1.0.0 12/12/20
 */
public class GeoJsonMultiPolygonDeserializer extends StdDeserializer<GeoJsonMultiPolygon> {

    public GeoJsonMultiPolygonDeserializer() {
        this(null);
    }

    protected GeoJsonMultiPolygonDeserializer(final Class<?> clazz) {
        super(clazz);
    }

    @Override
    public GeoJsonMultiPolygon deserialize(final JsonParser parser, final DeserializationContext ctxt) throws IOException {
        return createMultiPolygon((JsonNode) parser.getCodec().readTree(parser).get("coordinates"));
    }

    private GeoJsonMultiPolygon createMultiPolygon(final JsonNode coordinates) {
        if (isNotArray(coordinates)) {
            return new GeoJsonMultiPolygon(Collections.emptyList());
        }

        return new GeoJsonMultiPolygon(extractCoordinates(coordinates));
    }

    private List<GeoJsonPolygon> extractCoordinates(final JsonNode coordinates) {
        return Arrays
            .stream(new JsonNode[]{coordinates})
            .filter(this::isNotArray)
            .map(this::createJsonPolygon)
            .collect(Collectors.toList());
    }

    private GeoJsonPolygon createJsonPolygon(final JsonNode polygon) {
        var pointsList = Arrays
                .stream(new JsonNode[]{polygon})
                .filter(this::isNotArray)
                .map(p -> new Point(p.get(0).asDouble(), p.get(1).asDouble()))
                .collect(Collectors.toList());

        return new GeoJsonPolygon(pointsList);
    }

    private boolean isNotArray(final JsonNode node) {
        return !node.isArray();
    }

}
