package com.dataloom.edm;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import com.dataloom.data.requests.FileType;
import com.dataloom.edm.requests.EdmDetailsSelector;
import com.dataloom.edm.requests.EdmRequest;
import com.dataloom.edm.requests.MetadataUpdate;
import com.dataloom.edm.set.EntitySetPropertyMetadata;
import com.dataloom.edm.type.AssociationDetails;
import com.dataloom.edm.type.AssociationType;
import com.dataloom.edm.type.ComplexType;
import com.dataloom.edm.type.EntityType;
import com.dataloom.edm.type.EnumType;
import com.dataloom.edm.type.PropertyType;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * @author Matthew Tamayo-Rios &lt;matthew@kryptnostic.com&gt;
 */
public interface EdmApi {
    /*
     * These determine the service routing for the LB
     */
    String SERVICE    = "/datastore";
    String CONTROLLER = "/edm";
    String BASE       = SERVICE + CONTROLLER;

    /*
     * These are the actual components after {SERVICE}/{CONTROLLER}/
     */
    String ID                  = "id";
    String ENTITY_TYPE_ID      = "entityTypeId";
    String ASSOCIATION_TYPE_ID = "associationTypeId";
    String PROPERTY_TYPE_ID    = "propertyTypeId";
    String NAME                = "name";
    String NAMESPACE           = "namespace";
    String NAMESPACES          = "namespaces";
    String ENTITY_SETS         = "entitySets";
    String ENTITY_TYPES        = "entityTypes";
    String ASSOCIATION_TYPES   = "associationTypes";
    String PROPERTY_TYPES      = "propertyTypes";
    String SCHEMA              = "schema";
    String SCHEMAS             = "schemas";
    String FILE_TYPE           = "fileType";
    String TOKEN               = "token";
    String VERSION             = "version";

    // {namespace}/{schema_name}/{class}/{FQN}/{FQN}
    /*
     * /entity/type/{namespace}/{name} /entity/set/{namespace}/{name} /schema/{namespace}/{name}
     * /property/{namespace}/{name}
     */

    String IDS_PATH              = "/ids";
    String SCHEMA_PATH           = "/schema";
    String ENUM_TYPE_PATH        = "/enum/type";
    String ENTITY_SETS_PATH      = "/entity/set";
    String ENTITY_TYPE_PATH      = "/entity/type";
    String PROPERTY_TYPE_PATH    = "/property/type";
    String COMPLEX_TYPE_PATH     = "/complex/type";
    String ASSOCIATION_TYPE_PATH = "/association/type";
    String HIERARCHY_PATH        = "/hierarchy";
    String DETAILED_PATH         = "/detailed";
    String AVAILABLE_PATH        = "/available";
    String SRC_PATH              = "/src";
    String DST_PATH              = "/dst";
    String DIFF_PATH             = "/diff";
    String VERSION_PATH          = "/version";
    String NEW_PATH              = "/new";
    String CLEAR_PATH            = "/clear";
    String FORCE_PATH            = "/force";
    String KEY_PATH              = "/key";

    String NAMESPACE_PATH           = "/{" + NAMESPACE + "}";
    String NAME_PATH                = "/{" + NAME + "}";
    String ID_PATH                  = "/{" + ID + "}";
    String ENTITY_TYPE_ID_PATH      = "/{" + ENTITY_TYPE_ID + "}";
    String ASSOCIATION_TYPE_ID_PATH = "/{" + ASSOCIATION_TYPE_ID + "}";
    String PROPERTY_TYPE_ID_PATH    = "/{" + PROPERTY_TYPE_ID + "}";

    String SCHEMA_BASE_PATH           = BASE + SCHEMA_PATH;
    String ENTITY_SETS_BASE_PATH      = BASE + ENTITY_SETS_PATH;
    String ENTITY_TYPE_BASE_PATH      = BASE + ENTITY_TYPE_PATH;
    String PROPERTY_TYPE_BASE_PATH    = BASE + PROPERTY_TYPE_PATH;
    String ASSOCIATION_TYPE_BASE_PATH = BASE + ASSOCIATION_TYPE_PATH;

    @DELETE( BASE + CLEAR_PATH )
    void clearAllData();

