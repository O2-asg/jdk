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

/*
 * @test
 * @bug 4651568 8008577 8174269
 * @modules jdk.localedata
 * @summary Verifies the currency pattern for pt_BR locale
 * @run main Bug4651568
 */

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class Bug4651568 {

    public static void main (String argv[] )  {
        Locale reservedLocale = Locale.getDefault();
        try {
            String expectedCurrencyPattern = "\u00a4\u00a0#.##0,00";

            Locale locale = new Locale ("pt", "BR");
            Locale.setDefault(locale);

            DecimalFormat formatter =
                (DecimalFormat) NumberFormat.getCurrencyInstance(locale);

            if (formatter.toLocalizedPattern().equals(
                        expectedCurrencyPattern)) {
                System.out.println ("Passed.");
            } else {
                 System.out.println ("Failed Currency pattern." +
                        "  Expected:  " + expectedCurrencyPattern +
                        "  Received:  " + formatter.toLocalizedPattern() );
                 throw new RuntimeException();

            }
        } finally {
            // restore the reserved locale
            Locale.setDefault(reservedLocale);
        }
    }
}
