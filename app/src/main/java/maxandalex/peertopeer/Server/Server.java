package maxandalex.peertopeer.Server;

import android.app.Activity;
import android.location.Location;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
import android.util.Log;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileInputStream;

import fi.iki.elonen.NanoHTTPD;
import maxandalex.peertopeer.MainActivity;
import maxandalex.peertopeer.QrCode.GeneratedQRCode;

import static android.content.Context.WIFI_SERVICE;

public class Server extends NanoHTTPD {

    private Activity mainActivity;
    private Location location;
    private MainActivity lien ;
    private File[] filesList;
    private String jsonList;

    public Server(String hostname, int port) {
        super(hostname, port);
        init(mainActivity);

    }

    private void init(Activity mainActivity){
        this.mainActivity = mainActivity;
    }

    @Override
    public Response serve(IHTTPSession session){
        if(session.getUri().contains("/getPosition/")){
            lien = (MainActivity) mainActivity.getApplicationContext();
            location = lien.getPosition();
            Log.i("TEST_POSITION","Position sent is: "+location.getLongitude() + "," + location.getLatitude());
            return new Response(Response.Status.OK, MIME_PLAINTEXT, location.getLongitude() + "," + location.getLatitude());
        }else if(session.getUri().contains("/getFileList/")){
            jsonList = new FilesList().listToJson();
            return new Response(Response.Status.OK, MIME_PLAINTEXT, jsonList);
        }else if(session.getUri().contains("/getFile/")){
            FileInputStream fileInputStream;
            String[] parametres = session.getUri().split("/");
            try{
                fileInputStream = new FileInputStream(FilesList.getFile(parametres.length - 1));
                return new Response(Response.Status.OK,MIME_PLAINTEXT,fileInputStream);
            }catch (Exception e){
                Log.i("FILE", "fichier non trouver");
            }
        }
        return new Response(Response.Status.NOT_FOUND, MIME_PLAINTEXT, "NOT FOUND");
    }

    public void startServer(){
        try {
            String ip = GeneratedQRCode.getLocalIpAddress();
            Server serveur = new Server(ip, 8080);
            serveur.stop();
            serveur.start();
            Log.i("SERVER","Server running at: " + ip + ":" );
            Log.i("SERVER","Server is up? " + serveur.isAlive()  );

        }
        catch(Exception e){
            Log.i("SERVER","The server can't be started : " + e);
        }
    }

}
