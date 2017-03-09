/*
 * Copyright 2017 Kryptnostic, Inc. (dba Loom)
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
 *
 */

package com.dataloom.edm.type;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.UUID;

import org.apache.olingo.commons.api.edm.FullQualifiedName;

import com.dataloom.authorization.securable.AbstractSchemaAssociatedSecurableType;
import com.dataloom.authorization.securable.SecurableObjectType;
import com.dataloom.client.serialization.SerializationConstants;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

/**
 * @author Matthew Tamayo-Rios &lt;matthew@kryptnostic.com&gt;
 */
public class EntityType extends ComplexType {
    private final LinkedHashSet<UUID> key;
    private final Optional<UUID>      baseType;
    private transient int h = 0;

    @JsonCreator
    public EntityType(
            @JsonProperty( SerializationConstants.ID_FIELD ) Optional<UUID> id,
            @JsonProperty( SerializationConstants.TYPE_FIELD ) FullQualifiedName type,
            @JsonProperty( SerializationConstants.TITLE_FIELD ) String title,
            @JsonProperty( SerializationConstants.DESCRIPTION_FIELD ) Optional<String> description,
            @JsonProperty( SerializationConstants.SCHEMAS ) Set<FullQualifiedName> schemas,
            @JsonProperty( SerializationConstants.KEY_FIELD ) LinkedHashSet<UUID> key,
            @JsonProperty( SerializationConstants.PROPERTIES_FIELD ) LinkedHashSet<UUID> properties,
            @JsonProperty( SerializationConstants.BASE_TYPE_FIELD ) Optional<UUID> baseType ) {
        super( id, type, title, description, schemas, properties, baseType );
        this.key = Preconditions.checkNotNull( key, "Entity set key properties cannot be null" );
        Preconditions.checkArgument( !this.key.isEmpty(), "Key properties cannot be empty" );
        this.baseType = baseType;
    }

    public EntityType(
            UUID id,
            FullQualifiedName type,
            String title,
            Optional<String> description,
            Set<FullQualifiedName> schemas,
            LinkedHashSet<UUID> key,
            LinkedHashSet<UUID> properties,
            Optional<UUID> baseType ) {
        this( Optional.of( id ), type, title, description, schemas, key, properties, baseType );
    }

    public EntityType(
            FullQualifiedName type,
            String title,
            String description,
            Set<FullQualifiedName> schemas,
            LinkedHashSet<UUID> key,
            LinkedHashSet<UUID> properties,
            Optional<UUID> baseType ) {
        this( Optional.absent(), type, title, Optional.of( description ), schemas, key, properties, baseType );
    }

    // TODO: It seems the objects do not allow property types from the different schemas.
    @JsonProperty( SerializationConstants.KEY_FIELD )
    public Set<UUID> getKey() {
        return Collections.unmodifiableSet( key );
    }

    @JsonProperty( SerializationConstants.BASE_TYPE_FIELD )
    public Optional<UUID> getBaseType() {
        return baseType;
    }

    @JsonIgnore
    public SecurableObjectType getCategory() {
        return SecurableObjectType.EntityType;
    }

    @Override public boolean equals( Object o ) {
        if ( this == o )
            return true;
        if ( o == null || getClass() != o.getClass() )
            return false;
        if ( !super.equals( o ) )
            return false;

        EntityType that = (EntityType) o;

        if ( !key.equals( that.key ) )
            return false;
        return baseType.equals( that.baseType );
    }

    @Override public int hashCode() {
        if ( h == 0 ) {
            int result = super.hashCode();
            result = 31 * result + key.hashCode();
            result = 31 * result + baseType.hashCode();
            h = result;
        }
        return h;
    }

    @Override
    public String toString() {
        return "EntityType [key=" + key + ", properties=" + getProperties() + ", baseType=" + baseType + ", schemas="
                + schemas + ", type=" + type + ", id=" + id + ", title=" + title + ", description=" + description + "]";
    }
}
