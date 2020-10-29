# BUSINESS COMMUNICATIONS: Command-Line Sample

This sample demonstrates how to use the [Business Communications Java client library](https://github.com/google-business-communications/java-businesscommunications) for performing operations
with the [Business Communications API](https://businesscommunications.googleapis.com).

This application assumes that you're signed up with
[Business Messages](https://developers.google.com/business-communications/business-messages/guides/set-up/register).

## Documentation

The documentation for the Business Commmunications API can be found [here](https://developers.google.com/business-communications/business-messages/reference/business-communications/rest).

## Prerequisite

You must have the following software installed on your machine:

* [Apache Maven](http://maven.apache.org) 3.3.9 or greater
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

## Before you begin

1.  [Register with Business Messages](https://developers.google.com/business-communications/business-messages/guides/set-up/register).
1.  Once registered, follow the instructions to [enable the APIs for your project](https://developers.google.com/business-communications/business-messages/guides/set-up/register#enable-api).

### Setup your API authentication credentials

This sample application uses a service account key file to authenticate the Business Communication API calls for your registered Google Cloud project. You must download a service account key and configure it for the sample.

To download a service account key and configure it for the sample, follow the instructions below.

1.  Open [Google Cloud Console](https://console.cloud.google.com) with your
    Business Messages Google account and make sure you select the project that you registered for Business Messages with.

1.  Create a service account.

    1.   Navigate to [Credentials](https://console.cloud.google.com/apis/credentials).

    2.   Click **Create service account**.

    3.   For **Service account name**, enter your agent's name, then click **Create**.

    4.   For **Select a role**, choose **Project** > **Editor**, then click **Continue**.

    5.   Under **Create key**, choose **JSON**, then click **Create**.

         Your browser downloads the service account key. Store it in a secure location.

    6.  Click **Done**.

    7.  Copy the JSON credentials file into this sample's /resources
        folder and rename it to "bc-agent-service-account-credentials.json".

## Samples

### Brand CRUD operations

This sample demonstrates how to create a new brand, get brand details, update a brand, list all brands, and remove the created brand.

View the [source code](https://github.com/google-business-communications/bc-bm-java-command-line-examples/blob/master/src/main/java/com/google/businesscommunications/businessmessages/samples/BrandSample.java).

Usage:

```bash
mvn compile && mvn exec:java -Dexec.mainClass="com.google.businesscommunications.businessmessages.samples.BrandSample"
```

Alternative usage:

```bash
mvn compile && mvn exec:java -Dexec.mainClass="com.google.businesscommunications.businessmessages.samples.BrandSample" -Dexec.args="NO-DELETE"
```

If the NO-DELETE argument exists, the brand that gets created by the sample will not be deleted.

### Agent CRUD operations

This sample demonstrates how to create a new agent, get agent details, update the agent, list all agent, and remove the created agent.

View the [source code](https://github.com/google-business-communications/bc-bm-java-command-line-examples/blob/master/src/main/java/com/google/businesscommunications/businessmessages/samples/AgentSample.java).

Usage:

```bash
mvn compile && mvn exec:java -Dexec.mainClass="com.google.businesscommunications.businessmessages.samples.AgentSample" -Dexec.args="BRAND_NAME"
```

Replace BRAND_NAME with a valid brand ID in "brands/BRAND_ID" format. If you haven't created a brand, run the brand sample with the NO-DELETE argument to create a brand to reference.

Alternative usage:

```bash
mvn compile && mvn exec:java -Dexec.mainClass="com.google.businesscommunications.businessmessages.samples.AgentSample" -Dexec.args="BRAND_NAME NO-DELETE"
```

Replace BRAND_NAME with a valid brand ID in "brands/BRAND_ID" format. If you haven't created a brand, run the brand sample with the NO-DELETE argument to create a brand to reference.

### Location CRUD operations

This sample demonstrates how to create a new location, get location details, update the location, list all location, and remove the created location.

View the [source code](https://github.com/google-business-communications/bc-bm-java-command-line-examples/blob/master/src/main/java/com/google/businesscommunications/businessmessages/samples/LocationSample.java).

Usage:

```bash
mvn compile && mvn exec:java -Dexec.mainClass="com.google.businesscommunications.businessmessages.samples.LocationSample" -Dexec.args="AGENT_NAME"
```

Replace AGENT_NAME with a valid agent ID in "brands/BRAND_ID/agents/AGENT_ID" format. If you haven't created an agent, run the agent sample with the NO-DELETE argument to create an agent to reference.
