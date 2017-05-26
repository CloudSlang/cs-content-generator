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
