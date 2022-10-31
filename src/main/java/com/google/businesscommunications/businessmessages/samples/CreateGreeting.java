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
 * This code snippet creates a new greeting.
 *
 * Read more: https://developers.google.com/business-communications/business-messages/reference/business-communications/rest/v1/brands.agents.greetings
 * 
 * This code is based on the https://github.com/google-business-communications/java-businesscommunications Java
 * Business Communications client library.
 */

public class CreateGreeting {

  private static BusinessCommunications.Builder builder;

  public static void main(String[] args) {

    builder = BusinessCommunicationsApi.getBusinessCommunicationsBuilder();

    String brandId = "EDIT_HERE";
    String agentId = "EDIT_HERE";

    String agentName = "brands/" + brandId + "/agents/" + agentId;

    // Create a list of conversation starters for the greeting 
    List<ConversationStarters> conversationStarters = new ArrayList<ConversationStarters>() {{
      add(new ConversationStarters().setSuggestion(new Suggestion()
          .setReply(new SuggestedReply().setText("Click me").setPostbackData("postback_option_1"))));
      add(new ConversationStarters().setSuggestion(new Suggestion()
          .setReply(new SuggestedReply().setText("Chip #2").setPostbackData("postback_option_2"))));
    }};

    // Create a list of welcome messages for the greeting 
    List<WelcomeMessage> welcomeMessages = new ArrayList<WelcomeMessage>() {{
      add(new WelcomeMessage().setText("Hello there!"));
      add(new WelcomeMessage().setText("How may I help you?"));
    }};

    // Actually create the greeting object
    Greeting greeting = new Greeting();
    greeting
      .setLocale("en")
      .setCustomId("My first greeting")
      .setWelcomeMessages(welcomeMessages)
      .setConversationStarters(conversationStarters);

    // Build the request to create the greeting and execute it
    try {
      BusinessCommunications.Brands.Agents.Greetings.Create request = builder
          .build().brands().agents().greetings().create(agentName, greeting);
      
      greeting = request.execute();

      // Print the response and display the contents of the greeting
      System.out.println(greeting.toPrettyString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
