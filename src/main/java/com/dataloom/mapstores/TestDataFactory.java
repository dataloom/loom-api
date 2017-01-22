package com.dataloom.mapstores;

import java.util.Arrays;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.olingo.commons.api.edm.EdmPrimitiveTypeKind;
import org.apache.olingo.commons.api.edm.FullQualifiedName;

import com.dataloom.authorization.Ace;
import com.dataloom.authorization.Acl;
import com.dataloom.authorization.AclData;
import com.dataloom.authorization.Action;
import com.dataloom.authorization.Permission;
import com.dataloom.authorization.Principal;
import com.dataloom.authorization.PrincipalType;
import com.dataloom.authorization.SecurableObjectType;
import com.dataloom.edm.internal.AbstractSecurableObject;
import com.dataloom.edm.internal.AbstractSecurableType;
import com.dataloom.edm.internal.EdmDetails;
import com.dataloom.edm.internal.EntitySet;
import com.dataloom.edm.internal.EntityType;
import com.dataloom.edm.internal.PropertyType;
import com.dataloom.organization.Organization;
import com.dataloom.requests.PermissionsRequestDetails;
import com.dataloom.requests.RequestStatus;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

public final class TestDataFactory {
    private static final SecurableObjectType[] securableObjectTypes = SecurableObjectType.values();
    private static final Permission[]          permissions          = Permission.values();
    private static final Action[]              actions              = Action.values();
    private static final Random                r                    = new Random();

    private TestDataFactory() {}

    public static Principal userPrincipal() {
        return new Principal( PrincipalType.USER, RandomStringUtils.randomAlphanumeric( 5 ) );
    }

    public static Principal rolePrincipal() {
        return new Principal( PrincipalType.ROLE, RandomStringUtils.randomAlphanumeric( 5 ) );
    }

    public static EntityType entityType( PropertyType... keys ) {
        Set<UUID> k = keys.length > 0
                ? Arrays.asList( keys ).stream().map( PropertyType::getId ).collect( Collectors.toSet() )
                : ImmutableSet.of( UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID() );
        return new EntityType(
                UUID.randomUUID(),
                fqn(),
                RandomStringUtils.randomAlphanumeric( 5 ),
                Optional.of( RandomStringUtils.randomAlphanumeric( 5 ) ),
                ImmutableSet.of( fqn(), fqn(), fqn() ),
                k,
                ImmutableSet.of( UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID() ) );
    }

    public static FullQualifiedName fqn() {
        return new FullQualifiedName(
                RandomStringUtils.randomAlphanumeric( 5 ),
                RandomStringUtils.randomAlphanumeric( 5 ) );
    }

    public static String name() {
        return RandomStringUtils.randomAlphanumeric( 5 );
    }

    public static EntitySet entitySet() {
        return new EntitySet(
                UUID.randomUUID(),
                UUID.randomUUID(),
                RandomStringUtils.randomAlphanumeric( 5 ),
                RandomStringUtils.randomAlphanumeric( 5 ),
                Optional.of( RandomStringUtils.randomAlphanumeric( 5 ) ) );
    }

    public static PropertyType propertyType() {
        return new PropertyType(
                UUID.randomUUID(),
                fqn(),
                RandomStringUtils.randomAlphanumeric( 5 ),
                Optional.of( RandomStringUtils.randomAlphanumeric( 5 ) ),
                ImmutableSet.of(),
                EdmPrimitiveTypeKind.String );
    }

    public static Organization organization() {
        return new Organization(
                Optional.of( UUID.randomUUID() ),
                RandomStringUtils.randomAlphanumeric( 5 ),
                Optional.of( RandomStringUtils.randomAlphanumeric( 5 ) ),
                Optional.of( ImmutableSet.of( UUID.randomUUID(), UUID.randomUUID(), UUID.randomUUID() ) ),
                ImmutableSet.of( RandomStringUtils.randomAlphanumeric( 5 ), RandomStringUtils.randomAlphanumeric( 5 ) ),
                ImmutableSet.of( userPrincipal() ),
                ImmutableSet.of( rolePrincipal() ) );
    }

    public static SecurableObjectType securableObjectType() {
        return securableObjectTypes[ r.nextInt( securableObjectTypes.length ) ];
    }

    public static Set<Permission> permissions() {
        return Arrays.asList( permissions )
                .stream()
                .filter( elem -> r.nextBoolean() )
                .collect( Collectors.toCollection( () -> EnumSet.noneOf( Permission.class ) ) );
    }

    public static Ace ace() {
        return new Ace( userPrincipal(), permissions() );
    }

    public static Acl acl() {
        return new Acl(
                ImmutableList.of( UUID.randomUUID(), UUID.randomUUID() ),
                ImmutableList.of( ace(), ace(), ace(), ace() ) );
    }

    public static AclData aclData() {
        return new AclData(
                acl(),
                actions[ r.nextInt( actions.length ) ] );
    }
    
    public static List<UUID> aclKey(){
        return ImmutableList.of( UUID.randomUUID(), UUID.randomUUID() );
    }

    public static EdmDetails edmDetails() {
        Set<PropertyType> pts = ImmutableSet.of( propertyType(), propertyType(), propertyType() );
        Set<EntityType> ets = ImmutableSet.of( entityType(), entityType(), entityType() );
        Set<EntitySet> ess = ImmutableSet.of( entitySet() );
        return new EdmDetails(
                pts.stream().collect( Collectors.toMap( AbstractSecurableType::getId, v -> v ) ),
                ets.stream().collect( Collectors.toMap( AbstractSecurableType::getId, v -> v ) ),
                ess.stream().collect( Collectors.toMap( AbstractSecurableObject::getId, v -> v ) ) );
    }

    public static Map<UUID, EnumSet<Permission>> aclChildPermissions(){
        Map<UUID, EnumSet<Permission>> permissions = new HashMap<>();
        permissions.put( UUID.randomUUID(), EnumSet.of( Permission.READ ));
        permissions.put( UUID.randomUUID(), EnumSet.of( Permission.WRITE ));
        permissions.put( UUID.randomUUID(), EnumSet.of( Permission.READ, Permission.WRITE ));
        return permissions;
    }
    
    public static PermissionsRequestDetails unresolvedPRDetails(){
        return new PermissionsRequestDetails( aclChildPermissions(), RequestStatus.SUBMITTED );
    }

    public static PermissionsRequestDetails resolvedPRDetails(){
        return new PermissionsRequestDetails( aclChildPermissions(), RequestStatus.APPROVED );
    }
}