    /**
     * Gets the entity data model, including namespaces, schemas, entity types, association types, and property types.
     *
     * @return EntityDataModel - The entire entity data model, including namespaces, schemas, entity types, association
     * types, and property types.
     */
    @GET( BASE )
    EntityDataModel getEntityDataModel();

    /**
     * Updates the entity data model, including schemas, entity types, association types, and property types.
     *
     * @param edm - The relevant elements of the entity data model to create or update, including schemas, entity types,
     *            association types, and property types
     */
    @PATCH( BASE )
    void updateEntityDataModel( EntityDataModel edm );

    /**
     * Gets the changes between the existing entity data model and the entity data model passed in, including schemas,
     * association types, entity types, and property types.
     *
     * @param edm - The entire entity data model, including schemas, entity types, association types, and property types
     */
    @POST( BASE + DIFF_PATH )
    EntityDataModelDiff getEntityDataModelDiff( EntityDataModel edm );

    /**
     * Returns the current entity data model version
     */
    @GET( BASE + VERSION_PATH )
    UUID getEntityDataModelVersion();

    /**
     * Generates and returns a new entity data model version
     */
    @GET( BASE + VERSION_PATH + NEW_PATH )
    UUID generateNewEntityDataModelVersion();

    /**
     * Gets information for any SecurableObjectType given its type and ID.
     *
     * @param selectors A set containing a given SecurableObjectType, ID, and a set of fields (SecurableObjectType) to
     *                  include in the response.
     * @return EdmDetails - The SecurableObjectType details requested.
     */
    @POST( BASE )
    EdmDetails getEdmDetails( @Body Set<EdmDetailsSelector> selectors );

    /**
     * Gets all property type details from the data model.
     *
     * @return An iterable containing all property types.
     */
    @GET( PROPERTY_TYPE_BASE_PATH )
    Iterable<PropertyType> getPropertyTypes();

    /**
     * Gets property type details for a property with a given ID.
     *
     * @param propertyTypeId ID for a given property type.
     * @return A property type with a given UUID.
     */
    @GET( PROPERTY_TYPE_BASE_PATH + ID_PATH )
    PropertyType getPropertyType( @Path( ID ) UUID propertyTypeId );

    /**
     * Gets property types that have the given namespace.
     *
     * @param namespace Name of the namespace.
     * @return An iterable containing property types in the given namespace.
     */
    @GET( PROPERTY_TYPE_BASE_PATH + "/" + NAMESPACE + NAMESPACE_PATH )
    Iterable<PropertyType> getPropertyTypesInNamespace( @Path( NAMESPACE ) String namespace );

    /**
     * Creates a property type if it doesn't exist. If property type already exists, then no action is taken.
     *
     * @param propertyType The property to create.
     * @return ID for the property created.
     */
    @POST( PROPERTY_TYPE_BASE_PATH )
    UUID createPropertyType( @Body PropertyType propertyType );

    /**
     * Deletes a property with a given ID. If the property is associated with an entity set, then no action is taken.
     *
     * @param propertyTypeId The property ID for the property to delete.
     */
    @DELETE( PROPERTY_TYPE_BASE_PATH + ID_PATH )
    Void deletePropertyType( @Path( ID ) UUID propertyTypeId );

    /**
     * Deletes a property with a given IDl, regardless of whether or not it is associated with an entity set.
     *
     * @param propertyTypeId The property ID for the property to delete.
     */
    @DELETE( PROPERTY_TYPE_BASE_PATH + ID_PATH + FORCE_PATH )
    Void forceDeletePropertyType( @Path( ID ) UUID propertyTypeId );

    @GET( BASE + ENUM_TYPE_PATH )
    Iterable<EnumType> getEnumTypes();

    @POST( BASE + ENUM_TYPE_PATH )
    UUID createEnumType( @Body EnumType enumType );

    @GET( BASE + ENUM_TYPE_PATH + ID_PATH )
    EnumType getEnumType( @Path( ID ) UUID enumTypeId );

    @DELETE( BASE + ENUM_TYPE_PATH + ID_PATH )
    Void deleteEnumType( @Path( ID ) UUID enumTypeId );

    @GET( BASE + COMPLEX_TYPE_PATH )
    Iterable<ComplexType> getComplexTypes();

    @POST( BASE + COMPLEX_TYPE_PATH )
    UUID createComplexType( @Body ComplexType complexType );

    @GET( BASE + COMPLEX_TYPE_PATH + ID_PATH )
    ComplexType getComplexType( @Path( ID ) UUID complexTypeId );

