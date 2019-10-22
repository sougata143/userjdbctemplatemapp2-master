package com.example.userjdbctemplate;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;
import javax.swing.tree.RowMapper;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class EmployeeDao {

	@Autowired
	DataSource dataSource;
	
	@Transactional
	public void createTable() {
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		System.out.println("creating table");
		jdbcTemplate.execute("drop table employees if exists");
		jdbcTemplate.execute("create table employees(\"\r\n" + 
				"+ \"id varchar(25), first_name varchar(255), middle_name varchar(255), last_name varchar(255), deptId varchar(25))");
	}
	
	@Transactional
	public void insertData() {
		JdbcTemplate jt = new JdbcTemplate(dataSource);
		System.out.println("inserting data into table");
		String[] records = "E001 Dean Andrew Roberts D25; E002 Jeff Longman Dean D42; E003 Erin Nancy Kirkland D66;".split(";");

		for ( String singleRecord : records ) {
			String [] cols = singleRecord.split(" ");
			System.out.printf( " Adding employee record for %s %s %s %s %s\n",cols[0], cols[1], cols[2], cols[3], cols[4]);
			jt.update("INSERT INTO employees(id, first_name, middle_name, last_name, deptId) values(?,?,?,?,?)",cols[0], cols[1], cols[2], cols[3], cols[4]);
		}
	}
	
	@Transactional
	public void getEmployeeByFirstName(String name) {
		System.out.println ( " Querying for customer records where first_name = 'Erin':" );
		JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
		List<Employee> results = jdbcTemplate.query("select * from employees where first_name = ?",
				new Object[] { "Erin" }, new RowMapper<Employee>() {
		public Employee mapRow(ResultSet rs, int rowNum)
				throws SQLException {
				return new Employee(rs.getString("id"),
rs.getString("first_name"), rs.getString("middle_name"), 
rs.getString("last_name"), rs.getString("deptId"));
					}
				});

		for (Employee employee : results) {
			System.out.println(employee);
		}
	}
	
}
