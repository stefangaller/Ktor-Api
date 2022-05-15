Feature: Web Server
  This feature checks only the web server; it does not access the database.

  Scenario: Web server is up and running
    When I request "/hello"
    Then the server responds "HELLO WORLD!"

