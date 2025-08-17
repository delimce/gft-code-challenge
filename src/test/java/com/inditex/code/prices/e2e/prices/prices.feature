Feature: Prices API Test Cases

  Background:
    # Ensure baseUrl is available even if karate-config.js wasn't applied
    * def baseUrl = karate.properties['baseUrl'] ? karate.properties['baseUrl'] : 'http://localhost:8083/v1'
    * url baseUrl

  Scenario: Test 1 - petición a las 10:00 del día 14 del producto 35455 para la brand 1 (ZARA)
    Given path 'prices'
    And param activeDate = '2020-06-14T10:00:00'
    And param productId = 35455
    And param brandId = 1
    When method GET
    Then status 200
    * assert response.length == 1
    * match response[0].productId == 35455
    * match response[0].brandId == 1
    * match response[0].priceList == 1
    * match response[0].price == 35.50

  Scenario: Test 2 - petición a las 16:00 del día 14 del producto 35455 para la brand 1 (ZARA)
    Given path 'prices'
    And param activeDate = '2020-06-14T16:00:00'
    And param productId = 35455
    And param brandId = 1
    When method GET
    Then status 200
    * assert response.length == 1
    * match response[0].priceList == 2
    * match response[0].price == 25.45

  Scenario: Test 3 - petición a las 21:00 del día 14 del producto 35455 para la brand 1 (ZARA)
    Given path 'prices'
    And param activeDate = '2020-06-14T21:00:00'
    And param productId = 35455
    And param brandId = 1
    When method GET
    Then status 200
    * assert response.length == 1
    * match response[0].priceList == 1
    * match response[0].price == 35.50

  Scenario: Test 4 - petición a las 10:00 del día 15 del producto 35455 para la brand 1 (ZARA)
    Given path 'prices'
    And param activeDate = '2020-06-15T10:00:00'
    And param productId = 35455
    And param brandId = 1
    When method GET
    Then status 200
    * assert response.length == 1
    * match response[0].priceList == 3
    * match response[0].price == 30.50

  Scenario: Test 5 - petición a las 21:00 del día 16 del producto 35455 para la brand 1 (ZARA)
    Given path 'prices'
    And param activeDate = '2020-06-16T21:00:00'
    And param productId = 35455
    And param brandId = 1
    When method GET
    Then status 200
    * assert response.length == 1
    * match response[0].priceList == 4
    * match response[0].price == 38.95
