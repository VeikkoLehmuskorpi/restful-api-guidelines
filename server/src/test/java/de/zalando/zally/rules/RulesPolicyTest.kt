package de.zalando.zally.rules

import de.zalando.zally.Violation
import de.zalando.zally.ViolationType
import io.swagger.models.Swagger
import org.junit.Assert.assertTrue
import org.junit.Assert.assertFalse
import org.junit.Test

class RulesPolicyTest {
    class TestRule(val result: Violation?) : AbstractRule() {
        override val title = "Test Rule"
        override val url = null
        override val violationType = ViolationType.MUST
        override val code = "M999"
        override fun validate(swagger: Swagger): Violation? = result
    }

    @Test
    fun shouldAcceptRuleIfNotFiltered() {
        val policy = RulesPolicy(arrayOf("M001", "M002"))
        val violation = Violation(TestRule(null), "dummy1", "dummy", ViolationType.MUST, "dummy", listOf("x"))
        assertTrue(policy.accepts(TestRule(violation)))
    }

    @Test
    fun shouldNotAcceptRuleIfFiltered() {
        val policy = RulesPolicy(arrayOf("M001", "M999"))
        val violation = Violation(TestRule(null), "dummy1", "dummy", ViolationType.MUST, "dummy", listOf("x"))
        assertFalse(policy.accepts(TestRule(violation)))
    }
}