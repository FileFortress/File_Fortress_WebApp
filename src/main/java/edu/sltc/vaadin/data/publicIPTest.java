package edu.sltc.vaadin.data;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Enumeration;

/**
 * @author Nuyun-Kalamullage
 * @IDE IntelliJ IDEA
 * @date 10/12/2023
 * @package edu.sltc.vaadin.data
 * @project_Name File_Fortress_WebApp
 */
public class publicIPTest {
    public static void main(String[] args) {
        try {
            // Get all network interfaces
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

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
                        System.out.println("IPv4 Address on " + iface.getDisplayName() + ": " + address.getHostAddress() + " : is Site Local " + address.isSiteLocalAddress());
                        // Retrieve SSID for Wi-Fi
                        String ssid = getWifiSSID();
                        System.out.println("Wi-Fi SSID: " + ssid);
                    }
                    }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private static String getWifiSSID() throws IOException {
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

        return "Not available";
    }
}
