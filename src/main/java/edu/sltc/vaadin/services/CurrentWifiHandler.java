package edu.sltc.vaadin.services;

import edu.sltc.vaadin.data.WifiListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 11/12/2023
 * @package edu.sltc.vaadin.services
 * @project_Name File_Fortress_WebApp
 */
public class CurrentWifiHandler {
    private static  CurrentWifiHandler instance;
    private static String currentConnectedWifiSSID = "";
    private static final CopyOnWriteArrayList<WifiListener> listeners = new CopyOnWriteArrayList<>();

    private CurrentWifiHandler() {
        startWifiListener();
    }
    public static CurrentWifiHandler getInstance(){
        if (instance == null) {
            synchronized (CurrentWifiHandler.class) {
                if (instance == null) {
                    instance = new CurrentWifiHandler();
                }
            }
        }
        return instance;
    }

    public void setListeners(WifiListener listener){
        listeners.add(listener);
    }
    public void removeListeners(WifiListener listener){
        listeners.remove(listener);
    }
    private static void notifyListeners(String connectedWifiSsid) {
            if (!currentConnectedWifiSSID.equals(connectedWifiSsid)) {
                for (WifiListener listener : listeners) {
                   listener.onWifiChanged(connectedWifiSsid);
                }
                currentConnectedWifiSSID = connectedWifiSsid;
            }
    }
    private static void startWifiListener() {
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                notifyListeners(getWifiSSID());
            }
        }, 0, 5000); // 5000 milliseconds = 5 seconds
    }
    public static String getWifiSSID() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("netsh", "wlan", "show", "interfaces");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("SSID")) {
                    return line.split(":")[1].trim();
                }
            }
        } catch (IOException e) {
            return "Not available";
        }
        return "Not available";
    }
    public static Map<String, String> getWlanIpAddress(){
        Map<String, String> ipList = new HashMap<>();
        try {
            // Get all network interfaces
            Enumeration<NetworkInterface> interfaces =  NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // Exclude loopback addresses and non-operational interfaces
                if (iface.isLoopback() || !iface.isUp()) {
                    continue;
                }
                // Get all IP addresses of the current interface
                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while (addresses.hasMoreElements()) {
                    InetAddress address = addresses.nextElement();
                    // Check if it's an IPv4 address
                    if (address.getHostAddress().contains(".") &&  address.isSiteLocalAddress()) {
//                        System.out.println("IPv4 Address on " + iface.getDisplayName() + ": " + address.getHostAddress());
                        ipList.put(iface.getDisplayName(), address.getHostAddress());
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ipList;
    }
    public static String getWifiDescription() {
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("netsh", "wlan", "show", "interfaces");
            processBuilder.redirectErrorStream(true);
            Process process = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("Description")) {
                    return line.split(":")[1].trim();
                }
            }
        } catch (IOException e) {
            return "Not available";
        }
        return "Not available";
    }

}
