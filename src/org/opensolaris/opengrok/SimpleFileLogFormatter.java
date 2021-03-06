/*
 * CDDL HEADER START
 *
 * The contents of this file are subject to the terms of the
 * Common Development and Distribution License (the "License").
 * You may not use this file except in compliance with the License.
 *
 * See LICENSE.txt included in this distribution for the specific
 * language governing permissions and limitations under the License.
 *
 * When distributing Covered Code, include this CDDL HEADER in each
 * file and include the License file at LICENSE.txt.
 * If applicable, add the following below this CDDL HEADER, with the
 * fields enclosed by brackets "[]" replaced with your own identifying
 * information: Portions Copyright [yyyy] [name of copyright owner]
 *
 * CDDL HEADER END
 */

/*
 * Copyright (c) 2010, 2013, Oracle and/or its affiliates. All rights reserved.
 */

package org.opensolaris.opengrok;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;

/**
 * Opengrok logfile formatter
 * Creates a logentry in the logfile on the following format
 * YYYY-MM-DD HH:MM:ss.SSSZ <loglevel> t<threadnumber> <Class.method>: <logmessage>
 * @author Lubos Kosco
 */
final public class SimpleFileLogFormatter extends Formatter {

   private final java.text.SimpleDateFormat formatter =
      new java.text.SimpleDateFormat("yyyy-MM-dd' 'HH:mm:ss.SSSZ");

   // Format the time stamp. Must be synchronized since SimpleDateFormatter
   // isn't thread safe.
   private synchronized String ts(Date date) {
      return formatter.format(date);
   }

   private String classNameOnly(String name) {
      int index = name.lastIndexOf('.') + 1;
      return name.substring(index);
   }

    @Override
    public String format(LogRecord record) {
        StringWriter sw = new StringWriter();

        try (PrintWriter pw = new PrintWriter(sw)) {
            pw.print(ts(new Date(record.getMillis())));
            pw.print(' ');
            pw.print(record.getLevel().getName());
            pw.print(" t");
            pw.print(record.getThreadID());
            pw.print(' ');
            pw.print(classNameOnly(record.getSourceClassName()));
            pw.print('.');
            pw.print(record.getSourceMethodName());
            pw.print(": ");
            pw.print(formatMessage(record));
            pw.println();
            Throwable thrown = record.getThrown();
            if (thrown != null) {
                thrown.printStackTrace(pw);
            }
        }

        return sw.toString();
    }
}
