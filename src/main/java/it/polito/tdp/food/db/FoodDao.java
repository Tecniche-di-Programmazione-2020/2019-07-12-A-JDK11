package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.food.model.Arco;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public Map<Integer,Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			Map<Integer,Food> list = new HashMap<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.put(res.getInt("food_code"),new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM `portion`" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Food> listFoodbyPortion(int n, Map<Integer,Food> alimenti){
		String sql = "SELECT p.food_code , COUNT(p.portion_id) AS porzioni\r\n" + 
				"FROM `portion` AS p\r\n" + 
				"GROUP BY p.food_code\r\n" + 
				"HAVING porzioni<=?" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, n);
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(alimenti.get(res.getInt("food_code")));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}

	public List<Arco> listArchi(Map<Integer,Food> alimenti){
		String sql = "SELECT f1.food_code AS food1, f2.food_code AS food2\r\n" + 
				"FROM food_condiment AS f1 ,food_condiment AS f2\r\n" + 
				"WHERE f1.condiment_code=f2.condiment_code AND f1.food_code>f2.food_code " ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Arco> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Arco(alimenti.get(res.getInt("food1")), alimenti.get(res.getInt("food2")) ));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}

	public double pesoArco(Food alimento1,Food alimento2){
		String sql = "SELECT f1.food_code , f2.food_code , f1.condiment_code, AVG(c.condiment_calories) AS peso\r\n" + 
				"FROM food_condiment AS f1 ,food_condiment AS f2, condiment AS c\r\n" + 
				"WHERE f1.condiment_code=f2.condiment_code  AND c.condiment_code=f1.condiment_code AND f1.food_code=? AND f2.food_code=? \r\n" + 
				"GROUP BY f1.food_code , f2.food_code" ;
		double peso=0;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			st.setInt(1, alimento1.getFood_code());
			st.setInt(2, alimento2.getFood_code());
			
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					peso= res.getDouble("peso");
					System.out.println("peso:"+peso);
					
					
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return peso ;

		} catch (SQLException e) {
			e.printStackTrace();
			return 0 ;
		}

	}
}


