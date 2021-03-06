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
package org.apache.poi.xwpf.converter.internal.itext;

import static org.apache.poi.xwpf.converter.internal.DxaUtil.dxa2points;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.poi.xwpf.converter.internal.itext.styles.StyleBorder;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTDecimalNumber;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGrid;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblGridCol;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTcPr;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STBorder;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;

import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.PdfPCell;

public class XWPFTableUtil
{

    /**
     * Compute column widths of the XWPF table.
     * 
     * @param table
     * @return
     */
    public static float[] computeColWidths( XWPFTable table )
    {

        float[] colWidths;
        // Get first row to know if there is cell which have gridSpan to compute
        // columns number.
        int nbCols = getNumberOfColumnFromFirstRow( table );

        // Compare nbCols computed with number of grid colList
        CTTblGrid grid = table.getCTTbl().getTblGrid();
        List<CTTblGridCol> cols = grid.getGridColList();
        if ( nbCols > cols.size() )
        {
            Collection<Float> maxColWidths = null;
            Collection<Float> currentColWidths = null;

            // nbCols computed is not equals to number of grid colList
            // columns width must be computed by looping for each row/cells
            List<XWPFTableRow> rows = table.getRows();
            for ( XWPFTableRow row : rows )
            {
                currentColWidths = computeColWidths( row );
                if ( maxColWidths == null )
                {
                    maxColWidths = currentColWidths;
                }
                else
                {
                    if ( currentColWidths.size() > maxColWidths.size() )
                    {
                        maxColWidths = currentColWidths;
                    }
                }
            }

            colWidths = new float[maxColWidths.size()];
            int i = 0;
            for ( Float colWidth : maxColWidths )
            {
                colWidths[i++] = colWidth;
            }
            return colWidths;

        }
        else
        {
            // nbCols computed is equals to number of grid colList
            // columns width can be computed by using the grid colList
            colWidths = new float[cols.size()];
            float colWidth = -1;
            for ( int i = 0; i < colWidths.length; i++ )
            {
                CTTblGridCol tblGridCol = cols.get( i );
                colWidth = tblGridCol.getW().floatValue();
                colWidths[i] = dxa2points( colWidth );
            }
        }
        return colWidths;
    }

    /**
     * Returns number of column if the XWPF table by using the declared cell (which can declare gridSpan) from the first
     * row.
     * 
     * @param table
     * @return
     */
    public static int getNumberOfColumnFromFirstRow( XWPFTable table )
    {
        // Get first row to know if there is cell which have gridSpan to compute
        // columns number.
        int nbCols = 0;
        int numberOfRows = table.getNumberOfRows();
        if ( numberOfRows > 0 )
        {
            XWPFTableRow firstRow = table.getRow( 0 );
            List<XWPFTableCell> tableCellsOffFirstRow = firstRow.getTableCells();
            for ( XWPFTableCell tableCellOffFirstRow : tableCellsOffFirstRow )
            {
                CTDecimalNumber gridSpan = getGridSpan( tableCellOffFirstRow );
                if ( gridSpan != null )
                {
                    nbCols += gridSpan.getVal().intValue();
                }
                else
                {
                    nbCols += 1;
                }
            }
        }
        return nbCols;
    }

    public static CTDecimalNumber getGridSpan( XWPFTableCell cell )
    {
        if ( cell.getCTTc().getTcPr() != null )
            return cell.getCTTc().getTcPr().getGridSpan();

        return null;
    }

    public static CTTblWidth getWidth( XWPFTableCell cell )
    {
        return cell.getCTTc().getTcPr().getTcW();
    }

    private static Collection<Float> computeColWidths( XWPFTableRow row )
    {
        List<Float> colWidths = new ArrayList<Float>();
        List<XWPFTableCell> cells = row.getTableCells();
        for ( XWPFTableCell cell : cells )
        {

            // Width
            CTTblWidth width = getWidth( cell );
            if ( width != null )
            {
                int nb = 1;
                CTDecimalNumber gridSpan = getGridSpan( cell );
                TableWidth tableCellWidth = getTableWidth( cell );
                if ( gridSpan != null )
                {
                    nb = gridSpan.getVal().intValue();
                }
                for ( int i = 0; i < nb; i++ )
                {
                    colWidths.add( tableCellWidth.width / nb );
                }
            }
        }
        return colWidths;
    }

    /**
     * Returns table width of teh XWPF table.
     * 
     * @param table
     * @return
     */
    public static TableWidth getTableWidth( XWPFTable table )
    {
        float width = 0;
        boolean percentUnit = false;
        CTTblPr tblPr = table.getCTTbl().getTblPr();
        if ( tblPr.isSetTblW() )
        {
            CTTblWidth tblWidth = tblPr.getTblW();
            return getTableWidth( tblWidth );
        }
        return new TableWidth( width, percentUnit );
    }

