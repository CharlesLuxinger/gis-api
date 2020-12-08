package com.github.charlesluxinger.gisapi.infra.config;

import com.github.charlesluxinger.gisapi.domain.model.CoordinateType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.mongodb.config.AbstractReactiveMongoConfiguration;
import org.springframework.data.mongodb.core.convert.MongoCustomConversions;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;

import java.util.Arrays;

/**
 * @author Charles Luxinger
 * @version 1.0.0 07/12/20
 */
@EnableReactiveMongoRepositories(basePackages = "com.github.charlesluxinger.gisapi.infra.repository")
public class MongoDBConfig extends AbstractReactiveMongoConfiguration {

    @Value("${spring.data.mongodb.database}")
    private String database;

    @Override
    protected String getDatabaseName() {
        return database;
    }

    @Bean
    public MongoCustomConversions customConversions() {
        Converter<String, CoordinateType> stringCoordinateType = new Converter<>() {
            @Override
            public CoordinateType convert(String s) {
                return CoordinateType.fromValue(s);
            }
        };

        Converter<CoordinateType, String> coordinateTypeString = new Converter<>() {
            @Override
            public String convert(CoordinateType s) {
                return s.getValue();
            }
        };

        return new MongoCustomConversions(Arrays.asList(stringCoordinateType, coordinateTypeString));
    }

}
