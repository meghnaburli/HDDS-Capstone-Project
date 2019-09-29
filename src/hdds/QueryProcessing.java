/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hdds;


import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

/**
 *
 * @author Harsh
 */
public class QueryProcessing {

    /**
     * @param args the command line arguments
     */
     /*
    public static void main(String[] args) {
    //    QueryProcessing obj=new QueryProcessing();
//         new recqueryarray().start();
// String err[]= obj.query_truncate("truncate  table data");
   //  String err[]=obj.query_create("create table harsh(age int)");
   //   if(err[0].equalsIgnoreCase("true"))
  //  System.out.println(" error in execution from main" + err[1]);
 //obj.query_update(" update      data         set      price = 10 where price=102");
 // String err[]= obj.query_join("select a.price,a.id,b.price from data as a,harsh as b where a.price=b.price");

   //   if(err[0].equalsIgnoreCase("true"))
   // System.out.println(" error in execution from main" + err[1]);
    }
*/


    public String[] query_select(String query)
    {   String err[]={"false",""};
    try
    {

    String type="org";
    boolean fondinlocal=false;
    String tab=null;
    String database=null;
    String username=null;
    String password=null;
    String queryontable=null;
    File result=new File(System.getProperty("user.dir")+"/temp//result.txt");
    if(result.exists())
    result.delete();
    result.createNewFile();
    if (query.contains(":"))
    type=query.split("from")[1].split(":")[0];
    if(query.contains(":"))
    queryontable=query.split("from")[1].split(":")[1];
    else if(query.contains("where") && query.contains(":"))
    queryontable=query.split("from")[1].split(":")[1].split("where")[0];
    else if(query.contains("where"))
    queryontable=query.split("from")[1].split("where")[0];
    else if(query.contains("group"))
    queryontable=query.split("from")[1].split("group")[0];
    else
        queryontable=query.split("from")[1];

    System.out.println("queryontable "  + queryontable);
    if(query.contains("local:"))
    {
        String temp[]=query.split("local:");
        query="";
        for(int i=0;i<temp.length;i++)
        query+=temp[i]+" ";
    }
    if(query.contains("loc:"))
    {
        String temp[]=query.split("loc:");
        query="";
        for(int i=0;i<temp.length;i++)
        query+=temp[i]+" ";
    }
    if(query.contains("org:"))
    {
        String temp[]=query.split("org:");
        query="";
        for(int i=0;i<temp.length;i++)
        query+=temp[i]+" ";
    }

System.out.println(" finalquery " + query + "type " + new Systemfunction().tableexist(queryontable.trim()));
                        if( new Systemfunction().tableexist(queryontable.trim()))
                        {if ((type.trim().equalsIgnoreCase("local")  || type.trim().equalsIgnoreCase("loc")))
                        {
                            //find if local instance there n if there execute it n show the result
                            BufferedReader cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//cnffile.txt")));
                            String read=cin.readLine();
                            while(read!=null)
                            {
                            tab=read.split("-")[1].split(",")[0];
                            //System.out.println("table " + tab + " database " + database);
                                 database=read.split("-")[1].split(",")[1].split(";")[0];
                                 read=cin.readLine();

                                 username=read.split("-")[1].split(",")[0];
                                 if(username==null || username.equalsIgnoreCase(""))
                                 username="null";
                                 password=read.split("-")[1].split(",")[1];//.split(";")[0];
                                 if(!password.equalsIgnoreCase(";"))
                                 password=password.split(";")[0];
                                 else
                                 password="null";

                                 //System.out.println("username " + username + " password" + password);
                                 read=cin.readLine();
                                 String ipread=cin.readLine();
                                 read=cin.readLine();
                                 String prim=read.split(":")[0];
                                   if(tab.trim().equalsIgnoreCase(queryontable.trim()) /*&& !prim.equalsIgnoreCase("r")*/)
                                    {
                                        String ipa=ipread.split(":")[1].split(";")[0];
                                        System.out.println("localip " + ipa);

                                        if(ipa.equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress()))
                                        {
                                            String  queryformat[]=new String [5];
                                                    queryformat[0]=database;
                                                    queryformat[1]=username;
                                                    queryformat[2]=password;
                                                    queryformat[3]=query;
                                                    queryformat[4]="select";
                                                    Systemfunction obj=new Systemfunction();
                                                    String er[]=obj.qexecaray(queryformat);
                                                    //execute function from
                                                    if(er[0].equalsIgnoreCase("false"))
                                                    {
                                                    //successful execution
                                                    obj.appendfile( System.getProperty("user.dir")+"/temp//retemp.txt",result.getAbsolutePath(), true);
                                                    }
                                                    else
                                                        {
                                                        //unsuccessful execution
                                                        //show dialog box with error from er[1];
                                                        System.out.println("error local" + er[1]);
                                                        err[0]="true";
                                                        err[1]=er[1];
                                                        break;
                                                        }
                                        fondinlocal=true;
                                        break;
                                        }
                                    }
                                 
                                 
                                 read=cin.readLine();
                            
                            }//end of while
                            cin.close();
                        }
                                if(!type.trim().equalsIgnoreCase("org") && !type.trim().equalsIgnoreCase("local") && !type.trim().equalsIgnoreCase("loc"))
                                {
                                //System.out.println("error in query near :" +queryontable);
                                err[0]="true";
                                err[1]="Error in query near :" +queryontable;
                                }else{
                        if (!fondinlocal) //check if localy found
                        {
                        System.out.println("fondinlocal " + fondinlocal);
                        //now find the global one could be more then 1
                            BufferedReader cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//cnffile.txt")));
                            String read=cin.readLine();
                            while(read!=null)
                            {
                                 tab=read.split("-")[1].split(",")[0];
                                 database=read.split("-")[1].split(",")[1].split(";")[0];
                                 read=cin.readLine();
                                 username=read.split("-")[1].split(",")[0];
                                 if(username==null || username.equalsIgnoreCase(""))
                                 username="null";
                                 password=read.split("-")[1].split(",")[1];//.split(";")[0];
                                 if(!password.equalsIgnoreCase(";"))
                                 password=password.split(";")[0];
                                 else
                                 password="null";

//                                 System.out.println("username " + username + " password" + password);
                                 read=cin.readLine();
                                 read=cin.readLine();
                                 String ipa=read.split(":")[1].split(";")[0];
                                 read=cin.readLine();
                                 String prim=read.split(":")[0];
                                 //System.out.println("table " + tab + " database " + database + " prim " + prim);
                                // System.out.println(tab.equalsIgnoreCase(queryontable.trim()) + " sad " + queryontable.trim() );

                                 if(tab.trim().equalsIgnoreCase(queryontable.trim()) && prim.equalsIgnoreCase("p"))
                                 {

                                
                                
                                 //connect n transfer the query to the ip

                                System.out.println("ip" + ipa);
                                Socket skts = new Socket(ipa,5559);
                               	DataInputStream ins=new DataInputStream(skts.getInputStream());
                                DataOutputStream outs=new DataOutputStream(skts.getOutputStream());

                                                if (skts.isConnected()) //got connected to remote host then cont
                                                {   String queryformat[]=new String [5];
                                                    queryformat[0]=database;
                                                    queryformat[1]=username;
                                                    queryformat[2]=password;
                                                    queryformat[3]=query;
                                                    queryformat[4]="select";
                                                  //now transfer the array
                                                    //Systemfunction obj=new Systemfunction();
                                                    //String er[]=obj.qexecaray(queryformat);


                                                     byte [] buff=new byte[512];
                                                    int reads=0;
                                                    outs.writeInt(queryformat.length);
                                                    for(int i=0;i<queryformat.length;i++)
                                                    {
                                                    outs.write(queryformat[i].getBytes(),0,queryformat[i].getBytes().length);
                                                    reads=ins.read(buff, 0, (buff.length));
                                                    }
                                                    reads=ins.read(buff, 0, (buff.length));
                                                    System.out.println(" execution by remote");
                                                            if(new String(buff,0,reads).equalsIgnoreCase("success"))
                                                            {
                                                                //continue successful transmision of error
                                                                //wait for the reply
                                                                //timer needed over here
                                                            System.out.println(" received success from remote");
                                                            DataInputStream in=null;
                                                            DataOutputStream out=null;
                                                            Socket sktt = null;
                                                            ServerSocket srvrt = null; //transfer
                                                            FileOutputStream ffo=null;
                                                            try{
                                                            srvrt =new ServerSocket(5554);
                                                            sktt =srvrt.accept();
                                                            in=new DataInputStream(sktt.getInputStream());
                                                            out=new DataOutputStream(sktt.getOutputStream());
                                                            ffo=new FileOutputStream(System.getProperty("user.dir")+"/temp//temp.txt");
                                                            int length=in.readInt();//read length;
                                                            int readl=0;
                                                            //System.out.println("read length" + length);
                                                            byte bufs []=new byte[1024*6];
                                                            while(readl<length)
                                                            {
                                                            reads=in.read(bufs,0,bufs.length);
                                                            if(reads==-1){break;}
                                                            ffo.write(bufs,0,reads);
                                                            readl+=reads;
                                                            //System.out.println(" read " + reads + " r " + read +  "  "+(read<=length));
                                                            out.write("delay".getBytes(), 0,"delay".getBytes().length);
                                                            out.flush();
                                                            }
                                                            out.write("success".getBytes(), 0,"success".getBytes().length);
                                                            out.flush();
                                                            ffo.close();
                                                            in.close();
                                                            out.close();
                                                            sktt.close();
                                                            srvrt.close();
                                                            ffo=null;
                                                            }catch(Exception e){e.printStackTrace();
                                                            out.write("abort".getBytes(), 0,"abort".getBytes().length);
                                                            out.flush();
                                                            ffo.close();
                                                            in.close();
                                                            out.close();
                                                            sktt.close();
                                                            srvrt.close();
                                                            ffo=null;

                                                            }
                                                            Systemfunction obj=new Systemfunction();
                                                            obj.appendfile( System.getProperty("user.dir")+"/temp//temp.txt",result.getAbsolutePath(), true);
                                                            System.out.println("appended once");
                                                            }
                                                            else if(new String(buff,0,reads).equalsIgnoreCase("abort"))
                                                            {//stop execution throw n error mesage
                                                            //get the error from the remote
                                                            outs.write("delay".getBytes(), 0,"delay".getBytes().length);
                                                            reads=ins.read(buff, 0, (buff.length));
                                        //
                                                            System.out.println(" received abort from remote");
                                                            System.out.println(" error masseage" + new String(buff,0,reads));
                                                            err[0]="true";err[1]=new String(buff,0,reads);
                                                            break;
                                                            }
                                                }
                                                        else
                                                        {
                                                            // could not get connected stop execution
                                                            System.out.println("  transfer error");
                                                            err[0]="true";err[1]="transfer err";
                                                            break;
                                                        }
                                }
                                 read=cin.readLine();
                            }
                            //over here take the result from the result.txt file n show it to the user in the textarea 
                                 cin.close();
                        }
                                }
                                }else   {
                                //System.out.println("error table doesnt exist");
                                err[0]="true";
                                err[1]="Error table doesnt exist";
                                }
                                
    }catch(Exception e){e.printStackTrace();err[0]="true";err[1]=e.getMessage();}
    return err;
    }

    public String[] query_drop(String query)
    {   String err[]={"false",""};
        try{
            boolean onlyfrag=false;
            String tab=null;
            String database=null;
            String username=null;
            String password=null;
            boolean queryok=true;
            if(query.contains("frag:")) //a query to drop all fragments
            {query="drop table " + query.split("frag:")[1];
             onlyfrag=true;
            }else if (query.contains(":"))
                {
                System.out.println("error in query");
                queryok=false;
                }

                            String queryontable=query.split("table")[1];
                            System.out.println("queryontable " + queryontable);
                            queryontable=queryontable.trim();
                            if(new Systemfunction().tableexist(queryontable))
                            {if(queryok)
                            {
                                //if(table_count(queryontable)==1)
                                //{
                                //table has 1 instance could be local or remote
                                int u=0;
                                String listiphavingtable[]=new String[table_count(queryontable)];
                                BufferedReader cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//cnffile.txt")));
                                String read=cin.readLine();

                                while(read!=null)
                                {
                                     tab=read.split("-")[1].split(",")[0];
                                     //database=read.split("-")[1].split(",")[1].split(";")[0];
                                     read=cin.readLine();
                                     //username=read.split("-")[1].split(",")[0];
                                     //if(username==null)
                                     //username="";
                                     //password=read.split("-")[1].split(",")[1];//.split(";")[0];
                                     //if(!password.equalsIgnoreCase(";"))
                                     //password=password.split(";")[0];
                                     //else
                                     //password="";
                                     read=cin.readLine();
                                     read=cin.readLine();
                                     String ip=read.split(":")[1].split(";")[0];
                                     read=cin.readLine();
                                     String prim=read.split(":")[0];
                                     boolean cont=false;
                                     if(onlyfrag)
                                     cont=prim.equalsIgnoreCase("f") && tab.equalsIgnoreCase(queryontable);
                                     else
                                     cont=tab.equalsIgnoreCase(queryontable) ;
                                     if(cont)
                                     {
                                         listiphavingtable[u++]=ip;
                                     }
                                     read=cin.readLine();
                                }//end off while
                                cin.close();
                                comm up=new comm("up",listiphavingtable);
                                String errup[]=up.run2();
                                //String errup[]={"true",""};
                                if(errup[0].equalsIgnoreCase("true"))
                                {
                                  System.out.println("all ips are up");
                     
                                //now finding n executing
                                String ipblocked[]=new String[u];
                                System.out.println("  length of u " + u);
                                u=0;
                                cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//cnffile.txt")));
                                read=cin.readLine();
                                int error=0;
                                boolean execonce=false;
                                while(read!=null)
                                {
                                     tab=read.split("-")[1].split(",")[0];
                                     database=read.split("-")[1].split(",")[1].split(";")[0];
                                     read=cin.readLine();
                                     username=read.split("-")[1].split(",")[0];
                                     if(username==null || username.equalsIgnoreCase(""))
                                     username="null";
                                     password=read.split("-")[1].split(",")[1];//.split(";")[0];
                                     if(!password.equalsIgnoreCase(";"))
                                     password=password.split(";")[0];
                                     else
                                     password="null";
                                     read=cin.readLine();
                                     read=cin.readLine();
                                     String ip=read.split(":")[1].split(";")[0];
                                     read=cin.readLine();
                                     String prim=read.split(":")[0];
                                     boolean cont=false;
                                     if(onlyfrag)
                                     cont=prim.equalsIgnoreCase("f") && tab.equalsIgnoreCase(queryontable);
                                     else
                                     cont=tab.equalsIgnoreCase(queryontable);
                                     System.out.println("cont " + cont + " comp " + tab.equalsIgnoreCase(queryontable));

                                     if(cont)
                                     {              execonce=true;
                                                    String queryformat[]=new String [5];
                                                    queryformat[0]=database;
                                                    queryformat[1]=username;
                                                    queryformat[2]=password;
                                                    queryformat[3]=query;
                                                    queryformat[4]="drop";
                                                    ipblocked[u++]=ip;
                                        if(ip.equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress()))
                                        {
                                            System.out.println("same ip");
                                                    Systemfunction obj=new Systemfunction();
                                                    err=obj.qexecaray(queryformat);
                                        }
                                        else{
                                        Socket skts = new Socket(ip,5559);
                                   	DataInputStream ins=new DataInputStream(skts.getInputStream());
                                        DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                               System.out.println("connected n transmititng");
                                                    if (skts.isConnected()) //got connected to remote host then cont
                                                {   
                                                    
                                                     byte [] buff=new byte[512];
                                                    int reads=0;
                                                    outs.writeInt(queryformat.length);
                                                    for(int i=0;i<queryformat.length;i++)
                                                    {
                                                    outs.write(queryformat[i].getBytes(),0,queryformat[i].getBytes().length);
                                                    reads=ins.read(buff, 0, (buff.length));
                                                    }
                                                    reads=ins.read(buff, 0, (buff.length));
                                                    System.out.println(" execution by remote");

                                                                if(new String(buff,0,reads).equalsIgnoreCase("success"))
                                                                {
                                                                    System.out.println(" received success from host");

                                                                }else if(new String(buff,0,reads).equalsIgnoreCase("abort"))
                                                                        {
                                                                        System.out.println(" received abort from remote");
                                                                        err[0]="true";
                                                                        
                                                                         outs.write("delay".getBytes(), 0,"delay".getBytes().length);
                                                                        reads=ins.read(buff, 0, (buff.length));

                                                                        err[1]=new String(buff,0,reads);
                                                                        System.out.println(" error masseage read from remote" + new String(buff,0,reads));

                                                                        error=1;
                                                                        break;
                                                                        }else
                                                                            {System.out.println("transfer error ");
                                                                            error=1;
                                                                            err[0]="true";
                                                                            err[1]="Transfer error";
                                                                             break;
                                                                            }
                                                }   else
                                                        {error=1;
                                                         err[0]="true";
                                                         err[1]="Remote Host not on network";
                                                         System.out.println(" couldnt get connected to the host.make sure ports open");
                                                        }
                                     }
                                    
                                } read=cin.readLine();
                                //System.out.println(" read " + read);
                                }//end of while
                                cin.close();
                               //removing of table from cnffile n sending cnffile to all left
                               if(execonce)
                               {
                               if(error!=1 && err[0].equalsIgnoreCase("false"))
                               {
                               
                               Systemfunction obj=new Systemfunction();
                               for(int i=0;i<ipblocked.length;i++)
                               {
                               obj.deltablefromcnf(queryontable, ipblocked[i],onlyfrag);
                               }
                               File iplistf=new File(System.getProperty("user.dir")+"/system//iplist.txt");
                               BufferedReader win =new BufferedReader(new FileReader(iplistf));
                               String reada=win.readLine();
                                 while(reada!=null)
                               {

                               if(reada.equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress()))
                               reada=win.readLine();
                               if(reada!=null)
                               {
                                   File cnffile=new File(System.getProperty("user.dir")+"/system//cnffile.txt");
                               initiatefiletransfer cn=new initiatefiletransfer(reada);
                               cn.setPriority(Thread.MAX_PRIORITY);
                               cn.run2(cnffile);
                               }reada=win.readLine();
                               }
                               win.close();
                                   }
                               }else
                               


                                    {err[0]="true";
                                    err[1]="Fragments of Table" + queryontable+" does not exist";
                                    }
                                 }else
                                    {
                                    err[0]="true";
                                    err[1]="One or more host with the table " + queryontable +" is not on the network right now";
                                    }//end of all ips up ,cant implement 2phase roll back

                            }else
                                {System.out.println("query not ok");
                                err[0]="true";
                                err[1]="Syntax Wrong for truncate";
                                }//end of queryok

                            }else
                                 {  err[0]="true";
                                    err[1]="Table does not exist";
                                 }



           }catch(Exception e){e.printStackTrace();err[0]="true";err[1]=e.getMessage();}


           return err;
    }

     public String[] query_truncate(String query)
    {   String err[]={"false",""};
        try{
            boolean onlyfrag=false;
            String tab=null;
            String database=null;
            String username=null;
            String password=null;
            boolean queryok=true;
            if(query.contains("frag:")) //a query to drop all fragments
            {query="truncate table " + query.split("frag:")[1];
             onlyfrag=true;
            }else if (query.contains(":"))
                {
                System.out.println("error in query");
                queryok=false;
                }

                            String queryontable=query.split("table")[1];
                            System.out.println("queryontable " + queryontable + " frag " + onlyfrag);
                            queryontable=queryontable.trim();
                            if(new Systemfunction().tableexist(queryontable))
                            {if(queryok)
                            {
                                //if(table_count(queryontable)==1)
                                //{
                                //table has 1 instance could be local or remote
                                int u=0;
                                int length=table_count(queryontable);
                                String listiphavingtable[]=new String[length];
                                String usernam[]=new String[length];
                                String pass[]=new String[length];
                                String datab[]=new String[length];
                                BufferedReader cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//cnffile.txt")));
                                String read=cin.readLine();
                                boolean execonce=false;
                                while(read!=null)
                                {
                                     tab=read.split("-")[1].split(",")[0];
                                     database=read.split("-")[1].split(",")[1].split(";")[0];
                                     read=cin.readLine();
                                     
                                     username=read.split("-")[1].split(",")[0];
                                     if(username==null || username.equalsIgnoreCase(""))
                                     username="null";
                                     password=read.split("-")[1].split(",")[1];//.split(";")[0];
                                     if(!password.equalsIgnoreCase(";"))
                                     password=password.split(";")[0];
                                     else
                                     password="null";
                                     read=cin.readLine();
                                     read=cin.readLine();
                                     String ip=read.split(":")[1].split(";")[0];
                                     read=cin.readLine();
                                     String prim=read.split(":")[0];
                                     boolean cont=false;
                                     if(onlyfrag)
                                     cont=prim.equalsIgnoreCase("f") && tab.equalsIgnoreCase(queryontable);
                                     else
                                     cont=tab.equalsIgnoreCase(queryontable);
                                     if(cont)
                                     {   execonce=true;
                                         listiphavingtable[u]=ip;
                                         usernam[u]=username;
                                         pass[u]=password;
                                         datab[u]=database;
                                         u++;
                                     }
                                     
                                     read=cin.readLine();
                                }//end off while
                                cin.close();
                                comm2 up=new comm2("up",listiphavingtable);
                                String errup[]=up.run2();
                                up.setPriority(Thread.MAX_PRIORITY);
                                //String errup[]={"true",""};
                                if(errup[0].equalsIgnoreCase("true"))
                                {
                                    System.out.println("query " + query);

                                System.out.println("all ips are up");
                              //now finding n executing
                                String ipblocked[]=new String[table_count(queryontable)];
                                u=0;
                                int error=0;
                                if(execonce)
                                {for(int h=0;h<listiphavingtable.length && listiphavingtable[h]!=null;h++)
                                {            System.out.println("listiphavingtable "+ listiphavingtable[h]);
                                                    String queryformat[]=new String [5];
                                                    queryformat[0]=datab[h];
                                                    queryformat[1]=usernam[h];
                                                    queryformat[2]=pass[h];
                                                    queryformat[3]=query;
                                                    queryformat[4]="truncate";
                                                    ipblocked[u++]=listiphavingtable[h];
                                                     for(int i=0;i<queryformat.length;i++)
                                                    {System.out.println("queryformat " + queryformat[i]);
                                                     }
                                                    Socket skts = new Socket(listiphavingtable[h],5559);
                                                    DataInputStream ins=new DataInputStream(skts.getInputStream());
                                                    DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                                    System.out.println("connected n transmititng  "+listiphavingtable[h]);
                                                    if (skts.isConnected()) //got connected to remote host then cont
                                                    {

                                                    byte [] buff=new byte[512];
                                                    int reads=0;
                                                    outs.writeInt(queryformat.length);
                                                    for(int i=0;i<queryformat.length;i++)
                                                    {
                                                    outs.write(queryformat[i].getBytes(),0,queryformat[i].getBytes().length);
                                                    reads=ins.read(buff, 0, (buff.length));
                                                    }
                                                    reads=ins.read(buff, 0, (buff.length));
                                                    System.out.println(" execution by remote");

                                                                if(new String(buff,0,reads).equalsIgnoreCase("success"))
                                                                {
                                                                    System.out.println(" received success from host");

                                                                }else if(new String(buff,0,reads).equalsIgnoreCase("abort"))
                                                                        {
                                                                        System.out.println(" received abort from remote");
                                                                        err[0]="true";

                                                                            outs.write("delay".getBytes(), 0,"delay".getBytes().length);
                                                                           reads=ins.read(buff, 0, (buff.length));

                                                                        err[1]=new String(buff,0,reads);
                                                                        System.out.println(" error masseage received from remote" + new String(buff,0,reads));
                                                                        
                                                                        error=1;
                                                                        break;
                                                                        }else
                                                                            {System.out.println("transfer error ");
                                                                            error=1;
                                                                             break;
                                                                            }




                                                        }   else
                                                        {   error=1;
                                                            System.out.println(" couldnt get connected to the host.make sure ports open");
                                                        }
                                }//end of for

                                


                                
                                // error=1;
                                String comm="";
                                //error=1;
                                if(error==0)
                                comm="success";                     //end of error=0
                                else
                                {comm="abort";
                                ipblocked[u-1]=null;
                                }
                                      for (int i=0;i<ipblocked.length && ipblocked[i]!=null ;i++)
                                     {
                                            Socket skts = new Socket(ipblocked[i],5554);
                                            DataInputStream ins=new DataInputStream(skts.getInputStream());
                                            DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                                    if(skts.isConnected())
                                                    {    outs.write(comm.getBytes(),0,comm.getBytes().length);
                                                         outs.flush();
                                                    }
                                            ins.close();
                                            outs.close();
                                            skts.close();
                                     }

                                

                                }else
                                    {err[0]="true";
                                    err[1]="NO fragments of the table found";
                                    }

                                }else
                                    {
                                    err[0]="true";
                                    err[1]="One or more host with the table " + queryontable +" is not on the network right now";
                                    }
                             }else
                                {
                                System.out.println("query not ok");
                                err[0]="true";
                                err[1]="Syntax Wrong for truncate";
                             }//end of queryok

                            }else
                                 {  err[0]="true";
                                    err[1]="Table does not exist";
                                 }


           }catch(Exception e){e.printStackTrace();err[0]="true";err[1]=e.getMessage();}


           return err;
    }

    public String[] query_insert(String query)
    {String err[]={"false",""};
    try{
        
            String tab=null;
            String database=null;
            String username=null;
            String password=null;

    String values[]=null;
    if(query.contains("values("))
    {
    String qva=query.split("values\\(")[1].split("\\)")[0];
    if(qva.contains(","))//more than 1 cols
    values=qva.split(",");
    else
    {values=new String[1];
     values[0]=qva;
    }//.split(",");
           // for(int i=0;i<values.length;i++)
           // System.out.println("colsva " + values[i]);

    String querywoutvalues=query.split("values\\(")[0];
    String queryontable=null;
    String smalq="";
    if(querywoutvalues.contains("("))
    {       queryontable=querywoutvalues.split("\\(")[0].split("into")[1];
            //System.out.println("queryontable " + new Systemfunction().tableexist(queryontable));
            String cols[]=null;
            String qca=query.split("values\\(")[0].split("\\(")[1].split("\\)")[0];
            if(qca.contains(","))
             cols=qca.split(",");
            else
            {
            cols=new String[1];
            cols[0]=qca;
            }
            //for(int i=0;i<cols.length;i++)
            //System.out.println("cols " + cols[i]);

                            if(cols.length==values.length)
                            {                       queryontable= queryontable.trim();
                                                    smalq=querywoutvalues.split("\\(")[0];
                                        if(new Systemfunction().tableexist(queryontable))
                                        {

                                                    int length=table_count(queryontable);
                                                    String listiphavingtable[]=new String[length];
                                                    String usernam[]=new String[length];
                                                    String pass[]=new String[length];
                                                    String datab[]=new String[length];
                                                    String modquery[]=new String[length];

                                                    BufferedReader cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//cnffile.txt")));
                                                    String read=cin.readLine();
                                                    int u=0;
                                                    boolean execonce=false;
                                                    while(read!=null)
                                                    {
                                                         tab=read.split("-")[1].split(",")[0];
                                                         database=read.split("-")[1].split(",")[1].split(";")[0];
                                                         read=cin.readLine();
//                                                         //System.out.println("table " +tab);
                                                         username=read.split("-")[1].split(",")[0];
                                                         if(username==null || username.equalsIgnoreCase(""))
                                                         username="null";
                                                         password=read.split("-")[1].split(",")[1];//.split(";")[0];
                                                         if(!password.equalsIgnoreCase(";"))
                                                         password=password.split(";")[0];
                                                         else
                                                         password="null";
                                                         read=cin.readLine();
                                                         String fragt=read.split("-")[1].split(",")[0];
                                                         //System.out.println(" fragt " + fragt);
                                                         read=cin.readLine();
                                                         String ip=read.split(":")[1].split(";")[0];
                                                         String ccols[]=read.split("-")[1].split(":")[0].split(",");
                                                         String newvalue="";
                                                         String newcols="";
                                                         for(int g=0;g<ccols.length;g++)
                                                         {
                                                           //  System.out.println(" ccosl " + ccols[g] + ip);
                                                                for(int h=0;h<cols.length;h++)
                                                                {
                                                                    if(ccols[g].equalsIgnoreCase(cols[h]))
                                                                    {newvalue+=values[h]+",";
                                                                    newcols+=cols[h]+",";
                                                                    }
                                                                }
                                                         }//end of for
                                                         if(!newvalue.contains(",") && !tab.equalsIgnoreCase(queryontable))
                                                         {//System.out.println(" no matching cols found");
                                                          //err[0]="true";
                                                          //err[1]="No matching cols found.Make sure tables having same names have same columns also";
                                                          read=cin.readLine();
                                                          //break;
                                                         }else
                                                         {
                                                         
                                                         //System.out.println(" new query " + newvalue + " cols" + newcols);
                                                         read=cin.readLine();
                                                         String prim=read.split(":")[0];
                                                         boolean cont=tab.equalsIgnoreCase(queryontable) /*&& (!prim.equalsIgnoreCase("r"))*/;
                                                         if(cont)
                                                          {  System.out.println(" exec once");
                                                             if(newvalue.contains(","))
                                                         {newvalue=newvalue.substring(0, newvalue.lastIndexOf(","));
                                                         newcols=newcols.substring(0, newcols.lastIndexOf(","));

                                                             execonce=true;
                                                             listiphavingtable[u]=ip;
                                                             usernam[u]=username;
                                                             pass[u]=password;
                                                             datab[u]=database;
                                                             modquery[u]=smalq+"("+newcols+")"+" values ("+newvalue+")";
                                                             System.out.println("modquery" + modquery[u]);
                                                             u++;
                                                             }
                                                         }
                                                         }
                                                    read=cin.readLine();
                                                    //System.out.println("read " +  read);
                                                    }
                                                    cin.close();
                                                    comm2 up=new comm2("up",listiphavingtable);
                                                    String errup[]=up.run2();
                                                    up.setPriority(Thread.MAX_PRIORITY);
                                                        if(errup[0].equalsIgnoreCase("true"))
                                                        {
                                                        System.out.println("all ips are up");
                                                      //now finding n executing
                                                        String ipblocked[]=new String[table_count(queryontable)];
                                                        u=0;
                                                        int error=0;
                                                    if(execonce)
                                                    {for(int h=0;h<listiphavingtable.length;h++)
                                                    {
                                                         System.out.println(listiphavingtable[h] + " listip " );
                                                    String queryformat[]=new String [5];
                                                    queryformat[0]=datab[h];
                                                    queryformat[1]=usernam[h];
                                                    queryformat[2]=pass[h];
                                                    queryformat[3]=modquery[h];
                                                    queryformat[4]="insert";
                                                    ipblocked[u++]=listiphavingtable[h];
                                                    Socket skts = new Socket(listiphavingtable[h],5559);
                                                    DataInputStream ins=new DataInputStream(skts.getInputStream());
                                                    DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                                    System.out.println("connected n transmititng");
                                                    if (skts.isConnected()) //got connected to remote host then cont
                                                    {

                                                    byte [] buff=new byte[512];
                                                    int reads=0;
                                                    outs.writeInt(queryformat.length);
                                                    for(int i=0;i<queryformat.length;i++)
                                                    {
                                                    outs.write(queryformat[i].getBytes(),0,queryformat[i].getBytes().length);
                                                    reads=ins.read(buff, 0, (buff.length));
                                                    }
                                                    reads=ins.read(buff, 0, (buff.length));
                                                    System.out.println(" execution by remote");

                                                                if(new String(buff,0,reads).equalsIgnoreCase("success"))
                                                                {
                                                                    System.out.println(" received success from host");

                                                                }else if(new String(buff,0,reads).equalsIgnoreCase("abort"))
                                                                        {
                                                                        System.out.println(" received abort from remote");
                                                                        err[0]="true";

                                                            outs.write("delay".getBytes(), 0,"delay".getBytes().length);
                                                                           reads=ins.read(buff, 0, (buff.length));

                                                                        err[1]=new String(buff,0,reads);
                                                                        System.out.println(" error masseage received from remote" + new String(buff,0,reads));

                                                                        error=1;
                                                                        break;
                                                                        }else
                                                                            {
                                                                            System.out.println("transfer error " + new String(buff,0,reads));
                                                                            error=1;
                                                                            err[0]="true";
                                                                            err[1]="Transfer Error";
                                                                             break;
                                                                            }




                                                        }   else
                                                        {   error=1;
                                                            err[0]="true";
                                                            err[1]="Couldn't get connected to host.";
                                                            System.out.println(" couldnt get connected to the host.Make sure ports open");
                                                        }
                                }//end of for





                                // error=1;
                                String comm="";
                                //error=1;
                                if(error==0)
                                comm="success";                     //end of error=0
                                else
                                {comm="abort";
                                ipblocked[u-1]=null;
                                }
                                      for (int i=0;i<ipblocked.length && ipblocked[i]!=null ;i++)
                                     {      System.out.println("ipoblocked" + ipblocked[i] + "u " + u);
                                            Socket skts = new Socket(ipblocked[i],5554);
                                            DataInputStream ins=new DataInputStream(skts.getInputStream());
                                            DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                                    if(skts.isConnected())
                                                    {    outs.write(comm.getBytes(),0,comm.getBytes().length);
                                                         outs.flush();
                                                    }
                                            ins.close();
                                            outs.close();
                                            skts.close();
                                     }

                                                        }else
                                                            {
                                                            err[0]="true";
                                                            err[1]="Internal error.Possibly due to tampering with files";
                                                            }
                                                        }else
                                                            {err[0]="true";
                                                             err[1]="Could not connect to a host having a table.make sure all host are available and their ports are open for connection";
                                                            }



                                        }else
                                                {err[0]="true";
                                                 err[1]="Table does not exist";
                                                }

                            }else
                                {err[0]="true";
                                 err[1]="No of columns and values dont match";
                                }




    }else
        {err[0]="true";
         err[1]="Columns are to be mentioned for the insert query";
        }
    }else
        {
            err[0]="true";
         err[1]="Syntax Wrong for insert Query";
        }
    }catch(Exception e){e.printStackTrace();
    err[0]="true";
    err[1]=e.getMessage();
    }
    
    return err;
    }

    public String[] query_create(String query)
    {String err[]={"false",""};
    try{

    String queryontable=null;
    String tabletype="access";
    if(!query.contains("database"))
    {
    if(query.contains(":"))
    {tabletype=query.split(":")[0].split("table")[1];
    query=query.split(tabletype +":")[0]+" " + query.split(tabletype+":")[1];
    }
    tabletype=tabletype.trim();
    System.out.println(" tabletype " + tabletype);
    System.out.println(" query " + query);


    if(query.contains("("))
    {       queryontable=query.split("\\(")[0].split("table")[1];
            System.out.println("queryontable " +  queryontable +" "  +  new Systemfunction().tableexist(queryontable));
            String cols[]=null;
           // String qca=query.split("\\(")[1].split("\\)")[0];
         //   String qca=query.split("\\(")[1];
           String  qca=query.substring((query.indexOf("("))+1,query.lastIndexOf(")"));
          //  System.out.println("query " + query);
           // System.out.println("qca 1 " + query.indexOf("("));
//            System.out.println("qca 2" + query.lastIndexOf(")"));
            System.out.println("qca " + qca);


           // qca=qca.substring(0,query.lastIndexOf("\\)"));
            
            if(qca.contains(","))
            { String tt[]=qca.split(",");
                cols=new String[tt.length];
                for(int k=0;k<tt.length;k++)
                {cols[k]=tt[k].trim().split(" ")[0];
                System.out.println("cols are " + cols[k]);
                }
            }else
            {
            cols=new String[1];
            cols[0]=qca.trim().split(" ")[0];
            }

                                                        //now check if the format is supported
                                                        queryontable=queryontable.trim();
                                                        if(!new Systemfunction().tableexist(queryontable))
                                                        {String connectionUrl=null;
                                                        String database=null;
                                                        Connection con=null;
                                                        BufferedReader cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//iplist.txt")));
                                                        String read=cin.readLine();
                                                        int line=1;
                                                        while(read!=null)
                                                        {
                                                        //System.out.println(read.equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress()));

                                                        if(read.equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress()))
                                                        break;
                                                        read=cin.readLine();
                                                        line++;
                                                        }
                                                        
                                                        cin.close();
                                                        cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//inf.txt")));
                                                        read=cin.readLine();
                                                        int line2=1;
                                                        while(read!=null )
                                                        {if(line==line2)
                                                        break;
                                                        read=cin.readLine();
                                                        line2++;
                                                        }
                                                        cin.close();
                                                        String avatype[]=read.split(",");
                                                        int type=0;
                                                        if(avatype.length!=3)
                                                        {
                                                        //System.out.println(" rearaging");
                                                        String temp []={"","",""};
                                                        for(int i=0;i<avatype.length;i++)
                                                            temp[i]=avatype[i];
                                                        avatype=new String[3];
                                                        for(int i=0;i<temp.length;i++)
                                                        {avatype[i]=temp[i];
                                                        System.out.println(avatype[i] + " " + i);
                                                        }
                                                        }
                                                        boolean error=false;
                                                        System.out.println(" comparing condition " + (tabletype.equalsIgnoreCase("access") && avatype[2].equalsIgnoreCase("2"))) ;
                                                        if(tabletype.equalsIgnoreCase("access") && avatype[1].equalsIgnoreCase("2"))
                                                        {//go for access
                                                        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                                                        connectionUrl = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=" +  new File("access//default.accdb").getAbsolutePath();
                                                        con=DriverManager.getConnection(connectionUrl);
                                                        database="default.accdb";
                                                        }else if(tabletype.equalsIgnoreCase("oracle") && avatype[2].equalsIgnoreCase("3"))
                                                            {type=3;
                                                            cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//info.txt")));
                                                            read=cin.readLine();
                                                            read=cin.readLine();
                                                            read=cin.readLine();
                                                            cin.close();
                                                            DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                                                            connectionUrl = "jdbc:oracle:thin:@localhost:1521:"+ read; //database name and password,username
                                                            con=DriverManager.getConnection(connectionUrl,"scott","tiger");
                                                            database=read;
                                                            }
                                                            else if(tabletype.equalsIgnoreCase("sql") && avatype[0].equalsIgnoreCase("1"))
                                                            {//go for sql
                                                            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                                                            connectionUrl = "jdbc:sqlserver://localhost;integratedSecurity=true";
                                                            con=DriverManager.getConnection(connectionUrl);
                                                            database="master";
                                                            }else
                                                                {err[0]="true";
                                                                 err[1]="Dataformat not supported.supported keywords before tablename are sql,oracle,access";
                                                                 error=true;
                                                                }

                                                            if(!error)
                                                            {
                                                             cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//iplist.txt")));
                                                             read=cin.readLine();
                                                             int count=1;
                                                             while(read!=null)
                                                             {read=cin.readLine();
                                                              count++;
                                                             }
                                                             cin.close();
                                                             String ipblocked[]=new String[count-1];
                                                             cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//iplist.txt")));
                                                             read=cin.readLine();
                                                             count=0;
                                                             while(read!=null)
                                                             {   ipblocked[count++]=read;
                                                                 read=cin.readLine();

                                                             }cin.close();
                                                             comm2 up=new comm2("up",ipblocked);
                                                             String errup[]=up.run2();
                                                             up.setPriority(Thread.MAX_PRIORITY);
                                                        //String errup[]={"true",""};
                                                         if(errup[0].equalsIgnoreCase("true"))
                                                        {
                                                            System.out.println("all ips are up");
                                                             Statement st=con.createStatement();
                                                             st.executeUpdate(query);
                                                             st.close();
                                                             con.close();
                                                                String user="";
                                                                String pas="";
                                                                if(type==3)
                                                                {user="scott";
                                                                 pas="tiger";
                                                                }
                                                                BufferedWriter wout=new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"/system/cnffile.txt",true));
                                                                wout.write("Name-"+queryontable+","+database+";"); // write to localcnfg file
                                                                wout.newLine();
                                                                wout.write("UP-"+user+","+pas+";");
                                                                wout.newLine();
                                                                wout.write("Frag-,;");
                                                                wout.newLine();
                                                                wout.write("Col-");
                                                                int p=0;
                                                                for (p=0;p<cols.length-1;p++)
                                                                {
                                                                wout.write(cols[p]+",");
                                                                }
                                                                wout.write(cols[p]);
                                                                wout.write(":"+	InetAddress.getLocalHost().getHostAddress()+";");
                                                                wout.newLine();
                                                                wout.write("P:0;");
                                                                wout.newLine();
                                                                wout.close();
                                                                File iplistf=new File(System.getProperty("user.dir")+"/system//iplist.txt");
                                                               BufferedReader win =new BufferedReader(new FileReader(iplistf));
                                                               String reada=win.readLine();
                                                                 while(reada!=null)
                                                               {
                                                               System.out.println(" self ip " + reada);
                                                               if(reada.equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress()))
                                                               reada=win.readLine();
                                                               if(reada!=null)
                                                               {File cnffile=new File(System.getProperty("user.dir")+"/system//cnffile.txt");
                                                               initiatefiletransfer cn=new initiatefiletransfer(reada);
                                                               cn.setPriority(Thread.MAX_PRIORITY);
                                                               cn.run2(cnffile);
                                                               }reada=win.readLine();
                                                               }

                                                            }else
                                                                {
                                                                    err[0]="true";
                                                                    err[1]="One or more host is not the network";
                                                                }
                                                            }
                                                        }else
                                                            {err[0]="true";
                                                            err[1]="A table with name " + queryontable+ " already exists on the network";
                                                            }
    }else
        {err[0]="true";
         err[1]="Wrong syntax for create query";
        }
    }else
        {err[0]="true";
         err[1]="Database creation not allowed";
        }
    }catch(Exception e){e.printStackTrace();
    err[0]="true";
    err[1]=e.getMessage();
    }
    return err;
    }

    public String[] query_delete(String query)
    {String err[]={"false",""};
    try{

            String tab=null;
            String database=null;
            String username=null;
            String password=null;
        boolean onlyfrag=false;
        boolean queryok=true;
        int error=0;
    if(query.contains(":") && !query.contains("frag:"))
    {
    err[0]="true";
    err[1]="Syntax Error only keyword 'frag' allowed before tablename";
    queryok=false;
    }

    if(query.contains("where"))
    {
    if(query.contains(":")&& query.contains("frag:"))
    {
    onlyfrag=true;
    query=query.split("frag:")[0]+" " + query.split("frag:")[1];
    }
                            if(queryok)
                            {           String queryontable=query.split("from")[1].split("where")[0].trim();
                                        if(new Systemfunction().tableexist(queryontable))
                                        {
                                        //now determine fragmentation
                                                     int length=table_count(queryontable);
                                                    String listiphavingtable[]=new String[length];
                                                    String usernam[]=new String[length];
                                                    String pass[]=new String[length];
                                                    String datab[]=new String[length];
                                                    
                                                    String frag[]=new String [length];
                                                    String pr[]=new String [length];

                                                    boolean cont=false;
                                                    int u=0;
                                            BufferedReader cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//cnffile.txt")));
                                            String read=cin.readLine();
                                            boolean execonce=false;
                                            while(read!=null)
                                            {
                                                 tab=read.split("-")[1].split(",")[0];
                                                 database=read.split("-")[1].split(",")[1].split(";")[0];
                                                 read=cin.readLine();
                                                 username=read.split("-")[1].split(",")[0];
                                                 if(username==null || username.equalsIgnoreCase(""))
                                                 username="null";
                                                 password=read.split("-")[1].split(",")[1];//.split(";")[0];
                                                 if(!password.equalsIgnoreCase(";"))
                                                 password=password.split(";")[0];
                                                 else
                                                 password="null";
                                                 read=cin.readLine();
                                                 String fragtype=read.split("-")[1].split(",")[0];
                                                 read=cin.readLine();
                                                 String ip=read.split(":")[1].split(";")[0];
                                                 read=cin.readLine();
                                                 String prim=read.split(":")[0];
                                                 if(onlyfrag)
                                                 cont=prim.equalsIgnoreCase("f") && tab.equalsIgnoreCase(queryontable);
                                                 else
                                                 cont=tab.equalsIgnoreCase(queryontable);
                                                     if(cont)
                                                     {   execonce=true;
                                                         listiphavingtable[u]=ip;
                                                         usernam[u]=username;
                                                         pass[u]=password;
                                                         datab[u]=database;
                                                         frag[u]=fragtype;
                                                         pr[u]=prim;
                                                                 u++;
                                                     }
                                                      read=cin.readLine();
                                            }//end off while
                                        cin.close();
                                        //now determine the fragtype
                                        if(execonce)
                                        {
                                            boolean horizontalfrag=true;
                                        for(int i=0;i<frag.length;i++)
                                        {
                                        System.out.println(" fragtype " + frag[i]);
                                        if(frag[i].equalsIgnoreCase("v"))
                                        {
                                        horizontalfrag=false;
                                        break;
                                        }
                                        }
                                        if(horizontalfrag)
                                        {
                                        //normal execution on all host
                                             comm2 up=new comm2("up",listiphavingtable);
                                            String errup[]=up.run2();
                                            up.setPriority(Thread.MAX_PRIORITY);
                                            //String errup[]={"true",""};
                                                        if(errup[0].equalsIgnoreCase("true"))
                                                        {

                                                                     System.out.println("all ips are up");
                                                                      //now finding n executing
                                                                        String ipblocked[]=new String[table_count(queryontable)];
                                                                        u=0;
                                                                        
                                                                        for(int h=0;h<listiphavingtable.length && listiphavingtable[h]!=null;h++)
                                                                                        {
                                                                                        if(onlyfrag)
                                                                                        {
                                                                                        if(pr[h].equalsIgnoreCase("p") || pr[h].equalsIgnoreCase("r"))
                                                                                        continue;
                                                                                        }
                                                                                        System.out.println("listiphavingtable "+ listiphavingtable[h]);
                                                                                        String queryformat[]=new String [5];
                                                                                        queryformat[0]=datab[h];
                                                                                        queryformat[1]=usernam[h];
                                                                                        queryformat[2]=pass[h];
                                                                                        queryformat[3]=query;
                                                                                        queryformat[4]="delete";
                                                                                        ipblocked[u++]=listiphavingtable[h];
                                                                                         for(int i=0;i<queryformat.length;i++)
                                                                                        {System.out.println("queryformat " + queryformat[i]);
                                                                                         }
                                                                                        Socket skts = new Socket(listiphavingtable[h],5559);
                                                                                        DataInputStream ins=new DataInputStream(skts.getInputStream());
                                                                                        DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                                                                        System.out.println("connected n transmititng  "+listiphavingtable[h]);
                                                                                        if (skts.isConnected()) //got connected to remote host then cont
                                                                                        {

                                                                                        byte [] buff=new byte[512];
                                                                                        int reads=0;
                                                                                        outs.writeInt(queryformat.length);
                                                                                        for(int i=0;i<queryformat.length;i++)
                                                                                        {
                                                                                        outs.write(queryformat[i].getBytes(),0,queryformat[i].getBytes().length);
                                                                                        reads=ins.read(buff, 0, (buff.length));
                                                                                        }
                                                                                        reads=ins.read(buff, 0, (buff.length));
                                                                                        System.out.println(" execution by remote");

                                                                                                    if(new String(buff,0,reads).equalsIgnoreCase("success"))
                                                                                                    {
                                                                                                        System.out.println(" received success from host");

                                                                                                    }else if(new String(buff,0,reads).equalsIgnoreCase("abort"))
                                                                                                            {
                                                                                                            System.out.println(" received abort from remote");
                                                                                                            err[0]="true";

                                                                                                                outs.write("delay".getBytes(), 0,"delay".getBytes().length);
                                                                                                               reads=ins.read(buff, 0, (buff.length));

                                                                                                            err[1]=new String(buff,0,reads);
                                                                                                            System.out.println(" error masseage received from remote" + new String(buff,0,reads));

                                                                                                            error=1;
                                                                                                            break;
                                                                                                            }else
                                                                                                                {System.out.println("transfer error ");
                                                                                                                error=1;
                                                                                                                 break;
                                                                                                                }




                                                                                            }   else
                                                                                            {   error=1;
                                                                                                System.out.println(" couldnt get connected to the host.make sure ports open");
                                                                                            }
                                                                    }//end of for
                                                                     String comm="";
                                                                                    //error=1;
                                                                            if(error==0)
                                                                            comm="success";                     //end of error=0
                                                                            else
                                                                            {comm="abort";
                                                                            ipblocked[u-1]=null;
                                                                            }
                                                                                  for (int i=0;i<ipblocked.length && ipblocked[i]!=null ;i++)
                                                                                 {
                                                                                        Socket skts = new Socket(ipblocked[i],5554);
                                                                                        DataInputStream ins=new DataInputStream(skts.getInputStream());
                                                                                        DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                                                                                if(skts.isConnected())
                                                                                                {    outs.write(comm.getBytes(),0,comm.getBytes().length);
                                                                                                     outs.flush();
                                                                                                }
                                                                                        ins.close();
                                                                                        outs.close();
                                                                                        skts.close();
                                                                                 }





                                                        }else
                                                            {err[0]="true";
                                                             err[1]="One or more host having the table is not on the network";
                                                            }



                                        }else
                                            {
                                            //vertically fragmented
                                            System.out.println(" going into vertical" + listiphavingtable.length);
                                            String sql[][]=new String[listiphavingtable.length][];
                                            int count=0;
                                            for(int i=0;i<listiphavingtable.length && listiphavingtable[i]!=null && pr[i].equalsIgnoreCase("p");i++)
                                            {
                                                                                        String queryformat[]=new String [6];
                                                                                        queryformat[0]=datab[i];
                                                                                        queryformat[1]=usernam[i];
                                                                                        queryformat[2]=pass[i];
                                                                                        queryformat[3]=query;
                                                                                        queryformat[4]="delete";
                                                                                        queryformat[5]="yes";
                                                                                        Socket skts = new Socket(listiphavingtable[i],5559);
                                                                                        DataInputStream ins=new DataInputStream(skts.getInputStream());
                                                                                        DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                                                                        System.out.println("connected n transmititng  "+listiphavingtable[i]);
                                                                                        if (skts.isConnected()) //got connected to remote host then cont
                                                                                        {

                                                                                        byte [] buff=new byte[512];
                                                                                        int reads=0;
                                                                                        outs.writeInt(queryformat.length);
                                                                                        for(int f=0;f<queryformat.length;f++)
                                                                                        {
                                                                                        outs.write(queryformat[f].getBytes(),0,queryformat[f].getBytes().length);
                                                                                        reads=ins.read(buff, 0, (buff.length));
                                                                                        }
                                                                                        reads=ins.read(buff, 0, (buff.length));
                                                                                        System.out.println(" execution by remote");

                                                                                                    if(new String(buff,0,reads).equalsIgnoreCase("success"))
                                                                                                    {
                                                                                                        System.out.println(" received success from host");

                                                                                                    }else if(new String(buff,0,reads).equalsIgnoreCase("abort"))
                                                                                                            {
                                                                                                            System.out.println(" received abort from remote");
                                                                                                            err[0]="true";

                                                                                                                outs.write("delay".getBytes(), 0,"delay".getBytes().length);
                                                                                                               reads=ins.read(buff, 0, (buff.length));

                                                                                                            err[1]=new String(buff,0,reads);
                                                                                                            System.out.println(" error masseage received from remote" + new String(buff,0,reads));

                                                                                                            error=1;
                                                                                                            break;
                                                                                                            }else
                                                                                                                {System.out.println("transfer error ");
                                                                                                                error=1;
                                                                                                                 err[0]="true";
                                                                                                                 err[1]="Transfer error ";
                                                                                                                    break;

                                                                                                                }




                                                                                            }   else
                                                                                            {   error=1;
                                                                                                System.out.println(" couldnt get connected to the host.make sure ports open");
                                                                                                err[0]="true";
                                                                                                err[1]=" couldnt get connected to the host.make sure ports open";
                                                                                            }

                                            

                                            if(error!=1)
                                            {

                                                Socket sktt = null;
                                                ServerSocket srvrt = null; //transfer
                                                DataInputStream in=null;
                                                DataOutputStream out=null;
                                                try{
                                                sktt = new Socket();
                                                srvrt = new ServerSocket(5554); //transfer
                                                System.out.print("waiting on 5554 ");
                                                sktt = srvrt.accept();
                                                in=new DataInputStream(sktt.getInputStream());
                                                out=new DataOutputStream(sktt.getOutputStream());
                                                byte bufs []=new byte[512];
                                                length=in.readInt();
                                                sql[i]=new String[length];
                                                System.out.println("new sql length " + sql[i].length);

                                                for(int j=0;j<length;j++)
                                                {
                                                   
                                                int red=0;
                                                red=in.read(bufs,0,bufs.length-1);
                                                out.write("ss".getBytes(),0,"ss".getBytes().length);
                                                if(red==-1){break;}
                                                sql[i][j]=new String(bufs,0,red);
                                                //System.out.println(" received String " + sql[j] + " length " + length + " j"  + red);
                                                out.flush();
                                                }
                                                System.out.println("successfully received array for vertical fragmentatoin");

                                                in.close();
                                                out.close();
                                                sktt.close();
                                                srvrt.close();
                                                }catch(Exception e){e.printStackTrace();
                                                out.flush();
                                                in.close();
                                                out.close();
                                                sktt.close();
                                                srvrt.close();
                                                }
                                                //now we got the prim id's now update the query n send to all
                                                                                         
                                          }//end of errror!=1




                                            }//end of vertical for


                                                if(error!=1)
                                                {
                                                 


                                                    count=0;
                                                for(int k=0;k<sql.length && sql[k]!=null;k++)
                                                {for(int b=0;(b<sql[k].length) && (sql[k][b]!=null);b++)
                                                { sql[k][b]=" delete from "+queryontable+" where " + sql[k][b];
                                                  System.out.println("sql queries"+ sql[k][b]);
                                                 count++;
                                                 }
                                               }
                                                System.out.println("sql count " + count);
                                                            int ipl=0;
                                                            u=0;
                                                    for(int i=0;i<listiphavingtable.length && listiphavingtable[i]!=null;i++)
                                                    {ipl++;}
                                                    String ipblocked[]=new String[ipl];
                                                    int count2=count;
                                                    for(int i=0;i<listiphavingtable.length && listiphavingtable[i]!=null;i++)
                                                    {                                   count=count2;
                                                                                        if(onlyfrag)
                                                                                        {
                                                                                        if(pr[i].equalsIgnoreCase("p") || pr[i].equalsIgnoreCase("r"))
                                                                                        continue;
                                                                                        }
                                                                                        String queryf[]=new String[count+5];
                                                                                        queryf[0]=datab[i];
                                                                                        queryf[1]=usernam[i];
                                                                                        queryf[2]=pass[i];
                                                                                        queryf[3]="dwm";
                                                                                        queryf[4]="spdelete";

                                                                                        count=5;
                                                                                        for(int k=0;k<sql.length && sql[k]!=null;k++)
                                                                                        {for(int b=0;b<sql[k].length && sql[k][b]!=null;b++)
                                                                                        {queryf[count]=sql[k][b];
                                                                                        // System.out.println("queryf" + queryf[count]);

                                                                                         count++;
                                                                                         }
                                                                                        }
                                                                                        System.out.println("count before newqueryformat" + count);
                                                                                        for(int r=0;r<queryf.length;r++)
                                                                                        {
                                                                                        System.out.println("new queryformat" + queryf[r]);
                                                                                        }
                                                                                        if(count>5) //this means no rows affected
                                                                                        {
                                                                                           Socket skts = new Socket(listiphavingtable[i],5559);

                                                                                        DataInputStream ins=new DataInputStream(skts.getInputStream());
                                                                                        DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                                                                        System.out.println("connected n transmititng  "+listiphavingtable[i]);
                                                                                        ipblocked[u++]=listiphavingtable[i];
                                                                                        skts.setTcpNoDelay(false);
                                                                                        
                                                                                        if (skts.isConnected()) //got connected to remote host then cont
                                                                                        {

                                                                                        byte [] buff=new byte[512];
                                                                                        int reads=0;
                                                                                        outs.writeInt(queryf.length);
                                                                                        for(int r=0;r<queryf.length;r++)
                                                                                        {

                                                                                        outs.write(queryf[r].getBytes(),0,queryf[r].getBytes().length);
                                                                                        reads=ins.read(buff, 0, (buff.length));
                                                                                        }
                                                                                        reads=ins.read(buff, 0, (buff.length));
                                                                                        System.out.println(" execution by remote");

                                                                                                    if(new String(buff,0,reads).equalsIgnoreCase("success"))
                                                                                                    {
                                                                                                        System.out.println(" received success from host");

                                                                                                    }else if(new String(buff,0,reads).equalsIgnoreCase("abort"))
                                                                                                            {
                                                                                                            System.out.println(" received abort from remote");
                                                                                                            err[0]="true";

                                                                                                                outs.write("delay".getBytes(), 0,"delay".getBytes().length);
                                                                                                               reads=ins.read(buff, 0, (buff.length));

                                                                                                            err[1]=new String(buff,0,reads);
                                                                                                            System.out.println(" error masseage received from remote" + new String(buff,0,reads));

                                                                                                            error=1;
                                                                                                            break;
                                                                                                            }else
                                                                                                                {System.out.println("transfer error ");
                                                                                                                error=1;
                                                                                                                 err[0]="true";
                                                                                                                 err[1]="Transfer error ";
                                                                                                                    break;

                                                                                                                }


                                                                                        }else
                                                                                            {err[0]="true";
                                                                                             err[1]="Network Connection Lost";
                                                                                            }
                                                                                }//end of count>5
                                                                            }//end of for

                                String comm="";
                                //error=1;
                                if(error==0)
                                comm="success";                     //end of error=0
                                else
                                {comm="abort";
                                ipblocked[u-1]=null;
                                }


                                 System.out.println("ipblocked length "  + ipblocked.length);

                                      for (int s=0;s<ipblocked.length && ipblocked[s]!=null ;s++)
                                     {

                                          System.out.println("ipblocked  "  + ipblocked[s]);
                                            Socket sktsf = new Socket(ipblocked[s],5554);
                                            DataInputStream insf=new DataInputStream(sktsf.getInputStream());
                                            DataOutputStream outsf=new DataOutputStream(sktsf.getOutputStream());
                                                    if(sktsf.isConnected())
                                                    {    outsf.write(comm.getBytes(),0,comm.getBytes().length);
                                                         outsf.flush();
                                                    }
                                            insf.close();
                                            outsf.close();
                                            sktsf.close();
                                     }



                                                                                      
                                                    }//end of error!=1

                                                  
                                            }//end of vertical

                                        }else
                                            {
                                            err[0]="true";
                                            err[1]="Fragments of the "+queryontable+" doesnot exist";

                                            }






                                            

                                        }else
                                            {err[0]="true";
                                             err[1]="Table does not exist";
                                            }
                            }//end of queryok








    












    }else
        {//this means normal delete from table which is equivalent to truncate
        if(queryok)
        {query="truncate table " + query.split("from")[1].trim();
        System.out.println("query in delete " + query);
        String errr[]=query_truncate(query);
        err[0]=errr[0];
        err[1]=errr[1];
        }
        }//end of else of query not containig where









    }catch(Exception e){e.printStackTrace();
    err[0]="true";
    err[1]=e.getMessage();
    }
    return err;
    }

    public String[] query_join(String query)
    {
    String err[]={"false",""};
    String tab=null;
    String database=null;
    String username=null;
    String password=null;
    int error=0;
    try{

        File result=new File(System.getProperty("user.dir")+"/temp//result.txt");
    if(result.exists())
    result.delete();
    result.createNewFile();


        boolean queryok=true;
    String queryontable[]=null;
        if(query.contains("where"))
    {
    queryontable=query.split("from")[1].split("where")[0].split(",");
    }
    else
    queryontable=query.split("from")[1].split(",");
        //System.out.println("queryontables" + queryontable[1]);
    if(queryontable.length>2 || !queryontable[0].contains("as") || !queryontable[1].contains("as"))
    {queryok=false;
    }
    if(queryok)
    {
                //extract aliases
                int index1=queryontable[0].trim().lastIndexOf("as");
                int index2=queryontable[1].trim().lastIndexOf("as");
                
                //System.out.println("queryontable" + queryontable);

                //String alias1=queryontable[0].trim().split("as")[1].trim();
                //String alias2=queryontable[1].trim().split("as")[1].trim();
               String alias1=queryontable[0].substring(index1,queryontable[0].length()).trim().split("as")[1];
               String alias2=queryontable[1].substring(index2,queryontable[1].length()).trim().split("as")[1];


               String cols=query.split("select")[1].split("from")[0].trim();
                String col[]=null;
                if(cols.contains(","))//more than 1 column selected
                col=cols.split(",");
                else
                {
                col=new String[1];
                col[0]=cols.trim();
                }
                                        for(int i=0;i<col.length;i++)
                                        col[i].trim();

                                        alias1=alias1.trim();
                                        alias2=alias2.trim();
                                        int countcol1=0;
                                        int countcol2=0;
                                        String alias1col[]=null;
                                        String alias2col[]=null;
                                        System.out.println("aliases are "+ alias1 + " 2 " + alias2);
                                        //find aliases of the column
                                        for(int l=0;l<col.length;l++)
                                        {       System.out.println("cols" + col[l]);
                                        if(col[l].split("\\.")[0].trim().equalsIgnoreCase(alias1))
                                        {    countcol1++;
                                        }else if(col[l].split("\\.")[0].trim().equalsIgnoreCase(alias2))
                                        {    countcol2++;
                                        }else
                                            queryok=false;
                                        }
                                       
                                        if(queryok)
                                        {

                                            //System.out.println("count c1 "+ countcol1 + " 2 " + countcol2);

                                            if(countcol1>0)
                                            alias1col=new String[countcol1];
                                            else
                                            alias1col=new String[0];

                                            if(countcol2>0)
                                            alias2col=new String[countcol2];
                                            else
                                            alias2col=new String[0];
                                            int c1=0;
                                            int c2=0;
                                            for(int l=0;l<col.length;l++)
                                        {
                                        if(col[l].split("\\.")[0].equalsIgnoreCase(alias1))
                                        {    alias1col[c1++]=col[l].split("\\.")[1];
                                        }
                                        if(col[l].split("\\.")[0].equalsIgnoreCase(alias2))
                                        {   alias2col[c2++]=col[l].split("\\.")[1];
                                        
                                        }

                                        }//end of for


                                       // for(int j=0;j<alias1col.length;j++)
                                       // System.out.println("cols of table 1 are "+ alias1col[j]);
                                        //for(int j=0;j<alias2col.length;j++)
                                        //System.out.println("cols of table 2 are "+ alias2col[j]);


                                        String table1=queryontable[0].substring(0,index1).trim();
                                        String table2=queryontable[1].substring(0,index2).trim();
                                        //System.out.println("table 1" +table1 + " table2" + table2);
                                        if(new Systemfunction().tableexist(table1) && new Systemfunction().tableexist(table2))
                                           {
                                            BufferedReader cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//cnffile.txt")));
                                            countcol1=0;
                                            countcol2=0;
                                            String read=cin.readLine();
                                            while(read!=null)
                                            {
                                            tab=read.split("-")[1].split(",")[0];
                                                 database=read.split("-")[1].split(",")[1].split(";")[0];
//                                            //System.out.println("table " + tab + " database " + database);
                                            
                                                 read=cin.readLine();
                                                 read=cin.readLine();
                                                 read=cin.readLine();
                                                 read=cin.readLine();
                                                 String prim=read.split(":")[0];
                                                 if((tab.trim().equalsIgnoreCase(table1) && prim.equalsIgnoreCase("p")))
                                                 {countcol1++;}
                                                 if((tab.trim().equalsIgnoreCase(table2) && prim.equalsIgnoreCase("p")))
                                                 {countcol2++;}
                                            read=cin.readLine();
                                            }
                                            cin.close();
                                            System.out.println("count1" + countcol1 + "2 " + countcol2);
                                                 String tab1[][]=new String[countcol1][5];
                                                 String tab2[][]=new String[countcol2][5];



                                                 countcol1=0;
                                                 countcol2=0;
                                            cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//cnffile.txt")));
                                            read=cin.readLine();
                                            while(read!=null)
                                            {
                                            tab=read.split("-")[1].split(",")[0];
                                            //System.out.println("table " + tab + " database " + database);
                                                 database=read.split("-")[1].split(",")[1].split(";")[0];
                                                 read=cin.readLine();

                                                 username=read.split("-")[1].split(",")[0];
                                                 if(username==null || username.equalsIgnoreCase(""))
                                                 username="null";
                                                 password=read.split("-")[1].split(",")[1];//.split(";")[0];
                                                 if(!password.equalsIgnoreCase(";"))
                                                 password=password.split(";")[0];
                                                 else
                                                 password="null";

                                                 //System.out.println("username " + username + " password" + password);
                                                 read=cin.readLine();
                                                 String ipread=cin.readLine();
                                                 String reacol=ipread.split("-")[1].split(":")[0];
                                                 read=cin.readLine();
                                                 String prim=read.split(":")[0];
                                                 
                                                 if((tab.trim().equalsIgnoreCase(table1) && prim.equalsIgnoreCase("p")))
                                                 {
                                                 tab1[countcol1][0]=database;
                                                 tab1[countcol1][1]=username;
                                                 tab1[countcol1][2]=password;
                                                 tab1[countcol1][3]=reacol;
                                                 tab1[countcol1][4]=ipread.split(":")[1].split(";")[0];//ip
                                                 countcol1++;
                                                 }
                                                 if((tab.trim().equalsIgnoreCase(table2) && prim.equalsIgnoreCase("p")))
                                                 {
                                                 tab2[countcol2][0]=database;
                                                 tab2[countcol2][1]=username;
                                                 tab2[countcol2][2]=password;
                                                 tab2[countcol2][3]=reacol;
                                                 tab2[countcol2][4]=ipread.split(":")[1].split(";")[0];//ip
                                                 countcol2++;
                                                 }
                                                 read=cin.readLine();
                                            }//end of while

                                            cin.close();


                                            //now we got the tables n their formats n everything
                                            //cases to be checked if both local & same database then let it go as it is
                                            //if pri n more then 1 instances of both tables
                                            //
                                            if(countcol1>=2 && countcol2>=2)
                                            {
                                            queryok=false;
                                            err[0]="true";
                                            err[1]="Joins on multiple primaries of two tables not supported";
                                            System.out.println("Joins on multiple primaries of two tables not supported");
                                            }else if((countcol1>1 && countcol2==1) ||(countcol1==1 && countcol2>1))
                                            {
                                            queryok=true;
                                            }


                                                        if(queryok)
                                                        {
                                                        
                                                        
                                                        System.out.println("normal execution");
                                                        //see whether the fired query is on same database n same ip
                                                            //then we can pass the query as it is
                                                        if(countcol1==1 && countcol2==1  && tab1[0][0].equalsIgnoreCase(tab2[0][0]) && tab1[0][4].equalsIgnoreCase(tab2[0][4]))
                                                        {
                                                            System.out.println("tab2 " + tab1[0][0] + "2 " + tab2[0][0] + " ip " + tab1[0][4]);
                                                        String queryformat[]=new String[5];
                                                        queryformat[0]=tab1[0][0];
                                                        queryformat[1]=tab1[0][1];
                                                        queryformat[2]=tab1[0][2];
                                                        queryformat[3]=query;
                                                        queryformat[4]="select";
                                                        System.out.println("ip" + tab1[0][4]);
                                                            Socket skts = new Socket(tab1[0][4],5559);
                                                            DataInputStream ins=new DataInputStream(skts.getInputStream());
                                                            DataOutputStream outs=new DataOutputStream(skts.getOutputStream());

                                                              if (skts.isConnected()) //got connected to remote host then cont
                                                            {

                                                                    byte [] buff=new byte[512];
                                                    int reads=0;
                                                    outs.writeInt(queryformat.length);
                                                    for(int i=0;i<queryformat.length;i++)
                                                    {
                                                    outs.write(queryformat[i].getBytes(),0,queryformat[i].getBytes().length);
                                                    reads=ins.read(buff, 0, (buff.length));
                                                    }
                                                    reads=ins.read(buff, 0, (buff.length));
                                                    System.out.println(" execution by remote");
                                                            if(new String(buff,0,reads).equalsIgnoreCase("success"))
                                                            {
                                                                //continue successful transmision of error
                                                                //wait for the reply
                                                                //timer needed over here
                                                            System.out.println(" received success from remote");
                                                            DataInputStream in=null;
                                                            DataOutputStream out=null;
                                                            Socket sktt = null;
                                                            ServerSocket srvrt = null; //transfer
                                                            FileOutputStream ffo=null;
                                                            try{
                                                            srvrt =new ServerSocket(5554);
                                                            sktt =srvrt.accept();
                                                            in=new DataInputStream(sktt.getInputStream());
                                                            out=new DataOutputStream(sktt.getOutputStream());
                                                            ffo=new FileOutputStream(System.getProperty("user.dir")+"/temp//temp.txt");
                                                            int length=in.readInt();//read length;
                                                            int readl=0;
                                                            //System.out.println("read length" + length);
                                                            byte bufs []=new byte[1024*1];
                                                            while(readl<length)
                                                            {
                                                            reads=in.read(bufs,0,bufs.length);
                                                            if(reads==-1){break;}
                                                            ffo.write(bufs,0,reads);
                                                            readl+=reads;
                                                            //System.out.println(" read " + reads + " r " + read +  "  "+(read<=length));
                                                            out.write("delay".getBytes(), 0,"delay".getBytes().length);
                                                            out.flush();
                                                            }
                                                            out.write("success".getBytes(), 0,"success".getBytes().length);
                                                            out.flush();
                                                            ffo.close();
                                                            in.close();
                                                            out.close();
                                                            sktt.close();
                                                            srvrt.close();
                                                            ffo=null;
                                                            }catch(Exception e){e.printStackTrace();
                                                            out.write("abort".getBytes(), 0,"abort".getBytes().length);
                                                            out.flush();
                                                            ffo.close();
                                                            in.close();
                                                            out.close();
                                                            sktt.close();
                                                            srvrt.close();
                                                            ffo=null;

                                                            }
                                                            Systemfunction obj=new Systemfunction();
                                                            obj.appendfile(System.getProperty("user.dir")+ "/temp//temp.txt",result.getAbsolutePath(), true);
                                                            System.out.println("appended once");
                                                            }
                                                            else if(new String(buff,0,reads).equalsIgnoreCase("abort"))
                                                            {//stop execution throw n error mesage
                                                            //get the error from the remote

                                                            outs.write("delay".getBytes(), 0,"delay".getBytes().length);
                                                            reads=ins.read(buff, 0, (buff.length));
                                        //
                                                            System.out.println(" received abort from remote");
                                                            System.out.println(" error masseage" + new String(buff,0,reads));
                                                            err[0]="true";err[1]=new String(buff,0,reads);

                                                            }
                                                        }
                                                        else
                                                        {
                                                            // could not get connected stop execution
                                                            System.out.println("  transfer error");
                                                            err[0]="true";err[1]="transfer err";
                                                            
                                                        }



                                                        }//end of both instance 1 n same format

                                                        
                                                        





                                                        
                                                        else{

                                                            //if(alias1col.length<=alias2col.length)
                                                            //{
                                                                if(query.contains("where"))
                                                                {String joincol[]=query.split("where")[1].split("=");
                                                            String join1=null;
                                                            String join2=null;
                                                            if(joincol[0].contains(alias1))
                                                                join1=joincol[0].split("\\.")[1];
                                                            else
                                                                join1=joincol[1].split("\\.")[1];

                                                            if(joincol[1].contains(alias2))
                                                                join2=joincol[1].split("\\.")[1];
                                                            else
                                                                join2=joincol[0].split("\\.")[1];
                                                                //System.out.println(" join 1 "+ join1 + "join2 " + join2) ;
                                                                String t1q="select ";
                                                                String t1c="";
                                                                for(int i=0;i<alias1col.length;i++)
                                                                t1c+=alias1col[i]+",";
                                                                t1c=t1c+join1;
                                                                //t1c=t1c.substring(0, t1c.lastIndexOf(","));
                                                                t1q=t1q+t1c+"  from " +table1;
                                                                System.out.println("query " + t1q);
                                                                String sql[][]=new String[tab1.length][];
                                                            for(int k=0;k<tab1.length;k++)
                                                            {
                                                                String []queryformat=new String[5];
                                                                queryformat[0]=tab1[k][0];
                                                                queryformat[1]=tab1[k][1];
                                                                queryformat[2]=tab1[k][2];
                                                                queryformat[3]=t1q;
                                                                queryformat[4]="selectjoin";
                                                                                        Socket skts = new Socket(tab1[k][4],5559);
                                                                                        DataInputStream ins=new DataInputStream(skts.getInputStream());
                                                                                        DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                                                                        System.out.println("connected n transmititng  "+tab1[k][4] + queryformat.length);
                                                                                        if (skts.isConnected()) //got connected to remote host then cont
                                                                                        {

                                                                                        byte [] buff=new byte[512];
                                                                                        int reads=0;
                                                                                        outs.writeInt(queryformat.length);
                                                                                        for(int f=0;f<queryformat.length;f++)
                                                                                        {
                                                                                        outs.write(queryformat[f].getBytes(),0,queryformat[f].getBytes().length);
                                                                                        reads=ins.read(buff, 0, (buff.length));
                                                                                        }
                                                                                        reads=ins.read(buff, 0, (buff.length));
                                                                                        System.out.println(" execution by remote");

                                                                                                    if(new String(buff,0,reads).equalsIgnoreCase("success"))
                                                                                                    {
                                                                                                        System.out.println(" received success from host");

                                                                                                    }else if(new String(buff,0,reads).equalsIgnoreCase("abort"))
                                                                                                            {
                                                                                                            System.out.println(" received abort from remote");
                                                                                                            err[0]="true";

                                                                                                                outs.write("delay".getBytes(), 0,"delay".getBytes().length);
                                                                                                               reads=ins.read(buff, 0, (buff.length));

                                                                                                            err[1]=new String(buff,0,reads);
                                                                                                            System.out.println(" error masseage received from remote" + new String(buff,0,reads));

                                                                                                            error=1;
                                                                                                           
                                                                                                            }else
                                                                                                                {System.out.println("transfer error ");
                                                                                                                error=1;
                                                                                                                 err[0]="true";
                                                                                                                 err[1]="Transfer error ";
                                                                                                           
                                                                                                                }




                                                                                            }   else
                                                                                            {   error=1;
                                                                                                System.out.println(" couldnt get connected to the host.make sure ports open");
                                                                                                err[0]="true";
                                                                                                err[1]=" couldnt get connected to the host.make sure ports open";
                                                                                            }

                                             if(error!=1)
                                            {

                                                Socket sktt = null;
                                                ServerSocket srvrt = null; //transfer
                                                DataInputStream in=null;
                                                DataOutputStream out=null;
                                                try{
                                                sktt = new Socket();
                                                srvrt = new ServerSocket(5554); //transfer
                                                //System.out.print("waiting on 5554 ");
                                                sktt = srvrt.accept();
                                                in=new DataInputStream(sktt.getInputStream());
                                                out=new DataOutputStream(sktt.getOutputStream());
                                                byte bufs []=new byte[512];
                                                int length=in.readInt();
                                                sql[k]=new String[length];
                                                //System.out.println("new sql length " + sql[k].length);

                                                for(int j=0;j<length;j++)
                                                {

                                                int red=0;
                                                red=in.read(bufs,0,bufs.length-1);
                                                out.write("ss".getBytes(),0,"ss".getBytes().length);
                                                if(red==-1){break;}
                                                sql[k][j]=new String(bufs,0,red);
                                                //System.out.println(" received String " + sql[j] + " length " + length + " j"  + red);
                                                out.flush();
                                                }
                                                System.out.println("successfully received array for updation");

                                                in.close();
                                                out.close();
                                                sktt.close();
                                                srvrt.close();
                                                }catch(Exception e){e.printStackTrace();
                                                out.flush();
                                                in.close();
                                                out.close();
                                                sktt.close();
                                                srvrt.close();
                                                }


                                             }//end of error
                                     }//end of for

                                                  //newly added line
                                                  if(error!=1)
                                                  {


                                                  
                                                  int count=0;
                                                  boolean coma=false;
                                                  for(int k=0;k<sql.length && sql[k]!=null;k++)
                                                  {for(int b=0;(b<sql[k].length) && (sql[k][b]!=null);b++)
                                                  { //sql[k][b]=" update "+queryontable+" set " + " where " + sql[k][b];
                                                  System.out.println("sql results from 1st table"+ sql[k][b]);
                                                  if(sql[k][b].contains(","))
                                                  coma=true;
                                                      count++;
                                                  }
                                                  } String newsql[]=new String[count];
                                                  count=0;
                                                                t1c="";
                                                                t1q="select ";
                                                                for(int i=0;i<alias2col.length;i++)
                                                                t1c+=alias2col[i]+",";
                                                                t1c=t1c+join2;
                                                                //if(alias2col.length)
                                                                //t1c=t1c.substring(0, t1c.lastIndexOf(","));
                                                                t1q=t1q+t1c+"  from " +table2;
                                                                System.out.println("query for second table " + t1q);
                                                 
                                                  for(int k=0;k<sql.length && sql[k]!=null;k++)
                                                  {for(int b=0;(b<sql[k].length) && (sql[k][b]!=null);b++)
                                                  {

                                                  if(coma)
                                                  {newsql[count]=t1q+" where "+ join2 +" = "+sql[k][b].split(",")[sql[k][b].split(",").length-1];
                                                  sql[k][b]=sql[k][b].substring(0,sql[k][b].lastIndexOf(","));
                                                  }
                                                  else
                                                  {newsql[count]=t1q+" where "+ join2 +" = "+sql[k][b];
                                                  sql[k][b]="";
                                                  }
                                                   System.out.println("newsql " + newsql[count]);
                                                   count++;
                                                   }
                                                  }


                                                                String tabsql2[][]=new String[tab2.length][];
                                                                 for(int k=0;k<tab2.length;k++)
                                                            {

                                                                String []queryformat=new String[5];
                                                                queryformat[0]=tab2[k][0];
                                                                queryformat[1]=tab2[k][1];
                                                                queryformat[2]=tab2[k][2];
                                                                queryformat[3]="select";
                                                                queryformat[4]="selectj2";
                                                                 Socket skts = new Socket(tab2[k][4],5559);
                                                                                        DataInputStream ins=new DataInputStream(skts.getInputStream());
                                                                                        DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                                                                        System.out.println("connected n transmititng  "+tab2[k][4] + queryformat.length);
                                                                                        if (skts.isConnected()) //got connected to remote host then cont
                                                                                        {

                                                                                        byte [] buff=new byte[512];
                                                                                        int reads=0;
                                                                                        outs.writeInt(queryformat.length+newsql.length);
                                                                                        for(int f=0;f<queryformat.length;f++)
                                                                                        {
                                                                                        outs.write(queryformat[f].getBytes(),0,queryformat[f].getBytes().length);
                                                                                        reads=ins.read(buff, 0, (buff.length));
                                                                                        }
                                                                                        for(int f=0;f<newsql.length;f++)
                                                                                        {
                                                                                        outs.write(newsql[f].getBytes(),0,newsql[f].getBytes().length);
                                                                                        reads=ins.read(buff, 0, (buff.length));
                                                                                        }
                                                                                        reads=ins.read(buff, 0, (buff.length));
                                                                                        System.out.println(" execution by remote");

                                                                                                    if(new String(buff,0,reads).equalsIgnoreCase("success"))
                                                                                                    {
                                                                                                        System.out.println(" received success from host");

                                                                                                    }else if(new String(buff,0,reads).equalsIgnoreCase("abort"))
                                                                                                            {
                                                                                                            System.out.println(" received abort from remote");
                                                                                                            err[0]="true";

                                                                                                                 outs.write("delay".getBytes(), 0,"delay".getBytes().length);
                                                                                                               reads=ins.read(buff, 0, (buff.length));

                                                                                                            err[1]=new String(buff,0,reads);
                                                                                                            System.out.println(" error masseage received from remote" + new String(buff,0,reads));

                                                                                                            error=1;

                                                                                                            }else
                                                                                                                {System.out.println("transfer error ");
                                                                                                                error=1;
                                                                                                                 err[0]="true";
                                                                                                                 err[1]="Transfer error ";

                                                                                                                }




                                                                                            }   else
                                                                                            {   error=1;
                                                                                                System.out.println(" couldnt get connected to the host.make sure ports open");
                                                                                                err[0]="true";
                                                                                                err[1]=" couldnt get connected to the host.make sure ports open";
                                                                                            }


                                                     if(error!=1)
                                            {

                                                Socket sktt = null;
                                                ServerSocket srvrt = null; //transfer
                                                DataInputStream in=null;
                                                DataOutputStream out=null;
                                                try{
                                                sktt = new Socket();
                                                srvrt = new ServerSocket(5554); //transfer
                                                //System.out.print("waiting on 5554 ");
                                                sktt = srvrt.accept();
                                                in=new DataInputStream(sktt.getInputStream());
                                                out=new DataOutputStream(sktt.getOutputStream());
                                                byte bufs []=new byte[512];
                                                int length=in.readInt();
                                                tabsql2[k]=new String[length];
                                                System.out.println("new sql length " + tabsql2[k].length);

                                                for(int j=0;j<length;j++)
                                                {

                                                int red=0;
                                                red=in.read(bufs,0,bufs.length-1);
                                                out.write("ss".getBytes(),0,"ss".getBytes().length);
                                                if(red==-1){break;}
                                                tabsql2[k][j]=new String(bufs,0,red);
                                                //System.out.println(" received String " + sql[j] + " length " + length + " j"  + red);
                                                out.flush();
                                                }
                                                System.out.println("successfully received array for updation");

                                                in.close();
                                                out.close();
                                                sktt.close();
                                                srvrt.close();
                                                }catch(Exception e){e.printStackTrace();
                                                out.flush();
                                                in.close();
                                                out.close();
                                                sktt.close();
                                                srvrt.close();
                                                }
                                                     }//end of error
                                                                 }//end of 2nd for table

                                                                 if(error!=1)
                                                                 {
                                                                 count=0;
                                                   for(int k=0;k<tabsql2.length && tabsql2[k]!=null;k++)
                                                  {for(int b=0;(b<tabsql2[k].length) && (tabsql2[k][b]!=null);b++)
                                                  { //sql[k][b]=" update "+queryontable+" set " + " where " + sql[k][b];
                                                  System.out.println("sql queries 2"+ tabsql2[k][b]);
                                                  //if(tabsql2[k][b].contains(","))
                                                  //{tabsql2[k][b]=tabsql2[k][b].substring(0, tabsql2[k][b].lastIndexOf(","));
                                                      //coma=true;
                                                  //}
                                                      count++;
                                                  }
                                                  }
                                                  String res[]=new String[count];
                                                  count=0;
                                                  /*
                                                  System.out.println("sql length " + sql.length + " tabsql " + tabsql2.length);
                                                  for(int h=0;h<sql.length && sql[h]!=null;h++)
                                                  {
                                                  for(int t=0;t<sql[h].length;t++)
                                                  {System.out.println("check " + sql[h][t]+ " t" +  t +  " h" + h );
                                                  }    
                                                  }
                                                  for(int h=0;h<tabsql2.length && tabsql2[h]!=null;h++)
                                                  {
                                                  for(int t=0;t<tabsql2[h].length;t++)
                                                  {System.out.println("check2 " + tabsql2[h][t]+ " t" +  t +  " h" + h );
                                                  }
                                                  }
                                                   
                                                   */
                                                  int d=tabsql2[0].length;
                                                  int r=0;
                                                  for(int k=0;k<sql.length && sql[k]!=null;k++)
                                                  {for(int b=0;(b<sql[k].length) && (sql[k][b]!=null) && r<d;b++)
                                                  {
                                                  if(tabsql2[0][r]!=null && !tabsql2[0][r].equalsIgnoreCase(null) && !tabsql2[0][r].equalsIgnoreCase("null") )
                                                  {
                                                  if(!sql[k][b].equalsIgnoreCase(""))
                                                  res[count]=sql[k][b]+"," + tabsql2[0][r];
                                                  else
                                                  res[count]=tabsql2[0][r];
                                                  }
                                                  else
                                                  res[count]=null;

                                                  System.out.println(res[count]);
                                                  count++;
                                                  r++;
                                                   }
                                                  }
                                                  
                                                  File rr=new File(System.getProperty("user.dir")+"/temp//result.txt");
                                                  BufferedWriter win=new BufferedWriter(new FileWriter(rr));
                                                  for(int i=0;i<alias1col.length;i++)
                                                  win.write(alias1col[i] +"\t");
                                                  for(int i=0;i<alias2col.length;i++)
                                                  win.write(alias2col[i]+"\t");
                                                  String temp[]=null;
                                                  //win.write(join1+"="+join2);
                                                  win.newLine();

                                                                for(int p=0;p<res.length;p++)
                                                                {
                                                                if(res[p]!=null)
                                                                {temp=res[p].split(",");
                                                                

                                                                for(int t=0;t<temp.length-1;t++)
                                                                {
                                                                win.write(temp[t]+"\t");
                                                                }
                                                                 win.newLine();

                                                                }
                                                                }
                                                                win.close();
                                                  }//end of error!=1 for 2 nd table
                                                  }//end of error!=1 for 1st table
                                                                }else{

                                                                err[0]="true";
                                                                err[1]="Heterogeeous joins must have a where clause";
                                                                }
                                                     //       }//less no of cols for 1st time execution






                                                        }//end of more than 1 istancce










                                                        }//end of queryok











                                           }else
                                           {
                                                     err[0]="true";
                                                     err[1]="Tables do not exist";


                                           }
                                        }else
                                        {

                                        err[0]="true";
                                        err[1]="Alias have to be mentioned with the column names";
                                        }
    }else
    {

    err[0]="true";
    err[1]="Heterogeneous joins only allowed on two tables,please use aliases for the tables";
    }
    
        
    }catch(Exception e){e.printStackTrace();
    err[0]="true";
    err[1]=e.getMessage();
    }
    return err;
    }









    
    public String[] query_update(String query)
    {
    String err[]={"false",""};
    try{
            String tab=null;
            String database=null;
            String username=null;
            String password=null;
        boolean queryok=true;
        int error=0;
        if(query.contains(":")  || !query.contains("set"))
        {
        queryok=false;
        err[0]="true";
        err[1]="Update query is written with set n where clauses only";
        }
        if(queryok)
        {
        String queryontable=query.split("update")[1].trim().split("set")[0];
        queryontable=queryontable.trim();
        String setcols=null;
        if(query.contains("where"))
        setcols=query.split("set")[1].trim().split("where")[0].trim();
        else
        setcols=query.split("set")[1].trim();
        
        String colwthval[]=null;
        if(setcols.contains(","))
        colwthval=setcols.split(",");
        else
        {colwthval=new String[1];
         colwthval[0]=setcols;
        }

            String col[]=new String[colwthval.length];

             for(int t=0;t<col.length;t++)
             {System.out.println("setcols ");
             col[t]=colwthval[t].split("=")[0].trim();
             System.out.println("setcols " + col[t]);
             }
                                         if(new Systemfunction().tableexist(queryontable))
                                        {
                                        //now determine fragmentation
                                                    int length=table_count(queryontable);
                                                    String listiphavingtable[]=new String[length];
                                                    String usernam[]=new String[length];
                                                    String pass[]=new String[length];
                                                    String datab[]=new String[length];
                                                    String ccols[][]=new String[length][];
                                                    String modquery[]=new String[length];
                                                    String frag[]=new String [length];
                                                    String pr[]=new String [length];

                                                    int u=0;
                                            BufferedReader cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//cnffile.txt")));
                                            String read=cin.readLine();
                                            boolean execonce=false;
                                            while(read!=null)
                                            {
                                                 tab=read.split("-")[1].split(",")[0];
                                                 database=read.split("-")[1].split(",")[1].split(";")[0];
                                                 read=cin.readLine();
                                                 username=read.split("-")[1].split(",")[0];
                                                 if(username==null || username.equalsIgnoreCase(""))
                                                 username="null";
                                                 password=read.split("-")[1].split(",")[1];//.split(";")[0];
                                                 if(!password.equalsIgnoreCase(";"))
                                                 password=password.split(";")[0];
                                                 else
                                                 password="null";
                                                 read=cin.readLine();
                                                 String fragtype=read.split("-")[1].split(",")[0];
                                                 read=cin.readLine();
                                                 String ip=read.split(":")[1].split(";")[0];
                                                 String colread=read;
                                                 read=cin.readLine();
                                                 String prim=read.split(":")[0];
                                                 if(tab.equalsIgnoreCase(queryontable))
                                                     {   execonce=true;
                                                         ccols[u]=colread.split("-")[1].split(":")[0].split(",");
                                                         String newquery="";

                                                         for(int g=0;g<ccols[u].length;g++)
                                                         {
                                                          for(int f=0;f<col.length;f++)
                                                          {
                                                           if(ccols[u][g].equalsIgnoreCase(col[f]))
                                                           {
                                                               System.out.println("cc cols" + ccols[u][g] + " q cols " + col[f]);
                                                            newquery=colwthval[f]+",";
                                                           }
                                                          }

                                                         }
                                                          if(newquery.contains(","))
                                                          {newquery=newquery.substring(0, newquery.lastIndexOf(","));
                                                          listiphavingtable[u]=ip;
                                                         usernam[u]=username;
                                                         pass[u]=password;
                                                         
                                                           modquery[u]=newquery;
                                                          System.out.println("modquery " + newquery);
                                                         datab[u]=database;
                                                         frag[u]=fragtype;
                                                         pr[u]=prim;
                                                                 u++;
                                                    
                                                          }

                                                         
                                                         //newquery=newquery;
                                                           }
                                                      read=cin.readLine();
                                            }//end off while
                                        cin.close();
                                        //now determine the fragtype
                                        if(execonce)
                                        {
                                        boolean horizontalfrag=true;
                                        for(int i=0;i<frag.length;i++)
                                        {
                                         System.out.println("for  fragtype " + frag[i]);
                                        if(frag[i].equalsIgnoreCase("v"))
                                        {
                                        horizontalfrag=false;
                                        break;
                                        }
                                        }
                                        if(horizontalfrag)
                                        {



                                        //normal execution on all host
                                             comm2 up=new comm2("up",listiphavingtable);
                                            String errup[]=up.run2();
                                            up.setPriority(Thread.MAX_PRIORITY);
                                            //String errup[]={"true",""};
                                                        if(errup[0].equalsIgnoreCase("true"))
                                                        {

                                                                     System.out.println("all ips are up");
                                                                      //now finding n executing
                                                                        String ipblocked[]=new String[table_count(queryontable)];
                                                                        u=0;

                                                                                        for(int h=0;h<listiphavingtable.length && listiphavingtable[h]!=null;h++)
                                                                                        {
                                                                                       
                                                                                        System.out.println("listiphavingtable "+ listiphavingtable[h]);
                                                                                        String queryformat[]=new String [5];
                                                                                        queryformat[0]=datab[h];
                                                                                        queryformat[1]=usernam[h];
                                                                                        queryformat[2]=pass[h];
                                                                                        queryformat[3]=query;
                                                                                        queryformat[4]="update";
                                                                                        ipblocked[u++]=listiphavingtable[h];
                                                                                         for(int i=0;i<queryformat.length;i++)
                                                                                         {
                                                                                             System.out.println("queryformat " + queryformat[i]);

                                                                                         }
                                                                                        Socket skts = new Socket(listiphavingtable[h],5559);
                                                                                        DataInputStream ins=new DataInputStream(skts.getInputStream());
                                                                                        DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                                                                        System.out.println("connected n transmititng  "+listiphavingtable[h]);
                                                                                        if (skts.isConnected()) //got connected to remote host then cont
                                                                                        {

                                                                                        byte [] buff=new byte[512];
                                                                                        int reads=0;
                                                                                        outs.writeInt(queryformat.length);
                                                                                        for(int i=0;i<queryformat.length;i++)
                                                                                        {
                                                                                        outs.write(queryformat[i].getBytes(),0,queryformat[i].getBytes().length);
                                                                                        reads=ins.read(buff, 0, (buff.length));
                                                                                        }
                                                                                        reads=ins.read(buff, 0, (buff.length));
                                                                                        System.out.println(" execution by remote");

                                                                                                    if(new String(buff,0,reads).equalsIgnoreCase("success"))
                                                                                                    {
                                                                                                        System.out.println(" received success from host");

                                                                                                    }else if(new String(buff,0,reads).equalsIgnoreCase("abort"))
                                                                                                            {
                                                                                                            System.out.println(" received abort from remote");
                                                                                                            err[0]="true";

                                                                                                                outs.write("delay".getBytes(), 0,"delay".getBytes().length);
                                                                                                               reads=ins.read(buff, 0, (buff.length));

                                                                                                            err[1]=new String(buff,0,reads);
                                                                                                            System.out.println(" error masseage received from remote" + new String(buff,0,reads));

                                                                                                            error=1;
                                                                                                            break;
                                                                                                            }else
                                                                                                                {System.out.println("transfer error ");
                                                                                                                error=1;
                                                                                                                 break;
                                                                                                                }




                                                                                            }   else
                                                                                            {   error=1;
                                                                                                System.out.println(" couldnt get connected to the host.make sure ports open");
                                                                                            }
                                                                    }//end of for
                                                                     String comm="";
                                                                                    //error=1;
                                                                            if(error==0)
                                                                            comm="success";                     //end of error=0
                                                                            else
                                                                            {comm="abort";
                                                                            ipblocked[u-1]=null;
                                                                            }
                                                                                  for (int i=0;i<ipblocked.length && ipblocked[i]!=null ;i++)
                                                                                 {
                                                                                        Socket skts = new Socket(ipblocked[i],5554);
                                                                                        DataInputStream ins=new DataInputStream(skts.getInputStream());
                                                                                        DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                                                                                if(skts.isConnected())
                                                                                                {    outs.write(comm.getBytes(),0,comm.getBytes().length);
                                                                                                     outs.flush();
                                                                                                }
                                                                                        ins.close();
                                                                                        outs.close();
                                                                                        skts.close();
                                                                                 }



                                                        }else
                                                            {
                                                            err[0]="true";
                                                            err[1]="All host on the network are not available please make sure all host are on";
                                                            System.out.println("All ip not up");
                                                            }

                                }//end of horizontaly fragmented
                                                        else


                                {
                                                            //verticaly fragmented

                                            System.out.println(" going into vertical" + listiphavingtable.length);
                                            String sql[][]=new String[listiphavingtable.length][];
                                            int count=0;
                                            for(int i=0;i<listiphavingtable.length && listiphavingtable[i]!=null && pr[i].equalsIgnoreCase("p");i++)
                                            {
                                                                                        String queryformat[]=new String [6];
                                                                                        queryformat[0]=datab[i];
                                                                                        queryformat[1]=usernam[i];
                                                                                        queryformat[2]=pass[i];
                                                                                        queryformat[3]=query;
                                                                                        queryformat[4]="delete";
                                                                                        queryformat[5]="yes";
                                                                                        Socket skts = new Socket(listiphavingtable[i],5559);
                                                                                        DataInputStream ins=new DataInputStream(skts.getInputStream());
                                                                                        DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                                                                        System.out.println("connected n transmititng  "+listiphavingtable[i] + queryformat.length);
                                                                                        if (skts.isConnected()) //got connected to remote host then cont
                                                                                        {

                                                                                        byte [] buff=new byte[512];
                                                                                        int reads=0;
                                                                                        outs.writeInt(queryformat.length);
                                                                                        for(int f=0;f<queryformat.length;f++)
                                                                                        {
                                                                                        outs.write(queryformat[f].getBytes(),0,queryformat[f].getBytes().length);
                                                                                        reads=ins.read(buff, 0, (buff.length));
                                                                                        }
                                                                                        reads=ins.read(buff, 0, (buff.length));
                                                                                        System.out.println(" execution by remote");

                                                                                                    if(new String(buff,0,reads).equalsIgnoreCase("success"))
                                                                                                    {
                                                                                                        System.out.println(" received success from host");

                                                                                                    }else if(new String(buff,0,reads).equalsIgnoreCase("abort"))
                                                                                                            {
                                                                                                            System.out.println(" received abort from remote");
                                                                                                            err[0]="true";

                                                                                                                outs.write("delay".getBytes(), 0,"delay".getBytes().length);
                                                                                                               reads=ins.read(buff, 0, (buff.length));

                                                                                                            err[1]=new String(buff,0,reads);
                                                                                                            System.out.println(" error masseage received from remote" + new String(buff,0,reads));

                                                                                                            error=1;
                                                                                                            break;
                                                                                                            }else
                                                                                                                {System.out.println("transfer error ");
                                                                                                                error=1;
                                                                                                                 err[0]="true";
                                                                                                                 err[1]="Transfer error ";
                                                                                                                    break;

                                                                                                                }




                                                                                            }   else
                                                                                            {   error=1;
                                                                                                System.out.println(" couldnt get connected to the host.make sure ports open");
                                                                                                err[0]="true";
                                                                                                err[1]=" couldnt get connected to the host.make sure ports open";
                                                                                            }



                                            if(error!=1)
                                            {

                                                Socket sktt = null;
                                                ServerSocket srvrt = null; //transfer
                                                DataInputStream in=null;
                                                DataOutputStream out=null;
                                                try{
                                                sktt = new Socket();
                                                srvrt = new ServerSocket(5554); //transfer
                                                //System.out.print("waiting on 5554 ");
                                                sktt = srvrt.accept();
                                                in=new DataInputStream(sktt.getInputStream());
                                                out=new DataOutputStream(sktt.getOutputStream());
                                                byte bufs []=new byte[512];
                                                length=in.readInt();
                                                sql[i]=new String[length];
                                                //System.out.println("new sql length " + sql[i].length);

                                                for(int j=0;j<length;j++)
                                                {

                                                int red=0;
                                                red=in.read(bufs,0,bufs.length-1);
                                                out.write("ss".getBytes(),0,"ss".getBytes().length);
                                                if(red==-1){break;}
                                                sql[i][j]=new String(bufs,0,red);
                                                //System.out.println(" received String " + sql[j] + " length " + length + " j"  + red);
                                                out.flush();
                                                }
                                                System.out.println("successfully received array for updation");

                                                in.close();
                                                out.close();
                                                sktt.close();
                                                srvrt.close();
                                                }catch(Exception e){e.printStackTrace();
                                                out.flush();
                                                in.close();
                                                out.close();
                                                sktt.close();
                                                srvrt.close();
                                                }
                                                //now we got the prim id's now update the query n send to all

                                          }//end of errror!=1




                                            }//end of vertical for



                                             if(error!=1)
                                                {



                                                    count=0;
                                                for(int k=0;k<sql.length && sql[k]!=null;k++)
                                                {for(int b=0;(b<sql[k].length) && (sql[k][b]!=null);b++)
                                                { sql[k][b]=" update "+queryontable+" set " + " where " + sql[k][b];
                                                  System.out.println("sql queries"+ sql[k][b]);
                                                 count++;
                                                 }
                                               }
                                             



                                            System.out.println("sql count " + count);
                                                            int ipl=0;
                                                    for(int i=0;i<listiphavingtable.length && listiphavingtable[i]!=null;i++)
                                                    {ipl++;}
                                                    String ipblocked[]=new String[ipl];
                                                    int count2=count;
                                                    u=0;
                                                    for(int i=0;i<listiphavingtable.length && listiphavingtable[i]!=null;i++)
                                                    {                                   count=count2;
                                                                                        ipblocked[u++]=listiphavingtable[i];
                                                                                        String queryf[]=new String[count+5];
                                                                                        queryf[0]=datab[i];
                                                                                        queryf[1]=usernam[i];
                                                                                        queryf[2]=pass[i];
                                                                                        queryf[3]="dwm";
                                                                                        queryf[4]="spdelete";

                                                                                        count=5;
                                                                                        for(int k=0;k<sql.length && sql[k]!=null;k++)
                                                                                        {for(int b=0;b<sql[k].length && sql[k][b]!=null;b++)
                                                                                        {queryf[count]=sql[k][b].split("set")[0] +" set " + modquery[i] +" " + sql[k][b].split("set")[1] ;
                                                                                        // System.out.println("queryf" + queryf[count]);

                                                                                         count++;
                                                                                         }
                                                                                        }
                                                                                        System.out.println("count before newqueryformat" + count);
                                                                                        for(int r=0;r<queryf.length;r++)
                                                                                        {
                                                                                        System.out.println("new queryformat" + queryf[r]);
                                                                                        }
                                                                                        if(count>5) //this means no rows affected
                                                                                        {
                                                                                           Socket skts = new Socket(listiphavingtable[i],5559);

                                                                                        DataInputStream ins=new DataInputStream(skts.getInputStream());
                                                                                        DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                                                                        System.out.println("connected n transmititng  "+listiphavingtable[i]);
                                                                                        skts.setTcpNoDelay(false);

                                                                                        if (skts.isConnected()) //got connected to remote host then cont
                                                                                        {

                                                                                        byte [] buff=new byte[512];
                                                                                        int reads=0;
                                                                                        outs.writeInt(queryf.length);
                                                                                        for(int r=0;r<queryf.length;r++)
                                                                                        {

                                                                                        outs.write(queryf[r].getBytes(),0,queryf[r].getBytes().length);
                                                                                        reads=ins.read(buff, 0, (buff.length));
                                                                                        }
                                                                                        reads=ins.read(buff, 0, (buff.length));
                                                                                        System.out.println(" execution by remote");

                                                                                                    if(new String(buff,0,reads).equalsIgnoreCase("success"))
                                                                                                    {
                                                                                                        System.out.println(" received success from host");

                                                                                                    }else if(new String(buff,0,reads).equalsIgnoreCase("abort"))
                                                                                                            {
                                                                                                            System.out.println(" received abort from remote");
                                                                                                            err[0]="true";

                                                                                                                outs.write("delay".getBytes(), 0,"delay".getBytes().length);
                                                                                                               reads=ins.read(buff, 0, (buff.length));

                                                                                                            err[1]=new String(buff,0,reads);
                                                                                                            System.out.println(" error masseage received from remote" + new String(buff,0,reads));

                                                                                                            error=1;
                                                                                                            break;
                                                                                                            }else
                                                                                                                {System.out.println("transfer error ");
                                                                                                                error=1;
                                                                                                                 err[0]="true";
                                                                                                                 err[1]="Transfer error ";
                                                                                                                    break;

                                                                                                                }


                                                                                        }else
                                                                                            {err[0]="true";
                                                                                             err[1]="Network Connection Lost";
                                                                                            }
                                                                                         }//end of count>5
                                                                                        }//end of for
                                 String comm="";
                                //error=1;
                                if(error==0)
                                comm="success";                     //end of error=0
                                else
                                {comm="abort";
                                ipblocked[u-1]=null;
                                }
                                 System.out.println("sending abort or success command");
                                      for (int s=0;s<ipblocked.length && ipblocked[s]!=null ;s++)
                                     {      System.out.println("ipblocked " + ipblocked[s]);
                                            Socket sktsf = new Socket(ipblocked[s],5554);
                                            DataInputStream insf=new DataInputStream(sktsf.getInputStream());
                                            DataOutputStream outsf=new DataOutputStream(sktsf.getOutputStream());
                                                    if(sktsf.isConnected())
                                                    {    outsf.write(comm.getBytes(),0,comm.getBytes().length);
                                                         outsf.flush();
                                                    }
                                            insf.close();
                                            outsf.close();
                                            sktsf.close();
                                     }



                                                                                       
                                                    }//end of error!=1

                                                   















                                                            }//end of else vertically fragmented

        
        
        
                                        } else{
                                            err[0]="true";
                                            err[1]="No tables found";
                                            System.out.println("deadlock");
                                            }
        
                                         }else{
                                            err[0]="true";
                                            err[1]="Table does not exist";
                                            System.out.println("no table");

                                              }
        
        
        
        
        
        }//end of queryof


        
    }catch(Exception e){e.printStackTrace();
    err[0]="true";
    err[1]=e.getMessage();
    }
    return err;
    }










    public int table_count(String table)
    { int val=0;
    try{
                            BufferedReader cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//cnffile.txt")));
                            String read=cin.readLine();
                            while(read!=null)
                            {
                            String tab=read.split("-")[1].split(",")[0];
                            cin.readLine();
                            cin.readLine();
                            read=cin.readLine();
                            String ip=read.split(":")[1].split(";")[0];
                            if(tab.equalsIgnoreCase(table))
                            {
                            val++;
                            }
                            cin.readLine();
                            read=cin.readLine();

                            }
                            cin.close();


        }catch(Exception e){e.printStackTrace();}
    return val;
    }








}