    public static TableWidth getTableWidth( XWPFTableCell cell )
    {
        float width = 0;
        boolean percentUnit = false;
        CTTcPr tblPr = cell.getCTTc().getTcPr();
        if ( tblPr.isSetTcW() )
        {
            CTTblWidth tblWidth = tblPr.getTcW();
            return getTableWidth( tblWidth );
        }
        return new TableWidth( width, percentUnit );
    }

    public static TableWidth getTableWidth( CTTblWidth tblWidth )
    {
        float width = tblWidth.getW().intValue();
        boolean percentUnit = ( STTblWidth.INT_PCT == tblWidth.getType().intValue() );
        if ( percentUnit )
        {
            width = width / 100f;
        }
        else
        {
            width = dxa2points( width );
        }
        return new TableWidth( width, percentUnit );
    }

    public static void setBorder( CTBorder border, PdfPCell pdfPCell, int borderSide )
    {
        if ( border == null )
        {
            return;
        }
        boolean noBorder = ( STBorder.NONE == border.getVal() );

        // No border
        if ( noBorder )
        {
            pdfPCell.disableBorderSide( borderSide );
        }
        else
        {
            float size = -1;
            BigInteger borderSize = border.getSz();
            if ( borderSize != null )
            {
                size = dxa2points( borderSize );
            }

            Color borderColor = null;
            String hexColor = getBorderColor( border );
            if ( hexColor != null )
            {
                borderColor = ColorRegistry.getInstance().getColor( "0x" + hexColor );
            }

            switch ( borderSide )
            {
                case Rectangle.TOP:
                    if ( size != -1 )
                    {
                        pdfPCell.setBorderWidthTop( size );
                    }
                    if ( borderColor != null )
                    {

                        pdfPCell.setBorderColorTop( borderColor );
                    }
                    break;
                case Rectangle.BOTTOM:
                    if ( size != -1 )
                    {
                        pdfPCell.setBorderWidthBottom( size );
                    }
                    if ( borderColor != null )
                    {
                        pdfPCell.setBorderColorBottom( borderColor );
                    }
                    break;
                case Rectangle.LEFT:
                    if ( size != -1 )
                    {
                        pdfPCell.setBorderWidthLeft( size );
                    }
                    if ( borderColor != null )
                    {
                        pdfPCell.setBorderColorLeft( borderColor );
                    }
                    break;
                case Rectangle.RIGHT:
                    if ( size != -1 )
                    {
                        pdfPCell.setBorderWidthRight( size );
                    }
                    if ( borderColor != null )
                    {
                        pdfPCell.setBorderColorRight( borderColor );
                    }
                    break;
            }
        }
    }

    public static void setBorder( StyleBorder border, PdfPCell pdfPCell, int borderSide )
    {
        if ( border == null )
        {
            return;
        }
        // boolean noBorder = (STBorder.NONE == border.getVal());

        // No border

        float size = -1;
        BigInteger borderSize = border.getWidth();
        if ( borderSize != null )
        {
            size = dxa2points( borderSize );
        }

        Color borderColor = border.getColor();

        switch ( borderSide )
        {
            case Rectangle.TOP:
                if ( size != -1 )
                {
                    pdfPCell.setBorderWidthTop( size );
                }
                if ( borderColor != null )
                {

                    pdfPCell.setBorderColorTop( borderColor );
                }
                break;
            case Rectangle.BOTTOM:
                if ( size != -1 )
                {
                    pdfPCell.setBorderWidthBottom( size );
                }
                if ( borderColor != null )
                {
                    pdfPCell.setBorderColorBottom( borderColor );
                }
                break;
            case Rectangle.LEFT:
                if ( size != -1 )
                {
                    pdfPCell.setBorderWidthLeft( size );
                }
                if ( borderColor != null )
                {
                    pdfPCell.setBorderColorLeft( borderColor );
                }
                break;
            case Rectangle.RIGHT:
                if ( size != -1 )
                {
                    pdfPCell.setBorderWidthRight( size );
                }
                if ( borderColor != null )
                {
                    pdfPCell.setBorderColorRight( borderColor );
                }
                break;
        }

    }

    public static String getBorderColor( CTBorder border )
    {
        if ( border == null )
        {
            return null;
        }
        // border.getColor returns object???, use attribute w:color to get
        // the color.
        Node colorAttr =
            border.getDomNode().getAttributes().getNamedItemNS( "http://schemas.openxmlformats.org/wordprocessingml/2006/main",
                                                                "color" );
        if ( colorAttr != null )
        {
            return ( (Attr) colorAttr ).getValue();
        }
        return null;
    }
}
