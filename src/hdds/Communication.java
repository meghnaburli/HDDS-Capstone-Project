/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package hdds;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JProgressBar;

/**
 *
 * @author Harsh
 */

/*
 * 5555=checking host up n sending litle commands
 * 5556=receive n send file
 * 5557=new host up n receive
 * 5558=send array n receive
 * 5559
 * 5560
 * check for the free variable set in communication if free=true then continue else dnt allow error
 * v may need timers for special conditions
    String aray gives true if success else false and then messages
 */




    class Communicate extends Thread{

	public void run(){
      try {
         
	
        while(true && global.server && global.free)
{       
        int port =5555;
	Socket skt = new Socket();

  	ServerSocket srvr = new ServerSocket(port);
	//System.out.print("Server is waiting and listening to connections!\n" );

        skt = srvr.accept();


	DataInputStream in=new DataInputStream(skt.getInputStream());
	DataOutputStream out=new DataOutputStream(skt.getOutputStream());

         //System.out.print("reading string: \n" );
         byte  b [] =new byte[50];

         if(global.server && !global.update)
        {
	
	int red=in.read(b,0,b.length);
	System.out.println("received string from server" + new String(b,0,b.length));
        String recd=new String(b,0,red);
        //System.out.println(" received string " + recd.equals("newhostup"));

   if (recd.equalsIgnoreCase("up"))
   {
   out.write("y".getBytes(),0,"y".getBytes().length);
   out.flush();
   }

        if (recd.equalsIgnoreCase("abort"))
   {
   out.write("y".getBytes(),0,"y".getBytes().length);
   out.flush();
   global.cont=false;
   System.out.println("received abort from client");
        }
        out.close();
        skt.close();
        srvr.close();

 if (recd.equals("newhostup"))
{
   //  System.out.println("new host up true");
recnewhostup ss=new recnewhostup();
ss.start();
}
        }//allowed if server true
        else
        {
                    //System.out.println("connected "  + skt.getInetAddress().toString() + "condition " + skt.getInetAddress().toString().equalsIgnoreCase(global.ip));
                    if(skt.getInetAddress().getHostAddress().equalsIgnoreCase(global.ip))
                    {   b=new byte[50];
                    int red=in.read(b,0,b.length);
                   // out.write("y".getBytes(),0,"y".getBytes().length);
                    System.out.println(" received string from server when this server false" + new String(b,0,red));
                    String recd=new String(b,0,red);
                    
                    if (recd.equalsIgnoreCase("success"))
                    {
                     out.write("y".getBytes(),0,"y".getBytes().length);
                     //commit the transfered mdb,sql,ora
                    //System.out.println("checking if access" + global.access);

                     if(global.access)
                    {
                    File dir =new File(System.getProperty("user.dir")+"/temp//");
                    String fil []=dir.list();
                    String ffile=null;
                    //System.out.println("file in temp " + dir.list()[0]);
                    Systemfunction obj=new Systemfunction();
                    String err[]=null;
                   // if(!global.replica)
                    err=obj.accessfilecopy(System.getProperty("user.dir")+"/temp//temp_"+Systemfunction.filename,System.getProperty("user.dir")+ "/access//"+Systemfunction.filename);
                    //else
                    //{err=obj.accessfilecopy("temp//temp_"+Systemfunction.filename, "access//replica//"+Systemfunction.filename);
                    //global.replica=false;
                    //}
                     //boolean ren=new File(fil).renameTo(new File("access//"+fil));
                    //System.out.println("file renaming after receiving success" + fil  + " ren " + ren) ;
                    //if(ren)
                    if(err[0].equalsIgnoreCase("true"))
                    {
                    for(int i=0;i<fil.length;i++)
                    {
                    new File(fil[0]).delete();
                    }
                    }
                    else
                    {
                    System.out.println("Couldnt rename the file and it is still in temp folder");
                        //we expect the databases to be unique n fragmentation will be allowed only once
                        //prob will rise when we replicate the the databases
                    }
                     
                    }//end of global.access
                                         else //this is for sql,ora
                                         {
                                         Systemfunction.con.commit();
                                         Systemfunction.con.setAutoCommit(true);
                                         Systemfunction.con.close();
                                         Systemfunction.con=null;
                                         Systemfunction.sqldatabase=null;
                                         Systemfunction.oracledatabase=null;
                                         }
                          }else if (recd.equalsIgnoreCase("abort"))
                                    {
                                    out.write("y".getBytes(),0,"y".getBytes().length);
                                    //roll back check if access or sql,ora
                                    System.out.println("received abort from server");
                                    File dir=new File(System.getProperty("user.dir")+"//backup_system").getCanonicalFile();
                                                                                File temp []=dir.listFiles();
                                                                                for(int i=0;i<temp.length;i++)
                                                                                {
                                                                                new Systemfunction().textfilecopy(temp[i].toString(),dir.getParentFile().getPath() + "//system//"  + temp[i].getName());
                                                                                //System.out.println("path" + dir.getParentFile().getPath() + "//system//"  + temp[i].getName()) ;

                                                                                }


                                                                                if(global.access)
                                                                                {
                                                                                dir =new File(System.getProperty("user.dir")+"/temp//");
                                                                                String fil[]=dir.list();
                                                                                for(int i=0;i<fil.length;i++)
                                                                                {new File(fil[i]).delete();
                                                                                }
                                                                                }else
                                                                                    {Systemfunction.con.rollback();
                                                                                     Systemfunction.con.setAutoCommit(true);
                                                                                     if(Systemfunction.sqldatabase!=null)
                                                                                     Systemfunction.con.createStatement().execute("drop datbase " + Systemfunction.sqldatabase);
                                                                                     if(Systemfunction.oracledatabase!=null)
                                                                                     Systemfunction.con.createStatement().execute("drop table " + Systemfunction.oracledatabase + " purge");
                                                                                     Systemfunction.con.close();
                                                                                     Systemfunction.con=null;//dont commit
                                                                                     Systemfunction.sqldatabase=null;
                                                                                    }
                                    }else if(recd.equalsIgnoreCase("successupdate"))
                                            {
                                            global.update=false;
                                            System.out.println(" successful update");
                                            out.write("y".getBytes(),0,"y".getBytes().length);
                                            out.flush();
                                            }
                                            else if(recd.equalsIgnoreCase("abortup"))
                                                    {
                                                    global.update=false;
                                                    File dir=new File(System.getProperty("user.dir")+"/backup_system").getCanonicalFile();
                                                    File temp []=dir.listFiles();
                                                    for(int i=0;i<temp.length;i++)
                                                    {
                                                    new Systemfunction().textfilecopy(temp[i].toString(),dir.getParentFile().getPath() + "//system//"  + temp[i].getName());
                                                    //System.out.println("path" + dir.getParentFile().getPath() + "//system//"  + temp[i].getName()) ;
                                                    }
                                                    System.out.println("abort update");
                                                    out.write("y".getBytes(),0,"y".getBytes().length);
                     
                                                    }  
                                                     else
                                                        {
                                                        out.write("y".getBytes(),0,"y".getBytes().length);
                                                        //error sum other string sent.leads to deadlock.handling left
                                                        System.out.println("received string from server when this server false " + recd);
                                                        }
                   
                    out.close();
                    skt.close();
                    srvr.close();
                    global.ip="";
                    global.server=true;
                    global.access=false;
                    global.free=true;
                    System.out.println("end of communicate");
                    }else
                    {
                    System.out.println("Blocked from Server");
                 out.write("blocked".getBytes(),0,"blocked".getBytes().length);
                  out.close();
                    skt.close();
                        srvr.close();
                    }
        }
//new Communicate().start();
     
}//end of while


}catch(Exception e ){
   // System.out.println("error thrown from communicate");
    //e.printStackTrace();

}

        }
}//end of Server


