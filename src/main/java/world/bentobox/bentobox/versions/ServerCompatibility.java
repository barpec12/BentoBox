package world.bentobox.bentobox.versions;

import world.bentobox.bentobox.BentoBox;

/**
 * Checks and ensures the current server software is compatible with BentoBox.
 * @author Poslovitch
 */
public class ServerCompatibility {

    // ---- SINGLETON ----

    private static ServerCompatibility instance = new ServerCompatibility();

    public static ServerCompatibility getInstance() {
        return instance;
    }

    private ServerCompatibility() { }

    // ---- CONTENT ----

    private Compatibility result;

    public enum Compatibility {
        /**
         * The server software is compatible with the current version of BentoBox.
         * There shouldn't be any issues.
         */
        COMPATIBLE(true),

        /**
         * The server software might not be compatible but is supported.
         * Issues might occur.
         */
        SUPPORTED(true),

        /**
         * The server software is not supported, even though BentoBox may work fine.
         * Issues are likely and won't receive any support.
         */
        NOT_SUPPORTED(true),

        /**
         * The server software is explicitly not supported and incompatible.
         * BentoBox won't run on it: that's pointless to try to run it.
         */
        INCOMPATIBLE(false);

        private boolean canLaunch;

        Compatibility(boolean canLaunch) {
            this.canLaunch = canLaunch;
        }

        public boolean isCanLaunch() {
            return canLaunch;
        }
    }

    /**
     * Provides a list of server software.
     * Any software that is not listed here is implicitly considered as "INCOMPATIBLE".
     */
    public enum ServerSoftware {
        CRAFTBUKKIT(Compatibility.INCOMPATIBLE),
        BUKKIT(Compatibility.INCOMPATIBLE),
        SPIGOT(Compatibility.COMPATIBLE),
        PAPER(Compatibility.NOT_SUPPORTED),
        TACOSPIGOT(Compatibility.NOT_SUPPORTED);

        private Compatibility compatibility;

        ServerSoftware(Compatibility compatibility) {
            this.compatibility = compatibility;
        }

        public Compatibility getCompatibility() {
            return compatibility;
        }
    }

    /**
     * Provides a list of server versions.
     * Any version that is not listed here is implicitly considered as "INCOMPATIBLE".
     */
    public enum ServerVersion {
        V1_13(Compatibility.NOT_SUPPORTED),
        V1_13_1(Compatibility.NOT_SUPPORTED),
        V1_13_2(Compatibility.COMPATIBLE);

        private Compatibility compatibility;

        ServerVersion(Compatibility compatibility) {
            this.compatibility = compatibility;
        }

        public Compatibility getCompatibility() {
            return compatibility;
        }

        @Override
        public String toString() {
            return super.toString().substring(1).replace("_", ".");
        }
    }

    /**
     * Checks the compatibility with the current server software and returns the {@link Compatibility}.
     * Note this is a one-time calculation: further calls won't change the result.
     * @param plugin BentoBox instance to provide.
     * @return the {@link Compatibility}.
     */
    public Compatibility checkCompatibility(BentoBox plugin) {
        if (result == null) {
            // Check the server version first
            ServerVersion version = getServerVersion(plugin.getServer().getBukkitVersion());

            if (version == null || version.getCompatibility().equals(Compatibility.INCOMPATIBLE)) {
                // 'Version = null' means that it's not listed. And therefore, it's implicitly incompatible.
                result = Compatibility.INCOMPATIBLE;
                return result;
            }

            // Now, check the server software
            ServerSoftware software = getServerSoftware(plugin.getServer().getVersion());

            if (software == null || software.getCompatibility().equals(Compatibility.INCOMPATIBLE)) {
                // 'software = null' means that it's not listed. And therefore, it's implicitly incompatible.
                result = Compatibility.INCOMPATIBLE;
                return result;
            }

            if (software.getCompatibility().equals(Compatibility.NOT_SUPPORTED) || version.getCompatibility().equals(Compatibility.NOT_SUPPORTED)) {
                result = Compatibility.NOT_SUPPORTED;
                return result;
            }

            if (software.getCompatibility().equals(Compatibility.SUPPORTED) || version.getCompatibility().equals(Compatibility.SUPPORTED)) {
                result = Compatibility.SUPPORTED;
                return result;
            }

            // Nothing's wrong, the server is compatible.
            result = Compatibility.COMPATIBLE;
            return result;
        }

        return result;
    }

    private ServerSoftware getServerSoftware(String server) {
        try {
            String serverSoftware = server.substring(4).split("-")[0];
            return ServerSoftware.valueOf(serverSoftware.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    private ServerVersion getServerVersion(String bukkitVersion) {
        try {
            String serverVersion = bukkitVersion.split("-")[0].replace(".", "_");
            return ServerVersion.valueOf("V" + serverVersion.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }
}
