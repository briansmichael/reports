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

package com.starfireaviation.reports.service;

import com.starfireaviation.reports.exception.ResourceNotFoundException;
import com.starfireaviation.model.Question;
import com.starfireaviation.model.Quiz;
import com.starfireaviation.model.User;
import com.starfireaviation.model.CommonConstants;
import lombok.extern.slf4j.Slf4j;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.StandardChartTheme;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.CategoryLabelPositions;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * ReportService.
 */
@Slf4j
public class ReportService {

    /**
     * QUESTION_ANSWERED_DESC_PATTERN.
     */
    private static final Pattern QUESTION_ANSWERED_DESC_PATTERN = Pattern.compile(
            "Duration [(.+)]; Answer Given [(.+)]; Answered Correctly [(.+)]");

    /**
     * NOT_ANSWERED.
     */
    private static final String NOT_ANSWERED = "Not Answered";

    /**
     * ANSWERED_CORRECTLY.
     */
    private static final String ANSWERED_CORRECTLY = "Answered Correctly";

    /**
     * ANSWERED_INCORRECTLY.
     */
    private static final String ANSWERED_INCORRECTLY = "Answered Incorrectly";

    /**
     * QUESTION_NUM.
     */
    private static final String QUESTION_NUM = "Question #";

    /**
     * GREEN.
     */
    private static final Color GREEN = new Color(0, 255, 0);

    /**
     * BLUE.
     */
    private static final Color BLUE = new Color(0, 0, 255);

    /**
     * RED.
     */
    private static final Color RED = new Color(255, 0, 0);

    /**
     * DataService.
     */
    private final DataService dataService;

    /**
     * ReportService.
     *
     * @param dService DataService
     */
    public ReportService(final DataService dService) {
        dataService = dService;
    }

    /**
     * Generates a quiz completion pie chart.
     *
     * @param quizId Quiz ID
     * @return JFreeChart
     * @throws ResourceNotFoundException when quiz is not found
     */
    public JFreeChart getQuizCompletionChart(final Long quizId) throws ResourceNotFoundException {
        final Quiz quiz = dataService.getQuiz(quizId);
        final List<Question> questions = getQuizQuestions(quiz);

        // Build dataset
        final List<Long> quizParticipants = new ArrayList<>();
        int answeredCorrectly = 0;
        int answeredIncorrectly = 0;
        // for (final Statistic statistic : getQuizQuestionAnsweredStatistics(quizId)) {
        // boolean correct = false;
        // final Matcher matcher =
        // QUESTION_ANSWERED_DESC_PATTERN.matcher(statistic.getDescription());
        // if (matcher.find()) {
        // log.info("getQuizCompletionChart() matching on " + matcher.group(3));
        // correct = Boolean.parseBoolean(matcher.group(3));
        // }
        // if (correct) {
        // answeredCorrectly += 1;
        // } else {
        // answeredIncorrectly += 1;
        // }
        // if (!quizParticipants.contains(statistic.getUserId())) {
        // quizParticipants.add(statistic.getUserId());
        // }
        // }
        final DefaultPieDataset dataset = new DefaultPieDataset();
        final int notAnswered = (questions.size() * quizParticipants.size())
                - (answeredCorrectly + answeredIncorrectly);
        dataset.setValue(NOT_ANSWERED + " [" + notAnswered + "]", notAnswered);
        dataset.setValue(ANSWERED_CORRECTLY + " [" + answeredCorrectly + "]", answeredCorrectly);
        dataset.setValue(ANSWERED_INCORRECTLY + " [" + answeredIncorrectly + "]", answeredIncorrectly);

        // Build chart
        final JFreeChart chart = ChartFactory.createPieChart(
                String.format("Quiz \"%s\" Completion", quiz.getTitle()),
                dataset);
        final PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint(NOT_ANSWERED + " [" + notAnswered + "]", BLUE);
        plot.setSectionPaint(ANSWERED_CORRECTLY + " [" + answeredCorrectly + "]", GREEN);
        plot.setSectionPaint(ANSWERED_INCORRECTLY + " [" + answeredIncorrectly + "]", RED);
        applyChartTheme(chart);
        return chart;
    }

