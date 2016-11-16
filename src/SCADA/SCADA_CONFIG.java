package SCADA;

/**
 * Created by madsn on 11-11-2016.
 */
public interface SCADA_CONFIG {

    public static final int REGISTRY_PORT = 8500;
    public static final String REMOTE_OBJECT_NAME = "GREENHOUSE_LIST";
    public static final String[] IP_ADRESSES = {
        "192.168.0.10",
        "192.168.0.20",
        "192.168.0.30",
        "192.168.0.40"
    };
}
