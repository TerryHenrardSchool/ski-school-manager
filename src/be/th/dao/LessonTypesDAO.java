package be.th.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import be.th.classes.LessonType;

public class LessonTypesDAO extends DAO<LessonType>{

	public LessonTypesDAO(Connection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean create(LessonType obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(LessonType obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean update(LessonType obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public LessonType find(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<LessonType> findAll() {
        List<LessonType> lessonTypes = new ArrayList<>();
        String sql = "SELECT lesson_type_id, name, price, skill_level, age_category_name FROM lesson_types";

        try (PreparedStatement stmt = super.connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int lessonTypeId = rs.getInt("lesson_type_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String skillLevel = rs.getString("skill_level");
                String ageCategoryName = rs.getString("age_category_name");
             
                LessonType lessonType = new LessonType(lessonTypeId, name, price, skillLevel, ageCategoryName);
                lessonTypes.add(lessonType);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lessonTypes;
    }

	@Override
	public List<LessonType> findAll(Map<String, Object> criteria) {
		// TODO Auto-generated method stub
		return null;
	}

}