    /**
     * Generates a quiz completion pie chart.
     *
     * @param quizId Quiz ID
     * @param userId User ID
     * @return JFreeChart
     * @throws ResourceNotFoundException when quiz is not found
     */
    public JFreeChart getQuizUserResultsChart(final Long quizId, final Long userId) throws ResourceNotFoundException {
        final Quiz quiz = dataService.getQuiz(quizId);
        final User user = dataService.getUser(userId);
        final List<Question> questions = getQuizQuestions(quiz);

        // Build dataset
        int answeredCorrectly = 0;
        int answeredIncorrectly = 0;
        // final List<Statistic> userStatistics =
        // getQuizQuestionAnsweredStatistics(quizId)
        // .stream()
        // .filter(statistic -> statistic.getUserId() == userId)
        // .collect(Collectors.toList());
        // for (final Statistic statistic : userStatistics) {
        // boolean correct = false;
        // final Matcher matcher =
        // QUESTION_ANSWERED_DESC_PATTERN.matcher(statistic.getDescription());
        // if (matcher.find()) {
        // log.info("getQuizCompletionChart() matching on " + matcher.group(3));
        // correct = Boolean.valueOf(matcher.group(3));
        // }
        // if (correct) {
        // answeredCorrectly += 1;
        // } else {
        // answeredIncorrectly += 1;
        // }
        // }
        final DefaultPieDataset dataset = new DefaultPieDataset();
        final int notAnswered = questions.size() - (answeredCorrectly + answeredIncorrectly);
        dataset.setValue(NOT_ANSWERED + " [" + notAnswered + "]", notAnswered);
        dataset.setValue(ANSWERED_CORRECTLY + " [" + answeredCorrectly + "]", answeredCorrectly);
        dataset.setValue(ANSWERED_INCORRECTLY + " [" + answeredIncorrectly + "]", answeredIncorrectly);

        // Build chart
        final JFreeChart chart = ChartFactory.createPieChart(
                String.format("%s %s's Quiz \"%s\" Results", user.getFirstName(), user.getLastName(), quiz.getTitle()),
                dataset);
        final PiePlot plot = (PiePlot) chart.getPlot();
        plot.setSectionPaint(NOT_ANSWERED + " [" + notAnswered + "]", BLUE);
        plot.setSectionPaint(ANSWERED_CORRECTLY + " [" + answeredCorrectly + "]", GREEN);
        plot.setSectionPaint(ANSWERED_INCORRECTLY + " [" + answeredIncorrectly + "]", RED);
        applyChartTheme(chart);
        return chart;
    }

    /**
     * Generates a quiz results chart.
     *
     * @param quizId Quiz ID
     * @return JFreeChart
     * @throws ResourceNotFoundException when quiz is not found
     */
    public JFreeChart getQuizResultsChart(final Long quizId) throws ResourceNotFoundException {
        final Quiz quiz = dataService.getQuiz(quizId);
        final List<Question> questions = getQuizQuestions(quiz);
        final Map<Long, Integer> questionIdMap = buildQuestionIdMap(questions);

        // Build dataset
        final int[][] questionData = new int[questions.size()][CommonConstants.THREE];
        final List<Long> quizParticipants = new ArrayList<>();
        // for (final Statistic statistic : getQuizQuestionAnsweredStatistics(quizId)) {
        // boolean correct = false;
        // final Matcher matcher =
        // QUESTION_ANSWERED_DESC_PATTERN.matcher(statistic.getDescription());
        // if (matcher.find()) {
        // log.info("getQuizCompletionChart() matching on " + matcher.group(3));
        // correct = Boolean.parseBoolean(matcher.group(3));
        // }
        // final int questionIndex = questionIdMap.get(statistic.getQuestionId());
        // if (correct) {
        // questionData[questionIndex][1] += 1;
        // } else {
        // questionData[questionIndex][2] += 1;
        // }
        // if (!quizParticipants.contains(statistic.getUserId())) {
        // quizParticipants.add(statistic.getUserId());
        // }
        // }
        final DefaultCategoryDataset dataset = new DefaultCategoryDataset();
        for (int i = 0; i < questionData.length; i++) {
            final int answered = questionData[i][1] + questionData[i][2];
            if (answered < quizParticipants.size()) {
                questionData[i][0] = quizParticipants.size() - answered;
            } else {
                questionData[i][0] = 0;
            }
            dataset.addValue(questionData[i][0], NOT_ANSWERED, QUESTION_NUM + (i + 1));
            dataset.addValue(questionData[i][1], ANSWERED_CORRECTLY, QUESTION_NUM + (i + 1));
            dataset.addValue(questionData[i][2], ANSWERED_INCORRECTLY, QUESTION_NUM + (i + 1));
        }

        // Build chart
        final JFreeChart chart = ChartFactory.createBarChart(
                String.format("Quiz \"%s\" Results", quiz.getTitle()),
                "Question",
                "# of Participants Answering",
                dataset);
        final CategoryPlot plot = (CategoryPlot) chart.getPlot();
        final BarRenderer renderer = (BarRenderer) plot.getRenderer();
        renderer.setSeriesPaint(0, BLUE);
        renderer.setSeriesPaint(1, GREEN);
        renderer.setSeriesPaint(2, RED);
        final CategoryAxis domainAxis = new CategoryAxis();
        domainAxis.setCategoryLabelPositions(CategoryLabelPositions.UP_45);
        plot.setDomainAxis(domainAxis);
        applyChartTheme(chart);
        return chart;
    }

