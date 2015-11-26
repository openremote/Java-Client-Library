/*
 * OpenRemote, the Home of the Digital Home.
 * Copyright 2008-2014, OpenRemote Inc.
 *
 * See the contributors.txt file in the distribution for a
 * full listing of individual contributors.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.openremote.entities.util;

import java.io.InputStream;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Simple utility class for converting to/from JSON using Jackson
 * 
 * @author <a href="mailto:richard@openremote.org">Richard Turner</a>
 * 
 */
public class JacksonProcessor {
  private static final ObjectMapper mapper = new ObjectMapper();

  static {
    // Jackson 2.X configuration settings
    mapper.configure(DeserializationFeature.UNWRAP_ROOT_VALUE, false);
    mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);
    mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    mapper.configure(DeserializationFeature.USE_JAVA_ARRAY_FOR_JSON_ARRAY, true);
    mapper.configure(DeserializationFeature.READ_ENUMS_USING_TO_STRING, true);

    mapper.setVisibilityChecker(mapper.getSerializationConfig().getDefaultVisibilityChecker()
            .withFieldVisibility(Visibility.ANY).withGetterVisibility(Visibility.NONE)
            .withSetterVisibility(Visibility.NONE).withCreatorVisibility(Visibility.NONE));
  }

  private JacksonProcessor() {
  }

  public static <T> T unMarshall(String data, Class<T> type) throws Exception {
    return mapper.readValue(data, type);
  }

  public static <T> T unMarshall(InputStream is, Class<T> type) throws Exception {
    return mapper.readValue(is, type);
  }
}
