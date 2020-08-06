package vdehorta.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Skispasse.
 * <p>
 * Properties are configured in the {@code application.properties} file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String applicationName;

    private Logging logging = new Logging();

    private Mongo mongo = new Mongo();

    private Security security = new Security();

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Logging getLogging() {
        return logging;
    }

    public void setLogging(Logging logging) {
        this.logging = logging;
    }

    public Mongo getMongo() {
        return mongo;
    }

    public void setMongo(Mongo mongo) {
        this.mongo = mongo;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public static class Logging {

        private boolean useJsonFormat = false;

        private final Logstash logstash = new Logstash();

        public boolean isUseJsonFormat() {
            return useJsonFormat;
        }

        public void setUseJsonFormat(boolean useJsonFormat) {
            this.useJsonFormat = useJsonFormat;
        }


        public Logstash getLogstash() {
            return logstash;
        }

        public static class Logstash {

            private boolean enabled;
            private String host;
            private int port;
            private int queueSize;

            public boolean isEnabled() {
                return enabled;
            }

            public void setEnabled(boolean enabled) {
                this.enabled = enabled;
            }

            public String getHost() {
                return host;
            }

            public void setHost(String host) {
                this.host = host;
            }

            public int getPort() {
                return port;
            }

            public void setPort(int port) {
                this.port = port;
            }

            public int getQueueSize() {
                return queueSize;
            }

            public void setQueueSize(int queueSize) {
                this.queueSize = queueSize;
            }
        }
    }

    public static class Mongo {

        private GridFs gridFs = new GridFs();

        public GridFs getGridFs() {
            return gridFs;
        }

        public static class GridFs {

            private String newsFactVideoBucket;

            public String getNewsFactVideoBucket() {
                return newsFactVideoBucket;
            }

            public void setNewsFactVideoBucket(String newsFactVideoBucket) {
                this.newsFactVideoBucket = newsFactVideoBucket;
            }
        }
    }

    public static class Security {

        private Csrf csrf = new Csrf();

        private String defaultPasswordBase;

        public Csrf getCsrf() {
            return csrf;
        }

        public void setCsrf(Csrf csrf) {
            this.csrf = csrf;
        }

        public String getDefaultPasswordBase() {
            return defaultPasswordBase;
        }

        public void setDefaultPasswordBase(String defaultPasswordBase) {
            this.defaultPasswordBase = defaultPasswordBase;
        }

        public static class Csrf {

            private boolean disabled = false;

            public boolean isDisabled() {
                return disabled;
            }

            public void setDisabled(boolean disabled) {
                this.disabled = disabled;
            }
        }
    }
}

