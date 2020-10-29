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
import com.google.api.services.businesscommunications.v1.model.ConversationStarters;
import com.google.api.services.businesscommunications.v1.model.ConversationalSetting;
import com.google.api.services.businesscommunications.v1.model.Location;
import com.google.api.services.businesscommunications.v1.model.LocationEntryPointConfig;
import com.google.api.services.businesscommunications.v1.model.OfflineMessage;
import com.google.api.services.businesscommunications.v1.model.OpenUrlAction;
import com.google.api.services.businesscommunications.v1.model.PrivacyPolicy;
import com.google.api.services.businesscommunications.v1.model.SuggestedAction;
import com.google.api.services.businesscommunications.v1.model.SuggestedReply;
import com.google.api.services.businesscommunications.v1.model.Suggestion;
import com.google.api.services.businesscommunications.v1.model.WelcomeMessage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A sample application that runs multiple requests against the Business Communications API. The
 * requests this sample makes are:
 * <ul>
 * <li>Create a location</li>
 * <li>Gets the location details</li>
 * <li>Updates the created locations's associated agent</li>
 * <li>Lists all locations for a brands</li>
 * <li>Delete the created location</li>
 * </ul>
 */
public class LocationSample {

  private static final Logger logger = Logger.getLogger(BrandSample.class.getName());

  private static BusinessCommunications.Builder builder;

  public static void main(String args[]) {
    if (args.length != 1) {
      logger.severe("Usage: mvn exec:java -Dexec.args=\"<AGENT_NAME>\"");

      System.exit(-1);
    }

    // Parse the input arguments
    String agentName = args[0];
    String brandName = "";

    // Check that the agent name matches the expected format
    String regex = "brands/\\S+/agents/\\S+";
    if (!agentName.matches(regex)) {
      logger.severe("Your agent name, " + agentName + ", is not properly formatted. It must be "
          + "the full agent name in the format of \"brands/BRAND_ID/agents/AGENT_ID\"");

      System.exit(-1);
    } else {
      brandName = agentName.substring(0, agentName.indexOf("/agents"));
    }

    View.header("Location script for brand: " + brandName + " and agent: " + agentName);

    builder = BusinessCommunicationsApi.getBusinessCommunicationsBuilder();

    // Create a location
    View.header("Create Location:");
    Location location = createLocation(brandName, agentName);

    View.printBreak(5);

    // Get location details
    View.header("Get Location Details:");
    location = getLocation(location.getName());

    View.printBreak(3);

    // Update the location
    // NOTE: This call will fail unless the agentId parameter is a valid agentId
    View.header("Updating Location:");
    location = updateLocation(location, "/brands/BRAND_ID/agents/AGENT_ID");

    View.printBreak(3);

    // List locations
    View.header("List Locations:");
    listLocations(brandName);

    // Delete the location
    View.header("Deleting Location:");
    deleteLocation(location.getName());
  }

