/*
 * JBoss, Home of Professional Open Source
 * Copyright 2013, Red Hat, Inc. and/or its affiliates, and individual
 * contributors by the @authors tag. See the copyright.txt in the
 * distribution for a full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.arquillian.droidium.native_.utils;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FileUtils;

/**
 * Set of utility methods for Droidium native plugin regarding file and directory management.
 *
 * @author <a href="mailto:smikloso@redhat.com">Stefan Miklosovic</a>
 *
 */
public class DroidiumNativeFileUtils {

    private static final Logger logger = Logger.getLogger(DroidiumNativeFileUtils.class.getName());

    /**
     * Removes working directory for Droidium native plugin
     *
     * @param dir directory to remove
     */
    public static void removeWorkingDir(File dir) {
        try {
            FileUtils.deleteDirectory(dir);
        } catch (IOException ex) {
            logger.log(Level.INFO, "Unable to delete temporary working dir {0}. Reason: {1}",
                new Object[] { dir.getAbsolutePath(), ex.getMessage() });
        }
    }

    /**
     * Creates directory with random name in {@code System.getProperty("java.io.tmpdir")}
     *
     * @return directory in system temporary directory
     */
    public static File createWorkingDir(File parent) {
        FileIdentifierGenerator fig = new FileIdentifierGenerator();
        File temp;

        try {
            do {
                temp = new File(parent, fig.getIdentifier(IdentifierType.FILE.getClass()));
            } while (!temp.mkdir());
        } catch (SecurityException ex) {
            logger.severe("Security manager denies to create the working dir in " + parent.getAbsolutePath());
            throw new RuntimeException("Unable to create working directory in " + parent.getAbsolutePath());
        }

        return temp;
    }

    /**
     * Creates empty file saved under {@code parent}
     *
     * @param parent parent directory of file to create
     * @return empty file saved in parent with random name
     */
    public static File createRandomEmptyFile(File parent) {
        FileIdentifierGenerator fig = new FileIdentifierGenerator();
        File temp;

        try {
            do {
                temp = new File(parent, fig.getIdentifier(IdentifierType.FILE.getClass()));
            } while (!temp.createNewFile());
        } catch (IOException ex) {
            throw new RuntimeException("Unable to create file in " + parent.getAbsolutePath());
        }
        return temp;
    }

    /**
     * Copies file to directory
     *
     * @param src source file
     * @param dest destination directory
     */
    public static void copyFileToDirectory(File src, File dest) {
        try {
            FileUtils.copyFileToDirectory(src, dest);
        } catch (IOException ex) {
            throw new RuntimeException("Unable to copy " + src.getAbsolutePath() + " to " + dest.getAbsolutePath());
        }
    }
}
