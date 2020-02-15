import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.io.*;
import java.util.Date;


public class preprocess {

    private static final String DATE_FORMAT = "yyyy-MM-dd hh:mm:ss";
    private static SimpleDateFormat sdfAmerica = new SimpleDateFormat(DATE_FORMAT);
    private static SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT);

    public static void main(String[] args) {
//        System.out.println(getrepo("INFO, 2017-03-23T11:12:36+00:00, ghtorrent-32 -- api_client.rb: Successful request. URL: https://api.github.com/repos/mdmahamodur2013/EcomAngularRawPhp/pulls?page=1&per_page=100, Remaining: 2348, Total: 62 ms\n"));
//        toAmerica("2017-03-22 11:42:24");
        try {
            Class.forName("org.postgresql.Driver");
            Connection con=DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres",
                    "abcdefg");
            PreparedStatement ps1=con.prepareStatement("CREATE TABLE IF NOT EXISTS schemma_bdmh_1.mytable (id varchar " +
                    "PRIMARY KEY,prof varchar,did varchar,nos varchar,tos varchar, pid " +
                    "varchar);");
            ps1.executeQuery();

        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
        }
////
//        try {
//            System.out.println("read");
//            BufferedReader br = new BufferedReader(new FileReader("/home/bansal/Downloads/a.txt"));
//            String line;
//            Class.forName("org.postgresql.Driver");
//            Connection con=DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres","postgres",
//                    "abcdefg");
//            PreparedStatement ps2= con.prepareStatement("INSERT INTO schemma_bda_1.mytable(\"log_lvl\",\"timestamp\"," +
//                    "\"dwnlder_id\",\"retrval_stage\", \"repo\", \"mssg\") VALUES (?, ?, ?, ?, ?, ?);");
//            int count= 0;
//            List<String> list;
//            while ((line = br.readLine()) != null) {
//                list = new ArrayList<>();
//                if(count>= 10000000){
//                    break;
//                }
//                if(count>= 9000000){
//                    list= getItems(line);
//                }
//                if(count%1000000== 0){
//                    System.out.println("whoops");
//                }
//                if (!list.isEmpty()) {
//                    ps2.setString(1, list.get(0));
//                    ps2.setString(2, list.get(1));
//                    ps2.setString(3, list.get(2));
//                    ps2.setString(4, list.get(3));
//                    ps2.setString(5, getrepo(list.get(4)));
//                    ps2.setString(6, list.get(4));
//
//                    ps2.addBatch();
//                }
//                count+= 1;
//            }
//            System.out.println(count);
//            ps2.executeBatch();
//        }catch (Exception e){
//            System.out.println(e);
//        }
    }

    public static String getrepo(String mssg) {
        if(!mssg.contains("api.github.com/repos/")){
            return mssg;
        }
        int in1=mssg.indexOf("/repos/");
        String mssg1=mssg.substring(in1+7);
//        System.out.println(mssg1);
        int in2=mssg1.indexOf("/", mssg1.indexOf('/')+1);
        int in3=mssg1.indexOf("?",1);
        int in4=mssg1.indexOf(",",1);
        if(in2== -1){
            in2= 1000000;
        }
        if(in3== -1){
            in3= 1000000;
        }
        if(in4== -1){
            in4= 1000000;
        }

        String ret= mssg1.substring(0,Math.min(Math.min(in2, in3),in4));
        return ret;
    }

    public static void toAmerica(String datetime){
        try {
            TimeZone tzInAmerica = TimeZone.getTimeZone("America/New_York");
            sdfAmerica.setTimeZone(tzInAmerica);
            Date date = formatter.parse(datetime);
            String sDateInAmerica = sdfAmerica.format(date);
            System.out.println(sDateInAmerica);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public static List<String> getItems(String s){
        try {
            List<String> list = new ArrayList<>();
            int index = s.indexOf(',', s.indexOf(',') + 1);
            String[] parts = s.substring(0, index + 1).split(",");
            list.add(parts[0]);
            String time= parts[1].replace('T', ' ');
            TimeZone tzInAmerica = TimeZone.getTimeZone("America/New_York");
            sdfAmerica.setTimeZone(tzInAmerica);
            int index5= time.indexOf("+");
            time= time.substring(0, index5);
            Date date= formatter.parse(time);
            String sDateInAmerica = sdfAmerica.format(date);
//            System.out.println(sDateInAmerica);
            list.add(sDateInAmerica);
            int index2 = s.substring(index + 1, s.length() - 1).indexOf(":") + index + 1;
            int index3 = s.substring(index + 1, s.length() - 1).indexOf("--") + 3 + index;
            list.add(s.substring(index + 1, index3 - 3));
            list.add(s.substring(index3, index2));
            String s2 = s.substring(index2+1, s.length()-1);//message part of the string
            list.add(s.substring(index2+1));
            if(list.size()< 5 || list.get(0).equals("") || list.get(1).equals("") || list.get(2).equals("") || list.get(3).equals(
                    "") || list.get(4).equals("")){
                return new ArrayList<>();
            }
//            System.out.println(list);
            return list;
        }catch (Exception e){
//            System.out.println(e);
            return new ArrayList<>();
        }
    }

}