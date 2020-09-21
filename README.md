# BUSINESS COMMUNICATIONS: Command-Line Sample

This sample demonstrates how to use the Java SDK for performing operations
with the [Business Communications API](https://businesscommunications.googleapis.com)

This application assumes that you're signed up with
[Business Messages](https://developers.google.com/business-communications/business-messages/guides/set-up/register).

## PREREQUISITES

You must have the following software installed on your machine:

* [Apache Maven](http://maven.apache.org) 3.3.9 or greater
* [Java 8](http://www.oracle.com/technetwork/java/javase/downloads/index.html)

## SETUP

Register with Business Messages:

    1.  Open [Google Cloud Console](https://console.cloud.google.com) with your
        Business Messages Google account and create a new project for your agent.

        Note the **Project ID** and **Project number** values.

    2.  Open the
        [Business Communications API](https://console.developers.google.com/apis/library/businesscommunications.googleapis.com)
        in the API Library.

    3.  Click **Enable**.

    4.  [Register your project](https://developers.google.com/business-communications/business-messages/guides/set-up/register)
        with Business Messages.

    5.  Create a service account.

        1.   Navigate to [Credentials](https://console.cloud.google.com/apis/credentials).

        2.   Click **Create service account**.

        3.   For **Service account name**, enter your agent's name, then click **Create**.

        4.   For **Select a role**, choose **Project** > **Editor**, the click **Continue**.

        5.   Under **Create key**, choose **JSON**, then click **Create**.

             Your browser downloads the service account key. Store it in a secure location.

    6.  Click **Done**.

    7.  Copy the JSON credentials file into this sample's /src/main/resources
        folder and rename it to "bc-agent-service-account-credentials.json".

## RUN THE SAMPLE

    1. In a terminal, navigate to this sample's root directory.

    2. Run the methods:
        *   Brand API example:

            mvn compile && mvn exec:java -Dexec.mainClass="com.google.businesscommunications.businessmessages.samples.BrandSample"

        *   Brand API example without brand deletion:

            mvn compile && mvn exec:java -Dexec.mainClass="com.google.businesscommunications.businessmessages.samples.BrandSample" -Dexec.args="NO-DELETE"

            If the NO-DELETE argument exists, the brand that gets created by the
            sample will not be deleted.

        *   Agent API example:

            mvn compile && mvn exec:java -Dexec.mainClass="com.google.businesscommunications.businessmessages.samples.AgentSample" -Dexec.args="BRAND_NAME"

            Replace BRAND_NAME with a valid brand ID in "brands/BRAND_ID" format.
            If you haven't created a brand, run the brand sample with the NO-DELETE
            argument to create a brand to reference.

        *   Agent API example without agent deletion:

            mvn compile && mvn exec:java -Dexec.mainClass="com.google.businesscommunications.businessmessages.samples.AgentSample" -Dexec.args="BRAND_NAME NO-DELETE"

            Replace BRAND_NAME with a valid brand ID in "brands/BRAND_ID" format.
            If you haven't created a brand, run the brand sample with the NO-DELETE
            argument to create a brand to reference.

        *   Location API example:

            mvn compile && mvn exec:java -Dexec.mainClass="com.google.businesscommunications.businessmessages.samples.LocationSample" -Dexec.args="AGENT_NAME"

            Replace AGENT_NAME with a valid agent ID in "brands/BRAND_ID/agents/AGENT_ID" format.
            If you haven't created an agent, run the agent sample with the NO-DELETE
            argument to create an agent to reference.
