package dev.amble.stargate.api.state;

import dev.amble.stargate.api.behavior.IrisBehavior;
import net.minecraft.registry.RegistryKey;

public interface IrisTier {

    abstract class Impl implements IrisTier {

        private RegistryKey<IrisTier> key;

        @Override
        public void setRegistryKey(RegistryKey<IrisTier> key) {
            this.key = key;
        }

        @Override
        public RegistryKey<IrisTier> getRegistryKey() {
            return key;
        }
    }

    int maxDurability();

    void setRegistryKey(RegistryKey<IrisTier> key);
    RegistryKey<IrisTier> getRegistryKey();

    default void onBroken(IrisBehavior.IrisDamageCtx ctx) { }
}
