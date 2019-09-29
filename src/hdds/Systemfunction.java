/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hdds;

import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import oracle.jdbc.OracleResultSetMetaData;

/**
 *
 * @author Harsh
 */
 class Systemfunction {
public static Connection con ;
public static boolean access=false;
public static String filename=null;
public static String sqldatabase=null;
public static String oracledatabase=null;

public String[] textfilecopy(String file1,String copy)
{   String ret[]={"true",""};
    try{
File org1=new File(file1);
File cop=new File(copy);
//loop:if (cop.createNewFile())
{   BufferedReader win =new BufferedReader(new FileReader(org1));
    BufferedWriter cin=new BufferedWriter(new FileWriter(cop));
    String read=win.readLine();
    while(read!=null)
    {
    cin.write(read);
    cin.newLine();
   //System.out.println("read " + read);
    read=win.readLine();
    }

win.close();
cin.close();
}
org1=null;
cop=null;

    }catch(Exception e){e.printStackTrace();ret[0]="false";ret[1]=e.getMessage();}
return ret;
}

public String[] accessfilecopy(String file1,String copy)
{
String ret[]={"true",""};
    try{
        File org1=new File(file1);
File cop=new File(copy);
FileInputStream inn=new FileInputStream(org1);
FileOutputStream outt=new FileOutputStream(cop);
byte [] rd=new byte[1024*10];
for (long h=org1.length();h>=0;h-=rd.length-1)
{
    inn.read(rd);
outt.write(rd);
}
inn.close();
outt.close();
 }catch(Exception e){e.printStackTrace();ret[0]="false";ret[1]=e.getMessage();}
return ret;
}



public String[] deltablefromcnf(String table,String ip,boolean onlyfrag)
{
    //File cnfcop=new File(System.getProperty("user.dir")+"/system//cnfcopy.txt");
    //if(!cnfcop.exists())
   // try{cnfcop.createNewFile();
   // }catch(Exception e){}
   System.out.println(" received  parameters are " + table + "  " + ip  + " onl  " + onlyfrag);
    textfilecopy(System.getProperty("user.dir")+"/system//cnffile.txt",System.getProperty("user.dir")+"/system//cnfcopy.txt");
    //System.out.println("part2");
    String ret []={"true",""};
    BufferedReader win=null ;
    String read="";
    String compfr="p";
    int count=1;
    int i=1;
    File cnf=null;
    try{
    if(onlyfrag)
    {compfr="f";}
    
    cnf=new File(System.getProperty("user.dir")+"/system//cnfcopy.txt");
    win =new BufferedReader(new FileReader(cnf));
    read=win.readLine();
    //System.out.println("read" + read);

    while(read!=null)
    {i++;read=win.readLine();
    //System.out.println(" reafd  " +i);
    }
    win.close();
    
    win =new BufferedReader(new FileReader(cnf));
    read=win.readLine();
    }catch(Exception e){e.printStackTrace();}
    //System.out.println(" j" + i);
    loop:for(int j=1;j<=i;j++)
    {
        try{
    String temp[]=read.split(",");
    System.out.println("temp " + temp[0]);
    if (temp[0]!=null )
        {
          String tt[]=temp[0].split("-");
          //  System.out.println("table read  " + tt[1]);
            if(tt[1]!=null && !tt[1].equalsIgnoreCase(""))
        {//System.out.println("table after split" + tt[1] );
                String tb=read.split("-")[1].split(",")[0];//tt[1];
           //    System.out.println("match " + tb.equalsIgnoreCase(table) + "j " + j + " table " + table);

                if (tb.equalsIgnoreCase(table))
                {
                //check for ip
                read=win.readLine();
                read=win.readLine();
                read=win.readLine();
                String ipt[]=read.split(":");
                read=win.readLine();
                String frag=read.split(":")[0];
                j+=4;
                if (ipt[1]!=null)
                {
                System.out.println("ip1" + "    " + ipt[1] );
                    String ips[]=ipt[1].split(";");
                System.out.println("ip " + ips[0] + " condition " + ip.equalsIgnoreCase(ips[0]) + "frag "  +  frag);
                if(onlyfrag)
                {
                    if (ip.equalsIgnoreCase(ips[0]) && frag.equalsIgnoreCase(compfr))
                {   System.out.println("break a match woth fragtrue" + count);
                    break loop;
                }
                else{
                count+=5;
                read=win.readLine();
                System.out.println("2nd read " + read);
                j+=1;
                continue loop;
                }
                
                }else
                {
                if (ip.equalsIgnoreCase(ips[0]))
                {   System.out.println("break without fragtrue " + count);
                    break loop;
                }
                else{
                count+=5;
                read=win.readLine();
                System.out.println("3nd read " + read);
                j+=1;
                continue loop;
                }





                }
                }
                }
                else
                
                {
    read=win.readLine();
    read=win.readLine();
    read=win.readLine();
    read=win.readLine();
    read=win.readLine();
    System.out.println("3nd read " + read + count + " j "+j);

     count+=5;
     j+=4;
                }
        }
    }
        
        
        }
    catch(Exception e ){e.printStackTrace();
    break loop;
    }
    }//end of while
    

    try{
        win.close();
   
        System.out.println("count " + count);
       
        File cnfnew=new File(System.getProperty("user.dir")+"/system//cnffile.txt");
    //System.out.println(cnfnew.delete()+"e " + cnfnew.exists());
    win =new BufferedReader(new FileReader(cnf));
    BufferedWriter cin=new BufferedWriter(new FileWriter(cnfnew,false));
            for (int j=1;j!=count;j++)
    {  cin.write(win.readLine());
       cin.newLine();

            }
    //System.out.println("writen" + count);
            for(int j=1;j<=5;j++)
            read=win.readLine();

            read=win.readLine();
   // System.out.println("final red" + read);
            while(read!=null)
    {
    cin.write(read);
    cin.newLine();
    read=win.readLine();
    }
    cin.close();
    win.close();

cnfnew=null;
cnf=null;

    }catch(Exception e ){
        e.printStackTrace();
   ret[0]="false";
   ret[1]=e.getMessage();
  // goto loops;
   //System.out.println("err");
    }

return ret;
}

public String[] appendfile(String file1,String fileapp,boolean append)
{String ret[]={"true",""};
try{ int i=1;
File org1=new File(file1);
File app=new File(fileapp);

    BufferedReader win =new BufferedReader(new FileReader(org1));
    BufferedWriter cin=new BufferedWriter(new FileWriter(app,append));
String read=win.readLine();
//System.out.println(read);
while(read!=null)
    {i++;
     read=win.readLine();
//System.out.println("readappend " + read +"  " +i );

    }
//System.out.println("nol" + i);
win.close();
win =new BufferedReader(new FileReader(org1));
   read=win.readLine();
for (int j=1;j<i;j++)
{
    cin.write(read);
    cin.newLine();
   //System.out.println("read " + read);
    read=win.readLine();}

win.close();
cin.close();
org1=null;
app=null;
}catch(Exception e){e.printStackTrace();
ret[0]="false";
ret[1]=e.getMessage();}

return ret;
}
   
public  void showerr(String err[],final JFrame frame,String str1)
{
    final String str2=str1;
    JButton button1= new JButton("ok");

    final JDialog errorBox=new JDialog(frame,"Error");
    errorBox.setLayout(new GridBagLayout());
    errorBox.setBounds(300,300,500,200);
    errorBox.setResizable(false);
    errorBox.setVisible(true);

    JLabel lb1= new JLabel(err[1],0);
    errorBox.add(lb1);
    lb1.setSize(500, 50);
    lb1.setVisible(true);
    errorBox.add(button1);
    button1.setBounds(200,100,70, 50);
    button1.setVisible(true);
    button1.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e)
            {   errorBox.dispose();


                /*if(str2.equalsIgnoreCase("network"))
                {
                   // errorBox.setLocationRelativeTo(frame);
                }
                //throw new UnsupportedOperationException("Not supported yet.");
              //  else errorBox.dispose();
            */}
        });



}


