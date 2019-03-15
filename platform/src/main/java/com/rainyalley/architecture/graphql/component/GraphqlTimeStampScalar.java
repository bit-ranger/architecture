package com.rainyalley.architecture.graphql.component;

import graphql.language.StringValue;
import graphql.schema.Coercing;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import graphql.schema.GraphQLScalarType;

import java.util.Date;

/**
 * 时间戳标量
 * @author bin.zhang
 */
public class GraphqlTimeStampScalar extends GraphQLScalarType {

    private static final String DEFAULT_NAME = "TimeStampScalar";

    public GraphqlTimeStampScalar() {
        this(DEFAULT_NAME);
    }

    public GraphqlTimeStampScalar(final String name) {
        super(name, "TimeStampScalar type", new Coercing<Date, Long>() {

            @Override
            public Long serialize(Object input) {
                if (input instanceof Date) {
                    return ((Date) input).getTime();
                } else {
                    throw new CoercingSerializeException("Invalid value '" + input + "' for TimeStampScalar");
                }
            }

            @Override
            public Date parseValue(Object input) {
                if (!(input instanceof Long)) {
                    throw new CoercingParseValueException("Invalid value '" + input + "' for TimeStampScalar");
                }
                return new Date(Long.valueOf(String.valueOf(input)));
            }

            @Override
            public Date parseLiteral(Object input) {
                if (!(input instanceof StringValue)) {
                    return null;
                }
                String value = ((StringValue) input).getValue();
                return new Date(Long.valueOf(value));
            }
        });
    }

}
