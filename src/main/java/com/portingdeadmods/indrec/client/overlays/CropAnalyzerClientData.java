package com.portingdeadmods.indrec.client.overlays;

import com.portingdeadmods.indrec.utils.CropDropsKey;
import com.portingdeadmods.indrec.utils.CropDrop;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class CropAnalyzerClientData {
    private static final Map<CropDropsKey, List<CropDrop>> DROPS = new HashMap<>();
    private static final Map<CropDropsKey, Long> PENDING_REQUESTS = new HashMap<>();
    private static int serverAnalyzeTicks;

    private CropAnalyzerClientData() {
    }

    public static void setDrops(CropDropsKey key, List<CropDrop> drops) {
        DROPS.put(key, List.copyOf(drops));
        PENDING_REQUESTS.remove(key);
    }

    public static @Nullable List<CropDrop> getDrops(CropDropsKey key) {
        return DROPS.get(key);
    }

    public static boolean shouldRequest(CropDropsKey key, long gameTime, int retryInterval) {
        if (DROPS.containsKey(key)) return false;
        Long requestTime = PENDING_REQUESTS.get(key);
        return requestTime == null || gameTime - requestTime >= retryInterval || gameTime < requestTime;
    }

    public static void markRequested(CropDropsKey key, long gameTime) {
        PENDING_REQUESTS.put(key, gameTime);
    }

    public static int getServerAnalyzeTicks() {
        return serverAnalyzeTicks;
    }

    public static void setServerAnalyzeTicks(int ticks) {
        serverAnalyzeTicks = ticks;
    }

    public static void clear() {
        serverAnalyzeTicks = 0;
        DROPS.clear();
        PENDING_REQUESTS.clear();
    }
}
