package dev.amblelabs.stargate.api.mod;

import dev.amblelabs.stargate.api.StargateAPI;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class StargateConfig {

    public interface CommonConfigAccess {

    }

    public interface ClientConfigAccess {

    }

    public interface ServerConfigAccess {

    }

    public static boolean anyMatch(List<? extends String> keys, ResourceLocation key) {
        for (String s : keys) {
            var rl = ResourceLocation.tryParse(s);

            if (rl != null && rl.equals(key)) {
                return true;
            }
        }
        return false;
    }

    @SuppressWarnings("unused")
    public static boolean noneMatch(List<? extends String> keys, ResourceLocation key) {
        return !anyMatch(keys, key);
    }

    @SuppressWarnings("unused")
    public static boolean anyMatchResLoc(List<? extends ResourceLocation> keys, ResourceLocation key) {
        return keys.stream().anyMatch(key::equals);
    }

    private static CommonConfigAccess common = trustMeBro();
    private static ClientConfigAccess client = trustMeBro();
    private static ServerConfigAccess server = trustMeBro();

    public static CommonConfigAccess common() {
        return Objects.requireNonNull(common, "accessed config too early");
    }

    public static ClientConfigAccess client() {
        return Objects.requireNonNull(client, "accessed config too early");
    }

    @SuppressWarnings("unused") // will be used later
    public static ServerConfigAccess server() {
        return Objects.requireNonNull(server, "accessed config too early");
    }

    @SuppressWarnings("ConstantValue")
    public static void setCommon(CommonConfigAccess access) {
        if (common != null) {
            StargateAPI.LOGGER.warn("CommonConfigAccess was replaced! Old {} New {}",
                common.getClass().getName(), access.getClass().getName());
        }

        common = access;
    }

    @SuppressWarnings("ConstantValue")
    public static void setClient(ClientConfigAccess access) {
        if (client != null) {
            StargateAPI.LOGGER.warn("ClientConfigAccess was replaced! Old {} New {}",
                client.getClass().getName(), access.getClass().getName());
        }

        client = access;
    }

    @SuppressWarnings("ConstantValue")
    public static void setServer(ServerConfigAccess access) {
        if (server != null) {
            StargateAPI.LOGGER.warn("ServerConfigAccess was replaced! Old {} New {}",
                server.getClass().getName(), access.getClass().getName());
        }

        server = access;
    }

    @NotNull
    @SuppressWarnings({"DataFlowIssue", "NullableProblems", "SameReturnValue"})
    private static <T> T trustMeBro() {
        return null;
    }
}