public String[] executearray(String sql [])
{           String ret[]={"true",""};
            System.out.println("format " + sql[0]);
            if (sql[0].equalsIgnoreCase("sql"))
            {       Statement st=null;
                    boolean exist=false;
                try{
                    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                    String connectionUrl = "jdbc:sqlserver://localhost;integratedSecurity=true";
                    Systemfunction.con = DriverManager.getConnection(connectionUrl);
                    st=Systemfunction.con.createStatement();
                    Systemfunction.sqldatabase=sql[1].split("database")[1].trim();
                    ResultSet rs= st.executeQuery("select * from sys.databases where name='"+Systemfunction.sqldatabase+"'" );
                    if(rs.next())
                         exist=true;
                       else
                       {
                    st.execute(sql[1]);//DATABASE CREATION
                       }
                    Systemfunction.con.setAutoCommit(false);
                    Systemfunction.con.setSavepoint();

                    for(int i=2;i<(sql.length-1) && !sql[i].equalsIgnoreCase("n");i++)
                     {
                      st.execute(sql[i]);
                     }

                    }catch(Exception e ){e.printStackTrace();ret[0]="false";ret[1]=e.getMessage();
                    try{
                    if(!Systemfunction.con.getAutoCommit())
                    Systemfunction.con.setAutoCommit(true);
                    if(!exist)
                    {   st.execute("use master");
                        st.execute("drop database " + Systemfunction.sqldatabase);
                    System.out.println("executing query  drop datbase" + Systemfunction.sqldatabase);

                    }
                    }
                    catch(Exception e1){e1.printStackTrace();}
                    }
            }
            if(sql[0].equalsIgnoreCase("ora"))
            {Statement st=null;
                try{
                        File info=new File(System.getProperty("user.dir")+"/system//info.txt");
                        BufferedReader in=new BufferedReader(new FileReader(info));
                        String read=in.readLine();
                        read=in.readLine();
                        read=in.readLine();
                        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                        Systemfunction.con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:"+ read,"scott" , "tiger"); //database name and password,username
                        st=Systemfunction.con.createStatement();
                        Systemfunction.oracledatabase=sql[1].split("table")[1].split("\\(")[0].trim();
                        Systemfunction.con.setAutoCommit(false);
                        Systemfunction.con.setSavepoint();

                        for(int i=1;i<(sql.length-1) && !sql[i].equalsIgnoreCase("n");i++)
                        {
                        st.executeUpdate(sql[i]);
                        }
                }catch(Exception  e){e.printStackTrace();ret[0]="false";ret[1]=e.getMessage();
                try{
                    System.out.println("drop for oracle");
                    st.execute("drop table " + Systemfunction.oracledatabase + " purge");
                }catch(Exception e1){}

                }

//System.out.println();
            }
return ret;
}

