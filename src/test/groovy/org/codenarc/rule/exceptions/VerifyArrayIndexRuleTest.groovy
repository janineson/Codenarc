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
 * Tests for VerifyArrayIndexRule
 *
 * @author Janine Son
 */
class VerifyArrayIndexRuleTest extends AbstractRuleTestCase {

    void testRuleProperties() {
        assert rule.priority == 2
        assert rule.name == 'VerifyArrayIndex'
    }

    void testSuccessScenario() {
        final SOURCE = '''
        	  def getSplitString(input) { 
        	    def splitted = input?.split(":") 
        	    if (splitted?.size() == 2) { 
        	        return splitted[1] 
        	    } else { 
        	        return null 
        	    }
        	  }
            getSplitString("abc:123") //ok
            getSplitString("abc:") //ok
        '''
        assertNoViolations(SOURCE)
    }

    void testSecondSuccessScenario() {
        final SOURCE = '''
        	  def getSplitString(input) { 
        	    if (input?.split(":").size() == 2) { 
        	        return input?.split(":")[1] 
        	    } else { 
        	        return null 
        	    }
        	  }
            getSplitString("abc:123") //ok
            getSplitString("abc:") //ok
        '''
        assertNoViolations(SOURCE)
    }

    void testViolation() {
        final SOURCE = '''
            def getSplitString(input) { 
                return input.split(":")[1]
            }
            // -> "123"
            getSplitString("abc:123") //ok
            // -> ArrayIndexOutOfBounds exception! 
            getSplitString("abc:") //BAD
        '''
        assertSingleViolation(SOURCE, 3, 'return input.split(":")[1]', "Verify array index.")
    }

    void testTwoViolations() {
        final SOURCE = '''
            // -> "123"
            "abc:123".split(":")[1] // result is ok but bad practice!
            // -> ArrayIndexOutOfBounds exception! 
            "abc:".split(":")[1] //BAD
        '''
        assertTwoViolations(SOURCE, 3, '"abc:123".split(":")[1]', "Verify array index.",
            5, '"abc:".split(":")[1]', "Verify array index.")
    }

    void testAnotherViolation() {
        final SOURCE = '''
        	  def getSplitString(input) { 
        	    if ("abc:".split(":").size() == 2) { 
        	        return input?.split(":")[1]
        	    } else { 
        	        return null 
        	    }
        	  }
        	  def getAnotherSplitString(input) { 
               if (input?.split(":").size() == 2) { 
                   return "abc:".split(":")[1]
               } else { 
                   return null 
               }
             }
        '''
        assertTwoViolations(SOURCE, 4, 'return input?.split(":")[1]', "Verify array index.",
            11, 'return "abc:".split(":")[1]', "Verify array index.")
    }

    protected Rule createRule() {
        new VerifyArrayIndexRule()
    }
}