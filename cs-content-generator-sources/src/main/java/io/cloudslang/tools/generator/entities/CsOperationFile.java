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
public class CsOperationFile implements Mappable{
    
    private String namespace;
    private CsOperation operation;
    private String description;

    public CsOperationFile(String namespace, CsOperation operation, String description) {
        this.namespace = namespace;
        this.operation = operation;
        this.description = StringUtils.defaultIfEmpty(description, "");
    }

    public Map<String, Object> toMap() {
        final Map<String, Object> csOperationFileMap = new HashMap<>(3);
        csOperationFileMap.put("namespace", namespace);
        csOperationFileMap.put("operation", operation.toMap());
        csOperationFileMap.put("documentation", description);
        return csOperationFileMap;
    }


}
