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
package org.codenarc.rule.security

import org.codenarc.rule.AbstractRuleTestCase
import org.codenarc.rule.Rule

/**
 * Tests for NoHardcodeSMSRule
 *
 * @author Janine Son
 */
class NoHardcodeSMSRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'NoHardcodeSMS'
    }

    void testSuccessScenario() {
        final SOURCE = '''
            preferences {
                input("recipients", "contact", title: "Send notifications to") {
                        input "spam", "enum", title: "Send Me Notifications?", options: ["Yes", "No"]
                    }
            }
        	 sendNotificationToContacts(msg, recipients)
        '''
        assertNoViolations(SOURCE)
    }
    void test2SuccessScenario() {
        final SOURCE = '''
            preferences {
                section("Send Notifications?") {
                        input "recipients", "phone", title: "Warn with text message (optional)",
                            description: "Phone Number", required: false                
                }
            }
        	 sendNotificationToContacts(msg, recipients)
        '''
        assertNoViolations(SOURCE)
    }

    void testSingleViolation() {
        final SOURCE = '''
             sendNotificationToContacts(msg, "01012341234")
        '''
        assertSingleViolation(SOURCE, 2, 'sendNotificationToContacts(msg, "01012341234")',
            "Do not hard-code SMS messages.")
    }

//    void testAnotherViolation() {
//        final SOURCE = '''
//            preferences {
//               input "spam", "enum", title: "Send Me Notifications?", options: ["Yes", "No"]
//            }
//            sendNotificationToContacts(msg, spam)
//        '''
//        assertSingleViolation(SOURCE, 5, 'sendNotificationToContacts(msg, spam)',
//            "Do not hard-code SMS messages.")
//    }

    protected Rule createRule() {
        new NoHardcodeSMSRule()
    }
}