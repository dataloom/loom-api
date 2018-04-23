/*
 * Copyright (C) 2018. OpenLattice, Inc.
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
 *
 */

package com.openlattice.data;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

import com.google.common.collect.SetMultimap;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * @author Matthew Tamayo-Rios &lt;matthew@openlattice.com&gt;
 */
public class DataAssociation {
    private final UUID              srcEntitySetId;
    private final Optional<Integer> srcEntityIndex;
    private final Optional<UUID>    srcEntityKeyId;

    private final UUID              dstEntitySetId;
    private final Optional<Integer> dstEntityIndex;
    private final Optional<UUID>    dstEntityKeyId;

    private final SetMultimap<UUID, Object> data;

    public DataAssociation(
            UUID srcEntitySetId,
            Optional<Integer> srcEntityIndex,
            Optional<UUID> srcEntityKeyId,
            UUID dstEntitySetId,
            Optional<Integer> dstEntityIndex,
            Optional<UUID> dstEntityKeyId,
            SetMultimap<UUID, Object> data ) {
        checkArgument( srcEntityIndex.isPresent() ^ srcEntityKeyId.isPresent(),
                "Only one of index or entity key id must be present for source" );
        checkArgument( dstEntityIndex.isPresent() ^ dstEntityKeyId.isPresent(),
                "Only one of index or entity key id must be present for destination" );
        this.srcEntitySetId = srcEntitySetId;
        this.srcEntityIndex = srcEntityIndex;
        this.srcEntityKeyId = srcEntityKeyId;
        this.dstEntitySetId = dstEntitySetId;
        this.dstEntityIndex = dstEntityIndex;
        this.dstEntityKeyId = dstEntityKeyId;
        this.data = data;
    }

    @Override
    public String toString() {
        return "DataAssociation{" +
                "srcEntitySetId=" + srcEntitySetId +
                ", srcEntityIndex=" + srcEntityIndex +
                ", srcEntityKeyId=" + srcEntityKeyId +
                ", dstEntitySetId=" + dstEntitySetId +
                ", dstEntityIndex=" + dstEntityIndex +
                ", dstEntityKeyId=" + dstEntityKeyId +
                ", data=" + data +
                '}';
    }

    public UUID getSrcEntitySetId() {
        return srcEntitySetId;
    }

    public Optional<Integer> getSrcEntityIndex() {
        return srcEntityIndex;
    }

    public UUID getDstEntitySetId() {
        return dstEntitySetId;
    }

    public Optional<Integer> getDstEntityIndex() {
        return dstEntityIndex;
    }

    public Optional<UUID> getSrcEntityKeyId() {
        return srcEntityKeyId;
    }

    public Optional<UUID> getDstEntityKeyId() {
        return dstEntityKeyId;
    }

    public SetMultimap<UUID, Object> getData() {
        return data;
    }

    @Override public boolean equals( Object o ) {
        if ( this == o ) { return true; }
        if ( !( o instanceof DataAssociation ) ) { return false; }
        DataAssociation that = (DataAssociation) o;
        return Objects.equals( srcEntitySetId, that.srcEntitySetId ) &&
                Objects.equals( srcEntityIndex, that.srcEntityIndex ) &&
                Objects.equals( srcEntityKeyId, that.srcEntityKeyId ) &&
                Objects.equals( dstEntitySetId, that.dstEntitySetId ) &&
                Objects.equals( dstEntityIndex, that.dstEntityIndex ) &&
                Objects.equals( dstEntityKeyId, that.dstEntityKeyId ) &&
                Objects.equals( data, that.data );
    }

    @Override public int hashCode() {

        return Objects.hash( srcEntitySetId,
                srcEntityIndex,
                srcEntityKeyId,
                dstEntitySetId,
                dstEntityIndex,
                dstEntityKeyId,
                data );
    }
}