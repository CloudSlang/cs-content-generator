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

import java.util.HashMap;
import java.util.Map;

@Data
public class CsJavaAction implements Mappable {

    private final String gav;
    private final String className;
    private final String methodName;

    public Map<String, Object> toMap() {
        final Map<String, Object> javaActionMap = new HashMap<>();
        javaActionMap.put("gav", gav);
        javaActionMap.put("class_name", className);
        javaActionMap.put("method_name", methodName);
        return javaActionMap;
    }
}
