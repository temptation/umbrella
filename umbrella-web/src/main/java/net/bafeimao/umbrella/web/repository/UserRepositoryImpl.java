/*
 * Copyright 2002-2015 by bafeimao.net
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package net.bafeimao.umbrella.web.repository;

import net.bafeimao.umbrella.support.repository.hibernate.HibernateRepositoryImpl;
import net.bafeimao.umbrella.web.domain.User;

import org.springframework.stereotype.Repository;

@Repository
public class UserRepositoryImpl extends HibernateRepositoryImpl<User, Long> implements UserRepository {

	@Override
	public boolean checkExistenceByEmail(String email) {
		return count("where email=?", email) != 0;
	}

	@Override
	public boolean checkExistenceByName(String name) {
		return count("where name=?", name) != 0;
	}

}
