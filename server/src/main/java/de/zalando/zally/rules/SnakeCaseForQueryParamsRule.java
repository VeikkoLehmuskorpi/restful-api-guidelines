package de.zalando.zally.rules;

import java.util.ArrayList;
import java.util.List;

import de.zalando.zally.Violation;
import de.zalando.zally.ViolationType;
import de.zalando.zally.utils.PatternUtil;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.parameters.QueryParameter;
import org.springframework.stereotype.Component;

@Component
class SnakeCaseForQueryParamsRule implements Rule {

    @Override
    public List<Violation> validate(Swagger swagger) {
        if (swagger == null || swagger.getPaths() == null) {
            return new ArrayList<>();
        }

        List<Violation> violations = new ArrayList<>();
        swagger.getPaths().forEach((path, pathObject) -> {
            pathObject.getOperationMap().forEach((httpMethod, operation) -> {
                operation.getParameters().forEach(param -> {
                    if (param instanceof QueryParameter) {
                        if (!PatternUtil.isSnakeCase(param.getName())) {
                            violations.add(getViolation(param));
                        }
                    }
                });
            });
        });
        return violations;
    }

    private Violation getViolation(Parameter parameter) {
        return new Violation(
                "Use snake_case (never camelCase) for Query Parameters",
                String.format("Query parameter '%s' does not use snake_case.", parameter.getName()),
                ViolationType.MUST,
                "http://zalando.github.io/restful-api-guidelines/naming/Naming.html#must-use-snakecase-never-camelcase-for-query-parameters"
        );
    }
}