    /**
     * Applies font theme to chart.
     *
     * @param chart JFreeChart
     */
    private static void applyChartTheme(final JFreeChart chart) {
        final StandardChartTheme chartTheme = (StandardChartTheme) org.jfree.chart.StandardChartTheme
                .createJFreeTheme();

        if (Locale.getDefault().getLanguage().equals(Locale.US.getLanguage())) {
            final Font oldExtraLargeFont = chartTheme.getExtraLargeFont();
            final Font oldLargeFont = chartTheme.getLargeFont();
            final Font oldRegularFont = chartTheme.getRegularFont();
            final Font oldSmallFont = chartTheme.getSmallFont();

            final Font extraLargeFont = new Font(
                    "Sans-serif",
                    oldExtraLargeFont.getStyle(),
                    oldExtraLargeFont.getSize());
            final Font largeFont = new Font("Sans-serif", oldLargeFont.getStyle(), oldLargeFont.getSize());
            final Font regularFont = new Font("Sans-serif", oldRegularFont.getStyle(), oldRegularFont.getSize());
            final Font smallFont = new Font("Sans-serif", oldSmallFont.getStyle(), oldSmallFont.getSize());

            chartTheme.setExtraLargeFont(extraLargeFont);
            chartTheme.setLargeFont(largeFont);
            chartTheme.setRegularFont(regularFont);
            chartTheme.setSmallFont(smallFont);
        }

        chartTheme.apply(chart);
    }

    /**
     * Gets Quiz Question(s) answered statistics.
     *
     * @param quizId Quiz ID
     * @return list of statistics
     * @throws ResourceNotFoundException when quiz statistics are not found
     */
    // private List<Statistic> getQuizQuestionAnsweredStatistics(final Long quizId)
    // throws ResourceNotFoundException {
    // final List<Statistic> statistics = statisticService.findByQuizId(quizId,
    // StatisticType.QUESTION_ANSWERED);
    // if (statistics == null) {
    // throw new ResourceNotFoundException("No statistics found for quiz");
    // }
    // return statistics;
    // }

    /**
     * Verifies input data as well as retrieves quiz question list.
     *
     * @param quiz Quiz
     * @return list of questions
     * @throws ResourceNotFoundException when quiz or quiz questions are not found
     */
    private static List<Question> getQuizQuestions(final Quiz quiz) throws ResourceNotFoundException {
        if (quiz == null) {
            throw new ResourceNotFoundException("Quiz not found");
        }
        final List<Question> questions = quiz.getQuestions();
        if (questions == null) {
            throw new ResourceNotFoundException("Quiz questions not found");
        }
        return questions;
    }

    /**
     * Builds a question id map.
     *
     * @param questions list of questions
     * @return Map
     */
    private static Map<Long, Integer> buildQuestionIdMap(final List<Question> questions) {
        final Map<Long, Integer> questionIdMap = new HashMap<>();
        int count = 0;
        for (final Question question : questions) {
            questionIdMap.put(question.getId(), count++);
        }
        return questionIdMap;
    }

}
