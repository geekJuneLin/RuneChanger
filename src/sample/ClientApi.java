package sample;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.apache.http.HttpHeaders;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;

import javax.net.ssl.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Base64;
import java.util.concurrent.atomic.AtomicBoolean;

public class ClientApi {
    private int portNum = 0;
    private String password = "";
    private final AtomicBoolean connected;
    private RunePage[] runePages;
    private String token;

    public ClientApi(){
        connected = new AtomicBoolean(false);
        try{
            findLockfile();
        }catch(Exception e){
            System.out.println("Having error finding the lockfile " + e);
        }
    }

    private void findLockfile() throws IOException{
        FileReader path = new FileReader(new File("C:\\Riot Games\\League of Legends\\lockfile"));
        BufferedReader reader = new BufferedReader(path);
        String i = " ";
        String content = " ";
        while (true){
            i = reader.readLine();
            if(i == null){
                break;
            }else{
                content = i;
            }
        }
        String[] strings = content.split(":");
        portNum = Integer.parseInt(strings[2]);
        password = strings[3];
        token = new String(Base64.getEncoder().encode(("riot:" + password).getBytes()));
        System.out.println("Port number: " + portNum + " Password: " + password);
        connected.set(true);
    }

    // need to be fixed later
    public void getCurrentChamp(){
        try{
            HttpGet httpGet = new HttpGet();
            httpGet.setURI(new URI("https://127.0.0.1:" + portNum + "/lol-champ-select-legacy/v1/current-champion"));
            httpGet.setHeader("Accept", "*/*");
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + token);
            System.out.println("executing getting current champ request: " + httpGet.getRequestLine());
        }catch(Exception e){
            System.out.println("Getting the current champ with error: " + e);
        }
    }

    public void autoAccept(){
        int code = 0;
        int times = 0;
        try{
            while(true){
                System.out.println(times);
                times++;
                code = checkQueue();
                if(code == -1){
                    System.out.println("Checking queue status has got some errors");
                    return;
                }else if(code == 1){
                    System.out.println("Successfully accept the match!");
                    accept();
                    return;
                }else{
                    Thread.sleep(1000);
                    continue;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void accept(){
        try{
            HttpPost httpPost = new HttpPost();
            httpPost.setURI(new URI("https://127.0.0.1:" + portNum + "/lol-matchmaking/v1/ready-check/accept"));
            httpPost.setHeader("Accept", "*/*");
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + token);
            CloseableHttpClient client = createHttpClient();
            CloseableHttpResponse response = client.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == 204){
                System.out.println("Successfully accept the match");
            }else{
                System.out.println("Unsuccessfully accept the match");
            }
        }catch(Exception e){
            System.out.println("Executing accept match with errors: " + e);
        }
    }

    private int checkQueue(){
        try{
            HttpGet httpGet = new HttpGet();
            httpGet.setURI(new URI("https://127.0.0.1:" + portNum + "/lol-matchmaking/v1/ready-check"));
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + token);
            CloseableHttpClient client = createHttpClient();
            CloseableHttpResponse response = client.execute(httpGet);
            System.out.println("Executing the checking queue request: " + httpGet.getRequestLine());
            if(response.getStatusLine().getStatusCode() == 200){
                String s = dumpStream(response.getEntity().getContent());
                if(new Gson().fromJson(s, QueueStatus.class).state.equals("InProgress")){
                    return 1;
                }else{
                    return 0;
                }
            }else{
                return 0;
            }
        }catch(Exception e){
            System.out.println("Executing getting queue status with error: " + e);
        }
        return -1;
    }

    public boolean isConnected(){
        return connected.get();
    }

    public void changeRunePage(String pageName, int primaryId, int[] selectedPerks, int subId){
        getFirstRunePage();
        if(runePages[0].isDeletable){
            deleteFirstRunePage();
        }
        postNewRunePage(pageName, primaryId, selectedPerks, subId);
    }

    private void postNewRunePage(String pageName, int primaryId, int[] selectedPerks, int subId){
        RunePage page = new RunePage(new String[]{"0"},
                true,
                0,
                true,
                true,
                true,
                true,
                "0",
                pageName,
                "0",
                primaryId,
                selectedPerks,
                subId
                );

        try{
            StringEntity entity = new StringEntity(new Gson().toJson(page, RunePage.class));
            HttpPost httpPost = new HttpPost();
            httpPost.setEntity(entity);
            httpPost.setURI(new URI("https://127.0.0.1:" + portNum + "/lol-perks/v1/pages/"));
            httpPost.setHeader("Content-Type", "application/json");
            httpPost.setHeader("Accept", "*/*");
            httpPost.setHeader(HttpHeaders.AUTHORIZATION, "Basic " + token);
            System.out.println("executing creating new rune request " + httpPost.getRequestLine());
            CloseableHttpClient client = createHttpClient();
            CloseableHttpResponse response = client.execute(httpPost);
            if(response.getStatusLine().getStatusCode() == 200){
                System.out.println("Successfully create a new rune page!");
            }else{
                System.out.println("Unsuccessfully create a new rune page!");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void deleteFirstRunePage(){
        System.out.println(runePages[0].id);
        try{
            HttpDelete httpDelete = new HttpDelete();
            httpDelete.setURI(new URI("https://127.0.0.1:" + portNum + "/lol-perks/v1/pages/" + runePages[0].id));
            httpDelete.addHeader("Accept", "*/*");
            httpDelete.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + token);
            CloseableHttpClient client = createHttpClient();
            CloseableHttpResponse response = client.execute(httpDelete);
            // may not need this part
            if(response.getStatusLine().getStatusCode() == 404){
                System.out.println("getting 404");
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void getFirstRunePage(){
        try{
            HttpGet httpGet = new HttpGet();
            httpGet.setURI(new URI("https://127.0.0.1:" + portNum + "/lol-perks/v1/pages"));
            httpGet.addHeader("Accept", "*/*");
            httpGet.addHeader(HttpHeaders.AUTHORIZATION, "Basic " + token);
            System.out.println("executing request " + httpGet.getRequestLine());
            CloseableHttpClient client = createHttpClient();
            CloseableHttpResponse response = client.execute(httpGet);
            if(response.getStatusLine().getStatusCode() == 200){
                String s = dumpStream(response.getEntity().getContent());
                Gson gson = new Gson();
                runePages = gson.fromJson(s, RunePage[].class);
            } else {
                System.out.println("getting response with error!");
                System.out.println(response);
            }
        }catch(Exception e){
            System.out.println("Sending request getting error: " + e);
        }
    }

    private static CloseableHttpClient createHttpClient() throws KeyManagementException, NoSuchAlgorithmException {
        //Localhost needs HTTPS, but doesn't provide valid SSL
        TrustManager[] trustAllCerts = new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    }

                }
        };
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCerts, new java.security.SecureRandom());
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

        HostnameVerifier allHostsValid = (hostname, session) -> true;
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
        return HttpClients.custom()
                .setSSLContext(sc)
                .setSSLHostnameVerifier(allHostsValid)
                .build();
    }

    private String dumpStream(InputStream is) throws IOException{
        java.util.Scanner s = new java.util.Scanner(is).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }
}
