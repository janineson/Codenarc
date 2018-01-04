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
 * Tests for SeparateParentChildAppRule
 *
 * @author Janine Son
 */
class SeparateParentChildAppRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'SeparateParentChildApp'
    }

    void testSuccessScenario() {
        final SOURCE = '''
        	  definition(
                name: "Lock It When I Leave"
            )
            preferences {
                page(name: "mainPage", title: "Child Apps", install: true, uninstall: true) {
                    section {
                        app(name: "childApps", appName: "Child App", namespace: "mynamespace", title: "New Child App", multiple: true)
                    }
                }
            }
        '''
        assertNoViolations(SOURCE)
    }
    void testAnotherSuccessScenario() {
        final SOURCE = '''
        	  definition(
                name: "Lock It When I Leave",
                parent: "yourNameSpace:Parent App Name"
            )
            preferences {
                section("When I leave...") {
                    input "presence1", "capability.presenceSensor", title: "Who?", multiple: true
                 }
            }
        '''
        assertNoViolations(SOURCE)
    }
    void testSingleViolation() {
        final SOURCE = '''
            definition(
                name: "Lock It When I Leave",
                parent: "yourNameSpace:Parent App Name"
            )
            preferences {
                page(name: "mainPage", title: "Child Apps", install: true, uninstall: true) {
                    section {
                        app(name: "childApps", appName: "Child App", namespace: "mynamespace", title: "New Child App", multiple: true)
                    }
                }
            }
        '''
        assertSingleViolation(SOURCE, 9, 'app(name: "childApps", appName: "Child App", namespace: "mynamespace", title: "New Child App", multiple: true)',
            "Parent and child SmartApp should exist in separate files.")
    }


    protected Rule createRule() {
        new SeparateParentChildAppRule()
    }
}