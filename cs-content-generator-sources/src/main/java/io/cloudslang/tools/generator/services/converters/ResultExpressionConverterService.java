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

package io.cloudslang.tools.generator.services.converters;

import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;

public class ResultExpressionConverterService {

    private static final String ARGUMENT = "%s";
    private static final String QUOTED_ARGUMENT = "\'%s\'";

    public static String getMatchingExpression(MatchType matchType) {
        switch (matchType) {

            //Compare arguments
            case COMPARE_NOT_EQUAL:
                return ARGUMENT + "!=" + QUOTED_ARGUMENT;
            case COMPARE_LESS:
                return ARGUMENT + "<" + QUOTED_ARGUMENT;
            case COMPARE_LESS_OR_EQUAL:
                return ARGUMENT + "<=" + QUOTED_ARGUMENT;
            case COMPARE_EQUAL:
                return ARGUMENT + "==" + QUOTED_ARGUMENT;
            case COMPARE_GREATER:
                return ARGUMENT + ">" + QUOTED_ARGUMENT;
            case COMPARE_GREATER_OR_EQUAL:
                return ARGUMENT + ">=" + QUOTED_ARGUMENT;

            //Containment arguments
            case CONTAINS:
                return ARGUMENT + " in " + ARGUMENT;
            case EXACT:
                return ARGUMENT + "==" + QUOTED_ARGUMENT;
            case NOT_EXACT:
                return ARGUMENT + "!=" + QUOTED_ARGUMENT;
            case BEGINS_WITH:
                return ARGUMENT + ".startswith(" + QUOTED_ARGUMENT + ")";
            case ENDS_WITH:
                return ARGUMENT + ".endswith(" + QUOTED_ARGUMENT + ")";
            case ALWAYS_MATCH:
                return "";
            default:
                throw new IllegalArgumentException("Invalid matchType: " + matchType);
        }
    }
}
