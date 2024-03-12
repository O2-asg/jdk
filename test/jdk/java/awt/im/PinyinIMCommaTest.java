/*
 * Copyright (c) 2016, 2024, Oracle and/or its affiliates. All rights reserved.
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

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jtreg.SkippedException;
import sun.awt.OSInfo;

/*
 * @test
 * @bug 8148984
 * @summary Verify that Chinese comma can be entered in JTextField
 *          with Pinyin input method (IM) on OS X.
 * @requires (os.family == "mac")
 * @modules java.desktop/sun.awt
 * @library /java/awt/regtesthelpers /test/lib
 * @build PassFailJFrame jtreg.SkippedException Util
 * @run main/manual PinyinIMCommaTest
 */

public class PinyinIMCommaTest {
    private static final String INSTRUCTIONS = """
            This test is for OS X only. It verifies if Chinese comma can be entered
            in JTextField with Pinyin input method (IM).

            Test settings:
            Go to "System Preferences -> Keyboard -> Input Sources" and
            add "Pinyin – Traditional" or "Pinyin – Simplified" IM from Chinese language group.
            Set current IM to "Pinyin".

            1. Set focus to the text field below and press "comma" character
               on the keyboard.
            2. Now change the current IM to the IM used before "Pinyin" or to "U.S".
               Press comma character again.
            3. You should notice a difference in the comma. Pinyin IM should output
             full width comma "\uff0c" and the other IM should output a Latin comma "\u002c".

            If above is true press "PASS", if "\u002c" character is displayed for Pinyin IM, press "FAIL".
            """;

    public static void main(String[] args) throws Exception {
        if (OSInfo.getOSType() != OSInfo.OSType.MACOSX) {
            throw new SkippedException("This test is for MacOS only");
        }
        PassFailJFrame.builder()
                      .title("Test Comma using Pinyin Input Method")
                      .instructions(INSTRUCTIONS)
                      .rows((int) INSTRUCTIONS.lines().count() + 2)
                      .columns(48)
                      .splitUIBottom(PinyinIMCommaTest::createUI)
                      .testTimeOut(10)
                      .build()
                      .awaitAndCheck();
    }

    private static JComponent createUI() {
        JPanel panel = new JPanel();
        JTextField input = new JTextField(20);
        panel.add(new JLabel("Text field:"));
        panel.add(input);
        return panel;
    }
}
