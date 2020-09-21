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
import com.google.api.services.businesscommunications.v1.model.Brand;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A sample application that runs multiple requests against the Business Communications API. The
 * requests this sample makes are:
 * <ul>
 * <li>Create a brand</li>
 * <li>Gets the brand details</li>
 * <li>Updates the created brand's display name</li>
 * <li>Lists all brands available</li>
 * <li>Delete the created brand</li>
 * </ul>
 */
public class BrandSample {

  private static final Logger logger = Logger.getLogger(BrandSample.class.getName());

  private static BusinessCommunications.Builder builder;

  public static void main(String args[]) {
    boolean deleteBrand = true;

    // Check arguments, if NO-DELETE is passed in, the brand will not be deleted
    if (args.length == 1) {
      deleteBrand = args[0].equals("NO-DELETE") ? false : true;
    }

    builder = BusinessCommunicationsApi.getBusinessCommunicationsBuilder();

    // Create a brand
    View.header("Create Brand:");
    Brand brand = createBrand();

    View.printBreak(5);

    // Get brand details
    View.header("Get Brand Details:");
    brand = getBrand(brand.getName());

    View.printBreak(3);

    // Update the brand
    View.header("Updating Brand:");
    brand = updateBrand(brand, "New Test Brand Name");

    View.printBreak(3);

    // List brands
    View.header("List Brands:");
    listBrands();

    if (deleteBrand) {
      View.printBreak(3);

      // Delete brand
      View.header("Deleting Brand:");
      deleteBrand(brand.getName());
    }
  }

  /**
   * Creates a brand with the name "Test Brand".
   *
   * @return The brand object that was created.
   */
  private static Brand createBrand() {
    Brand brand = null;
    try {
      BusinessCommunications.Brands.Create request = builder
          .build().brands().create(new Brand().setDisplayName("Test Brand"));

      brand = request.execute();

      System.out.println(brand.toPrettyString());
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }

    return brand;
  }

  /**
   * Updates the passed in brand object with a new display name.
   *
   * @param brand The brand to be updated.
   * @param displayName The new display name.
   * @return The updated brand object.
   */
  private static Brand updateBrand(Brand brand, String displayName) {
    Brand updatedBrand = null;
    try {
      // Update the brand object's display name for the new value
      brand.setDisplayName(displayName);

      BusinessCommunications.Brands.Patch request = builder
          .build().brands().patch(brand.getName(), brand);

      updatedBrand = request.execute();

      System.out.println(updatedBrand.toPrettyString());
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }

    return updatedBrand;
  }

  /**
   * Based on the brand name, looks up the brand details.
   *
   * @param brandName The unique identifier for the brand in "brands/BRAND_ID" format.
   * @return The matching brand object.
   */
  private static Brand getBrand(String brandName) {
    Brand brand = null;
    try {
      BusinessCommunications.Brands.Get request = builder
          .build().brands().get(brandName);

      brand = request.execute();

      System.out.println(brand.toPrettyString());
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }

    return brand;
  }

  /**
   * Lists all brands for the configured Cloud project.
   */
  private static void listBrands() {
    try {
      BusinessCommunications.Brands.List request = builder.build().brands().list();

      List<Brand> brands = request.execute().getBrands();
      brands.stream().forEach(brand -> {
        try {
          System.out.println(brand.toPrettyString());
        } catch (IOException e) {
          logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
        }
      });
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }
  }

  /**
   * Based on the brand name, deletes the brand. Deleting a brand with associated agents will also
   * result in the agents also being deleted. Only brands without verified agents can be deleted.
   *
   * @param brandName The unique identifier for the brand in "brands/BRAND_ID" format.
   */
  private static void deleteBrand(String brandName) {
    try {
      BusinessCommunications.Brands.Delete request = builder.build().brands().delete(brandName);

      System.out.println(request.execute());
    } catch (Exception e) {
      logger.log(Level.SEVERE, Constants.EXCEPTION_WAS_THROWN, e);
    }
  }
}
