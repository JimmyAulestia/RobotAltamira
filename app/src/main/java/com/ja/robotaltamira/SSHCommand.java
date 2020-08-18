package com.ja.robotaltamira;

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.Properties;

public class SSHCommand {

        public static String executeRemoteCommand( String username, String password, String hostname, int port) throws Exception {

            JSch jsch = new JSch();
            Session session = jsch.getSession(username, hostname, port);
            session.setPassword(password);
            String comando1 = "source $HOME/.bash_profile && /usr/local/RobotAndroid/estado_general.py 962667025 PROD";
            //String comando1 = Comando;
            // Avoid asking for key confirmation
            Properties prop = new Properties();
            prop.put("StrictHostKeyChecking", "no");
            session.setConfig(prop);
            session.connect();

            // SSH Channel
            ChannelExec channelssh = (ChannelExec) session.openChannel("exec");
            final ByteArrayOutputStream baos = new ByteArrayOutputStream();
            channelssh.setOutputStream(baos);

            // Execute command
            channelssh.setCommand(comando1);
            channelssh.connect();

            InputStream commandOutput = channelssh.getInputStream();
            StringBuilder outputBuffer = new StringBuilder();

            //InputStream in=channelssh.getInputStream();

            byte[] tmp=new byte[1024];
            while(true){
                while(commandOutput.available()>0){
                    int i=commandOutput.read(tmp, 0, 1024);
                    if(i<0)break;
                    outputBuffer.append(new String(tmp, 0, i));
                    //System.out.print(new String(tmp, 0, i));
                }
                if(channelssh.isClosed()){
                    if(commandOutput.available()>0) continue;
                    System.out.println("exit-status: "+channelssh.getExitStatus());
                    break;
                }
                try{Thread.sleep(1000);}catch(Exception ee){}
            }
            System.out.println("**** salida  ***** "+outputBuffer.toString());
            channelssh.disconnect();
            return outputBuffer.toString();
        }

}
