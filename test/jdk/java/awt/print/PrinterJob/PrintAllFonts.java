/*
 * Copyright (c) 2007, 2024, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterJob;

/*
 * @test
 * @bug 4884389 7183516
 * @key printer
 * @library /test/lib /java/awt/regtesthelpers
 * @build PassFailJFrame
 * @summary Font specified with face name loses style on printing
 * @run main/manual PrintAllFonts
 */
public class PrintAllFonts implements Printable {

    private static Font[] allFonts;
    private int lineHeight = 18;
    private int fontNum = 0;
    private int startNum = 0;
    private int thisPage = 0;

    private static final String INSTRUCTIONS =
            "You must have a printer available to perform this test.\n" +
            "\n" +
            "This bug is system dependent and is not always reproducible.\n" +
            "A passing test will have all text printed with correct font style.";

    public static void main(String[] args) throws Exception {

        if (PrinterJob.lookupPrintServices().length == 0) {
            throw new RuntimeException("Printer not configured or available.");
        }

        PassFailJFrame passFailJFrame = new PassFailJFrame.Builder()
                .instructions(INSTRUCTIONS)
                .rows((int) INSTRUCTIONS.lines().count() + 1)
                .columns(45)
                .build();

        GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
        allFonts = ge.getAllFonts();

        PrinterJob pj = PrinterJob.getPrinterJob();
        pj.setPrintable(new PrintAllFonts());
        if (pj.printDialog()) {
            pj.print();
        }
        passFailJFrame.awaitAndCheck();
    }

    public int print(Graphics g, PageFormat pf, int pageIndex) {

        if (fontNum >= allFonts.length && pageIndex > thisPage) {
            return NO_SUCH_PAGE;
        }
        if (pageIndex > thisPage) {
            startNum = fontNum;
            thisPage = pageIndex;
        } else {
            fontNum = startNum;
        }

        int fontsPerPage = (int) pf.getImageableHeight() / lineHeight;
        int x = (int) pf.getImageableX() + 10;
        int y = (int) pf.getImageableY() + lineHeight;

        g.setColor(Color.black);
        for (int n = 0; n < fontsPerPage; n++) {
            Font f = allFonts[fontNum].deriveFont(Font.PLAIN, 14);
            Font fi = allFonts[fontNum].deriveFont(Font.ITALIC, 14);
            g.setFont(f);
            g.drawString(f.getFontName(), x, y);
            g.setFont(fi);
            g.drawString(f.getFontName(), (int) (x + pf.getImageableWidth() / 2), y);
            y += lineHeight;
            fontNum++;
            if (fontNum >= allFonts.length) {
                break;
            }
        }
        return PAGE_EXISTS;
    }
}
