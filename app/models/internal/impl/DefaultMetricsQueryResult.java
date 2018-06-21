/*
 * Copyright 2017 Smartsheet
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package models.internal.impl;

import com.arpnetworking.commons.builder.OvalBuilder;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMultimap;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import models.internal.AlertTrigger;
import models.internal.MetricsQueryResult;
import net.sf.oval.constraint.Min;
import net.sf.oval.constraint.NotEmpty;
import net.sf.oval.constraint.NotNull;
import org.joda.time.DateTime;

import java.util.List;
import java.util.function.Function;

/**
 * Model class to represent a metrics query response from KairosDB.
 *
 * @author Brandon Arp (brandon dot arp at smartsheet dot com)
 */
public final class DefaultMetricsQueryResult implements MetricsQueryResult {
    @JsonAnyGetter
    public ImmutableMap<String, Object> getOtherArgs() {
        return _otherArgs;
    }

    @Override
    public ImmutableList<? extends MetricsQueryResult.Query> getQueries() {
        return _queries;
    }

    private DefaultMetricsQueryResult(final Builder builder) {
        _otherArgs = builder._otherArgs;
        _queries = builder._queries;
    }

    private final ImmutableMap<String, Object> _otherArgs;
    private final ImmutableList<? extends MetricsQueryResult.Query> _queries;

    /**
     * Implementation of the builder pattern for {@link DefaultMetricsQueryResult}.
     *
     * @author Brandon Arp (brandon dot arp at smartsheet dot com)
     */
    public static final class Builder extends OvalBuilder<DefaultMetricsQueryResult> {
        /**
         * Public constructor.
         */
        public Builder() {
            super(DefaultMetricsQueryResult::new);
        }

        /**
         * Adds an "unknown" parameter. Optional.
         *
         * @param key key for the entry
         * @param value value for the entry
         * @return this {@link Builder}
         */
        @JsonAnySetter
        public Builder addOtherArg(final String key, final Object value) {
            _otherArgs = new ImmutableMap.Builder<String, Object>().putAll(_otherArgs).put(key, value).build();
            return this;
        }

        /**
         * Sets the queries. Cannot be null.
         *
         * @param value the name
         * @return this {@link Builder}
         */
        public Builder setQueries(final ImmutableList<? extends MetricsQueryResult.Query> value) {
            _queries = value;
            return this;
        }

        @NotNull
        private ImmutableList<? extends MetricsQueryResult.Query> _queries;
        @NotNull
        private ImmutableMap<String, Object> _otherArgs = ImmutableMap.of();
    }

    /**
     * Model class to represent a query in a metrics query response.
     *
     * @author Brandon Arp (brandon dot arp at smartsheet dot com)
     */
    public static final class Query implements MetricsQueryResult.Query {
        private Query(final Builder builder) {
            _otherArgs = builder._otherArgs;
            _sampleSize = builder._sampleSize;
            _results = builder._results;
        }

        @JsonAnyGetter
        public ImmutableMap<String, Object> getOtherArgs() {
            return _otherArgs;
        }

        @Override
        @JsonProperty("sample_size")
        public long getSampleSize() {
            return _sampleSize;
        }

        @Override
        public ImmutableList<? extends MetricsQueryResult.Result> getResults() {
            return _results;
        }

        private final ImmutableMap<String, Object> _otherArgs;
        private final long _sampleSize;
        private final ImmutableList<? extends MetricsQueryResult.Result> _results;

        /**
         * Implementation of the builder pattern for {@link Query}.
         *
         * @author Brandon Arp (brandon dot arp at smartsheet dot com)
         */
        public static final class Builder extends OvalBuilder<MetricsQueryResult.Query> {
            /**
             * Public constructor.
             */
            public Builder() {
                super(Query::new);
            }

