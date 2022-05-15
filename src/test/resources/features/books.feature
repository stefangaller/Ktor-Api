Feature: Books database
  Scenario: Basic CRUD operations on the books database
    Given empty books database

    When I create the following book
    """
    {"title":"xyz","author":"abc","age":42}
    """
    And I request all books
    Then my book is returned

    When I delete the book titled "xyz"
    And I request all books
    Then no book is returned
