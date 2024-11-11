package be.th.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import be.th.models.LessonType;

public class LessonTypeDAO extends DAO<LessonType>{

	public LessonTypeDAO(Connection conn) {
		super(conn);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean create(LessonType obj) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(int id) {
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
        String sql = """
    		SELECT 
  		  		lesson_type_id, 	
  		  		name, price, 
  		  		skill_level, 
  		  		age_category_name, 
  		  		min_age, 
  		  		max_age 
	  		FROM 
		 		lesson_types 
	 		ORDER BY 
      			lesson_type_id
		""";

        try (PreparedStatement stmt = super.connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                int lessonTypeId = rs.getInt("lesson_type_id");
                String name = rs.getString("name");
                double price = rs.getDouble("price");
                String skillLevel = rs.getString("skill_level");
                String ageCategoryName = rs.getString("age_category_name");
                int minAge = rs.getInt("min_age");
                int maxAgeDatabase = rs.getInt("max_age");
                Optional<Integer> maxAge = rs.wasNull() ? Optional.empty() : Optional.of(maxAgeDatabase);    
                
                LessonType lessonType = new LessonType(lessonTypeId, name, price, skillLevel, ageCategoryName, minAge, maxAge);
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
