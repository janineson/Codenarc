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
 * Tests for DocumentExternalHTTPRequestsRule
 *
 * @author Janine Son
 */
class DocumentExternalHTTPRequestsRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'DocumentExternalHTTPRequests'
    }

    void testSuccessScenario() {
        final SOURCE = '''
        	  def getFoo() {}
            def putFoo() {}
            def postFoo() {}
            def deleteFoo() {}
            def getBar() {}
        '''
        assertNoViolations(SOURCE)
    }

    void testSingleViolation() {
        final SOURCE = '''
            def params = [
                uri: "http://httpbin.org",
                path: "/get"
            ]
            
            try {
                httpGet(params) { resp ->
                    resp.headers.each {
                       log.debug "${it.name} : ${it.value}"
                    }
                    log.debug "response contentType: ${resp.contentType}"
                    log.debug "response data: ${resp.data}"
                }
            } catch (e) {
                log.error "something went wrong: $e"
            }
        '''
        assertSingleViolation(SOURCE, 8, 'httpGet(params) { resp ->', "Document external HTTP requests.")
    }


    protected Rule createRule() {
        new DocumentExternalHTTPRequestsRule()
    }
}