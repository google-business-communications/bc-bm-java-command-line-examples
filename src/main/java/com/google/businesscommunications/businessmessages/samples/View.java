/*
 * Copyright (C) 2020 Google Inc. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.businesscommunications.businessmessages.samples;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

public class View {
  private static final Logger logger = Logger.getLogger(View.class.getName());

  static void printBreak(int delay) {
    System.out.println();
    for(int i = 0; i < delay; i++) {
      System.out.print(".");
      try {
        TimeUnit.SECONDS.sleep(1);
      } catch (Exception e) {
        logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
      }
    }
    System.out.println();
  }

  static void header(String name) {
    System.out.println();
    System.out.println("================== " + name + " ==================");
    System.out.println();
  }
}
