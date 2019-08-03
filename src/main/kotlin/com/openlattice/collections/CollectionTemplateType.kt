package com.openlattice.collections


import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.openlattice.client.serialization.SerializationConstants

import java.util.Optional
import java.util.UUID

data class CollectionTemplateType(
        val id: UUID,
        val name: String,
        val title: String,
        val description: String,
        val entityTypeId: UUID
) {
    @JsonCreator
    constructor(
        @JsonProperty(SerializationConstants.ID_FIELD) id: Optional<UUID>,
        @JsonProperty(SerializationConstants.NAME_FIELD) name: String,
        @JsonProperty(SerializationConstants.TITLE_FIELD) title: String,
        @JsonProperty(SerializationConstants.DESCRIPTION_FIELD) description: Optional<String>,
        @JsonProperty(SerializationConstants.ENTITY_TYPE_ID) entityTypeId: UUID
    ): this(id.orElse(UUID.randomUUID()), name, title, description.orElse(""), entityTypeId)

    constructor(
            id: UUID,
            name: String,
            title: String,
            description: Optional<String>,
            entityTypeId: UUID) : this(Optional.of<UUID>(id), name, title, description, entityTypeId)
}
