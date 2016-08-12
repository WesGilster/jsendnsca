/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.googlecode.jsendnsca;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.Validate;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.UnknownHostException;

import static org.apache.commons.lang3.builder.ToStringStyle.SHORT_PREFIX_STYLE;

/**
 * The Passive Check Message Payload
 *
 * @author Raj.Patel
 * @version 1.0
 * @see com.googlecode.jsendnsca.builders.MessagePayloadBuilder
 */
public class MessagePayload implements Serializable {

    private static final long serialVersionUID = 6014395299584333124L;

    private static final String DEFAULT_SERVICENAME = "UNDEFINED";
    public static final String UNKNOWN = "UNKNOWN";

    private String hostname = UNKNOWN;
    private Level level = Level.UNKNOWN;
    private String serviceName = DEFAULT_SERVICENAME;
    private String message = StringUtils.EMPTY;

    /**
     * Construct a new {@link MessagePayload} with hostname being the short
     * hostname of this machine, level unknown, service name "undefined" and
     * empty message
     */
    public MessagePayload() {
        this(true);
    }

    /**
     * Construct a new {@link MessagePayload} with level unknown, service name "undefined" and
     * empty message
     *
     * @param useLocalHostname - set hostname to be the local hostname of this machine, otherwise
     *                         will be set to "UNKNOWN";
     */
    public MessagePayload(boolean useLocalHostname) {
        if (useLocalHostname) {
            useLocalHostname();
        }
    }

    /**
     * Construct a new {@link MessagePayload}
     *
     * @param hostname    the hostname to be sent in this passive check
     * @param level       the level
     * @param serviceName the service name
     * @param message     the message
     */
    public MessagePayload(String hostname, Level level, String serviceName, String message) {
        Validate.notEmpty(hostname, "hostname cannot be null or an empty String");
        Validate.notEmpty(serviceName, "serviceName cannot be null or an empty String");

        this.hostname = hostname;
        this.level = level;
        this.serviceName = serviceName;
        this.message = message;
    }

    /**
     * The hostname to be sent in this passive check
     *
     * @return the hostname, defaults to "localhost"
     */
    public String getHostname() {
        return hostname;
    }

    /**
     * Use the short hostname of this machine in the passive check
     */
    public void useLocalHostname() {
        setHostname(false);
    }

    /**
     * Set the hostname in the passive check
     *
     * @param useCanonical true to use this machines fully qualified domain name, false
     *                     to use the short hostname
     */
    public void setHostname(boolean useCanonical) {
        InetAddress ipAddress;
        try {
            ipAddress = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            throw new UnknownHostRuntimeException(e);
        }
        if (useCanonical) {
            this.hostname = ipAddress.getCanonicalHostName();
        } else {
            this.hostname = ipAddress.getHostName();
        }
    }

    /**
     * Set the hostname in the passive check
     *
     * @param hostname the hostname to use
     */
    public void setHostname(String hostname) {
        Validate.notEmpty(hostname, "hostname cannot be null or an empty String");
        this.hostname = hostname;
    }

    /**
     * Get the level of the Passive check
     *
     * @return the level
     */
    public Level getLevel() {
        return level;
    }

    /**
     * Set the level of the Passive check using a {@link String} The case of the
     * {@link String} is ignored
     *
     * @param level either "ok", "warning", "critical" or "unknown"
     */
    public void setLevel(String level) {
        this.level = Level.tolevel(level);
    }

    /**
     * Set the level of the Passive check
     *
     * @param level the level
     */
    public void setLevel(Level level) {
        this.level = level;

    }

    /**
     * The service name of this passive check
     *
     * @return the service name, default is "UNDEFINED"
     */
    public String getServiceName() {
        return serviceName;
    }

    /**
     * Set the service name of this passive check
     *
     * @param serviceName the service name
     */
    public void setServiceName(String serviceName) {
        Validate.notEmpty(serviceName, "serviceName cannot be null or an empty String");
        this.serviceName = serviceName;
    }

    /**
     * The message to send in this passive check
     *
     * @return the message, default is an empty string
     */
    public String getMessage() {
        return message;
    }

    /**
     * Set the message to send in this passive check
     *
     * @param message the message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder(21, 57)
                .append(hostname)
                .append(level)
                .append(serviceName)
                .append(message)
                .toHashCode();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof MessagePayload)) {
            return false;
        }
        if (this == obj) {
            return true;
        }
        MessagePayload other = (MessagePayload) obj;

        return new EqualsBuilder()
                .append(hostname, other.hostname)
                .append(level, other.level)
                .append(serviceName, other.serviceName)
                .append(message, other.message)
                .isEquals();
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return new ToStringBuilder(this, SHORT_PREFIX_STYLE)
                .append("level", level)
                .append("hostname", hostname)
                .append("serviceName", serviceName)
                .append("message", message)
                .toString();
    }

    public static class UnknownHostRuntimeException extends RuntimeException {

        private static final long serialVersionUID = 6164363358198216472L;

        public UnknownHostRuntimeException(UnknownHostException e) {
            super(e);
        }
    }
}