public String remove_(String filename)
{
    String file="";
    String [] temp=filename.split("\\.")[0].split("_");
    int i=0;
   /*
    for(int j=0;j<temp.length;j++)
    System.out.println("temp " + temp[j]);
    */
    if(temp.length>2)
    {
    for(  i=0;i<temp.length-2;i++)
    file=file+temp[i]+"_";
    file+=temp[i];
    }
    else
    file=temp[0];
    return file+"."+filename.split("\\.")[1];
    }

public void initializetabledata(String arrvdfil,String localfile,int time)
{
try{
File local=new File(localfile);
File arfl=new File(arrvdfil);

File cnffil =new File(System.getProperty("user.dir")+"/system//newcnffile.txt");//makes a new copy of cnffile
if(time==1)
cnffil.delete();
BufferedReader lin=new BufferedReader(new FileReader(local));
BufferedReader ain=new BufferedReader(new FileReader(arfl));
String readl="";
String reada="";

lin=new BufferedReader(new FileReader(local));
ain=new BufferedReader(new FileReader(arfl));
BufferedWriter cin=new BufferedWriter(new FileWriter(cnffil,true));

readl=lin.readLine();
String l1=readl;
String l2="";
String l4="";
String ipl="";
String ipa="";
String l3="";

String a4="";
//String frag="";
int fragc=0;
int hov=0;
//ain.mark((int)arfl.length());
int countl=1;
while(readl!=null)
{countl++;
readl=lin.readLine();
}
ain=new BufferedReader(new FileReader(arfl));
reada=ain.readLine();
int counta=1;
while(reada!=null)
{counta++;
reada=ain.readLine();
}
lin=new BufferedReader(new FileReader(local));
readl=lin.readLine();
//System.out.println("counta " + counta  + " countl " + countl);
String ofragt="";
boolean hor=false;
for(int i=1;i<countl;i++)
{
String []temp=readl.split(",");
String []nam=temp[0].split("-");
String tl=nam[1];


l2=lin.readLine();
l3=lin.readLine();
l4=lin.readLine();
//i+=3;
//System.out.println("l4 = " + l4);
String [] tcl=l4.split(":");
String []ctl=tcl[0].split("-")[1].split(",");
    //for (int  u =0;u<ctl.length;u++)
    //System.out.println("arr" + ctl[u] + u);
ipl=tcl[1];
String [] fdt=l3.split(",");
String [] fdd=fdt[1].split(";");
fragc=Integer.parseInt(fdd[0]);
String []fr=fdt[0].split("-");
if(fr.length>1)
ofragt=fr[1];
//System.out.println("fragc =" + fdd[0]	+ "for extracting for table = " + tl);
ain=new BufferedReader(new FileReader(arfl));
reada=ain.readLine();


        for(int j=1;j<counta;j++)
        {
	//get the name

		//System.out.println("read line "  + reada + j);

//module for replicated tables

		String []tempi=reada.split(",");
                String []nami=tempi[0].split("-");
		String ta=nami[1];
		//System.out.println("table name =" +nami[1] + fragc	);
                //System.out.println("a4 "+);

	if (tl.equals(ta))
	{
	//System.out.println("equals = " + tl  + ta);
         if (time==1)
        {
	ain.readLine();
	ain.readLine();
	a4=ain.readLine();

       String [] tca=a4.split(":");
	 //System.out.println("a4"+ a4+ ""+tca[0]);
	
       String [] cta=tca[0].split("-")[1].split(",");
	ipa=tca[1];
          //for (int k=0;k<cta.length;k++)
          //  System.out.println(" cta "+ cta[k]);
	int countcolloc=ctl.length;
	int countcolarv=cta.length;

	if (countcolloc!=countcolarv) //guranteed vertical
	{hov++;
	}
        else    //could be vertical or horizontal col by col checking
        {
            if(ofragt.equalsIgnoreCase("") || ofragt.equalsIgnoreCase("h"))
            {   int f=0;
                loop1:for (int d =0;d<ctl.length;d++)
                {
                loop2:for(int p=0;p<cta.length;p++)
                {
                    if(cta[p].equalsIgnoreCase(ctl[d]))
                        {f++;
                        break loop2;}
                }
                
                }
                if (f==ctl.length && f==cta.length)
                {hov=0;hor=true;}//guranteed horizontal
                else
                hov++;
            }   //end of if ofragt
       
              }//end of else
        //System.out.println("fragc =" + fragc	+ " for table = " + tl);
	fragc++;

	//compare cols for getn info bot fragmentation


	ain.readLine();
	reada=ain.readLine();
         }//end of time==1
         else
         {

            ain.readLine();
            String a3=	ain.readLine();
           // System.out.println(" sdsads " + a3);
          String[] df=a3.split(",");
         fragc=Integer.parseInt(df[1].split(";")[0]);
         ofragt=df[0].split("-")[1];
         ain.readLine();
         ain.readLine();
         reada=ain.readLine();
         //System.out.println("fragc " + fragc + " 2nd time read " + reada);
         }
        }//table name matches now match columns
	else
	{ain.readLine();
	ain.readLine();
	ain.readLine();
	ain.readLine();
	reada=ain.readLine();
	}
        j+=4;
        //System.out.println("j"  + j);
	}//end of iinner while

//add the localtable
        
if(hov>0)
ofragt="v";
if(hov==0 && hor)
ofragt="h";
hov=0;
        

//System.out.println("fragmentation type " + ofragt  + " fragc" + fragc + tl);
//cin.newLine();

cin.write(l1);
cin.newLine();
cin.write(l2);
cin.newLine();
cin.write("Frag-"+ofragt+","+fragc+";");
cin.newLine();
cin.write(l4);
cin.newLine();
cin.write(lin.readLine());
cin.newLine();

ain.close();
i+=4;
//System.out.println("i " + i);
readl=lin.readLine();
l1=readl;
fragc=0;
}//end of outer while

cin.close();
//ain.close();
lin.close();

local=null;
arfl=null;


}catch(Exception e){e.printStackTrace();}

}//end of initializetabledata

