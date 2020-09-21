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

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.businesscommunications.v1.BusinessCommunications;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BusinessCommunicationsApi {

  private static final Logger logger = Logger
      .getLogger(BusinessCommunicationsApi.class.getName());

  // Object to maintain OAuth2 credentials to call the BM API
  private static GoogleCredential credential;

  /**
   * Initializes credentials used by the Business Communications API.
   *
   * @param credentialsFileLocation The location for the GCP service account key file.
   */
  private static void initCredentials(String credentialsFileLocation) {
    logger.info("Initializing credentials for the Business Communications API.");

    try {
      credential = GoogleCredential
          .fromStream(
              BusinessCommunicationsApi.class.getResourceAsStream("/" + credentialsFileLocation));

      credential = credential.createScoped(Arrays.asList(
          "https://www.googleapis.com/auth/businesscommunications"));

      credential.refreshToken();
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }
  }

  /**
   * Initializes the Business Communications builder.
   */
  public static BusinessCommunications.Builder getBusinessCommunicationsBuilder() {
    if (credential == null) {
      initCredentials(Constants.CREDENTIALS_FILE_NAME);
    }

    BusinessCommunications.Builder builder = null;

    try {
      HttpTransport httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      JacksonFactory jsonFactory = JacksonFactory.getDefaultInstance();

      // Create instance of the Business Communications API
      builder = new BusinessCommunications
          .Builder(httpTransport, jsonFactory, null)
          .setApplicationName(credential.getServiceAccountProjectId());

      // Set the API credentials and endpoint
      builder.setHttpRequestInitializer(credential);
      builder.setRootUrl(Constants.API_URL);
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }

    return builder;
  }
}
