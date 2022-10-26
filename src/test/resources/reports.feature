@Reports
Feature: Reports
  As a user
  I want to interact with reports
  So that I might be able to see my learning progress

  Scenario Outline: Create a new report
    When I get the <reportType> report
    Then I should receive a success response
    And I should receive the <reportType> report

    Examples:
    | reportType           |
    | QuizCompletionChart  |
    | QuizUserResultsChart |
    | QuizResultsChart     |