    @GET( BASE + COMPLEX_TYPE_PATH + ID_PATH + HIERARCHY_PATH )
    Set<ComplexType> getComplexTypeHierarchy( @Path( ID ) UUID complexTypeId );

    @DELETE( BASE + COMPLEX_TYPE_PATH + ID_PATH )
    Void deleteComplexType( @Path( ID ) UUID complexTypeId );

    /**
     * Get all entity types.
     *
     * @return Iterable containing all entity types.
     */
    @GET( ENTITY_TYPE_BASE_PATH )
    Iterable<EntityType> getEntityTypes();

    /**
     * Get all association entity types.
     *
     * @return Iterable containing all association entity types.
     */
    @GET( ASSOCIATION_TYPE_BASE_PATH + ENTITY_TYPE_PATH )
    Iterable<EntityType> getAssociationEntityTypes();

    /**
     * Creates an entity type if it doesn't already exist.
     *
     * @param entityType The entity type to create.
     * @return ID for the entity type created.
     */
    @POST( ENTITY_TYPE_BASE_PATH )
    UUID createEntityType( @Body EntityType entityType );

    /**
     * Gets entity type details for a entity type with a given ID.
     *
     * @param entityTypeId ID for a given entity type.
     * @return An entity type with a given UUID.
     */
    @GET( ENTITY_TYPE_BASE_PATH + ID_PATH )
    EntityType getEntityType( @Path( ID ) UUID entityTypeId );

    /**
     * Gets entity type hierarchy. Returns a set of Entity types and its base types.
     *
     * @param entityTypeId ID for a given entity type.
     * @return A set of entity types and their corresponding base types.
     */
    @GET( ENTITY_TYPE_BASE_PATH + ID_PATH + HIERARCHY_PATH )
    Set<EntityType> getEntityTypeHierarchy( @Path( ID ) UUID entityTypeId );

    /**
     * Deletes an entity type with a given ID.
     *
     * @param entityTypeId The entity type ID to delete.
     */
    @DELETE( ENTITY_TYPE_BASE_PATH + ID_PATH )
    Void deleteEntityType( @Path( ID ) UUID entityTypeId );

    /**
     * Adds a property type with a given ID to an entity type with a given ID.
     *
     * @param entityTypeId   The ID for the entity type that will have a property added to it.
     * @param propertyTypeId The ID for the property type that will be added to the entity type.
     */
    @PUT( ENTITY_TYPE_BASE_PATH + ENTITY_TYPE_ID_PATH + PROPERTY_TYPE_ID_PATH )
    Void addPropertyTypeToEntityType(
            @Path( ENTITY_TYPE_ID ) UUID entityTypeId,
            @Path( PROPERTY_TYPE_ID ) UUID propertyTypeId );

    /**
     * Removes a property type with a given ID from an entity type with a given ID.
     *
     * @param entityTypeId   The ID for the entity type that will have a property removed from it.
     * @param propertyTypeId The ID for the property type that will be removed from the entity type.
     */
    @DELETE( ENTITY_TYPE_BASE_PATH + ENTITY_TYPE_ID_PATH + PROPERTY_TYPE_ID_PATH )
    Void removePropertyTypeFromEntityType(
            @Path( ENTITY_TYPE_ID ) UUID entityTypeId,
            @Path( PROPERTY_TYPE_ID ) UUID propertyTypeId );

    /**
     * Adds a primary key with a given ID to an entity type with a given ID.
     *
     * @param entityTypeId   The ID for the entity type that will have a property added to it.
     * @param propertyTypeId The ID for the primary key that will be added to the entity type.
     */
    @PUT( ENTITY_TYPE_BASE_PATH + KEY_PATH + ENTITY_TYPE_ID_PATH + PROPERTY_TYPE_ID_PATH )
    Void addPrimaryKeyToEntityType(
            @Path( ENTITY_TYPE_ID ) UUID entityTypeId,
            @Path( PROPERTY_TYPE_ID ) UUID propertyTypeId );