public boolean findsipduplicate(String[] str)
{int i=0;
 int j=1;
        if(str.length==1)
            return false;
        while(i< str.length-1)
        {
            while(j< str.length)
            {
                if(str[i].compareToIgnoreCase(str[j])==0)
                {
                    return true;
                }
                else j++;
            }

            i++;
            j=i+1;
        }
        return false;
}


public boolean backup(String filename)
{   boolean error=false;
    Systemfunction obj=new Systemfunction();
    try{

        File org=new File(filename);
    //System.out.println("sd" + org.getCanonicalPath() + " s " + org.isDirectory());
    if(org.isDirectory())
    { new File(System.getProperty("user.dir")+"/backup_" + org.getName()).mkdir();
      
    File temp [] =org.listFiles();
    for(int i=0;i<temp.length;i++)
    {
   // System.out.println( temp[i].toString());
    obj.textfilecopy(temp[i].toString(),  System.getProperty("user.dir")+"/backup_"+org.getName() +"//"+ temp[i].getName());
   // System.out.println(new File("backup_"+org.getName() + temp[i].getName()).getName());
    }
    }
    else
    {
        //System.out.println("asd" + new File(filename).getCanonicalPath());
    obj.textfilecopy(System.getProperty("user.dir")+"/system//"+filename, System.getProperty("user.dir")+"/backup"+filename);
    }

    }catch(Exception e){e.printStackTrace();}
    return error;

}

 public boolean tableexist(String  table)
{   boolean err=false;
        try{
        BufferedReader cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//cnffile.txt")));
        String read=cin.readLine();
        while(read!=null)
        {
        String tab=read.split("-")[1].split(",")[0];
        //System.out.println("table name is " + table);
        String dat=read.split("-")[1].split(",")[1].split(";")[0];
        //System.out.println("database " + dat);

        read=cin.readLine();
        read=cin.readLine();
        read=cin.readLine();
        String ipa=read.split(":")[1].split(";")[0];
        //System.out.println("ip  " + ipa);
        if(tab.equalsIgnoreCase(table) )
        {
        err=true;
        break;
        }
        read=cin.readLine();
        read=cin.readLine();

        }




        }catch(Exception e){e.printStackTrace();}
        return err;
    }

public boolean checkfragment(String table)
{       boolean err=false;
        try{
        BufferedReader cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//cnffile.txt")));
        String read=cin.readLine();
        while(read!=null)
        {
        String tab=read.split("-")[1].split(",")[0];
       // System.out.println("table name is " + tab.equalsIgnoreCase(table));
        read=cin.readLine();
        read=cin.readLine();
        cin.readLine();
        String tread=cin.readLine();
        String type=tread.split(":")[0].trim();
        if(table.equalsIgnoreCase(tab))
        {
            String fragt=read.split("-")[1].split(",")[0];
            if(!fragt.equalsIgnoreCase(""))
            {String no=read.split(",")[1].split(";")[0];
            
                if(fragt.equalsIgnoreCase("v") || Integer.parseInt(no)>1 || type.equalsIgnoreCase("F"))
            {//fragmentation yes
                //System.out.println("fragt " + fragt + " " + tab.equalsIgnoreCase(table));

                err=true;
                break;
            }}
        }
        read=cin.readLine();
        }
        cin.close();
        cin=null;
            }catch(Exception e){e.printStackTrace();err=true;}
        return err;

    }


