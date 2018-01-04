/*
 * Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.codenarc.rule.exceptions

import org.codenarc.rule.AbstractRuleTestCase
import org.codenarc.rule.Rule

/**
 * Tests for AvoidPrintStackTraceRule
 *
 * @author Janine Son
 */
class AvoidPrintStackTraceRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'AvoidPrintStackTrace'
    }

    void testSuccessScenario() {
        final SOURCE = '''
        	println("...")
        	printStackTrace(4)
        '''
        assertNoViolations(SOURCE)
    }

    void testViolation() {
        final SOURCE = '''
            this.printStackTrace()
            printStackTrace()
        '''
        assertTwoViolations SOURCE,
            2, "this.printStackTrace()", "Avoid printStackTrace. Use a logger instead.",
            3, "printStackTrace()", "Avoid printStackTrace. Use a logger instead."
    }


    protected Rule createRule() {
        new AvoidPrintStackTraceRule()
    }
}