class initiatefiletransfer extends Thread{
String remote="";
FileInputStream fin;
initiatefiletransfer(String rr)
{
remote=rr;
}
public String[] run2(File ff)
{
        String [] ret={"true",""};
try{    global obj=new global();
        fin=new FileInputStream(ff);
        
        Socket skts = new Socket(remote,5556);
	DataInputStream ins=new DataInputStream(skts.getInputStream());
	DataOutputStream outs=new DataOutputStream(skts.getOutputStream());

if (skts.isConnected()) //got connected to remote host then cont
{

 byte [] buff=new byte[1024];
int length=(int)ff.length();
System.out.println("sending file of length " + length);
System.out.println("sending file of name " + ff.getName());

outs.write(ff.getName().getBytes(),0,ff.getName().getBytes().length);
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
if(new String(buff, 0, reads).equalsIgnoreCase("suces"))
{//suc="true";
    ret[0]="true";
}
else
{//suc="false";
    ret[1]="false";
}}
fin.close();
outs.close();
ins.close();
skts.close();
//System.out.println("conn closed");
}
else
{}//initiate again
}catch(Exception e){e.printStackTrace();
ret[0]="false";
ret[1]=e.getMessage();
//suc="false";
//err=e.getMessage();
}
  //      System.out.println("sending of file done");
return ret;
}
}//end of initiatefiletransfer


class initiatearraytransfer extends Thread
{
String remote;
String []sql;
initiatearraytransfer(String rr,String [] ss)
{
remote=rr;
sql=ss;
}
public String[] run2(){
 String ret[]={"true",""};
    try{
   

        Socket skts = new Socket(remote,5558);
	DataInputStream ins=new DataInputStream(skts.getInputStream());
	DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
	
if (skts.isConnected()) //got connected to remote host then cont
{byte [] buff=new byte[100];
 int reads=0;
outs.writeInt(sql.length);
for(int i=0;i<sql.length;i++)
{
    if(sql[i]!=null)
    {outs.write(sql[i].getBytes(),0,sql[i].getBytes().length);
    reads=ins.read(buff, 0, (buff.length));
    //System.out.println("reads " + new String(buff,0,reads));
    }
    else
    {
    outs.write("n".getBytes(),0,"n".getBytes().length);
    reads=ins.read(buff, 0, (buff.length));
  //  System.out.println("reads " + new String(buff,0,reads));
    }
}
//outs.writeInt(-1);
reads=ins.read(buff, 0, (buff.length));
//System.out.println("reads succersswala" + new String(buff,0,reads));

if (reads!=-1)
{
if(new String(buff, 0, reads).equalsIgnoreCase("suces"))
{
ret[0]="true";
}
else
{System.out.println(" error from iniatearay" + new String(buff, 0, reads));
    ret[0]="false";
}}
outs.close();
ins.close();
skts.close();
}
else {} //initiate again

}catch(Exception e){e.printStackTrace();
ret[0]="false";
ret[1]=e.getMessage();}

return ret;
}
}//end of array transfer