public String[] qexecaray(String queryformat[])
{
String []err={"false",""};
boolean oracle=false;
try{
    //for(int i=0;i<queryformat.length;i++)
   // {
    //System.out.println("query format" + queryformat[i]);
    //}

    if(queryformat[1].equalsIgnoreCase("null"))
    queryformat[1]="";
    if(queryformat[2].equalsIgnoreCase("null"))
    queryformat[2]="";

    String []temp=queryformat[0].split("\\.");
    //Connection conn=null;
    String connectionUrl =null;
    
                        if(temp[temp.length-1].equalsIgnoreCase("mdf")) //sql format
                        {
                        String database="";
                        for(int i=0;i<temp.length-1;i++)
                        database+=temp[i];
                        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
                        connectionUrl = "jdbc:sqlserver://localhost;database="+database+";integratedSecurity=true";
                        }
                         if(temp[temp.length-1].equalsIgnoreCase("accdb") || temp[temp.length-1].equalsIgnoreCase("mdb")) //access format
                        {
                        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                        connectionUrl = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=" +  new File(System.getProperty("user.dir")+"/access//"+queryformat[0]).getAbsolutePath();
                        //connectionUrl = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=" +  new File("access//"+queryformat[0]).getAbsolutePath();
                        System.out.println("inseide access " );
                        Systemfunction.access=true;
                        }
                        
                                
                        if(!queryformat[0].contains(".mdf") && !queryformat[0].contains(".accdb") && !queryformat[0].contains(".mdb")) //oracle
                        {
                        DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                        connectionUrl = "jdbc:oracle:thin:@localhost:1521:"+ queryformat[0]; //database name and password,username
                        queryformat[1]="scott";
                        queryformat[2]="tiger";
                        oracle=true;
                        }

                                System.out.println("conurl " + connectionUrl + " usrnam " + queryformat[1] + " pass " + queryformat[2]);
                                Systemfunction.con=DriverManager.getConnection(connectionUrl,queryformat[1],queryformat[2]);
                                Statement st=Systemfunction.con.createStatement();
                                if(queryformat[4].equalsIgnoreCase("select")) //select query
                                {
                                    ResultSet rs=st.executeQuery(queryformat[3]);
                                    /*SQLServer*/ResultSetMetaData rsMetaData = /*(SQLServerResultSetMetaData) */rs.getMetaData();
                                    int totalcol=rsMetaData.getColumnCount();
                                    File retemp=new File(System.getProperty("user.dir")+"/temp/retemp.txt");
                                    BufferedWriter cout=new BufferedWriter(new FileWriter(retemp));
                                    for(int i=1;i<=totalcol;i++)
                                    cout.write(rsMetaData.getColumnLabel(i)+ "  \t" );
                                    while(rs.next())
                                    {
                                        cout.newLine();
                                    for(int i=1;i<=totalcol;i++)
                                    cout.write(rs.getString(i)+ "  \t" );
                                    }
                                    cout.close();
                                    Systemfunction.con.close();
                                    Systemfunction.con=null;
                                }
                                else if(queryformat[4].equalsIgnoreCase("truncate")) //not a select query
                                {
                                    //query execution wleft
                                            if(!Systemfunction.access)
                                            {   Systemfunction.con.setAutoCommit(false);
                                                Systemfunction.con.setSavepoint();
                                                st.executeUpdate(queryformat[3]);
                                            }else
                                                {String table=queryformat[3].split("table")[1];
                                                st.executeUpdate("delete from " + table);
                                                accessfilecopy(new File(System.getProperty("user.dir")+"/access//"+queryformat[0]).getAbsolutePath(),new File(System.getProperty("user.dir")+"/access//temp//backup_"+queryformat[0]).getAbsolutePath());
                                                Systemfunction.filename=queryformat[0];
                                                System.out.println("no from qexecarray");
                                                }
                                }else if(queryformat[4].equalsIgnoreCase("drop"))
                                    {
                                        if(oracle)
                                        st.executeUpdate(queryformat[3] + " purge");
                                        else
                                        st.executeUpdate(queryformat[3]);

                                        Systemfunction.con.close();
                                        Systemfunction.con=null;
                                    }else if(queryformat[4].equalsIgnoreCase("insert") || (queryformat[4].equalsIgnoreCase("delete") && queryformat.length==5) || (queryformat[4].equalsIgnoreCase("update") && queryformat.length==5))
                                        {
                                        if(!Systemfunction.access)
                                            {   Systemfunction.con.setAutoCommit(false);
                                                Systemfunction.con.setSavepoint();
                                                st.executeUpdate(queryformat[3]);
                                            }else
                                                {
                                                accessfilecopy(new File(System.getProperty("user.dir")+"/access//"+queryformat[0]).getAbsolutePath(),new File(System.getProperty("user.dir")+"/access//temp//backup_"+queryformat[0]).getAbsolutePath());
                                                Systemfunction.filename=queryformat[0];
                                                st.executeUpdate(queryformat[3]);
                                                //System.out.println("insert from qexecarray");
                                                }
                                        }
                                        else if(queryformat[4].equalsIgnoreCase("delete") && queryformat.length==6)
                                        {

                                            ResultSet rs=null;
                                         String queryontable=null;
                                         String prim="";
                                         if(queryformat[3].contains("from")&& queryformat[3].contains("where"))
                                             queryontable=queryformat[3].split("from")[1].split("where")[0].trim();
                                            else if(queryformat[3].contains("set"))
                                            queryontable=queryformat[3].split("update")[1].trim().split("set")[0].trim();
                                            else
                                                queryontable=null;
                                            
                                         if(Systemfunction.access)
                                        {   
                                            System.out.println("table " + queryontable);
                                            rs=Systemfunction.con.getMetaData().getBestRowIdentifier(con.getCatalog(), null,queryontable , DatabaseMetaData.bestRowTemporary, false);

                                         while(rs.next())
                                          prim+=rs.getString(2)+",";
                                         System.out.println("primeary 2" + prim);

                                         }else
                                            { System.out.println("table " + queryontable);
                                           
                                             rs=Systemfunction.con.getMetaData().getPrimaryKeys(null,null,queryontable.toUpperCase());
                                            while(rs.next())
                                            prim+=rs.getString(4)+",";
                                             System.out.println("primeary 1" + prim);

                                             }
                                         if(!prim.equalsIgnoreCase(""))
                                         {
                                             prim=prim.substring(0,prim.lastIndexOf(","));

                                        String newquery=null;
                                        if(queryformat[3].contains("from")&& queryformat[3].contains("delete"))
                                        newquery="select " + prim+" from " + queryformat[3].split("from")[1];
                                        else if(queryformat[3].contains("set") && queryformat[3].contains("where"))
                                            newquery="select "+prim+" from " + queryontable +" where "+ queryformat[3].split("where")[1];
                                        else if(queryformat[3].contains("set"))
                                            newquery="select "+prim+" from " + queryontable;
                                        


                                        System.out.println(" prim " + prim + "sqlnewquery " + newquery);

                                        Statement sta=Systemfunction.con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                                        rs=sta.executeQuery(newquery); // table name
                                        int countrow=0;
                                        rs.last();
                                        countrow=rs.getRow();
                                        err=new String[countrow+2];
                                        countrow=2;
                                        err[0]="false";
                                        err[1]="";
                                        rs.beforeFirst();
                                        while(rs.next())
                                        {err[countrow]=prim + " = "+rs.getString(1);
                                        System.out.println(err[countrow]);
                                        countrow++;
                                        }
                                        rs.close();
                                        st.close();
                                        Systemfunction.con.close();
                                         }
                                         else
                                            {err[0]="true";err[1]="No primary key found delete/update cant be performed on a table fragmented verticaly at setup.";
                                            }
                                        }   else if(queryformat[4].equalsIgnoreCase("spdelete") && queryformat[3].equalsIgnoreCase("dwm"))
                                            {
                                            if(!Systemfunction.access)
                                            {   Systemfunction.con.setAutoCommit(false);
                                                Systemfunction.con.setSavepoint();
                                                for(int j=5;j<queryformat.length;j++)
                                                {st.executeUpdate(queryformat[j]);
                                                System.out.println("query executing "  + queryformat[j]);}
                                            }else
                                                {
                                                for(int j=5;j<queryformat.length;j++)
                                                {st.executeUpdate(queryformat[j]);
                                                System.out.println("query executing "  + queryformat[j]);
                                                }
                                                accessfilecopy(new File(System.getProperty("user.dir")+"/access//"+queryformat[0]).getAbsolutePath(),new File(System.getProperty("user.dir")+"/access//temp//backup_"+queryformat[0]).getAbsolutePath());
                                                Systemfunction.filename=queryformat[0];
                                                System.out.println("insert from qexecarray");
                                                }


                                            }
                                        
                                            else if(queryformat[4].equalsIgnoreCase("selectjoin"))
                                            {

                                            Statement sta=Systemfunction.con.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE,ResultSet.CONCUR_READ_ONLY);
                                            ResultSet rs=sta.executeQuery(queryformat[3]);
                                            ResultSetMetaData rsMetaData = /*(SQLServerResultSetMetaData) */rs.getMetaData();
                                            int totalcol=rsMetaData.getColumnCount();
                                            int countrow=0;
                                            rs.last();
                                            countrow=rs.getRow();
                                            err=new String[countrow+2];
                                            countrow=2;
                                            err[0]="false";
                                            err[1]="";
                                            rs.beforeFirst();
                                            //for(int i=1;i<=totalcol;i++)
                                            //cout.write(rsMetaData.getColumnLabel(i)+ "  \t" );
                                            while(rs.next())
                                            {   String re="";
                                                for(int i=1;i<=totalcol;i++)
                                                {
                                                    String gt=rs.getMetaData().getColumnTypeName(i);
                                                    if(gt.equalsIgnoreCase("varchar") || gt.equalsIgnoreCase("varchar2")||gt.equalsIgnoreCase("nvarchar")  || gt.equalsIgnoreCase("string") || gt.equalsIgnoreCase("text") || gt.equalsIgnoreCase("clob")|| gt.equalsIgnoreCase("datetime")|| gt.equalsIgnoreCase("time")|| gt.equalsIgnoreCase("date"))
                                                    re+="'"+rs.getString(i)+"',";
                                                    else
                                                    re+=rs.getString(i)+",";
                                                }
                                                err[countrow]=re.substring(0,re.lastIndexOf(","));
                                            System.out.println(err[countrow] + " countrow" +countrow+ " re " + re);
                                            countrow++;
                                            }
                                            //else{countrow++;
                                            //err[countrow]="null";
                                            //}




                                            }else if((queryformat[4].equalsIgnoreCase("selectj2")&& queryformat[3].equalsIgnoreCase("select")))
                                            {
                                               // Systemfunction.access=false;

                                                err=new String[queryformat.length-3];
                                            err[0]="false";
                                            err[1]="";

                                             for(int j=5;j<queryformat.length;j++)
                                             {
                                                 System.out.println("query executing "  + queryformat[j]);

                                             
                                            ResultSet rs=st.executeQuery(queryformat[j]);
                                            ResultSetMetaData rsMetaData = /*(SQLServerResultSetMetaData) */rs.getMetaData();
                                            int totalcol=rsMetaData.getColumnCount();
                                             if(rs.next())
                                             {    String re="";
                                                for(int i=1;i<=totalcol;i++)
                                                {
                                                    String gt=rs.getMetaData().getColumnTypeName(i);
                                                    if(gt.equalsIgnoreCase("varchar") || gt.equalsIgnoreCase("varchar2")||gt.equalsIgnoreCase("nvarchar")  || gt.equalsIgnoreCase("string") || gt.equalsIgnoreCase("text") || gt.equalsIgnoreCase("clob")|| gt.equalsIgnoreCase("datetime")|| gt.equalsIgnoreCase("time")|| gt.equalsIgnoreCase("date"))
                                                    re+="'"+rs.getString(i)+"',";
                                                    else
                                                    re+=rs.getString(i)+",";
                                                
                                                }
                                                err[j-3]=re.substring(0,re.lastIndexOf(","));
                                            System.out.println(err[j-3]);
                                            }
                                             }
                                            }else
                                            {System.out.println("some weird request from server ");
                                            }



                }catch(Exception e){
                    e.printStackTrace();
                    err[0]="true";
                    err[1]=e.getMessage();
                   try{
                    if(Systemfunction.access)
                    Systemfunction.access=false;
                    else
                    {
                        Systemfunction.con.rollback();
                    Systemfunction.con.setAutoCommit(true);
                                                
                    }
                    Systemfunction.con.close();
                    Systemfunction.con=null;
                    Systemfunction.filename=null;
                   }catch(Exception e1){}
                   }