    /**
     * Removes a primary key with a given ID from an entity type with a given ID.
     *
     * @param entityTypeId   The ID for the entity type that will have a property removed from it.
     * @param propertyTypeId The ID for the primary key that will be removed from the entity type.
     */
    @DELETE( ENTITY_TYPE_BASE_PATH + KEY_PATH + ENTITY_TYPE_ID_PATH + PROPERTY_TYPE_ID_PATH )
    Void removePrimaryKeyFromEntityType(
            @Path( ENTITY_TYPE_ID ) UUID entityTypeId,
            @Path( PROPERTY_TYPE_ID ) UUID propertyTypeId );

    /**
     * Removes a property type with a given ID from an entity type with a given ID regardless of whether there is data
     * associated with the property type.
     *
     * @param entityTypeId   The ID for the entity type that will have a property removed from it.
     * @param propertyTypeId The ID for the property type that will be removed from the entity type.
     */
    @DELETE( ENTITY_TYPE_BASE_PATH + ENTITY_TYPE_ID_PATH + PROPERTY_TYPE_ID_PATH + FORCE_PATH )
    Void forceRemovePropertyTypeFromEntityType(
            @Path( ENTITY_TYPE_ID ) UUID entityTypeId,
            @Path( PROPERTY_TYPE_ID ) UUID propertyTypeId );

    /**
     * Reorders the specified entity type's properties;
     *
     * @param entityTypeId    ID for entity type.
     * @param propertyTypeIds The new ordering of the entity type's properties.
     */
    @PATCH( ENTITY_TYPE_BASE_PATH + ENTITY_TYPE_ID_PATH + PROPERTY_TYPE_PATH )
    Void reorderPropertyTypesInEntityType( @Path( ID ) UUID entityTypeId, @Body LinkedHashSet<UUID> propertyTypeIds );

    /**
     * Gets all entity sets available to the calling user.
     *
     * @return Iterable containing entity sets available to the calling user.
     */
    @GET( ENTITY_SETS_BASE_PATH )
    Iterable<EntitySet> getEntitySets();

    /**
     * Creates multiple entity sets if they do not exist.
     *
     * @param entitySets The entity sets to create.
     * @return The entity sets created with UUIDs.
     */
    @POST( ENTITY_SETS_BASE_PATH )
    Map<String, UUID> createEntitySets( @Body Set<EntitySet> entitySets );

    /**
     * Get entity set ID, entity type ID, name, title, description, and contacts list for a given entity set.
     *
     * @param entitySetId The ID for the entity set.
     */
    @GET( ENTITY_SETS_BASE_PATH + ID_PATH )
    EntitySet getEntitySet( @Path( ID ) UUID entitySetId );

    @DELETE( ENTITY_SETS_BASE_PATH + ID_PATH )
    Void deleteEntitySet( @Path( ID ) UUID entitySetId );

    /**
     * Creates an empty schema, if it doesn't exist. If schema exists then no action is taken.
     *
     * @param namespace The namespace for the schema.
     * @param name      The name for the schema.
     */
    @PUT( SCHEMA_BASE_PATH + NAMESPACE_PATH + NAME_PATH )
    Void createEmptySchema( @Path( NAMESPACE ) String namespace, @Path( NAME ) String name );

    @POST( SCHEMA_BASE_PATH )
    Void createSchemaIfNotExists( @Body Schema schema );

    /**
     * Gets all schemas.
     *
     * @return An iterable containing all the schemas available to the calling user.
     */
    @GET( SCHEMA_BASE_PATH )
    Iterable<Schema> getSchemas();

    /**
     * Gets all schemas associated with a given namespace and accessible by the caller.
     *
     * @param namespace The namespace for which to retrieve all accessible schemas.
     * @return All accessible schemas in the provided namespace.
     */
    @GET( SCHEMA_BASE_PATH + NAMESPACE_PATH )
    Iterable<Schema> getSchemasInNamespace( String namespace );

    /**
     * Gets the schema contents for a corresponding namespace and name
     *
     * @param namespace The namespace for a schema.
     * @param name      The name for a schema.
     * @return All schemas identified by namespace and name, across all accessible Acls.
     */
    @GET( SCHEMA_BASE_PATH + NAMESPACE_PATH + NAME_PATH )
    Schema getSchemaContents(
            @Path( NAMESPACE ) String namespace,
            @Path( NAME ) String name );

