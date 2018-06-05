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
package org.codenarc.rule.design

import org.codenarc.rule.AbstractRuleTestCase
import org.codenarc.rule.Rule

/**
 * Tests for AvoidChainedRunInCallRule
 *
 * @author Janine Son
 */
class AvoidChainedRunInCallRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'AvoidChainedRunInCall'
    }

    void testSuccessScenario() {
        final SOURCE = '''
        	 def initialize() { 
                runIn(60, handler) //ok
            }
            def handler() { 
                //some logic here
            }
        '''
        assertNoViolations(SOURCE)
    }
    void testSuccess2Scenario() {
        final SOURCE = '''
        	 def initialize() { 
                runEvery5Minutes(handler) //ok
            }
            def handler() { 
                //some logic here
            }
        '''
        assertNoViolations(SOURCE)
    }
    void testViolation() {
        final SOURCE = '''
            def initialize() { 
                runIn(60, handler) //ok
            }
            def handler() { 
                // do something here
                // schedule to run again in one minute - this is an antipattern! 
                runIn(60, handler)
            }
        '''
        assertSingleViolation(SOURCE, 8, 'runIn(60, handler)', "Avoid chained runIn() calls.")
    }

    protected Rule createRule() {
        new AvoidChainedRunInCallRule()
    }
}