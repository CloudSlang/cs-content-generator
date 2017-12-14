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

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

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
        csOperationFileMap.put("documentation", description);
        csOperationFileMap.put("operation", operation.toMap());
        return csOperationFileMap;
    }

    public String getNamespace() {
        return namespace;
    }
    public CsOperation getOperation() {
        return operation;
    }

}
