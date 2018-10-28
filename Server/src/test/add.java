package test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.reader.dao.util.DBUtil;




public class add {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Connection conn=null;
		PreparedStatement ps=null;
		conn=DBUtil.getConnection();
		String sql="insert into userinfo (username,pswd,money,userload)values(?,?,?,?)";
		try {
			ps=conn.prepareStatement(sql);
			ps.setString(1,"guan");
			ps.setString(2,"1");
			ps.setInt(3,1001);
			ps.setBoolean(4,true);
		
			int result = ps.executeUpdate();
			System.out.println(result);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			DBUtil.close(conn, ps, null);
		}
		
	}

}
