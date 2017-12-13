/*
 * Copyright © 2017 EntIT Software LLC, a Micro Focus company (L.P.)
 *
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
package io.cloudslang.tools.generator.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Optional;

/**
 * Created by moldovso on 6/9/2017.
 */
@Slf4j
public class YamlUtils {
    public static Optional<Object> simpleYPath(final Object object, final String yPath, final String delimiter) {
        if (StringUtils.isNotEmpty(yPath)) {
            if (object instanceof Map) {
                final Map map = (Map) object;
                final String whatWeSearchFor = StringUtils.substringBefore(yPath, delimiter);

                final Object something = map.get(whatWeSearchFor);

                if (something != null) {
                    return simpleYPath(something, StringUtils.substringAfter(yPath, delimiter), delimiter);
                }
            }
        } else {
            return Optional.of(object);
        }
        return Optional.empty();
    }
}
