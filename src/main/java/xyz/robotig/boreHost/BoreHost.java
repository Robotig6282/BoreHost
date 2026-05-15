package xyz.robotig.boreHost;

import org.bukkit.plugin.java.JavaPlugin;
import xyz.robotig.bore4j.BoreClient;

import java.io.IOException;
import java.util.logging.Level;

public final class BoreHost extends JavaPlugin {
    private BoreClient client;
    private Thread listenerThread;

    @Override
    public void onEnable() {
        saveDefaultConfig();

        String serverHost = getConfig().getString("server-host", "").trim();
        int remotePort = getConfig().getInt("remote-port", 0);
        String secret = getConfig().getString("secret", "");

        if (serverHost.isEmpty()) {
            getLogger().severe("Config key 'server-host' must not be empty.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (remotePort < 0 || remotePort > 65535) {
            getLogger().severe("Config key 'remote-port' must be in range 0..65535.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        int localPort = getServer().getPort();
        try {
            this.client = BoreClient.builder()
                    .localHost("127.0.0.1")
                    .localPort(localPort)
                    .serverHost(serverHost)
                    .remotePort(remotePort)
                    .secret(secret)
                    .connect();
        } catch (IOException | IllegalArgumentException exception) {
            getLogger().log(Level.SEVERE, "Failed to connect to Bore server.", exception);
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        getLogger().info("Public remote endpoint: " + serverHost + ":" + this.client.remotePort());
        this.listenerThread = Thread.ofVirtual()
                .name("BoreHost-listener")
                .start(() -> runListenerLoop(serverHost));
    }

    @Override
    public void onDisable() {
        BoreClient currentClient = this.client;
        this.client = null;
        if (currentClient != null) {
            currentClient.close();
        }

        Thread currentListener = this.listenerThread;
        this.listenerThread = null;
        if (currentListener != null && currentListener.isAlive()) {
            try {
                currentListener.join(1000L);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void runListenerLoop(String serverHost) {
        BoreClient currentClient = this.client;
        if (currentClient == null) {
            return;
        }

        try {
            currentClient.listen();
        } catch (IOException exception) {
            if (isEnabled()) {
                getLogger().log(
                        Level.SEVERE,
                        "Bore listener stopped unexpectedly for host " + serverHost + ".",
                        exception
                );
            }
        }
    }
}