  /**
   * Creates a location associated with the given brand and agent.
   *
   * @param brandName The brand name that this location belongs to in brands/BRAND_ID format.
   * @param agentName The agent name to associate with this location in
   * "brands/BRAND_ID/agents/AGENT_ID" format.
   * @return The newly created location object.
   */
  private static Location createLocation(String brandName, String agentName) {
    Location location = null;
    try {
      List<LocationEntryPointConfig> locationEntryPointConfigs = new ArrayList<>();
      locationEntryPointConfigs
          .add(new LocationEntryPointConfig().setAllowedEntryPoint("PLACESHEET"));
      locationEntryPointConfigs
          .add(new LocationEntryPointConfig().setAllowedEntryPoint("MAPS_TACTILE"));

      // Create 5 conversation starters, including one that will redirect to a URL
      // to be shown as part of the welcome message
      List<ConversationStarters> conversationStarters = new ArrayList<ConversationStarters>() {{
        add(new ConversationStarters().setSuggestion(new Suggestion()
            .setReply(new SuggestedReply().setText("Chip #1").setPostbackData("chip_1"))));
        add(new ConversationStarters().setSuggestion(new Suggestion()
            .setReply(new SuggestedReply().setText("Chip #2").setPostbackData("chip_2"))));
        add(new ConversationStarters().setSuggestion(new Suggestion()
            .setReply(new SuggestedReply().setText("Chip #3").setPostbackData("chip_3"))));
        add(new ConversationStarters().setSuggestion(new Suggestion()
            .setReply(new SuggestedReply().setText("Chip #4").setPostbackData("chip_4"))));
        add(new ConversationStarters().setSuggestion(new Suggestion()
            .setAction(new SuggestedAction().setText("Chip #5").setPostbackData("chip_5")
                .setOpenUrlAction(new OpenUrlAction().setUrl("https://www.google.com")))));
      }};

      // Create a map between the language code and the initial conversation settings
      // NOTE: Only "en" is supported at the moment
      Map<String, ConversationalSetting> conversationalSettings = new HashMap<String, ConversationalSetting>() {{
        put("en", new ConversationalSetting()
            .setPrivacyPolicy(new PrivacyPolicy().setUrl("http://www.company.com/privacy"))
            .setWelcomeMessage(new WelcomeMessage().setText("Welcome! How can I help?"))
            .setOfflineMessage(new OfflineMessage().setText(
                "This location is currently offline, please leave a message and we will get back to you as soon as possible."))
            .setConversationStarters(conversationStarters));
      }};

      BusinessCommunications.Brands.Locations.Create request = builder
          .build().brands().locations().create(brandName,
              new Location()
                  .setDefaultLocale("en")
                  .setAgent(agentName)
                  .setPlaceId("ChIJj61dQgK6j4AR4GeTYWZsKWw")
                  .setConversationalSettings(conversationalSettings)
                  .setLocationEntryPointConfigs(locationEntryPointConfigs));

      location = request.execute();

      System.out.println(location.toPrettyString());
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }

    return location;
  }

  /**
   * Updates the agent associated with the given location.
   *
   * @param location The location that needs to be updated.
   * @param agentName The new agent to associate with the location.
   * @return The updated location object.
   */
  private static Location updateLocation(Location location, String agentName) {
    Location updatedLocation = null;
    try {
      // Update the location object's associated agent
      location.setAgent(agentName);

      BusinessCommunications.Brands.Locations.Patch request = builder
          .build().brands().locations().patch(location.getName(), location);

      request.setUpdateMask("agent");

      updatedLocation = request.execute();

      System.out.println(updatedLocation.toPrettyString());
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }

    return updatedLocation;
  }

  /**
   * Based on the location name, looks up the location details.
   *
   * @param locationName The unique identifier for the location in "brands/BRAND_ID/locations/LOCATION_ID"
   * format.
   * @return The matching location object.
   */
  private static Location getLocation(String locationName) {
    Location location = null;
    try {
      BusinessCommunications.Brands.Locations.Get request = builder
          .build().brands().locations().get(locationName);

      location = request.execute();

      System.out.println(location.toPrettyString());
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }

    return location;
  }

  /**
   * Lists all locations for given brand.
   *
   * @param brandName The unique identifier for the brand.
   */
  private static void listLocations(String brandName) {
    try {
      BusinessCommunications.Brands.Locations.List request
          = builder.build().brands().locations().list(brandName);

      List<Location> locations = request.execute().getLocations();
      locations.stream().forEach(location -> {
        try {
          System.out.println(location.toPrettyString());
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }
  }

  /**
   * Based on the location name, deletes location agent. Only a non-verified location can be
   * deleted.
   *
   * @param locationName The unique identifier for the location in "brands/BRAND_ID/locations/LOCATION_ID"
   * format.
   */
  private static void deleteLocation(String locationName) {
    try {
      BusinessCommunications.Brands.Locations.Delete request = builder.build().brands().locations()
          .delete(locationName);

      System.out.println(request.execute());
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }
  }
}