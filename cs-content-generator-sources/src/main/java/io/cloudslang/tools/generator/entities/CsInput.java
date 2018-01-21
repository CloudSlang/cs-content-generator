/*
 * (c) Copyright 2017 Micro Focus, L.P.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License v2.0 which accompany this distribution.
 *
 * The Apache License is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.cloudslang.tools.generator.entities;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class CsInput implements Mappable {

    private final String name;
    private final String description;
    private final boolean required;
    private final String defaultValue;
    private final boolean privateField;
    private final boolean sensitive;

    public CsInput(String name, String description, boolean required, String defaultValue, boolean sensitive, boolean overridable) {
        this.name = name;
        this.description = description;
        this.required = required;
        this.defaultValue = StringUtils.defaultIfEmpty(defaultValue, "");
        this.sensitive = sensitive;
        this.privateField = overridable;
    }

    public Map<String, Object> toMap() {
        boolean hasDefault = StringUtils.isNotEmpty(defaultValue);
        final Map<String, Object> csInputMap = new HashMap<>();
        csInputMap.put("name", name);
        csInputMap.put("description", description);
        csInputMap.put("default", defaultValue);
        csInputMap.put("hasAny", hasDefault || !required || sensitive);
        csInputMap.put("hasDefault", hasDefault);
        csInputMap.put("isRequired", required);
        csInputMap.put("isPrivate", privateField);
        csInputMap.put("isSensitive", sensitive);
        return csInputMap;

    }

    public String getDescription() { return description; }
    public String getName() {
        return name;
    }
    public boolean isRequired() {
        return required;
    }
    public boolean isPrivateField() {
        return privateField;
    }


}
