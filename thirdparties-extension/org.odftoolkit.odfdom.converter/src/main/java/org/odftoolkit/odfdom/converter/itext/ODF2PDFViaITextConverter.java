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
package org.odftoolkit.odfdom.converter.itext;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

import org.odftoolkit.odfdom.converter.IODFConverter;
import org.odftoolkit.odfdom.converter.ODFConverterException;
import org.odftoolkit.odfdom.converter.internal.AbstractODFConverter;
import org.odftoolkit.odfdom.converter.internal.itext.ElementVisitorForIText;
import org.odftoolkit.odfdom.converter.internal.itext.StyleEngineForIText;
import org.odftoolkit.odfdom.doc.OdfDocument;
import org.odftoolkit.odfdom.dom.OdfContentDom;
import org.odftoolkit.odfdom.dom.OdfStylesDom;
import org.odftoolkit.odfdom.incubator.doc.office.OdfOfficeMasterStyles;

public class ODF2PDFViaITextConverter
    extends AbstractODFConverter<PDFViaITextOptions>
{

    private static final IODFConverter<PDFViaITextOptions> INSTANCE = new ODF2PDFViaITextConverter();

    public static IODFConverter<PDFViaITextOptions> getInstance()
    {
        return INSTANCE;
    }

    @Override
    protected void doConvert( OdfDocument odfDocument, OutputStream out, Writer writer, PDFViaITextOptions options )
        throws ODFConverterException, IOException
    {
        StyleEngineForIText styleEngine = new StyleEngineForIText( odfDocument, options );
        try
        {
            OdfStylesDom stylesDom = odfDocument.getStylesDom();
            OdfContentDom contentDom = odfDocument.getContentDom();
            // 1.1) Parse styles.xml//office:document-styles/office:styles
            stylesDom.getOfficeStyles().accept( styleEngine );

            // 1.2) Parse
            // styles.xml//office:document-styles/office:automatic-styles
            stylesDom.getAutomaticStyles().accept( styleEngine );
            // 1.3) Parse
            // content.xml//office:document-content/office:automatic-styles
            contentDom.getAutomaticStyles().accept( styleEngine );

            ElementVisitorForIText visitorForIText =
                new ElementVisitorForIText( odfDocument, out, writer, styleEngine, options );

            // 2) Generate XHTML Page

            // 2.1) Parse
            // styles.xml//office:document-styles/office:master-styles
            OdfOfficeMasterStyles masterStyles = odfDocument.getOfficeMasterStyles();
            masterStyles.accept( visitorForIText );

            // 2) Compute meta
            // TODO
            odfDocument.getContentRoot().accept( visitorForIText );

            visitorForIText.save();

        }
        catch ( Exception e )
        {
            throw new ODFConverterException( e );
        }
    }

}