//put the global free checking where it is called
class newhostup extends Thread{
String []remote;
JFrame frame=null;
newhostup(String rem [],JFrame f)
{remote=rem;
frame=f;
}
    

public String[] run2()
{                                                                                           int w=0;
                                                                                            final JDialog msgbox=new JDialog(frame,"Message");
                                                                                            GridBagConstraints c = new GridBagConstraints();
                                                                                            msgbox.setLayout(new GridBagLayout());
                                                                                            msgbox.setSize(900,400);
                                                                                            msgbox.setResizable(false);
                                                                                            c.fill = GridBagConstraints.HORIZONTAL;
                                                                                            c.gridwidth=1;
                                                                                            c.gridx=0;
                                                                                            c.gridy=w;
                                                                                            JLabel lbl=new JLabel("1.Finding New Nodes");
                                                                                            msgbox.add(lbl,c);
                                                                                           // msgbox.pack();
                                                                                            //msgbox.setSize((int)msgbox.getSize().getWidth()*2,(int)msgbox.getSize().getHeight()*2);
                                                                                            msgbox.setLocationRelativeTo(frame);
                                                                                            msgbox.setVisible(true);


String ipblocked []=null;
String ret[]={"false","",""};

try{
loop1:for (int i=0;i<remote.length ;i++)
 {
Socket skts = new Socket();
try{
        if(remote[i].equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress()))
        continue;
        
	System.out.println("remote host " + remote[i]);
	skts.connect(new InetSocketAddress(remote[i],5555),100);
	//System.out.println("connected = " + skts.isConnected());
}catch(Exception e ){
    //e.printStackTrace();
    continue;}


if (skts.isConnected()) //got connected to remote host then cont
{       ret[2]="Found a new node at ip " +skts.getInetAddress().getHostAddress();
        
                                                                                     w=w+2;
                                                                                    c.gridy=w+2;
                                                                                    msgbox.add(new JLabel("2.Found a new node.Trying to connect to ip:" +ret[2]),c);
                                                                                     //msgbox.pack();
                                                                                            //msgbox.setSize((int)msgbox.getSize().getWidth()*2,(int)msgbox.getSize().getHeight()*2);

                                                                                            msgbox.update(msgbox.getGraphics());
                                                                                            msgbox.paintAll(msgbox.getGraphics());


	DataInputStream ins=new DataInputStream(skts.getInputStream());
	DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
	outs.write("newhostup".getBytes(),0,"newhostup".length());
	outs.flush();
	outs.close();
	ins.close();
	skts.close();

System.out.println("server tryin to connect on  5557");
Socket skt = new Socket(remote[i],5557);
DataInputStream in=new DataInputStream(skt.getInputStream());
DataOutputStream out=new DataOutputStream(skt.getOutputStream());

if (skt.isConnected())
{
    byte bufs []=new byte[1024*15];

int fl=in.read(bufs,0,bufs.length);
String filename=new String(bufs,0,fl);

//module to remove uderscore from the file
                                                                                            w=w+2;
                                                                                            c.gridy=w+2;
                                                                                            msgbox.add(new JLabel("3.Receiving Files...Please wait"),c);
                                                                                            //msgbox.pack();
                                                                                            //msgbox.setSize((int)msgbox.getSize().getWidth()*2,(int)msgbox.getSize().getHeight()*2);

                                                                                            msgbox.update(msgbox.getGraphics());
                                                                                            msgbox.paintAll(msgbox.getGraphics());

FileOutputStream ffo=new FileOutputStream(System.getProperty("user.dir")+"//system//"+filename);
System.out.println("Receiving file  " + filename);
int length=in.readInt();//read length;

int read=0;
//System.out.println("read length" + length);

while(read<length)
{
int reads=in.read(bufs,0,bufs.length);
if(reads==-1){break;}
ffo.write(bufs,0,reads);
read+=reads;
//System.out.println(" read " + reads + " r " + read +  "  "+(read<=length));
out.write("delay".getBytes(), 0,"delay".getBytes().length);
out.flush();
}
//System.out.println("closed");
//int j=0;
//while(j<=500)
//{
out.write("suces".getBytes(),0 , ("suces".getBytes().length));
out.flush();
//j++;}
//cnffile
 fl=in.read(bufs,0,bufs.length);
filename=new String(bufs,0,fl);

//module to remove uderscore from the file

ffo=new FileOutputStream(System.getProperty("user.dir")+"/system//"+filename+ "arivd");
System.out.println("Rreceiving file" + filename);
length=in.readInt();//read length;

read=0;
//System.out.println("read length" + length);

while(read<length)
{
int reads=in.read(bufs,0,bufs.length);
if(reads==-1){break;}
ffo.write(bufs,0,reads);
read+=reads;
//System.out.println(" read " + reads + " r " + read +  "  "+(read<=length));
out.write("delay".getBytes(), 0,"delay".getBytes().length);
out.flush();
}
//System.out.println("closed");
//int j=0;
//while(j<=500)
//{
out.write("suces".getBytes(),0 , ("suces".getBytes().length));
out.flush();


ffo.close();
//inf file
 fl=in.read(bufs,0,bufs.length);
filename=new String(bufs,0,fl);

//module to remove uderscore from the file

ffo=new FileOutputStream(System.getProperty("user.dir")+"/system//"+filename + "arivd");
System.out.println("Receiving filename is " + filename);
length=in.readInt();//read length;

read=0;
//System.out.println("read length" + length);

while(read<length)
{
int reads=in.read(bufs,0,bufs.length);
if(reads==-1){break;}
ffo.write(bufs,0,reads);
read+=reads;
//System.out.println(" read " + reads + " r " + read +  "  "+(read<=length));
out.write("delay".getBytes(), 0,"delay".getBytes().length);
out.flush();
}
//System.out.println("closed");
//int j=0;
//while(j<=500)
//{
out.write("suces".getBytes(),0 , ("suces".getBytes().length));
out.flush();


ffo.close();
in.close();
out.close();
skt.close();
}
else
{
System.out.println("not connected");
continue;
}//initiate again

ret[0]="false";
break loop1;
}
}//end of loop1

//add own ip to file and send file to all host,left with cnffile and checking of file 
//
boolean alreadyipexist=false;
BufferedReader win=new BufferedReader(new FileReader(System.getProperty("user.dir")+"/system//iplist.txt"));
String read="";
read=win.readLine();
while(read!=null)
{
    if(read.equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress()))
    {alreadyipexist=true;
    break;
    }
read=win.readLine();
}
win.close();
if(!alreadyipexist)
{
                                                                                            w=w+2;
                                                                                            c.gridy=w+2;
                                                                                            msgbox.add(new JLabel("3.Updating Recieved Files...Please wait"),c);
                                                                                            //msgbox.pack();
                                                                                            //msgbox.setSize((int)msgbox.getSize().getWidth()*2,(int)msgbox.getSize().getHeight()*2);

                                                                                            msgbox.update(msgbox.getGraphics());
                                                                                            msgbox.paintAll(msgbox.getGraphics());


new Systemfunction().backup(new File(System.getProperty("user.dir")+"/system//cnffile.txt").getParent());
BufferedWriter wout=new BufferedWriter(new FileWriter(System.getProperty("user.dir")+"/system//iplist.txt",true));
InetAddress hip=InetAddress.getLocalHost();
String host=hip.getHostAddress();
wout.newLine();
wout.write(host);
wout.close();
             Systemfunction obj=new Systemfunction();
             obj.initializetabledata(System.getProperty("user.dir")+"/system//cnffile.txtarivd", System.getProperty("user.dir")+"/system//cnffile.txt",1);
             obj.initializetabledata(System.getProperty("user.dir")+"/system//newcnffile.txt", System.getProperty("user.dir")+"/system//cnffile.txtarivd",2);
             obj.textfilecopy(System.getProperty("user.dir")+"/system//newcnffile.txt",System.getProperty("user.dir")+"/system//cnffile.txt");
             obj.appendfile(System.getProperty("user.dir")+"/system//inf.txt", System.getProperty("user.dir")+"/system//inf.txtarivd", true);
             obj.textfilecopy(System.getProperty("user.dir")+"/system//inf.txtarivd",System.getProperty("user.dir")+"/system//inf.txt");












win=new BufferedReader(new FileReader(System.getProperty("user.dir")+"/system//iplist.txt"));
read="";
int u=0;
int suc=0;
String transerr[]=null;

read=win.readLine();
u=1;
while(read!=null)
{
    if(read.equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress()))
     read=win.readLine();
 //  System.out.println("sending files now" +read );
u++;
read=win.readLine();
}
win.close();





