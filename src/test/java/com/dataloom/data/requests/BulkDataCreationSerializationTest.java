/*
 * Copyright (C) 2017. OpenLattice, Inc
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *
 * You can contact the owner of the copyright at support@openlattice.com
 *
 */

package com.dataloom.data.requests;

import com.dataloom.mapstores.TestDataFactory;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableSetMultimap;
import com.google.common.collect.SetMultimap;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.UUID;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.joda.time.DateTime;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Matthew Tamayo-Rios &lt;matthew@openlattice.com&gt;
 */
public class BulkDataCreationSerializationTest {

    @Test
    public void test() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream( baos );
        BulkDataCreation bdc = new BulkDataCreation( new HashSet<>(), new HashSet<>(), new HashSet<>() );

        bdc.getTickets().add( UUID.randomUUID() );

        bdc.getEntities().add( new Entity( TestDataFactory.entityKey(), getObjects() ) );
        oos.writeObject( bdc );
        oos.flush();

        byte[] bytes = baos.toByteArray();
        ByteArrayInputStream bais = new ByteArrayInputStream( bytes );
        ObjectInputStream ois = new ObjectInputStream( bais );
        BulkDataCreation actual = (BulkDataCreation) ois.readObject();
        Assert.assertEquals( bdc, actual );
    }

    public SetMultimap<UUID, Object> getObjects() {
        return HashMultimap.create( ImmutableSetMultimap.of(
                UUID.randomUUID(), RandomUtils.nextDouble(0.0, 1e6 ),
                UUID.randomUUID(), DateTime.now(),
                UUID.randomUUID(), RandomStringUtils.random( 10 ),
                UUID.randomUUID(), RandomUtils.nextInt( 0, 10000 ),
                UUID.randomUUID(), RandomUtils.nextLong( 0, 1 << 35 ) ) );
    }
}