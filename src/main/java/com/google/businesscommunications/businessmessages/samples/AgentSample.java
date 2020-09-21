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
import com.google.api.services.businesscommunications.v1.enums.BusinessMessagesEntryPointConfigValues;
import com.google.api.services.businesscommunications.v1.enums.DayOfWeek;
import com.google.api.services.businesscommunications.v1.enums.InteractionType;
import com.google.api.services.businesscommunications.v1.model.Agent;
import com.google.api.services.businesscommunications.v1.model.BotRepresentative;
import com.google.api.services.businesscommunications.v1.model.BusinessMessagesAgent;
import com.google.api.services.businesscommunications.v1.model.BusinessMessagesEntryPointConfig;
import com.google.api.services.businesscommunications.v1.model.ConversationStarters;
import com.google.api.services.businesscommunications.v1.model.ConversationalSetting;
import com.google.api.services.businesscommunications.v1.model.Hours;
import com.google.api.services.businesscommunications.v1.model.HumanRepresentative;
import com.google.api.services.businesscommunications.v1.model.MessagingAvailability;
import com.google.api.services.businesscommunications.v1.model.OfflineMessage;
import com.google.api.services.businesscommunications.v1.model.OpenUrlAction;
import com.google.api.services.businesscommunications.v1.model.Phone;
import com.google.api.services.businesscommunications.v1.model.PrivacyPolicy;
import com.google.api.services.businesscommunications.v1.model.SuggestedAction;
import com.google.api.services.businesscommunications.v1.model.SuggestedReply;
import com.google.api.services.businesscommunications.v1.model.Suggestion;
import com.google.api.services.businesscommunications.v1.model.SupportedAgentInteraction;
import com.google.api.services.businesscommunications.v1.model.TimeOfDay;
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
 * <li>Create an agent</li>
 * <li>Gets the agent details</li>
 * <li>Updates the display name</li>
 * <li>Updates the agent logo</li>
 * <li>Updates the agent welcome message</li>
 * <li>Updates the agent's primary interaction's operating hours</li>
 * <li>Lists all agents for a brand</li>
 * <li>Delete the created agent</li>
 * </ul>
 */
public class AgentSample {

  private static final Logger logger = Logger.getLogger(BrandSample.class.getName());

  private static BusinessCommunications.Builder builder;

  public static void main(String[] args) {
    if (args.length < 1) {
      logger.severe("Usage: mvn exec:java -Dexec.args=\"<AGENT_ID>\"");

      System.exit(-1);
    }

    // Parse the input arguments
    String brandName = args[0];

    // Check that the brand name matches the expected format
    String regex = "brands/\\S+";
    if (!brandName.matches(regex)) {
      logger.severe("Your brand name, " + brandName + ", is not properly formatted. It must be "
          + "the full brand name in the format of \"brands/BRAND_ID\"");

      System.exit(-1);
    }

    boolean deleteAgent = true;

    // Check arguments, if NO-DELETE is passed in, the agent will not be deleted
    if (args.length == 2) {
      deleteAgent = args[1].equals("NO-DELETE") ? false : true;
    }

    View.header("Agent script for brand name: " + brandName);

    builder = BusinessCommunicationsApi.getBusinessCommunicationsBuilder();

    // Create an agent
    View.header("Create Agent:");
    Agent agent = createAgent(brandName);

    View.printBreak(5);

    // Get agent details
    View.header("Get Agent Details:");
    agent = getAgent(agent.getName());

    View.printBreak(3);

    // Update the agent
    View.header("Updating Agent Display Name:");
    agent = updateAgentDisplayName(agent, "New Test Agent Name");

    View.printBreak(3);

    View.header("Updating Agent Logo URL:");
    updateAgentLogo(agent,
        "https://storage.googleapis.com/sample-logos/growing-tree-bank-logo-square.png");

    View.printBreak(3);

    View.header("Updating Agent Welcome Message:");
    ConversationalSetting conversationalSetting = agent.getBusinessMessagesAgent()
        .getConversationalSettings().get("en");
    conversationalSetting
        .setWelcomeMessage(new WelcomeMessage().setText("The updated welcome message!"));
    updateAgentConversationalSettings(agent, conversationalSetting);

    View.printBreak(3);

    View.header("Updating Agent Primary Interaction Available Hours:");

    // Get the existing list of hours
    List<Hours> existingHours = agent.getBusinessMessagesAgent().getPrimaryAgentInteraction()
        .getBotRepresentative().getBotMessagingAvailability().getHours();

    // Changing the start time from 9am to 8am
    existingHours.get(0).setStartTime(new TimeOfDay().setHours(8));

    // Update the primary agent interaction object
    SupportedAgentInteraction supportedAgentInteraction = agent.getBusinessMessagesAgent()
        .getPrimaryAgentInteraction();
    supportedAgentInteraction.getBotRepresentative().getBotMessagingAvailability()
        .setHours(existingHours);

    // Send the update to the API
    updateAgentPrimaryAgentInteraction(agent, supportedAgentInteraction);

    View.printBreak(3);

    // List agents
    View.header("List Agents:");
    listAgents(brandName);

    if (deleteAgent) {
      View.printBreak(3);

      // Delete the agent
      View.header("Deleting Agent:");
      deleteAgent(agent.getName());
    }
  }