                                                                                            w=w+2;
                                                                                            c.gridy=w+2;
                                                                                            msgbox.add(new JLabel("4.Sending Updated Configuration Files to all nodes."),c);
                                                                                            w=w+2;
                                                                                            c.gridy=w+2;
                                                                                            msgbox.add(new JLabel("Please Wait...This may take few minutes."),c);
                                                                                            JProgressBar pro=new JProgressBar(0,(u-1)*5*3);
                                                                                            pro.setValue(1);
                                                                                            pro.setForeground(Color.GREEN);
                                                                                            pro.setSize(msgbox.getSize());
                                                                                            pro.setVisible(true);
                                                                                            w=w+2;
                                                                                            c.gridy=w+2;
                                                                                            msgbox.add(new JLabel("2.Sending files now "),c);
                                                                                            c.gridx=1;

                                                                                            msgbox.add(pro,c);
                                                                                            //msgbox.pack();
                                                                                           // msgbox.setSize((int)msgbox.getSize().getWidth()*2,(int)msgbox.getSize().getHeight()*2);

                                                                                            msgbox.update(msgbox.getGraphics());
                                                                                            msgbox.paintAll(msgbox.getGraphics());
                                                                                            c.gridx=0;
                                                                                            //msgbox.pack();
                                                                                            //msgbox.setSize((int)msgbox.getSize().getWidth()*2,(int)msgbox.getSize().getHeight()*2);

                                                                                            msgbox.update(msgbox.getGraphics());
                                                                                            msgbox.paintAll(msgbox.getGraphics());









int val=1;
ipblocked=new String[u];
u=0;
win=new BufferedReader(new FileReader(System.getProperty("user.dir")+"/system//iplist.txt"));
read=win.readLine();
while(read!=null)
{   
    if(read.equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress()))
    read=win.readLine();
    System.out.println("read " + read);
    ipblocked[u++]=read;
    System.out.println("sending files now" +read );
    
    if(read!=null)
    {initiatefiletransfer ff=  new initiatefiletransfer(read);
    transerr=ff.run2(new File(System.getProperty("user.dir")+"/system//iplist.txt"));
    ff.setPriority(Thread.MAX_PRIORITY);

   

    if(transerr[0].equalsIgnoreCase("false"))
    {
    //we got an error while transfering file stop updation n sent abortup to all blocked clients
    new comm("abortup",ipblocked).run2();
    suc=1;
    break;
    }
    }
     pro.setValue(val*5);
    val++;
    pro.update(pro.getGraphics());

    read=win.readLine();

}
win=new BufferedReader(new FileReader(System.getProperty("user.dir")+"/system//iplist.txt"));
read="";
read=win.readLine();
while(read!=null && suc==0)
{
    if(read.equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress()))
    read=win.readLine();
if(read!=null)
{initiatefiletransfer ff=  new initiatefiletransfer(read);
transerr=ff.run2(new File(System.getProperty("user.dir")+"/system//cnffile.txt"));
ff.setPriority(Thread.MAX_PRIORITY);


if(transerr[0].equalsIgnoreCase("false"))
    {
    //we got an error while transfering file stop updation n sent abortup to all blocked clients
    new comm("abortup",ipblocked).run2();
    suc=1;
    break;
    }
}

        pro.setValue(val*5);
    val++;
    pro.update(pro.getGraphics());



