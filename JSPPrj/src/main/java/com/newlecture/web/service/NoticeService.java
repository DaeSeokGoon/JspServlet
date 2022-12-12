package com.newlecture.web.service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.newlecture.web.entity.Notice;
import com.newlecture.web.entity.NoticeView;

public class NoticeService {
	public int removeNoticeAll(int[] ids){
		
		return 0;
	}
	public int pubNoticeAll(int[] ids){
		return 0;
	}
	public int insertNotice(Notice notice){
		int result = 0;
		
	
		String sql = "INSERT INTO NOTICE (TITLE,CONTENT,WRITER_ID,PUB) VALUES (?,?,?,?)";
		
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(url,"scott","tiger");
			PreparedStatement st = con.prepareStatement(sql);
			
			st.setString(1, notice.getTitle());
			st.setString(2, notice.getContent());
			st.setString(3, notice.getWriterId());
			st.setBoolean(4, notice.getPub());
			
			result = st.executeUpdate();

				st.close();
				con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return result;
	}
	public int deleteNotice(int id){
		return 0;
	}
	public int updateNotice(Notice notice){
		return 0;
	}
	public List<Notice> getNoticeNewestList(){
		return null;
	}
	
	public List<NoticeView> getNoticeList(){
		
		return getNoticeList("title","",1);
	}
	public List<NoticeView> getNoticeList(int page){
		
		return getNoticeList("title","",page);
	}
	public List<NoticeView> getNoticeList(String field/*title,writer_id*/, String query/*A*/,int page){
		List<NoticeView> list = new ArrayList<>();
		
		String sql = "select * from ("
				+ "select rownum num, n.* "
				+ "from (select * from notice_view where "+field+" like ? order by regdate desc) n"
				+ ") "
				+ "where num between ? and ?";
		// 1, 11, 21, 31 -> an = 1+(page-1)*10
		// 10,20,30,40 - > page*10
		// 등차수열 사용 an = a1+(n-1)*d
		
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(url,"scott","tiger");
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, "%"+query+"%");
			st.setInt(2, 1+(page-1)*10);
			st.setInt(3, page*10);
			ResultSet rs = st.executeQuery();

			 while(rs.next()){
					int id = rs.getInt("ID");
					String title =	rs.getString("TITLE");
					Date regdate =	rs.getDate("REGDATE");
					String writerId =	rs.getString("WRITER_ID");
					String hit =	rs.getString("HIT");
					String files =	rs.getString("FILES");
					//String content =	rs.getString("CONTENT");
					int cmtCount = rs.getInt("CMT_COUNT");
					boolean pub = rs.getBoolean("PUB");
					
					NoticeView notice = new NoticeView(
							id,
							title,
							regdate,
							writerId,
							hit,
							files,
							pub,
							//content,
							cmtCount
							);
					list.add(notice);
			}
			 
			 	rs.close();
				st.close();
				con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return list;
	}
	
	public int getNoticeCount() {
		
		return getNoticeCount("title", "");
	}
	
	public int getNoticeCount(String field, String query) {
		
		int count = 0;
		
		
		String sql = "select count(id) count from ("
				+ "select rownum num, n.* "
				+ "from (select * from notice where "+field+" like ? order by regdate desc) n"
				+ ") "
				;
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(url,"scott","tiger");
			PreparedStatement st = con.prepareStatement(sql);
			st.setString(1, "%"+query+"%");
			
			ResultSet rs = st.executeQuery();
			
			if(rs.next())
			count = rs.getInt("count");
			 
			 	rs.close();
				st.close();
				con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		return count;
	}
	public Notice getNotice(int id) {
		Notice notice = null;
		
		
		String sql = "select * from notice where id=?";
		
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(url,"scott","tiger");
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, id);
			
			ResultSet rs = st.executeQuery();

			if(rs.next()){
					int nid = rs.getInt("ID");
					String title =	rs.getString("TITLE");
					Date regdate =	rs.getDate("REGDATE");
					String writerId =	rs.getString("WRITER_ID");
					String hit =	rs.getString("HIT");
					String files =	rs.getString("FILES");
					String content =	rs.getString("CONTENT");
					boolean pub = rs.getBoolean("PUB");
					
					notice = new Notice(
							nid,
							title,
							regdate,
							writerId,
							hit,
							files,
							content,
							pub
							);
			}
			 
			 	rs.close();
				st.close();
				con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return notice;
	}
	public Notice getNextNotice(int id) {
		Notice notice = null;
		
		String sql="select * from notice"
				+ "where id = ("
				+ "     select id from notice"
				+ "    where regdate > (select regdate from notice where id=?)"
				+ "    and rownum=1)";
		
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(url,"scott","tiger");
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, id);
			
			ResultSet rs = st.executeQuery();

			if(rs.next()){
					int nid = rs.getInt("ID");
					String title =	rs.getString("TITLE");
					Date regdate =	rs.getDate("REGDATE");
					String writerId =	rs.getString("WRITER_ID");
					String hit =	rs.getString("HIT");
					String files =	rs.getString("FILES");
					String content =	rs.getString("CONTENT");
					boolean pub = rs.getBoolean("PUB");
					
					notice = new Notice(
							nid,
							title,
							regdate,
							writerId,
							hit,
							files,
							content,
							pub
							);
			}
			 
			 	rs.close();
				st.close();
				con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return notice;
	}
	public Notice getPrevNotice(int id) {
		Notice notice = null;
		
		String sql = "select id from (select * from notice order by regdate desc)"
				+ "    where regdate < (select regdate from notice where id=?)"
				+ "and rownum=1;";
		
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(url,"scott","tiger");
			PreparedStatement st = con.prepareStatement(sql);
			st.setInt(1, id);
			
			ResultSet rs = st.executeQuery();

			if(rs.next()){
					int nid = rs.getInt("ID");
					String title =	rs.getString("TITLE");
					Date regdate =	rs.getDate("REGDATE");
					String writerId =	rs.getString("WRITER_ID");
					String hit =	rs.getString("HIT");
					String files =	rs.getString("FILES");
					String content =	rs.getString("CONTENT");
					
					boolean pub = rs.getBoolean("PUB");
					notice = new Notice(
							nid,
							title,
							regdate,
							writerId,
							hit,
							files,
							content,
							pub
							);
			}
			 
			 	rs.close();
				st.close();
				con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return notice;
	}
	public int deleteNoticeAll(int[] ids) {
		
		int result = 0;
		
		String params = "";
		
		for(int i=0; i<ids.length; i++) {
			params += ids[i];
		if(i < ids.length-1)
				params += ",";
		}
		String sql = "DELETE NOTICE WHERE ID IN ("+params+")";
		
		String url = "jdbc:oracle:thin:@localhost:1521:xe";
		try {
			Class.forName("oracle.jdbc.OracleDriver");
			Connection con = DriverManager.getConnection(url,"scott","tiger");
			Statement st = con.createStatement();
			
			result = st.executeUpdate(sql);

				st.close();
				con.close();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		return result;
	}
}
/*
create view notice_view as            
select n.id,n.title,n.writer_id,n.regdate,
n.hit ,n.files,count(c.id) cmt_count from notice n left join 
"COMMENT1" c on n.id = c.notice_id group by n.id,n.title,n.writer_id,n.regdate,
n.hit ,n.files;*/
//뷰 테이블생성