    /**
     * Gets the schema contents for a corresponding namespace and name
     *
     * @param namespace The namespace for a schema.
     * @param name      The name for a schema.
     * @param fileType
     * @param token
     * @return All schemas identified by namespace and name, across all accessible Acls.
     */
    @GET( SCHEMA_BASE_PATH + NAMESPACE_PATH + NAME_PATH )
    Schema getSchemaContentsFormatted(
            @Path( NAMESPACE ) String namespace,
            @Path( NAME ) String name,
            @Query( FILE_TYPE ) FileType fileType,
            @Query( TOKEN ) String token );

    /**
     * Edits the schema contents for a corresponding namespace and name
     *
     * @param namespace The namespace for a schema.
     * @param name      The name for a schema.
     * @param request
     */
    @PATCH( SCHEMA_BASE_PATH + NAMESPACE_PATH + NAME_PATH )
    Void updateSchema(
            @Path( NAMESPACE ) String namespace,
            @Path( NAME ) String name,
            @Body EdmRequest request );

    /**
     * Get ID for entity set with given name.
     *
     * @param entitySetName The name of the entity set.
     * @return ID for entity set.
     */
    @GET( BASE + IDS_PATH + ENTITY_SETS_PATH + NAME_PATH )
    UUID getEntitySetId( @Path( NAME ) String entitySetName );

    /**
     * Get ID for property type with given namespace and name.
     *
     * @param namespace The namespace for a property.
     * @param name      The name for a property.
     * @return ID for property type.
     */
    @GET( BASE + IDS_PATH + PROPERTY_TYPE_PATH + NAMESPACE_PATH + NAME_PATH )
    UUID getPropertyTypeId( @Path( NAMESPACE ) String namespace, @Path( NAME ) String name );

    /**
     * Get ID for entity type with given namespace and name.
     *
     * @param namespace The namespace for a property.
     * @param name      The name for a property.
     * @return ID for property type.
     */
    @GET( BASE + IDS_PATH + ENTITY_TYPE_PATH + NAMESPACE_PATH + NAME_PATH )
    UUID getEntityTypeId( @Path( NAMESPACE ) String namespace, @Path( NAME ) String name );

    /**
     * Edit property type metadata for a given property type.
     *
     * @param propertyTypeId ID for property type.
     * @param update         Only title, description, and type fields are accepted. Other fields are ignored.
     */
    @PATCH( PROPERTY_TYPE_BASE_PATH + ID_PATH )
    Void updatePropertyTypeMetadata( @Path( ID ) UUID propertyTypeId, @Body MetadataUpdate update );

    /**
     * Edit entity type metadata for a given entity type.
     *
     * @param entityTypeId ID for entity type.
     * @param update       Only title, description, and type fields are accepted. Other fields are ignored.
     */
    @PATCH( ENTITY_TYPE_BASE_PATH + ID_PATH )
    Void updateEntityTypeMetadata( @Path( ID ) UUID entityTypeId, @Body MetadataUpdate update );

    /**
     * Edit entity set metadata for a given entity set.
     *
     * @param entitySetId ID for entity set.
     * @param update      Only title, description, contacts and name fields are accepted. Other fields are ignored.
     */
    @PATCH( ENTITY_SETS_BASE_PATH + ID_PATH )
    Void updateEntitySetMetadata( @Path( ID ) UUID entitySetId, @Body MetadataUpdate update );

    /**
     * Get all association entity types.
     *
     * @return Iterable containing all association types.
     */
    @GET( ASSOCIATION_TYPE_BASE_PATH )
    Iterable<AssociationType> getAssociationTypes();

    /**
     * Create association type if it doesn't exist.
     *
     * @param associationType
     * @return ID for the association type.
     */
    @POST( ASSOCIATION_TYPE_BASE_PATH )
    UUID createAssociationType( @Body AssociationType associationType );

    /**
     * Delete association type with a given ID.
     *
     * @param associationTypeId
     */
    @DELETE( ASSOCIATION_TYPE_BASE_PATH + ID_PATH )
    Void deleteAssociationType( @Path( ID ) UUID associationTypeId );

    /**
     * Adds an entity type with a given ID to the src field of an association type with a given ID.
     *
     * @param associationTypeId The ID for the association type that will have an entity type id added to its src types.
     * @param entityTypeId      The ID for the entity type that will be added to the association type's src field.
     */
    @PUT( ASSOCIATION_TYPE_BASE_PATH + ASSOCIATION_TYPE_ID_PATH + SRC_PATH + ENTITY_TYPE_ID_PATH )
    Void addSrcEntityTypeToAssociationType(
            @Path( ASSOCIATION_TYPE_ID ) UUID associationTypeId,
            @Path( ENTITY_TYPE_ID ) UUID entityTypeId );