read=win.readLine();
}
win=new BufferedReader(new FileReader(System.getProperty("user.dir")+"/system//iplist.txt"));
read="";
read=win.readLine();
while(read!=null && suc==0)
{
    if(read.equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress()))
    read=win.readLine();
 if(read!=null)
 {initiatefiletransfer ff=  new initiatefiletransfer(read);
transerr=ff.run2(new File(System.getProperty("user.dir")+"/system//inf.txt"));
ff.setPriority(Thread.MAX_PRIORITY);

    

//transerr[0]="fasle";
if(transerr[0].equalsIgnoreCase("false"))
    {
    //we got an error while transfering file stop updation n sent abortup to all blocked clients
    new comm("abortup",ipblocked).run2();
    suc=1;
    break;
    }

 }

        pro.setValue(val*5);
    val++;
    pro.update(pro.getGraphics());



read=win.readLine();
}
win.close();
//delete all the arived files
if(suc==1)
{
//error while sending
//roll back everything
                                                                                            w=w+2;
                                                                                            c.gridy=w+2;
                                                                                            msgbox.add(new JLabel("4.Rolling back...Error encountered during updating of files"),c);
                                                                                            //msgbox.pack();
                                                                                            //msgbox.setSize((int)msgbox.getSize().getWidth()*2,(int)msgbox.getSize().getHeight()*2);
                                                                                             msgbox.update(msgbox.getGraphics());
                                                                                            msgbox.paintAll(msgbox.getGraphics());


 File dir=new File(System.getProperty("user.dir")+"//backup_system").getCanonicalFile();
 File temp []=dir.listFiles();
 for(int i=0;i<temp.length;i++)
 {
 new Systemfunction().textfilecopy(temp[i].toString(),dir.getParentFile().getPath() + "//system//"  + temp[i].getName());
 //System.out.println("path" + dir.getParentFile().getPath() + "//system//"  + temp[i].getName()) ;
 }
                                                                                            JButton ok=new JButton("OK");
                                                                                            ok.setSize(40, 20);
                                                                                            ok.setVisible(true);
                                                                                            ok.addActionListener(new ActionListener() {

                                                                                              public void actionPerformed(ActionEvent e) {
                                               msgbox.setVisible(false);
                                               msgbox.update(msgbox.getGraphics());
                                               msgbox.paintAll(msgbox.getGraphics());

                                                }
                                                                                              });
                                                                                            w=w+2;
                                                                                              c.gridy=w=w+2;
                                                                                            msgbox.add(ok);
                                                                                            //msgbox.pack();
                                                                                            //msgbox.setSize((int)msgbox.getSize().getWidth()*2,(int)msgbox.getSize().getHeight()*2);

                                                                                            msgbox.update(msgbox.getGraphics());
                                                                                            msgbox.paintAll(msgbox.getGraphics());

}
else
{
//rite successupdate to all blocked ips
                                                                                        

String er[]=new comm("successupdate",ipblocked).run2();
if(er[0].equalsIgnoreCase("true"))                                                        
{
      w=w+2;
    c.gridy=w+2;
                                                                                            msgbox.add(new JLabel("Successfully Updated all nodes."),c);
                                                                                            JButton ok=new JButton("OK");
                                                                                            ok.setSize(40, 20);
                                                                                            ok.setVisible(true);
                                                                                            ok.addActionListener(new ActionListener() {

                                                                                              public void actionPerformed(ActionEvent e) {
                                               msgbox.setVisible(false);
                                               msgbox.update(msgbox.getGraphics());
                                               msgbox.paintAll(msgbox.getGraphics());

                                                }
                                                                                              });
                                                                                            w=w+2;
                                                                                              c.gridy=w=w+2;
                                                                                            msgbox.add(ok);
                                                                                            //msgbox.pack();
                                                                                           // msgbox.setSize((int)msgbox.getSize().getWidth()*2,(int)msgbox.getSize().getHeight()*2);

                                                                                            msgbox.update(msgbox.getGraphics());
                                                                                            msgbox.paintAll(msgbox.getGraphics());
                    }
                    else
                    {
                                                                                            msgbox.setVisible(false);
                                                                                             msgbox.update(msgbox.getGraphics());
                                                                                            msgbox.paintAll(msgbox.getGraphics());
                                                                                            final JDialog errbox=new JDialog(frame,"Error");
                                                                                            errbox.setLayout(new GridBagLayout());
                                                                                            c.fill = GridBagConstraints.HORIZONTAL;
                                                                                            c.gridwidth=1;
                                                                                            c.gridx=0;
                                                                                            c.gridy=1;
                                                                                            JLabel lb=new JLabel("Software caused a irrecoverale error please perform setup again on all nodes");
                                                                                            errbox.add(lb,c);
                                                                                            JButton ok=new JButton("OK");
                                                                                            ok.setSize(40, 20);
                                                                                            ok.setVisible(true);
                                                                                            ok.addActionListener(new ActionListener() {

                                                                                              public void actionPerformed(ActionEvent e) {
                                               errbox.setVisible(false);
                                               errbox.update(msgbox.getGraphics());
                                               errbox.paintAll(msgbox.getGraphics());

                                                }
                                                                                              });
                                                                                            c.gridy=2;
                                                                                            errbox.add(ok,c);
                                                                                            
                                                                                            errbox.setSize(600,400);
                                                                                            errbox.setResizable(false);
                                                                                            errbox.setLocationRelativeTo(frame);
                                                                                            errbox.setVisible(true);

                    }
}
new File(System.getProperty("user.dir")+"/system//cnffile.txtarivd").delete();
new File(System.getProperty("user.dir")+"/system//inf.txtarivd").delete();
new File(System.getProperty("user.dir")+"/system//newcnffile.txtarivd").delete();
}//end of alreadyipexist
else
{
    msgbox.setVisible(false);
    msgbox.dispose();


     final JDialog errbox=new JDialog(frame,"Error");
                                                                                            errbox.setLayout(new GridBagLayout());
                                                                                            c.fill = GridBagConstraints.HORIZONTAL;
                                                                                            c.gridwidth=1;
                                                                                            c.gridx=0;
                                                                                            c.gridy=1;
                                                                                            JLabel lb=new JLabel("Your node is on the network");
                                                                                            errbox.add(lb,c);
                                                                                            JButton ok=new JButton("OK");
                                                                                            ok.setSize(40, 20);
                                                                                            ok.setVisible(true);
                                                                                            ok.addActionListener(new ActionListener() {

                                                                                              public void actionPerformed(ActionEvent e) {
                                               errbox.setVisible(false);
                                               errbox.update(msgbox.getGraphics());
                                               errbox.paintAll(msgbox.getGraphics());

                                                }
                                                                                              });
                                                                                            c.gridy=2;
                                                                                            errbox.add(ok,c);
                                                                                            errbox.pack();
                                                                                            errbox.setSize(400,200);
                                                                                            errbox.setResizable(false);
                                                                                            errbox.setLocationRelativeTo(frame);
                                                                                            errbox.setVisible(true);





System.out.println("IP already found in file");
//ret[0]="true";
//ret[1]="Your node is on network.";
}
}
catch(Exception e)
{
ret[0]="true";
ret[1]=e.getMessage();
e.printStackTrace();
}
return ret;
}//end of run
}//end of newhost up
//end of client initiated connections

