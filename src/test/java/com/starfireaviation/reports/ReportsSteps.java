/*
 *  Copyright (C) 2022 Starfire Aviation, LLC
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.starfireaviation.reports;

import com.starfireaviation.model.CommonConstants;
import io.cucumber.java.Before;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import static org.assertj.core.api.Assertions.fail;

@Slf4j
public class ReportsSteps {

    /**
     * URL.
     */
    protected static final String URL = "http://localhost:8080";

    /**
     * ORGANIZATION.
     */
    protected static final String ORGANIZATION = "TEST_ORG";

    /**
     * RestTemplate.
     */
    protected RestTemplate restTemplate = new RestTemplateBuilder()
            .errorHandler(new RestTemplateResponseErrorHandler()).build();

    @Autowired
    protected TestContext testContext;

    @Before
    public void init() {
        testContext.reset();
    }

    @When("^I get the (.*) report")
    public void iGetTheXReport(final String reportType) throws Throwable {
        // TODO
    }

    @When("^I submit the report")
    public void iAddTheReport() throws Throwable {
        log.info("I submit the report");
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (testContext.getOrganization() != null) {
            headers.add(CommonConstants.ORGANIZATION_HEADER_KEY, testContext.getOrganization());
        }
        if (testContext.getCorrelationId() != null) {
            headers.add(CommonConstants.CORRELATION_ID_HEADER_KEY, testContext.getCorrelationId());
        }
        //final HttpEntity<Question> httpEntity = new HttpEntity<>(testContext.getQuestion(), headers);
        //testContext.setResponse(restTemplate.postForEntity(URL, httpEntity, Void.class));
    }

    @Then("^I should receive (.*)$")
    public void iShouldReceive(final String expectedResult) throws Throwable {
        switch (expectedResult) {
            case "a report added response":
                log.info("I should receive a report added response");
                //Assertions.assertSame(HttpStatus.OK, testContext.getResponse().getStatusCode());
                break;
            case "an InvalidPayloadException":
                log.info("I should receive an InvalidPayloadException");
                //Assertions.assertSame(HttpStatus.OK, testContext.getResponse().getStatusCode());
                break;
            case "a success response":
                log.info("I should receive a success response");
                //Assertions.assertSame(HttpStatus.OK, testContext.getResponse().getStatusCode());
                break;
            case "an unauthenticated response":
                log.info("I should receive an unauthenticated response");
                //Assertions.assertSame(HttpStatus.OK, testContext.getResponse().getStatusCode());
                break;
            case "the QuizCompletionChart report":
                log.info("I should receive the QuizCompletionChart report");
                //Assertions.assertSame(HttpStatus.OK, testContext.getResponse().getStatusCode());
                break;
            case "the QuizUserResultsChart report":
                log.info("I should receive the QuizUserResultsChart report");
                //Assertions.assertSame(HttpStatus.OK, testContext.getResponse().getStatusCode());
                break;
            case "the QuizResultsChart report":
                log.info("I should receive the QuizResultsChart report");
                //Assertions.assertSame(HttpStatus.OK, testContext.getResponse().getStatusCode());
                break;
            default:
                fail("Unexpected error");
        }
    }
}
