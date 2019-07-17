/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2019 iText Group NV
    Authors: iText Software.

    This program is free software; you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License version 3
    as published by the Free Software Foundation with the addition of the
    following permission added to Section 15 as permitted in Section 7(a):
    FOR ANY PART OF THE COVERED WORK IN WHICH THE COPYRIGHT IS OWNED BY
    ITEXT GROUP. ITEXT GROUP DISCLAIMS THE WARRANTY OF NON INFRINGEMENT
    OF THIRD PARTY RIGHTS

    This program is distributed in the hope that it will be useful, but
    WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
    or FITNESS FOR A PARTICULAR PURPOSE.
    See the GNU Affero General Public License for more details.
    You should have received a copy of the GNU Affero General Public License
    along with this program; if not, see http://www.gnu.org/licenses or write to
    the Free Software Foundation, Inc., 51 Franklin Street, Fifth Floor,
    Boston, MA, 02110-1301 USA, or download the license from the following URL:
    http://itextpdf.com/terms-of-use/

    The interactive user interfaces in modified source and object code versions
    of this program must display Appropriate Legal Notices, as required under
    Section 5 of the GNU Affero General Public License.

    In accordance with Section 7(b) of the GNU Affero General Public License,
    a covered work must retain the producer line in every PDF that is created
    or manipulated using iText.

    You can be released from the requirements of the license by purchasing
    a commercial license. Buying such a license is mandatory as soon as you
    develop commercial activities involving the iText software without
    disclosing the source code of your own applications.
    These activities include: offering paid services to customers as an ASP,
    serving PDFs on the fly in a web application, shipping iText with a closed
    source product.

    For more information, please contact iText Software Corp. at this
    address: sales@itextpdf.com
 */
package com.itextpdf.html2pdf.attach.impl.layout;

import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.test.annotations.type.UnitTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;

@Category(UnitTest.class)
public class HtmlDocumentRendererTest {

    @Test
    public void shouldAttemptTrimLastPageTest() {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new ByteArrayOutputStream()));
        Document document = new Document(pdfDocument);
        HtmlDocumentRenderer documentRenderer = new HtmlDocumentRenderer(document, false);
        document.setRenderer(documentRenderer);
        pdfDocument.addNewPage();

        Assert.assertEquals(1, pdfDocument.getNumberOfPages());
        // For one-page documents it does not make sense to attempt to trim last page
        Assert.assertFalse(documentRenderer.shouldAttemptTrimLastPage());

        pdfDocument.addNewPage();
        Assert.assertEquals(2, pdfDocument.getNumberOfPages());
        // If there are more than one page, we try to trim last page
        Assert.assertTrue(documentRenderer.shouldAttemptTrimLastPage());
    }

    @Test
    public void trimLastPageIfNecessaryTest() {
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(new ByteArrayOutputStream()));
        Document document = new Document(pdfDocument);
        HtmlDocumentRenderer documentRenderer = new HtmlDocumentRenderer(document, false);
        document.setRenderer(documentRenderer);
        pdfDocument.addNewPage();
        pdfDocument.addNewPage();
        new PdfCanvas(pdfDocument.getLastPage()).moveTo(10, 10).lineTo(20, 20).stroke();
        pdfDocument.addNewPage();

        Assert.assertEquals(3, pdfDocument.getNumberOfPages());
        documentRenderer.trimLastPageIfNecessary();
        Assert.assertEquals(2, pdfDocument.getNumberOfPages());
        documentRenderer.trimLastPageIfNecessary();
        Assert.assertEquals(2, pdfDocument.getNumberOfPages());
    }
    
}