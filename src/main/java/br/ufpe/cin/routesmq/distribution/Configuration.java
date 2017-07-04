package br.ufpe.cin.routesmq.distribution;

import java.io.*;
import java.util.*;

/**
 * Created by tjamir on 7/3/17.
 */
public class Configuration {

    private Integer port;

    private UUID peerid;

    private List<Seed> seeds;

    private Long announcementInterval;

    private Long pingInterval;


    public void save() throws FileNotFoundException {
        Properties properties=new Properties();
        if(port!=null) {
            properties.setProperty("port", Integer.toString(port));
        }
        if(peerid!=null) {
            properties.setProperty("peerid", peerid.toString());
        }
        if(seeds!=null && !seeds.isEmpty()){
            String seeds="";
            Iterator<Seed> it= this.seeds.iterator();
            while(it.hasNext()){
                seeds=seeds+it.next().toString();
                if(it.hasNext()){
                    seeds+=",";
                }
            }
            properties.setProperty("seeds", seeds);
        }
        if(announcementInterval!=null){
            properties.setProperty("announcementInterval", announcementInterval.toString());
        }
        if(pingInterval!=null){
            properties.setProperty("pingInterval", pingInterval.toString());
        }

        try(FileOutputStream fos=new FileOutputStream("routesmq.properties")){
            properties.store(fos, "");
        } catch (IOException e) {
            e.printStackTrace();
        }



    }


    public void load(){
        Properties properties=new Properties();
        try(FileInputStream fis=new FileInputStream("routesmq.properties")){
            properties.load(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(properties.containsKey("port")){
            port = Integer.parseInt(properties.getProperty("port"));

        }
        if(properties.containsKey("peerid")){
            peerid = UUID.fromString(properties.getProperty("peerid"));
        }
        if(properties.containsKey("seeds")){
            seeds=new ArrayList<>();
            String[] seeds=properties.getProperty("sees").split(",");
            for(String s:seeds){
                this.seeds.add(Seed.fromString(s));
            }
        }
        if(properties.containsKey("announcementInterval")){
            announcementInterval=Long.parseLong("announcementInterval");
        }
        if(properties.containsKey("pingInterval")){
            pingInterval=Long.parseLong("pingInterval");
        }

    }


    public Configuration setPort(int port) {
        this.port = port;
        return this;
    }

    public Configuration setPeerid(UUID peerid) {
        this.peerid = peerid;
        return this;
    }

    public Configuration setSeeds(List<Seed> seeds) {
        this.seeds = seeds;
        return this;
    }

    public Configuration setAnnouncementInterval(long announcementInterval) {
        this.announcementInterval = announcementInterval;
        return this;
    }

    public Configuration setPingInterval(long pingInterval) {
        this.pingInterval = pingInterval;
        return this;
    }

    public Configuration addSeed(Seed seed){
        if(this.seeds ==null ){
            seeds=new ArrayList<>();
        }
        seeds.add(seed);
        return this;
    }


    public Integer getPort() {
        return port;
    }

    public UUID getPeerid() {
        return peerid;
    }

    public List<Seed> getSeeds() {
        return seeds;
    }

    public Long getAnnouncementInterval() {
        return announcementInterval;
    }

    public Long getPingInterval() {
        return pingInterval;
    }
}