class recnewhostup extends Thread{
@Override
public void run()
{
try{

Socket sktt = new Socket();
ServerSocket srvrt = new ServerSocket(5557); //transfer
System.out.print("waiting on 5557 new host up");

sktt = srvrt.accept();
//waiting to send the ip file to the newly up host

	DataInputStream in=new DataInputStream(sktt.getInputStream());
	DataOutputStream out=new DataOutputStream(sktt.getOutputStream());

if (sktt.isConnected()) //got connected to remote host then cont
{
File ff=new File(System.getProperty("user.dir")+"/system//iplist.txt");
FileInputStream fin=new FileInputStream(ff);

String ret[]={"true",""};
byte [] buff=new byte[1024*15];
int length=(int)ff.length();
System.out.println("sending length" + length);

out.write(ff.getName().getBytes(),0,ff.getName().getBytes().length);
int read=0;
out.writeInt(length);//send length
byte buf2 [] =new byte[1024];
while(read<=length)
{
int reads=fin.read(buff,0,buff.length);
if(reads==-1){break;}
read+=reads;
//System.out.println("read " + read);
out.write(buff,0,reads);
out.flush();
//this.sleep(100);
in.read(buf2, 0, buf2.length);
}
int reads=in.read(buff, 0, (buff.length));
//System.out.println("reads " + new String(buff,0,reads));
if (reads!=-1)
{
if(new String(buff, 0, reads).equalsIgnoreCase("suces"))
{//suc="true";
    ret[0]="true";
}
else
{//suc="false";
    ret[1]="false";
}}
fin.close();
//out.close();
//in.close();
//sktt.close();
//srvrt.close();
//if succesful in sending ip file then send cnnfile
if (ret[0].equalsIgnoreCase("true"))
{
File ffs=new File(System.getProperty("user.dir")+"/system//cnffile.txt");
FileInputStream fins=new FileInputStream(ffs);
String rets[]={"true",""};
length=(int)ffs.length();
System.out.println("sending length" + length);
out.write(ffs.getName().getBytes(),0,ffs.getName().getBytes().length);
read=0;
out.writeInt(length);//send length
buf2=new byte[1024];
while(read<=length)
{
reads=fins.read(buff,0,buff.length);
if(reads==-1){break;}
read+=reads;
//System.out.println("read " + read);
out.write(buff,0,reads);
out.flush();
//this.sleep(100);
in.read(buf2, 0, buf2.length);
}
reads=in.read(buff, 0, (buff.length));
//System.out.println("reads " + new String(buff,0,reads));
if (reads!=-1)
{
if(new String(buff, 0, reads).equalsIgnoreCase("suces"))
{//suc="true";
    rets[0]="true";
}
else
{//suc="false";
    rets[1]="false";
}}
fin.close();


ffs=new File(System.getProperty("user.dir")+"/system//inf.txt");
fins=new FileInputStream(ffs);
//String rets[]={"true",""};
length=(int)ffs.length();
System.out.println("sending length" + length);
out.write(ffs.getName().getBytes(),0,ffs.getName().getBytes().length);
read=0;
out.writeInt(length);//send length
buf2=new byte[1024];
while(read<=length)
{
reads=fins.read(buff,0,buff.length);
if(reads==-1){break;}
read+=reads;
//System.out.println("read " + read);
out.write(buff,0,reads);
out.flush();
//this.sleep(100);
in.read(buf2, 0, buf2.length);
}
reads=in.read(buff, 0, (buff.length));
//System.out.println("reads " + new String(buff,0,reads));
if (reads!=-1)
{
if(new String(buff, 0, reads).equalsIgnoreCase("suces"))
{//suc="true";
    rets[0]="true";
}
else
{//suc="false";
    rets[1]="false";
}}
fin.close();

out.close();
in.close();
sktt.close();
srvrt.close();

}
new recfiletransfer().run();

}//end of if connected

    /*
File ff=new File("ipfile.txt");
FileInputStream fin=new FileInputStream(ff);
byte [] buff=new byte[1024*2];
int length=(int)ff.length();
System.out.println("sending length" + length);

out.write(ff.getName().getBytes(),0,ff.getName().getBytes().length);
int read=0;
out.writeInt(length);//send length

while(read<=length)
{
int reads=fin.read(buff,0,buff.length);
if(reads==-1){break;}
read+=reads;
//System.out.println("read " + read);
out.write(buff,0,reads);
out.flush();
}
out.write("suces".getBytes(),0 , ("suces".getBytes().length));
out.flush();
//out.writeInt(-1);
fin.close();
out.close();
in.close();
sktt.close();
srvrt.close();
//System.out.println("close = " + srvrt.isClosed());
*/


}catch(Exception e){e.printStackTrace();}
}//end of run
}//end of recnewhostup

