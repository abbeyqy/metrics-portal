/*
 * Copyright 2019 Inscope Metrics, Inc
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
package models.internal;


/**
 * A generic time-series query.
 *
 * @author Brandon Arp (brandon dot arp at inscopemetrics dot com)
 */
public interface MetricsQuery {
    /**
     * Gets the raw string query.
     *
     * @return the query
     */
    String getQuery();

    /**
     * The serialization format of this query.
     *
     * @return the serialization format.
     */
    MetricsQueryFormat getQueryFormat();
}
