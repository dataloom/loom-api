/*
 * Copyright (C) 2019. OpenLattice, Inc.
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

package com.openlattice.auditing

import com.openlattice.authorization.AclKey
import com.openlattice.data.EntityDataKey
import java.time.OffsetDateTime
import java.util.*

/**
 * Represents an auditable event in the system. Will be mostly generated by backend, but can be submitted from the
 * front end.
 *
 * @param principal The user or securable principal that took the action.
 * @param aclKeys The acl keys of the securable objects involved.
 * @param entities The entities involvedin this auditable event.
 * @param eventType The [AuditEventType] of this event.
 * @param description A description of the event.
 * @param data Represents additional data relevant to the event. Maybe properly indexed by ES in the future.
 * @param timestamp Represents the instant in time at which an auditable event occured. The current time is assumed if
 * @param operationId Sometimes complex operations will interact with multiple entity sets. This field allow makes it
 * possible to tie those operations together.
 * none is provided.
 */
data class AuditableEvent(
        val principal: UUID,
        val aclKeys:AclKey,
        val eventType: AuditEventType,
        val description: String,
        val entities: Optional<Set<UUID>>,
        val data: Map<String, Any>,
        val timestamp: OffsetDateTime = OffsetDateTime.now(),
        val operationId: Optional<Int> = Optional.empty()
)