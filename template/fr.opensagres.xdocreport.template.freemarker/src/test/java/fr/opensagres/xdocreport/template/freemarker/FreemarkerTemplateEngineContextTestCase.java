/**
 * Copyright (C) 2011 Angelo Zerr <angelo.zerr@gmail.com> and Pascal Leclercq <pascal.leclercq@gmail.com>
 *
 * All rights reserved.
 *
 * Permission is hereby granted, free  of charge, to any person obtaining
 * a  copy  of this  software  and  associated  documentation files  (the
 * "Software"), to  deal in  the Software without  restriction, including
 * without limitation  the rights to  use, copy, modify,  merge, publish,
 * distribute,  sublicense, and/or sell  copies of  the Software,  and to
 * permit persons to whom the Software  is furnished to do so, subject to
 * the following conditions:
 *
 * The  above  copyright  notice  and  this permission  notice  shall  be
 * included in all copies or substantial portions of the Software.
 *
 * THE  SOFTWARE IS  PROVIDED  "AS  IS", WITHOUT  WARRANTY  OF ANY  KIND,
 * EXPRESS OR  IMPLIED, INCLUDING  BUT NOT LIMITED  TO THE  WARRANTIES OF
 * MERCHANTABILITY,    FITNESS    FOR    A   PARTICULAR    PURPOSE    AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE
 * LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION
 * OF CONTRACT, TORT OR OTHERWISE,  ARISING FROM, OUT OF OR IN CONNECTION
 * WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package fr.opensagres.xdocreport.template.freemarker;

import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;
import fr.opensagres.xdocreport.template.IContext;
import fr.opensagres.xdocreport.template.ITemplateEngine;

/**
 * Freemarker context test case.
 */
public class FreemarkerTemplateEngineContextTestCase
    extends TestCase
{

    public void testSimpleContext()
        throws Exception
    {
        ITemplateEngine templateEngine = new FreemarkerTemplateEngine();

        Reader reader = new StringReader( "Project: ${projectName}." );
        Writer writer = new StringWriter();
        IContext context = templateEngine.createContext();
        context.put( "projectName", "XDocReport" );

        templateEngine.process( "", context, reader, writer );
        assertEquals( "Project: XDocReport.", writer.toString() );
    }

    public void testContextWithDot()
        throws Exception
    {
        ITemplateEngine templateEngine = new FreemarkerTemplateEngine();

        Reader reader = new StringReader( "Project: ${project.name}." );
        Writer writer = new StringWriter();
        IContext context = templateEngine.createContext();
        context.put( "project.name", "XDocReport" );

        templateEngine.process( "", context, reader, writer );
        assertEquals( "Project: XDocReport.", writer.toString() );
    }

    public void testTwoContextWithTwoDot()
        throws Exception
    {
        ITemplateEngine templateEngine = new FreemarkerTemplateEngine();

        Reader reader =
            new StringReader(
                              "Project: ${project.meta.name}. Users: [#list project.meta.users as user]${user} [/#list]" );
        Writer writer = new StringWriter();
        IContext context = templateEngine.createContext();
        context.put( "project.meta.name", "XDocReport" );
        List<String> users = new ArrayList<String>();
        users.add( "Angelo" );
        users.add( "Pascal" );
        context.put( "project.meta.users", users );

        templateEngine.process( "", context, reader, writer );
        assertEquals( "Project: XDocReport. Users: Angelo Pascal ", writer.toString() );
    }
}
