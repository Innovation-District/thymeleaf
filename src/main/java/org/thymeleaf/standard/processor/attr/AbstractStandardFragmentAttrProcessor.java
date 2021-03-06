/*
 * =============================================================================
 * 
 *   Copyright (c) 2011-2012, The THYMELEAF team (http://www.thymeleaf.org)
 * 
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 * 
 * =============================================================================
 */
package org.thymeleaf.standard.processor.attr;

import org.thymeleaf.Arguments;
import org.thymeleaf.dom.Element;
import org.thymeleaf.exceptions.TemplateProcessingException;
import org.thymeleaf.processor.IAttributeNameProcessorMatcher;
import org.thymeleaf.processor.attr.AbstractFragmentAttrProcessor;
import org.thymeleaf.standard.expression.FragmentSelection;
import org.thymeleaf.standard.expression.StandardExpressionProcessor;

/**
 * 
 * @author Daniel Fern&aacute;ndez
 * 
 * @since 1.0
 * @deprecated replaced by {@link AbstractStandardFragmentHandlingAttrProcessor}. Will be
 *             removed in 2.1.x.
 *
 */
@Deprecated
@SuppressWarnings("deprecation")
public abstract class AbstractStandardFragmentAttrProcessor 
        extends AbstractFragmentAttrProcessor {

    
    
    

    
    protected AbstractStandardFragmentAttrProcessor(final IAttributeNameProcessorMatcher matcher) {
        super(matcher);
    }

    protected AbstractStandardFragmentAttrProcessor(final String attributeName) {
        super(attributeName);
    }

    




    @Override
    protected final AbstractFragmentSpec getFragmentSpec(
            final Arguments arguments,
            final Element element, final String attributeName, final String attributeValue) {

        
        final FragmentSelection fragmentSelection =
            StandardExpressionProcessor.parseFragmentSelection(arguments, attributeValue);
        
        final Object templateNameObject = 
            StandardExpressionProcessor.executeExpression(arguments, fragmentSelection.getTemplateName());
        if (templateNameObject == null) {
            throw new TemplateProcessingException(
                    "Evaluation of template name from expression \"" + attributeValue + "\" " + 
                    "returned null.");
        }
        
        final String templateName = templateNameObject.toString();
        
        
        if (fragmentSelection.hasFragmentSelector()) {

            final Object fragmentSelectorObject = 
                StandardExpressionProcessor.executeExpression(arguments, fragmentSelection.getFragmentSelector());
            if (fragmentSelectorObject == null) {
                throw new TemplateProcessingException(
                        "Evaluation of fragment selector from expression \"" + attributeValue + "\" " + 
                        "returned null.");
            }

            final String fragmentSelector = fragmentSelectorObject.toString();
            
            if (fragmentSelection.isXPath()) {
                
                return new DOMSelectorFragmentSpec(
                        templateName,
                        fragmentSelector);
                
            }
            
            final String fragmentAttributeName = 
                getFragmentAttributeName(arguments, element, attributeName, attributeValue, fragmentSelection);
            
            return new NamedFragmentSpec(
                    templateName,
                    fragmentAttributeName,
                    fragmentSelector);
            
        }
        
        return new CompleteTemplateFragmentSpec(templateName);
        
    }

    
    
    protected abstract String getFragmentAttributeName(
            final Arguments arguments,
            final Element element, final String attributeName, final String attributeValue, 
            final FragmentSelection fragmentSelection);

    
}