class recfiletransfer extends Thread{
public void run(){
DataInputStream in=null;
DataOutputStream out=null;
Socket sktt = null;
ServerSocket srvrt = null; //transfer

    try{
sktt = new Socket();
srvrt = new ServerSocket(5556); //transfer
//System.out.print("waiting on 5556");

sktt = srvrt.accept();


in=new DataInputStream(sktt.getInputStream());
out=new DataOutputStream(sktt.getOutputStream());
if(global.free)
{
                byte bufs []=new byte[1024];
                global.ip=sktt.getInetAddress().getHostAddress();
                global.server=false;
System.out.println("connected to ip for file transfer " + global.ip);
int fl=in.read(bufs,0,bufs.length-1);
String filename=new String(bufs,0,fl);


//module to remove uderscore from the file

   Systemfunction obj=new Systemfunction();

String ext[] =filename.split("\\.");

    if(ext[ext.length-1].equalsIgnoreCase("mdb") || ext[ext.length-1].equalsIgnoreCase("accdb"))
{

    filename=obj.remove_(filename);
    Systemfunction.filename=filename;
    
    filename=System.getProperty("user.dir")+"/temp//"+"temp_"+filename;
    global.access=true;

 //   if(filename.contains("$R"))
  //  global.replica=true;
    
}//access file may come from fragmentation store it as temp
if(ext[ext.length-1].equalsIgnoreCase("txt"))
{   //take backup coz file transfer may corupt the system files only during update or frag,repl when cnffile would be received
    if(filename.equalsIgnoreCase(System.getProperty("user.dir")+"/cnffile.txt"))
    obj.backup(new File(System.getProperty("user.dir")+"/system//cnffile.txt").getParent());
    if(filename.equalsIgnoreCase("iplist.txt"))
    {
    global.update=true;
    obj.backup(new File(System.getProperty("user.dir")+"/system//cnffile.txt").getParent());
    System.out.println("took backup");
    }

        filename=System.getProperty("user.dir")+"/system//"+filename;
//System.out.println("filename " + filename );
}

    

//setting the path left.file should go into system//access
FileOutputStream ffo=new FileOutputStream(filename);
//System.out.println("filename is " + filename);
int length=in.readInt();//read length;
int read=0;
System.out.println("read length" + length);

while(read<length)
{
int reads=in.read(bufs,0,bufs.length);
if(reads==-1){break;}
ffo.write(bufs,0,reads);
read+=reads;
//System.out.println(" read " + reads + " r " + read +  "  "+(read<=length));
out.write("delay".getBytes(), 0,"delay".getBytes().length);
out.flush();
}
System.out.println("received file ");
out.write("suces".getBytes(),0 , ("suces".getBytes().length));
out.flush();
if(length==0)
{
global.ip="";
global.server=true;
global.free=true;
}

ffo.close();
in.close();
out.close();
sktt.close();
srvrt.close();
ffo=null;
new recfiletransfer().start();
}
else
{//blocked state
    
}

}catch(Exception e){
    e.printStackTrace();
    try{
    out.write("abort".getBytes(),0 , ("abort".getBytes().length));
    out.flush();
    in.close();
    out.close();
    sktt.close();
    srvrt.close();
    global.server=true;
    global.access=false;
   // global.replica=false;
    global.cont=true;
    global.update=false;
    global.ip="";
    new recfiletransfer().start();

    }catch(Exception e1){}
}
}
}//end of filetransfer

class rectransferofarray extends Thread{
public void run(){
        Socket sktt = null;
        ServerSocket srvrt = null; //transfer
        DataInputStream in=null;
	DataOutputStream out=null;
        try{
        sktt = new Socket();
        srvrt = new ServerSocket(5558); //transfer
	//System.out.print("waiting on 5558 ");
        sktt = srvrt.accept();
	in=new DataInputStream(sktt.getInputStream());
	out=new DataOutputStream(sktt.getOutputStream());
if( global.free)
{
global.ip=sktt.getInetAddress().getHostAddress();
    System.out.println("got connected for arraytransfer to ip " +global.ip);

byte bufs []=new byte[1024];
int length=in.readInt();
String sql[]=new String[length];
for(int j=0;j<length;j++)
{
int red=0;
//while(red!=-1)
//{
    red=in.read(bufs,0,bufs.length-1);

        out.write("ss".getBytes(),0,"ss".getBytes().length);
    if(red==-1){break;}
sql[j]=new String(bufs,0,red);
//System.out.println(" received String " + sql[j] + " length " + length + " j"  + red);
out.flush();
}
//}


System.out.println("successfully received array now executing ");

Systemfunction obj=new Systemfunction();
String err[]=obj.executearray(sql);
if(err[0].equalsIgnoreCase("true"))
{System.out.println("successfully executed array");
out.write("suces".getBytes(),0 , ("suces".getBytes().length));
out.flush();

}else
{   out.write("abort".getBytes(),0 , ("abort".getBytes().length));
    out.flush();

    String ip[]={global.ip};
    new comm("abort",ip).run2();
    System.out.println("sendong abort to server coz cudnt execute array");
    //send abort to the server so that it stops sending to other ips

}


}
else
{//blocked
System.out.println("Blocked from receive array");
}

in.close();
out.close();
sktt.close();
srvrt.close();
new rectransferofarray().start();
}catch(Exception e){
    e.printStackTrace();
    try{
    out.write("abort".getBytes(),0 , ("abort".getBytes().length));
    out.flush();
    in.close();
    out.close();
    sktt.close();
    srvrt.close();
    global.server=true;
    global.access=false;
//    global.replica=false;
    global.cont=true;
    global.update=false;
    global.ip="";
    }catch(Exception e1){}
}
}
}//end of transferofarray

