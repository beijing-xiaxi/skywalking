/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package org.apache.skywalking.apm.collector.core.util;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author peng-yongsheng
 */
public enum FileUtils {
    INSTANCE;

    private static final Logger logger = LoggerFactory.getLogger(FileUtils.class);

    public String readLastLine(File file) {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "r");
            long length = randomAccessFile.length();
            if (length == 0) {
                return Const.EMPTY_STRING;
            } else {
                long position = length - 1;
                randomAccessFile.seek(position);
                while (position >= 0) {
                    String lineString = "";
                    if (randomAccessFile.read() == '\n' && StringUtils.isNotEmpty(lineString = randomAccessFile.readLine())) {
                        return lineString;
                    }
                    randomAccessFile.seek(position);
                    if (position == 0) {
                        return randomAccessFile.readLine();
                    }
                    position--;
                }
            }
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
        return Const.EMPTY_STRING;
    }

    public void writeAppendToLast(File file, String value) {
        RandomAccessFile randomAccessFile = null;
        try {
            randomAccessFile = new RandomAccessFile(file, "rwd");
            long length = randomAccessFile.length();
            randomAccessFile.seek(length);
            randomAccessFile.writeBytes(System.lineSeparator() + value);
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
        } finally {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }
    }
}