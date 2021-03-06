/*
    This file is part of the iText (R) project.
    Copyright (c) 1998-2020 iText Group NV
    Authors: iText Software.

    This program is offered under a commercial and under the AGPL license.
    For commercial licensing, contact us at https://itextpdf.com/sales.  For AGPL licensing, see below.

    AGPL licensing:
    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU Affero General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU Affero General Public License for more details.

    You should have received a copy of the GNU Affero General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.itextpdf.html2pdf.css;

import com.itextpdf.html2pdf.ConverterProperties;
import com.itextpdf.html2pdf.ExtendedHtmlConversionITextTest;
import com.itextpdf.html2pdf.HtmlConverter;
import com.itextpdf.html2pdf.LogMessageConstant;
import com.itextpdf.io.util.UrlUtil;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.utils.CompareTool;
import com.itextpdf.test.annotations.LogMessage;
import com.itextpdf.test.annotations.LogMessages;
import com.itextpdf.test.annotations.type.IntegrationTest;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.FileInputStream;
import java.io.IOException;

@Category(IntegrationTest.class)
public class TargetCounterTest extends ExtendedHtmlConversionITextTest {

    public static final String sourceFolder = "./src/test/resources/com/itextpdf/html2pdf/css/TargetCounterTest/";
    public static final String destinationFolder = "./target/test/com/itextpdf/html2pdf/css/TargetCounterTest/";

    @BeforeClass
    public static void beforeClass() {
        createOrClearDestinationFolder(destinationFolder);
    }

    @Test
    public void targetCounterPageUrlNameTest() throws IOException, InterruptedException {
        convertToPdfWithTargetCounterEnabledAndCompare("targetCounterPageUrlName");
    }

    @Test
    public void targetCounterPageUrlIdTest() throws IOException, InterruptedException {
        convertToPdfWithTargetCounterEnabledAndCompare("targetCounterPageUrlId");
    }

    @Test
    @LogMessages(messages = @LogMessage(messageTemplate = LogMessageConstant.EXCEEDED_THE_MAXIMUM_NUMBER_OF_RELAYOUTS))
    public void targetCounterManyRelayoutsTest() throws IOException, InterruptedException {
        convertToPdfWithTargetCounterEnabledAndCompare("targetCounterManyRelayouts");
    }

    @Test
    public void targetCounterPageBigElementTest() throws IOException, InterruptedException {
        convertToPdfWithTargetCounterEnabledAndCompare("targetCounterPageBigElement");
    }

    @Test
    public void targetCounterPageAllTagsTest() throws IOException, InterruptedException {
        convertToPdfWithTargetCounterEnabledAndCompare("targetCounterPageAllTags");
    }

    @Test
    @LogMessages(messages = @LogMessage(messageTemplate = LogMessageConstant.CANNOT_RESOLVE_TARGET_COUNTER_VALUE, count = 2))
    public void targetCounterNotExistingTargetTest() throws IOException, InterruptedException {
        convertToPdfWithTargetCounterEnabledAndCompare("targetCounterNotExistingTarget");
    }

    private void convertToPdfWithTargetCounterEnabledAndCompare(String name) throws IOException, InterruptedException {
        String sourceHtml = sourceFolder + name + ".html";
        String cmpPdf = sourceFolder + "cmp_" + name + ".pdf";
        String destinationPdf = destinationFolder + name + ".pdf";

        ConverterProperties converterProperties = new ConverterProperties().setBaseUri(sourceFolder).setTargetCounterEnabled(true);
        PdfDocument pdfDocument = new PdfDocument(new PdfWriter(destinationPdf));
        try (FileInputStream fileInputStream = new FileInputStream(sourceHtml)) {
            HtmlConverter.convertToPdf(fileInputStream, pdfDocument, converterProperties);
        }
        System.out.println("html: " + UrlUtil.getNormalizedFileUriString(sourceHtml) + "\n");
        Assert.assertNull(new CompareTool().compareByContent(destinationPdf, cmpPdf, destinationFolder, "diff_" + name + "_"));
    }
}