class recqueryarray extends Thread{
public void run(){
        Socket sktt = null;
        ServerSocket srvrt = null; //transfer
        DataInputStream in=null;
	DataOutputStream out=null;
        try{
        sktt = new Socket();
        srvrt = new ServerSocket(5559); //transfer
	System.out.print("waiting on 5559 ");
        sktt = srvrt.accept();
	in=new DataInputStream(sktt.getInputStream());
	out=new DataOutputStream(sktt.getOutputStream());
        sktt.setTcpNoDelay(false);
        
if(global.free)
{
//global.ip=sktt.getInetAddress().getHostAddress();
//System.out.println("got connected for arraytransfer to ip " +global.ip);
System.out.println("transfering of arrays");
byte bufs []=new byte[512];
int length=in.readInt();
String sql[]=new String[length];
for(int j=0;j<length;j++)
{
int red=0;
red=in.read(bufs,0,bufs.length-1);
out.write("ss".getBytes(),0,"ss".getBytes().length);
if(red==-1){break;}
sql[j]=new String(bufs,0,red);
//System.out.println(" received String " + sql[j] + " length " + length + " j"  + red);
out.flush();
}
System.out.println("successfully received array now executing ");


Systemfunction obj=new Systemfunction();
String err[]=obj.qexecaray(sql);
            if(err[0].equalsIgnoreCase("false"))
            {   //System.out.println("successfully executed array");
                out.write("success".getBytes(),0 , ("success".getBytes().length));
                out.flush();
                //System.out.println("conditiom " + (sql[4].equalsIgnoreCase("delete") && (sql.length==5)));
                if(sql[4].equalsIgnoreCase("select"))
                {
                sendresult objec=  new sendresult(sktt.getInetAddress().getHostAddress());
                objec.run2(new File(System.getProperty("user.dir")+"/temp//retemp.txt"));
                objec.setPriority(Thread.MAX_PRIORITY);
                System.out.println("sent temp//retemp");

                }else if(sql[4].equalsIgnoreCase("insert") || sql[4].equalsIgnoreCase("truncate")|| (sql[4].equalsIgnoreCase("delete") && (sql.length==5)) || (sql[4].equalsIgnoreCase("spdelete") && sql[3].equalsIgnoreCase("dwm")) || ( sql[4].equalsIgnoreCase("update") && (sql.length==5)))  //other then select query
                    {

                    global.free=false;
                    Socket sktt2 = null;
                    ServerSocket srvrt2 = null; //transfer
                    DataInputStream in2=null;
                    DataOutputStream out2=null;
                    try{
                    sktt2 = new Socket();
                    srvrt2 = new ServerSocket(5554); //transfer
                    System.out.print("waiting on 5554 ");
                    sktt2 = srvrt2.accept();
                    in2=new DataInputStream(sktt2.getInputStream());
                    out2=new DataOutputStream(sktt2.getOutputStream());
                    //timer needed over here
                    byte buf []=new byte[1024];
                    int read2=in2.read(buf, 0, buf.length);
                    if(new String(buf,0,read2).equalsIgnoreCase("success"))
                    {
                    //commit the query executed from qexec
                           System.out.println(" commiting the query");
                            if(!Systemfunction.access)
                            {
                                         Systemfunction.con.commit();
                                         Systemfunction.con.setAutoCommit(true);
                                         Systemfunction.con.close();
                                         Systemfunction.con=null;
                            }else
                                    {
                                         Systemfunction.access=false;
                                         Systemfunction.con.close();
                                         Systemfunction.con=null;
                                    }
                    }else if(new String(buf,0,read2).equalsIgnoreCase("abort"))
                        {   System.out.println(" aborting the query");

                            if(!Systemfunction.access)
                            {
                                         Systemfunction.con.rollback();
                                         Systemfunction.con.setAutoCommit(true);
                                         Systemfunction.con.close();
                                         Systemfunction.con=null;

                            }else{      Systemfunction.access=false;
                                         new Systemfunction().accessfilecopy(System.getProperty("user.dir")+"/access//temp//backup_"+Systemfunction.filename,System.getProperty("user.dir")+"/access//"+Systemfunction.filename);
                                         Systemfunction.con.close();
                                         Systemfunction.con=null;
                                 }
                                        
                        }else
                            {
                            System.out.println("an unexplained state");
                            }
                    }catch(Exception e ){e.printStackTrace();



                    }
                    in2.close();
                    out2.close();
                    sktt2.close();
                    srvrt2.close();
                    global.free=true;
                    if(Systemfunction.con!=null)
                        Systemfunction.con=null;
                        Systemfunction.filename=null;
                        global.free=true;
                    }//end of else comparinf yes
                else if(sql.length==6 && sql[5].equalsIgnoreCase("yes"))
                {
                //convert the query to select n find all primary ids;
                //err[2] onwards would be having the affected id
                                        Socket skts = new Socket(sktt.getInetAddress().getHostAddress(),5554);
                                        DataInputStream ins=new DataInputStream(skts.getInputStream());
                                        DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                        System.out.println(" trying to coonect to remote host for delete query");
                                        if (skts.isConnected()) //got connected to remote host then cont
                                        {
                                            byte [] buff=new byte[512];

                                         int reads=0;
                                        outs.writeInt(err.length-2);
                                        //System.out.println(" query length " + err.length);
                                        for(int i=2;i<err.length;i++)
                                        {  // System.out.println(" query  " + err[i]);
                                            if(err[i]!=null)
                                            {outs.write(err[i].getBytes(),0,err[i].getBytes().length);
                                            reads=ins.read(buff, 0, (buff.length));
                                            }
                                            else
                                            {
                                            outs.write("null".getBytes(),0,"null".getBytes().length);
                                            reads=ins.read(buff, 0, (buff.length));
                                            }
                                            
                                        }
                                         outs.write("success".getBytes(),0,"success".getBytes().length);
                                           
                                        
                                        outs.close();
                                        ins.close();
                                        skts.close();
                                        }
                }
                else if(sql[4].equalsIgnoreCase("selectjoin"))
                {
                 Socket skts = new Socket(sktt.getInetAddress().getHostAddress(),5554);
                                        DataInputStream ins=new DataInputStream(skts.getInputStream());
                                        DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                        System.out.println(" trying to coonect to remote host for selete query");
                                        if (skts.isConnected()) //got connected to remote host then cont
                                        {
                                            byte [] buff=new byte[512];

                                         int reads=0;
                                        outs.writeInt(err.length-2);
                                        //System.out.println(" query length " + err.length);
                                        for(int i=2;i<err.length;i++)
                                        {  // System.out.println(" query  " + err[i]);
                                            if(err[i]!=null)
                                            {outs.write(err[i].getBytes(),0,err[i].getBytes().length);
                                            reads=ins.read(buff, 0, (buff.length));
                                            }
                                            else
                                            {
                                            outs.write("null".getBytes(),0,"null".getBytes().length);
                                            reads=ins.read(buff, 0, (buff.length));
                                            }

                                        }
                                         outs.write("success".getBytes(),0,"success".getBytes().length);
                                        }
                }else if(sql[4].equalsIgnoreCase("selectj2") || sql[3].equalsIgnoreCase("select"))
                {
                                Socket skts = new Socket(sktt.getInetAddress().getHostAddress(),5554);
                                        DataInputStream ins=new DataInputStream(skts.getInputStream());
                                        DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                                        System.out.println(" trying to coonect to remote host for selete query");
                                        if (skts.isConnected()) //got connected to remote host then cont
                                        {
                                            byte [] buff=new byte[512];

                                         int reads=0;
                                        outs.writeInt(err.length-2);
                                        //System.out.println(" query length " + err.length);
                                        for(int i=2;i<err.length;i++)
                                        {  // System.out.println(" query  " + err[i]);
                                            if(err[i]!=null)
                                            {outs.write(err[i].getBytes(),0,err[i].getBytes().length);
                                            reads=ins.read(buff, 0, (buff.length));
                                            }
                                            else
                                            {
                                            outs.write("null".getBytes(),0,"null".getBytes().length);
                                            reads=ins.read(buff, 0, (buff.length));
                                            }

                                        }
                                         outs.write("success".getBytes(),0,"success".getBytes().length);
                                        }
                }else
                {
                System.out.println("drop query wont be able to have 2 phase roll back");
                }




            }else
                {   System.out.println("error mesage sending " + err[1]);
                    out.write("abort".getBytes(),0 , ("abort".getBytes().length));
                    int rj=in.read(bufs,0,bufs.length-1);
                    System.out.println("specail read" + new String(bufs,0,rj));
                    out.write(err[1].getBytes(),0,err[1].getBytes().length);
                    out.flush();
                }
}
else
{//blocked
System.out.println("Free not");
}

in.close();
out.close();
sktt.close();
srvrt.close();
new recqueryarray().start();
}catch(Exception e){
    e.printStackTrace();
    try{
    out.write("abort".getBytes(),0 , ("abort".getBytes().length));
    out.flush();
    in.close();
    out.close();
    sktt.close();
    srvrt.close();
    new recqueryarray().start();
    }catch(Exception e1){e1.printStackTrace();}
}
}
}//end of recqueryarray


