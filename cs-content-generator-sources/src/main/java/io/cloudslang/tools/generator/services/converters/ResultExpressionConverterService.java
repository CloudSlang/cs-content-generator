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