return err;
}


public boolean checkreplica(String datab)
{
        boolean err=false;
        try{
            


        BufferedReader cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//cnffile.txt")));
        String read=cin.readLine();
        while(read!=null)
        {
        String tab=read.split("-")[1].split(",")[0];
        cin.readLine();
        cin.readLine();
        cin.readLine();
        read=cin.readLine();
        String prim=read.split(":")[0];
        
            if((datab).equalsIgnoreCase(tab) && prim.equalsIgnoreCase("r"))
            { 
                err=true;
                break;
            }

        
        
        read=cin.readLine();
        }cin.close();
        }catch(Exception e){e.printStackTrace();}

return err;
}

public String getcols(String table,String database,int type,String username,String password)
{
    String cols="Columns are:-";
try{
if(type==1)
{
    Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
String connectionUrl = "jdbc:sqlserver://localhost;databaseName="+database+";integratedSecurity=true"; // database name,password and username
Connection connect = DriverManager.getConnection(connectionUrl,username,password);
Statement st=connect.createStatement();
ResultSetMetaData rs=st.executeQuery("select * from " + table).getMetaData();
for(int i=1;i<=rs.getColumnCount();i++)
{
cols+=""+rs.getColumnName(i) + "  " + rs.getColumnTypeName(i)+" , ";
}

}else if (type==2)
{
    Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
String conStro = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb, *.accdb)};DBQ=" +  new File(System.getProperty("user.dir")+"/access/"+database).getAbsolutePath();
Connection connect=DriverManager.getConnection(conStro,username,password);
Statement st=connect.createStatement();
ResultSetMetaData rs=st.executeQuery("select * from " + table).getMetaData();
for(int i=1;i<=rs.getColumnCount();i++)
{
cols+=""+rs.getColumnName(i) + "  " + rs.getColumnTypeName(i)+" , ";
}


}else if(type==3)
{

DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
Connection connect = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:"+ database,"scott" , "tiger"); //database name and password,username
Statement st=connect.createStatement();
OracleResultSetMetaData rs=(OracleResultSetMetaData) st.executeQuery("select * from " + table).getMetaData();
for(int i=1;i<=rs.getColumnCount();i++)
{
cols+=""+rs.getColumnName(i) + "  " + rs.getColumnTypeName(i)+" , ";
}


}
cols=cols.substring(0, cols.lastIndexOf(","));

}catch(Exception e){e.printStackTrace();}