    /**
     * Adds an entity type with a given ID to the dst field of an association type with a given ID.
     *
     * @param associationTypeId The ID for the association type that will have an entity type id added to its dst types.
     * @param entityTypeId      The ID for the entity type that will be added to the association type's dst field.
     */
    @PUT( ASSOCIATION_TYPE_BASE_PATH + ASSOCIATION_TYPE_ID_PATH + DST_PATH + ENTITY_TYPE_ID_PATH )
    Void addDstEntityTypeToAssociationType(
            @Path( ASSOCIATION_TYPE_ID ) UUID associationTypeId,
            @Path( ENTITY_TYPE_ID ) UUID entityTypeId );

    /**
     * Removes an entity type with a given ID from the src field of an association type with a given ID.
     *
     * @param associationTypeId The ID for the association type that will have an entity type id removed from its src
     *                          types.
     * @param entityTypeId      The ID for the entity type that will be removed from the association type's src field.
     */
    @DELETE( ASSOCIATION_TYPE_BASE_PATH + ASSOCIATION_TYPE_ID_PATH + SRC_PATH + ENTITY_TYPE_ID_PATH )
    Void removeSrcEntityTypeFromAssociationType(
            @Path( ASSOCIATION_TYPE_ID ) UUID associationTypeId,
            @Path( ENTITY_TYPE_ID ) UUID entityTypeId );

    /**
     * Removes an entity type with a given ID from the dst field of an association type with a given ID.
     *
     * @param associationTypeId The ID for the association type that will have an entity type id removed from its dst
     *                          types.
     * @param entityTypeId      The ID for the entity type that will be removed from the association type's dst field.
     */
    @DELETE( ASSOCIATION_TYPE_BASE_PATH + ASSOCIATION_TYPE_ID_PATH + DST_PATH + ENTITY_TYPE_ID_PATH )
    Void removeDstEntityTypeFromAssociationType(
            @Path( ASSOCIATION_TYPE_ID ) UUID associationTypeId,
            @Path( ENTITY_TYPE_ID ) UUID entityTypeId );

    /**
     * Get association type by given ID.
     *
     * @param associationTypeId
     * @return AssociationType
     */
    @GET( ASSOCIATION_TYPE_BASE_PATH + ID_PATH )
    AssociationType getAssociationTypeById( @Path( ID ) UUID associationTypeId );

    /**
     * Returns the src and dst entity types for a given association type ID, as well as whether it is bidirectional.
     *
     * @param associationTypeId
     * @return AssociationDetails
     */
    @GET( ASSOCIATION_TYPE_BASE_PATH + ID_PATH + DETAILED_PATH )
    AssociationDetails getAssociationDetailsForType( @Path( ID ) UUID associationTypeId );

    /**
     * Get all available association entity types for a particular entity type.
     *
     * @return Iterable containing all association entity types that includes the specified entity type id in its src or
     * dst types.
     */
    @GET( ASSOCIATION_TYPE_BASE_PATH + ID_PATH + AVAILABLE_PATH )
    Iterable<EntityType> getAvailableAssociationTypesForEntityType( @Path( ID ) UUID entityTypeId );

    @GET( ENTITY_SETS_PATH + ID_PATH + PROPERTY_TYPE_PATH )
    Map<UUID, EntitySetPropertyMetadata> getAllEntitySetPropertyMetadata( @Path( ID ) UUID entitySetId );

    @GET( ENTITY_SETS_PATH + ID_PATH + PROPERTY_TYPE_PATH + PROPERTY_TYPE_ID_PATH )
    EntitySetPropertyMetadata getEntitySetPropertyMetadata(
            @Path( ID ) UUID entitySetId,
            @Path( PROPERTY_TYPE_ID ) UUID propertyTypeId );

    @POST( ENTITY_SETS_PATH + ID_PATH + PROPERTY_TYPE_PATH + PROPERTY_TYPE_ID_PATH )
    Void updateEntitySetPropertyMetadata(
            @Path( ID ) UUID entitySetId,
            @Path( PROPERTY_TYPE_ID ) UUID propertyTypeId,
            @Body MetadataUpdate update );

}