class comm extends Thread{
String comm="";
String ip[]={""};
comm(String comm,String []ip)
{this.comm=comm;
 this.ip=ip;
}
public String[] run2()
{
String ret[]={"true",""};
try{   
    loop:for (int i=0;i<ip.length && ip[i]!=null &&  !ip[i].equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress());i++)
    {    System.out.println("communicating to the node " + ip[i] + " cond " + ip[i].equalsIgnoreCase(InetAddress.getLocalHost().getHostAddress()));
 	Socket skts = new Socket(ip[i],5555);
	DataInputStream ins=new DataInputStream(skts.getInputStream());
	DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                if(skts.isConnected())
                {   System.out.println("communicating via the command " + comm);
                    outs.write(comm.getBytes(),0,comm.getBytes().length);
                     outs.flush();
                     byte [] buf=new byte[200];
                     int reads= ins.read(buf,0,buf.length);
                     String re=new String(buf,0,reads);
                     System.out.println(" comm received " + re);
                     if (re.equalsIgnoreCase("y"))
                     continue loop;
                     else
                     {
                                 if(new String(buf,0,reads).equalsIgnoreCase("blocked"))
                                 {System.out.println("blocked from one of the ips");
                                 }
                                 System.out.println(" String received from client in response to " + comm + " received " + re);
                                 break loop;
                                 
                     }
                }

    }

}
    catch(Exception e){
    e.printStackTrace();
    ret[0]="false";
    ret[1]=e.getMessage();
    
    }

return ret;}
}

//comm2 allows to check itself
class comm2 extends Thread{
String comm="";
String ip[]={""};
comm2(String comm,String []ip)
{this.comm=comm;
 this.ip=ip;
}
public String[] run2()
{
String ret[]={"true",""};
try{    
    loop:for (int i=0;i<ip.length && ip[i]!=null;i++)
    {   System.out.println("communicating to the node " + ip[i]);
 	Socket skts = new Socket(ip[i],5555);
	DataInputStream ins=new DataInputStream(skts.getInputStream());
	DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                if(skts.isConnected())
                {   System.out.println("communicating via the command " + comm);
                    outs.write(comm.getBytes(),0,comm.getBytes().length);
                     outs.flush();
                     byte [] buf=new byte[100];
                     int reads= ins.read(buf,0,buf.length);
                     System.out.println(" comm received " + new String(buf,0,reads));
                     if (new String(buf,0,reads).equalsIgnoreCase("y"))
                     continue loop;
                     else
                     {
                                 if(new String(buf,0,reads).equalsIgnoreCase("blocked"))
                                 {System.out.println("blocked from one of the ips");
                                 }
                                 System.out.println(" String received from client in response to " + comm + " received " + new String(buf,0,reads));
                                 break loop;

                     }
                }

    }

}
    catch(Exception e){
    e.printStackTrace();
    ret[0]="false";
    ret[1]=e.getMessage();

    }

return ret;}
}

/*
class comm2 extends Thread{
String comm="";
String ip[]={""};
comm2(String comm,String []ip)
{this.comm=comm;
 this.ip=ip;
}
public String[] run2()
{
String ret[]={"false",""};
try{    //System.out.println("communicating to the node " + ip[0]);
    loop:for (int i=0;i<ip.length && ip[i]!=null;i++)
    {
 	Socket skts = new Socket(ip[i],5561);
	DataInputStream ins=new DataInputStream(skts.getInputStream());
	DataOutputStream outs=new DataOutputStream(skts.getOutputStream());
                if(skts.isConnected())
                {   System.out.println("communicating via the command " + comm);
                    outs.write(comm.getBytes(),0,comm.getBytes().length);
                     outs.flush();
                     byte [] buf=new byte[100];
                     int reads= ins.read(buf,0,buf.length);
                     if (new String(buf,0,reads).equalsIgnoreCase("y"))
                     continue loop;
                     else
                     {
                                 if(new String(buf,0,reads).equalsIgnoreCase("blocked"))
                                 {System.out.println("blocked from one of the ips");
                                 }
                                 System.out.println(" String received from client in response to " + comm + " received " + new String(buf,0,reads));
                                 break loop;

                     }
                }

    }

}
    catch(Exception e){
    e.printStackTrace();
    ret[0]="true";
    ret[1]=e.getMessage();

    }

return ret;}
}
*/





public class Communication
{
public static void main(String arg[])
{ 
    try{
Communicate ss=new Communicate();
new rectransferofarray().start();
new recfiletransfer().start();

ss.start();
String []sss={"192.168.1.3","192.168.1.2","127.0.0.1","192.168.1.4"};
//new newhostup(sss).run2();
//new initiatefiletransfer("192.168.1.3").run2(new File("system//iplist.txt"));
 //new initiatearraytransfer("192.168.1.2",sss).run2();
    }catch(Exception e){e.printStackTrace();}
}
}

/*
public class Cmain
{
public static void main(String arg[])
{
    try{
//Communication ss=new Communication();
//ss.start();
    String []ss={"192.168.1.3","192.168.1.2","192.168.1.3","192.168.1.4"};
 // new initiatearraytransfer("127.0.0.1",ss).start();
File ff=new File("C:\\Users\\Harsh\\Documents\\NetBeansProjects\\HDDS\\src\\hdds\\access\\test.accdb");
new initiatefiletransfer("127.0.0.1",ff).start();
new newhostup(ss).start();

    }catch(Exception e){e.printStackTrace();}
}
}


*/



