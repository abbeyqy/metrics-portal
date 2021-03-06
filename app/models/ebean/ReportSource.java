/*
 * Copyright 2018 Dropbox, Inc.
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
package models.ebean;

import io.ebean.annotation.CreatedTimestamp;
import io.ebean.annotation.SoftDelete;
import io.ebean.annotation.UpdatedTimestamp;

import java.sql.Timestamp;
import java.util.UUID;
import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.Table;

/**
 * Data Model for SQL storage of a report generation scheme.
 *
 * NOTE: This class is enhanced by Ebean to do things like lazy loading and
 * resolving relationships between beans. Therefore, including functionality
 * which serializes the state of the object can be dangerous (e.g. {@code toString},
 * {@code @Loggable}, etc.).
 *
 * @author Christian Briones (cbriones at dropbox dot com)
 */
@Entity
@Table(name = "report_sources", schema = "portal")
@DiscriminatorColumn(name = "type")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
// CHECKSTYLE.OFF: MemberNameCheck
public abstract class ReportSource {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private long id;
    @Column(name = "uuid")
    private UUID uuid;
    @CreatedTimestamp
    @Column(name = "created_at")
    private Timestamp createdAt;
    @UpdatedTimestamp
    @Column(name = "updated_at")
    private Timestamp updatedAt;
    @SoftDelete
    @Column(name = "deleted")
    private boolean deleted;

    public Long getId() {
        return id;
    }

    public void setId(final Long value) {
        id = value;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(final UUID value) {
        uuid = value;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public Timestamp getUpdatedAt() {
        return updatedAt;
    }

    /**
     * Transform this object into its internal representation.
     *
     * @return The internal representation of this {@code ReportSource}.
     */
    public abstract models.internal.reports.ReportSource toInternal();
}
// CHECKSTYLE.ON: MemberNameCheck
