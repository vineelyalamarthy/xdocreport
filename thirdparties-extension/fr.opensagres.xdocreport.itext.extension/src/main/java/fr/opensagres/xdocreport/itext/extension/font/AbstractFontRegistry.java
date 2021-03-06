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
package fr.opensagres.xdocreport.itext.extension.font;

import java.awt.Color;

import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;

public abstract class AbstractFontRegistry
{

    private boolean systemEncodingDeterminated;

    private String systemEncoding;

    private static boolean registerFontDirectories = false;

    /*
     * public Font getFont(String familyName, float size, int style, Color color) { return getFont(familyName,
     * FontFactory.defaultEncoding, size, style, color); }
     */

    public Font getFont( String familyName, String encoding, float size, int style, Color color )
    {
        registerFontDirectoriesIfNeeded();
        if ( familyName != null )
        {
            familyName = resolveFamilyName( familyName, style );
        }

        return FontFactory.getFont( familyName, encoding, size, style, color );
    }

    /**
     * Register fonts from files (ex : for windows, load files from C:\WINDOWS\Fonts).
     */
    private void registerFontDirectoriesIfNeeded()
    {
        if ( !registerFontDirectories )
        {
            FontFactory.registerDirectories();
            registerFontDirectories = true;
        }
    }

    /**
     * checks if this font is Bold.
     * 
     * @return a <CODE>boolean</CODE>
     */
    public boolean isBold( int style )
    {
        if ( style == Font.UNDEFINED )
        {
            return false;
        }
        return ( style & Font.BOLD ) == Font.BOLD;
    }

    /**
     * checks if this font is Bold.
     * 
     * @return a <CODE>boolean</CODE>
     */
    public boolean isItalic( int style )
    {
        if ( style == Font.UNDEFINED )
        {
            return false;
        }
        return ( style & Font.ITALIC ) == Font.ITALIC;
    }

    /**
     * checks if this font is underlined.
     * 
     * @return a <CODE>boolean</CODE>
     */
    public boolean isUnderlined( int style )
    {
        if ( style == Font.UNDEFINED )
        {
            return false;
        }
        return ( style & Font.UNDERLINE ) == Font.UNDERLINE;
    }

    /**
     * checks if the style of this font is STRIKETHRU.
     * 
     * @return a <CODE>boolean</CODE>
     */
    public boolean isStrikethru( int style )
    {
        if ( style == Font.UNDEFINED )
        {
            return false;
        }
        return ( style & Font.STRIKETHRU ) == Font.STRIKETHRU;
    }

    public String getSystemEncoding()
    {
        if ( systemEncodingDeterminated )
        {
            return systemEncoding;
        }
        // don't rely on file.encoding property because
        // it may be changed if application is launched inside an ide
        systemEncoding = System.getProperty( "sun.jnu.encoding" );
        if ( systemEncoding != null && systemEncoding.length() > 0 )
        {
            systemEncodingDeterminated = true;
            return systemEncoding;
        }
        systemEncoding = System.getProperty( "ibm.system.encoding" );
        if ( systemEncoding != null && systemEncoding.length() > 0 )
        {
            systemEncodingDeterminated = true;
            return systemEncoding;
        }
        systemEncoding = FontFactory.defaultEncoding;
        systemEncodingDeterminated = true;
        return systemEncoding;
    }

    protected abstract String resolveFamilyName( String familyName, int style );
}
