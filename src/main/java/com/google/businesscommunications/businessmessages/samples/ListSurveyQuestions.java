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

import com.google.api.services.businesscommunications.v1.BusinessCommunications;

import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A sample application that lists all available template survey questions.
 */
public class ListSurveyQuestions {

  private static BusinessCommunications.Builder builder;
  private static final Logger logger = Logger.getLogger(BrandSample.class.getName());

  public static void main(String[] args) {

    View.header("Fetching template survey questions");

    builder = BusinessCommunicationsApi.getBusinessCommunicationsBuilder();

    try {
      // Produce a request builder for sending a request to /v1/surveyQuestions.
      BusinessCommunications.SurveyQuestions.List request = builder.build().surveyQuestions().list();

      // Execute the request.
      System.out.println(request.execute());
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }
  }
}
