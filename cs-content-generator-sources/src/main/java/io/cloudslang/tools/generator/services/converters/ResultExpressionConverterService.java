package io.cloudslang.tools.generator.services.converters;

import com.hp.oo.sdk.content.plugin.ActionMetadata.MatchType;
import org.springframework.stereotype.Service;

/**
 * Author: Ligia Centea
 * Date: 4/26/2016.
 */
@Service
public class ResultExpressionConverterService {

    private static final String ARGUMENT = "%s";
    private static final String QUOTED_ARGUMENT = "\'%s\'";

    public String getMatchingExpression(MatchType matchType) {
        switch (matchType) {

            //compare arguments
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

            //containment arguments
            case CONTAINS:
                return ARGUMENT + " in " + ARGUMENT;
            case EXACT:
                return ARGUMENT + "==" + QUOTED_ARGUMENT;
            case NOT_EXACT:
                return ARGUMENT + "!=" + QUOTED_ARGUMENT;
            //TODO -> missing items here
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
