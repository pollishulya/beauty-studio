package classes;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Shedule  implements Salon, Serializable {
    private List<Observer> observers = new ArrayList<Observer>();
    private static final long serialVersionUID = 6529685098267757690L;
    private int id = 0;
    private String master;
    private String client;
    private String service;
    private String date;
    private String time;
    private String type = "";
    private int idService = 0;
    private int idMaster = 0;

    public Shedule() {}

    public Shedule(int id, String service,String master, String date,String time) {
        this.id = id;
        this.service = service;
        this.master=master;
        this.date = date;
        this.time=time;
    }

    public Shedule( String service,String master, String date,String time) {
        this.service = service;
        this.master=master;
        this.date = date;
        this.time=time;
    }

    public Shedule(int id, String service,String master, String date,String time, String client) {
        this.id = id;
        this.service = service;
        this.master=master;
        this.date = date;
        this.time=time;
        this.client=client;
    }

    public Shedule(String service,String master, String date,String time, String client) {
        this.service = service;
        this.master=master;
        this.date = date;
        this.time=time;
        this.client=client;
    }
    public String getType() {
        return type;
    }
    public void setIdMaster(int idMaster) {
        this.idMaster = idMaster;
    }

    public int getId() {
        return id;
    }

    public String getMaster() {
        return master;
    }
    public String getClient() {
        return client;
    }

    public void setIdService(int idService) {
        this.idService = idService;
    }


    public String getService() {
        return service;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setService(String service) {
        this.service= service;
    }

    public void setMaster(String master) {
        this.master= master;
    }

    public void setDate(String date) {
        this.date= date;
    }

    public void setTime(String time) {
        this.time= time;
    }

    public Shedule(int id, String type) {
        this.id = id;
        this.type = type;
    }

    public Shedule(String type) {
        this.type = type;
    }

    public Shedule(int id, String service,String master, String date,String time, String client, String type) {
        this.id = id;
        this.master=master;
        this.client = client;
        this.service = service;
        this.date = date;
        this.time=time;
        this.type = type;
    }

public List<Object> getAllShedule(Connection con, Shedule shedule){
    List<Object> list = new ArrayList();
    try {
        Statement stmt = con.createStatement();
        ResultSet rs = stmt.executeQuery("select * from Shedule where clientName='Свободно' order by date,time,ServiceName ");
        while (rs.next()) {
            list.add(new Shedule(rs.getInt("IdShedule"),rs.getString("ServiceName"),
                    rs.getString("MasterName"),
                    rs.getString("Date"), rs.getString("Time")
                    /*,rs.getInt("Phone")*/));
        }
        System.out.println("shed ok");
        rs.close();
        stmt.close();

    }catch (SQLException e) {
        e.printStackTrace();
    }
    return list;
}

public List<Object> editShedule(Connection con, Shedule shedule) {
        List<Object> list = new ArrayList();
    System.out.println("Edit "+shedule.getId());
        try{
            System.out.println("ID "+shedule.getId());
            PreparedStatement st = con.prepareStatement("DELETE FROM Shedule WHERE IdShedule = " + shedule.getId() + ";");
            st.executeUpdate();
            st.close();
            Statement stmt = con.createStatement();

            ResultSet rs = stmt.executeQuery("select * from Service;");
            while (rs.next()) {
                if(shedule.getService().equals(rs.getString("Name"))){
                    shedule.setIdService(rs.getInt("IdService"));
                    break;
                }
            }

            String str_update = "insert into studio.shedule(IdShedule, ServiceName, MasterName,Date, Time,ClientName/*, IdMaster*/)" +
                    " values ('"+shedule.getId()+"','"+ shedule.getService() + "', '" + shedule.getMaster() + "', '" + shedule.getDate() + "', '" +
                    shedule.getTime()+ "', '" + shedule.getClient()+ "')";
            stmt.executeUpdate(str_update);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        list.add("true");
        return list;
    }

    public List<Object> removeShedule(Connection con, Shedule shedule) {
        try {
            PreparedStatement st = con.prepareStatement("DELETE FROM Shedule WHERE IdShedule = " + shedule.getId() + ";");
            st.executeUpdate();
            st.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        List<Object> list = new ArrayList();
        list.add("true");
        return list;
    }
    public List<Object> getDateService(Connection con){
        List<Object> list = new ArrayList();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Shedule;");
            while (rs.next()) {
                list.add(rs.getString("Date"));
            }
            rs.close();
            stmt.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Object> getAllServiceName(Connection con){
        List<Object> list = new ArrayList();
        try {
            Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("select distinct shedule.servicename from shedule;");
            while (rs.next()) {
                list.add(rs.getString("ServiceName"));
            }
            rs.close();
            stmt.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
    public List<Object> bookingShedule(Connection con, Shedule shedule) throws SQLException {

        List<Object> list = new ArrayList();
        try {
            List<Integer> id = new ArrayList();
            List<Integer> nameService = new ArrayList();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Shedule;");
            while (rs.next()) {
                id.add(rs.getInt("IdShedule"));
            }
            System.out.println("start get service");
            int count = id.get(id.size() - 1);
            int countUser = id.get(id.size() - 1);
            rs = stmt.executeQuery("select * from Service;");
            while (rs.next()) {
                System.out.println("serv 1"+shedule.getService());
                if (shedule.getService().equals(rs.getString("Name"))) {
                    System.out.println("serv2 "+shedule.getService());
                    shedule.setIdService(rs.getInt("IdService"));
                    System.out.println("serv "+shedule.getService());
                    break;
                }
                //else  shedule.setService("nope");
            }
//            count = id.get(id.size() - 1);
//            rs = stmt.executeQuery("select * from Master;");
//            while (rs.next()) {
//                if (shedule.getMaster().equals(rs.getString("MasterName"))) {
//                    shedule.setIdMaster(rs.getInt("IdMaster"));
//                    break;
//                }
//            }
            String str_update = "DELETE FROM Shedule WHERE  ServiceName='"+shedule.getService()+"'and MasterName='"+getMaster()+
           /* "',Date='"+shedule.getDate()+"',Time='"+shedule.getTime()*/"'";
            stmt.executeUpdate(str_update);
             str_update = "insert into Shedule(IdShedule, ServiceName, MasterName,Date, Time,ClientName/*, IdMaster*/) values (" +
                    (count + 1) + ",'" + shedule.getService() + "', '" + shedule.getMaster() + "', '" + shedule.getDate() + "', '" +
                    shedule.getTime()+"','"+shedule.getClient()+/*+ "', " + service.getIdMaster()*/ "')";
            System.out.println("select good");
            stmt.executeUpdate(str_update);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
        list.add("true");
        return list;

    }
    public List<Object> addShedule(Connection con, Shedule shedule) throws SQLException {

        List<Object> list = new ArrayList();
        try {
            List<Integer> id = new ArrayList();
            List<Integer> nameService = new ArrayList();
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select * from Shedule;");
            while (rs.next()) {
                id.add(rs.getInt("IdShedule"));
            }
            System.out.println("start get service");
            int count = id.get(id.size() - 1);
            int countUser = id.get(id.size() - 1);
            rs = stmt.executeQuery("select * from Service;");
            while (rs.next()) {
                System.out.println("serv 1"+shedule.getService());
                if (shedule.getService().equals(rs.getString("Name"))) {
                    System.out.println("serv2 "+shedule.getService());
                       shedule.setIdService(rs.getInt("IdService"));
                    System.out.println("serv "+shedule.getService());
                    break;
                }
            }

            String str_update = "insert into Shedule(IdShedule, ServiceName, MasterName,Date, Time,ClientName/*, IdMaster*/) values (" +
                    (count + 1) + ",'" + shedule.getService() + "', '" + shedule.getMaster() + "', '" + shedule.getDate() + "', '" +
                    shedule.getTime()+"','"+"Свободно" + "')";
                    System.out.println("select good");
            stmt.executeUpdate(str_update);
            stmt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            con.close();
        }
        list.add("true");
        return list;

    }
    public List<Object> getAllService(Connection con, Shedule service){
        List<Object> list = new ArrayList();
        try {
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery("select distinct shedule.servicename from shedule;");
            list.add(new Shedule(rs.getInt("IdShedule"),rs.getString("ServiceName"),
                    rs.getString("MasterName"),
                    rs.getString("Date"), rs.getString("Time")
                    /*,rs.getInt("Phone")*/));
            rs.close();
            stmt.close();

        }catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void notifyAllObservers(){
        for(Observer observer : observers){
            observer.update();
        }
    }

    public void attach(Observer observer){
        observers.add(observer);
    }
}

