package com.javaex.phone;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PhoneDao {
	// 필드

	// 0. import java.sql.*;
	private Connection conn = null;
	private PreparedStatement pstmt = null;
	private ResultSet rs = null;

	private String driver = "oracle.jdbc.driver.OracleDriver";
	private String url = "jdbc:oracle:thin:@localhost:1521:xe";
	private String id = "phonedb";
	private String pw = "phonedb";

	// 생성자
	// 메소드 gs
	// 메소드 일반

	public void getConnection() {

		try {
			// 1. JDBC 드라이버 (Oracle) 로딩
			Class.forName(driver);

			// 2. Connection 얻어오기
			conn = DriverManager.getConnection(url, id, pw);

		} catch (ClassNotFoundException e) {
			System.out.println("error: 드라이버 로딩 실패 - " + e);
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}
	}

	public void close() {
		// 5. 자원정리
		try {
			if (rs != null) {
				rs.close();
			}
			if (pstmt != null) {
				pstmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

	}

	public void phoneInsert(PhoneVo phoneVo) {

		getConnection();

		try {

			// 3. SQL문 준비 / 바인딩 / 실행
			String query = "";
			query += " insert into person ";
			query += " values (seq_person_id.nextval, ?, ?, ?)";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, phoneVo.getName());
			pstmt.setString(2, phoneVo.getHp());
			pstmt.setString(3, phoneVo.getCompany());

			int count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
	}

	public List<PhoneVo> phoneSelect() {

		List<PhoneVo> phoneList = new ArrayList<PhoneVo>();

		getConnection();

		try {
			String query = "";
			query += " select person_id, ";
			query += "		  name, ";
			query += "		  hp, ";
			query += "		  company ";
			query += " from person ";

			pstmt = conn.prepareStatement(query);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int id = rs.getInt("person_id");
				String name = rs.getString("name");
				String hp = rs.getString("hp");
				String company = rs.getString("company");

				PhoneVo vo = new PhoneVo(id, name, hp, company);
				phoneList.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
		return phoneList;
	}

	public void phoneUpdate(PhoneVo phoneVo) {

		getConnection();

		try {
			String query = "";
			query += " update person ";
			query += " set name = ?, ";
			query += "	   hp = ?, ";
			query += " 	   company = ? ";
			query += " where person_id = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setString(1, phoneVo.getName());
			pstmt.setString(2, phoneVo.getHp());
			pstmt.setString(3, phoneVo.getCompany());
			pstmt.setInt(4, phoneVo.getPersonId());

			int count = pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
	}

	public void phoneDelete(int index) {

		getConnection();

		try {
			String query = "";
			query += " delete from person ";
			query += " where person_id = ? ";

			pstmt = conn.prepareStatement(query);

			pstmt.setInt(1, index);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			System.out.println("error:" + e);
		}

		close();
	}
}
