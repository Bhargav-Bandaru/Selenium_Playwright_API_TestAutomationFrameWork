# ExpertRise BDD Automation Framework

A complete, production-ready BDD automation framework built on:
**Java 17 · Cucumber 7 · Selenium 4 · Playwright · RestAssured · TestNG · JUnit 5 · Allure · Extent Reports**

---

## Framework Structure

```
expertrise-bdd-framework/
├── src/
│   └── test/
│       ├── java/com/expertrise/automation/
│       │   ├── runners/
│       │   │   └── RunCucumberTest.java          ← JUnit 5 Suite runner
│       │   ├── hooks/
│       │   │   └── Hooks.java                    ← @Before/@After per tag
│       │   ├── stepDefinitions/
│       │   │   ├── LoginStepDefinitions.java     ← Selenium login steps
│       │   │   ├── ApiStepDefinitions.java       ← RestAssured API steps
│       │   │   └── PlaywrightStepDefinitions.java← Playwright steps
│       │   ├── pages/
│       │   │   └── LoginPage.java                ← Page Object (Selenium)
│       │   ├── actions/
│       │   │   └── LoginActions.java             ← Business logic layer
│       │   ├── config/
│       │   │   ├── DriverFactory.java            ← ThreadLocal WebDriver + Playwright
│       │   │   └── ConfigManager.java            ← config.properties reader
│       │   └── utils/
│       │       ├── ApiUtility.java               ← RestAssured helpers
│       │       ├── PlaywrightUtil.java            ← Playwright helpers
│       │       ├── JsonUtil.java                 ← JSON read/write (Jackson)
│       │       ├── WaitUtil.java                 ← Selenium explicit waits
│       │       ├── ScreenshotUtil.java           ← Screenshot on failure
│       │       ├── ExtentReportManager.java      ← Extent Reports singleton
│       │       └── TestDataUtil.java             ← testdata.properties reader
│       └── resources/
│           ├── features/
│           │   ├── login.feature                 ← Selenium UI scenarios
│           │   ├── api.feature                   ← RestAssured API scenarios
│           │   └── playwright.feature            ← Playwright UI scenarios
│           ├── config/
│           │   └── config.properties             ← framework config
│           ├── testdata/
│           │   └── testdata.properties           ← test credentials + data
│           ├── junit-platform.properties         ← Cucumber + parallel config
│           └── log4j2.xml                        ← logging config
├── .github/workflows/
│   └── bdd-pipeline.yml                         ← GitHub Actions CI/CD
├── User.json                                     ← json-server database
├── User.clean.json                               ← clean state for CI reset
└── pom.xml                                       ← all dependencies + plugins
```

---

## Prerequisites

| Tool | Version | Install |
|------|---------|---------|
| Java JDK | 17+ | https://adoptium.net |
| Maven | 3.9+ | https://maven.apache.org |
| Node.js | 18+ | https://nodejs.org (for json-server mock API) |
| Chrome | Latest | https://www.google.com/chrome |

---

## Quick Start

### 1. Clone and build
```bash
git clone <your-repo-url>
cd expertrise-bdd-framework
mvn clean compile
```

### 2. Install Playwright browsers (first time only)
```bash
mvn exec:java -e -D exec.mainClass=com.microsoft.playwright.CLI -D exec.args="install"
```

### 3. Start the mock API (for API tests)
```bash
npm install -g json-server
cp User.clean.json User.json
json-server --watch User.json --port 3000
```

### 4. Run tests

```bash
# All tests
mvn test

# Smoke tests only (fastest — CI default)
mvn test -Dcucumber.filter.tags="@smoke"

# Full regression
mvn test -Dcucumber.filter.tags="@regression"

# API tests only (no browser needed)
mvn test -Dcucumber.filter.tags="@api or @mockService"

# Playwright tests only
mvn test -Dcucumber.filter.tags="@playwright"

# Specific browser
mvn test -Dbrowser=firefox

# Headless mode (CI)
mvn test -Dheadless=true

# Custom environment
mvn test -Denv=staging -Dbase.url=https://staging.example.com
```

### 5. Generate reports
```bash
# Cucumber HTML report
mvn verify

# Allure report (opens in browser)
mvn allure:serve
```

---

## Reports

| Report         | Location                    | When Generated      |
|----------------|-----------------------------|---------------------|
| Cucumber HTML  | `target/cucumber-report/`   | `mvn verify`        |
| Extent Reports | `target/extent-reports/`    | After each test run |
| Allure         | `target/allure-report/`     | `mvn allure:report` |
| Test logs      | `target/logs/framework.log` | During test run     |
| Screenshots    | `target/screenshots/`       | On test failure     |

---

## Tag Strategy

| Tag            | Purpose                       | Runs In                   |
|----------------|-------------------------------|---------------------------|
| `@smoke`       | Critical path — fast feedback | Every CI push             |
| `@regression`  | Full suite                    | Nightly / release         |
| `@ui`          | Selenium WebDriver tests      | With Chrome/Firefox       |
| `@playwright`  | Playwright tests              | With Playwright browsers  |
| `@api`         | RestAssured API tests         | No browser needed         |
| `@mockService` | Tests against json-server     | Needs json-server running |
| `@login`       | Login/auth scenarios          | Subset of @ui             |
| `@negative`    | Negative / error path         | Subset of @regression     |
| `@crud`        | CRUD operation tests          | Subset of @api            |

---

## Adding New Tests — 3 Steps

### Step 1 — Write the feature file
```gherkin
# src/test/resources/features/myfeature.feature
@ui @smoke
Feature: My New Feature
  Scenario: My test
    Given ...
    When ...
    Then ...
```

### Step 2 — Create the Step Definitions
```java
// src/test/java/com/expertrise/automation/stepDefinitions/MyStepDefinitions.java
public class MyStepDefinitions {
    @Given("...")  public void ... { myActions.doSomething(); }
    @When("...")   public void ... { myActions.doSomethingElse(); }
    @Then("...")   public void ... { myActions.verify(); }
}
```

### Step 3 — Create Page Object + Actions (for UI tests)
```java
// Page Object: src/.../pages/MyPage.java
// Actions:     src/.../actions/MyActions.java
```

---

## CI/CD Pipeline

The GitHub Actions pipeline (`.github/workflows/bdd-pipeline.yml`) runs:

1. **API Tests** — RestAssured against json-server mock (fastest)
2. **UI Selenium Tests** — headless Chrome (parallel with API tests)
3. **UI Playwright Tests** — Playwright chromium (parallel with API tests)
4. **Allure Report** — deployed to GitHub Pages after all jobs
5. **Email Notification** — HTML report attached, sent to configured recipient

### Required GitHub Secrets
| Secret               | Value                                     |
|----------------------|-------------------------------------------|
| `GMAIL_USERNAME`     | your-email@gmail.com                      |
| `GMAIL_APP_PASSWORD` | Gmail App Password (not regular password) |
| `TO_EMAIL`           | recipient@email.com                       |

---

## Architecture Decisions

**ThreadLocal WebDriver** — Every parallel thread gets its own browser instance. No shared state = no flakiness during parallel execution.

**Tag-based Hooks** — `@Before("@ui")` only fires for Selenium scenarios. `@Before("@playwright")` only for Playwright. `@Before("@api")` for API-only. No unnecessary browser launches.

**3-Layer Pattern** — Step Definitions → Actions → Page Objects. Steps stay clean, Actions contain assertions, Pages contain locators.

**Explicit Waits Only** — Zero `Thread.sleep()` in the framework. All waits use `WebDriverWait + ExpectedConditions` or Playwright's built-in auto-waiting.
