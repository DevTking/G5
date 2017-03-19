package g5.org.g5;

import android.util.ArrayMap;

import java.util.UUID;

public final class aC {
    public static final UUID AuthenticationControlPointCharUuid = UUID.fromString("f8083535-849e-531c-c594-30f1f86a4ea5");
    public static final UUID CgmControlPointCharUuid = UUID.fromString("f8083534-849e-531c-c594-30f1f86a4ea5");
    public static final UUID DexcomAdvertisedUuid = UUID.fromString("0000febc-0000-1000-8000-00805f9b34fb");
    public static final UUID DexcomCgmServiceUuid = UUID.fromString("f8083532-849e-531c-c594-30f1f86a4ea5");
    private static final UUID DexcomTestServiceUuid = UUID.fromString("f8084532-849e-531c-c594-30f1f86a4ea5");
    public static final UUID ExchangeCharUuid = UUID.fromString("f8083536-849e-531c-c594-30f1f86a4ea5");
    public static final UUID SynchronizationCharUuid = UUID.fromString("f8083533-849e-531c-c594-30f1f86a4ea5");
    private static final ArrayMap<UUID, String> m_names;

    static {
        ArrayMap arrayMap = new ArrayMap();
        m_names = arrayMap;
        arrayMap.put(DexcomAdvertisedUuid, "Dexcom SIG Service");
        m_names.put(DexcomCgmServiceUuid, "Dexcom CGM Service");
        m_names.put(DexcomTestServiceUuid, "Dexcom Test Service");
        m_names.put(SynchronizationCharUuid, "Sync");
        m_names.put(CgmControlPointCharUuid, "CGM_CP");
        m_names.put(AuthenticationControlPointCharUuid, "Auth_CP");
        m_names.put(ExchangeCharUuid, "Exchange");
    }

    private aC() {
    }

    public static String getName(UUID uuid) {
        String str = (String) m_names.get(uuid);
        return str != null ? str : uuid.toString();
    }
}
