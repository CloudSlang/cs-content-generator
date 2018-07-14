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

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

@Data
public class CsResponse implements Mappable {

    private String name;
    private String description;
    private String rule;

    public CsResponse(String name, String description, String rule) {
        this.name = name;
        this.description = description;
        this.rule = StringUtils.defaultIfEmpty(rule, "");
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> csResponseMap = new HashMap<>();
        csResponseMap.put("name", name);
        csResponseMap.put("description", description);
        csResponseMap.put("hasValue", StringUtils.isNoneEmpty(rule) && !name.equalsIgnoreCase("FAILURE")); //at the moment FAILURE should not have an evaluator
        csResponseMap.put("value", rule);
        return csResponseMap;
    }

}
