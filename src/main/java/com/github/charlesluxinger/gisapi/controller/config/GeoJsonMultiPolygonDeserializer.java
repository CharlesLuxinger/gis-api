package com.github.charlesluxinger.gisapi.controller.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonMultiPolygon;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;

import java.io.IOException;
import java.util.ArrayList;

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
        JsonNode node = parser.getCodec().readTree(parser);
        JsonNode coordinates = node.get("coordinates");

        return createMultiPolygon(coordinates);
    }

    private GeoJsonMultiPolygon createMultiPolygon(final JsonNode coordinates) {
        var polygons = new ArrayList<GeoJsonPolygon>();

        if (coordinates.isArray()) {
            for (JsonNode polygon : coordinates) {
                if (polygon.isArray()) {
                    for (JsonNode points : polygon) {
                        if (points.isArray()) {
                            var pointsList = new ArrayList<Point>();
                            for (JsonNode eachPoint : points) {
                                pointsList.add(new Point(eachPoint.get(0).asDouble(), eachPoint.get(1).asDouble()));
                            }
                            polygons.add(new GeoJsonPolygon(pointsList));
                        }
                    }
                }
            }
        }

        return new GeoJsonMultiPolygon(polygons);
    }
}
