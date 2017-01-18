package com.dataloom.authorization;

import org.junit.BeforeClass;

import com.dataloom.data.serializers.FullQualifedNameJacksonDeserializer;
import com.dataloom.data.serializers.FullQualifedNameJacksonSerializer;
import com.dataloom.mapstores.TestDataFactory;
import com.dataloom.serializer.BaseJacksonSerializationTest;

public class AclDataSerializerTest extends BaseJacksonSerializationTest<AclData> {

    @BeforeClass
    public static void configureSerializer() {
        FullQualifedNameJacksonSerializer.registerWithMapper( mapper );
        FullQualifedNameJacksonDeserializer.registerWithMapper( mapper );
        FullQualifedNameJacksonSerializer.registerWithMapper( smile );
        FullQualifedNameJacksonDeserializer.registerWithMapper( smile );
    }

    @Override
    protected AclData getSampleData() {
        return TestDataFactory.aclData();
    }

    @Override
    protected Class<AclData> getClazz() {
        return AclData.class;
    }
}