package dev.amble.stargate;

import net.fabricmc.loader.api.Version;
import net.fabricmc.loader.api.VersionParsingException;

public class VerTest {

    public static void main(String[] args) throws VersionParsingException {
        Version artifactV = Version.parse("1.2.3-dev.+mc.1.20.1");
        Version publicV = Version.parse("1.2.3-dev.456+mc.1.20.1");

        Version artifactBranchV = Version.parse("1.2.3-feat-test-dev+mc.1.20.1");
        Version publicBranchV = Version.parse("1.2.3-feat-test-dev.456+mc.1.20.1");

        System.out.println(publicV.compareTo(artifactV));
        System.out.println(publicBranchV.compareTo(artifactBranchV));
    }
}
