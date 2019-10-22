/*
 * Copyright 2019 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.cassandra.cassandra;

import com.datastax.driver.core.utils.UUIDs;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.github.nosan.boot.test.autoconfigure.cassandra.DataCassandraTest;
import com.github.nosan.embedded.cassandra.spring.test.EmbeddedCassandra;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Tests for {@link CustomerRepository}.
 *
 * @author Dmytro Nosan
 */
@DataCassandraTest
@EmbeddedCassandra(scripts = "schema.cql")
class CustomerRepositoryTests {

	@Autowired
	private CustomerRepository repository;

	@BeforeEach
	void deleteAll() {
		this.repository.deleteAll();
	}

	@Test
	void testRepository() {
		Customer alice = new Customer(UUIDs.timeBased(), "Alice", "Smith");
		Customer bob = new Customer(UUIDs.timeBased(), "Bob", "Smith");
		Customer john = new Customer(UUIDs.timeBased(), "John", "Coffee");
		this.repository.save(alice);
		this.repository.save(bob);
		this.repository.save(john);
		assertThat(this.repository.findAll()).containsOnly(alice, bob, john);
		assertThat(this.repository.findByFirstName("Alice")).isEqualTo(alice);
		assertThat(this.repository.findByLastName("Smith")).containsOnly(alice, bob);
		assertThat(this.repository.findByLastName("Coffee")).containsOnly(john);
	}

}