            /**
             * Adds an "unknown" parameter. Optional.
             *
             * @param key key for the entry
             * @param value value for the entry
             * @return this {@link Builder}
             */
            @JsonAnySetter
            public Builder addOtherArg(final String key, final Object value) {
                _otherArgs = new ImmutableMap.Builder<String, Object>().putAll(_otherArgs).put(key, value).build();
                return this;
            }

            /**
             * Sets the sample size. Required.
             *
             * @param value the sample size
             * @return this {@link Builder}
             */
            @JsonProperty("sample_size")
            public Builder setSampleSize(final long value) {
                _sampleSize = value;
                return this;
            }

            /**
             * Sets the results object. Required.
             *
             * @param value the results
             * @return this {@link Builder}
             */
            public Builder setResults(final ImmutableList<? extends MetricsQueryResult.Result> value) {
                _results = value;
                return this;
            }

            @NotNull
            private ImmutableList<? extends MetricsQueryResult.Result> _results;
            @Min(0)
            private long _sampleSize = 0;
            @NotNull
            private ImmutableMap<String, Object> _otherArgs = ImmutableMap.of();
        }
    }

    /**
     * Model class representing the result object in a kairosdb metrics query.
     *
     * @author Brandon Arp (brandon dot arp at smartsheet dot com)
     */
    public static final class Result implements MetricsQueryResult.Result {
        @Override
        public String getName() {
            return _name;
        }

        @Override
        public ImmutableList<? extends MetricsQueryResult.DataPoint> getValues() {
            return _values;
        }

        @Override
        public ImmutableMultimap<String, String> getTags() {
            return _tags;
        }

        @Override
        public ImmutableList<AlertTrigger> getAlerts() {
            return _alerts;
        }

        @Override
        public ImmutableList<? extends MetricsQueryResult.QueryGroupBy> getGroupBy() {
            return _groupBy;
        }


        @JsonAnyGetter
        public ImmutableMap<String, Object> getOtherArgs() {
            return _otherArgs;
        }

        private Result(final Builder builder) {
            _otherArgs = builder._otherArgs;
            _values = builder._values;
            _alerts = builder._alerts;
            _name = builder._name;
            _tags = builder._tags;
            _groupBy = builder._groupBy;
        }

        private final ImmutableMap<String, Object> _otherArgs;
        private final ImmutableList<? extends MetricsQueryResult.DataPoint> _values;
        private final ImmutableList<AlertTrigger> _alerts;
        private final String _name;
        private final ImmutableMultimap<String, String> _tags;
        private final ImmutableList<? extends MetricsQueryResult.QueryGroupBy> _groupBy;

        /**
         * Implementation of the builder pattern for a {@link Result}.
         *
         * @author Brandon Arp (brandon dot arp at smartsheet dot com)
         */
        public static final class Builder extends OvalBuilder<MetricsQueryResult.Result> {
            /**
             * Public constructor.
             */
            public Builder() {
                super(Result::new);
            }

            /**
             * Adds an "unknown" parameter. Optional.
             *
             * @param key key for the entry
             * @param value value for the entry
             * @return this {@link Builder}
             */
            @JsonAnySetter
            public Builder addOtherArg(final String key, final Object value) {
                _otherArgs = new ImmutableMap.Builder<String, Object>().putAll(_otherArgs).put(key, value).build();
                return this;
            }

            /**
             * Set other args. Optional.
             *
             * @param value value for the other args
             * @return this {@link Builder}
             */
            @JsonAnySetter
            public Builder setOtherArgs(final ImmutableMap<String, Object> value) {
                _otherArgs = value;
                return this;
            }

            /**
             * Sets the values list. Optional. Cannot be null.
             *
             * @param value the values
             * @return this {@link Builder}
             */
            public Builder setValues(final ImmutableList<? extends MetricsQueryResult.DataPoint> value) {
                _values = value;
                return this;
            }

            /**
             * Sets the name. Required. Cannot be null or empty.
             *
             * @param value the name of the metric
             * @return this {@link Builder}
             */
            public Builder setName(final String value) {
                _name = value;
                return this;
            }