return cols;
}

public boolean tableexistatip(String table,String ip[])
{boolean ret=false;
try{

        BufferedReader cin=new BufferedReader(new FileReader(new File(System.getProperty("user.dir")+"/system//cnffile.txt")));
        String read=cin.readLine();
        loop:while(read!=null)
        {
        String tab=read.split("-")[1].split(",")[0];
        cin.readLine();
        cin.readLine();
        read=cin.readLine();
        String ipr=read.split(":")[1].split(";")[0];
        // System.out.println("tab" + tab );
        if(tab.equalsIgnoreCase(table))
        {
        for(int i=0;i<ip.length;i++)
        {
        System.out.println("tab" + tab  + "ip " + ip[i]);

        
        if(ipr.equalsIgnoreCase(ip[i]))
        {
        ret=true;
        break loop;
        }
        }
        }
        cin.readLine();
        read=cin.readLine();
        }
        cin.close();

}catch(Exception e){e.printStackTrace();}

return ret;
}

public void addback(JFrame f)
{

JPanel jPanel2=new JPanel();
ImageIcon icon =null;
if(f.getHeight()<450)
icon = new ImageIcon(System.getProperty("user.dir")+"/pics\\background.png");
else
icon = new ImageIcon(System.getProperty("user.dir")+"/pics\\back.png");

jPanel2.setSize(icon.getIconWidth(),icon.getIconHeight());
JLabel label=new JLabel();
label.setIcon(icon);
label.setSize(icon.getIconWidth(),icon.getIconHeight());
label.setVisible(true);
jPanel2.add(label);
f.setSize(icon.getIconWidth(),icon.getIconHeight());
f.getContentPane().add(jPanel2);
f.update(f.getGraphics());
}

