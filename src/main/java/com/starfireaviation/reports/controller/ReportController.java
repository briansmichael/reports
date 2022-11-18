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

package com.starfireaviation.reports.controller;

import com.starfireaviation.common.exception.AccessDeniedException;
import com.starfireaviation.common.exception.ResourceNotFoundException;
import com.starfireaviation.reports.service.ReportService;
import com.starfireaviation.reports.validation.ReportValidator;
import com.starfireaviation.common.CommonConstants;
import org.jfree.chart.ChartUtils;
import org.jfree.chart.JFreeChart;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.Principal;

/**
 * ReportController.
 */
@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping({ "/api/reports" })
public class ReportController {

        /**
         * ReportService.
         */
        private final ReportService reportService;

        /**
         * ReportValidator.
         */
        private final ReportValidator reportValidator;

        /**
         * ReportController.
         *
         * @param rService   ReportService
         * @param rValidator ReportValidator
         */
        public ReportController(final ReportService rService,
                                final ReportValidator rValidator) {
                reportService = rService;
                reportValidator = rValidator;
        }

        /**
         * Generates a pie chart image of a quiz's completion.
         *
         * @param quizId    Quiz ID
         * @param principal Principal
         * @return chart image
         * @throws IOException               when chart image is not producible
         * @throws ResourceNotFoundException when quiz is not found
         * @throws AccessDeniedException     when user is not permitted to perform
         *                                   operation
         */
        @GetMapping(path = "/quizzes/{quizId}/completion", produces = MediaType.IMAGE_PNG_VALUE)
        public @ResponseBody final byte[] getQuizCompletionChart(
                        @PathVariable("quizId") final long quizId,
                        final Principal principal)
                        throws IOException, ResourceNotFoundException, AccessDeniedException {
                reportValidator.accessAdminOrInstructor(principal);
                final JFreeChart chart = reportService.getQuizCompletionChart(quizId);
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                ChartUtils.writeChartAsPNG(out, chart, CommonConstants.SIX_HUNDRED_FORTY,
                        CommonConstants.FOUR_HUNDRED_EIGHTY);
                return out.toByteArray();
        }

        /**
         * Generates a pie chart image of a quiz user's results.
         *
         * @param quizId    Quiz ID
         * @param userId    User ID
         * @param principal Principal
         * @return chart image
         * @throws IOException               when chart image is not producible
         * @throws ResourceNotFoundException when quiz or user is not found
         * @throws AccessDeniedException     when user is not permitted to perform
         *                                   operation
         */
        @GetMapping(path = "/quizzes/{quizId}/user/{userId}/results", produces = MediaType.IMAGE_PNG_VALUE)
        public @ResponseBody final byte[] getQuizUserResultsChart(
                        @PathVariable("quizId") final long quizId,
                        @PathVariable("userId") final long userId,
                        final Principal principal)
                        throws IOException, ResourceNotFoundException, AccessDeniedException {
                reportValidator.accessAdminInstructorOrSpecificUser(userId, principal);
                final JFreeChart chart = reportService.getQuizUserResultsChart(quizId, userId);
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                ChartUtils.writeChartAsPNG(out, chart, CommonConstants.SIX_HUNDRED_FORTY,
                        CommonConstants.FOUR_HUNDRED_EIGHTY);
                return out.toByteArray();
        }

        /**
         * Generates a chart image of a quiz's results.
         *
         * @param quizId    Quiz ID
         * @param principal Principal
         * @return chart image
         * @throws IOException               when chart image is not producible
         * @throws ResourceNotFoundException when quiz is not found
         * @throws AccessDeniedException     when user is not permitted to perform
         *                                   operation
         */
        @GetMapping(path = "/quizzes/{quizId}/results", produces = MediaType.IMAGE_PNG_VALUE)
        public @ResponseBody final byte[] getQuizResultsChart(
                        @PathVariable("quizId") final long quizId,
                        final Principal principal)
                        throws IOException, ResourceNotFoundException, AccessDeniedException {
                reportValidator.accessAdminOrInstructor(principal);
                final JFreeChart chart = reportService.getQuizResultsChart(quizId);
                final ByteArrayOutputStream out = new ByteArrayOutputStream();
                ChartUtils.writeChartAsPNG(out, chart, CommonConstants.SIX_HUNDRED_FORTY,
                        CommonConstants.FOUR_HUNDRED_EIGHTY);
                return out.toByteArray();
        }
}
