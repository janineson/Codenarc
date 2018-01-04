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
 * Tests for NoBusyLoopRule
 *
 * @author Janine Son
 */
class NoBusyLoopRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'NoBusyLoop'
    }

    void testSuccessScenario() {
        final SOURCE = '''
        	def someEventHandler(evt) {
                // execute handler in five minutes from now
                runIn(60*5, handler)
            }
            
            def handler() {
                theswitch.off()
            }
        '''
        assertNoViolations(SOURCE)
    }
    void testAnotherSuccessScenario() {
        final SOURCE = '''
        	   def mywait(ms) {
                def start = now()
                while (now() < start + ms) {
                    handler()
                }
            }
        '''
        assertNoViolations(SOURCE)
    }

    void testSingleViolation() {
        final SOURCE = '''
            def mywait(ms) {
                def start = now()
                while (now() < start + ms) {
                    // do nothing, just wait
                }
            }
        '''
        assertSingleViolation(SOURCE, 4, 'while (now() < start + ms) {', "Do not use busy loops. Use schedule instead.")
    }
    void testAnotherViolation() {
        final SOURCE = '''
            while (now() < (now() + 3000)) {
                // do nothing, just wait
            }
        '''
        assertSingleViolation(SOURCE, 2, 'while (now() < (now() + 3000)) {', "Do not use busy loops. Use schedule instead.")
    }
    void testThirdViolation() {
        final SOURCE = '''
            def mywait(ms) {
                def start = now()
                while (start < start + ms) {
                    // do nothing, just wait
                }
            }
        '''
        assertSingleViolation(SOURCE, 4, 'while (start < start + ms) {', "Do not use busy loops. Use schedule instead.")
    }
    protected Rule createRule() {
        new NoBusyLoopRule()
    }
}