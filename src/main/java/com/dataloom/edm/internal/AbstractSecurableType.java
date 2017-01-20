package com.dataloom.edm.internal;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.UUID;

import org.apache.olingo.commons.api.edm.FullQualifiedName;

import com.dataloom.data.SerializationConstants;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Optional;

/**
 * @author Matthew Tamayo-Rios &lt;matthew@kryptnostic.com&gt;
 */
public abstract class AbstractSecurableType extends AbstractSecurableObject {
    protected final FullQualifiedName type;

    protected AbstractSecurableType(
            UUID id,
            FullQualifiedName type,
            String title,
            Optional<String> description ) {
        this( Optional.of( id ), type, title, description );
    }

    protected AbstractSecurableType(
            Optional<UUID> id,
            FullQualifiedName type,
            String title,
            Optional<String> description ) {
        super( id, title, description );
        this.type = checkNotNull( type );
    }

    @JsonProperty( SerializationConstants.TYPE_FIELD )
    public FullQualifiedName getType() {
        return type;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ( ( type == null ) ? 0 : type.hashCode() );
        return result;
    }

    @Override
    public boolean equals( Object obj ) {
        if ( this == obj ) {
            return true;
        }
        if ( !super.equals( obj ) ) {
            return false;
        }
        if ( !( obj instanceof AbstractSecurableType ) ) {
            return false;
        }
        AbstractSecurableType other = (AbstractSecurableType) obj;
        if ( type == null ) {
            if ( other.type != null ) {
                return false;
            }
        } else if ( !type.equals( other.type ) ) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "AbstractSecurableType [type=" + type + ", id=" + id + ", title=" + title + ", description="
                + description + "]";
    }

}
