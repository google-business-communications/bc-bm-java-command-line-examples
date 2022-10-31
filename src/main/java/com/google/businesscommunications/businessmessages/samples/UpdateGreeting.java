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
 * This code snippet gets a greeting.
 *
 * Read more: https://developers.devsite.corp.google.com/business-communications/business-messages/reference/business-communications/rest/v1/brands.agents.greetings#get
 * 
 * This code is based on the https://github.com/google-business-communications/java-businesscommunications Java
 * Business Communications client library.
 */

public class UpdateGreeting {

  private static BusinessCommunications.Builder builder;

  public static void main(String[] args) {

    builder = BusinessCommunicationsApi.getBusinessCommunicationsBuilder();

    String brandId = "EDIT_HERE";
    String agentId = "EDIT_HERE";
    String greetingId = "EDIT_HERE";

    String greetingName = "brands/" + brandId + "/agents/" + agentId + 
        "/greetings/" + greetingId;

    // Create a list of welcome messages for the updated greeting 
    List<WelcomeMessage> welcomeMessages = new ArrayList<WelcomeMessage>() {{
      add(new WelcomeMessage().setText("Hello there!"));
      add(new WelcomeMessage().setText("Updated message"));
    }};

    // Actually create the updated greeting object
    Greeting greeting = new Greeting();
    greeting
      .setCustomId("Updated greeting name")
      .setWelcomeMessages(welcomeMessages);

    // Build the request to get the greeting object and execute it
    try {
      BusinessCommunications.Brands.Agents.Greetings.Patch request = builder
          .build().brands().agents().greetings().patch(greetingName, greeting);

      request.setUpdateMask("welcomeMessages,customId");

      Greeting updateGreeting = request.execute();

      // Print the response and display the contents of the greeting
      System.out.println(updateGreeting.toPrettyString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
