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
 * Tests for HandleNullValueRule
 *
 * @author Janine Son
 */
class HandleNullValueRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'HandleNullValue'
    }

    void testSuccessScenario() {
        final SOURCE = '''
        	  // if a location does not have any modes, statement simply returns null
            // but does not throw a NullPointerException
             if (location.modes?.find{
                  it.name == newMode }){
                    //do something
             }       
        '''
        assertNoViolations(SOURCE)
    }
    void test2SuccessScenario() {
        final SOURCE = '''
        	  def phrase = find('phrase', buttonNumber, value)
            if (phrase != null) location.helloHome.execute(phrase)
     
        '''
        assertNoViolations(SOURCE)
    }
    void testSecondSuccessScenario() {
        final SOURCE = '''
        	  // if the LAN event does not have headers, or a "content-type" header,
            // don't blow up with a NullPointerException!
            if (lanEvent.headers?."content-type"?.contains("xml")) {
                //do something 
             }      
        '''
        assertNoViolations(SOURCE)
    }

    void testSingleViolation() {
        final SOURCE = '''
            if (location.modes.find{
                  it.name == newMode }) {
                    //do something
             }    
        '''
        assertSingleViolation(SOURCE, 2, 'if (location.modes.find{',
            "Handle null values. Avoid NullPointerException by using the safe navigation (?) operator.")
    }

    void testSecondSingleViolation() {
        final SOURCE = '''
            if (lanEvent.headers."content-type".contains("xml")) {
                //do something 
             }     
        '''
        assertSingleViolation(SOURCE, 2, 'if (lanEvent.headers."content-type".contains("xml")) {',
            "Handle null values. Avoid NullPointerException by using the safe navigation (?) operator.")
    }


    protected Rule createRule() {
        new HandleNullValueRule()
    }
}