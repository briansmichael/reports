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

import com.starfireaviation.common.model.Quiz;
import com.starfireaviation.common.model.User;

public class DataService {

    /**
     * Gets a User by username.
     *
     * @param name user name
     * @return User
     */
    public User getUser(final String name) {
        // TODO call GET https://users.starfireaviation.com/api/users?username={name}
        final Long userId = null;
        if (userId != null) {
            return getUser(userId);
        }
        return null;
    }

    /**
     * Gets a User by ID.
     *
     * @param userId user ID
     * @return User
     */
    public User getUser(final Long userId) {
        // TODO call GET https://users.starfireaviation.com/api/users/{userId}
        return null;
    }

    /**
     * Gets a quiz.
     *
     * @param quizId Quiz ID
     * @return Quiz
     */
    public Quiz getQuiz(final Long quizId) {
        return null;
    }
}