            /**
             * Sets the tags. Required. Cannot be null or empty.
             *
             * @param value the tags
             * @return this {@link Builder}
             */
            public Builder setTags(final ImmutableMultimap<String, String> value) {
                _tags = value;
                return this;
            }

            /**
             * Sets the alerts. Optional. Cannot be null. Defaults to empty.
             *
             * @param value the alerts
             * @return this {@link Builder}
             */
            public Builder setAlerts(final ImmutableList<AlertTrigger> value) {
                _alerts = value;
                return this;
            }

            /**
             * Sets the group by. Optional. Cannot be null.
             *
             * @param value the group by list
             * @return this {@link Builder}
             */
            public Builder setGroupBy(final ImmutableList<? extends MetricsQueryResult.QueryGroupBy> value) {
                _groupBy = value;
                return this;
            }


            @NotNull
            @NotEmpty
            private String _name;
            @NotNull
            private ImmutableList<? extends MetricsQueryResult.DataPoint> _values = ImmutableList.of();
            @NotNull
            private ImmutableMap<String, Object> _otherArgs = ImmutableMap.of();
            @NotNull
            private ImmutableList<AlertTrigger> _alerts = ImmutableList.of();
            @NotNull
            @NotEmpty
            private ImmutableMultimap<String, String> _tags = ImmutableMultimap.of();
            @NotNull
            private ImmutableList<? extends MetricsQueryResult.QueryGroupBy> _groupBy = ImmutableList.of();
        }
    }

    /**
     * Model class for a data point in a kairosdb metrics query.
     *
     * @author Brandon Arp (brandon dot arp at smartsheet dot com)
     */
    public static final class DataPoint implements MetricsQueryResult.DataPoint {
        @Override
        public DateTime getTime() {
            return _time;
        }

        @Override
        public Object getValue() {
            return _value;
        }

        @SuppressFBWarnings("UPM_UNCALLED_PRIVATE_METHOD") // Invoked reflectively by Jackson
        @JsonValue
        private ImmutableList<Object> serialize() {
            return ImmutableList.of(_time.getMillis(), _value);
        }

        private DataPoint(final Builder builder) {
            _time = builder._time;
            _value = builder._value;
        }

        private final DateTime _time;
        private final Object _value;

        /**
         * Implementation of the builder pattern for a {@link DataPoint}.
         *
         * @author Brandon Arp (brandon dot arp at smartsheet dot com)
         */
        public static final class Builder extends OvalBuilder<MetricsQueryResult.DataPoint> {
            /**
             * Public constructor.
             */
            public Builder() {
                super(DataPoint::new);
            }

            /**
             * Public constructor.
             *
             * @param arr a 2-element {@link List} containing the time and value at that time.
             */
            @JsonCreator
            public Builder(final List<Object> arr) {
                super(DataPoint::new);
                final long timestamp = (long) arr.get(0);
                _time = new DateTime(timestamp);
                _value = arr.get(1);
            }

            /**
             * Sets the time. Required. Cannot be null.
             *
             * @param value the time
             * @return this {@link Builder}
             */
            public Builder setTime(final DateTime value) {
                _time = value;
                return this;
            }

            /**
             * Sets the value. Required. Cannot be null.
             *
             * @param value the value
             * @return this {@link Builder}
             */
            public Builder setValue(final Object value) {
                _value = value;
                return this;
            }

            @NotNull
            private DateTime _time;
            @NotNull
            private Object _value;
        }
    }

    /**
     * Model for the group_by fields in the {@link Result}.
     *
     * @author Brandon Arp (brandon dot arp at smartsheet dot com)
     */
    public abstract static class QueryGroupBy implements MetricsQueryResult.QueryGroupBy {
        private QueryGroupBy(final Builder<?, ?> builder) {
        }

