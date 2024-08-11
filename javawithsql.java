import java.sql.*;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import com.mysql.cj.protocol.Resultset;

public class javawithsql {
    public static void main(String[] args){
    while(true){
        System.out.println("\n\n\t\t-----------------------------WELCOME TO GCT LIBRARY OF STORY BOOKS------------------------------------\n\n");
        transaction t = new transaction();
    Scanner sc= new Scanner(System.in);
    final String url = "jdbc:mysql://localhost:3306/librarymanagementsystem";
    final String user ="root";
    final String password = "Maha@1022";

    int tid;
    int ID;
    String ans;
    try{
        Connection con=DriverManager.getConnection(url, user, password);
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("Select * from members");
        int id=0;
        int k=0;
        String choice;
        System.out.print("Already a member?");
        ans=sc.nextLine();
        if(ans.equalsIgnoreCase("yes")){
            ID=sc.nextInt();
        while(rs.next()){
             if(rs.getInt(1)==ID){
                  System.out.println("WELCOME");
                 k++;
             }
        }
    }
        if(k==0||ans.equalsIgnoreCase("no")){
            System.out.println("WOULD YOU LIKE TO BE A MEMBER?");
            System.out.println("1.YES\n2.NO");
            choice=sc.nextLine();
            if(choice.equalsIgnoreCase("Yes")){
                Insert();
            }
            else{
                System.out.println("THANK YOU");
            }
        }
        else{
            System.out.print("1.Book return\n2.Book borrow\n3.Check details\nchoose any one(in numbers)");
            int ch=sc.nextInt();
            String s;
            if(ch==1){
                System.out.println("Enter your transaction id :");
                tid=sc.nextInt();
                 t.returnbooks(tid);
                 System.out.println("Do you want to borrow a book?\n1.yes\n2.no");
                 s=sc.nextLine();
                 if(s.equalsIgnoreCase("yes")){
                    showbooks();
                 }
                 else{
                    System.out.println("THANK YOU");
                 }

            }
            else if(ch==2){
                showbooks();
         }
         else{
            System.out.print("Enter your Transaction ID:");
            tid=sc.nextInt();
            t.checkdetails(tid);
         }
        }

    }
    catch(SQLException e){
        e.printStackTrace();
    }
}
    }
  static void Insert(){
    Random random = new Random();
    int ID=0;
    final String url = "jdbc:mysql://localhost:3306/librarymanagementsystem";
    final String user ="root";
    final String password = "Maha@1022";
    Scanner sc1 = new Scanner(System.in);
    System.out.print("Enter your name:");
    String name = sc1.nextLine();
    System.out.print("Enter your emailID:");
    String email = sc1.nextLine();
    System.out.print("Enter your Mobile no:");
    String phone = sc1.nextLine();
    try{
        Connection conn=DriverManager.getConnection(url, user, password);
        Statement st = conn.createStatement();
        ResultSet r=st.executeQuery("Select * from members;");
        Set<Integer> hsh = new HashSet<>();
        while(r.next()){
            hsh.add(r.getInt(1));
        }
        int k=0;
        while(k==0){
          ID=random.nextInt();
          if(!hsh.contains(ID))
              k++;
        }
        String insertquery = "INSERT INTO members (memid,name,email,phone) values(?,?,?,?);";
        PreparedStatement p = conn.prepareStatement(insertquery);
        p.setInt(1,ID);
        p.setString(2,name);
        p.setString(3,email);
        p.setString(4,phone);
        p.executeUpdate();

  }
  
  catch(SQLException e){
    e.printStackTrace();
  }
  String choice;
  System.out.println("Your ID number is:"+ID+"\nMembership Created successfully");
  System.out.println("Would you like to Borrow a book?\n1.Yes\n2.No");
  choice = sc1.nextLine();
  if(choice.equalsIgnoreCase("yes")){
    showbooks();
    
  }
  else{
    System.out.println("Thank you");
  }
}

static void showbooks(){
    Scanner sc2 = new Scanner(System.in);
    transaction trans = new transaction();
    final String url = "jdbc:mysql://localhost:3306/librarymanagementsystem";
    final String user ="root";
    final String password = "Maha@1022";
    try{
        Connection connection = DriverManager.getConnection(url, user, password);
        String query ="Select * from books where quantity>0";
        PreparedStatement p1 = connection.prepareStatement(query);
        ResultSet rs = p1.executeQuery();
        System.out.println("Books available for borrowing:");
        System.out.println("Book ID | Title | Author | Quantity");
        System.out.println("------------------------------------");
        while (rs.next()) {
            int bookID = rs.getInt("idbooks");
            String title = rs.getString("title");
            String author = rs.getString("author");
            int quantity = rs.getInt("quantity");

            System.out.printf("%7d | %s | %s | %d%n", bookID, title, author, quantity);
        }
        System.out.println("------------------------------------");

    }catch(SQLException e){
        e.printStackTrace();
    }
    System.out.print("Which book you you like to take?\nEnter the bookID:");
    int bookID=sc2.nextInt();
    System.out.print("Enter your membershipID:");
    int memID=sc2.nextInt();
    trans.borrow(bookID,memID);

}
}

