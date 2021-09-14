# DCH Zwift Selenium Example
TestNG test suites can be run from the .xml files located in the test/suite directory. see zwift-regression.xml for example.

## Requirements & Setup
- JAVA JDK 1.8 or higher.
- Chrome Version 93.   
- This project assumes you will be running from an IDE. Import project as a Gradle project. A wrapper is included but will depend on your IDE. You will need to enable the plugin within your IDE and modify `build.gradle` to include your IDE's plugin.
ex. ```plugins {
  id 'visual-studio'
  }```
- If your IDE requires a local gradle installation, please see https://gradle.org/install/
- Running from command-line: Can be accomplished by modifying the gradle.build file but not featured in this demo. 


## TestNG Parameters
This framework uses TestNG. To run tests a TestNG xml file must be executed. `testng.xml`
- Parameters
    - `<parameter name="environment" value="PROD"/>` Will accept Prod or Stage.
    - `<parameter name="webdriver" value="LOCAL"/>` LOCAL is the only option for this demo.
    - `<parameter name="browser" value="chrome"/>` Chrome is the only option for this demo.
    - `<parameter name="config" value="config.properties"/>` .properties file used to pass parameters.

## Running Tests
To run tests in a local browser: change `environment` parameter in `zwift-regression.xml` to `STAGE` or `PROD`

- Note: For this demo, the tests will only run on `PROD` unless we change the `STAGE_URL` variable in WebDriverSetup to Zwift's appropriate staging URL.   

# Generate Allure Report
Prereq - In order to generate Allure Reports you must install Allure locally. See https://docs.qameta.io/allure/#_installing_a_commandline
- From the project directory: `allure serve allure-results`
- To maintain test history locally, gradle task `copyHistory` must be executed after each run.
- Attachments & Test Steps require JVM parameter `-javaagent:"lib/aspectjweaver-1.9.6.jar`
  
- See allure_example folder for screenshots of the report.

# Troubleshooting
- This framework does not support Linux or the Apple M1 Chip.