        /**
         * Implementation of the builder pattern for a {@link QueryGroupBy}.
         *
         * @param <B> type of the builder
         * @param <T> type of the thing to be built
         * @author Brandon Arp (brandon dot arp at smartsheet dot com)
         */
        public abstract static class Builder<B extends Builder<B, T>, T extends MetricsQueryResult.QueryGroupBy> extends OvalBuilder<T> {

            /**
             * Protected constructor.
             *
             * @param targetConstructor the constructor for the QueryGroupBy
             * @param <B> Type of the builder
             */
            protected <B extends com.arpnetworking.commons.builder.Builder<T>> Builder(final Function<B, T> targetConstructor) {
                super(targetConstructor);
            }

            /**
             * Gets the instance of the {@link Builder} with the proper type.
             *
             * @return this {@link Builder}
             */
            protected abstract B self();
        }
    }

    /**
     * Model for the group_by fields of type "tag" in the {@link Result}.
     *
     * @author Brandon Arp (brandon dot arp at smartsheet dot com)
     */
    public static final class QueryTagGroupBy extends QueryGroupBy implements MetricsQueryResult.QueryTagGroupBy{
        public ImmutableList<String> getTags() {
            return _tags;
        }

        public ImmutableMap<String, String> getGroup() {
            return _group;
        }

        private QueryTagGroupBy(final Builder builder) {
            super(builder);
            _tags = builder._tags;
            _group = builder._group;
        }

        private final ImmutableList<String> _tags;
        private final ImmutableMap<String, String> _group;

        /**
         * Implementation of the builder pattern for a {@link QueryTagGroupBy}.
         *
         * @author Brandon Arp (brandon dot arp at smartsheet dot com)
         */
        public static final class Builder extends QueryGroupBy.Builder<Builder, MetricsQueryResult.QueryTagGroupBy> {
            /**
             * Public constructor.
             */
            public Builder() {
                super(QueryTagGroupBy::new);
            }

            /**
             * Sets the tags. Required. Cannot be null or empty.
             *
             * @param value the tags
             * @return this {@link Builder}
             */
            public Builder setTags(final ImmutableList<String> value) {
                _tags = value;
                return self();
            }

            /**
             * Sets the group. Required. Cannot be null or empty.
             *
             * @param value the group
             * @return this {@link Builder}
             */
            public Builder setGroup(final ImmutableMap<String, String> value) {
                _group = value;
                return self();
            }

            /**
             * Gets the instance of the {@link Builder} with the proper type.
             *
             * @return this {@link Builder}
             */
            protected Builder self() {
                return this;
            }

            @NotNull
            @NotEmpty
            private ImmutableList<String> _tags;
            @NotNull
            @NotEmpty
            private ImmutableMap<String, String> _group;
        }
    }

    /**
     * Model for the group_by fields of type "type" in the {@link Result}.
     *
     * @author Brandon Arp (brandon dot arp at smartsheet dot com)
     */
    public static final class QueryTypeGroupBy extends QueryGroupBy implements MetricsQueryResult.QueryTypeGroupBy {
        public String getType() {
            return _type;
        }

        private QueryTypeGroupBy(final Builder builder) {
            super(builder);
            _type = builder._type;
        }

        private final String _type;

        /**
         * Implementation of the builder pattern for a {@link QueryTypeGroupBy}.
         *
         * @author Brandon Arp (brandon dot arp at smartsheet dot com)
         */
        public static final class Builder extends QueryGroupBy.Builder<Builder, MetricsQueryResult.QueryTypeGroupBy> {
            /**
             * Public constructor.
             */
            public Builder() {
                super(QueryTypeGroupBy::new);
            }

            /**
             * Sets the type. Required. Cannot be null or empty.
             *
             * @param value the type
             * @return this {@link Builder}
             */
            public Builder setType(final String value) {
                _type = value;
                return self();
            }

            /**
             * Gets the instance of the {@link Builder} with the proper type.
             *
             * @return this {@link Builder}
             */
            protected Builder self() {
                return this;
            }

            @NotNull
            @NotEmpty
            private String _type;
        }
    }
}