class sendresult extends Thread{
String remote="";
FileInputStream fin;
sendresult(String rr)
{
remote=rr;
}
public String[] run2(File ff)
{
        String [] ret={"true",""};
try{    global obj=new global();
        fin=new FileInputStream(ff);

        Socket skts = new Socket(remote,5554);
	DataInputStream ins=new DataInputStream(skts.getInputStream());
	DataOutputStream outs=new DataOutputStream(skts.getOutputStream());

if (skts.isConnected()) //got connected to remote host then cont
{

 byte [] buff=new byte[1024*6];
int length=(int)ff.length();
System.out.println("sending file of length " + length);
//System.out.println("sending file of name " + ff.getName());

//outs.write(ff.getName().getBytes(),0,ff.getName().getBytes().length);
int read=0;
outs.writeInt(length);//send length
byte buf2 [] =new byte[1024];
while(read<=length)
{
int reads=fin.read(buff,0,buff.length);
if(reads==-1){break;}
read+=reads;
//System.out.println("read " + read);
outs.write(buff,0,reads);
outs.flush();
//his.sleep(50);
ins.read(buf2, 0, buf2.length);
}
int reads=ins.read(buff, 0, (buff.length));
//System.out.println("reads " + new String(buff,0,reads));
if (reads!=-1)
{
if(new String(buff, 0, reads).equalsIgnoreCase("success"))
{   ret[0]="true";
}
else
{//suc="false";
    ret[1]="false";
}}
fin.close();
outs.close();
ins.close();
skts.close();
System.out.println("conn closed");
}
else
{}//initiate again
}catch(Exception e){e.printStackTrace();
ret[0]="false";
ret[1]=e.getMessage();
//suc="false";
//err=e.getMessage();
}
       System.out.println("sending of file done");
return ret;
}
}//end of sendresult




//before calling any function call the tableexist function of systemfunction