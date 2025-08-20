Feature: API de precios - Casos de error (escenarios KO)

  Background:
    # Ensure baseUrl is available even if karate-config.js wasn't applied
    * def baseUrl = karate.properties['baseUrl'] ? karate.properties['baseUrl'] : 'http://localhost:8083/v1'
    * url baseUrl

  Scenario: Test KO-7 - petición con fecha invalida devuelve status 400
    Given path 'prices'
    And param activeDate = '2020-06-14-invalid-format'
    And param productId = 35455
    And param brandId = 1
    When method GET
    Then status 400
    And match response.error == 'Bad Request'
    And match response.message != null

  Scenario: Test KO-8 - petición con productId en formato (string) devuelve status 400
    Given path 'prices'
    And param activeDate = '2020-06-14T10:00:00'
    And param productId = 'invalid'
    And param brandId = 1
    When method GET
    Then status 400
    And match response.error == 'Bad Request'
    And match response.message != null

  Scenario: Test KO-9 - petición con brandId formato (string) devuelve status 400
    Given path 'prices'
    And param activeDate = '2020-06-14T10:00:00'
    And param productId = 35455
    And param brandId = 'invalid'
    When method GET
    Then status 400
    And match response.error == 'Bad Request'
    And match response.message != null

  # Casos de prueba para escenarios con resultado vacío (200 OK pero arreglo vacío)
  
  Scenario: Test KO-10 - producto inexistente debe devolver lista vacía
    Given path 'prices'
    And param activeDate = '2020-06-14T10:00:00'
    And param productId = 99999
    And param brandId = 1
    When method GET
    Then status 200
    And match response == []
    And assert response.length == 0

  Scenario: Test KO-11 - marca inexistente debe devolver lista vacía
    Given path 'prices'
    And param activeDate = '2020-06-14T10:00:00'
    And param productId = 35455
    And param brandId = 999
    When method GET
    Then status 200
    And match response == []
    And assert response.length == 0

  Scenario: Test KO-12 - fecha fuera del rango de precios disponible debe devolver lista vacía
    Given path 'prices'
    And param activeDate = '2019-01-01T10:00:00'
    And param productId = 35455
    And param brandId = 1
    When method GET
    Then status 200
    And match response == []
    And assert response.length == 0

  Scenario: Test KO-13 - fecha futura fuera del rango de precios disponible debe devolver lista vacía
    Given path 'prices'
    And param activeDate = '2021-12-31T23:59:59'
    And param productId = 35455
    And param brandId = 1
    When method GET
    Then status 200
    And match response == []
    And assert response.length == 0

  Scenario: Test KO-14 - combinación de producto y marca inexistentes debe devolver lista vacía
    Given path 'prices'
    And param activeDate = '2020-06-14T10:00:00'
    And param productId = 88888
    And param brandId = 777
    When method GET
    Then status 200
    And match response == []
    And assert response.length == 0
