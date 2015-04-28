/*
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS HEADER.
 *
 * Copyright (c) 2012-2015 Oracle and/or its affiliates. All rights reserved.
 *
 * The contents of this file are subject to the terms of either the GNU
 * General Public License Version 2 only ("GPL") or the Common Development
 * and Distribution License("CDDL") (collectively, the "License").  You
 * may not use this file except in compliance with the License.  You can
 * obtain a copy of the License at
 * http://glassfish.java.net/public/CDDL+GPL_1_1.html
 * or packager/legal/LICENSE.txt.  See the License for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing the software, include this License Header Notice in each
 * file and include the License file at packager/legal/LICENSE.txt.
 *
 * GPL Classpath Exception:
 * Oracle designates this particular file as subject to the "Classpath"
 * exception as provided by Oracle in the GPL Version 2 section of the License
 * file that accompanied this code.
 *
 * Modifications:
 * If applicable, add the following below the License Header, with the fields
 * enclosed by brackets [] replaced by your own identifying information:
 * "Portions Copyright [year] [name of copyright owner]"
 *
 * Contributor(s):
 * If you wish your version of this file to be governed by only the CDDL or
 * only the GPL Version 2, indicate your decision by adding "[Contributor]
 * elects to include this software in this distribution under the [CDDL or GPL
 * Version 2] license."  If you don't indicate a single choice of license, a
 * recipient has the option to distribute your version of this file under
 * either the CDDL, the GPL Version 2 or to extend the choice of license to
 * its licensees as provided above.  However, if you add GPL Version 2 code
 * and therefore, elected the GPL Version 2 license, then the option applies
 * only if the new code is made subject to such option by the copyright
 * holder.
 */
package me.bayes.vertx.vest.response;

import javax.ws.rs.core.*;

/**
 * Base outbound message context implementation.
 *
 * @author Marek Potociar (marek.potociar at oracle.com)
 */
public class OutboundMessageContext {

    private final MultivaluedMap<String, Object> headers;
    private Object entity;

    /**
     * Create new outbound message context.
     */
    public OutboundMessageContext() {
        this.headers = new MultivaluedHashMap<>();
    }

    public OutboundMessageContext(OutboundMessageContext context) {
        this.headers = context.headers;
        this.entity = context.entity;
    }

    /**
     * Replace all headers.
     *
     * @param headers new headers.
     */
    public void replaceHeaders(MultivaluedMap<String, Object> headers) {
        getHeaders().clear();
        if (headers != null) {
            getHeaders().putAll(headers);
        }
    }

    /**
     * Get a multi-valued map representing outbound message headers with their values converted
     * to strings.
     *
     * @return multi-valued map of outbound message header names to their string-converted values.
     */
    public MultivaluedMap<String, String> getStringHeaders() {
        MultivaluedMap<String, String> multivaluedMap = new MultivaluedHashMap<>();
        headers.forEach( (key, values) -> {
            multivaluedMap.add(key, headers.getFirst(key).toString());
        });

        return multivaluedMap;
    }

    /**
     * Get a message header as a single string value.
     * <p>
     * Each single header value is converted to String using a
     * {@link javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate} if one is available
     * via {@link javax.ws.rs.ext.RuntimeDelegate#createHeaderDelegate(java.lang.Class)}
     * for the header value class or using its {@code toString} method  if a header
     * delegate is not available.
     *
     * @param name the message header.
     * @return the message header value. If the message header is not present then
     * {@code null} is returned. If the message header is present but has no
     * value then the empty string is returned. If the message header is present
     * more than once then the values of joined together and separated by a ','
     * character.
     */
    public String getHeaderString(String name) {
        Object first = headers.getFirst(name);
        return first != null ? first.toString() : null;
    }

    /**
     * Get the mutable message headers multivalued map.
     *
     * @return mutable multivalued map of message headers.
     */
    public MultivaluedMap<String, Object> getHeaders() {
        return headers;
    }

    // Message entity

    /**
     * Check if there is an entity available in the message.
     * <p>
     * The method returns {@code true} if the entity is present, returns
     * {@code false} otherwise.
     *
     * @return {@code true} if there is an entity present in the message,
     * {@code false} otherwise.
     */
    public boolean hasEntity() {
        return entity != null;
    }

    /**
     * Get the message entity Java instance.
     * <p>
     * Returns {@code null} if the message does not contain an entity.
     *
     * @return the message entity or {@code null} if message does not contain an
     * entity body.
     */
    public Object getEntity() {
        return entity;
    }

    /**
     * Set a new message message entity.
     *
     * @param entity entity object.
     * @see javax.ws.rs.ext.MessageBodyWriter
     */
    public void setEntity(Object entity) {
        this.entity = entity;
    }

    /**
     * Set the message content media type.
     *
     * @param mediaType message content media type.
     */
    public void setMediaType(MediaType mediaType) {
        this.headers.putSingle(HttpHeaders.CONTENT_TYPE, mediaType);
    }

    /**
     * Get the raw message entity type information.
     *
     * @return raw message entity type information.
     */
    public Class<?> getEntityClass() {
        return entity == null ? null : entity.getClass();
    }

}