class transaction{
    static void borrow(int bookID,int memID){
        Random random = new Random();
        final String url = "jdbc:mysql://localhost:3306/librarymanagementsystem";
        final String user ="root";
        final String password = "Maha@1022";
        int transid=0;
        try{
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement st = connection.createStatement();
            ResultSet r=st.executeQuery("Select * from transaction;");
            Set<Integer> hsh = new HashSet<>();
            while(r.next()){
                hsh.add(r.getInt(1));
            }
            int k=0;
            while(k==0){
              transid=random.nextInt();
              if(!hsh.contains(transid))
                  k++;
            }
            String query2="Insert into transaction (transaction_id,book_id,mem_id,borrow_date,return_date) values (?,?,?,?,?);";
            PreparedStatement p2 = connection.prepareStatement(query2);
            LocalDate today = LocalDate.now();
            LocalDate returnDate = today.plusDays(15);
            p2.setInt(1,transid);
            p2.setInt(2,bookID);
            p2.setInt(3,memID);
            p2.setDate(4,Date.valueOf(today));
            p2.setDate(5,Date.valueOf(returnDate));
            p2.executeUpdate();
            System.out.println("Your Transaction id is :"+transid);
            System.out.println("The book should be returned before or on "+returnDate);
            String query3 = "UPDATE books set quantity = quantity - 1 where idbooks =?;";
            PreparedStatement ps = connection.prepareStatement(query3);
            ps.setInt(1,bookID);
            ps.executeUpdate(); 


    }
    catch (SQLException e){
        e.printStackTrace();
    }
}

    public void returnbooks(int tid) {
        final String url = "jdbc:mysql://localhost:3306/librarymanagementsystem";
        final String user ="root";
        final String password = "Maha@1022";
        try{
            Connection conn= DriverManager.getConnection(url, user, password);
            String query4 = "Delete from transaction where transaction id=?;";
            PreparedStatement pr = conn.prepareStatement(query4);
            pr.setInt(1,tid);
            String query5 = "update books set quantity = quantity +1 where idbooks=?;";
            Statement sm = conn.createStatement();
            ResultSet rt = sm.executeQuery("Select * from transaction");
            int bid=0;
            while(rt.next()){
               if(rt.getInt(1)==tid){
                bid=rt.getInt(2);
               }
            }
            PreparedStatement pp = conn.prepareStatement(query5);
            pp.setInt(1,bid);
            pp.executeUpdate(); 
        }
        catch(SQLException e){
            e.printStackTrace();
        }
    }
    public void checkdetails(int tid){
        final String url = "jdbc:mysql://localhost:3306/librarymanagementsystem";
        final String user ="root";
        final String password = "Maha@1022";
        try{
            Connection connec= DriverManager.getConnection(url, user, password);
            String query6="Select * from transaction where transaction_id=? ;";
            PreparedStatement pst = connec.prepareStatement(query6);
            pst.setInt(1,tid);
            ResultSet rst = pst.executeQuery();
            System.out.println("Your transaction");
            System.out.println("Transaction ID | Book ID | Mem_ID | Borrow Date | Return Date");
            System.out.println("------------------------------------");
            while (rst.next()) {
                int transid=rst.getInt("transaction_id");
                int bookID = rst.getInt("book_id");
                int memID = rst.getInt("mem_id");
                Date rdate = rst.getDate("return_date");
                Date bdate = rst.getDate("borrow_date");
                System.out.println(transid+"     | "+bookID+" |    "+memID+" |    "+bdate+" |   "+rdate);
                
            }
            System.out.println("------------------------------------");

    }
    catch(SQLException e){
            e.printStackTrace();
    }

}
}

