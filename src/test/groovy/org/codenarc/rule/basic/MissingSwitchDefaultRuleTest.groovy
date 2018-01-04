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
package org.codenarc.rule.basic

import org.codenarc.rule.AbstractRuleTestCase
import org.codenarc.rule.Rule

/**
 * Tests for MissingSwitchDefaultRule
 *
 * @author Janine Son
 */
class MissingSwitchDefaultRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'MissingSwitchDefault'
    }

    void testSuccessScenario() {
        final SOURCE = '''
        	switch(mystring) {
                case "cmd1":
                    object.cmd1()
                    break
                case "cmd2":
                    object.cmd2()
                    break
                case "cmd3":
                    object.cmd3()
                    break
                default:
                    return "ERROR"
            }
        '''
        assertNoViolations(SOURCE)
    }

    void testSingleViolation() {
        final SOURCE = '''
            switch(mystring) {
                case "cmd1":
                    object.cmd1()
                    break
                case "cmd2":
                    object.cmd2()
                    break
                case "cmd3":
                    object.cmd3()
                    break
            }
        '''
        assertSingleViolation(SOURCE, 2, 'switch(mystring) {',
            "Missing default: case statement in switch().")
    }

    protected Rule createRule() {
        new MissingSwitchDefaultRule()
    }
}