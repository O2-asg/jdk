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
 * @bug 4938846 8008577 8174269
 * @modules jdk.localedata
 * @summary Test case for en_IE TimeZone info
 * @run main Bug4938846
 */

import java.util.Locale;
import java.util.TimeZone;

public class Bug4938846 {

   public static void main(String[] args) {
       Locale tzLocale = Locale.of("en", "IE");

       TimeZone ieTz = TimeZone.getTimeZone("Europe/Dublin");

       // "Standard" because of the negative DST, summer is considered standard for Europe/Dublin
       if (!ieTz.getDisplayName(true, TimeZone.LONG, tzLocale).equals ("Irish Standard Time"))
             throw new RuntimeException("\nString for Europe/Dublin, en_IE locale should be \"Irish Standard Time\"");

   }
}
