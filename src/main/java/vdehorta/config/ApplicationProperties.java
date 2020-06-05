package vdehorta.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Skispasse.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {

    private String applicationName;

    private Mongo mongo = new Mongo();

    private Logging logging = new Logging();

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public Mongo getMongo() {
        return mongo;
    }

    public Logging getLogging() {
        return logging;
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
}

