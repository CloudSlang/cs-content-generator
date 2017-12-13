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

package io.cloudslang.tools.generator.entities;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CsJavaAction implements Mappable {

    private final String gav;
    private final String methodName;
    private final String className;

    public CsJavaAction(String gav, String methodName, String className) {
        this.gav = gav;
        this.className = methodName;
        this.methodName = className;
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> javaActionMap = new HashMap<>();
        javaActionMap.put("gav", gav);
        javaActionMap.put("class_name", className);
        javaActionMap.put("method_name", methodName);
        return javaActionMap;
    }
}