public int maxavailable(int type)
{int ret=0;
try{
String ipre="";
BufferedReader ipl=new BufferedReader(new FileReader(System.getProperty("user.dir")+"/system\\iplist.txt"));
//validation if file doesnt exist
ipre=ipl.readLine();
int countip=1;
while (ipre!=null)
{countip++;
ipre=ipl.readLine();
//System.out.println(ipre);
}
String ip1 []=new String[countip-1];
ipl.close();
//System.out.println("count ip " + countip);

BufferedReader inf=new BufferedReader(new FileReader(System.getProperty("user.dir")+"/system\\inf.txt"));
String inread=inf.readLine();
String temp[]=null;
countip=0;
while(inread!=null)
{
temp=inread.split(",");
if(temp.length>=1)
{if(temp[0].equalsIgnoreCase("1") && type==0)
    countip++;
}
if(temp.length>=2)
{//System.out.println("temop " + temp[1]);
    if(temp[1].equalsIgnoreCase("2") && (type==2))
countip++;
}
if(temp.length>=3)
{if(temp[2].equalsIgnoreCase("3") && (type==1))
 countip++;
}
inread=inf.readLine();
}
ret=countip;

}catch(Exception e){e.printStackTrace();}
return ret;
}

//public void addback(JFrame f)
//{


//}
/*
public static void main(String arg[])
{
Systemfunction ff=new Systemfunction();
//String ip[]={"127.0.0.1","192.168.1.2","192.168.2.4"};
//for(int i=0;i<ip.length;i++)
{
   // ff.deltablefromcnf("passenger", ip[i], false);
    //System.out.println("loop");
}
//System.out.println(ff.tableexistatip("flight", ip));
//System.out.println(ff.checkfragment("cust"));
//ff.initializetabledata("system//tempcnffile.txt", "system//cnffile.txt",1);
//ff.initializetabledata("system//temp.txt", "system//tempcnffile.txt",2);
//String sql[]={"department.accdb","","","select * from dept","yes"};
//ff.qexecaray(sql);
//ff.initializetabledata("system//cnffile.txtarivd", "system//cnffile.txt",1);
//ff.initializetabledata("system//newcnffile.txt", "system//cnffile.txtarivd",2);
// ff.filecopy("system//newcnffile.txt","system//cnffile.txt");
//boolean err=ff.checksystem();
//System.out.println("err " + err);
//ff.backup(new File("system//cnffile").getParent());
//String er[]=ff.deltablefromcnf("DEPT","Harsh-VAIO/127.0.0.1");
//System.out.println("err" + er[0] + "   sa "  + er [1] );
//ff.appendfile("system//cnfcopy", "system//cnffile", false);
//ff.appendfile("system//tempcnffile", "system//cnffile", true);
 //ff.accessfilecopy("access//airline.accdb","access//temp//airlcopy.accdb");
ff.deltablefromcnf("customer", "192.168.1.3", true);
}
  
 */
}

 
class global
{
public static boolean free=true;
public static String ip="";
public static boolean server=true;
public static boolean access=false;
public static boolean cont=true;
public static boolean update=false;
//public static boolean replica=false;
}