  /**
   * Creates a test agent associated with the given brand.
   *
   * This test agent is configured with a bot as its primary interaction type with an additional
   * non-primary interaction type for human to human interactions.
   *
   * If this was a real agent, the expected behavior for a conversation would be that an end-user
   * begins a conversation with a bot and if the conversation goes beyond the capabilities of the
   * bot, the conversation could be handed off to a live human agent.
   *
   * @param brandName The brand name that this agent belongs to.
   * @return The newly created agent object.
   */
  private static Agent createAgent(String brandName) {
    Agent agent = null;
    try {
      // Create a list of available hours to be used by the configured agent interaction types
      // This is availability between 9am and 5pm in the PST timezone
      // This is availability between 9am and 5pm in the PST timezone
      List<Hours> hours = new ArrayList<Hours>() {{
        add(new Hours()
            .setStartDay(DayOfWeek.MONDAY.toString())
            .setStartTime(new TimeOfDay().setHours(9).setMinutes(0))
            .setEndDay(DayOfWeek.FRIDAY.toString())
            .setEndTime(new TimeOfDay().setHours(17).setMinutes(0))
            .setTimeZone("America/Los_Angeles"));
      }};

      // Create a list of supported agent interactions for the non-primary interaction types
      List<SupportedAgentInteraction> additionalAgentInteractions
          = new ArrayList<SupportedAgentInteraction>() {{
        add(new SupportedAgentInteraction()
            .setInteractionType(InteractionType.HUMAN.toString())
            .setHumanRepresentative(new HumanRepresentative()
                .setHumanMessagingAvailability(new MessagingAvailability()
                    .setHours(hours))));
      }};

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
      Map<String, ConversationalSetting> conversationalSettings = new HashMap<String, ConversationalSetting>() {{
        put("en", new ConversationalSetting()
            .setPrivacyPolicy(new PrivacyPolicy().setUrl("http://www.company.com/privacy"))
            .setWelcomeMessage(new WelcomeMessage().setText("Welcome! How can I help?"))
            .setOfflineMessage(new OfflineMessage().setText(
                "We are currently offline, please leave a message and we will get back to you as soon as possible."))
            .setConversationStarters(conversationStarters));
      }};

      BusinessCommunications.Brands.Agents.Create request = builder
          .build().brands().agents().create(brandName,
              new Agent()
                  .setDisplayName("Test Agent")
                  .setBusinessMessagesAgent(new BusinessMessagesAgent()
                      .setDefaultLocale("en")
                      .setCustomAgentId("My custom agent ID") // Optional
                      .setPhone(new Phone().setNumber("+12223334444")) // Optional
                      .setLogoUrl("https://storage.googleapis.com/sample-logos/google-logo.png")
                      .setEntryPointConfigs(new ArrayList<BusinessMessagesEntryPointConfig>() {{
                        add(new BusinessMessagesEntryPointConfig()
                            .setAllowedEntryPoint(
                                BusinessMessagesEntryPointConfigValues.LOCATION.toString()));
                      }})
                      .setPrimaryAgentInteraction(new SupportedAgentInteraction()
                          .setInteractionType(InteractionType.BOT.toString())
                          .setBotRepresentative(new BotRepresentative()
                              .setBotMessagingAvailability(new MessagingAvailability()
                                  .setHours(hours))))
                      .setAdditionalAgentInteractions(additionalAgentInteractions) // Optional
                      .setConversationalSettings(conversationalSettings)
                  ));

      agent = request.execute();

      System.out.println(agent.toPrettyString());
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }

    return agent;
  }

