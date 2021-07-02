/*
 * See the NOTICE file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package org.xwiki.resource.temporary;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.xwiki.model.reference.EntityReference;
import org.xwiki.resource.ResourceType;
import org.xwiki.resource.entity.EntityResourceAction;
import org.xwiki.resource.entity.EntityResourceReference;

/**
 * Points to a temporary Resource that's been generated by some XWiki process (For example the Formula Macro generates
 * images of rendered formulas, the Chart Macro generates images of rendered chart data, etc).
 *
 * @version $Id$
 * @since 6.1M2
 */
public class TemporaryResourceReference extends EntityResourceReference
{
    /**
     * Represents a Temporary Resource Type.
     */
    public static final ResourceType TYPE = new ResourceType("tmp");

    private final List<String> resourcePath;

    private final String moduleId;

    /**
     * Create a new temporary resource reference.
     * 
     * @param moduleId see {@link #getModuleId()}
     * @param resourcePath see {@link #getResourcePath()}
     * @param owningEntityReference see {@link #getOwningEntityReference()}
     */
    public TemporaryResourceReference(String moduleId, List<String> resourcePath, EntityReference owningEntityReference)
    {
        super(owningEntityReference, EntityResourceAction.fromString(""));

        setType(TYPE);
        this.moduleId = moduleId;
        this.resourcePath = Collections.unmodifiableList(new ArrayList<>(resourcePath));
    }

    /**
     * @param moduleId see {@link #getModuleId()}
     * @param resourceName see {@link #getResourceName()}
     * @param owningEntityReference see {@link #getOwningEntityReference()}
     */
    public TemporaryResourceReference(String moduleId, String resourceName, EntityReference owningEntityReference)
    {
        this(moduleId, Collections.singletonList(resourceName), owningEntityReference);
    }

    /**
     * @param moduleId see {@link #getModuleId()}
     * @param resourceName see {@link #getResourceName()}
     */
    public TemporaryResourceReference(String moduleId, String resourceName)
    {
        this(moduleId, resourceName, null);
    }

    /**
     * @return the reference to the entity owning the current temporary resource. This can be used for example to verify
     *         that the user asking for the temporary resource has the permission to view the owning entity before
     *         letting him access the temporary resource.
     */
    public EntityReference getOwningEntityReference()
    {
        return getEntityReference();
    }

    /**
     * @return the name of the temporary resource (e.g. the temporary file name of a generated image)
     */
    public String getResourceName()
    {
        return this.resourcePath.get(this.resourcePath.size() - 1);
    }

    /**
     * @return the path to the temporary resource (within the namespace defined by the module id)
     */
    public List<String> getResourcePath()
    {
        return this.resourcePath;
    }

    /**
     * @return the module id, a free name (used as a namespace) allowing several components to generate temporary
     *         resources for the same entity
     */
    public String getModuleId()
    {
        return this.moduleId;
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder(super.hashCode(), 5).append(getModuleId()).append(getOwningEntityReference())
            .append(getResourcePath()).toHashCode();
    }

    @Override
    public boolean equals(Object object)
    {
        if (!super.equals(object)) {
            return false;
        }

        TemporaryResourceReference reference = (TemporaryResourceReference) object;
        return new EqualsBuilder().append(getModuleId(), reference.getModuleId())
            .append(getOwningEntityReference(), reference.getOwningEntityReference())
            .append(getResourcePath(), reference.getResourcePath()).isEquals();
    }
}
