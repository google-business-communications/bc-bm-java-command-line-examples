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
import com.google.api.services.businesscommunications.v1.model.*;

import java.util.*;

/**
 * This code snippet lists all greetings associated with an agent
 * Read more: https://developers.google.com/business-communications/business-messages/reference/business-communications/rest/v1/brands.agents.greetings/list
 *
 * This code is based on the https://github.com/google-business-communications/java-businesscommunications Java
 * Business Communications client library.
 */


public class ListGreetings {

  private static BusinessCommunications.Builder builder;

  public static void main(String[] args) {

    builder = BusinessCommunicationsApi.getBusinessCommunicationsBuilder();
    
    String brandId = "EDIT_HERE";
    String agentId = "EDIT_HERE";

    String agentName = "brands/" + brandId + "/agents/" + agentId;

    // Build the request to get the greeting object and execute it
    try {
      BusinessCommunications.Brands.Agents.Greetings.List request = builder
          .build().brands().agents().greetings().list(agentName);

      ListGreetingsResponse greetings = request.execute();

      // Print the response and display the contents of the greeting
      System.out.println(greetings.toPrettyString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