  /**
   * Updates the agent display name.
   *
   * @param agent The agent that needs to be updated.
   * @param displayName The new agent agent display name.
   * @return The updated agent object.
   */
  private static Agent updateAgentDisplayName(Agent agent, String displayName) {
    // Update the agent's display name
    agent.setDisplayName(displayName);

    return updateAgent(agent, "displayName");
  }

  /**
   * Updates the agent logo.
   *
   * @param agent The agent that needs to be updated.
   * @param logoUrl The new agent agent logo URL.
   * @return The updated agent object.
   */
  private static Agent updateAgentLogo(Agent agent, String logoUrl) {
    // Update the agent's logo
    agent.getBusinessMessagesAgent().setLogoUrl(logoUrl);

    return updateAgent(agent, "businessMessagesAgent.logoUrl");
  }

  /**
   * Update the agent's conversational settings. NOTE: To update any field within the conversational
   * settings, you must update all fields within the object.
   *
   * @param agent The agent that needs to be updated.
   * @param conversationalSetting The new conversational settings.
   * @return The updated agent object.
   */
  private static Agent updateAgentConversationalSettings(Agent agent,
      ConversationalSetting conversationalSetting) {
    // Set the new conversational settings within the agent object
    agent.getBusinessMessagesAgent()
        .setConversationalSettings(new HashMap<String, ConversationalSetting>() {{
          put("en", conversationalSetting);
        }});

    return updateAgent(agent, "businessMessagesAgent.conversationalSettings.en");
  }

  /**
   * Update the agent's primary agent's interaction object.
   *
   * @param agent The agent that needs to be updated.
   * @param supportedAgentInteraction The new agent interaction for the primary interaction.
   * @return The updated agent object.
   */
  private static Agent updateAgentPrimaryAgentInteraction(Agent agent,
      SupportedAgentInteraction supportedAgentInteraction) {
    // Set the new primary interaction within the agent object
    agent.getBusinessMessagesAgent().setPrimaryAgentInteraction(supportedAgentInteraction);

    return updateAgent(agent, "businessMessagesAgent.primaryAgentInteraction");
  }

  /**
   * Updates the saved agent with the passed in agent object for the fields specified in the update
   * mask.
   *
   * @param agent The agent that needs to be updated.
   * @param updateMask A comma-separated list of fully qualified names of fields that are to be
   * included in the update.
   * @return The updated agent object.
   */
  private static Agent updateAgent(Agent agent, String updateMask) {
    Agent updatedAgent = null;
    try {
      BusinessCommunications.Brands.Agents.Patch request = builder
          .build().brands().agents().patch(agent.getName(), agent);

      request.setUpdateMask(updateMask);

      updatedAgent = request.execute();

      System.out.println(updatedAgent.toPrettyString());
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }

    return updatedAgent;
  }

  /**
   * Based on the agent name, looks up the agent details.
   *
   * @param agentName The unique identifier for the agent in "brands/BRAND_ID/agents/AGENT_ID"
   * format.
   * @return The matching agent object.
   */
  private static Agent getAgent(String agentName) {
    Agent agent = null;
    try {
      BusinessCommunications.Brands.Agents.Get request = builder
          .build().brands().agents().get(agentName);

      agent = request.execute();

      System.out.println(agent.toPrettyString());
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }

    return agent;
  }

  /**
   * Lists all agents for the given brand.
   *
   * @param brandName The unique identifier for the brand in "brands/BRAND_ID" format.
   */
  private static void listAgents(String brandName) {
    try {
      BusinessCommunications.Brands.Agents.List request
          = builder.build().brands().agents().list(brandName);

      List<Agent> agents = request.execute().getAgents();
      agents.stream().forEach(agent -> {
        try {
          System.out.println(agent.toPrettyString());
        } catch (IOException e) {
          e.printStackTrace();
        }
      });
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }
  }

  /**
   * Based on the agent name, deletes the agent. Only a non-verified agent can be deleted.
   *
   * @param agentName The unique identifier for the agent in "brands/BRAND_ID/agents/AGENT_ID"
   * format.
   */
  private static void deleteAgent(String agentName) {
    try {
      BusinessCommunications.Brands.Agents.Delete request = builder.build().brands().agents()
          .delete(agentName);

      System.out.println(request.execute());
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }
  }
}
