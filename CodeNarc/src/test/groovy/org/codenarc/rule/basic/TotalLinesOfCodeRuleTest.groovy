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
 * Tests for TotalLinesOfCodeRule
 *
 * @author Janine Son
 */
class TotalLinesOfCodeRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'TotalLinesOfCode'
    }

    void testSuccessScenario() {
        final SOURCE = ''' '''
        assertNoViolations(SOURCE)
    }


    void testSingleViolation() {
        final SOURCE = '''
            definition()
            /**
             *  Step Notifier
             *
             *  Copyright 2014 Jeff's Account
             *
             *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
             *  in compliance with the License. You may obtain a copy of the License at:
             *
             *      http://www.apache.org/licenses/LICENSE-2.0
             *
             *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed
             *  on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License
             *  for the specific language governing permissions and limitations under the License.
             *
             */
             //comment
        '''

        assertSingleViolation(SOURCE, 2, 'definition()', "File has 19 lines")
    }


    protected Rule createRule() {
        new TotalLinesOfCodeRule()
    }
}